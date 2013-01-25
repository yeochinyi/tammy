package org.moomoocow.tammy.model;

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
  
  public Set<Stock> getActiveStocks() {
    Set<Stock> set = getStocks();
    for (Stock s : set) {
      if(Boolean.TRUE.equals(s.getActive())){
        set.remove(s);
      }          
    }
    
    return set;
  }
 

  public void setStocks(Set<Stock> stocks) {
    this.stocks = stocks;
  }

  public Integer addTotalStocks(Integer totalStocks) {
    this.totalStocks = (this.totalStocks == null ? 0 : this.totalStocks)
        + totalStocks;
    return this.totalStocks;
  }

  public Integer getTotalStocks() {
    return totalStocks;
  }

}
