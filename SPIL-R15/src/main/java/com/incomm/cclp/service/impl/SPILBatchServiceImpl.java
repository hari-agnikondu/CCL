package com.incomm.cclp.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.domain.BulkTransactionResponseFile;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.response.SPILResponseBuilder;
import com.incomm.cclp.service.CardDetailsService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.service.SPILBatchService;
import com.incomm.cclp.service.SpilCommonService;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.transaction.validation.AuthorizationValidation;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.validation.SPILDataElementsValidation;

/**
 * 
 * @author rathanakumark
 *
 */
@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SPILBatchServiceImpl implements SPILBatchService {

	@Autowired
	private SpilDAO spilDAO;

	@Autowired
	private SPILDataElementsValidation dataElementsValidation;

	@Autowired
	private CardDetailsService cardDetailsService;

	@Autowired
	private ProductService productService;

	@Autowired
	private SPILResponseBuilder responseBuilder;

	@Autowired
	private LocalCacheServiceImpl localCacheService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	AuthorizationValidation authorizationValidation;

	@Autowired
	private Map<String, SpilCommonService> spilCommonService;

	private static final Logger logger = LogManager.getLogger(SpilServiceImpl.class);

	/**
	 * @Description Method to Process the spil transactions
	 */
	@Override
	public BulkTransactionResponseFile callSPILTransaction(String jsonMsg) throws ServiceException {
		logger.debug("Entered");
		long timeBeforeTransaction = System.currentTimeMillis();
		Map<String, String> valueObj = new HashMap<>(50);
		ValueDTO valueDto = new ValueDTO();
		String respCode = null;
		BulkTransactionResponseFile txnResponse = null;
		Map<String, String> transMap = new HashMap<>();
		Map<String, Map<String, Object>> productAttributes = null;
		String[] response = null;
		long daoProcessTime = 0;
		logger.info("Transaction start time: {}", timeBeforeTransaction);

		try {

			// ParseMasterdata
			valueObj = parseMasterData(jsonMsg);
			valueObj.put(ValueObjectKeys.DELIVERYCHNL, "06");
			// Setting values to value Object
			valueObj.put(ValueObjectKeys.SPNUMBER, valueObj.get("spNumber"));
			valueObj.put(ValueObjectKeys.SPIL_STORE_ID, valueObj.get("storeId"));
			valueObj.put(ValueObjectKeys.SPIL_TERM_ID, valueObj.get("terminalId"));
			valueObj.put(ValueObjectKeys.ORIGINAL_MSGTYPE, valueObj.get(ValueObjectKeys.ACTION));
			valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, valueObj.get("amount"));
			valueObj.put(ValueObjectKeys.ORGNL_TRAN_AMOUNT, valueObj.get("amount"));
			valueObj.put(ValueObjectKeys.SPIL_BLK_TXN_RECORD_NUM, valueObj.get("recordNum"));
			valueObj.put(ValueObjectKeys.MDM_ID, valueObj.get("mdmId"));
			// Getting transaction details
			SpilStartupMsgTypeBean msgTypeBean = transactionService
				.getSpilMessageTypeBean(valueObj.get(ValueObjectKeys.ORIGINAL_MSGTYPE) + valueObj.get(ValueObjectKeys.DELIVERYCHNL));

			if (msgTypeBean == null) {
				throw new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_INVALID, ResponseCodes.INVALID_REQUEST,
						new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_INVALID));
			}

			// Setting transaction details into valueObj
			constructValueObjValues(valueObj, msgTypeBean);

			// Generating INCOM_REF_NUMBER
			Date date = new Date();
			String refDate = new SimpleDateFormat(ValueObjectKeys.REFERENCE_DATE_FORMAT).format(date);
			valueObj.put(ValueObjectKeys.INCOM_REF_NUMBER, valueObj.get(ValueObjectKeys.SOURCE_REF_NUMBER) + valueObj.get("batchId"));

			valueObj.put(ValueObjectKeys.SPIL_TRAN_DATE, new SimpleDateFormat(ValueObjectKeys.TRANLOG_DATE_FORMAT).format(date));
			valueObj.put(ValueObjectKeys.SPIL_TRAN_TIME, new SimpleDateFormat(ValueObjectKeys.TIME_FORMAT).format(date));
			if (valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER) != null) {
				ThreadContext.put("RRN", valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
			}

			// Get card detail
			valueDto = cardDetailsService.getCardDetails(valueObj.get(ValueObjectKeys.SPNUMBER), valueObj);

			// Data Element validations
			dataElementsValidation.validateBatchElements(valueObj);

			// // get product details from DB
			productAttributes = productService.getProductAttributes(valueDto.getValueObj()
				.get(ValueObjectKeys.PRODUCT_ID), null);
			valueDto.setProductAttributes(productAttributes);

			cardDetailsService.doPopulateSupportedPurseDtls(valueDto);

			// getProductUPC
			String actualUpcCode = productService.getProductUPC(valueDto);
			// String mdmId=productService.getPartnerDetails(valueDto);
			String currencyCode = productService.getProductDefaultCurr(valueDto);
			valueObj.put(ValueObjectKeys.SPIL_UPC_CODE, actualUpcCode);

			valueObj.put(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR, currencyCode);
			valueObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, currencyCode);

			authorizationValidation.validate(valueDto);

			// Expired product Check
			if (!ValueObjectKeys.EXPIRED_PRODUCT_CARD_STATUS.equals(String.valueOf(valueDto.getValueObj()
				.get(ValueObjectKeys.CARD_CARDSTAT)))) {
				// checkproductvalidity
				boolean flag = productService.checkProductValidity(valueDto);

				if (!flag) {
					logger.info("Product is expired. Updating card status to expired product");
					productService.updateCardStatus(String.valueOf(valueDto.getValueObj()
						.get(ValueObjectKeys.CARD_NUM_HASH)), ValueObjectKeys.EXPIRED_PRODUCT_CARD_STATUS);
					valueDto.getValueObj()
						.put(ValueObjectKeys.CARD_CARDSTAT, ValueObjectKeys.EXPIRED_PRODUCT_CARD_STATUS);
					throw new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_NOT_ALLOWED, ResponseCodes.INVALID_CARD);
				}
			}

			// Load TransacionDAO Classes
			long timeBeforeDaoClass = System.currentTimeMillis();
			response = processSPILRequest(valueDto);
			logger.info("response::::::::::::", response);
			long timeAfterDaoClass = System.currentTimeMillis();
			daoProcessTime = timeAfterDaoClass - timeBeforeDaoClass;
			logger.info("Transaction dao process time:{}ms", daoProcessTime);

			// Build Response
			txnResponse = batchBuildResponse(valueObj, response);

		} catch (ServiceException serviceExec) {

			if (Objects.isNull(valueDto) || CollectionUtils.isEmpty(valueDto.getValueObj()))
				transMap.putAll(valueObj);
			else
				transMap.putAll(valueDto.getValueObj());

			insertFailedTransaction(transMap, serviceExec.getResponseID(), serviceExec.getMessage());
			logger.error(
					"ResponseCode:" + serviceExec.getResponseID() + " Message:" + serviceExec.getMessage() + "  Exception:" + serviceExec);
			txnResponse = this.buildFailResponse(valueObj, serviceExec.getResponseID(), serviceExec.getMessage());

		} catch (Exception e) {

			if (Objects.isNull(valueDto) || CollectionUtils.isEmpty(valueDto.getValueObj()))
				transMap.putAll(valueObj);
			else
				transMap.putAll(valueDto.getValueObj());

			logger.debug("Put an entry in transaction log");
			respCode = ResponseCodes.SYSTEM_ERROR;
			insertFailedTransaction(transMap, respCode, e.getMessage());
			logger.error("Process Terminated Due to Exception :" + e.getMessage(), e);
			txnResponse = this.buildFailResponse(valueObj, respCode, e.getMessage());

		} finally {
			long timeAfterTransaction = System.currentTimeMillis();
			long timeTaken = timeAfterTransaction - timeBeforeTransaction;
			logger.info("Transaction response Time:{}ms", timeTaken);

		}
		logger.info("Transaction response {}", txnResponse);
		logger.debug("Exit");
		ThreadContext.clearAll();
		return txnResponse;

	}

	/**
	 * @Description Method to convert Json String to Obejct
	 * @param xmlMsg
	 * @return
	 * @throws ServiceException
	 */
	private Map<String, String> parseMasterData(String xmlMsg) throws ServiceException {
		HashMap<String, String> masterData = null;
		try {
			masterData = new ObjectMapper().readValue(xmlMsg, HashMap.class);
		} catch (Exception e) {
			throw new ServiceException("Invalid request data", ResponseCodes.SYSTEM_ERROR);
		}
		return masterData;

	}

	/**
	 * @Description Method to insert failure transaction into transaction log table
	 * @param valObject
	 * @param respCode
	 * @param respMsg
	 */
	void insertFailedTransaction(Map<String, String> valObject, String respCode, String respMsg) {

		try {

			// MSGType is not available default msgtype is adding for isert
			if (valObject.get(ValueObjectKeys.MSGTYPE) == null)
				valObject.put(ValueObjectKeys.MSGTYPE, "0200");

			// if RRN length is more than 15 digits substringed for insert
			// in transactionlog
			String rrn = String.valueOf(valObject.get(ValueObjectKeys.INCOM_REF_NUMBER));
			if (rrn != null && rrn.length() > 40) {
				rrn = rrn.substring(0, 40);
				logger.debug("Transaction failed rrn: " + rrn);
				valObject.put(ValueObjectKeys.INCOM_REF_NUMBER, rrn);
			}
			// if BUSINESS_DATE length is more than 8 digits substringed for
			// insert in transactionlog
			String bdate = String.valueOf(valObject.get(ValueObjectKeys.SPIL_TRAN_DATE));
			if (bdate != null && bdate.length() > 8) {
				bdate = bdate.substring(0, 8);
				logger.debug("Transaction failed trans date: " + bdate);
				valObject.put(ValueObjectKeys.SPIL_TRAN_DATE, bdate);
			}
			// if TRANS_CODE length is more than 2 digits substringed for
			// insert in transactionlog
			String trancde = String.valueOf(valObject.get(ValueObjectKeys.TRANS_CODE));
			if (trancde != null && trancde.length() > 3) {

				trancde = trancde.substring(0, 3);
				logger.debug("Transaction failed trans code: " + trancde);
				valObject.put(ValueObjectKeys.TRANS_CODE, trancde);
			}
			// if TXN_MODE length is more than 1 digits substringed for
			// insert in transactionlog
			// if DELIVERYCHNL length is more than 2 digits substringed for
			// insert in transactionlog
			String delChanel = String.valueOf(valObject.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
			if (delChanel != null && delChanel.length() > 2) {
				delChanel = delChanel.substring(0, 2);
				logger.debug("Transaction failed delivery channel: " + delChanel);
				valObject.put(ValueObjectKeys.DELIVERY_CHANNEL_CODE, delChanel);
			}
			// if BUSINESS_TIME length is more than 6 digits substringed for
			// insert in transactionlog_dtl
			String bussinsessTime = String.valueOf(valObject.get(ValueObjectKeys.SPIL_TRAN_TIME));
			if (bussinsessTime != null && bussinsessTime.length() > 6) {
				bussinsessTime = bussinsessTime.substring(0, 6);
				logger.debug("Transaction failed trans time: " + bussinsessTime);
				valObject.put(ValueObjectKeys.SPIL_TRAN_TIME, bussinsessTime);
			}
			// if TRANAMOUNT length is more than 11 digits substringed for
			// insert in transactionlog
			double doubleTranAmt = 0;
			String tranAmt = "";
			try {
				if (valObject.get(ValueObjectKeys.SPIL_TRAN_AMT) != null) {
					doubleTranAmt = Double.parseDouble(String.valueOf(valObject.get(ValueObjectKeys.SPIL_TRAN_AMT)));
				} else {
					doubleTranAmt = 0;
				}

			} catch (NumberFormatException ne) {
				tranAmt = "0";
				valObject.put(ValueObjectKeys.SPIL_TRAN_AMT, tranAmt);
			}

			if (doubleTranAmt != 0) {
				String tranAmttemp = doubleTranAmt + "";
				if (!tranAmt.matches("[0-9]{1,012}|([0-9]{1,012}\\.[0-9]{1,3})")) {
					tranAmt = "0";

					if (tranAmttemp.length() > 15) {
						tranAmt = tranAmttemp.substring(0, 15);

					}
					logger.debug("Transaction failed trans amt: " + tranAmt);
					valObject.put(ValueObjectKeys.SPIL_TRAN_AMT, tranAmt);
				} else {
					tranAmt = tranAmttemp;
					logger.debug("Transaction failed trans amt: " + tranAmt);
					valObject.put(ValueObjectKeys.SPIL_TRAN_AMT, tranAmt);
				}
			}

			logger.debug("tranAmt:" + tranAmt);

			// if OrgnlTRANAMOUNT length is more than 11 digits substringed for
			// insert in transactionlog
			double doubleorgnlTranAmt = 0;
			String orgnltranAmt = "";
			try {
				if (valObject.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT) != null) {
					doubleorgnlTranAmt = Double.parseDouble(String.valueOf(valObject.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)));
				} else {
					doubleorgnlTranAmt = 0;
				}

			} catch (NumberFormatException ne) {
				orgnltranAmt = "0";
				valObject.put(ValueObjectKeys.ORGNL_TRAN_AMOUNT, orgnltranAmt);
			}

			if (doubleorgnlTranAmt != 0) {
				String orgnltranAmttemp = doubleorgnlTranAmt + "";
				Pattern pattern = Pattern.compile("[0-9]{1,012}|([0-9]{1,012}\\.[0-9]{1,3})");

				if (!pattern.matcher(orgnltranAmttemp)
					.matches()) {
					orgnltranAmt = "0";

					if (orgnltranAmttemp.length() > 15) {
						orgnltranAmt = orgnltranAmttemp.substring(0, 15);

					}
					logger.debug("Transaction failed Orgnl trans amt: " + orgnltranAmt);
					valObject.put(ValueObjectKeys.ORGNL_TRAN_AMOUNT, orgnltranAmt);
				} else {
					orgnltranAmt = orgnltranAmttemp;
					logger.debug("Transaction failed orgnl trans amt: " + orgnltranAmt);
					valObject.put(ValueObjectKeys.ORGNL_TRAN_AMOUNT, orgnltranAmt);
				}
			}

			logger.debug("orgnltranAmt:" + orgnltranAmt);

			logger.debug("Curr:" + valObject.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));

			String currCode = String.valueOf(valObject.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
			if (currCode != null && currCode.length() > 3) {
				currCode = currCode.substring(0, 3);
				logger.debug("Transaction failed trans currency code: " + currCode);
				valObject.put(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR, currCode);
			}

			logger.debug("Purse Curr:" + valObject.get(ValueObjectKeys.TXN_CURRENCY_CODE));

			String pursecurrCode = String.valueOf(valObject.get(ValueObjectKeys.TXN_CURRENCY_CODE));
			if (!Util.isEmpty(pursecurrCode) && pursecurrCode.length() > 3) {
				pursecurrCode = pursecurrCode.substring(0, 3);
				logger.debug("Transaction failed purse currency code: " + pursecurrCode);
				valObject.put(ValueObjectKeys.TXN_CURRENCY_CODE, pursecurrCode);
			}

			String respMsg1 = null;
			if (respMsg != null) {
				respMsg1 = respMsg.substring(0, respMsg.length() > 500 ? 500 : respMsg.length());
			} else {
				respMsg1 = "System Error";
			}

			// logged the merchant detail
			// Modified by Ramkumar.MK on 15 sep 2012,check the null value of merchant
			// name,state and city.
			String mercName = "";
			mercName = String.valueOf(valObject.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
			if (mercName != null) {
				if (mercName.length() > 30) {
					mercName = mercName.substring(0, 30);
					logger.debug("Transaction failed trans merchant name: " + mercName);
					valObject.put(ValueObjectKeys.SPIL_MERCHANT_NAME, mercName);
				}
			} else {
				logger.debug("Transaction failed trans merchant Name: " + mercName);
				valObject.put(ValueObjectKeys.SPIL_MERCHANT_NAME, "");
			}
			if (valObject.get(ValueObjectKeys.SPIL_FEE_AMT) == null) {
				valObject.put(ValueObjectKeys.SPIL_FEE_AMT, "0.00");
			}
			// logged the merchant detail
			if (respCode != null && respMsg1 != null) {
				logger.debug("Insert failed transaction in log table respCode: {}, respMsg: {}", respCode, respMsg);
			}
			logger.debug("Put an entry in transaction log");
			spilDAO.transactionLogEntry(valObject, respCode, respMsg1);

		} catch (Exception e) {
			logger.error("Exception occured while validating transaction log entry: " + e.getMessage(), e);
		}

	}

	/**
	 * 
	 * @Description Method to build failure response
	 * @param hKeyValue
	 * @param respField
	 * @return XML response
	 */
	public BulkTransactionResponseFile buildFailResponse(Map<String, String> hKeyValue, String respCode, String respDesc) {
		String respMsg = "";
		String responseCode = "";
		// form a key to get card status attribute
		Map<String, String> cardStatusDefs = null;
		cardStatusDefs = localCacheService.getAllCardStatus(cardStatusDefs);
		if (CollectionUtils.isEmpty(cardStatusDefs)) {
			cardStatusDefs = transactionService.getAllCardStatus();
			localCacheService.getAllCardStatus(cardStatusDefs);
		}
		SpilResponseCode spilResponseCode = transactionService.getSpilResponseCode(respCode);
		String cardStatusDesc = null;
		if (spilResponseCode != null) {
			responseCode = spilResponseCode.getChannelResponseCode();
			if (ResponseCodes.INVALID_REQUEST.equals(spilResponseCode.getResponseCode())) {
				respMsg = respDesc;
			} else {
				respMsg = spilResponseCode.getResponseDescription();
			}

		} else {
			responseCode = "10029";
			respMsg = "System Error";
		}
		if (hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT) != null && !hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT)
			.isEmpty()) {
			cardStatusDesc = hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT);
		}
		hKeyValue.put(ValueObjectKeys.RESP_CODE, responseCode);
		hKeyValue.put(ValueObjectKeys.RESP_MSG, respMsg);// Added to carry forward response code & message in
															// valueObject to PRM/notification services by
															// venkateshgaddam

		BulkTransactionResponseFile bulkTxnRespFile = responseBuilder.addResponseBodyBatch(responseCode, respMsg, "", hKeyValue,
				cardStatusDesc);

		return bulkTxnRespFile;
	}

	/**
	 * @Description Method to build success response
	 * @param hKeyValue
	 * @param respFields
	 * @return
	 * @throws ServiceException
	 */
	public BulkTransactionResponseFile batchBuildResponse(Map<String, String> hKeyValue, String... respFields) throws ServiceException {

		SpilResponseCode spilResponseCode = transactionService.getSpilResponseCode(respFields[0]);
		String cardStatusDesc = null;
		String oldCardStatusDesc = null;

		if (spilResponseCode != null) {
			respFields[0] = spilResponseCode.getChannelResponseCode();
			respFields[1] = spilResponseCode.getResponseDescription(); // Response Message is coming in Field 1 by
			// Karthik L
		}
		BulkTransactionResponseFile bulkTxnRespFile = null;
		hKeyValue.put(ValueObjectKeys.RESP_CODE, respFields[0]);
		hKeyValue.put(ValueObjectKeys.RESP_MSG, respFields[1]); // Added to carry forward response code & message in
																// valueObject to PRM/notification services by
																// venkateshgaddam

		// form a key to get card status attribute
		/*
		 * Map<String, String> cardStatusDefs = null;
		 * 
		 * cardStatusDefs = localCacheService.getAllCardStatus(cardStatusDefs); if
		 * (CollectionUtils.isEmpty(cardStatusDefs)) { cardStatusDefs = transactionService.getAllCardStatus();
		 * localCacheService.getAllCardStatus(cardStatusDefs); }
		 */

		if (hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT) != null && !hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT)
			.isEmpty()) {
			cardStatusDesc = hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT);
		}
		if (hKeyValue.get(ValueObjectKeys.OLD_CARD_STATUS) != null && !hKeyValue.get(ValueObjectKeys.OLD_CARD_STATUS)
			.isEmpty()) {
			oldCardStatusDesc = hKeyValue.get(ValueObjectKeys.OLD_CARD_STATUS);
		}

		if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_ACTIVATION)
				&& hKeyValue.get(ValueObjectKeys.MSG_TYPE)
					.equals(MsgTypeConstants.MSG_TYPE_NORMAL)) {
			bulkTxnRespFile = responseBuilder.addResponseBodyForActivationBatch(respFields[0], respFields[1], respFields[2], respFields[3],
					respFields[4], respFields[5], hKeyValue);
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_BALANCE_INQUIRY)) {
			bulkTxnRespFile = responseBuilder.addResponseBodyForBalInqBatch(respFields[0], respFields[1], respFields[2], respFields[3],
					respFields[4], respFields[5], hKeyValue);
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_DEACTIVATION)
				&& hKeyValue.get(ValueObjectKeys.MSG_TYPE)
					.equals(MsgTypeConstants.MSG_TYPE_NORMAL)) {
			bulkTxnRespFile = responseBuilder.addResponseBodyForDeactBatch(respFields[0], respFields[1], respFields[2], respFields[3],
					respFields[4], hKeyValue, cardStatusDesc, oldCardStatusDesc);
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_VALUE_INSERTION)) {

			bulkTxnRespFile = responseBuilder.addResponseBodyForReLoadBatch(respFields[0], respFields[1], respFields[3], respFields[2],
					hKeyValue, cardStatusDesc);

		} else {
			bulkTxnRespFile = responseBuilder.addResponseBodyBatch(respFields[0], respFields[2], respFields[3], hKeyValue, cardStatusDesc);
		}
		return bulkTxnRespFile;
	}

	/**
	 * @Description Method to set values into valueObj
	 * @param valueObj
	 * @param msgTypeBean
	 * @throws ServiceException
	 */
	private void constructValueObjValues(Map<String, String> valueObj, SpilStartupMsgTypeBean msgTypeBean) {
		valueObj.put(ValueObjectKeys.MSGTYPE, msgTypeBean.getMsgType());
		valueObj.put(ValueObjectKeys.DELIVERYCHNL, msgTypeBean.getDeliveryChannel());
		valueObj.put(ValueObjectKeys.TRANS_CODE, msgTypeBean.getTxnCode());
		valueObj.put(ValueObjectKeys.MEMBERNO, "000");
		valueObj.put(ValueObjectKeys.PARTY_SUPPORTED, msgTypeBean.getPartySupported());
		valueObj.put(ValueObjectKeys.AUTH_JAVA_CLASS_NAME, msgTypeBean.getAuthJavaClass());
		valueObj.put(ValueObjectKeys.IS_FINANCIAL, msgTypeBean.getIsFinacial());
		valueObj.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, msgTypeBean.getCreditDebitIndicator());
		valueObj.put(ValueObjectKeys.SPIL_MSG_TYPE, msgTypeBean.getSpilMsgType());
		valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, msgTypeBean.getChannelShortName());
		valueObj.put(ValueObjectKeys.PASSIVE_SUPPORT_FLAG, msgTypeBean.getPassiveSupported());
		valueObj.put(ValueObjectKeys.TRANSACTIONDESC, msgTypeBean.getTransactionDesc());
	}

	/**
	 * @Description Method to process spil transactions
	 * @param valueDto
	 * @return
	 * @throws ServiceException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String[] processSPILRequest(ValueDTO valueDto) throws ServiceException, SQLException, ParseException {
		logger.debug("Entered in to LoadSPILTransactionsDAOs");

		if (!CollectionUtils.isEmpty(valueDto.getValueObj()) && !Util.isEmpty(valueDto.getValueObj()
			.get("transactionClassName"))) {
			return spilCommonService.get(valueDto.getValueObj()
				.get("transactionClassName"))
				.invoke(valueDto);

		} else {
			logger.error("Exception occured while Loading the transaction class ");
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

	}

}
