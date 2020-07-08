package com.incomm.cclpvms.fraud.service.impl;

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
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.service.PrmService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class PrmServiceImpl implements PrmService{
	
	private static final Logger logger = LogManager.getLogger(PrmServiceImpl.class.getName());

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${CONFIG_BASE_URL}") String CONFIG_BASE_URL;
	@Value("${INS_USER}")
	long userId;
	

	
	/*PRM Starts */
public ResponseDTO updatePrmConfig(Map<String, String> prmAttributes) throws ServiceException {

	logger.debug("ENTER");
	ResponseDTO responseBody = null;

	try {

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(prmAttributes, headers);
		logger.debug("Calling '{}' service to update prm", CONFIG_BASE_URL);

		logger.debug("prmAttributes" + prmAttributes);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
				CONFIG_BASE_URL + "/prms/prmConfigs", HttpMethod.PUT, requestEntity, ResponseDTO.class);

		responseBody = responseEntity.getBody();
		logger.info("responseBody "+responseBody);
	} catch (RestClientException e) {
		logger.error("Exception while updating prm {}", e);
		throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
	}
	logger.debug("EXIT");
	return responseBody;
}



public ResponseDTO updateAllPrmConfig() throws ServiceException {
	logger.debug("ENTER");
	ResponseDTO responseBody = null;

	try {

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(headers);
		logger.debug("Calling '{}' service to update prm", CONFIG_BASE_URL);

		
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
				CONFIG_BASE_URL + "/prms/prmConfigs/all", HttpMethod.PUT, requestEntity, ResponseDTO.class);

		responseBody = responseEntity.getBody();
		logger.info("responseBody "+responseBody);
	} catch (RestClientException e) {
		logger.error("Exception while updating prm {}", e);
		throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
	}
	logger.debug("EXIT");
	return responseBody;

}


}
