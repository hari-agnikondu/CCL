package com.incomm.cclp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.ProductAlertDAO;

@Repository
public class ProductAlertDAOImpl implements ProductAlertDAO {

	@PersistenceContext
	EntityManager em;
	
	@Override
	@Transactional
	public void deleteProductAlertByProductId(Long productId) {
		em.createNativeQuery(QueryConstants.DELETE_PRODUCT_ALERT_BY_PRODUCT_ID)
		.setParameter(CCLPConstants.PRODUCT_ID, productId).executeUpdate();
	}

}
