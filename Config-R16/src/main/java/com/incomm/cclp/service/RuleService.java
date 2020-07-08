package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.RuleDTO;
import com.incomm.cclp.exception.ServiceException;

public interface RuleService {
	
	public void createRule(RuleDTO ruleDto) throws ServiceException;
	
	public List<RuleDTO> getAllRules();
	
	public void updateRule(RuleDTO ruleDto) throws ServiceException;
	
	public void deleteRule(RuleDTO ruleDto);
	
	public RuleDTO getRuleById(long ruleId);
	
	public List<RuleDTO> getRulesByName(String ruleName);
	
	public List<Object[]> getRuleConfig() throws ServiceException;

	public List<Object[]> getTransactionsByTxnType(String txnTypeId);


}
