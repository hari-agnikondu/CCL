package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.MasterDao;
import com.incomm.cclp.domain.AuthenticationType;
import com.incomm.cclp.domain.ClpResource;
import com.incomm.cclp.domain.CountryCode;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.Group;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.domain.PurseType;
import com.incomm.cclp.domain.StateCode;

@Repository
public class MasterDaoImpl implements MasterDao{
	
	@PersistenceContext
	private EntityManager em;
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	public List<PurseType> getAllPurseType() {

		return em.createQuery(QueryConstants.GET_ALL_PURSE_TYPE)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CurrencyCode> getAllCurrencyCode() {

		return em.createQuery(QueryConstants.GET_ALL_CURRENCY_CODE)
				.getResultList();
	}

	
	@Override
	@Transactional
	public void createCurrency(CurrencyCode currencycode) {

		em.persist(currencycode);
	}
	
	@Override
	@Transactional
	public void createPurseType(PurseType pursetype) {

		em.persist(pursetype);

	}

	@Override
	public PurseType getPurseTypeById(long purseTypeId) {
		return em.find(PurseType.class, purseTypeId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getFulfillmentShipmentAttrs() {
		Query query = em.createNativeQuery(QueryConstants.GET_FULFILLMENT_SHIPMENT_ATTRIBUTE);
		return query.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPackageShipmentAttrs() {
		Query query = em.createNativeQuery(QueryConstants.GET_PACKAGE_SHIPMENT_ATTRIBUTE);
		return query.getResultList();

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CountryCode> getAllCountryCode() {

		return em.createQuery(QueryConstants.GET_ALL_COUNTRY_CODE)
				.getResultList();
	}
	
	@Override
	public CountryCode getCountryCodeById(CountryCode countryCode) {
		
		return em.find(CountryCode.class, countryCode.getCountryCodeID());
		
	}
	/**
	 * Getting all menus
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ClpResource> getMenus() {
		return em.createQuery(QueryConstants.GET_ALL_MENUS)
				.getResultList();
	}
	
		@SuppressWarnings("unchecked")
	@Override
	public List<StateCode> getStateCodeByCountryId(Long countryCodeId) {
		String qry= QueryConstants.GET_ALL_STATE_CODE;
		
		qry+= " where statecode.countryCode like :countryCodeId";
		return em.createQuery(qry)
				.setParameter("countryCodeId", countryCodeId)
				.getResultList();
	}

		@SuppressWarnings("unchecked")
		@Override
		public List<Object[]> getCardStatusAttrs() {
			return em.createNativeQuery(QueryConstants.GET_ALL_CARDSTATUS)
					.getResultList();
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<Group> getGroupsForUser() {
			
			return em.createQuery(QueryConstants.GET_GROUPS).getResultList();
		}

		@Override
		public List<AuthenticationType> getAllAuthenticationTypes() {
			return em.createQuery(QueryConstants.GET_AUTHENTICATION_TYPES, AuthenticationType.class).getResultList();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public List<Purse> getAllPurse() {
			logger.debug("Enter getAllPurse>>>");
			logger.debug("Exit getAllPurse<<<");
			return em.createQuery(QueryConstants.GET_ALL_PURSE)
					.getResultList();
			
		}
}
