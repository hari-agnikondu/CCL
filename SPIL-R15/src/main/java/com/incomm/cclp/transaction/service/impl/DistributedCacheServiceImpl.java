/**
 * 
 */
package com.incomm.cclp.transaction.service.impl;

import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.incomm.cclp.transaction.bean.RuleSet;

@Service
@CacheConfig(cacheManager = "hazelcastCacheManager")
public class DistributedCacheServiceImpl {

	public static final String PRODUCT_ATTRIBUTE_CACHE = "ProductAttribute";
	public static final String PRODUCT_PURSE_ATTRIBUTE_CACHE = "ProductPurseAttribute";
	public static final String RULESET_CACHE = "ruleSet";

	@Cacheable(value = PRODUCT_ATTRIBUTE_CACHE, key = "#productId")
	public Map<String, Map<String, Object>> getProductAttributesCache(Long productId, Map<String, Map<String, Object>> productAttributes) {

		return productAttributes;
	}

	/*
	 * This method is used to the get/put product purse attributes from Cache.
	 * 
	 * @Raja
	 */
	@Cacheable(value = PRODUCT_PURSE_ATTRIBUTE_CACHE, key = "#productpurse")
	public Map<String, Map<String, Object>> getProductPurseAttributesCache(String productpurse,
			Map<String, Map<String, Object>> productPurseAttributes) {
		return productPurseAttributes;
	}

	@Cacheable(value = RULESET_CACHE, key = "#ruleSetId")
	public RuleSet getOrAddRuleSet(Long ruleSetId, RuleSet ruleSet) {

		return ruleSet;
	}

}
