package com.incomm.cclp.account.domain.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.incomm.cclp.account.domain.model.AccountPurseAltKeyAttributes;
import com.incomm.cclp.account.domain.model.AccountPurseKey;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountPurseViewNew {

	private final AccountPurseKey accountPurseKey;
	private final AccountPurseAltKeyAttributes accountPurseAltKeyAttributes;

	private BigDecimal ledgerBalance;
	private BigDecimal availableBalance;

	private LocalDateTime firstLoadDate;

}
