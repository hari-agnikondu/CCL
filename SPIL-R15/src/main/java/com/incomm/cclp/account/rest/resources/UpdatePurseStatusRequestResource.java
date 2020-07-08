package com.incomm.cclp.account.rest.resources;

import lombok.Data;

@Data
public class UpdatePurseStatusRequestResource {

	private String spNumberType;
	private String spNumber;
	private String mdmId;

	private String purseName;
	private String purseStatus;

	private String terminalId;
	private String storeId;

	private String upc;

	private String startDate;
	private String endDate;

}
