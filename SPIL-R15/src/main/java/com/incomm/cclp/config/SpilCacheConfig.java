package com.incomm.cclp.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.incomm.cclp.transaction.service.impl.DistributedCacheServiceImpl;

/**
 * Cache Configuration for the spil project. Includes configuration for both Hazelcast for distributed caching and
 * Caffeine for in memory caching.
 * 
 * @author abutani
 *
 */

@Profile("!test")
@Configuration
public class SpilCacheConfig {

	@Value("${hazelcast.groupName}")
	private String groupName;

	@Value("${hazelcast.groupPass}")
	private String groupPass;

	@Value("${hazelcast.member}")
	private String hazelcastMember;

	/** added for keeping hazelcast alive */

	@Value("${hazelcast.attemptLimit}")
	private int attemptList;

	@Value("${hazelcast.attemptPeriod}")
	private int attemptPeriod;

	/** added for keeping hazelcast alive ends */

	public static final String SPIL_DATA_CACHE = "SpilDataCache";

	@Bean(name = "cacheManager")
	@Primary
	public CacheManager caffeineCacheManager() {

		SimpleCacheManager cacheManager = new SimpleCacheManager();

		CaffeineCache dataValidationsDefinitionCache = new CaffeineCache(SPIL_DATA_CACHE, Caffeine.newBuilder()
			.maximumSize(400)
			.build());

		cacheManager.setCaches(Arrays.asList(dataValidationsDefinitionCache));

		return cacheManager;
	}

	@Bean(name = "hazelcastCacheManager")
	public CacheManager hazelcastCacheManager() {

		return new HazelcastCacheManager(hazelcastInstance());
	}

	@Bean
	public HazelcastInstance hazelcastInstance() {

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getGroupConfig()
			.setName(groupName)
			.setPassword(groupPass);
		clientConfig.getNetworkConfig()
			.addAddress(hazelcastMember);

		/** added for keeping hazelcast alive --08-11-2018 **/
		clientConfig.getNetworkConfig()
			.setConnectionAttemptLimit(attemptList);
		// 0 value means try forever
		clientConfig.getNetworkConfig()
			.setConnectionAttemptPeriod(attemptPeriod);
		/** added for keeping hazelcast alive --08-11-2018 ends **/

		// Configure Near Cache
		NearCacheConfig nearCacheConfig = new NearCacheConfig();
		nearCacheConfig.setName(DistributedCacheServiceImpl.PRODUCT_ATTRIBUTE_CACHE);
		clientConfig.addNearCacheConfig(nearCacheConfig);

		return HazelcastClient.newHazelcastClient(clientConfig);

	}

}
