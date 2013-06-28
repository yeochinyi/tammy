package org.moomoocow.tammy.analysis.signal;

import java.util.Date;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;
import org.moomoocow.tammy.analysis.Action.ActionType;
import org.moomoocow.tammy.analysis.MathHelper;

public class EnhancedProtective extends Protective {
    
  private final double dropPercentAction;
  private boolean greaterThanIsReached;  
  private double top;
  
  public static EnhancedProtective getRandom(AbstractChainedSignal s){
    double greaterThan = Math.random() * 0.35 + 0.05;
    double dropPercentAction = Math.random() * (greaterThan / 2.0);
    return new EnhancedProtective(greaterThan, dropPercentAction, s);
  }


  public EnhancedProtective(double greaterThan, double dropPercentAction, AbstractChainedSignal signal) {
    super(greaterThan, signal);
    this.dropPercentAction = dropPercentAction;
    reset();
  }
  
  @Override
  public Action override(Action a, Date date, double open, double close,
      double high, double low, double mid, long vol, Accountant tm) {

    Action r = super.override(a, date, open, close, high, low, mid, vol, tm);
    
    if(dropPercentAction < 0.0) return r;
    
    if(ActionType.TAKEPROFIT.equals(r)){
      this.greaterThanIsReached = true;
    }
    
    if(this.greaterThanIsReached){
      
      if(top < mid)
        top = mid;
      else{
        double percentDropFromTop =  MathHelper.divide(top - mid,top);
        if (percentDropFromTop  >= dropPercentAction){
          reset();
          Action act = new Action(ActionType.TAKEPROFIT,date,mid);
          this.actions.put("EPT-" + act, act);
          return act;
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
