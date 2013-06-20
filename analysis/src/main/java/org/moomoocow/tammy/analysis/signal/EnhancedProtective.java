package org.moomoocow.tammy.analysis.signal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Deal.Action;

public class EnhancedProtective extends Protective {
    
  private final double dropPercentAction;
  private boolean greaterThanIsReached;  
  private double top;
  
  
  public static EnhancedProtective getRandom(Signal s){
    double greaterThan = Math.random() * 0.35 + 0.05;
    double dropPercentAction = Math.random() * (greaterThan / 2.0);
    return new EnhancedProtective(greaterThan, dropPercentAction, s);
  }


  public EnhancedProtective(double greaterThan, double dropPercentAction, Signal signal) {
    super(greaterThan, signal);
    this.dropPercentAction = dropPercentAction;
    reset();
  }
  
  @Override
  public Action override(Action a, Date date, double open, double close,
      double high, double low, double mid, long vol, Accountant tm) {

    Action r = super.override(a, date, open, close, high, low, mid, vol, tm);
    
    if(dropPercentAction < 0.0) return r;
    
    if(Action.TAKEPROFIT.equals(r)){
      this.greaterThanIsReached = true;
    }
    
    if(this.greaterThanIsReached){
      
      if(top < mid)
        top = mid;
      else{
        double percentDropFromTop = new BigDecimal(top - mid).divide(new BigDecimal(top),2,RoundingMode.HALF_EVEN).doubleValue();
        if (percentDropFromTop  >= dropPercentAction){
          reset();
          return Action.TAKEPROFIT;
        }
      }      
    }
    
    return a;
    
  }
  
  private void reset(){
    this.greaterThanIsReached = false;
    this.top = 0.0;
  }

  @Override
  public String chainedToString() {
    return "EnhancedProtective [dropPercentAction=" + dropPercentAction + "]=>" + super.chainedToString();
  }
  
  
}
