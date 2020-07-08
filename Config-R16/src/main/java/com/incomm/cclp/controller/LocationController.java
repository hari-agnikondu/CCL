
package com.incomm.cclp.controller;

import io.swagger.annotations.Api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.LocationDTO;
import com.incomm.cclp.dto.MerchantDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.LocationService;
import com.incomm.cclp.service.MerchantService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

/**
 * Group Access Controller provides all the REST operations for Group Access. 
 */
@RestController
@RequestMapping("/locations")
@Api(value="Merchant Location")
public class LocationController {

	// the LocationService service.
	@Autowired
	private LocationService locationService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	@Autowired
	private MerchantService merchantService;

	// the logger
	private static final Logger logger = LogManager.getLogger(LocationController.class);
	
	public static final String LOCATION_RETRIEVE = "LOCATION_RETRIEVE_";
	public static final String MERCHANT_NAME = "MerchantName";
	

	/**
	 * Getting Group Access details By Group Access name and Product Name
	 * @throws ServiceException 
	 * 
	 * */
	@RequestMapping(value="/search",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getLocations(@RequestParam("merchantName") String merchantName) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto =null;
		if (Util.isEmpty(merchantName)) 
		{
			List<LocationDTO> locationList =  locationService.getAllLocations();
			responseDto = responseBuilder.buildSuccessResponse(locationList,
					LOCATION_RETRIEVE+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS);
		}
		else{
			List<LocationDTO> locationDtoList = locationService.getLocationByMerchantName(merchantName);
			responseDto = responseBuilder.buildSuccessResponse(locationDtoList,
						ResponseMessages.LOCATION_RETRIEVE_000,"");
			
			Map<String,String> valuesMap = new HashMap<>();
			valuesMap.put(MERCHANT_NAME, merchantName);
			responseDto.setMessage(new StrSubstitutor(valuesMap).replace(responseDto.getMessage()));			
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Creates Location
	 * 
	 * @param  The LocationDTO.

	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createLocation(@RequestBody LocationDTO locationDto) 
			throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		
		Map<String,String> valuesMap = new HashMap<>();
		
		logger.info("locationDto:"+locationDto);
		
		ValidationService.validateLocation(locationDto, true);
		
		locationService.createLocation(locationDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("LOCATION_ADD_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		valuesMap.put(CCLPConstants.LOCATION_NAME, locationDto.getLocationName());
		valuesMap.put(MERCHANT_NAME, locationDto.getMerchantName()!=null?locationDto.getMerchantName().trim():"");
		String  templateString= "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		logger.info("Exit");
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * To get all Locations by using this 
	 * @param  empty 
	 * @return the ResponseEntity with the result.
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getLocations(){
		logger.info(CCLPConstants.ENTER);		
		
		List<LocationDTO> locationList=locationService.getAllLocations();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(locationList, 
				(LOCATION_RETRIEVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * To update Location   by using locationDto
	 * @param  locationDto 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateLocation(@RequestBody LocationDTO locationDto) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		
		logger.info("groupDto:"+locationDto);
		
		Map<String,String> valuesMap = new HashMap<>();
		
		ValidationService.validateLocation(locationDto, false);
		
		locationService.updateLocation(locationDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("LOCATION_UPDATED_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		 valuesMap.put(CCLPConstants.LOCATION_NAME,locationDto.getLocationName());
		 
		 MerchantDTO merchantDto = merchantService.getMerchantById(locationDto.getMerchantId());
		 
		 valuesMap.put(MERCHANT_NAME, merchantDto.getMerchantName());
		 String templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 responseDto.setMessage(sub.replace(templateString));
		 logger.info(CCLPConstants.EXIT);
		
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	/**
	 * To get  Location by id  
	 * @param  locationId 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 *
	 */
	
	@RequestMapping(value="/{locationId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getLocationById(@PathVariable("locationId") Long locationId) throws ServiceException{
		logger.info(CCLPConstants.ENTER);		
		LocationDTO locationObj=locationService.locationById(locationId);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(locationObj, 
				(LOCATION_RETRIEVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * To get result by locatioID
	 * @param  locatioID 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 *  
	 */
	@RequestMapping(value = "/{locationId}",method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteLocation(@PathVariable("locationId") Long locationId) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		Map<String,String> valuesMap = new HashMap<>();
		LocationDTO locationDto =new LocationDTO();
		locationDto.setLocationId(locationId);
		locationService.deleteLocation(locationDto);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("LOCATION_DELETE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
		 
		 valuesMap.put(CCLPConstants.LOCATION_NAME,locationDto.getLocationName());
		 valuesMap.put(MERCHANT_NAME, locationDto.getMerchantName());
		 String templateString=responseDto.getMessage();
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 responseDto.setMessage(sub.replace(templateString));
		 logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
}
