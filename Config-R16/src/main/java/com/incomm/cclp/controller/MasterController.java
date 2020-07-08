package com.incomm.cclp.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ClpResourceDTO;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.PurseTypeDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MasterService;
import com.incomm.cclp.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.util.ResponseBuilder;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/master")
@Api(value="master")
public class MasterController {
	
	@Autowired
	private MasterService masterService;

	@Autowired
	LocalCacheServiceImpl localCacheService;

	@Autowired
	private ResponseBuilder responseBuilder;
	
	private static final Logger logger = LogManager.getLogger(MasterController.class);

	
	@RequestMapping(value = "/purseType", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllPurseType() {
		logger.info(CCLPConstants.ENTER);
		
		List<PurseTypeDTO> purseTypeList=masterService.getAllPurseType();
		
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(purseTypeList, 
				("PRS_RETRIVE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/currencyCode", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllCurrencyCode()throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		List<CurrencyCodeDTO> currencyCodeList=masterService.getAllCurrencyCode();
		
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(currencyCodeList, 
				("PRS_RETRIVE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fulFillmentList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getFulfillmentShipmentAttrs() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> shipmentList = masterService.getFulfillmentShipmentAttrs();
		responseDto = responseBuilder.buildSuccessResponse(shipmentList, "FUL_SHIPMENT_" + ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/packageShipmentList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getShipmentAttList() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> shipmentList = masterService.getPackageShipmentAttrs();
		responseDto = responseBuilder.buildSuccessResponse(shipmentList, "PAC_SHIPMETHODS_" + ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cardstatus", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardStatusList() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> cardStatusList = masterService.getCardStatus();
		responseDto = responseBuilder.buildSuccessResponse(cardStatusList, "CARD_STATUS_" + ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="/countryCodes",method=RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCountryCodes() {
		
		ResponseDTO responseDto=responseBuilder
				.buildSuccessResponse(masterService.getAllCountryCode(),CCLPConstants.COUNTRY_CODE_RETRIVE+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS );
		
		return new  ResponseEntity<>(responseDto,HttpStatus.OK);
	}
	
	@RequestMapping(value="/{countryCode}/search",method=RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCountryCodeById(@PathVariable("countryCode") Long countryCode) {
		
		ResponseDTO responseDto=responseBuilder
				.buildSuccessResponse(masterService.getCountryCodeById(countryCode),CCLPConstants.COUNTRY_CODE_RETRIVE+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS );
		
		
		return new  ResponseEntity<>(responseDto,HttpStatus.OK);
	}
	
	/**
	 * Getting all menus and permissions
	 * @return ResponseEntity<ResponseDTO>
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/menus", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMenus()throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		List<ClpResourceDTO> clpResourceDTOList=masterService.getMenus();
		
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(clpResourceDTOList, 
				(ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
		@RequestMapping(value="/{countryCode}/searchState",method=RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getStateCodeByCountryId(@PathVariable("countryCode") Long countryCode) {
		
		ResponseDTO responseDto=responseBuilder
				.buildSuccessResponse(masterService.getStateCodeByCountryId(countryCode),CCLPConstants.COUNTRY_CODE_RETRIVE+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS );
		
		return new  ResponseEntity<>(responseDto,HttpStatus.OK);
	}
		
		
		@RequestMapping(value="/groups",method = RequestMethod.GET)
		public ResponseEntity<ResponseDTO> getGroups() throws ServiceException{
			logger.info(CCLPConstants.ENTER);		
			
			List<GroupDTO> groupDtolist = masterService.getGroups();

			ResponseDTO responseDto = responseBuilder.buildSuccessResponse(groupDtolist, 
					(CCLPConstants.GROUP_RETRIVE+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
			
			logger.info(CCLPConstants.EXIT);
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		}
		
	
	@GetMapping(value = "/authTypes")
	public ResponseEntity<ResponseDTO> getCSSAuthenticationTypes() throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, Map<String, String>> authenticationTypes = masterService.getAuthenticationTypes();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(authenticationTypes,
				(CCLPConstants.CSS_AUTH_TYPES_RETRIVE + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@GetMapping(value = "/domain")
	public ResponseEntity<ResponseDTO> getDomains() throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, String> environmentTypes = masterService.getAllEnvironmentTypes();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(environmentTypes,
				(CCLPConstants.ENV_TYPES_RETRIVE + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/purses", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllPurse()throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		List<PurseDTO> purseList=masterService.getAllPurse();
		ResponseDTO	responseDto = responseBuilder.buildSuccessResponse(purseList, 
				("PRS_RETRIVE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
