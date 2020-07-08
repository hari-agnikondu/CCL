package com.incomm.cclp.account.domain.exception;

public class DomainExceptionFactory {

	private DomainExceptionFactory() {
		// NOOP constructor.
	}

	public static final DomainException.DomainExceptionBuilder builder = DomainException.builder();

	public static DomainException from(DomainExceptionType exceptionType, String message) {
		return DomainException.builder()
			.exceptionType(exceptionType)
			.message(message)
			.build();
	}

	public static DomainException from(DomainExceptionType exceptionType, String message, String systemMessage) {
		return DomainException.builder()
			.exceptionType(exceptionType)
			.message(message)
			.systemMessage(systemMessage)
			.build();
	}

}
