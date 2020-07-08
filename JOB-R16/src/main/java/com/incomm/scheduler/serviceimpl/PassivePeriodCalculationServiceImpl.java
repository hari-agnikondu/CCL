package com.incomm.scheduler.serviceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.PassivePeriodCalculationDAO;
import com.incomm.scheduler.service.PassivePeriodCalculationService;

@Service
public class PassivePeriodCalculationServiceImpl implements PassivePeriodCalculationService {

	private static final Logger logger = LogManager.getLogger(PassivePeriodCalculationServiceImpl.class);
	
	@Autowired
	PassivePeriodCalculationDAO passivePeriodCalculationDao;
	
	@Override
	public void passivePeriodCalculation() {
		
		String response = "Failure";
		try {
			response = passivePeriodCalculationDao.passivePeriodCalculation();
			if("FINISHED".equalsIgnoreCase(response)) 
				logger.info("Passive Period Calculation Procedure Called successfully {} ", response);
			else
				logger.info("Failed to call the Passive Period Calculation procedure {} ", response);
		}catch(Exception e) {
			logger.error("Error while running Passive Period Calculation Job {} ",e);
		}
	
	}

}
