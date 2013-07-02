package org.moomoocow.tammy.analysis;

import static org.moomoocow.tammy.model.StockHistoricalData.Price.CLOSE;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.HIGH;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.LOW;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.MID;
import static org.moomoocow.tammy.model.StockHistoricalData.Price.OPEN;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.analysis.signal.BuyAtFirst;
import org.moomoocow.tammy.analysis.signal.EnhancedProtective;
import org.moomoocow.tammy.analysis.signal.MACrosser;
import org.moomoocow.tammy.analysis.signal.MinPeriod;
import org.moomoocow.tammy.analysis.signal.Protective;
import org.moomoocow.tammy.analysis.signal.Signal;
import org.moomoocow.tammy.model.ModelHelper;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.StockHistoricalData;
import org.moomoocow.tammy.model.util.Helper;

/**
 * Set the stage with historical market prices to be executed
 * 
 * @author yeocae
 * 
 */
public class Simulator {
  
  private static final Logger log = Logger.getLogger(Simulator.class);

  private List<StockHistoricalData> sortedDailyData;
  
  private final static DateFormat df = new SimpleDateFormat("yyyy-mm-dd");

  private Map<Signal, Accountant> accountantMap;

  private Map<Double, List<Signal>> pnlMap;
  
  private Map<Signal, MtmManager> mtmMap;
  
  private final Stock stock;
  
  private Double benchmark; 

  @SuppressWarnings("unchecked")
  public static final void main(String args[]) throws ParseException {

	  System.out.println("TEST123");
	  
	  
    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
    Query q = pm.newQuery(Stock.class, "this.code == '" + "FAS" + "'");
    List<Stock> s = (List<Stock>) q.execute();

    //Date d = args.length > 1 ? df.parse(args[1]) : null ;
    Date d = df.parse("2010-01-01");
    
    Simulator sim = new Simulator(s.get(0), d);
    
    sim.executeDefaultBenchMark();
    
    for(int i=0; i < 2000; i++){
    //int i = 0;
    //while(sim.accountantMap.size() < 2){

    	//int[] mas = { 13, 39 };
    	//int[] mas = { 4, 69 };
        //sim.execute(new BuyAtFirst(new EnhancedProtective(0.22, 0.1,new Protective(0.04,false,new MACrosser(mas, true)))));
    //sim.execute(new BuyAtFirst(new MinPeriod(12,new EnhancedProtective(0.09, 0.04,new Protective(0.03,false,new MACrosser(mas, true))))));
      sim.execute(new BuyAtFirst(MinPeriod.getRandom(EnhancedProtective.getRandom(Protective.getRandomStopLoss(MACrosser.getRandomEMA(null))))));
      //sim.execute(new BuyAtFirst(EnhancedProtective.getRandom(Protective.getRandomStopLoss(MACrosser.getRandomEMA(null)))));
      i++;
      if(i % 1000 == 0) System.out.println("Counting " + i);
    }
    
    //int[] mas = { 21, 28 };
    //sim.execute(new ProtectiveSignal(new MAHLSignal(mas, true),0.08 , 0.08, 30));
    
    /*
    int step = 7;
    int periods = 5;

    for (int x = step; x <= periods * step; x += step) {
      for (int y = x + step; y <= periods * step; y += step) {
        int[] mas = { x, y };
        //sim.execute(new MAHLSignal(mas, true));
        //sim.execute(new MAHLSignal(mas, false));
        for(int x1 = 10; x1 <= 20 ; x1 += 5){
          for(int y1 = 0; y1 <= 20 ; y1 += 5){
            for(int z1 = 0; z1 <= 10 ; z1 += 2){
              double xD = ((double) x1) / 100.0;
              double yD = ((double) y1) / 100.0;
              sim.execute(new BuyAtFirst(new MinPeriod(z1,new EnhancedProtective(xD, 0.05,new Protective(yD,false,new MovingAverage(mas, true))))));
              //sim.execute(new ProtectiveSignal(new MAHLSignal(mas, false),xD, yD, z1));
            }
          }
        }
      }
    }
    

    /*
    for(int x = 5; x <= 30 ; x += 5){
      for(int y = 5; y <= 30 ; y += 5){
        for(int z = 0; z <= 5 ; z ++){
          double xD = ((double) x) / 100.0;
          double yD = ((double) y) / 100.0;
          sim.execute(new ProtectiveSignal(new BuyAndHoldSignal(),xD , yD, z));
        }
      }
    }*/
    
System.out.println("Finished execute");
    
    for (Entry<Double, List<Signal>> e : sim.pnlMap.entrySet()) {
      System.out.println(e.getKey() + "-->");
      for (Signal st : e.getValue()) {
    	  MtmManager mtmManager = sim.mtmMap.get(st);
        System.out.println("  " + st.toString() + "->"
            + mtmManager);
      }
    }
  }

  public Simulator(Stock s, Date date) {

    this.stock = s;
    this.accountantMap = new HashMap<Signal, Accountant>();
    this.mtmMap = new HashMap<Signal, MtmManager>();
    this.pnlMap = new TreeMap<Double, List<Signal>>(new Comparator<Double>() {
      @Override
      public int compare(Double o1, Double o2) {
        return o1.compareTo(o2) * -1;
      }
    });

    this.sortedDailyData = ModelHelper.prepareDailyData(s,date);
    log.info("From Date=" + this.sortedDailyData.get(0).getDate() 
        + " ~ " + this.sortedDailyData.get(this.sortedDailyData.size() - 1).getDate() + " looping " 
        + this.sortedDailyData.size() + " recs.");
  }


  private int getMonth(Date d){
    Calendar c = new GregorianCalendar();
    c.setTime(d);
    return c.get(Calendar.MONTH);

  }
  
  public double executeDefaultBenchMark(){
    this.benchmark = execute(new BuyAtFirst(null)).getTotalAnnualizedPnl();
    return this.benchmark;
  }

  public MtmManager execute(Signal signal) {

    Accountant accountant = new Accountant(10000.0,0.004);
    MtmManager mm = null;
        
    boolean firstTrans = true;
    Action r = null;

    double mid = 0.0;
    double open = 0.0;
    double close = 0.0;
    double high = 0.0;
    double low = 0.0;

    Date currentDate = null;
    int lastMonth = -1;
    boolean hasFinalMtmCommit = true;
    
    for (StockHistoricalData h : this.sortedDailyData) {
    	currentDate = h.getDate();
    	
      if (firstTrans) {
        firstTrans = false;
        lastMonth = getMonth(currentDate);
        mm = new MtmManager(10000.0, currentDate);
        continue;
      }

      mid = h.getAccX(MID);
      open = h.getAccX(OPEN);
      close = h.getAccX(CLOSE);
      high = h.getAccX(HIGH);
      low = h.getAccX(LOW);
      
      if (r != null){
        Deal d = accountant.transact(mid, currentDate, r);
        if(d != null)
          mm.add(d);
      }

      r = signal.analyze(h.getDate(),open,close,high,low,mid,h.getVol(), accountant);
      
      int currentMonth = getMonth(currentDate);
      if(currentMonth != lastMonth){
        mm.commitMarkToMarket(currentDate, mid);
        lastMonth = currentMonth;
        hasFinalMtmCommit = false;
      }
      else{
        hasFinalMtmCommit = true;
      }
    }

    if(hasFinalMtmCommit)
        mm.commitMarkToMarket(currentDate, mid);
    Double pnl = mm.getTotalAnnualizedPnl();
    
    if((this.benchmark == null || pnl > this.benchmark)){

      if(signal.isTriggeredAtLeast(1)){
          this.accountantMap.put(signal, accountant);
          this.mtmMap.put(signal, mm); 
          
          List<Signal> s = pnlMap.get(pnl);
          if (s == null) {
            s = new ArrayList<Signal>();
            pnlMap.put(pnl, s);
          }
          s.add(signal);    	  
      }      
    }
    return mm;
  }
  
  public List<StockHistoricalData> getSortedDailyData() {
    return sortedDailyData;
  }

  public Stock getStock() {
    return stock;
  }

}
