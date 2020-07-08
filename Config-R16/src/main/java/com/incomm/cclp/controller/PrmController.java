package com.incomm.cclp.controller;


import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.service.PrmService;
import com.incomm.cclp.util.ResponseBuilder;

import io.swagger.annotations.Api;
@RestController
@RequestMapping("/prms")
@Api(value="prms")


public class PrmController {
	
	@Autowired
	private PrmService prmservice;
	
	@Autowired
	private ResponseBuilder responseBuilder;
	
	
	
	private static final Logger logger = LogManager.getLogger(PrmController.class);

	
	/**
	 * Update the PRM  After fetching the existing PRM Enable/Disable
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/prmConfigs", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateprmAttributes(@RequestBody Map<String, String> prmAttributes) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		try {
			logger.debug("Updating prm  for txn: {} prm Attributes -> {}", prmAttributes);
			int count = prmservice.updatePrmAttributes(prmAttributes);
			if (count > 0) {
				logger.info("Prm  updated successfully");
				responseDto = responseBuilder.buildSuccessResponse(count, ResponseMessages.SUCCESS_PRM_UPDATE,
						ResponseMessages.SUCCESS);
			} else if (count == 0) {
				logger.info("No Values updated prm ");
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.SUCCESS_PRM_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);

			} else {
				logger.info("Failed to update prm ");
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRM_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);

			}
		} catch (Exception e) {
			logger.error("Error while updating prm attributes {}", e);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRM_UPDATE,
					ResponseMessages.CONFIGSERVICE_EXCEPTION);
		}

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}
	
	/**
	 * Update the PRM  After fetching the existing PRM Enable/Disable All Disabled
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/prmConfigs/all", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateprmAttributes() {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		try {
				
				int count = prmservice.updateAllPrmAttributes();
				if (count > 0) {
					logger.info("Prm  updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,ResponseMessages.SUCCESS_PRM_UPDATE,ResponseMessages.SUCCESS);
				}else if( count == 0 ){
					logger.info("No Values updated prm ");	
					responseDto = responseBuilder.buildSuccessResponse(count,ResponseMessages.SUCCESS_PRM_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);

				} 
				else {
					logger.info("Failed to update prm ");	
					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRM_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating prm attributes {}",e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRM_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}


}
