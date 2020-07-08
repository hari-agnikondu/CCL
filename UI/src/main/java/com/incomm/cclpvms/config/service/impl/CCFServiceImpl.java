package com.incomm.cclpvms.config.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.CCFConfDetail;
import com.incomm.cclpvms.config.model.CCFConfigReq;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.CCFService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class CCFServiceImpl implements CCFService {
	private static final Logger logger = LogManager.getLogger(CCFServiceImpl.class);
	private static final String CCF_REQ_URL = "ccfConfigs";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	String CONFIG_BASE_URL;

	@Value("${INS_USER}")
	long userId;

	public String getCCFParam() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		ResponseEntity<ResponseDTO> responseEntity = restTemplate
				.getForEntity(CONFIG_BASE_URL + CCF_REQ_URL + "/ccfParams", ResponseDTO.class);
		responseBody = responseEntity.getBody();
		return responseBody.getData().toString();
	}

	public String getCCFKey() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		ResponseEntity<ResponseDTO> responseEntity = restTemplate
				.getForEntity(CONFIG_BASE_URL + CCF_REQ_URL + "/ccfKeys", ResponseDTO.class);
		responseBody = responseEntity.getBody();
		return responseBody.getData().toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, List> getCCFList() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		Map<String, List> dropdownlist = new HashMap<>();
		try {
			responseBody = restTemplate.getForObject(CONFIG_BASE_URL + CCF_REQ_URL + "/ccfList", ResponseDTO.class);
			dropdownlist = (Map<String, List>) responseBody.getData();

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getCCFList()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}
		logger.debug("EXIT");
		return dropdownlist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getCCFVersionDtls(String versionID) throws ServiceException {
		logger.debug("ENTER getCCFVersionDtls");
		ResponseDTO responseBody = null;
		List<CCFConfDetail> ccfConfig = null;
		logger.debug("ENTER getCCFVersionDtls:" + CONFIG_BASE_URL + CCF_REQ_URL
				+ "/ccfConfigDetails/?CCFVersionID=" + versionID);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
				CONFIG_BASE_URL + CCF_REQ_URL + "/ccfConfigDetails/?CCFVersionID=" + versionID, ResponseDTO.class);
		responseBody = responseEntity.getBody();
		ccfConfig = (List<CCFConfDetail>) responseBody.getData();

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonArrray;
		try {
			jsonArrray = objectMapper.writeValueAsString(ccfConfig);
		} catch (JsonProcessingException e) {
			logger.error("Error Occured during conversion of JSON Object" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}
		logger.debug("ENTER jsonArrray" + jsonArrray);
		logger.debug("ENTER ccfConfig" + ccfConfig);
		return jsonArrray;
	}

	@Override
	public ResponseDTO addCCFConfiguration(CCFConfigReq ccfConfigReq) throws ServiceException {
		ResponseDTO responseBody = null;
		logger.debug(CCLPConstants.ENTER + ccfConfigReq.getVersionID());
		try {
			if (ccfConfigReq.getVersionID() != null) {
				ccfConfigReq.setInsUser(userId);
				ccfConfigReq.setInsDate(new Date());
				ccfConfigReq.setLastUpdUser(userId);
				ccfConfigReq.setLastUpdDate(new Date());
				logger.debug("Calling '{}' service to add CCF Configuration", CONFIG_BASE_URL);
				ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(CONFIG_BASE_URL + CCF_REQ_URL,
						ccfConfigReq, ResponseDTO.class);
				responseBody = responseEntity.getBody();
				logger.debug("Response from service", responseBody.getMessage());
			}
			logger.debug("ccfConfigReq ccfConfigReq" + ccfConfigReq.toString());
		} catch (RestClientException ex) {
			logger.error("Error while creating new FulFillment, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;

	}

	@Override
	public ResponseDTO updateCCFConfiguration(CCFConfigReq ccfConfigReq) throws ServiceException {
		ResponseDTO responseBody = null;
		logger.debug(CCLPConstants.ENTER + userId);
		try {
			if (ccfConfigReq.getVersionID() != null) {
				ccfConfigReq.setInsUser(userId);
				ccfConfigReq.setLastUpdUser(userId);
				ccfConfigReq.setInsDate(new Date());
				ccfConfigReq.setLastUpdDate(new Date());
				logger.debug("Calling '{}' service to update CCF Configuration", CONFIG_BASE_URL);
				ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + CCF_REQ_URL,
						HttpMethod.PUT, new HttpEntity<CCFConfigReq>(ccfConfigReq), ResponseDTO.class);
				responseBody = responseEntity.getBody();
				logger.debug("Response from service", responseBody.getMessage());
			}
			logger.debug("ccfConfigReq ccfConfigReq" + ccfConfigReq.toString());
		} catch (RestClientException ex) {
			logger.error("Error while creating new FulFillment, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;

	}
}
