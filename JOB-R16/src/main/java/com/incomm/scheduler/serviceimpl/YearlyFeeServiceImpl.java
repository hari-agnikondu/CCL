package com.incomm.scheduler.serviceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.YearlyFeeDAO;
import com.incomm.scheduler.service.YearlyFeeService;

@Service
public class YearlyFeeServiceImpl implements YearlyFeeService {

	private static final Logger logger = LogManager.getLogger(YearlyFeeServiceImpl.class);
	
	@Autowired
	YearlyFeeDAO yearlyfeeDao;
	
	@Override
	public void yearlyFee() {

		String response = "FAILURE";
		try {
				response = yearlyfeeDao.callYearlyFeeProcedure();
				
				if ("FINISHED".equalsIgnoreCase(response))
					logger.info("Yearly Fee Procedure Called successfully {} ", response);
				else
					logger.info("Failed to call the Yearly fee procedure {} ", response);
		} catch (Exception e) {
			logger.error("Error while running Yearly Fee Job {} ", e);
		}

	}

}
