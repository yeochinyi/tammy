package org.moomoocow.tammy.analysis.signal;

import java.util.Date;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;

public class BuyAtFirst extends AbstractChainedSignal {

  private boolean bought = false;
  
  public BuyAtFirst(AbstractChainedSignal s){
    super(s);
  }

  @Override
  public Action override(Action a, Date date, double open, double close,
      double high, double low, double mid, long vol, Accountant tm) {
    if (!bought) {
      bought = true;
      return Action.BUY;
    }
    return a;
  }

  @Override
  public String chainedToString() {
    return "BuyAndHold=>" + super.chainedToString();
  }
  
  @Override
  public boolean shouldNotBeChainedTriggered(){
    return true;
  }
}
