package com.incomm.cclp.fsapi.service;

import java.util.Map;

import com.incomm.cclp.exception.ServiceException;

public interface CommonService {

	public void getBusinessTime(final Map<String, Object> tempValueMap);

	public void getDelchannelTranCode(Map<String, Object> valuHashMap, String activationApi, String put)
			throws ServiceException;
	
	public boolean checkProductValidity(String productId);
	
}
