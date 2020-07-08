package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.impl.SpilDAOImpl;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.impl.CommonValidationsServiceImpl;
import com.incomm.cclp.service.impl.ProductServiceImpl;
import com.incomm.cclp.transaction.bean.SupportedPurse;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.transaction.service.impl.TransactionServiceImpl;
import com.incomm.cclp.util.HSMCommandBuilder;
import com.incomm.cclp.util.Util;

@RunWith(MockitoJUnitRunner.class)
public class CommonValidationServiceImplTest {

	Map<String, Map<String, Object>> productAttributes;
	Map<String, Object> prodAttributes = new HashMap<String, Object>();
	Map<String, Object> generalAttributes = new HashMap<String, Object>();
	Map<String, Object> cardStatusAttributes = new HashMap<String, Object>();
	Map<String, Object> cvvAttributes = new HashMap<String, Object>();
	Map<String, Object> partyTypeAttributes = new HashMap<String, Object>();
	Map<String, Object> purseAttributes = new HashMap<String, Object>();

	ValueDTO valueDto = new ValueDTO();
	Map<String, String> valueObj = new HashMap<String, String>();
	@Autowired
	@InjectMocks
	CommonValidationsServiceImpl commonValidationServiceImplInstance;

	@Mock
	LocalCacheServiceImpl localCacheService;

	@Mock
	TransactionServiceImpl transactionService;

	@Mock
	SpilDAOImpl spilDao;

	@Mock
	HSMCommandBuilder hsmCommandBuilder;

	@Mock
	ProductServiceImpl productService;

	@Mock
	SupportedPurse suppPurseBean;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		productAttributes = new HashMap<String, Map<String, Object>>();
		productAttributes.put("Product", prodAttributes);
		productAttributes.put("General", generalAttributes);
		productAttributes.put("Card Status", cardStatusAttributes);
		productAttributes.put("CVV", cvvAttributes);
		productAttributes.put("PARTY_TYPE", partyTypeAttributes);
		productAttributes.put("Purse", purseAttributes);

		valueDto.setValueObj(valueObj);
		valueDto.setProductAttributes(productAttributes);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getProductAttributeValueTestSuccess() {

		prodAttributes.put("cvvSupported", "Enabled");

		String cvvSupportedExpected = "Enabled";
		Object cvvSupportedActual = Util.getProductAttributeValue(productAttributes, "Product", "cvvSupported");

		assertEquals(cvvSupportedExpected, cvvSupportedActual);

		String txnCardStatusExpected = null;
		Object txnCardStatusActual = Util.getProductAttributeValue(productAttributes, "Card Status", "");

		assertEquals(txnCardStatusExpected, txnCardStatusActual);
	}

	@Test
	public void cardStatusCheckValidation_inactive_balanceInquiry() {

		productAttributes.get("Card Status")
			.put("spil_balinq_INACTIVE", "true");

		valueObj.put(ValueObjectKeys.CARD_CARDSTAT, "0");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, "spil");
		valueObj.put(ValueObjectKeys.ORIGINAL_MSGTYPE, "balinq");

		valueDto.setProductAttributes(productAttributes);

		Map<String, String> cardStatusMap = new HashMap<String, String>();
		cardStatusMap.put("0", "INACTIVE");
		when(localCacheService.getAllCardStatus(Mockito.anyMap())).thenReturn(cardStatusMap);
		when(transactionService.getAllCardStatus()).thenReturn(cardStatusMap);
		commonValidationServiceImplInstance.cardStatusCheckValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void cardStatusCheckValidation_inactive_valins_consumed() {
		prodAttributes.put(ValueObjectKeys.PRODUCT_TYPE_KEY, "Retail");
		generalAttributes.put("consumedFlag", "true");

		valueObj.put(ValueObjectKeys.CARD_CARDSTAT, "0");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, "spil");
		valueObj.put(ValueObjectKeys.ORIGINAL_MSGTYPE, "valins");

		valueObj.put(ValueObjectKeys.REPL_FLAG, "0");

		valueDto.setProductAttributes(productAttributes);

		Map<String, String> cardStatusMap = new HashMap<String, String>();
		cardStatusMap.put("0", "INACTIVE");
		when(localCacheService.getAllCardStatus(Mockito.anyMap())).thenReturn(cardStatusMap);
		when(transactionService.getAllCardStatus()).thenReturn(cardStatusMap);
		doNothing().when(spilDao)
			.updateCardStatus(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		commonValidationServiceImplInstance.cardStatusCheckValidation(valueDto);

		String cardStatusActual = valueObj.get(ValueObjectKeys.CARD_CARDSTAT);
		assertEquals("17", cardStatusActual);
	}

	@Test
	public void spillCvv2Validation_success() {

		valueObj.put(ValueObjectKeys.CARD_NUMBER, "1234567890123456");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "1");
		valueObj.put(ValueObjectKeys.CARD_EXPDATE, "1909");
		valueObj.put(ValueObjectKeys.CVV2, "123");

		prodAttributes.put("cvvSupported", "Enabled");
		cvvAttributes.put("cvkA", "ABCDEFABCDEF000");
		cvvAttributes.put("cvkB", "111ABCDEFABCDEF");

		commonValidationServiceImplInstance.spillCvv2Validation(valueDto);
	}

	@Test
	public void upcValidation_withValidB2B_UPCAsInput() {
		valueObj.put(ValueObjectKeys.SPIL_UPC_CODE, "123123123123");

		prodAttributes.put(ValueObjectKeys.B2B_UPC, "123123123123");
		productAttributes.put(ValueObjectKeys.RETAIL_UPC, null);

		commonValidationServiceImplInstance.upcValidation(valueDto);
	}

	@Test
	public void upcValidation_withValidRetail_UPCAsInput() {
		valueObj.put(ValueObjectKeys.SPIL_UPC_CODE, "123123123123");

		prodAttributes.put(ValueObjectKeys.B2B_UPC, null);
		prodAttributes.put(ValueObjectKeys.RETAIL_UPC, "123123123123");

		commonValidationServiceImplInstance.upcValidation(valueDto);
	}

	@Test(expected = ServiceException.class)
	public void upcValidation_withInvalidSpillRequest() {
		valueObj.put(ValueObjectKeys.SPIL_UPC_CODE, "123123123123");

		prodAttributes.put(ValueObjectKeys.B2B_UPC, null);
		prodAttributes.put(ValueObjectKeys.RETAIL_UPC, null);

		commonValidationServiceImplInstance.upcValidation(valueDto);
	}

	@Test(expected = ServiceException.class)
	public void upcValidation_withInvalidUPC() {
		valueObj.put(ValueObjectKeys.SPIL_UPC_CODE, "123123123123");

		prodAttributes.put(ValueObjectKeys.B2B_UPC, "123123123124");
		prodAttributes.put(ValueObjectKeys.RETAIL_UPC, null);

		commonValidationServiceImplInstance.upcValidation(valueDto);
	}

	@Test
	public void firstPartyThirdPartyCheck_success_withSameMdmID() {
		valueObj.put(ValueObjectKeys.PARTNER_ID, "123");
		valueObj.put(ValueObjectKeys.MDM_ID, "456");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "5");
		valueObj.put(ValueObjectKeys.PARTY_SUPPORTED, "FIRST PARTY");

		when(productService.getMdmId(Mockito.anyString())).thenReturn("456");

		commonValidationServiceImplInstance.firstPartyThirdPartyCheck(valueDto);
	}

	@Test
	public void firstPartyThirdPartyCheck_success_withPartySupportedAsFirstParty() {
		valueObj.put(ValueObjectKeys.PARTNER_ID, "123");
		valueObj.put(ValueObjectKeys.MDM_ID, "456");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "5");
		valueObj.put(ValueObjectKeys.PARTY_SUPPORTED, "FIRST PARTY");

		partyTypeAttributes.put("5_456", "FIRST PARTY");

		when(productService.getMdmId(Mockito.anyString())).thenReturn("457");

		commonValidationServiceImplInstance.firstPartyThirdPartyCheck(valueDto);
	}

	@Test
	public void firstPartyThirdPartyCheck_success_withPartySupportedAsBoth() {
		valueObj.put(ValueObjectKeys.PARTNER_ID, "123");
		valueObj.put(ValueObjectKeys.MDM_ID, "456");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "5");
		valueObj.put(ValueObjectKeys.PARTY_SUPPORTED, "BOTH");

		partyTypeAttributes.put("5_456", "FIRST PARTY");

		when(productService.getMdmId(Mockito.anyString())).thenReturn("457");

		commonValidationServiceImplInstance.firstPartyThirdPartyCheck(valueDto);
	}

	@Test(expected = ServiceException.class)
	public void firstPartyThirdPartyCheck_withDifferentPartyType() {
		valueObj.put(ValueObjectKeys.PARTNER_ID, "123");
		valueObj.put(ValueObjectKeys.MDM_ID, "456");
		valueObj.put(ValueObjectKeys.PRODUCT_ID, "5");
		valueObj.put(ValueObjectKeys.PARTY_SUPPORTED, "FIRST_PARTY");

		partyTypeAttributes.put("5_456", "THIRD_PARTY");

		when(productService.getMdmId(Mockito.anyString())).thenReturn("457");

		commonValidationServiceImplInstance.firstPartyThirdPartyCheck(valueDto);
	}

	@Test
	public void purseValidityCheck_success_withDefaultPurse() {
		valueObj.put(ValueObjectKeys.PURSE_ID, "1");

		prodAttributes.put(ValueObjectKeys.DEFAULT_PURSE, "1");

		commonValidationServiceImplInstance.purseValidityCheck(valueDto);
	}

	@Test
	public void purseValidityCheck_success_withExtensionPurse() {
		valueObj.put(ValueObjectKeys.PURSE_ID, "2");

		prodAttributes.put(ValueObjectKeys.DEFAULT_PURSE, "1");

		commonValidationServiceImplInstance.purseValidityCheck(valueDto);
	}

	@Test
	public void purseValidityCheck_success_withValidExtensionPurseDate() {
		valueObj.put(ValueObjectKeys.PURSE_ID, "2");

		prodAttributes.put(ValueObjectKeys.DEFAULT_PURSE, "1");
		purseAttributes.put("purseActiveDate", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMonths(1).format(DateTimeFormatter.ofPattern(GeneralConstants.MMDDYYYYHHMMSS)));
		purseAttributes.put("purseValidityPeriod", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusYears(1).format(DateTimeFormatter.ofPattern(GeneralConstants.MMDDYYYYHHMMSS)));

		commonValidationServiceImplInstance.purseValidityCheck(valueDto);
	}

	@Test(expected = ServiceException.class)
	public void purseValidityCheck_withExpiredExtensionPurseDate() {
		valueObj.put(ValueObjectKeys.PURSE_ID, "2");

		prodAttributes.put(ValueObjectKeys.DEFAULT_PURSE, "1");
		purseAttributes.put("purseActiveDate", "08/15/2018 17:47:00");
		purseAttributes.put("purseValidityPeriod", "08/30/2019 17:47:00");

		commonValidationServiceImplInstance.purseValidityCheck(valueDto);
	}

	@Test
	public void expiryDateCheck_success_withValidationTypeAsMandatory() {
		commonValidationServiceImplInstance.expiryDateCheck("9902", "9902", "M");
	}

	@Test
	public void expiryDateCheck_success_withValidationTypeAsOptional() {
		commonValidationServiceImplInstance.expiryDateCheck("9902", "9902", "O");
	}

	@Test
	public void expiryDateCheck_success_withValidationTypeAsOptional_NoInput() {
		commonValidationServiceImplInstance.expiryDateCheck("9902", null, "O");
	}

	@Test(expected = ServiceException.class)
	public void expiryDateCheck_withValidationTypeAsMandatory_NoInput() {
		commonValidationServiceImplInstance.expiryDateCheck("9902", null, "M");
	}

	@Test(expected = ServiceException.class)
	public void expiryDateCheck_withInvalidExpDate() {
		commonValidationServiceImplInstance.expiryDateCheck("9902", "9802", "M");
	}

	@Test
	public void transactionAllowedCheck_success_deactivation() {
		when(spilDao.getCountInStatementLog(Mockito.anyString())).thenReturn(0);
		when(spilDao.getCountInTransactiontLog(Mockito.anyString())).thenReturn(0);
		commonValidationServiceImplInstance.transactionAllowedCheck(SpilTranConstants.SPIL_DEACTIVATION, "7788778800000490");
	}

	@Test
	public void transactionAllowedCheck_success_other_transaction() {
		commonValidationServiceImplInstance.transactionAllowedCheck(SpilTranConstants.SPIL_ACTIVATION, "7788778800000490");
	}

	@Test(expected = ServiceException.class)
	public void transactionAllowedCheck_deactivation_withEntryExistInStatmentLog() {
		when(spilDao.getCountInStatementLog(Mockito.anyString())).thenReturn(1);
		commonValidationServiceImplInstance.transactionAllowedCheck(SpilTranConstants.SPIL_DEACTIVATION, "7788778800000490");
	}

	@Test(expected = ServiceException.class)
	public void transactionAllowedCheck_deactivation_withEntryExistInTranLog() {
		when(spilDao.getCountInStatementLog(Mockito.anyString())).thenReturn(0);
		when(spilDao.getCountInTransactiontLog(Mockito.anyString())).thenReturn(1);
		commonValidationServiceImplInstance.transactionAllowedCheck(SpilTranConstants.SPIL_DEACTIVATION, "7788778800000490");
	}

	@Test
	public void getSpNumberType_cardNumber() {
		valueObj.put(ValueObjectKeys.CARD_NUMBER, "1");
		String spNumberType_Actual = commonValidationServiceImplInstance.getSpNumberType(valueObj, "1");
		assertEquals(ValueObjectKeys.SP_NUM_TYPE_PAN, spNumberType_Actual);
	}

	@Test
	public void getSpNumberType_proxyNumber() {
		valueObj.put(ValueObjectKeys.PROXY_NUMBER, "1");
		String spNumberType_Actual = commonValidationServiceImplInstance.getSpNumberType(valueObj, "1");
		assertEquals(ValueObjectKeys.SP_NUM_TYPE_PROXY_NUMBER, spNumberType_Actual);
	}

	@Test
	public void getSpNumberType_serialNumber() {
		valueObj.put(ValueObjectKeys.CARD_SERIAL_NUMBER, "1");
		String spNumberType_Actual = commonValidationServiceImplInstance.getSpNumberType(valueObj, "1");
		assertEquals(ValueObjectKeys.SP_NUM_TYPE_SERIAL_NUMBER, spNumberType_Actual);
	}

	@Test
	public void getSpNumberType_accountNumber() {
		valueObj.put(ValueObjectKeys.ACCOUNT_NUMBER, "1");
		String spNumberType_Actual = commonValidationServiceImplInstance.getSpNumberType(valueObj, "1");
		assertEquals(ValueObjectKeys.SP_NUM_TYPE_ACCOUNT_NUMBER, spNumberType_Actual);
	}
	
	@Test
	public void getSpNumberType_customerId() {

		String spNumberType_Actual = commonValidationServiceImplInstance.getSpNumberType(valueObj, "1");
		assertEquals(ValueObjectKeys.SP_NUM_TYPE_CUSTOMER_ID, spNumberType_Actual);
	}

	@Test
	public void validateCurrencyAndAmount_Success_PointPurse() {

		commonValidationServiceImplInstance.validateCurrencyAndAmount("POINTS", "10", null, "2", false);
	}

	@Test
	public void validateCurrencyAndAmount_Success_SKUPurse() {

		commonValidationServiceImplInstance.validateCurrencyAndAmount("SKU", "10", "SKU00001", "3", false);
	}

	@Test
	public void instrumentTypeCheck_success_act_cardnumber() {

		valueObj.put(ValueObjectKeys.ORIGINAL_MSGTYPE, "act");
		prodAttributes.put(ValueObjectKeys.ACTIVATION_ID, ValueObjectKeys.SP_NUM_TYPE_PAN);

		valueObj.put(ValueObjectKeys.SPNUMBER, "1");
		valueObj.put(ValueObjectKeys.CARD_NUMBER, "1");

		commonValidationServiceImplInstance.instrumentTypeCheck(valueDto);
	}

	@Test
	public void instrumentTypeCheck_success_valins_cardnumber() {

		valueObj.put(ValueObjectKeys.ORIGINAL_MSGTYPE, "valins");
		prodAttributes.put(ValueObjectKeys.OTHER_TXN_ID, ValueObjectKeys.SP_NUM_TYPE_PAN);

		valueObj.put(ValueObjectKeys.SPNUMBER, "1");
		valueObj.put(ValueObjectKeys.CARD_NUMBER, "1");

		commonValidationServiceImplInstance.instrumentTypeCheck(valueDto);
	}

	@Test
	public void passiveStatusValidation_success() {
		valueObj.put(ValueObjectKeys.CARD_CARDSTAT, "8");
		valueObj.put(ValueObjectKeys.PASSIVE_SUPPORT_FLAG, "Y");
		valueObj.put(ValueObjectKeys.OLD_CARD_STATUS, "1");

		commonValidationServiceImplInstance.passiveStatusValidation(valueObj);
	}

	@Test
	public void currencyCodeValidation_with_TxnCurrencyAsNull() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, "purse");
		valueObj.put(ValueObjectKeys.SKIP_CURRENCY_CHECK_IF_NULL, "true");
		valueObj.put(ValueObjectKeys.SKIP_CURRENCY_CHECK_IF_NULL, "true");
		valueObj.put(ValueObjectKeys.MINORUNITS, "2");
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "4");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_TRAN_AMT, "7777.77");
		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void currencyCodeValidation_with_currencyCodeNotMatched() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, "purse");
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "4");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, "443");
		valueObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, "840");
		valueObj.put(ValueObjectKeys.MINORUNITS, "7");

		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void currencyCodeValidation_with_minorUnitsNotMatched() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, "purse");
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "4");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, null);
		valueObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, "840");
		valueObj.put(ValueObjectKeys.MINORUNITS, "1");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_TRAN_AMT, "7777.77");

		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void currencyCodeValidation_with_pointsId() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, "purse");
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "2");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, null);
		valueObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, "840");
		valueObj.put(ValueObjectKeys.MINORUNITS, "1");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_TRAN_AMT, "10");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_SKUCODE, "89756");
		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void currencyCodeValidation_with_skuId() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, "purse");
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "3");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, "55");
		valueObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, "840");
		valueObj.put(ValueObjectKeys.MINORUNITS, "1");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_TRAN_AMT, "10");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_SKUCODE, "89756");
		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void default_Condition_In_CurrencyValidation() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, "purse");
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "8");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, "55");
		valueObj.put(ValueObjectKeys.MINORUNITS, "1");
		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void currecycode_validationForDefaultPurse() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, null);
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "8");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, "55");
		valueObj.put(ValueObjectKeys.MINORUNITS, "1");
		valueObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, "99");

		SupportedPurse suppPurseBean = new SupportedPurse();
		suppPurseBean.setProductId("55");
		suppPurseBean.setPurseId("55");
		suppPurseBean.setIsDefault("55");
		suppPurseBean.setCurrencyId("55");
		suppPurseBean.setCurrencyCode("77");
		suppPurseBean.setUpc("55");
		suppPurseBean.setMinorUnits("55");
		List<SupportedPurse> purseMapping = new ArrayList<>();
		purseMapping.add(suppPurseBean);

		when(productService.getSupportedPurseDtls(valueDto)).thenReturn(purseMapping);

		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

	@Test(expected = ServiceException.class)
	public void currecycode_product_doesNotContainCurrencyCode() {

		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, null);
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID, "8");
		valueObj.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, "55");
		valueObj.put(ValueObjectKeys.MINORUNITS, "1");
		valueObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, "99");

		when(productService.getSupportedPurseDtls(valueDto)).thenReturn(null);

		commonValidationServiceImplInstance.currencyCodeValidation(valueDto);

	}

}
