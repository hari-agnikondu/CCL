package com.incomm.cclp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.dto.RoleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RoleService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/roles")
@Api(value="role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	long userId=1;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(RoleController.class);
	
	/**
	 * Gets all roles.
	 *
	 * @return the ResponseEntity with the result.
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllRoles() {
		logger.info(CCLPConstants.ENTER);
		logger.debug("Fetching all the roles from table");
		List<RoleDTO> roleDtos = roleService.getAllRoles();
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(roleDtos, 
				ResponseMessages.ALL_SUCCESS_ROLE_RETRIEVE,ResponseMessages.SUCCESS);
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Add new Role record in table.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createRole(@RequestBody RoleDTO roleDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, String> valuesMap = new HashMap<>();
		
		logger.debug("Creating new Role in tatble {}",roleDto.toString());
		roleService.createRole(roleDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("ROLE_ADD_"+ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		 valuesMap.put(CCLPConstants.ROLE_NAME, roleDto.getRoleName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		 logger.info("Role created successfully");
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	

	}
	
	
	/**
	 * Gets a role by name. The name can be a complete or partial Role name.
	 * 
	 * @param roleName The name of the role to be retrieved.

	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRoleByName(@RequestParam("roleName") String roleName) {
		logger.info(CCLPConstants.ENTER);
		
		ResponseDTO responseDto = null;
		
		if (Util.isEmpty(roleName))
		{
			logger.info("Role name is empty");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_ROLE_NAME,ResponseMessages.DOESNOT_EXISTS);
		}
		else
		{
			List<RoleDTO> roleDtos = roleService.getRoleByName(roleName);
			if(roleDtos.isEmpty()) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_ROLE_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
				
				 
				 logger.error("Failed to retrive role details for RoleName: {}",roleName);
				
			}
			else {
				responseDto = responseBuilder.buildSuccessResponse(roleDtos, 
						ResponseMessages.ALL_SUCCESS_ROLE_RETRIEVE,ResponseMessages.SUCCESS);
				logger.error("successfully retrived role details for RoleName: {}",roleName);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Gets a role by name. The name can be a complete or partial Role name.
	 * 
	 * @param roleName The name of the role to be retrieved.

	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRoleById(@PathVariable("roleId") long roleId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, String> valuesMap = new HashMap<>();
		
		if (roleId < 0)
		{
			logger.info("RoleId Cant be negative");
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_ROLE_ID,ResponseMessages.DOESNOT_EXISTS);
		}
		else
		{
			RoleDTO roleDto = roleService.getRoleById(roleId);
			if(roleDto == null) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_ROLE_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
				
			}
			else {
				responseDto = responseBuilder.buildSuccessResponse(roleDto,"ROLE_RETRIVE_"+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS);
				valuesMap.put(CCLPConstants.ROLE_NAME,roleDto.getRoleName());
				 String templateString = "";

				 templateString=responseDto.getMessage();
				
				 StrSubstitutor sub = new StrSubstitutor(valuesMap);
				 String resolvedString = sub.replace(templateString);
				 
				 responseDto.setMessage(resolvedString);
				logger.error("Failed to retrive role details for RoleName: {}",roleId);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	/**
	 * Updates a Role record.
	 * 
	 * @param roleDto The RoleDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping (method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateRole(@RequestBody RoleDTO roleDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		
		logger.debug(roleDto.toString());	
		roleService.updateRole(roleDto);
		logger.debug("Updating role details for {} as {}",roleDto.getRoleName(),roleDto.toString());
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("ROLE_UPDATE_"+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
		
		 valuesMap.put(CCLPConstants.ROLE_NAME, roleDto.getRoleName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * approve or reject a role
	 * 
	 * @param roleDto The RoleDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping (value="/status",method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> changeRoleStatus(@RequestBody RoleDTO roleDto) 
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		ResponseDTO responseDto =new ResponseDTO();
		
		logger.debug("updating the role status to {} ",roleDto.getStatus());
		
		roleService.changeRoleStatus(roleDto);
		
		if(roleDto.getStatus().equalsIgnoreCase("rejected")) {
			 responseDto = responseBuilder.buildSuccessResponse(null, 
					("ROLE_REJECTED_"+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
			 logger.debug("Role status changed to rejected for roleId{}",roleDto.getRoleId());
		}else if(roleDto.getStatus().equalsIgnoreCase("approved")) {
			
			responseDto = responseBuilder.buildSuccessResponse(null, 
				("ROLE_APPROVED_"+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
			logger.debug("Role status changed to approved for roleId{}",roleDto.getRoleId());
		}
		RoleDTO roleDtoTmp = roleService.getRoleById(roleDto.getRoleId());
		if(roleDtoTmp != null) {
		 valuesMap.put(CCLPConstants.ROLE_NAME, roleDtoTmp.getRoleName());
		 String templateString = "";

		 templateString=responseDto.getMessage();
		
		 StrSubstitutor sub = new StrSubstitutor(valuesMap);
		 String resolvedString = sub.replace(templateString);
		 
		 responseDto.setMessage(resolvedString);
		}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * delete a role record for given role id.
	 * 
	 * @param roleId The RoleId.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping(value="/{roleId}",method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteRole(@PathVariable("roleId") long roleId ) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
	
		ResponseDTO responseDto =null;
		
				logger.info("Deleting Role:'{}' from Role table", roleId);
				RoleDTO roleDtoTmp = roleService.getRoleById(roleId);
					try {
					roleService.deleteRole(roleId);
					responseDto = responseBuilder.buildSuccessResponse(null,
							("ROLE_DELETE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
					
					valuesMap.put(CCLPConstants.ROLE_NAME, roleDtoTmp.getRoleName());
					String templateString = "";
					templateString = responseDto.getMessage();
					StrSubstitutor sub = new StrSubstitutor(valuesMap);
					String resolvedString = sub.replace(templateString);
					responseDto.setMessage(resolvedString);
					
			} catch (ServiceException e) {
				logger.error("Error while deleting record for '{}'", roleId);
				throw new ServiceException(ResponseMessages.FAIL_ROLE_DELETE, ResponseMessages.DOESNOT_EXISTS);
				
			}	
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/entityPermissionMaps", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getEntityPermissionMap() {
		logger.info(CCLPConstants.ENTER);
		
		ResponseDTO responseDto = null;
		
			Map<Object, Map<Object, Object>> entityMap = roleService.getEntityPermissionMap();
			if(entityMap.isEmpty()) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAIL_ENTITY_PERMISSION_FETCH,ResponseMessages.DOESNOT_EXISTS);
				logger.info("Failed to retrive entityPermissionMap: {}");
			}
			else {
				logger.info("Successfull retrieved permission Map");
				responseDto = responseBuilder.buildSuccessResponse(entityMap,"ENTITY_PERMISSION_FETCH_"+ResponseMessages.SUCCESS,ResponseMessages.SUCCESS);
				
			}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDTO> exceptionHandler(Exception exception) 
	{
		logger.info(CCLPConstants.ENTER);
		String errMessage = ResponseMessages.CONFIGSERVICE_EXCEPTION;	
		String errCode =ResponseMessages.CONFIGSERVICE_EXCEPTION;
		if (exception instanceof ServiceException) {
			errMessage  = exception.getMessage();
			errCode=((ServiceException) exception).getCode();
		}
		logger.error(errMessage);
		
		ResponseDTO responseDto = responseBuilder.buildFailureResponse(errMessage,errCode);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
