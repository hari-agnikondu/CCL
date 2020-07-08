package com.incomm.scheduler.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BatchLoadAccountPurseLog {

	private long batchId;
	private long requestId;

	private String correlationId;

	private String accountNumber;
	private Long accountPurseId;

	private String responseCode;
	private String responseMessage;

	private BigDecimal availableBalance;
	private BigDecimal authAmount;

	private Long insertedByUser;
	private LocalDateTime insertedDate;
	private Long lastUpdatedByUser;
	private LocalDateTime lastUpdatedDate;

}
