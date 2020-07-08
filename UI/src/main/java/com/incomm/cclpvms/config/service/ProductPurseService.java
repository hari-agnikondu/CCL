package com.incomm.cclpvms.config.service;

import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface ProductPurseService {

	public ResponseDTO addLimitAttributesByProgramID(Map<String, Object> productAttributes, Long programID, Long purseId)throws ServiceException;

	public ResponseDTO addMonthlyCapAttributesByProgramID(Map<String, Object> productAttributes, Long programID,Long purseId)throws ServiceException;

	public ResponseDTO addTxnFeeAttributesByProgramID(Map<String, Object> productAttributes, Long programID,Long purseId) throws ServiceException;

	
}
