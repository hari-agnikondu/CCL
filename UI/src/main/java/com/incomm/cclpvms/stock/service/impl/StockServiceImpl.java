package com.incomm.cclpvms.stock.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.LocationDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.stock.model.StockDTO;
import com.incomm.cclpvms.stock.service.StockService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class StockServiceImpl implements StockService{

	// the logger
	private static final Logger logger = LogManager.getLogger(StockServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate ;

	@Value("${ORDER_BASE_URL}") 
	String orderBaseUrl;
	
	@Value("${CONFIG_BASE_URL}")
	String configBaseUrl;
	
	public ResponseDTO saveStock(StockDTO stockForm) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			logger.debug("Calling '{}' service to create new stock ",orderBaseUrl);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(orderBaseUrl + "/stocks", stockForm,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while creating new stock, {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	
	public ResponseDTO updateStock(StockDTO stockForm) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			logger.debug("Calling '{}' service to update stock ",orderBaseUrl);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<StockDTO> requestEntity = new HttpEntity<>(stockForm, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(orderBaseUrl + "/stocks", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while updating stock, {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	
	public Map<String, String> getAllMerchants()  throws ServiceException{
		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;
		Map<String, String> sortedMerchantMap = null;

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(orderBaseUrl + "/stocks/merchants", ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase("success")) {
				@SuppressWarnings("unchecked")
				Map<String, String> merchantMap = mm.map(responseBody.getData(), Map.class);
				sortedMerchantMap = merchantMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
				          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
				logger.debug(merchantMap);
			} else {
				logger.error("Failed to Fetch merchants from order service");
				if(responseBody.getMessage()!=null)
					throw new ServiceException(responseBody.getMessage());
				else
					throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.MERCHANT_FETCH_001));
			}
		} catch (RestClientException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return sortedMerchantMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getStoresAndProductsByMerchantId(Long merchantId) throws ServiceException {
		ResponseDTO responseBody = null;
		List<Map<String, String>>  list ;
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(orderBaseUrl + "/stocks/{merchantId}/locationsAndProducts",
					ResponseDTO.class,merchantId);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch Store id and products from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			
			list = (List<Map<String, String>>) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching stores by merchant id{}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return list;
	}
	
	@Override
	public Map<String, String> getAllLocations()  throws ServiceException{
		
		ResponseDTO responseBody = null;
		Map<String, String> sortedLocationMap = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity( configBaseUrl+ "/locations", ResponseDTO.class);
			responseBody = responseEntity.getBody();
			
			if (responseBody.getResult().equalsIgnoreCase("success")) {
				@SuppressWarnings("unchecked")
				List<Object> locationDtoList =(List<Object>)responseBody.getData();
				Map<String, String> locationMap = new HashMap<>();
				if (locationDtoList != null) {
					Iterator<Object> itr = locationDtoList.iterator();
					while (itr.hasNext()) {
						LocationDTO location = objectMapper.convertValue(itr.next(), LocationDTO.class);
						locationMap.put(String.valueOf(location.getLocationId()), location.getLocationName());
					} 
				}
				
				sortedLocationMap = locationMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
				          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
				logger.debug(locationMap);
			} else {
				logger.error("Failed to Fetch locations from order service");
				if(responseBody.getMessage()!=null)
					throw new ServiceException(responseBody.getMessage());
				else
					throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.LOCATION_FETCH_001));
			}
		} catch (RestClientException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		
		return sortedLocationMap;
	}


	@Override
	public ResponseDTO getStockByMerchantLocationAndProduct(StockDTO stockForm) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		logger.debug("Calling '{}' service for get stock for merchant ID '{}'",orderBaseUrl,stockForm.getMerchantId());
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(orderBaseUrl + "/stocks/{merchantId}/{locationId}/{productId}",ResponseDTO.class, stockForm.getMerchantId(), stockForm.getLocationId(),stockForm.getProductId());
		ResponseDTO responseDTO = responseEntity.getBody();
		logger.info("Stock information "+responseEntity.getBody());
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StockDTO> getAllStocksByMerchantAndLocation(StockDTO stockDTO) throws ServiceException {
		ResponseDTO responseBody = null;
		List<StockDTO>   stockList ;
		String merchantId = stockDTO.getMerchantId()!=null && stockDTO.getMerchantId().equals("")?"-1" :stockDTO.getMerchantId();
		String locationId = stockDTO.getLocationId();
		
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(orderBaseUrl + "/stocks/searchStocks/{merchantId}/{locationId}",
					ResponseDTO.class,merchantId,locationId);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch stocks from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			 stockList = ( List<StockDTO> ) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching stocks for status ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return stockList;
	}
}
