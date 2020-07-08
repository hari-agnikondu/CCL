package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.Rule;
import com.incomm.cclp.domain.RuleDetails;

public interface RuleDAO {
	
	public void createRule(Rule rule);
	
	public List<Rule> getAllRules();
	
	public void updateRule(Rule rule);
	
	public void deleteRule(Rule rule);
	
	public Rule getRuleById(long ruleId);
	
	public List<Rule> getRulesByName(String ruleName);
	
	public List<Object[]> getRuleConfig();

	public void createRuleDetail(RuleDetails ruleDetail);

	public void updateRuleDetail(RuleDetails ruleDetail);

	public String encryptFilter(String filter);

	public List<Object[]> getTransactionsByTxnType(String txnTypeId);
}
