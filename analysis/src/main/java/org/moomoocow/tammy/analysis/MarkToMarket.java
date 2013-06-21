package org.moomoocow.tammy.analysis;

import java.util.Date;
import java.util.List;

public class MarkToMarket {

  private final Date endDate;  
  private final double endPrice;
  private final List<Deal> deals;
  private final double realPnl;
  public MarkToMarket(Date endDate, double endPrice, List<Deal> deals,double realPnl) {
    super();
    this.endDate = endDate;
    this.endPrice = endPrice;
    this.deals =deals;
    this.realPnl  = realPnl;
  }
  
  public double getRealPnl() {
    return realPnl;
  }

  public Date getEndDate() {
    return endDate;
  }

  public double getEndPrice() {
    return endPrice;
  }

  public List<Deal> getDeals() {
    return deals;
  }

  
}
