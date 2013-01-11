package org.moomoocow.tammy.marketdata;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
// @Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
public class Exchange extends BaseStaticData {

  @Persistent(mappedBy = "exchange")
  private Set<Stock> stocks;
  
  private Integer totalStocks;

  public Exchange(String code, String desc) {
    super(code, desc);

    this.stocks = new HashSet<Stock>();
  }

  public Set<Stock> getStocks() {
    return stocks;
  }

  public void setStocks(Set<Stock> stocks) {
    this.stocks = stocks;
  }

  public Integer addTotalStocks(Integer totalStocks) {
    return (this.totalStocks == null ? 0 :  this.totalStocks) +  totalStocks;    
  }

  public Integer getTotalStocks() {
    return totalStocks;
  }

}
