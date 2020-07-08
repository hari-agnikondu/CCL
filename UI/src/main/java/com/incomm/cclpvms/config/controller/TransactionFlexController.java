package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.TransactionFlex;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.service.TransactionFlexService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Controller
@RequestMapping("/config/TransactionFlex")
public class TransactionFlexController {
	
private static final Logger logger = LogManager.getLogger(TransactionFlexController.class);
	
	@Autowired
	public ProductService productService;
	
	@Autowired
	public TransactionFlexService transflexservice;
	
	@RequestMapping("/TransactionFlexConfig")
    public ModelAndView transactionFlexConfig(HttpServletRequest request,HttpServletResponse response) throws ServiceException {
        logger.debug(CCLPConstants.ENTER);
		
		ModelAndView mav = new ModelAndView();
		List<Object> txnDtls;		
		txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);
		mav.addObject("deliverChannelList",txnDtls.get(0));
		mav.addObject("dlctxn",txnDtls.get(4));
		mav.addObject("txndesc",txnDtls.get(5));
		
	
		mav.setViewName("TransactionFlexConfig");
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@RequestMapping("/savetransactionflex")
	public ModelAndView savetransactionflex(@ModelAttribute("transflexForm") TransactionFlex transflexForm ) throws ServiceException {
		ModelAndView mav = new ModelAndView("TransactionFlexConfig");
		logger.debug(CCLPConstants.ENTER);
		logger.debug("transflexForm "+transflexForm.getTxnDesc());
		ResponseDTO responseDto = null;
		
			 responseDto = transflexservice.updateTransFlexConfig(transflexForm.getTxnDesc());
		
		if(responseDto.getResult()!=null && responseDto.getResult().equalsIgnoreCase("success")) {
			logger.info("transflexForm  record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, "success");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,  ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {
			
			mav.addObject("statusFlag", "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,   ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}
		List<Object> txnDtls;
		txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);
		mav.addObject("deliverChannelList",txnDtls.get(0));
		mav.addObject("dlctxn",txnDtls.get(4));
		mav.addObject("txndesc",txnDtls.get(5));
		
		
				
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
		mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

}
