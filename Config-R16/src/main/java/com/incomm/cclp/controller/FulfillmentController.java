package com.incomm.cclp.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.FulfillmentDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.FulfillmentService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

/**
 * @author Raja
 */

@RestController
@RequestMapping("/fulfillments")
@Api(value = "fulfillment")
public class FulfillmentController {

	private static final Logger logger = LogManager.getLogger(FulfillmentController.class);

	@Autowired
	private FulfillmentService fulfillmentService;

	@Autowired
	private ResponseBuilder responseBuilder;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createFulfillment(@RequestBody FulfillmentDTO fullfillmentDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto;

		ValidationService.validateFulfillment(fullfillmentDto, true);
		int cnt = fulfillmentService.chkDuplicateByID(fullfillmentDto.getFulfillmentID());

		if (cnt > 0) {
			logger.info("Already exists");
			responseDto = responseBuilder.buildFailureResponse(("FUL_ERR_" + ResponseMessages.ALREADY_EXISTS),
					ResponseMessages.ALREADY_EXISTS);
		} else {
			fulfillmentService.createFulfillment(fullfillmentDto);
			logger.info("Created fulfillment successfully");
			responseDto = responseBuilder.buildSuccessResponse(null, ("FUL_ADD_" + ResponseMessages.SUCCESS),
					ResponseMessages.SUCCESS);
		}
		
		responseDto.setMessage(ResponseBuilder.fillPlaceHolder(CCLPConstants.FULFILLMENT_ID,
				fullfillmentDto.getFulfillmentID() + "~" + fullfillmentDto.getFulFillmentName(),
				responseDto.getMessage()));
		
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
				+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateFulfillment(@RequestBody FulfillmentDTO fullfillmentDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(fullfillmentDto.toString());
		ValidationService.validateFulfillment(fullfillmentDto, false);
		ResponseDTO responseDto ;

		fulfillmentService.updateFulfillment(fullfillmentDto);
		responseDto = responseBuilder.buildSuccessResponse(null, ("FUL_UPD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);

		responseDto.setMessage(ResponseBuilder.fillPlaceHolder(CCLPConstants.FULFILLMENT_ID,
				fullfillmentDto.getFulfillmentID() + "~" + fullfillmentDto.getFulFillmentName(),
				responseDto.getMessage()));
		logger.info(GeneralConstants.LOG_RESPONSE_CODE + responseDto.getResponseCode()
				+ GeneralConstants.LOG_RESPONSE_MESSAGE + responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteFulfillment(@RequestBody FulfillmentDTO fullfillmentDto)
			throws ServiceException {
		int cnt = 0;
		FulfillmentDTO fullfillmentDtoDb =null;
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		String fulFillmentID = fullfillmentDto.getFulfillmentID();
		cnt = fulfillmentService.checkPackageIdMap(fulFillmentID);
		if (cnt > 0) {
			logger.info("Already Exists");
			responseDto = responseBuilder.buildFailureResponse("FUL_PAC_" + ResponseMessages.ALREADY_EXISTS,
					ResponseMessages.ALREADY_EXISTS);
		} else {
			 fullfillmentDtoDb = fulfillmentService.getFulfillmentById(fullfillmentDto.getFulFillmentSEQID());
			if (fullfillmentDtoDb != null) {
				fulfillmentService.deleteFulfillment(fullfillmentDto.getFulFillmentSEQID()); 
				logger.info("Successfully deleted fulfillment");
				responseDto = responseBuilder.buildSuccessResponse(null, ("FUL_DEL_" + ResponseMessages.SUCCESS),
					ResponseMessages.SUCCESS);
				
			}
			else{
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_FULFILLMENT_DOESNOT_EXISTS,
						ResponseMessages.DOESNOT_EXISTS);
				logger.info("Record Doesn't exist: " + fullfillmentDto.getFulFillmentSEQID());
				
			}
		}
		
		responseDto.setMessage(ResponseBuilder.fillPlaceHolder(CCLPConstants.FULFILLMENT_ID,fulFillmentID ,responseDto.getMessage()));
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/{fulfillmentSEQID}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getFulfillmentById(@PathVariable("fulfillmentSEQID") long fulfillmentSEQID)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("fulfillmentID: " + fulfillmentSEQID);
		if (fulfillmentSEQID < 0 ) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_FULFILLMENT_ID,
					ResponseMessages.ERR_FULFILLMENT_ID);
		} else {
			FulfillmentDTO fullfillmentDto = fulfillmentService.getFulfillmentById(fulfillmentSEQID);
			if (fullfillmentDto == null) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.DOESNOT_EXISTS,
						ResponseMessages.DOESNOT_EXISTS);
				logger.info("Record not exist: " + fulfillmentSEQID);
			} else {
				logger.info("fullfillmentDto CN File Identifier:"+fullfillmentDto.getB2bCnFileIdentifier());
				responseDto = responseBuilder.buildSuccessResponse(fullfillmentDto,
						CCLPConstants.FUL_RETRIVE + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				logger.debug("Record for FulFillment: {} has been retrieved successfully {}", fulfillmentSEQID,
						fullfillmentDto.toString());
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getFulfillmentByName(@RequestParam("fulFillmentName") String fulFillmentName)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("fulFillmentName: " + fulFillmentName);
		if (Util.isEmpty(fulFillmentName)) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_FULFILLMENT_NAME,
					ResponseMessages.ERR_FULFILLMENT_NAME);
		} else {
			List<FulfillmentDTO> fulfillmentDtos = fulfillmentService.getFulfillmentByName(fulFillmentName);
			if (fulfillmentDtos == null) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.DOESNOT_EXISTS,
						ResponseMessages.DOESNOT_EXISTS);
				logger.info("Record Does not exist: " + responseDto);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(fulfillmentDtos,
						CCLPConstants.FUL_RETRIVE + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				logger.debug("Record for FulFillment: {} has been retrieved successfully {}", fulFillmentName,
						fulfillmentDtos.toString());
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllFulfillment() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<FulfillmentDTO> fulfillmentDtos = fulfillmentService.getAllFulfillments();
		responseDto = responseBuilder.buildSuccessResponse(fulfillmentDtos, CCLPConstants.FUL_RETRIVE + ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
