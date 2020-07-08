/**
 * 
 */
package com.incomm.cclp.gpp.security;

/**
 * @author viyer
 * 
 */
public class APISecurityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public APISecurityException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public APISecurityException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public APISecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param cause
	 */
	public APISecurityException(Throwable cause) {
		super(cause);
	}
}
