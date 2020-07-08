package com.incomm.cclp.account.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Value;

@Value
public class AccountPurseAltKeyAttributes implements Serializable {

	private static final long serialVersionUID = 2763009561359601008L;

	public static final AccountPurseAltKeyAttributes DEFAULT = AccountPurseAltKeyAttributes.from(null, null, null);

	private LocalDateTime effectiveDate;
	private LocalDateTime expiryDate;
	private String skuCode;

	private AccountPurseAltKeyAttributes(LocalDateTime effectiveDate, LocalDateTime expiryDate, String skuCode) {
		super();
		this.effectiveDate = effectiveDate;
		this.expiryDate = expiryDate;
		this.skuCode = skuCode;
	}

	public static AccountPurseAltKeyAttributes from(LocalDateTime effectiveDate, LocalDateTime expiryDate, String skuCode) {
		return new AccountPurseAltKeyAttributes(effectiveDate, expiryDate, skuCode);
	}

}
