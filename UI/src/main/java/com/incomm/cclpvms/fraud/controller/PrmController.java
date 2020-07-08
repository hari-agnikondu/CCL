package com.incomm.cclpvms.fraud.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.model.prm;
import com.incomm.cclpvms.fraud.service.PrmService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Controller
@RequestMapping("/fraud")
public class PrmController {
	
	private static final Logger logger = LogManager.getLogger(PrmController.class);
	
	@Autowired
	public ProductService productService;
	
	@Autowired
	public PrmService prmservice;
	
	@PreAuthorize("hasRole('EDIT_PRM_ENABLE_DISABLE')")
	@RequestMapping("/prmConfig")
    public ModelAndView prmFileConfig(HttpServletRequest request,HttpServletResponse response) throws ServiceException {
        logger.debug(CCLPConstants.ENTER);
		
		ModelAndView mav = new ModelAndView();
		List<Object> txnDtls;		
		txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);
		mav.addObject("deliverChannelList",txnDtls.get(0));
		mav.addObject("txnERIFFlag",txnDtls.get(2));
		mav.addObject("txnMRIFFlag",txnDtls.get(3));
		mav.addObject("dlctxn",txnDtls.get(4));
		
	
		mav.setViewName("prmConfig");
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('EDIT_PRM_ENABLE_DISABLE')")
	@RequestMapping("/saveprm")
	public ModelAndView savePrm(@ModelAttribute("prmForm") prm prmForm ) throws ServiceException {
		ModelAndView mav = new ModelAndView("prmConfig");
		logger.debug(CCLPConstants.ENTER);
		logger.debug("prmForm "+prmForm.getPrmAttributes());
		ResponseDTO responseDto = null;
		if(prmForm.getPrmAttributes() == null) {
			 responseDto = prmservice.updateAllPrmConfig();
			
		}else {
			 responseDto = prmservice.updatePrmConfig(prmForm.getPrmAttributes());
		}
		if(responseDto.getResult()!=null && responseDto.getResult().equalsIgnoreCase("success")) {
			logger.info("Prm  record '{}' has been updated successfully");
			mav.addObject("statusFlag", "success");
			if (responseDto.getMessage() != null)
				mav.addObject("statusMessage", responseDto.getMessage());
			else
				mav.addObject("statusMessage",  ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {
			
			mav.addObject("statusFlag", "fail");
			if (responseDto.getMessage() != null)
				mav.addObject("statusMessage", responseDto.getMessage());
			else
				mav.addObject("statusMessage",   ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}
		List<Object> txnDtls;
		txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);
		mav.addObject("deliverChannelList",txnDtls.get(0));
		mav.addObject("txnERIFFlag",txnDtls.get(2));
		mav.addObject("txnMRIFFlag",txnDtls.get(3));
		mav.addObject("dlctxn",txnDtls.get(4));
		
		
				
		return mav;
	}
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("statusMessage", errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
}
