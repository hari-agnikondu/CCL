/**
 * 
 */
package com.incomm.scheduler.exception;

import lombok.ToString;

/**
 * ServiceException class handles all the exceptions from the Service layer.
 * 
 * @author abutani
 *
 */
@ToString
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String code;
	private final String message;

	public ServiceException(String message) {
		super(message);
		this.message = message;
		this.code = null;
	}

	public ServiceException(String message, String code) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
