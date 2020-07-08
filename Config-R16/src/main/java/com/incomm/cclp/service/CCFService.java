package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.domain.CCFConfDetail;
import com.incomm.cclp.dto.CCFConfDetailDTO;
import com.incomm.cclp.dto.CCFConfigReq;
import com.incomm.cclp.exception.ServiceException;

public interface CCFService {
	public void createCCFConfig(CCFConfigReq configRequest) throws ServiceException;

	public void updateCCFConfig(CCFConfigReq configRequest) throws ServiceException;

	public CCFConfDetailDTO getCCFVersionByName(String versionName) throws ServiceException;

	public CCFConfDetailDTO getAllCCFVersionDtls() throws ServiceException;

	public List<Object[]> getCCFParam() throws ServiceException;

	public List<CCFConfDetail> getCCFVersionDtls(String versionID) throws ServiceException;

	public List<Object[]> getCCFMappingKeys() throws ServiceException;

	@SuppressWarnings("rawtypes")
	public Map<String, List> getCCFlist() throws ServiceException;
}
