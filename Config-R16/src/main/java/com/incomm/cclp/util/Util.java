/**
 * 
 */
package com.incomm.cclp.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.CCLPConstants;

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

	/**
	 * 
	 * Converting a Map to JSON String
	 * @throws JsonProcessingException 
	 * 
	 * @throws Exception
	 */
	public static String mapToJson(Map<String, Map<String, Object>> productAttributes) throws JsonProcessingException  {
		ObjectMapper objectMapper = new ObjectMapper();
		String attributesJsonResp = null;
		attributesJsonResp = objectMapper.writeValueAsString(productAttributes);
		return attributesJsonResp;
	}

	
	public static Integer convertAsInteger(Object dataObject) {				
		return (dataObject != null && !String.valueOf(dataObject).isEmpty())
				? Integer.parseInt(String.valueOf(dataObject).trim())
				: Integer.parseInt("0");
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
	
	public static boolean useFilter(String filterName){
		
		return (!isEmpty(filterName) && !"*".equals(filterName)) ? true : false;
	}
	/**
	 * 
	 * Removing empty values from map of map
	
	 */
	public static Map<String, Map<String, Object>> removeEmptyValuefromMapOfMap(Map<String, Map<String, Object>> productAttributes)  {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> attributeMapWithNoEmpty = new HashMap<>();
		 Map<String, Object> attributeWithNoEmpty;
		 try {
		 for(Entry<String,Map<String, Object>> attributesMap : productAttributes.entrySet()) {
			 
			 if(!CollectionUtils.isEmpty(attributesMap.getValue())) {
				  Map<String, Object> attributes = attributesMap.getValue();
				  attributeWithNoEmpty = attributes.entrySet().stream().filter( p -> (p.getValue() != null && !Util.isEmpty(String.valueOf(p.getValue())))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
				  attributeMapWithNoEmpty.put(attributesMap.getKey(),attributeWithNoEmpty);
			 }else {
				 attributeMapWithNoEmpty.put(attributesMap.getKey(),new HashMap<>());
			 }
		 }
		 }catch(Exception e) {
			 logger.error("Error while removing the empty value" + e);
			 return productAttributes;
		 }
		 logger.info(CCLPConstants.EXIT);
		return attributeMapWithNoEmpty;
	}
	/**
	 * 
	 * Removing empty values from map
	
	 */
	public static Map<String, Object> removeEmptyValuefromMap( Map<String, Object> productAttributes){
		
		logger.info(CCLPConstants.ENTER);
		 Map<String, Object> attributeWithNoEmpty;
		try {	 
			 if(!CollectionUtils.isEmpty(productAttributes)) {
				  attributeWithNoEmpty = productAttributes.entrySet().stream().filter( p -> (p.getValue() != null && !Util.isEmpty(String.valueOf(p.getValue())))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		 }else {
			 attributeWithNoEmpty = productAttributes;
		 }
		}catch(Exception e) {
			logger.error("Error while removing empty value"+e);
			return productAttributes;
		}
		logger.info(CCLPConstants.EXIT);
		return attributeWithNoEmpty;
	}
	/**
	 * 
	 * Update values to defaultMap
	
	 */
	public static Map<String, Object> updateValuesToMap( Map<String, Object> defaultAttributes,Map<String, Object> productAttributes) {
		logger.info(CCLPConstants.ENTER);
		Map<String, Object> updatedAttributes = new HashMap<>();
		try {
			if(!CollectionUtils.isEmpty(productAttributes)) {
				defaultAttributes.entrySet().stream().forEach(
						p ->{
							updatedAttributes.put( p.getKey(),productAttributes.entrySet().stream().filter(e -> e.getKey().equals(p.getKey()))
									.map(s -> {
										if(s.getValue() == null) {
											return "";
										}
										return s.getValue();
									}).findAny().orElse(p.getValue()));
						}
						);
			}else {
				updatedAttributes.putAll(defaultAttributes);
			}
			
		}catch(Exception e) {
			logger.error("Error while updating values to attribute definition "+e);
		}
		logger.info(CCLPConstants.EXIT);
		return updatedAttributes;
	}
	/**
	 * 
	 * Update values to defaultMap
	
	 */
	public static Map<String, Map<String, Object>> updateValuesToMapOfMap( Map<String, Map<String, Object>> defaultAttributesMap,Map<String, Map<String, Object>> productAttributesMap) {
		 
		Map<String, Map<String, Object>> updatedAtributesMap = new HashMap<>();
		try {
			 
			for (Entry<String, Map<String, Object>> defaultAttributesEntry : defaultAttributesMap.entrySet()) {

				Map<String, Object> defaultAttributes = new HashMap<>(defaultAttributesEntry.getValue());
				Map<String, Object> productAttributes = productAttributesMap.get(defaultAttributesEntry.getKey());

				if (defaultAttributesEntry.getKey().equalsIgnoreCase("Card Status")) {
					updatedAtributesMap.put("Card Status", productAttributesMap.get("Card Status"));
				} else {
					updatedAtributesMap.put(defaultAttributesEntry.getKey(),
							updateValuesToMap(defaultAttributes, productAttributes));
				}
			}

		} catch (Exception e) {
			logger.error("Error while updating values to attribute definition " + e);
		}

		return updatedAtributesMap;
		
		
	}
}
