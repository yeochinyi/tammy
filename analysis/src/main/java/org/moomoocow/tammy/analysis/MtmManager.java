package org.moomoocow.tammy.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MtmManager {
  
  private List<MarkToMarket> mtms;
  
  private List<Deal> currentDeals;
  
  private double currentProfit;
  
  private double currentCost;
  
  private Date startDate;
  
  public MtmManager(){
    this.mtms = new ArrayList<MarkToMarket>();
  }
  
  public void reset(){
    this.currentDeals = new ArrayList<Deal>();
    this.currentCost = 0.0;
    this.currentProfit = 0.0;
  }
  
  public void start(Date startDate){
    this.startDate = startDate;
    reset();
  }
  
  public void add(Deal d){
    double t = d.getTotalTransaction();
    if(d.isBuy()){
      currentCost += t;  
    }
    else{
      currentProfit += t;
    }
    this.currentDeals.add(d);
  }
  
  public void markToMarket(Date endDate, double price){
    
    
    
    //MarkToMarket m = new MarkToMarket(endDate, price,this.currentDeals);
    //mtms.add(m);
    reset();
  }
   

}
