package com.incomm.cclpvms.inventory.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclpvms.config.model.CardRangeDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.inventory.service.CardNumberInventoryService;

@Service
public class CardNumberInventoryServiceImpl implements CardNumberInventoryService{

	private static final Logger logger = LogManager.getLogger(CardNumberInventoryServiceImpl.class);
	public static final String INVENTORIES="inventories/";

	@Autowired
	private RestTemplate restTemplate ;
		
	@Value("${JOB_BASE_URL}")
	String schedulerBaseUrl;
	
	
	
	@Override
	public ResponseDTO getAllCardInvntryDtls() throws ServiceException {
		
		logger.debug("ENTER getAllCardInvntryDtls");
		logger.debug("Calling '{}' service to get all delivery channels",schedulerBaseUrl);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(schedulerBaseUrl + "/inventories",
		ResponseDTO.class);
		ResponseDTO responseDTO = responseEntity.getBody();
		logger.info("responseBody from config service "+responseDTO);
		logger.debug("Exit getAllCardInvntryDtls");
		return responseDTO;
		
		
	}

	@Override
	public ResponseDTO generateCardInventory(long cardRangeId) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseBody=null;
	
		CardRangeDTO cardRangeDto=new CardRangeDTO();
		cardRangeDto.setCardRangeId(cardRangeId);
		
		final String ACTION_GENERATE="/generate";
		
		try
		{
		
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					schedulerBaseUrl + INVENTORIES + cardRangeId + ACTION_GENERATE, HttpMethod.PUT, null,
					ResponseDTO.class);
			responseBody=responseEntity.getBody();
			logger.debug("ResponseBody data "+responseBody.getResult());
		
		}
		catch (RestClientException e) {
		
			logger.error("Error Occured during making a Rest Call in getting CardInvntryDtls()" + e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		
		}
		
		logger.info(CCLPConstants.EXIT);
		return responseBody;
	}


	@Override
	public ResponseDTO pauseCardInventory(long cardRangeId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseBody=null;
		
		try {
			ResponseEntity<ResponseDTO> responseEntity=restTemplate.exchange(schedulerBaseUrl+INVENTORIES+cardRangeId+"/pause",HttpMethod.PUT,null ,ResponseDTO.class);
			responseBody=responseEntity.getBody();
			logger.debug("ResponseBody data... "+responseBody.getResult());
		}
		catch (RestClientException e) {
			
			logger.error("Error Occured during making a Rest Call in getCard InvntryDtls()" + e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		
		}
		finally {
			logger.info(CCLPConstants.EXIT);
		}
		return responseBody;
	}

	@Override
	public ResponseDTO resumeCardInventory(long cardRangeId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseBody=null;
	
		CardRangeDTO cardRangeDto=new CardRangeDTO();
		cardRangeDto.setCardRangeId(cardRangeId);
		final String ACTION_RESUME="/resume";
		try
		{
		
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					schedulerBaseUrl + INVENTORIES + cardRangeId + ACTION_RESUME, HttpMethod.PUT, null,
					ResponseDTO.class);
			responseBody=responseEntity.getBody();
			logger.debug("ResponseBody data.. "+responseBody.getResult());
		
		}
		catch (RestClientException e) {
		
			logger.error("Error Occured during making a Rest Call in getCardInvntryDtls()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		
		}
		
		logger.info(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO regenerateCardInventory(long cardRangeId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseBody=null;
	
		CardRangeDTO cardRangeDto=new CardRangeDTO();
		cardRangeDto.setCardRangeId(cardRangeId);
		
		try
		{
		
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(schedulerBaseUrl+INVENTORIES+cardRangeId+"/regenerate",HttpMethod.PUT,null ,ResponseDTO.class);
			responseBody=responseEntity.getBody();
			logger.debug("ResponseBody data. "+responseBody.getResult());
		
		}
		catch (RestClientException e) {
		
			logger.error("Error Occured during making a Rest Call in getCardInvntryDtls()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		
		}
		
		logger.info(CCLPConstants.EXIT);
		return responseBody;

	}

}
