package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.FulfillmentDAO;
import com.incomm.cclp.domain.FulFillmentVendor;

/**
 * @Raja DAO Operations for Fulfillment(Vendor) details.
 * 
 */
@Repository
public class FulfillmentDAOImpl implements FulfillmentDAO {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void createFulfillment(FulFillmentVendor fulFillmentVendor) {
		em.merge(fulFillmentVendor);
	}

	@Override
	public void updateFulfillment(FulFillmentVendor fulFillmentVendor) {
		em.merge(fulFillmentVendor);
	}

	@Override
	public FulFillmentVendor getFulfillmentById(long fulfillmentSEQID) {
		return em.find(FulFillmentVendor.class, fulfillmentSEQID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int chkDuplicateByID(String fulfillmentID) {
		List<BigDecimal> cntList = null;
		cntList = em.createNativeQuery(QueryConstants.GET_FULFILLMENT_ID_CNT)
				.setParameter("fulfillmentID", fulfillmentID).getResultList();
		BigDecimal cntBD =  cntList.get(0);
		return cntBD.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FulFillmentVendor> getAllFulfillments() {
		return em.createQuery(QueryConstants.GET_ALL_FULFILLMENTS).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FulFillmentVendor> getFulfillmentByName(String fulFillmentName) {
		return em.createQuery(QueryConstants.GET_FULFILLMENT_BY_NAME)
				.setParameter("fulFillmentName", "%" + fulFillmentName.toUpperCase() + "%").getResultList();
	}

	@Override
	@Transactional
	public int deleteFulfillment(long fulFillmentSEQID) {
		return em.createQuery(QueryConstants.REMOVE_FULFILLMENT_BY_ID).setParameter("fulFillmentSEQID", fulFillmentSEQID).executeUpdate();
	}


	@SuppressWarnings("unchecked")
	@Override
	public int checkPackageIdMap(String fulfillmentID) {
		List<BigDecimal> cntList = null;
		cntList = em.createNativeQuery(QueryConstants.GET_FULFILLMENT_PKG_ID_CNT)
				.setParameter("fulfillmentID", fulfillmentID).getResultList();
		BigDecimal cntBD = cntList.get(0);
		return cntBD.intValue();
	}

}
