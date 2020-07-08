package com.incomm.cclpvms.fraud.service;

import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface PrmService {

	public ResponseDTO updatePrmConfig(Map<String, String> prmAttributes) throws ServiceException ;

	public ResponseDTO updateAllPrmConfig() throws ServiceException;
	
}
