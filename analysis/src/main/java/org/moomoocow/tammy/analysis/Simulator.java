package org.moomoocow.tammy.analysis;

import static org.moomoocow.tammy.model.StockHistoricalData.Price.CLOSE;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.HIGH;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.LOW;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.MID;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.OPEN;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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

  private Map<Signal, Accountant> actionsMap;

  public Map<Signal, Accountant> getActionsMap() {
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
    sim.execute(new BuyAndHoldSignal());

    int step = 7;
    int periods = 10;

    for (int x = step; x <= periods * step; x += step) {
      for (int y = x + step; y <= periods * step; y += step) {
        int[] mas = { x, y };
        sim.execute(new ActionCondition(new MAHLSignal(mas, true),0.20, 0.05, 3));
        sim.execute(new ActionCondition(new MAHLSignal(mas, false),0.20, 0.05, 3));
      }
    }

    Map<Signal, Accountant> actionsMap2 = sim.getActionsMap();

    for (Entry<Double, List<Signal>> e : sim.getPnlMap().entrySet()) {
      System.out.println(e.getKey() + "-->");
      for (Signal st : e.getValue()) {
        System.out.println("  " + st.toString() + "->"
            + actionsMap2.get(st).size());
      }
    }
  }

  public Simulator(List<StockHistoricalData> oldSortedDailyData, int days) {

    this.actionsMap = new HashMap<Signal, Accountant>();
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



  public Double execute(Signal signal) {

    Accountant tm = new Accountant(10000,0.004);
        
    boolean firstTrans = true;
    Deal.Action r = null;

    double mid = 0.0;
    double open = 0.0;
    double close = 0.0;
    double high = 0.0;
    double low = 0.0;

    for (int x = 0; x < this.sortedDailyData.size(); x++) {
      StockHistoricalData h = sortedDailyData.get(x);
      if (firstTrans) {
        firstTrans = false;
        continue;
      }

      mid = h.getAccX(MID);
      open = h.getAccX(OPEN);
      close = h.getAccX(CLOSE);
      high = h.getAccX(HIGH);
      low = h.getAccX(LOW);

      // Buy
      if (r == null){
        
      }      
      else if(r.isBuy) {      
        tm.buyAll(mid, h.getDate(), r);
      }// Sell
      else{
        tm.sellAll(mid, h.getDate(), r);
      }

      r = signal.analyze(h, tm);
    }

    this.actionsMap.put(signal, tm);

    Double pnl = tm.getAbsolutePnl(mid);

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
