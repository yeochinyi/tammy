package org.moomoocow.tammy.analysis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.moomoocow.tammy.analysis.Deal.Action;

public class Accountant {
  
  private static Logger log = Logger.getLogger(Accountant.class);
  
  private final double initialCash;
  
  private double cash;
  
  public double getCash() {
    return cash;
  }

  public long getStock() {
    return stock;
  }

  private long stock;
  
  private final double commission;
  
  private List<Deal> transactions;
  
  public List<Deal> getTransactions() {
    return transactions;
  }

  public int getPeriodAfterLastDealExclWeekends(Date date) {
    Deal transaction = getLastTransaction();
    if(transaction == null) return 0;
    
    DateTime begin = new DateTime(transaction.getDate());
    DateTime end = new DateTime(date);
    
    int count = 0;
    while(begin.compareTo(end) <= 0){
      int dayOfWeek = begin.getDayOfWeek();
      if(dayOfWeek != DateTimeConstants.SATURDAY && dayOfWeek != DateTimeConstants.SUNDAY){
        count++;
      }
      begin = begin.plusDays(1);
    }
    
    return count;
  }
  
  public Accountant(double initialCash, double commission){
    this.initialCash = initialCash;
    this.commission = commission;
    this.cash = initialCash;
    this.stock = 0;
    this.transactions = new ArrayList<Deal>();
  }
  
  public boolean buyAll(Double price, Date date, Action r){
    double realUnitPrice = price * (1.0 + commission);
    long maxStockAllowed  = BigDecimal.valueOf(cash).divide(BigDecimal.valueOf(realUnitPrice),0,RoundingMode.HALF_UP).longValue();
    
    long checkMaxStockAllowed  = (long) (maxStockAllowed * realUnitPrice);
    if(checkMaxStockAllowed > cash) maxStockAllowed -= 1;

    if(maxStockAllowed == 0){
      //if(log.isDebugEnabled())
        //log.debug("Can't buy as Cash:" + cash  + " vs Price " + price + ".");
      return false;
    }
    
    double cashSpentWoCom = maxStockAllowed * price;
    double commissionAmt = cashSpentWoCom * commission;

    add(new Deal(maxStockAllowed, price, commissionAmt,
        date,r));

    cash -= (cashSpentWoCom + commissionAmt);
    stock += maxStockAllowed;

    return true;
  }
  
  public boolean sellAll(Double price, Date date, Action r){
    if(stock == 0){
      //if(log.isDebugEnabled())
        //log.debug("No stock to sell.");
      return false;
    }

    double cashReceived = stock * price;
    double commissionAmt = cashReceived * commission;

    add(new Deal(stock, price, commissionAmt,
        date,r));

    stock = 0;
    cash += (cashReceived - commissionAmt);
    
    return true;
  }
    
  private void add(Deal t){
    this.transactions.add(t);    
  }
  
  //public int size(){
  //  return this.transactions.size();
  //}
  
  public double getAbsolutePnl(double price){
    double numerator = cash + (stock * price) - this.initialCash;
    return BigDecimal.valueOf(numerator).
        divide(BigDecimal.valueOf(this.initialCash),2,RoundingMode.HALF_EVEN).doubleValue();
  }
  
  public boolean hasStock(){
    return this.stock > 0;
  }
  
  private Deal getLastTransaction(){
    int size = this.transactions.size();
    if(size == 0) return null;
    return this.transactions.get(size - 1); 
  }
  
  public double getPnlSinceLastTransaction(double price){
    Deal transaction = getLastTransaction();
    if(transaction == null) return 0.0;
    double lastPrice = transaction.getPrice();
    double pnl = BigDecimal.valueOf(price -  lastPrice).divide(BigDecimal.valueOf(lastPrice),2,RoundingMode.HALF_EVEN).doubleValue();
    return pnl;
  }
  
  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder("Num:" + transactions.size()  + " --> ");
    for (Deal d : transactions) {
      sb.append(d).append(",");
    }
    
    return sb.toString();
  }
  
  

}
