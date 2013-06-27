package org.moomoocow.tammy.analysis.signal;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;

public interface Signal {

  Action analyze(Date date, double open, double close, double high, double low, double mid, long vol, Accountant tm);
  
  boolean isTriggeredAtLeast(int times);
  
  Map<String,SortedMap<Date,Double>> getGraphPoints();
  
  Map<String,Action> getActions();

}
