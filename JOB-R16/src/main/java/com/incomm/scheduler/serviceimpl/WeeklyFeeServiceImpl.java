package com.incomm.scheduler.serviceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.WeeklyFeeDAO;
import com.incomm.scheduler.service.WeeklyFeeService;

@Service
public class WeeklyFeeServiceImpl implements WeeklyFeeService {

	private static final Logger logger = LogManager.getLogger(WeeklyFeeServiceImpl.class);

	@Autowired 
	WeeklyFeeDAO weeklyfeeDao;
	/*
	 * TO execute the procedure and invoke the rest end point
	 * 
	 * @see com.incomm.scheduler.service.WeeklyFeeService()
	 */
	@Override
	public void weeklyFee() {
		
		String response = "Failure";
		try {
			response = weeklyfeeDao.callProcedure();
			if("FINISHED".equalsIgnoreCase(response)) 
				logger.info("Weekly Fee Procedure Called successfully {} ", response);
			else
				logger.info("Failed to call the weekly fee procedure {} ", response);
		}catch(Exception e) {
			logger.error("Error while running Weekly Fee Job {} ",e);
		}
	
	}

}
