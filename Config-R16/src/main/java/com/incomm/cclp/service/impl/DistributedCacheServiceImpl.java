/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.incomm.cclp.config.ClpCacheConfig;

/**
 * DistributedCacheServiceImpl provides the necessary operations to retrieve and
 * store product attributes in distributed cache.
 * 
 * @author abutani
 *
 */

@Service
@CacheConfig(cacheManager = "hazelcastCacheManager")
public class DistributedCacheServiceImpl {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@CachePut(value = ClpCacheConfig.PRODUCT_ATTRIBUTE_CACHE, key = "#productId")
	public Map<String, Map<String, Object>> updateProductAttributesCache(Long productId,
			Map<String, Map<String, Object>> productAttributes) {

		logger.info("Updating DISTRIBUTED Product Cache : " + productAttributes);

		return productAttributes;
	}

	@Cacheable(value = ClpCacheConfig.PRODUCT_ATTRIBUTE_CACHE, key = "#productId")
	public Map<String, Map<String, Object>> getOrAddProductAttributesCache(Long productId,
			Map<String, Map<String, Object>> productAttributes) {

		logger.info("Adding to DISTRIBUTED Product Cache: " + productAttributes);

		return productAttributes;
	}
	

	@CachePut(value = ClpCacheConfig.PRODUCT_PURSE_ATTRIBUTE_CACHE, key = "#productpurse")
	public Map<String, Map<String, Object>> updateProductPurseAttributesCache(String productpurse,
			Map<String, Map<String, Object>> productPurseAttributes) {

		logger.info("Updating DISTRIBUTED Product Purse Cache : " + productPurseAttributes);

		return productPurseAttributes;
	}

}
