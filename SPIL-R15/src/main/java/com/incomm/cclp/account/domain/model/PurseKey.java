package com.incomm.cclp.account.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PurseKey {

	private long accountId;
	private long purseId;

	public static PurseKey from(long accountId, long purseId) {
		return new PurseKey(accountId, purseId);
	}

}
