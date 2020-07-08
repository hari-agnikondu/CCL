package com.incomm.cclp.account.domain.exception;

import lombok.Getter;

@Getter
public enum DomainExceptionType {

	INPUT_VALIDATION_FAILED(400, "RC001", "Invalid Request."),
	NOT_FOUND(400, "RC002", "Not Found"),
	DUPLICATE_REQUEST(400, "RC003", "Duplicate Request."),
	DOMAIN_VALIDATION_FAILED(400, "RC004", "Validation Failed."),
	RETRY_REQUEST(200, "RC005", "Retry Request."),
	SYSTEM_ERROR(500, "RC999", "System Error.");

	final private int errorCode;
	final private String responseCode;
	final private String defaultMessage;

	private DomainExceptionType(int errorCode, String responseCode, String defaultMessage) {
		this.errorCode = errorCode;
		this.responseCode = responseCode;
		this.defaultMessage = defaultMessage;
	}

}
