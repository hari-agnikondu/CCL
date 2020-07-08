package com.incomm.scheduler.model;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
@Getter
public class PartnerProductInfo {

	private long partnerId;
	private String partnerName;
	private String mdmId;

	private long productId;
	private String productName;
	
	private String productAttributes ;

	private long purseId;
	private String purseName;
	private int purseTypeId;

	private Boolean isDefault;
	
	private int preemptDays;


}
