package com.incomm.scheduler.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BatchLoadAccountPurse {

	private long batchId;

	private long partnerId;
	private long productId;
	private String purseName;
	private String mdmId;

	private BigDecimal transactionAmount;

	private LocalDateTime effectiveDate;
	private LocalDateTime expiryDate;

	private String skuCode;

	private String actionType;
	private String overrideCardStatus;
	
	private String status;
	

	private long insertedByUser;
	private LocalDateTime insertedDate;
	private long lastUpdatedByUser;
	private LocalDateTime lastUpdatedDate;
	
	private LocalDate nextRunDate;
	private BigDecimal percentageAmount;
	private long preemptDays;
	
	

}
