package com.incomm.scheduler.controller;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.service.CcfGenerationService;
import com.incomm.scheduler.utils.ResponseBuilder;
import com.incomm.scheduler.utils.ResponseMessages;

@RestController
@RequestMapping("/ccfGeneration")
public class CCFGenarationController {

	@Autowired
	CcfGenerationService ccfGenerationService;

	@Autowired
	private ResponseBuilder responseBuilder;
	
	private static final Logger logger = LogManager.getLogger(CCFGenarationController.class);

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> ccfGenerationJob(@RequestBody String[] ccf,@PathVariable("userId") String userId) {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("Order Id "+Arrays.toString(ccf) +" UserId "+userId );
		String result = "success";
		Long timeTaken;
		long timeBeforeTransaction = System.currentTimeMillis();
		if (ccf != null) {
			logger.info("start ccf generation process");
			 ccfGenerationService.generateCcfFile(ccf,userId);
			 responseDto = responseBuilder.buildSuccessResponse(result, ResponseMessages.CCF_RESULT,
						ResponseMessages.SUCCESS);
			
		} else {
			logger.error("No ccf file found in input");
			responseDto = responseBuilder.buildFailureResponse("", ResponseMessages.FAILURE);
		}
		long timeAfterTransaction = System.currentTimeMillis();
		timeTaken = timeAfterTransaction - timeBeforeTransaction;
		logger.debug("Time taken to execute ccf generation"+timeTaken);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
}
