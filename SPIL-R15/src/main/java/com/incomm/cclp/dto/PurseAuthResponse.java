package com.incomm.cclp.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurseAuthResponse {
	long accountPurseId;
	String purseName;
	BigDecimal transactionAmount;
	BigDecimal availableBalance;
	String currencyCode;
	String skuCode;
}
