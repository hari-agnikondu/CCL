package com.incomm.cclp.dto;

import java.math.BigDecimal;
import java.util.List;

import com.incomm.cclp.domain.AccountPurseBalance;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResponseInfo {

	private String respCode;
	private String errMsg;
	private String authId;
	private double authorizedAmt;
	private String cardStatus;
	private BigDecimal tranSeqId;
	private BigDecimal serialNumber;
	private String cardNumber;
	private String currCode;
	private String digitalPin;
	private List<AccountPurseBalance> purseBalance;
	private AccountPurseDTO accountPurse;

}
