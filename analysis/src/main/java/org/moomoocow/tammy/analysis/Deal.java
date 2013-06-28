package org.moomoocow.tammy.analysis;

import java.util.Date;

public class Deal {
  
  private long qty;
  
  private double unitPrice;
  
  private double commission;
  
  private Date date;
  
  private Action reason;

  public Deal(long qty, double unitPrice, double commission,
      Date date, Action reason) {
    super();
    this.qty = qty;
    this.unitPrice = unitPrice;
    this.commission = commission;
    this.date = date;
    this.reason = reason;
  }

  public boolean isBuy() {
    return reason.isBuy();
  }

  public long getQty() {
    return qty;
  }

  public double getUnitPrice() {
    return unitPrice;
  }

  public double getCommission() {
    return commission;
  }

  public Date getDate() {
    return date;
  }
  
  public double getTotalTransaction(){
    return qty * unitPrice + (isBuy() ? commission : -commission );
  }

  
  
  @Override
  public String toString() {    
     
    return (isBuy() ? "B" : "S") + "@"
        + String.format("%(,.2f", unitPrice) + ",qty=" + qty + 
        ",date=" + String.format("%1$te/%<tm/%<tY", date) + " due to " + this.reason;
  }

  public Action getReason() {
    return reason;
  }

  
  
  
}
