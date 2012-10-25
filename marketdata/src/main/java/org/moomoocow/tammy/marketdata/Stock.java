package org.moomoocow.tammy.marketdata;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

@PersistenceCapable
@Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
public class Stock
{
    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
    private long id;
        
    public Set<DailyData> getDailyData() {
		return dailyData;
	}

	public void setDailyData(Set<DailyData> dailyData) {
		this.dailyData = dailyData;
	}

	@Unique
    private String code;
    
    private String desc;
    
    private boolean active;    
    
    private Exchange exchange;
    
	@Persistent(mappedBy="stock")
	private Set<DailyData>	dailyData;

    

	public Stock(String code, String desc, boolean active, Exchange exchange) {
		super();
		this.code = code;
		this.desc = desc;
		this.active = active;
		this.exchange = exchange;
		this.dailyData = new HashSet<DailyData>();
	}

	@Override
	public String toString() {
		return "Stock [id=" + id + ", code=" + code + ", desc=" + desc
				+ ", active=" + active + ", exchange=" + exchange + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}
    
    
    
    
    
}
