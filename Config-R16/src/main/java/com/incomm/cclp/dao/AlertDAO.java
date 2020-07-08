package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.Alert;

public interface AlertDAO {

	public Alert getAlertById(Long alertId);
	
	public List<Alert> getAllAlerts();
	
}
