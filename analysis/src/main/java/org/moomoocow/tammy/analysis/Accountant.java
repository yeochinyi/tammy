package org.moomoocow.tammy.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class Accountant {

  @SuppressWarnings("unused")
  private static Logger log = Logger.getLogger(Accountant.class);

  //private final double initialCash;

  private double cash;
   
  private long stock;

  private final double commission;

  private List<Deal> deals;
   
  public Integer getPeriodAfterLastDealExclWeekends(Date date) {
    Deal transaction = getLastDeal();
    if (transaction == null)
      return null;

    DateTime begin = new DateTime(transaction.getDate());
    DateTime end = new DateTime(date);

    int count = 0;
    while (begin.compareTo(end) <= 0) {
      int dayOfWeek = begin.getDayOfWeek();
      if (dayOfWeek != DateTimeConstants.SATURDAY
          && dayOfWeek != DateTimeConstants.SUNDAY) {
        count++;
      }
      begin = begin.plusDays(1);
    }

    return count;
  }

  /**
   * @param initialCash Real Cash in dollars
   * @param commission percent where 1.0 is 100%
   */
  public Accountant(double initialCash, double commission) {
    //this.initialCash = initialCash;
    this.commission = commission;
    this.cash = initialCash;
    this.stock = 0;
    this.deals = new ArrayList<Deal>();

  }
  
  public Deal transact(Double price, Date date, Action r){
    if(r.isBuy()){
      return buyAll(price, date, r);
    }
    else
      return sellAll(price, date, r);
  }
  
  private Deal buyAll(Double price, Date date, Action r) {
    double realUnitPrice = price * (1.0 + commission);
    long maxStockAllowed = MathHelper.divideRoundUp(cash, realUnitPrice);
    long checkMaxStockAllowed = (long) (maxStockAllowed * realUnitPrice);
    if (checkMaxStockAllowed > cash)
      maxStockAllowed -= 1;

    if (maxStockAllowed == 0) {
      // if(log.isDebugEnabled())
      // log.debug("Can't buy as Cash:" + cash + " vs Price " + price + ".");
      return null;
    }

    double cashSpentWoCom = maxStockAllowed * price;
    double commissionAmt = cashSpentWoCom * commission;

    Deal d = new Deal(maxStockAllowed, price, commissionAmt, date, r);
    add(d);

    cash -= (cashSpentWoCom + commissionAmt);
    stock += maxStockAllowed;
    
    return d;
  }

  private Deal sellAll(Double price, Date date, Action r) {
    if (stock == 0) {
      // if(log.isDebugEnabled())
      // log.debug("No stock to sell.");
      return null;
    }

    double cashReceived = stock * price;
    double commissionAmt = cashReceived * commission;

    Deal d = new Deal(stock, price, commissionAmt, date, r);
    add(d);

    stock = 0;
    cash += (cashReceived - commissionAmt);

    return d;
  }

  
  private void add(Deal t) {
    this.deals.add(t);
  }

  /**
   * @return real pnl i.e -0.2 if Loss of 20% or 0.1 if gain of 10%
   */
  
  /*public double getRealPnl() {
    double numerator = cash + (stock * this.lastMTMPrice)
        * (1.0 - commission) - this.initialCash;
    return MathHelper.divide(numerator, this.initialCash);
  }*/

  public boolean hasStock() {
    return this.stock > 0;
  }

  
  private Deal getLastDeal() {
    int size = this.deals.size();
    if (size == 0)
      return null;
    return this.deals.get(size - 1);
  }

  /**
   * @return real pnl based on last BUY i.e -0.2 if Loss of 20% or 0.1 if gain of 10%
   */  
  public double getRealPnlSinceLastTran(double price) { 
    Deal transaction = getLastDeal();
    if (transaction == null)
      return 0.0;
    if(!transaction.isBuy()) throw new RuntimeException("getRealPnlSinceLastTran last tran is not a buy.");
    double lastPrice = transaction.getUnitPrice();
    double pnl = MathHelper.divide(price - lastPrice, lastPrice);
    return pnl;
  }

  public double getCash() {
    return cash;
  }

  public long getStock() {
    return stock;
  }

  @Override
  public String toString() {

    Map<Action, Integer> m = new HashMap<Action, Integer>();

    StringBuilder sb = new StringBuilder("Num:" + deals.size() + " --> ");
    for (Deal d : deals) {
      Integer i = m.get(d.getReason());
      m.put(d.getReason(), i == null ? 1 : ++i);
      sb.append(d).append(",");
    }

    return sb.toString() + ",Actions:" + m.toString();
  }
}
