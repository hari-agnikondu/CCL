/**
 * 
 */
package com.incomm.cclp.controller;

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
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.IssuerService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

/**
 * Issuer Controller provides all the REST operations pertaining to Issuer.
 * 
 * @author NAWAZ
 *
 */
@RestController
@RequestMapping("/issuers")
@Api(value="issuer")
public class IssuerController {

	// the issuer service.
	@Autowired
	private IssuerService issuerService;
	
	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(IssuerController.class);
	
	
	/**
	 * Creates an issuer.
	 * 
	 * @param issuerDto The IssuerDTO.

	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createIssuer(@RequestBody IssuerDTO issuerDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String,String> valuesMap = new HashMap<>();
		logger.info(issuerDto.toString());
		ValidationService.validateIssuer(issuerDto, true);
		
		issuerService.createIssuer(issuerDto);
		
		 
	
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("ISS_ADD_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
		
		 valuesMap.put(CCLPConstants.ISSUER_NAME, issuerDto.getIssuerName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		logger.info(" Config Response Code : "+responseDto.getCode()+" Error Message : "+responseDto.getMessage());
		logger.info(responseDto.toString());	
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Gets all Active Issuers.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllIssuers() {
		logger.info(CCLPConstants.ENTER);
		List<IssuerDTO> issuerDtos = issuerService.getAllIssuers();
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(issuerDtos, 
				("ISS_RETRIVE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); //KALAIVANI P ADDED		
		
		responseDto.setMessage(responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Updates an issuer.
	 * 
	 * @param issuerDto The IssuerDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateIssuer(@RequestBody IssuerDTO issuerDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String,String> valuesMap = new HashMap<>();
		logger.info(issuerDto.toString());	
		/**
		 * making as null for code coverage
		 */
		ResponseDTO responseDto=null;
		ValidationService.validateIssuer(issuerDto, false);
		
       int issCount=issuerService.countOfIssuer(issuerDto.getIssuerId());
		
		if(issCount==0) {
			logger.info("Issuer does not exists");
			responseDto = responseBuilder.buildFailureResponse(
					("ISS_"+ResponseMessages.DOESNOT_EXISTS),ResponseMessages.DOESNOT_EXISTS);
		}else {
			logger.info("Updating the issuer..");
			issuerService.updateIssuer(issuerDto);
		
			responseDto = responseBuilder.buildSuccessResponse(issuerDto, 
				("ISS_UPDATE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); //KALAIVANI P ADDED
		
		 
		 valuesMap.put(CCLPConstants.ISSUER_NAME, issuerDto.getIssuerName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		
		
			
		logger.info(responseDto.toString());
	}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Deletes an issuer.
	 * 
	 * @param issuerId The id of the Issuer to be deleted.

	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/{issuerId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteIssuer(@PathVariable("issuerId") Long issuerId) {
		logger.info(CCLPConstants.ENTER);
		/**
		 * making IssuerDTO as null for code coverage
		 */
		IssuerDTO issuer=null;
		 Map<String,String> valuesMap ;
		 valuesMap=new HashMap<>();
		int delCount=0;
		String templateString = "";
		String issuerName="";
		String resolvedString="";
		logger.info("Issuer Id to delete :"+issuerId);	
		ResponseDTO responseDto=null;
		if (issuerId <= 0)
		{
			logger.info("invalid issuer ID: {}",issuerId);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_ISSUER_ID,ResponseMessages.ERR_ISSUER_ID);
			logger.info(CCLPConstants.EXIT);
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		}
		
		int issCount=issuerService.countOfIssuer(issuerId);
		
		if(issCount==0) {
			logger.info("Issuer with id: {} does not exists",issuerId);
			responseDto = responseBuilder.buildFailureResponse(
					("ISS_"+ResponseMessages.DOESNOT_EXISTS),ResponseMessages.DOESNOT_EXISTS);
		}else {
			issuer= issuerService.getIssuerById(issuerId);
			issuerName=issuer.getIssuerName();
			
				delCount=issuerService.countAndDeleteIssuerById(issuerId);
				if(delCount > 0)
				{
						
					 responseDto = responseBuilder.buildSuccessResponse(null, 
							("ISS_DELETE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 
					 valuesMap.put(CCLPConstants.ISSUER_NAME, issuerName.trim());
					 templateString=responseDto.getMessage();
					
					 StrSubstitutor sub = new StrSubstitutor(valuesMap);
					 resolvedString = sub.replace(templateString);
					 
					 responseDto.setMessage(resolvedString);			 					
				}
				else
				{
					responseDto = responseBuilder.buildSuccessResponse(null, 
							"CARD_RANGE_"+ResponseMessages.ALREADY_EXISTS,ResponseMessages.ALREADY_EXISTS);
								
					valuesMap.put(CCLPConstants.ISSUER_NAME, issuer.getIssuerName());					 
					 templateString=responseDto.getMessage();
					 StrSubstitutor sub = new StrSubstitutor(valuesMap);
					  resolvedString = sub.replace(templateString);
					 responseDto.setMessage(resolvedString);
					 responseDto.setCode(ResponseMessages.ALREADY_EXISTS);
					
					
				}
				logger.info(responseDto.toString());
		}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Gets an issuer by Issuer Id.
	 * 
	 * @param issuerId The id of the Issuer to be retrieved.

	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{issuerId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getIssuerById(@PathVariable("issuerId") Long issuerId) {
		logger.info(CCLPConstants.ENTER);
		logger.info("Issuer Id to get data: "+issuerId);	
		ResponseDTO responseDto = null;
		if (issuerId <= 0)
		{
			logger.info("invalid issuer ID: {}",issuerId);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_ISSUER_ID,ResponseMessages.ERR_ISSUER_ID);
		}
		else
		{
			IssuerDTO issuerDto = issuerService.getIssuerById(issuerId);
			if(issuerDto == null) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.DOESNOT_EXISTS,ResponseMessages.DOESNOT_EXISTS);
			logger.info("Failed to retrieve data for Issuer id: "+issuerId);
			}
			else
			{
				responseDto = responseBuilder.buildSuccessResponse(issuerDto,ResponseMessages.SUCCESS,ResponseMessages.SUCCESS);
				logger.debug("Record for Issuer: {} has been retrieved successfully {}",issuerId,issuerDto.toString());
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Gets an issuer by name. The name can be a complete or partial Issuer name.
	 * 
	 * @param issuerName The name of the Issuer to be retrieved.

	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getIssuersByName(@RequestParam("issuerName") String issuerName) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("Issuer Name to get data: "+issuerName);	
		if (Util.isEmpty(issuerName))
		{
			logger.info("invalid issuer name: {}",issuerName);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_ISSUER_NAME,ResponseMessages.ERR_ISSUER_NAME);
		}
		else
		{
			List<IssuerDTO> issuerDtos = issuerService.getIssuersByName(issuerName);
			
			responseDto = responseBuilder.buildSuccessResponse(issuerDtos,
					ResponseMessages.SUCCESS,ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{issuerId}/cardRangeCount", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardRangeCount(@PathVariable("issuerId") Long issuerId) {
		logger.info(CCLPConstants.ENTER);
		logger.info("Issuer Id : " + issuerId);
		ResponseDTO responseDto = null;
		int issuerCount = 0;
		if (issuerId <= 0) {
			logger.info("invalid issuer ID: {}",issuerId);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ISSUER_ID,
					ResponseMessages.ERR_ISSUER_ID);
		} else {
			issuerCount = issuerService.countOfCardRangeById(issuerId);
			logger.debug("issuer count is " + issuerCount);
			if (issuerCount == 0) {

				responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS,
						ResponseMessages.SUCCESS);
			} else {

				responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.CARDRANGE_EXISTS,
						ResponseMessages.CARDRANGE_EXISTS);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
}
