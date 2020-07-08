package com.incomm.scheduler.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.SerialNumberRequestDAO;
import com.incomm.scheduler.service.SerialNumberRequest;

@Service
public class SerialNumberRequestServiceImpl implements SerialNumberRequest {

	@Autowired
	SerialNumberRequestDAO serialNumberRequestDao;
	
	@Override
	public String getSerialNumber(Long productId, String upc, Long serialNumberQuantity) {
	
		serialNumberRequestDao.getSerialNumber(productId, upc, serialNumberQuantity);
		return "success";
	}
	
}
