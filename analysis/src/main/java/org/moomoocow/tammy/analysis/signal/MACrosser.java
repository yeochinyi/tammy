package org.moomoocow.tammy.analysis.signal;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;
import org.moomoocow.tammy.analysis.Action.ActionType;
import org.moomoocow.tammy.analysis.MathHelper;
import org.moomoocow.tammy.analysis.math.ExponentialMA;
import org.moomoocow.tammy.analysis.math.MA;
import org.moomoocow.tammy.analysis.math.SimpleMA;

public class MACrosser extends AbstractChainedSignal {
  
  
  public static MACrosser getRandomSMA(Signal s){
    int[] i = { 
    		MathHelper.randomInt(3, 14),
    		MathHelper.randomInt(15, 60),        
    };
    return new MACrosser(i, true);
  }
  
  public static MACrosser getRandomEMA(Signal s){
    int[] i = { 
    		MathHelper.randomInt(3, 14),
    		MathHelper.randomInt(15, 60),        
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
  
  private ActionType lastAction = null;
  
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
      this.displayPoints.put(MACrosser.class.getSimpleName() + "_" + i.toString(), m);
    }
  }

  @Override
  public Action override(Action a,Date date, double open, double close, double high, double low, double mid,long vol, Accountant tm) {
    ma.add(mid);

    for (Integer i : maPeriods){ 
      this.displayPoints.get(MACrosser.class.getSimpleName() + "_" + i.toString()).put(date, ma.getMA(i)); 
    }
    
    Double maShort = ma.getMA(shortestMaPeriod);
    Double maLong = ma.getMA(longestMaPeriod);
        
    if (maShort == null || maLong == null) return null;
    
    if(maShort.equals(maLong)) return null;
            
    ActionType type = maShort > maLong ? ActionType.BUY : ActionType.SELL;
    
    if(type.equals(lastAction)) return null;   
    
    this.lastAction = type;    
    
    return new Action(type,date,mid);
  }

  @Override
  public Map<String,SortedMap<Date,Double>> getCombinedGraphPoints() {
    return displayPoints;
  }
  
  @Override
  public String chainedToString(){
    StringBuffer s = new StringBuffer("MAHLStrategy[");
    for (int i : this.maPeriods) {
      s.append(i).append(",");
    }
    s.append("buyWhenShortOverLong=").append(buyWhenShortOverLong).append("]=>" + super.chainedToString());
    return  s.toString();
  }
  
  @Override
  public boolean shouldNotBeChainedTriggered(){
    return true;
  }

}
