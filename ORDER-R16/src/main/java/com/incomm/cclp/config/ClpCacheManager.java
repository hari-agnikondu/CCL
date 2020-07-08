/*package com.incomm.cclp.config;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.bean.FsApiDetail;
import com.incomm.cclp.fsapi.bean.FsApiMaster;
import com.incomm.cclp.fsapi.bean.FsApiTransaction;
import com.incomm.cclp.fsapi.bean.FsApiValidationDetail;
import com.incomm.cclp.fsapi.service.TransactionService;


@Configuration
public class ClpCacheManager {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	TransactionService transactionService;

	private final Logger logger = LogManager.getLogger(this.getClass());

	public static final String FSAPI_API = "Fsapi";

	public static final String FSAPI_DETAILS = "FsapiDetails";

	public static final String FSAPI_VALIDATION_DTLS = "FsapiValidationDetails";

	public static final String FSAPI_TRANSACTION = "FsapiTransaction";

	public static final String TRANSACTION_AUTH_DEFINITION_CACHE = "TransactionAuthDefinition";

	public static final String CARD_STATUS = "CardStatus";

	public static final String CSS_RESPONSE_CODE = "CSSResponseCodes";
	
	public static final String B2B_RESPONSE_CODE = "B2BResponseCodes";

	@PostConstruct
	public void loadFsapiMasterToLocalCache() {
		Map<String, FsApiMaster> mapFsapiMastDetails = transactionService.getFsapiMasterDetails();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(FSAPI_API, mapFsapiMastDetails);

		logger.info("Adding Fsapi mast details to LOCAL Cache on startup : " + mapFsapiMastDetails);
	}

	@PostConstruct
	public void loadFsapiDetailToLocalCache() {
		Map<String, FsApiDetail> mapFsapiDetails = transactionService.getFsapiDetails();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(FSAPI_DETAILS, mapFsapiDetails);

		logger.info("Adding Fsapi  details to LOCAL Cache on startup : " + mapFsapiDetails);
	}

	@PostConstruct
	public void loadFsapiValidationDetailToLocalCache() {
		Map<String, Map<String, FsApiValidationDetail>> mapFsapiValidationDetails = transactionService
				.getFsapiValidationDetails();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(FSAPI_VALIDATION_DTLS, mapFsapiValidationDetails); 

		logger.info("Adding Fsapi validation details to LOCAL Cache on startup : " + mapFsapiValidationDetails);
	}

	@PostConstruct
	public void loadFsapiTraansactionDetailToLocalCache() {
		Map<String, FsApiTransaction> mapFsapiTransactionDetails = transactionService.getFsapiTransactionDetails();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(FSAPI_TRANSACTION, mapFsapiTransactionDetails);

		logger.info("Adding Fsapi transaction details to LOCAL Cache on startup : " + mapFsapiTransactionDetails);
	}

	@PostConstruct
	public void loadResponseCodeToLocalCache() {
		Map<String, CSSResponseCode> mapCssResponseCode = transactionService.getAllCSSResponseCodeDetails();
		cacheManager.getCache(ClpCacheConfig.ATTRIBUTE_DEFINITION_CACHE).put(B2B_RESPONSE_CODE, mapCssResponseCode);

		logger.info("Adding CSS Response Code Details to LOCAL Cache on startup : " + mapCssResponseCode);
	}
}
*/