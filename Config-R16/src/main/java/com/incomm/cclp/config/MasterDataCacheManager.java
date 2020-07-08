package com.incomm.cclp.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MasterService;

@Configuration
public class MasterDataCacheManager {

	@Autowired
	MasterService masterService;

	@Autowired
	CacheManager cacheManager;

	public static final String CSS_AUTHENTICATION_TYPE = "CSSAuthenticationType";
	
	public static final String ENVIRONMENT_TYPE = "EnvironmentType";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@PostConstruct
	public void loadAuthenticationTypesToLocalCache() throws ServiceException {
		Map<String, Map<String, String>> authenticationTypes = masterService.getAllAuthenticationTypes();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(CSS_AUTHENTICATION_TYPE,
				authenticationTypes);

		logger.info("Adding Authentication Types Master Data to LOCAL Cache on startup : " + authenticationTypes);
	}
	
	@PostConstruct
	public void loadEnvironmentTypesToLocalCache() throws ServiceException {
		Map<String, String> environmentTypes = masterService.getEnvironmentTypes();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(ENVIRONMENT_TYPE,
				environmentTypes);

		logger.info("Adding Environment Types Master Data to LOCAL Cache on startup : " + environmentTypes);
	}

}
