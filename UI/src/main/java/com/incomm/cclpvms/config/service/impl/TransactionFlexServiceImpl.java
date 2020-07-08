package com.incomm.cclpvms.config.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.TransactionFlexService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class TransactionFlexServiceImpl implements  TransactionFlexService{
	
	private static final Logger logger = LogManager.getLogger(TransactionFlexServiceImpl.class.getName());

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${CONFIG_BASE_URL}") String CONFIG_BASE_URL;
	@Value("${INS_USER}")
	long userId;
	

	@Override
	public ResponseDTO updateTransFlexConfig(Map<String, String> txnDesc) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(txnDesc, headers);
			logger.debug("Calling '{}' service to update txnDesc", CONFIG_BASE_URL);

			logger.debug("txnDesc" + txnDesc);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					CONFIG_BASE_URL + "/TransactionFlexs/TransactionFlex", HttpMethod.PUT, requestEntity, ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.info("responseBody "+responseBody);
		} catch (RestClientException e) {
			logger.error("Exception while updating txnDesc {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

}
