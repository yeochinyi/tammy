package org.moomoocow.tammy.analysis;

import java.util.Date;

public class Deal {
  
  public static enum Action { 
    BUY(true), 
    SELL(false), 
    STOPLOSS(false), 
    TAKEPROFIT(false);
    
    boolean isBuy;
    
    boolean isBuy(){
      return isBuy;
    }
    
    Action(boolean isBuy){
      this.isBuy = isBuy;
    }
   }

  //private Boolean isBuy;
  
  private Long qty;
  
  private Double price;
  
  private Double commission;
  
  private Date date;
  
  private Action reason;

  public Deal(Long qty, Double price, Double commission,
      Date date, Action reason) {
    super();
    this.qty = qty;
    this.price = price;
    this.commission = commission;
    this.date = date;
    this.reason = reason;
  }

  public Boolean isBuy() {
    return reason.isBuy();
  }

  public Long getQty() {
    return qty;
  }

  public Double getPrice() {
    return price;
  }

  public Double getCommission() {
    return commission;
  }

  public Date getDate() {
    return date;
  }

  @Override
  public String toString() {    
     
    return (isBuy() ? "B" : "S") + "@"
        + String.format("%(,.2f", price) + ",qty=" + qty + 
        ",date=" + String.format("%1$te/%<tm/%<tY", date) + " due to " + this.reason;
  }

  public Action getReason() {
    return reason;
  }

  
  
  
}
