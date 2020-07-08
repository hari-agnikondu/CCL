package com.incomm.cclp.account.application.service;

import static com.incomm.cclp.account.util.CodeUtil.isNotNullAndEmpty;
import static com.incomm.cclp.account.util.CodeUtil.mapToBigDecimal;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.util.StringUtils;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.application.command.SpNumber;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.account.domain.model.TransactionType;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.AccountPurseDTO;
import com.incomm.cclp.dto.ValueDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandMapper {

	public static AccountTransactionCommand map(ValueDTO valueDto) {
		Map<String, String> valueObject = valueDto.getValueObj();

		AccountPurseDTO accountPurseDto = valueDto.getAccountPurseDto();

		String defaultPurse = (String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT)
			.get(ValueObjectKeys.DEFAULT_PURSE);

		Long accountPurseId = null;

		String skuCode = null;

		if (accountPurseDto != null) {
			accountPurseId = accountPurseDto.getAccountPurseId();
			skuCode = accountPurseDto.getSkuCode();
		}

		Optional<SpNumber> targetSpNumber = mapTargetSpNumber(valueObject);

		return AccountTransactionCommand.builder()
			.transactionInfo(mapTransactionInfo(valueObject))
			.transactionAmount(mapToBigDecimal(valueObject.get(ValueObjectKeys.SPIL_TRAN_AMT)))
			.purseName(Optional.ofNullable(valueObject.get(ValueObjectKeys.PURSE_NAME)))
			.accountPurseId(mapAccountPurseId(accountPurseId))
			.purseCurrency(Optional.ofNullable(valueObject.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY)))
			.transactionCurrency(Optional.ofNullable(valueObject.get(ValueObjectKeys.TXN_CURRENCY_CODE)))
			.skuCode(mapSkuCode(skuCode))
			.defaultPurseId(Long.parseLong(defaultPurse))
			.targetSpNumber(targetSpNumber)
			.valueDTO(valueDto)
			.build();
	}

	private static TransactionInfo mapTransactionInfo(Map<String, String> valueObject) {

		return TransactionInfo.builder()
			.correlationId(valueObject.get(ValueObjectKeys.INCOM_REF_NUMBER))
			.deliveryChannelCode(valueObject.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE))
			.messageType(mapMessageType(valueObject.get(ValueObjectKeys.MSG_TYPE)))
			.transactionType(mapTransactionType(valueObject.get(ValueObjectKeys.SPIL_MSG_TYPE),
					mapMessageType(valueObject.get(ValueObjectKeys.MSG_TYPE))))
			.mdmId(valueObject.get(ValueObjectKeys.MDM_ID))
			.spNumber(valueObject.get(ValueObjectKeys.SPNUMBER))
			.storeId(valueObject.get(ValueObjectKeys.STORE_ID))
			.terminalId(valueObject.get(ValueObjectKeys.TERMINALID))
			.upc(valueObject.get(ValueObjectKeys.SPIL_UPC_CODE))
			.userName(valueObject.get(ValueObjectKeys.SPIL_USER_ID))
			.password(valueObject.get(ValueObjectKeys.SPIL_PASSWORD))
			.build();

	}

	private static Optional<SpNumber> mapTargetSpNumber(Map<String, String> valueObject) {

		String targetCardNumber = valueObject.get(ValueObjectKeys.SPIL_TARGET_CARDNUM);

		if (isNotNullAndEmpty(targetCardNumber)) {
			return Optional.of(SpNumber.from(Optional.of(SpNumberType.CARD_NUMBER), targetCardNumber));
		}

		return Optional.empty();
	}

	private static TransactionType mapTransactionType(String transactionShortName, MessageType messageType) {

		return TransactionType.byTransactionShortNameAndMessageType(transactionShortName, messageType)
			.orElseThrow(() -> new ServiceException("Unable to map transactionShortName to TransactionType."));
	}

	private static Optional<Long> mapAccountPurseId(Long accountPurseId) {
		if (!Objects.isNull(accountPurseId) && accountPurseId > 0) {
			return Optional.of(accountPurseId);
		} else {
			return Optional.empty();
		}
	}

	private static Optional<String> mapSkuCode(String skuCode) {
		if (!StringUtils.isEmpty(skuCode)) {
			return Optional.of(skuCode);
		} else {
			return Optional.empty();
		}
	}

	private static MessageType mapMessageType(String messageTypeCode) {
		return MessageType.byMessageTypeCode(messageTypeCode)
			.orElseThrow(() -> new ServiceException("Unable to map messageTypeCode to MessageType."));
	}
}
