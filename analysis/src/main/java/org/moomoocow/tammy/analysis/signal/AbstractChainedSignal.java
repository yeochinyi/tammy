package org.moomoocow.tammy.analysis.signal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Deal.Action;

public abstract class AbstractChainedSignal implements Signal {

  private Signal chainSignal;

  private Map<Date, Action[]> overriddenMap;

  public AbstractChainedSignal() {
    this(null);
  }

  public AbstractChainedSignal(Signal chainSignal) {
    this.overriddenMap = new HashMap<Date, Action[]>();
    this.chainSignal = chainSignal;
  }

  //public abstract String chainedToString();

  
  public abstract Action override(Action a, Date date, double open,
      double close, double high, double low, double mid, long vol, Accountant tm);

  @Override
  public Action analyze(Date date, double open, double close, double high,
      double low, double mid, long vol, Accountant tm) {
    Action original = this.chainSignal != null ? this.chainSignal.analyze(date,
        open, close, high, low, mid, vol, tm) : null;
    Action override = this.override(original, date, open, close, high, low,
        mid, vol, tm);
    if ((original != null && !original.equals(override)) || 
        (override != null && !override.equals(original))) {
      Action[] as = { original, override };
      this.overriddenMap.put(date, as);
    }

    return override;
  }

  @Override
  public Map<String, SortedMap<Date, Double>> getDisplayPoints() {
    return null;
  }
  
  public String toString(){
    return this.chainedToString()  + (chainSignal != null ? "+=+" + chainSignal.toString() : "");
  }
  
  public String chainedToString() {
    return "Chained[triggered=" + overriddenMap.size() + "]";
  }
  
  public boolean shouldNotBeChainedTriggered(){
    return false;
  }
  
  @Override
  public boolean isTriggeredAtLeast(int times){
    boolean shouldNotBeChainedTriggered = shouldNotBeChainedTriggered();
    int size = overriddenMap.size();
    boolean chain = chainSignal != null ? chainSignal.isTriggeredAtLeast(times) : true;
    return (shouldNotBeChainedTriggered || size  >= times) 
        && chain;
  }

}
