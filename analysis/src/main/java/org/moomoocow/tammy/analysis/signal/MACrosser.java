package org.moomoocow.tammy.analysis.signal;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Deal.Action;
import org.moomoocow.tammy.analysis.math.ExponentialMA;
import org.moomoocow.tammy.analysis.math.MA;
import org.moomoocow.tammy.analysis.math.SimpleMA;

public class MACrosser extends AbstractChainedSignal {
  
  
  public static MACrosser getRandomSMA(Signal s){
    int[] i = { 
        (int) (Math.random() * 11.0 + 3),
        (int) (Math.random() * 60.0 + 15),
        
    };
    return new MACrosser(i, true);
  }
  
  public static MACrosser getRandomEMA(Signal s){
    int[] i = { 
        (int) (Math.random() * 11.0 + 3),
        (int) (Math.random() * 60.0 + 15),
        
    };
    return new MACrosser(i, false);
  }
  
  @SuppressWarnings("unused")
  private static Logger log = Logger.getLogger(MACrosser.class);

  private int[] maPeriods;
  private MA ma;
  private int shortestMaPeriod;
  private int longestMaPeriod;
  private boolean buyWhenShortOverLong;
  
  private Action lastAction = null;
  
  private Map<String,SortedMap<Date,Double>> displayPoints;

  public MACrosser(int[] maPeriods, boolean isSimpleMA) {
    this.maPeriods = maPeriods;
    this.ma = isSimpleMA ? new SimpleMA(maPeriods) : new ExponentialMA(maPeriods);
    this.displayPoints = new HashMap<String,SortedMap<Date,Double>>();
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
  public Action override(Action a,Date date, double open, double close, double high, double low, double mid,long vol, Accountant tm) {
    ma.add(mid);

    for (Integer i : maPeriods){ 
      this.displayPoints.get(i.toString()).put(date, ma.getMA(i)); 
    }
    
    Double maShort = ma.getMA(shortestMaPeriod);
    Double maLong = ma.getMA(longestMaPeriod);
        
    if (maShort == null || maLong == null) return null;
    
    if(maShort.equals(maLong)) return null;
        
    Action returnAction = maShort > maLong ? Action.BUY : Action.SELL;
        
    boolean returnNull  = returnAction.equals(lastAction);
    
    this.lastAction = returnAction;
    
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
  public String chainedToString(){
    StringBuffer s = new StringBuffer("MAHLStrategy[");
    for (int i : this.maPeriods) {
      s.append(i).append(",");
    }
    s.append("buyLongOverShort=").append(buyWhenShortOverLong).append("]=>" + super.chainedToString());
    return  s.toString();
  }
  
  @Override
  public boolean shouldNotBeChainedTriggered(){
    return true;
  }

}
