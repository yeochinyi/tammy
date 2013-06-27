package org.moomoocow.tammy.analysis.signal;

import java.util.Date;

import org.moomoocow.tammy.analysis.Accountant;
import org.moomoocow.tammy.analysis.Action;

public class MinPeriod extends AbstractChainedSignal {

	private final int minPeriod;
	private Action delayedAction;

	public static MinPeriod getRandom(AbstractChainedSignal s) {
		return new MinPeriod((int) (Math.random() * 14.0), s);
	}

	public MinPeriod(int minPeriod, AbstractChainedSignal signal) {
		super(signal);
		this.minPeriod = minPeriod;
	}

	@Override
	public Action override(Action a, Date date, double open, double close,
			double high, double low, double mid, long vol, Accountant tm) {
		if (minPeriod > 0 && tm != null) {
			Integer period = tm.getPeriodAfterLastDealExclWeekends(date);
			if (period != null && period <= minPeriod) {
				if (a != null)
					delayedAction = a;
				return null;
			} else if (delayedAction != null) {
				delayedAction = null;
				return delayedAction;
			}
		}

		return a;
	}

	@Override
	public String chainedToString() {
		return "MinPeriod [minPeriod=" + minPeriod + "]=>"
				+ super.chainedToString();
	}

}
