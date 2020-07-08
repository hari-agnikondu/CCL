package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.AlertDAO;
import com.incomm.cclp.domain.Alert;

@Repository
public class AlertDAOImpl implements AlertDAO {
	
	@PersistenceContext
	EntityManager em;

	@Override
	public Alert getAlertById(Long alertId) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Alert> getAllAlerts() {		
		return em.createQuery(QueryConstants.GET_ALERT_MESSAGES).getResultList();
	}

}
