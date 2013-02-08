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
import org.moomoocow.tammy.model.Exchange;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.util.Helper;

public class Simulator {
    
  public enum Side {
    B, S, H
  };
  
  private PersistenceManager pm;

  public static final void main(String args[]) {
    Simulator bta = new Simulator();
    bta.testStocks();
  }
  
  public Simulator(){
    this.pm = Helper.SINGLETON.getPersistenceManager();
  }

  @SuppressWarnings("unchecked")
  public void testStocks() {

    Query q = pm.newQuery(Exchange.class,"this.code == 'TEST'");
    List<Exchange> e = (List<Exchange> ) q.execute();
       
    for (Stock stock : e.get(0).getActiveStocks()) {
      simulate(stock);
    }
  }

  public void simulate(Stock s) {

    double cash = 100;
    double stock = 0;

    int trans = 0;

    Side ops = Side.H;
    Double accumulatedMultipler = null;

    MA ma = new MA();

    List<StockHistoricalData> data = s.getSortedDailyData();

    double mid = 0.0;
    double open = 0.0;
    double close = 0.0;
    double high = 0.0;
    double low = 0.0;
    
    Date lastTransDate = null;

    for (int i = 0; i < data.size(); i++) {

      StockHistoricalData h = data.get(i);
      //System.out.println(h);

      if (i == 0){
        lastTransDate = h.getDate();
        continue;
      }

      StockHistoricalData p = data.get(i - 1);
      accumulatedMultipler = h.accumlateMultiplers(accumulatedMultipler);

      mid = h.getAccX(MID);
      open = h.getAccX(OPEN);
      close = h.getAccX(CLOSE);
      high = h.getAccX(HIGH);
      low = h.getAccX(LOW);
      
      ma.add(mid);

      switch (ops) {
      case B:
        stock = cash / mid;
        cash = 0;
        int diffDays = Days.daysBetween(new DateTime(lastTransDate), new DateTime(h.getDate())).getDays();
        lastTransDate = h.getDate();
        System.out.println(" B@" + mid + "->" + stock + " units, diffDays=" + diffDays);        
        trans++;
        break;
      case S:
        cash = stock * mid;
        stock = 0;
        trans++;
        diffDays = Days.daysBetween(new DateTime(lastTransDate), new DateTime(h.getDate())).getDays();
        lastTransDate = h.getDate();
        System.out.println("S@" + mid + "->" + cash + " dollars, diffDays=" + diffDays);
        break;
      case H: // do nothing
        //System.out.println("Hold");
        break;
      }

      Double maShort = ma.getMA(7);
      Double maLong = ma.getMA(14);
      
      // Buy on next
      //if (close < open && cash > 0) {
      if (maShort != null  && maLong != null && maLong > maShort && close < open && cash > 0) {
        ops = Side.B;
        
      } // Sell on next
      else if (maShort != null  && maLong != null && maLong < maShort && close > open && stock > 0) {
        ops = Side.S;
        
      } else {
        ops = Side.H;
      }
    }

    double firstMid = data.get(0).getMid();

    System.out.println("BS PL -> " + (int) (cash + (stock * mid) - 100)
        + ", trans=" + trans + ", Cash=" + (int) cash + ", stock ="
        + (int) stock + "(cash=" + (int) (stock * mid) + ")");
    
    System.out.println("BH PL -> " + (int) ((mid - firstMid) / firstMid * 100)
        + ", firstMid=" + firstMid + ",mid=" + mid + "");
  }
}
