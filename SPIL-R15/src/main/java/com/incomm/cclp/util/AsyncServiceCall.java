package com.incomm.cclp.util;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.constants.ValueObjectKeys;

@Service
public class AsyncServiceCall {
	private static final Logger logger = LogManager.getLogger(AsyncServiceCall.class);

	@Value("${PRM_BASE_URL:}")
	private String prmBaseUrl;

	@Value("${NOTIFICATION_SERVICE_URL:}")
	private String notificationUrl;

	@Autowired
	RestTemplate restTemplate;

	@Async
	public void callPrmService(Map<String, String> valueObj) {

		if (valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER) != null) {
			ThreadContext.put("RRN", valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
		}
		try {
			if (CodeUtil.isNullOrEmpty(prmBaseUrl)) {
				logger.info("Empty PRM URL: PRM service call is disabled.");
				return;
			} else {
				logger.info("*****prm_base_url**:{}", prmBaseUrl);
				String response = restTemplate.postForObject(prmBaseUrl, valueObj, String.class);
				logger.info("********response*****:{}", response);
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in callPrmService: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception in callPrmService: {}", e.getMessage(), e);
		} finally {
			ThreadContext.clearAll();
		}
	}

	@Async
	public void callNotificationService(Map<String, String> valueObj) {
		if (valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER) != null) {
			ThreadContext.put("RRN", valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
		}
		try {
			if (CodeUtil.isNullOrEmpty(notificationUrl)) {
				logger.info("Empty Notification URL: Notification service call is disabled.");
				return;
			}

			if (CodeUtil.isNotNullAndEmpty(notificationUrl) && !(("10010".equals(valueObj.get(ValueObjectKeys.RESP_CODE)))
					|| ("10029".equals(valueObj.get(ValueObjectKeys.RESP_CODE)))
					|| ("10085".equals(valueObj.get(ValueObjectKeys.RESP_CODE))))) {
				logger.info("*****notification_url**:{}", notificationUrl);

				String response = restTemplate.postForObject(notificationUrl, valueObj, String.class);
				logger.info("********response*****: {}", response);
			} else {
				logger.info("********Invalid request, service it wont invoke*****");
			}

			logger.debug("Message Sent");
		} catch (RestClientException e) {
			logger.error("RestClientException in callNotificationService: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception in callNotificationService: " + e.getMessage(), e);
		} finally {
			ThreadContext.clearAll();
		}
	}

}
