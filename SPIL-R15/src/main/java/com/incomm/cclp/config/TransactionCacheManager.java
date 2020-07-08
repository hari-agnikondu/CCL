package com.incomm.cclp.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.bean.Transactions;
import com.incomm.cclp.transaction.dao.TransactionDAO;
import com.incomm.cclp.transaction.service.TransactionService;

/**
 * TransactionCacheManager prepopulate transaction data to Cache
 * 
 * @author venkateshgaddam
 */

@Configuration
public class TransactionCacheManager {

	@Autowired
	TransactionDAO transactionDAO;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	TransactionService transactionService;

	private final Logger logger = LogManager.getLogger(this.getClass());

	public static final String INPUT_DATA_VALIDATION_CACHE = "InputDataValidations";

	public static final String SPIL_MESSAGE_TYPE_BEAN_CACHE = "SpilMessageTypeBean";

	public static final String TRANSACTION_AUTH_DEFINITION_CACHE = "TransactionAuthDefinition";

	public static final String SPIL_RESPONSE_CODE_CACHE = "SpilResponseCode";

	public static final String CARD_STATUS = "CardStatus";

	public static final String RULE_PARAMETERS = "RuleParameters";

	public static final String RULE_PARAMETERS_MAPPING = "ruleParametersMapping";

	public static final String TRANSACTIONS = "transactions";

	/**
	 * Prepopulate transaction input validations to cache
	 * 
	 */
	@PostConstruct
	public void loadInputValidationDefinitionToLocalCache() throws ServiceException {
		logger.debug("Entered loadInputValidationDefinitionToLocalCache");
		String deliveryChannel = "01";
		Map<String, List<TransactionInputValidation>> inputValidationsMap = transactionService.loadTxnInputValidations(deliveryChannel);
		cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
			.put(INPUT_DATA_VALIDATION_CACHE, inputValidationsMap);
		logger.debug("Exit loadInputValidationDefinitionToLocalCache");
	}

	/**
	 * Prepopulate spil message Type bean to cache
	 * 
	 */
	@PostConstruct
	public void loadSpilMsgTypeBean() {
		logger.debug("Entered loadSpilMsgTypeBean");
		String deliveryChannel = "01";
		List<SpilStartupMsgTypeBean> spilStartupMsgTypeBeanList = transactionDAO.getSpilMessageType(deliveryChannel);

		Map<String, SpilStartupMsgTypeBean> spilStartupMsgTypeBeanMap = new HashMap<>();
		if (spilStartupMsgTypeBeanList != null && !spilStartupMsgTypeBeanList.isEmpty()) {
			for (Iterator<SpilStartupMsgTypeBean> iterator = spilStartupMsgTypeBeanList.iterator(); iterator.hasNext();) {
				SpilStartupMsgTypeBean spilStartupMsgTypeBean = iterator.next();
				spilStartupMsgTypeBeanMap.put(spilStartupMsgTypeBean.getSpilMsgType() + spilStartupMsgTypeBean.getDeliveryChannel(),
						spilStartupMsgTypeBean);
			}
			cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
				.put(SPIL_MESSAGE_TYPE_BEAN_CACHE, spilStartupMsgTypeBeanMap);
		}
		logger.debug("Exit loadSpilMsgTypeBean");
	}

	/**
	 * Prepopulate Transaction Auth Definitions to cache
	 * 
	 */
	@PostConstruct
	public void loadTransactionAuthDefinitionToLocalCache() {
		Map<String, List<TransactionAuthDefinition>> mapTxnAuthDefinitions = transactionService.getAllTransactionAuthDefinitions();
		cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
			.put(TRANSACTION_AUTH_DEFINITION_CACHE, mapTxnAuthDefinitions);

	}

	/**
	 * Prepopulate Response Codes to cache
	 * 
	 */
	@PostConstruct
	public void loadResponseCodesToLocalCache() throws ServiceException {
		String deliveryChannel = "01";
		Map<String, SpilResponseCode> mapSpilResponseCode = transactionService.getSpilResponseCodes(deliveryChannel);
		cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
			.put(SPIL_RESPONSE_CODE_CACHE, mapSpilResponseCode);
	}

	/**
	 * Prepopulate Card status to cache
	 * 
	 */
	@PostConstruct
	public void loadCardStatusToLocalCache() throws ServiceException {
		Map<String, String> mapCardStatus = transactionService.getAllCardStatus();
		cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
			.put(CARD_STATUS, mapCardStatus);
	}

	/**
	 * Prepopulate Rule Parameter to cache
	 * 
	 */
	@PostConstruct
	public void loadRuleParametersToLocalCache() throws ServiceException {
		Map<String, String> ruleParameters = transactionService.getAllRuleParameters();
		cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
			.put(RULE_PARAMETERS, ruleParameters);
	}

	@PostConstruct
	public void loadRuleParametersMappingToLocalCache() throws ServiceException {
		Map<String, String> ruleParametersMapping = transactionService.getAllRuleParametersMapping();
		cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
			.put(RULE_PARAMETERS_MAPPING, ruleParametersMapping);
	}

	/**
	 * Loading TransactionCodes to LocalCache
	 * 
	 * @author venkateshgaddam
	 * @throws ServiceException
	 */
	@PostConstruct
	public void loadTransactionCodesToLocalCache() throws ServiceException {
		Map<String, Transactions> mapSpilTxnCode = transactionService.getSpilTransactionCodes("01");
		cacheManager.getCache(SpilCacheConfig.SPIL_DATA_CACHE)
			.put(TRANSACTIONS, mapSpilTxnCode);

	}

}
