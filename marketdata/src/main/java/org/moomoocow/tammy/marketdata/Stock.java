package org.moomoocow.tammy.marketdata;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

@PersistenceCapable
public class Stock
{
    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
    private long id;
        
    @Unique
    private String code;
    
    private String desc;
    
    private boolean active;
    
    private Exchange exchange;

    

	public Stock(String code, String desc, boolean active, Exchange exchange) {
		super();
		this.code = code;
		this.desc = desc;
		this.active = active;
		this.exchange = exchange;
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
