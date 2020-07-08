package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.dao.CCFDAO;
import com.incomm.cclp.domain.CCFConfDetail;
import com.incomm.cclp.domain.CCFConfVersion;
import com.incomm.cclp.dto.CCFConfDetailDTO;
import com.incomm.cclp.dto.CCFConfigReq;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CCFService;

@Service
public class CCFServiceImpl implements CCFService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private CCFDAO ccfDAO;

	@Override
	@Transactional
	public void updateCCFConfig(CCFConfigReq configRequest) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		CCFConfVersion ccfVersion = constructConfig(configRequest);
		ccfDAO.deleteCCFConfig(ccfVersion);
		ccfDAO.createCCFConfig(ccfVersion);
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public CCFConfDetailDTO getCCFVersionByName(String versionName) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ccfDAO.getCCFVersionByName(versionName);
		logger.info(CCLPConstants.EXIT);
		return null;
	}

	public CCFConfDetailDTO getAllCCFVersionDtls() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ccfDAO.getAllCCFVersionDtls();
		logger.info(CCLPConstants.EXIT);
		return null;
	}

	public List<Object[]> getCCFParam() throws ServiceException {
		return ccfDAO.getCCFParam();

	}

	public List<Object[]> getCCFMappingKeys() throws ServiceException {
		return ccfDAO.getCCFMappingKeys();

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, List> getCCFlist() throws ServiceException
	{
		return ccfDAO.getCCFlist();

	}

	@Override
	@Transactional
	public void createCCFConfig(CCFConfigReq configRequest) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		CCFConfVersion ccfVersion = constructConfig(configRequest);
		ccfDAO.createCCFConfig(ccfVersion);
		logger.info(CCLPConstants.EXIT);
	}

	public CCFConfVersion constructConfig(CCFConfigReq configRequest) {
		logger.info(CCLPConstants.ENTER);
		List<CCFConfDetailDTO> ccfDtos = configRequest.getRowData();
		// Master Table entry
		CCFConfVersion ccfVersion = new CCFConfVersion();
		ccfVersion.setVersionName(configRequest.getVersionName());
		ccfVersion.setInsUser(configRequest.getInsUser());
		ccfVersion.setInsDate(configRequest.getInsDate());
		ccfVersion.setLupdUser(configRequest.getLastUpdUser());
		ccfVersion.setLupdDate(configRequest.getInsDate());
		ObjectMapper objectMapper = new ObjectMapper();
		List<CCFConfDetail> ccfDetailList = new ArrayList<>();
		ccfDtos.forEach(ccfDto -> {
			ccfDto.setInsUser(ccfVersion.getInsUser());
			ccfDto.setLastUpdUser(ccfVersion.getLupdUser());
			ccfDto.setInsDate(configRequest.getInsDate());
			ccfDto.setLastUpdDate(configRequest.getLastUpdDate());
			ccfDto.setVersionName(configRequest.getVersionName());
			ccfDetailList.add(objectMapper.convertValue(ccfDto, CCFConfDetail.class));
		});

		ccfVersion.setCcfConfigRecords(ccfDetailList);
		logger.info(CCLPConstants.EXIT);
		return ccfVersion;
	}

	@Override
	public List<CCFConfDetail> getCCFVersionDtls(String versionID) throws ServiceException {

		return ccfDAO.getCCFVersionDtls(versionID);
	}

}
