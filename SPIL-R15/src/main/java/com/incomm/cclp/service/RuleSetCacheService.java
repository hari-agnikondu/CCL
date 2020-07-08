package com.incomm.cclp.service;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.RuleSet;

public interface RuleSetCacheService {

	RuleSet getRuleSet(String ruleSetId) throws ServiceException;

}
