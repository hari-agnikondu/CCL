package com.incomm.scheduler.external.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadAccountPurseResponse {

	private String responseCode;
	private String responseMessage;

	private String correlationId;
	private String spNumberType;
	private String spNumber;
	private String mdmId;

	private String deliveryChannel;
	private String username;

	private String purseName;
	private String accountPurseId;

	private ZonedDateTime effectiveDate;
	private ZonedDateTime expiryDate;

	private BigDecimal purseValue;
	private String currency;
	private String skuCode;

	private String terminalId;
	private String storeId;

	private String upc;
	private String actionType;

	private BigDecimal availablePurseBalance;
	private BigDecimal authAmount;
	private String cardStatus;
	private String batchId;
	private LocalDateTime date;

}
