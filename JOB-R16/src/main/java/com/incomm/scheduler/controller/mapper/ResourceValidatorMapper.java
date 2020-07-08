package com.incomm.scheduler.controller.mapper;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import com.incomm.scheduler.constants.PurseUpdateActionType;
import com.incomm.scheduler.constants.ResponseMessages;
import com.incomm.scheduler.controller.resource.CreateBatchPurseLoadJobRequestResource;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.service.command.CreatePurseLoadJobCommand;
import com.incomm.scheduler.utils.Util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceValidatorMapper {

	public static CreatePurseLoadJobCommand map(String correlationId, String userName, CreateBatchPurseLoadJobRequestResource request) {

		validateRequest(request);

		if (correlationId == null || correlationId.trim()
			.isEmpty()) {
			throw new ServiceException("correlationId is invalid", ResponseMessages.BAD_REQUEST);
		}

		if (userName == null || userName.trim()
			.isEmpty()) {
			throw new ServiceException("userName is invalid", ResponseMessages.BAD_REQUEST);
		}

		Optional<PurseUpdateActionType> actionType = PurseUpdateActionType.byAction(request.getAction());
		if (!actionType.isPresent()) {
			throw new ServiceException("actionType is invalid", ResponseMessages.BAD_REQUEST);
		}

		return CreatePurseLoadJobCommand.builder()
			.productId(request.getProductId())
			.partnerId(request.getPartnerId())
			.mdmId(request.getMdmId())
			.purseName(request.getPurseName())
			.actionType(actionType.get())
			.transactionAmount(request.getTransactionAmount())
			.effectiveDate(request.getEffectiveDate() == null ? null : mapZonedDateTime("effectiveDate", request.getEffectiveDate()))
			.expiryDate(request.getExpiryDate() == null ? null : mapZonedDateTime("expiryDate", request.getExpiryDate()))
			.skuCode(request.getSkuCode())
			.overrideCardStatus(request.getOverrideCardStatus())
			.build();

	}

	private static void validateRequest(CreateBatchPurseLoadJobRequestResource request) {
		if (request.getPartnerId() == null) {
			throw new ServiceException("partnerId is invalid", ResponseMessages.BAD_REQUEST);
		}

		if (request.getProductId() == null) {
			throw new ServiceException("productId is invalid", ResponseMessages.BAD_REQUEST);
		}

		if (Util.isEmpty(request.getMdmId())) {
			throw new ServiceException("mdmId is invalid", ResponseMessages.BAD_REQUEST);
		}

		if (Util.isEmpty(request.getPurseName())) {
			throw new ServiceException("purseName is invalid", ResponseMessages.BAD_REQUEST);
		}

		BigDecimal transactionAmount = request.getTransactionAmount();
		if (transactionAmount == null //
				|| transactionAmount.signum() <= 0 //
		) {
			throw new ServiceException("transactionAmount is invalid", ResponseMessages.BAD_REQUEST);
		}

		if (getNumberOfDecimalPlaces(transactionAmount) > 2) {
			throw new ServiceException("transactionAmount is invalid", ResponseMessages.BAD_REQUEST);
		}

		if ("null".equalsIgnoreCase(request.getSkuCode())) {
			throw new ServiceException("skuCode is invalid", ResponseMessages.BAD_REQUEST);
		}

		Optional<PurseUpdateActionType> actionType = PurseUpdateActionType.byAction(request.getAction());
		if (!actionType.isPresent()) {
			throw new ServiceException("action is invalid", ResponseMessages.BAD_REQUEST);
		}

	}

	public static ZonedDateTime mapZonedDateTime(String fieldName, String value) {
		try {
			return ZonedDateTime.parse(value);
		} catch (DateTimeParseException ex) {
			throw new ServiceException(fieldName + " is invalid", ResponseMessages.BAD_REQUEST);
		}
	}

	private static int getNumberOfDecimalPlaces(BigDecimal value) {
		String decimalPart = value.stripTrailingZeros()
			.toPlainString();
		int index = decimalPart.indexOf('.');
		return index < 0 ? 0 : decimalPart.length() - index - 1;
	}

}
