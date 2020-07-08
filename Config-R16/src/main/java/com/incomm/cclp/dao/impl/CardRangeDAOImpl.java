package com.incomm.cclp.dao.impl;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.CardRangeDAO;
import com.incomm.cclp.domain.CardRange;
import com.incomm.cclp.domain.Issuer;
import com.incomm.cclp.util.Util;

/**
 * 
 * CardRange DAO provides all the cardRange data access operations.
 * 
 */
@Repository
public class CardRangeDAOImpl implements CardRangeDAO {

	// the entity manager
	@PersistenceContext
	private EntityManager em;


	/**
	 * Persists an card range.
	 * 
	 * @param cardRange The CardRange to be persisted.
	 */
	@Override
	@Transactional
	public void createCardRange(CardRange cardRange) {

		em.persist(cardRange);

	}


	/**
	 * update an card range.
	 * 
	 * @param cardRange The CardRange to be updated.
	 */
	@Override
	@Transactional
	public void updateCardRange(CardRange cardRange) {
		em.merge(cardRange);
	}

	/**
	 * delete card range.
	 * 
	 * @param cardRange The CardRange to be updated.
	 */
	@Override
	@Transactional
	public void deleteCardRange(CardRange cardRange) {
		em.remove(em.contains(cardRange) ? cardRange : em.merge(cardRange));
	}
	

	/**
	 * Getting the all card ranges list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CardRange> getCardRanges() {
			return em.createQuery(QueryConstants.GET_CARDRANGE)
					.getResultList();
	}
	
	/**
	 * Getting the card ranges by IssuerName and Prefix
	 * 
	 * @param  issuerName, prefix 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CardRange> getCardRangeByIssuerNameAndPrefix(String issuerName,String prefix) {
		String qry= QueryConstants.GET_CARDRANGE;
		if(( Util.useFilter(issuerName) && Util.useFilter(prefix))){
			qry+= " where upper(cardrange.issuer.issuerName) like :issuerName  ESCAPE '\\'  and  cardrange.prefix like :prefix and cardrange.issuer.isActive= :isActive";
			return em.createQuery(qry)
					.setParameter("issuerName", "%"+issuerName.replaceAll("_", "\\\\_").toUpperCase()+"%")
					.setParameter("prefix", prefix+"%")
					.setParameter(CCLPConstants.ISACTIVE, "Y")
					.getResultList();
		}

		else if( Util.useFilter(prefix)){
			qry+= " where cardrange.prefix like :prefix  and cardrange.issuer.isActive=:isActive";
			return em.createQuery(qry)
					.setParameter("prefix", prefix+"%")
					.setParameter(CCLPConstants.ISACTIVE, "Y")
					.getResultList();
		}
		else if(Util.useFilter(issuerName) ){
			qry+= " where upper(cardrange.issuer.issuerName) like :issuerName  ESCAPE '\\'  and  cardrange.issuer.isActive=:isActive";
			return em.createQuery(qry)
					.setParameter("issuerName", "%"+issuerName.replaceAll("_", "\\\\_").toUpperCase()+"%")
					.setParameter(CCLPConstants.ISACTIVE, "Y")
					.getResultList();
		}
		
		else{
			qry+= " where  cardrange.issuer.isActive=:isActive";
			return em.createQuery(qry)
					.setParameter(CCLPConstants.ISACTIVE, "Y")
					.getResultList();
		}
	}
	/**
	 * Getting list of Issuers
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Issuer> getIssuers(){
		return em.createQuery(QueryConstants.GET_ACTIVE_ISSUERS)
				.setParameter("activeStatus","Y")
				.getResultList();
	}
	

	/**
	 * Getting CardRange Entity object by cardRangeId
     * @param  cardRangeId
	 */
	@Override
	public CardRange getCardRangeById(String cardRangeId){
		
		return (CardRange) em.createQuery(QueryConstants.GET_CARDRANGE_BY_CARDRANGE_ID)
				.setParameter(CCLPConstants.CARD_RANGE_ID, Long.valueOf(cardRangeId))
				.getSingleResult();
	}
	
	/**
	 * changing the card range status APPROVE/REJECT
     * @param   cardRangeId, newStatus, checkerDesc
     * updating the checkerDesc
	 */
	@Override
	@Transactional
	public int changeCardRangeStatus(long cardRangeId,String newStatus,String checkerDesc, long lastUpdUser) {
		
      return  em.createNativeQuery(QueryConstants.UPDATE_CARDRANGE_STATUS)
		.setParameter("status", newStatus)
		.setParameter("checkerDesc", checkerDesc)
		.setParameter(CCLPConstants.CARD_RANGE_ID, cardRangeId)
		.setParameter("newStatus", "NEW")
		.setParameter("lastUpdUser", lastUpdUser)
		.executeUpdate();
	}
	
	/**
	 * Checking overlap condition for card range 
     * @param startRange, endRange
	 */
	@Override
	public boolean checkCardRangeAvail(String startRange,String endRange,String checkDigit){
		boolean flag=false;
		String value="";
		
		value=(String)	em.createNativeQuery(QueryConstants.CARDRANGE_AVAIL_CHECK_CHECKDIGIT)
				
				.setParameter("startRange", "1"+startRange)
				.setParameter("endRange", "1"+endRange)
				.setParameter("checkDigit", checkDigit)
				.getSingleResult();
		
		if("NO-OVERLAP".equals(value)){
			flag=true;
		}
		return flag;
	}
	
	
	/**
	 * Getting the card ranges by issuer name
	 * 
	 * @param issuerName 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CardRange> getCardRangesByIssuerId(Long issuerId) {
		return em.createQuery(QueryConstants.GET_CARDRANGES_BY_ISSUERID)
				.setParameter("issuerId", issuerId)
				.getResultList();
	}
	
	/**
	 * Getting the card ranges is exist or not 
	 * 
	 * @param issuerName 
	 */

	@Override
	public CardRange getExistCardRanges(CardRange cardRange) {
		
		return em.find(CardRange.class, cardRange.getCardRangeId());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getAllCardRanges() {
	
		return em.createQuery(QueryConstants.GET_CARD_RANGE)
				.getResultList();
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCardRangeDataById(List<Long> cardRangeId) {
	
		return em.createQuery(QueryConstants.GET_CARD_RANGE_BY_CARD_ID)
				.setParameter(CCLPConstants.CARD_RANGE_ID, cardRangeId)
				.getResultList();
		

	}

	

}
