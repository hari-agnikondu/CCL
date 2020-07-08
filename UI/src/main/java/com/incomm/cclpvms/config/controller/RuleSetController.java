package com.incomm.cclpvms.config.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.RuleSetDTO;
import com.incomm.cclpvms.config.model.Ruleset;
import com.incomm.cclpvms.config.service.RuleSetService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;

@Controller
@RequestMapping("/config/ruleSet")
public class RuleSetController {

	@Autowired
	private RuleSetService ruleSetService;

	@Autowired
	private SessionService sessionService;

	private static final String RULESET_FORM = "ruleSetForm";
	private static final String RULESET = "ruleSet";
	private static final String RULES = "rules";

	private static final Logger logger = LogManager.getLogger(RuleSetController.class);

	/*
	 * This Methods displays the ruleset config page
	 */
	@RequestMapping("/ruleSetConfig")
	public ModelAndView rulesetConfiguration() throws ServiceException {
		logger.debug(" ENTER rulesetConfiguration ");
		ModelAndView mav = new ModelAndView();
		Ruleset ruleset = new Ruleset();

		mav.addObject(RULESET, ruleSetService.getRuleSet());
		System.out.println("ruleSet check"+ruleSetService.getRuleSet());
		mav.addObject(RULES, ruleSetService.getRule());
		System.out.println("ruleCheck"+ruleSetService.getRule());
		mav.addObject(RULESET_FORM, ruleset);
		mav.setViewName(CCLPConstants.RULE_SET_CONFIG);
		logger.debug(" EXIT rulesetConfiguration");
		return mav;
	}

	@RequestMapping("/addRuleSet")
	public ModelAndView addRuleSet(Map<String, Object> model, @ModelAttribute("ruleSetForm") Ruleset ruleSetForm,
			BindingResult bindingResult, HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		String opsList;
		String delList = "";
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDto = null;

		mav.setViewName(CCLPConstants.RULE_SET_CONFIG);

		logger.debug(" RuleSet ID:" + ruleSetForm.getRuleSetId());
		logger.debug(" RuleSet Name text:" + ruleSetForm.getRuleSetNametxt());
		logger.debug(" Rule ID:" + ruleSetForm.getRuleId());
		logger.debug(" original Opertaion List:" + ruleSetForm.getOperationList());
		logger.debug(" Deleted Opertaion List:" + ruleSetForm.getDeletedValues());

		if (bindingResult.hasErrors()) {
			mav.addObject("ruleSetForm", ruleSetForm);
			logger.error("Some error occured while binding the ruleSetForm object");
			return mav;
		}

		RuleSetDTO ruleSetDto = new RuleSetDTO();
		opsList = ruleSetForm.getOperationList();
		delList = ruleSetForm.getDeletedValues();

		// Remove the records original List from the deleted records.
		if (delList.length() > 0) {
			List<String> myList = new ArrayList<>(Arrays.asList(opsList.split(",")));
			List<String> subsetList = new ArrayList<>(Arrays.asList(delList.split(",")));
			if (myList.removeAll(subsetList)) {
				String operationList = myList.toString().replace("]", " ").replace("[", " ").trim();
				ruleSetDto.setOperationList(operationList);
			}
		} else {
			ruleSetDto.setOperationList(ruleSetForm.getOperationList());
		}

		logger.debug(" rule List pass to services:" + ruleSetForm.getOperationList());
		ruleSetDto.setInsUser(sessionService.getUserId());
		ruleSetDto.setLastUpdUser(sessionService.getUserId());
		ruleSetDto.setInsDate(new Date());
		ruleSetDto.setLastUpdDate(new Date());

		if (ruleSetForm.getRuleSetId() > 0) {
			ruleSetDto.setRuleSetName(ruleSetForm.getRuleSetId() + ":" + ruleSetForm.getRuleSetNametxt());
			responseDto = ruleSetService.updateRuleSet(ruleSetDto);

		} else {

			ruleSetDto.setRuleSetName(ruleSetForm.getRuleSetNametxt());
			responseDto = ruleSetService.createRuleSet(ruleSetDto);
		}

		logger.debug("RESPONSE CODE: " + responseDto.getCode() + " Response Message : " + responseDto.getMessage());

		if (ResponseMessages.SUCCESS.equals(responseDto.getCode().trim())) {

			logger.info("RuleSet record '{}' has been added successfully", ruleSetForm.getRuleSetNametxt());
			mav.addObject("statusFlag", "success");
			mav.addObject("statusMessage", responseDto.getMessage());
		} else {
			mav.addObject("statusFlag", "fail");
			mav.addObject("statusMessage", responseDto.getMessage());
		}
		Ruleset ruleset = new Ruleset();

		mav.addObject(RULESET, ruleSetService.getRuleSet());
		mav.addObject(RULES, ruleSetService.getRule());
		mav.addObject(RULESET_FORM, ruleset);
		mav.setViewName(CCLPConstants.RULE_SET_CONFIG);

		return mav;
	}
	
	
	/*
	 * This Methods invoked from the ajax call to pull the rule details
	 */
	@RequestMapping(value = "/ruleDetails", method = RequestMethod.GET)
	public @ResponseBody String ruleDetails(HttpServletRequest request) throws ServiceException {
		logger.info(" ENTER ruleDetails ");
		String result = "";
		long ruleSetId =0;
		String reqValue = request.getParameter("ruleSetId");
		if(reqValue != null)
		{
			ruleSetId = Long.parseLong(reqValue);	
		}
		logger.info("ruleSetId : " + ruleSetId);
		result = ruleSetService.getRuleDetails(ruleSetId);
		logger.info(" EXIT ruleDetails "+result);
		return result;
	}

}
