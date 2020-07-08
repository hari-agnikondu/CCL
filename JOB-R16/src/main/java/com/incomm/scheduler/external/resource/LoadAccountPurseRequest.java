package com.incomm.scheduler.external.resource;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoadAccountPurseRequest {

	private String correlationId;
	private String spNumberType;
	private String spNumber;
	private String mdmId;

	private String action;
	private String deliveryChannel;

	private String purseName;
	private String accountPurseId;
	private String effectiveDate;
	private String expiryDate;

	private String transactionAmount;
	private String currency;
	private String skuCode;
	
	private String percentageAmount;

}
