package org.moomoocow.tammy.marketdata;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
  }

  public Exchange getExchange() {
    return exchange;
  }

  public void setExchange(Exchange exchange) {
    this.exchange = exchange;
  }

  public Set<StockHistoricalData> getDailyData() {    
    return this.dailyData;
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
  
  public Collection<StockHistoricalData> getSortedDailyData() {
    SortedSet<StockHistoricalData> s = new TreeSet<StockHistoricalData>();
    for(StockHistoricalData d : getDailyData()){
      s.add(d);
    }
    
    return s;
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
    this.totalHistorialData = (this.totalHistorialData == null ? 0 :  this.totalHistorialData) +  totalHistorialData; 
    return this.totalHistorialData;
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
    this.totalSnapshotData = (this.totalSnapshotData == null ? 0 :  this.totalSnapshotData) +  totalSnapshotData;
    return  this.totalSnapshotData;
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
