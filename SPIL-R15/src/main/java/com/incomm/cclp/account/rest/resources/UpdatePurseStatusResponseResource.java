package com.incomm.cclp.account.rest.resources;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class UpdatePurseStatusResponseResource {

	private String responseCode;
	private String responseMessage;
	private LocalDateTime date;
	private String transactionId;

	private String spNumberType;
	private String spNumber;

	private String purseName;
	private String purseStatus;

	private String startDate;
	private String endDate;

}
