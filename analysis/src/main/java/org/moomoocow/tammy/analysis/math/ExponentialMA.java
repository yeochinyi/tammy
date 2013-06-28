package org.moomoocow.tammy.analysis.math;

import java.util.HashMap;
import java.util.Map;

import org.moomoocow.tammy.analysis.MathHelper;

public class ExponentialMA implements MA {

  private Map<Integer,Double> lastRecords;
  
  private Map<Integer,Double> smoothingFactors;
  
  private MA ma;

  public ExponentialMA(int[] periods) {
    
    this.ma = new SimpleMA(periods);
    
    this.lastRecords = new HashMap<Integer,Double>();
    this.smoothingFactors = new HashMap<Integer,Double>();
    
    for (int i : periods) {
      double s = MathHelper.divide(2, i + 1);
      smoothingFactors.put(i,s);
    }
    
  }

  public ExponentialMA add(double d) {
    this.ma.add(d);

    for (int i : smoothingFactors.keySet()) {
      //see if there is a prev record
      Double lastEMA = this.lastRecords.get(i);

      if(lastEMA == null){
        Double ma = this.ma.getMA(i);
        if(ma == null){
          //no values in MA yet.. do nothing
        }
        else{
          this.lastRecords.put(i,ma);
        }
      }
      else{
        double sf = smoothingFactors.get(i);
        double ema =  (d - lastEMA) * sf +  lastEMA;
        this.lastRecords.put(i,ema);
      }
    }
    
    return this;
  }

  public Double getMA(int span) {
    return this.lastRecords.get(span);
    
  }

}
