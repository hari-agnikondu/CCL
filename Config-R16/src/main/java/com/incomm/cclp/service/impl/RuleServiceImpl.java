package com.incomm.cclp.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.RuleDAO;
import com.incomm.cclp.domain.Rule;
import com.incomm.cclp.domain.RuleDetails;
import com.incomm.cclp.domain.RuleRuleDetailId;
import com.incomm.cclp.dto.RuleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RuleService;

@Service
public class RuleServiceImpl implements RuleService {
	
	@Autowired
	private RuleDAO ruleDAO;
	
	private  final Logger logger = LogManager.getLogger(this.getClass());
	
	/**
	 * Create an rule.
	 * 
	 * @param ruleDto The RuleDTO to be created.
	 * @throws ServiceException 
	 */
	@Override
	@Transactional
	public void createRule(RuleDTO ruleDto) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		// Make sure rule does not already exist
		List<RuleDTO> rules = getRulesByName(ruleDto.getRuleName());
		List<RuleDTO> existingRules = rules.stream().filter(rule -> 
		rule.getRuleName().equalsIgnoreCase(ruleDto.getRuleName())).collect(Collectors.toList());
		if (existingRules != null && !existingRules.isEmpty())
		{
			logger.error("Rule already exists");
			throw new ServiceException(
					"RULE_"+ResponseMessages.ALREADY_EXISTS);
		}
		
		
		 ModelMapper mm = new ModelMapper();
		 ruleDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		 ruleDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));
		 Rule rule = mm.map(ruleDto, Rule.class);	
		 ruleDAO.createRule(rule);
		 //creating rule filter
		 for(Entry<String,String> set:ruleDto.getRuleDetails().entrySet())
		 {
            
			 RuleRuleDetailId primaryKey=new RuleRuleDetailId(Integer.parseInt(set.getKey()),rule.getRuleId());
			 RuleDetails ruleDetail=new RuleDetails(primaryKey,set.getValue(),ruleDto.getLastUpdUser(),ruleDto.getInsUser());
			 ruleDAO.createRuleDetail(ruleDetail);
			 logger.info("Record created for RuleID-RuleDetailID:"+ruleDetail.getPrimaryKey().getRuleID()+"-"+ruleDetail.getPrimaryKey().getRuleDetailsId());
			 
		 }
		 
		 logger.info("Record created for :"+rule.getRuleId());
		 ruleDto.setRuleId(rule.getRuleId());
		 logger.info(CCLPConstants.EXIT);
	}
	
	
	/**
	 * Gets all Rules.
	 * 
	 * @return the list of all Rules.
	 */
	@Override
	public List<RuleDTO> getAllRules() {
		
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
	    java.lang.reflect.Type targetListType = new 
	    		TypeToken<List<RuleDTO>>() {}.getType();
	    logger.info(CCLPConstants.EXIT);
	    return mm.map(ruleDAO.getAllRules(), 
	    		targetListType);
	}
	
	
	/**
	 * Updates an rule.
	 * 
	 * @param ruleDto The RuleDTO to be updated.
	 */
	@Override
	@Transactional
	public void updateRule(RuleDTO ruleDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside updateRule with data :"+ruleDto.toString());

		RuleDTO ruleloc= null;
		List<RuleDTO> existingRules = getRulesByName(ruleDto.getRuleName());
        if (existingRules != null && !existingRules.isEmpty()) {
            Iterator<RuleDTO> iterator = existingRules.iterator();
            while(iterator.hasNext())
            {
            	ruleloc = iterator.next();
                if(ruleloc.getRuleName().equalsIgnoreCase(ruleDto.getRuleName())
                		&& (ruleloc.getRuleId()!=ruleDto.getRuleId()) )
                {
                	throw new ServiceException("RULE_"+ResponseMessages.ALREADY_EXISTS);
                }
                    
            }
            
        }
       

        ModelMapper mm = new ModelMapper();
        ruleDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));
        Rule rule = mm.map(ruleDto, Rule.class);
       
        ruleDAO.updateRule(rule);
      
      
		 //creating rule filter
		for(Entry<String,String> set:ruleDto.getRuleDetails().entrySet())
			 {
				 RuleRuleDetailId primaryKey=new RuleRuleDetailId(Integer.parseInt(set.getKey()),ruleDto.getRuleId());
				 RuleDetails ruleDetail=new RuleDetails(primaryKey,set.getValue(),ruleDto.getLastUpdUser(),ruleDto.getInsUser());
				
				 ruleDAO.updateRuleDetail(ruleDetail);
				
				
				 logger.info("after updating data for RuleID-RuleDetailID:"+ruleDetail.getPrimaryKey().getRuleID()+"-"+ruleDetail.getPrimaryKey().getRuleDetailsId());
				 
			 }
		
		logger.info("after updating data for :"+rule.getRuleId());
		logger.info(CCLPConstants.EXIT);
		
	}

	
	/**
	 * Deletes an rule.
	 * 
	 * @param ruleDto The RuleDTO to be deleted.
	 */
	@Override
	public void deleteRule(RuleDTO ruleDto) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside deleteRule with data :"+ruleDto.toString());
		 ModelMapper mm = new ModelMapper();
		 Rule rule = mm.map(ruleDto, Rule.class);
		ruleDAO.deleteRule(rule);
		logger.info("after deleting data for :"+rule.getRuleId());
		logger.info(CCLPConstants.EXIT);
	}
	
	
	/**
	 * Gets an Rule by ID.
	 * 
	 * @param ruleId The rule id for the Rule to be retrieved.
	 * 
	 * @return the RuleDTO.
	 */
	@Override
	public RuleDTO getRuleById(long ruleId) {
		ModelMapper mm = new ModelMapper();
		
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getRuleById with data : "+ruleId);
		Rule rule = ruleDAO.getRuleById(ruleId);
		if(rule!=null) {
		
		logger.info("after retrieving data for RuleId : "+rule.getRuleId());
		}else {
			rule=new Rule();
		}
		logger.info(CCLPConstants.EXIT);
		return mm.map(rule, RuleDTO.class);
	}
	
	
	/**
	 * Gets an Rule by Name.
	 * 
	 * @param ruleName The rule name for the Rule to be retrieved.
	 * 
	 * @return the List of RuleDTOs.
	 */
	@Override
	public List<RuleDTO> getRulesByName(String ruleName)  {	
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();		
	    java.lang.reflect.Type targetListType = new TypeToken<List<RuleDTO>>() {}.getType();
		logger.info(CCLPConstants.EXIT);		
	    return mm.map(ruleDAO.getRulesByName(ruleName), targetListType);

	}


	/**
	 * Gets all the rule configurations
	 * 
	 * @return the List of rule configurations.
	 */
	@Override
	public List<Object[]> getRuleConfig() throws ServiceException {

		return ruleDAO.getRuleConfig();
	}
	
	
	@Override
	public List<Object[]> getTransactionsByTxnType(String txnTypeId) {
		
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getTransactionsByTxnType with data : "+ txnTypeId);
		List<Object[]> txnList = ruleDAO.getTransactionsByTxnType(txnTypeId);
		logger.info(CCLPConstants.EXIT);
		return txnList;
	}
}
