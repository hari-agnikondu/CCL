package com.incomm.cclp.account.application.command;

import java.math.BigDecimal;
import java.util.Optional;

import com.incomm.cclp.dto.ValueDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountTransactionCommand {

	private TransactionInfo transactionInfo;

	private BigDecimal transactionAmount;
	private Optional<String> transactionCurrency;

	private Optional<String> cardExpiryDate;

	private Long defaultPurseId;

	private Optional<String> purseName;
	private Optional<Long> accountPurseId;
	private Optional<String> purseCurrency;
	private Optional<String> skuCode;

	private Optional<SpNumber> targetSpNumber;

	private ValueDTO valueDTO;

}
