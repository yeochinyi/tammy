package org.moomoocow.tammy.marketdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.mdimension.jchronic.Chronic;

public class YahooImport {

  final private static int[] CALENDAR_FIELDS = { Calendar.MONTH, Calendar.DATE,
      Calendar.YEAR };
  final private static String DATE_START_PARAMS = "abc";
  final private static String DATE_END_PARAMS = "def";

  final private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

  // ['K':1,'M':2,'B':3,'T':4]
  final private Map<String, Integer> digitMultiplerMap = new HashMap<String, Integer>();

  private PersistenceManager pm;

  public static void main(String[] args) throws IOException, ParseException {
    YahooImport y = new YahooImport();
    y.importData(true);

  }

  public YahooImport() {
    PersistenceManagerFactory pmf = JDOHelper
        .getPersistenceManagerFactory("datanucleus.properties");
    pm = pmf.getPersistenceManager();

    // ['K':1,'M':2,'B':3,'T':4]
    digitMultiplerMap.put("K", 1);
    digitMultiplerMap.put("M", 2);
    digitMultiplerMap.put("B", 3);
    digitMultiplerMap.put("T", 4);

  }

  @SuppressWarnings("unchecked")
  public void importData(boolean importHistoricalData) throws IOException {
    // Query q = pm.newQuery(Stock.class, "this.code == 'Z74.SI'");
    Query q = pm.newQuery(Stock.class,
        "this.active == true || this.active == null");

    List<Stock> stocks = (List<Stock>) q.execute();

    // System.out.println("ImportAll for " + stocks.size() + " stocks.");

    if (importHistoricalData) {
      for (Stock s : stocks) {
        importHistoricalData(s);
      }
    } else {

      List<Stock> batch = new ArrayList<Stock>();

      for (Stock s : stocks) {
        batch.add(s);
        if (batch.size() == 100) {
          importSnapshotData(batch);
          batch.clear();
        }
      }

      // Import rest
      if (batch.size() > 0)
        importSnapshotData(batch);
    }

  }

  private void importSnapshotData(List<Stock> stocks) throws IOException {

    List<Field> fields = new ArrayList<Field>();

    List<Field> specialDelimitedFields = new ArrayList<Field>();

    StringBuilder colCodes = new StringBuilder();

    StringBuilder specialDelimitedColCodes = new StringBuilder();

    for (Field f : StockSnapshotData.class.getDeclaredFields()) {
      // System.out.println("Field=" + f.getName());
      // if(f.isAnnotationPresent(YahooData.class)){

      YahooData annotation = f.getAnnotation(YahooData.class);

      if (annotation != null) {
        String key = annotation.key();
        boolean mayContainCommas = annotation.mayContainCommas();
        boolean alwaysImported = annotation.isAlwaysImported();

        if (alwaysImported || mayContainCommas) {
          specialDelimitedColCodes.append(key).append("n4");
          specialDelimitedFields.add(f);
        }

        if (alwaysImported || !mayContainCommas) {
          colCodes.append(key);
          fields.add(f);
        }
      }
    }

    System.out.println("Total fields=" + fields.size());

    StringBuilder sb = new StringBuilder();

    for (Stock s : stocks) {
      sb.append(s.getCode()).append("+");
    }
    String symbolsString = sb.substring(0, sb.length() - 1);

    Map<String, StockSnapshotData> ssdMap1 = importSnapshotData(symbolsString,
        fields, colCodes.toString(), false);

    Map<String, StockSnapshotData> ssdMap2 = importSnapshotData(symbolsString,
        specialDelimitedFields, specialDelimitedColCodes.toString(), true);

    for (Stock s : stocks) {

      StockSnapshotData ssd1 = ssdMap1.get(s.code);
      StockSnapshotData ssd2 = ssdMap2.get(s.code);

      if (ssd1 != null) {
        StockSnapshotData.copyNonNullFields(ssd1, ssd2);
        ssd1.setStock(s);
      }

      pm.makePersistent(ssd1);
      s.addTotalSnapshotData(1);
      s.setLastSnapshotDate(new Date());
      pm.makePersistent(s);
    }

  }

  private Map<String, StockSnapshotData> importSnapshotData(
      String symbolsString, List<Field> fields, String colCodes,
      boolean useSpecialDelimiter) throws MalformedURLException, IOException {

    Map<String, StockSnapshotData> map = new HashMap<String, StockSnapshotData>();

    String url = "http://download.finance.yahoo.com/d/quotes.csv?e=.csv&s="
        + symbolsString + "&f=" + colCodes;

    System.out.println("URL=" + url);

    BufferedReader r = new BufferedReader(new InputStreamReader(
        new URL(url).openStream()));

    String text;

    // int index = 0;

    while ((text = r.readLine()) != null) {
      System.out.println(text);

      StockSnapshotData ssData = new StockSnapshotData();

      // http://download.finance.yahoo.com/d/quotes.csv?e=.csv&s=Z74.SI&f=snll1d1t1k3cc1p2t7va2it6bb6aa5pomwj5j6k4k5ers7r1qdyf6j1t8e7e8e9r6r7r5b4p6p5j4m3m7m8m4m5m6s1p1c3v1w1g1g4d2g3l2l3n4k1b3b2k2c6v7w4g5g6m2j3r2c8i5xe1ghkj
      // "Z74.SI","SingTel","4:04am - <b>3.37</b>",3.37,"1/10/2013","4:04am",9,000,"+0.06 - +1.81%",+0.06,"+1.81%","&nbsp;=+-===&nbsp;",43301000,18747300,"cn","<a href="http://edit.finance.yahoo.com/ef?.intl=us&.done=http://finance.yahoo.com/d/quotes.csv%3fe%3d.csv%26s%3dZ74.SI%26f%3dsnll1d1t1k3cc1p2t7va2it6bb6aa5pomwj5j6k4k5ers7r1qdyf6j1t8e7e8e9r6r7r5b4p6p5j4m3m7m8m4m5m6s1p1c3v1w1g1g4d2g3l2l3n4k1b3b2k2c6v7w4g5g6m2j3r2c8i5xe1ghkj">Choose&nbsp;Brokerage</a>",3.36,N/A,3.37,N/A,3.31,3.31,"3.30 - 3.38","3.01 - 3.62",+0.36,+11.96%,-0.25,-6.91%,0.00,N/A,N/A,"N/A","19-Dec-06",0.158,4.77,
      // 0,N/A,N/A,0.00,0.00,0.00,N/A,N/A,N/A,0.00,N/A,N/A,0,3.2677,+0.1023,+3.13%,3.2285,+0.1415,+4.38%,-,-,-,-,"- - +1.81%","- - -",-,-,"-",-,-,"-","N/A - <b>3.37</b>",3.36,3.37,"N/A - +1.81%","+0.06",N/A,"N/A - N/A","N/A - N/A",N/A,"N/A - N/A",N/A,N/A,"N/A - N/A","N/A","SES","N/A",3.30,3.38,3.62,3.01

      if (useSpecialDelimiter)
        text = text.replaceAll(",", "");

      String t[] = text.split(useSpecialDelimiter ? "\"" : ",");

      List<String> newT = new ArrayList<String>();

      System.out.println("Old array");
      for (int i = 0; i < t.length; i++) {

        System.out.println(i + "->" + t[i]);

        if (!t[i].trim().equals("")) {
          newT.add(t[i]);
        } else {
          System.out.println("Chopped 1 blank");
        }
      }

      t = newT.toArray(new String[newT.size()]);

      System.out.println("New array");
      for (int i = 0; i < t.length; i++) {
        System.out.println(i + "->" + t[i]);
      }

      System.out.println("Parsing num of fields=" + fields.size());

      for (int i = 0; i < fields.size(); i++) {

        Field f = fields.get(i);
        Class<?> c = f.getType();

        String value = t[useSpecialDelimiter ? i * 2 : i];

        System.out.println("Field=" + f.getName() + ",class=" + c.getName()
            + ",value=" + value);

        value = value.replaceAll("\"", "");
        value = value.replaceAll("&nbsp;", "");
        value = value.trim();

        Object v = null;

        if (value.equals("N/A") || value.equals(""))
          value = null;
        else if (c.equals(Date.class) || c.equals(Time.class)) {
          // value = value.replaceAll("-"," ");
          try {
            v = Chronic.parse(value).getEndCalendar().getTime();
          } catch (Exception e) {
            System.out.println("Can't parse '" + value + "' to Date/Time");
            // e.printStackTrace();

          }
        } else if (c.equals(String.class)) {
          v = value;
        } else if (c.equals(Double.class)) {
          try {
            value = value.replaceAll("%+", "");
            v = Double.parseDouble(value);
          } catch (Exception e) {
            System.out.println("Can't parse '" + value + "' to Double");
            // e.printStackTrace();
          }
        } else if (c.equals(BigInteger.class)) {
          try {
            String lastCh = value.substring(value.length() - 1).toUpperCase();
            Integer m = digitMultiplerMap.get(lastCh);
            if (m != null) {
              Double d1 = Double.parseDouble(value.substring(0,
                  value.length() - 1));
              d1 = d1 * Math.pow(1000, m);
              v = BigInteger.valueOf(d1.longValue());
              // v = m.getRealValue(d1);
            }
          } catch (Exception e) {
            System.out.println("Can't parse '" + value + "' to BigInt");
            // e.printStackTrace();

          }
        } else {
          try {
            v = Long.parseLong(value);
          } catch (NumberFormatException e) {
            System.out.println("Can't parse '" + value + "' to Long");
            // e.printStackTrace();
          }
        }

        if (v != null) {
          // StockSnapshotData d = ssData.get(index);
          try {
            f.set(ssData, v);
          } catch (IllegalArgumentException e) {
            e.printStackTrace();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
      // index++;

      map.put(ssData.getSymbol(), ssData);
    }

    return map;

  }

  public void importHistoricalData(Stock s){

    System.out.println(s.getDescription());

    Calendar startDate = new GregorianCalendar();

    StockHistoricalData lastDD = s.getMaxDatedDailyData();

    startDate.setTime(lastDD == null ? new Date(0) : lastDD.getDate());

    Calendar endDate = new GregorianCalendar();

    String datesParam = constructDateParams(DATE_START_PARAMS, startDate)
        + constructDateParams(DATE_END_PARAMS, endDate);

    String url = "http://ichart.finance.yahoo.com/table.csv?" + datesParam
        + "g=d&s=" + s.getCode();

    System.out.println("URL=" + url);

    BufferedReader r;
    try {
      r = new BufferedReader(new InputStreamReader(
          new URL(url).openStream()));
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return;
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    /*
     * File format Date,Open,High,Low,Close,Volume,Adj Close
     * 2013-01-03,3.34,3.35,3.33,3.35,13666000,3.35
     * 2013-01-02,3.32,3.34,3.32,3.33,15587000,3.33
     * 2013-01-01,3.30,3.30,3.30,3.30,000,3.30
     * 2012-12-31,3.30,3.32,3.29,3.30,18787000,3.30
     * 2012-12-28,3.31,3.33,3.31,3.33,11723000,3.33
     */

    // Read heading
    try {
      r.readLine();
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    }

    StockHistoricalData prevDD = null;

    Date lastHistoricalDate = null;

    int filecount = 0;
    boolean hasMultipler = false;

    while (true) {
      
      String text = null;
      try {
        text = r.readLine();
      } catch (IOException e1) {
        e1.printStackTrace();
        break;
      }
      
      if(text == null) break;
      
      System.out.println(text);

      String[] t = text.split(",");

      Date d = null;
      try {
        d = df.parse(t[0]);
      } catch (ParseException e) {
        e.printStackTrace();
        continue;
      }

      if (filecount == 0) {
        if (lastDD != null && d.equals(lastDD.getDate())) {
          System.out.println("No new data as first date is equal db max.");
          break;
        } else {
          lastHistoricalDate = d;
        }
      }

      Double open = Double.valueOf(t[1]);
      Double high = Double.valueOf(t[2]);
      Double low = Double.valueOf(t[3]);
      Double close = Double.valueOf(t[4]);
      Long vol = Long.valueOf(t[5]);
      Double adjustedClose = Double.valueOf(t[6]);

      StockHistoricalData currDD = new StockHistoricalData(d, s, open, high,
          low, close, vol, adjustedClose);

      // Date Open High Low Close Volume, Adj Close, C / Adj C, T+1 / T, Filter
      // >, ABS(x-1)
      // 5/12/2004 26.81 27.18 25.76 27.08 26108100 27.08 1.0000 2.000373692
      // 2.000373692 1.00037
      // 5/11/2004 52.35 54 52.18 53.53 34553400 26.76 0.4999 0.999813189
      // 0.00019

      if (prevDD != null) {
        Double multipler = prevDD.getCloseMultipler()
            / currDD.getCloseMultipler();
        if (Math.abs(multipler - 1) > 0.03) {
          prevDD.setMultipler(multipler);
          hasMultipler = true;
        }

        pm.makePersistent(prevDD);

      }

      filecount++;

      prevDD = currDD;
    }

    // Save the last DD
    if (lastDD == null)
      pm.makePersistent(prevDD);

    if (filecount != 0) {
      if (hasMultipler)
        s.setPriceMultiplied(true);
      s.addTotalHistorialData(filecount - 1);
      if (lastHistoricalDate != null)
        s.setLastHistoricalDate(lastHistoricalDate);
      pm.makePersistent(s);
    }

  }

  private String constructDateParams(String names, Calendar c) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 3; i++) {
      sb.append(names.charAt(i)).append("=").append(c.get(CALENDAR_FIELDS[i]))
          .append("&");
    }

    return sb.toString();

  }

}
