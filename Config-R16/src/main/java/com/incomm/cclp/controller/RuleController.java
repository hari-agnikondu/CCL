package com.incomm.cclp.controller;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.dto.RuleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RuleService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

/**
 * @author Raja
 */

@RestController
@RequestMapping("/rules")
@Api(value = "rule")
public class RuleController {
	
	private static final Logger logger = LogManager.getLogger(RuleController.class);
	
	@Autowired
	private RuleService ruleService;

	@Autowired
	private ResponseBuilder responseBuilder;
	
	
	
	/**
	 * Creates a rule.
	 * 
	 * @param ruleDto The RuleDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createRule(@RequestBody RuleDTO ruleDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		ValidationService.validateRule(ruleDto, true);
		
		logger.debug("Creating new rule in tatble {}",ruleDto.toString());	
		ruleService.createRule(ruleDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("RULE_ADD_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		 valuesMap.put(CCLPConstants.RULE_NAME, ruleDto.getRuleName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		
		logger.info("Rule created successfully");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Gets all Active Rules.
	 *
	 * @return the ResponseEntity with the result.
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllRules() {
		logger.info(CCLPConstants.ENTER);
		List<RuleDTO> ruleDtos = ruleService.getAllRules();
		logger.info("Performing full search for rules");
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(ruleDtos, 
				ResponseMessages.ALL_SUCCESS_RULE_RETRIEVE,ResponseMessages.SUCCESS);
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Updates a rule.
	 * 
	 * @param ruleDto The RuleDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping (method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateRule(@RequestBody RuleDTO ruleDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		ValidationService.validateRule(ruleDto, false);
		
		logger.debug(ruleDto.toString());	
		ruleService.updateRule(ruleDto);
		logger.debug("Updating rule details for {} as {}",ruleDto.getRuleName(),ruleDto.toString());
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("RULE_UPDATE_"+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
		
		 valuesMap.put(CCLPConstants.RULE_NAME, ruleDto.getRuleName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	/**
	 * Gets a rule by Rule Id.
	 * 
	 * @param ruleId The id of the Rule to be retrieved.

	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{ruleId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRuleById(@PathVariable("ruleId") Long ruleId) {
		logger.info(CCLPConstants.ENTER);
		RuleDTO rule;
		 Map<String, String> valuesMap = new HashMap<>();
	
		ResponseDTO responseDto = null;
		
		if (ruleId <= 0)
		{
			logger.info("Rule Id is negative: {}",ruleId);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_RULE_ID,ResponseMessages.DOESNOT_EXISTS);
		}
		else
		{
			RuleDTO ruleDto = ruleService.getRuleById(ruleId);
			if(ruleDto == null) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_RULE_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
				logger.error("Failed to fetch rule details for rule {}", ruleId);
			}
			else {
				responseDto = responseBuilder.buildSuccessResponse(ruleDto,("RULE_RETRIVE_"+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
				rule= ruleService.getRuleById(ruleId);
				
				 valuesMap.put(CCLPConstants.RULE_NAME, rule.getRuleName());
				 String templateString = "";

				 templateString=responseDto.getMessage();
				
				 StrSubstitutor sub = new StrSubstitutor(valuesMap);
				 String resolvedString = sub.replace(templateString);
				 
				 responseDto.setMessage(resolvedString);
				logger.debug("Rule record for ruleId: {} has retrieved successfully {}",ruleId,ruleDto.toString());
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Gets a rule by name.
	 * 
	 * @param ruleName The name of the Rule to be retrieved.

	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRuleByName(@RequestParam("ruleName") String ruleName) {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		ResponseDTO responseDto = null;
		
		if (Util.isEmpty(ruleName))
		{
			logger.info("Rule name is empty");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_RULE_NAME,ResponseMessages.DOESNOT_EXISTS);
		}
		else
		{
			List<RuleDTO> ruleDtos = ruleService.getRulesByName(ruleName);
			if(ruleDtos.isEmpty()) {
				
				logger.error("Failed to retrive rule details for RuleName: {}",ruleName);
				
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_RULE_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
				
				 valuesMap.put(CCLPConstants.RULE_NAME,ruleName);
				 String templateString = "";

				 templateString=responseDto.getMessage();
				
				 StrSubstitutor sub = new StrSubstitutor(valuesMap);
				 String resolvedString = sub.replace(templateString);
				 
				 responseDto.setMessage(resolvedString);
				
			}
			else {
				responseDto = responseBuilder.buildSuccessResponse(ruleDtos,"RULE_RETRIVE_"+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS);
				
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ruleConfigs", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRuleConfig() throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> ruleList =  ruleService.getRuleConfig();
		HashMap<String, String> ruleMap = new HashMap<>();
		
		ruleList.stream().forEach(record -> 
	        ruleMap.put(record[0].toString(),clob2String(record[1]))
		);

		responseDto = responseBuilder.buildSuccessResponse(ruleMap, ResponseMessages.SUCCESS_RULE_CONFIG_RET,
				ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	public static final String clob2String(final Object value) {
		logger.info(CCLPConstants.ENTER);
		final Clob clobValue = (Clob) value;
		String result = null;

		try {
			final long clobLength = clobValue.length();

			if (clobLength < Integer.MIN_VALUE || clobLength > Integer.MAX_VALUE) {
				logger.info("CLOB size too big for String!");
			} else {
				result = clobValue.getSubString(1, (int) clobValue.length());
			}
		} catch (SQLException e) {
			logger.error("tryClob2String ERROR: {}" + e);
			try {
				clobValue.free();
			} catch (SQLException e1) {
				logger.error(e1);
			}
		} 
		logger.info(CCLPConstants.EXIT);
		return result;
	}
	/**
	 * Gets Transactions List  by Transaction type
	 */
	@RequestMapping(value = "/{txnTypeId}/transaction", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getTransactionsByTxnType(@PathVariable("txnTypeId") String txnTypeId) {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;
		List<Object[]> transaction = null;
		transaction = ruleService.getTransactionsByTxnType(txnTypeId);
		if (transaction == null) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_TRANSACTION_TYPE_RETRIEVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error("Failed to fetch transaction details for transaction type {}", txnTypeId);
		} else {
			responseDto = responseBuilder.buildSuccessResponse(transaction,
					("TRANSACTIONS_RETRIVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		

			logger.debug("Transaction list fortxnTypeId : {} has retrieved successfully {}", txnTypeId, transaction);
		}

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

}
