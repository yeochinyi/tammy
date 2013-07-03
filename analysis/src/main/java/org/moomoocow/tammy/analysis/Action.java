package org.moomoocow.tammy.analysis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Action {

	private final static DateFormat df = new SimpleDateFormat("d/M");
	
	private final ActionType type;

	private final Date date;

	private final Double price;

	public Action(ActionType type, Date date, Double price) {
		super();
		this.type = type;
		this.date = date;
		this.price = price;
	}

	public enum ActionType {
		BUY(true,"B"), 
                SELL(false,"S"), 
                STOPLOSS(false,"L"), 
                TAKEPROFIT(false,"T");

		boolean isBuy;
                String code;

		ActionType(boolean isBuy,String code) {
			this.isBuy = isBuy;
                        this.code = code;
		}
	}

	public boolean isBuy() {
		return type.isBuy;
	}

	public Date getDate() {
		return date;
	}

	public Double getPrice() {
		return price;
	}

	public ActionType getType() {
		return type;
	}
        
	@Override
	public String toString() {
		return type.code + "@" + String.format("%(,.2f",price) + "_" + df.format(date);				
	}
	
	

}