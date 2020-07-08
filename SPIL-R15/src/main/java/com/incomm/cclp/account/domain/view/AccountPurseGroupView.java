package com.incomm.cclp.account.domain.view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.incomm.cclp.account.domain.model.AccountPurseGroupKey;
import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.account.domain.model.ProductPurse;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountPurseGroupView {

	private final AccountPurseGroupKey accountPurseGroupKey;

	private final ProductPurse productPurse;

	private final List<AccountPurseViewNew> accountPurses;

	private Map<String, Object> usageLimits;
	private Map<String, Object> usageFees;

	private AccountPurseGroupStatus groupStatus;

	private LocalDateTime lastTransactionDate;

}
