package org.moomoocow.tammy.analysis;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import org.moomoocow.tammy.analysis.Deal.Action;

public class BuyAndHoldSignal implements Signal {

  private boolean bought = false;
  
  @Override
  public Action analyze(Date date, double open, double close, double high, double low, double mid,Accountant tm) {
    if(!bought){
      bought=true;
      return Action.BUY;
    }
    return null;
  }

  @Override
  public Map<String, SortedMap<Date, Double>> getDisplayPoints() {
    return null;
  }

}
