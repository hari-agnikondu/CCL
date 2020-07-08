package com.incomm.cclpvms.fraud.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.model.BlockList;
import com.incomm.cclpvms.fraud.service.BlockListService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class BlockListServiceImpl implements BlockListService{

	// the logger
	private static final Logger logger = LogManager.getLogger(BlockListServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate ;
		
	@Value("${CONFIG_BASE_URL}")
	String CONFIG_BASE_URL;
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> getAllDelivaryChannels() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		
		Map<String,String> sorteddeliverChannelsMap = null;
		Map<String,String> jobMap = new HashMap<>();
		
		try {
			logger.debug("Calling '{}' service to get all delivery channels",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/blockLists/deliveryChannels",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("responseBody from config service "+responseBody);
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch delivery channels from config srvice");
				throw new ServiceException(responseBody.getMessage() );
			}
			List<List<Object>> schedulerServiceJobsDtos = (List<List<Object>>) responseBody.getData();
			if (schedulerServiceJobsDtos != null) {
				for (List<Object>objects : schedulerServiceJobsDtos) {
					jobMap.put((String)objects.get(0), (String)objects.get(1));
				}
			}
			sorteddeliverChannelsMap = jobMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		} catch (RestClientException e) {
			logger.error("Exception while fetching delivery channels {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return sorteddeliverChannelsMap;
	}
	
	@Override
	public ResponseDTO getBlockedListByDelChnlCode(String delChnlCode)  throws ServiceException{
		logger.debug(CCLPConstants.ENTER);
		logger.debug("Calling '{}' service for get BlockedListByDelChnlCode '{}'",CONFIG_BASE_URL,delChnlCode);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/blockLists/{delChnlCode}",ResponseDTO.class, delChnlCode);
		ResponseDTO responseDTO = responseEntity.getBody();
		logger.info("ResponseDTO "+responseDTO);
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	
	@Override
	public ResponseDTO getAllBlockedList() throws ServiceException{
		logger.debug(CCLPConstants.ENTER);
		logger.debug("Calling '{}' service for get BlockedListByDelChnlCode '{}'",CONFIG_BASE_URL);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/blockLists",ResponseDTO.class);
		ResponseDTO responseDTO = responseEntity.getBody();
		logger.info("ResponseDTO "+responseDTO);
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}

	@Override
	public ResponseDTO deleteBlockList(Object[] deleteDelCode) {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		ArrayList<Object> listCode = new ArrayList<>(Arrays.asList(deleteDelCode));
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<List<Object>> requestEntity = new HttpEntity<>(listCode, headers);
		logger.debug("Calling '{}' service to delete blockList '{}'",CONFIG_BASE_URL,deleteDelCode);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + "/blockLists",
				HttpMethod.DELETE, requestEntity, ResponseDTO.class);
		responseBody = responseEntity.getBody();
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	@Override
	public ResponseDTO createBlockList(BlockList blockList) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			logger.debug("Calling '{}' service to create new BlockList Record",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(CONFIG_BASE_URL + "/blockLists", blockList,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while creating new blocklist, {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<List<Object>> getDeliveryChnlTxns() throws ServiceException {
		ResponseDTO responseBody = null;
		List<List<Object>> deliveryChannels = new ArrayList<>();
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/blockLists/deliveryChannels",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch delivery channel list from config srvice");
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : ResourceBundleUtil.getMessage(ResponseMessages.ERR_JOB_SCHEDULER_FETCH));
			}
			
			deliveryChannels = (List<List<Object>>) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching records {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return deliveryChannels;
	}
	
}
