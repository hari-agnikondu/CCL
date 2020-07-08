
package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.RuleSet;


public interface RuleSetDAO {
	
	public List<RuleSet> getRuleSet();
	
	public List<RuleSet> getRuleSetByName(String ruleSetName);
	
	public void updateRuleSet(RuleSet ruleSet);
	
	public void createRuleSet(RuleSet ruleSet);
	
	public void deleteRuleSet(RuleSet ruleSet);
	
	public long getRuleSetSeqId();
	
	public int getRuleSetById(long ruleSetId);
	
	public List<Object[]> getRuleDetails(long ruleSetId);

}
