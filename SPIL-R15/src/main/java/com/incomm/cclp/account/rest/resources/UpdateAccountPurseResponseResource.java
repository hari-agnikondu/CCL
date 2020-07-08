package com.incomm.cclp.account.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class UpdateAccountPurseResponseResource {

	private String responseCode;
	private String responseMessage;
	private LocalDateTime date;

	private String spNumberType;
	private String spNumber;
	private String purseName;
	private String accountPurseId;

	private String effectiveDate;
	private String expiryDate;

	private BigDecimal transactionAmount;
	private BigDecimal authAmount;
	private BigDecimal availablePurseBalance;

	private String currency;
	private String skuCode;

	private String cardStatus;
	private String transactionId;
	private PurseUpdateActionType action;

}
