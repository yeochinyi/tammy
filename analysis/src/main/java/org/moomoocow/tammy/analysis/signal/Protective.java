package org.moomoocow.tammy.analysis.signal;

import java.util.Date;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Deal;
import org.moomoocow.tammy.analysis.Deal.Action;

public class Protective extends AbstractChainedSignal {

  public static Protective getRandomTakeProfit(Signal s){
    double greaterThan = Math.random() * 0.35 + 0.05;
    return new Protective(greaterThan,s);
  }
  
  public static Protective getRandomStopLoss(Signal s){
    double greaterThan = Math.random() * 0.1 + 0.05;
    return new Protective(greaterThan,false,s);
  }

  
  @SuppressWarnings("unused")
  private static Logger log = Logger.getLogger(Protective.class);

  private final double greaterThan;
  private final boolean isTakeProfit;

  public Protective(double greaterThan, Signal signal) {
    this(greaterThan, true, signal);
  }

  public Protective(double greaterThan,
      boolean isTakeProfit, Signal signal) {
    super(signal);
    this.greaterThan = greaterThan;
    this.isTakeProfit = isTakeProfit;
  }

  @Override
  public Action override(Action a, Date date, double open, double close,
      double high, double low, double mid, long vol, Accountant tm) {

    if (tm.hasStock()) {
      double pnl = tm.getPnlSinceLastTransaction(mid);
      double m = isTakeProfit ? 1.0 : -1.0;
      if (greaterThan > 0.0 && (pnl * m) >= greaterThan) {
        return isTakeProfit ? Deal.Action.TAKEPROFIT : Deal.Action.STOPLOSS;
      }
    }

    return a;
  }

  @Override
  public String chainedToString() {
    return "ProtectiveSignal [greaterThan=" + greaterThan
        + ", isTakeProfit=" + isTakeProfit + "]";
  }

}
