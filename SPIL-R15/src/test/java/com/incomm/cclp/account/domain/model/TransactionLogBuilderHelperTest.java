package com.incomm.cclp.account.domain.model;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.account.application.command.AccountTransactionCommand;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.factory.LogServiceFactory;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.dto.ValueDTO;

//@RunWith(MockitoJUnitRunner.class)
public class TransactionLogBuilderHelperTest {

	@Autowired
	@InjectMocks
	LogServiceFactory logServiceFactory;

	@Mock
	SequenceService sequenceService;

	// @Test
	public void testTransactionLogCreation() {
		AccountTransactionCommand command = createAccountCommand();
		CardEntity cardEntity = createCardEntity();
		when(sequenceService.getNextTxnSeqId()).thenReturn(new BigDecimal(123456));

		TransactionLog log = logServiceFactory.getTransactionLog(command, cardEntity, createAccountAggregate(), "00:00:00");
		System.out.println("Card number : " + log.getCardNumber());
		System.out.println("CorrelationId : " + log.getCorrelationId());
		System.out.println("PurseName :" + log.getPurseName());
		System.out.println("PurseName :" + log.getPurseName());
	}

	private AccountAggregateUpdate createAccountAggregate() {

		return AccountAggregateUpdate.builder()
			.authorizedAmount(new BigDecimal(100))
			.transactionFee(new BigDecimal(1))
			.build();
	}

	private CardEntity createCardEntity() {
		return CardEntity.builder()
			.cardNumberHash("KPootnxEAy3zd3TTJIdGxud9wLI6SA0Z0c2L4C108Es")
			.cardStatus(CardStatusType.ACTIVE)
			.expiryDate("12/05/2019")
			.build();
	}

	private AccountTransactionCommand createAccountCommand() {
		return AccountTransactionCommand.builder()
			.purseName(Optional.of("CURRENCY-PURSE"))
			.transactionAmount(new BigDecimal(100))
			.transactionCurrency(Optional.of("USD"))
			.cardExpiryDate(Optional.of("12/05/2019"))
			.transactionInfo(createTransactionInfo())

			.valueDTO(createValueDTO())
			.build();
	}

	private ValueDTO createValueDTO() {
		ValueDTO valueDTO = new ValueDTO();
		Map<String, String> valueObj = new HashMap<String, String>();
		valueObj.put(ValueObjectKeys.MSGTYPE, "0200");
		valueObj.put(ValueObjectKeys.DELIVERYCHNL, "SPIL");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "001");
		valueObj.put(ValueObjectKeys.MEMBERNO, "000");
		valueObj.put(ValueObjectKeys.PARTY_SUPPORTED, "Prty01");
		// valueObj.put(ValueObjectKeys.AUTH_JAVA_CLASS_NAME, msgTypeBean.getAuthJavaClass());
		valueObj.put(ValueObjectKeys.IS_FINANCIAL, "Y");
		valueObj.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, "C");
		valueObj.put(ValueObjectKeys.SPIL_MSG_TYPE, "0200");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, "SPIL");
		valueObj.put(ValueObjectKeys.PASSIVE_SUPPORT_FLAG, "SPRTFLAG");
		valueObj.put(ValueObjectKeys.TRANSACTIONDESC, "activation");
		valueObj.put(ValueObjectKeys.TRANSACTIONSHORTNAME, "act");

		valueDTO.setValueObj(valueObj);
		return valueDTO;
	}

	private TransactionInfo createTransactionInfo() {
		return TransactionInfo.builder()
			.correlationId("RRN123456")
			.deliveryChannelCode("SPIL")
			.deliveryChannelType("SPIL")
			.mdmId("1234")
			.messageType(MessageType.ORIGINAL)
			.password("password")
			.spNumber("ACCOUNT1234")
			.spNumberType(SpNumberType.ACCOUNT_NUMBER)
			.transactionType(TransactionType.ACTIVATION_WITH_PREAUTHORIZATION)
			.storeId("Stoer100")
			.terminalId("TRM100")
			.build();
	}
}
