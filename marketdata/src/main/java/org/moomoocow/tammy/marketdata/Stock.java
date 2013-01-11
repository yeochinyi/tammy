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

  private Integer totalHistorialData;
  
  @Persistent(mappedBy = "stock")
  private Set<StockHistoricalData> dailyData;
  
  
  @Column(jdbcType="DATE")
  private Date lastSnapshotDate;
  
  private Integer totalSnapshotData;

  @Persistent(mappedBy = "stock")
  private Set<StockSnapshotData> snapshotData;

  public Stock(String code, String desc, Exchange exchange) {
    
    super(code,desc);
    
    this.exchange = exchange;
    this.dailyData = new HashSet<StockHistoricalData>();
  }

  public Exchange getExchange() {
    return exchange;
  }

  public void setExchange(Exchange exchange) {
    this.exchange = exchange;
  }

  public Set<StockHistoricalData> getDailyData() {
    return dailyData;
  }

  public void setDailyData(Set<StockHistoricalData> dailyData) {
    this.dailyData = dailyData;
  }

  public void setPriceMultiplied(Boolean priceMultiplied) {
    this.priceMultiplied = priceMultiplied;
  }

  public Boolean getPriceMultiplied() {
    return priceMultiplied;
  }
  
  public StockHistoricalData getMaxDatedDailyData(){
    
    StockHistoricalData max = null;
    
    for (StockHistoricalData dd : this.getDailyData()) {
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

  public Integer getTotalHistorialData() {
    return totalHistorialData;
  }

  public Integer addTotalHistorialData(Integer totalHistorialData) {
    return (this.totalHistorialData == null ? 0 :  this.totalHistorialData) +  totalHistorialData;
  }

  public Date getLastSnapshotDate() {
    return lastSnapshotDate;
  }

  public void setLastSnapshotDate(Date lastSnapshotDate) {
    this.lastSnapshotDate = lastSnapshotDate;
  }

  public Integer getTotalSnapshotData() {
    return totalSnapshotData;
  }

  public Integer addTotalSnapshotData(Integer totalSnapshotData) {
    return (this.totalSnapshotData == null ? 0 :  this.totalSnapshotData) +  totalSnapshotData;
  }

  public Set<StockSnapshotData> getSnapshotData() {
    return snapshotData;
  }

  public void setSnapshotData(Set<StockSnapshotData> snapshotData) {
    this.snapshotData = snapshotData;
  }

  public Date getLastHistoricalDate() {
    return lastHistoricalDate;
  }

}
