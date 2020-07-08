package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RuleSetCacheService;
import com.incomm.cclp.transaction.bean.Rule;
import com.incomm.cclp.transaction.bean.RuleSet;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.service.impl.DistributedCacheServiceImpl;

@Service
public class RuleSetCacheServiceImpl implements RuleSetCacheService {

	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductDAO productDao;

	@Autowired
	DistributedCacheServiceImpl distributedCacheServiceImpl;

	@Override
	public RuleSet getRuleSet(String ruleSetId) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		RuleSet ruleSet = null;

		try {
			ruleSet = distributedCacheServiceImpl.getOrAddRuleSet(Long.parseLong(ruleSetId), ruleSet);

			List<Rule> ruleList = new ArrayList<>();
			if (ruleSet == null) {

				if (productDao.checkRuleSetById(ruleSetId) == 1) {
					/**
					 * Get List Of rules for ruleSetID
					 */
					List<Map<String, Object>> ruleObj = productDao.getRuleSetById(ruleSetId);

					for (Map<String, Object> obj : ruleObj) {
						int ruleId = Integer.parseInt(String.valueOf(obj.get("rule_id")));
						List<Map<String, Object>> ruleDetailsObj = productDao.getRuleDetailsById(ruleId);
						HashMap<String, String> ruleDetails = new HashMap<>();
						for (Map<String, Object> obj1 : ruleDetailsObj) {
							ruleDetails.put(String.valueOf(obj1.get("rule_details_id")), String.valueOf(obj1.get("rule_filter")));
						}
						Rule rule = new Rule(ruleId, String.valueOf(obj.get("rule_name")), String.valueOf(obj.get("rule_exp")),
								String.valueOf(obj.get("ACTION_TYPE")), ruleDetails, String.valueOf(obj.get("TRANSACTION_TYPE")));
						ruleList.add(rule);
					}

					return new RuleSet(ruleList);

				} else {
					logger.error("Rule set does not exist");
					throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
				}

			}

		} catch (NumberFormatException ne) {
			logger.error("Error Occured while try to convert String to Long, {}", ne);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			logger.error("Error Occured while try to get RuleSet {}", e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);

		return ruleSet;
	}

}
