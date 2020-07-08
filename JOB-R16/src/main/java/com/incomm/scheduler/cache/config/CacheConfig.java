package com.incomm.scheduler.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.incomm.scheduler.cache.service.DistributedCacheService;

@Configuration
public class CacheConfig {

	@Value("${hazelcast.groupName}")
	private String groupName;

	@Value("${hazelcast.groupPass}")
	private String groupPass;

	@Value("${hazelcast.member}")
	private String hazelcastMember;
	
	@Value("${hazelcast.attemptLimit}")
	private int attemptList;

	@Value("${hazelcast.attemptPeriod}")
	private int attemptPeriod;
	
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

		clientConfig.getNetworkConfig()
			.setConnectionAttemptLimit(attemptList);
		// 0 value means try forever
		clientConfig.getNetworkConfig()
			.setConnectionAttemptPeriod(attemptPeriod);
		

		// Configure Near Cache
		NearCacheConfig nearCacheConfig = new NearCacheConfig();
		nearCacheConfig.setName(DistributedCacheService.PRODUCT_ATTRIBUTE_CACHE);
		clientConfig.addNearCacheConfig(nearCacheConfig);

		return HazelcastClient.newHazelcastClient(clientConfig);

	}
}
