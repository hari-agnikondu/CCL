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

import com.incomm.cclp.domain.Product;
import com.incomm.cclp.config.ClpCacheConfig;


/**
 * DistributedCacheServiceImpl provides the necessary operations 
 * to retrieve and store product attributes in distributed cache.
 * 
 * @author abutani
 *
 */

@Service
@CacheConfig(cacheManager="hazelcastCacheManager")
public class DistributedCacheServiceImpl {
	
	private  final Logger logger = LogManager.getLogger(this.getClass());
	
	
	@Cacheable(value=ClpCacheConfig.PRODUCT_ATTRIBUTE_CACHE, key="#product.productId")
	public Map<String, Map<String, Object>> getOrAddProductAttributes(
			Product product, Map<String, Map<String, Object>> productAttributes)
	{
	
		logger.info("Adding to DISTRIBUTED Cache: " + productAttributes);
		
		return productAttributes;
	}
	
	@CachePut(value=ClpCacheConfig.PRODUCT_ATTRIBUTE_CACHE, key="#product.productId")
	public Map<String, Map<String, Object>> updateProductAttributes(
			Product product, Map<String, Map<String, Object>> productAttributes)
	{
	
		logger.info("Updating DISTRIBUTED Cache: " + productAttributes);
		
		return productAttributes;
	}

}
