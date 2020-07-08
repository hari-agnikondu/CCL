
package com.incomm.cclp.fsapi.dao;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.bean.CardStatusBean;
import com.incomm.cclp.fsapi.bean.FsApiMaster;
import com.incomm.cclp.fsapi.bean.FsApiTransaction;


public interface TransactionDao {

	
	public List<FsApiMaster> getAllFsapiMastDetails();

	public List<Map<String, Object>> getAllFsapiDetails();

	public List<Map<String, Object>> getAllFsapiValidationDetails();

	public List<FsApiTransaction> getAllFsapiTransactionDetails();

	public List<CSSResponseCode> getCssResponseCodes();
	
	public List<CardStatusBean> getAllCardStatus();

}
