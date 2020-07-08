package com.incomm.cclp.account.application.command;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.SpNumberType;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class TransactionInfoTest {

	static String correlationId = "6058298765123456";

	static SpNumberType spNumberType = SpNumberType.CUSTOMER_ID;
	static String spNumber = "6058298765123456";

	static String mdmId = "123";

	static String deliveryChannelType = "SPIL";
	static String userName = "user123";;

	static String storeId;
	static String terminalId;
	static String upc = "UPC";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	/////////////////////
	// SPNumber
	/////////////////////
	@Test
	public void validate_SPNumberEmpty_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("spNumber is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().spNumber("")
			.build();
	}

	@Test
	public void validate_SPNumberNull_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("spNumber is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().spNumber(null)
			.build();

	}

	@Test
	public void validate_SPNumberLengthGreaterThan21_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("spNumber is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().spNumber("9876543212345678987651")
			.build();

	}

	@Test
	public void validate_SPNumberHaingOtherThanNumeralsAndLetters_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("spNumber is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().spNumber("987#$3212345678987651")
			.build();

	}

	@Test
	public void validate_SPNumberNotNumeric_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("spNumber is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().spNumber("9876ABCD1234567898765")
			.build();
	}

	@Test
	public void validate_SPNumberValid_NoValidationExceptioThrown() {
		TransactionInfo transactionInfo = getDefaultTransactionInfo().spNumber("9876ABCD1234567898765")
			.spNumberType(SpNumberType.CARD_NUMBER)
			.build();

		transactionInfo = getDefaultTransactionInfo().spNumber("234293420384")
			.build();

	}

	/////////////////////
	// CorrelationId
	/////////////////////

	@Test
	public void validate_CorrelationIdEmpty_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("correlationId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().correlationId("")
			.build();

	}

	@Test
	public void validate_CorrelationIdNull_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("correlationId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().correlationId(null)
			.build();

	}

	@Test
	public void validate_CorrelationIdLengthGreaterThan40_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("correlationId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().correlationId("98765432123456789876510987654321234567890")
			.build();

	}

	@Test
	public void validate_CorrelationIdHavingOtherThanNumeralsAndLetters_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("correlationId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().correlationId("987#$3212345678987651")
			.build();

	}

	@Test
	public void validate_CorrelationIdValid_NoValidationExceptioThrown() {

		TransactionInfo transactionInfo = getDefaultTransactionInfo().correlationId("9")
			.build();

		transactionInfo = getDefaultTransactionInfo().correlationId("9876ABCD1234567898765")
			.build();

		transactionInfo = getDefaultTransactionInfo().correlationId("41-A00032000000002174")
			.build();

	}

	/////////////////////
	// MdmId
	/////////////////////
	@Test
	public void validate_MdmIdEmpty_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("mdmId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().mdmId("")
			.build();
	}

	@Test
	public void validate_MdmIdLengthGreaterThan20_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("mdmId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().mdmId("98765432123456789876510987654321234567890")
			.build();

	}

	@Test
	public void validate_MdmIdrHaingOtherThanNumerals_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("mdmId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().mdmId("987AB3212345678987651")
			.build();

	}

	@Test
	public void validate_MdmIdValid_NoValidationExceptioThrown() {
		TransactionInfo transactionInfo = getDefaultTransactionInfo().mdmId("98765")
			.build();

	}

	// StoreId

	@Test
	public void validate_StoreIdEmpty_NoValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("storeId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().storeId("")
			.build();
	}

	@Test
	public void validate_StoreIdNull_NoValidationExceptionThrown() {
		TransactionInfo transactionInfo = getDefaultTransactionInfo().storeId(null)
			.build();

	}

	@Test
	public void validate_StoreIdLengthGreaterThan64_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("storeId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().storeId(
				"98765432123456789876510987654321234567890987654321234567898765109876543212345678909876543212345678987651098765432123456789098765432123456789876510987654321234567890")
			.build();

	}

	@Test
	public void validate_StoreIdrHaingOtherThanNumerals_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("storeId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().storeId("987AB3212345678987651987AB3212345678987651987AB3212345678987651987AB3212345678987651987AB3212345678987651")
			.build();

	}


	// TerminalId

	@Test
	public void validate_TerminalIdEmpty_NoValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("terminalId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().terminalId("")
			.build();
	}

	@Test
	public void validate_TerminalIdNull_NoValidationExceptionThrown() {
		TransactionInfo transactionInfo = getDefaultTransactionInfo().terminalId(null)
			.build();

	}

	@Test
	public void validate_TerminalIdLengthGreaterThan20_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("terminalId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().terminalId("98765432123456789876510987654321234567890")
			.build();

	}

	@Test
	public void validate_TerminalIdrHaingOtherThanNumerals_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("terminalId is invalid");
		TransactionInfo transactionInfo = getDefaultTransactionInfo().terminalId("987AB3212345678987651")
			.build();

	}

	public static TransactionInfo.TransactionInfoBuilder getDefaultTransactionInfo() {
		return TransactionInfo.builder()
			.correlationId(correlationId)
			.deliveryChannelType(deliveryChannelType)
			.messageType(MessageType.ORIGINAL)
			.spNumberType(spNumberType)
			.spNumber(spNumber)
			.mdmId(mdmId)
			.storeId(storeId)
			.terminalId(terminalId)
			.upc(upc)
			.userName(userName);
	}

}
