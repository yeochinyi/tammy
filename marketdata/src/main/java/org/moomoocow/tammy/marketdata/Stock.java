package org.moomoocow.tammy.marketdata;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
// @Version(strategy=VersionStrategy.DATE_TIME, column="TIMESTAMP")
public class Stock extends BaseStaticData {

  private Exchange exchange;
  
  private Boolean priceMultiplied;
  
  @Column(jdbcType="DATE")
  private Date lastHistoricalDate;

  @Persistent(mappedBy = "stock")
  private Set<HistoricalData> dailyData;

  public Stock(String code, String desc, Exchange exchange) {
    
    super(code,desc);
    
    this.exchange = exchange;
    this.dailyData = new HashSet<HistoricalData>();
  }

  public Exchange getExchange() {
    return exchange;
  }

  public void setExchange(Exchange exchange) {
    this.exchange = exchange;
  }

  public Set<HistoricalData> getDailyData() {
    return dailyData;
  }

  public void setDailyData(Set<HistoricalData> dailyData) {
    this.dailyData = dailyData;
  }

  public void setPriceMultiplied(Boolean priceMultiplied) {
    this.priceMultiplied = priceMultiplied;
  }

  public Boolean getPriceMultiplied() {
    return priceMultiplied;
  }
  
  public HistoricalData getMaxDatedDailyData(){
    
    HistoricalData max = null;
    
    for (HistoricalData dd : this.getDailyData()) {
      if(max == null || dd.getDate().after(max.getDate())) 
        max = dd;
    }
    
    return max;
  }

  public void setLastHistoricalDate(Date lastHistoricalDate) {
    this.lastHistoricalDate = lastHistoricalDate;
  }

  public Date getLastHistoricalData() {
    return lastHistoricalDate;
  }

}
