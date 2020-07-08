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
import com.incomm.cclp.service.TransactionFlexService;
import com.incomm.cclp.util.ResponseBuilder;
@RestController
@RequestMapping("/TransactionFlexs")
public class TransactionFlexController {
	

	@Autowired
	private TransactionFlexService transactionflexservice;
	
	@Autowired
	private ResponseBuilder responseBuilder;
	
	private static final Logger logger = LogManager.getLogger(TransactionFlexController.class);
	
	/**
	 * Update the PRM  After fetching the existing PRM Enable/Disable
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/TransactionFlex", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateTransactionFlexDesc(@RequestBody Map<String, String> txnFlexDesc) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		try {
			logger.debug("Updating for txn: {} txnFlexDesc -> {}", txnFlexDesc);
			int count = transactionflexservice.updateTransactionFlexDesc(txnFlexDesc);
			if (count > 0) {
				logger.info("txnFlexDesc  updated successfully");
				responseDto = responseBuilder.buildSuccessResponse(count, ResponseMessages.SUCCESS_TRANFLEXDESC_UPDATE,
						ResponseMessages.SUCCESS);
			} else if (count == 0) {
				logger.info("No Values updated txnFlexDesc ");
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.SUCCESS_TRANFLEXDESC_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);

			} else {
				logger.info("Failed to update txnFlexDesc ");
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_TRANFLEXDESC_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);

			}
		} catch (Exception e) {
			logger.error("Error while updating txnFlexDesc {}", e);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_TRANFLEXDESC_UPDATE,
					ResponseMessages.CONFIGSERVICE_EXCEPTION);
		}

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
}
