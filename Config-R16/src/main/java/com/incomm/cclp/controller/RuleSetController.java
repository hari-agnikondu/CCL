
package com.incomm.cclp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.incomm.cclp.dto.RuleSetDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RuleSetService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;

import io.swagger.annotations.Api;
/**
 * Rule Set Controller provides all the REST operations for Rule Set. 
 */
@RestController
@RequestMapping("/ruleSet")
@Api(value="rule set")
public class RuleSetController {

	// the CardRangeService service.
	@Autowired
	private RuleSetService ruleSetService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;


	// the logger
	private static final Logger logger = LogManager.getLogger(RuleSetController.class);

	/**
	 * Getting rule set details
	 * 
	 * */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRuleSet(){
		logger.info(CCLPConstants.ENTER);
		
		List<RuleSetDTO> ruleSetList=ruleSetService.getRuleSet();
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(ruleSetList,ResponseMessages.SUCCESS_RULESET_RETRIEVE,"");
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/*
	 * Creates a ruleSet.
	 * 
	 * @param ruleDto The RuleDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createRuleSet(@RequestBody RuleSetDTO ruleSetDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		
		logger.debug("Creating new ruleset in tatble {}",ruleSetDto.toString());	
		ruleSetService.createRuleSet(ruleSetDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("RULESET_ADD_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		 valuesMap.put("RuleSetName", ruleSetDto.getRuleSetName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 responseDto.setMessage(resolvedString);
		
		logger.info("Rule created successfully");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	

	/*
	 * Update a ruleSet.
	 * 
	 * @param ruleDto The RuleDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateRuleSet(@RequestBody RuleSetDTO ruleSetDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();

		logger.debug("Creating new ruleset in tatble {}",ruleSetDto.toString());	
		ruleSetService.updateRuleSet(ruleSetDto);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("RULESET_UPDATE_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		 valuesMap.put("RuleSetName", ruleSetDto.getRuleSetName());
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
	 * Gets a ruleset by name.
	 * 
	 * @param rulesetName  The name of the RuleSet to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRuleByName(@RequestParam("ruleSetName") String ruleSetName) {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		ResponseDTO responseDto = null;

		if (Util.isEmpty(ruleSetName)) {
			logger.error("RuleSet name is empty");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_RULESET_NAME,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			List<RuleSetDTO> ruleSetDtos = ruleSetService.getRuleSetByName(ruleSetName);
			if (ruleSetDtos.isEmpty()) {

				logger.error("Failed to retrive rule details for RuleSetName: {}", ruleSetName);

				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_RULESET_RETRIEVE,
						ResponseMessages.DOESNOT_EXISTS);

				valuesMap.put("ruleSetName", ruleSetName);
				String templateString = "";

				templateString = responseDto.getMessage();

				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);

				responseDto.setMessage(resolvedString);

			} else {
				responseDto = responseBuilder.buildSuccessResponse(ruleSetDtos,
						"RULESET_RETRIVE_" + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);

			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{ruleSetId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRuleDetails(@PathVariable("ruleSetId") String ruleSetId) 
			throws ServiceException, JSONException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if(ruleSetId.length()> 0)
		{
		logger.debug("RuleSetId",Long.parseLong(ruleSetId));
		List<Object[]> ruleList = ruleSetService.getRuleDetails(Long.parseLong(ruleSetId));
		JSONArray jsonParamArray = new JSONArray();
		String outputJsonArray = "";

		for (Object[] result : ruleList) {
			JSONObject jObj = new JSONObject();
			jObj.put("value", result[0]);
			jObj.put("key", result[1]);
			jsonParamArray.put(jObj);
		}
		
		outputJsonArray = jsonParamArray.toString();
		logger.debug("getRuleDetails {}",outputJsonArray);
		responseDto = responseBuilder.buildSuccessResponse(outputJsonArray, 
				ResponseMessages.ALL_SUCCESS_RULESET_RETRIEVE,
				ResponseMessages.SUCCESS);
		}
		else
		{
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.FAILED_RULESET_RETRIEVE, 
					ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
