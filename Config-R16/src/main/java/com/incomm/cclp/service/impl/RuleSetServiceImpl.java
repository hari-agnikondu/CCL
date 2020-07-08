/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.RuleSetDAO;
import com.incomm.cclp.domain.RuleRuleSet;
import com.incomm.cclp.domain.RuleRuleSetId;
import com.incomm.cclp.domain.RuleSet;
import com.incomm.cclp.dto.RuleSetDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RuleSetService;


@Service
public class RuleSetServiceImpl implements RuleSetService {
	
	@Autowired
	RuleSetDAO ruleSetDAO;
	
	private static final Logger logger = LogManager.getLogger(RuleSetServiceImpl.class);
	
	@Override
	public List<RuleSetDTO> getRuleSet(){

		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new 
				TypeToken<List<RuleSetDTO>>() {}.getType();
				logger.info(CCLPConstants.EXIT);
				return mm.map(ruleSetDAO.getRuleSet(), 
						targetListType);
	}
	
	/**
	 * Create an rule.
	 * 
	 * @param ruleDto The RuleDTO to be created.
	 * @throws ServiceException 
	 */
	@Override
	@Transactional
	public void createRuleSet(RuleSetDTO ruleSetDto) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		logger.debug("Rule SetName:"+ruleSetDto.getRuleSetName());
		
		List<RuleSetDTO> ruleSets = getRuleSetByName(ruleSetDto.getRuleSetName());
		List<RuleSetDTO> existingRules = ruleSets.stream().filter(ruleSet -> 
		ruleSet.getRuleSetName().equalsIgnoreCase(ruleSetDto.getRuleSetName())).collect(Collectors.toList());
		if (existingRules != null && !existingRules.isEmpty())
		{
			logger.error("Ruleset already exists");
			throw new ServiceException(
					"RULESET_"+ResponseMessages.ALREADY_EXISTS);
		}
		
		long ruleSetId = ruleSetDAO.getRuleSetSeqId();
		logger.debug("SEQ RULE SET ID:"+ruleSetId);
		ruleSetDAO.createRuleSet(constructBean(ruleSetDto,ruleSetId));
		
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	@Transactional
	public void updateRuleSet(RuleSetDTO ruleSetDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("Rule SetName:" + ruleSetDto.getRuleSetName());
		logger.debug("Rule Set Name"+ruleSetDto.getRuleSetName());
		String ruleSetName = ruleSetDto.getRuleSetName();
		long ruleSetId = 0;
		if (ruleSetName.contains(":")) {
			String[] splitObj = ruleSetName.split(":");
			ruleSetId = Long.parseLong(splitObj[0]);
			ruleSetName = splitObj[1];
		}

		logger.debug("Rule Set ID"+ruleSetId);
		logger.debug("Rule Set Name"+ruleSetName+"###"+ruleSetDto.toString());
		ruleSetDto.setRuleSetName(ruleSetName);
		int dupCount = getRuleSetById(ruleSetId);
		logger.info("dupCount:" + dupCount);
		if (dupCount > 0) {
			RuleSet rSet = constructBean(ruleSetDto, ruleSetId);
			ruleSetDAO.deleteRuleSet(rSet);
			ruleSetDAO.updateRuleSet(rSet);
		} else {
			logger.error("RuleSet does not exists");
			throw new ServiceException("RULESET_UPDATE_" + ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);
	}
	
	
	private RuleSet constructBean(RuleSetDTO ruleSetDto,long ruleSetId) {
		
		logger.info(CCLPConstants.ENTER);
		RuleSet ruleSet = new RuleSet();
		ruleSet.setRuleSetId(ruleSetId);
		ruleSet.setRuleSetName(ruleSetDto.getRuleSetName());
		ruleSet.setInsUser(ruleSetDto.getInsUser());
		ruleSet.setInsDate(ruleSetDto.getInsDate());
		ruleSet.setLastUpdUser(ruleSetDto.getLastUpdUser());
		ruleSet.setLastUpdDate(ruleSetDto.getLastUpdDate());

		String ruleList = ruleSetDto.getOperationList();
		StringTokenizer tokenizer = new StringTokenizer(ruleList, ",");
		List<RuleRuleSet> ruleSetList = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			String tokenValue = tokenizer.nextToken().trim();
			RuleRuleSet ruleRuleSet = new RuleRuleSet();
			ruleRuleSet.setPrimaryKey(new RuleRuleSetId(ruleSetId, Long.parseLong(tokenValue)));
			ruleRuleSet.setInsUser(ruleSetDto.getInsUser());
			ruleRuleSet.setInsDate(ruleSetDto.getInsDate());
			ruleRuleSet.setLastUpdUser(ruleSetDto.getLastUpdUser());
			ruleRuleSet.setLastUpdDate(ruleSetDto.getLastUpdDate());
			ruleSetList.add(ruleRuleSet);
		}
		ruleSet.setRuleRuleSetMapping(ruleSetList);
		logger.info(CCLPConstants.EXIT);
		return ruleSet;
	}

	@Override
	public List<RuleSetDTO> getRuleSetByName(String ruleSetName) {
		
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();		
	    java.lang.reflect.Type targetListType = new TypeToken<List<RuleSetDTO>>() {}.getType();
	    logger.info(CCLPConstants.EXIT);	
	    return mm.map(ruleSetDAO.getRuleSetByName(ruleSetName), targetListType);
	}
		
	@Override
	public int getRuleSetById(long ruleSetId) {
		
		logger.info(CCLPConstants.ENTER);
		int cnt = ruleSetDAO.getRuleSetById(ruleSetId);
		logger.info(CCLPConstants.EXIT);
		return cnt;
	}
	
	@Override
	public List<Object[]> getRuleDetails(long ruleSetId) throws ServiceException {
		return ruleSetDAO.getRuleDetails(ruleSetId);
	}

}
