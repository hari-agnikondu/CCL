package com.incomm.cclp.message.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.netbeans.xml.schema.serviceProviderTxn.Extension;
import org.netbeans.xml.schema.serviceProviderTxn.RequestType;
import org.netbeans.xml.schema.serviceProviderTxn.ServiceProviderTxnDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.util.Util;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SPILRequestParser {

	private Map<String, String> msHashMapBodyXMLMsg = new HashMap<>(50);
	private Map<String, String> msHashMapHeaderXMLMsg = new HashMap<>(10);
	private ServiceProviderTxnDocument cclpAuthDoc;
	private ServiceProviderTxnDocument.ServiceProviderTxn cclpDocElement;

	private RequestType cclpReqElement;

	Logger logger = LogManager.getLogger(SPILRequestParser.class);

	Map<String, String> valueObject = new HashMap<>();

	@Autowired
	TransactionService transactionService;

	/**
	 * @param msXMLMsg
	 * @param propertyPath
	 * @return valueObject
	 * @throws ServiceException
	 */
	public Map<String, String> parse(String msXMLMsg, Map<String, String> valueObj) throws ServiceException {
		try {

			cclpAuthDoc = ServiceProviderTxnDocument.Factory.parse(msXMLMsg);
			cclpDocElement = cclpAuthDoc.getServiceProviderTxn();
			cclpReqElement = cclpDocElement.getRequest();

			doPopulate(valueObj);

			valueObject = getBodyHashMap();

		} catch (ServiceException servExec) {
			throw servExec;

		} catch (XmlException xmle) {

			cleanMemory();
			throw new ServiceException(SpilExceptionMessages.REQPARSING_EXEC, ResponseCodes.INVALID_REQUEST, xmle);

		} catch (IllegalArgumentException | SecurityException e) {

			throw new ServiceException(SpilExceptionMessages.REQPARSING_EXEC, ResponseCodes.INVALID_REQUEST, e);

		} catch (Exception e) {

			throw new ServiceException(e.getMessage(), ResponseCodes.INVALID_REQUEST, e);

		}
		return valueObject;
	}

	public ServiceProviderTxnDocument.ServiceProviderTxn getCclpDocElement() {
		return cclpDocElement;
	}

	public void setCclpDocElement(ServiceProviderTxnDocument.ServiceProviderTxn cclpDocElement) {
		this.cclpDocElement = cclpDocElement;
	}

	public Map<String, String> parseMasterData(String msXMLMsg) throws ServiceException {

		try {

			cclpAuthDoc = ServiceProviderTxnDocument.Factory.parse(msXMLMsg);
			cclpDocElement = cclpAuthDoc.getServiceProviderTxn();
			cclpReqElement = cclpDocElement.getRequest();
			doPopulateMasterData();
			valueObject = getMastDataHashMap();

		} catch (XmlException xmle) {

			logger.error("Error occured in parsing master data: " + xmle.getMessage(), xmle);
			throw new ServiceException(SpilExceptionMessages.REQPARSING_MASTERDATA_EXEC, ResponseCodes.INVALID_REQUEST, xmle);

		} catch (ServiceException servExec) {
			logger.error("Error occured in parsing master data: " + servExec.getMessage(), servExec);
			throw servExec;

		} catch (Exception e) {

			logger.error("Error occured in parsing master data: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.REQPARSING_MASTERDATA_EXEC, ResponseCodes.INVALID_REQUEST, e);

		}

		return valueObject;
	}

	/**
	 * Populates the hashmap object with the respective request elements excluding the message header elements which are
	 * taken from the property file.
	 */
	public void doPopulate(Map<String, String> valueObj) throws ServiceException {
		try {

			Class<?> reqParserClass = Class.forName("com.incomm.cclp.message.parser.SPILRequestParser");
			List<TransactionInputValidation> inputValidationList = transactionService.getInputValidations(valueObj);
			if (inputValidationList != null && !inputValidationList.isEmpty()) {
				String methodName = null;
				for (Iterator<TransactionInputValidation> iterator = inputValidationList.iterator(); iterator.hasNext();) {
					TransactionInputValidation transactionInputValidation = iterator.next();
					methodName = transactionInputValidation.getFieldName();
					Method m = reqParserClass.getMethod("get" + methodName);
					String tempObject = (String) m.invoke(this);
					if (tempObject != null) {
						this.msHashMapBodyXMLMsg.put(methodName, tempObject);
					}

				}
			}

			getExtensionValues();

		} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException
				| ClassNotFoundException ex) {

			logger.error("Error in SPILRequestParser::dopopulate() ", ex.getMessage(), ex);
			throw new ServiceException(SpilExceptionMessages.REQPARSING_EXEC, ResponseCodes.INVALID_REQUEST, ex);

		} catch (InvocationTargetException ex) {
			if (ex.getCause() instanceof ServiceException) {

				throw (ServiceException) ex.getCause();
			}

			logger.error("Error in SPILRequestParser::dopopulate() ", ex.getMessage(), ex);
			throw new ServiceException(SpilExceptionMessages.REQPARSING_EXEC, ResponseCodes.INVALID_REQUEST, ex);

		} catch (Exception ex) {

			logger.error("Error in SPILRequestParser::dopopulate() ", ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage(), ResponseCodes.INVALID_REQUEST, ex);

		}

	}

	/**
	 * Populates the Card number and Transaction code from the XML message
	 */
	public void doPopulateMasterData() throws ServiceException {
		try {
			Class<?> reqParserClass = Class.forName("com.incomm.cclp.message.parser.SPILRequestParser");
			Method m1 = reqParserClass.getMethod("get" + ValueObjectKeys.MSGTYPE);
			String tempObjectMsgType = (String) m1.invoke(this);

			if (tempObjectMsgType == null)
				throw new ServiceException("Required Property Message type  Not Present", ResponseCodes.INVALID_REQUEST);
			this.msHashMapBodyXMLMsg.put(ValueObjectKeys.MSGTYPE, tempObjectMsgType);

			Method m2 = reqParserClass.getMethod("get" + ValueObjectKeys.SPNUMBER);
			String tempObjectSpNum = (String) m2.invoke(this);

			if (tempObjectSpNum == null)
				throw new ServiceException("Required Property SPNUMBER  Not Present", ResponseCodes.INVALID_REQUEST);
			this.msHashMapBodyXMLMsg.put(ValueObjectKeys.SPNUMBER, tempObjectSpNum);

			Method m3 = reqParserClass.getMethod("get" + ValueObjectKeys.INCOM_REF_NUMBER);
			String tempObjectIncomRefNum = (String) m3.invoke(this);

			if (tempObjectIncomRefNum == null)
				throw new ServiceException("Required Property IncommRefNum  Not Present", ResponseCodes.INVALID_REQUEST);
			this.msHashMapBodyXMLMsg.put(ValueObjectKeys.INCOM_REF_NUMBER, tempObjectIncomRefNum);

			// VMSCL-818
			Method m4 = reqParserClass.getMethod("get" + ValueObjectKeys.SOURCE_INFO);
			String tempObjectSourceInfo = (String) m4.invoke(this);
			this.msHashMapBodyXMLMsg.put(ValueObjectKeys.SOURCE_INFO, tempObjectSourceInfo);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException ex) {

			logger.error("Error occured in populating master data" + ex.getMessage(), ex);
			throw new ServiceException(SpilExceptionMessages.REQPARSING_MASTERDATA_EXEC, ResponseCodes.INVALID_REQUEST, ex);

		} catch (ServiceException ex) {
			logger.error("Error occured in populating master data" + ex.getMessage(), ex);
			throw ex;

		} catch (Exception ex) {
			logger.error("Error occured in populating master data" + ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage(), ResponseCodes.INVALID_REQUEST, ex);

		}
	}

	/**
	 * HashMap of key value pair for the XML message recieved after parsing is successful where Key is the field name of
	 * the message and value is in XmlString type
	 * 
	 * @return
	 */

	public Map<String, String> getHeaderHashMap() {
		return this.msHashMapHeaderXMLMsg;
	}

	public Map<String, String> getBodyHashMap() {
		return this.msHashMapBodyXMLMsg;
	}

	public Map<String, String> getMastDataHashMap() {
		return this.msHashMapBodyXMLMsg;
	}

	// Starting of Header Fields

	public String getIncommRefNum() {
		return this.cclpReqElement.getIncommRefNum();
	}

	public String getMsgType() {
		return this.cclpReqElement.getMsgType();
	}

	public String getDateTimeInfo() {
		if (this.cclpReqElement.getDateTimeInfo() == null)
			return null;
		else
			return this.cclpReqElement.getDateTimeInfo()
				.toString();
	}

	public String getMIN() {
		return this.cclpReqElement.getProduct()
			.getMIN();
	}

	public String getDate() {

		return this.cclpReqElement.getDateTimeInfo()
			.getDate();

	}

	public String getTime() {
		return this.cclpReqElement.getDateTimeInfo()
			.getTime();

	}

	public String getAmount() {

		// Amount is optional for cashout, balance inquiry,
		// transaction history, balance transfer and cardtocard transfer.

		if (this.cclpReqElement.getProduct()
			.getValue() == null
				|| this.cclpReqElement.getProduct()
					.getValue()
					.getMoney() == null
				|| this.cclpReqElement.getProduct()
					.getValue()
					.getMoney()
					.getAmount() == null)
			return "0";

		return this.cclpReqElement.getProduct()
			.getValue()
			.getMoney()
			.getAmount();

	}

	public String getProduct() {
		return this.cclpReqElement.getProduct()
			.toString();

	}

	public String getTrack2() {
		return this.cclpReqElement.getProduct()
			.getTrack2();

	}

	public String getCurrencyCode() {

		if (this.cclpReqElement.getProduct() == null || this.cclpReqElement.getProduct()
			.getValue() == null || this.cclpReqElement.getProduct()
				.getValue()
				.getMoney() == null || this.cclpReqElement.getProduct()
					.getValue()
					.getMoney()
					.getCurrencyCode() == null)
			return null;
		return this.cclpReqElement.getProduct()
			.getValue()
			.getMoney()
			.getCurrencyCode();
	}

	public String getTermID() {
		return this.cclpReqElement.getOrigin()
			.getStoreInfo()
			.getTermID();
	}

	public String getTimeZone() {
		return this.cclpReqElement.getDateTimeInfo()
			.getTimeZone();
	}

	public String getMerchName() {
		return this.cclpReqElement.getOrigin()
			.getMerchName();

	}

	public String getStoreID() {
		return this.cclpReqElement.getOrigin()
			.getStoreInfo()
			.getStoreID();

	}

	public String getMerchRefNum() {
		return this.cclpReqElement.getOrigin()
			.getMerchRefNum();

	}

	public String getLanguage() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getLocaleInfo() == null || this.cclpReqElement.getOrigin()
				.getLocaleInfo()
				.getLanguage() == null)
			return null;
		return this.cclpReqElement.getOrigin()
			.getLocaleInfo()
			.getLanguage();
	}

	public String getUserID() {

		if (this.cclpReqElement.getAuthInfo() == null || this.cclpReqElement.getAuthInfo()
			.getUserID() == null)
			return null;
		else
			return this.cclpReqElement.getAuthInfo()
				.getUserID();
	}

	public String getPassword() {
		if (this.cclpReqElement.getAuthInfo() == null || this.cclpReqElement.getAuthInfo()
			.getPassword() == null)
			return null;
		else
			return this.cclpReqElement.getAuthInfo()
				.getPassword();
	}

	public String getSeqNum() {
		return this.cclpReqElement.getAuthInfo()
			.getSeqNum();

	}

	public String getSourceIP() {
		return this.cclpReqElement.getAuthInfo()
			.getSourceIP();

	}

	public String getUPC() {
		if (this.cclpReqElement.getProduct() == null || this.cclpReqElement.getProduct()
			.getUPC() == null)
			return null;
		else
			return this.cclpReqElement.getProduct()
				.getUPC();
	}

	public String getTrack1() {
		if (this.cclpReqElement.getProduct() == null || this.cclpReqElement.getProduct()
			.getTrack1() == null)
			return null;
		else
			return this.cclpReqElement.getProduct()
				.getTrack1();
	}

	public String getProductId() {
		if (this.cclpReqElement.getProduct() == null || this.cclpReqElement.getProduct()
			.getProductId() == null)
			return null;
		else
			return this.cclpReqElement.getProduct()
				.getProductId();
	}

	public String getFee() {
		if (this.cclpReqElement.getProduct() == null || this.cclpReqElement.getProduct()
			.getFee() == null)
			return "0";
		else {
			String fee = "";
			fee = this.cclpReqElement.getProduct()
				.getFee();
			fee = (Util.isEmpty(fee) ? "0" : fee);
			return fee;
		}
	}

	public String getTargetCardNumber() {
		return this.cclpReqElement.getProduct()
			.getTargetCardNumber();

	}

	public String getPIN() {
		if (this.cclpReqElement.getProduct() == null || this.cclpReqElement.getProduct()
			.getPIN() == null)
			return null;
		else
			return this.cclpReqElement.getProduct()
				.getPIN();
	}

	public String getEntryMode() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getPOSInfo() == null || this.cclpReqElement.getOrigin()
				.getPOSInfo()
				.getEntryMode() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getPOSInfo()
				.getEntryMode();

	}

	public String getConditionCode() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getPOSInfo() == null || this.cclpReqElement.getOrigin()
				.getPOSInfo()
				.getConditionCode() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getPOSInfo()
				.getConditionCode();
	}

	public String getTxnCount() {

		if (this.cclpReqElement.getHistory() == null || this.cclpReqElement.getHistory()
			.getTxnCount() == null)
			return null;
		else
			return this.cclpReqElement.getHistory()
				.getTxnCount();
	}

	public String getStartDate() {
		if (this.cclpReqElement.getHistory() == null || this.cclpReqElement.getHistory()
			.getStartDate() == null)
			return null;
		else
			return this.cclpReqElement.getHistory()
				.getStartDate();
	}

	public String getEndDate() {

		if (this.cclpReqElement.getHistory() == null || this.cclpReqElement.getHistory()
			.getEndDate() == null)
			return null;
		else
			return this.cclpReqElement.getHistory()
				.getEndDate();

	}

	public String getCountryCode() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getLocaleInfo() == null || this.cclpReqElement.getOrigin()
				.getLocaleInfo()
				.getCountryCode() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getLocaleInfo()
				.getCountryCode();
	}

	public String getSPNumber() {
		return this.cclpReqElement.getProduct()
			.getSPNumber();
	}

	public String getVersion() {

		return cclpDocElement.getVersion();
	}

	public String getOrginDate() {

		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getDateTimeInfo() == null || this.cclpReqElement.getOrigin()
				.getDateTimeInfo()
				.getDate() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getDateTimeInfo()
				.getDate();
	}

	public String getOrginTime() {

		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getDateTimeInfo() == null || this.cclpReqElement.getOrigin()
				.getDateTimeInfo()
				.getTime() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getDateTimeInfo()
				.getTime();

	}

	public String getOrginTimeZone() {

		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getDateTimeInfo() == null || this.cclpReqElement.getOrigin()
				.getDateTimeInfo()
				.getTimeZone() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getDateTimeInfo()
				.getTimeZone();
	}

	public void cleanMemory() {
		this.msHashMapBodyXMLMsg = null;
		this.msHashMapHeaderXMLMsg = null;
		this.cclpAuthDoc = null;
		this.cclpDocElement = null;
		this.cclpReqElement = null;
	}

	public RequestType getCclpReqElement() {
		return cclpReqElement;
	}

	public void setCclpReqElement(RequestType cclpReqElement) {
		this.cclpReqElement = cclpReqElement;
	}

	public String getAddress1() {

		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getStoreInfo() == null || this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc() == null || this.cclpReqElement.getOrigin()
					.getStoreInfo()
					.getStoreLoc()
					.getAddress1() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc()
				.getAddress1();
	}

	public String getAddress2() {

		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getStoreInfo() == null || this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc() == null || this.cclpReqElement.getOrigin()
					.getStoreInfo()
					.getStoreLoc()
					.getAddress2() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc()
				.getAddress2();
	}

	public String getCity() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getStoreInfo() == null || this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc() == null || this.cclpReqElement.getOrigin()
					.getStoreInfo()
					.getStoreLoc()
					.getCity() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc()
				.getCity();
	}

	public String getState() {

		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getStoreInfo() == null || this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc() == null || this.cclpReqElement.getOrigin()
					.getStoreInfo()
					.getStoreLoc()
					.getState() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc()
				.getState();
	}

	public String getZip() {

		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getStoreInfo() == null || this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc() == null || this.cclpReqElement.getOrigin()
					.getStoreInfo()
					.getStoreLoc()
					.getZip() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getStoreInfo()
				.getStoreLoc()
				.getZip();
	}

	public String getLocaleCurrencyCode() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getLocaleInfo() == null || this.cclpReqElement.getOrigin()
				.getLocaleInfo()
				.getCurrencyCode() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getLocaleInfo()
				.getCurrencyCode();
	}

	public String getSourceInfo() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getSourceInfo() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getSourceInfo();
	}

	/**
	 * Getting the Extension values to valueObj
	 * 
	 * @throws ServiceException
	 */
	public void getExtensionValues() throws ServiceException {
		try {
			Extension[] extensionTag = this.cclpReqElement.getExtensionArray();
			if (extensionTag != null && extensionTag.length > 0) {
				for (int i = 0; i < extensionTag.length; i++) {
					this.msHashMapBodyXMLMsg.put(extensionTag[i].getName(), extensionTag[i].getValue());
					if (ValueObjectKeys.CLCVV.equals(extensionTag[i].getName()))
						this.msHashMapBodyXMLMsg.put(ValueObjectKeys.CVV2, extensionTag[i].getValue());

					if (ValueObjectKeys.PURAUTHREQ.equals(extensionTag[i].getName())) {
						String[] purseFields = extensionTag[i].getValue()
							.split("\\|", 5);
						this.msHashMapBodyXMLMsg.put(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID, purseFields[0]);
						this.msHashMapBodyXMLMsg.put(ValueObjectKeys.PURAUTHREQ_PURSE_NAME, purseFields[1]);
						this.msHashMapBodyXMLMsg.put(ValueObjectKeys.PURAUTHREQ_TRAN_AMT, purseFields[2]);
						this.msHashMapBodyXMLMsg.put(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY, purseFields[3]);
						this.msHashMapBodyXMLMsg.put(ValueObjectKeys.PURAUTHREQ_SKUCODE, purseFields[4]);

					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(SpilExceptionMessages.REQPARSING_EXEC, ResponseCodes.INVALID_REQUEST, e);
		}
	}

	// Empty declaration of Extension tags Starts
	public String getexpDate() {
		return null;

	}

	public String getcashierID() {
		return null;

	}

	public String getmerchantdbid() {
		return null;

	}

	public String getStoredbid() {
		return null;

	}

	public String getCVV2() {
		return null;

	}

	public String getConsumedstatus() {
		return null;

	}

	public String getPurAuthReq() {
		return null;

	}

	// Empty declaration of Extension tags Ends

	public String getreqAccountPurseId() {
		return null;

	}

	public String getpurseName() {
		return null;

	}

	public String getpurseCurrency() {
		return null;

	}

	public String getpurseTranAmt() {
		return null;

	}

	public String getskuCode() {
		return null;

	}

	public String getRetailPartnerReferenceNumber() {
		if (this.cclpReqElement.getOrigin() == null || this.cclpReqElement.getOrigin()
			.getRetailPartnerReferenceNumber() == null)
			return null;
		else
			return this.cclpReqElement.getOrigin()
				.getRetailPartnerReferenceNumber();
	}

}
