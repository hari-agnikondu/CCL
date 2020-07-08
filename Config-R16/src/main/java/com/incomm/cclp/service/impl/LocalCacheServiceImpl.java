/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.incomm.cclp.config.ClpCacheConfig;


/**
 * LocalCacheServiceImpl provides the necessary operations 
 * to retrieve and store JSON attributes in local cache.
 * 
 * @author abutani
 *
 */

@Service
@CacheConfig(cacheManager="cacheManager")
public class LocalCacheServiceImpl {
	
	private  final Logger logger = LogManager.getLogger(this.getClass());

	public static final String ATTRIBUTE_DEFINITIONS = "StaticAttributeDefinitions";
	
	public static final String CARD_ATTRIBUTE_DEFINITIONS = "CardStaticAttributeDefinitions";

	public static final String ATTRIBUTE_DEFINITIONS_TABLE = "AttributeDefinitionsByOrder";
	
	public static final String CSS_AUTHENTICATION_TYPE = "CSSAuthenticationType";

	public static final String ENVIRONMENT_TYPE = "EnvironmentType";

	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.ATTRIBUTE_DEFINITIONS")
	public Map<String, Map<String, Object>> getAttributeDefinition(Map<String, Map<String, Object>> attributeMap) {
		logger.info("Adding to LOCAL Cache: " + attributeMap);
		return attributeMap;
	}
	
	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.CARD_ATTRIBUTE_DEFINITIONS")
	public Map<String, Map<String, Object>> getCardAttributeDefinition(Map<String, Map<String, Object>> attributeMap) {
		logger.info("Adding to LOCAL Cache: " + attributeMap);
		return attributeMap;
	}

	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.ATTRIBUTE_DEFINITIONS_TABLE")
	public Map<Double, String> getAttributeDefinitionByOrder(Map<Double, String> attributeMap) {
		logger.info("Adding to LOCAL Cache: " + attributeMap);
		return attributeMap;
	}
	
	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.CSS_AUTHENTICATION_TYPE")
	public Map<String, Map<String, String>> getAllAuthenticationTypes(Map<String, Map<String, String>> authenticationTypes) {
		logger.info("Adding CSS Authentication Type to LOCAL Cache: " + authenticationTypes);
		return authenticationTypes;
	}

	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.ENVIRONMENT_TYPE")
	public Map<String, String> getAllEnvironmentTypes(Map<String, String> environmentTypes) {
		logger.info("Adding Environment Types to LOCAL Cache: " + environmentTypes);
		return environmentTypes;
	}
}
