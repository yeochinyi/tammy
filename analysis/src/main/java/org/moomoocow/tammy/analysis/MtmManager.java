package org.moomoocow.tammy.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class MtmManager {

	private List<MarkToMarket> mtms;

	private List<Deal> currentDeals;
	
	private Map<Action,Integer> actionCount;

	private double currentSale;

	private double currentCost;

	private double totalCost;

	private double totalSale;

	private final double initialCost;

	private final Date startDate;

	private Date lastDate;

	private long stock;

	public MtmManager(double initialOutlay, Date startDate) {
		this.mtms = new ArrayList<MarkToMarket>();
		this.totalCost = 0;
		this.totalSale = 0;
		this.currentCost = 0;
		this.currentSale = 0;
		this.initialCost = initialOutlay;
		this.startDate = startDate;
		this.currentDeals = new ArrayList<Deal>();
		this.lastDate = startDate;
		this.stock = 0;
		this.actionCount = new HashMap<Action,Integer>();
	}

	public void add(Deal d) {
		double t = d.getTotalTransaction();
		if (d.isBuy()) {
			currentCost += t;
			totalCost += t;
			stock += d.getQty();
		} else {
			currentSale += t;
			totalSale += t;
			stock -= d.getQty();
		}
		this.currentDeals.add(d);
		Integer count = this.actionCount.get(d.getReason());
		if(count == null) count = 0;
		this.actionCount.put(d.getReason(),++count);
	}

	public double commitMarkToMarket(Date endDate, double price) {

		/*
		 * if there is deals in the period, need to cal cost + sale if there is
		 * stock , need to get final price by "selling all" if these is no stock
		 * and not deals, everythign zero
		 */

		double days = Days.daysBetween(new DateTime(lastDate),
				new DateTime(endDate)).getDays();
		double irr = 0.0;
		double irrAnnual = 0.0;
		double fakeSell = 0.0;
		if (stock != 0 || currentSale != 0) {
			fakeSell = price * stock;
			if (currentCost != 0.0) {
				double numerator = currentSale + fakeSell - currentCost;
				irr = MathHelper.divide(numerator, currentCost);
                                double denomintor = currentCost * days;
				irrAnnual = MathHelper.divide(numerator * 365.0, denomintor);
			}
		}
		MarkToMarket m = new MarkToMarket(endDate, price, this.currentDeals,
				irr, irrAnnual);
		mtms.add(m);
		this.currentDeals = new ArrayList<Deal>();
		this.currentCost = fakeSell; //need to buy back
		this.currentSale = 0.0;
		this.lastDate = endDate;

		return irrAnnual;
	}

	public double getTotalAnnualizedPnl() {
		MarkToMarket lastMtm = mtms.get(mtms.size() - 1);
		double days = Days.daysBetween(new DateTime(startDate),
				new DateTime(lastMtm.getEndDate())).getDays();
		double numerator = lastMtm.getEndPrice() * stock + totalSale
				- totalCost;
		double irr = MathHelper.divide(numerator * 365.0, initialCost * days);
		return irr;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(String.format("stdev=%(,.2f",
				getAnnualizedStdDev()));
		sb.append(",actionMap=").append(this.actionCount).append(",");
		for (MarkToMarket m : this.mtms) {
			sb.append(m);
		}
		return sb.toString();
	}

	public double getAnnualizedStdDev() {
		StandardDeviation st = new StandardDeviation(true);
		double[] ds = new double[this.mtms.size()];

		int i = 0;
		for (MarkToMarket m : this.mtms)
			ds[i++] = m.getRealAnnualPnl();
		return st.evaluate(ds);
	}
}
