package com.incomm.cclp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountBalance {
	long accountPurseId;
	String purseName;
	BigDecimal availableBalance;
	String currencyCode;
	LocalDateTime expiryDate;
	String skuCode;
}
