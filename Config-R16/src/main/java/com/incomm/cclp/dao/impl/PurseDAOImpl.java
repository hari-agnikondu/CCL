/**
 * 
 */
package com.incomm.cclp.dao.impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.PurseDAO;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.ProductPurse;
import com.incomm.cclp.domain.Purse;

/**
 * Purse DAO provides all the purse data access operations.
 * 
 */
@Repository
public class PurseDAOImpl implements PurseDAO {

	// the entity manager
	@PersistenceContext
	private EntityManager em;


	/**
	 * Create new purse
	 */
	@Override
	@Transactional
	public void createPurse(Purse purse) {
		em.persist(purse);
	}

	
	/**
	 * Gets all Purses.
	 * 
	 * @return the list of Active Purses.
	 */
	@SuppressWarnings("unchecked")
	public List<Purse> getAllPurses() {

		return em.createQuery(QueryConstants.GET_ALL_PURSES)
				.getResultList();
	}

	/**
	 * To get the purse by purseID
	 */
	@Override
	public Purse getPurseById(Long purseId) {
		return em.find(Purse.class, purseId);
	}
	
	/**
	 * Getting list of product purses
	 * Used to check whether purse mapped to product
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProductPurse> getProductByPurseId(Long purseId) {
		return em.createQuery(QueryConstants.GET_PRODUCT_BY_PURSE)
				.setParameter("purseId",purseId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getPartnerByPurseId(Long purseId) {
		int count = ((Number)em.createNativeQuery(QueryConstants.GET_PARTNER_BY_PURSE)
				.setParameter("purseId", purseId).getSingleResult()).intValue();
		if(count<=0)
			return 0;
		return 1;
	}
	/**
	 * To search the purse details by currency or UPC code or empty search
	 * @param CurrencyCode and UPC
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Purse> getPursesByCurrencyAndUpcCode(String currencyCode,String upc){
		String qry= QueryConstants.GET_ALL_PURSES;
		if((currencyCode!=null && !"*".equals(currencyCode) && !currencyCode.trim().isEmpty()) 
				&& (upc!=null && !"*".equals(upc) && !upc.isEmpty())){
			
			qry+= " where purse.currencyCode.currencyTypeID like :currencyCode and purse.upc like :upc";
			return em.createQuery(qry)
					.setParameter("currencyCode",currencyCode+"%")
					.setParameter("upc",upc+"%")
					.getResultList();
		}
		else if(upc!=null && !upc.trim().isEmpty() && !"*".equals(upc)){
			qry+= " where purse.upc like :upc";
			return em.createQuery(qry)
					.setParameter("upc",upc+"%")
					.getResultList();
		}
		else if(currencyCode!=null && !currencyCode.trim().isEmpty() && !"*".equals(currencyCode)){
			qry+= " where purse.currencyCode.currencyTypeID like :currencyCode";
			return em.createQuery(qry)
					.setParameter("currencyCode",currencyCode+"%")
					.getResultList();
		}

		else{
			return em.createQuery(QueryConstants.GET_ALL_PURSES)
					.getResultList();
		}


	}

	/**
	 * update an purse.
	 * 
	 * @param Purse The Purse to be updated.
	 */
	@Transactional
	public void updatePurseDetails(Purse purse){
		em.merge(purse);
	}
	
	/**
	 * delete the purse
	 * @param Purse The Purse to be delete.
	 */
	@Override
	@Transactional
	public void deletePurseDetails(Purse purse) {
			em.remove(em.contains(purse) ? purse : em.merge(purse));
		
	}

			public List<String> getPurseByIds(List<Long> purseIds) {
				
			 @SuppressWarnings("unchecked")
			List<String> purseList= em.createQuery(QueryConstants.GET_PURSES_BY_IDS)
					.setParameter("purseId", purseIds)
					.getResultList();
		
			return purseList;
		}

	@Override
	public CurrencyCode getCurrencyByID(String currencyCode) {
		return (CurrencyCode) em.createQuery(
				"SELECT currencyCode FROM  CurrencyCode currencyCode where currencyCode.currCodeAlpha=:currencyCode or currencyCode.currencyTypeID=:currencyCode ",
				CurrencyCode.class).setParameter("currencyCode", currencyCode).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Purse> getPurseByExtPurseId(String extPurseId) {
		String qry = " select purse from Purse purse where lower(purse.extPurseId) = lower(:extPurseId)";
		return em.createQuery(qry).setParameter("extPurseId", extPurseId).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Purse> getPursesBypurseTypePurseExtID(Long purseType,String currencyCode,String extPurseId,String upc){
		String qry= QueryConstants.GET_ALL_PURSES;	
		if ((purseType != null && !purseType.equals(Long.valueOf(-1)))
				&& (upc!=null && !"*".equals(upc) && !upc.isEmpty())
				&& (extPurseId != null && !"*".contentEquals(extPurseId) && !extPurseId.isEmpty())) {

			qry += " where purse.purseType.purseTypeId = :purseType"
					+ " and lower(purse.extPurseId) like lower(:extPurseId) and purse.upc like :upc";
			return em.createQuery(qry).setParameter("purseType", purseType)
					.setParameter("extPurseId", extPurseId + "%").setParameter("upc", upc).getResultList();
		}
		else if ((currencyCode != null && !"*".equals(currencyCode) && !currencyCode.trim().isEmpty())
				&& (purseType != null && !purseType.equals(Long.valueOf(-1)))
				&& (extPurseId != null && !"*".contentEquals(extPurseId) && !extPurseId.isEmpty())) {

			qry += " where purse.currencyCode.currencyTypeID like :currencyCode and purse.purseType.purseTypeId = :purseType"
					+ " and lower(purse.extPurseId) like lower(:extPurseId)";
			return em.createQuery(qry).setParameter("currencyCode", currencyCode + "%").setParameter("purseType", purseType)
					.setParameter("extPurseId", extPurseId + "%").getResultList();
		}
		else if ((currencyCode != null && !"*".equals(currencyCode) && !currencyCode.trim().isEmpty())
				&& (purseType != null && !purseType.equals(Long.valueOf(-1)))) {
			qry += " where purse.currencyCode.currencyTypeID like :currencyCode and purse.purseType.purseTypeId like :purseType";
			return em.createQuery(qry).setParameter("currencyCode", currencyCode + "%").setParameter("purseType", purseType)
					.getResultList();
		}
		else if ((purseType != null && !purseType.equals(Long.valueOf(-1)))
				&&(extPurseId != null && !"*".contentEquals(extPurseId) && !extPurseId.isEmpty())) {
			qry += " where purse.purseType.purseTypeId = :purseType and lower(purse.extPurseId) like lower(:extPurseId)";
			return em.createQuery(qry).setParameter("purseType", purseType).setParameter("extPurseId", extPurseId + "%").getResultList();
		}
		else if (extPurseId != null && !"*".contentEquals(extPurseId) && !extPurseId.isEmpty()) {
			qry += " where lower(purse.extPurseId) like lower(:extPurseId)";
			return em.createQuery(qry).setParameter("extPurseId", extPurseId + "%").getResultList();
		}
		else {
			return em.createQuery(QueryConstants.GET_ALL_PURSES).getResultList();
		}
	}
}
