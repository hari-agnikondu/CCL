package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.RuleSetDAO;
import com.incomm.cclp.domain.RuleSet;


/*
 * RuleSet DAO provides all the rule set data access operations.
 */
@Repository
public class RuleSetDAOImpl implements RuleSetDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<RuleSet> getRuleSet(){
		return em.createQuery(QueryConstants.GET_RULE_SET_LIST).getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RuleSet> getRuleSetByName(String ruleSetName) {
		return em.createQuery(QueryConstants.GET_RULESET_BY_NAME).
				setParameter("ruleSetName", ruleSetName.toUpperCase())
				.getResultList();
	}

	@Override
	public void createRuleSet(RuleSet ruleSet) {
		em.persist(ruleSet);

	}

	@Override
	@Transactional
	public void updateRuleSet(RuleSet ruleSet) {
		em.merge(ruleSet);

	}

	@Override
	public long getRuleSetSeqId() {
		Query query = em.createNativeQuery(QueryConstants.GET_RULE_SET_SEQ_NO);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		return result.longValue();
	}

	public void deleteRuleSet(RuleSet ruleSet) {
		Query q = em.createNativeQuery(QueryConstants.DELETE_RULESET_DETAIL_BY_ID);
		q.setParameter(CCLPConstants.RULESET_ID, ruleSet.getRuleSetId());
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getRuleSetById(long ruleSetId) {
		List<BigDecimal> cntList = null;
		cntList = em.createNativeQuery(QueryConstants.GET_RULESET_ID_CNT).setParameter(CCLPConstants.RULESET_ID, ruleSetId)
				.getResultList();
		BigDecimal cntBD =  cntList.get(0);
		return cntBD.intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getRuleDetails(long ruleSetId) {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_RULEDETAILS).setParameter(CCLPConstants.RULESET_ID, ruleSetId);
		return query.getResultList();
	}	

}
