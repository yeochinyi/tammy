package org.moomoocow.tammy.marketdata;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Unique;

@PersistenceCapable
// @Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@Unique(name = "STOCK_DATE_IDX", members = { "date", "stock" })
public class DailyData extends BasePersistData {

  @Column(jdbcType="DATE")
  private Date date;

  private Stock stock;

  private Double open;

  private Double high;

  private Double low;

  private Double close;

  private Long vol;

  private Double multipler;

  public DailyData(Date date, Stock stock, Double open, Double high,
      Double low, Double close, Long vol, Double multipler) {
    super();
    this.setDate(date);
    this.setStock(stock);
    this.setOpen(open);
    this.setHigh(high);
    this.setLow(low);
    this.setClose(close);
    this.setVol(vol);
    this.setMultipler(multipler);
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

  public Double getOpen() {
    return open;
  }

  public void setHigh(Double high) {
    this.high = high;
  }

  public Double getHigh() {
    return high;
  }

  public void setLow(Double low) {
    this.low = low;
  }

  public Double getLow() {
    return low;
  }

  public void setClose(Double close) {
    this.close = close;
  }

  public Double getClose() {
    return close;
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

}
