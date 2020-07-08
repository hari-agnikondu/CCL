/**
 * 
 */
package com.incomm.scheduler.exception;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.incomm.scheduler.constants.ResponseMessages;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.utils.ResponseBuilder;




/**
 * GlobalControllerExceptionHandler class handles all the exceptions 
 * at the controller level in a consistent manner.
 * 
 * @author abutani
 *
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@Autowired
	private ResponseBuilder responseBuilder;
	

	private static final Logger logger = LogManager.getLogger(GlobalControllerExceptionHandler.class);
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity exceptionHandler(Exception exception) 
	{
		
		String errMessage = ResponseMessages.GENERIC_ERR_MESSAGE;	
		
		logger.error("Exception: ", exception);
		
		if (exception instanceof ServiceException)
			errMessage  = exception.getMessage();
		
		logger.error(errMessage);
		
		ResponseDTO responseDto = responseBuilder.buildFailureResponse(errMessage,errMessage);
	
		return new ResponseEntity(responseDto, HttpStatus.OK);
	}

}
