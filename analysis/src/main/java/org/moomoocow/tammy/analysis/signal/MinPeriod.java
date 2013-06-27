package org.moomoocow.tammy.analysis.signal;

import java.util.Date;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.MathHelper;
import org.moomoocow.tammy.analysis.Deal.Action;

public class MinPeriod extends AbstractChainedSignal {

  private final int minPeriod;
  
  public static MinPeriod getRandom(Signal s){
    return new MinPeriod(MathHelper.randomInt(3, 14),s);
  }

  public MinPeriod(int minPeriod, Signal signal) {
    super(signal);
    this.minPeriod = minPeriod;
  }

  @Override
  public Action override(Action a, Date date, double open, double close, double high,
      double low, double mid, long vol, Accountant tm) {   
    if(minPeriod > 0 && tm != null){
      Integer period = tm.getPeriodAfterLastDealExclWeekends(date);
      if(period != null && period <= minPeriod)
        return null;
    }
      
    
    return a;
  }

  @Override
  public String chainedToString() {
    return "MinPeriod [minPeriod=" + minPeriod
        + "]=>" + super.chainedToString();
  }
  
  


}
