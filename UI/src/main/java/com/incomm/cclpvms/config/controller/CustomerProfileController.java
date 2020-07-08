package com.incomm.cclpvms.config.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.CustomerProfile;
import com.incomm.cclpvms.config.model.CustomerProfileDTO;

import com.incomm.cclpvms.config.model.Limitmap;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.maintenanceFee;
import com.incomm.cclpvms.config.model.monthlyFeeCapFee;
import com.incomm.cclpvms.config.model.transactionFee;
import com.incomm.cclpvms.config.service.CustomerProfileService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/customerProfile")
public class CustomerProfileController {

	private static final Logger logger = LogManager.getLogger(CustomerProfileController.class);

	HttpSession session;
	
	@Autowired
	CustomerProfileService customerProfileService;
	
	@Autowired
	SessionService sessionService;

	// @PreAuthorize("")
	@RequestMapping("/customerProfileConfig")
	public ModelAndView customerProfileConfig(HttpServletRequest request, HttpServletResponse response) {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(CCLPConstants.CUSTOMER_PROFLE_CONFIG);
		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, new CustomerProfile());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	// @PreAuthorize("")
	@RequestMapping("/viewCard")
	public ModelAndView viewCard(HttpServletRequest request, HttpServletResponse response)	throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("viewCard");
		String accountNumber=Util.isEmpty(request.getParameter(CCLPConstants.ACCT_NUM_HIDDEN))?"":request.getParameter(CCLPConstants.ACCT_NUM_HIDDEN);
		logger.debug("accountNumber"+accountNumber);
		List<Object[]> cardList=customerProfileService.getCardsByAccountNumber(accountNumber);
		mav.addObject("cardList",cardList);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	// @PreAuthorize("")
	@RequestMapping("/searchCustomerProfile")
	public ModelAndView searchCustomerProfile(@ModelAttribute("customerProfileForm") CustomerProfile customerProfileForm,HttpServletRequest request, HttpServletResponse response)	throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(CCLPConstants.CUSTOMER_PROFLE_CONFIG);
		if( Util.isEmpty(customerProfileForm.getAccountNumber()) && Util.isEmpty(customerProfileForm.getProxyNumber()) && Util.isEmpty(customerProfileForm.getCardNumber()) ) {
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE,"Please enter atleast one value");
		}
		else
		{
		List<CustomerProfileDTO> customerProfileList= customerProfileService.getCustomerProfileByCardOrAccountOrProxy(customerProfileForm);
		mav.addObject("customerProfileList",customerProfileList);
		mav.addObject("showGrid","true");
		}
	
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	// @PreAuthorize("hasRole('ADD_PRODUCT')")
	@RequestMapping("/showAddCustomerProfile")
	public ModelAndView showAddCustomerProfile(HttpServletRequest req)
			{
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		CustomerProfile customerProfile=new CustomerProfile();
		logger.debug("account number",req.getParameter(CCLPConstants.ACCT_NUM_HIDDEN));
		String accountNumber=Util.isEmpty(req.getParameter(CCLPConstants.ACCT_NUM_HIDDEN))?"":req.getParameter(CCLPConstants.ACCT_NUM_HIDDEN);
		String proxyNumber=Util.isEmpty(req.getParameter("proxyNumber_hidden"))?"":req.getParameter("proxyNumber_hidden");
		customerProfile.setAccountNumber(accountNumber);
		customerProfile.setProxyNumber(proxyNumber);
		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
		mav.setViewName(CCLPConstants.ADD_CUSTOMERPROFILE);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
 
	
	//@PreAuthorize("/")
	@RequestMapping("/addCustomerProfile")
	public ModelAndView addCustomerProfile(
			@ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfile,
			HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		customerProfile.setLastUpdUser(sessionService.getUserId());
		ResponseDTO responseDto = customerProfileService.addCustomerProfile(customerProfile);
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Customer Profile  has been added successfully");
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, new CustomerProfile());
				mav.setViewName(CCLPConstants.CUSTOMER_PROFLE_CONFIG);
			} else {
				logger.error("Error while adding Order " + responseDto.getMessage());
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
				mav.setViewName(CCLPConstants.ADD_CUSTOMERPROFILE);
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	// @PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/showEditCustomerProfile")
	public ModelAndView showEditCustomerProfile(HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		String id = request.getParameter("profileId_hidden");
		Long profileID;
		// when request is from tab click profileId is fetched from session
		if (id == null || id.isEmpty()) {
			profileID = (Long) session.getAttribute(CCLPConstants.CUST_PFL_ID);
		} else {
			profileID = Long.parseLong(id);
		}
		logger.debug("Profile ID", request.getParameter("profileId_hidden"));
		CustomerProfileDTO dto = customerProfileService.getCustomerProfileById(profileID);
		CustomerProfile customerProfile = new CustomerProfile();
		customerProfile.setProfileId(profileID);
		customerProfile.setAccountNumber(dto.getAccountNumber());
		customerProfile.setProxyNumber(dto.getProxyNumber());

		Map<String, String> headerData = new HashMap<>();

		String limitsTab = "";
		String txnFeesTab = "";
		String maintenanceFeesTab = "";

		ModelAndView mav = new ModelAndView();

		try {

			session = request.getSession();
			// the below session data is used in customerProfile edit tabs(limit,fee)
			session.setAttribute(CCLPConstants.CUST_PFL_ID, profileID);
			Map<String, Object> temMap = dto.getAttributesMap().get("Card");
			customerProfile.setCardAttributes(temMap);
			if (temMap != null) {

				if (temMap.containsKey(CCLPConstants.FEESSUPPORTED_CARD)
						&& !Objects.isNull(temMap.get(CCLPConstants.FEESSUPPORTED_CARD))) {
					txnFeesTab = String.valueOf(temMap.get(CCLPConstants.FEESSUPPORTED_CARD));
					headerData.put("txnFeesTabCard", txnFeesTab);
				}

				if (temMap.containsKey(CCLPConstants.LIMITSUPPORTED_CARD)
						&& !Objects.isNull(temMap.get(CCLPConstants.LIMITSUPPORTED_CARD))) {
					limitsTab = String.valueOf(temMap.get(CCLPConstants.LIMITSUPPORTED_CARD));
					headerData.put("limitsTabCard", limitsTab);
				}

				if (temMap.containsKey(CCLPConstants.MAINTAINANCEFEE_SUPPORTED_CARD)
						&& !Objects.isNull(temMap.get(CCLPConstants.MAINTAINANCEFEE_SUPPORTED_CARD))) {
					maintenanceFeesTab = String.valueOf(temMap.get(CCLPConstants.MAINTAINANCEFEE_SUPPORTED_CARD));
					headerData.put("maintenanceFeesTabCard", maintenanceFeesTab);
				}

			}
			mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
			session.setAttribute("headerData", headerData);
			mav.setViewName(CCLPConstants.EDIT_CUSTOMERPROFILE);
		} catch (NumberFormatException e) {
			mav.setViewName(CCLPConstants.EDIT_CUSTOMERPROFILE);
			logger.error("NumberFormatException Occured While Editing customer profile" + e);
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/*Delete customer profile*/
	//	@PreAuthorize("hasRole('DELETE_ROLE')")
	@RequestMapping("/deleteCustomerProfile")
	public ModelAndView deleteCustomerProfile(HttpServletRequest request,
			@ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfile) {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView(CCLPConstants.CUSTOMER_PROFLE_CONFIG);
		ResponseDTO responseDto = null;
		Long profileID = Util.isEmpty(String.valueOf(customerProfile.getProfileId())) ? 0
				: customerProfile.getProfileId();
		logger.info("profileID " + profileID + " Delete customer profile from table {}", customerProfile);
		responseDto = customerProfileService.deleteCustomerProfile(profileID);
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Customer Profile record '{}' has been deleted successfully", profileID);
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			} else {
				logger.error("Failed to delete record for Customer Profile'{}'", profileID);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.addObject("showGrid", "true");
			}
		}

		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	/*Update card Attributes*/
	//	@PreAuthorize("hasRole('DELETE_ROLE')")
	@RequestMapping("/updateCustomerProfile")
	public ModelAndView updateCardAttributes(HttpServletRequest request, @ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfile) throws ServiceException
	{
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDto = null;
		logger.info("profileID " + customerProfile.getProfileId() + " update customer profile from table {}",
				customerProfile);
		responseDto = customerProfileService.updateCardAttributes(customerProfile);
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Customer Profile record '{}' has been updated successfully",
						customerProfile.getProfileId());
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(CCLPConstants.CUSTOMER_PROFLE_CONFIG);
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, new CustomerProfile());

			} else {
				logger.error("Failed to update record for Customer Profile'{}'", customerProfile.getProfileId());
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(CCLPConstants.EDIT_CUSTOMERPROFILE);
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);

			}
		}

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}	
	/*Copy customer profile*/
	@SuppressWarnings("unchecked")
	@RequestMapping("/getCustomerProfileDetails")
	public ModelAndView getCustomerProfileDetails(
			@ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfile,
			HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		String viewPage = request.getParameter("viewPage");
		ModelAndView mav = new ModelAndView(viewPage);
		String spnumbertype = request.getParameter("spnumbertype");
		String spnumber = request.getParameter("parentCardData");
		logger.debug("type:" + spnumbertype + "value:" + spnumber);
		String attributeGroup = mappingAttribueGroupAndView(viewPage);
		if (attributeGroup != null && !Util.isEmpty(spnumber) && !Util.isEmpty(spnumbertype)) {
			responseDto = customerProfileService.getCustomerProfileDetails(spnumbertype, spnumber, attributeGroup);
			if (responseDto != null) {
				if (responseDto.getResult() != null
						&& responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
					Map<String, Object> cardAttributes = (Map<String, Object>) responseDto.getData();
					customerProfile.setCardAttributes(cardAttributes);
				} else {
					logger.error("Failed to retrive record for Customer Profile'{}'",
							customerProfile.getAccountNumber());
					mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
					if (responseDto.getMessage() != null)
						mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());

				}
			}
		}
		if (viewPage.equalsIgnoreCase(CCLPConstants.CUSTOMERPROFILE_LIMIT)
				|| viewPage.equalsIgnoreCase(CCLPConstants.CUSTOMERPROFILE_TXN_FEE)) {
			List<Object> txnDtls = customerProfileService.getDeliveryChnlTxns();
			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));

		}
		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	/* Limits starts*/
	/*
	 * This method is called onClick of limits tab or onClick of Copy From button.
	 * productId = productId which propagates from general tab, productName =
	 * productName which propagates from general tab. copyFromProduct = productId of
	 * copy from dropdown which propagates from limits tab
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/customerProfileLimit")
	public ModelAndView customerProfileLimit(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView();
		session = request.getSession(false);

		String profileId = String.valueOf((Long) session.getAttribute(CCLPConstants.CUST_PFL_ID));
		logger.debug("profile Id : " + profileId);

		CustomerProfile customerProfile = new CustomerProfile();

		if (profileId != null) {

			ModelMapper mm = new ModelMapper();
			List<Object> txnDtls = customerProfileService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.setViewName(CCLPConstants.CUSTOMERPROFILE_LIMIT);

			customerProfile.setProfileId(Long.parseLong(profileId));
			ResponseDTO responseDTO = customerProfileService.getLimits(Long.parseLong(profileId));
			if (responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> cardLimitMap = responseDTO.getData() != null
						? mm.map(responseDTO.getData(), Map.class)
						: new HashMap<>();
				customerProfile.setCardAttributes(cardLimitMap);
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);

			} else {
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getMessage());
			}
		} else {
			logger.error("mainProfileId and copyProfileId are null");

		}

		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	//@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveLimits")
	public ModelAndView saveLimits(@Validated(Limitmap.class) @ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfileForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		logger.debug(CCLPConstants.ENTER);
		

		mav.setViewName(CCLPConstants.CUSTOMERPROFILE_LIMIT);
		if (bindingResult.hasErrors()) {
			List<Object> txnDtls = customerProfileService.getDeliveryChnlTxns();
			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfileForm);

			return mav;
		}
		ResponseDTO responseDto = customerProfileService.addLimits(customerProfileForm.getCardAttributes(),
				customerProfileForm.getProfileId());
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Limit record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_LIMIT_UPDATE_FAIL));
		}

		List<Object> txnDtls = customerProfileService.getDeliveryChnlTxns();
		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfileForm);


		return mav;
	}
    /*Limits ends*/
	
	
	/* Transaction Fee starts*/
	/*
	 * This method is called onClick of TxnFee tab or onClick of Copy From button.
	 * productId = productId which propagates from general tab, productName =
	 * productName which propagates from general tab. copyFromProduct = productId of
	 * copy from dropdown which propagates from limits tab
	 */
	@SuppressWarnings("unchecked")
	//@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/customerProfileTransactionFee")
	public ModelAndView customerProfileTransactionFee(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView();
		session = request.getSession(false);

		String profileId = String.valueOf((Long) session.getAttribute(CCLPConstants.CUST_PFL_ID));
		logger.debug("profile Id : " + profileId);

		
		CustomerProfile customerProfile=new CustomerProfile();

		if (profileId != null ) {

			ModelMapper mm = new ModelMapper();
			List<Object> txnDtls = customerProfileService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.setViewName(CCLPConstants.CUSTOMERPROFILE_TXN_FEE);

			customerProfile.setProfileId(Long.parseLong(profileId));
			ResponseDTO responseDTO = customerProfileService.getTxnFees(Long.parseLong(profileId));
			if (responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> cardLimitMap = responseDTO.getData() != null
						? mm.map(responseDTO.getData(), Map.class)
						: new HashMap<>();
				customerProfile.setCardAttributes(cardLimitMap);
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);

			} else {
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getMessage());
			}
		} else {
			logger.error("mainProfileId and copyProfileId are null");

		}

		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	//@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveTransactionFees")
	public ModelAndView saveTxnFees(@Validated(transactionFee.class) @ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfileForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView(CCLPConstants.CUSTOMERPROFILE_TXN_FEE);
		logger.debug(CCLPConstants.ENTER);
		if (bindingResult.hasErrors()) {
			logger.debug("Server side validation errors");
			List<Object> txnDtls = customerProfileService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.addObject("productForm", customerProfileForm);

			return mav;
		}
		ResponseDTO responseDto = customerProfileService.addTxnFees(customerProfileForm.getCardAttributes(),
				customerProfileForm.getProfileId());
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Txn Fee record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}

		List<Object> txnDtls = customerProfileService.getDeliveryChnlTxns();
		logger.debug(txnDtls);

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfileForm);

		return mav;
	}
	/* Transaction Fee ends*/
	/* Monthly Fee Cap starts*/
	@SuppressWarnings("unchecked")
	//@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/customerProfileMonthlyFeeCap")
	public ModelAndView customerProfileMonthlyFeeCap(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		ModelAndView mav = new ModelAndView(CCLPConstants.CUSTOMERPROFILE_MONTHLYFEE_CAP);
		session = request.getSession();

		String profileId = String.valueOf((Long) session.getAttribute(CCLPConstants.CUST_PFL_ID));
		CustomerProfile customerProfile=new CustomerProfile();
		customerProfile.setProfileId(Long.parseLong(profileId));
		if (profileId != null ) {

			ResponseDTO responseBody = customerProfileService.getMonthlyFeeCap(Long.parseLong(profileId));

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> productFeeCapMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				if (productFeeCapMap != null) {

					logger.debug("productFeeCapMap " + productFeeCapMap);

					customerProfile.setCardAttributes(productFeeCapMap);
					mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
				}
			} else {
				logger.error("Failed to Fetch Monthly Fee Cap Attributes from config srvice");
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {

			logger.error("Profile ID is null");
		}
		return mav;
	}

	//@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveMonthlyFeeCap")
	public ModelAndView saveMonthlyFeeCap(
			@Validated(monthlyFeeCapFee.class)  @ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfileForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView(CCLPConstants.CUSTOMERPROFILE_MONTHLYFEE_CAP);
		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.CUSTOMER_PROFLE_FORM + customerProfileForm);
		if (bindingResult.hasErrors()) {
			logger.debug("Server side validation errors");
			mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfileForm);

			return mav;
		}
		ResponseDTO responseDto = customerProfileService.addMonthlyFeeCap(customerProfileForm.getCardAttributes(),
				customerProfileForm.getProfileId());
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Monthly Fee Cap record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}

		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfileForm);

		return mav;
	}
	/* Monthly Fee Cap ends*/
	
	/* Maintenance Fee starts*/
	@SuppressWarnings("unchecked")
	//@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/customerProfileMaintenanceFee")
	public ModelAndView customerProfileMaintenanceFee(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		ModelAndView mav = new ModelAndView(CCLPConstants.CUSTOMERPROFILE_MAINTENANCE_FEE);
		session = request.getSession(false);

		String profileId = String.valueOf((Long) session.getAttribute(CCLPConstants.CUST_PFL_ID));

		CustomerProfile customerProfile=new CustomerProfile();
		customerProfile.setProfileId(Long.parseLong(profileId));
		if (profileId != null) {

			ResponseDTO responseBody = customerProfileService.getMaintenanceFee(Long.parseLong(profileId));

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> customerProfileMaintanancefeeMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				if (customerProfileMaintanancefeeMap != null) {

					logger.debug("customerProfileMaintenanceFeeMap " + customerProfileMaintanancefeeMap);
					customerProfile.setCardAttributes(customerProfileMaintanancefeeMap);
					mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
				}
			} else {
				logger.error("Failed to Fetch Maintenance Fee Attributes from config srvice");
				mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfile);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {
			logger.error("Profile ID is null");
		}
		return mav;
	}

	//@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveMaintenanceFee")
	public ModelAndView saveMaintenanceFee(
			@Validated(maintenanceFee.class) @ModelAttribute(CCLPConstants.CUSTOMER_PROFLE_FORM) CustomerProfile customerProfileForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView(CCLPConstants.CUSTOMERPROFILE_MAINTENANCE_FEE);
		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.CUSTOMER_PROFLE_FORM + customerProfileForm);
		if (bindingResult.hasErrors()) {
			logger.info("Server Side validation errors");
			mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfileForm);
			return mav;
		}
		ResponseDTO responseDto = customerProfileService.addMaintenanceFee(customerProfileForm.getCardAttributes(),customerProfileForm.getProfileId());
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Maintenance Fee record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());

		}
		mav.addObject(CCLPConstants.CUSTOMER_PROFLE_FORM, customerProfileForm);
		return mav;
	}
	/* Maintenance Fee ends*/
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();
		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("failstatus", errMessage);
		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
	public String mappingAttribueGroupAndView(String viewPage)
	{
				
		if(viewPage.equalsIgnoreCase(CCLPConstants.ADD_CUSTOMERPROFILE) || viewPage.equalsIgnoreCase(CCLPConstants.EDIT_CUSTOMERPROFILE))
			return "Card";
		if(viewPage.equalsIgnoreCase(CCLPConstants.CUSTOMERPROFILE_LIMIT))
			return "Limits";
		if(viewPage.equalsIgnoreCase(CCLPConstants.CUSTOMERPROFILE_MAINTENANCE_FEE))
			return "Maintenance Fees";
		if(viewPage.equalsIgnoreCase(CCLPConstants.CUSTOMERPROFILE_MONTHLYFEE_CAP))
			return "Monthly Fee Cap";
		if(viewPage.equalsIgnoreCase(CCLPConstants.CUSTOMERPROFILE_TXN_FEE))
			return "Transaction Fees";
		return "null";
	}

}
