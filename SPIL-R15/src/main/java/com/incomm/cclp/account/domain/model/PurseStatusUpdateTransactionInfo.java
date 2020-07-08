package com.incomm.cclp.account.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurseStatusUpdateTransactionInfo {

	private SpNumberType spNumberType;
	private String spNumber;
	private String purseName;
	private PurseStatusType purseStatus;
	private LocalDateTime transactionDateTime;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

}
