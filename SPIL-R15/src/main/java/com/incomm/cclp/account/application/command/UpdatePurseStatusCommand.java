package com.incomm.cclp.account.application.command;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.incomm.cclp.account.domain.model.PurseStatusType;

import lombok.Builder;
import lombok.Value;

@Value
public class UpdatePurseStatusCommand implements Command {

	public static final String TRANSACTION_SHORT_CODE = "updatePurseStatus";
	public static final String TRANSACTION_CODE = "103";

	@NotNull
	private final TransactionInfo transactionInfo;

	@NotNull
	@Size(min = 1, max = 15)
	private final String purseName;

	@NotNull
	private final PurseStatusType purseStatus;

	private final ZonedDateTime startDate;
	private final ZonedDateTime endDate;

	@Builder
	public UpdatePurseStatusCommand( //
			TransactionInfo transactionInfo, //
			String purseName, //
			String purseStatus, //
			String startDate, String endDate) {

		this.transactionInfo = transactionInfo;
		this.purseName = purseName;
		this.purseStatus = CommandValidator.mapPurseStatusTypeByName(purseStatus);

		this.startDate = startDate == null ? null : CommandValidator.mapZonedDateTime("startDate", startDate);
		this.endDate = endDate == null ? null : CommandValidator.mapZonedDateTime("endDate", endDate);

		CommandValidator.validate(this);
		CommandValidator.validateRequiredValue("spNumberType", transactionInfo.getSpNumberType());

	}

}
