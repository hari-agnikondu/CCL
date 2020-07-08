package com.incomm.cclp.account.util;

import static com.incomm.cclp.account.util.CodeUtil.isNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.config.SpringContext;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JsonUtil {

	private static final String EMPTY_MAP_JSON_STRING = "{}";

	private JsonUtil() {
	}

	/**
	 * Maps the json string to Object Map and returns the Map for given atributeName
	 * 
	 * @param jsonString
	 * @param attributeName
	 * @return
	 */
	public static Map<String, Object> jsontoMap(String jsonString) {

		if (CodeUtil.isNullOrEmpty(jsonString) || EMPTY_MAP_JSON_STRING.equals(jsonString)) {
			log.debug("Empty JSON string in input.");
			return new HashMap<>();
		}

		ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

		try {
			return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
			});
		} catch (IOException ioException2) {
			log.warn("Unable to parase JSON string to object, error: {}, jsonString: {}", ioException2.getMessage(), jsonString);
		}

		return new HashMap<>();

	}

	public static String convertMapToJson(Map<String, Object> attributes) {
		String attributesJsonString = null;

		if (CodeUtil.isNullOrEmpty(attributes)) {

			ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

			try {

				attributesJsonString = objectMapper.writeValueAsString(attributes);

			} catch (JsonProcessingException e) {
				log.error(e.getMessage());
			}
		}
		return attributesJsonString;
	}

	public static <T> String mapToJson(T t) {
		if (isNull(t)) {
			return null;
		}

		ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

		try {

			return objectMapper.writeValueAsString(t);

		} catch (JsonProcessingException e) {
			log.error("Unable to convert object to json string:{}", t);
		}

		return null;
	}

	public static <T> T mapToObject(String jsonString, Class<T> objectClass) {

		if (isNull(jsonString)) {
			return null;
		}

		ObjectMapper objectMapper = SpringContext.getBean(ObjectMapper.class);

		try {
			return objectMapper.readValue(jsonString, objectClass);
		} catch (IOException ioException) {
			log.warn("Unable to parase JSON string to object, error: {}, jsonString: {}", ioException.getMessage(), jsonString);
		}

		return null;
	}

}