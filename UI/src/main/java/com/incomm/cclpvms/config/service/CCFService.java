package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.CCFConfigReq;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface CCFService {
	
	public String getCCFParam() throws ServiceException;
	
	public String getCCFKey() throws ServiceException;
	
	public String getCCFVersionDtls(String versionID) throws ServiceException;
	
	public ResponseDTO addCCFConfiguration(CCFConfigReq request) throws ServiceException;
	
	public ResponseDTO updateCCFConfiguration(CCFConfigReq request) throws ServiceException;
	
	public Map<String,List>  getCCFList() throws ServiceException;

}
