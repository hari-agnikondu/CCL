package com.incomm.cclp.dao;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.domain.CCFConfDetail;
import com.incomm.cclp.domain.CCFConfVersion;
import com.incomm.cclp.dto.CCFConfDetailDTO;
import com.incomm.cclp.exception.ServiceException;

public interface CCFDAO {
	public void createCCFConfig(CCFConfVersion ccfVersion) throws ServiceException;

	 public void updateCCFConfig(CCFConfVersion ccfVersion) throws ServiceException;

	public CCFConfDetailDTO getCCFVersionByName(String versionName) throws ServiceException;

	public void deleteCCFConfig(CCFConfVersion ccfVersion) throws ServiceException;

	public CCFConfDetailDTO getAllCCFVersionDtls() throws ServiceException;

	public List<Object[]> getCCFMappingKeys() throws ServiceException;

	public List<Object[]> getCCFParam() throws ServiceException;
	public List<CCFConfDetail> getCCFVersionDtls(String versionID) throws ServiceException;
	@SuppressWarnings("rawtypes")
	public Map<String,List> getCCFlist()throws ServiceException;
}
