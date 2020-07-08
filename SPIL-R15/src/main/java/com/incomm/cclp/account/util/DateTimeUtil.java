package com.incomm.cclp.account.util;

import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.isNull;

// import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.exception.ServiceException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DateTimeUtil {

	private DateTimeUtil() {
		// no op
	}

	/**
	 * returns parsed ZoneDateTime object from string value in Optional wrapper. if not able to parse, it returns empty
	 * Optional.
	 * 
	 * @param value
	 * @return
	 */
	public static Optional<ZonedDateTime> parse(String value) {
		try {
			return value == null ? Optional.empty() : Optional.of(ZonedDateTime.parse(value));
		} catch (DateTimeParseException ex) {
			return Optional.empty();
		}
	}

	public static boolean compareDates(Date startDate, Date endDate) {
		return startDate.before(endDate);
	}

	public static boolean isFutureDate(Date endDate) {
		Date today = new java.sql.Date(new java.util.Date().getTime());
		return today.before(endDate);
	}

	public static boolean isPastDate(Date endDate) {
		Date today = new java.sql.Date(new java.util.Date().getTime());
		return endDate.before(today);
	}

	public static Date parseDate(String inputDate, DateTimeFormatType formatType) {
		if (isNull(inputDate)) {
			throw new ServiceException("INPUT DATE STRING IS NULL", ResponseCodes.SYSTEM_ERROR);
		}

		try {
			return formatType.getSimpleDateFormat()
				.parse(inputDate);
		} catch (ParseException e) {
			log.error("Unable to parse input date:" + inputDate, e);
			throw new ServiceException("Unable to parse input date:" + inputDate, ResponseCodes.SYSTEM_ERROR);
		}

	}

	public static String convert(Date date, DateTimeFormatType formatType) {
		if (isNull(date)) {
			return null;
		}

		return formatType.getSimpleDateFormat()
			.format(date);

	}

	public static String convert(LocalDateTime localDateTime, DateTimeFormatType formatType) {
		if (isNull(localDateTime)) {
			return null;
		}

		return localDateTime.format(formatType.getDateTimeFormatter());

	}

	public static LocalDateTime convert(ZonedDateTime zonedDatetime) {
		return zonedDatetime == null ? null
				: zonedDatetime.withZoneSameInstant(getDefaultZonedId())
					.toLocalDateTime();
	}

	public static ZonedDateTime convert(LocalDateTime localDateTime) {
		return localDateTime == null ? null : ZonedDateTime.of(localDateTime, getDefaultZonedId());
	}

	public static LocalDateTime map(java.sql.Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}

	public static java.sql.Timestamp map(LocalDateTime localDateTime) {
		return localDateTime == null ? null : java.sql.Timestamp.valueOf(localDateTime);
	}

	public static LocalDateTime formatToLocalDateTime(String format, String inputDate) {
		Date date = null;
		try {
			if (inputDate != null && !inputDate.isEmpty()) {
				DateFormat pDfrmt = new SimpleDateFormat(format);
				date = new Date(pDfrmt.parse(inputDate)
					.getTime());
				return new Timestamp(date.getTime()).toLocalDateTime();
			}
			return null;
		} catch (ParseException e) {
			log.error("Input String date parse error", e);
			throw new ServiceException("Input String date parse error", ResponseCodes.SYSTEM_ERROR);
		}
	}

	public static Date currentDate() {
		return new Date();
	}

	public static LocalDateTime currentDateTime() {
		return LocalDateTime.now();
	}

	public static String currentDateTime(DateTimeFormatType format) {
		return convert(LocalDateTime.now(), format);
	}

	public static ZonedDateTime toDefaultTimeZone(ZonedDateTime value) {
		if (isNull(value)) {
			return null;
		}

		return value.withZoneSameInstant(getDefaultZonedId());
	}

	public static boolean isEqual(ZonedDateTime date1, ZonedDateTime date2) {
		if (isNull(date1) && isNull(date2)) {
			return true;
		}
		if (isNull(date1) && isNotNull(date2) || isNotNull(date1) && isNull(date2)) {
			return false;
		}

		return date1.toInstant()
			.equals(date2.toInstant());

	}

	public static boolean isEqual(LocalDateTime date1, LocalDateTime date2) {
		if (isNull(date1) && isNull(date2)) {
			return true;
		}
		if (isNull(date1) && isNotNull(date2) || isNotNull(date1) && isNull(date2)) {
			return false;
		}

		return date1.compareTo(date2) == 0;

	}

	public static String toISOString(ZonedDateTime dateTime) {
		return dateTime == null ? null : dateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}

	public static boolean isFutureDate(LocalDateTime dateTime) {
		return dateTime.isAfter(currentDateTime());
	}

	public static boolean isPastDate(LocalDateTime dateTime) {
		return dateTime.isBefore(currentDateTime());
	}

	public static ZoneId getDefaultZonedId() {
		return ZoneId.systemDefault();
	}

}
