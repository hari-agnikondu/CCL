package com.incomm.cclp.service.impl;

import static com.incomm.cclp.account.util.CodeUtil.isNullOrEmpty;
import static com.incomm.cclp.account.util.CodeUtil.not;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.incomm.cclp.constants.PurseAPIConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CommonValidationsDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CommonValidationsService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.transaction.bean.SupportedPurse;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;
import com.incomm.cclp.util.HSMCommandBuilder;
import com.incomm.cclp.util.PadUtil;
import com.incomm.cclp.util.Util;

@Service
public class CommonValidationsServiceImpl implements CommonValidationsService {

	@Autowired
	HSMCommandBuilder hsmCommandBuilder;

	@Value("${CCF_HSM_IPADDRESS}")
	String hsmIpAddress;

	@Value("${CCF_HSM_PORT}")
	int hsmPort;

	@Autowired
	SpilDAO spilDao;

	private static final Logger logger = LogManager.getLogger(CommonValidationsServiceImpl.class);

	private static Map<String, String> cardStatusRespMsg;
	private static Map<String, String> cardStatusRespCode;

	static {
		cardStatusRespMsg = new HashMap<>();
		cardStatusRespCode = new HashMap<>();
		cardStatusRespMsg.put("ACTIVE", SpilExceptionMessages.ACTIVE_CARD);
		cardStatusRespMsg.put("LOST-STOLEN", SpilExceptionMessages.STOLEN_CARD);
		cardStatusRespMsg.put("DAMAGE", SpilExceptionMessages.DAMAGED_CARD);
		cardStatusRespMsg.put("EXPIRED CARD", SpilExceptionMessages.EXPIRED_CARD);
		cardStatusRespMsg.put("CLOSED", SpilExceptionMessages.CLOSED_CARD);

		cardStatusRespCode.put("ACTIVE", ResponseCodes.ACTIVE_CARD);
		cardStatusRespCode.put("LOST-STOLEN", ResponseCodes.STOLEN_CARD);
		cardStatusRespCode.put("DAMAGE", ResponseCodes.DAMAGED_CARD);
		cardStatusRespCode.put("EXPIRED CARD", ResponseCodes.EXPIRED_CARD);
		cardStatusRespCode.put("CLOSED", ResponseCodes.CLOSED_CARD);
	}

	@Autowired
	CommonValidationsDAO commonValidationsDao;

	@Autowired
	LocalCacheServiceImpl localCacheService;

	@Autowired
	TransactionService transactionService;

	@Autowired
	ProductService productService;

	/* Card status check validation */
	@Override
	public void cardStatusCheckValidation(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		logger.info("card status check..");

		Map<String, String> valueObj = valueDto.getValueObj();
		String cardStatusCode = valueObj.get(ValueObjectKeys.CARD_CARDSTAT);

		if (cardStatusCode != null) {

			Map<String, Map<String, Object>> productAttributes = valueDto.getProductAttributes();

			String channelShortName = valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME);

			String txnShortName = valueObj.get(ValueObjectKeys.ORIGINAL_MSGTYPE);

			String cardStatusDesc = getCardStatusDesc(cardStatusCode);

			String cardStatusAttributeKey = (channelShortName + "_" + txnShortName + "_" + cardStatusDesc);

			// get card status attribute
			Object txnCardStatusFlag = Util.getProductAttributeValue(productAttributes, "Card Status", cardStatusAttributeKey);

			if (txnCardStatusFlag == null)
				txnCardStatusFlag = "false";

			logger.info("card status flag:" + cardStatusAttributeKey + " " + txnCardStatusFlag);
			if (cardStatusDesc.equalsIgnoreCase("INACTIVE") && !txnCardStatusFlag.toString()
				.equalsIgnoreCase("true")) {
				if (checkConsumedCardStatus(valueDto, cardStatusCode)) {
					logger.info(GeneralConstants.CONSUMED_STATUS_MSG);
					throw new ServiceException(GeneralConstants.CONSUMED_STATUS_MSG, ResponseCodes.INVALID_CARD_STATE);
				} else {
					logger.info("Transaction not supported for inactive card status");
					throw new ServiceException(SpilExceptionMessages.INACTIVE_CARD, ResponseCodes.INACTIVE_CARD);
				}

			} else {
				/**
				 * card status attribute value is not true throw Service Exception
				 */
				if (!txnCardStatusFlag.toString()
					.equalsIgnoreCase("true")) {
					if (cardStatusRespCode.containsKey(cardStatusDesc.toUpperCase())) {
						logger.info(GeneralConstants.CARD_STATUS_CHECK_FAILED + " " + cardStatusRespMsg.get(cardStatusDesc.toUpperCase()));
						throw new ServiceException(cardStatusRespMsg.get(cardStatusDesc.toUpperCase()),
								cardStatusRespCode.get(cardStatusDesc.toUpperCase()));
					} else {
						logger.info(GeneralConstants.CARD_STATUS_CHECK_FAILED);
						throw new ServiceException(SpilExceptionMessages.INVALID_CARD_STATE, ResponseCodes.INVALID_CARD_STATE);
					}
				}
			}
		} else {
			logger.info(GeneralConstants.CARD_STATUS_CHECK_FAILED);
			throw new ServiceException(SpilExceptionMessages.INVALID_CARD_STATE, ResponseCodes.INVALID_CARD_STATE);
		}

		logger.info("card status check success");
		logger.debug(GeneralConstants.EXIT);
	}

	private boolean checkConsumedCardStatus(ValueDTO valueDto, String cardStatus) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		boolean result = false;
		String consumedFlag = String.valueOf(Util.getProductAttributeValue(valueDto.getProductAttributes(), "General", "consumedFlag"));
		String cardActivationDate = valueDto.getValueObj()
			.get(ValueObjectKeys.CARD_ACTIVATION_DATE);
		String replFlag = valueDto.getValueObj()
			.get(ValueObjectKeys.REPL_FLAG);
		String productType = String.valueOf(
				Util.getProductAttributeValue(valueDto.getProductAttributes(), ValueObjectKeys.PRODUCT, ValueObjectKeys.PRODUCT_TYPE_KEY));

		logger.debug("Product level Consumed Flag value : " + consumedFlag);
		logger.debug("Card status : " + cardStatus);
		logger.debug("Card Activation Date : " + cardActivationDate);
		logger.debug("ProductType : " + productType);

		if ("true".equalsIgnoreCase(consumedFlag) && Util.isEmpty(cardActivationDate) && !Util.isEmpty(replFlag) && "0".equals(replFlag)
				&& ValueObjectKeys.PRODUCT_TYPE_RETAIL.equalsIgnoreCase(productType)) {
			spilDao.updateCardStatus(String.valueOf(valueDto.getValueObj()
				.get(ValueObjectKeys.CARD_NUM_HASH)), cardStatus, "17");
			valueDto.getValueObj()
				.put(ValueObjectKeys.CARD_CARDSTAT, "17");
			valueDto.getValueObj()
				.put(ValueObjectKeys.OLD_CARD_STATUS, cardStatus);
			result = true;
		}

		logger.info("Consumed Status:" + result);
		logger.debug(GeneralConstants.EXIT);

		return result;
	}

	@Override
	public void spillCvv2Validation(ValueDTO valueDto) throws ServiceException {
		logger.debug("Validating CVV2 : ENTER");
		logger.info("CVV2 validation....");
		Map<String, String> valueObj = valueDto.getValueObj();
		String msPan = null;
		String cvv2 = null;

		if (!Util.isEmpty(valueObj.get(ValueObjectKeys.CARD_NUMBER)) && !Util.isEmpty(valueObj.get(ValueObjectKeys.PRODUCT_ID))
				&& !Util.isEmpty(valueObj.get(ValueObjectKeys.CARD_EXPDATE))) {
			msPan = valueObj.get(ValueObjectKeys.CARD_NUMBER);

			String cvvSupported = (String) Util.getProductAttributeValue(valueDto.getProductAttributes(), ValueObjectKeys.PRODUCT,
					"cvvSupported");
			if (!Util.isEmpty(cvvSupported) && cvvSupported.equalsIgnoreCase("Enable")) {

				cvv2 = String.valueOf(valueObj.get(ValueObjectKeys.CVV2));
				if (Util.isEmpty(cvv2)) {
					logger.info("Invalid cvv2 in request : {}", valueObj.get(ValueObjectKeys.PRODUCT_ID));
					throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
				}

				Object cvkA = Util.getProductAttributeValue(valueDto.getProductAttributes(), "CVV", "cvkA");
				Object cvkB = Util.getProductAttributeValue(valueDto.getProductAttributes(), "CVV", "cvkB");

				if (!ObjectUtils.isEmpty(cvkA) && !ObjectUtils.isEmpty(cvkB)) {

					String cvk = String.valueOf(cvkA) + String.valueOf(cvkB);

					if (!Util.isEmpty(cvk)) {
						String expDateMMYY = valueObj.get(ValueObjectKeys.CARD_EXPDATE)
							.substring(2, 4)
								+ valueObj.get(ValueObjectKeys.CARD_EXPDATE)
									.substring(0, 2);
						logger.debug("CVK for the product is {}", cvk);
						String responseCode = hsmCommandBuilder.verifyCVV(PadUtil.padRight(msPan, msPan.length(), ' '), expDateMMYY, "000",
								cvk, cvv2, hsmIpAddress, hsmPort);
						logger.info("HSM Response {}", responseCode);
						if (responseCode.trim()
							.equalsIgnoreCase("00")) {
							logger.info("CVV verification successful");
						} else if (responseCode.trim()
							.equalsIgnoreCase("01")) {
							logger.info("CVV Verification failed");
							throw new ServiceException(SpilExceptionMessages.CVV_VERIFY_FAIL, ResponseCodes.CVV_VERIFICATION_FAILED);
						} else {
							logger.info("Error occured while validating cvv2 for product : {}",
									valueObj.get(ValueObjectKeys.PRODUCT_ID) + " HSM Response code:" + responseCode);
							throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
						}

					} else {
						logger.warn("CVV2 validation not done, CVK is empty for product id : {}", valueObj.get(ValueObjectKeys.PRODUCT_ID));
					}
				} else {
					logger.warn("CVV Attribute is empty for product id : {}", valueObj.get(ValueObjectKeys.SPIL_PROD_ID));
				}

			} else {
				logger.warn("CVV is not enabled for this product");
			}
		} else {
			logger.error("CVV2 Verification is failed : {}", SpilExceptionMessages.INVALID_INPUT_DATA);
			throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
		}
		logger.info("CVV2 validation success");
		logger.debug("Validating CVV2 : EXIT");
	}

	@Override
	public void expiryDateCheck(String cardExpiryDate, String tranExpiryDate, String validationType) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		logger.info("Expiry Date check...");
		logger.debug("cardExpiryDate: ", cardExpiryDate);
		logger.debug("tranExpiryDate: ", tranExpiryDate);

		if (!Util.isEmpty(cardExpiryDate)) {
			if ("M".equalsIgnoreCase(validationType)) {
				if (!Objects.isNull(tranExpiryDate) && cardExpiryDate.equalsIgnoreCase(tranExpiryDate)) {
					logger.debug("Expiry date check validated successfully");
				} else {
					logger.info("Mandatory Expiry date check validation failed");
					throw new ServiceException(SpilExceptionMessages.INVALID_CARD, ResponseCodes.INVALID_CARD);
				}
			} else {
				if (!Objects.isNull(tranExpiryDate) && !Util.isEmpty(tranExpiryDate) && !cardExpiryDate.equalsIgnoreCase(tranExpiryDate)) {
					logger.info("Expiry date check validation failed");
					throw new ServiceException(SpilExceptionMessages.INVALID_CARD, ResponseCodes.INVALID_CARD);
				}
			}
		} else {
			logger.info("Expiry Date missing in card table");
			throw new ServiceException(SpilExceptionMessages.INVALID_EXPDATE, ResponseCodes.INVALID_REQUEST);
		}
		logger.info("Expiry Date check success");
		logger.debug(GeneralConstants.EXIT);
	}

	@Override
	public void passiveStatusValidation(Map<String, String> valueObj) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		logger.info("Verifying the Passive Status of the flag...");
		String cardStatus = valueObj.get(ValueObjectKeys.CARD_CARDSTAT);
		if (!Util.isEmpty(valueObj.get(ValueObjectKeys.CARD_CARDSTAT))
				&& !Util.isEmpty(valueObj.get(ValueObjectKeys.PASSIVE_SUPPORT_FLAG))) {

			// stored in constant
			String oldCardStatus = String.valueOf(valueObj.get(ValueObjectKeys.OLD_CARD_STATUS));
			String passiveCheckFlag = valueObj.get(ValueObjectKeys.PASSIVE_SUPPORT_FLAG);

			if (passiveCheckFlag.equals("Y") && cardStatus.equals("8")) {
				valueObj.replace(ValueObjectKeys.CARD_CARDSTAT, oldCardStatus);
				valueObj.replace(ValueObjectKeys.OLD_CARD_STATUS, cardStatus);
			}
		} else {
			logger.error("Error occured while validating the passive status of the card, passive flag or card status is empty");
			throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
		}
		logger.info("Passive Status check success");
		logger.debug(GeneralConstants.EXIT);
	}

	@Override
	public void upcValidation(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		logger.info("UPC validation");
		Map<String, String> valueObj = valueDto.getValueObj();

		String spilUpcCode = valueObj.get(ValueObjectKeys.SPIL_UPC_CODE);

		Object retailUpc = Util.getProductAttributeValue(valueDto.getProductAttributes(), ValueObjectKeys.PRODUCT, "retailUPC");
		Object b2bUpc = Util.getProductAttributeValue(valueDto.getProductAttributes(), ValueObjectKeys.PRODUCT, "b2bUpc");

		String productUpcCode = "";

		if (!Objects.isNull(retailUpc)) {
			productUpcCode = retailUpc.toString();
		} else if (!Objects.isNull(b2bUpc)) {
			productUpcCode = b2bUpc.toString();
		}

		if (!Objects.isNull(spilUpcCode)) {

			if (!Util.isEmpty(productUpcCode)) {
				if (productUpcCode.equals(spilUpcCode)) {
					logger.debug("UPC validation check succssfully passed");
				} else {
					logger.info("Invalid UPC");
					throw new ServiceException(SpilExceptionMessages.INVALID_UPC, ResponseCodes.INVALID_UPC);
				}
			} else {
				logger.warn("Product does not contain UPC code value");
				throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
			}

		}
		logger.info("UPC validation success");
		logger.debug(GeneralConstants.EXIT);
	}

	@Override
	public void instrumentTypeCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		logger.info("instrument type check...");
		String instrumentType = null;
		String spNumberType = null;
		String[] transactionTypes = null;
		String msgType = null;
		Map<String, String> valueObj = valueDto.getValueObj();

		msgType = valueObj.get(ValueObjectKeys.ORIGINAL_MSGTYPE);

		if (msgType.equalsIgnoreCase("act")) {

			Object activationInstruments = Util.getProductAttributeValue(valueDto.getProductAttributes(), ValueObjectKeys.PRODUCT,
					ValueObjectKeys.ACTIVATION_ID);

			if (!Objects.isNull(activationInstruments)) {
				instrumentType = activationInstruments.toString();
			} else {
				logger.error("Invalid SP Number Type Check");
				throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
			}
		} else {

			Object otherTranInstruments = Util.getProductAttributeValue(valueDto.getProductAttributes(), ValueObjectKeys.PRODUCT,
					ValueObjectKeys.OTHER_TXN_ID);

			if (!Objects.isNull(otherTranInstruments)) {
				instrumentType = otherTranInstruments.toString();
			} else {
				logger.error("Invalid SP Number Type Check");
				throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
			}
		}

		transactionTypes = instrumentType.split(",");

		List<String> msgTypeList = new ArrayList<>(Arrays.asList(transactionTypes));

		String spNumber = valueObj.get(ValueObjectKeys.SPNUMBER);

		spNumberType = getSpNumberType(valueObj, spNumber);

		if (msgTypeList.contains(spNumberType)) {
			logger.debug("Transaction should be allowed for SP Number Type");
		} else {
			logger.error("Transaction should not be allowed for SP Number Type");
			throw new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_NOT_ALLOWED, ResponseCodes.INVALID_REQUEST);
		}
		logger.info("instrument type check success");
		logger.debug(GeneralConstants.EXIT);
	}

	public String getSpNumberType(Map<String, String> valueObj, String spNumber) {
		String spNumberType = "";
		if (spNumber.equals(valueObj.get(ValueObjectKeys.CARD_NUMBER))) {
			spNumberType = ValueObjectKeys.SP_NUM_TYPE_PAN;
		} else if (spNumber.equals(valueObj.get(ValueObjectKeys.PROXY_NUMBER))) {

			spNumberType = ValueObjectKeys.SP_NUM_TYPE_PROXY_NUMBER;
		} else if (spNumber.equals(valueObj.get(ValueObjectKeys.CARD_SERIAL_NUMBER))) {
			spNumberType = ValueObjectKeys.SP_NUM_TYPE_SERIAL_NUMBER;

		} else if (spNumber.equals(valueObj.get(ValueObjectKeys.ACCOUNT_NUMBER))) {
			spNumberType = ValueObjectKeys.SP_NUM_TYPE_ACCOUNT_NUMBER;
		} else {
			spNumberType = ValueObjectKeys.SP_NUM_TYPE_CUSTOMER_ID;
		}
		return spNumberType;
	}

	/**
	 * First Party Third Party Check
	 * 
	 * @param valueDto
	 * @throws ServiceException
	 * @author venkateshgaddam
	 */
	@Override
	public void firstPartyThirdPartyCheck(ValueDTO valueDto) throws ServiceException {
		Map<String, String> valueObj = valueDto.getValueObj();
		String firstThirdPartyFlag = null;
		String mdmId = productService.getMdmId(valueObj.get(ValueObjectKeys.PARTNER_ID));
		String spilMdmId = valueObj.get(ValueObjectKeys.MDM_ID);

		if (mdmId.equals(spilMdmId)) {
			logger.info("firstPartyThirdPartyCheck success...");
		} else {
			Object partyFlag = Util.getProductAttributeValue(valueDto.getProductAttributes(), ValueObjectKeys.PARTY_TYPE,
					valueObj.get(ValueObjectKeys.PRODUCT_ID) + "_" + valueObj.get(ValueObjectKeys.MDM_ID));

			if (!ObjectUtils.isEmpty(partyFlag)) {
				firstThirdPartyFlag = (String) partyFlag;
			}

			if (Util.isEmpty(firstThirdPartyFlag)) {
				throw new ServiceException(SpilExceptionMessages.TRANSACTION_NOT_SUPPORTED_ON_PARTNER,
						ResponseCodes.TRANSACTION_NOT_SUPPORTED_ON_PARTNER);
			}

			String partySupported = valueObj.get(ValueObjectKeys.PARTY_SUPPORTED);
			if (partySupported != null && !partySupported.equals(firstThirdPartyFlag) && !"BOTH".equals(partySupported)) {
				throw new ServiceException(SpilExceptionMessages.TRANSACTION_NOT_SUPPORTED_ON_PARTNER,
						ResponseCodes.TRANSACTION_NOT_SUPPORTED_ON_PARTNER);
			}
			logger.info("firstPartyThirdPartyCheck success...");
		}
	}

	/**
	 * This provides Currency Code Validation. Fetching the SupportedPurseDetails from ProductAttributes in Cache.
	 * 
	 * @param valueDto
	 * @throws ServiceException
	 * @author venkateshgaddam
	 */
	@Override
	public void currencyCodeValidation(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		Map<String, String> valueObj = valueDto.getValueObj();
		String minorUnit = valueObj.get(ValueObjectKeys.MINORUNITS);
		if (valueObj.containsKey(ValueObjectKeys.PURAUTHREQ_PURSE_NAME)
				&& !Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME))) {
			String txnCurrency = valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY);
			String tranAmt = valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT);
			String skuCode = valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE);

			String purseType = valueDto.getValueObj()
				.get(ValueObjectKeys.PURSE_TYPE_ID);

			String skipCurrencyCheckIfNull = valueObj.get(ValueObjectKeys.SKIP_CURRENCY_CHECK_IF_NULL);
			boolean skipCurrencyCheck = false;

			if (TransactionConstant.TRUE.equals(skipCurrencyCheckIfNull) && isNullOrEmpty(txnCurrency)) {
				skipCurrencyCheck = true;
				logger.info("Currency code check skipped as transaction currency is null and skip flag is set as true.");
			}

			switch (purseType) {
			case PurseAPIConstants.CONSUMER_FUNDED_CURRENCY:
			case PurseAPIConstants.PARTNER_FUNDED_CURRENCY:
				int minorDigits = Integer.parseInt(minorUnit);
				String purseCurrency = valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE);

				if (not(skipCurrencyCheck) && txnCurrency != null && !txnCurrency.equalsIgnoreCase(purseCurrency)) {
					logger.info("Currency is not matched, transaction currency: " + txnCurrency + ", purse currency:" + purseCurrency);
					throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_CURRENCY, ResponseCodes.INVALID_CURRENCY_CODE);
				} else if (tranAmt.contains(".") && tranAmt.split("\\.")[1].length() > minorDigits) {
					logger.info("Minor units are not matched ");
					throw new ServiceException(SpilExceptionMessages.INVALID_AMOUNT, ResponseCodes.INVALID_AMOUNT);
				}
				break;
			case PurseAPIConstants.POINTS_ID:
				validateCurrencyAndAmount(txnCurrency, tranAmt, skuCode, PurseAPIConstants.POINTS_ID, skipCurrencyCheck);
				break;
			case PurseAPIConstants.SKU_ID:
				validateCurrencyAndAmount(txnCurrency, tranAmt, skuCode, PurseAPIConstants.SKU_ID, skipCurrencyCheck);
				break;
			default:
				logger.error("Default condition in currency validation");
				throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_CURRENCY, ResponseCodes.INVALID_CURRENCY_CODE);
			}
		} else {
			logger.info("Currency code validation for default purse");
			List<SupportedPurse> supported = productService.getSupportedPurseDtls(valueDto);
			if (!CollectionUtils.isEmpty(supported)) {
				supported.stream()
					.filter(p -> valueDto.getValueObj()
						.get(ValueObjectKeys.TXN_CURRENCY_CODE)
						.equalsIgnoreCase(p.getCurrencyCode()))
					.findAny()
					.orElseThrow(
							() -> new ServiceException(SpilExceptionMessages.SPIL_INVALID_CURRENCY, ResponseCodes.INVALID_CURRENCY_CODE));

			} else {
				logger.error("Product does not contain currency code value");
				throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_REQUEST, ResponseCodes.INVALID_REQUEST);
			}

			logger.info("Currency code validation success");
			logger.debug(GeneralConstants.EXIT);
		}
	}

	@Override
	public void purseValidityCheck(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		String purseId = valueDto.getValueObj()
			.get(ValueObjectKeys.PURSE_ID);

		String defaultPurse = (String) valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT)
			.get(ValueObjectKeys.DEFAULT_PURSE);
		if (!defaultPurse.equals(purseId)) {
			Map<String, Object> purseMap = valueDto.getProductAttributes()
				.get(ValueObjectKeys.PURSE);

			String purseActiveDateStr = (purseMap != null && purseMap.containsKey("purseActiveDate"))
					? String.valueOf(purseMap.get("purseActiveDate"))
					: null;
			String purseValidityPeriod = (purseMap != null && purseMap.containsKey("purseValidityPeriod"))
					? String.valueOf(purseMap.get("purseValidityPeriod"))
					: null;
			purseValidityDateCheck(purseActiveDateStr, purseValidityPeriod);
		} else {
			logger.debug("Validity check Skipped due to Default Purse");
		}
		logger.debug(GeneralConstants.EXIT);
	}

	public void purseValidityDateCheck(String purseActiveDateStr, String purseValidityPeriod) {
		LocalDateTime currentDate = LocalDateTime.now()
			.truncatedTo(ChronoUnit.SECONDS);
		LocalDateTime purseValidityDate = null;
		LocalDateTime purseActiveDate = null;
		if (Util.isEmpty(purseValidityPeriod)) {
			purseValidityDate = currentDate;
		} else {
			purseValidityDate = Util.getMMDDYYYYHHMMSS(purseValidityPeriod);
		}

		if (Util.isEmpty(purseActiveDateStr)) {
			purseActiveDate = currentDate;
		} else {
			purseActiveDate = Util.getMMDDYYYYHHMMSS(purseActiveDateStr);
		}

		if (!currentDate.isEqual(purseValidityDate) && (currentDate.isAfter(purseValidityDate) || currentDate.isBefore(purseActiveDate))) {
			logger.info("Product Purse Validity date is not between the Active and expiry date");
			throw new ServiceException(SpilExceptionMessages.PROD_PURSE_VALIDITY, ResponseCodes.PROD_PURSE_VALIDITY);
		}

	}

	public void validateCurrencyAndAmount(String txnCurrency, String tranAmt, String skuCode, String purseTypeId,
			boolean skipCurrencyCheck) {

		if (tranAmt.contains(".")) {
			logger.info("Amount Contains Decimal Value");
			throw new ServiceException(SpilExceptionMessages.INVALID_AMOUNT, ResponseCodes.INVALID_AMOUNT);
		}

		if (not(skipCurrencyCheck) && PurseAPIConstants.POINTS_ID.equals(purseTypeId)
				&& !PurseAPIConstants.POINTS.equalsIgnoreCase(txnCurrency)) {
			logger.info("Points purse does not have an valid currency");
			throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_CURRENCY, ResponseCodes.INVALID_CURRENCY_CODE);
		} else if (PurseAPIConstants.SKU_ID.equals(purseTypeId)) {
			if (not(skipCurrencyCheck) && !PurseAPIConstants.SKU.equalsIgnoreCase(txnCurrency)) {
				logger.info("SKU purse does not have an valid currency");
				throw new ServiceException(SpilExceptionMessages.SPIL_INVALID_CURRENCY, ResponseCodes.INVALID_CURRENCY_CODE);
			} else if (Util.isEmpty(skuCode)) {
				throw new ServiceException("Data Element Name " + PurseAPIConstants.SKU_CODE + " is Invalid",
						ResponseCodes.INVALID_REQUEST);
			}
		}
	}

	public void transactionAllowedCheck(String transactionCode, String cardNumber) {

		if (SpilTranConstants.SPIL_DEACTIVATION.equals(transactionCode)) {
			int countInStmtLog = spilDao.getCountInStatementLog(cardNumber);
			if (countInStmtLog > 0)
				throw new ServiceException(SpilExceptionMessages.DEACTIVATION_NOT_ALLOWED, ResponseCodes.INVALID_REQUEST);

			int countInTxnLog = spilDao.getCountInTransactiontLog(cardNumber);
			if (countInTxnLog > 0)
				throw new ServiceException(SpilExceptionMessages.DEACTIVATION_NOT_ALLOWED, ResponseCodes.INVALID_REQUEST);
		}
	}

	public String getCardStatusDesc(String cardStatusCode) {
		// form a key to get card status attribute
		Map<String, String> cardStatusDefs = null;

		cardStatusDefs = localCacheService.getAllCardStatus(cardStatusDefs);
		if (CollectionUtils.isEmpty(cardStatusDefs)) {
			cardStatusDefs = transactionService.getAllCardStatus();
			localCacheService.getAllCardStatus(cardStatusDefs);
		}
		return cardStatusDefs.get(cardStatusCode);
	}

}
