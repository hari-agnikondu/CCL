package com.incomm.cclpvms.config.controller;


import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.Rule;
import com.incomm.cclpvms.config.model.RuleDTO;
import com.incomm.cclpvms.config.service.RuleService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/rule")
public class RuleController {
	
	@Autowired
	private RuleService ruleService;
	
	@Autowired
	private SessionService sessionService;
	
	
	private static final String RULES = "rules";
	private static final String FILTERS = "filters";
	private static final String OPERATORS_LIST = "operators_list";
	private static final String OPERATORS_CONFIG = "OPERATORS_CONFIG";
	private static final String OPERATORS = "operators";
	private static final String RULE_TRANSACTION_TYPE = "RULE_TRANSACTION_TYPE";
	private static final String TRANSACTION_TYPES = "txnTypes";

	private static final Logger logger = LogManager.getLogger(RuleController.class);
	
	
	public void init(Rule rule, ModelAndView mav) throws ServiceException
	{
		Map<String, String> txnTypes = new HashMap<>();

		mav.addObject(RULES, ruleService.getRulesList());
		Map<String, String> ruleConfigs = ruleService.getRuleConfig();
		
		if (ruleConfigs != null)
		{
			rule.setFilters(ruleConfigs.get(FILTERS));
			mav.addObject(OPERATORS_LIST,ruleConfigs.get(OPERATORS_LIST));
			mav.addObject(OPERATORS_CONFIG,ruleConfigs.get(OPERATORS_CONFIG));
			mav.addObject(OPERATORS,ruleConfigs.get(OPERATORS));
			String txnTypesList = ruleConfigs.get(RULE_TRANSACTION_TYPE);
			if (!Util.isEmpty(txnTypesList))
			{
				String[] txnTypeArr = txnTypesList.split(",");
				for(String txnType : txnTypeArr)
				{
					String[] txnTypeSplit = txnType.split(":");
					if (txnTypeSplit != null && txnTypeSplit.length > 1)
						txnTypes.put(txnTypeSplit[0], txnTypeSplit[1]);
				}
			}
		}
		
		mav.addObject(TRANSACTION_TYPES, txnTypes);
	}
	
	
	/*
	 * This Methods displays the rule config page
	 */
	@PreAuthorize("hasRole('SEARCH_RULE')")
	@RequestMapping("/ruleConfig")
	public ModelAndView ruleConfiguration() throws ServiceException {
		
		logger.debug(" ENTER ruleConfiguration ");
		ModelAndView mav = new ModelAndView();
		Rule rule = new Rule();

		init(rule, mav);
		
		mav.addObject(CCLPConstants.RULE_FORM, rule);
		mav.setViewName("ruleConfig");
		logger.debug(" EXIT ruleConfiguration");
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_RULE')")
	@RequestMapping(value = "/ruleOperators", method = RequestMethod.GET)
	public @ResponseBody Map<String,String> getRuleConfig() throws ServiceException {
		logger.debug(" ENTER ruleConfig ");

		logger.debug(" EXIT ruleConfig");
		
		return ruleService.getRuleConfig();
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('ADD_RULE')")
	@RequestMapping("/saveRule")
	public ModelAndView saveRule(Map<String, Object> model,
		@ModelAttribute("ruleForm") Rule ruleForm,
			BindingResult bindingResult,HttpServletRequest request)
					throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("ruleConfig");
		
		if (bindingResult.hasErrors()) {
			mav.addObject(CCLPConstants.RULE_FORM, ruleForm);
			logger.error("Some error occured while binding the ruleForm object");
			return mav;
		}
		
		RuleDTO ruleDto =new RuleDTO();
		ruleDto.setActionType(ruleForm.getAction());
		ruleDto.setRuleName(ruleForm.getRuleName());
		ruleDto.setJsonReq(ruleForm.getJsonReq());
		Object[] ruledet = getRuleDetails(ruleForm.getJsonReq());
		String ruleexpression=(String)ruledet[0];
		LinkedHashMap<String,String> lhm = (LinkedHashMap<String,String>)ruledet[1];
		
		ruleDto.setRuleExp(ruleexpression);
		ruleDto.setRuleDetails(lhm);
		ruleDto.setTxnTypeId(ruleForm.getTxnTypeIds());
		ruleDto.setInsUser(sessionService.getUserId());
		ruleDto.setLastUpdUser(sessionService.getUserId());
	 
		ResponseDTO responseDto = null;
		if (ruleForm.getRuleId() > 0)
		{
			ruleDto.setRuleId(ruleForm.getRuleId());
			responseDto = ruleService.updateRule(ruleDto);
		}
		else
			responseDto = ruleService.createRule(ruleDto);
	
		
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase("success")) 
			{
				logger.info("Rule record '{}' has been added successfully", ruleForm.getRuleName());

				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				Rule newRule = new Rule();
				mav.addObject(CCLPConstants.RULE_FORM, newRule);
				init(newRule, mav);

			} else 
			{
				logger.error("Error while adding rule"+responseDto.getResult());
				
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				else
					mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.ADD_FAIL) + " '"
							+ ruleForm.getRuleName() + "'");
				
				mav.addObject(CCLPConstants.RULE_FORM, ruleForm);
				init(ruleForm, mav);
			}
		}
		
		logger.debug(" EXIT ruleConfiguration");
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_RULE')")
	@RequestMapping(value = "/rules", method = RequestMethod.GET)
	public @ResponseBody List<Rule> getRules(@RequestParam("ruleName") String ruleName) throws ServiceException {

		logger.debug(" ENTER getRules ");

		logger.debug(" EXIT getRules");
		return ruleService.getRulesByName(ruleName);
	}
	@RequestMapping(value = "/viewRule", method = RequestMethod.GET)
	public @ResponseBody ModelAndView viewRule(@RequestParam("ruleId") Long ruleId) throws ServiceException {

		logger.debug(" ENTER viewRule");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("viewRule");
		
		Rule rule = new Rule();
		init(rule, mav);
		mav.addObject(CCLPConstants.RULE_FORM, rule);
	
		Rule rulebyId=ruleService.getRulesById(ruleId);
		rule.setJsonReq(rulebyId.getJsonReq());
		rule.setTxnTypeId(rulebyId.getTxnTypeId());
		rule.setRuleName(rulebyId.getRuleName());
		rule.setAction(rulebyId.getAction());
		
		logger.debug(" EXIT viewRule");
		return mav;
		}
	@PreAuthorize("hasRole('SEARCH_RULE')")
	@RequestMapping(value = "/viewTransactions")
	public @ResponseBody ModelAndView getTransactionByTxnType(HttpServletRequest request, HttpServletResponse response) throws ServiceException {

		logger.debug(" ENTER getTransactionByTxnType ");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("viewTransaction");
		String txnTypeId=Util.isEmpty(request.getParameter("txnTypeId"))?"":request.getParameter("txnTypeId");
		logger.debug("txnTypeId is " , txnTypeId);
		List<Object[]> txnList=ruleService.getTransactionByTxnType(txnTypeId);
		mav.addObject("txnList",txnList);
		logger.debug(" EXIT getTransactionByTxnType");
		return mav;
	}

	public Object[] getRuleDetails(String jsonval) {

		Object[] ret = new Object[3];
		logger.debug("jsonval in getRuleDetails"+jsonval);
		JsonParser parser = Json.createParser(new StringReader(jsonval));
		StringBuilder sb = new StringBuilder();
		StringBuilder tmp = new StringBuilder();
		String condition = null;
		String key = "";
		int ob = 0;
		int op = 0;
		boolean isValueArray = false;
		Stack<String> stack = new Stack<>();
		LinkedHashMap<String, String> hm = new LinkedHashMap<>();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder tmp1 = new StringBuilder();
		StringBuilder tmp2 = new StringBuilder();
		StringBuilder keyvalarr = new StringBuilder();
		int sno = 1;
		boolean isFirst = true;
		String keyval = "";
		while (parser.hasNext()) {
			JsonParser.Event event = parser.next();
			switch (event) {
			case START_ARRAY:
				if (key.equals("value") && !isValueArray) {
					isValueArray = true;
				} else {
					ob = 0;
					tmp.append("( ");
					tmp1.append("( ");
				}
				break;
			case START_OBJECT:
				op = 0;

				break;
			case END_ARRAY:
				if (isValueArray) {
					tmp.append(keyvalarr.toString());
					tmp2.append(keyvalarr.toString());
					tmp1.append(sno);
					hm.put(String.valueOf(sno++), tmp2.toString());
					isValueArray = false;
					key = "";
				} else {
					stack.pop();
					sb.append(" )");
					sb1.append(" )");
				}
				break;
			case END_OBJECT:
				if (tmp.toString().length() > 0 && op == 0) {
					if (ob == 0) {
						if (stack.size() >= 2)
							condition = stack.get((stack.size() - 2));
						else
							condition = stack.peek();
					} else
						condition = stack.peek();
					sb.append((isFirst ? "" : " " + condition + " ") + tmp.toString());
					sb1.append((isFirst ? "" : " " + condition + " ") + tmp1.toString());
					isFirst = false;

					tmp.delete(0, tmp.toString().length());
					tmp1.delete(0, tmp1.toString().length());
					tmp2.delete(0, tmp2.toString().length());
					keyvalarr.delete(0, keyvalarr.toString().length());
				}

				ob++;
				op++;
				break;
			case KEY_NAME:
				key = parser.getString();

				break;
			case VALUE_FALSE:
			case VALUE_NULL:
			case VALUE_TRUE:
			case VALUE_STRING:
			case VALUE_NUMBER:

				if (("~id~operator~value~").indexOf("~" + key + "~") != -1) {
					if (!event.toString().equals("VALUE_NULL")) {
						if (event.toString().equals("VALUE_TRUE"))
							keyval = "true";
						else if (event.toString().equals("VALUE_FALSE"))
							keyval = "false";
						else
							keyval = parser.getString();
						if (isValueArray) {
							keyvalarr.append((keyvalarr.toString().length() == 0 ? " " : ",") + keyval);
						} else {
							tmp.append((key.equals("id") ? "" : " ") + keyval);
							tmp2.append((key.equals("id") ? "" : " ") + keyval);
						}
					}

					if (("~value~").indexOf("~" + key + "~") != -1 && !isValueArray) {
						tmp1.append(sno);
						hm.put(String.valueOf(sno++), tmp2.toString());
					}
				} else if (key.equals("condition")) {
					condition = parser.getString();
					stack.push(condition);
				}
				break;

			}
		}

		ret[0] = sb1.toString();
		ret[1] = hm;
		ret[2] = sb.toString();
		return ret;
	}
}
