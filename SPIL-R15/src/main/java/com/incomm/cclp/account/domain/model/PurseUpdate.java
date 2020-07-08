package com.incomm.cclp.account.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurseUpdate {

	private PurseKey purseKey;
	private PurseStatusType previousStatus;
	private PurseStatusType newStatus;

	private LocalDateTime previousStartDate;
	private LocalDateTime newStartDate;

	private LocalDateTime previousEndDate;
	private LocalDateTime newEndDate;

}
