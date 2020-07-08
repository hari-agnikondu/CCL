package com.incomm.cclp.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountPurseBalance {

	// TODO Later rename this class to AccountPurseInfo when the AccountPurseInfo is not longer used.

	private long accountId;
	private long purseId;
	private long productId;
	private long accountPurseId;

	private BigDecimal ledgerBalance;
	private BigDecimal availableBalance;

	private LocalDateTime effectiveDate;
	private LocalDateTime expiryDate;

	private String purseType;
	private String currencyCode;
	private int purseTypeId;
	private String skuCode;

	private LocalDateTime firstLoadDate;

	private String topupStatus;
}
