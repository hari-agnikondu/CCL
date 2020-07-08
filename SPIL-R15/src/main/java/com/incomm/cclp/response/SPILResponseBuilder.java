package com.incomm.cclp.response;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.netbeans.xml.schema.serviceProviderTxn.Extension;
import org.netbeans.xml.schema.serviceProviderTxn.ProductType;
import org.netbeans.xml.schema.serviceProviderTxn.RequestType;
import org.netbeans.xml.schema.serviceProviderTxn.ResponseType;
import org.netbeans.xml.schema.serviceProviderTxn.ServiceProviderTxnDocument;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.PurseAPIConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.domain.BulkTransactionResponseFile;
import com.incomm.cclp.message.parser.SPILRequestParser;
import com.incomm.cclp.util.Util;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SPILResponseBuilder {

	private ServiceProviderTxnDocument cclpAuthDoc;
	private ServiceProviderTxnDocument.ServiceProviderTxn cclpDocElement;

	private ResponseType cclpResElement;
	private String msXmlResponse;
	Logger logger = LogManager.getLogger(SPILResponseBuilder.class);

	public SPILResponseBuilder(SPILRequestParser reqParser) {
		this.cclpAuthDoc = ServiceProviderTxnDocument.Factory.newInstance();
		this.cclpDocElement = cclpAuthDoc.addNewServiceProviderTxn();
		this.cclpResElement = cclpDocElement.addNewResponse();
		this.cclpDocElement.setVersion(reqParser.getCclpDocElement()
			.getVersion());
	}

	public SPILResponseBuilder() {
		this.cclpAuthDoc = ServiceProviderTxnDocument.Factory.newInstance();
		this.cclpDocElement = cclpAuthDoc.addNewServiceProviderTxn();
		this.cclpResElement = cclpDocElement.addNewResponse();
	}

	/**
	 * 
	 * @param respCode
	 * @param respData
	 * @param hKeyValue
	 * @param requestXMLObject
	 */
	public void addResponseBody(String respCode, String respData, Map<String, String> hKeyValue, RequestType requestXMLObject) {

		if (requestXMLObject != null) {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setOriginalRequest(requestXMLObject);
		}
		cclpResElement.setRespCode(respCode);
		cclpResElement.setRespMsg(respData);

		this.setXmlResponse();
	}

	public void addResponseBody(String respCode, Map<String, String> hKeyValue, RequestType requestXMLObject) {

		cclpResElement.setMsgType(requestXMLObject.getMsgType());
		cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
		cclpResElement.setRespCode(respCode);
		cclpResElement.setOriginalRequest(requestXMLObject);
		this.setXmlResponse();
	}

	// Reponse for Card issuance
	public void addResponseBody(String responseCode, String respData, String authId, RequestType requestXMLObject) {
		cclpResElement.setMsgType(requestXMLObject.getMsgType());
		cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
		cclpResElement.setRespCode(responseCode);
		cclpResElement.setRespMsg(respData);
		if (authId != null && !authId.equals("0")) {
			cclpResElement.setServiceProviderRefNum(authId);
		}
		cclpResElement.setOriginalRequest(requestXMLObject);
		this.setXmlResponse();

	}

	public void addResponseBodyForactPreAuth(String responseCode, String respMsg, String authId, String respData, String currCode,
			RequestType requestXMLObject) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg);
			// Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(respData != null ? respData : "0");
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
			}

			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForactPreAuth:" + e);
		}

	}

	public void addResponseBodyForDeact(String responseCode, String respMsg, String authId, String respData, String currCode,
			RequestType requestXMLObject) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg);
			// Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(respData != null ? respData : "0");
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
			}

			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForactPreAuth:" + e);
		}

	}

	public void addResponseBodyForactRev(String responseCode, String respMsg, String authId, String respData, String cardCurrency,
			RequestType requestXMLObject, String purAuthResp, String purseBalance) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg);
			// Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(respData != null ? respData : "0");
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(cardCurrency);
			}
			if (purAuthResp != null && !purAuthResp.isEmpty()) {
				Extension extension = cclpResElement.addNewExtension();
				extension.setName(ValueObjectKeys.PURAUTHRESP);
				extension.setValue(purAuthResp);
			}
			if (!Util.isEmpty(purseBalance)) {
				Extension purextension = cclpResElement.addNewExtension();
				purextension.setName(ValueObjectKeys.PUR_BAL);
				purextension.setValue(purseBalance.replaceAll(",", ",\n"));
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception occured in addResponseBodyForactRev:" + e);
		}

	}

	public void addResponseBodyForActivation(String responseCode, String respMsg, String authId, String amount, String currCode,
			String cardStatus, RequestType requestXMLObject, String purAuthResp, String purseBalance) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg); // Msg will have success or error message of the txn processed
			if (!Util.isEmpty(authId) && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.setStatus(cardStatus);
				cclpResElement.getProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(amount);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
			}
			if (purAuthResp != null && !purAuthResp.isEmpty()) {
				Extension extension = cclpResElement.addNewExtension();
				extension.setName(ValueObjectKeys.PURAUTHRESP);
				extension.setValue(purAuthResp);
			}
			if (!Util.isEmpty(purseBalance)) {
				Extension purextension = cclpResElement.addNewExtension();
				purextension.setName(ValueObjectKeys.PUR_BAL);
				purextension.setValue(purseBalance.replaceAll(",", ",\n"));
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForActivation:" + e);
		}
	}

	public void addResponseBodyForBalInq(String responseCode, String respMsg, String authId, String amount, String currCode,
			String cardStatus, RequestType requestXMLObject, String purBal) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg); // Msg will have success or error message of the txn processed
			if (!Util.isEmpty(authId) && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.setStatus(cardStatus);
				cclpResElement.getProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(amount);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
			}
			if (!Util.isEmpty(purBal)) {
				Extension purextension = cclpResElement.addNewExtension();
				purextension.setName(ValueObjectKeys.PUR_BAL);
				purextension.setValue(purBal.replaceAll(",", ",\n"));
			}

			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForBalInq:" + e);
		}

	}

	public void addResponseBodyForCredit(String responseCode, String respMsg, String authId, String balance, String currCode,
			RequestType requestXMLObject, StringBuilder purseAuthResponseValue, String purbal, String authorizedAmt) {
		try {

			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg); // Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(balance);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
				cclpResElement.getProduct()
					.addNewAuthorizedAmount()
					.addNewMoney()
					.setAmount(authorizedAmt);
			}
			if (respMsg.equalsIgnoreCase(ValueObjectKeys.SUCCESS_MSG)) {
				if (purseAuthResponseValue != null) {
					Extension extension = cclpResElement.addNewExtension();
					extension.setName(PurseAPIConstants.PURSE_AUTH_RESP);
					extension.setValue(purseAuthResponseValue.toString());
				}
				if (!Util.isEmpty(purbal)) {
					Extension purextension = cclpResElement.addNewExtension();
					purextension.setName(PurseAPIConstants.PURSE_BALANCE);
					purextension.setValue(purbal.replaceAll(",", ",\n"));
				}
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();

		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForCardRedemption:" + e);
		}

	}

	public void addResponseBodyForCardRedemption(String responseCode, String respMsg, String authId, String balance, String authrisedAmt,
			String currCode, RequestType requestXMLObject, String purseAuthResponseValue, String purbal) {
		try {

			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg); // Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(balance);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
				cclpResElement.getProduct()
					.addNewAuthorizedAmount()
					.addNewMoney()
					.setAmount(authrisedAmt);
			}

			if (respMsg.equalsIgnoreCase(ValueObjectKeys.SUCCESS_MSG)) {

				if (purseAuthResponseValue != null && !Util.isEmpty(purseAuthResponseValue)) {
					Extension extension = cclpResElement.addNewExtension();
					extension.setName(PurseAPIConstants.PURSE_AUTH_RESP);
					extension.setValue((purseAuthResponseValue.replaceAll(",$", "")).replaceAll(",", ",\n"));
				}
				if (!Util.isEmpty(purbal)) {
					Extension purextension = cclpResElement.addNewExtension();
					purextension.setName(PurseAPIConstants.PURSE_BALANCE);
					purextension.setValue(purbal.replaceAll(",", ",\n"));
				}
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();

		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForCardRedemption:" + e);
		}

	}

	public void addResponseBodyForRedemptionUnlock(String responseCode, String respMsg, String authId, String balance, String authrisedAmt,
			String currCode, RequestType requestXMLObject) {
		try {

			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg); // Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(balance);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
				cclpResElement.getProduct()
					.addNewAuthorizedAmount()
					.addNewMoney()
					.setAmount(authrisedAmt);
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();

		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForRedemptionUnlock:" + e);
		}

	}

	public void addResponseBodyForCtoCtransfer(String responseCode, String sourceCardstat, String respMsg, String authId,
			String targetCardstat, String sourceBalAmt, String sourceCurrCode, String targetBalAmt, String targetCurrCode, String purbal,
			RequestType requestXMLObject) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg);
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.setStatus(sourceCardstat);
				cclpResElement.getProduct()
					.setTargetCardStatus(targetCardstat);
				cclpResElement.getProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(sourceBalAmt);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(sourceCurrCode);

				cclpResElement.getProduct()
					.addNewTargetCardBalance()
					.addNewMoney()
					.setAmount(targetBalAmt);
				cclpResElement.getProduct()
					.getTargetCardBalance()
					.getMoney()
					.setCurrencyCode(targetCurrCode);
			}
			if (!Util.isEmpty(purbal)) {
				Extension purextension = cclpResElement.addNewExtension();
				purextension.setName(PurseAPIConstants.PURSE_BALANCE);
				purextension.setValue(purbal.replaceAll(",", ",\n"));
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForCtoCtransfer:" + e);
		}

	}

	// Response for card reload
	public void addResponseBodyForReLoad(String responseCode, String respMsg, String balance, String authId,
			StringBuilder purseAuthResponseValue, String purbal, RequestType requestXMLObject, String currency, String authorizedAmt) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());

			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg); // Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(balance);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currency);
				/*
				 * cclpResElement.getProduct() .addNewAuthorizedAmount() .addNewMoney() .setAmount(authorizedAmt);
				 */
			}
			if (respMsg.equalsIgnoreCase(ValueObjectKeys.SUCCESS_MSG)) {
				if (purseAuthResponseValue != null) {
					Extension extension = cclpResElement.addNewExtension();
					extension.setName(PurseAPIConstants.PURSE_AUTH_RESP);
					extension.setValue(purseAuthResponseValue.toString());
				}
				if (!Util.isEmpty(purbal)) {
					Extension purextension = cclpResElement.addNewExtension();
					purextension.setName(PurseAPIConstants.PURSE_BALANCE);
					purextension.setValue(purbal.replaceAll(",", ",\n"));
				}
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForReLoad:" + e);
		}

	} // the below method is added to get desired response for Credit & Store Credit
		// Trans

	public void addedResponseForCreditAndStoreCredit(RequestType requestXMLObject, String... respFields) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(respFields[0]);
			cclpResElement.setRespMsg(respFields[1]);
			if (respFields[3] != null && !respFields[3].equals("0")) {
				cclpResElement.setServiceProviderRefNum(respFields[2]);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(respFields[3]);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(respFields[5]);
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();

		} catch (Exception e) {
			logger.error("Error occured in Response Method :" + e);
		}
	}
	// end

	/**
	 * 
	 * This Method to build response for Spil Cash Out Transaction
	 * 
	 */
	public void addResponseForCashOut(String responseCode, String respMsg, String authId, String balance, String currencycode,
			RequestType requestXMLObject) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg);
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(balance);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currencycode);
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();

		} catch (Exception e) {
			logger.error("Error occured in Response Method :" + e);
		}
	}

	// The Below Method is to get Desire Response for Redemption Lock transaction
	// Added By D.Dharma on 10.12.2013
	public void addedResponseForRedemptionLock(RequestType requestXMLObject, String... respFields) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(respFields[0]);
			cclpResElement.setRespMsg(respFields[1]);
			if (respFields[3] != null && !respFields[3].equals("0")) {
				cclpResElement.setServiceProviderRefNum(respFields[2]);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(respFields[3]);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(respFields[5]);
				cclpResElement.getProduct()
					.addNewAuthorizedAmount()
					.addNewMoney()
					.setAmount(respFields[4]);
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();

		} catch (Exception e) {
			logger.error("Exception in Response Method :" + e);
		}
	}
	// end

	public void addResponseBodyForTranHist(String responseCode, String respData, String respMsg, String authId, String txnHistory,
			String txnCount, String cardCurrency, RequestType requestXMLObject) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg);
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(respData);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(cardCurrency);
				cclpResElement.addNewHistory()
					.setTxnCount(txnCount);
				cclpResElement.getHistory()
					.setTxnHistory(txnHistory);

			}

			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForBalInq:" + e);
		}

	}

	public void addResponseBodyForTransactionHistory(String responseCode, String balance, String respMsg, String authId,
			String currencycode, String cardStatus, String recdCount, String history, RequestType requestXMLObject) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());

			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg); // Msg will have success or error message of the txn processed
			if (authId != null && !authId.equals("0")) {
				cclpResElement.setServiceProviderRefNum(authId);
				cclpResElement.addNewProduct()
					.setStatus(cardStatus);
				cclpResElement.getProduct()
					.addNewBalance()
					.addNewMoney()
					.setAmount(balance);
				cclpResElement.getProduct()
					.getBalance()
					.getMoney()
					.setCurrencyCode(currencycode);
				cclpResElement.addNewHistory()
					.setTxnCount(recdCount);
				cclpResElement.getHistory()
					.setTxnHistory(history == null ? " " : history);
			}
			cclpResElement.setOriginalRequest(requestXMLObject);
			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForTransactionHistory:" + e);
		}

	}

	public void addResponseBodyForSaleActiveCode(String responseCode, String respMsg, String authId, String amount, String currCode,
			String cardStatus, String productCode, String serialNumber, String pin, RequestType requestXMLObject) {
		try {
			cclpResElement.setMsgType(requestXMLObject.getMsgType());
			cclpResElement.setDateTimeInfo(requestXMLObject.getDateTimeInfo());
			cclpResElement.setRespCode(responseCode);
			cclpResElement.setRespMsg(respMsg);
			if (!Util.isEmpty(authId) && !"0".equals(authId)) {
				ProductType productType = cclpResElement.addNewProduct();
				productType.addNewBalance()
					.addNewMoney()
					.setAmount(amount);
				productType.getBalance()
					.getMoney()
					.setCurrencyCode(currCode);
				productType.setStatus(cardStatus);
				productType.setProductCode(productCode);
				productType.setPIN(pin);
				productType.setSPNumber(serialNumber);
			}

			cclpResElement.setOriginalRequest(requestXMLObject);

			this.setXmlResponse();
		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForSaleActiveCode:" + e);
		}
	}

	public void setXmlResponse() {

		Map<String, String> map = new HashMap<>();
		XmlOptions xmloption = new XmlOptions();
		map.put("http://www.w3.org/2001/XMLSchema-instance", "");
		xmloption.setUseDefaultNamespace();
		xmloption.setSavePrettyPrint();
		xmloption.setSavePrettyPrintIndent(4);
		xmloption.setLoadLineNumbers();

		this.msXmlResponse = cclpAuthDoc.xmlText(xmloption);

	}

	public String getXmlResponse() {
		return this.msXmlResponse;
	}

	/**
	 * This method populates the response object from the HashMap object (Key Value Pair) where Key will be the element
	 * name and value will be value for the element
	 * 
	 * @param hKeyValue -> HashMap object where Key will be the element name and value will be value for the Key
	 *                  Element.
	 */
	private void doPopulate(Map<String, String> hKeyValue) {
		Class<?> resBuilderClass = null;
		Set<Entry<String, String>> obj = null;
		Iterator<Entry<String, String>> itr = null;
		Entry<String, String> en = null;
		Method addRespElementMethod = null;
		try {

			resBuilderClass = Class.forName("org.netbeans.xml.schema.serviceProviderTxn.ResponseType");
			obj = hKeyValue.entrySet();
			itr = obj.iterator();
			while (itr.hasNext()) {
				en = itr.next();
				addRespElementMethod = resBuilderClass.getMethod("set" + en.getKey(), String.class); // Method name (key
																										// from
				// the hashmap object)
				// and parameter type
				// (String)
				if (en.getValue() == null) {
					// value is empty
				} else

					addRespElementMethod.invoke(cclpResElement, en.getValue());
			}
		} catch (Exception ex) {
			logger.error("Exception occured in doPopulate" + ex);
		}
	}

	/**
	 * Parse the request message using the request element object and populate in the response message object which
	 * needs to be sent to the caller in the XML message.
	 */
	@SuppressWarnings("unused")
	private void doPopulate(RequestType reqMessage, String parsingType) {

		try {
			Class<?> reqGetterClass = null;
			Class<?> resBuilderClass = null;
			Properties prop = new Properties();
			FileInputStream reader = null;
			String methodName = null;
			Method m = null;
			Method addRespElementMethod = null;
			reqGetterClass = Class.forName("org.netbeans.xml.schema.serviceProviderTxn.RequestType");
			resBuilderClass = Class.forName("org.netbeans.xml.schema.serviceProviderTxn.ResponseType");
			reader = new FileInputStream(parsingType);
			prop.load(reader);
			for (Enumeration<?> elements = prop.propertyNames(); elements.hasMoreElements();) {
				methodName = elements.nextElement()
					.toString();
				m = reqGetterClass.getMethod("get" + methodName);
				addRespElementMethod = resBuilderClass.getMethod("set" + methodName, String.class); // Method name and
				// parameter type
				addRespElementMethod.invoke(cclpResElement, m.invoke(reqMessage));// invoke
				// the
				// getter
				// method
				// on
				// the
				// request
				// object
				// and
				// pass
				// the
				// same
				// as
				// parameter
				// to
				// the
				// response
				// object

			}
		} catch (IllegalAccessException | IOException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException ex) {
			logger.error("Exception in doPopulate" + ex);
		}
	}

	public BulkTransactionResponseFile addResponseBodyForActivationBatch(String responseCode, String respMsg, String authId, String amount,
			String currCode, String cardStatus, Map<String, String> hKeyValue) {

		BulkTransactionResponseFile bulkTxnResponseFile = null;
		amount = (!Util.isEmpty(amount)) ? amount
				: (!Util.isEmpty(hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)) ? hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)
						: "0.00");

		cardStatus = (!Util.isEmpty(cardStatus)) ? cardStatus : hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT);

		try {
			bulkTxnResponseFile = new BulkTransactionResponseFile();
			bulkTxnResponseFile.setAmount(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_AMT));
			bulkTxnResponseFile.setSourceReferenceNumber(hKeyValue.get(ValueObjectKeys.SOURCE_REF_NUMBER));
			bulkTxnResponseFile.setBatchId(hKeyValue.get(ValueObjectKeys.BATCHID));
			bulkTxnResponseFile.setCardStatus(cardStatus);
			bulkTxnResponseFile.setTransactionDate(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_DATE));
			bulkTxnResponseFile.setTransactionTime(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_TIME));
			bulkTxnResponseFile.setAvailableBalance(amount);
			bulkTxnResponseFile.setMdmId(hKeyValue.get(ValueObjectKeys.MDM_ID));
			bulkTxnResponseFile.setStoreId(hKeyValue.get(ValueObjectKeys.STORE_ID));
			bulkTxnResponseFile.setTerminalId(hKeyValue.get(ValueObjectKeys.SPIL_TERM_ID));
			bulkTxnResponseFile.setResponseCode(responseCode);
			bulkTxnResponseFile.setResponseMessage(respMsg);
			bulkTxnResponseFile.setSpNumber(hKeyValue.get(ValueObjectKeys.SPNUMBER));
			bulkTxnResponseFile.setTransactionDesc(hKeyValue.get(ValueObjectKeys.TRANSACTIONDESC));
			bulkTxnResponseFile.setRecordNum(hKeyValue.get(ValueObjectKeys.SPIL_BLK_TXN_RECORD_NUM));

		} catch (Exception e) {
			logger.error("Exception occured in addResponseBodyForActivationBatch:" + e);
		}
		return bulkTxnResponseFile;

	}

	public BulkTransactionResponseFile addResponseBodyForBalInqBatch(String responseCode, String respMsg, String authId, String amount,
			String currCode, String cardStatus, Map<String, String> hKeyValue) {

		BulkTransactionResponseFile bulkTxnResponseFile = null;
		amount = (!Util.isEmpty(amount)) ? amount
				: (!Util.isEmpty(hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)) ? hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)
						: "0.00");

		cardStatus = (!Util.isEmpty(cardStatus)) ? cardStatus : hKeyValue.get(ValueObjectKeys.CARD_CARDSTAT);

		try {
			bulkTxnResponseFile = new BulkTransactionResponseFile();
			bulkTxnResponseFile.setAmount(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_AMT));
			bulkTxnResponseFile.setSourceReferenceNumber(hKeyValue.get(ValueObjectKeys.SOURCE_REF_NUMBER));
			bulkTxnResponseFile.setBatchId(hKeyValue.get(ValueObjectKeys.BATCHID));
			bulkTxnResponseFile.setCardStatus(cardStatus);
			bulkTxnResponseFile.setTransactionDate(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_DATE));
			bulkTxnResponseFile.setTransactionTime(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_TIME));
			bulkTxnResponseFile.setAvailableBalance(amount);
			bulkTxnResponseFile.setMdmId(hKeyValue.get(ValueObjectKeys.MDM_ID));
			bulkTxnResponseFile.setStoreId(hKeyValue.get(ValueObjectKeys.STORE_ID));
			bulkTxnResponseFile.setTerminalId(hKeyValue.get(ValueObjectKeys.SPIL_TERM_ID));
			bulkTxnResponseFile.setResponseCode(responseCode);
			bulkTxnResponseFile.setResponseMessage(respMsg);
			bulkTxnResponseFile.setSpNumber(hKeyValue.get(ValueObjectKeys.SPNUMBER));
			bulkTxnResponseFile.setTransactionDesc(hKeyValue.get(ValueObjectKeys.TRANSACTIONDESC));
			bulkTxnResponseFile.setRecordNum(hKeyValue.get(ValueObjectKeys.SPIL_BLK_TXN_RECORD_NUM));

		} catch (Exception e) {
			logger.error("Exception occured in addResponseBodyForBalInqBatch:" + e);
		}
		return bulkTxnResponseFile;
	}

	public BulkTransactionResponseFile addResponseBodyForDeactBatch(String responseCode, String respMsg, String authId, String respData,
			String currCode, Map<String, String> hKeyValue, String cardStatus, String oldCardStatusDesc) {

		BulkTransactionResponseFile bulkTxnResponseFile = null;

		if (responseCode.equalsIgnoreCase(ResponseCodes.SUCCESS_EXT_RESPONSE)) {
			cardStatus = oldCardStatusDesc;
		}
		String amount = (!Util.isEmpty(hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)) ? hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)
				: "0.00");

		try {
			bulkTxnResponseFile = new BulkTransactionResponseFile();
			bulkTxnResponseFile.setAmount(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_AMT));
			bulkTxnResponseFile.setSourceReferenceNumber(hKeyValue.get(ValueObjectKeys.SOURCE_REF_NUMBER));
			bulkTxnResponseFile.setBatchId(hKeyValue.get(ValueObjectKeys.BATCHID));
			bulkTxnResponseFile.setCardStatus(cardStatus);
			bulkTxnResponseFile.setTransactionDate(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_DATE));
			bulkTxnResponseFile.setTransactionTime(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_TIME));
			bulkTxnResponseFile.setAvailableBalance(amount);
			bulkTxnResponseFile.setMdmId(hKeyValue.get(ValueObjectKeys.MDM_ID));
			bulkTxnResponseFile.setStoreId(hKeyValue.get(ValueObjectKeys.STORE_ID));
			bulkTxnResponseFile.setTerminalId(hKeyValue.get(ValueObjectKeys.SPIL_TERM_ID));
			bulkTxnResponseFile.setResponseCode(responseCode);
			bulkTxnResponseFile.setResponseMessage(respMsg);
			bulkTxnResponseFile.setSpNumber(hKeyValue.get(ValueObjectKeys.SPNUMBER));
			bulkTxnResponseFile.setTransactionDesc(hKeyValue.get(ValueObjectKeys.TRANSACTIONDESC));
			bulkTxnResponseFile.setRecordNum(hKeyValue.get(ValueObjectKeys.SPIL_BLK_TXN_RECORD_NUM));

		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForactPreAuth:" + e);
		}
		return bulkTxnResponseFile;
	}

	public BulkTransactionResponseFile addResponseBodyForReLoadBatch(String responseCode, String respMsg, String amount, String authId,
			Map<String, String> hKeyValue, String cardStatus) {

		BulkTransactionResponseFile bulkTxnResponseFile = null;
		amount = (!Util.isEmpty(amount)) ? amount
				: (!Util.isEmpty(hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)) ? hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)
						: "0.00");

		try {
			bulkTxnResponseFile = new BulkTransactionResponseFile();
			bulkTxnResponseFile.setAmount(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_AMT));
			bulkTxnResponseFile.setSourceReferenceNumber(hKeyValue.get(ValueObjectKeys.SOURCE_REF_NUMBER));
			bulkTxnResponseFile.setBatchId(hKeyValue.get(ValueObjectKeys.BATCHID));
			bulkTxnResponseFile.setCardStatus(cardStatus);
			bulkTxnResponseFile.setTransactionDate(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_DATE));
			bulkTxnResponseFile.setTransactionTime(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_TIME));
			bulkTxnResponseFile.setAvailableBalance(amount);
			bulkTxnResponseFile.setMdmId(hKeyValue.get(ValueObjectKeys.MDM_ID));
			bulkTxnResponseFile.setStoreId(hKeyValue.get(ValueObjectKeys.STORE_ID));
			bulkTxnResponseFile.setTerminalId(hKeyValue.get(ValueObjectKeys.SPIL_TERM_ID));
			bulkTxnResponseFile.setResponseCode(responseCode);
			bulkTxnResponseFile.setResponseMessage(respMsg);
			bulkTxnResponseFile.setSpNumber(hKeyValue.get(ValueObjectKeys.SPNUMBER));
			bulkTxnResponseFile.setTransactionDesc(hKeyValue.get(ValueObjectKeys.TRANSACTIONDESC));
			bulkTxnResponseFile.setRecordNum(hKeyValue.get(ValueObjectKeys.SPIL_BLK_TXN_RECORD_NUM));

		} catch (Exception e) {
			logger.error("Exception in addResponseBodyForReLoadBatch:" + e);
		}
		return bulkTxnResponseFile;
	}

	// Reponse for Card issuance
	public BulkTransactionResponseFile addResponseBodyBatch(String responseCode, String respMsg, String authId,
			Map<String, String> hKeyValue, String cardStatus) {

		BulkTransactionResponseFile bulkTxnResponseFile = null;
		String amount = (!Util.isEmpty(hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)) ? hKeyValue.get(ValueObjectKeys.AVAILABLE_BALACE)
				: "0.00");

		try {
			bulkTxnResponseFile = new BulkTransactionResponseFile();
			bulkTxnResponseFile.setAmount(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_AMT));
			bulkTxnResponseFile.setSourceReferenceNumber(hKeyValue.get(ValueObjectKeys.SOURCE_REF_NUMBER));
			bulkTxnResponseFile.setBatchId(hKeyValue.get(ValueObjectKeys.BATCHID));
			bulkTxnResponseFile.setCardStatus(cardStatus);
			bulkTxnResponseFile.setTransactionDate(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_DATE));
			bulkTxnResponseFile.setTransactionTime(hKeyValue.get(ValueObjectKeys.SPIL_TRAN_TIME));
			bulkTxnResponseFile.setAvailableBalance(amount);
			bulkTxnResponseFile.setMdmId(hKeyValue.get(ValueObjectKeys.MDM_ID));
			bulkTxnResponseFile.setStoreId(hKeyValue.get(ValueObjectKeys.STORE_ID));
			bulkTxnResponseFile.setTerminalId(hKeyValue.get(ValueObjectKeys.SPIL_TERM_ID));
			bulkTxnResponseFile.setResponseCode(responseCode);
			bulkTxnResponseFile.setResponseMessage(respMsg);
			bulkTxnResponseFile.setSpNumber(hKeyValue.get(ValueObjectKeys.SPNUMBER));
			bulkTxnResponseFile.setTransactionDesc(hKeyValue.get(ValueObjectKeys.TRANSACTIONDESC));
			bulkTxnResponseFile.setRecordNum(hKeyValue.get(ValueObjectKeys.SPIL_BLK_TXN_RECORD_NUM));

		} catch (Exception e) {
			logger.error("Exception in addResponseBodyBatch:" + e);
		}
		return bulkTxnResponseFile;
	}

	public void cleanMemory() {
		cclpAuthDoc = null;
		cclpDocElement = null;
		cclpResElement = null;
		msXmlResponse = null;

	}

	public Map<String, Object> pingSuccessResponse(String code, String message, String strDate, boolean hostId, String hostName) {
		Map<String, Object> response = new HashMap<>();

		response.put("responseCode", code);
		response.put("responseMessage", message);
		if (!Util.isEmpty(strDate))
			response.put("dateTime", strDate);
		if ("00".equals(code) && !hostId)
			response.put("hostIdentifier", hostName.hashCode());

		return response;
	}
}
