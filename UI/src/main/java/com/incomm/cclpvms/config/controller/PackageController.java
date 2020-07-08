package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.PackageID;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.PackageService;
import com.incomm.cclpvms.config.validator.ValidationPackageID;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/package")
public class PackageController {
	
	
	private static final Logger logger = LogManager.getLogger(PackageController.class);

	
	@Autowired
	public PackageService packageService;
	
	@Autowired
	public ValidationPackageID customValidator;
	
	
	
	private static final String ADD_PACKAGE_URL = "addPackageurl";
	private static final String ADD_PACKAGE = "addPackage";
	private static final String DELETE_BOX = "deleteBox";
	private static final String EDIT_PACKAGE = "editPackage";
	private static final String EDIT_PACKAGE_ID = "editpackageId";
	private static final String PACKAGE_FORM = "packageForm";
	private static final String PACKAGE_ID_CNFG = "packageIDConfig";
	private static final String PACKAGE_ID_LIST = "packageIdList";
	private static final String PARENT_PACKAGE_MAP = "parentPackageMap";
	private static final String SHOW_COPY_FROM = "showCopyFrom";
	private static final String VIEW_PACKAGE = "viewPackage";
	private static final String VIEW_PACKAGE_ID = "viewpackageId";
	
	
	
	
	
	/**
	 * This Methods Displays the Search page for entering Package
	 * Name/Prefix.
	 */
	@PreAuthorize("hasRole('SEARCH_PACKAGEID')")
	@RequestMapping("/packageIDConfig")
	public ModelAndView packageConfiguration()  {
		logger.info(" ENTER packageConfiguration ");
		ModelAndView mav = new ModelAndView();
		PackageID pack = new PackageID();
		mav.addObject(PACKAGE_FORM, pack);
		mav.setViewName(PACKAGE_ID_CNFG);
		logger.info(" EXIT packageConfiguration ");
		return mav;
	}
	
	
	/**
	 * An empty search will return all the Available Package Names.
	 */
	@PreAuthorize("hasRole('SEARCH_PACKAGEID')")
	@RequestMapping("/showAllPackages")
	public ModelAndView showAllPackages(Map<String, Object> model,
			@ModelAttribute(PACKAGE_FORM) PackageID packageForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException {
		logger.info("****showAllPackages Method ********");
		ModelAndView mav = new ModelAndView();
		model.put("pkgTblList", packageService.getAllPackages());
		mav.addObject(PACKAGE_FORM, packageForm);
		mav.addObject(DELETE_BOX, new PackageID());
		mav.addObject("showGrid", "true");
		mav.setViewName(PACKAGE_ID_CNFG);
		logger.info(" EXIT showAllPackages ");
		return mav;
	}

	
	/**
	 * An empty search will return all the Available Package Names.
	 */
	@PreAuthorize("hasRole('SEARCH_PACKAGEID')")
	@RequestMapping("/showAllPackagesByName")
	public ModelAndView showAllPackagesByName(Map<String, Object> model,
			@ModelAttribute(PACKAGE_FORM) PackageID packageForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException {
		logger.info("****showAllPackagesByName Method ********");
		ModelAndView mav = new ModelAndView();
		model.put("pkgTblList", packageService.getAllPackagesByName(packageForm.getDescription().trim()));
		mav.addObject(PACKAGE_FORM, packageForm);
		mav.addObject("showGrid", "true");
		mav.setViewName(PACKAGE_ID_CNFG);
		logger.info(" EXIT showAllPackages ");
		return mav;
	}

	
	
	/**
	 * This Method is to display the Add Package Screen
	 */
	@PreAuthorize("hasRole('ADD_PACKAGEID')")
	@RequestMapping("/showAddPackage")
	public ModelAndView showAddPackage(Map<String, Object> model,HttpServletRequest request) throws ServiceException {
		logger.info("****showAddPackage Method ********");
		ModelAndView mav = new ModelAndView();

		Map<String, String> parentPackageMap = packageService.getPackageIdList();

		mav.addObject(ADD_PACKAGE_URL,Util.constructUrl(request));

		if (parentPackageMap != null && !parentPackageMap.isEmpty()) {

			mav.addObject(PARENT_PACKAGE_MAP, parentPackageMap);
			mav.addObject(SHOW_COPY_FROM, "true");

		}
		mav.addObject("shipmentMethodList", packageService.getShipmentAttList());
		mav.addObject("fulFillmentList", packageService.getfulFillmentList());
		mav.addObject(PACKAGE_ID_LIST, packageService.getPackageIdList());
		mav.addObject(PACKAGE_FORM, new PackageID());
		mav.setViewName(ADD_PACKAGE);
		logger.info(" EXIT showAddPackage ");
		return mav;
	}
	
	
	/**
	 * This Method is used to add the Package ID details entered in the UI.
	 */
	@PreAuthorize("hasRole('ADD_PACKAGEID')")
	@RequestMapping(value = "/addPackage", method = RequestMethod.POST)
	public ModelAndView addPackage(HttpServletRequest request,
			@Validated({ PackageID.ValidationStepOne.class,
					PackageID.ValidationStepTwo.class }) @ModelAttribute(PACKAGE_FORM) PackageID packagedtls,
			BindingResult result) throws ServiceException {
		logger.info("****addPackage Method ********");
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDTO = null;
		String message = "";
		Map<String, String> parentPackageMap = packageService.getPackageIdList();
		

		mav.addObject(ADD_PACKAGE_URL,Util.constructUrl(request));
	     

		if (parentPackageMap != null && !parentPackageMap.isEmpty()) {

			mav.addObject(PARENT_PACKAGE_MAP, parentPackageMap);
			mav.addObject(SHOW_COPY_FROM, "true");
			

		}
		customValidator.validate(packagedtls, result);

		if (result.hasErrors()) {
			logger.info("****came inside add method errors ********" + result.getAllErrors());
			loadDropDown(mav);
			mav.setViewName(ADD_PACKAGE);
			return mav;
		}
		
		
		if(packagedtls.getReplacementPackageId().isEmpty()||packagedtls.getReplacementPackageId()==null)
		{
			packagedtls.setReplacementPackageId(packagedtls.getPackageId());
		}

		responseDTO = packageService.createPackage(packagedtls);
		logger.debug("RESPONSE CODE: " + responseDTO.getCode() + " Error Message : " + responseDTO.getMessage());
		message = responseDTO.getMessage();
		if (ResponseMessages.SUCCESS.equals(responseDTO.getCode().trim())) {
			mav.addObject("successstatus", message);
			mav.setViewName(PACKAGE_ID_CNFG);
			mav.addObject(PACKAGE_FORM, new PackageID());

		} else {
			loadDropDown(mav);
			mav.addObject("failstatus", message);
			mav.setViewName(ADD_PACKAGE);
			mav.addObject(PACKAGE_FORM, packagedtls);
		}

		mav.addObject(DELETE_BOX, new PackageID());
		logger.info("**** EXIT addPackage ********");
		return mav;
	}
	
	
	

	/**
	 * This method is to display the edit Fulfillment screen to the user.
	 */
	@PreAuthorize("hasRole('EDIT_PACKAGEID')")
	
	@RequestMapping("/showEditPackage")
	public ModelAndView showEditPackage(HttpServletRequest request) throws ServiceException {
		logger.info("****showEditPackage Method ********");
		String packageId ;
		PackageID packageID = null;
		ModelAndView mav = new ModelAndView();
		try {
			packageId = request.getParameter("packageId").trim();
			mav.setViewName(EDIT_PACKAGE);
			logger.debug("packageId:: " + packageId);
			packageID = packageService.getPackageId(packageId);
			logger.info("****packageID OBJ ********" + packageID);
			if(packageID!=null) {
			loadDropDown(mav);
			mav.addObject(PACKAGE_FORM, packageID);
			mav.addObject(EDIT_PACKAGE_ID, packageId);
			}

		} catch (NumberFormatException e) {
			mav.setViewName(EDIT_PACKAGE);
			mav.addObject(PACKAGE_FORM, packageID);
			mav.addObject(PACKAGE_FORM, new PackageID());
			logger.error("NumberFormatException Occured While Editing in showEditPackage()" + e);
		}
		logger.info("****EXIT showEditPackage Method ********");
		return mav;
	}
	
	

	/**
	 * This Method is used to update the Package ID details entered in the UI.
	 */
	@PreAuthorize("hasRole('EDIT_PACKAGEID')")
	@RequestMapping(value = "/updatePackage")
	public ModelAndView updatePackage(
			@Validated({ PackageID.ValidationStepOne.class,
					PackageID.ValidationStepTwo.class }) @ModelAttribute(PACKAGE_FORM) PackageID packagedtls,
			BindingResult result, HttpServletRequest request) throws ServiceException {
		logger.info("****addPackage Method ********");
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDTO = null;
		String packageid=null ;
		String message = "";

		customValidator.validate(packagedtls, result);

		if (request.getParameter(EDIT_PACKAGE_ID) != null
				&& !request.getParameter(EDIT_PACKAGE_ID).equalsIgnoreCase("0")) {
			packageid = request.getParameter(EDIT_PACKAGE_ID);
		}

		if (result.hasErrors()) {
			logger.info("****came inside add method errors ********" + result.getAllErrors());
			try {
				loadDropDown(mav);
				mav.addObject(EDIT_PACKAGE_ID, packageid);
				mav.setViewName(EDIT_PACKAGE);
				return mav;
			} catch (Exception e) {
				logger.error("Binding Result Errors" + e.getMessage());
			}
		}

	
		if(packagedtls.getReplacementPackageId().isEmpty()||packagedtls.getReplacementPackageId()==null)
		{
			packagedtls.setReplacementPackageId(packagedtls.getPackageId());
		}
	

		responseDTO = packageService.udpatePackage(packagedtls);
		logger.debug("RESPONSE CODE: " + responseDTO.getCode() + " Error Message : " + responseDTO.getMessage());
		message = responseDTO.getMessage();
		if (ResponseMessages.SUCCESS.equals(responseDTO.getCode().trim())) {
			mav.addObject("successstatus", message);
			mav.setViewName(PACKAGE_ID_CNFG);
			mav.addObject(PACKAGE_FORM, new PackageID());

		} else {
			loadDropDown(mav);
			mav.addObject("failstatus", message);
			mav.setViewName(EDIT_PACKAGE);
			mav.addObject(PACKAGE_FORM, packagedtls);
		}

		mav.addObject(DELETE_BOX, new PackageID());
		logger.info("**** EXIT addPackage ********");
		return mav;
	}
	
	
	
	public void loadDropDown(ModelAndView mav) throws ServiceException
	{
		mav.addObject("shipmentMethodList", packageService.getShipmentAttList());
		mav.addObject("fulFillmentList", packageService.getfulFillmentList());
		mav.addObject(PACKAGE_ID_LIST, packageService.getPackageIdList());	
	}
	

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug("ENTER");
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("statusMessage", errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.error(exception.getMessage());
		logger.debug("EXIT");

		return mav;
	}

	@RequestMapping("/getPackageDetails")
	public ModelAndView getAlertProductDetails(@ModelAttribute(PACKAGE_FORM) PackageID packagedtls,
			HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		String packageId;
		PackageID packageID =null;
		ModelAndView mav = new ModelAndView();

		mav.addObject(ADD_PACKAGE_URL,Util.constructUrl(request));
		
		Map<String, String> parentPackageMap = packageService.getPackageIdList();

		if (parentPackageMap != null && !parentPackageMap.isEmpty()) {
			
			mav.addObject(PARENT_PACKAGE_MAP, parentPackageMap);
			mav.addObject(SHOW_COPY_FROM, "true");

		}

			
		try {
			packageId = request.getParameter("parentPackageId").trim();
			mav.setViewName(ADD_PACKAGE);
			 packageID = packageService.getPackageId(packageId);
			if (packageID != null) {
				loadDropDown(mav);
				packageID.setParentPackageId(packagedtls.getParentPackageId());
				mav.addObject(PACKAGE_ID_LIST, packageService.getPackageIdList());
				
				mav.addObject(PACKAGE_FORM, packageID);
				
				
			}

		} catch (NumberFormatException e) {
			mav.setViewName(ADD_PACKAGE);
			mav.addObject(PACKAGE_FORM, packageID);
			mav.addObject(PACKAGE_ID_LIST, packageService.getPackageIdList());
			mav.addObject(PACKAGE_FORM, new PackageID());
		}
		
		
		

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	/**
	 * View Package Screen
	 */
	@PreAuthorize("hasRole('VIEW_PACKAGEID')")
	@RequestMapping("/showViewPackage")
	public ModelAndView showViewPackage(HttpServletRequest request) throws ServiceException {
		logger.info("****showViewPackage Method ********");
		String packageId ;
		PackageID packageID = null;
		ModelAndView mav = new ModelAndView();
		try {
			packageId = request.getParameter("packageId").trim();
			mav.setViewName(VIEW_PACKAGE);
			logger.debug("packageId:: " + packageId);
			packageID = packageService.getPackageId(packageId);
			logger.info("****packageID OBJ in view method********" + packageID);
			if(packageID!=null) {
			loadDropDown(mav);
			mav.addObject(PACKAGE_FORM, packageID);
			mav.addObject(VIEW_PACKAGE_ID, packageId);
			}

		} catch (NumberFormatException e) {
			mav.setViewName(VIEW_PACKAGE);
			mav.addObject(PACKAGE_FORM, packageID);
			mav.addObject(PACKAGE_FORM, new PackageID());
			logger.error("Exception Occured While Viewing in showViewPackage()" + e);
		}
		logger.info("****EXIT showViewPackage Method ********");
		return mav;
	}
}
