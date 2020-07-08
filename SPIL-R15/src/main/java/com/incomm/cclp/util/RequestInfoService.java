package com.incomm.cclp.util;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.dto.AccountPurseDTO;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RequestInfoService {
	/**
	 * generate object for request info
	 * 
	 * @throws ServiceException
	 **/

	public RequestInfo getRequestInfo(ValueDTO valueDto) throws ServiceException {
		log.debug(GeneralConstants.ENTER);

		Map<String, String> valueObj = valueDto.getValueObj();

		String digit = valueObj.get(ValueObjectKeys.MINORUNITS);
		log.debug("roundOffDigit: " + valueObj.get(ValueObjectKeys.MINORUNITS));
		int roundoffdigits = "null".equalsIgnoreCase(valueObj.get(ValueObjectKeys.MINORUNITS)) ? 0
				: Integer.parseInt(valueObj.get(ValueObjectKeys.MINORUNITS));

		RequestInfo req = new RequestInfo(roundoffdigits);

		Date lasttxndate = valueObj.get(ValueObjectKeys.LAST_TXN_DATE) == null ? null
				: Util.formatDate(ValueObjectKeys.DATE_FORMAT, valueObj.get(ValueObjectKeys.LAST_TXN_DATE));
		req.setLastTxnDate(lasttxndate);
		log.debug(ValueObjectKeys.LAST_TXN_DATE + ":" + lasttxndate);

		req.setCardNum(valueObj.get(ValueObjectKeys.CARD_NUMBER));
		log.debug(ValueObjectKeys.CARD_NUMBER + ":" + valueObj.get(ValueObjectKeys.CARD_NUMBER));

		req.setProductId(valueObj.get(ValueObjectKeys.SPIL_PROD_ID));
		log.debug(ValueObjectKeys.SPIL_PROD_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_PROD_ID));

		req.setAccountId(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));
		log.debug(ValueObjectKeys.CARD_ACCOUNT_ID + ":" + valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));

		req.setCardStatus(valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
		log.debug(ValueObjectKeys.CARD_CARDSTAT + ":" + valueObj.get(ValueObjectKeys.CARD_CARDSTAT));

		req.setOldCardStatus(valueObj.get(ValueObjectKeys.OLD_CARD_STATUS));
		log.debug(ValueObjectKeys.OLD_CARD_STATUS + ":" + valueObj.get(ValueObjectKeys.OLD_CARD_STATUS));

		req.setProxyNumber(valueObj.get(ValueObjectKeys.PROXY_NUMBER));
		log.debug(ValueObjectKeys.PROXY_NUMBER + ":" + valueObj.get(ValueObjectKeys.PROXY_NUMBER));

		req.setUpc((String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT)
			.get(ValueObjectKeys.RETAIL_UPC));
		log.debug(ValueObjectKeys.RETAIL_UPC + ":" + (String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT)
			.get(ValueObjectKeys.RETAIL_UPC));

		req.setMsgType(valueObj.get(ValueObjectKeys.MSG_TYPE));
		log.debug(ValueObjectKeys.MSG_TYPE + ":" + valueObj.get(ValueObjectKeys.MSG_TYPE));

		req.setChannel(valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
		log.debug(ValueObjectKeys.DELIVERY_CHANNEL_CODE + ":" + valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));

		req.setTxnCode(valueObj.get(ValueObjectKeys.TRANS_CODE));
		log.debug(ValueObjectKeys.TRANS_CODE + ":" + valueObj.get(ValueObjectKeys.TRANS_CODE));

		req.setTxnDate(valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
		log.debug(ValueObjectKeys.SPIL_TRAN_DATE + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));

		req.setTxnTime(valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
		log.debug(ValueObjectKeys.SPIL_TRAN_TIME + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));

		req.setTxnTimeZone(valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
		log.debug(ValueObjectKeys.SPIL_REQTIME_ZONE + ":" + valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));

		req.setRrn(valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
		log.debug(ValueObjectKeys.INCOM_REF_NUMBER + ":" + valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));

		req.setMerchantName(valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
		log.debug(ValueObjectKeys.SPIL_MERCHANT_NAME + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));

		req.setStoreDbId(valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
		log.debug(ValueObjectKeys.SPIL_STORE_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_STORE_ID));

		req.setTerminalId(valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
		log.debug(ValueObjectKeys.SPIL_TERM_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_TERM_ID));

		req.setStoreAddress1(valueObj.get(ValueObjectKeys.SPIL_ADDRESS_1));
		log.debug(ValueObjectKeys.SPIL_ADDRESS_1 + ":" + valueObj.get(ValueObjectKeys.SPIL_ADDRESS_1));

		req.setStoreAddress2(valueObj.get(ValueObjectKeys.SPIL_ADDRESS_2));
		log.debug(ValueObjectKeys.SPIL_ADDRESS_2 + ":" + valueObj.get(ValueObjectKeys.SPIL_ADDRESS_2));

		req.setStoreCity(valueObj.get(ValueObjectKeys.SPIL_CITY));
		log.debug(ValueObjectKeys.SPIL_CITY + ":" + valueObj.get(ValueObjectKeys.SPIL_CITY));

		req.setStoreState(valueObj.get(ValueObjectKeys.SPIL_STATE));
		log.debug(ValueObjectKeys.SPIL_STATE + ":" + valueObj.get(ValueObjectKeys.SPIL_STATE));

		req.setMerchantRefNum(valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
		log.debug(ValueObjectKeys.SPIL_MERCREF_NUMBER + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));

		req.setLocaleCountry(valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
		log.debug(ValueObjectKeys.SPIL_LOCALE_CNTRY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));

		req.setLocaleCurrency(valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
		log.debug(ValueObjectKeys.SPIL_LOCALE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));

		req.setLocaleLanguage(valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
		log.debug(ValueObjectKeys.SPIL_LOCALE_LANGUAGE + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));

		req.setPosEntryMode(valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
		log.debug(ValueObjectKeys.SPIL_POS_ENTRYMODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));

		req.setPosConditionCode(valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
		log.debug(ValueObjectKeys.SPIL_POS_CONDCODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));

		req.setFeeAmount(valueObj.get(ValueObjectKeys.SPIL_FEE_AMT));
		log.debug(ValueObjectKeys.SPIL_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_FEE_AMT));

		req.setOrgnlTranCurr(valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
		log.debug(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR + ":" + valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));

		req.setPartySupported(valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));
		log.debug(ValueObjectKeys.PARTY_SUPPORTED + ":" + valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));

		req.setPartialPreAuthInd((String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.GENERAL)
			.get(ValueObjectKeys.PARTIALAUTHINDICATOR));
		log.debug(ValueObjectKeys.PARTIALAUTHINDICATOR + ":" + (String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.GENERAL)
			.get(ValueObjectKeys.PARTIALAUTHINDICATOR));

		req.setMdmId(valueObj.get(ValueObjectKeys.MDM_ID));
		log.debug(ValueObjectKeys.MDM_ID + ":" + valueObj.get(ValueObjectKeys.MDM_ID));

		String productFunding = (String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT)
			.get(ValueObjectKeys.PRODUCT_FUNDING);
		req.setProductFunding(productFunding);
		log.debug(ValueObjectKeys.PRODUCT_FUNDING + ":" + productFunding);

		req.setCashierId(valueObj.get(ValueObjectKeys.CASHIER_ID));
		log.debug(ValueObjectKeys.CASHIER_ID + ":" + valueObj.get(ValueObjectKeys.CASHIER_ID));

		req.setExpiryDate(valueObj.get(ValueObjectKeys.CARD_EXPDATE));
		log.debug(ValueObjectKeys.CARD_EXPDATE + ":" + valueObj.get(ValueObjectKeys.CARD_EXPDATE));

		req.setCardNumHash(valueObj.get(ValueObjectKeys.CARD_NUM_HASH));
		log.debug(ValueObjectKeys.CARD_NUM_HASH + ":" + valueObj.get(ValueObjectKeys.CARD_NUM_HASH));

		req.setCardNumEncr(valueObj.get(ValueObjectKeys.CARD_NUM_ENCR));
		log.debug(ValueObjectKeys.CARD_NUM_ENCR + ":" + valueObj.get(ValueObjectKeys.CARD_NUM_ENCR));

		req.setCardLast4digits(valueObj.get(ValueObjectKeys.LAST_4DIGIT));
		log.debug(ValueObjectKeys.LAST_4DIGIT + ":" + valueObj.get(ValueObjectKeys.LAST_4DIGIT));

		req.setTxnDesc(valueObj.get(ValueObjectKeys.TRANSACTIONDESC));
		log.debug(ValueObjectKeys.TRANSACTIONDESC + ":" + valueObj.get(ValueObjectKeys.TRANSACTIONDESC));

		req.setCreditDebitFlg(valueObj.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR));
		log.debug(ValueObjectKeys.CREDIT_DEBIT_INDICATOR + ":" + valueObj.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR));

		req.setTxnType(valueObj.get(ValueObjectKeys.IS_FINANCIAL));
		log.debug(ValueObjectKeys.IS_FINANCIAL + ":" + valueObj.get(ValueObjectKeys.IS_FINANCIAL));

		req.setAccountNumber(valueObj.get(ValueObjectKeys.ACCOUNT_NUMBER));
		log.debug(ValueObjectKeys.ACCOUNT_NUMBER + ":" + valueObj.get(ValueObjectKeys.ACCOUNT_NUMBER));

		req.setPartnerId(valueObj.get(ValueObjectKeys.PARTNER_ID));
		log.debug(ValueObjectKeys.PARTNER_ID + ":" + valueObj.get(ValueObjectKeys.PARTNER_ID));

		req.setIssuerId(valueObj.get(ValueObjectKeys.ISSUER_ID));
		log.debug(ValueObjectKeys.ISSUER_ID + ":" + valueObj.get(ValueObjectKeys.ISSUER_ID));

		req.setProdFeeCondition(valueObj.get(ValueObjectKeys.PROD_FEE_CONDITION));
		log.debug(ValueObjectKeys.PROD_FEE_CONDITION + ":" + valueObj.get(ValueObjectKeys.PROD_FEE_CONDITION));

		req.setTxnFee(valueObj.get(ValueObjectKeys.PROD_TXN_FEE_AMT));
		log.debug(ValueObjectKeys.PROD_TXN_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.PROD_TXN_FEE_AMT));

		req.setProdFlatFeeAmount(valueObj.get(ValueObjectKeys.PROD_FLAT_FEE_AMT));
		log.debug(ValueObjectKeys.PROD_FLAT_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.PROD_FLAT_FEE_AMT));

		req.setProdPercentFeeAmount(valueObj.get(ValueObjectKeys.PROD_PERCENT_FEE_AMT));
		log.debug(ValueObjectKeys.PROD_PERCENT_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.PROD_PERCENT_FEE_AMT));

		req.setProdMinFeeAmount(valueObj.get(ValueObjectKeys.PROD_MIN_FEE_AMT));
		log.debug(ValueObjectKeys.PROD_MIN_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.PROD_MIN_FEE_AMT));

		req.setFreeFeeFlg(valueObj.get(ValueObjectKeys.FREE_FEE_FLAG));
		log.debug(ValueObjectKeys.FREE_FEE_FLAG + ":" + valueObj.get(ValueObjectKeys.FREE_FEE_FLAG));

		req.setMaxFeeFlg(valueObj.get(ValueObjectKeys.MAX_FEE_FLAG));
		log.debug(ValueObjectKeys.MAX_FEE_FLAG + ":" + valueObj.get(ValueObjectKeys.MAX_FEE_FLAG));

		req.setFirstTimeTopupFlag(valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
		log.debug(ValueObjectKeys.FIRST_TIMETOPUP_FLAG + ":" + valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));

		req.setMaxPurseBalance(valueDto.getProductAttributes()
			.get(ValueObjectKeys.GENERAL)
			.get(ValueObjectKeys.MAXCARDBAL));
		log.debug(ValueObjectKeys.MAXCARDBAL + ":" + (String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.GENERAL)
			.get(ValueObjectKeys.MAXCARDBAL));

		req.setRedemDelayTranAmount(valueObj.get(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT));
		log.debug(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT + ":" + valueObj.get(ValueObjectKeys.REDEMPTION_DELAY_TRAN_AMOUNT));

		req.setSysDate(valueObj.get(ValueObjectKeys.DB_SYSDATE));
		log.debug(ValueObjectKeys.DB_SYSDATE + ":" + valueObj.get(ValueObjectKeys.DB_SYSDATE));

		req.setSerialNumber(valueObj.get(ValueObjectKeys.CARD_SERIAL_NUMBER));
		log.debug(ValueObjectKeys.CARD_SERIAL_NUMBER + ":" + valueObj.get(ValueObjectKeys.CARD_SERIAL_NUMBER));

		req.setMerchantId(valueObj.get(ValueObjectKeys.MERCHANT_ID));
		log.debug(ValueObjectKeys.MERCHANT_ID + ":" + valueObj.get(ValueObjectKeys.MERCHANT_ID));

		req.setLocationId(valueObj.get(ValueObjectKeys.LOCATION_ID));
		log.debug(ValueObjectKeys.LOCATION_ID + ":" + valueObj.get(ValueObjectKeys.LOCATION_ID));

		req.setTxnReversalFlag(valueObj.get(ValueObjectKeys.REVERSAL_FLAG));
		log.debug(ValueObjectKeys.REVERSAL_FLAG + ":" + valueObj.get(ValueObjectKeys.REVERSAL_FLAG));

		req.setValueDTO(valueDto);

		req.setDigitalPin(valueObj.get(ValueObjectKeys.DIGITAL_PIN));
		log.debug(ValueObjectKeys.DIGITAL_PIN + ":" + valueObj.get(ValueObjectKeys.DIGITAL_PIN));

		req.setReqTxnAmt(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT));
		req.setConvRate(valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE));
		req.setTranCurr(valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));

		req.setTxnAmount(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
		log.debug(ValueObjectKeys.SPIL_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
		req.setPurseId(new BigInteger(valueObj.get(ValueObjectKeys.PURSE_ID)));

		if (valueDto.getAccountPurseDto() != null && valueDto.getAccountPurseDto()
			.getPurseName() != null) {
			req.setPurseType(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
			req.setPurseTypeId(Integer.valueOf(valueObj.get(ValueObjectKeys.PURSE_TYPE_ID)));
			addPurseExtensionDetails(valueDto.getAccountPurseDto(), req, valueObj);
			req.setMaxPurseBalance(valueDto.getProductAttributes()
				.get(ValueObjectKeys.PURSE) == null ? 0
						: valueDto.getProductAttributes()
							.get(ValueObjectKeys.PURSE)
							.get(ValueObjectKeys.MAX_PURSE_BALANCE));
		}

		// defualt purse
		String defaultPurseId = (String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT)
			.get(ValueObjectKeys.DEFAULT_PURSE);
		req.setDefaultPurseId(new BigInteger(defaultPurseId));

		req.setSourceName(valueObj.get(ValueObjectKeys.SOURCE_INFO));

		req.setStoreId(valueObj.get(ValueObjectKeys.STORE_ID));
		log.debug(ValueObjectKeys.STORE_ID + ":" + valueObj.get(ValueObjectKeys.STORE_ID));

		log.debug(GeneralConstants.EXIT);
		return req;
	}

	private void addPurseExtensionDetails(AccountPurseDTO extPurse, RequestInfo req, Map<String, String> valueObj) {

		AccountPurseBalance purse = AccountPurseBalance.builder()
			.accountPurseId(extPurse.getAccountPurseId())
			.accountId(req.getAccountId()
				.longValue())
			.purseId(req.getPurseId()
				.longValue())
			.productId(req.getProductId()
				.longValue())

			.effectiveDate(null)
			.expiryDate(null)
			.purseType(valueObj.get(ValueObjectKeys.PURSE_TYPE))
			.purseTypeId(req.getPurseTypeId())
			.currencyCode(valueObj.get(ValueObjectKeys.CURRENCY_ID))
			.skuCode(extPurse.getSkuCode())
			.build();
		req.setPurAuthReq(purse);
		req.setPurseName(valueObj.get(ValueObjectKeys.PURSE_NAME));
	}
}
