package com.incomm.cclp.account.application.command;

import static com.incomm.cclp.account.util.CodeUtil.isNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;
import com.incomm.cclp.account.util.DateTimeUtil;

import lombok.Builder;
import lombok.Value;

@Value
public class UpdateRolloverAccountPurseCommand implements Command {
	@NotNull
	private final TransactionInfo transactionInfo;

	@NotNull
	@Size(min = 1, max = 15)
	private final String purseName;

	@NotNull
	private final BigDecimal transactionAmount;

	@Pattern(regexp = "[a-zA-Z \\s]{1,6}")
	private final String currency;

	@Pattern(regexp = "[a-zA-Z0-9-_]{1,20}")
	private final String skuCode;

	@NotNull
	private final PurseUpdateActionType actionType;

	private final Long accountPurseId;
	private final ZonedDateTime effectiveDate;
	private final ZonedDateTime expiryDate;

	private final BigDecimal percentageAmount;

	@Builder
	public UpdateRolloverAccountPurseCommand( //
			TransactionInfo transactionInfo, //
			String actionType, //
			String purseName, //
			String accountPurseId, //
			String transactionAmount, //
			String effectiveDate, //
			String expiryDate, //
			String currency, //
			String skuCode, BigDecimal percentageAmount//
	) {

		this.transactionInfo = transactionInfo;
		this.purseName = purseName;
		this.currency = currency;
		this.skuCode = skuCode;

		this.accountPurseId = accountPurseId == null ? null : CommandValidator.mapLong("accountPurseId", accountPurseId, 15);
		this.effectiveDate = effectiveDate == null ? null : CommandValidator.mapZonedDateTime("effectiveDate", effectiveDate);
		this.expiryDate = expiryDate == null ? null : CommandValidator.mapZonedDateTime("expiryDate", expiryDate);
		this.actionType = CommandValidator.mapPurseUpdateActionTypeByName(actionType);
		this.transactionAmount = CommandValidator.mapBigDecimal("transactionAmount", transactionAmount,
				"[0-9]{1,012}|([0-9]{1,012}\\.[0-9]{1,3})");

		this.percentageAmount = percentageAmount;
		CommandValidator.validate(this);
		CommandValidator.validateRequiredValue("spNumberType", transactionInfo.getSpNumberType());

		this.validateExpiryDate();

	}

	public final void validateExpiryDate() {
		if (isNotNull(this.expiryDate) && DateTimeUtil.convert(expiryDate)
			.isBefore(LocalDateTime.now())) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, "expiryDate is invalid:" + this.expiryDate);
		}

	}
}
