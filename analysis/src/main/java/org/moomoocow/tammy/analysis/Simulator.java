package org.moomoocow.tammy.analysis;

import static org.moomoocow.tammy.model.StockHistoricalData.Price.CLOSE;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.HIGH;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.LOW;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.MID;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.OPEN;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.util.Helper;

public class Simulator {
    
    
  public enum Side {
    B, S, H
  };
  
  private String symbol;
  
  private PersistenceManager pm;
  
  private int days;
  private int maLong;
  private int maShort;
  
  private SimulatorListener listener;
    

  public static final void main(String args[]) {
    
    SimulatorListener l = new SimulatorListener(){

      @Override
      public void buy(double price, long qty, Date d, int holdingDays) {
        System.out.println(" B@" + String.format("%(,.2f", price) + " qty->" + qty + " units, diffDays=" + holdingDays + ",date=" + d);
      }

      @Override
      public void sell(double price, long qty, Date d, int holdingDays) {
        System.out.println(" S@" + String.format("%(,.2f", price) + " qty-> " + qty + "dollars, diffDays=" + holdingDays + ",date=" + d);
      }
      
    };
    
    Simulator bta = new Simulator(args[0], 
        Integer.parseInt(args[1]),
        Integer.parseInt(args[2]),
            Integer.parseInt(args[3]), l);
    bta.testStocks();
  }
  
  public Simulator(String symbol, int days, int maLong, int maShort,SimulatorListener listeners){
    this.pm = Helper.SINGLETON.getPersistenceManager();
    this.symbol = symbol;
    this.days = days;
    this.maLong = maLong;
    this.maShort = maShort;
    
    this.listener = listeners;
  }

  @SuppressWarnings("unchecked")
  public void testStocks() {

    Query q = pm.newQuery(Stock.class,"this.code == '" + symbol + "'");
    List<Stock> s = (List<Stock> ) q.execute();
           
    simulate(s.get(0));
  }
    

  private void simulate(Stock s) {

    double cash = 100;
    long stock = 0;
    double commission = 0.004;

    int trans = 0;

    Side ops = Side.H;
    Double accumulatedMultipler = null;

    MA ma = new MA();

    //List<StockHistoricalData> data = s.getSortedDailyData();

    double mid = 0.0;
    double open = 0.0;
    double close = 0.0;
    double high = 0.0;
    double low = 0.0;
    
    Date lastTransDate = null;
    
    List<StockHistoricalData> sortedDailyData = s.getSortedDailyData();
    
    int sortedDailyDataSize = sortedDailyData.size();
    
    int x = (sortedDailyDataSize < days ? 0 : sortedDailyDataSize - days);

    boolean firstTrans = true;
    
    double firstMid = sortedDailyData.get(x).getMid();
    
    boolean isHolding = false;
    
    while(x < sortedDailyDataSize){
      
      StockHistoricalData h = sortedDailyData.get(x++);

    //for (int i = 0; i < data.size(); i++) {
      //StockHistoricalData h = sortedDailyData.get(x);
      //System.out.println(h);

      if (firstTrans){
        firstTrans=false;
        lastTransDate = h.getDate();
        continue;
      }

      StockHistoricalData p = sortedDailyData.get(x - 1);
      accumulatedMultipler = h.accumlateMultiplers(accumulatedMultipler);

      mid = h.getAccX(MID);
      open = h.getAccX(OPEN);
      close = h.getAccX(CLOSE);
      high = h.getAccX(HIGH);
      low = h.getAccX(LOW);
      
      ma.add(mid);
      
      

      switch (ops) {
      case B:
        stock = (long) Math.floor(cash * (1.0 - commission) / mid);
        cash = (cash - (stock * mid));
        int diffDays = Days.daysBetween(new DateTime(lastTransDate), new DateTime(h.getDate())).getDays();
        lastTransDate = h.getDate();
        this.listener.buy(mid, stock, lastTransDate,diffDays);
        trans++;
        isHolding = true;
        break;
      case S:
        cash = cash + (stock * mid * (1.0 - commission));       
        trans++;
        diffDays = Days.daysBetween(new DateTime(lastTransDate), new DateTime(h.getDate())).getDays();
        lastTransDate = h.getDate();
        this.listener.sell(mid, stock, lastTransDate,diffDays);
        isHolding = false;
        stock = 0;
        break;
      case H: // do nothing
        //System.out.println("Hold");
        break;
      }

      Double maShort = ma.getMA(this.maShort);
      Double maLong = ma.getMA(this.maLong);
      
      // Buy on next
      //if (close < open && cash > 0) {
      if (maShort != null  && maLong != null && maLong > maShort && !isHolding) {
        ops = Side.B;
        
      } // Sell on next
      else if (maShort != null  && maLong != null && maLong < maShort && isHolding) {
        ops = Side.S;
        
      } else {
        ops = Side.H;
      }
    }

    

    System.out.println("BS PL -> " + (int) (cash + (stock * mid) - 100)
        + ", trans=" + trans + ", Cash=" + (int) cash + ", stock ="
        + (int) stock + "(cash=" + (int) (stock * mid) + ")");
    
    System.out.println("BH PL -> " + (int) ((mid - firstMid) / firstMid * 100)
        + ", firstMid=" + firstMid + ",mid=" + mid + "");
  }
}
