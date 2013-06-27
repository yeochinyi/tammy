package org.moomoocow.tammy.analysis;

import java.util.Date;
import java.util.List;

public class MarkToMarket {

  private final Date endDate;  
  private final double endPrice;
  private final List<Deal> deals;
  private final double realPnl;
  private final double realAnnualPnl;
  public MarkToMarket(Date endDate, double endPrice, List<Deal> deals,double realPnl, double realAnnualPnl) {
    super();
    this.endDate = endDate;
    this.endPrice = endPrice;
    this.deals =deals;
    this.realPnl  = realPnl;
    this.realAnnualPnl = realAnnualPnl;
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

  @Override
  public String toString() {
        
    return "MTM[endDate=" + String.format("%1$te/%<tm/%<tY",endDate) + ", endPrice=" + String.format("%(,.2f",endPrice)
        + ", deals=" + deals + ", realPnl=" + String.format("%(,.2f",realPnl) + ", realAnnualPnl=" + String.format("%(,.2f",realAnnualPnl) +  "]";
  }

  public double getRealAnnualPnl() {
    return realAnnualPnl;
  }
  
}
