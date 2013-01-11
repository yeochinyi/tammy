package org.moomoocow.tammy.marketdata;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class StockSnapshotData extends BasePersistData {
    
  public void copyNonNullFields(StockSnapshotData copyFrom){
    
    if(copyFrom == null) return;
    
    for (Field f : StockSnapshotData.class.getDeclaredFields()) {      
      try {
        Object v = f.get(copyFrom);
        if(v==null) continue;
        f.set(this, v);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }     
    }
  }

  private Stock stock;

  protected String multipler;
  
  @NotPersistent
  @YahooData(key = "s", isAlwaysImported=true)
  protected String symbol;

  @NotPersistent
  @YahooData(key = "n")
  protected String name;

  @NotPersistent
  @YahooData(key = "l")
  protected String lastTradeWithTime;

  @YahooData(key = "l1")
  protected Double last;

  @Column(jdbcType = "DATE")
  @YahooData(key = "d1")
  protected Date lastTradeDate;

  @NotPersistent
  @Column(jdbcType = "TIME")
  @YahooData(key = "t1")
  protected Date lastTradeTime;
  
  @YahooData(key = "k3",mayContainCommas=true)
  protected Long lastTradeSize;

  @NotPersistent
  @YahooData(key = "c")
  protected String changeAndPercentChange;

  @YahooData(key = "c1")
  protected String change;

  @YahooData(key = "p2")
  protected Double changeInPercent;

  @NotPersistent
  @YahooData(key = "t7")
  protected String tickerTrend;
  
  @YahooData(key = "v")
  protected Long vol;

  @YahooData(key = "a2")
  protected BigInteger avgDailyVol;

  @YahooData(key = "i")
  protected String moreInfo;

  @NotPersistent
  @YahooData(key = "t6")
  protected String tradeLinks;

  @YahooData(key = "b")
  protected Double bid;

  @YahooData(key = "b6",mayContainCommas=true)
  protected Long bidSize;

  @YahooData(key = "a")
  protected Double ask;

  @YahooData(key = "a5",mayContainCommas=true)
  protected Long askSize;

  @YahooData(key = "p")
  protected Double previousClose;

  @YahooData(key = "o")
  protected Double open;

  @NotPersistent
  @YahooData(key = "m")
  protected String daysRange;

  @NotPersistent
  @YahooData(key = "w")
  protected String yearRange;

  @YahooData(key = "j5")
  protected Double changeFromYearLow;

  @YahooData(key = "j6")
  protected Double percentChangeFromYearLow;

  @YahooData(key = "k4")
  protected Double changeFromYearHigh;

  @YahooData(key = "k5")
  protected Double percentChangeFromYearHigh;

  @YahooData(key = "e")
  protected Double earningShare;

  @YahooData(key = "r")
  protected Double priceEarningRatio;

  @YahooData(key = "s7")
  protected Double shorealtimeRatio;

  @Column(jdbcType = "DATE")
  @YahooData(key = "r1")
  protected Date divPayDate;

  @Column(jdbcType = "DATE")
  @YahooData(key = "q")
  protected Date exDivDate;

  @YahooData(key = "d")
  protected Double divShare;

  @YahooData(key = "y")
  protected Double divYield;

  @YahooData(key = "f6",mayContainCommas=true)
  protected Long floatShares;

  @YahooData(key = "j1")
  protected BigInteger marketCap;

  @YahooData(key = "t8")
  protected Double yearTargetPrice;

  @YahooData(key = "e7")
  protected Double epsEstCurrentYear;

  @YahooData(key = "e8")
  protected Double epsEstNextYear;

  @YahooData(key = "e9")
  protected Double epsEstNextQuarealtimeer;

  @YahooData(key = "r6")
  protected Double priceEpsEstCurrentYear;

  @YahooData(key = "r7")
  protected Double priceEpsEstNextYear;

  @YahooData(key = "r5")
  protected Double pegRatio;

  @YahooData(key = "b4")
  protected Long bookValue;

  @YahooData(key = "p6")
  protected Double priceBook;

  @YahooData(key = "p5")
  protected Double priceSales;

  @YahooData(key = "j4")
  protected Double ebitda;

  @YahooData(key = "m3")
  protected Double fiftyDayMovAvg;

  @YahooData(key = "m7")
  protected Double changeFromFiftyDayMovAvg;

  @YahooData(key = "m8")
  protected Double percentChangeFromFiftyDayMovAvg;

  @YahooData(key = "m4")
  protected Double twoHundredDayMovAvg;

  @YahooData(key = "m5")
  protected Double changeFromTwoHundredDayMovAvg;

  @YahooData(key = "m6")
  protected Double percentChangeFromTwoHundredDayMovAvg;

  @YahooData(key = "s1")
  protected Long sharesOwned;

  @YahooData(key = "p1")
  protected Double pricePaid;

  @YahooData(key = "c3")
  protected Double commission;

  @YahooData(key = "v1")
  protected Double holdValue;

  @YahooData(key = "w1")
  protected Double daysValueChange;

  @YahooData(key = "g1")
  protected Double holdGainPercent;

  @YahooData(key = "g4")
  protected Double holdGain;

  @Column(jdbcType = "DATE")
  @YahooData(key = "d2")
  protected Date tradeDate;

  @YahooData(key = "g3")
  protected Double annualizedGain;

  @YahooData(key = "l2")
  protected Double highLimit;

  @YahooData(key = "l3")
  protected Double lowLimit;

  @YahooData(key = "n4")
  protected String notes;

  @NotPersistent
  @YahooData(key = "k1")
  protected String lastTradeRealtimeWithTime;

  @NotPersistent
  @YahooData(key = "b3")
  protected Double bidRealtime;

  @NotPersistent
  @YahooData(key = "b2")
  protected Double askRealtime;

  @NotPersistent
  @YahooData(key = "k2")
  protected Double changePercentRealtime;

  @NotPersistent
  @YahooData(key = "c6")
  protected Double changeRealtime;

  @NotPersistent
  @YahooData(key = "v7")
  protected Double holdValueRealtime;

  @NotPersistent
  @YahooData(key = "w4")
  protected Double dayValueChangeRealtime;

  @NotPersistent
  @YahooData(key = "g5")
  protected Double holdGainPercentRealtime;

  @NotPersistent
  @YahooData(key = "g6")
  protected Double holdGainRealtime;

  @NotPersistent
  @YahooData(key = "m2")
  protected Double daysRangeRealtime;

  @NotPersistent
  @YahooData(key = "j3")
  protected Double marketCapriceRealtime;

  @NotPersistent
  @YahooData(key = "r2")
  protected Double priceEarningRealtime;

  @NotPersistent
  @YahooData(key = "c8")
  protected Double afterHoursChangeRealtime;

  @NotPersistent
  @YahooData(key = "i5")
  protected String orderBookRealtime;

  @NotPersistent
  @YahooData(key = "x")
  protected String stockExchange;

  @YahooData(key = "e1")
  protected String error;

  @YahooData(key = "g")
  protected Double dayLow;

  @YahooData(key = "h")
  protected Double dayHigh;

  @YahooData(key = "k")
  protected Double yearHigh;

  @YahooData(key = "j")
  protected Double yearLow;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastTradeWithTime() {
    return lastTradeWithTime;
  }

  public void setLastTradeWithTime(String lastTradeWithTime) {
    this.lastTradeWithTime = lastTradeWithTime;
  }

  public Double getLast() {
    return last;
  }

  public void setLast(Double last) {
    this.last = last;
  }

  public Date getLastTradeDate() {
    return lastTradeDate;
  }

  public void setLastTradeDate(Date lastTradeDate) {
    this.lastTradeDate = lastTradeDate;
  }

  public Date getLastTradeTime() {
    return lastTradeTime;
  }

  public void setLastTradeTime(Date lastTradeTime) {
    this.lastTradeTime = lastTradeTime;
  }

  public Long getLastTradeSize() {
    return lastTradeSize;
  }

  public void setLastTradeSize(Long lastTradeSize) {
    this.lastTradeSize = lastTradeSize;
  }

  public String getChangeAndPercentChange() {
    return changeAndPercentChange;
  }

  public void setChangeAndPercentChange(String changeAndPercentChange) {
    this.changeAndPercentChange = changeAndPercentChange;
  }

  public String getChange() {
    return change;
  }

  public void setChange(String change) {
    this.change = change;
  }

  public Double getChangeInPercent() {
    return changeInPercent;
  }

  public void setChangeInPercent(Double changeInPercent) {
    this.changeInPercent = changeInPercent;
  }

  public String getTickerTrend() {
    return tickerTrend;
  }

  public void setTickerTrend(String tickerTrend) {
    this.tickerTrend = tickerTrend;
  }

  public Long getVol() {
    return vol;
  }

  public void setVol(Long vol) {
    this.vol = vol;
  }

  public BigInteger getAvgDailyVol() {
    return avgDailyVol;
  }

  public void setAvgDailyVol(BigInteger avgDailyVol) {
    this.avgDailyVol = avgDailyVol;
  }

  public String getMoreInfo() {
    return moreInfo;
  }

  public void setMoreInfo(String moreInfo) {
    this.moreInfo = moreInfo;
  }

  public String getTradeLinks() {
    return tradeLinks;
  }

  public void setTradeLinks(String tradeLinks) {
    this.tradeLinks = tradeLinks;
  }

  public Double getBid() {
    return bid;
  }

  public void setBid(Double bid) {
    this.bid = bid;
  }

  public Long getBidSize() {
    return bidSize;
  }

  public void setBidSize(Long bidSize) {
    this.bidSize = bidSize;
  }

  public Double getAsk() {
    return ask;
  }

  public void setAsk(Double ask) {
    this.ask = ask;
  }

  public Long getAskSize() {
    return askSize;
  }

  public void setAskSize(Long askSize) {
    this.askSize = askSize;
  }

  public Double getPreviousClose() {
    return previousClose;
  }

  public void setPreviousClose(Double previousClose) {
    this.previousClose = previousClose;
  }

  public Double getOpen() {
    return open;
  }

  public void setOpen(Double open) {
    this.open = open;
  }

  
  public String getDaysRange() {
    return daysRange;
  }

  public void setDaysRange(String daysRange) {
    this.daysRange = daysRange;
  }

  public String getYearRange() {
    return yearRange;
  }

  public void setYearRange(String yearRange) {
    this.yearRange = yearRange;
  }

  public Double getChangeFromYearLow() {
    return changeFromYearLow;
  }

  public void setChangeFromYearLow(Double changeFromYearLow) {
    this.changeFromYearLow = changeFromYearLow;
  }

  public Double getPercentChangeFromYearLow() {
    return percentChangeFromYearLow;
  }

  public void setPercentChangeFromYearLow(Double percentChangeFromYearLow) {
    this.percentChangeFromYearLow = percentChangeFromYearLow;
  }

  public Double getChangeFromYearHigh() {
    return changeFromYearHigh;
  }

  public void setChangeFromYearHigh(Double changeFromYearHigh) {
    this.changeFromYearHigh = changeFromYearHigh;
  }

  public Double getPercentChangeFromYearHigh() {
    return percentChangeFromYearHigh;
  }

  public void setPercentChangeFromYearHigh(Double percentChangeFromYearHigh) {
    this.percentChangeFromYearHigh = percentChangeFromYearHigh;
  }

  public Double getEarningShare() {
    return earningShare;
  }

  public void setEarningShare(Double earningShare) {
    this.earningShare = earningShare;
  }

  public Double getPriceEarningRatio() {
    return priceEarningRatio;
  }

  public void setPriceEarningRatio(Double priceEarningRatio) {
    this.priceEarningRatio = priceEarningRatio;
  }

  public Double getShorealtimeRatio() {
    return shorealtimeRatio;
  }

  public void setShorealtimeRatio(Double shorealtimeRatio) {
    this.shorealtimeRatio = shorealtimeRatio;
  }

  public Date getDivPayDate() {
    return divPayDate;
  }

  public void setDivPayDate(Date divPayDate) {
    this.divPayDate = divPayDate;
  }

  public Date getExDivDate() {
    return exDivDate;
  }

  public void setExDivDate(Date exDivDate) {
    this.exDivDate = exDivDate;
  }

  public Double getDivShare() {
    return divShare;
  }

  public void setDivShare(Double divShare) {
    this.divShare = divShare;
  }

  public Double getDivYield() {
    return divYield;
  }

  public void setDivYield(Double divYield) {
    this.divYield = divYield;
  }

  public Long getFloatShares() {
    return floatShares;
  }

  public void setFloatShares(Long floatShares) {
    this.floatShares = floatShares;
  }

  public BigInteger getMarketCap() {
    return marketCap;
  }

  public void setMarketCap(BigInteger marketCap) {
    this.marketCap = marketCap;
  }

  public Double getYearTargetPrice() {
    return yearTargetPrice;
  }

  public void setYearTargetPrice(Double yearTargetPrice) {
    this.yearTargetPrice = yearTargetPrice;
  }

  public Double getEpsEstCurrentYear() {
    return epsEstCurrentYear;
  }

  public void setEpsEstCurrentYear(Double epsEstCurrentYear) {
    this.epsEstCurrentYear = epsEstCurrentYear;
  }

  public Double getEpsEstNextYear() {
    return epsEstNextYear;
  }

  public void setEpsEstNextYear(Double epsEstNextYear) {
    this.epsEstNextYear = epsEstNextYear;
  }

  public Double getEpsEstNextQuarealtimeer() {
    return epsEstNextQuarealtimeer;
  }

  public void setEpsEstNextQuarealtimeer(Double epsEstNextQuarealtimeer) {
    this.epsEstNextQuarealtimeer = epsEstNextQuarealtimeer;
  }

  public Double getPriceEpsEstCurrentYear() {
    return priceEpsEstCurrentYear;
  }

  public void setPriceEpsEstCurrentYear(Double priceEpsEstCurrentYear) {
    this.priceEpsEstCurrentYear = priceEpsEstCurrentYear;
  }

  public Double getPriceEpsEstNextYear() {
    return priceEpsEstNextYear;
  }

  public void setPriceEpsEstNextYear(Double priceEpsEstNextYear) {
    this.priceEpsEstNextYear = priceEpsEstNextYear;
  }

  public Double getPegRatio() {
    return pegRatio;
  }

  public void setPegRatio(Double pegRatio) {
    this.pegRatio = pegRatio;
  }

  public Long getBookValue() {
    return bookValue;
  }

  public void setBookValue(Long bookValue) {
    this.bookValue = bookValue;
  }

  public Double getPriceBook() {
    return priceBook;
  }

  public void setPriceBook(Double priceBook) {
    this.priceBook = priceBook;
  }

  public Double getPriceSales() {
    return priceSales;
  }

  public void setPriceSales(Double priceSales) {
    this.priceSales = priceSales;
  }

  public Double getEbitda() {
    return ebitda;
  }

  public void setEbitda(Double ebitda) {
    this.ebitda = ebitda;
  }

  public Double getFiftyDayMovAvg() {
    return fiftyDayMovAvg;
  }

  public void setFiftyDayMovAvg(Double fiftyDayMovAvg) {
    this.fiftyDayMovAvg = fiftyDayMovAvg;
  }

  public Double getChangeFromFiftyDayMovAvg() {
    return changeFromFiftyDayMovAvg;
  }

  public void setChangeFromFiftyDayMovAvg(Double changeFromFiftyDayMovAvg) {
    this.changeFromFiftyDayMovAvg = changeFromFiftyDayMovAvg;
  }

  public Double getPercentChangeFromFiftyDayMovAvg() {
    return percentChangeFromFiftyDayMovAvg;
  }

  public void setPercentChangeFromFiftyDayMovAvg(
      Double percentChangeFromFiftyDayMovAvg) {
    this.percentChangeFromFiftyDayMovAvg = percentChangeFromFiftyDayMovAvg;
  }

  public Double getTwoHundredDayMovAvg() {
    return twoHundredDayMovAvg;
  }

  public void setTwoHundredDayMovAvg(Double twoHundredDayMovAvg) {
    this.twoHundredDayMovAvg = twoHundredDayMovAvg;
  }

  public Double getChangeFromTwoHundredDayMovAvg() {
    return changeFromTwoHundredDayMovAvg;
  }

  public void setChangeFromTwoHundredDayMovAvg(
      Double changeFromTwoHundredDayMovAvg) {
    this.changeFromTwoHundredDayMovAvg = changeFromTwoHundredDayMovAvg;
  }

  public Double getPercentChangeFromTwoHundredDayMovAvg() {
    return percentChangeFromTwoHundredDayMovAvg;
  }

  public void setPercentChangeFromTwoHundredDayMovAvg(
      Double percentChangeFromTwoHundredDayMovAvg) {
    this.percentChangeFromTwoHundredDayMovAvg = percentChangeFromTwoHundredDayMovAvg;
  }

  public Long getSharesOwned() {
    return sharesOwned;
  }

  public void setSharesOwned(Long sharesOwned) {
    this.sharesOwned = sharesOwned;
  }

  public Double getPricePaid() {
    return pricePaid;
  }

  public void setPricePaid(Double pricePaid) {
    this.pricePaid = pricePaid;
  }

  public Double getCommission() {
    return commission;
  }

  public void setCommission(Double commission) {
    this.commission = commission;
  }

  public Double getHoldValue() {
    return holdValue;
  }

  public void setHoldValue(Double holdValue) {
    this.holdValue = holdValue;
  }

  public Double getDaysValueChange() {
    return daysValueChange;
  }

  public void setDaysValueChange(Double daysValueChange) {
    this.daysValueChange = daysValueChange;
  }

  public Double getHoldGainPercent() {
    return holdGainPercent;
  }

  public void setHoldGainPercent(Double holdGainPercent) {
    this.holdGainPercent = holdGainPercent;
  }

  public Double getHoldGain() {
    return holdGain;
  }

  public void setHoldGain(Double holdGain) {
    this.holdGain = holdGain;
  }

  public Date getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(Date tradeDate) {
    this.tradeDate = tradeDate;
  }

  public Double getAnnualizedGain() {
    return annualizedGain;
  }

  public void setAnnualizedGain(Double annualizedGain) {
    this.annualizedGain = annualizedGain;
  }

  public Double getHighLimit() {
    return highLimit;
  }

  public void setHighLimit(Double highLimit) {
    this.highLimit = highLimit;
  }

  public Double getLowLimit() {
    return lowLimit;
  }

  public void setLowLimit(Double lowLimit) {
    this.lowLimit = lowLimit;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getLastTradeRealtimeWithTime() {
    return lastTradeRealtimeWithTime;
  }

  public void setLastTradeRealtimeWithTime(String lastTradeRealtimeWithTime) {
    this.lastTradeRealtimeWithTime = lastTradeRealtimeWithTime;
  }

  public Double getBidRealtime() {
    return bidRealtime;
  }

  public void setBidRealtime(Double bidRealtime) {
    this.bidRealtime = bidRealtime;
  }

  public Double getAskRealtime() {
    return askRealtime;
  }

  public void setAskRealtime(Double askRealtime) {
    this.askRealtime = askRealtime;
  }

  public Double getChangePercentRealtime() {
    return changePercentRealtime;
  }

  public void setChangePercentRealtime(Double changePercentRealtime) {
    this.changePercentRealtime = changePercentRealtime;
  }

  public Double getChangeRealtime() {
    return changeRealtime;
  }

  public void setChangeRealtime(Double changeRealtime) {
    this.changeRealtime = changeRealtime;
  }

  public Double getHoldValueRealtime() {
    return holdValueRealtime;
  }

  public void setHoldValueRealtime(Double holdValueRealtime) {
    this.holdValueRealtime = holdValueRealtime;
  }

  public Double getDayValueChangeRealtime() {
    return dayValueChangeRealtime;
  }

  public void setDayValueChangeRealtime(Double dayValueChangeRealtime) {
    this.dayValueChangeRealtime = dayValueChangeRealtime;
  }

  public Double getHoldGainPercentRealtime() {
    return holdGainPercentRealtime;
  }

  public void setHoldGainPercentRealtime(Double holdGainPercentRealtime) {
    this.holdGainPercentRealtime = holdGainPercentRealtime;
  }

  public Double getHoldGainRealtime() {
    return holdGainRealtime;
  }

  public void setHoldGainRealtime(Double holdGainRealtime) {
    this.holdGainRealtime = holdGainRealtime;
  }

  public Double getDaysRangeRealtime() {
    return daysRangeRealtime;
  }

  public void setDaysRangeRealtime(Double daysRangeRealtime) {
    this.daysRangeRealtime = daysRangeRealtime;
  }

  public Double getMarketCapriceRealtime() {
    return marketCapriceRealtime;
  }

  public void setMarketCapriceRealtime(Double marketCapriceRealtime) {
    this.marketCapriceRealtime = marketCapriceRealtime;
  }

  public Double getPriceEarningRealtime() {
    return priceEarningRealtime;
  }

  public void setPriceEarningRealtime(Double priceEarningRealtime) {
    this.priceEarningRealtime = priceEarningRealtime;
  }

  public Double getAfterHoursChangeRealtime() {
    return afterHoursChangeRealtime;
  }

  public void setAfterHoursChangeRealtime(Double afterHoursChangeRealtime) {
    this.afterHoursChangeRealtime = afterHoursChangeRealtime;
  }

  public String getOrderBookRealtime() {
    return orderBookRealtime;
  }

  public void setOrderBookRealtime(String orderBookRealtime) {
    this.orderBookRealtime = orderBookRealtime;
  }

  public String getStockExchange() {
    return stockExchange;
  }

  public void setStockExchange(String stockExchange) {
    this.stockExchange = stockExchange;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Double getDayLow() {
    return dayLow;
  }

  public void setDayLow(Double dayLow) {
    this.dayLow = dayLow;
  }

  public Double getDayHigh() {
    return dayHigh;
  }

  public void setDayHigh(Double dayHigh) {
    this.dayHigh = dayHigh;
  }

  public Double getYearHigh() {
    return yearHigh;
  }

  public void setYearHigh(Double yearHigh) {
    this.yearHigh = yearHigh;
  }

  public Double getYearLow() {
    return yearLow;
  }

  public void setYearLow(Double yearLow) {
    this.yearLow = yearLow;
  }

  public String getMultipler() {
    return multipler;
  }

  public void setMultipler(String multipler) {
    this.multipler = multipler;
  }

  public void setStock(Stock stock) {
    this.stock = stock;
  }

  public Stock getStock() {
    return stock;
  }

}
