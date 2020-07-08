
package com.incomm.cclp.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetail {

	private String accountNumber;
	private long accountId;
	private long productId;
	private long partnerId;
	private long customerCode;

	private long issuerId;

	private String cardNumberEncrypted;
	private String cardNumberHash;
	private String cardNumber;
	private String serialNumber;
	private String proxyNumber;
	private String lastFourDigit;

	private String expiryDate;

	private String oldCardStatus;
	private String cardStatus;
	private String isRedeemed;

	private String digitalPin;

	private LocalDateTime dateOfActication;
	private String firstTimeTopUp;
	private String starterCardFlag;

	private LocalDateTime lastTransactionDate;

	private String profileCode;
	private Integer replFlag;

	private LocalDateTime dbSysDate;

}
