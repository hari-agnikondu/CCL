package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(value = AccessLevel.PACKAGE)
public class AccountEntity implements AccountInterface {

	private final long accountId;
	private final String accountNumber;
	private String accountStatus;
	private final BigInteger productId;
	private BigDecimal initialLoadAmount;
	private BigDecimal newInitialLoadAmount;
	private LocalDateTime lastUpdateDate;
	private final BigInteger typeCode;
	private final Short statCode;
	private final String activeFlag;
	private final String redemptionDelayFlag;

	private boolean dirty = false;

	@Builder
	public AccountEntity(long accountId, //
			String accountNumber, //
			String accountStatus, //
			BigInteger productId, //
			BigDecimal initialLoadAmount, //
			BigDecimal newInitialLoadAmount, //
			LocalDateTime lastUpdateDate, //
			BigInteger typeCode, //
			Short statCode, //
			String activeFlag, //
			String redemptionDelayFlag) {
		super();
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.accountStatus = accountStatus;
		this.productId = productId;
		this.initialLoadAmount = initialLoadAmount;
		this.newInitialLoadAmount = newInitialLoadAmount;
		this.lastUpdateDate = lastUpdateDate;
		this.typeCode = typeCode;
		this.statCode = statCode;
		this.activeFlag = activeFlag;
		this.redemptionDelayFlag = redemptionDelayFlag;
	}

	public AccountUpdate updateInitialLoadAmount(BigDecimal loadedAmountBeforeFees) {

		return apply(AccountUpdate.builder()
			.accountId(accountId)
			.previousInitialLoadAmount(initialLoadAmount)
			.previousNewInitialLoadAmount(newInitialLoadAmount)
			.initialLoadAmount(loadedAmountBeforeFees)
			.newInitialLoadAmount(loadedAmountBeforeFees)
			.lastUpdateDate(lastUpdateDate)
			.build());
	}

	private AccountUpdate apply(AccountUpdate accountUpdate) {
		this.dirty = true;
		this.initialLoadAmount = accountUpdate.getInitialLoadAmount();
		this.newInitialLoadAmount = accountUpdate.getNewInitialLoadAmount();
		return accountUpdate;
	}

}
