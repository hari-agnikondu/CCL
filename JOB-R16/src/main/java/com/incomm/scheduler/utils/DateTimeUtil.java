package com.incomm.scheduler.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DateTimeUtil {

	private DateTimeUtil() {
		// Noop
	}

	public static LocalDateTime map(java.sql.Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}

	public static java.sql.Timestamp map(LocalDateTime localDateTime) {
		return localDateTime == null ? null : java.sql.Timestamp.valueOf(localDateTime);
	}
	
	public static LocalDate getMMDDYYYY(String inputDate) {
		LocalDate dateMMDDYYY = null;
		try {
			dateMMDDYYY = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern(JobConstants.DATE_FORMAT));
		} catch (Exception e) {
			
			log.warn("Error in parsing Date::" + e.getMessage(), e);
		}
		return dateMMDDYYY;
	}
	
	public static LocalDateTime getMMDDYYYYHHMMSS(String inputDate) {
		LocalDateTime dateMMDDYYYHHMMSS = null;
		try {
			dateMMDDYYYHHMMSS = LocalDateTime.parse(inputDate, DateTimeFormatter.ofPattern(JobConstants.DATETIME_FORMAT));
		} catch (Exception e) {
			
			log.warn("Error in parsing Date::" + e.getMessage(), e);
		}
		return dateMMDDYYYHHMMSS;
	}
	
	
	public static String getConvertedTimeZone(String timeZone, String dateTime) {

		ZoneId estZoneId = ZoneId.systemDefault();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(JobConstants.DATETIME_FORMAT)
				.withZone(ZoneId.of(timeZone));
		ZonedDateTime zdt = ZonedDateTime.parse(dateTime, dtf);
		DateTimeFormatter zonedDateTime = DateTimeFormatter.ofPattern(JobConstants.DATETIME_FORMAT)
				.withZone(estZoneId);
		return zdt.format(zonedDateTime);
	}
	
	public static String convertLocalDateToString(LocalDate inputDate) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern(JobConstants.DATE_FORMAT);
		return inputDate.format(formatter);
	}

}
