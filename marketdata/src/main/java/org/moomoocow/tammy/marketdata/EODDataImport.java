package org.moomoocow.tammy.marketdata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class EODDataImport {

  private PersistenceManager pm;

  public EODDataImport() {
    PersistenceManagerFactory pmf = JDOHelper
    .getPersistenceManagerFactory("datanucleus.properties");
    pm = pmf.getPersistenceManager();

  }

  public static void main(String[] args) throws IOException, SAXException {
    EODDataImport main = new EODDataImport();    
    main.createDefaultExchanges();
    main.fakeImportEODStocks();
  }
  
  @SuppressWarnings("unchecked")
  public void fakeImportEODStocks() throws IOException{
    
    Query q = pm.newQuery(Exchange.class, "this.code == 'SGX'");

    for (Exchange e : (List<Exchange>) q.execute()) {
      System.out.println(e.getCode());
      
      BufferedReader r = new BufferedReader(new FileReader("C:/Downloads/" + e.getCode() + ".txt"));
      String t;
      while((t = r.readLine()) != null){
        if(t.startsWith("Symbol")) continue;
        String[] texts  = t.split("\t");        
          Stock s = new Stock(texts[0],texts[1],e);
          pm.makePersistent(s);        
      }
    }

    

  }
  
  public void importEODStocks() throws IOException, SAXException{

    
    WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8,"FM-AP-HKG-PROXY.fm.rbsgrp.net",8080);
    //WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8,"127.0.0.1",18080);
    //WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);        
    
    //SystemDefaultHttpClient client = new SystemDefaultHttpClient();
    
    //final DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
    //credentialsProvider.addProxyCredentials("fx_build", "Franom52");
    
    //webClient.get
    
    //webClient.setJavaScriptEnabled(true);
    //webClient.setThrowExceptionOnScriptError(false);
    
    //set proxy username and password 
    
    HtmlPage page = webClient.getPage("http://eoddata.com/myaccount/default.aspx");
    
    HtmlForm f = page.getFormByName("aspnetForm");
    
    //System.out.println(r.getText());        
    
    f.getInputByName("ctl00$cph1$ls1$txtEmail").setValueAttribute("yeochinyi");
    f.getInputByName("ctl00$cph1$ls1$txtPassword").setValueAttribute("E0dd@t@");
    HtmlPage p = f.getButtonByName("ctl00$cph1$ls1$btnLogin").click();
       
    
    //r = f.submit(b);
    
    Writer pw = new PrintWriter(new FileWriter("c:/temp/test.html"));
    
    pw.append(p.asText());
    
    pw.close();
    
    //System.out.println();
    
    //r = wc.getResponse("http://eoddata.com/Data/symbollist.aspx?e=SGX");
    
    //System.out.println(r.getText());
    
  }

  @SuppressWarnings("unchecked")
  public void createDefaultExchanges() {
    
    Query q = pm.newQuery(Exchange.class);
    
    List<Exchange> list = (List<Exchange>) q.execute();
    
    if(list.size() != 0) return;
    
        
    String[] exchanges = { "AMS", "Euronext Amsterdam", "ASX",
        "Australian Securities Exchange", "BRU", "Euronext Brussels", "CBOT",
        "Chicago Board of Trade", "CFE", "Chicago Futures Exchange", "CME",
        "Chicago Merchantile Exchange", "COMEX", "New York Commodity Exchange",
        "EUREX", "EUREX Futures Exchange", "FOREX", "Foreign Exchange", "HKEX",
        "Hong Kong Stock Exchange", "INDEX", "Global Indices", "KCBT",
        "Kansas City Board of Trade", "LIFFE", "LIFFE Futures and Options",
        "LIS", "Euronext Lisbon", "LSE", "London Stock Exchange", "MGEX",
        "Minneapolis Grain Exchange", "MLSE", "Milan Stock Exchange", "NASDAQ",
        "NASDAQ Stock Exchange", "NYBOT", "New York Board of Trade", "NYMEX",
        "New York Merchantile Exchange", "NYSE", "New York Stock Exchange",
        "NZX", "New Zealand Exchange", "OPRA", "US Options", "OTCBB",
        "OTC Bulletin Board", "PAR", "Euronext Paris", "SGX",
        "Singapore Stock Exchange", "TSX", "Toronto Stock Exchange", "TSXV",
        "Toronto Venture Exchange", "USMF", "Mutual Funds", "WCE",
        "Winnipeg Commodity Exchange", };

    for (int i = 0; i < exchanges.length; i += 2) {
      Exchange e = new Exchange(exchanges[i], exchanges[i + 1]);
      pm.makePersistent(e);
    }

    /*
    Query q = pm.newQuery(Exchange.class, "this.active == true");

    for (Exchange e : (List<Exchange>) q.execute()) {
      System.out.println(e.getCode());
    }*/

  }

}
