package org.moomoocow.tammy.analysis.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MA {

  private List<Double> data;

  public MA() {
    this.data = new ArrayList<Double>();
  }

  public MA add(Double d) {
    data.add(d);
    return this;
  }

  public Double getMA(int span) {

    int size = data.size();

    if (size < span)
      return null;

    double total = 0;

    for (int i = size; i > size - span; i--) {
      total += data.get(i - 1);
    }

    double r = new BigDecimal(total).divide(new BigDecimal(span), 2,
        RoundingMode.HALF_EVEN).doubleValue();
    
    return r;

  }

}
