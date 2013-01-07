package org.moomoocow.tammy.marketdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public class YahooImport {

  final private static int[] CALENDAR_FIELDS = { Calendar.MONTH, Calendar.DATE,
      Calendar.YEAR };
  final private static String DATE_START_PARAMS = "abc";
  final private static String DATE_END_PARAMS = "def";
  
  final private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

  public static void main(String[] args) throws IOException, ParseException {
    YahooImport y = new YahooImport();
    y.importHistoricalData();

  }

  @SuppressWarnings("unchecked")
  public void importHistoricalData() throws IOException, ParseException {
    PersistenceManagerFactory pmf = JDOHelper
        .getPersistenceManagerFactory("datanucleus.properties");
    PersistenceManager pm = pmf.getPersistenceManager();

    Query q = pm.newQuery(Stock.class, "this.code == 'Z74.SI'");        
    //Query q = pm.newQuery(Stock.class, "this.active == true && ");

    for (Stock s : (List<Stock>) q.execute()) {
      System.out.println(s.getDescription());
                 
      Calendar startDate = new GregorianCalendar();
      
      HistoricalData lastDD = s.getMaxDatedDailyData();
            
      startDate.setTime(lastDD == null ? new Date(0) : lastDD.getDate());

      Calendar endDate = new GregorianCalendar();

      String datesParam = constructDateParams(DATE_START_PARAMS, startDate) +
      constructDateParams(DATE_END_PARAMS, endDate);

      String url = "http://ichart.finance.yahoo.com/table.csv?" + datesParam + "g=d&s=" + s.getCode();
      
      System.out.println("URL=" + url);
      
      BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
      
      String text;
      
      /*
      File format
      Date,Open,High,Low,Close,Volume,Adj Close
      2013-01-03,3.34,3.35,3.33,3.35,13666000,3.35
      2013-01-02,3.32,3.34,3.32,3.33,15587000,3.33
      2013-01-01,3.30,3.30,3.30,3.30,000,3.30
      2012-12-31,3.30,3.32,3.29,3.30,18787000,3.30
      2012-12-28,3.31,3.33,3.31,3.33,11723000,3.33
      */
      
      //Read heading
      r.readLine();
      
      HistoricalData prevDD = null;
      
      Date lastHistoricalDate = null;
            
      int filecount = 0;
      boolean hasMultipler = false;
      
      while((text = r.readLine()) != null){
        System.out.println(text);
        
        String[] t = text.split(",");
                        
        Date d = df.parse(t[0]);
        
        if(filecount == 0){          
          if(lastDD != null && d.equals(lastDD.getDate())){
            System.out.println("No new data as first date is equal db max.");
            break;
          }
          else{
            lastHistoricalDate = d;
          }
        }
        
        Double open = Double.valueOf(t[1]);
        Double high = Double.valueOf(t[2]);
        Double low = Double.valueOf(t[3]);
        Double close = Double.valueOf(t[4]);
        Long vol = Long.valueOf(t[5]);
        Double adjustedClose = Double.valueOf(t[6]);        
                
        HistoricalData currDD = new HistoricalData(d,s,open,high,low,close,vol,adjustedClose);
        
        //Date  Open  High  Low Close Volume,  Adj Close,   C / Adj C, T+1 / T, Filter >, ABS(x-1)          
        //5/12/2004 26.81 27.18 25.76 27.08 26108100  27.08 1.0000  2.000373692 2.000373692   1.00037
        //5/11/2004 52.35 54  52.18 53.53 34553400  26.76   0.4999  0.999813189               0.00019
       
        if(prevDD != null){          
          Double multipler =  prevDD.getCloseMultipler() / currDD.getCloseMultipler();
          if(Math.abs(multipler - 1) > 0.03){
            prevDD.setMultipler(multipler);
            hasMultipler = true;
          }
          
          pm.makePersistent(prevDD);
          
        }
        
        filecount++;
        
        prevDD = currDD;        
      }
      
      //Save the last DD
      if(lastDD == null)
        pm.makePersistent(prevDD);
      
      if(filecount != 0){
        if(hasMultipler) s.setPriceMultiplied(true);
        s.addTotalChildren(filecount - 1);
        if(lastHistoricalDate != null) s.setLastHistoricalDate(lastHistoricalDate);
        pm.makePersistent(s);
      }
    }
    
    
  }

  public String constructDateParams(String names, Calendar c) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 3; i++) {
      sb.append(names.charAt(i)).append("=").append(c.get(CALENDAR_FIELDS[i]))
          .append("&");
    }

    return sb.toString();

  }

}
