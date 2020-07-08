package com.incomm.cclp.account.domain.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.dto.PurseAuthResponse;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountSummaryView {

	private AccountTransactionCommand command;

	private String responseCode;
	private String responseMessage;

	private long transacionId;
	private long authId;
	private BigDecimal authorizedAmount;
	private BigDecimal sourceCardBalance;

	private BigDecimal cardSerialNumber;
	private CardStatusType sourceCardStatus;

	private Optional<CardStatusType> targetCardStatus;
	private Optional<BigDecimal> targetCardBalance;

	private String currencyCode;

	private String digitalPin;

	private BigDecimal transactionFee;

	private List<PurseAuthResponse> purseAuthResponses;

	private List<AccountPurseGroupView> accountPurseGroupViews;

}
