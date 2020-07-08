package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.CCFConfDetail;
import com.incomm.cclpvms.config.model.CCFConfigReq;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.CCFService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

/*
 * CCF Controller handles all incoming requests.
 */
@Controller
@RequestMapping("/config/ccf")
public class CCFController {

	private static final Logger logger = LogManager.getLogger(CCFController.class);

	@Autowired
	public CCFService ccfService;

	@Value("${INS_USER}")
	public long insUser;

	/*
	 * This Methods display the CCF version details in UI page.
	 */
	@RequestMapping("/ccfConfig")
	public ModelAndView ccfConfiguration() throws ServiceException {
		logger.info(" ENTER ccfConfiguration ");
		ModelAndView mav = new ModelAndView();
		Map<String, List> ccfList = null;
		ccfList = ccfService.getCCFList();
		mav.addObject("ccfData", ccfList.get("ccfData"));
		mav.addObject("ccfForm", new CCFConfDetail());
		mav.setViewName("ccfConfig");
		logger.info("EXIT ccfConfiguration");
		return mav;
	}

	/*
	 * This Methods pull the CCF parameters to display the values in the
	 * dropdown and this method will be invoked through Ajax call.
	 */
	@RequestMapping(value = "/ccfparam", method = RequestMethod.GET)
	public @ResponseBody String ccfparam() throws ServiceException {
		logger.info(" ENTER ccfparam ");
		String result = "";
		result = ccfService.getCCFParam();
		logger.info(" EXIT ccfparam" + result);
		return result;
	}

	/*
	 * This Methods pull the CCF mapping keys to display the values in the
	 * dropdown and this method will be invoked through Ajax call.
	 */
	@RequestMapping(value = "/ccfkey", method = RequestMethod.GET)
	public @ResponseBody String ccfkey() throws ServiceException {
		logger.info(" ENTER ccfkey ");
		String result = "";
		result = ccfService.getCCFKey();
		logger.info(" EXIT ccfkey ");
		return result;
	}

	/*
	 * This method is to display the edit CCF screen to the user.
	 */
	@RequestMapping(value = "/showEditCCF", method = RequestMethod.GET)
	public @ResponseBody String showEditCCF(HttpServletRequest request) throws ServiceException {
		logger.info("****ENTER showEditCCF Method********");
		String versionID = request.getParameter("versionId");
		logger.info("versionID : " + versionID);
		String result = ccfService.getCCFVersionDtls(versionID);
		logger.info("EXIT showEditCCF inside");
		return result;
	}

	/*
	 * This method is to display the edit CCF screen to the user.
	 */
	@RequestMapping(value = "/addCCF", method = RequestMethod.POST)
	public @ResponseBody String addCCFConfiguration(@RequestBody CCFConfigReq request) throws ServiceException {
		logger.info("****ENTER addCCFConfiguration Method********" + request.toString());
		String verionID = request.getVersionID();
		ResponseDTO responseDTO = null;
		String message ="";
		if (verionID != null) {
			if (verionID.equals("N")) // N : New version to be added in the database
			{
				logger.info("****New CCF version to be added in the DB");
				request.setVersionID(request.getVersionName());
				responseDTO = ccfService.addCCFConfiguration(request);
			} else {
				logger.info("****Update the existing CCF version");
				 request.setVersionName(request.getVersionID());
				responseDTO = ccfService.updateCCFConfiguration(request);
			}

			message = responseDTO.getMessage();
			logger.debug("RESPONSE CODE: " + responseDTO.getCode() + " Error Message : " + message);

		}
		logger.info("EXIT addCCFConfiguration inside");
		return message;
	}
	
	/*
	 * This Methods display the CCF Result details in UI page.
	 */
	@RequestMapping("/ccfConfigResult")
	public ModelAndView ccfFailureResultPage(){
		logger.info(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("ccfConfigResult");
		logger.info(CCLPConstants.EXIT);
		return mav;
	}

	
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("statusMessage", errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.error(exception.getMessage());
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

}
