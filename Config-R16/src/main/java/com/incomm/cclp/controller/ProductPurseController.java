package com.incomm.cclp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.ProductPurseService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.util.ResponseBuilder;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/productPurse")
@Api(value = "productPurse")
public class ProductPurseController {
	@Autowired
	private ProductPurseService productPurseService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	private static final Logger logger = LogManager.getLogger(ProductPurseController.class);

	
	
	/**
	 * Updating the Product Purse Attributes by ProgramId
	 * 
	 * @throws ServiceException
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/updateLimitAttributesByProgramId/{programId}/program/{purseId}/purseId", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateLimitAttributesByProgramId(@RequestBody Map<String, Object> inputAttributes,
			@PathVariable("programId") Long programId,
			@PathVariable("purseId") Long purseId) throws IOException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<String> errorList;

		if (programId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);

			logger.error("Program id is Negative", programId);
		} else {
			errorList = productPurseService.updateProductPurseLimitAttributesByProgramId(inputAttributes, programId,purseId);
			if (CollectionUtils.isEmpty(errorList)) {
				logger.info("Updated product purse Limit attributes successfully");
				responseDto = responseBuilder.buildSuccessResponse(errorList,
						ResponseMessages.SUCCESS_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.SUCCESS);
			} else {
				logger.error("Error while updating product purse limit attributes");
				responseDto = responseBuilder.buildFailureResponse(errorList,
						ResponseMessages.FAILED_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.FAILURE);
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateTxnFeeAttributesByProgramId/{programId}/program/{purseId}/purseId", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateTxnFeeAttributesByProgramId(@RequestBody Map<String, Object> inputAttributes,
			@PathVariable("programId") Long programId,
			@PathVariable("purseId") Long purseId) throws IOException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<String> errorList;

		if (programId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);

			logger.error("Program id is Negative", programId);
		} else {
			errorList = productPurseService.updateProductPurseTxnFeeAttributesByProgramId(inputAttributes, programId,purseId);
			if (CollectionUtils.isEmpty(errorList)) {
				logger.info("Updated product purse Txn Fee attributes successfully");
				responseDto = responseBuilder.buildSuccessResponse(errorList,
						ResponseMessages.SUCCESS_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.SUCCESS);
			} else {
				logger.error("Error while updating product purse Txn Fee attributes");
				responseDto = responseBuilder.buildFailureResponse(errorList,
						ResponseMessages.FAILED_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.FAILURE);
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateMonthlyCapAttributesByProgramId/{programId}/program/{purseId}/purseId", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateMonthlyCapAttributesByProgramId(@RequestBody Map<String, Object> inputAttributes,
			@PathVariable("programId") Long programId,
			@PathVariable("purseId") Long purseId) throws IOException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<String> errorList;

		if (programId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);

			logger.error("Program id is Negative", programId);
		} else {
			errorList = productPurseService.updateProductPurseMonthlyCapAttributesByProgramId(inputAttributes, programId,purseId);
			if (CollectionUtils.isEmpty(errorList)) {
				logger.info("Updated product purse MonthlyCap attributes successfully");
				responseDto = responseBuilder.buildSuccessResponse(errorList,
						ResponseMessages.SUCCESS_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.SUCCESS);
			} else {
				logger.error("Error while updating product purse MonthlyCap attributes");
				responseDto = responseBuilder.buildFailureResponse(errorList,
						ResponseMessages.FAILED_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.FAILURE);
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	
}
