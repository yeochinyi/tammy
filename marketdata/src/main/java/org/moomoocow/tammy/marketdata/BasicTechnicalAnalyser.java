package org.moomoocow.tammy.marketdata;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;


public class BasicTechnicalAnalyser {

  public enum Side {
    B, S, H
  };
  
  public static final void main(String args[]) {
    BasicTechnicalAnalyser bta = new BasicTechnicalAnalyser();
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

  public void simulate(Stock s){
    
    double cash = 100;
    double stock = 0;

    int trans = 0;

    Side ops = Side.H;

    double firstMid = 0;
    double mid = 0;
    long totalVol = 0;
    int count = 0;
    double avgVol = 0;

    
    StockHistoricalData prevRS = null;
    
    for (StockHistoricalData h : s.getSortedDailyData()) {
      
      System.out.println(h);
      
      totalVol += h.getVol();      
      count++;      
      avgVol = totalVol / count;
            
      mid = h.getMid();
      if(firstMid == 0) firstMid = mid;
      
        
      
      if(prevRS != null){                
        switch(ops){
          case B:
            stock = cash / mid;      
            cash = 0;
            System.out.println(" B@" + mid  + "->" + stock + " units");
          trans++;
            break;
          case S:
            cash = stock * mid;
            stock = 0;
            trans++;
            System.out.println("S@" + mid  + "->" + cash + " dollars");
            break;
          case H: //do nothing
            System.out.println("Hold");
            break;
        }
        
        //Buy on next
        if(prevRS.getClose() < h.getOpen() && h.getVol() > avgVol * 1.05 && cash > 0) {
          ops = Side.B;      
        } //Sell on next
        else if(prevRS.getClose() > h.getOpen() && h.getVol() > avgVol  * 1.05  && stock > 0) {
          ops = Side.S;
        }
        else{
           ops = Side.H;
        }          
      }
      
      prevRS = h;
    }
    
    System.out.println("BS PL -> " + (int) (cash + (stock * mid) - 100 ) + ", trans=" + trans  + ", Cash=" + (int) cash + ", stock =" + (int) stock + "(cash="  + (int) (stock * mid) +")<BR>"); 
    System.out.println("BH PL -> " + (int) ((mid - firstMid) / firstMid * 100  )  + ", firstMid=" + firstMid + ",mid=" + mid + "<BR>");
  }
}
