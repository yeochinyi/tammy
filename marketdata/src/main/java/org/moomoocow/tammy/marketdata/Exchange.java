package org.moomoocow.tammy.marketdata;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;


@PersistenceCapable
public class Exchange
{
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private long id;
	
	@Unique
	private String code;
	
	private String desc;
    
	@Persistent(mappedBy="exchange")
	private Set<Stock> stocks;

	public Exchange(String code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
		
		this.stocks = new HashSet<Stock>();
	}

	@Override
	public String toString() {
		return "Exchange [id=" + id + ", code=" + code + ", desc=" + desc
				+ ", stocks=" + stocks + "]";
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

	public Set<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(Set<Stock> stocks) {
		this.stocks = stocks;
	}

	

	
	
}
