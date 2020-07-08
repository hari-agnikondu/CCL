package com.incomm.cclpvms.admin.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.admin.model.ClpUser;
import com.incomm.cclpvms.admin.model.ClpUserDTO;
import com.incomm.cclpvms.admin.model.GroupDTO;
import com.incomm.cclpvms.admin.service.GroupService;
import com.incomm.cclpvms.admin.service.UserService;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.validator.ValidateUser;
import com.incomm.cclpvms.config.validator.ValidateUserEdit;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.security.LdapService;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/user")
public class UserController {	

	private static final Logger logger = LogManager.getLogger(UserController.class);
	

	@Autowired
	public GroupService groupService;
	
	@Autowired
	public UserService userService;
	
	@Autowired
	LdapService ldapService;
	
	@Autowired
	public ValidateUser customValidatorUser;
	
	@Autowired
	public ValidateUserEdit customValidatorUserEdit;
	
	
	@Autowired
	SessionService sessionService;
	
	@Value("${INS_USER}")
	public long insUser;
	
	
	
	public static final String USER_FORM="userForm";
	public static final String USER_CONFIG="userConfig";
	public static final String GROUP_LIST="groupList";
	public static final String ADD_USER="addUser";
	public static final String FAIL_STATUS="failstatus";
	public static final String SUCCESS_STATUS="successstatus";
	public static final String SELECTED_GROUP_NAMES="selectedGroupNames";
	public static final String USER_ID="userId";
	public static final String USERID= "userID";
	public static final String CHECKER_REMARKS="checkerRemarks";
	public static final String USER_NAME="userName";
	public static final String EDIT_USER="editUser";
	public static final String EDIT_USER_ID="editUserId";
	public static final String VIEW_USER="viewUser";
	
	@PreAuthorize("hasRole('SEARCH_USER')")
	@RequestMapping("/userConfig")
	public ModelAndView userConfiguration(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
	
		logger.debug(CCLPConstants.ENTER);
		HttpSession session = request.getSession();
		session.setAttribute(CCLPConstants.INSUER, insUser);
		ModelAndView mav = new ModelAndView();
		mav.addObject(USER_FORM , new ClpUser());
		mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
		mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
		mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
		mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
		
		mav.setViewName(USER_CONFIG);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	
	@PreAuthorize("hasRole('ADD_USER')")
	@RequestMapping("/showAddUser")
	public ModelAndView showAddUser(Map<String, Object> model) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<GroupDTO> groupList = null;
		ClpUser clpuser= new ClpUser();
		
		groupList=userService.getAllGroups();
		
		mav.addObject(GROUP_LIST,groupList);
		mav.addObject(USER_FORM, clpuser);
		mav.setViewName(ADD_USER);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	@RequestMapping("/getUserDetails")
	public ModelAndView getUserDetails(Map<String, Object> model, 
			@ModelAttribute(USER_FORM) ClpUser userForm
			) throws  Exception {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<GroupDTO> groupList = null;
		String ldapId="";
		String message="";
		ClpUser clpuser= new ClpUser();
		
		groupList=userService.getAllGroups();
		
		mav.addObject(GROUP_LIST,groupList);
		
		ldapId=userForm.getUserLoginId();
		
		ClpUserDTO userDto =ldapService.validateUser(ldapId);
		
		if(userDto!=null && !("".equals(userDto.getUserLoginId())))
		{
		clpuser.setUserName(userDto.getUserName());
		clpuser.setUserLoginId(ldapId);
		clpuser.setUserEmail(userDto.getUserEmail());
		clpuser.setUserContactNumber(userDto.getUserContactNumber());
		}
		else
		{
			message=ResourceBundleUtil.getMessage(ResponseMessages.USER_NOT_FOUND);			
			mav.addObject(FAIL_STATUS, message);
		}
		
		mav.addObject(USER_FORM, clpuser);
		mav.addObject("flag",true);
		mav.setViewName(ADD_USER);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	
	@PreAuthorize("hasRole('ADD_USER')")
	@RequestMapping("/addUser")
	public ModelAndView addUser(Map<String, Object> model,
		 @ModelAttribute(USER_FORM) ClpUser userForm,
			BindingResult bindingResult,HttpServletRequest request)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		ClpUserDTO clpuser = new ClpUserDTO();

		try {

			/** List<String> selectedGroupNames= new ArrayList<String>(); */
			/** String flag = ""; */
			ResponseDTO responseDTO = null;
			String userName = "";
			long insuser = sessionService.getUserId();
			String message = "";

			/** List<GroupDTO> selGroupDTO = new ArrayList<GroupDTO>(); */

			List<String> selGroupNames = new ArrayList<>();

			List<String> selectedGroupNames = userForm.getGroupNames();

			/** selGroupDTO=userForm.getGroups(); */

			if (Util.isEmpty(userForm.getUserName())) {
				userName = "";
			} else {
				userName = userForm.getUserName().trim();
			}

			/** flag = "Y"; */

			String userLoginId = "";
			String emailID = "";
			Long phone = null;
			String status = "NEW";
			List<String> groupNames = new ArrayList<>();

			List<GroupDTO> groups = new ArrayList<>();
			List<GroupDTO> groupList = null;

			/** List<Long> groupIds= new ArrayList<>(); */

			String ldapId = "";

			groupList = userService.getAllGroups();

			List<GroupDTO> selectedGroups = new ArrayList();

			List<GroupDTO> selectedGroupsList = new LinkedList<>();
			for (Iterator<GroupDTO> iterator = groupList.iterator(); iterator.hasNext();) {
				Object groupDataDTO = iterator.next();
				if (groupList.size() == selectedGroupNames.size()) {
					break;
				}

				GroupDTO groupData = new GroupDTO();

				HashMap hashMap = (HashMap) groupDataDTO;
				if (hashMap.get("groupId") != null && hashMap.get("groupName") != null) {
					String idstr = String.valueOf(hashMap.get("groupId"));
					String name = String.valueOf(hashMap.get("groupName"));
					Long id = Long.parseLong(idstr);
					groupData.setGroupId(id);
					groupData.setGroupName(name);

					if (selectedGroupNames.contains(String.valueOf(groupData.getGroupId()))) {
						selectedGroupsList.add(groupData);
					}
				}

			}

			mav.addObject(SELECTED_GROUP_NAMES, selectedGroupsList);

			ldapId = userForm.getUserLoginId();

			ClpUserDTO userDto = ldapService.validateUser(ldapId);

			if (userDto.getUserLoginId() == null || "".equals(userDto.getUserLoginId())) {

				userForm.setGroupNames(selGroupNames);
				userForm.setUserName(userDto.getUserName());
				mav.addObject(GROUP_LIST, groupList);

				mav.addObject(SELECTED_GROUP_NAMES, selectedGroupsList);

				mav.addObject(USER_FORM, userForm);

				message = ResourceBundleUtil.getMessage(ResponseMessages.USER_NOT_FOUND);
				mav.addObject(FAIL_STATUS, message);

				mav.setViewName(ADD_USER);

				return mav;
			}
			customValidatorUser.validate(userForm, bindingResult);
			if (bindingResult.hasErrors()) {

				mav.addObject(GROUP_LIST, groupList);
				mav.addObject(SELECTED_GROUP_NAMES, selectedGroups);
				mav.addObject(USER_FORM, userForm);
				mav.setViewName(ADD_USER);
				return mav;
			}

			clpuser.setUserName(userName);
			userLoginId = userForm.getUserLoginId();
			emailID = userForm.getUserEmail();
			phone = userForm.getUserContactNumber();
			groupNames = userForm.getGroupNames();

			for (String groupId : groupNames) {
				GroupDTO groupData = new GroupDTO();
				groupData.setGroupId(Long.parseLong(groupId));
				groups.add(groupData);
			}

			ClpUserDTO clpuserAdd = new ClpUserDTO(null, userLoginId, null, userName, emailID, phone, status, null,
					insuser, new Date(), insuser, new Date(), groups);

			logger.info(" User  details :: User Name: " + userName);

			logger.debug("Before Calling userservice.createUser");
			responseDTO = userService.createUser(clpuserAdd);
			logger.debug("after Calling userservice.createUser");
			logger.info("Message from createUser method: " + responseDTO.getMessage());

			logger.debug("CCLP-VMS Response Code : " + responseDTO.getCode() + " Error Message : "
					+ responseDTO.getMessage());
			if (responseDTO.getCode().equalsIgnoreCase("000")) {
				message = responseDTO.getMessage();
				mav.addObject(SUCCESS_STATUS, message);
				mav.setViewName(USER_CONFIG);
				mav.addObject(USER_FORM, new ClpUserDTO());
				mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
				mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
				mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
				mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());

			} else {

				message = responseDTO.getMessage();
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(ADD_USER);
				mav.addObject(USER_FORM, clpuserAdd);
				mav.addObject(SELECTED_GROUP_NAMES, selectedGroupsList);
				mav.addObject(GROUP_LIST, userService.getAllGroups());
			}

		} catch (Exception e) {
			logger.error("Exception Occured " + e);
		}

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_USER')")
	@RequestMapping("/searchUserByName")
	public ModelAndView searchUserByName(Map<String, Object> model,
			  @ModelAttribute(USER_FORM) ClpUser userForm,
			BindingResult bindingresult, HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		String userName="";
		String searchType="";
			
			List<ClpUserDTO> userList = null;
			mav.setViewName(USER_CONFIG);
			mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
			searchType = request.getParameter("searchType");
			mav.addObject("SearchType", searchType);
			
			userName = userForm.getUserName();
			logger.info("userName : " + userName);
			logger.debug("Before calling userservice.getUsersByName()");
			userList = userService.getUsersByName(userName);
			logger.debug("after calling userservice.getUsersByName()");
			mav.addObject(USER_FORM, new ClpUser());
			mav.setViewName(USER_CONFIG);
			mav.addObject("userTableList", userList);
			mav.addObject(USER_FORM, userForm);
			mav.addObject("showGrid", "true");
			mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
			mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
			
			mav.addObject("loginUserId", sessionService.getUserId());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_USER')")
	@RequestMapping("/showAllUsers")
	public ModelAndView showAllUsers(Map<String, Object> model,
			@ModelAttribute("userForm") ClpUser userForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<ClpUserDTO> userList = null;
		
			 mav.setViewName(USER_CONFIG);
			
		
			logger.debug("Before calling userService.getAllUsers()");
			
			userList = userService.getAllUsers(userForm);
						
			logger.debug("after calling userService.getAllUsers()");
			
			mav.addObject(USER_FORM, new ClpUser());
			mav.setViewName(USER_CONFIG);
			mav.addObject("userTableList", userList);
			mav.addObject(USER_FORM, userForm);
			mav.addObject("showGrid", "true");
			mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
			mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
			mav.addObject("loginUserId", sessionService.getUserId());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	
	
	@PreAuthorize("hasRole('EDIT_USER')")
	@RequestMapping("/showEditUser")
	public ModelAndView showEditUser(Map<String, Object> model,HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<GroupDTO> groupList = null;
		
		Long userId=null;
		
		if(request.getParameter(USER_ID)!=null)
		{
		userId=Long.parseLong(request.getParameter(USER_ID));
		}
		/** ClpUser clpuser= new ClpUser();*/
		
		groupList=userService.getAllGroups();
		
		ClpUser clpuser=userService.getUserByUserId(userId);
		
		
		mav.addObject(GROUP_LIST,groupList);
		mav.addObject(USERID,userId);
		mav.addObject(USER_FORM, clpuser);
		mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
		mav.setViewName(EDIT_USER);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	@PreAuthorize("hasRole('EDIT_USER')")
	@RequestMapping("/editUser")
	public ModelAndView editUser(Map<String, Object> model,@ModelAttribute("userForm") ClpUser userForm,HttpServletRequest request,BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<GroupDTO> groupList = null;
		Long userId=null;
		String userLoginId="";
		String emailID="";
		Long phone=null;
		String status="EDIT";
		String userName="";
		String message="";
		long updatedUser = sessionService.getUserId();
		List<String> groupNames=new ArrayList<>();  
		
		List<GroupDTO> groups=new ArrayList<>();
	
		ResponseDTO responseDTO = new ResponseDTO();

		ClpUser clpuser= new ClpUser();
		
		groupList=userService.getAllGroups();
		customValidatorUserEdit.validate(userForm, bindingResult);
		if(bindingResult.hasErrors()) {
			
			groupList=userService.getAllGroups();
			
			mav.addObject(GROUP_LIST,groupList);
			mav.addObject(USERID, userForm.getUserId());
			mav.addObject(EDIT_USER_ID, userForm.getUserId());
			mav.addObject(USER_FORM, userForm);
			mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
			
			mav.setViewName(EDIT_USER);
			
			return mav;
		}
		

		if(request.getParameter(USER_ID)!=null)
		{
		userId=Long.parseLong(request.getParameter(USER_ID));
		}
		
	
		userName=userForm.getUserName();
		userLoginId=userForm.getUserLoginId();
		emailID=userForm.getUserEmail();
		phone=userForm.getUserContactNumber();
		groupNames=userForm.getGroupNames();

		
		for (String groupId: groupNames) {
            GroupDTO groupData= new GroupDTO();
            groupData.setGroupId(Long.parseLong(groupId));
            groups.add(groupData);
        }
		
			
		ClpUserDTO clpuserEdit = new 
				ClpUserDTO(userId, userLoginId, null, userName,  emailID, phone, status,
						null, updatedUser, new Date(), updatedUser, new Date(), groups);
	
		
		responseDTO=userService.updateUser(clpuserEdit);
		
		if (responseDTO.getCode().equalsIgnoreCase("000")) { 
			message=responseDTO.getMessage();
			mav.addObject(SUCCESS_STATUS, message);
			mav.setViewName(USER_CONFIG);
			mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
			mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
		}	
		
		
		else if(responseDTO.getCode().equalsIgnoreCase("002"))
		{
			message=responseDTO.getMessage() +" for " + userName;
			mav.addObject(FAIL_STATUS, message);
			mav.setViewName(USER_CONFIG);
			mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
			mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
			
			mav.addObject(USER_ID, userForm.getUserId());
			mav.addObject(EDIT_USER_ID, userForm.getUserId());
			
		}
		
		else
		{
			message=ResourceBundleUtil.getMessage(ResponseMessages.FAILURE) + userName;
			mav.addObject(FAIL_STATUS, message);
			mav.addObject(USER_FORM, userForm);
			mav.setViewName(EDIT_USER);
			mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
			mav.addObject(USER_ID, userForm.getUserId());
			mav.addObject(EDIT_USER_ID, userForm.getUserId());
		}
		
		
		mav.addObject(USER_FORM, clpuser);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	
	
	/**
     * This Method calls when user tries to approve/Reject the user from the search page.
     */
	@PreAuthorize("hasRole('APPROVE_USER')")
    @RequestMapping("/approveUser")
    public ModelAndView approveUser(@ModelAttribute("approveBox") ClpUser clpUser,
    		HttpServletRequest req)  throws ServiceException {
        logger.debug(CCLPConstants.ENTER);
        Long userId=null;
        ModelAndView mav=new ModelAndView();
        Long insuser=null;
    
        HttpSession session = req.getSession();
        
        if(session.getAttribute(CCLPConstants.INSUER)!=null)
        {
        	insuser=Long.parseLong(session.getAttribute(CCLPConstants.INSUER).toString());
        clpUser.setInsUser(insuser);
        }
        
        if(req.getParameter(CCLPConstants.USER_ID)!=null)
        {
        userId=Long.parseLong(req.getParameter(CCLPConstants.USER_ID));
        clpUser.setUserId(userId);
        }

        if(null!=req.getParameter(USER_NAME))
        {
            clpUser.setUserName(req.getParameter(USER_NAME));
        }
 
        if(null!=req.getParameter(CHECKER_REMARKS))
        {
            clpUser.setCheckerRemarks(req.getParameter(CHECKER_REMARKS));
        }
        
    
 
        ResponseEntity<ResponseDTO> responseDTO=userService.approveUser(clpUser);
        if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
        	
        	logger.info("Success approve user"+responseDTO.getBody().getCode());
            mav.addObject(USER_FORM, new ClpUser());
            mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
            mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
            mav.addObject(SUCCESS_STATUS,responseDTO.getBody().getMessage()); 
            mav.setViewName(USER_CONFIG); 
        }
        else{
            mav.addObject(USER_FORM, new ClpUser());
            mav.addObject(FAIL_STATUS,responseDTO.getBody().getMessage());
            logger.info("Exit Approve User",responseDTO.getBody().getMessage());
            mav.setViewName(EDIT_USER);
            mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
            mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
            mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
        }
        logger.debug(CCLPConstants.EXIT);
        return mav;
    }

	
	
    /**
     * This Method calls when user tries to Reject the user from the search page.
     */
	@PreAuthorize("hasRole('APPROVE_USER')")
    @RequestMapping("/rejectUser")
    public ModelAndView rejectUser(@ModelAttribute("rejectBox") ClpUser clpUser,
    		HttpServletRequest req)  throws ServiceException {
        logger.debug(CCLPConstants.ENTER);
        Long userId=null;
        ModelAndView mav=new ModelAndView();
        Long insuser=null;
    
        HttpSession session = req.getSession();
        logger.info("Entered in to reject User");
        
        if(session.getAttribute(CCLPConstants.INSUER)!=null)
        {
        	insuser=Long.parseLong(session.getAttribute(CCLPConstants.INSUER).toString());
        clpUser.setInsUser(insuser);
        }
        
        if(req.getParameter(CCLPConstants.USER_ID)!=null)
        {
        userId=Long.parseLong(req.getParameter(CCLPConstants.USER_ID));
        clpUser.setUserId(userId);
        }

        if(null!=req.getParameter(USER_NAME))
        {
            clpUser.setUserName(req.getParameter(USER_NAME));
        }
 
        if(null!=req.getParameter(CHECKER_REMARKS))
        {
            clpUser.setCheckerRemarks(req.getParameter(CHECKER_REMARKS));
        }
        
    
 
        ResponseEntity<ResponseDTO> responseDTO=userService.approveUser(clpUser);
        if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
            mav.addObject(USER_FORM, new ClpUser());
            mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
            mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
            mav.addObject(SUCCESS_STATUS,responseDTO.getBody().getMessage()); 
            logger.info("Success of Reject user"+responseDTO.getBody().getMessage());
            mav.setViewName(USER_CONFIG); 
        }
        else{
            mav.addObject(USER_FORM, new ClpUser());
            mav.addObject(FAIL_STATUS,responseDTO.getBody().getMessage());
            mav.setViewName(EDIT_USER);
            mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
            mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
            mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
        }
        logger.debug(CCLPConstants.EXIT);
        return mav;
    }

	
	
	
	
	
    /**
     * This Method calls when user tries to Reject the user from the search page.
     */
    
	@PreAuthorize("hasRole('DELETE_USER')")
    @RequestMapping("/deleteUser")
    public ModelAndView deleteUser(@ModelAttribute("deleteBox") ClpUser clpUser,
    		HttpServletRequest req)  throws ServiceException {
        logger.debug(CCLPConstants.ENTER);
        Long userId=null;
        ModelAndView mav=new ModelAndView();
        Long insuser=null;
    
        HttpSession session = req.getSession();
        
        if(session.getAttribute("insUser")!=null)
        {
        	insuser=Long.parseLong(session.getAttribute("insUser").toString());
        clpUser.setInsUser(insuser);
        }
        
        if(req.getParameter(USER_ID)!=null)
        {
        userId=Long.parseLong(req.getParameter(USER_ID));
        clpUser.setUserId(userId);
        }

        if(null!=req.getParameter(USER_NAME))
        {
            clpUser.setUserName(req.getParameter(USER_NAME));
        }
 
        if(null!=req.getParameter(CHECKER_REMARKS))
        {
            clpUser.setCheckerRemarks(req.getParameter(CHECKER_REMARKS));
        }
        logger.info("clpUser",clpUser);
    
 
        ResponseEntity<ResponseDTO> responseDTO=userService.deleteUser(clpUser);
        if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
            mav.addObject(USER_FORM, new ClpUser());
            mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
            mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
            mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
            mav.addObject(SUCCESS_STATUS,responseDTO.getBody().getMessage()); 
            logger.info("SUCCESS_STATUS",responseDTO.getBody().getMessage());
            mav.setViewName(USER_CONFIG); 
        }
        else if(responseDTO.getBody().getResult().equalsIgnoreCase("USER012")) {
        	mav.addObject(USER_FORM, new ClpUser());
            mav.addObject(FAIL_STATUS,responseDTO.getBody().getMessage());
            logger.info("FAIL_STATUS",responseDTO.getBody().getMessage());
            mav.setViewName(USER_CONFIG);
            mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
            mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
            mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
        }
        else{
            mav.addObject(USER_FORM, new ClpUser());
            mav.addObject(FAIL_STATUS,responseDTO.getBody().getMessage());
            logger.info("entered in to fail status of delete user"+responseDTO.getBody().getMessage());
            
            mav.setViewName(USER_CONFIG);
            mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
            mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
            mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
            mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
            
        }
        logger.debug(CCLPConstants.EXIT);
        return mav;
    }
	
	/**
	 * View USER
	 * @param exception
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW_USER')")
	@RequestMapping("/showViewUser")
	public ModelAndView showViewUser(Map<String, Object> model,HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<GroupDTO> groupList = null;
		
		Long userId=null;
		
		if(request.getParameter(USER_ID)!=null)
		{
		userId=Long.parseLong(request.getParameter(USER_ID));
		}
		
		groupList=userService.getAllGroups();
		
		ClpUser clpuser=userService.getUserByUserId(userId);
		
		
		mav.addObject(GROUP_LIST,groupList);
		logger.info("groupList in view Screen"+groupList);
		mav.addObject(USERID,userId);
		mav.addObject(USER_FORM, clpuser);
		mav.addObject(CCLPConstants.CONFIRM_BOX, new ClpUser());
		
		mav.setViewName(VIEW_USER);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug("Exception for user starts here");
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);
		
		logger.info("Error Message in Exception Handler",errMessage);

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("statusMessage", errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug("exception for user ends here");

		return mav;
	}
	

	/**
     * This Method calls when user tries to approve/Reject the user from the search page.
     */
	
	@RequestMapping("/updateAccessStatus")
	public ModelAndView deactiveUser(@ModelAttribute("accessStatusBox") ClpUser clpUser, HttpServletRequest req)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		Long userId = null;
		ModelAndView mav = new ModelAndView();
		Long insuser = null;

		HttpSession session = req.getSession();

		if (session.getAttribute(CCLPConstants.INSUER) != null) {
			insuser = Long.parseLong(session.getAttribute(CCLPConstants.INSUER).toString());
			clpUser.setInsUser(insuser);
		}

		if (req.getParameter(CCLPConstants.USER_ID) != null) {
			userId = Long.parseLong(req.getParameter(CCLPConstants.USER_ID));
			clpUser.setUserId(userId);
		}

		if (null != req.getParameter(USER_NAME))
			clpUser.setUserName(req.getParameter(USER_NAME));

		if (null != req.getParameter(CHECKER_REMARKS))
			clpUser.setCheckerRemarks(req.getParameter(CHECKER_REMARKS));

		if (null != req.getParameter(CHECKER_REMARKS))
			clpUser.setUserStatus(req.getParameter("userStatus"));

		if (null != req.getParameter(CHECKER_REMARKS))
			clpUser.setUserLoginId(req.getParameter("userLoginId"));

		ResponseEntity<ResponseDTO> responseDTO = userService.updateAccessStatus(clpUser);
		if (ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())) {

			logger.info("Success updating access status of user" + responseDTO.getBody().getCode());
			mav.addObject(USER_FORM, new ClpUser());
			mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
			mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
			mav.addObject(SUCCESS_STATUS, responseDTO.getBody().getMessage());
			mav.setViewName(USER_CONFIG);
		} else {
			logger.info("failed to update access status of user" + responseDTO.getBody().getCode());
			mav.addObject(USER_FORM, new ClpUser());
			mav.addObject(CCLPConstants.DELETE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.APPROVE_BOX, new ClpUser());
			mav.addObject(CCLPConstants.REJECT_BOX, new ClpUser());
			mav.addObject(CCLPConstants.ACCESS_STATUS_BOX, new ClpUser());
			mav.addObject(FAIL_STATUS, responseDTO.getBody().getMessage());
			mav.setViewName(USER_CONFIG);
			
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	
}
