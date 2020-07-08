package com.incomm.scheduler.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.QueryConstants;
import com.incomm.scheduler.dao.CardNumberInventoryDAO;
import com.incomm.scheduler.model.CardRange;
import com.incomm.scheduler.model.CardRangeInventory;

@Repository
public class CardNumberInventoryDAOImpl implements CardNumberInventoryDAO {

	@Autowired
	@Qualifier("inventoryEntityManagerFactory")
	private EntityManager em;
	
	private static final Logger logger = LogManager.getLogger(CardNumberInventoryDAOImpl.class);

	@Override
	public List<CardRange> getCardInvtryDtls() {
		return em.createQuery(QueryConstants.GET_ALL_CARD_INVNTRY_LIST, CardRange.class).getResultList();
	}

	@Override
	public List<CardRange> getCardInvtryDtlsByScheduler() {
		return em.createQuery(QueryConstants.GET_ALL_CARD_INVNTRY_LIST_BY_SCHEDULER, CardRange.class).getResultList();
	}

	
	@Override
	public CardRange getCardRangeById(Long cardRangeId) {
		return em.find(CardRange.class, cardRangeId);
	}

	@Transactional
	@Override
	public int initiateCardNumberGeneration(Long cardRangeId) {

		logger.info(CCLPConstants.ENTER);
		StoredProcedureQuery query = em.createStoredProcedureQuery(QueryConstants.CARD_INVENTORY_GENERATION_PROCEDURE)
				.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN)
				.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT)
				.setParameter(1, cardRangeId);
		query.execute();

		String pResponse = (String) query.getOutputParameterValue(2);
		
		logger.info(QueryConstants.CARD_INVENTORY_GENERATION_PROCEDURE + " response: " + pResponse);

		logger.info(CCLPConstants.EXIT);
		if (pResponse.equalsIgnoreCase("OK")) {
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public CardRangeInventory getCardRangeInventoryByCardRange(Long cardRangeId) {
		return em.find(CardRangeInventory.class, cardRangeId);
	}

	@Transactional
	@Override
	public int pauseCardNumberProcessByCardRangeId(Long cardRangeId) {

		return em.createQuery(QueryConstants.PAUSE_CARD_NUMBER_GENERATION).setParameter("cardRangeId", cardRangeId)
				.executeUpdate();

	}

}
