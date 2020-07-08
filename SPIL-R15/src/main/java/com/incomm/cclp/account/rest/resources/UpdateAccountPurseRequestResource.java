package com.incomm.cclp.account.rest.resources;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UpdateAccountPurseRequestResource {

	private String spNumberType;
	private String spNumber;

	private String mdmId;

	private String action;
	private String purseName;
	private String accountPurseId;
	private String effectiveDate;
	private String expiryDate;

	private String transactionAmount;
	private String currency;
	private String skuCode;

	private String terminalId;
	private String storeId;

	private String upc;

	private BigDecimal percentageAmount;

}
