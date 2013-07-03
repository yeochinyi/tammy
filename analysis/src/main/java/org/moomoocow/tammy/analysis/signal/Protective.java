package org.moomoocow.tammy.analysis.signal;

import java.util.Date;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;
import org.moomoocow.tammy.analysis.Action.ActionType;
import org.moomoocow.tammy.analysis.MathHelper;


public class Protective extends AbstractChainedSignal {

  public static Protective getRandomTakeProfit(AbstractChainedSignal s){
    double greaterThan = MathHelper.randomDouble(0.05, 0.3);       
    return new Protective(greaterThan,s);
  }
  
  public static Protective getRandomStopLoss(AbstractChainedSignal s){
    double greaterThan = MathHelper.randomDouble(0.03, 0.07);
    return new Protective(greaterThan,false,s);
  }

  
  @SuppressWarnings("unused")
  private static Logger log = Logger.getLogger(Protective.class);

  private final double greaterThan;
  private final boolean isTakeProfit;

  public Protective(double greaterThan, AbstractChainedSignal signal) {
    this(greaterThan, true, signal);
  }

  public Protective(double greaterThan,
      boolean isTakeProfit, AbstractChainedSignal signal) {
    super(signal);
    this.greaterThan = greaterThan;
    this.isTakeProfit = isTakeProfit;
  }

  @Override
  public Action override(Action a, Date date, double open, double close,
      double high, double low, double mid, long vol, Accountant tm) {

    if (tm != null && tm.hasStock()) {
      double pnl = tm.getRealPnlSinceLastTran(mid);
      double m = isTakeProfit ? 1.0 : -1.0;
      if (greaterThan > 0.0 && (pnl * m) >= greaterThan) {
    	Action act = new Action(isTakeProfit ?  ActionType.TAKEPROFIT : ActionType.STOPLOSS, date, mid);
    	this.actions.put("P-" + act, act);
        return  act;
      }
    }

    return a;
  }

  @Override
  public String chainedToString() {
    return "ProtectiveSignal [greaterThan=" + greaterThan
        + ", isTakeProfit=" + isTakeProfit + "]=>" + super.chainedToString();
  }
  

}
