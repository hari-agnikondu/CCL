package com.incomm.scheduler.service.command;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.incomm.scheduler.constants.PurseUpdateActionType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreatePurseLoadJobCommand {

	private String correlationId;
	private long partnerId;
	private long productId;
	private String purseName;
	private BigDecimal transactionAmount;
	private ZonedDateTime effectiveDate;
	private ZonedDateTime expiryDate;
	private String skuCode;
	private PurseUpdateActionType actionType;
	private String mdmId;
	private List<String> overrideCardStatus;

	private String userName;

}
