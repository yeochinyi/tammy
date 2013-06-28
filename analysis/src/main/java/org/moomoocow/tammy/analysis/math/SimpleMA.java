package org.moomoocow.tammy.analysis.math;

import java.util.LinkedList;
import java.util.List;

import org.moomoocow.tammy.analysis.MathHelper;

public class SimpleMA implements MA {

  private List<Double> data;
  private int max;

  public SimpleMA(int[] periods) {
    this.data = new LinkedList<Double>();
        
    for (int i : periods) {
      if(i > max){
        max = i;
      }
    }
  }

  /* (non-Javadoc)
   * @see org.moomoocow.tammy.analysis.math.MA#add(double)
   */
  @Override
  public MA add(double d) {
    data.add(0,d);
    if(data.size() > max){
      data.remove(max);
    }
    return this;
  }

  /* (non-Javadoc)
   * @see org.moomoocow.tammy.analysis.math.MA#getMA(int)
   */
  @Override
  public Double getMA(int span) {

    int size = data.size();

    if (size < span)
      return null;

    double total = 0;

    for (int i = 0; i < span; i++) {
      total += data.get(i);
    }

    return MathHelper.divide(total,span);

  }

}
