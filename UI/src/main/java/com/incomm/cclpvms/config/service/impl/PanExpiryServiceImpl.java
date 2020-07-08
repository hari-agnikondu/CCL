package com.incomm.cclpvms.config.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.PanExpiryService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service

public class PanExpiryServiceImpl implements PanExpiryService{
	
	
	private static final Logger logger = LogManager.getLogger(PanExpiryServiceImpl.class);

	private static RestTemplate restTemplate = new RestTemplate();
	
	@Value("${CONFIG_BASE_URL}") String configBaseUrl;
	
	
	/**
	 * update PanExpiry
	 */

	@Override
	public ResponseDTO updatePanExpiryParameters(Map<String, Object> productAttributes, Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
						ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + "products/"+productId +"/panExpiry", HttpMethod.PUT,
					new HttpEntity<Map<String,Object>>(productAttributes), ResponseDTO.class);
			
			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in updatePanExpiryParameters()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	/**
	 *  get PanExpiry Parameters
	 */


	@Override
	public Map<String, Object> getPanExpiryParameters()  throws ServiceException {
		
		
			Map<String,Object> panExpiryParametersMap=null;
			
			logger.debug(CCLPConstants.ENTER);

			try {
				ResponseDTO responseDTO=restTemplate.getForObject(configBaseUrl+"/panExpiryDateExemption" , ResponseDTO.class);
				if(responseDTO!=null && responseDTO.getData()!=null){
					panExpiryParametersMap= (Map<String, Object>) responseDTO.getData();
				}
			} 
			catch (RestClientException e) {
				logger.error("RestClientException in getPanExpiryParameters:"+e);
				throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
			}
			logger.debug(CCLPConstants.EXIT);

			return panExpiryParametersMap;

	}

}
