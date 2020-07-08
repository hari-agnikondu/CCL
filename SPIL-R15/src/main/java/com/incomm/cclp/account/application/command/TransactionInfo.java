package com.incomm.cclp.account.application.command;

import static com.incomm.cclp.account.util.CodeUtil.isNotNull;
import static com.incomm.cclp.account.util.CodeUtil.not;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.DeliveryChannelType;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.account.domain.model.TransactionType;
import com.incomm.cclp.account.util.CodeUtil;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
public class TransactionInfo {

	@NotNull
	@Pattern(regexp = "[0-9a-zA-Z-_ \\s]{1,40}")
	private final String correlationId;

	@NotNull
	private final DeliveryChannelType deliveryChannelType;

	@NotNull
	private final MessageType messageType;

	private final Optional<SpNumberType> spNumberType;

	private final TransactionType transactionType;

	@NotNull
	@Pattern(regexp = "[a-zA-Z0-9]{1,21}")
	private final String spNumber;

	@Pattern(regexp = "[0-9]{1,20}")
	private final String mdmId;

	@Pattern(regexp = "[a-zA-Z0-9$]{1,120}")
	private final String upc;

	@Pattern(regexp = "[0-9a-zA-Z \\s]{1,15}")
	private final String terminalId;

	@Pattern(regexp = "[a-zA-Z0-9#\\s-]{1,64}")
	private final String storeId;

	private final String userName;

	@ToString.Exclude
	private final String password;

	@NotNull
	private final LocalDateTime transactionDateTime;

	@Builder
	public TransactionInfo(String correlationId, //
			String deliveryChannelCode, //
			String deliveryChannelType, //
			TransactionType transactionType, //
			MessageType messageType, //
			SpNumberType spNumberType, //
			String spNumberTypeString, //
			String spNumber, //
			String mdmId, //
			String upc, //
			String userName, //
			String password, //
			String terminalId, //
			String storeId) {
		super();
		this.correlationId = correlationId;
		this.userName = userName;
		this.password = password;
		this.spNumber = spNumber;
		this.terminalId = terminalId;
		this.storeId = storeId;

		this.transactionType = transactionType;

		this.deliveryChannelType = isNotNull(deliveryChannelType) ? CommandValidator.mapDeliveryChannelTypeByName(deliveryChannelType)
				: CommandValidator.mapDeliveryChannelTypeByCode(deliveryChannelCode);
		this.messageType = messageType;

		this.transactionDateTime = LocalDateTime.now();

		if (isNotNull(spNumberType)) {
			this.spNumberType = Optional.of(spNumberType);
		} else if (isNotNull(spNumberTypeString)) {
			this.spNumberType = Optional.of(CommandValidator.mapSpNumberType(spNumberTypeString));
		} else {
			this.spNumberType = Optional.empty();
		}

		this.mdmId = mdmId == null ? null : mdmId;
		this.upc = upc == null ? null : upc;

		CommandValidator.validate(this);
		this.validateSpNumber();
	}

	public boolean hasMdmId() {
		return CodeUtil.isNotNull(this.mdmId);
	}

	public boolean hasUpc() {
		return CodeUtil.isNotNull(this.upc);
	}

	public void validateSpNumber() {
		if (this.spNumberType.isPresent() && this.spNumberType.get() == SpNumberType.CUSTOMER_ID) {
			if (not(this.spNumber.matches("\\d+"))) {
				throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, "spNumber is invalid:" + this.spNumber);
			}
		}
	}

}
