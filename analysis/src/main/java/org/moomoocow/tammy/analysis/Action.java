package org.moomoocow.tammy.analysis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Action {

	private final static DateFormat df = new SimpleDateFormat("dd/MM");
	
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
		BUY(true), SELL(false), STOPLOSS(false), TAKEPROFIT(false);

		boolean isBuy;

		ActionType(boolean isBuy) {
			this.isBuy = isBuy;
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

	@Override
	public String toString() {
		return type + "@" + String.format("%(,.2f",price) + " on " + df.format(date);				
	}
	
	

}