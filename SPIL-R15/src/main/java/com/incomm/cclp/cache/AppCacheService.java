package com.incomm.cclp.cache;

import java.util.Collections;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppCacheService {

	private Map<String, Map<String, Object>> productAttributes;

	public static AppCacheService from(Map<String, Map<String, Object>> productAttributes) {
		return new AppCacheService(productAttributes);
	}

	public Map<String, Object> getProductAttributesForGroup(String groupName) {
		return productAttributes.containsKey(groupName) ? productAttributes.get(groupName) : Collections.emptyMap();
	}

	public Object getProductAttribute(String groupName, String attributeName) {
		return getProductAttributesForGroup(groupName).get(attributeName);
	}

}
