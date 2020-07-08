/**
 * 
 */
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ClpUserDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.UserService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

/**
 * User Controller provides all the REST operations pertaining to User
 * Management.
 * 
 * @author abutani
 *
 */
@RestController
@RequestMapping("/users")
@Api(value = "users")
public class UserController {

	// the user service.
	@Autowired
	private UserService userService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;

	// the logger
	private static final Logger logger = LogManager.getLogger(UserController.class);

	/**
	 * Gets a user by user name
	 * 
	 * @param userName
	 *            The username of the User to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getUserByUserName(@RequestParam("userName") String userName) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("User Name: " + userName);

		if (Util.isEmpty(userName)) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_USER_NAME,
					ResponseMessages.ERR_USER_NAME);
		} else {
			ClpUserDTO userDto = userService.getUserByUserName(userName);
			logger.debug("LoginName: {}, Last login: {}",userDto.getUserLoginId(),userDto.getLastLoginTime());
			responseDto = responseBuilder.buildSuccessResponse(userDto, ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}

		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update the last login time for a user.
	 * 
	 * @param userId
	 *            The userId of the User to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{userId}/lastLoginTime", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateLastLoginTime(@PathVariable("userId") long userId) {

		logger.info(CCLPConstants.ENTER);

		userService.updateLastLoginTime(userId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createUser(@RequestBody ClpUserDTO userDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("Clp User DTO: " + userDto.getUserName());

		ResponseDTO responseDto = null;
		String templateString = "";
		Map<String, String> valuesMap = new HashMap<>();

		ValidationService.validateUser(userDto, true);

		logger.debug("Creating new user in tatble {}", userDto.toString());

		userService.createUser(userDto);

		responseDto = responseBuilder.buildSuccessResponse(null, ("CLP_USER_ADD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);

		templateString = responseDto.getMessage();
		valuesMap.put(CCLPConstants.USER_NAME, userDto.getUserName());
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);

		logger.info("User '{}' created successfully", userDto.getUserName());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/searchUser", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getUsersByName(@RequestParam("userName") String userName) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("User Name to get data: " + userName);
		if (Util.isEmpty(userName)) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ISSUER_NAME,
					ResponseMessages.ERR_ISSUER_NAME);
		} else {
			List<ClpUserDTO> userDtos = userService.getUsersByName(userName);

			responseDto = responseBuilder.buildSuccessResponse(userDtos, ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets all Active Users.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllUsers() {
		logger.info(CCLPConstants.ENTER);

		List<ClpUserDTO> userDtos = userService.getAllUsers();

		logger.info("Performing full serach for users");

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(userDtos,
				("USER_RETRIVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userLoginId}/{userName}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getUserByUserLoginIdAndUserName(@PathVariable("userLoginId") String userLoginId,
			@PathVariable("userName") String userName){
		logger.info(CCLPConstants.ENTER);

		List<ClpUserDTO> clpUserList = userService.getUserByUserLoginIdAndUserName(userLoginId, userName);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(clpUserList,
				("USER_RETRIVE_ALL_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getUsersByUserId(@PathVariable("userId") Long userId) {

		logger.info(CCLPConstants.ENTER);

		ClpUserDTO clpuserDTO = null;

		clpuserDTO = userService.getUserByUserId(userId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(clpuserDTO,
				("USER_RETRIVE_ALL_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Updates an product.
	 * 
	 * @param productDto
	 *            The ProductDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateUser(@RequestBody ClpUserDTO userDto) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		logger.debug(userDto.toString());

		Map<String, String> valuesMap = new HashMap<>();
		ResponseDTO responseDto = null;
		String templateString = "";

		ValidationService.validateUser(userDto, false);
		
	int usrCount=userService.countOfUser(userDto.getUserId());
		
		if(usrCount==0) {
			responseDto = responseBuilder.buildFailureResponse(
					("USR_"+ResponseMessages.DOESNOT_EXISTS),ResponseMessages.DOESNOT_EXISTS);
		}else {
				
		userService.updateUser(userDto);

		logger.debug("Updating user details for {} as {}", userDto.getUserName(), userDto.toString());

		responseDto = responseBuilder.buildSuccessResponse(null, ("USER_UPDATE_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		valuesMap.put(CCLPConstants.USER_NAME, userDto.getUserName());

		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);
		}
		
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Change user status
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */	
	@RequestMapping(value = "/changeUserStatus", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> changeUserStatus(@RequestBody ClpUserDTO userDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		String  templateString= "";
		Map<String, String> valuesMap = new HashMap<>();
		
		if (userDto.getUserId() <= 0) {
			logger.info("User Id is negative: {}", userDto.getUserId());
			
			throw new ServiceException(ResponseMessages.ERR_CLP_USER_ID,
					ResponseMessages.DOESNOT_EXISTS);
		}
		
		if (Util.isEmpty(userDto.getUserStatus())) {
			logger.info("User status is empty");
			
			throw new ServiceException(ResponseMessages.ERR_CLP_USER_STATUS,
					ResponseMessages.DOESNOT_EXISTS);
		}
		
		userService.changeUserStatus(userDto);
		
		if(CCLPConstants.USER_STATUS_APPROVED.equalsIgnoreCase(userDto.getUserStatus()))
		{
			logger.info("User '{}' is approved successfully", userDto.getUserName());
			
			responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_USER_APPROVED,
					ResponseMessages.SUCCESS);
		}
		else
		{
			logger.info("User '{}' is rejected successfully", userDto.getUserName());
			
			responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_USER_REJECTED,
					ResponseMessages.SUCCESS);
		}
		
		valuesMap.put(CCLPConstants.USER_NAME, userDto.getUserName());

		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);

		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Deletes a user.
	 * 
	 * @param userId The id of the User to be deleted.

	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/{userId}" , method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteUser(@PathVariable("userId") long userId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		String templateString = "";
		ResponseDTO responseDto = null;
		ClpUserDTO clpUserDto = null;
		Map<String, String> valuesMap = new HashMap<>();
		
		if (userId <= 0) {
			logger.info("User Id is negative: {}", userId);
			
			throw new ServiceException(ResponseMessages.ERR_CLP_USER_ID,
					ResponseMessages.DOESNOT_EXISTS);
		}
		
		
		int usrCount=userService.countOfUser(userId);
		
		if(usrCount==0) {
			responseDto = responseBuilder.buildFailureResponse(
					("USR_"+ResponseMessages.DOESNOT_EXISTS),ResponseMessages.DOESNOT_EXISTS);
		}else {
		
		
		clpUserDto = userService.getUserByUserId(userId);
		
		userService.deleteUserById(userId);
		
		responseDto = responseBuilder.buildSuccessResponse(null,
				("USR_DELETE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		valuesMap.put(CCLPConstants.USER_NAME, clpUserDto.getUserName());
		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);
		
		logger.info("User '{}' record deleted successfully", clpUserDto.getUserName());
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Change user status
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */	
	@RequestMapping(value = "/updateAccessStatus", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateAccessStatus(@RequestBody ClpUserDTO userDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		String templateString = "";
		Map<String, String> valuesMap = new HashMap<>();

		if (userDto.getUserId() <= 0) {
			logger.info("User Id is negative: {}", userDto.getUserId());
			throw new ServiceException(ResponseMessages.ERR_CLP_USER_ID, ResponseMessages.DOESNOT_EXISTS);
		}

		if (Util.isEmpty(userDto.getAccessStatus())) {
			logger.info("User status is empty");
			throw new ServiceException(ResponseMessages.ERR_CLP_USER_STATUS, ResponseMessages.DOESNOT_EXISTS);
		}

		userService.updateAccessStatus(userDto);
		logger.info("User '{}' is approved successfully", userDto.getUserName());
		if (userDto.getAccessStatus().equalsIgnoreCase("ACTIVE")) {
			responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_USER_ACTIVATED,
					ResponseMessages.SUCCESS);
		} else {
			responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_USER_DEACTIVATED,
					ResponseMessages.SUCCESS);
		}

		valuesMap.put(CCLPConstants.USER_NAME, userDto.getUserName());

		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);

		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	
}
