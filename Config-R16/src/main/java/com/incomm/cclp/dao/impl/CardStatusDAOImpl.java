package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.CardStatusDAO;
import com.incomm.cclp.domain.CardStatus;


/*
 * Card Status DAO provides all the card status data access operations.
 */
@Repository
public class CardStatusDAOImpl implements CardStatusDAO {
	@PersistenceContext
	private EntityManager em;
	/**
	 * Getting Active Card Status details
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<CardStatus> getCardStatus(){
		return em.createQuery(QueryConstants.GET_ACTIVE_CARD_STATUS).getResultList();
		
	}

}
