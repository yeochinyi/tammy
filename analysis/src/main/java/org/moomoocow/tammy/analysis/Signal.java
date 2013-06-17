package org.moomoocow.tammy.analysis;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import org.moomoocow.tammy.analysis.Deal.Action;
import org.moomoocow.tammy.model.StockHistoricalData;

public interface Signal {

  Action analyze(Date date, double open, double close, double high, double low, double mid, long vol, Accountant tm);
  
  Map<String,SortedMap<Date,Double>> getDisplayPoints();

}
