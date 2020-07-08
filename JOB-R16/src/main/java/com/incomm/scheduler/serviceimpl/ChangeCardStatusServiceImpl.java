package com.incomm.scheduler.serviceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.ChangeCardStatusDAO;

import com.incomm.scheduler.service.ChangeCardStatusService;

@Service
public class ChangeCardStatusServiceImpl implements ChangeCardStatusService{
	
private static final Logger logger = LogManager.getLogger(ChangeCardStatusServiceImpl.class);
	
	@Autowired
	ChangeCardStatusDAO changeCardStatusDao;


	@Override
	public void changeCardStatus() {
		String response = "Failure";
		try {
			response = changeCardStatusDao.changeCardStatus();
			if("FINISHED".equalsIgnoreCase(response)) 
				logger.info("Change Card Status Procedure Called successfully {} ", response);
			else
				logger.info("Failed to call the Change Card Status procedure {} ", response);
		}catch(Exception e) {
			logger.error("Error while running Change Card Status Job {} ",e);
		}
		
	}

}
