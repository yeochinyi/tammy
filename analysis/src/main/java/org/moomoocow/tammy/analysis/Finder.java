package org.moomoocow.tammy.analysis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jdo.PersistenceManager;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.signal.BuyAtFirst;
import org.moomoocow.tammy.analysis.signal.EnhancedProtective;
import org.moomoocow.tammy.analysis.signal.MACrosser;
import org.moomoocow.tammy.analysis.signal.MinPeriod;
import org.moomoocow.tammy.analysis.signal.Protective;
import org.moomoocow.tammy.analysis.signal.Signal;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockGroup;
import org.moomoocow.tammy.model.util.Helper;

/**
 * Set the stage with historical market prices to be executed
 * 
 * @author yeocae
 * 
 */
public class Finder {
  
  private static final Logger log = Logger.getLogger(Finder.class);

  private Map<Stock,Simulator> simMap;
  
  private final static DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
  
  //private final double minAnnualizedReturn = 0.1;

  @SuppressWarnings("unchecked")
  public static final void main(String args[]) throws ParseException {
    
    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
    List<StockGroup> s = (List<StockGroup>) pm.newQuery(StockGroup.class, "this.code == '" + args[0] + "'").execute();

    Date d = args.length > 1 ? df.parse(args[1]) : null ;
    
    Finder finder = new Finder(s.get(0).getStocks(), d);
    
    Stock stock = finder.execute(new BuyAtFirst(
        MinPeriod.getRandom(EnhancedProtective.getRandom(Protective.getRandomStopLoss(MACrosser.getRandomEMA(null))))));
    
    System.out.println("");
  }

  @SuppressWarnings("unchecked")
  public Finder(List<Stock> stocks, Date date) {
    this.simMap = new HashMap<Stock,Simulator>();
    
    for (Stock stock : stocks) {
      Simulator sim = new Simulator(stock,new GregorianCalendar(2013,5,10).getTime());
      sim.executeDefaultBenchMark();
      this.simMap.put(stock,sim);
    }
  }
  
  public Stock execute(Signal signal) {
    
    double best = 0.1;
    Stock bestStock = null;
    
    for(Entry<Stock, Simulator> e : this.simMap.entrySet()){
      Simulator sim = e.getValue();
      MtmManager mm = sim.execute(signal);
      double pnl = mm.getTotalAnnualizedPnl();
      if(pnl > best){
        best = pnl;
        bestStock = e.getKey();
      }
    }
    
    return bestStock;
  }

}
