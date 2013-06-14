package org.moomoocow.tammy.analysis;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.Deal.Action;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.StockHistoricalData.Price;

public class ProtectiveSignal extends AbstractChainedSignal {
  
  private static Logger log = Logger.getLogger(ProtectiveSignal.class);
  
  private final double takeProfit;
  private final double stopLoss; 
  private final int minHoldingPeriod;
  
  private int takeProfitCounter = 0;
  private int stopLossCounter = 0;
  private int minHoldingPeriodCounter = 0;
  
  public ProtectiveSignal(Signal signal, double takeProfit,
      double stopLoss, int minHoldingPeriod) {
    super(signal);
    this.takeProfit = takeProfit;
    this.stopLoss = stopLoss;
    this.minHoldingPeriod = minHoldingPeriod;
  }

  public Double getTakeProfit() {
    return takeProfit;
  }

  public Double getStopLoss() {
    return stopLoss;
  }

  public Integer getMinHoldingPeriod() {
    return minHoldingPeriod;
  }
  


  @Override
  public String toString() {
    return "ProtectiveSignal [takeProfit=" + takeProfit + ", stopLoss="
        + stopLoss + ", minHoldingPeriod=" + minHoldingPeriod
        + ", takeProfitCounter=" + takeProfitCounter + ", stopLossCounter="
        + stopLossCounter + ", minHoldingPeriodCounter="
        + minHoldingPeriodCounter + "] chaining " + 
        (this.chainSignal != null ? this.chainSignal.toString() : "");
  }

  @Override
  public Action analyze(Date date, double open, double close, double high, double low, double mid, Accountant tm) {
  
    Action chainedSignalAction = this.chainSignal != null ? this.chainSignal.analyze(date,open,close,high,low,mid,tm) : null;
    
    if(tm.hasStock()){
      
      if(chainedSignalAction != null && minHoldingPeriod > 0 && tm.getPeriodAfterLastDealExclWeekends(date) <= minHoldingPeriod){
        minHoldingPeriodCounter++;
        //if(log.isDebugEnabled())
          //log.info("Hold signal due to minHoldingPeriod of " + minHoldingPeriod);
        return null;
      }
      
      double pnl = tm.getPnlSinceLastTransaction(mid);
      if(takeProfit >  0.0  &&  pnl >= takeProfit){
        takeProfitCounter++;
        return Deal.Action.TAKEPROFIT;
      }
      else if(stopLoss >  0.0 && (pnl * -1) >= stopLoss){
        stopLossCounter++;
        return Deal.Action.STOPLOSS;
      }
    }
  
    return chainedSignalAction;
  }

  @Override
  public Map<String, SortedMap<Date, Double>> getDisplayPoints() {
    return null;
  }
  

  
  

}
