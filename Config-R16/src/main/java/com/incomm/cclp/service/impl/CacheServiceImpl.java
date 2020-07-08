package com.incomm.cclp.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.service.CacheService;
import com.incomm.cclp.util.Util;

@Service
public class CacheServiceImpl implements CacheService {

	private static final Logger logger = LogManager.getLogger(CacheServiceImpl.class);

	@Value("${cacheServer.update}")
	Boolean updateFlag;
	
	@Value("${cacheServer.refresh.url}")
	String cacheServerUrl;
	
	@Override
	@Async
	public void updateCache(String cacheName, String cacheId) {
		logger.debug(CCLPConstants.ENTER);
		if (updateFlag && !Util.isEmpty(cacheServerUrl)) {
			try {
				Thread.sleep(CCLPConstants.THREAD_SLEEP);
				new RestTemplate().postForEntity(cacheServerUrl + "?cacheName=" + cacheName + "&cacheId=" + cacheId,
						null, ResponseDTO.class);
				logger.debug("{} updated Successfully",cacheName);
			} catch (Exception e) {
				logger.error("Error Occured while refreshing {} service {}",cacheName, e);
			}
		} else {
			logger.debug("update flag is set as {}",updateFlag);
		}
		logger.debug(CCLPConstants.EXIT);
	}

}
