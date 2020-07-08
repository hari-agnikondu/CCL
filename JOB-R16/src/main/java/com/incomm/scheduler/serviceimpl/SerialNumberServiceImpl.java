package com.incomm.scheduler.serviceimpl;

import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.scheduler.dao.SerialNumberRequestDAO;
import com.incomm.scheduler.service.SerialNumberService;

@Service
public class SerialNumberServiceImpl implements SerialNumberService {

	private static final Logger logger = LogManager.getLogger(SerialNumberServiceImpl.class);
	
	@Autowired
	SerialNumberRequestDAO serialNumberRequestDao;
	
	@Override
    @Async
	public void serialNumberRequest() {
		logger.info("Entered into serviceImpl");
		List<Long> products = serialNumberRequestDao.getAllProductIds();
		
		logger.info("The number of products retrieved" + products.size());
		if(!CollectionUtils.isEmpty(products)) {
			Iterator<Long> itr = products.iterator();
			while(itr.hasNext())
			serialNumberRequestDao.getSerialNumber(itr.next());
			
			
		}
		
	}

}
