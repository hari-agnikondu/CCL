package com.incomm.cclp.account.domain.view;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.model.PurseStatusType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurseView implements DomainView {

	private TransactionInfo transactionInfo;
	private String responseCode;
	private String responseMessage;
	private LocalDateTime date;
	private String transactionId;

	private String purseName;
	private PurseStatusType purseStatus;

	private ZonedDateTime purseStartDate;
	private ZonedDateTime purseEndDate;

}
