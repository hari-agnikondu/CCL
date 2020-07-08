package com.incomm.cclp.account.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AccountPurseGroupStatus {

	PurseStatusType purseStatusType;
	LocalDateTime startDate;
	LocalDateTime endDate;

	public static AccountPurseGroupStatus from(PurseStatusType purseStatusType, LocalDateTime startDate, LocalDateTime endDate) {
		return new AccountPurseGroupStatus(purseStatusType, startDate, endDate);
	}

	public static AccountPurseGroupStatus from(PurseStatusType purseStatusType) {
		return new AccountPurseGroupStatus(purseStatusType, null, null);
	}

	public boolean isActive() {
		return this.purseStatusType == PurseStatusType.ACTIVE;
	}

}
