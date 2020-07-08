package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;

public interface AccountInterface {

	public AccountUpdate updateInitialLoadAmount(BigDecimal transactionAmount);
}
