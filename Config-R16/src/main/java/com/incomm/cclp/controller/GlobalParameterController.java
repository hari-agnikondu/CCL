package com.incomm.cclp.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.GlobalParametersDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.GlobalParametersService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/globalParameters")
@Api(value = "global parameters")
public class GlobalParameterController {

	private static final Logger logger = LogManager.getLogger(GlobalParameterController.class);

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private GlobalParametersService globalParametersService;

	/**
	 * Gets Global Parameters
	 *
	 * @return the ResponseEntity with the result.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getGlobalParameters() {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		Map<String, Object> globalParameters = globalParametersService.getGlobalParameters();

		if (CollectionUtils.isEmpty(globalParameters)) {

			logger.info("Failed to fetch Global Parameters");

			responseDto = responseBuilder.buildSuccessResponse(globalParameters,
					("FAILED_GLOBAL_PARAMETERS_RETRIVE_" + ResponseMessages.DOESNOT_EXISTS),
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.debug("Global Parameters {} has retrieved successfully", globalParameters);

			responseDto = responseBuilder.buildSuccessResponse(globalParameters,
					("GLOBAL_PARAMETERS_RETRIVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		}

		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity(responseDto, HttpStatus.OK);
	}

	/**
	 * Update Global Parameters
	 *
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity updateGlobalParameters(@RequestBody GlobalParametersDTO globalParameterDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("Input global parameters Dto {}", globalParameterDto);

		ResponseDTO responseDto = null;

		ValidationService.validateGlobalParameters(globalParameterDto);

		try {
			logger.info("Updating global parameters in table {}", globalParameterDto);

			globalParametersService.updateGlobalParameters(globalParameterDto.getGlobalParameters());
			
			
			   Map<String, Object> globalParameters = globalParametersService.getGlobalParameters();
	            
	            globalParameterDto.setGlobalParameters(globalParameters);
	            
	            responseDto = responseBuilder.buildSuccessResponse(globalParameterDto,
	                    "GLOBAL_PARAMETERS_UPDATE_" + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);

		} catch (Exception e) {
			logger.error("Error while updating Global Parameters {}", e);

			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_GLOBAL_PARAMETERS_UPDATE,
					ResponseMessages.CONFIGSERVICE_EXCEPTION);
		}

		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity(responseDto, HttpStatus.OK);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	//@RequestMapping(value="/threadPoolUpdate/{chunkSize}/{threadsCount}/{maxThreadPoolSize}",method = RequestMethod.GET)
	@RequestMapping(value="/threadPoolUpdate",method = RequestMethod.GET)
	public ResponseEntity threadPoolConfiguration(@RequestParam(value="chunkSize",required=false,defaultValue="0") int chunkSize,
			@RequestParam(value="threadsCount",required=false,defaultValue="0") int threadsCount,@RequestParam(value="maxThreadPoolSize",required=false,defaultValue="0") int maxThreadPoolSize)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		try {
			logger.info("Updating thread configurations..");
		globalParametersService.updateThreadProperties(chunkSize,threadsCount,maxThreadPoolSize);
		

		
		responseDto = responseBuilder.buildSuccessResponse("SUCCESS",
                "GLOBAL_PARAMETERS_UPDATE_" + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
		} catch (Exception e) {
			logger.error("Error while updating Global Parameters {}", e);

			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_GLOBAL_PARAMETERS_UPDATE,
					ResponseMessages.CONFIGSERVICE_EXCEPTION);
		}

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity(responseDto, HttpStatus.OK);
		
		
	}
	

}
