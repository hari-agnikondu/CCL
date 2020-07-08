package com.incomm.cclpvms.config.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.incomm.cclpvms.config.model.CountryCodeDTO;
import com.incomm.cclpvms.config.model.Location;
import com.incomm.cclpvms.config.model.LocationDTO;
import com.incomm.cclpvms.config.model.Merchant;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.StateCodeDTO;
import com.incomm.cclpvms.config.service.LocationService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class LocationServiceImpl implements LocationService {
	private static final Logger logger = LogManager
			.getLogger(LocationServiceImpl.class.getName());

	@Autowired
	RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	String CONFIG_BASE_URL;

	String locationBaseUrl = "/locations";

	@Value("${INS_USER}")
	long userId;

	@SuppressWarnings("unchecked")
	public List<Location> getAllLocations(Location location)
			throws ServiceException {

		String tempUrl = "";
		List<Location> locationDtoList = null;

		tempUrl = CONFIG_BASE_URL + locationBaseUrl + "/search";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(
				tempUrl).queryParam("merchantName", location.getMerchantName());

		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
				builder.build().encode().toUri(), ResponseDTO.class);

		if (responseEntity != null && responseEntity.getBody() != null) {
			ResponseDTO responseDTO = responseEntity.getBody();
			logger.debug("response from Rest Call : " + responseDTO.getResult());

			if (!ResponseMessages.SUCCESS.equals(responseDTO.getCode())) {
				throw new ServiceException(responseDTO.getResult());
			}
			locationDtoList = (List<Location>) responseDTO.getData();
		}

		return locationDtoList;
	}

	/**
	 * this method is used to save the location.
	 */
	@Override
	public ResponseDTO addLocation(Location location) throws ServiceException {
		
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to create new Location",CONFIG_BASE_URL);

			ModelMapper mm = new ModelMapper();
			LocationDTO locationDTO = mm.map(location, LocationDTO.class);
			Merchant merchant =new Merchant();
			merchant.setMerchantId(location.getMerchantId());
			merchant.setMerchantName(location.getMerchantName());
			
			locationDTO.setInsUser(userId);
			locationDTO.setLastUpdUser(userId);
			responseDTO=restTemplate.postForObject(CONFIG_BASE_URL + locationBaseUrl, locationDTO, ResponseDTO.class); 
			
		} 
		catch (RestClientException e) {
			logger.error("Error while creating new Location:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}

	
	/**
	 * This Method is used to retrieve the Location details based on locationId
	 * 
	 */
	@Override
	public Location getLocationById(Long locationId) throws ServiceException {
		Location location=null;
		logger.debug(CCLPConstants.ENTER);
		LocationDTO	locationDTO=null;
		try {
			logger.debug("Calling '{}' service to Location '{}'",CONFIG_BASE_URL,locationId);
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL +locationBaseUrl+ "/"+locationId,
					ResponseDTO.class);			  
			ResponseDTO responseBody  = responseEntity.getBody();
			
			if(responseBody!=null && responseBody.getData()!=null) {
				
				ModelMapper mm = new ModelMapper();
			
				locationDTO= mm.map(responseBody.getData(),new TypeToken<LocationDTO>() {}.getType());
				location=mm.map(locationDTO, Location.class);
			}
		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getLocationbyId:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return location;
	}

	/*
	 * This Service Method calls the restTemplate to update the Card range details entered by the user.
	 */
	public ResponseEntity<ResponseDTO> updateLocation(Location location) throws ServiceException{
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();
			LocationDTO locationDTO = mm.map(location, LocationDTO.class);
			locationDTO.setInsUser(userId);
			locationDTO.setLastUpdUser(userId);

			logger.debug("Calling '{}' service to update Card Range",CONFIG_BASE_URL);
			
			 responseDTO = restTemplate.exchange(CONFIG_BASE_URL + locationBaseUrl,
					HttpMethod.PUT,new HttpEntity<LocationDTO>(locationDTO), ResponseDTO.class);
		}
		catch (RestClientException e) {
			logger.error("RestClientException in updateLocation:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 

		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	 
	public Map<Long,String> getAllCountries() throws ServiceException{
		List<CountryCodeDTO> countryList=null;
		Map<Long,String> sortedcountryMap=null;
		
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();

			logger.debug("Calling '{}' service to search merchants",CONFIG_BASE_URL);
			ResponseDTO responseDTO=restTemplate.getForObject(CONFIG_BASE_URL + "/master"+"/countryCodes", ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){

				countryList= mm.map(responseDTO.getData(),new TypeToken<List<CountryCodeDTO>>() {}.getType());	
					Map<Long, String> countryMap = countryList.stream().collect(
			                Collectors.toMap(CountryCodeDTO::getCountryCodeID, CountryCodeDTO::getCountryName));
					
					sortedcountryMap = 
							 countryMap.entrySet().stream()
						    .sorted(Entry.comparingByValue())
						    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
						                              (e1, e2) -> e1, LinkedHashMap::new));
			}
		}
		catch (RestClientException e) {
			logger.error("RestClientException in getAllMerchants:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		
		logger.debug(CCLPConstants.EXIT);
		return sortedcountryMap;
	}
	
	@Override
	public CountryCodeDTO getStates(Long countryCode) throws ServiceException{
		CountryCodeDTO countrycode=null;
			
		ResponseDTO responseDTO=restTemplate.getForObject(CONFIG_BASE_URL + "/master/"+countryCode+"/search", ResponseDTO.class);
		
		ModelMapper mm = new ModelMapper();
		countrycode= mm.map(responseDTO.getData(),new TypeToken<CountryCodeDTO>() {}.getType());
		
		return countrycode;
	}
	
	public Map<Long,String> listStates(Long countryCode) throws ServiceException{
		
		List<StateCodeDTO> stateList=null;
		Map<Long,String> sortedStateMap=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			ModelMapper mm = new ModelMapper();

			logger.debug("Calling '{}' service to search States",CONFIG_BASE_URL);
			ResponseDTO responseDTO=restTemplate.getForObject(CONFIG_BASE_URL + "/master/"+countryCode+"/searchState", ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){
							
				stateList= mm.map(responseDTO.getData(),new TypeToken<List<StateCodeDTO>>() {}.getType());	
					Map<Long, String> stateMap = stateList.stream().collect(
			                Collectors.toMap(StateCodeDTO::getStateCodeID, StateCodeDTO::getStateName));
					
					sortedStateMap = 
							stateMap.entrySet().stream()
						    .sorted(Entry.comparingByValue())
						    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
						                              (e1, e2) -> e1, LinkedHashMap::new));
			}
		}
		catch (RestClientException e) {
			logger.error("RestClientException in listStates:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		
		logger.debug(CCLPConstants.EXIT);
		return sortedStateMap;
	}
	

	@Override
	public ResponseEntity<ResponseDTO> deleteLocations(Long locationId) throws ServiceException {
		
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
						
			logger.debug("Calling '{}' service to delete location '{}'",CONFIG_BASE_URL,locationId);
		    responseDTO = restTemplate.exchange(CONFIG_BASE_URL +locationBaseUrl+"/"+locationId,HttpMethod.DELETE,null, ResponseDTO.class);
		}//try
		catch (RestClientException e) {
			logger.error("RestClientException in delete location:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
}
