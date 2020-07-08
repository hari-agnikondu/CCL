package com.incomm.cclp.transaction.mapper;

import java.math.BigDecimal;
import java.util.Optional;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.ValueDTO;

public class CommandFactory {

	public AccountTransactionCommand map(ValueDTO valueDTO) {
		String msgType = valueDTO.getValueObj()
			.get(ValueObjectKeys.MSG_TYPE);
		MessageType messageType = null;
		Optional<SpNumberType> spNumberType = SpNumberType.byName(valueDTO.getValueObj()
			.get(ValueObjectKeys.SP_NUM_TYPE));

		switch (msgType) {
		case MsgTypeConstants.MSG_TYPE_REVERSAL:
			messageType = MessageType.REVERSE;
			break;
		default:
			messageType = MessageType.ORIGINAL;
		}

		TransactionInfo transaction = TransactionInfo.builder()
			.correlationId(valueDTO.getValueObj()
				.get(ValueObjectKeys.INCOM_REF_NUMBER))
			.deliveryChannelType(valueDTO.getValueObj()
				.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE))
			.mdmId(valueDTO.getValueObj()
				.get(ValueObjectKeys.INCOM_REF_NUMBER))
			.messageType(messageType)
			.spNumber(valueDTO.getValueObj()
				.get(ValueObjectKeys.SPNUMBER))
			.spNumberType(spNumberType.get())
			.storeId(valueDTO.getValueObj()
				.get(ValueObjectKeys.STORE_ID))
			.terminalId(valueDTO.getValueObj()
				.get(ValueObjectKeys.TERMINALID))
			.upc(valueDTO.getValueObj()
				.get(ValueObjectKeys.SPIL_UPC_CODE))
			.userName(valueDTO.getValueObj()
				.get(ValueObjectKeys.SPIL_USER_ID))
			.password(valueDTO.getValueObj()
				.get(ValueObjectKeys.SPIL_PASSWORD))
			.build();

		return AccountTransactionCommand.builder()
			.transactionInfo(transaction)
			.transactionAmount(new BigDecimal(valueDTO.getValueObj()
				.get(ValueObjectKeys.SPIL_TRAN_AMT)))
			.purseName(Optional.of(valueDTO.getValueObj()
				.get(ValueObjectKeys.PURSE_NAME)))
			.valueDTO(valueDTO)
			.build();
	}

}
