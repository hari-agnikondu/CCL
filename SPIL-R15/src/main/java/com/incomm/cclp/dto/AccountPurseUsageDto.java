package com.incomm.cclp.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountPurseUsageDto {

	private long accountId;
	private long purseId;

	private Map<String, Object> usageLimits;
	private Map<String, Object> usageFees;

	private LocalDateTime lastTransactionDate;

	private String purseStatus;

	private LocalDateTime startDate;
	private LocalDateTime endDate;

}