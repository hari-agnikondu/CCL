/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.application.service.AccountApplicationService;
import com.incomm.cclp.account.domain.validator.CardValidator;
import com.incomm.cclp.constants.APIConstants;
import com.incomm.cclp.constants.CardStatusConstants;
import com.incomm.cclp.constants.DeliveryChannelConstants;
import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ProductAttributesConstants;
import com.incomm.cclp.constants.PurseAPIConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CardDetailsDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.AccountPurseDTO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.gpp.security.AES;
import com.incomm.cclp.gpp.security.APISecurityException;
import com.incomm.cclp.message.parser.SPILRequestParser;
import com.incomm.cclp.response.SPILResponseBuilder;
import com.incomm.cclp.service.CardDetailsService;
import com.incomm.cclp.service.CommonValidationsService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.service.RuleEngineService;
import com.incomm.cclp.service.SpilCommonService;
import com.incomm.cclp.service.SpilService;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.transaction.validation.AuthorizationValidation;
import com.incomm.cclp.util.AsyncServiceCall;
import com.incomm.cclp.util.LoggerUtil;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationUtil;
import com.incomm.cclp.util.XSSValidator;
import com.incomm.cclp.validation.SPILDataElementsValidation;

/**
 * Spil Service provides all the Service operations for Spil Transactions.
 * 
 * @author venkateshgaddam
 */

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SpilServiceImpl implements SpilService {

	@Autowired
	private SPILRequestParser reqParser;

	@Autowired
	CommonValidationsService commonValidationService;

	@Autowired
	private SpilDAO spilDAO;

	@Autowired
	SPILDataElementsValidation dataElementsValidation;

	@Autowired
	CardDetailsService cardDetailsService;

	@Autowired
	ProductService productService;

	@Autowired
	private SPILResponseBuilder responseBuilder;

	@Autowired
	LocalCacheServiceImpl localCacheService;

	@Value("${SPIL_USERID}")
	private String spilUserId;

	@Value("${SPIL_PASSWORD}")
	private String spilPassword;

	@Autowired
	TransactionService transactionService;

	@Autowired
	AuthorizationValidation authorizationValidation;

	@Autowired
	RuleEngineService ruleEngineService;

	@Autowired
	private Map<String, SpilCommonService> spilCommonService;

	@Autowired
	AsyncServiceCall asyncServiceCall;

	@Autowired
	ValidationUtil validationUtil;

	@Autowired
	private LoggerUtil loggerUtil;

	@Autowired
	private XSSValidator xssValidator;

	@Autowired
	private CardDetailsDAO cardDetailsDao;

	@Autowired
	private BalanceTransferServiceImpl balanceTransferServiceImpl;

	@Autowired
	CardValidator cardValidator;

	@Value("${CCA_USERID}")
	private String ccaUserId;

	@Value("${CCA_PASSWORD}")
	private String ccaPassword;

	@Value("${FSAPI_ENCRYPTION_DECRYPTION_KEY}")
	private String fsapikey;

	@Autowired
	private AccountApplicationService accountApplicationService;

	private static final Logger logger = LogManager.getLogger(SpilServiceImpl.class);

	/**
	 * calling spil specific transaction flow
	 * 
	 * @throws SQLException
	 */

	public String callSPILTransaction(String xmlMsg) throws ServiceException {
		logger.debug("Entered");
		long timeBeforeTransaction = System.currentTimeMillis();
		Map<String, String> valueObj = new HashMap<>(50);
		ValueDTO valueDto = new ValueDTO();
		String respCode = null;
		String txnResponse = null;
		Map<String, String> transMap = new HashMap<>();
		Map<String, Map<String, Object>> productAttributes = null;
		String[] response = null;
		long daoProcessTime = 0;
		logger.info("Transaction start time: {}", timeBeforeTransaction);

		xmlMsg = xmlMsg != null ? xmlMsg.trim() : "";
		xmlMsg = xmlMsg.replace("<ServiceProviderTxn>",
				"<ServiceProviderTxn  xmlns=\"http://xml.netbeans.org/schema/ServiceProviderTxn\">");
		try {

			valueObj = reqParser.parseMasterData(xmlMsg);
			valueObj.put(ValueObjectKeys.ORIGINAL_MSGTYPE, valueObj.get(ValueObjectKeys.MSGTYPE));

			SpilStartupMsgTypeBean msgTypeBean = null;

			if (DeliveryChannelConstants.DELIVERY_CHANNEL_IH.equalsIgnoreCase(valueObj.get(ValueObjectKeys.SOURCE_INFO))) {
				msgTypeBean = transactionService.getSpilMessageTypeBean(
						valueObj.get(ValueObjectKeys.ORIGINAL_MSGTYPE) + DeliveryChannelConstants.DELIVERY_CHANNEL_CODE_IH);
			} else {
				msgTypeBean = transactionService.getSpilMessageTypeBean(
						valueObj.get(ValueObjectKeys.ORIGINAL_MSGTYPE) + DeliveryChannelConstants.DELIVERY_CHANNEL_CODE_SPIL);
			}

			if (msgTypeBean == null) {
				throw new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_INVALID, ResponseCodes.INVALID_REQUEST,
						new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_INVALID));
			}

			logger.info("Spil Transaction request: {}", xmlMsg);
			constructValueObjValues(valueObj, msgTypeBean);

			/**
			 * Blacklisting - Maintain list of html and javascript tags and Validate the entire input XML message for
			 * cross site scripting vulnerabilities such as <script> tag
			 */
			xssValidator.validateRequestXml(xmlMsg);

			if (valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER) != null) {
				ThreadContext.put("RRN", valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
			}
			// Parsing the xml request and populating to valueObj
			valueObj.putAll(reqParser.parse(xmlMsg, valueObj));

			valueObj.put(ValueObjectKeys.ORGNL_TRAN_AMOUNT, valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
			valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, "0");
			
			if( "0".equals(valueObj.get(ValueObjectKeys.SPNUMBER)) || (!Util.isEmpty(valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM)) && "0".equals(valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM)))){
				logger.info("SP number Or TargetCardNumber  contains zero or invalid value ");
				throw new ServiceException(SpilExceptionMessages.INVALID_CARD, ResponseCodes.INVALID_CARD);
			}

			// if spill currency code is not present in input, Use local currency code
			if (Util.isEmpty(valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR))) {
				valueObj.put(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
			}
			String cvv2 = String.valueOf(valueObj.get(ValueObjectKeys.CVV2));
			if (!Util.isEmpty(cvv2)) {
				valueObj.put(ValueObjectKeys.CVV2_LENGTH, String.valueOf(cvv2.length()));
			} else {
				valueObj.put(ValueObjectKeys.CVV2_LENGTH, "0");
			}

			if (SpilTranConstants.SPIL_SALE_ACTIVE_CODE.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				valueObj.put(ValueObjectKeys.SPNUMBER, null);
				String productId = productService.getProductIdUsingUPC(valueObj.get(ValueObjectKeys.SPIL_UPC_CODE));
				logger.info("productId using UPC-->" + productId);
				if (!Util.isEmpty(productId)) {
					valueObj.put(ValueObjectKeys.PRODUCT_ID, productId);
					valueDto.setValueObj(valueObj);
				}
			}
			String targetCustomerId = null;
			if (DeliveryChannelConstants.DELIVERY_CHANNEL_IH.equalsIgnoreCase(valueObj.get(ValueObjectKeys.SOURCE_INFO))
					&& (SpilTranConstants.SPIL_BAL_TRANSFER.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
							|| SpilTranConstants.SPIL_CARD_SWAP.equals(valueObj.get(ValueObjectKeys.TRANS_CODE)))
					&& MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSG_TYPE))) {
				targetCustomerId = valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM);

			}

			/*
			 * Get card details before validating request elements validation, hence we able to log card details into
			 * transaction log even through elements validation failed. Added on 12-OCT-2018
			 */
			valueDto = cardDetailsService.getCardDetails(valueObj.get(ValueObjectKeys.SPNUMBER), valueObj);

			cardValidator.validateTransactionAllowed(valueObj);

			// Decline financial transactions for closed card status
			/* ramaprabhur March 18 2019 handled for cashout/balance transfer reversals */
			if (CardStatusConstants.CLOSED.equals(valueObj.get(ValueObjectKeys.CARD_CARDSTAT))) {
				boolean flag = false;

				if ("Y".equals(valueObj.get(ValueObjectKeys.IS_FINANCIAL))
						&& !(SpilTranConstants.SPIL_BAL_TRANSFER.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
								&& MsgTypeConstants.MSG_TYPE_REVERSAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) // balance
																														// transfer
						&& !(SpilTranConstants.SPIL_CASHOUT.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
								&& MsgTypeConstants.MSG_TYPE_REVERSAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE)))) // cashout
				{
					flag = true;
				}

				if (SpilTranConstants.SPIL_REDEMPTION_AUTH_LOCK.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))) { // redemption
																													// lock
					flag = true;
				}

				if (flag) {
					logger.info("card status check failed");
					throw new ServiceException(SpilExceptionMessages.CLOSED_CARD, ResponseCodes.CLOSED_CARD);
				}
			}
			// Data Element validations
			dataElementsValidation.validate(valueObj);

			// Password Authentication Process
			if (!isAuthenticated(valueObj.get(ValueObjectKeys.SPIL_USER_ID), valueObj.get(ValueObjectKeys.SPIL_PASSWORD)
				.getBytes(StandardCharsets.UTF_8), valueObj.get(ValueObjectKeys.SOURCE_INFO))) {
				throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_USERNAEM_PASSWORD, ResponseCodes.INVALID_USERID_OR_PASSWORD);
			}

			if (!SpilTranConstants.SPIL_CASHOUT.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_DEACTIVATION.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_C2C_TRANSFER.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_BAL_TRANSFER.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_BALANCE_INQUIRY.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_TRAN_HISTORY.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_CARD_SWAP.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				if (valueObj.containsKey(ValueObjectKeys.PURAUTHREQ) && !Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ))
						&& !Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT))) {
					valueObj.put(ValueObjectKeys.ORGNL_TRAN_AMOUNT, valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT));
					valueObj.put(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR, valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
					if (!PurseAPIConstants.POINTS.equalsIgnoreCase(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY))
							&& !PurseAPIConstants.SKU.equalsIgnoreCase(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY))) {
						valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, "0");

					} else {
						valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT));

					}

				}
			} else {
				valueObj.remove(ValueObjectKeys.PURAUTHREQ_PURSE_NAME);
			}

			valueDto.setAccountPurseDto(accountPurseDto(valueObj));

			// get product details from DB
			/**
			 * valueDto = cardDetailsService.getCardDetails(valueObj.get(ValueObjectKeys.SPNUMBER),valueObj);
			 */
			productService.getPurseDetails(valueDto);
			String purseId = valueObj.get(ValueObjectKeys.PURSE_ID);

			// get product details from DB
			productAttributes = productService.getProductAttributes(valueDto.getValueObj()
				.get(ValueObjectKeys.PRODUCT_ID), purseId);
			valueDto.setProductAttributes(productAttributes);

			// Validate MultiPurse
			if (valueObj.containsKey(ValueObjectKeys.PURAUTHREQ)) {
				String mpurseFlag = String.valueOf(valueDto.getProductAttributes()
					.get("Product")
					.get("multiPurseSupport"));
				validateMultiPurse(mpurseFlag);
			}

			cardDetailsService.doPopulateSupportedPurseDtls(valueDto);
			valueDto = cardDetailsService.getAccountPurseUsageDetails(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID),
					valueObj.get(ValueObjectKeys.PURSE_ID), valueDto);

			transactionService.duplicateCheckProductBased(valueDto);

			// BalanceTransfer validation for Duplicate RRN
			if (SpilTranConstants.SPIL_BAL_TRANSFER.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
				checkDuplicateRRNforBalTransfer(valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER),
						valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM), targetCustomerId);
			}

			// SaleActiveCode validation
			if (SpilTranConstants.SPIL_SALE_ACTIVE_CODE.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				// Duplicate Check starts 05042019

				// Duplicate Check ends 05042019
				if (MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
					String formFactor = productService.getProductType(valueDto);
					if (!Util.isEmpty(formFactor) && APIConstants.PRODUCT_FORM_FACTOR_DIGITAL.equalsIgnoreCase(formFactor)) {
						String packageId = valueObj.get(APIConstants.PACKAGE_ID);
						if (Util.isEmpty(packageId)) {
							valueDto.getValueObj()
								.put(APIConstants.PACKAGE_ID,
										String.valueOf(productAttributes.get(ProductAttributesConstants.ATTRIBUTE_GROUP_PRODUCT)
											.get(ProductAttributesConstants.ATTRIBUTE_DEFAULT_PACKAGE)));
						} else {
							if (!validatePackageID(packageId, valueObj.get(ValueObjectKeys.PRODUCT_ID))) {
								logger.info(SpilExceptionMessages.INVALID_PACKAGE_ID);
								throw new ServiceException(SpilExceptionMessages.INVALID_PACKAGE_ID, ResponseCodes.INVALID_UPC);
							}
						}
					} else {
						logger.info(SpilExceptionMessages.NOT_DIGITAL_PRODUCT);
						throw new ServiceException(SpilExceptionMessages.NOT_DIGITAL_PRODUCT, ResponseCodes.INVALID_UPC);
					}
				}
			}

			// getProductUPC
			String actualUpcCode = productService.getProductUPC(valueDto);
			String inUpcCode = valueObj.get(ValueObjectKeys.SPIL_UPC_CODE);
			if (!Util.isEmpty(actualUpcCode) && !actualUpcCode.equals(inUpcCode))
				valueObj.put(ValueObjectKeys.SPIL_UPC_CODE, actualUpcCode);

			// Currency Conversion Check
			if (MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {

				if (!PurseAPIConstants.POINTS.equalsIgnoreCase(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY))
						&& !PurseAPIConstants.SKU.equalsIgnoreCase(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY))) {

					String txnCurrency = valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR);
					String purseCurrency = valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE);
					logger.info("Transaction_currency: {}, purseCurrency: {}", txnCurrency, purseCurrency);

					if (purseCurrency.equalsIgnoreCase(PurseAPIConstants.SKU) || purseCurrency.equalsIgnoreCase(PurseAPIConstants.POINTS)) {
						logger.info("International not supported for POINTS and SKU");
						throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_CURRENCY, ResponseCodes.INVALID_CURRENCY_CODE);
					} else if (!txnCurrency.equals(purseCurrency)) {
						authorizationValidation.internationalSupportCheck(valueDto);
					} else if (txnCurrency.equals(purseCurrency)) {
						logger.info("Both Currency are same");

						Object generalProductAttributes = Util.getProductAttributeValue(valueDto.getProductAttributes(),
								ValueObjectKeys.GENERAL, ValueObjectKeys.TRANSACTION_AMOUNT_BUMP_UP);
						String usdOneBumpPumpTxn = generalProductAttributes == null ? "false" : generalProductAttributes.toString();

						if (GeneralConstants.TRUE.equals(usdOneBumpPumpTxn)
								&& SpilTranConstants.SPIL_REDEMPTION_AUTH_LOCK.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
								&& new BigDecimal(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)).compareTo(BigDecimal.ONE) == 0
								&& valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME) == null) {
							logger.info("One Dollar Redemption lock txn");
							valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, SpilTranConstants.USD_75_BUMP_AMT);
							valueObj.put(ValueObjectKeys.SPIL_ONE_USD_TRAN, ValueObjectKeys.FLAG_YES);

						} else {
							valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT));
							valueObj.put(ValueObjectKeys.SPIL_ONE_USD_TRAN, ValueObjectKeys.FLAG_NO);
						}

					} else {
						logger.info("International Transaction not Supported for this product");
						throw new ServiceException(SpilExceptionMessages.ACTION_NOT_SUPPORTED, ResponseCodes.ACTION_NOT_SUPPORTED);
					}
				}
			} else if (!MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))
					&& !PurseAPIConstants.POINTS.equals(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY))
					&& !PurseAPIConstants.SKU.equals(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY))) {
				logger.info("Reversal Transactions");
				valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT,
						valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMNT) != null ? valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMNT) : "0");
				valueObj.put(ValueObjectKeys.CURRENCY_CONV_RATE, valueObj.get(ValueObjectKeys.ORGNL_CURRENCY_CONV_RATE));
				logger.info("Reversal Transactions conversion rate" + valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE));
			}

			ruleEngineService.callTransactionFilter(valueDto);
			// auth validating
			authorizationValidation.validate(valueDto);

			if (!ValueObjectKeys.EXPIRED_PRODUCT_CARD_STATUS.equals(String.valueOf(valueDto.getValueObj()
				.get(ValueObjectKeys.CARD_CARDSTAT)))) {
				// checkproductvalidity
				boolean flag = productService.checkProductValidity(valueDto);

				if (!flag) {
					logger.error("Product is expired. Updating card status to expired product");
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
			long timeAfterDaoClass = System.currentTimeMillis();
			daoProcessTime = timeAfterDaoClass - timeBeforeDaoClass;
			logger.info("Transaction dao process time:{}ms", daoProcessTime);

			txnResponse = buildResponse(valueObj, response);

		} catch (ServiceException serviceExec) {
			if (Objects.isNull(valueDto) || CollectionUtils.isEmpty(valueDto.getValueObj()))
				transMap.putAll(valueObj);
			else
				transMap.putAll(valueDto.getValueObj());

			if (SpilTranConstants.SPIL_SALE_ACTIVE_CODE.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				if (MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
					if (valueObj.get(ValueObjectKeys.SPNUMBER) != null) {
						cardDetailsDao.updateCardStatus(valueObj.get(ValueObjectKeys.SPNUMBER), CardStatusConstants.PRINTER_SENT);
						transMap.put(ValueObjectKeys.CARD_CARDSTAT, CardStatusConstants.PRINTER_SENT);
					}
				} else if (MsgTypeConstants.MSG_TYPE_REVERSAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
					transMap.put(ValueObjectKeys.TRANSACTIONDESC, "Sale Active Code Reversal");
					transMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, "D");
				}
			}
			insertFailedTransaction(transMap, serviceExec.getResponseID(), serviceExec.getMessage());
			logger.info(
					"ResponseCode:" + serviceExec.getResponseID() + " Message:" + serviceExec.getMessage() + "  Exception:" + serviceExec);
			txnResponse = this.buildFailResponse(valueObj, serviceExec.getResponseID(), serviceExec.getMessage());

		} catch (Exception e) {
			if (Objects.isNull(valueDto) || CollectionUtils.isEmpty(valueDto.getValueObj()))
				transMap.putAll(valueObj);
			else
				transMap.putAll(valueDto.getValueObj());

			if (SpilTranConstants.SPIL_SALE_ACTIVE_CODE.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				if (MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
					if (valueObj.get(ValueObjectKeys.SPNUMBER) != null) {
						cardDetailsDao.updateCardStatus(valueObj.get(ValueObjectKeys.SPNUMBER), CardStatusConstants.PRINTER_SENT);
						transMap.put(ValueObjectKeys.CARD_CARDSTAT, CardStatusConstants.PRINTER_SENT);
					} else if (MsgTypeConstants.MSG_TYPE_REVERSAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
						transMap.put(ValueObjectKeys.TRANSACTIONDESC, "Sale Active Code Reversal");
						transMap.put(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, "D");
					}
				}
			}
			logger.debug("Put an entry in transaction log");
			respCode = ResponseCodes.SYSTEM_ERROR;
			insertFailedTransaction(transMap, respCode, e.getMessage());
			logger.error("Process Terminated Due to Exception :" + e.getMessage(), e);
			txnResponse = this.buildFailResponse(valueObj, respCode, e.getMessage());

		} finally {
			long timeAfterTransaction = System.currentTimeMillis();
			long timeTaken = timeAfterTransaction - timeBeforeTransaction;
			logger.info("Transaction response Time:{}ms", timeTaken);

			String reqXml = validationUtil.maskSecuredData(xmlMsg);
			String resXml = validationUtil.maskSecuredData(txnResponse);

			/**
			 * Splunk log for request XML
			 */
			loggerUtil.logSupport(LoggerConstants.PAYLOAD_MSGTYPE_REQUEST, LoggerConstants.CLASS_METHOD_FULLNAME, reqXml, "", "", valueObj);

			/**
			 * Splunk log for response XML
			 */
			loggerUtil.logSupport(LoggerConstants.PAYLOAD_MSGTYPE_RESPONSE, LoggerConstants.CLASS_METHOD_FULLNAME, resXml,
					String.valueOf(timeTaken), String.valueOf(daoProcessTime), valueObj);

			asyncServiceCall.callPrmService(valueObj);

			asyncServiceCall.callNotificationService(valueObj);

		}
		logger.info("Transaction response {}", txnResponse);
		logger.debug("Exit");
		ThreadContext.clearAll();
		return txnResponse;

	}

	void insertFailedTransaction(Map<String, String> valObject, String respCode, String respMsg) {

		try {

			/**
			 * if any value is larger than database size doing substring for insert
			 */

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
					valObject.put(ValueObjectKeys.SPIL_TRAN_AMT, String.valueOf(doubleTranAmt));
				}

			} catch (NumberFormatException ne) {
				tranAmt = "0";
				valObject.put(ValueObjectKeys.SPIL_TRAN_AMT, tranAmt);
			}

			if (doubleTranAmt != 0) {
				String tranAmttemp = doubleTranAmt + "";

				Pattern pattern = Pattern.compile("[0-9]{1,012}|([0-9]{1,012}\\.[0-9]{1,3})");

				if (!pattern.matcher(tranAmttemp)
					.matches()) {

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
					valObject.put(ValueObjectKeys.ORGNL_TRAN_AMOUNT, String.valueOf(doubleorgnlTranAmt));
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
			if (!Util.isEmpty(currCode) && currCode.length() > 6) {
				currCode = currCode.substring(0, 6);
				logger.debug("Transaction failed trans currency code: " + currCode);
				valObject.put(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR, currCode);
			}

			logger.debug("Purse Curr:" + valObject.get(ValueObjectKeys.TXN_CURRENCY_CODE));

			String pursecurrCode = String.valueOf(valObject.get(ValueObjectKeys.TXN_CURRENCY_CODE));
			if (!Util.isEmpty(pursecurrCode) && pursecurrCode.length() > 6) {
				pursecurrCode = pursecurrCode.substring(0, 6);
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
			if (mercName.length() > 30) {
				mercName = mercName.substring(0, 30);
				logger.debug("Transaction failed trans merchant name: " + mercName);
				valObject.put(ValueObjectKeys.SPIL_MERCHANT_NAME, mercName);
			} else {
				logger.debug("Transaction failed trans merchant Name: " + mercName);
				valObject.put(ValueObjectKeys.SPIL_MERCHANT_NAME, mercName);
			}

			// logged the merchant detail
			if (respCode != null && respMsg1 != null) {
				logger.debug("Insert failed transaction into log table - respCode: {}, respMsg: {}", respCode, respMsg);
			}
			logger.debug("Put an entry in transaction log");
			spilDAO.transactionLogEntry(valObject, respCode, respMsg1);

		} catch (Exception e) {
			logger.error("Exception occured while validating transaction log entry: " + e.getMessage(), e);
		}

	}

	/**
	 * Building Fail Response
	 * 
	 * @param hKeyValue
	 * @param respField
	 * @return XML response
	 */
	public String buildFailResponse(Map<String, String> hKeyValue, String respCode, String respDesc) {
		String respMsg = "";
		String responseCode = "";

		SpilResponseCode spilResponseCode = transactionService.getSpilResponseCode(respCode);
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
		hKeyValue.put(ValueObjectKeys.RESP_CODE, responseCode);
		hKeyValue.put(ValueObjectKeys.RESP_MSG, respMsg);// Added to carry forward response code & message in
															// valueObject to PRM/notification services by
															// venkateshgaddam

		responseBuilder.addResponseBody(responseCode, respMsg, hKeyValue, reqParser.getCclpReqElement());

		return responseBuilder.getXmlResponse();
	}

	/**
	 * Method used to authenticate the user ID and password passed in the XML message.
	 * 
	 * @param userID
	 * @param password
	 * @return true if authenticated successfully
	 * @throws APISecurityException
	 */
	public boolean isAuthenticated(String userID, byte[] password, String channel) {
		if (DeliveryChannelConstants.DELIVERY_CHANNEL_IH.equalsIgnoreCase(channel)) {
			String ccaDecrPassword = null;
			try {
				ccaDecrPassword = AES.decrypt(new String(password, StandardCharsets.UTF_8), fsapikey);
			} catch (APISecurityException e) {
				logger.error("Error occuring while decrypting given password:\n" + password + "\n" + e.getMessage());
				// logger.error("\nCheck password if it is correct and encrypted");
			}
			if (userID != null && userID.equalsIgnoreCase(ccaUserId)) {
				if (!(ccaPassword.equals(ccaDecrPassword))) {
					return false;
				}
			} else
				return false;
		} else {
			if (userID != null && userID.equalsIgnoreCase(spilUserId)) {
				if (!Arrays.equals(spilPassword.getBytes(StandardCharsets.UTF_8), password)) {
					return false;
				}
			} else
				return false;

		}
		return true;
	}

	/**
	 * Method used to build success response
	 * 
	 * @param hKeyValue
	 * @param respFields
	 * @return XML response
	 * @throws ServiceException
	 */
	public String buildResponse(Map<String, String> hKeyValue, String... respFields) throws ServiceException {
		String authorizedAmt = null;
		String acctBalanceDefaultPurse = null;
		String defaultCurrency = null;
		StringBuilder purseAuthResponseValue = null;

		SpilResponseCode spilResponseCode = transactionService.getSpilResponseCode(respFields[0]);
		if (spilResponseCode != null) {
			respFields[0] = spilResponseCode.getChannelResponseCode();
			respFields[1] = spilResponseCode.getResponseDescription(); // Response Message is coming in Field 1 by
																		// Karthik L
		}

		hKeyValue.put(ValueObjectKeys.RESP_CODE, respFields[0]);
		hKeyValue.put(ValueObjectKeys.RESP_MSG, respFields[1]); // Added to carry forward response code & message in
																// valueObject to PRM/notification services by
																// venkateshgaddam

		// form a key to get card status attribute
		Map<String, String> cardStatusDefs = null;

		String purbal = null;
		logger.info("Account query:{}", hKeyValue.get(ValueObjectKeys.CARD_ACCOUNT_ID));

		// This method will be invoked only the PurseReq present and set the balance for the detault purse in the main
		// tag.

		if (hKeyValue.containsKey(ValueObjectKeys.PURAUTHREQ)) {
			spilDAO.getAcccountBalanceAndCurrency(hKeyValue);
		}

		cardStatusDefs = localCacheService.getAllCardStatus(cardStatusDefs);
		if (CollectionUtils.isEmpty(cardStatusDefs)) {
			cardStatusDefs = transactionService.getAllCardStatus();
			localCacheService.getAllCardStatus(cardStatusDefs);
		}

		if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_ACTIVATION)
				&& hKeyValue.get(ValueObjectKeys.MSG_TYPE)
					.equals(MsgTypeConstants.MSG_TYPE_REVERSAL)) {
			purbal = spilDAO.getPurseBalance(hKeyValue.get(ValueObjectKeys.CARD_ACCOUNT_ID));
			responseBuilder.addResponseBodyForactRev(respFields[0], respFields[1], respFields[2], respFields[3],
					hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE), reqParser.getCclpReqElement(), respFields[4], purbal);
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_ACTIVATION)
				&& hKeyValue.get(ValueObjectKeys.MSG_TYPE)
					.equals(MsgTypeConstants.MSG_TYPE_NORMAL)) {
			purbal = spilDAO.getPurseBalance(hKeyValue.get(ValueObjectKeys.CARD_ACCOUNT_ID));
			responseBuilder.addResponseBodyForActivation(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					cardStatusDefs.get(respFields[5]), reqParser.getCclpReqElement(), respFields[8], purbal);
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_BALANCE_INQUIRY)) {
			responseBuilder.addResponseBodyForBalInq(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					cardStatusDefs.get(respFields[5]), reqParser.getCclpReqElement(), hKeyValue.get(ValueObjectKeys.PUR_BAL));
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_STORE_CREDIT)
				|| hKeyValue.get(ValueObjectKeys.TRANS_CODE)
					.equals(SpilTranConstants.SPIL_CREDIT)) {
			purbal = hKeyValue.get(ValueObjectKeys.PUR_BAL);
			if (hKeyValue.containsKey(ValueObjectKeys.PURAUTHREQ)) {
				purseAuthResponseValue = Util.getPurseAuthResp(hKeyValue, respFields[3],
						hKeyValue.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID));
				authorizedAmt = GeneralConstants.ZERO;
				acctBalanceDefaultPurse = hKeyValue.get(ValueObjectKeys.AVAIL_BALANCE);
				defaultCurrency = hKeyValue.get(ValueObjectKeys.DEFAULT_PURSE_CURRENCY);
			} else {
				authorizedAmt = respFields[4];
				acctBalanceDefaultPurse = respFields[3];
				defaultCurrency = hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE);
			}

			responseBuilder.addResponseBodyForCredit(respFields[0], respFields[1], respFields[2], acctBalanceDefaultPurse, defaultCurrency,
					reqParser.getCclpReqElement(), purseAuthResponseValue, purbal, authorizedAmt);
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_CARD_REDEMPTION)
				|| hKeyValue.get(ValueObjectKeys.TRANS_CODE)
					.equals(SpilTranConstants.SPIL_REDEMPTION_AUTH_LOCK)
				|| hKeyValue.get(ValueObjectKeys.TRANS_CODE)
					.equals(SpilTranConstants.SPIL_REDEMPTION_AUTH_UNLOCK)) {
			logger.debug("welcome to card Redemption");

			String purseAuthResponse = null;
			if (hKeyValue.containsKey(ValueObjectKeys.PURAUTHREQ)) {
				purseAuthResponse = respFields[6];
				authorizedAmt = GeneralConstants.ZERO;
				acctBalanceDefaultPurse = hKeyValue.get(ValueObjectKeys.AVAIL_BALANCE);
				defaultCurrency = hKeyValue.get(ValueObjectKeys.DEFAULT_PURSE_CURRENCY);
				logger.info("Account acctBalanceDefaultPurse" + acctBalanceDefaultPurse);
			} else {
				authorizedAmt = respFields[4];
				acctBalanceDefaultPurse = respFields[3];
				defaultCurrency = hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE);
			}
			purbal = hKeyValue.get(ValueObjectKeys.PUR_BAL);
			responseBuilder.addResponseBodyForCardRedemption(respFields[0], respFields[1], respFields[2], acctBalanceDefaultPurse,
					authorizedAmt, defaultCurrency, reqParser.getCclpReqElement(), purseAuthResponse, purbal);
		} else if (SpilTranConstants.SPIL_C2C_TRANSFER.equals(hKeyValue.get(ValueObjectKeys.TRANS_CODE))
				|| SpilTranConstants.SPIL_BAL_TRANSFER.equals(hKeyValue.get(ValueObjectKeys.TRANS_CODE))) {
			logger.debug("I am inside C2C & bal transfer SPIL");
			responseBuilder.addResponseBodyForCtoCtransfer(respFields[0], respFields[5], respFields[1], respFields[2], respFields[6],
					respFields[3], hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE), respFields[4],
					hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE), "", reqParser.getCclpReqElement());
			logger.debug("I am after C2C SPIL & bal transfer");
		} // EN
		else if (SpilTranConstants.SPIL_RELOAD.equals(hKeyValue.get(ValueObjectKeys.TRANS_CODE))
				|| SpilTranConstants.SPIL_VALUE_INSERTION.equals(hKeyValue.get(ValueObjectKeys.TRANS_CODE))
				|| SpilTranConstants.SPIL_PRE_VALUE_INSERTION.equals(hKeyValue.get(ValueObjectKeys.TRANS_CODE))) {
			if (hKeyValue.containsKey(ValueObjectKeys.PURAUTHREQ)
					&& !SpilTranConstants.SPIL_PRE_VALUE_INSERTION.equals(hKeyValue.get(ValueObjectKeys.TRANS_CODE))) {
				purseAuthResponseValue = Util.getPurseAuthResp(hKeyValue, respFields[3],
						hKeyValue.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID));
				authorizedAmt = respFields[4];
				acctBalanceDefaultPurse = hKeyValue.get(ValueObjectKeys.AVAIL_BALANCE);
				defaultCurrency = hKeyValue.get(ValueObjectKeys.DEFAULT_PURSE_CURRENCY);
				logger.info("Account acctBalanceDefaultPurse" + acctBalanceDefaultPurse);
			} else {
				authorizedAmt = GeneralConstants.ZERO;
				acctBalanceDefaultPurse = respFields[3];
				defaultCurrency = hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE);
			}

			purbal = hKeyValue.get(ValueObjectKeys.PUR_BAL);

			responseBuilder.addResponseBodyForReLoad(respFields[0], respFields[1], acctBalanceDefaultPurse, respFields[2],
					purseAuthResponseValue, purbal, reqParser.getCclpReqElement(), defaultCurrency, authorizedAmt);
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_CASHOUT)
				&& (hKeyValue.get(ValueObjectKeys.MSG_TYPE)
					.equals(MsgTypeConstants.MSG_TYPE_NORMAL)
						|| hKeyValue.get(ValueObjectKeys.MSG_TYPE)
							.equals(MsgTypeConstants.MSG_TYPE_REVERSAL))) {
			logger.debug("Inside Cash Out Transaction Response");
			responseBuilder.addResponseForCashOut(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					reqParser.getCclpReqElement());
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_TRAN_HISTORY)) {
			responseBuilder.addResponseBodyForTranHist(respFields[0], respFields[3], respFields[1], respFields[2], respFields[4],
					respFields[5], hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE), reqParser.getCclpReqElement());
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals("38")) {
			logger.debug("I am inside txn history SPIL");
			responseBuilder.addResponseBodyForTransactionHistory(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					respFields[5], respFields[6], respFields[7], reqParser.getCclpReqElement());
			logger.debug("I am after txn history SPIL");
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_PREAUTH_ACTIVATION)) {
			responseBuilder.addResponseBodyForactPreAuth(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					reqParser.getCclpReqElement());
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_DEACTIVATION)
				&& hKeyValue.get(ValueObjectKeys.MSG_TYPE)
					.equals(MsgTypeConstants.MSG_TYPE_NORMAL)) {
			responseBuilder.addResponseBodyForDeact(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					reqParser.getCclpReqElement());
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_DEACTIVATION)
				&& hKeyValue.get(ValueObjectKeys.MSG_TYPE)
					.equals(MsgTypeConstants.MSG_TYPE_REVERSAL)) {
			responseBuilder.addResponseBodyForDeact(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					reqParser.getCclpReqElement());
		} else if (hKeyValue.get(ValueObjectKeys.TRANS_CODE)
			.equals(SpilTranConstants.SPIL_SALE_ACTIVE_CODE)) {
			responseBuilder.addResponseBodyForSaleActiveCode(respFields[0], respFields[1], respFields[2], respFields[3], respFields[4],
					cardStatusDefs.get(respFields[5]), respFields[6], respFields[7], respFields[8], reqParser.getCclpReqElement());
		} else if (SpilTranConstants.SPIL_CARD_SWAP.equals(hKeyValue.get(ValueObjectKeys.TRANS_CODE))) {
			logger.debug("building response for cardSwap");
			responseBuilder.addResponseBodyForCtoCtransfer(respFields[0], respFields[9], respFields[1], respFields[2], respFields[8],
					respFields[3], hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE), respFields[7],
					hKeyValue.get(ValueObjectKeys.TXN_CURRENCY_CODE), hKeyValue.get(ValueObjectKeys.PUR_BAL),
					reqParser.getCclpReqElement());
		} else {
			responseBuilder.addResponseBody(respFields[0], respFields[2], respFields[3], reqParser.getCclpReqElement());
		}
		return responseBuilder.getXmlResponse();
	}

	/**
	 * Method used to populate the spil message type transaction code,messageType,DeliveryChannel to valueObj
	 * 
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
		valueObj.put(ValueObjectKeys.TRANSACTIONSHORTNAME, msgTypeBean.getTransactionShortName());
	}

	/**
	 * This method will commit the connection
	 * 
	 * @param con
	 */
	public void commit(Connection con) {

		try {
			if (con != null)
				con.commit();
		} catch (SQLException e) {
			logger.error("Exception occured while commiting the transaction: " + e.getMessage(), e);
		}
	}

	/**
	 * This method will rollback the connection
	 * 
	 * @param con
	 */
	public void rollback(Connection con) {

		try {
			if (con != null)
				con.rollback();
		} catch (SQLException e) {
			logger.error("Exception occured while commiting the transaction " + e.getMessage(), e);
		}
	}

	//
	public String[] processSPILRequest(ValueDTO valueDto) throws ServiceException, SQLException, ParseException {
		logger.debug("Entered in to LoadSPILTransactionsDAOs");

		if (!CollectionUtils.isEmpty(valueDto.getValueObj()) && !Util.isEmpty(valueDto.getValueObj()
			.get("transactionClassName"))) {
			return spilCommonService.get(valueDto.getValueObj()
				.get("transactionClassName"))
				.invoke(valueDto);

		} else {
			return accountApplicationService.processSpilTransaction(valueDto);
		}

	}

	private boolean validatePackageID(String packageID, String productId) throws ServiceException {
		logger.debug("Entered in to ValidatePackageID");

		boolean isValidPackage = false;
		List<String> packageIds = productService.getPackageIdsByProductId(productId);

		if (packageIds.contains(packageID)) {
			isValidPackage = true;
		}

		logger.debug("Exit from ValidatePackageID");

		return isValidPackage;
	}

	public static AccountPurseDTO accountPurseDto(Map<String, String> valueObj) {
		if (valueObj.containsKey(ValueObjectKeys.PURAUTHREQ_PURSE_NAME)
				&& !Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME))) {
			return AccountPurseDTO.builder()
				.accountPurseId(Util.convStringToLong(valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID)))
				.purseName(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME))
				.transactionAmount(new BigDecimal(valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT)))
				.skuCode(valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE))
				.purseType(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY))
				.build();
		} else {
			return null;
		}

	}

	private void validateMultiPurse(String mpurseFlag) throws ServiceException {
		logger.debug("Entered in to validateMultiPurse" + mpurseFlag);
		if (mpurseFlag != null && "Disable".equalsIgnoreCase(mpurseFlag)) {
			logger.info("MultiPurse Flag has been disabled.");
			throw new ServiceException(SpilExceptionMessages.MULTIPURSE_DISABLED, ResponseCodes.MULTIPURSE_DISABLED);
		}
		logger.debug("Exit from validateMultiPurse");
	}

	private void checkDuplicateRRNforBalTransfer(String rrn, String targetCardNumber, String targetCustomerId) throws ServiceException {
		logger.debug("Entered into checkDuplicateRRNforBalTransfer:: rrn:: {} targetCardNumber::{} targetCustomerId::{} ", rrn,
				targetCardNumber, targetCustomerId);
		boolean isDuplicateRRN = false;
		isDuplicateRRN = balanceTransferServiceImpl.checkDuplicateRRN(rrn, targetCardNumber, targetCustomerId);
		if (isDuplicateRRN) {
			logger.info("RRN with TargetCard or customerId already exists in balance_transfer_details table. Transaction declined.");
			throw new ServiceException(SpilExceptionMessages.DUPLICATE_RRN, ResponseCodes.DUPLICATE_RRN_OR_REQUEST);
		}
		logger.debug("Exit from checkDuplicateRRNforBalTransfer:: rrn:: {} targetCardNumber::{} targetCustomerId::{} ", rrn,
				targetCardNumber, targetCustomerId);
	}

}
