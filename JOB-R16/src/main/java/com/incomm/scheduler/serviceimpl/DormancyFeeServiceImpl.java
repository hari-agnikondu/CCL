package com.incomm.scheduler.serviceimpl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.DormancyFeeDAO;
import com.incomm.scheduler.service.DormancyFeeService;

@Service
public class DormancyFeeServiceImpl implements DormancyFeeService {

	private static final Logger logger = LogManager.getLogger(WeeklyFeeServiceImpl.class);
	
	@Autowired
	DormancyFeeDAO dormancyFeeDao;
	
	@Override
	public void dormancyFee() {

		String response = "Failure";
		try {
			response = dormancyFeeDao.callDormancyFeeProcedure();
			if("FINISHED".equalsIgnoreCase(response)) 
				logger.info("Weekly Fee Procedure Called successfully {} ", response);
			else
				logger.info("Failed to call the weekly fee procedure {} ", response);
		}catch(Exception e) {
			logger.error("Error while running Weekly Fee Job {} ",e);
		}

	}

}
