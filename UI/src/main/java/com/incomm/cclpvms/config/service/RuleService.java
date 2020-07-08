package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.Rule;
import com.incomm.cclpvms.config.model.RuleDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface RuleService {
	
	public Map<Long,String> getRulesList() throws ServiceException;
	
	public ResponseDTO createRule(RuleDTO ruleDto) throws ServiceException;
	
	public ResponseDTO updateRule(RuleDTO ruleDto) throws ServiceException;
	
	public Map<String,String> getRuleConfig() throws ServiceException;
	
	public List<Rule> getRulesByName(String ruleName) throws ServiceException;
	
	public Rule getRulesById(Long ruleId) throws ServiceException;

	public List<Object[]> getTransactionByTxnType(String txnTypeId) throws ServiceException;

}
