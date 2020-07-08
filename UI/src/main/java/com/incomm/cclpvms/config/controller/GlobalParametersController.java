package com.incomm.cclpvms.config.controller;

import java.util.Arrays;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.GlobalParameters;
import com.incomm.cclpvms.config.model.GlobalParametersDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.GlobalParameterService;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.IssuerException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.config.validator.ValidateGlobalParameters;

@Controller

@RequestMapping("/config/globalParameters")
public class GlobalParametersController {
	private static final Logger logger = LogManager.getLogger(GlobalParametersController.class);
	
	public static final String ENTER="Enter";
	public static final String EXIT="Exit";
	
	@Autowired
	public GlobalParameterService globalParameterService;
	
	@Autowired
	public  ValidateGlobalParameters validateGlobalParameters;
	
	
	private static final String GLOBAL_PARAMETERS = "globalParameters";
	private static final String GLOBAL_PARAM_CONFIG = "globalParametersConfig";
	
	
	/**
	 * 
	 * @param model
	 * @param globalParameters
	 * @param request
	 * @param response
	 * @return
	 * @throws ServiceException
	 */
	@PreAuthorize("hasRole('EDIT_GLOBALPARAMETERS')")
	@RequestMapping("/globalParametersConfig")
	public ModelAndView globalParametersConfiguration(Map<String, Object> model,@ModelAttribute(GLOBAL_PARAMETERS) 
	GlobalParameters globalParameters,
			HttpServletRequest request, HttpServletResponse response) throws ServiceException {
	
		logger.debug(ENTER);
		ModelAndView mav=new ModelAndView();
		GlobalParameters globalParameter=null;
		
		Map<String, Object> globalParamData = null;
		globalParamData=globalParameterService.getGlobalParameters();
		globalParameter=new GlobalParameters(globalParamData);
		Map<String, String> domainTypes = globalParameterService.getDomainMetadata();
		mav.addObject("dateformat", globalParamData.get("dateFormat"));
		mav.addObject(GLOBAL_PARAMETERS,globalParameter);
		if (!CollectionUtils.isEmpty(domainTypes)) {
			mav.addObject("domainTypes", domainTypes);
		}
		mav.setViewName(GLOBAL_PARAM_CONFIG);
		
		logger.debug("EXIT");
		return mav;
	}
	
	/**
	 * 
	 * @param model
	 * @param globalParameters
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @throws IssuerException
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT_GLOBALPARAMETERS')")
	@RequestMapping("/saveGlobalParameters")
	public ModelAndView saveGlobalParameters(Map<String, Object> model, @ModelAttribute(GLOBAL_PARAMETERS) GlobalParameters globalParameters,
			BindingResult bindingResult,HttpServletRequest request)
			throws ServiceException {

		logger.debug(ENTER);
		String message = "";

		ModelAndView mav = new ModelAndView();
		Map<String, String> domainTypes = globalParameterService.getDomainMetadata();
		if (!CollectionUtils.isEmpty(domainTypes)) {
			mav.addObject("domainTypes", domainTypes);
		}
		validateGlobalParameters.validate(globalParameters, bindingResult);
		if (bindingResult.hasErrors()) {

			mav.addObject(GLOBAL_PARAMETERS, globalParameters);
			mav.setViewName(GLOBAL_PARAM_CONFIG);

			return mav;

		}

		GlobalParametersDTO globalParametersDTO = new GlobalParametersDTO();
		ResponseDTO responseDTO = null;

		Map<String, Object> globData = null;

		Map<String, Object> globMapData = null;

		Map<String, Object> globalMap = null;

		globalMap = globalParameters.getGlobalParameters();

		globalParametersDTO.setGlobalParameters(globalMap);

		logger.debug("Before Calling globalParametersService");
		responseDTO = globalParameterService.updateGlobalParameters(globalParametersDTO);
		logger.debug("after Calling globalParametersService");
		logger.info("Message from globalParametersService.add method: " + responseDTO.getMessage());

		globData = (Map<String, Object>) responseDTO.getData();

		globMapData = (Map<String, Object>) globData.get(GLOBAL_PARAMETERS);

		globalParameters = new GlobalParameters(globMapData);

		logger.debug(
				"CCLP-VMS Response Code : " + responseDTO.getCode() + " Error Message : " + responseDTO.getMessage());
		if (responseDTO.getCode().equalsIgnoreCase("000")) {
			message = responseDTO.getMessage();
			mav.addObject("status", message);
			mav.setViewName(GLOBAL_PARAM_CONFIG);
			mav.addObject(GLOBAL_PARAMETERS, globalParameters);

		} else {
			message = responseDTO.getMessage();
			mav.addObject("statusMessage", message);
			mav.setViewName(GLOBAL_PARAM_CONFIG);
			mav.addObject(GLOBAL_PARAMETERS, globalParameters);
		}

		logger.debug("EXIT");
		return mav;

	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.info("exceptionHandler Method for global parameter Starts Here");
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException||exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();
		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("failstatus", errMessage);
		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.info("exceptionHandler Method for global parameter Ends Here");

		return mav;
	}
	
	
	
	
}
