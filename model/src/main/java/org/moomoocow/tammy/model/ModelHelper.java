package org.moomoocow.tammy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ModelHelper {


  public static StockHistoricalData getMaxDatedDailyData(Stock s){
    
    StockHistoricalData max = null;
    
    for (StockHistoricalData dd : s.getDailyData()) {
      if(max == null || dd.getDate().after(max.getDate())) 
        max = dd;
    }
    
    return max;
  }
  
  
  public static List<StockHistoricalData> prepareDailyData(Stock s, Date date) {
    List<StockHistoricalData> l = new ArrayList<StockHistoricalData>(s.getDailyData());
    Collections.sort(l, new Comparator<StockHistoricalData>(){
      @Override
      public int compare(StockHistoricalData o1, StockHistoricalData o2) {
        int r = o1.getDate().compareTo(o2.getDate()) * -1; // Latest date first
        return r;
      }});
    
    //final int startOfDays = (l.size() < daysFromLatestRec ? 0
      //: l.size() - daysFromLatestRec);

    LinkedList<StockHistoricalData> returnValue = new LinkedList<StockHistoricalData>();
    
    Double accumulatedMultipler = null;
    int x =0;
    while(x < l.size()){
      StockHistoricalData h = l.get(x++);
      if(date != null && h.getDate().compareTo(date) == -1) break;
      accumulatedMultipler = h.accumlateMultiplers(accumulatedMultipler);
      //System.out.println("accumulatedMultipler=" + accumulatedMultipler);
      returnValue.addFirst(h);
    }
    
    return returnValue;
  }
 

  

}
