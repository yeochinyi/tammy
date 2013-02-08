package org.moomoocow.tammy.analysis;

import java.util.ArrayList;
import java.util.List;

public class MA {

  public final static void main(String[] args) {
    MA ta = new MA();
    System.out.println(ta.add(1.0).add(2.0).add(3.0).getMA(2));
  }

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

    return total / span;

  }

}
