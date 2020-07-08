/**
 * 
 */
package com.incomm.cclp.exception;

/**
 * SuspendCardException class handles all the exceptions from the Service layer.
 *
 */
public class SuspendCardException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public SuspendCardException() {

		super();
	}

	public SuspendCardException(String message) {

		super(message);
	}

	public SuspendCardException(String message, String responseID) {

		super(message);
		setResponseID(responseID);
	}
}