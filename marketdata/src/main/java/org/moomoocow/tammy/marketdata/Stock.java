package org.moomoocow.tammy.marketdata;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
// @Version(strategy=VersionStrategy.DATE_TIME, column="TIMESTAMP")
public class Stock extends BaseStaticData {

  private Exchange exchange;
  
  private Boolean priceMultiplied;

  @Persistent(mappedBy = "stock")
  private Set<DailyData> dailyData;

  public Stock(String code, String desc, Exchange exchange) {
    
    super(code,desc);
    
    this.exchange = exchange;
    this.dailyData = new HashSet<DailyData>();
  }

  public Exchange getExchange() {
    return exchange;
  }

  public void setExchange(Exchange exchange) {
    this.exchange = exchange;
  }

  public Set<DailyData> getDailyData() {
    return dailyData;
  }

  public void setDailyData(Set<DailyData> dailyData) {
    this.dailyData = dailyData;
  }

  public void setPriceMultiplied(Boolean priceMultiplied) {
    this.priceMultiplied = priceMultiplied;
  }

  public Boolean getPriceMultiplied() {
    return priceMultiplied;
  }

}
