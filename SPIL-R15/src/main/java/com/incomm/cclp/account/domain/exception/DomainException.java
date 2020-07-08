package com.incomm.cclp.account.domain.exception;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 7823321596619251936L;

	private final DomainExceptionType exceptionType;
	private final String correlationId;
	private final String systemMessage;

	@Builder
	DomainException(DomainExceptionType exceptionType, String correlationId, String message, String systemMessage, Throwable cause) {
		super(message, cause);
		this.exceptionType = exceptionType;
		this.correlationId = correlationId;
		this.systemMessage = systemMessage;
	}

}
