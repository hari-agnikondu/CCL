package com.incomm.cclp.fsapi.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.fsapi.dao.SerialNumberRangeActivationDAO;
import com.incomm.cclp.fsapi.service.SerialNumberRangeActivationService;

@Service
public class SerialNumberRangeActivationServiceImpl implements SerialNumberRangeActivationService {
	
	@Autowired
	SerialNumberRangeActivationDAO serialNumberRangeActivationDAO;
	
	@Override
	public Map<String, Object> serialNumberRangeActivation(Map<String, Object> valuMap) {

		return serialNumberRangeActivationDAO.serialNumberRangeActivation(valuMap);
	}
	public void updateCardInfo(Map<String, Object> tempValuHashMap) {

		serialNumberRangeActivationDAO.updateCardInfo(tempValuHashMap);
	}
	
	public  void logAPIRequestDtls(Map<String,Object> tempValuHashMap,String respMsg)
	{
		serialNumberRangeActivationDAO.logAPIRequestDtls(tempValuHashMap,respMsg);
	}

}
