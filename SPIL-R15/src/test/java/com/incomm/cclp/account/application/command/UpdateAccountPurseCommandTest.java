package com.incomm.cclp.account.application.command;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;

@SuppressWarnings("unused")
@RunWith(MockitoJUnitRunner.class)
public class UpdateAccountPurseCommandTest {

	String correlationId = "6058298765123456";
	String spNumber = "6058298765123456";
	String mdmId = "123";
	String deliveryChannel = "SPIL";
	String userName = "user123";;
	String terminalId;
	String storeId;
	String upc;

	String purseName = "testpurse";
	String accountPurseId;

	String effectiveDate = "2007-12-03T10:15:30+01:00[Europe/Paris]";
	String expiryDate = "2024-12-03T10:15:30+01:00[Europe/Paris]";

	String transactionAmount = "13.47";
	String currency = "usd";
	String skuCode;

	String actionType = "load";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	/////////////////////
	// PurseName
	/////////////////////
	@Test
	public void validate_PurseNameEmpty_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("purseName is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().purseName("")
			.build();

	}

	@Test
	public void validate_PurseNameNull_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("purseName is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().purseName(null)
			.build();

	}

	@Test
	public void validate_PurseNameLengthGreaterThan15_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("purseName is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().purseName("9876543212345678")
			.build();

	}

	@Test
	public void validate_PurseNameHaingOtherThanNumeralsAndLetters_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("purseName is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().purseName("987#$3212345678987651")
			.build();

	}

	@Test
	public void validate_PurseNameValid_NoValidationExceptioThrown() {
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().purseName("9876ABCD123456")
			.build();

	}

	/////////////////////
	// AccountPurseId
	/////////////////////
	@Test
	public void validate_AccountPurseIdEmpty_NoValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("accountPurseId is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().accountPurseId("")
			.build();
	}

	@Test
	public void validate_AccountPurseIdNull_NoValidationExceptionThrown() {
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().accountPurseId(null)
			.build();

	}

	@Test
	public void validate_AccountPurseIdLengthGreaterThan15_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("accountPurseId is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues()
			.accountPurseId("98765432123456789876510987654321234567890")
			.build();

	}

	@Test
	public void validate_AccountPurseIdrHaingOtherThanNumerals_ValidationExceptioThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("accountPurseId is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().accountPurseId("987AB3212345678987651")
			.build();

	}

	@Test
	public void validate_AccountPurseIdValid_NoValidationExceptioThrown() {
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().accountPurseId("98765")
			.build();

	}

	// Action

	@Test
	public void validate_ActionNull_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("actionType is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().actionType(null)
			.build();

	}

	@Test
	public void validate_transactionAmount_ValidationExceptionThrown() {
		expectedEx.expect(DomainException.class);
		expectedEx.expectMessage("transactionAmount is invalid");
		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().transactionAmount("123.3456")
			.build();

	}

	@Test
	public void validate_transactionAmount_succeeds() {

		UpdateAccountPurseCommand updateAccountPurseCommand = getBuilderWithDefaultValues().transactionAmount("123.123")
			.build();

		updateAccountPurseCommand = getBuilderWithDefaultValues().transactionAmount("123")
			.build();

	}

	private UpdateAccountPurseCommand.UpdateAccountPurseCommandBuilder getBuilderWithDefaultValues() {
		return UpdateAccountPurseCommand.builder()
			.transactionInfo(TransactionInfoTest.getDefaultTransactionInfo()
				.build())
			.purseName(purseName)
			.accountPurseId(accountPurseId)
			.effectiveDate(effectiveDate)
			.expiryDate(expiryDate)
			.transactionAmount(transactionAmount)
			.currency(currency)
			.skuCode(skuCode)
			.accountPurseId(accountPurseId)
			.actionType(actionType);
	}

}
