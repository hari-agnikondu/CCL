/**
 * 
 */
package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.IssuerDAO;
import com.incomm.cclp.domain.Issuer;

/**
 * Issuer DAO provides all the issuer data access operations.
 * 
 * @author abutani
 *
 */
@Repository
public class IssuerDAOImpl implements IssuerDAO {
	
	// the entity manager
	@PersistenceContext
	private EntityManager em;

	
	/**
	 * Persists an issuer.
	 * 
	 * @param issuer The Issuer to be persisted.
	 */
	@Override
	public void createIssuer(Issuer issuer) {
		em.persist(issuer);
	}
	
	
	/**
	 * Gets all Issuers.
	 * 
	 * @return the list of Active Issuers.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Issuer> getAllIssuers() {

		return em.createQuery(QueryConstants.GET_ALL_ISSUERS)
				.getResultList();
	}
	
	
	/**
	 * Updates an issuer.
	 * 
	 * @param issuer The Issuer to be updated.
	 */
	@Override
	@Transactional
	public void updateIssuer(Issuer issuer) {
		
		em.merge(issuer);
	}
	
	
	/**
	 * Deletes an issuer.
	 * 
	 * @param issuer The Issuer to be deleted.
	 */
	@Override
	@Transactional
	public void deleteIssuer(Issuer issuer) {
		
		em.remove(em.contains(issuer) ? issuer : em.merge(issuer));
	}

	
	/**
	 * Gets an Issuer by ID.
	 * 
	 * @param issuerId The issuer id for the Issuer to be retrieved.
	 * 
	 * @return the Issuer.
	 */
	@Override
	public Issuer getIssuerById(long issuerId) {
		
		return em.find(Issuer.class, issuerId);
	}
	
	
	/**
	 * Gets an issuer by name.
	 * 
	 * @param issuerName The issuer name for the Issuer to be retrieved.
	 * The issuerName parameter can be a partial or complete name.
	 * 
	 * @return the list of all Issuers for the given name.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Issuer> getIssuersByName(String issuerName) {

		
		String issName="";
				if(issuerName!=null)
		{
					issName=issuerName;
		}
		
		
		if(issName.equalsIgnoreCase("_"))
		{
			issName="\\_";

			return em.createQuery(QueryConstants.GET_ISSUER_BY_NAME_UNDERSCORE)
					.setParameter("issuerName", "%" + issName.toUpperCase() + "%")
					.getResultList();
		}
		else 
			{
		return em.createQuery(QueryConstants.GET_ISSUER_BY_NAME)
				.setParameter("issuerName", "%" + issName.toUpperCase() + "%")
				.getResultList();
			
		
		}
		
		
		
		
	}


	@org.springframework.transaction.annotation.Transactional
	@Override
	public void deleteIssuerById(long issuer) {
		
		int ckhCountofIssuers=0;
		
		ckhCountofIssuers=em.createNativeQuery(QueryConstants.CARDRANGE_COUNT_BY_ISSUER)
				.setParameter(CCLPConstants.ISSUER_ID, String.valueOf(issuer))
	            .executeUpdate();
		
		if(ckhCountofIssuers==0)
		{
		int isSuccessful = em.createNativeQuery(QueryConstants.DELETE_ISSUER_BY_ID)
	            .setParameter(CCLPConstants.ISSUER_ID, String.valueOf(issuer))
	            .executeUpdate();
	    if (isSuccessful == 0) {
	        throw new OptimisticLockException(" issuer modified concurrently");
	    }
	    
	}
		
		
		
	}
	
	
	
	
	@Transactional
	@Override
	public int  countAndDeleteIssuerById(long issuerId) {		
		int isSuccessful=0;
		List<?> cardrangeCount = null;
		
			cardrangeCount=em.createQuery(QueryConstants.CARDRANGE_COUNT_BY_ISSUER)
				.setParameter(CCLPConstants.ISSUER_ID, issuerId)
	            .getResultList();
		
			if(CollectionUtils.isEmpty(cardrangeCount))
		{
		 isSuccessful = em.createNativeQuery(QueryConstants.DELETE_ISSUER_BY_ID)
	            .setParameter(CCLPConstants.ISSUER_ID, String.valueOf(issuerId))
	            .executeUpdate();
	   
	    return isSuccessful;
	}
		else
		{
			return isSuccessful;
		}
				
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@org.springframework.transaction.annotation.Transactional
	public int  countOfCardRangeById(long issuer) {
		int count=0;
		List <BigDecimal> ckhCountofIssuerss=null;
		
		ckhCountofIssuerss=em.createNativeQuery(QueryConstants.CARDRANGE_COUNT_BY_ISSUER)
				.setParameter(CCLPConstants.ISSUER_ID, String.valueOf(issuer))
				.getResultList();

		BigDecimal ckhCountofIssuers= ckhCountofIssuerss.get(0);
		
		count=ckhCountofIssuers.intValue();

		return count;
		
				
	}


	@SuppressWarnings("unchecked")
	@Override
	public int countOfIssuer(long issuer) {
		int count=0;
		List <BigDecimal> countofIssuerss=null;
		
	 countofIssuerss=em.createNativeQuery(QueryConstants.COUNT_BY_ISSUER)
				.setParameter(CCLPConstants.ISSUER_ID, String.valueOf(issuer))
				.getResultList();

		BigDecimal countofIssuers= countofIssuerss.get(0);
		
		count=countofIssuers.intValue();

		return count;
	}
	
	
	
}
