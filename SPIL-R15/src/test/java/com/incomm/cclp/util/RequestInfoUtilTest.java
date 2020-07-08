package com.incomm.cclp.util;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.AccountPurseDTO;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ValueDTO;

@RunWith(MockitoJUnitRunner.class)
public class RequestInfoUtilTest {

	@Autowired
	@InjectMocks
	RequestInfoService requestInfoUtil;

	public void testRequestInfo_withNullInput() {
		try {
			ValueDTO valueDTO = new ValueDTO();
			valueDTO.setValueObj(generateEmptyValueObject());
			valueDTO.setProductAttributes(generateEmptyProductAttributes());
			requestInfoUtil.getRequestInfo(valueDTO);
		} catch (Exception e) {
			fail("Test failed due to exception");
		}

	}

	@Test
	public void testRequestInfo_withValidInput() {
		try {
			ValueDTO valueDTO = new ValueDTO();
			valueDTO.setValueObj(generateValueObject());
			valueDTO.setProductAttributes(generateProductAttributes());
			RequestInfo req = requestInfoUtil.getRequestInfo(valueDTO);

		} catch (Exception e) {
			fail("Test failed due to exception");
		}

	}

	@Test
	public void testRequestInfo_withPurAuthReqPresent() {
		try {
			ValueDTO valueDTO = new ValueDTO();
			valueDTO.setAccountPurseDto(generateAccountPurseDTO());
			valueDTO.setValueObj(generateValueObject());
			valueDTO.setProductAttributes(generateProductAttributes());
			RequestInfo req = requestInfoUtil.getRequestInfo(valueDTO);
			
			
		} catch (Exception e) {
			fail("Test failed due to exception");
		}

	}

	public Map<String, String> generateEmptyValueObject() {
		Map<String, String> valueObj = new HashMap<String, String>();
		valueObj.put(ValueObjectKeys.MINORUNITS, "2");
		valueObj.put(ValueObjectKeys.PURSE_ID, "1");
		valueObj.put(ValueObjectKeys.LAST_TXN_DATE, null);
		valueObj.put(ValueObjectKeys.CARD_NUMBER, null);
		valueObj.put(ValueObjectKeys.SPIL_PROD_ID, null);
		valueObj.put(ValueObjectKeys.CARD_ACCOUNT_ID, null);
		valueObj.put(ValueObjectKeys.CARD_CARDSTAT, null);
		valueObj.put(ValueObjectKeys.ISSUER_ID, null);
		valueObj.put(ValueObjectKeys.PARTNER_ID, null);
		valueObj.put(ValueObjectKeys.ACCOUNT_PURSE_ID,null);
		return valueObj;
	}

	public Map<String, String> generateValueObject() {
		Map<String, String> valueObj = new HashMap<String, String>();
		valueObj.put(ValueObjectKeys.MINORUNITS, "2");
		valueObj.put(ValueObjectKeys.LAST_TXN_DATE, null);
		valueObj.put(ValueObjectKeys.CARD_NUMBER, "7788778800000490");
		valueObj.put(ValueObjectKeys.SPIL_PROD_ID, "32");
		valueObj.put(ValueObjectKeys.CARD_ACCOUNT_ID, "320001317");
		valueObj.put(ValueObjectKeys.CARD_CARDSTAT, "99");
		valueObj.put(ValueObjectKeys.OLD_CARD_STATUS, "98");
		valueObj.put(ValueObjectKeys.PROXY_NUMBER, "53466575635");
		valueObj.put(ValueObjectKeys.MSG_TYPE, "0200");
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, "01");
		valueObj.put(ValueObjectKeys.TRANS_CODE, "098");
		valueObj.put(ValueObjectKeys.SPIL_TRAN_DATE, "04-01-2019");
		valueObj.put(ValueObjectKeys.SPIL_TRAN_TIME, "09:09:09");
		valueObj.put(ValueObjectKeys.INCOM_REF_NUMBER, "235467545674");
		valueObj.put(ValueObjectKeys.SPIL_MERCHANT_NAME, "Incomm");
		valueObj.put(ValueObjectKeys.SPIL_STORE_ID, "412312");
		valueObj.put(ValueObjectKeys.SPIL_TERM_ID, "35454");
		valueObj.put(ValueObjectKeys.SPIL_MERCREF_NUMBER, "12458764");
		valueObj.put(ValueObjectKeys.SPIL_FEE_AMT, "0.0");
		valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, "10.0");
		valueObj.put(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR, "USD");
		valueObj.put(ValueObjectKeys.MDM_ID, "455726");
		valueObj.put(ValueObjectKeys.CARD_NUM_HASH, "/MzNUVQExTY9KlGVvoCctl1t9zhHKEHv3kTcEMQdBFg=");
		valueObj.put(ValueObjectKeys.CARD_NUM_ENCR, "37AF3B8C67B90DEEFFD3F7587707F89F2CF1D1DE68A2B5AFCC29C929D0388999");
		valueObj.put(ValueObjectKeys.LAST_4DIGIT, "0490");
		valueObj.put(ValueObjectKeys.TRANSACTIONDESC, "Sale Active Code");
		valueObj.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, "C");
		valueObj.put(ValueObjectKeys.IS_FINANCIAL, "Y");
		valueObj.put(ValueObjectKeys.ACCOUNT_NUMBER, "A00032000000001317");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT, "0");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		valueObj.put(ValueObjectKeys.ISSUER_ID, "01");
		valueObj.put(ValueObjectKeys.PARTNER_ID, "01");
		valueObj.put(ValueObjectKeys.PURSE_ID, "1");
		valueObj.put(ValueObjectKeys.ACCOUNT_PURSE_ID,"32455");
		valueObj.put(ValueObjectKeys.PURSE_TYPE_ID,"1");
		return valueObj;
	}

	public Map<String, Map<String, Object>> generateEmptyProductAttributes() {
		Map<String, Map<String, Object>> prodAttri = new HashMap<String, Map<String, Object>>();
		Map<String, Object> generalAttributes = new HashMap<String, Object>();
		Map<String, Object> productAttributes = new HashMap<String, Object>();
		productAttributes.put(ValueObjectKeys.RETAIL_UPC, null);
		productAttributes.put(ValueObjectKeys.PRODUCT_FUNDING, null);
		generalAttributes.put(ValueObjectKeys.MAXCARDBAL, "");
		generalAttributes.put(ValueObjectKeys.PARTIALAUTHINDICATOR, null);
		prodAttri.put("Product", productAttributes);
		prodAttri.put("General", generalAttributes);
		return prodAttri;
	}

	public Map<String, Map<String, Object>> generateProductAttributes() {
		Map<String, Map<String, Object>> prodAttri = new HashMap<String, Map<String, Object>>();
		Map<String, Object> generalAttributes = new HashMap<String, Object>();
		Map<String, Object> productAttributes = new HashMap<String, Object>();
		productAttributes.put(ValueObjectKeys.RETAIL_UPC, "124557");
		productAttributes.put(ValueObjectKeys.PRODUCT_FUNDING, "");
		productAttributes.put(ValueObjectKeys.DEFAULT_PURSE, "1");
		generalAttributes.put(ValueObjectKeys.MAXCARDBAL, "3000");
		generalAttributes.put(ValueObjectKeys.PARTIALAUTHINDICATOR, "true");
		prodAttri.put("Product", productAttributes);
		prodAttri.put("General", generalAttributes);
		return prodAttri;
	}

	public AccountPurseDTO generateAccountPurseDTO() {
		AccountPurseDTO purse = AccountPurseDTO.builder()
		.accountPurseId(43245)
		.transactionAmount(BigDecimal.TEN)
		.build();
		return purse;
	}
}
