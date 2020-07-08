package com.incomm.cclpvms.config.service;

import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface  PanExpiryService {
	
	public ResponseDTO updatePanExpiryParameters(Map<String, Object> productAttributes, Long productId) throws ServiceException ;

	public Map<String, Object> getPanExpiryParameters()  throws ServiceException;
	
}
