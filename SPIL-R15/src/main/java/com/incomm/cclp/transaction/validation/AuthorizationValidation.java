package com.incomm.cclp.transaction.validation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCurrencyConversionService;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.util.Util;

@Service
public class AuthorizationValidation {

	@Autowired
	LocalCacheServiceImpl localCacheService;

	@Autowired
	TransactionService transactionService;

	@Autowired
	CommonAuthValidation commonAuthValidation;

	@Autowired
	SpilCurrencyConversionService spilCurrencyConversionService;

	@Autowired
	TransactionAuthValidation transactionAuthValidation;

	private static final Logger logger = LogManager.getLogger(AuthorizationValidation.class);

	public void validate(ValueDTO valueDto) throws ServiceException {

		logger.debug(GeneralConstants.ENTER);

		Map<String, String> valueObj = valueDto.getValueObj();

		/**
		 * Check whether card was already activated for Activation transaction. If Yes consider transaction as Recharge
		 */
		if (cardAlreadyActivatedCheck(valueDto)) {
			// Change Transaction Code as Recharge (012)
			SpilStartupMsgTypeBean msgTypeBean = transactionService.getSpilMessageTypeBean("recharge" + "01");
			if (msgTypeBean == null) {
				throw new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_INVALID, ResponseCodes.INVALID_REQUEST,
						new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_INVALID));
			}

			valueDto.getValueObj()
				.replace(ValueObjectKeys.MSGTYPE, msgTypeBean.getMsgType());
			valueDto.getValueObj()
				.replace(ValueObjectKeys.DELIVERYCHNL, msgTypeBean.getDeliveryChannel());
			valueDto.getValueObj()
				.replace(ValueObjectKeys.TRANS_CODE, msgTypeBean.getTxnCode());
			valueDto.getValueObj()
				.replace(ValueObjectKeys.MEMBERNO, "000");
			valueDto.getValueObj()
				.replace(ValueObjectKeys.PARTY_SUPPORTED, msgTypeBean.getPartySupported());
			valueDto.getValueObj()
				.replace(ValueObjectKeys.AUTH_JAVA_CLASS_NAME, msgTypeBean.getAuthJavaClass());
			valueDto.getValueObj()
				.replace(ValueObjectKeys.IS_FINANCIAL, msgTypeBean.getIsFinacial());
			valueDto.getValueObj()
				.replace(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, msgTypeBean.getCreditDebitIndicator());
		}

		String msgType = valueObj.get(ValueObjectKeys.MSGTYPE);

		logger.debug("Msg Type : {}", msgType);

		String transactionCode = valueObj.get(ValueObjectKeys.TRANS_CODE);

		logger.debug("Transaction Code : {}", transactionCode);

		String deliveryChannel = valueObj.get(ValueObjectKeys.DELIVERYCHNL);

		logger.debug("deliveryChannel Code : {}", deliveryChannel);

		String mappingKey = (transactionCode + msgType + deliveryChannel);

		List<TransactionAuthDefinition> txnAuthDefs = getTxnAuthDefinitionsByMappingKey(mappingKey);

		if (!CollectionUtils.isEmpty(txnAuthDefs)) {

			for (TransactionAuthDefinition txnAuthDef : txnAuthDefs) {
				/**
				 * If Process type value is 'COMMON' call common validation
				 */
				if (txnAuthDef.getProcessType()
					.equalsIgnoreCase(GeneralConstants.AUTH_VAL_PROCESS_TYPE_COMMON)) {

					commonAuthValidation.validateCommonauth(txnAuthDef.getProcessKey(), txnAuthDef.getValidationType(), valueDto);

				}

				/**
				 * else Process type value is 'TXN' call transaction specific validation
				 */
				else if (txnAuthDef.getProcessType()
					.equalsIgnoreCase(GeneralConstants.AUTH_VAL_PROCESS_TYPE_TXN)) {

					transactionAuthValidation.validateTransactionauth(txnAuthDef.getProcessKey(), valueDto);
				}
			}

			String cvv2 = String.valueOf(valueObj.get(ValueObjectKeys.CVV2));

			if (!Util.isEmpty(cvv2) && cvv2.length() == 3) {
				commonAuthValidation.commonValidationsService.spillCvv2Validation(valueDto);
			}
		}

		logger.debug(GeneralConstants.EXIT);
	}

	private List<TransactionAuthDefinition> getTxnAuthDefinitionsByMappingKey(String mappingKey) {
		logger.debug(GeneralConstants.ENTER);

		List<TransactionAuthDefinition> txnAuthDefs = new ArrayList<>();
		Map<String, List<TransactionAuthDefinition>> mapTxnAuthDefinitions = null;
		mapTxnAuthDefinitions = localCacheService.getAllTransactionAuthDefinitions(mapTxnAuthDefinitions);

		if (CollectionUtils.isEmpty(mapTxnAuthDefinitions)) {

			mapTxnAuthDefinitions = transactionService.getAllTransactionAuthDefinitions();

			localCacheService.updateOrGetAllTransactionAuthDefinitions(mapTxnAuthDefinitions);
		}

		if (!CollectionUtils.isEmpty(mapTxnAuthDefinitions)) {

			if (mapTxnAuthDefinitions.containsKey(mappingKey)) {

				logger.debug("Transaction Auth validations for mapping key exist");

				txnAuthDefs = mapTxnAuthDefinitions.get(mappingKey);

			}
		}

		logger.debug(GeneralConstants.EXIT);

		return txnAuthDefs;
	}

	private boolean cardAlreadyActivatedCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		logger.info("card Already Activated Check...");
		boolean status = false;
		String cardStatus = valueDto.getValueObj()
			.get(ValueObjectKeys.CARD_CARDSTAT);
		String deliveryChannel = valueDto.getValueObj()
			.get(ValueObjectKeys.DELIVERYCHNL);
		String transactionCode = valueDto.getValueObj()
			.get(ValueObjectKeys.TRANS_CODE);
		String msgType = valueDto.getValueObj()
			.get(ValueObjectKeys.MSGTYPE);
		Map<String, Object> productAttributes = valueDto.getProductAttributes()
			.get("Product");

		if (transactionCode.equalsIgnoreCase("002") && msgType.equals("0200") && cardStatus.equals("1") && !deliveryChannel.equals("06")) {

			if (!CollectionUtils.isEmpty(productAttributes) && productAttributes.containsKey(ValueObjectKeys.ACTIVATION_AS_RELOAD)
					&& !Objects.isNull(productAttributes.get(ValueObjectKeys.ACTIVATION_AS_RELOAD))
					&& productAttributes.get(ValueObjectKeys.ACTIVATION_AS_RELOAD)
						.equals("true")) {

				logger.info("Activation request will be consider as Reload transaction");
				status = true;

			} else {
				logger.info("Card already activated");
				throw new ServiceException(SpilExceptionMessages.ACTIVE_CARD, ResponseCodes.ACTIVE_CARD);
			}
		}
		logger.info("card Already Activated Check success");
		logger.debug(GeneralConstants.EXIT);

		return status;
	}

	public void internationalSupportCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		logger.info("International Suppor Check...");
		String cardStatusDesc = null;
		String channelShortName = null;
		String txnShortName = null;
		String cardStatusAttributeKey = null;
		Map<String, Map<String, Object>> productAttributes = null;
		Map<String, String> valueObj = valueDto.getValueObj();
		productAttributes = valueDto.getProductAttributes();

		String productId = valueDto.getValueObj()
			.get(ValueObjectKeys.PRODUCT_ID);
		String spilOrginalTranCurr = valueDto.getValueObj()
			.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR);

		productCurrencyCheck(productAttributes, productId, spilOrginalTranCurr);

		String internationalSupport = String.valueOf(Util.getProductAttributeValue(productAttributes, "Product", "internationalSupported"));

		if (!Util.isEmpty(internationalSupport) && internationalSupport.equals("Enable")) {

			txnShortName = valueObj.get(ValueObjectKeys.TRANSACTIONSHORTNAME);

			channelShortName = valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME);
			String roundoffdigits = valueObj.get(ValueObjectKeys.MINORUNITS);
			// get card status attributes
			Map<String, Object> cardStatusAttributes = productAttributes.get("Card Status");

			// form a key to get card status attribute
			Map<String, String> cardStatusDefs = null;

			cardStatusDefs = localCacheService.getAllCardStatus(cardStatusDefs);
			if (CollectionUtils.isEmpty(cardStatusDefs)) {
				cardStatusDefs = transactionService.getAllCardStatus();
				localCacheService.getAllCardStatus(cardStatusDefs);
			}

			cardStatusDesc = cardStatusDefs.get(GeneralConstants.INTERNATIONAL_CARD_STATUS);
			cardStatusAttributeKey = (channelShortName + "_" + txnShortName + "_" + cardStatusDesc);
			if (!CollectionUtils.isEmpty(cardStatusAttributes) && !Objects.isNull(cardStatusAttributes.get(cardStatusAttributeKey))) {
				if (!cardStatusAttributes.get(cardStatusAttributeKey)
					.toString()
					.equalsIgnoreCase("true")) {
					logger.info("International transaction not enabled for this transaction");
					throw new ServiceException(SpilExceptionMessages.ACTION_NOT_SUPPORTED, ResponseCodes.ACTION_NOT_SUPPORTED);
				} else {
					String convRate = spilCurrencyConversionService.getCurrencyRate(valueObj.get(ValueObjectKeys.MDM_ID),
							valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR), valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));
					logger.info("Conversion Rate {}", convRate);

					Object generalProductAttributes = Util.getProductAttributeValue(valueDto.getProductAttributes(),
							ValueObjectKeys.GENERAL, ValueObjectKeys.TRANSACTION_AMOUNT_BUMP_UP);
					String usdOneBumpPumpTxn = generalProductAttributes == null ? "false" : generalProductAttributes.toString();

					if (!Util.isEmpty(convRate)) {
						String tempTranAmount = null;
						if (GeneralConstants.TRUE.equals(usdOneBumpPumpTxn)
								&& SpilTranConstants.SPIL_REDEMPTION_AUTH_LOCK.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
								&& new BigDecimal(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)).compareTo(BigDecimal.ONE) == 0
								&& valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME) == null) {

							tempTranAmount = SpilTranConstants.USD_75_BUMP_AMT;
							valueObj.put(ValueObjectKeys.SPIL_ONE_USD_TRAN, ValueObjectKeys.FLAG_YES);

						} else {
							tempTranAmount = valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT);
							valueObj.put(ValueObjectKeys.SPIL_ONE_USD_TRAN, ValueObjectKeys.FLAG_NO);
						}
						String txnAmt = conversionRateCalc(new BigDecimal(convRate), new BigDecimal(tempTranAmount), roundoffdigits);
						valueObj.put(ValueObjectKeys.SPIL_TRAN_AMT, txnAmt);
						valueObj.put(ValueObjectKeys.CURRENCY_CONV_RATE, convRate);
					} else {
						logger.info("No conversion Rate for this currency");
						throw new ServiceException(SpilExceptionMessages.CONVERSION_RATE_NOT_FOUND, ResponseCodes.SYSTEM_ERROR);
					}
				}
			} else {
				logger.info("International keys not configured");
				throw new ServiceException(SpilExceptionMessages.ACTION_NOT_SUPPORTED, ResponseCodes.ACTION_NOT_SUPPORTED);
			}
		}

	}

	private String conversionRateCalc(BigDecimal conversionRate, BigDecimal txnAmount, String roundingoff) {
		logger.debug(GeneralConstants.ENTER);
		logger.info("Currency Conversion Calc...");
		BigDecimal convTxnAmt = conversionRate.multiply(txnAmount);
		logger.debug(GeneralConstants.EXIT);
		return convTxnAmt.setScale(Integer.parseInt(roundingoff), BigDecimal.ROUND_HALF_UP)
			.toString();

	}

	private void productCurrencyCheck(Map<String, Map<String, Object>> productAttributes, String productId, String spilOrginalTranCurr)
			throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		@SuppressWarnings("unchecked")
		Map<String, List<Object>> internationalCurrency = (Map<String, List<Object>>) productAttributes.get("Product")
			.get("product_currency");
		if (internationalCurrency != null && !internationalCurrency.isEmpty()) {
			List<Object> internationalCurrencyList = internationalCurrency.get(productId + "_" + spilOrginalTranCurr);
			if (CollectionUtils.isEmpty(internationalCurrencyList)) {
				logger.info("Product level currencies are not matched");
				throw new ServiceException(SpilExceptionMessages.INVALID_TXN_CURRENCY_CODE, ResponseCodes.INVALID_TXN_CURRENCY_CODE);
			}
		} else {
			logger.info("Product level currencies are not configured for this product");
			throw new ServiceException(SpilExceptionMessages.INVALID_TXN_CURRENCY_CODE, ResponseCodes.INVALID_TXN_CURRENCY_CODE);
		}
		logger.debug(GeneralConstants.EXIT);
	}

}
