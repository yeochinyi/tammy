package org.moomoocow.tammy.marketdata;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

@PersistenceCapable
@Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
public class DailyData
{
    @PrimaryKey        
    private Date date;
    
    @PrimaryKey
    private Stock stock;
    
    private double open;
    
    private double high;
    
    private double low;
    
    private double close;
    
    private long vol;
    
    private double adjClose;

	public DailyData(Date date, Stock stock, double open, double high,
			double low, double close, long vol, double adjClose) {
		super();
		this.date = date;
		this.stock = stock;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.vol = vol;
		this.adjClose = adjClose;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getVol() {
		return vol;
	}

	public void setVol(long vol) {
		this.vol = vol;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}
        
    

    
}
