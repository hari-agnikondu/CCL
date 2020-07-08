package com.incomm.cclpvms.admin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.admin.model.PermissionDTO;
import com.incomm.cclpvms.admin.model.Role;
import com.incomm.cclpvms.admin.service.RoleService;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/admin/")
public class RoleController {
	
    private static final Logger logger = LogManager.getLogger(RoleController.class);
    private static final String PERMISSION_MAP="permissionMap";
    private static final String ROLE_FORM="roleForm";
    private static final String ROLE_CONFIG="roleConfig";
    private static final String SUCCESS="SUCCESS";
    private static final String FAIL="fail";
    private static final String SHOW_VIEW_ROLE="showViewRole";
    
    @Autowired
  	SessionService sessionService;
    
	@Autowired
	public RoleService roleservice;
	
	@PreAuthorize("hasRole('SEARCH_ROLE')")
	@RequestMapping("/roleConfig")
    public ModelAndView roleConfig(HttpServletRequest request,HttpServletResponse response)  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(ROLE_CONFIG);
		mav.addObject(ROLE_FORM, new Role());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_ROLE')")
	@RequestMapping("/searchRoleByName")
	public ModelAndView searchRoleByName( @ModelAttribute(ROLE_FORM) Role role, BindingResult bindingResult,HttpServletRequest request)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		List<Role> roleList=null;

		ModelAndView mav = new ModelAndView(ROLE_CONFIG);

		if (!Util.isEmpty(role.getRoleName())) {
			
			if (bindingResult.hasErrors()) {
				mav.addObject(ROLE_FORM, role);
				logger.error("Some error occured while binding the Role object");
				return mav;
			}

			logger.info("Searching roleName '{}'", role.getRoleName());
			role.setCheckerRemarks("");
			roleList = roleservice.getRoleByName(role.getRoleName());

		} else {
			role.setCheckerRemarks("");
			logger.info("There is no role name , performing full search");
			roleList = roleservice.getAllRoles();
		}
		
		mav.addObject("loginUserId", sessionService.getUserId());
		mav.addObject("showGrid", "true");
		mav.addObject("roleList", roleList);
	
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
	
	@PreAuthorize("hasRole('ADD_ROLE')")
	@RequestMapping("/showAddRole")
	public ModelAndView showAddRole() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView("showAddRole");
		mav.addObject(ROLE_FORM, new Role());
		Map<String,Map<String,Long>> map =  roleservice.getEntityPermissionMap();
		mav.addObject(PERMISSION_MAP,map);
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
	
	@PreAuthorize("hasRole('ADD_ROLE')")
	@RequestMapping("/AddRole")
	public ModelAndView addRole(@Valid @ModelAttribute(ROLE_FORM) Role roleForm, BindingResult bindingResult) throws ServiceException {
		logger.debug("ENTER "+roleForm);

	
		roleForm.setInsUser(sessionService.getUserId());
		roleForm.setLastUpdUser(sessionService.getUserId());
		ModelAndView mav = new ModelAndView("showAddRole");
		mav.addObject(ROLE_FORM, roleForm);
		if (bindingResult.hasErrors()) {
			mav.addObject(ROLE_FORM, roleForm);
			Map<String,Map<String,Long>> map =  roleservice.getEntityPermissionMap();
			mav.addObject(PERMISSION_MAP,map);
			logger.error("Some error occured while binding the role object");
			return mav;
		}
		
		ResponseDTO responseDto = roleservice.createRole(roleForm);
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase("success")) {
				logger.info("role record '{}' has been added successfully", roleForm.getRoleName());
				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(ROLE_CONFIG);
				mav.addObject(ROLE_FORM, new Role());

			} else {
				logger.error("Error while adding role");
				mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.addObject(ROLE_FORM, roleForm);
			}
		}
		Map<String,Map<String,Long>> map = roleservice.getEntityPermissionMap();
		mav.addObject(PERMISSION_MAP,map);
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	@PreAuthorize("hasRole('EDIT_ROLE')")
	@RequestMapping("/showEditRole")
	public ModelAndView showEditRole(@ModelAttribute(ROLE_FORM) Role roleForm) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView("showEditRole");
		ResponseDTO responseDTO=roleservice.getRoleByRoleId(roleForm.getRoleId());
		if(responseDTO!=null ) {
			if( responseDTO.getData()!=null && responseDTO.getResult().equalsIgnoreCase(SUCCESS)) {
				logger.debug("RoleForm fetched successfully. Response from config service is "+responseDTO);
				ObjectMapper objectMapper = new ObjectMapper();
				Role role=objectMapper.convertValue(responseDTO.getData(), Role.class);
				long[] permissionIdAry = new long[role.getPermissions().size()];
				int i=0;
				for (PermissionDTO permDto : role.getPermissions()) {
					permissionIdAry[i]=permDto.getPermissionId();
					i++;
				}
				role.setPermissionID(permissionIdAry);
				logger.debug(role);
				mav.addObject(ROLE_FORM, role);
			}else {
				mav = new ModelAndView();
				mav.setViewName(ROLE_CONFIG);
				mav.addObject(ROLE_FORM, new Role());
				mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
				mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
			}
		}
		else {
			mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
			mav.setViewName(ROLE_CONFIG);
			mav.addObject(ROLE_FORM, new Role());
			logger.error("Failed to fetch Role details. Response from config service is "+responseDTO);
		}
		
		Map<String,Map<String,Long>> map = roleservice.getEntityPermissionMap();
		mav.addObject(PERMISSION_MAP,map);
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
	
	@PreAuthorize("hasRole('EDIT_ROLE')")
	@RequestMapping("/EditRole")
	public ModelAndView editRole(@Valid @ModelAttribute(ROLE_FORM) Role roleForm, BindingResult bindingResult) throws ServiceException {
		logger.debug("ENTER "+roleForm);
	
		roleForm.setInsUser(sessionService.getUserId());
		roleForm.setLastUpdUser(sessionService.getUserId());
		ModelAndView mav = new ModelAndView("showEditRole");
		mav.addObject(ROLE_FORM, roleForm);
		if (bindingResult.hasErrors()) {
			ResponseDTO responseDTO=roleservice.getRoleByRoleId(roleForm.getRoleId());
			if(responseDTO!=null ) {
				if( responseDTO.getData()!=null && responseDTO.getResult().equalsIgnoreCase(SUCCESS)) {
					logger.debug("RoleForm fetched successfully. Response from config service is "+responseDTO);
					ObjectMapper objectMapper = new ObjectMapper();
					Role role=objectMapper.convertValue(responseDTO.getData(), Role.class);
					long[] permissionIdAry = new long[role.getPermissions().size()];
					int i=0;
					for (PermissionDTO permDto : role.getPermissions()) {
						permissionIdAry[i]=permDto.getPermissionId();
						i++;
					}
					role.setPermissionID(permissionIdAry);
					logger.debug(role);
					mav.addObject(ROLE_FORM, role);
				}
			return mav;
		  }
		}
		ResponseDTO responseDto = roleservice.updateRole(roleForm);
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase("success")) {
				logger.info("Role record '{}' has been updated successfully", roleForm.getRoleName());
				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(ROLE_CONFIG);
				mav.addObject(ROLE_FORM, new Role());

			} else {
				logger.error("Error while updating role");
				mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			}
		}
		Map<String,Map<String,Long>> map = roleservice.getEntityPermissionMap();
		mav.addObject(PERMISSION_MAP,map);
		
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('DELETE_ROLE')")
	@RequestMapping("/deleteRole")
	public ModelAndView deleteRole(HttpServletRequest request, @ModelAttribute(ROLE_FORM)Role roleForm) throws ServiceException
	{
		logger.debug(CCLPConstants.ENTER);
		List<Role> roleList=null;
		ModelAndView mav = new ModelAndView(ROLE_CONFIG);
		ResponseDTO responseDto = null;
		String searchRoleName = request.getParameter("searchRoleName");
		logger.info("searchRoleName "+searchRoleName+" Delete role from table {}", roleForm);
		responseDto = roleservice.deleteRole(roleForm.getRoleId());
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase("success")) {
				logger.info("Role record '{}' has been deleted successfully", roleForm.getRoleId());
				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			} else {
				logger.error("Failed to delete record for role '{}'", roleForm.getRoleId());
				mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			}
		}
		if (!Util.isEmpty(searchRoleName)) {
			logger.info("Searching for roleName '{}'", searchRoleName);
			roleList = roleservice.getRoleByName(searchRoleName);

		} else {
			logger.info("There is no role name given, performing full search");
			roleList = roleservice.getAllRoles();
		}
		roleForm.setCheckerRemarks("");
		mav.addObject(ROLE_FORM, roleForm);
		mav.addObject("showGrid", "true");
		mav.addObject("roleList", roleList);

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('APPROVE_ROLE')")
	@RequestMapping("/approveRejectRole")
	public ModelAndView approveRejectRole(HttpServletRequest request, @ModelAttribute(ROLE_FORM)Role roleForm) throws ServiceException
	{
		logger.debug(CCLPConstants.ENTER);
		List<Role> roleList=null;
		ModelAndView mav = new ModelAndView(ROLE_CONFIG);
		ResponseDTO responseDto = null;
	
		
		roleForm.setInsUser(sessionService.getUserId());
		roleForm.setLastUpdUser(sessionService.getUserId());
		String searchRoleName = request.getParameter("searchRoleName");
		logger.info("searchRoleName "+searchRoleName+" Approve Reject role from table {}", roleForm);
		
		responseDto = roleservice.approveRejectRole(roleForm);
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase("success")) {
				logger.info("Role record '{}' has been "+roleForm.getStatus()+" successfully", roleForm.getRoleId());
				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			} else {
				logger.error("Failed to "+roleForm.getStatus()+" record for role '{}'", roleForm.getRoleId());
				mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			}
		}
		if (!Util.isEmpty(searchRoleName)) {
			logger.info("Searching for roleName '{}'", searchRoleName);
			roleList = roleservice.getRoleByName(searchRoleName);

		} else {
			logger.info("There is no role name given, performing full search");
			roleList = roleservice.getAllRoles();
		}
		roleForm.setCheckerRemarks("");
		mav.addObject(ROLE_FORM, roleForm);
		mav.addObject("showGrid", "true");
		mav.addObject("roleList", roleList);

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	/**
	 * show view for Role
	 * @param exception
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW_ROLE')")
	@RequestMapping("/showViewRole")
	public ModelAndView showViewRole(@ModelAttribute(ROLE_FORM) Role roleForm) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView(SHOW_VIEW_ROLE);
		ResponseDTO responseDTO=roleservice.getRoleByRoleId(roleForm.getRoleId());
		if(responseDTO!=null ) {
			if( responseDTO.getData()!=null && responseDTO.getResult().equalsIgnoreCase(SUCCESS)) {
				logger.debug("RoleForm fetched successfully in showViewRole. Response from config service is "+responseDTO);
				ObjectMapper objectMapper = new ObjectMapper();
				Role role=objectMapper.convertValue(responseDTO.getData(), Role.class);
				long[] permissionIdAry = new long[role.getPermissions().size()];
				int i=0;
				for (PermissionDTO permDto : role.getPermissions()) {
					permissionIdAry[i]=permDto.getPermissionId();
					i++;
				}
				role.setPermissionID(permissionIdAry);
				logger.debug(role);
				mav.addObject(ROLE_FORM, role);
			}else {
				mav = new ModelAndView();
				mav.setViewName(ROLE_CONFIG);
				mav.addObject(ROLE_FORM, new Role());
				mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
				mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
				logger.error("responseDTO.getMessage() in showViewRole"+responseDTO);
			}
		}
		else {
			mav.addObject(CCLPConstants.STATUS_FLAG, FAIL);
			mav.addObject(CCLPConstants.STATUS_MESSAGE,ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
			mav.setViewName(ROLE_CONFIG);
			mav.addObject(ROLE_FORM, new Role());
			logger.error("Failed to fetch Role details. Response from config service in showViewRole is "+responseDTO);
		}
		
		Map<String,Map<String,Long>> map = roleservice.getEntityPermissionMap();
		mav.addObject(PERMISSION_MAP,map);
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
	
}
