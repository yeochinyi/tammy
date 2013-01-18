package org.moomoocow.tammy.marketdata;

import java.util.ArrayList;
import java.util.List;

public class MovingAverager {

  public final static void main(String[] args) {
    MovingAverager ta = new MovingAverager();
    System.out.println(ta.add(1.0).add(2.0).add(3.0).getMA(2));
  }

  private List<Double> data;

  public MovingAverager() {
    this.data = new ArrayList<Double>();
  }

  public MovingAverager add(Double d) {
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
