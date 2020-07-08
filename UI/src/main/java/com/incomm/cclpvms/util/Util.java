/**
 * 
 */
package com.incomm.cclpvms.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap.SimpleEntry;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.controller.ProductController;
import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.constants.CCLPConstants;

/**
 * Util class provides all utility methods which can be reused across the service.
 * 
 * @author abutani
 *
 */
public class Util {


	/**
	 * Util class should not be instantiated.
	 */
	private Util() 
	{
		throw new IllegalStateException("Utility class");
	}

	private static final Logger logger = LogManager.getLogger(Util.class);
	
	/**
	 * Checks whether an input String is empty or not.
	 * Returns true is input string is null or has 0 length.
	 * 
	 * @param input The input string to be checked.
	 * 
	 * @return boolean indicating whether input string is empty or not.
	 */
	public static boolean isEmpty(String input)
	{
		return (input == null || input.trim().isEmpty());
	}


	public static Map<String, Map<String, Object>> convertJsonToHashMap(String  attributesJsonString)
	{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, Object>> productAttributes = null;

		if (!Util.isEmpty(attributesJsonString)) {
			try {
				productAttributes = mapper.readValue(attributesJsonString,
						new TypeReference<Map<String, Map<String, Object>>>() {
				});
			}catch (IOException e) {
				e.getMessage();
			}
		}

		return productAttributes;
	}



	public static Map<String, Map<String, String>> convertJsonStringToHashMap(String  attributesJsonString)
	{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, String>> productAttributes = null;

		if (!Util.isEmpty(attributesJsonString)) {
			try {
				productAttributes = mapper.readValue(attributesJsonString,
						new TypeReference<Map<String, Map<String, Object>>>() {
				});
			}catch (IOException e) {
				e.getMessage();
			}
		}

		return productAttributes;
	}


	/**
	 * Convert the input date to output date format.
	 * 
	 * @param inputDateStr The input date to be parsed.
	 * @param inputDateFormat The input date format.
	 * @param outputDateFormat The output date format.
	 * 
	 * @return the output date in the desired format.
	 */
	public static String convertDateFormat(String inputDateStr, 
			String inputDateFormat, String outputDateFormat)
	{

		DateTimeFormatter dateTimeFormatter = 
				new DateTimeFormatterBuilder().appendPattern(inputDateFormat)
				.parseLenient()
				.appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).
				toFormatter();
		
		String outputDateStr = "";

		if (isEmpty(inputDateStr))
			return outputDateStr;
		else 
		{
			LocalDateTime ldt = LocalDateTime.parse(inputDateStr, dateTimeFormatter);

			ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());        
			outputDateStr = DateTimeFormatter.ofPattern(outputDateFormat).format(zdt);
		}

		return outputDateStr;
	}
	
	
	/**
	 * If the input string is null or has the value 'null' return blank.
	 * 
	 * @param inputVal The input string to be checked.
	 * 
	 * @return the appropriate string
	 */
	public static String returnBlank(String inputVal) {
		
		if (inputVal == null || inputVal.equalsIgnoreCase("null")) {
			return "";
		}

		return inputVal;
	}
	
	
	public static String convertAsString(Object dataObject) {
		return (dataObject != null) && !"null".equalsIgnoreCase(String.valueOf(dataObject).trim())
				&& !"".equalsIgnoreCase(String.valueOf(dataObject).trim()) ? String.valueOf(dataObject) : "";
	}


	public static String convertAsLong(Object dataObject) {
		return (dataObject != null) && !"null".equalsIgnoreCase(String.valueOf(dataObject).trim())
				&& !"".equalsIgnoreCase(String.valueOf(dataObject).trim()) ? String.valueOf(dataObject) : "0";
	}
	
	public static String constructUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}


	public static Map<String, String> getTimeZones() {

		boolean sortByRegion = false;
		LocalDateTime localDateTime = LocalDateTime.now();
		Map<String, String> timezones = new LinkedHashMap<>();

		long total = ZoneId.getAvailableZoneIds().stream().map(ZoneId::of)
				.map(zoneId -> new SimpleEntry<>(zoneId.toString(),
						localDateTime.atZone(zoneId).getOffset().getId().replaceAll("Z", "+00:00")))
				.sorted(sortByRegion ? Map.Entry.comparingByKey()
						: Map.Entry.<String, String>comparingByValue().reversed())
				.peek(e -> timezones.put(e.getKey(), String.format("%35s (UTC %s) %n", e.getValue(), e.getKey())))
				.count();
		logger.info("Total number of time zones: {} ", total);
		return timezones;
	}


	public static String getConvertedTimeZone(String timeZone, String dateTime) {

		ZoneId estZoneId = ZoneId.systemDefault();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CCLPConstants.TIMEZONE_TIME_FORMAT)
				.withZone(ZoneId.of(timeZone));
		ZonedDateTime zdt = ZonedDateTime.parse(dateTime, dtf);
		DateTimeFormatter zonedDateTime = DateTimeFormatter.ofPattern(CCLPConstants.TIMEZONE_TIME_FORMAT)
				.withZone(estZoneId);
		return zdt.format(zonedDateTime);
	}

	public static LocalDateTime getLocalDateTime(String date) {
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern(CCLPConstants.TIMEZONE_TIME_FORMAT);
		return LocalDateTime.parse(date, sdf);

	}
	
	
}
