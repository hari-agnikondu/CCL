package com.incomm.cclp.account.application.command;

import static com.incomm.cclp.account.util.CodeUtil.isNull;
import static com.incomm.cclp.account.util.CodeUtil.isNullOrEmpty;
import static com.incomm.cclp.account.util.CodeUtil.not;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.DeliveryChannelType;
import com.incomm.cclp.account.domain.model.PurseStatusType;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.config.SpringContext;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandValidator {

	private static final String DATETIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss z";
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATETIME_FORMAT_STRING);

	public static ZonedDateTime mapZonedDateTime(String fieldName, String value) {

		try {
			return ZonedDateTime.parse(value, DATE_FORMAT);
		} catch (DateTimeParseException ex) {
			// no op
		}

		try {
			return ZonedDateTime.parse(value);
		} catch (DateTimeParseException ex) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, fieldName + " is invalid:" + value);
		}
	}

	public static SpNumberType mapSpNumberType(String spNumberType) {
		return SpNumberType.byName(spNumberType)
			.orElseThrow(() -> DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
					"spNumberType is invalid:" + spNumberType));

	}

	public static DeliveryChannelType mapDeliveryChannelTypeByName(String deliveryChannel) {
		return DeliveryChannelType.byName(deliveryChannel)
			.orElseThrow(() -> DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
					"Delivery channel is invalid:" + deliveryChannel));

	}

	public static DeliveryChannelType mapDeliveryChannelTypeByCode(String deliveryChannelCode) {
		return DeliveryChannelType.byChannelCode(deliveryChannelCode)
			.orElseThrow(() -> DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
					"Delivery channel is invalid:" + deliveryChannelCode));

	}

	public static PurseUpdateActionType mapPurseUpdateActionTypeByName(String actionType) {
		return PurseUpdateActionType.byAction(actionType)
			.orElseThrow(
					() -> DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, "actionType is invalid:" + actionType));

	}

	public static BigDecimal mapBigDecimal(String fieldName, String value, String validationPattern) {

		if (CodeUtil.isNullOrEmpty(value)) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, fieldName + " is invalid:" + value);
		}

		if (!validateRegex(value, validationPattern)) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, fieldName + " is invalid:" + value);
		}

		return new BigDecimal(value);
	}

	public static Long mapLong(String fieldName, String value, int maxLength) {

		if (value == null || value.isEmpty() || value.length() > maxLength) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, fieldName + " is invalid:" + value);
		}

		try {
			return Long.parseLong(value);
		} catch (NumberFormatException exception) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, fieldName + " is invalid:" + value);
		}
	}

	public static boolean validateRegex(String value, String regex) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		return pattern.matcher(value)
			.matches();
	}

	public static PurseStatusType mapPurseStatusTypeByName(String purseStatus) {

		return PurseStatusType.byName(purseStatus)
			.orElseThrow(() -> DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
					"purseStatus is invalid:" + purseStatus));
	}

	public static void validateNotNull(String fieldName, String value) {
		if (isNullOrEmpty(value)) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, fieldName + " is invalid:" + value);
		}
	}

	public static <T> void validateRequiredValue(String fieldName, Optional<T> value) {
		if (isNull(value) || not(value.isPresent())) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, fieldName + " is invalid:");
		}
	}

	public static <T> void validate(T bean) {
		Validator beanValidator = CommandValidator.getValidator();
		Set<ConstraintViolation<T>> violations = beanValidator.validate(bean);

		violations.stream()
			.findFirst()
			.ifPresent(violation -> {

				throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
						violation.getPropertyPath() + " is invalid:" + violation.getInvalidValue());

			});
	}

	static Validator getValidator() {
		Validator beanValidator = SpringContext.getBean(Validator.class);
		return beanValidator == null ? Validation.buildDefaultValidatorFactory()
			.getValidator() : beanValidator;
	}

}
