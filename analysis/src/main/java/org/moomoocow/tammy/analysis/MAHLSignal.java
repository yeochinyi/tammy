package org.moomoocow.tammy.analysis;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.Deal.Action;

public class MAHLSignal implements Signal {
  
  private static Logger log = Logger.getLogger(MAHLSignal.class);

  private int[] maPeriods;
  private MA ma;
  private int shortestMaPeriod;
  private int longestMaPeriod;
  private int buyLongOverShort;
  
  private Action lastAction = null;
  
  private Map<String,SortedMap<Date,Double>> displayPoints;

  public MAHLSignal(int[] maPeriods, boolean buyLongOverShort) {
    this.maPeriods = maPeriods;
    this.ma = new MA();
    this.displayPoints = new HashMap<String,SortedMap<Date,Double>>();
    this.buyLongOverShort = buyLongOverShort ? 1 : 0;
    shortestMaPeriod = Integer.MAX_VALUE;
    longestMaPeriod = 0;
    for (Integer i : maPeriods) {
      if(i > longestMaPeriod) longestMaPeriod = i;
      if(i < shortestMaPeriod) shortestMaPeriod = i;
      SortedMap<Date,Double> m = new TreeMap<Date,Double>(new Comparator<Date>() {
        @Override
        public int compare(Date o1, Date o2) {
          return o1.compareTo(o2);
        }
      });
      this.displayPoints.put(i.toString(), m);
    }
  }

  @Override
  public Action analyze(Date date, double open, double close, double high, double low, double mid, Accountant tm) {
    ma.add(mid);

    for (Integer i : maPeriods){ 
      this.displayPoints.get(i.toString()).put(date, ma.getMA(i)); 
    }
    
    Double maShort = ma.getMA(shortestMaPeriod);
    Double maLong = ma.getMA(longestMaPeriod);
        
    if (maShort == null || maLong == null) return null;
    
    if(maShort.equals(maLong)) return null;
        
    int maBit = maLong > maShort ? 1 : 0;
        
    Action returnAction = (maBit ^ buyLongOverShort) > 0 ? Action.BUY : Action.SELL;
    
    boolean returnNull  = returnAction.equals(lastAction) || lastAction == null;
    
    this.lastAction = returnAction;
    
    //if(log.isDebugEnabled()){
    //  log.debug("maShort=" + maShort + ",maLong=" + maLong);
    //}

    if(returnNull){
      return null;
    }
    
    return returnAction;
    
         
    
  }

  @Override
  public Map<String,SortedMap<Date,Double>> getDisplayPoints() {
    return displayPoints;
  }
  
  @Override
  public String toString(){
    StringBuffer s = new StringBuffer("MAHLStrategy[");
    for (int i : this.maPeriods) {
      s.append(i).append(",");
    }
    s.append("buyLongOverShort=").append(buyLongOverShort).append("]");
    return  s.toString();
  }

}
