package com.incomm.cclpvms.util;

import java.util.List;

public class CronSchedule {

	private String frequency;
	private int interval;
	private List<Integer> dayOfMonth;
	private int month;
	private String cron;
	private String nextRunDate;

	public String getNextRunDate() {
		return nextRunDate;
	}

	public void setNextRunDate(String nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<Integer> getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(List<Integer> dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

}
