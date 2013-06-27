package org.moomoocow.tammy.analysis.signal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;

public abstract class AbstractChainedSignal implements Signal {

  private AbstractChainedSignal chainSignal;

  private Map<Date, Action[]> overriddenMap;

  protected Map<String,Action> actions;
  
  public AbstractChainedSignal() {
    this(null);
  }

  public AbstractChainedSignal(AbstractChainedSignal chainSignal) {
    this.overriddenMap = new HashMap<Date, Action[]>();
    this.chainSignal = chainSignal;
    this.actions = new  HashMap<String,Action>();
  }
  
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
  
  public Map<String, SortedMap<Date, Double>> getCombinedGraphPoints(){
    return null;
  }
  
  @Override
  public Map<String, SortedMap<Date, Double>> getGraphPoints() {
    Map<String, SortedMap<Date, Double>> chained = (chainSignal != null ? chainSignal.getGraphPoints() : null);
    Map<String, SortedMap<Date, Double>> current = getCombinedGraphPoints();
    
    if(chained != null){
      if(current != null)
        chained.putAll(current);
      return chained;
    }
    return current;
  }

  public String chainedToString() {
    return "Chained[triggered=" + overriddenMap.size() + "]";
  }  
  
  public String toString(){
    return this.chainedToString()  + (chainSignal != null ? "+=+" + chainSignal.toString() : "");
  }
  
  
  public Map<String,Action> getCombinedActions(){
	    return this.actions;
 }

  
  @Override
  public Map<String,Action> getActions(){
    Map<String,Action> chained = (chainSignal != null ? chainSignal.getActions() : null);
    Map<String,Action> current = getCombinedActions();
    
    if(chained != null){
      if(current != null)
        chained.putAll(current);
      return chained;
    }
    return current;
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
