/**
 * 
 */
package com.incomm.cclp.transaction.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.incomm.cclp.config.SpilCacheConfig;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.bean.Transactions;

/**
 * LocalCacheServiceImpl provides the necessary operations to retrieve and store JSON attributes in local cache.
 * 
 */

@Service
@CacheConfig(cacheManager = "cacheManager")
public class LocalCacheServiceImpl {

	public static final String INPUT_DATA_VALIDATION_CACHE = "InputDataValidations";

	public static final String SPIL_MESSAGE_TYPE_BEAN_CACHE = "SpilMessageTypeBean";

	public static final String TRANSACTION_AUTH_DEFINITION_CACHE = "TransactionAuthDefinition";

	public static final String SPIL_RESPONSE_CODE_CACHE = "SpilResponseCode";

	public static final String CARD_STATUS = "CardStatus";

	public static final String RULE_PARAMETERS = "RuleParameters";

	public static final String RULE_PARAMETERS_MAPPING = "ruleParametersMapping";

	public static final String TRANSACTIONS = "transactions";

	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.INPUT_DATA_VALIDATION_CACHE")
	public Map<String, List<TransactionInputValidation>> getInputValidations(
			Map<String, List<TransactionInputValidation>> inputValidations) {
		return inputValidations;
	}

	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.SPIL_MESSAGE_TYPE_BEAN_CACHE")
	public Map<String, SpilStartupMsgTypeBean> getSpilMessageTypeBean(Map<String, SpilStartupMsgTypeBean> spilStartupMsgTypeBean) {
		return spilStartupMsgTypeBean;
	}

	/**
	 * add Transaction Auth Definitions to cache
	 * 
	 * @param mapTxnAuthDefinitions
	 * @return Map with Txn Code and Msg Type as key
	 * 
	 * @author ulagana
	 */
	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.TRANSACTION_AUTH_DEFINITION_CACHE")
	public Map<String, List<TransactionAuthDefinition>> getAllTransactionAuthDefinitions(
			Map<String, List<TransactionAuthDefinition>> mapTxnAuthDefinitions) {

		return mapTxnAuthDefinitions;
	}

	/**
	 * update Transaction Auth Definition cache values
	 * 
	 * @param mapTxnAuthDefinitions
	 * @return Map with Txn Code and Msg Type as key
	 * 
	 * @author ulagana
	 */
	@CachePut(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.TRANSACTION_AUTH_DEFINITION_CACHE")
	public Map<String, List<TransactionAuthDefinition>> updateOrGetAllTransactionAuthDefinitions(
			Map<String, List<TransactionAuthDefinition>> mapTxnAuthDefinitions) {

		return mapTxnAuthDefinitions;
	}

	/**
	 * get the Spil Response Code into cache.
	 * 
	 */
	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.SPIL_RESPONSE_CODE_CACHE")
	public Map<String, SpilResponseCode> getSpilResponseCode(Map<String, SpilResponseCode> spilResponseCode) {
		return spilResponseCode;
	}

	@CachePut(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.SPIL_RESPONSE_CODE_CACHE")
	public Map<String, SpilResponseCode> setSpilResponseCode(Map<String, SpilResponseCode> spilResponseCode) {
		return spilResponseCode;
	}

	/**
	 * To refresh the input validations in cache
	 * 
	 * @param inputValidations
	 * @return
	 */
	@CachePut(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.INPUT_DATA_VALIDATION_CACHE")
	public Map<String, List<TransactionInputValidation>> setInputValidations(
			Map<String, List<TransactionInputValidation>> inputValidations) {
		return inputValidations;
	}

	/**
	 * add card status to cache
	 * 
	 * @param mapCardStatus
	 * @return
	 */
	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.CARD_STATUS")
	public Map<String, String> getAllCardStatus(Map<String, String> mapCardStatus) {

		return mapCardStatus;
	}

	/**
	 * add card status to cache
	 * 
	 * @param mapCardStatus
	 * @return
	 */
	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.RULE_PARAMETERS")
	public Map<String, String> getRuleParameters(Map<String, String> ruleParameters) {

		return ruleParameters;
	}

	/**
	 * add card status to cache
	 * 
	 * @param mapCardStatus
	 * @return
	 */
	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.RULE_PARAMETERS_MAPPING")
	public Map<String, String> getRuleParametersMapping(Map<String, String> ruleParametersMapping) {

		return ruleParametersMapping;
	}

	@Cacheable(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.TRANSACTIONS")
	public Map<String, Transactions> getTransactionMapping(Map<String, Transactions> mapTransactions) {
		return mapTransactions;
	}

	/**
	 * To refresh the input validations in cache
	 * 
	 * @param inputValidations
	 * @return
	 */
	@CachePut(cacheNames = SpilCacheConfig.SPIL_DATA_CACHE, key = "#root.target.TRANSACTIONS")
	public Map<String, Transactions> setTransactionMapping(Map<String, Transactions> mapTransactions) {
		return mapTransactions;
	}

}
