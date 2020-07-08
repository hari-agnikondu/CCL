package com.incomm.cclp.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Cache Configuration for the CCLP project.
 * Includes configuration for both Hazelcast for distributed caching 
 * and Caffeine for in memory caching.
 * 
 * @author abutani
 *
 */

/*
 * ramaprabhur - Commented @Configuration for development purpose, uncomment once hazelcast dependency is resolved
 * */
//@Configuration
public class ClpCacheConfig {
	
	@Value("${hazelcast.groupName}")
	private String groupName;
	
	@Value("${hazelcast.groupPass}")
	private String groupPass;
	
	@Value("${hazelcast.member}")
	private String hazelcastMember;
	
	public static final String ATTRIBUTE_DEFINITION_CACHE = "AttributeDefinition";
	
	public static final String PRODUCT_ATTRIBUTE_CACHE = "ProductAttribute";
	
	
    @Bean(name="cacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {
    	
    	SimpleCacheManager cacheManager = new SimpleCacheManager();
    	
        CaffeineCache attributeDefinitionCache = new CaffeineCache(ATTRIBUTE_DEFINITION_CACHE, 
        		Caffeine.newBuilder()
                .maximumSize(400)
                .build());
    	
    	cacheManager.setCaches(Arrays.asList(attributeDefinitionCache));
    	
    	return cacheManager;
    }
	

}
