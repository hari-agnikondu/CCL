package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.Partner;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.searchPartner;
import com.incomm.cclpvms.config.service.PartnerService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/partner")
public class PartnerController {

	// the logger
	private static final Logger logger = LogManager.getLogger(PartnerController.class);

	@Autowired
	public PartnerService partnerservice;

	@Value("${INS_USER}")
	long userId;

	@PreAuthorize("hasRole('SEARCH_PARTNER')")
	@RequestMapping("/partnerConfig")
	public ModelAndView partnerConfig() {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView(CCLPConstants.PARTNER_CONFIG);
		mav.addObject(CCLPConstants.PARTNER_FORM, new Partner());

		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_PARTNER')")
	@RequestMapping("/searchPartnerByName")
	public ModelAndView searchPartnerByName(
			@Validated(searchPartner.class) @ModelAttribute("partnerForm") Partner partner, BindingResult bindingResult,
			HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		List<Partner> partnerList = null;

		ModelAndView mav = new ModelAndView(CCLPConstants.PARTNER_CONFIG);

		if (!Util.isEmpty(partner.getPartnerName())) {

			if (bindingResult.hasErrors()) {
				mav.addObject(CCLPConstants.PARTNER_FORM, partner);
				logger.error("Error occured while binding the partner object");
				return mav;
			}
			logger.info("Searching for partnerName '{}'", partner.getPartnerName());
			partnerList = partnerservice.getPartnersByName(partner.getPartnerName());

		} else {
			logger.info("There is no partner name given, performing full search");
			partnerList = partnerservice.getAllPartners();
		}
		mav.addObject("showGrid", "true");
		mav.addObject("partnerList", partnerList);

		if (request.getParameter(CCLPConstants.STATUS_MESSAGE) != null) {
			logger.info("Search completed and statusFlag has set to {}",
					request.getParameter(CCLPConstants.STATUS_FLAG));

			mav.addObject(CCLPConstants.STATUS_FLAG, request.getParameter(CCLPConstants.STATUS_FLAG));
			mav.addObject(CCLPConstants.STATUS_MESSAGE, request.getParameter(CCLPConstants.STATUS_MESSAGE));
		}

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('ADD_PARTNER')")
	@RequestMapping("/showAddPartner")
	public ModelAndView showAddPartner() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView("showAddPartner");
		mav.addObject(CCLPConstants.PARTNER_FORM, new Partner());

		List<CurrencyCodeDTO> currencyCodes = partnerservice.getCurrencyCodes();
		mav.addObject(CCLPConstants.CURRENCY_CODES, currencyCodes);
		
		List<PurseDTO> purses = partnerservice.getPurses();
		mav.addObject(CCLPConstants.PURSES, purses);
		
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PARTNER')")
	@RequestMapping("/showEditPartner")
	public ModelAndView showEditPartner(@ModelAttribute("partnerForm") Partner partner) throws ServiceException {
		logger.debug(CCLPConstants.ENTER + partner);

		ModelAndView mav = new ModelAndView(CCLPConstants.SHOW_EDIT_PARTNER);
		logger.info("Adding partner to table {}", partner);

		List<CurrencyCodeDTO> currencyCodes = partnerservice.getCurrencyCodes();
		mav.addObject(CCLPConstants.CURRENCY_CODES, currencyCodes);
		
		List<PurseDTO> purses = partnerservice.getPurses();
		mav.addObject(CCLPConstants.PURSES, purses);

		Partner editPartner = partnerservice.getPartnerByPartnerId(partner.getPartnerId());
		if (editPartner != null) {
			mav.addObject(CCLPConstants.PARTNER_FORM, editPartner);
		} else {
			mav = new ModelAndView(CCLPConstants.PARTNER_CONFIG);
			mav.addObject(CCLPConstants.PARTNER_FORM, new Partner());
			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
		}
		logger.debug(CCLPConstants.EXIT + editPartner);
		return mav;
	}

	@PreAuthorize("hasRole('ADD_PARTNER')")
	@RequestMapping(value = "/addPartner")
	public ModelAndView addPartner(@Valid @ModelAttribute("partnerForm") Partner partner, BindingResult bindingResult)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		partner.setInsUser(userId);
		partner.setLastUpdUser(userId);

		List<CurrencyCodeDTO> currencyCodes = partnerservice.getCurrencyCodes();
		mav.addObject(CCLPConstants.CURRENCY_CODES, currencyCodes);
		
		List<PurseDTO> purses = partnerservice.getPurses();
		mav.addObject(CCLPConstants.PURSES, purses);

		mav.setViewName("showAddPartner");

		if (bindingResult.hasErrors()) {
			mav.addObject(CCLPConstants.PARTNER_FORM, partner);
			logger.error("Some error occured while binding the partner object");
			return mav;
		}

		logger.info("Adding partner to table {}", partner.toString());

		ResponseDTO responseDto = partnerservice.createPartner(partner);

		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Partner record '{}' has been added successfully", partner.getPartnerName());

				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(CCLPConstants.PARTNER_CONFIG);
				mav.addObject(CCLPConstants.PARTNER_FORM, new Partner());

			} else {
				logger.error("Error while adding partner");

				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");

				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				else
					mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.ADD_FAIL)
							+ " '" + partner.getPartnerName() + "'");

				mav.addObject(CCLPConstants.PARTNER_FORM, partner);
			}
		}

		logger.debug("EXIT " + partner);
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PARTNER')")
	@RequestMapping("/editPartner")
	public ModelAndView editPartner(@Valid @ModelAttribute("partnerForm") Partner partner, BindingResult bindingResult)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		List<CurrencyCodeDTO> currencyCodes = partnerservice.getCurrencyCodes();
		mav.addObject(CCLPConstants.CURRENCY_CODES, currencyCodes);
		
		List<PurseDTO> purses = partnerservice.getPurses();
		mav.addObject(CCLPConstants.PURSES, purses);

		mav.addObject(CCLPConstants.PARTNER_FORM, partner);
		partner.setLastUpdUser(userId);
		mav.setViewName(CCLPConstants.SHOW_EDIT_PARTNER);

		if (bindingResult.hasErrors()) {
			mav.addObject(CCLPConstants.PARTNER_FORM, partner);
			logger.error("Some error occured while binding the partner object");
			return new ModelAndView(CCLPConstants.SHOW_EDIT_PARTNER);
		}

		logger.info("Update partner in table {}", partner.toString());
		ResponseDTO responseDto = partnerservice.updatePartner(partner);

		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Partner record '{}' has been updated successfully", partner.getPartnerName());

				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(CCLPConstants.PARTNER_CONFIG);

				mav.addObject(CCLPConstants.PARTNER_FORM, new Partner());

			} else {
				logger.error("Failed to update record for partner '{}'", partner.getPartnerName());

				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");

				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				else
					mav.addObject(CCLPConstants.STATUS_MESSAGE,
							ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_FAIL) + " '"
									+ partner.getPartnerName() + "'");

				Partner editPartner = partnerservice.getPartnerByPartnerId(partner.getPartnerId());
				mav.addObject(CCLPConstants.PARTNER_FORM, editPartner);
			}
		}

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('DELETE_PARTNER')")
	@RequestMapping("/deletePartner")
	public ModelAndView deletePartner(HttpServletRequest request, @ModelAttribute("partnerForm") Partner partner) {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView("redirect:searchPartnerByName");

		ResponseDTO responseDto = null;

		logger.info("Delete partner from table {}", partner);

		responseDto = partnerservice.deletePartner(partner.getPartnerId());

		String deletedPartnerName = request.getParameter("deletePartnerName");

		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Partner record '{}' has been deleted successfully", partner.getPartnerName());
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			} else {
				logger.error("Failed to delete record for partner '{}'", partner.getPartnerName());
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				else
					mav.addObject(CCLPConstants.STATUS_MESSAGE,
							ResourceBundleUtil.getMessage(ResponseMessages.DELETE_FAIL) + " '" + deletedPartnerName
									+ "'");
			}
		}
		mav.addObject(CCLPConstants.PARTNER_FORM, partner);
		mav.addObject("partnerName", partner.getPartnerName());

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('VIEW_PARTNER')")
	@RequestMapping("/showViewPartner")
	public ModelAndView showViewPartner(@ModelAttribute("partnerForm") Partner partner) throws ServiceException {
		logger.debug(CCLPConstants.ENTER + partner);

		ModelAndView mav = new ModelAndView(CCLPConstants.SHOW_VIEW_PARTNER);

		logger.info("Viewing the partner {}", partner);
		List<CurrencyCodeDTO> currencyCodes = partnerservice.getCurrencyCodes();
		mav.addObject(CCLPConstants.CURRENCY_CODES, currencyCodes);
		
		List<PurseDTO> purses = partnerservice.getPurses();
		mav.addObject(CCLPConstants.PURSES, purses);

		Partner viewPartner = partnerservice.getPartnerByPartnerId(partner.getPartnerId());

		if (viewPartner != null)
			mav.addObject(CCLPConstants.PARTNER_FORM, viewPartner);
		else {

			logger.info("Partner retrieving failed");
			mav = new ModelAndView(CCLPConstants.PARTNER_CONFIG);
			mav.addObject(CCLPConstants.PARTNER_FORM, new Partner());
			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");

		}
		logger.debug(CCLPConstants.EXIT + viewPartner);
		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
}
