package com.incomm.cclp.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.constants.CardStatusConstants;
import com.incomm.cclp.constants.DeliveryChannelConstants;
import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.PurseAPIConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CardDetailsDAO;
import com.incomm.cclp.domain.CardDetail;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CardDetailsService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.transaction.bean.SupportedPurse;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.util.Util;

@Service
public class CardDetailsServiceImpl implements CardDetailsService {

	private static final Logger logger = LogManager.getLogger(CardDetailsServiceImpl.class);

	@Autowired
	CardDetailsDAO cardDetailsDao;

	@Autowired
	ProductService productService;

	@Override
	public ValueDTO getCardDetails(String spNumber, Map<String, String> valueObj) {
		logger.debug(GeneralConstants.ENTER);

		String targetCardNumber = valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM);

		ValueDTO valueDto = null;
		Map<String, Object> dbCardDetails = null;
		Map<String, Object> dbTargetCardDetails = null;

		try {
			if (SpilTranConstants.SPIL_SALE_ACTIVE_CODE.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				spNumber = getCardNumber(valueObj);
			}
		} catch (DataAccessException e) {
			logger.error("Error occured while getting card Number: " + e.getMessage(), e);
			if (MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
				throw new ServiceException(SpilExceptionMessages.CARD_NOT_FOUND, ResponseCodes.INVALID_REQUEST);
			} else {
				throw new ServiceException(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND,
						ResponseCodes.ORIGINAL_TRANSACTION_NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Error occured while getting card Number: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		logger.info("Source Info is:: " + valueObj.get(ValueObjectKeys.SOURCE_INFO));

		// if channel is IH CCA, get spNumber based on customer id which passed in spNumber

		// Start changes for VMSCL 819
		if (!Util.isEmpty(spNumber)) {
			if (DeliveryChannelConstants.DELIVERY_CHANNEL_IH.equalsIgnoreCase(valueObj.get(ValueObjectKeys.SOURCE_INFO))) {

				List<String> spTargetCardNumbersList = getSpTargetNumbersForIH(valueObj.get(ValueObjectKeys.TRANS_CODE),
						valueObj.get(ValueObjectKeys.MSGTYPE), valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER),
						valueObj.get(ValueObjectKeys.SPNUMBER), valueObj.get(ValueObjectKeys.SPIL_TARGET_CARDNUM));

				if (!CollectionUtils.isEmpty(spTargetCardNumbersList) && !Utils.isEmpty(spTargetCardNumbersList.get(0))) {
					spNumber = spTargetCardNumbersList.get(0);
					valueObj.put(ValueObjectKeys.SPNUMBER, spNumber);
				}

				if (!CollectionUtils.isEmpty(spTargetCardNumbersList) && !Utils.isEmpty(spTargetCardNumbersList.get(1))) {
					targetCardNumber = spTargetCardNumbersList.get(1);
					valueObj.put(ValueObjectKeys.SPIL_TARGET_CARDNUM, targetCardNumber);
				}

			}
			valueDto = new ValueDTO();

			try {
				if (SpilTranConstants.SPIL_BAL_TRANSFER.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
						|| SpilTranConstants.SPIL_C2C_TRANSFER.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
						|| SpilTranConstants.SPIL_CARD_SWAP.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))) {

					dbTargetCardDetails = cardDetailsDao.getCardDetailsBySPNumber(targetCardNumber);
					if (CollectionUtils.isEmpty(dbTargetCardDetails)) {
						logger.error("Target Card Number {} is not found as PAN, proxy_number and serial number", targetCardNumber);
						throw new ServiceException(SpilExceptionMessages.INVALID_TARGET_CARD, ResponseCodes.INVALID_CARD);
					}
					dbCardDetails = cardDetailsDao.getCardDetailsBySPNumber(spNumber);
					if (SpilTranConstants.SPIL_BAL_TRANSFER.equals(valueObj.get(ValueObjectKeys.TRANS_CODE))
							&& MsgTypeConstants.MSG_TYPE_REVERSAL.equals(valueObj.get(ValueObjectKeys.MSG_TYPE))) {
						dbCardDetails = cardDetailsDao.getCardDetailsByCustomerPref(spNumber, targetCardNumber);
					}
				} else {

					dbCardDetails = cardDetailsDao.getCardDetailsBySPNumber(spNumber);
				}

			} catch (Exception e) {
				logger.error("Error Occured in get card details: " + e.getMessage());
				// throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);

			}

			if (!CollectionUtils.isEmpty(dbCardDetails) && !Objects.isNull(dbCardDetails.get("PRODUCT_ID"))) {

				valueObj.put(ValueObjectKeys.PRODUCT_ID, dbCardDetails.get("PRODUCT_ID")
					.toString());

				doPopulateCardDetails(valueObj, dbCardDetails);

				valueDto.setValueObj(valueObj);

//					valueDto.setUsageLimit(getUsageLimitsAndFeesAttributes(dbCardDetails, "USAGE_LIMIT"));

//					valueDto.setUsageFee(getUsageLimitsAndFeesAttributes(dbCardDetails, "USAGE_FEE")); //Added to get the UsageFee from Card table @venkateshgaddam 

			} else {
				logger.info("Card number '{}' not found or not mapped with any product", spNumber);
				throw new ServiceException(SpilExceptionMessages.INVALID_CARD, ResponseCodes.INVALID_CARD);
			}

		} else {
			logger.info("SP Number is missing");
			throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
		}
		logger.debug(GeneralConstants.EXIT);

		return valueDto;
	}

	private List<String> getSpTargetNumbersForIH(String transCode, String msgtype, String rrn, String spNumberInRequest,
			String targetCardNumberInRequest) {

		String spNumber = null;
		String targetCardNumber = null;
		List<String> spTargetCardNumbersList = new ArrayList<>();
		if (SpilTranConstants.SPIL_BAL_TRANSFER.equals(transCode) && MsgTypeConstants.MSG_TYPE_REVERSAL.equals(msgtype)) {

			Map<String, Object> cardDetails = null;
			cardDetails = getCardNumberFromBalTransferDetails(rrn, targetCardNumberInRequest);
			if (!CollectionUtils.isEmpty(cardDetails)) {

				/*
				 * if(cardDetails.size()>1) {
				 * logger.error("More than one record found in balance_transfer_details table."); throw new
				 * ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR); }
				 */

				if (!Objects.isNull(cardDetails.get("SOURCE_CARD_NUM"))) {
					spNumber = cardDetails.get("SOURCE_CARD_NUM")
						.toString();
				} else {
					logger.error("Source Card Number Not Found in Balance_Transfer_Details for RRN: " + rrn);
					throw new ServiceException(SpilExceptionMessages.INVALID_CARD, ResponseCodes.INVALID_CARD);
				}

				if (!Objects.isNull(cardDetails.get("TARGET_CARD_NUM"))) {
					targetCardNumber = cardDetails.get("TARGET_CARD_NUM")
						.toString();
				} else {
					logger.error("Target Card Number Not Found in Balance_Transfer_Details for RRN: " + rrn);
					throw new ServiceException(SpilExceptionMessages.INVALID_TARGET_CARD, ResponseCodes.INVALID_CARD);
				}

			} else {
				logger.error("No Card Data Found in Balance_Transfer_Details for RRN: " + rrn);
				throw new ServiceException(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND,
						ResponseCodes.ORIGINAL_TRANSACTION_NOT_FOUND);
			}

		} else {
			try {
				spNumber = getCardNumberByCustId(spNumberInRequest);
			} catch (DataAccessException e) {
				logger.info("Source card number Not found by customer id : " + spNumberInRequest);
				logger.info("Passing Source number as it is in request as it could be Serial Number or proxy number");
			}

			try {
				if (!Utils.isEmpty(targetCardNumberInRequest)) {
					targetCardNumber = getCardNumberByCustId(targetCardNumberInRequest);
				} else if (SpilTranConstants.SPIL_BAL_TRANSFER.equals(transCode) || SpilTranConstants.SPIL_C2C_TRANSFER.equals(transCode)) {
					logger.info("Target Card Number is missing. ");
					throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
				}
			} catch (DataAccessException e) {
				logger.info("Target card number Not found by customer id : " + targetCardNumberInRequest);
				logger.info("Passing Target number as it is in request as it could be Serial Number or proxy number");
			}

		}
		spTargetCardNumbersList.add(0, spNumber);
		spTargetCardNumbersList.add(1, targetCardNumber);
		return spTargetCardNumbersList;

	}

	private String getCardNumberByCustId(String customerId) {
		return cardDetailsDao.getCardNumberByCustId(customerId);

	}

	private Map<String, Object> getUsageLimitsAttributes(Map<String, Object> dbCardDetails, String attributeGrp) {
		logger.debug(LoggerConstants.ENTER);
		Map<String, Object> attributesMap = null;
		if (!Objects.isNull(dbCardDetails.get(attributeGrp))) {
			try {
				if (attributeGrp.equalsIgnoreCase("USAGE_LIMIT"))
					attributesMap = Util.jsonToSingleMap((String) dbCardDetails.get(attributeGrp));
			} catch (IOException e) {
				logger.error("Error Occured while trying to convert product attributes json to map, {}", e.getMessage(), e);
				throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
			}
		} else {
			logger.debug("There is no attributes found for {}", attributeGrp);
			return null;
		}
		logger.debug(LoggerConstants.EXIT);
		return attributesMap;
	}

	private Map<String, Object> getUsageFeesAttributes(Map<String, Object> dbCardDetails, String attributeGrp) {
		logger.debug(LoggerConstants.ENTER);
		Map<String, Object> attributesMap = null;
		String attributesString = null;
		if (!Objects.isNull(dbCardDetails.get(attributeGrp)) && attributeGrp.equalsIgnoreCase("USAGE_FEE")) {
			attributesString = (String) dbCardDetails.get(attributeGrp);
			try {
				attributesMap = Util.jsonToSingleMap(attributesString);
			} catch (IOException e) {
				logger.error("Error Occured while trying to convert product attributes json to map, {}", e.getMessage(), e);
			}

		} else {
			logger.debug("There is no attributes found for {}", attributeGrp);
			return null;
		}
		logger.debug(LoggerConstants.EXIT);
		return attributesMap;

	}

	private void doPopulateCardDetails(Map<String, String> valueObj, Map<String, Object> dbCardDetails) {

		logger.debug(LoggerConstants.ENTER);
		if (!Objects.isNull(dbCardDetails.get("CARD_NUM_HASH"))) {
			valueObj.put(ValueObjectKeys.CARD_NUM_HASH, dbCardDetails.get("CARD_NUM_HASH")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_NUM_HASH, null);
		}

		// Profile Id
		if (!Objects.isNull(dbCardDetails.get("PRFL_CODE"))) {
			valueObj.put(ValueObjectKeys.PRFL_ID, dbCardDetails.get("PRFL_CODE")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.PRFL_ID, null);
		}

		// Card Number
		if (!Objects.isNull(dbCardDetails.get("CARD_NUMBER"))) {
			valueObj.put(ValueObjectKeys.CARD_NUMBER, dbCardDetails.get("CARD_NUMBER")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_NUMBER, null);
		}

		// Account ID
		if (!Objects.isNull(dbCardDetails.get("ACCOUNT_ID"))) {
			valueObj.put(ValueObjectKeys.CARD_ACCOUNT_ID, dbCardDetails.get("ACCOUNT_ID")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_ACCOUNT_ID, null);
		}

		// Customer Code
		if (!Objects.isNull(dbCardDetails.get("CUSTOMER_CODE"))) {
			valueObj.put(ValueObjectKeys.CUSTOMER_CODE, dbCardDetails.get("CUSTOMER_CODE")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CUSTOMER_CODE, null);
		}

		// Serial Number
		if (!Objects.isNull(dbCardDetails.get("SERIAL_NUMBER"))) {
			valueObj.put(ValueObjectKeys.CARD_SERIAL_NUMBER, dbCardDetails.get("SERIAL_NUMBER")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_SERIAL_NUMBER, null);
		}

		// Proxy Number
		if (!Objects.isNull(dbCardDetails.get("PROXY_NUMBER"))) {
			valueObj.put(ValueObjectKeys.PROXY_NUMBER, dbCardDetails.get("PROXY_NUMBER")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.PROXY_NUMBER, null);
		}

		// Card Status
		if (!Objects.isNull(dbCardDetails.get("CARD_STATUS"))) {
			valueObj.put(ValueObjectKeys.CARD_CARDSTAT, dbCardDetails.get("CARD_STATUS")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_CARDSTAT, null);
		}

		// Card Exp Date
		if (!Objects.isNull(dbCardDetails.get("EXPIRY_DATE"))) {
			valueObj.put(ValueObjectKeys.CARD_EXPDATE, dbCardDetails.get("EXPIRY_DATE")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_EXPDATE, null);
		}

		// Card Activation Date
		if (!Objects.isNull(dbCardDetails.get("DATE_OF_ACTIVATION"))) {
			valueObj.put(ValueObjectKeys.CARD_ACTIVATION_DATE, dbCardDetails.get("DATE_OF_ACTIVATION")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_ACTIVATION_DATE, null);
		}

		// First Time Top up flag
		if (!Objects.isNull(dbCardDetails.get("FIRSTTIME_TOPUP"))) {
			valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, dbCardDetails.get("FIRSTTIME_TOPUP")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "N");
		}

		// Old Card Status
		if (!Objects.isNull(dbCardDetails.get("OLD_CARDSTAT"))) {
			valueObj.put(ValueObjectKeys.OLD_CARD_STATUS, dbCardDetails.get("OLD_CARDSTAT")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.OLD_CARD_STATUS, null);
		}

		// Last Transaction Date
		/*
		 * if (!Objects.isNull(dbCardDetails.get("LAST_TXNDATE"))) { valueObj.put(ValueObjectKeys.LAST_TXN_DATE,
		 * dbCardDetails.get("LAST_TXNDATE").toString()); } else { valueObj.put(ValueObjectKeys.LAST_TXN_DATE, null); }
		 */

		// Is Redeemed
		if (!Objects.isNull(dbCardDetails.get("IS_REDEEMED"))) {
			valueObj.put(ValueObjectKeys.IS_REDEEMED, dbCardDetails.get("IS_REDEEMED")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.IS_REDEEMED, "N");
		}

		/* added by nawaz for getting issuer id and partnerid 28062018 */

		if (!Objects.isNull(dbCardDetails.get("ISSUER_ID"))) {
			valueObj.put(ValueObjectKeys.ISSUER_ID, dbCardDetails.get("ISSUER_ID")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.ISSUER_ID, "N");
		}

		if (!Objects.isNull(dbCardDetails.get("PARTNER_ID"))) {
			valueObj.put(ValueObjectKeys.PARTNER_ID, dbCardDetails.get("PARTNER_ID")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.PARTNER_ID, "N");
		}

		// Old Card Status
		if (!Objects.isNull(dbCardDetails.get(ValueObjectKeys.REPL_FLAG))) {
			valueObj.put(ValueObjectKeys.REPL_FLAG, dbCardDetails.get(ValueObjectKeys.REPL_FLAG)
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.REPL_FLAG, null);
		}

		if (!Objects.isNull(dbCardDetails.get("CARD_NUM_ENCR"))) {
			valueObj.put(ValueObjectKeys.CARD_NUM_ENCR, dbCardDetails.get("CARD_NUM_ENCR")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.CARD_NUM_ENCR, null);
		}

		if (!Objects.isNull(dbCardDetails.get("LAST4DIGIT"))) {
			valueObj.put(ValueObjectKeys.LAST_4DIGIT, dbCardDetails.get("LAST4DIGIT")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.LAST_4DIGIT, null);
		}
		// Account Number
		if (!Objects.isNull(dbCardDetails.get("ACCOUNT_NUMBER"))) {
			valueObj.put(ValueObjectKeys.ACCOUNT_NUMBER, dbCardDetails.get("ACCOUNT_NUMBER")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.ACCOUNT_NUMBER, null);
		}

		if (!Objects.isNull(dbCardDetails.get("DIGITAL_PIN"))) {
			valueObj.put(ValueObjectKeys.DIGITAL_PIN, dbCardDetails.get("DIGITAL_PIN")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.DIGITAL_PIN, null);
		}
		/** R08 changes added by venkateshgaddam starts **/
		if (!Objects.isNull(dbCardDetails.get("DB_SYSDATE"))) {
			valueObj.put(ValueObjectKeys.DB_SYSDATE, dbCardDetails.get("DB_SYSDATE")
				.toString());
		} else {
			valueObj.put(ValueObjectKeys.DB_SYSDATE, null);
		}
		/** R08 changes added by venkateshgaddam ends **/

		logger.debug(LoggerConstants.EXIT);
	}

	/**
	 * populating Supported purse from productAttributes
	 * 
	 * @param valueDto
	 * @throws ServiceException
	 * @author venkateshgaddam
	 */
	@Override
	public void doPopulateSupportedPurseDtls(ValueDTO valueDto) {

		logger.debug(GeneralConstants.ENTER);
		String purseId = valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID) == null ? String.valueOf(
					valueDto.getProductAttributes()
						.get("Product")
						.get("defaultPurse"))
					: valueDto.getValueObj()
						.get(ValueObjectKeys.PURSE_ID);

		List<SupportedPurse> supported = productService.getSupportedPurseDtls(valueDto);
		if (!CollectionUtils.isEmpty(supported)) {
			SupportedPurse accountPurseDtls = supported.stream()
				.filter(p -> purseId.equalsIgnoreCase(p.getPurseId()))
				.findAny()
				.orElseThrow(() -> new ServiceException(SpilExceptionMessages.PURSE_NOT_LINKED_WITH_PRODUCT,
						ResponseCodes.PURSE_NOT_LINKED_WITH_PRODUCT));

			valueDto.getValueObj()
				.put(ValueObjectKeys.PURSE_ID, accountPurseDtls.getPurseId());
			valueDto.getValueObj()
				.put(ValueObjectKeys.CURRENCY_ID, accountPurseDtls.getCurrencyId());
			valueDto.getValueObj()
				.put(ValueObjectKeys.TXN_CURRENCY_CODE, accountPurseDtls.getCurrencyCode());
			valueDto.getValueObj()
				.put(ValueObjectKeys.MINORUNITS, accountPurseDtls.getMinorUnits());
		} else {
			logger.info("Product does not contain purse value, purseId:" + purseId);
			throw new ServiceException(SpilExceptionMessages.PURSE_NOT_LINKED_WITH_PRODUCT, ResponseCodes.PURSE_NOT_LINKED_WITH_PRODUCT);
		}

		logger.info("doPopulateAccntPurseDtls validation success");
		logger.debug(GeneralConstants.EXIT);
	}

	@Transactional
	public String getCardNumber(Map<String, String> valueObj) {
		String cardNumber = "";
		if (MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
			cardNumber = cardDetailsDao.getCardNoByProductId(valueObj.get(ValueObjectKeys.PRODUCT_ID));
			cardDetailsDao.updateCardStatus(cardNumber, CardStatusConstants.DIGITAL_IN_PROCESS);
			valueObj.put(ValueObjectKeys.SPNUMBER, cardNumber);
		} else {
			cardNumber = cardDetailsDao.getCardNoByRefNum(valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER),
					valueObj.get(ValueObjectKeys.TRANS_CODE), valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
			valueObj.put(ValueObjectKeys.SPNUMBER, cardNumber);
		}
		return cardNumber;
	}

	@Override
	public ValueDTO getAccountPurseUsageDetails(String accountId, String purseId, ValueDTO valueDto) {
		logger.debug(GeneralConstants.ENTER);
		Map<String, Object> dbAccountUsage = null;
		Map<String, String> valueObj = valueDto.getValueObj();

		dbAccountUsage = cardDetailsDao.getAccountPurseUsageDetails(accountId, purseId);

		if (!CollectionUtils.isEmpty(dbAccountUsage)) {

			valueDto.setUsageLimit(getUsageLimitsAttributes(dbAccountUsage, GeneralConstants.USAGE_LIMIT));
			valueDto.setUsageFee(getUsageFeesAttributes(dbAccountUsage, GeneralConstants.USAGE_FEE));

			// Last Transaction Date
			if (!Objects.isNull(dbAccountUsage.get(GeneralConstants.LAST_TXNDATE))) {
				valueObj.put(ValueObjectKeys.LAST_TXN_DATE, dbAccountUsage.get(GeneralConstants.LAST_TXNDATE)
					.toString());
			} else {
				valueObj.put(ValueObjectKeys.LAST_TXN_DATE, null);
			}

			if (!Objects.isNull(dbAccountUsage.get(PurseAPIConstants.PURSE_STATUS))
					&& PurseAPIConstants.INACTIVE.equals(dbAccountUsage.get(PurseAPIConstants.PURSE_STATUS))) {
				/*
				 * Need to add proper response code
				 */
				throw new ServiceException(SpilExceptionMessages.ACCOUNTPURSENOTACTIVE, ResponseCodes.ACCOUNT_PURSE_NOT_ACTIVE);
			}

		} else {
			logger.info("No data found account purse usage/account purse for accountId:" + accountId + ", purseId: " + purseId);
			if (!SpilTranConstants.SPIL_ACTIVATION.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_RELOAD.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_VALUE_INSERTION.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_PREAUTH_ACTIVATION.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))
					&& !SpilTranConstants.SPIL_PRE_VALUE_INSERTION.equalsIgnoreCase(valueObj.get(ValueObjectKeys.TRANS_CODE))) {
				throw new ServiceException(SpilExceptionMessages.INVALID_CARD, ResponseCodes.INVALID_CARD);
			}
		}
		valueDto.setValueObj(valueObj);
		logger.debug(GeneralConstants.EXIT);
		return valueDto;
	}

	@Override
	public CardDetail getCardDetails(SpNumberType spNumberType, String spNumber) {
		return this.cardDetailsDao.getCardDetails(spNumberType, spNumber);
	}

	private Map<String, Object> getCardNumberFromBalTransferDetails(String rrn, String targetCardNumberInRequest) {
		return cardDetailsDao.getCardNumberFromBalTransferDetails(rrn, targetCardNumberInRequest);
	}

	@Override
	public int updateCard(String cardNumberHash, CardStatusType newCardStatus, CardStatusType oldCardStatus, LocalDateTime activationDate,
			Boolean firstTimeTopup) {
		return cardDetailsDao.updateCard(cardNumberHash, newCardStatus.getStatusCode(), oldCardStatus.getStatusCode(), activationDate,
				firstTimeTopup);
	}

}
