package com.incomm.cclpvms.config.service;

import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.RuleSetDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface RuleSetService {
	
	public Map<Long,String> getRuleSet() throws ServiceException;
	public Map<Long,String> getRule() throws ServiceException;
	public ResponseDTO createRuleSet(RuleSetDTO ruleSetDto) throws ServiceException;
	public ResponseDTO updateRuleSet(RuleSetDTO ruleSetDto) throws ServiceException;
	public String getRuleDetails(long ruleSetId) throws ServiceException;

}
