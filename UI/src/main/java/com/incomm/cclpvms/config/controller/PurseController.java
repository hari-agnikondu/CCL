package com.incomm.cclpvms.config.controller;




import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.Purse;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.PurseService;
import com.incomm.cclpvms.config.validator.ValidationPurseImpl;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;


/**
 * Purse Controller handles all incoming requests.
 */

@Controller
@RequestMapping("/config/purse")
public class PurseController {
	Logger logger = LogManager.getLogger(PurseController.class);
	
	private static final String PURSE_CONFIG = "purseConfig";
	private static final String PURSE_SEARCH = "purseSearch";
	private static final String EDIT_PURSE = "editPurse";
	private static final String CURRENCY_CODE_LIST = "currencyCodeList";
	private static final String PURSE_TYPE_LIST = "purseTypeList";
	private static final String ADD_PURSE = "addPurse";
	private static final String VIEW_PURSE = "viewPurse";
	
	
	@Autowired
	PurseService purseService;

	@Autowired
	ValidationPurseImpl validationPurse;
	
	/*
	 * This Methods Displays the Search page for entering Currency and UPC code
	 */
	@PreAuthorize("hasRole('SEARCH_PURSE')")
	@RequestMapping("/purseConfig")
	public ModelAndView purseConfig() {
		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.EXIT);
		return new ModelAndView(PURSE_CONFIG,PURSE_SEARCH, new Purse());
	}

	/*
	 * This Method Search for the purse based on currency and UPC code Entered. 
	 * An empty search will return all the Available Purses.
	 */
	@PreAuthorize("hasRole('SEARCH_PURSE')")
	@RequestMapping("/searchPurse")
	public ModelAndView searchPurse( @ModelAttribute(PURSE_SEARCH) Purse purse) throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
		mav.setViewName(PURSE_CONFIG);
		mav.addObject("purseList", purseService.getPurses(purse));
		mav.addObject(PURSE_SEARCH, purse);
		mav.addObject("showGrid", "true");
		logger.debug(CCLPConstants.EXIT);
		return mav;

	}

	/**
	 * This method is to display the edit purse screen.
	 */
	@PreAuthorize("hasRole('EDIT_PURSE')")
	@RequestMapping("/editPurse")
	public ModelAndView editPurseConfiguration( @ModelAttribute(PURSE_SEARCH) Purse purse) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=null;
		Purse purseModel=purseService.getPurseById(purse.getPurseId());

		if(purseModel!=null)
		{
			mav=new ModelAndView(EDIT_PURSE,EDIT_PURSE,purseModel);
			mav.addObject(PURSE_TYPE_LIST,purseService.getPurseTypeList());
			mav.addObject(CURRENCY_CODE_LIST,purseService.getCurrencyCodeList());
		}
		else
		{
			mav= new ModelAndView(PURSE_CONFIG,PURSE_SEARCH, new Purse());
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * This method is to display the add purse screen.
	 */
	@PreAuthorize("hasRole('ADD_PURSE')")
	@RequestMapping("/addPurse")
	public ModelAndView addPurseConfiguration( @ModelAttribute(ADD_PURSE) Purse purse) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView(ADD_PURSE);
		mav.addObject(PURSE_TYPE_LIST,purseService.getPurseTypeList());
		mav.addObject(CURRENCY_CODE_LIST,purseService.getCurrencyCodeList());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * This method is to update the purse.
	 */
	@PreAuthorize("hasRole('EDIT_PURSE')")
	@RequestMapping("/updatePurse")
	public ModelAndView updatePurseConfig( @ModelAttribute(EDIT_PURSE) Purse purse) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView(EDIT_PURSE);
				
		ResponseEntity<ResponseDTO> responseDTO=purseService.updatePurseDetails(purse);
		if(responseDTO!=null && responseDTO.getBody()!=null){
			if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
				mav.setViewName("forward:/config/purse/purseConfig");
				mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage()); 
			}
			else
			{
				Purse purseModel=purseService.getPurseById(purse.getPurseId());
				mav.addObject(EDIT_PURSE,purseModel);
				mav.addObject(PURSE_TYPE_LIST,purseService.getPurseTypeList());
				mav.addObject(CURRENCY_CODE_LIST,purseService.getCurrencyCodeList());
				mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
			}	
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * This method is to insert the purse.
	 */
	@PreAuthorize("hasRole('ADD_PURSE')")
	@RequestMapping("/savePurse")
	public ModelAndView savePurseConfig( @ModelAttribute(ADD_PURSE) Purse purse,BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView(ADD_PURSE);
		
		validationPurse.validate(purse, bindingResult);
		if(bindingResult.hasErrors())
		{
			mav.addObject(PURSE_TYPE_LIST,purseService.getPurseTypeList());
			mav.addObject(CURRENCY_CODE_LIST,purseService.getCurrencyCodeList());
			mav.addObject(ADD_PURSE,purse);
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}

		ResponseEntity<ResponseDTO> responseDTO=purseService.savePurseDetails(purse);
		if(responseDTO!=null && responseDTO.getBody()!=null){
			if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
				mav.setViewName("forward:/config/purse/purseConfig");
				mav.addObject("status",responseDTO.getBody().getMessage()); 
			}
			else
			{
				mav.addObject(PURSE_TYPE_LIST,purseService.getPurseTypeList());
				mav.addObject(CURRENCY_CODE_LIST,purseService.getCurrencyCodeList());
				mav.addObject(ADD_PURSE,purse);
				mav.addObject("statusMessage",responseDTO.getBody().getMessage());
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * This method is to delete the purse.
	 */
	@PreAuthorize("hasRole('DELETE_PURSE')")
	@RequestMapping("/deletePurse")
	public ModelAndView deletePurseConfig( @ModelAttribute("deletePurse") Purse purse) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();

		ResponseEntity<ResponseDTO> responseDTO=purseService.deletePurseDetails(purse);
		mav.setViewName("forward:/config/purse/searchPurse");
		if(responseDTO!=null && responseDTO.getBody()!=null){
			if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
				mav.addObject("status",responseDTO.getBody().getMessage()); 
			}
			else
			{
				mav.addObject("statusMessage",responseDTO.getBody().getMessage());
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	/**
	 * View Purse
	 * @param purse
	 * @return
	 * @throws ServiceException
	 */
	
	@PreAuthorize("hasRole('VIEW_PURSE')")
	@RequestMapping("/viewPurse")
	public ModelAndView viewPurseConfiguration( @ModelAttribute(PURSE_SEARCH) Purse purse) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=null;
		Purse purseModel=purseService.getPurseById(purse.getPurseId());

		if(purseModel!=null)
		{
			mav=new ModelAndView(VIEW_PURSE,VIEW_PURSE,purseModel);
			mav.addObject(PURSE_TYPE_LIST,purseService.getPurseTypeList());
			mav.addObject(CURRENCY_CODE_LIST,purseService.getCurrencyCodeList());
		}
		else
		{
			mav= new ModelAndView(PURSE_CONFIG,PURSE_SEARCH, new Purse());
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
}

