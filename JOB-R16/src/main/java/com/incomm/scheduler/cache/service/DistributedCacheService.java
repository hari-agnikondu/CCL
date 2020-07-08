package com.incomm.scheduler.cache.service;

import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheManager = "hazelcastCacheManager")
public class DistributedCacheService {
	
	public static final String PRODUCT_ATTRIBUTE_CACHE = "ProductAttribute";
	
	@Cacheable(value = PRODUCT_ATTRIBUTE_CACHE, key = "#productId")
	public Map<String, Map<String, Object>> getProductAttributesCache(Long productId, Map<String, Map<String, Object>> productAttributes) {

		return productAttributes;
	}

}
