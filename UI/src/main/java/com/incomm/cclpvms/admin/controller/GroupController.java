package com.incomm.cclpvms.admin.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.admin.model.Group;
import com.incomm.cclpvms.admin.model.GroupDTO;
import com.incomm.cclpvms.admin.model.RoleDTO;
import com.incomm.cclpvms.admin.service.GroupService;
import com.incomm.cclpvms.admin.validator.ValidationAdminImpl;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.CardRange.ValidateSearch;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.Util;


@Controller
@RequestMapping("/admin/group")
public class GroupController {
	Logger logger = LogManager.getLogger(GroupController.class);
	
	private static final String GROUP_CONFIG = "groupConfig";
	private static final String GROUP_SEARCH = "groupSearch";
	private static final String ROLELIST = "RoleList";
	private static final String ADDGROUP = "addGroup";
	private static final String GROUPNAMEMAP = "groupNameMap";
	private static final String SELECTEDROLELIST = "selectedRoleList";
	private static final String UPDATEGROUP = "updateGroup";
	
	
	@Autowired
	GroupService groupService;

	@Autowired
	ValidationAdminImpl validationAdmin;
	
	@Autowired
	SessionService sessionService;
	
	/**
	 * This Methods Displays the Search page for entering Group and Role Name
	 */
	@PreAuthorize("hasRole('SEARCH_GROUP')")
	@RequestMapping("/groupConfig")	
	public ModelAndView groupConfig() {
		
		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.EXIT);
		return new ModelAndView(GROUP_CONFIG,GROUP_SEARCH, new Group());
	}

	/**
	 * This Method Search for the group based on the Group Name Entered. 
	 * An empty search will return all the Available Groups.
	 */
	@PreAuthorize("hasRole('SEARCH_GROUP')")
	@RequestMapping("/searchAndDispGroup")
	public ModelAndView searchGroup( @Validated({ValidateSearch.class}) @ModelAttribute("groupSearch") Group groupConfig, BindingResult bindingResult) throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView();
		
		if(bindingResult.hasErrors())
		{
			mav.setViewName(GROUP_CONFIG);
			return mav;
		} 
		mav.setViewName(GROUP_CONFIG);
		mav.addObject("groupList", groupService.getGroupByName(groupConfig.getGroupName()));
		mav.addObject(GROUP_SEARCH, groupConfig);
		mav.addObject("showGrid", "true");
		mav.addObject("loginUserId", sessionService.getUserId());
		logger.debug(CCLPConstants.EXIT);
		return mav;

	}
	
	/**
	 * This Method is to display the Add Group Screen
	 */
	@PreAuthorize("hasRole('ADD_GROUP')")
	@RequestMapping("/addGroup")
	public ModelAndView addGroupConfiguration( @ModelAttribute("addGroup") Group groupConfig) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView(ADDGROUP);
		mav.addObject(ROLELIST,groupService.getAllRoles());
		mav.addObject(ADDGROUP, new Group());
		logger.debug(CCLPConstants.EXIT);
		return mav;

	}
	
	/**
	 * This Method calls when user tries to edit page.
	 */
	@PreAuthorize("hasRole('EDIT_GROUP')")
	@RequestMapping("/editGroup")
	public ModelAndView editGroupConfiguration( @ModelAttribute("groupSearch") Group groupConfig,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView("editGroup");
		List<GroupDTO> groupNameList=groupService.getGroupNameList();
		if(groupNameList!=null){
			mav.addObject(GROUPNAMEMAP, groupNameList.stream().collect(Collectors.toMap(GroupDTO::getGroupId, GroupDTO::getGroupName)));
		}		
		
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		mav.addObject(ROLELIST,groupService.getAllRoles());
		mav.addObject(UPDATEGROUP, groupConfig);
		logger.debug(CCLPConstants.EXIT);
		return mav;

	}
	
	
	
	

	/**
	 * This Method calls when user tries to approve/Reject Group from the search page.
	 */
	@PreAuthorize("hasRole('APPROVE_GROUP')")
	@RequestMapping("/groupStatusUpdate")
	public ModelAndView changeGroupStatus(@ModelAttribute("groupSearch") Group groupConfig,HttpServletRequest req)  throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		
		ModelAndView mav=new ModelAndView();

		ResponseEntity<ResponseDTO> responseDTO=groupService.changeGroupStatus(groupConfig);
		if(ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())){
			mav.setViewName("forward:/admin/group/searchAndDispGroup");
			mav.addObject(GROUP_SEARCH, new Group());
			mav.addObject(CCLPConstants.STATUS,responseDTO.getBody().getMessage()); 
		}
		else{
			mav.addObject(GROUP_SEARCH,new Group());
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getBody().getMessage());
			mav.setViewName(GROUP_CONFIG);
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	/**
	 * This Method delete Group from the search page.
	 */
	
	@PreAuthorize("hasRole('DELETE_GROUP')")
	@RequestMapping("/deleteGroup")
	public ModelAndView deleteGroup(@ModelAttribute("groupSearch") Group groupConfig,HttpServletRequest req)throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		
		ModelAndView mav=new ModelAndView();

		ResponseEntity<ResponseDTO> responseDTO=groupService.deleteGroup(groupConfig.getGroupId());
		mav.setViewName("forward:/admin/group/searchAndDispGroup");
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
	 * This method used for add  the new  Group .
	 * @param groupConfig
	 * @param bindingResult
	 * @return
	 * @throws ServiceException
	 */
	@PreAuthorize("hasRole('ADD_GROUP')")
	@RequestMapping(value = "/saveGroup") 
	public ModelAndView saveGroupAccess( @ModelAttribute("addGroup") Group groupConfig,BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
			
		ModelAndView mav=new ModelAndView(ADDGROUP);	
		mav.addObject(ROLELIST,groupService.getAllRoles());
		mav.addObject(SELECTEDROLELIST,groupConfig.getSelectedRoleList());
		
		validationAdmin.validate(groupConfig, bindingResult);
		if(bindingResult.hasErrors())
		{
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}		
		ResponseDTO responseDTO=groupService.addGroup(groupConfig);
		if(ResponseMessages.SUCCESS.equals(responseDTO.getCode()))			
		{
			mav.setViewName("forward:/admin/group/groupConfig");
			mav.addObject(CCLPConstants.STATUS,responseDTO.getMessage());
		}
		else
		{
			mav.addObject(GROUP_CONFIG,groupConfig);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());			
			mav.addObject(ROLELIST,groupService.getAllRoles());
			mav.addObject(SELECTEDROLELIST,groupConfig.getSelectedRoleList());		
			mav.setViewName(ADDGROUP);
			
			List<RoleDTO> selectedRoles=new LinkedList<>();
			for (Iterator<RoleDTO> iterator = groupService.getAllRoles().iterator(); iterator
					.hasNext();) {
				RoleDTO role =  iterator.next();
				if(selectedRoles.size()==groupConfig.getSelectedRoleList().size()){
					break;
				}
				if(groupConfig.getSelectedRoleList().contains(String.valueOf(role.getRoleId()))){
					selectedRoles.add(role);
				}
			}
			mav.addObject("roleIds",selectedRoles);
						
		}
		logger.debug(CCLPConstants.EXIT);
		return mav; 


	}
/**
 *This method used for Update the Group and RoleList.
 * @throws ServiceException
 */
	@PreAuthorize("hasRole('EDIT_GROUP')")
	@RequestMapping("/updateGroup")
	public ModelAndView updateGroup( @ModelAttribute("updateGroup") Group groupConfig,HttpServletRequest req,BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView("editGroup");		
		List<GroupDTO> groupNameList=groupService.getGroupNameList();
		
		if(groupConfig.getGroupId()==null && groupConfig.getGroupId() == -1)
		{
			bindingResult.rejectValue("groupId", "messageNotEmpty.updateGroup.groupAccessName", "Please select Group Name");

		} 
		if(groupConfig.getSelectedRoleList().isEmpty()) 
		{
			bindingResult.rejectValue(SELECTEDROLELIST, "messageNotEmpty.updateGroup.selectedRoleListEmpty", "Please select Role Name");
		}
		
		if(bindingResult.hasErrors()) {
			
			if(groupNameList!=null){
				mav.addObject(GROUPNAMEMAP, groupNameList.stream().collect(Collectors.toMap(GroupDTO::getGroupId, GroupDTO::getGroupName)));
			}		

			mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
			mav.addObject(ROLELIST,groupService.getAllRoles());
			mav.addObject(UPDATEGROUP, new Group());
			logger.debug("EXIT");
			return mav;
			
		}
else {
		
		ResponseDTO responseDTO=groupService.updateGroup(groupConfig);		
		if(ResponseMessages.SUCCESS.equals(responseDTO.getCode()))
		{
			mav.setViewName("forward:/admin/group/groupConfig");
			mav.addObject(CCLPConstants.STATUS,responseDTO.getMessage());
		}
		
		else {
					
		if(groupNameList!=null){
			mav.addObject(GROUPNAMEMAP, groupNameList.stream().collect(Collectors.toMap(GroupDTO::getGroupId, GroupDTO::getGroupName)));
		}		
		mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());	

		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		mav.addObject(ROLELIST,groupService.getAllRoles());
		mav.addObject(UPDATEGROUP, new Group());
		logger.debug(CCLPConstants.EXIT);
		return mav;
		}
		return mav;

	}
		
}
	
	/**
	 * View Group screen
	 * @param exception
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW_GROUP')")
	@RequestMapping("/viewGroup")
	public ModelAndView viewGroupConfiguration( @ModelAttribute("groupSearch") Group groupConfig,HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav=new ModelAndView("viewGroup");
		List<GroupDTO> groupNameList=groupService.getGroupNameList();
		if(groupNameList!=null){
			mav.addObject(GROUPNAMEMAP, groupNameList.stream().collect(Collectors.toMap(GroupDTO::getGroupId, GroupDTO::getGroupName)));
		}
		mav.addObject(CCLPConstants.SRV_URL,Util.constructUrl(req));
		mav.addObject(ROLELIST,groupService.getAllRoles());
		mav.addObject(UPDATEGROUP, groupConfig);
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

	
}



