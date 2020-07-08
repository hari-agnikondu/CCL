package com.incomm.cclpvms.config.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.CardRange;
import com.incomm.cclpvms.config.model.CardRange.ValidateSearch;
import com.incomm.cclpvms.config.model.CardRangeDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.CardRangeService;
import com.incomm.cclpvms.config.validator.ValidationMethods;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;


/**
 * CardRange Controller handles all incoming requests.
 */

@Controller
@RequestMapping("/config/cardrange")
public class CardRangeController {
	Logger logger = LogManager.getLogger(CardRangeController.class);
	@Autowired
	CardRangeService cardRangeService;

	@Autowired
	ValidationMethods customValidator;
	
	@Autowired
	SessionService sessionService;

	/**
	 * This Methods Displays the Search page for entering Issuer Name/Prefix.
	 */
	@PreAuthorize("hasRole('SEARCH_CARDRANGE')")
	@RequestMapping("/cardRangeConfig")
	public ModelAndView cardRangeConfiguration() {
		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.EXIT);
		return new ModelAndView(CCLPConstants.CARD_RANGE_CONFIG,CCLPConstants.CARD_RANGE_SEARCH, new CardRange());
	}


	/**
	 * This Method Search for the Card Range based on IssuerName/Prefix Entered. 
	 * An empty search will return all the Available card Ranges.
	 */
	@PreAuthorize("hasRole('SEARCH_CARDRANGE')")
	@RequestMapping("/searchCardRange")
	public ModelAndView searchCardRange(@Validated({ValidateSearch.class}) @ModelAttribute(CCLPConstants.CARD_RANGE_SEARCH) CardRange cardRange,BindingResult bindingResult) throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();

		if(bindingResult.hasErrors())
		{
			mav.setViewName(CCLPConstants.CARD_RANGE_CONFIG);
			return mav;
		} 
		mav.setViewName(CCLPConstants.CARD_RANGE_CONFIG);
		mav.addObject("cardRangeList", cardRangeService.getCardRange(cardRange));
		mav.addObject(CCLPConstants.CARD_RANGE_SEARCH, cardRange);
		mav.addObject("showGrid", "true");
		mav.addObject("loginUserId", sessionService.getUserId());
		logger.debug(CCLPConstants.EXIT);
		return mav;

	}

	/**
	 * This Method is to display the Add Card Range Screen
	 */
	@PreAuthorize("hasRole('ADD_CARDRANGE')")
	@RequestMapping("/addCardRange")
	public ModelAndView addCardRangeConfiguration( @ModelAttribute(CCLPConstants.ADD_CARD_RANGE) CardRange cardRange) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		cardRange.setPrefix("");
		ModelAndView mav=new ModelAndView(CCLPConstants.ADD_CARD_RANGE);
		mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * After User Enters the details in the Addcardrange, this method will be invoked to save the details.
	 */
	@PreAuthorize("hasRole('ADD_CARDRANGE')")
	@RequestMapping(value = "/saveCardRange") 
	public ModelAndView saveCardRange(@Valid  @ModelAttribute(CCLPConstants.ADD_CARD_RANGE) CardRange cardRange, BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();

		if(bindingResult.hasErrors())
		{
			customValidator.chkCardRange(cardRange, bindingResult);
			mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
			mav.setViewName(CCLPConstants.ADD_CARD_RANGE);
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}
		else
		{  
			customValidator.chkCardRange(cardRange, bindingResult);
			if(bindingResult.hasErrors())
			{
				mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
				mav.setViewName(CCLPConstants.ADD_CARD_RANGE);
				logger.debug(CCLPConstants.EXIT);
				return mav;
			}   
			ResponseDTO responseDTO=cardRangeService.addCardRange(cardRange);
			if(ResponseMessages.SUCCESS.equals(responseDTO.getCode()))
			{
				mav.setViewName("forward:/config/cardrange/cardRangeConfig");
				mav.addObject(CCLPConstants.STATUS,responseDTO.getMessage());
				
			}
			else
			{
				mav.addObject(CCLPConstants.ADD_CARD_RANGE,cardRange);
				mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
				mav.setViewName(CCLPConstants.ADD_CARD_RANGE);
			}
			mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
		}
		logger.debug(CCLPConstants.EXIT);
		return mav; 

	}


	/**
	 * This method is to display the edit Card Range screen to the user.
	 */
	@PreAuthorize("hasRole('EDIT_CARDRANGE')")
	@RequestMapping("/editCardRange")
	public ModelAndView editCardRangeConfiguration(HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=null;
		CardRange cardRange=cardRangeService.getCardRangeById(req.getParameter("cardRangeID"));
		if(cardRange!=null)
		{
			mav=new ModelAndView(CCLPConstants.EDIT_CARD_RANGE,CCLPConstants.EDIT_CARD_RANGE,cardRange);
			mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
		}
		else
		{
			logger.debug(CCLPConstants.EXIT);
			return new ModelAndView(CCLPConstants.CARD_RANGE_CONFIG,CCLPConstants.CARD_RANGE_SEARCH, new CardRange());	
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * This Methods Updates the Cardrange details.
	 */
	@PreAuthorize("hasRole('EDIT_CARDRANGE')")
	@RequestMapping(value = "/updateCardRange") 
	public ModelAndView editCardRange( @Valid @ModelAttribute(CCLPConstants.EDIT_CARD_RANGE) CardRange cardRange, BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();

		if(bindingResult.hasErrors())
		{
			logger.debug("Inside Binding Result Error");
			customValidator.chkCardRange(cardRange, bindingResult);
			mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
			mav.setViewName(CCLPConstants.EDIT_CARD_RANGE);	
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}
		else{ 

			customValidator.chkCardRange(cardRange, bindingResult);	
			if(bindingResult.hasErrors())
			{
				mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
				mav.setViewName(CCLPConstants.EDIT_CARD_RANGE);	
				logger.debug(CCLPConstants.EXIT);
				return mav;
			}  

			ResponseEntity<ResponseDTO> responseDTO=cardRangeService.updateCardRange(cardRange);
			if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
				mav.setViewName("forward:/config/cardrange/cardRangeConfig");
				mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage()); 
			}
			else
			{
				mav.addObject(CCLPConstants.EDIT_CARD_RANGE,cardRange);
				mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
				mav.setViewName(CCLPConstants.EDIT_CARD_RANGE);
			}
			mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
		} 
		logger.debug(CCLPConstants.EXIT);
		return mav; 

	}



	/**
	 * This Method calls when Delete operation performs for the cardrange from search page.
	 */
	@PreAuthorize("hasRole('DELETE_CARDRANGE')")
	@RequestMapping("/deleteCardRange")
	public ModelAndView deleteCardRange(@ModelAttribute(CCLPConstants.CARD_RANGE_SEARCH) CardRange cardRange,HttpServletRequest req)throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		String issuerName = "";
		
		ModelAndView mav=new ModelAndView();
		if(null!=req.getParameter(CCLPConstants.ISSUERS_NAME))
			{
			issuerName = req.getParameter(CCLPConstants.ISSUERS_NAME);
			}
		ResponseEntity<ResponseDTO> responseDTO=cardRangeService.deleteCardRange(cardRange.getCardRangeId(),issuerName);
		mav.setViewName("forward:/config/cardrange/searchCardRange");
		if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
			mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage());
		}
		else{
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * This Method calls when user tries to approve/Reject the card range from the search page.
	 */
	@PreAuthorize("hasRole('APPROVE_CARDRANGE')")
	@RequestMapping("/approveCardRangeRequest")
	public ModelAndView changeCardRangeStatus(@ModelAttribute(CCLPConstants.CARD_RANGE_SEARCH) CardRange cardRange,HttpServletRequest req)  throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		
		if(null!=req.getParameter(CCLPConstants.ISSUERS_NAME))
		{
		cardRange.setIssuerName(req.getParameter(CCLPConstants.ISSUERS_NAME));
		}
		
		ModelAndView mav=new ModelAndView();

		ResponseEntity<ResponseDTO> responseDTO=cardRangeService.changeCardRangeStatus(cardRange);
		if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
			mav.setViewName("forward:/config/cardrange/searchCardRange");
			mav.addObject(CCLPConstants.CARD_RANGE_SEARCH, new CardRange());
			mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage()); 
		}
		else{
			mav.addObject(CCLPConstants.CARD_RANGE_SEARCH,new CardRange());
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
			mav.setViewName(CCLPConstants.CARD_RANGE_CONFIG);
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	
	
	/**
	 * This method is to display the view Card Range screen to the user.
	 */
	@PreAuthorize("hasRole('VIEW_CARDRANGE')")
	@RequestMapping("/viewCardRange")
	public ModelAndView viewCardRangeConfiguration(HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=null;
		CardRange cardRange=cardRangeService.getCardRangeById(req.getParameter("cardRangeID"));
		if(cardRange!=null)
		{
			mav=new ModelAndView(CCLPConstants.VIEW_CARD_RANGE,CCLPConstants.VIEW_CARD_RANGE,cardRange);
			mav.addObject(CCLPConstants.ISSUER_LIST,cardRangeService.getAllIssuers());
		}
		else
		{
			logger.debug(CCLPConstants.EXIT);
			return new ModelAndView(CCLPConstants.CARD_RANGE_CONFIG,CCLPConstants.CARD_RANGE_SEARCH, new CardRange());	
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {

		logger.info("exceptionHandler Method Starts Here");
		String errMessage = ResponseMessages.SERVER_DOWN;
		ModelAndView mav = new ModelAndView();
		if (exception instanceof ServiceException)
			errMessage = exception.getMessage();

		mav.setViewName("serviceError");
		mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);

		logger.info("exceptionHandler Method Ends Here");
		return mav;
	}
	
	/**added by nawaz for getting card range details based on issuer id*/
	@RequestMapping(value = "/getCardRangeDetails/{issuerId}", method = RequestMethod.GET)
	public String getCardRangeDetails(@PathVariable("issuerId") Long issuerId)  throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<CardRangeDTO> cardRange=cardRangeService.getCardRangeByIssuerId(issuerId);
		
		logger.debug(CCLPConstants.EXIT);
		return cardRange.toString();
	}

}

