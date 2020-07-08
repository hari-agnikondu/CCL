package com.incomm.cclp.account.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class TransactionResponseInfo {
	private final String responseCode;
	private final String responseMessage;
	private final CardEntity cardEntity;

	public static TransactionResponseInfo from(String responseCode, String responseMessage, CardEntity cardEntity) {
		return new TransactionResponseInfo(responseCode, responseMessage, cardEntity);
	}
}
