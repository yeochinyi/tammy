package org.moomoocow.tammy.analysis;

import static org.moomoocow.tammy.model.StockHistoricalData.Price.CLOSE;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.HIGH;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.LOW;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.MID;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.OPEN;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.moomoocow.tammy.analysis.Transaction.REASON;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.util.Helper;

/**
 * Set the stage with historical market prices to be executed
 * 
 * @author yeocae
 * 
 */
public class Simulator {

  private List<StockHistoricalData> sortedDailyData;

  public List<StockHistoricalData> getSortedDailyData() {
    return sortedDailyData;
  }

  private Map<Signal, List<Transaction>> actionsMap;

  public Map<Signal, List<Transaction>> getActionsMap() {
    return actionsMap;
  }

  private Map<Double, List<Signal>> pnlMap;

  public Map<Double, List<Signal>> getPnlMap() {
    return pnlMap;
  }

  @SuppressWarnings("unchecked")
  public static final void main(String args[]) {

    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
    Query q = pm.newQuery(Stock.class, "this.code == '" + args[0] + "'");
    List<Stock> s = (List<Stock>) q.execute();

    Simulator sim = new Simulator(s.get(0).getSortedDailyData(), 720);

    ActionCondition ac = new ActionCondition(10000.0, 0.20, 0.05, 3);
    
    sim.execute(ac, new BuyAndHoldSignal());

    int step = 7;
    int periods = 10;

    for (int x = step; x <= periods * step; x += step) {
      for (int y = x + step; y <= periods * step; y += step) {
        int[] mas = { x, y };
        sim.execute(ac, new MAHLSignal(mas, true));
        sim.execute(ac, new MAHLSignal(mas, false));
      }
    }

    Map<Signal, List<Transaction>> actionsMap2 = sim.getActionsMap();

    for (Entry<Double, List<Signal>> e : sim.getPnlMap().entrySet()) {
      System.out.println(e.getKey() + "-->");
      for (Signal st : e.getValue()) {
        System.out.println("  " + st.toString() + "->"
            + actionsMap2.get(st).size());
      }
    }
  }

  public Simulator(List<StockHistoricalData> oldSortedDailyData, int days) {

    this.actionsMap = new HashMap<Signal, List<Transaction>>();
    this.pnlMap = new TreeMap<Double, List<Signal>>(new Comparator<Double>() {
      @Override
      public int compare(Double o1, Double o2) {
        return o1.compareTo(o2) * -1;
      }
    });

    final int startOfDays = (oldSortedDailyData.size() < days ? 0
        : oldSortedDailyData.size() - days);
    this.sortedDailyData = new ArrayList<StockHistoricalData>();

    // Populated multiplers
    Double accumulatedMultipler = null;
    for (int x = startOfDays; x < oldSortedDailyData.size(); x++) {
      StockHistoricalData h = oldSortedDailyData.get(x);
      accumulatedMultipler = h.accumlateMultiplers(accumulatedMultipler);
      this.sortedDailyData.add(h);
    }

    System.out.println("Total recs = " + this.sortedDailyData.size());
  }

  private void print(boolean isBuy, double price, long qty, Date d,
      int holdingDays) {
    System.out.println((isBuy ? "B" : "S") + "@"
        + String.format("%(,.2f", price) + ",qty=" + qty + ",diffDays="
        + holdingDays + ",date=" + String.format("%1$te/%<tm/%<tY", d));
  }

  public Double execute(ActionCondition ac, Signal signal) {

    List<Transaction> actions = new ArrayList<Transaction>();

    double cash = ac.getInitialCash();
    long stock = 0;
    double lastPurchasedPrice = 0.0; 
    double commissionPercent = 0.004;

    long buySellSignal = 0;
    Date lastTransDate = null;
    

    boolean firstTrans = true;
    
    Transaction.REASON r = null;

    double mid = 0.0;
    double open = 0.0;
    double close = 0.0;
    double high = 0.0;
    double low = 0.0;

    for (int x = 0; x < this.sortedDailyData.size(); x++) {
      StockHistoricalData h = sortedDailyData.get(x);
      if (firstTrans) {
        firstTrans = false;
        lastTransDate = h.getDate();
        continue;
      }

      // StockHistoricalData p = sortedDailyData.get(x - 1);

      mid = h.getAccX(MID);
      open = h.getAccX(OPEN);
      close = h.getAccX(CLOSE);
      high = h.getAccX(HIGH);
      low = h.getAccX(LOW);

      int diffDays = Days.daysBetween(new DateTime(lastTransDate),
          new DateTime(h.getDate())).getDays();
      // Buy
      if (buySellSignal > 0 ) {
        long maxStockAllowed = (long) Math.floor(cash
            / (mid * (1.0 + commissionPercent) * buySellSignal));

        if(maxStockAllowed == 0){
          System.out.println("Can't buy anymore stocks");
          continue;
        }
        
        lastPurchasedPrice = mid;
        double cashSpentWoCom = maxStockAllowed * mid;
        double commissionAmt = cashSpentWoCom * 0.04;

        lastTransDate = h.getDate();
        actions.add(new Transaction(true, maxStockAllowed, mid, commissionAmt,
            lastTransDate,r));
        print(true, mid, maxStockAllowed, h.getDate(), diffDays);

        cash -= (cashSpentWoCom + commissionAmt);
        stock += maxStockAllowed;
      }// Sell
      else if (buySellSignal < 0) {
        
        if(stock == 0){
          System.out.println("Can't sell anymore stocks");
          continue;
        }

        double cashReceived = stock * (buySellSignal * -1) * mid;
        double commissionAmt = cashReceived * 0.04;

        lastTransDate = h.getDate();
        actions.add(new Transaction(false, stock, mid, commissionAmt,
            lastTransDate,r));
        print(false, mid, stock, h.getDate(), diffDays);

        stock = 0;
        cash += (cashReceived - commissionAmt);
      }

      buySellSignal = signal.analyze(h.getDate(), mid, open, close, high, low,
          h.getVol());
      
      if(buySellSignal != 0) r = Transaction.REASON.SIGNAL;
      
      r = ac.getSignal(stock, lastPurchasedPrice, mid, diffDays);
      
      if(r==REASON.STOPLOSS || r==REASON.TAKEPROFIT){
        buySellSignal = 1;
      }
      
    }

    this.actionsMap.put(signal, actions);

    Double pnl = (cash + (stock * mid) - ac.getInitialCash()) / ac.getInitialCash();

    List<Signal> s = pnlMap.get(pnl);
    if (s == null) {
      s = new ArrayList<Signal>();
      pnlMap.put(pnl, s);
    }
    s.add(signal);

    System.out.println("PNL=" + String.format("%(,.2f", pnl));

    return pnl;
  }
}
