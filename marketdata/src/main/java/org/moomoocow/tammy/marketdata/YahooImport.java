package org.moomoocow.tammy.marketdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
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
      
      while((text = r.readLine()) != null){
        System.out.println(text);
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
