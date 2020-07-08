package com.incomm.scheduler.utils;

import java.util.Calendar;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

@Component
public class GenUtils 
{
	private GenUtils() {}
	public static java.sql.Date converToDate(String inputDate)
	{
		int year = 0;
		int month = 0;
		int day = 0;
		StringTokenizer poSt = new StringTokenizer(inputDate, "/ : - ");

		if (poSt.hasMoreElements())
			month = Integer.parseInt(poSt.nextToken());
		if (poSt.hasMoreElements())
			day = Integer.parseInt(poSt.nextToken());
		if (poSt.hasMoreElements())
			year = Integer.parseInt(poSt.nextToken());

		return (getSQLDate(day, month, year));
	}

	public static java.sql.Date getSQLDate(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		// since calendar constructs takes 0 as jan we subtract 1 from Month
		// provided
		calendar.set(year, month - 1, day);
		java.util.Date utilDate = calendar.getTime();
		long time = utilDate.getTime();
		return new java.sql.Date(time);
	}

}
