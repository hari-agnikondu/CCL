package com.incomm.cclpvms.config.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.incomm.cclpvms.config.model.Issuer;
import com.incomm.cclpvms.config.model.IssuerDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.IssuerService;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.IssuerException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/issuer")
public class IssuerController {	

	private static final Logger logger = LogManager.getLogger(IssuerController.class);
	

	@Autowired
	public IssuerService issuerService;
	
	@Value("${INS_USER}")
	public long insUser;
	
	public static final String ENTER="Enter";
	public static final String EXIT="Exit";
	
	private static final String SEARCH_TYPE = "SearchType";
	private static final String ADD_ISSUER = "addIssuer";
	private static final String CONFIRM_BOX = "confirmBox";
	private static final String DELETE_BOX = "deleteBox";
	private static final String EDIT_ISSUER = "editIssuer";
	private static final String EDIT_ISSUERID = "editIssuerId";
	private static final String FAIL_STATUS = "failstatus";
	private static final String INS_USER = "insUser";
	private static final String ISSUER_CONFIG = "issuerConfig";
	private static final String ISSUER_FORM = "issuerForm";
	private static final String ISSUER_TABLE_LIST = "issuerTableList";
	private static final String SRCH_TYPE = "searchType";
	private static final String SHOW_GRID = "showGrid";
	private static final String SUCCESS_STATUS = "successstatus";
	private static final String VIEW_ISSUER = "viewIssuer";
	private static final String ISSUER_ID="issuerId";


	@PreAuthorize("hasRole('SEARCH_ISSUER')")
	@RequestMapping("/issuerConfig")
	public ModelAndView issuerConfiguration(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
	
		logger.debug(ENTER);
		HttpSession session = request.getSession();
		session.setAttribute(INS_USER, insUser);
		ModelAndView mav = new ModelAndView();
		mav.addObject(ISSUER_FORM, new Issuer());
		mav.addObject(DELETE_BOX, new Issuer());
		
		mav.setViewName(ISSUER_CONFIG);
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('ADD_ISSUER')")
	@RequestMapping("/addIssuer")
	public ModelAndView addIssuer(Map<String, Object> model,
			@Validated({ Issuer.ValidationStepOne.class,
					Issuer.ValidationStepTwo.class }) @ModelAttribute(ISSUER_FORM) Issuer issuerForm,
			BindingResult bindingResult,HttpServletRequest request)
					throws ServiceException {

		logger.debug(ENTER);
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		IssuerDTO iss = new IssuerDTO();
		String flag = "";
		ResponseDTO responseDTO = null;
		String issuerName ="";
		String iMdmId="";
		String iDescription="";
		long insuser=0;
		String message = "";

			if (bindingResult.hasErrors()) {
				mav.setViewName(ADD_ISSUER);
				return mav;

			} 
			else
			{
				
				if (Util.isEmpty(issuerForm.getIssuerName())) {
					issuerName="";
				}else {
					issuerName = issuerForm.getIssuerName().trim();
				}
				
				if (Util.isEmpty(issuerForm.getMdmId())) {
					iMdmId="";
				}else {
					iMdmId = issuerForm.getMdmId();
				}
				
				if (Util.isEmpty(issuerForm.getDescription())) {
					iDescription="";
				}else {
					iDescription = issuerForm.getDescription();
				}
								
					
				insuser= (Long) session.getAttribute(INS_USER);
			
			flag = "Y";
				
			   
				logger.info(" Issuer  details :: Issuer Name: " + issuerName + " MDMID: " + iMdmId + " Active: " + flag
						+ " Description: " + iDescription);

				iss.setIssuerName(issuerName);
				iss.setMdmId(iMdmId);
				iss.setInsUser(insUser);
				iss.setIsActive(flag);
				iss.setInsDate(new Date());
				iss.setLastUpdDate(new Date());
				iss.setLastUpdUser(insuser);
				iss.setDescription(iDescription);

				logger.debug("Before Calling issuerservice.createIssuer");
				responseDTO = issuerService.createIssuer(iss);
				logger.debug("after Calling issuerservice.createIssuer");
				logger.info("Message from createIssuer method: " + responseDTO.getMessage());

				logger.debug("CCLP-VMS Response Code : "+responseDTO.getCode()+" Error Message : "+responseDTO.getMessage());
				if (responseDTO.getCode().equalsIgnoreCase("000")) 
				{ 
					message=responseDTO.getMessage(); 
					mav.addObject(SUCCESS_STATUS, message);
					mav.setViewName(ISSUER_CONFIG);
					mav.addObject(ISSUER_FORM,  new IssuerDTO());
					mav.addObject(DELETE_BOX, new Issuer());
				} else {
					message=responseDTO.getMessage(); 
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(ADD_ISSUER);
					mav.addObject(ISSUER_FORM, iss);
				}	
			}
			logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('ADD_ISSUER')")
	@RequestMapping("/showAddIssuer")
	public ModelAndView showAddIssuer(Map<String, Object> model) {
		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		mav.addObject(ISSUER_FORM, new Issuer());
		mav.setViewName(ADD_ISSUER);
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_ISSUER')")
	@RequestMapping("/showAllIssuers")
	public ModelAndView showAllIssuers(Map<String, Object> model,
			@ModelAttribute(ISSUER_FORM) Issuer issuerForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException, IssuerException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		List<IssuerDTO> issList = null;
		String issuerName ="";
		String searchType="";
		
			 mav.setViewName(ISSUER_CONFIG);
			 issuerName = issuerForm.getIssuerName();
			 if(request.getParameter(SRCH_TYPE)==null) {
				 searchType="";
			 }else {
			 searchType = request.getParameter(SRCH_TYPE);
			 }
			 mav.addObject(SEARCH_TYPE, searchType);
			logger.info("Issuer Name: " + issuerName);
			logger.debug("Before calling issuerservice.getAllIssuers()");
			
			issList = issuerService.getAllIssuers();
			
			logger.debug("after calling issuerservice.getAllIssuers()");
			mav.addObject(ISSUER_FORM, new Issuer());
			mav.setViewName(ISSUER_CONFIG);
			mav.addObject(ISSUER_TABLE_LIST, issList);
			mav.addObject(ISSUER_FORM, issuerForm);
			mav.addObject(SHOW_GRID, "true");
			mav.addObject(DELETE_BOX, new Issuer());
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_ISSUER')")
	@RequestMapping("/searchIssuerByName")
	public ModelAndView searchIssuerByName(Map<String, Object> model,
			@Validated(Issuer.SearchScreen.class)  @ModelAttribute(ISSUER_FORM) Issuer issuerForm,
			BindingResult bindingresult, HttpServletRequest request) throws ServiceException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		String issuerName="";
		String searchType="";
			
			List<IssuerDTO> issuserList = null;
			mav.setViewName(ISSUER_CONFIG);
			mav.addObject(DELETE_BOX, new Issuer());
			searchType = request.getParameter(SRCH_TYPE);
			mav.addObject(SEARCH_TYPE, searchType);
			if (bindingresult.hasErrors()) {
				return mav;
			}
			issuerName = issuerForm.getIssuerName();
			logger.info("Issuer Name: " + issuerName);
			logger.debug("Before calling issuerservice.getIssuersByName()");
			issuserList = issuerService.getIssuersByName(issuerName);
			logger.debug("after calling issuerservice.getIssuersByName()");
			mav.addObject(ISSUER_FORM, new Issuer());
			mav.setViewName(ISSUER_CONFIG);
			mav.addObject(ISSUER_TABLE_LIST, issuserList);
			mav.addObject(ISSUER_FORM, issuerForm);
			mav.addObject(SHOW_GRID, "true");

		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_ISSUER')")
	@RequestMapping("/showEditIssuer")
	public ModelAndView showEditIssuer(Map<String, Object> model, HttpServletRequest request)
			throws ServiceException {

		logger.debug(ENTER);
		long issuerId=0;
		String id="";
		Issuer issuer =null;
		ModelAndView mav = new ModelAndView();
		try {
			
			id = request.getParameter(ISSUER_ID);
			issuerId = Long.parseLong(id);
			mav.setViewName(EDIT_ISSUER);
			logger.info("Issuer id :: "+issuerId);
			logger.debug("Before Calling issuerservice.getIssuerById() ");
			issuer = issuerService.getIssuerById(issuerId);
			logger.debug("After Calling issuerservice.getIssuerById() ");
			mav.addObject(ISSUER_FORM, issuer);
			mav.addObject(EDIT_ISSUERID, id);
			mav.setViewName(EDIT_ISSUER);
			mav.addObject(CONFIRM_BOX, new Issuer());
			

		} catch (NumberFormatException e) {
			mav.setViewName(EDIT_ISSUER);
			logger.error("NumberFormatException Occured While Editing Issuer in editIssuer()"+e);       
			 }
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_ISSUER')")
	@RequestMapping("/editIssuer")
	public ModelAndView updateIssuer(Map<String, Object> model,
			@Validated({ Issuer.ValidationStepOne.class,
					Issuer.ValidationStepTwo.class }) @ModelAttribute(ISSUER_FORM) Issuer issuerForm,
			BindingResult bindingresult, HttpServletRequest request) throws ServiceException {
		
		logger.debug(ENTER);
		long issuerId = 0;
		String issuerName="";
		String iDescription="";
		String iMdmId = "";
		String message = "";
		long insuser=0;
		String flag = "";
		IssuerDTO issuerdto = new IssuerDTO();
		ResponseDTO responseDTO = null;
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();


	if (request.getParameter(EDIT_ISSUERID) != null && 
			!request.getParameter(EDIT_ISSUERID).equalsIgnoreCase("")) {
				issuerId = Long.parseLong(request.getParameter(EDIT_ISSUERID));
			}
			if (bindingresult.hasErrors()) {
				mav.setViewName(EDIT_ISSUER);
				mav.addObject(EDIT_ISSUERID, issuerId);
				mav.addObject(CONFIRM_BOX, new Issuer());
				return mav;
			}
			mav.addObject(EDIT_ISSUERID, issuerId);
			mav.addObject(CONFIRM_BOX, new Issuer());
			
			issuerName = issuerForm.getIssuerName();			
			insuser= (Long) session.getAttribute(INS_USER);
			
			if (issuerForm.getDescription() == null) {
				iDescription = "";
			} else {
				iDescription = issuerForm.getDescription();
			}

			if (issuerForm.getMdmId() == null) {
				iMdmId = "";
			} else {
				iMdmId = issuerForm.getMdmId();
			}

			flag = "Y";
				
			logger.info("issuer  details :: Issuer Name: " + issuerName + " MDMID: " + iMdmId + " Active: " + flag
					+ " Description: " + iDescription + "  insuser "+insuser);

			issuerdto.setIssuerId(issuerId);
			issuerdto.setIssuerName(issuerName);
			issuerdto.setMdmId(iMdmId);
			issuerdto.setInsUser(insuser);
			issuerdto.setIsActive(flag);
			issuerdto.setInsDate(new Date());
			issuerdto.setLastUpdDate(new Date());
			issuerdto.setLastUpdUser(insuser);
			issuerdto.setDescription(iDescription);
			
			logger.debug("Before Calling issuerservice.getIssuerById() ");
			Issuer issuer  = issuerService.getIssuerById(issuerId);
			
			if (issuer==null ||issuer.getIssuerCode().equalsIgnoreCase("") || issuer.getIssuerCode().equalsIgnoreCase("0")) {
				message=ResourceBundleUtil.getMessage("ISS_"+ResponseMessages.DOESNOT_EXISTS);
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(EDIT_ISSUER);
				mav.addObject(ISSUER_FORM, issuerForm);
			}
			else
			{
			logger.debug("After Calling issuerservice.getIssuerById() ");
			
			
			logger.debug("Before Calling issuerservice.updateIssuer() ");
			responseDTO = issuerService.updateIssuer(issuerdto);
			logger.debug("After Calling issuerservice.updateIssuer() ");
			logger.info("Message from  response "+responseDTO.getMessage());
			Object data = responseDTO.getData();
			if(data!=null) {
			Map<String, Object> obj = (Map<String, Object>) data;

			issuerName = (String) obj.get("issuerName");
			/**iCode = String.valueOf(obj.get("issuerId"));*/
			/**
			if (obj.get("description") != null) {
				iDescription = String.valueOf(obj.get("description"));
			} else {
				iDescription = "";
			}
			if (obj.get("mdmId") != null) {
				iMdmId = String.valueOf(obj.get("mdmId"));
			} else {
				iMdmId = "";
			}*/
			/** iActive = String.valueOf(obj.get("is_Active"));*/
			
			mav.addObject(ISSUER_FORM, new IssuerDTO());
			}
			if (responseDTO.getCode().equalsIgnoreCase("000")) { 
				message=responseDTO.getMessage();
				mav.addObject(SUCCESS_STATUS, message);
				mav.setViewName(ISSUER_CONFIG);
				mav.addObject(DELETE_BOX, new Issuer());
			}	
			else if (responseDTO.getCode().equalsIgnoreCase("ISS_001")) { 
				message=ResourceBundleUtil.getMessage("ISS_"+ResponseMessages.ALREADY_EXISTS); 
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(EDIT_ISSUER);
			}
            else if (responseDTO.getCode().equalsIgnoreCase("ISS_002")) {
				message=ResourceBundleUtil.getMessage("ISS_"+ResponseMessages.DOESNOT_EXISTS);
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(EDIT_ISSUER);
			}
			else {
				
				message=ResourceBundleUtil.getMessage(ResponseMessages.FAILURE) + issuerName;
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(EDIT_ISSUER);
				mav.addObject(DELETE_BOX, new Issuer());
			}
	}
		logger.debug("EXIT");
		
		return mav;
	}

	@PreAuthorize("hasRole('DELETE_ISSUER')")
	@RequestMapping("/deleteIssuer")
	public ModelAndView deleteIssuer(Map<String, Object> model, HttpServletRequest request)
			throws ServiceException, IssuerException {

		logger.debug(ENTER);
		List<IssuerDTO> issuerList = null;
		IssuerDTO issdtoobj = new IssuerDTO();
		ResponseDTO responseDTO = null;
		String id ="";
		String searchedName="";
		String deletedName="";
		String searchType="";
		String message="";
		Issuer iss = new Issuer();
		ModelAndView mav = new ModelAndView();
		long issuerId =0;
	
			mav.setViewName(ISSUER_CONFIG);
			 id = request.getParameter(ISSUER_ID);
			 searchedName = request.getParameter("searchedName");
			 if(request.getParameter("deletedName")!=null)
			 {
			 deletedName=request.getParameter("deletedName");
			 }
			 logger.info("id and SearchedName : "+id +"  "+searchedName);
			if (searchedName == null || searchedName.isEmpty()) {
				searchedName = request.getParameter("retrievedName");
			}
			mav.addObject("SearchedName", searchedName);
			searchType = request.getParameter(SRCH_TYPE);
			mav.addObject(SEARCH_TYPE, searchType);
			logger.info("SearchType : "+searchType);
         	 issuerId = Long.valueOf(id);
			issdtoobj.setIssuerId(issuerId);
			logger.debug("Before Calling issuerservice.getIssuerCount()");
			responseDTO = issuerService.getCardRangeCount(issuerId);
			logger.debug("After Calling issuerservice.getIssuerCount()");
			logger.info("Response message "+responseDTO.getMessage());
			if(responseDTO.getCode().equalsIgnoreCase("ISS_001")) {
				mav.addObject(FAIL_STATUS, responseDTO.getMessage());
			}
		else {
				logger.debug("Before Calling issuerservice.deleteIssuer()");
				responseDTO = issuerService.deleteIssuer(issuerId);
				logger.debug("After Calling issuerservice.deleteIssuer()");

				if (responseDTO.getCode().equalsIgnoreCase("000")) { 
					message="ISSUER "+"\""+deletedName+"\" "
							+ ResourceBundleUtil.getMessage("ISS_DELETE_"+responseDTO.getCode());
					mav.addObject(SUCCESS_STATUS, message);
					mav.setViewName(ISSUER_CONFIG);
					mav.addObject(DELETE_BOX, new Issuer());
				}	
				else
				{
					message=responseDTO.getMessage();
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(ISSUER_CONFIG);
					mav.addObject(DELETE_BOX, new Issuer());
				}
			
				
				
			}

			/**
			 * displaying remaining things after delete starts here 
			 */

			if (searchType != null && searchedName!=null && 
					!searchedName.isEmpty() && searchType.equalsIgnoreCase("byName")) {
				logger.info("Before Calling issuerservice.getIssuersByName() with name "+searchedName);
				issuerList = issuerService.getIssuersByName(searchedName);
				logger.debug("After Calling issuerservice.getIssuersByName()");
			} else {
				logger.debug("Before Calling issuerservice.getAllIssuers()");
				issuerList = issuerService.getAllIssuers();
				logger.debug("After Calling issuerservice.getAllIssuers()");
			}

			mav.addObject(ISSUER_TABLE_LIST, issuerList);
			mav.addObject(SHOW_GRID, "true");

			

			logger.info("Response Message "+responseDTO.getMessage());
			iss.setIssuerName(searchedName);
			mav.addObject(ISSUER_FORM, new IssuerDTO());
			mav.addObject(DELETE_BOX, new Issuer());

		logger.debug("EXIT");
		return mav;
	}
	/**
	 * View Issuer
	 */
	
	@PreAuthorize("hasRole('VIEW_ISSUER')")
	@RequestMapping("/showViewIssuer")
	public ModelAndView showViewIssuer(Map<String, Object> model, HttpServletRequest request)
			throws ServiceException {

		logger.debug(ENTER);
		long issuerId=0;
		String id="";
		Issuer issuer =null;
		ModelAndView mav = new ModelAndView();
		try {
			
			id = request.getParameter(ISSUER_ID);
			issuerId = Long.parseLong(id);
			mav.setViewName(VIEW_ISSUER);
			logger.info("Issuer id :: "+issuerId);
			logger.debug("Before Calling issuerservice.getIssuerById() for view Issuer ");
			issuer = issuerService.getIssuerById(issuerId);
			logger.debug("After Calling issuerservice.getIssuerById() for view Issuer ");
			mav.addObject(ISSUER_FORM, issuer);
			mav.addObject(EDIT_ISSUERID, id);
			mav.setViewName(VIEW_ISSUER);
			mav.addObject(CONFIRM_BOX, new Issuer());
			

		} catch (NumberFormatException e) {
			mav.setViewName(VIEW_ISSUER);
			logger.error("Exception Occured While Viewing Issuer in view Issuer()"+e);       
			 }
		logger.debug(EXIT);
		return mav;
	}
	
	
	/**
	 *  Issuer Methods Ends */

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {

		logger.info("exceptionHandler Method Starts Here");
		String errMessage = ResponseMessages.SERVER_DOWN;
		ModelAndView mav = new ModelAndView();
		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		mav.setViewName("serviceError");
		mav.addObject(FAIL_STATUS, errMessage);
		
		logger.info("exceptionHandler Method Ends Here");
		return mav;

	}


}
