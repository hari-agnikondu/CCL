package com.incomm.cclpvms.config.service;

import java.util.Map;

import com.incomm.cclpvms.config.model.GlobalParametersDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface GlobalParameterService {

	public ResponseDTO updateGlobalParameters(GlobalParametersDTO globalParametersDTO) throws ServiceException;

	public Map<String, Object> getGlobalParameters() throws ServiceException;

	public Map<String, String> getDomainMetadata() throws ServiceException;

}
