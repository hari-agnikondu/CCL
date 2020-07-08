package com.incomm.cclp.fsapi.service;

import java.util.Map;

import com.incomm.cclp.exception.ServiceException;

public interface ProductService {
	
	public Map<String, Map<String, Object>> getProductAttributes(String productId)  throws ServiceException;

}
