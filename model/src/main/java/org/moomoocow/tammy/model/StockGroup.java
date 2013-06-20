package org.moomoocow.tammy.model;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Unique;

@PersistenceCapable
@Unique(members={"code"})
// @Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
public class StockGroup extends BaseStaticData {

  @Join
  private List<Stock> stocks;

  private Integer totalStocks;

  public StockGroup(String code, String desc) {
    super(code, desc);
  }
  
  public void addStock(Stock s){
    if(stocks == null) stocks = new ArrayList<Stock>();
    stocks.add(s);    
    if(this.totalStocks == null){
      this.totalStocks = 0;
    }
    this.totalStocks++;

  }

  public List<Stock> getStocks() {
    return stocks;
  }

  /*
  public void setStocks(List<Stock> stocks) {
    this.stocks = stocks;
  }*/

  public Integer getTotalStocks() {
    return totalStocks;
  }

}
