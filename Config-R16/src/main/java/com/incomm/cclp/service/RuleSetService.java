/**
 * 
 */
package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.RuleSetDTO;
import com.incomm.cclp.exception.ServiceException;



public interface RuleSetService {

	public List<RuleSetDTO> getRuleSet();
	
	public void createRuleSet(RuleSetDTO ruleSetDto) throws ServiceException;
	
	public void updateRuleSet(RuleSetDTO ruleSetDto) throws ServiceException;
	
	public List<RuleSetDTO> getRuleSetByName(String ruleSetName);
	
	public int getRuleSetById(long ruleSetId);
	
	public List<Object[]> getRuleDetails(long ruleSetId) throws ServiceException;
	
}
