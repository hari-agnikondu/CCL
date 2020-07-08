package com.incomm.cclp.account.domain.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountPurseView implements DomainView {

	private TransactionInfo transactionInfo;
	private String responseCode;
	private String responseMessage;
	private LocalDateTime date;
	private String transactionId;

	private String purseName;
	private String accountPurseId;

	private ZonedDateTime effectiveDate;
	private ZonedDateTime expiryDate;

	private BigDecimal transactionAmount;
	private BigDecimal authorizedAmount;
	private BigDecimal availablePurseBalance;

	private String currency;
	private String skuCode;

	private String cardStatus;
	private PurseUpdateActionType action;

}
