package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountPurseUpdate { // TODO Later SPIL: remove this class when this class support purseLoad
	private AccountPurseKey accountPurseKey;

	private long productId;

	private boolean isNewAccountPurse;

	private PurseType pursetype;

	private BigDecimal transactionAmount;

	private BigDecimal previousLedgerBalance;
	private BigDecimal previousAvailableBalance;

	private BigDecimal newLedgerBalance;
	private BigDecimal newAvailableBalance;

	private BigDecimal authorizedAmount;

	private LocalDateTime effectiveDate;
	private LocalDateTime expiryDate;

	private String currencyCode;

	private String skuCode;

	private boolean isNewPurseUsage;

	private Map<String, Object> previousUsageLimit;
	private Map<String, Object> previousUsageFee;

	private Map<String, Object> newUsageLimit;
	private Map<String, Object> newUsageFee;

	private AccountPurseGroupStatus previousGroupStatus;
	private AccountPurseGroupStatus newGroupStatus;

	private String topupStatus;

}
