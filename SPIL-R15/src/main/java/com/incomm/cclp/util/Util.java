/**
 * 
 */
package com.incomm.cclp.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.AccountPurseDTO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;

/**
 * Util class provides all utility methods which can be reused across the service.
 * 
 * @author abutani
 *
 */
public class Util {

	private static final Logger logger = LogManager.getLogger(Util.class);

	/**
	 * Util class should not be instantiated.
	 */
	private Util() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Checks whether an input String is empty or not. Returns true is input string is null or has 0 length.
	 * 
	 * @param input The input string to be checked.
	 * 
	 * @return boolean indicating whether input string is empty or not.
	 */
	public static boolean isEmpty(String input) {
		return (input == null || input.trim()
			.isEmpty() || input.equalsIgnoreCase("null"));
	}

	/**
	 * 
	 * Converting a Map to JSON String
	 * 
	 * @throws JsonProcessingException
	 * 
	 * @throws Exception
	 */
	public static String mapToJson(Map<String, Map<String, Object>> productAttributes) throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		String attributesJsonResp = null;
		attributesJsonResp = objectMapper.writeValueAsString(productAttributes);
		return attributesJsonResp;
	}

	public static String convertHashMapToJson(Map<String, Map<String, Object>> attributes) {
		String attributesJsonString = null;

		if (!attributes.isEmpty()) {
			ObjectMapper mapperObj = new ObjectMapper();
			try {
				attributesJsonString = mapperObj.writeValueAsString(attributes);

			} catch (JsonProcessingException e) {
				logger.warn("Unable to convert HashMap to Json: " + e.getMessage(), e);
			}
		}

		return attributesJsonString;
	}

	public static String convertMapToJson(Map<String, Object> attributes) {
		String attributesJsonString = null;

		if (attributes != null && !attributes.isEmpty()) {
			ObjectMapper mapperObj = new ObjectMapper();
			try {
				attributesJsonString = mapperObj.writeValueAsString(attributes);

			} catch (JsonProcessingException e) {
				logger.warn("Unable to convert Map to Json: " + e.getMessage(), e);
			}
		}

		return attributesJsonString;
	}

	public static Map<String, Map<String, Object>> convertJsonToHashMap(String attributesJsonString) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, Object>> productAttributes = null;

		if (!Util.isEmpty(attributesJsonString)) {
			try {
				productAttributes = mapper.readValue(attributesJsonString, new TypeReference<Map<String, Map<String, Object>>>() {
				});
			} catch (IOException e) {
				logger.warn("Unable to convert Json to HashMap: " + e.getMessage(), e);
			}
		}

		return productAttributes;
	}

	/**
	 * 
	 * Converting a JSON String to Map
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * 
	 * @throws Exception
	 */
	public static Map<String, Map<String, Object>> jsonToMap(String productAttributesStr) throws IOException {
		logger.debug(GeneralConstants.ENTER + productAttributesStr);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Map<String, Object>> productAttributes = null;

		productAttributes = objectMapper.readValue(productAttributesStr, new TypeReference<Map<String, Map<String, Object>>>() {
		});

		return productAttributes;

	}

	public static Map<String, Object> jsonToSingleMap(String productAttributesStr) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> productAttributes = null;

		productAttributes = objectMapper.readValue(productAttributesStr, new TypeReference<Map<String, Object>>() {
		});

		return productAttributes;

	}

	public static boolean useFilter(String filterName) {
		return (!isEmpty(filterName) && !"*".equals(filterName));

	}

	public static String getDateWithTimeZone(String input) {

		DateFormat pDfrmt = new SimpleDateFormat(input);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());

		return pDfrmt.format(cal.getTime());
	}

	/**
	 * to left pad the string
	 * 
	 * @param asInput   a string that has to be padded
	 * @param aiLen     required length of the string
	 * @param aiPadChar the character with which the string has to be padded
	 * @return a padded string
	 */
	public static String padLeft(String asInput, int aiLen, String aiPadChar) {
		int i = 0;
		int piLen = 0;
		String psretval = null;

		asInput = returnBlank(asInput);
		asInput = asInput.trim();
		piLen = asInput.length();
		psretval = asInput;

		for (i = 0; i < (aiLen - piLen); i++)
			psretval = aiPadChar + psretval;

		return (psretval);
	}

	/**
	 * returns the balnk string whereever a null value is got or the input string is null
	 * 
	 * @param inputVal a string which has to be compared
	 * @return the blank string
	 */
	public static String returnBlank(String inputVal) {
		if (inputVal == null) {
			return "";
		}
		if (inputVal.equals("null")) {
			return "";
		}

		return inputVal;
	}

	public static String padRight(final String str, final int length, final char c) {
		int needed;

		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}
		if (needed <= 0) {
			return str;
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		if (str != null) {
			sb.append(str);
		}
		sb.append(padding);
		return sb.toString();
	}

	public static String getProductPartialIndicator(ValueDTO valueDto) {
		logger.debug(GeneralConstants.ENTER);

		Map<String, String> valueObj = valueDto.getValueObj();

		String partialauthindicator = "N";
		Map<String, Object> productOrPurseAttributes = null;

		if (valueObj.containsKey(ValueObjectKeys.PURAUTHREQ_PURSE_NAME)
				&& !Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME))) {

			productOrPurseAttributes = valueDto.getProductAttributes()
				.get(ValueObjectKeys.PURSE);

			if (SpilTranConstants.SPIL_CARD_REDEMPTION.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				partialauthindicator = String.valueOf(productOrPurseAttributes.get(ValueObjectKeys.PURSE_REDEEM_SPLIT_TENDER));
			} else if (SpilTranConstants.SPIL_REDEMPTION_AUTH_LOCK.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				partialauthindicator = String.valueOf(productOrPurseAttributes.get(ValueObjectKeys.PURSE_REDEEMLOCK_SPLIT_TENDER));
			}
		} else {

			productOrPurseAttributes = valueDto.getProductAttributes()
				.get(ValueObjectKeys.GENERAL);

			if (SpilTranConstants.SPIL_CARD_REDEMPTION.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				partialauthindicator = String.valueOf(productOrPurseAttributes.get(ValueObjectKeys.REDEEM_SPLIT_TENDER));
			} else if (SpilTranConstants.SPIL_REDEMPTION_AUTH_LOCK.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				partialauthindicator = String.valueOf(productOrPurseAttributes.get(ValueObjectKeys.REDEEMLOCK_SPLIT_TENDER));
			}
		}
		logger.debug(GeneralConstants.EXIT);
		return (partialauthindicator.equalsIgnoreCase("true")) ? "Y" : "N";
	}

	public static String convertAsString(Object dataObject) {
		return (dataObject != null) && !"null".equalsIgnoreCase(String.valueOf(dataObject)
			.trim()) && !"".equalsIgnoreCase(
					String.valueOf(dataObject)
						.trim()) ? String.valueOf(dataObject) : "";
	}

	public static Date formatDate(String format, String inputDate) throws ServiceException {
		Date date = null;
		try {

			if (format == null) {
				throw new ServiceException("INPUT DATE STRING IS NULL", ResponseCodes.SYSTEM_ERROR);
			}
			if (inputDate != null && !inputDate.isEmpty() && !"null".equalsIgnoreCase(inputDate)) {

				DateFormat pDfrmt = new SimpleDateFormat(format);
				date = new Date(pDfrmt.parse(inputDate)
					.getTime());

			} else {
				date = new Date(System.currentTimeMillis());
			}
		} catch (ParseException e) {
			logger.warn("Unable to parse Date" + e.getMessage(), e);

			throw new ServiceException("Input String date parse error: ", ResponseCodes.SYSTEM_ERROR);
		}
		return date;
	}

	public static String getMaskCardNum(String cardNum) {
		String maskCardNum = "";
		int startIndex = 6;
		int endIndex = 4;
		if (!cardNum.isEmpty()) {
			char[] charArray = cardNum.toCharArray();
			for (int i = startIndex; i < cardNum.substring(startIndex, cardNum.length() - endIndex)
				.length() + startIndex; i++) {
				charArray[i] = '*';
			}
			maskCardNum = String.valueOf(charArray);
		}
		return maskCardNum;
	}

	public static int getMonth(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// since calendar class gives month as 0-jan 1-feb,we add 1 before returning
		// month
		return (calendar.get(Calendar.MONTH) + 1);
	}

	public static int getYear(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static String formatExpDate(String expDate) throws ParseException {
		String year = expDate.substring(0, 2);
		String month = expDate.substring(2);
		DateFormat pDfrmt = new SimpleDateFormat("MM-dd-yy");
		Date date = new Date(pDfrmt.parse(month + "-01-" + year)
			.getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new SimpleDateFormat("dd-MMM-yy").format(cal.getTime());
	}

	// to check valid timestamp
	public static boolean isValid(String inputDateTime, String format) {
		java.util.Date dateTime = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setLenient(false);
			dateTime = sdf.parse(inputDateTime);
			if (!inputDateTime.equals(sdf.format(dateTime))) {
				dateTime = null;
			}
		} catch (ParseException ex) {
			logger.warn("Invalid dateTime inputted::" + ex.getMessage(), ex);
		}
		return dateTime == null ? false : true;

	}

	public static String formatDtOrCurrDate(String inputDateTime, String requiredFormat, String existingFormat) {
		java.util.Date dateTime = null;
		try {

			SimpleDateFormat sdf = new SimpleDateFormat(existingFormat);
			sdf.setLenient(false);
			if (inputDateTime == null || inputDateTime.isEmpty()) {
				dateTime = new Date(System.currentTimeMillis());
			} else {
				dateTime = sdf.parse(inputDateTime);
			}
		} catch (ParseException ex) {

			logger.warn("Error in parsing Date::" + ex.getMessage(), ex);
		}

		return new SimpleDateFormat(requiredFormat).format(dateTime)
			.toUpperCase();
	}

	public static String formatDt(String inputDateTime, String requiredFormat, String existingFormat) {
		java.util.Date dateTime = null;
		try {

			SimpleDateFormat sdf = new SimpleDateFormat(existingFormat);
			sdf.setLenient(false);
			if (inputDateTime != null && !inputDateTime.isEmpty()) {
				dateTime = sdf.parse(inputDateTime);
			}
		} catch (ParseException ex) {

			logger.warn("Error in parsing Date::" + ex.getMessage(), ex);
		}

		return new SimpleDateFormat(requiredFormat).format(dateTime)
			.toUpperCase();
	}

	/*
	 * This method is used to convert the string to Long when the value had an digits.If not return 0.
	 */
	public static Long convStringToLong(String input) {
		Long longVal;
		if (Util.isEmpty(input)) {
			longVal = 0L;
		} else if (!input.matches("\\d*")) {
			longVal = 0L;
		} else {
			longVal = Long.parseLong(input);
		}
		return longVal;
	}

	/*
	 * This method is used to convert the date format into MMDDYYYY format.
	 */
	public static Date getMMDDYYYY(String inputDate) {
		Date dateMMDDYYY = null;
		try {
			dateMMDDYYY = new java.sql.Date(new SimpleDateFormat(GeneralConstants.MMDDYYYY).parse(inputDate)
				.getTime());
		} catch (ParseException e) {

			logger.warn("Error in parsing Date::" + e.getMessage(), e);
		}
		return dateMMDDYYY;
	}

	/*
	 * This method is used to get the current System Date
	 */
	public static Long getCurrentDate() {
		return new java.util.Date().getTime();
	}

	/*
	 * This method is used to build the Purse Auth Respose in the SPIL response message if the request contains
	 * PurAuthREq
	 */
	public static StringBuilder getPurseAuthResp(Map<String, String> hKeyValue, String availableBalance, String accountPurseId) {
		StringBuilder response = new StringBuilder();
		if (hKeyValue.containsKey(ValueObjectKeys.PURAUTHREQ)) {
			response.append(accountPurseId);
			response.append('|')
				.append(hKeyValue.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME));
			response.append('|')
				.append(hKeyValue.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT)); // Authorized Amount
			response.append('|')
				.append(availableBalance);
			response.append('|')
				.append(hKeyValue.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
			response.append('|')
				.append(hKeyValue.get(ValueObjectKeys.PURAUTHREQ_SKUCODE));
		}
		return response;
	}

	public static String getPurseAuthResp(AccountPurseDTO purAuthResp) {
		StringBuilder response = new StringBuilder();
		if (purAuthResp != null) {
			response.append(purAuthResp.getAccountPurseId());
			response.append('|')
				.append(purAuthResp.getPurseName());
			response.append('|')
				.append(purAuthResp.getTransactionAmount());
			response.append('|')
				.append(purAuthResp.getAuthAmount());
			response.append('|')
				.append(purAuthResp.getPurseType());
			response.append('|')
				.append(purAuthResp.getSkuCode());
		}
		return response.toString();
	}

	public static Object getProductAttributeValue(Map<String, Map<String, Object>> productAttributes, String attributeGroup,
			String attributeName) {
		Map<String, Object> attributeGroupMap = productAttributes.get(attributeGroup);
		if (attributeGroupMap != null)
			return attributeGroupMap.get(attributeName);
		else
			return null;
	}

	/*
	 * This method is used to convert the date format into MMDDYYYY format.
	 */
	public static LocalDateTime getMMDDYYYYHHMMSS(String inputDate) {
		LocalDateTime dateMMDDYYYHHMMSS = null;
		try {
			dateMMDDYYYHHMMSS = LocalDateTime.parse(inputDate, DateTimeFormatter.ofPattern(GeneralConstants.MMDDYYYYHHMMSS));
		} catch (Exception e) {

			logger.warn("Error in parsing Date::" + e.getMessage(), e);
		}
		return dateMMDDYYYHHMMSS;
	}

	public static BigDecimal getScaledAmount(double amount, int currencyMinorUnits) {
		return new BigDecimal(amount).setScale(currencyMinorUnits);
	}

}
