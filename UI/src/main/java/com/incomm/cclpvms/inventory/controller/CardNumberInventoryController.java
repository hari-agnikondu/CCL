package com.incomm.cclpvms.inventory.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.inventory.model.CardNumberInventory;
import com.incomm.cclpvms.inventory.service.CardNumberInventoryService;


@Controller
@RequestMapping("/cardNumber")
public class CardNumberInventoryController {
	Logger logger = LogManager.getLogger(CardNumberInventoryController.class);
	
	@Autowired
	public CardNumberInventoryService cardNmbrInvtryService;
	
	public static final String CARD_RANGE_ID="cardRangeId";
	public static final String FORWARD_URL="forward:cardNumberInventory";
	public static final String PAUSE_EXCEPTION="Exception occured in pauseCardInventory method.";
	
	
	@RequestMapping("/cardNumberInventory")
	public ModelAndView cardNumberInventory() throws ServiceException {
		
		logger.info("Enter CardNumberInventory");
		ModelAndView mav=new ModelAndView("cardnumberinventorygeneration");
		List<CardNumberInventory> cardInventoryDtls=new ArrayList<>();
		ResponseDTO responseDTO=null;
		
		responseDTO=cardNmbrInvtryService.getAllCardInvntryDtls();
		
		if(responseDTO!=null) {
			
			if (responseDTO.getData() != null && responseDTO.getResult().equalsIgnoreCase("SUCCESS")) {
				
				logger.debug("Card Inventory list fetched successfully. Response from config service is " + responseDTO);
				ObjectMapper objectMapper=new ObjectMapper();
				@SuppressWarnings("unchecked")
				List<Object> cardInvtryListDtos=(List<Object>) responseDTO.getData();
				if(cardInvtryListDtos!=null) {
					Iterator<Object> itr=cardInvtryListDtos.iterator();
					while(itr.hasNext())
						cardInventoryDtls.add(objectMapper.convertValue(itr.next(),CardNumberInventory.class));
				}
				
				mav.addObject("cardInventoryList", cardInventoryDtls);
			}
		}
		
		logger.info("Exit CardNumberInventory");
		return mav;
	}
	
	
	@RequestMapping("/generateCardInventory")
	public ModelAndView generateCardInventory(Map<String, Object> model, HttpServletRequest request)throws ServiceException{
		
		logger.info(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
		long cardRangeId=0;
		ResponseDTO responseDTO=null;
		String message="";
		
		try {
		
			logger.info("request..."+request.getParameter(CARD_RANGE_ID));
			cardRangeId=Long.parseLong(request.getParameter(CARD_RANGE_ID));
			responseDTO=cardNmbrInvtryService.generateCardInventory(cardRangeId);
			String forward=FORWARD_URL;
			
			if (responseDTO.getCode().equalsIgnoreCase("000")) { 
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.SUCCESS_STATUS, message);
				mav.setViewName(forward);
			}
			else {
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.FAIL_STATUS, message);
				mav.setViewName(forward);
			}
		}
		catch(org.springframework.web.client.ResourceAccessException e) {
			logger.debug(PAUSE_EXCEPTION+e.getMessage());
		}
		catch(Exception e) {
			logger.debug("Exception occured in generateCardInventory method.."+e.getMessage());
			
		}
		finally {
			logger.info(CCLPConstants.EXIT);
		}
	 return mav;
	}

	@RequestMapping("/pauseCardInventory")
	public ModelAndView pauseCardInventory(Map<String, Object> model, HttpServletRequest request) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
		long cardRangeId=0;
		ResponseDTO responseDTO=null;
		String message="";
		String forward=FORWARD_URL;
		try {
			cardRangeId=Long.parseLong(request.getParameter(CARD_RANGE_ID));
			responseDTO=cardNmbrInvtryService.pauseCardInventory(cardRangeId);

			if (responseDTO.getCode().equalsIgnoreCase("000")) { 
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.SUCCESS_STATUS, message);
				mav.setViewName(forward);
			}
			else {
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.FAIL_STATUS, message);
				mav.setViewName(forward);
			}
		}
	
		catch(Exception e) {
			logger.debug(PAUSE_EXCEPTION+e.getMessage());
		}
		finally {
		logger.info(CCLPConstants.EXIT);
		}
		return mav;
	}
	
	
	@RequestMapping("/resumeCardInventory")
	public ModelAndView resumeCardInventory(Map<String, Object> model, HttpServletRequest request) throws ServiceException{
		
		logger.info(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
		long cardRangeId=0;
		ResponseDTO responseDTO=null;
		String message="";
		String forward=FORWARD_URL;
		
		try {
			cardRangeId=Long.parseLong(request.getParameter(CARD_RANGE_ID));
			responseDTO=cardNmbrInvtryService.resumeCardInventory(cardRangeId);

			if (responseDTO.getCode().equalsIgnoreCase("000")) { 
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.SUCCESS_STATUS, message);
				mav.setViewName(forward);
			}
			else {
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.FAIL_STATUS, message);
				mav.setViewName(forward);
			}
		}
		
		catch(Exception e) {
			logger.debug(PAUSE_EXCEPTION+e.getMessage());
		}
		return mav;
	}
	
	
	@RequestMapping("/regenerateCardInventory")
	public ModelAndView regenerateCardInventory (Map<String, Object> model, HttpServletRequest request) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
		long cardRangeId=0;
		ResponseDTO responseDTO=null;
		String message="";
		String forward=FORWARD_URL;
		
		try {
			cardRangeId=Long.parseLong(request.getParameter(CARD_RANGE_ID));
			responseDTO=cardNmbrInvtryService.regenerateCardInventory(cardRangeId);

			if (responseDTO.getCode().equalsIgnoreCase("000")) { 
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.SUCCESS_STATUS, message);
				mav.setViewName(forward);
			}
			else {
				
				message=responseDTO.getMessage();
				mav.addObject(CCLPConstants.FAIL_STATUS, message);
				mav.setViewName(forward);
			}
		}
		catch(org.springframework.web.client.ResourceAccessException e) {
			logger.error(PAUSE_EXCEPTION+e.getMessage());
		
		}
		catch(Exception e) {
			logger.debug(PAUSE_EXCEPTION+e.getMessage());
			
		}
		finally {
		logger.info(CCLPConstants.EXIT);
		}
		return mav;
	}
	
}
