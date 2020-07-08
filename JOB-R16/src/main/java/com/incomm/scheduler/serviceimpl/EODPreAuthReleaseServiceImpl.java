package com.incomm.scheduler.serviceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.EODPreAuthReleaseDAO;
import com.incomm.scheduler.service.EODPreAuthReleaseService;
@Service
public class EODPreAuthReleaseServiceImpl implements EODPreAuthReleaseService {

private static final Logger logger = LogManager.getLogger(MonthlyFeeServiceImpl.class);
	
	@Autowired
	EODPreAuthReleaseDAO eodPreAuthReleaseDao;
	
	
	@Override
	public void EODPreAuthRelease() {
		String response = "Failure";
		try {
			response = eodPreAuthReleaseDao.callEODPreAuthReleaseProcedure();
			if("FINISHED".equalsIgnoreCase(response)) 
				logger.info("EOD PreAuth Release Procedure Called successfully {} ", response);
			else
				logger.info("Failed to call the EOD PreAuth Release  procedure {} ", response);
		}catch(Exception e) {
			logger.info("Error while running EOD PreAuth Release  Job {} ",e);
		}
	}

}
