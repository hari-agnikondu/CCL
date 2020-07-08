package com.incomm.cclp.account.rest.mapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.rest.resources.ErrorDetails;
import com.incomm.cclp.constants.APIConstants;
import com.incomm.cclp.exception.ServiceException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice(basePackages = "com.incomm.cclp.account.rest.controller")
public class RestExceptionHandler {

	private static final String SERVER_ERROR_MESSAGE = "Error occured while processing the request.";

	@ExceptionHandler(DomainException.class)
	public final ResponseEntity<ErrorDetails> handleDomainException(DomainException exception, WebRequest request) {

		HttpStatus mappedHttpStatus = this.mapExceptionType(exception.getExceptionType());

		String message = exception.getExceptionType() == DomainExceptionType.SYSTEM_ERROR ? SERVER_ERROR_MESSAGE : exception.getMessage();

		ErrorDetails errorDetails = ErrorDetails.from(exception.getExceptionType()
			.getResponseCode(), message, exception.getSystemMessage());

		HttpHeaders headers = ResourceMapper.map(APIConstants.API_HEADER_CORRELATION_ID,
				request.getHeader(APIConstants.API_HEADER_CORRELATION_ID));

		return new ResponseEntity<>(errorDetails, headers, mappedHttpStatus);
	}

	@ExceptionHandler(ServiceException.class)
	public final ResponseEntity<ErrorDetails> handleServiceException(ServiceException exception, WebRequest request) {

		HttpStatus mappedHttpStatus = HttpStatus.BAD_REQUEST;

		String message = exception.getResponseID() + ": " + exception.getMessage();

		ErrorDetails errorDetails = ErrorDetails.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED.getResponseCode(), message,
				exception.getReason());

		HttpHeaders headers = ResourceMapper.map(APIConstants.API_HEADER_CORRELATION_ID,
				request.getHeader(APIConstants.API_HEADER_CORRELATION_ID));

		return new ResponseEntity<>(errorDetails, headers, mappedHttpStatus);
	}

	@ExceptionHandler({ org.springframework.http.converter.HttpMessageNotReadableException.class })
	public final ResponseEntity<ErrorDetails> handleInvalidRequestExceptions(Exception exception, WebRequest request) {

		log.warn("Invalid request received: {}", exception.getMessage());
		HttpStatus mappedHttpStatys = HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorDetails errorDetails = ErrorDetails.from(DomainExceptionType.INPUT_VALIDATION_FAILED.getResponseCode(),
				DomainExceptionType.INPUT_VALIDATION_FAILED.getDefaultMessage(), exception.getMessage());
		return new ResponseEntity<>(errorDetails, mappedHttpStatys);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception exception, WebRequest request) {

		log.error(exception.getMessage(), exception);
		HttpStatus mappedHttpStatys = HttpStatus.INTERNAL_SERVER_ERROR;

		ErrorDetails errorDetails = ErrorDetails.from(DomainExceptionType.SYSTEM_ERROR.getResponseCode(), SERVER_ERROR_MESSAGE,
				exception.getMessage());
		return new ResponseEntity<>(errorDetails, mappedHttpStatys);
	}

	private HttpStatus mapExceptionType(DomainExceptionType exceptionType) {

		try {
			return HttpStatus.valueOf(exceptionType.getErrorCode());
		} catch (IllegalArgumentException exception) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}
