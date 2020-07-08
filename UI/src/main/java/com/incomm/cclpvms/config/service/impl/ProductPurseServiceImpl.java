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
import com.incomm.cclpvms.config.service.ProductPurseService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class ProductPurseServiceImpl implements ProductPurseService{
	private static final Logger logger = LogManager.getLogger(ProductPurseServiceImpl.class.getName());
	
	@Autowired
	RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	String configBaseUrl;

	@Autowired
	SessionService sessionService;

	String productPurseBaseURL = "/productPurse";
	String alertsBaseURL = "/alerts";

	
	@Override
	public ResponseDTO addLimitAttributesByProgramID(Map<String, Object> productPurseAttributes, Long programID, Long purseId)
			throws ServiceException {

		ResponseDTO responseBody = null;

		try {
			logger.info(CCLPConstants.ENTER);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(productPurseAttributes, headers);
			logger.debug("Calling '{}' service to update product purse", configBaseUrl);
			logger.debug("Limit attributes" + productPurseAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/productPurse/updateLimitAttributesByProgramId/{programID}/program/{purseId}/purseId", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					programID, purseId);

			responseBody = responseEntity.getBody();
			logger.info(CCLPConstants.EXIT);
		} catch (RestClientException e) {
			logger.error("Exception while updating limit fees attributes {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return responseBody;
	}
	
	
	@Override
	public ResponseDTO addTxnFeeAttributesByProgramID(Map<String, Object> productPurseAttributes, Long programID, Long purseId)
			throws ServiceException {

		ResponseDTO responseBody = null;

		try {
			logger.info(CCLPConstants.ENTER);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(productPurseAttributes, headers);
			logger.debug("Calling '{}' service to update product purse", configBaseUrl);
			logger.debug("Limit attributes" + productPurseAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/productPurse/updateTxnFeeAttributesByProgramId/{programID}/program/{purseId}/purseId", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					programID, purseId);

			responseBody = responseEntity.getBody();
			logger.info(CCLPConstants.EXIT);
		} catch (RestClientException e) {
			logger.error("Exception while updating transaction fees attributes {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return responseBody;
	}
	
	
	@Override
	public ResponseDTO addMonthlyCapAttributesByProgramID(Map<String, Object> productPurseAttributes, Long programID, Long purseId)
			throws ServiceException {

		ResponseDTO responseBody = null;

		try {
			logger.info(CCLPConstants.ENTER);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(productPurseAttributes, headers);
			logger.debug("Calling '{}' service to update product purse Monthly Cap Attributes", configBaseUrl);
			logger.debug("Limit attributes" + productPurseAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/productPurse/updateMonthlyCapAttributesByProgramId/{programID}/program/{purseId}/purseId", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					programID, purseId);

			responseBody = responseEntity.getBody();
			logger.info(CCLPConstants.EXIT);
		} catch (RestClientException e) {
			logger.error("Exception while updating monthly cap attributes {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return responseBody;
	}
	
	
}
