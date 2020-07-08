/*package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;

*//**
 * 
 * @author ulagana
 *
 *//*
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cardmetadata.sql")
public class CommonValidationsServiceTest {

	@Autowired
	CommonValidationsService commonValidationsService;

	
	 * Verifying the card status by providing Proxy Number as SPNumber
	 
	@Test
	public void checkCardStatusUsing_ProxyNumber() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER, "100002599");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "002");
		try {
			commonValidationsService.cardStatusCheckValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION, e.getMessage());
		}
	}

	
	 * Verifying the card status by providing Card Number Hash as SPNumber
	 
	@Test
	public void checkCardStatusUsing_CardNumberHash() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER, "46ugZPGQMvGCjJQ8DlwAuFr/gfd/dNpVtbVV7A39HBY=");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "002");
		try {
			commonValidationsService.cardStatusCheckValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION, e.getMessage());
		}
	}

	
	 * Verifying the card status by Serial Number as SPNumber
	 
	@Test
	public void checkCardStatusUsing_with_SerialNumber() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER, "123456789");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "002");
		try {
			commonValidationsService.cardStatusCheckValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION, e.getMessage());
		}
	}

	
	 * Verifying the card status by providing accountId as SPNumber
	 
	@Test
	public void checkCardStatusUsing_with_AccountId() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER, "100002555");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "002");
		try {
			commonValidationsService.cardStatusCheckValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION, e.getMessage());
		}
	}

	
	 * Validate the Card Status with non existing SPNumber 
	 
	@Test
	public void checkCardStatusUsing_with_nonExistingSPNumber() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER, "123123123");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "002");
		try {
			commonValidationsService.cardStatusCheckValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION, e.getMessage());
		}
	}

	
	 * SPIL CVV2 Validation Providing all the values and getting success response
	 * for CVV supported
	 
	@Test
	public void checkCvv2Validation() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PAN, "55555655264");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "112");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "1018");
		valueObj.put(ValueObjectKeys.CVV2, "738");
		try {
			commonValidationsService.spillCvv2Validation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CVV_VERIFY_FAIL, e.getMessage());
		}
	}

	
	 * Providing invalid ProductId It will throw Exception
	 
	@Test
	public void checkCvv2Validation_with_invalid_ProductId() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PAN, "55555655264");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "999");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "1018");
		valueObj.put(ValueObjectKeys.CVV2, "738");
		try {
			commonValidationsService.spillCvv2Validation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CVK_NOT_SUPPORTED, e.getMessage());
		}
	}

	
	 * Proving null Expiry Date to check the Exception
	 
	@Test
	public void checkCvv2Validation_with_nullExpiryDate() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PAN, "55555655264");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "112");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, null);
		valueObj.put(ValueObjectKeys.CVV2, "738");
		try {
			commonValidationsService.spillCvv2Validation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.INVALID_INPUT_DATA, e.getMessage());
		}
	}

	
	 * Proving empty PAN Number to check the Exception Type
	 
	@Test
	public void checkCvv2Validation_with_EmptyPANnumber() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PAN, "");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "112");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "1018");
		valueObj.put(ValueObjectKeys.CVV2, "738");
		try {
			commonValidationsService.spillCvv2Validation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.INVALID_INPUT_DATA, e.getMessage());
		}
	}

	
	 * Proving empty product Id to check the Exception Type
	 
	@Test
	public void checkCvv2Validation_with_EmptyProductId() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PAN, "");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "1018");
		valueObj.put(ValueObjectKeys.CVV2, "738");
		try {
			commonValidationsService.spillCvv2Validation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.INVALID_INPUT_DATA, e.getMessage());
		}
	}

	
	 * Validating the CVV2 by providing Wrong CVV as input
	 
	@Test
	public void checkCvv2Validation_with_WrongCVV2() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PAN, "55555655264");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "112");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "1018");
		valueObj.put(ValueObjectKeys.CVV2, "123");
		try {
			commonValidationsService.spillCvv2Validation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CVV_VERIFY_FAIL, e.getMessage());
		}
	}

	
	 * Check the Status of the card by Providing card Hash number as input
	 
	@Test
	public void checkCardStatusUsing_cardNumberHash() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CARD_NUM_HASH, "123456789");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "002");
		try {
			commonValidationsService.cardStatusCheckValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION, e.getMessage());
		}
	}

	
	 * Validating the Card Expiry Date by providing correct Expirydate as input value
	 
	@Test
	public void validateExpiryDateCheckWithAllValues() throws ServiceException {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CARD_NUM_HASH, "46ugZPGQMvGCjJQ8DlwAuFr/gfd/dNpVtbVV7A39HBY=");
		valueObj.put(ValueObjectKeys.CARD_ACCOUNT_ID, "100002624");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "1018");

		try {
			commonValidationsService.expiryDateCheck(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.INVALID_EXPDATE, e.getMessage());
		}
	}

	
	 * Validating the Card Expiry Date by providing null as input value
	 
	@Test
	public void validateExpiryDateCheckwithNullValues() throws ServiceException {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.HASH_CARDNO, null);
		valueObj.put(ValueObjectKeys.CARD_ACCOUNT_ID, null);
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "0520");

		try {
			commonValidationsService.expiryDateCheck(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.INVALID_EXPDATE, e.getMessage());
		}
	}

	
	 * JUNIT Test cases started for passiveStatusValidation
	 * 
	 * getting success response by providing all valid data
	 
	@Test
	public void passiveStatusValidation_withAllInputValues() {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CARD_NUM_HASH, "46ugZPGQMvGCjJQ8DlwAuFr/gfd/dNpVtbVV7A39HBY=");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "0520");
		try {
			commonValidationsService.passiveStatusValidation(valueObj);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	
	 * getting Exception if the provided input is not proper
	 
	@Test
	public void passiveStatusValidation_withEmptyHashNumber() {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CARD_NUM_HASH, "");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "0520");
		try {
			commonValidationsService.passiveStatusValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.INVALID_INPUT_DATA, e.getMessage());
		}
	}

	
	 * This Test will throw Exception if the DeliveryChannel is not supported the
	 * Passive Status
	 
	@Test
	public void passiveStatusValidation_withnonExistingPassiveStatus() {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CARD_NUM_HASH, "");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "0520");
		try {
			commonValidationsService.passiveStatusValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.INVALID_INPUT_DATA, e.getMessage());
		}
	}

	
	 * Providing the correct Currency Code to validate the Currency code
	 
	@Test
	public void currencyCodeValidationforExistingValue() {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CURRENCY_CODE, "USD");
		try {
			commonValidationsService.currencyCodeValidation(valueObj);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	
	 * NonExisting currency code is passing as input to validathe the currency code
	 
	@Test
	public void invalidCurrencyCodeValidation() {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CURRENCY_CODE, "ASD");
		try {
			commonValidationsService.currencyCodeValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CURRENCY_CODE_EXEC, e.getMessage());
		}
	}

	
	 * Passing NUll as the input currency code, It will throw Exception
	 
	@Test
	public void nullCurrencyCodeValidation() {

		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.CURRENCY_CODE, null);
		try {
			commonValidationsService.currencyCodeValidation(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CURRENCY_CODE_EXEC, e.getMessage());
		}
	}

	
	 * Getting the card Details by providing proxy number
	 
	@Test
	public void getCardDetails_byProxyNumber() {
		
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER,"100002599");
		try {
			assertNotNull(commonValidationsService.getCardDetails(valueObj));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	
	 * Getting the card Details by providing Account ID
	 
	@Test
	public void getCardDetails_byAccountId() {
		
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER,"100002624");
		try {
			assertNotNull(commonValidationsService.getCardDetails(valueObj));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	
	 * Getting the card Details by providing Empty SPNumber
	 * It will throw Exception
	 
	@Test
	public void getCardDetails_withEmptySPNumber() {
		
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.SPNUMBER,"");
		try {
			commonValidationsService.getCardDetails(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION,e.getMessage());
		}
	}
	
	
	
	 * Getting the Product Attributes if the Product ID is exists
	 
	@Test
	public void getProductDetailsUsingProductId() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "112");
		try {
			assertNotNull(commonValidationsService.getProductDetails(valueObj));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	
	 * Getting the Product Attributes if the Product ID is exists
	 
	@Test
	public void getProductDetailsUsingProductId_AsNULLorEmpty() {
		Map<String, String> valueObj = new HashMap<>();
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "");
		try {
			commonValidationsService.getProductDetails(valueObj);
		} catch (ServiceException e) {
			assertEquals(SpilExceptionMessages.CARDSTAT_EXECEPTION, e.getMessage());
		}
	}
}
*/