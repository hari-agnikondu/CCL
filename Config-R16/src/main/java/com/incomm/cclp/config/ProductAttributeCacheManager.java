package com.incomm.cclp.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.AttributeDefinitionService;

/**
 * ProductAttributeCacheManager prepopulate Default attributes to Cache
 * with CacheName as 'AttributeDefinition' and key as 'ProductAttribute'.
 * 
 * @author ulagan
 *
 */

@Configuration
public class ProductAttributeCacheManager {

	@Autowired
	AttributeDefinitionService attributeDefinitionService;

	@Autowired
	CacheManager cacheManager;
	
	public static final String ATTRIBUTE_DEFINITIONS = "StaticAttributeDefinitions";

	public static final String ATTRIBUTE_DEFINITIONS_TABLE = "AttributeDefinitionsByOrder";

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	/**
	 * Prepopulate default attributes to cache
	 * 
	 */
	@PostConstruct
	public void loadAttributeDefinitionToLocalCache() {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> attributeMap = attributeDefinitionService.getAllAttributeDefinitions();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE)
			.put(ATTRIBUTE_DEFINITIONS, attributeMap);

		logger.debug("Adding to LOCAL Cache on startup : " + attributeMap);
		logger.info(CCLPConstants.EXIT);
	}

	@PostConstruct
	public void loadattributeDefinitionByOrderLocalCache() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<Double, String> attributeMap = attributeDefinitionService.getAttributeDefinitionsByOrder();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(ATTRIBUTE_DEFINITIONS_TABLE, attributeMap);

		logger.debug("Adding to LOCAL Cache on startup : " + attributeMap);
		logger.info(CCLPConstants.EXIT);
	}

}
