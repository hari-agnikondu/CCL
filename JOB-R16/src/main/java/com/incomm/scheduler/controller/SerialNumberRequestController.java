package com.incomm.scheduler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.service.SerialNumberRequest;
import com.incomm.scheduler.utils.ResponseBuilder;
import com.incomm.scheduler.utils.ResponseMessages;

@RestController
@RequestMapping("/serialNumberRequest")
public class SerialNumberRequestController {

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	SerialNumberRequest serialNumberRequest;

	private static final Logger logger = LogManager.getLogger(CCFGenarationController.class);

	@RequestMapping(value = "/{productId}/{upc}/{serialNumberQuantity}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> serialNumberRequest(@PathVariable("productId") Long productId,
			@PathVariable("upc") String upc, @PathVariable("serialNumberQuantity") Long serialNumberQuantity) {

		logger.info("Manual Serial Number Request : ENTER");
		ResponseDTO responseDto = null;
		String result = null;

		if (productId > 0 && serialNumberQuantity > 0 && upc!=null) {
			result = serialNumberRequest.getSerialNumber(productId, upc, serialNumberQuantity);
			responseDto = responseBuilder.buildSuccessResponse(result, ResponseMessages.SERIAL_NUMBER_REQUEST_SUCCESS,
					ResponseMessages.SUCCESS);

		} else {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.SERIAL_NUMBER_REQUEST_FAILURE,
					ResponseMessages.FAILURE);
		}
		logger.info("Manual Serial Number Request : EXIT");
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
