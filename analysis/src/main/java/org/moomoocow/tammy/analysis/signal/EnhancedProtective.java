package org.moomoocow.tammy.analysis.signal;

import java.util.Date;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;
import org.moomoocow.tammy.analysis.Action.ActionType;
import org.moomoocow.tammy.analysis.MathHelper;

public class EnhancedProtective extends AbstractChainedSignal {
    
  private final double dropPercentAction;
  private boolean greaterThanIsReached;  
  private double top;
  private Protective trigger;
  
  public static EnhancedProtective getRandom(AbstractChainedSignal s){
    double greaterThan = MathHelper.randomDouble(0.05, 0.4);   
    double dropPercentAction = MathHelper.randomDouble(0.02, greaterThan);
    return new EnhancedProtective(greaterThan, dropPercentAction, s);
  }


  public EnhancedProtective(double greaterThan, double dropPercentAction, AbstractChainedSignal signal) {
    super(signal);
    trigger = new Protective(greaterThan, null);
    this.dropPercentAction = dropPercentAction;
    reset();
  }
  
  @Override
  public Action override(Action a, Date date, double open, double close,
      double high, double low, double mid, long vol, Accountant tm) {

      if(dropPercentAction == 0.0) return null;
      
    Action r = trigger.override(a, date, open, close, high, low, mid, vol, tm);        
    
    if(r!= null && ActionType.TAKEPROFIT.equals(r.getType())){
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
          this.actions.put("E-" + act, act);
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
