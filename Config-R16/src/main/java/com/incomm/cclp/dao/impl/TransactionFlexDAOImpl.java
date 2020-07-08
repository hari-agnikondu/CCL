package com.incomm.cclp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.TransactionFlexDAO;

@Repository
public class TransactionFlexDAOImpl implements TransactionFlexDAO{

	@PersistenceContext
	EntityManager em;
	@Override
	@Transactional
	public int updateTransactionFlexDesc(String key, String value) {
			return  em.createNativeQuery(QueryConstants.UPDATE_TRANSACTION_FLEX_DESCRIPTION).setParameter("key", key).setParameter("value", value).executeUpdate();
	}

}
