package com.incomm.cclp.transaction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.MsgTypeConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.CommonValidationsDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CommonValidationsService;
import com.incomm.cclp.transaction.bean.CardStatus;
import com.incomm.cclp.transaction.bean.RuleParameter;
import com.incomm.cclp.transaction.bean.RuleParameterMapping;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.bean.Transactions;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.dao.TransactionDAO;
import com.incomm.cclp.transaction.service.TransactionService;

/**
 * Transaction Service provides all the Service operations for Transactions service impl.
 * 
 * @author venkateshgaddam
 */

@Service
/* @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS) */
public class TransactionServiceImpl implements TransactionService {

	Logger logger = LogManager.getLogger(TransactionServiceImpl.class);

	@Autowired
	TransactionDAO transactionDAO;

	@Autowired
	CommonValidationsDAO commonValidationsDao;

	@Autowired
	CommonValidationsService commonValidationService;

	@Autowired
	CacheManager cacheManager;
	@Autowired
	LocalCacheServiceImpl cacheServiceImpl;

	public static final String INPUT_DATA_VALIDATION_CACHE = "InputDataValidations";

	/**
	 * Loading input validations based on delivery channel in local cache on refresh
	 */
	public Map<String, List<TransactionInputValidation>> loadTxnInputValidations(String deliveryChannel) throws ServiceException {
		Map<String, List<TransactionInputValidation>> inputValidationsMap = new HashMap<>();
		try {
			List<TransactionInputValidation> inputValidationsList = transactionDAO.getTransactionInputValidations(deliveryChannel);
			List<TransactionInputValidation> inputValidationsListInner = new ArrayList<>();
			inputValidationsListInner.addAll(inputValidationsList);

			for (Iterator<TransactionInputValidation> iterator = inputValidationsList.iterator(); iterator.hasNext();) {
				List<TransactionInputValidation> validationsList = new ArrayList<>();
				TransactionInputValidation transactionInputValidation = iterator.next();
				String key = "";
				for (Iterator<TransactionInputValidation> iteratorInner = inputValidationsListInner.iterator(); iteratorInner.hasNext();) {
					TransactionInputValidation transactionInputValidationInner = iteratorInner.next();
					if (transactionInputValidation.getTransactionCode()
						.equals(transactionInputValidationInner.getTransactionCode())
							&& transactionInputValidation.getMsgType()
								.equals(transactionInputValidationInner.getMsgType())
							&& transactionInputValidation.getChannelCode()
								.equals(transactionInputValidationInner.getChannelCode())) {
						key = transactionInputValidation.getTransactionCode() + "_" + transactionInputValidation.getMsgType() + "_"
								+ transactionInputValidation.getChannelCode();
						validationsList.add(transactionInputValidationInner);
						iteratorInner.remove();
					}
				}
				if (!validationsList.isEmpty()) {
					inputValidationsMap.put(key, validationsList);
				}
			}

		} catch (Exception e) {
			logger.error("Error occured in loadTxnInputValidations:" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		return inputValidationsMap;

	}

	/**
	 * Getting input validations list based on Transaction_code and MessageType return List<TransactionInputValidation>
	 */
	public List<TransactionInputValidation> getInputValidations(Map<String, String> valueObj) throws ServiceException {
		List<TransactionInputValidation> inputValidations = null;
		try {
			Map<String, List<TransactionInputValidation>> inputValidationMap = cacheServiceImpl.getInputValidations(null);
			if (CollectionUtils.isEmpty(inputValidationMap)) {
				inputValidationMap = loadTxnInputValidations(valueObj.get(ValueObjectKeys.DELIVERYCHNL));

			}

			if (inputValidationMap.containsKey(valueObj.get(ValueObjectKeys.TRANS_CODE) + "_" + valueObj.get(ValueObjectKeys.MSGTYPE) + "_"
					+ valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE))) {
				inputValidations = inputValidationMap.get(valueObj.get(ValueObjectKeys.TRANS_CODE) + "_"
						+ valueObj.get(ValueObjectKeys.MSGTYPE) + "_" + valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
			}

		} catch (Exception e) {
			logger.error("Error occured in getInputValidations:" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		return inputValidations;
	}

	/**
	 * Getting Spil mesage Type Bean based on spilMsgType from request
	 * 
	 * @param spilMsgType
	 * @return SpilStartupMsgTypeBean
	 */
	public SpilStartupMsgTypeBean getSpilMessageTypeBean(String spilMsgType) throws ServiceException {
		SpilStartupMsgTypeBean spilStartupMsgTypeBean = null;
		try {
			Map<String, SpilStartupMsgTypeBean> spilStartupMsgTypeBeanMap = cacheServiceImpl.getSpilMessageTypeBean(null);
			if (spilStartupMsgTypeBeanMap != null && !spilStartupMsgTypeBeanMap.isEmpty()
					&& spilStartupMsgTypeBeanMap.containsKey(spilMsgType)) {
				spilStartupMsgTypeBean = spilStartupMsgTypeBeanMap.get(spilMsgType);
			}
		} catch (Exception e) {
			logger.error("Error occured in getSpilMessageTypeBean:" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		return spilStartupMsgTypeBean;
	}

	@Override
	public Map<String, List<TransactionAuthDefinition>> getAllTransactionAuthDefinitions() {
		logger.debug(GeneralConstants.ENTER);

		List<TransactionAuthDefinition> listOfTxnAuthDefinition = transactionDAO.getAllTransactionAuthDefinitions();

		Map<String, List<TransactionAuthDefinition>> mapTxnAuthDefinitions = new HashMap<>();

		if (!CollectionUtils.isEmpty(listOfTxnAuthDefinition)) {
			listOfTxnAuthDefinition.stream()
				.forEach(txnAuthDefinition -> {
					List<TransactionAuthDefinition> txnAuthDefs = null;
					String authDefKey = (txnAuthDefinition.getTransactionCode() + txnAuthDefinition.getMsgType()
							+ txnAuthDefinition.getChannelCode());

					if (mapTxnAuthDefinitions.containsKey(authDefKey)) {
						txnAuthDefs = mapTxnAuthDefinitions.get(authDefKey);
						txnAuthDefs.add(txnAuthDefinition);
						mapTxnAuthDefinitions.replace(authDefKey, txnAuthDefs);
					} else {
						txnAuthDefs = new ArrayList<>();
						txnAuthDefs.add(txnAuthDefinition);
						mapTxnAuthDefinitions.put(authDefKey, txnAuthDefs);
					}
				});
		}

		logger.debug(GeneralConstants.EXIT);
		return mapTxnAuthDefinitions;
	}

	@Override
	public Map<String, SpilResponseCode> getSpilResponseCodes(String deliveryChannel) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		Map<String, SpilResponseCode> mapSpilResponseCode = new HashMap<>();
		try {
			List<SpilResponseCode> listOfSpilResponseCodes = transactionDAO.getSpilResponseCodes(deliveryChannel);

			if (!CollectionUtils.isEmpty(listOfSpilResponseCodes)) {
				listOfSpilResponseCodes.stream()
					.forEach(responseCode -> mapSpilResponseCode.put(responseCode.getResponseCode(), responseCode));
			}
		} catch (Exception e) {
			logger.error("Error occured in getSpilResponseCodes:" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);
		return mapSpilResponseCode;
	}

	public SpilResponseCode getSpilResponseCode(String internalRespCode) {
		logger.debug(GeneralConstants.ENTER);
		SpilResponseCode spilResponseCode = null;
		try {
			Map<String, SpilResponseCode> responeCodeMap = cacheServiceImpl.getSpilResponseCode(null);
			if (!CollectionUtils.isEmpty(responeCodeMap)) {
				spilResponseCode = responeCodeMap.get(internalRespCode);
			}

		} catch (Exception e) {
			logger.error("Error occured in getSpilResponseCode:" + e.getMessage(), e);
			spilResponseCode = new SpilResponseCode("10029", "System Error");
		}
		logger.debug(GeneralConstants.EXIT);
		return spilResponseCode;
	}

	@Override
	public Map<String, String> getAllCardStatus() throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		Map<String, String> cardStatusDefs = new HashMap<>();

		try {
			List<CardStatus> listCardStatusDef = transactionDAO.getAllCardStatus();
			if (!CollectionUtils.isEmpty(listCardStatusDef)) {
				listCardStatusDef.stream()
					.forEach(cardStatus -> cardStatusDefs.put(cardStatus.getStatusCode(), cardStatus.getStatusDesc()));
			}
		} catch (Exception e) {
			logger.error("Error occured in get card status definitions: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);

		return cardStatusDefs;
	}

	@Override
	public Map<String, String> getAllRuleParameters() throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		Map<String, String> ruleParameters = new HashMap<>();

		try {
			List<RuleParameter> listRuleParameter = transactionDAO.getAllRuleParameters();
			if (!CollectionUtils.isEmpty(listRuleParameter)) {
				listRuleParameter.stream()
					.forEach(ruleparam -> ruleParameters.put(ruleparam.getParameterId(), ruleparam.getParameterType()));
			}
		} catch (Exception e) {
			logger.error("Error occured in get rule parameters: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);

		return ruleParameters;
	}

	@Override
	public Map<String, String> getAllRuleParametersMapping() throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		Map<String, String> ruleParametersMapping = new HashMap<>();

		try {
			List<RuleParameterMapping> listRuleParamDef = transactionDAO.getAllRuleParametersMapping();
			if (!CollectionUtils.isEmpty(listRuleParamDef)) {
				listRuleParamDef.stream()
					.forEach(ruleparam -> ruleParametersMapping.put(ruleparam.getParameterId(), ruleparam.getParameterMapping()));
			}
		} catch (Exception e) {
			logger.error("Error occured in get rule parameters Mapping: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);

		return ruleParametersMapping;
	}

	/**
	 * Duplicate check based on RRN,DELIVERY_CHANNEL,TRANSACTION_DATE,MSG_TYPE,RESPONSE_ID
	 * 
	 * @param valueDto
	 * @throws ServiceException
	 * @author venkateshgaddam
	 * @throws ServiceException
	 */
	public void duplicateCheckProductBased(ValueDTO valueDto) throws ServiceException {
		logger.info(GeneralConstants.ENTER);

		Map<String, String> valueObj = valueDto.getValueObj();
		try {
			Map<String, String> dbTxnLogDetls = transactionDAO.getTxnLogDtls(valueObj);
			if (MsgTypeConstants.MSG_TYPE_NORMAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
				if (!CollectionUtils.isEmpty(dbTxnLogDetls)) {
					throw new ServiceException(SpilExceptionMessages.DUPLICATE_RRN, ResponseCodes.DUPLICATE_RRN_OR_REQUEST);
				}
			} else if (MsgTypeConstants.MSG_TYPE_REVERSAL.equals(valueObj.get(ValueObjectKeys.MSGTYPE))) {
				if (!CollectionUtils.isEmpty(dbTxnLogDetls)) {
					if (GeneralConstants.TRAN_REVERSAL_FLAG.equals(dbTxnLogDetls.get(ValueObjectKeys.REVERSAL_FLAG))) {
						throw new ServiceException(SpilExceptionMessages.REVERSAL_ALREADY_DONE,
								ResponseCodes.NOT_REVERSIBLE_OR_ALREADY_REVERSED);
					}
					logger.info("Original Tran Amount : " + valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)
							+ "Txn Log Original Transaction Amount" + dbTxnLogDetls.get(ValueObjectKeys.ORGNL_TRAN_REQ_AMNT));
					if (valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT) != null
							&& dbTxnLogDetls.get(ValueObjectKeys.ORGNL_TRAN_REQ_AMNT) != null
							&& Double.valueOf(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)) > Double
								.valueOf(dbTxnLogDetls.get(ValueObjectKeys.ORGNL_TRAN_REQ_AMNT))) {
						throw new ServiceException(SpilExceptionMessages.REVERSAL_AMT_EXCEEDS, ResponseCodes.INVALID_AMOUNT);
					}
					valueObj.putAll(dbTxnLogDetls);
				} else {
					throw new ServiceException(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND,
							ResponseCodes.ORIGINAL_TRANSACTION_NOT_FOUND);
				}
			} else {
				throw new ServiceException(SpilExceptionMessages.SPIL_MSG_TYPE_INVALID, ResponseCodes.INVALID_REQUEST);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Error in duplicateCheck:" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		logger.info(GeneralConstants.EXIT);
	}

	@Override
	public Map<String, Transactions> getSpilTransactionCodes(String deliveryChannel) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		Map<String, Transactions> mapSpilResponseCode = new HashMap<>();
		try {
			List<Transactions> listOfSpilResponseCodes = transactionDAO.getSpilTransactionCodes(deliveryChannel);

			logger.debug("Retrived Spil Response Codes : {}" + listOfSpilResponseCodes);
			if (!CollectionUtils.isEmpty(listOfSpilResponseCodes)) {
				listOfSpilResponseCodes.stream()
					.forEach(responseCode -> mapSpilResponseCode.put(responseCode.getTransactionShortName(), responseCode));
			}
		} catch (Exception e) {
			logger.error("Error in getSpilTransactionCodes:" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug("spil response code  maped based on responseCode : {}", mapSpilResponseCode);
		logger.debug(GeneralConstants.EXIT);
		return mapSpilResponseCode;
	}

}
