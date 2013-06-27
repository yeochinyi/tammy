package org.moomoocow.tammy.analysis;

import java.util.Date;

public class Action{
  
  private final ActionType type;
  
  private final Date date;
  
  private final Double price;
  
  
  public Action(ActionType type, Date date, Double price) {
    super();
    this.type = type;
    this.date = date;
    this.price = price;
  }

  public enum ActionType { 
    BUY(true), 
    SELL(false), 
    STOPLOSS(false), 
    TAKEPROFIT(false);
    
    boolean isBuy;
    
    
    ActionType(boolean isBuy){
      this.isBuy = isBuy;
    }
   }
  
  public boolean isBuy(){
    return type.isBuy;
  }

}