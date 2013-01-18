package org.moomoocow.tammy.marketdata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Unique;

@PersistenceCapable
// @Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@Unique(name = "STOCK_DATE_IDX", members = { "date", "stock" })
public class StockHistoricalData extends BasePersistData implements Comparable<StockHistoricalData> {

  public enum Price {OPEN,HIGH,LOW,CLOSE,MID};
  
  @Column(jdbcType="DATE")
  private Date date;

  private Stock stock;

  private Double open;

  private Double high;

  private Double low;

  private Double close;

  private Long vol;

  private Double multipler;

  @NotPersistent
  private Double adjustedClose;
    
  public StockHistoricalData(Date date, Stock stock, Double open, Double high,
      Double low, Double close, Long vol, Double adjustedClose) {
    super();
    this.setDate(date);
    this.setStock(stock);
    this.setOpen(open);
    this.setHigh(high);
    this.setLow(low);
    this.setClose(close);
    this.setVol(vol);
    this.setAdjustedClose(adjustedClose);
  }
  
  public Double getCloseMultipler(){
    return this.close / this.adjustedClose;
  }
  

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  public void setStock(Stock stock) {
    this.stock = stock;
  }

  public Stock getStock() {
    return stock;
  }

  public void setOpen(Double open) {
    this.open = open;
  }

  public void setHigh(Double high) {
    this.high = high;
  }
  
  public Double getOpen() {
    return open;
  }
  public Double getClose() {
    return close;
  }


  public Double getHigh() {
    return high;
  }
  public Double getLow() {
    return low;
  }

  public void setLow(Double low) {
    this.low = low;
  }


  public void setClose(Double close) {
    this.close = close;
  }


  public void setVol(Long vol) {
    this.vol = vol;
  }

  public Long getVol() {
    return vol;
  }

  public void setMultipler(Double multipler) {
    this.multipler = multipler;
  }

  public Double getMultipler() {
    return multipler;
  }

  public void setAdjustedClose(Double adjustedClose) {
    this.adjustedClose = adjustedClose;
  }

  public Double getAdjustedClose() {
    return adjustedClose;
  }

  @Override
  public int compareTo(StockHistoricalData o) {      
    return this.getDate().compareTo(o.getDate());
  }
  
  public Double getMid(){
    return (getHigh() + getLow()) / 2.0;
  }

  @Override
  public String toString() {
    return "StockHistoricalData [date=" + date + ", open="
        + open + ", high=" + high + ", low=" + low + ", close=" + close
        + ", vol=" + vol + ", multipler=" + multipler + "]";
  }
  
  //private Double accumlatedMultipler = 1.0;
  
  private Map<Price,Double> accXPrices;
  
  public Double accumlateMultiplers(Double prevAccX){    
    Double m = (prevAccX == null ? 1.0 : prevAccX) * 
      (this.multipler == null ? 1.0 : this.multipler);
        
    accXPrices = new HashMap<Price,Double>();
    accXPrices.put(Price.OPEN, m * getOpen());
    accXPrices.put(Price.CLOSE, m * getClose());
    accXPrices.put(Price.HIGH, m * getHigh());
    accXPrices.put(Price.LOW, m * getLow());
    accXPrices.put(Price.MID, m * getMid());
    
    return m;    
  }  
  
  public Double getAccX(Price p){
    return accXPrices.get(p);
  }
  
  /*
  public Double getAccXOpen() {
    return getOpen() * accumlatedMultipler;
  }
  public Double getAccXClose() {
    return getClose() * accumlatedMultipler;
  }

  public Double getAccXHigh() {
    return getHigh() * accumlatedMultipler;
  }
  public Double getAccXLow() {
    return getLow() * accumlatedMultipler;
  }
  public Double getAccXMid() {
    return getMid() * accumlatedMultipler;
  }
  */

  
  

}
