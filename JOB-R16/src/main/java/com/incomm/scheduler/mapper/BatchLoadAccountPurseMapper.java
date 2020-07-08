package com.incomm.scheduler.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.incomm.scheduler.external.resource.LoadAccountPurseRequest;
import com.incomm.scheduler.model.BatchLoadAccountPurse;
import com.incomm.scheduler.model.BatchLoadAccountPurseLog;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchLoadAccountPurseMapper {
	
	private static final String SP_NUMBER_TYPE = "ACCOUNT_NUMBER";

	public static LoadAccountPurseRequest map(BatchLoadAccountPurse request, BatchLoadAccountPurseLog log) {
		return LoadAccountPurseRequest.builder()
			.correlationId(log.getCorrelationId())
			.spNumberType(SP_NUMBER_TYPE)
			.spNumber(log.getAccountNumber())
			.action(request.getActionType())
			.accountPurseId(mapLong(log.getAccountPurseId()))
			.effectiveDate(mapLocalDateTime(request.getEffectiveDate()))
			.expiryDate(mapLocalDateTime(request.getExpiryDate()))
			.mdmId(request.getMdmId())
			.purseName(request.getPurseName())
			.skuCode(request.getSkuCode())
			.transactionAmount(request.getTransactionAmount()
				.toString())
			.percentageAmount(request.getPercentageAmount()
				.toString())
			.build();

	}

	static String mapLocalDateTime(LocalDateTime dateTime) {
		
		return dateTime == null ? null
				: dateTime.atZone(ZoneId.systemDefault())
					.format(DateTimeFormatter.ISO_DATE_TIME);
	}

	static String mapLong(Long value) {
		return value == null || value == 0L ? null : Long.toString(value);
	}

}
