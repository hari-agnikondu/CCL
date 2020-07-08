/**
 * 
 */
package com.incomm.cclp.fsapi.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.incomm.cclp.config.ClpCacheConfig;
import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.bean.FsApiDetail;
import com.incomm.cclp.fsapi.bean.FsApiMaster;
import com.incomm.cclp.fsapi.bean.FsApiTransaction;
import com.incomm.cclp.fsapi.bean.FsApiValidationDetail;


/**
 * LocalCacheServiceImpl provides the necessary operations 
 * to retrieve and store JSON attributes in local cache.
 * 
 * @author abutani
 *
 */

@Service
@CacheConfig(cacheManager="cacheManager")
public class LocalCacheServiceImpl {
	
	private  final Logger logger = LogManager.getLogger(this.getClass());

	public static final String FSAPI_API = "Fsapi";

	public static final String FSAPI_DETAILS = "FsapiDetails";

	public static final String FSAPI_VALIDATION_DTLS = "FsapiValidationDetails";

	public static final String FSAPI_TRANSACTION = "FsapiTransaction";

	public static final String TRANSACTION_AUTH_DEFINITION_CACHE = "TransactionAuthDefinition";

	public static final String CARD_STATUS = "CardStatus";

	public static final String B2B_RESPONSE_CODE = "B2BResponseCodes";
	
	/**
	 * cache for fsapi master
	 * 
	 */
	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_API")
	public Map<String, FsApiMaster> getFsapiMasterDetails(Map<String, FsApiMaster> mapFsapiMasterDetails) {

		logger.info("Adding Fsapi master details to LOCAL Cache: {}", mapFsapiMasterDetails);

		return mapFsapiMasterDetails;
	}

	@CachePut(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_API")
	public Map<String, FsApiMaster> updateOrGetFsapiMasterDetails(Map<String, FsApiMaster> mapFsapiMasterDetails) {

		logger.info("Updating Fsapi master to LOCAL Cache: {}", mapFsapiMasterDetails);

		return mapFsapiMasterDetails;
	}
	/**
	 * cache for fsapi details
	 */
	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_DETAILS")
	public Map<String, FsApiDetail> getFsapiDetails(Map<String, FsApiDetail> mapFsapiDetails) {

		logger.info("Adding Fsapi  details to LOCAL Cache: {}", mapFsapiDetails);

		return mapFsapiDetails;
	}

	@Cacheable(cacheNames =  ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.B2B_RESPONSE_CODE")
	public Map<String, CSSResponseCode> getAllCSSResponseDetails(Map<String, CSSResponseCode> mapCSSRespCode) {

		logger.info("Adding CSS Response Codes to LOCAL Cache: {}", mapCSSRespCode);

		return mapCSSRespCode;
	}
	
	@CachePut(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_DETAILS")
	public Map<String, FsApiDetail> updateOrGetFsapiDetails(Map<String, FsApiDetail> mapFsapiDetails) {

		logger.info("Updating Fsapi details to LOCAL Cache: {}", mapFsapiDetails);

		return mapFsapiDetails;
	}

	/**
	 * cache for fsapi validation details
	 */

	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_VALIDATION_DTLS")
	public Map<String, Map<String, FsApiValidationDetail>> getFsapiValidationDetails(
			Map<String, Map<String, FsApiValidationDetail>> mapFsapiValidationDetails) {

		logger.info("Adding Fsapi validation details to LOCAL Cache: {}", mapFsapiValidationDetails);

		return mapFsapiValidationDetails;
	}

	@CachePut(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_VALIDATION_DTLS")
	public Map<String, Map<String, FsApiValidationDetail>> updateOrGetFsapiValidationDetails(
			Map<String, Map<String, FsApiValidationDetail>> mapFsapiValidationDetails) {

		logger.info("Updating Fsapi validation details to LOCAL Cache: {}", mapFsapiValidationDetails);

		return mapFsapiValidationDetails;
	}

	/**
	 * cache for fsapi transaction details
	 */
	@Cacheable(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_TRANSACTION")
	public Map<String, FsApiTransaction> getFsapiTransactionDetails(
			Map<String, FsApiTransaction> mapFsapiTransactioDetails) {

		logger.info("Adding Fsapi transaction details to LOCAL Cache: {}", mapFsapiTransactioDetails);

		return mapFsapiTransactioDetails;
	}

	@CachePut(cacheNames = ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE, key = "#root.target.FSAPI_TRANSACTION")
	public Map<String, FsApiTransaction> updateOrGetFsapiTransactionDetails(
			Map<String, FsApiTransaction> mapFsapiTransactioDetails) {

		logger.info("Updating Fsapi transaction details to LOCAL Cache: {}", mapFsapiTransactioDetails);

		return mapFsapiTransactioDetails;
	}

}
