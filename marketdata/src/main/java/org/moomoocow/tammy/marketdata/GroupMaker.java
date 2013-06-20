package org.moomoocow.tammy.marketdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.moomoocow.tammy.model.StockGroup;
import org.moomoocow.tammy.model.Stock;
import org.moomoocow.tammy.model.util.Helper;

public class GroupMaker {

  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(GroupMaker.class);
  
  
  @SuppressWarnings("unchecked")
  public static void main(String[] args){
    PersistenceManager pm = Helper.SINGLETON.getPersistenceManager();
        
    Query q = (Query) pm.newQuery(StockGroup.class);
    
    Map<String, StockGroup> groupMap = new HashMap<String, StockGroup>();
    
     for(StockGroup g : (List<StockGroup>) q.execute()){
       groupMap.put(g.getCode(), g);
     }
    
    q = (Query) pm.newQuery(Stock.class,
        "this.description.toUpperCase().matches(\".*ETF.*\")");
    
    List<Stock> stocks = (List<Stock>) q.execute();
    
    for (Stock s : stocks) {
      System.out.println(s.getCode() + " " + s.getDescription());
      String exchange = s.getExchange().getCode();
      String groupName = exchange + "_ETF";
      StockGroup g = groupMap.get(groupName);
      
      if(g == null){
        g = new StockGroup(groupName, groupName);
        groupMap.put(groupName,g);
      }
      
      g.addStock(s);
    }
    
    for (StockGroup g : groupMap.values()) {
      pm.makePersistent(g);
    }

  }
}
