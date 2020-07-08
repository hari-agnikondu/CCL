package com.incomm.scheduler.serviceimpl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.MonthlyFeeDAO;
import com.incomm.scheduler.service.MonthlyFeeService;

@Service
public class MonthlyFeeServiceImpl implements MonthlyFeeService {

	private static final Logger logger = LogManager.getLogger(MonthlyFeeServiceImpl.class);
	
	@Autowired
	MonthlyFeeDAO monthlyfeeDao;
	
	@Override
	public void monthlyFee(){

		String response = "Failure";
		try {
			response = monthlyfeeDao.callMonthlyFeeProcedure();
			if("FINISHED".equalsIgnoreCase(response)) 
				logger.info("Monthly Fee Procedure Called successfully {} ", response);
			else
				logger.info("Failed to call the Monthly fee procedure {} ", response);
		}catch(Exception e) {
			logger.info("Error while running Monthly Fee Job {} ",e);
		}
	}

}
