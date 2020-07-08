/**
 * 
 */
package com.incomm.cclp.exception;

/**
 * ServiceException class handles all the exceptions from the Service layer.
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String responseID;
	private String reason;
	private Throwable serviceExecption;

	public ServiceException() {

		super();
	}

	public ServiceException(String message) {

		super(message);
	}

	public ServiceException(String message, String responseID) {

		super(message);
		setResponseID(responseID);
	}

	public ServiceException(String message, String reason, String responseID) {

		super(message);
		setReason(reason);
		setResponseID(responseID);
	}

	public ServiceException(String message, String responseID, Throwable t) {

		super(message);
		setResponseID(responseID);
		setCclpAuthExecption(t);
	}

	public void setCclpAuthExecption(Throwable cclpAuthExecption) {

		this.serviceExecption = cclpAuthExecption;
	}

	public Throwable getCclpAuthExecption() {

		return serviceExecption;
	}

	public void setResponseID(String responseID) {

		this.responseID = responseID;
	}

	public String getResponseID() {
		return responseID;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
