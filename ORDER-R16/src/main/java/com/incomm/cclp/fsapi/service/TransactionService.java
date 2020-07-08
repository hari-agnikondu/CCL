package com.incomm.cclp.fsapi.service;

import java.util.Map;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.bean.FsApiDetail;
import com.incomm.cclp.fsapi.bean.FsApiMaster;
import com.incomm.cclp.fsapi.bean.FsApiTransaction;
import com.incomm.cclp.fsapi.bean.FsApiValidationDetail;

public interface TransactionService {
	
	public Map<String, FsApiMaster> getFsapiMasterDetails();

	public Map<String, FsApiDetail> getFsapiDetails();

	public Map<String, Map<String, FsApiValidationDetail>> getFsapiValidationDetails();

	public Map<String, FsApiTransaction> getFsapiTransactionDetails();

	public FsApiMaster getFsapiMasterDetailByApiKey(String key) throws ServiceException;
	
	public FsApiDetail getFsapiDetailByApiKey(String key) throws ServiceException;
	
	public Map<String, FsApiValidationDetail> getFsapiValidationDetailByApiKey(String key);
	
	public FsApiTransaction getFsapiTransactionDetailByApiKey(String key) throws ServiceException;

	Map<String, CSSResponseCode> getAllCSSResponseCodeDetails();

	CSSResponseCode getCSSResponseCodeByRespId(String channelCode, String respId) throws ServiceException;
	
	public Map<String, String> getAllCardStatus() throws ServiceException;
	

}
