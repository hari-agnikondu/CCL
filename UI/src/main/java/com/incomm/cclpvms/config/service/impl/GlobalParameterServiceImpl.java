package com.incomm.cclpvms.config.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclpvms.config.model.GlobalParametersDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.GlobalParameterService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class GlobalParameterServiceImpl implements GlobalParameterService {

	private static final Logger logger = LogManager.getLogger(IssuerServiceImpl.class);

	private static RestTemplate restTemplate = new RestTemplate();

	@Value("${CONFIG_BASE_URL}")
	String configBaseUrl;
	
	private static final String MASTER_BASE_URL = "/master";

	/**
	 * updateGlobalParameters
	 */

	@Override
	public ResponseDTO updateGlobalParameters(GlobalParametersDTO globalParametersDTO) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();

			HttpEntity<GlobalParametersDTO> requestEntity = new HttpEntity<>(globalParametersDTO, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + "globalParameters",
					HttpMethod.PUT, requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in updateIssuer()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	/**
	 * get global parameters
	 */

	@Override
	public Map<String, Object> getGlobalParameters() throws ServiceException {

		Map<String, Object> globalParametersMap = null;

		logger.debug(CCLPConstants.ENTER);

		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + "/globalParameters", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				globalParametersMap = (Map<String, Object>) responseDTO.getData();
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in alertMessagesMap:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return globalParametersMap;

	}

	@Override
	public Map<String, String> getDomainMetadata() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		Map<String, String> domainTypes = new HashMap<>();

		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + MASTER_BASE_URL + "/domain",
					ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				domainTypes = new ModelMapper().map(responseDTO.getData(), new TypeToken<Map<String, String>>() {
				}.getType());
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getDomainMetadata:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return domainTypes;

	}

}
