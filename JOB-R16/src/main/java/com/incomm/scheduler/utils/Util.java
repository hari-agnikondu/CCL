/**
 * 
 */
package com.incomm.scheduler.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.model.ProcessSchedule;

/**
 * Util class provides all utility methods which can be reused across the
 * service.
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
	 * Checks whether an input String is empty or not. Returns true is input string
	 * is null or has 0 length.
	 * 
	 * @param input
	 *            The input string to be checked.
	 * 
	 * @return boolean indicating whether input string is empty or not.
	 */
	public static boolean isEmpty(String input) {
		return (input == null || input.trim().isEmpty());
	}

	public static boolean isEmptyString(String input) {
		return (input == null || input.trim().isEmpty() || "null".equals(input));
	}

	public static boolean useFilter(String filterName) {
		return (!isEmpty(filterName) && !"*".equals(filterName));
	}

	/**
	 * Checks whether input profile contains a local profile or not.
	 * 
	 * @param inputProfiles
	 *            An array of input profiles to be checked.
	 * 
	 * @return boolean indicating whether input profile has local or not.
	 */
	public static boolean hasLocalProfile(String[] inputProfiles) {

		if (inputProfiles == null || inputProfiles.length == 0)
			return false;

		for (String inputProfile : inputProfiles) {
			if (inputProfile.equalsIgnoreCase("local") || inputProfile.equalsIgnoreCase("dev"))
				return true;
		}

		return false;
	}

	/**
	 * This method gives the CRON Expression for scheduling jobs using screen
	 * configuration.
	 * 
	 * @param processSchedule
	 * @return Cron Expression author venkateshgaddam
	 */
	public static String getCronExp(ProcessSchedule processSchedule) {
		StringBuilder cronExp = new StringBuilder();
		try {
			if (processSchedule.getSchedularStatus() != null && "E".equals(processSchedule.getSchedularStatus())) {

				if (processSchedule.getMultiRunFlag() != null && "1".equals(processSchedule.getMultiRunFlag())
						&& Integer.parseInt(processSchedule.getMultiRunInterval()) > 0) {
					if ("HH".equals(processSchedule.getMultiRunIntervalType())) {
						cronExp.append(processSchedule.getStartSS()).append(" * ").append("*").append("/")
								.append(processSchedule.getMultiRunInterval());
					} else {
						cronExp.append(processSchedule.getStartSS()).append(" ").append("*").append("/")
								.append(processSchedule.getMultiRunInterval()).append(" * ");
					}
				} else {
					cronExp.append(processSchedule.getStartSS()).append(" ").append(processSchedule.getStartMM())
							.append(" ").append(processSchedule.getStartHH());
				}
				if (processSchedule.getDayOfMonth() != null) {
					String dayOfMonth = "1";
					if ("2".equals(processSchedule.getDayOfMonth())) {
						dayOfMonth = String.valueOf(Calendar.getInstance().getActualMaximum(Calendar.DATE));
					}
					cronExp.append(" ").append(dayOfMonth).append(" * ?");
				} // if
				else if (processSchedule.getScheduleDays() != null) {
					String scheduleDays = processSchedule.getScheduleDays().replaceAll("\\|", ",");
					if (scheduleDays.contains("ALL")) {
						scheduleDays = "SUN,MON,TUE,WED,THU,FRI,SAT";
					}
					cronExp.append(" * * ").append(scheduleDays);
				} // else if
			} else {
				logger.info("Scheduler configuration is not Enabled for " + processSchedule.getProcessName() + " job");
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return cronExp.toString();

	}

	public static void moveToArchive(File rootDir, String fileName) {
		File delDir = new File(rootDir + File.separator + JobConstants.CN_DEL_FILE_DIR);

		if (!delDir.exists()) {
			delDir.mkdirs();
		}
		File file = new File(rootDir + File.separator + fileName);
		String newDir = rootDir + File.separator + JobConstants.CN_DEL_FILE_DIR + File.separator + fileName;
		file.renameTo(new File(newDir));
		if (file.delete()) {
			logger.info("File moved successfully:" + fileName);
		}

	}
	public static void moveFromTempToArchive(File rootDir,File rootDelDir, String fileName) {
		File delDir = new File(rootDelDir + File.separator + JobConstants.CN_DEL_FILE_DIR);

		if (!delDir.exists()) {
			delDir.mkdirs();
		}
		File file = new File(rootDir + File.separator + fileName);
		String newDir = rootDelDir + File.separator + JobConstants.CN_DEL_FILE_DIR + File.separator + fileName;
		// renaming the file and moving it to a new location
		file.renameTo(new File(newDir));
		// if file copied successfully then delete the original file
		if (file.delete()) {
			logger.info("File moved successfully:" + fileName);
		}

	}
	
	public static void moveToTempDir(File rootDir, String fileName) {
		File delDir = new File(rootDir + File.separator + JobConstants.TEMP_FILE_DIR);

		if (!delDir.exists()) {
			delDir.mkdirs();
		}
		File file = new File(rootDir + File.separator + fileName);
		String newDir = rootDir + File.separator + JobConstants.TEMP_FILE_DIR + File.separator + fileName;
		file.renameTo(new File(newDir));
		if (file.delete()) {
			logger.info("File moved successfully:" + fileName);
		}

	}

	public static void RemoveFile(File rootDir, String fileName) {
		
		File file = new File(rootDir + File.separator + fileName);

		if (file.exists()) {
			if (file.delete()) {
				logger.info("File Deleted successfully:" + fileName);
			}

			else {
				logger.debug("Failed to Delete the file");
			}
		}else {
			logger.error("File does not exist");
		}

	}

	public static Map<String, String> stringToMap(String input) throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<>();

		String[] nameValuePairs = input.split("&");
		for (String nameValuePair : nameValuePairs) {
			String[] nameValue = nameValuePair.split("=");
			try {
				map.put(URLDecoder.decode(nameValue[0], "UTF-8"),
						nameValue.length > 1 ? URLDecoder.decode(nameValue[1], "UTF-8") : "");
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
				throw e;
			}
		}

		return map;
	}

	public static BigDecimal convertAsBigDecimal(Object dataObject) {
		return (dataObject != null && !String.valueOf(dataObject).isEmpty())
				? new BigDecimal(String.valueOf(dataObject).trim().replace(",", ""))
				: new BigDecimal("0.00");
	}

	public static String convertAsString(Object dataObject) {
		return (dataObject != null) && !"null".equalsIgnoreCase(String.valueOf(dataObject).trim())
				&& !"".equalsIgnoreCase(String.valueOf(dataObject).trim()) ? String.valueOf(dataObject) : "";
	}

	public static String convertHashMapToJson(Map<String, Map<String, Object>> attributes) {
		String attributesJsonString = null;

		if (!attributes.isEmpty()) {
			ObjectMapper mapperObj = new ObjectMapper();
			try {
				attributesJsonString = mapperObj.writeValueAsString(attributes);

			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
			}
		}

		return attributesJsonString;
	}

	public static Map<String, Map<String, Object>> convertJsonToHashMap(String attributesJsonString) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Map<String, Object>> productAttributes = null;

		if (!Util.isEmpty(attributesJsonString)) {
			try {
				productAttributes = mapper.readValue(attributesJsonString,
						new TypeReference<Map<String, Map<String, Object>>>() {
						});
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		return productAttributes;
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

	public static String padRight(final String str, final int length, final char c, final boolean upperCaseFlag) {
		int needed;

		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}
		if (needed <= 0 && str != null) {
			return str.substring(0, str.length() + needed);
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		if (str != null) {
			sb.append(str);
		}
		sb.append(padding);
		if (upperCaseFlag) {
			return sb.toString().toUpperCase();
		} else {
			return sb.toString();
		}
	}

	public static String padLeft(final String str, final int length, final char c) {
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
		sb.append(padding);
		if (str != null) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static int getYear(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// since calenadar class gives month as 0-jan 1-feb,we add 1 before returning
		// month
		return (calendar.get(Calendar.MONTH) + 1);
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
		logger.debug(CCLPConstants.ENTER);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Map<String, Object>> productAttributes = null;

		productAttributes = objectMapper.readValue(productAttributesStr, new TypeReference<Map<String, Map<String, String>>>() {
		});

		logger.debug(CCLPConstants.EXIT);
		return productAttributes;

	}
	
	public static Object getProductAttributeValue(Map<String, Map<String, Object>> productAttributes,String attributeGroup,String attributeName) {
		Map<String,Object> attributeGroupMap = productAttributes.get(attributeGroup);
		if(attributeGroupMap != null)
			return attributeGroupMap.get(attributeName);
		else
			return null;
	}
}
