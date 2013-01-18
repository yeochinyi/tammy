package org.moomoocow.tammy.marketdata;

import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import static Price.*;

public class StockSimulator {

  public enum Side {
    B, S, H
  };

  public static final void main(String args[]) {
    StockSimulator bta = new StockSimulator();
    bta.testStocks();
  }

  @SuppressWarnings("unchecked")
  public void testStocks() {
    PersistenceManagerFactory pmf = JDOHelper
        .getPersistenceManagerFactory("datanucleus.properties");
    PersistenceManager pm = pmf.getPersistenceManager();

    Query q = pm.newQuery(Stock.class,
    // "this.active == true || this.active == null");
        "this.code == 'Z74.SI'");

    for (Stock stock : (List<Stock>) q.execute()) {
      simulate(stock);
    }
  }

  public void simulate(Stock s) {

    double cash = 100;
    double stock = 0;

    int trans = 0;

    Side ops = Side.H;
    Double accumulatedMultipler = null;

    MovingAverager ma = new MovingAverager();

    List<StockHistoricalData> data = s.getSortedDailyData();

    for (int i = 0; i < data.size(); i++) {

      StockHistoricalData h = data.get(i);
      System.out.println(h);

      if (i == 0)
        continue;
      
      StockHistoricalData p = data.get(i-1);
      accumulatedMultipler = h.accumlateMultiplers(accumulatedMultipler);
      
      //double mid = h.getAccXMid();
      //double open = h.getAccXOpen();
      //double close = h.getAccXClose();
      //double high = h.getAccXHigh();
      //double low = h.getAccXLow();

      switch (ops) {
      case B:
        stock = cash / h.getAccX(MID);
        cash = 0;
        System.out.println(" B@" + mid + "->" + stock + " units");
        trans++;
        break;
      case S:
        cash = stock * mid;
        stock = 0;
        trans++;
        System.out.println("S@" + mid + "->" + cash + " dollars");
        break;
      case H: // do nothing
        System.out.println("Hold");
        break;

      // Buy on next
      if (p.getAccXClose() < open && cash > 0) {
        ops = Side.B;
      } // Sell on next
      else if (p.getAccXClose() > h.getOpen() && stock > 0) {
        ops = Side.S;
      } else {
        ops = Side.H;
      }
    }

  }

  System.out.println("BS PL -> " + (int) (cash + (stock * mid) - 100)
      + ", trans=" + trans + ", Cash=" + (int) cash + ", stock =" + (int) stock
      + "(cash=" + (int) (stock * mid) + ")<BR>");
  System.out.println("BH PL -> " + (int) ((mid - firstMid) / firstMid * 100)
      + ", firstMid=" + firstMid + ",mid=" + mid + "<BR>");
}
}