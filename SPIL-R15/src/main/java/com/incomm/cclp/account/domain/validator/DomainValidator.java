package com.incomm.cclp.account.domain.validator;

import java.util.function.Supplier;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainValidator {

	/**
	 * 
	 * 
	 * @param validState      use the condition in the parameter that could be used in an if condition to check if the
	 *                        domain is in a valid state.
	 * @param level
	 * @param messageSupplier message supplier is to used to defer any expensive toString or such operation when the
	 *                        invalid state is encountered.
	 */
	public static void validateState(boolean validState, DomainValidationLevel level, Supplier<java.lang.String> messageSupplier) {

		if (validState) {
			return;
		}

		String message = messageSupplier == null ? "Null message supplier" : messageSupplier.get();
		if (level == DomainValidationLevel.ERROR) {
			log.error("Domain Validation failed for error:" + message);
			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED, message);
		} else {
			log.warn("Domain Validation failed for warning:" + message);
		}
	}

}
