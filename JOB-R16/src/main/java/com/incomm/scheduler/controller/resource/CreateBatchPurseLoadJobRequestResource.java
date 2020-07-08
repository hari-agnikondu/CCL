package com.incomm.scheduler.controller.resource;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBatchPurseLoadJobRequestResource {

	private Long partnerId;
	private Long productId;
	private String purseName;
	private BigDecimal transactionAmount;
	private String effectiveDate;
	private String expiryDate;
	private String skuCode;
	private String action;
	private String mdmId;
	private List<String> overrideCardStatus;

}
