/**
 * 
 */
package com.incomm.cclpvms.exception;

/**
 * ServiceException class handles all the exceptions from the Service layer.
 *
 *
 */

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException(String message)
	{
		super(message);
	}
	
}
