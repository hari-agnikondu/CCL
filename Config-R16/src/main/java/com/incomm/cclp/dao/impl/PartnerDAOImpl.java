package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.PartnerDAO;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.Partner;
import com.incomm.cclp.domain.Purse;

/**
 * PartnerDAOImpl class defines all the Database operations for 
 * Partner Configuration.
 *
 */
@Repository
public class PartnerDAOImpl implements PartnerDAO {

	@PersistenceContext
	private EntityManager em;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(PartnerDAOImpl.class);

	@Override
	public void createPartner(Partner partner) {	
		em.persist(partner);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Partner> getAllPartners() {
		logger.debug("Performing database query \"{}\"",QueryConstants.GET_ALL_PARTNERS);
		return em.createQuery(QueryConstants.GET_ALL_PARTNERS).getResultList();
	}

	@Override
	public void updatePartner(Partner partner) {
		em.merge(partner);
	}

	@Override
	public int deletePartner(long partnerId) {
		int count = 0;
		try {
			logger.debug("Performing database query \"{}\"", QueryConstants.REMOVE_PARTNER_BY_ID);
			
			count = em.createQuery(QueryConstants.REMOVE_PARTNER_BY_ID).setParameter("partnerId", partnerId)
					.executeUpdate();

		} catch (Exception e) {
			logger.error("Failed to remove partner::"+e.getMessage());
			em.close();

		}
		return count;
	}

	@Override
	public int deletePartnerPurse(long partnerId) {
		int ptnrPrseCount = 0;
		try {
			logger.debug("Performing database query \"{}\"", QueryConstants.REMOVE_FROM_PARTNER_PURSE);
			
			ptnrPrseCount = em.createQuery(QueryConstants.REMOVE_FROM_PARTNER_PURSE).setParameter("partnerId", partnerId)
					.executeUpdate();
		} catch (Exception e) {
			logger.error("Failed to remove partner purse::"+e.getMessage());
			em.close();
		}
		return ptnrPrseCount;
	}
	
	@Override
	public int deletePartnerCurr(long partnerId) {
		int ptnrPCurrCount = 0;
		try {
			logger.debug("Performing database query \"{}\"", QueryConstants.REMOVE_FROM_PARTNER_CURRENCY);
			
			ptnrPCurrCount = em.createQuery(QueryConstants.REMOVE_FROM_PARTNER_CURRENCY).setParameter("partnerId", partnerId)
					.executeUpdate();
		} catch (Exception e) {
			logger.error("Failed to remove partner currency::"+e.getMessage());
			em.close();
		}
		return ptnrPCurrCount;
	}
	
	@Override
	public int deletePartnerProg(long partnerId) {
		int ptnrProgCount = 0;
		try {
			logger.debug("Performing database query \"{}\"", QueryConstants.REMOVE_FROM_PARTNER_PROGRAM);
			
			ptnrProgCount = em.createQuery(QueryConstants.REMOVE_FROM_PARTNER_PROGRAM).setParameter("partnerId", partnerId)
					.executeUpdate();
		} catch (Exception e) {
			logger.error("Failed to remove partner program::"+e.getMessage());
			em.close();
		}
		return ptnrProgCount;
	}
	
	
	
	@Override
	public Partner getPartnerById(Long partnerId) {	
		return em.find(Partner.class, partnerId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Partner> getPartnerByName(String partnerName) {

		String partnerName1 = partnerName.replaceAll("_", "\\\\_");
		return em.createQuery(QueryConstants.GET_PARTNER_BY_NAME)
				.setParameter("partnerName", "%" + partnerName1.toUpperCase() + "%").getResultList();
	}

	@Override
	public int partnerLinkCheck(long partnerId) {

		int count = ((Number)em.createNativeQuery(QueryConstants.GET_PRODUCT_COUNT_BY_PARTNER_ID)
					.setParameter("partnerId", partnerId).getSingleResult()).intValue();
		if(count<=0)
			return 0;
		return 1;
	}

	@Override
	public CurrencyCode getCurrencyCodeById(String currencyTypeID) {
		return em.find(CurrencyCode.class, currencyTypeID);
	}

	@Override
	public List<String> getAttachedCurrencyList(long partnerId) {
		
		@SuppressWarnings("unchecked")
		List<String> count = (List<String>) em.createNativeQuery(QueryConstants.GET_CURRENCY_COUNT_BY_PARTNER_ID)
				.setParameter("partnerId", partnerId).getResultList();
		
	return count;
	}

	
	@Override
	public List<String> getAttachedPurseList(long partnerId) {
		
		@SuppressWarnings("unchecked")
		List<String> count = (List<String>) em.createNativeQuery(QueryConstants.GET_PURSE_COUNT_BY_PARTNER_ID)
				.setParameter("partnerId", partnerId).getResultList();
		
	return count;
	}
	
	@Override
	public Purse getPurseById(String purseId) {
		logger.debug(CCLPConstants.ENTER);
		return em.find(Purse.class, Long.valueOf(purseId));
		
	}
	
	
}
