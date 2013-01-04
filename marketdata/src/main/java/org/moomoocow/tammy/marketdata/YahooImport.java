package org.moomoocow.tammy.marketdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

  public static void main(String[] args) throws IOException {
    YahooImport y = new YahooImport();
    y.importDailyData();

  }

  public void importDailyData() throws IOException {
    PersistenceManagerFactory pmf = JDOHelper
        .getPersistenceManagerFactory("datanucleus.properties");
    PersistenceManager pm = pmf.getPersistenceManager();

    Query q = pm.newQuery(Stock.class, "this.code == 'Z74.SI'");

    for (Stock s : (List<Stock>) q.execute()) {
      System.out.println(s.getDescription());

      //Date startDate = new Date(0);
      Calendar startDate = new GregorianCalendar();
      startDate.add(Calendar.DATE, -7);
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
      
      //reading heading
      r.readLine();
      
      DailyData lastDD;
      
      Double lastCloseDivAdj;
      
      while((text = r.readLine()) != null){
        System.out.println(text);
        
        String[] t = text.split(",");
                        
        Date d = df.parse(t[0]);
        Double open = Double.valueOf(t[1]);
        Double high = Double.valueOf(t[2]);
        Double low = Double.valueOf(t[3]);
        Double close = Double.valueOf(t[4]);
        Long vol = Long.valueOf(t[5]);
        Double adj = Double.valueOf(t[6]);
        
        Double closeDivAdj = close/adj;
                
        lastDD = new DailyData(d,s,open,high,low,close,vol,m);
        
        
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
