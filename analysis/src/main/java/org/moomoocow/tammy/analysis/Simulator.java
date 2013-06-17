package org.moomoocow.tammy.analysis;

import static org.moomoocow.tammy.model.StockHistoricalData.Price.CLOSE;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.HIGH;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.LOW;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.MID;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.OPEN;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.model.ModelHelper;
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
  
  private static final Logger log = Logger.getLogger(Simulator.class);

  private List<StockHistoricalData> sortedDailyData;
  
  private final static DateFormat df = new SimpleDateFormat("yyyy-mm-dd");

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
  public static final void main(String args[]) throws ParseException {

    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
    Query q = pm.newQuery(Stock.class, "this.code == '" + args[0] + "'");
    List<Stock> s = (List<Stock>) q.execute();

    Date d = args.length > 1 ? df.parse(args[1]) : null ;
    
    Simulator sim = new Simulator(s.get(0), d);
    sim.execute(new BuyAndHoldSignal());
    
    //int[] mas = { 21, 28 };
    //sim.execute(new ProtectiveSignal(new MAHLSignal(mas, true),0.08 , 0.08, 30));
    
    
    int step = 7;
    int periods = 5;

    for (int x = step; x <= periods * step; x += step) {
      for (int y = x + step; y <= periods * step; y += step) {
        int[] mas = { x, y };
        //sim.execute(new MAHLSignal(mas, true));
        //sim.execute(new MAHLSignal(mas, false));
        for(int x1 = 0; x1 <= 20 ; x1 += 5){
          for(int y1 = 0; y1 <= 20 ; y1 += 5){
            for(int z1 = 0; z1 <= 10 ; z1 += 2){
              double xD = ((double) x1) / 100.0;
              double yD = ((double) y1) / 100.0;
              sim.execute(new ProtectiveSignal(new MAHLSignal(mas, true),xD , yD, z1));
              //sim.execute(new ProtectiveSignal(new MAHLSignal(mas, false),xD, yD, z1));
            }
          }
        }
      }
    }
    

    /*
    for(int x = 5; x <= 30 ; x += 5){
      for(int y = 5; y <= 30 ; y += 5){
        for(int z = 0; z <= 5 ; z ++){
          double xD = ((double) x) / 100.0;
          double yD = ((double) y) / 100.0;
          sim.execute(new ProtectiveSignal(new BuyAndHoldSignal(),xD , yD, z));
        }
      }
    }*/
    

    Map<Signal, Accountant> actionsMap2 = sim.getActionsMap();

    for (Entry<Double, List<Signal>> e : sim.getPnlMap().entrySet()) {
      System.out.println(e.getKey() + "-->");
      for (Signal st : e.getValue()) {
        System.out.println("  " + st.toString() + "->"
            + actionsMap2.get(st));
      }
    }
  }

  public Simulator(Stock s, Date date) {

    this.actionsMap = new HashMap<Signal, Accountant>();
    this.pnlMap = new TreeMap<Double, List<Signal>>(new Comparator<Double>() {
      @Override
      public int compare(Double o1, Double o2) {
        return o1.compareTo(o2) * -1;
      }
    });

    this.sortedDailyData = ModelHelper.prepareDailyData(s,date);
    log.info("From Date=" + this.sortedDailyData.get(0).getDate() 
        + " ~ " + this.sortedDailyData.get(this.sortedDailyData.size() - 1).getDate() + " looping " + this.sortedDailyData.size() + " recs.");
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
        if(tm.buyAll(mid, h.getDate(), r)){
          //log.info("B");
        }
      }// Sell
      else{
        tm.sellAll(mid, h.getDate(), r);
        //log.info("S");
      }

      r = signal.analyze(h.getDate(),open,close,high,low,mid,h.getVol(), tm);
    }

    this.actionsMap.put(signal, tm);

    Double pnl = tm.getAbsolutePnl(mid);

    List<Signal> s = pnlMap.get(pnl);
    if (s == null) {
      s = new ArrayList<Signal>();
      pnlMap.put(pnl, s);
    }
    s.add(signal);

    //System.out.println("PNL=" + String.format("%(,.2f", pnl));

    return pnl;
  }
}
