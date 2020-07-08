package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.FulFillment;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.FulFillmentService;
import com.incomm.cclpvms.config.service.GlobalParameterService;
import com.incomm.cclpvms.config.validator.ValidateFulfillment;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

/**
 * Fulfillment Controller handles all incoming requests.
 */

@Controller
@RequestMapping("/config/fulfillment")
public class FulfillmentController {

	private static final Logger logger = LogManager.getLogger(FulfillmentController.class);

	@Autowired
	public FulFillmentService fulFillmentService;

	@Value("${INS_USER}")
	public long insUser;

	@Autowired
	public ValidateFulfillment validateFulfillment;
	
	@Autowired
	public GlobalParameterService globalParameterService;
	/**
	 * This Methods Displays the Search page for entering Fulfillment
	 * Name/Prefix.
	 */
	@PreAuthorize("hasRole('SEARCH_FULFILLMENT_VENDOR')")
	@RequestMapping("/fulFillmentConfig")
	public ModelAndView fulFillmentConfiguration() {
		logger.info(" ENTER fulFillmentConfiguration ");
		ModelAndView mav = new ModelAndView();
		FulFillment fullfillment = new FulFillment();
		mav.addObject(CCLPConstants.FULFILLMENT_FORM, fullfillment);
		mav.addObject(CCLPConstants.DELETE_BOX, fullfillment);
		mav.setViewName(CCLPConstants.FULFILLMENT_CONFIG);
		logger.info(" EXIT fulFillmentConfiguration ");
		return mav;
	}

	/**
	 * An empty search will return all the Available FulFillment Names.
	 */
	@PreAuthorize("hasRole('SEARCH_FULFILLMENT_VENDOR')")
	@RequestMapping("/showAllFulFillments")
	public ModelAndView showAllFulFillments(Map<String, Object> model,
			@ModelAttribute("fulFillmentForm") FulFillment fulfillmentForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException {
		logger.info("****showAllFulFillments Method ********");
		ModelAndView mav = new ModelAndView();
		model.put("fulFillmentTblList", fulFillmentService.getAllFulFillment());
		mav.addObject(CCLPConstants.FULFILLMENT_FORM, fulfillmentForm);
		mav.addObject(CCLPConstants.DELETE_BOX, new FulFillment());
		mav.addObject("showGrid", "true");
		mav.setViewName(CCLPConstants.FULFILLMENT_CONFIG);
		logger.info(" EXIT showAllFulFillments ");
		return mav;
	}

	/**
	 * This Method Search records based on FulFillment Name/Prefix
	 * 
	 */
	@PreAuthorize("hasRole('SEARCH_FULFILLMENT_VENDOR')")
	@RequestMapping("/searchFulFillmentByName")
	public ModelAndView searchFulFillmentByName(Map<String, Object> model,
			@Validated(FulFillment.ValidationStepOne.class) @ModelAttribute("fulFillmentForm") FulFillment fulfillmentForm,
			BindingResult bindingresult, HttpServletRequest request) throws ServiceException {
		logger.info("****searchFulFillmentByName Method ********");
		ModelAndView mav = new ModelAndView();
		String fulFillmentName = "";
		String searchType = "";

		searchType = request.getParameter("searchType");
		fulFillmentName = fulfillmentForm.getFulFillmentName();
		logger.debug("FulFillment Name: " + fulFillmentName);

		model.put("fulFillmentTblList", fulFillmentService.getAllFulFillmentByName(fulFillmentName));
		mav.addObject("SearchType", searchType);
		mav.addObject(CCLPConstants.FULFILLMENT_FORM, fulfillmentForm);
		mav.addObject(CCLPConstants.DELETE_BOX, new FulFillment());
		mav.addObject("showGrid", "true");
		mav.setViewName(CCLPConstants.FULFILLMENT_CONFIG);
		logger.info(" EXIT searchFulFillmentByName ");
		return mav;
	}

	/**
	 * This Method is to display the Add FulFillment Screen
	 */
	@PreAuthorize("hasRole('ADD_FULFILLMENT_VENDOR')")
	@RequestMapping("/showAddFullfilment")
	public ModelAndView showAddFullfilment(Map<String, Object> model) throws ServiceException {
		logger.info("****showAddFullfilment Method ********");
		ModelAndView mav = new ModelAndView();
		mav.addObject(CCLPConstants.ISAUTOMATIC_SHIPPED_LIST, fulFillmentService.getShipmentAttList());
		FulFillment fulfillment =  new FulFillment();
		Map<String, Object> globalParamData = globalParameterService.getGlobalParameters();
		Optional<Object> srcPlatform =globalParamData.entrySet().stream().filter(keyFilt->keyFilt.getKey().equalsIgnoreCase("srcPlatform")).map(Map.Entry::getValue).findFirst();
		Optional<Object> ccfFileName=globalParamData.entrySet().stream().filter(keyFilt->keyFilt.getKey().equalsIgnoreCase("ccfFileName")).map(Map.Entry::getValue).findFirst();
		Optional<Object> replCcfFileName=globalParamData.entrySet().stream().filter(keyFilt->keyFilt.getKey().equalsIgnoreCase("replCcfFileName")).map(Map.Entry::getValue).findFirst();
		fulfillment.setCcfFileFormat(ccfFileName.isPresent()?String.valueOf(ccfFileName.get()):"");
		fulfillment.setReplaceCcfFileFormat(replCcfFileName.isPresent()?String.valueOf(replCcfFileName.get()):"");
		mav.addObject("srcPlatform", srcPlatform.isPresent()?String.valueOf(srcPlatform.get()):"");

		mav.addObject(CCLPConstants.FULFILLMENT_FORM,fulfillment);
		mav.setViewName(CCLPConstants.ADD_FULLFILMENT);
		logger.info(" EXIT showAddFullfilment ");
		return mav;
	}

	/**
	 * This Method is used to add the Fulfillment vendor details entered in the
	 * UI.
	 */
	@PreAuthorize("hasRole('ADD_FULFILLMENT_VENDOR')")
	@RequestMapping(value = "/addFulfillment", method = RequestMethod.POST)
	public ModelAndView addFulfillment(
			@Validated({ FulFillment.ValidationStepOne.class,
					FulFillment.ValidationStepTwo.class }) @ModelAttribute("fulFillmentForm") FulFillment fulfillment,
			BindingResult result) throws ServiceException {
		logger.info("****addFulfillment Method ********");
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDTO = null;
		String message = "";

		validateFulfillment.validate(fulfillment, result);
		
		if (result.hasErrors()) {
			mav.addObject(CCLPConstants.ISAUTOMATIC_SHIPPED_LIST, fulFillmentService.getShipmentAttList());
			mav.setViewName(CCLPConstants.ADD_FULLFILMENT);
			return mav;
		}
		logger.debug("===fulfillment.getB2bVendorConfReq()"+fulfillment.getB2bVendorConfReq()+" "+fulfillment.getB2bCnFileIdentifier());
	
		responseDTO = fulFillmentService.createFulfillment(fulfillment);
		logger.debug("RESPONSE CODE: " + responseDTO.getCode() + " Error Message : " + responseDTO.getMessage());
		message = responseDTO.getMessage();
		if (ResponseMessages.SUCCESS.equals(responseDTO.getCode().trim())) {
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			mav.addObject(CCLPConstants.STATUS_MESSAGE, message);
			mav.setViewName(CCLPConstants.FULFILLMENT_CONFIG);
			mav.addObject(CCLPConstants.FULFILLMENT_FORM, new FulFillment());
			mav.addObject(CCLPConstants.DELETE_BOX, new FulFillment());
		} else {
			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			mav.addObject(CCLPConstants.STATUS_MESSAGE, message);
			mav.addObject(CCLPConstants.DELETE_BOX, new FulFillment());
			mav.addObject(CCLPConstants.ISAUTOMATIC_SHIPPED_LIST, fulFillmentService.getShipmentAttList());
			mav.setViewName(CCLPConstants.ADD_FULLFILMENT);
			mav.addObject(CCLPConstants.FULFILLMENT_FORM, fulfillment);
		}
		logger.info("**** EXIT addFulfillment ********");
		return mav;
	}

	/**
	 * This method is to display the edit Fulfillment screen to the user. 
	 */
	@PreAuthorize("hasRole('EDIT_FULFILLMENT_VENDOR')")
	@RequestMapping("/showEditFulfillment")
	public ModelAndView showEditFulfillment(HttpServletRequest request) throws ServiceException {
		logger.info("****showEditFulfillment Method ********");
		long fulfillmentSEQID = 0;
		FulFillment fulFillment = null;
		ModelAndView mav = new ModelAndView();
		try {
			fulfillmentSEQID = Long.parseLong(request.getParameter(CCLPConstants.FULFILLMENT_SEQ_ID).trim());
			mav.setViewName(CCLPConstants.EDIT_FULFILLMENT);
			logger.debug("fulfillmentID:: " + fulfillmentSEQID);
			fulFillment = fulFillmentService.getFulfillmentById(fulfillmentSEQID);
			logger.info("****fulFillment OBJ ********" + fulFillment.getB2bCnFileIdentifier());
			mav.addObject(CCLPConstants.ISAUTOMATIC_SHIPPED_LIST, fulFillmentService.getShipmentAttList());
			mav.addObject(CCLPConstants.FULFILLMENT_FORM, fulFillment);
			mav.addObject("editFulfillmentSQID", fulfillmentSEQID);

		} catch (NumberFormatException e) {
			mav.setViewName(CCLPConstants.EDIT_FULFILLMENT);
			mav.addObject(CCLPConstants.FULFILLMENT_FORM, fulFillment);
			logger.error("NumberFormatException Occured While Editing in showEditFulfillment()" + e);
		}
		logger.info("****EXIT showEditFulfillment Method ********");
		return mav;
	}

	/**
	 * This Methods Updates the FulFillment Vendor details.
	 */
	@PreAuthorize("hasRole('EDIT_FULFILLMENT_VENDOR')")
	@RequestMapping("/updateFulfillment")
	public ModelAndView updateFulfillment(
			@Validated({ FulFillment.ValidationStepOne.class,
				FulFillment.ValidationStepTwo.class }) @ModelAttribute("fulFillmentForm") FulFillment fulfillment, BindingResult bindingResult,HttpServletRequest request)
			throws ServiceException {
		ModelAndView mav = new ModelAndView();
			logger.debug("ENTER updateFulfillment updateFulfillment");
			long fulfillmentSQID = 0;
			String respMsg = "";
			ResponseDTO responseDTO ;
			logger.debug("===fulfillment.getB2bVendorConfReq()"+fulfillment.getB2bVendorConfReq()+" "+fulfillment.getB2bCnFileIdentifier());
			
			validateFulfillment.validate(fulfillment, bindingResult);
			
			if (bindingResult.hasErrors()) {
				mav.addObject(CCLPConstants.ISAUTOMATIC_SHIPPED_LIST, fulFillmentService.getShipmentAttList());
				mav.addObject(CCLPConstants.EDIT_FULFILLMENT_ID, fulfillmentSQID);
				mav.setViewName(CCLPConstants.EDIT_FULFILLMENT);
				return mav;
			}

			
			if ("disabled".equalsIgnoreCase(fulfillment.getB2bVendorConfReq())) {
				fulfillment.setB2bCnFileIdentifier("");//set as blank if we selected as disabled
			}
				

			if (request.getParameter(CCLPConstants.EDIT_FULFILLMENT_ID) != null
					&& !request.getParameter(CCLPConstants.EDIT_FULFILLMENT_ID).equalsIgnoreCase("0")) {
				fulfillmentSQID = Long.parseLong(request.getParameter(CCLPConstants.EDIT_FULFILLMENT_ID));
			}

			mav.addObject(CCLPConstants.EDIT_FULFILLMENT_ID, fulfillmentSQID);
			responseDTO = fulFillmentService.updateFulfillment(fulfillment);

			logger.debug("RESPONSE CODE: " + responseDTO.getCode() + " Error Message : " + responseDTO.getMessage());
			respMsg = responseDTO.getMessage();
			if (ResponseMessages.SUCCESS.equals(responseDTO.getCode())) {
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, respMsg);
				mav.setViewName(CCLPConstants.FULFILLMENT_CONFIG);
			} else {
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, respMsg);
				mav.setViewName(CCLPConstants.EDIT_FULFILLMENT);
			}
			mav.addObject(CCLPConstants.FULFILLMENT_FORM, new FulFillment());
			mav.addObject(CCLPConstants.DELETE_BOX, new FulFillment());
		
		return mav;
	}

	/**
	 * This Method calls when Delete operation performs for the FulFillment from 
	 * search page.
	 */
	@PreAuthorize("hasRole('DELETE_FULFILLMENT_VENDOR')")
	@RequestMapping("/deleteFulfillment")
	public ModelAndView deleteFulfillment(HttpServletRequest request) throws ServiceException {
		logger.info("****deleteFulfillment Method ********");
		ResponseDTO responseDTO ;
		ModelAndView mav = new ModelAndView();
		String respMessage = "";

		logger.info("fulfillment SEQID: " + request.getParameter(CCLPConstants.FULFILLMENT_SEQ_ID));
		logger.info("fulfillment ID: " + request.getParameter("fulfillmentID"));

		responseDTO = fulFillmentService.deleteFulfillment(
								Long.valueOf(request.getParameter(CCLPConstants.FULFILLMENT_SEQ_ID)),
								request.getParameter("fulfillmentID"));
		mav.setViewName("forward:/config/fulfillment/showAllFulFillments");

		respMessage = responseDTO.getMessage();
		logger.info("respMsg" + respMessage);
		if (ResponseMessages.SUCCESS.equals(responseDTO.getCode())) {
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			mav.addObject(CCLPConstants.STATUS_MESSAGE, respMessage);
			 }
		else {
			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			mav.addObject(CCLPConstants.STATUS_MESSAGE, respMessage);	 
			mav.addObject(CCLPConstants.DELETE_BOX, new FulFillment());
		}
		logger.debug("EXIT");
		return mav;

	}
	
	/**
	 * This method is to display the view Fulfillment screen to the user. 
	 */
	@PreAuthorize("hasRole('VIEW_FULFILLMENT_VENDOR')")
	@RequestMapping("/showViewFulfillment")
	public ModelAndView showViewFulfillment(HttpServletRequest request) throws ServiceException {
		logger.info("****showViewFulfillment Method ********");
		long fulfillmentSEQID = 0;
		FulFillment fulFillment = null;
		ModelAndView mav = new ModelAndView();
		try {
			fulfillmentSEQID = Long.parseLong(request.getParameter(CCLPConstants.FULFILLMENT_SEQ_ID).trim());
			mav.setViewName(CCLPConstants.VIEW_FULFILLMENT);
			logger.debug("fulfillmentID:: " + fulfillmentSEQID);
			fulFillment = fulFillmentService.getFulfillmentById(fulfillmentSEQID);
			logger.info("****fulFillment OBJ ********" + fulFillment.getB2bCnFileIdentifier());
			mav.addObject(CCLPConstants.ISAUTOMATIC_SHIPPED_LIST, fulFillmentService.getShipmentAttList());
			mav.addObject(CCLPConstants.FULFILLMENT_FORM, fulFillment);
			mav.addObject("viewFulfillmentSQID", fulfillmentSEQID);

		} catch (NumberFormatException e) {
			mav.setViewName(CCLPConstants.VIEW_FULFILLMENT);
			mav.addObject(CCLPConstants.FULFILLMENT_FORM, fulFillment);
			logger.error("Exception Occured While Viewing in showViewFulfillment()" + e);
		}
		logger.info("****EXIT showViewFulfillment Method ********");
		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug("ENTER");
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
		mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);
		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.error(exception.getMessage());
		logger.debug("EXIT");

		return mav;
	}
}
