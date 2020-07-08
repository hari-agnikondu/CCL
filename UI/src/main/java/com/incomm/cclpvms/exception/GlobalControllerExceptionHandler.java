/**
 * 
 */
package com.incomm.cclpvms.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.constants.ResponseMessages;


/**
 * GlobalControllerExceptionHandler class handles all the exceptions at the
 * controller level in a consistent manner.
 * 
 * @author abutani
 *
 */
@ControllerAdvice(basePackages = {"com.incomm.cclpvms.config"})
public class GlobalControllerExceptionHandler{
	private static final Logger logger = LogManager.getLogger(GlobalControllerExceptionHandler.class);

	ModelAndView mav = new ModelAndView();

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {

		String errMessage = ResponseMessages.GENERIC_ERR_MESSAGE;
		
		logger.error("Exception in GlobalControllerExceptionHandler"+exception.getMessage());

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();
		
		mav.addObject("statusFlag", "fail");
		mav.setViewName("serviceError");
		mav.addObject("statusMessage", errMessage);
		
		return mav;

	}

}
