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

}
