package com.incomm.cclpvms.constants;

import java.util.HashMap;
import java.util.Map;

public enum CronFrequency {
	DAY("Interval", "day"), 
	DAY_OF_MONTH("Day Of Month", "dayOfMonth"), 
	QUARTER("Quarter", "quarter"), 
	YEAR("Year", "year"),MONTH("Month", "month");
	
	private final String frequencyName;
	private final String frequencyShortName;
	private static final Map<String, CronFrequency> byFrequencyShortName = new HashMap<>();
	
	static {
		for (CronFrequency type : CronFrequency.values()) {
			byFrequencyShortName.put(type.frequencyShortName, type);
		}
	}

	public String getfrequencyName() {
		return frequencyName;
	}

	public String getFrequencyShortName() {
		return frequencyShortName;
	}

	private CronFrequency(String frequencyName, String frequencyShortName) {
		this.frequencyName = frequencyName;
		this.frequencyShortName = frequencyShortName;
	}
	
	public static CronFrequency byFrequencyShortName(String frequencyShortName) {
		return byFrequencyShortName.get(frequencyShortName);
	}

}
