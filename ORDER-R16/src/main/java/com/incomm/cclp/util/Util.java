/**
 * 
 */
package com.incomm.cclp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;

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
		return (input == null || input.trim().isEmpty() || input.equalsIgnoreCase("null"));
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

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Map<String, Object>> productAttributes = null;

		productAttributes = objectMapper.readValue(productAttributesStr,
				new TypeReference<Map<String, Map<String, Object>>>() {
				});

		return productAttributes;

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

	// used in Order Status
	public static Set<String> csvToSet(final String lineItemIds) {
		Set<String> lineItemSet = new HashSet<>();
		if (lineItemIds != null) {
			lineItemSet = new HashSet<>(Arrays.asList(lineItemIds.split(FSAPIConstants.COMMA_SEPARATOR)));
		}

		return lineItemSet;
	}

	public static boolean setComparision(Set<?> set1, Set<?> set2) {

		if (set1 == null || set2 == null) {
			return false;
		}
		return set1.containsAll(set2);
	}
	
	 public static Map<String, String> stringToMap(String input) {
		   Map<String, String> map = new HashMap<>();

		   String[] nameValuePairs = input.split("&");
		   for (String nameValuePair : nameValuePairs) {
		    String[] nameValue = nameValuePair.split("=");
		    try {
		     map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? URLDecoder.decode(
		     nameValue[1], "UTF-8") : "");
		    } catch (UnsupportedEncodingException e) {
		     throw new RuntimeException("This method requires UTF-8 encoding support", e);
		    }
		   }

		   return map;
		  }
	 
	 public static String mapToString(Map<String, String> map) {  
		   StringBuilder stringBuilder = new StringBuilder();  
		  
		   for (String key : map.keySet()) {  
		    if (stringBuilder.length() > 0) {  
		     stringBuilder.append("&");  
		    }  
		    String value = map.get(key);  
		    try {  
		     stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));  
		     stringBuilder.append("=");  
		     stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");  
		    } catch (UnsupportedEncodingException e) {  
		     throw new RuntimeException("This method requires UTF-8 encoding support", e);  
		    }  
		   }  
		  
		   return stringBuilder.toString();  
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
		public static Map<String, String> jsonToMapCon(String st) throws IOException {

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, String> str = null;

			str = objectMapper.readValue(st,
					new TypeReference<Map<String, String>>() {
					});
			
			return str;

		}

		public static String convertAsString(Object dataObject) {
			return (dataObject != null) && !"null".equalsIgnoreCase(String.valueOf(dataObject).trim())
					&& !"".equalsIgnoreCase(String.valueOf(dataObject).trim()) ? String.valueOf(dataObject) : "";
		}
		
		public static String objectToString(Object obj) {
			
			return (!Objects.isNull(obj)? String.valueOf(obj): null);
		}
	 
}
