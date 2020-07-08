package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.RuleDAO;
import com.incomm.cclp.domain.Rule;
import com.incomm.cclp.domain.RuleDetails;

@Repository
public class RuleDAOImpl implements RuleDAO {

	@PersistenceContext
	private EntityManager em;
	
	
	/**
	 * Persists a rule.
	 * 
	 * @param rule The Rule to be persisted.
	 */
	@Override
	public void createRule(Rule rule) {
	    em.persist(rule);
	  }
	/**
	 * Persists a rule detail.
	 * 
	 * @param rule The Rule Detail to be persisted.
	 */
	@Override
	public String encryptFilter(String filter){
		
		return em.createNativeQuery("select fn_emaps_main(:ruleFilter) from dual")
				.setParameter("ruleFilter",filter).getSingleResult().toString();

	}
	@Override
	public void createRuleDetail(RuleDetails ruleDetail) {
			em.persist(ruleDetail);
		
	}
	/**
	 * Gets all Rules.
	 * 
	 * @return the list of Rules.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rule> getAllRules() {

		return em.createQuery(QueryConstants.GET_ALL_RULES)
				.getResultList();
	}
	
	
	/**
	 * Updates a rule.
	 * 
	 * @param rule The Rule to be updated.
	 */
	@Override
	public void updateRule(Rule rule) {
		
		em.merge(rule);
	}
	
	/**
	 * Updates a rule detail.
	 * 
	 * @param rule The Rule Detail to be updated.
	 */
	@Override
	public void updateRuleDetail(RuleDetails ruleDetail) {
		
		em.merge(ruleDetail);
	}
	
	/**
	 * Deletes a rule.
	 * 
	 * @param rule The Rule to be deleted.
	 */
	@Override
	@Transactional
	public void deleteRule(Rule rule) {
		
		em.remove(em.contains(rule) ? rule : em.merge(rule));
	}

	
	/**
	 * Gets a Rule by ID.
	 * 
	 * @param ruleId The rule id for the Rule to be retrieved.
	 * 
	 * @return the Rule.
	 */
	@Override
	public Rule getRuleById(long ruleId) {
		
		return em.find(Rule.class, ruleId);
	}
	
	
	/**
	 * Gets a Rule by Name.
	 * 
	 * @param ruleName The rule name for the Rule to be retrieved.
	 * 
	 * @return the list of Rules.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rule> getRulesByName(String ruleName) {

		String name = ruleName.replaceAll("_", "\\\\_");
		return em.createQuery(QueryConstants.GET_RULE_BY_NAME)
				.setParameter("ruleName", name.toUpperCase()).getResultList();
	}
	
	
	@Override
	public List<Object[]> getRuleConfig()
	{
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_RULECONFIG);
		@SuppressWarnings("unchecked")
		List<Object[]> ruleConfigList = query.getResultList();
		return ruleConfigList;
	}

    @Override
    	public List<Object[]> getTransactionsByTxnType(String txnTypeId) {
    		
    		
    		Query query = em.createNativeQuery(QueryConstants.GET_TRANSACTION_BY_TXN_IDENTIFIER).setParameter("txnTypeId",txnTypeId);
    		@SuppressWarnings("unchecked")	
    		List<Object[]> txnList = query.getResultList();
    		return txnList;
    	}	
}
