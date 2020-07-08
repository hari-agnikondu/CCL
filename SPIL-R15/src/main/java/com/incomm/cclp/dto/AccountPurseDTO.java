package com.incomm.cclp.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountPurseDTO {

	private long accountPurseId;
	private String purseName;

	private BigDecimal transactionAmount;
	private BigDecimal authAmount;

	private String purseType;
	private String skuCode;

	private Date effectiveDate;
	private Date expiryDate;

}
