package com.incomm.cclpvms.admin.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.admin.model.ClpUser;
import com.incomm.cclpvms.admin.model.ClpUserDTO;
import com.incomm.cclpvms.admin.model.GroupDTO;
import com.incomm.cclpvms.admin.service.UserService;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

/**
 * UserServiceImpl implements the UserService to provide 
 * necessary User related operations.
 * 
 * @author abutani
 *
 */

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}") String configBaseUrl;

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	public static final String USER_NAME="userName";
	public static final String USERS="users/";


	/**
	 * Gets an User by user name.
	 * 
	 * @param userName The user name for the User to be retrieved.
	 * 
	 * @return the User.
	 * 
	 * @throws ServiceException
	 */
	public ClpUserDTO getUserByUserName(String userName) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		ClpUserDTO user = null;
		ObjectMapper objectMapper = new ObjectMapper();

		try 
		{
			logger.debug("Calling '{}' service to get users by username",configBaseUrl);

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(configBaseUrl+"users/search")
					.queryParam(USER_NAME, userName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
					builder.build().encode().toUri(),
					ResponseDTO.class);
			
			responseBody = responseEntity.getBody();
			user  = objectMapper.convertValue(responseBody.getData(), ClpUserDTO.class);

		} catch (RestClientException e) 
		{
			logger.error("Exception while fetching user records {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(
					ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		logger.debug(CCLPConstants.EXIT);
		logger.info("Exit getUserByUserName serviceimpl");
		return user;
	}

	
	/**
	 * Updates last login time for user.
	 * 
	 * @param userId The userId for the User to be updated.
	 * 
	 * @throws ServiceException
	 */
	public void updateLastLoginTime(long userId) throws ServiceException
	{
		logger.debug(CCLPConstants.ENTER);
		String url = "";
		
		try 
		{
			url = USERS + userId + "/lastLoginTime";
			logger.info("Entered in to updateLostLoginTime");
			
			restTemplate.exchange(configBaseUrl + url, 
					HttpMethod.PUT,
					null, ResponseDTO.class);

		} catch (RestClientException e) 
		{
			logger.error("Error Occured during making a Rest Call in updateLastLoginTime()" + e);
		
			throw new ServiceException(ResourceBundleUtil.getMessage(
					ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
	}
	
	
	
	public ResponseDTO createUser(ClpUserDTO clpUserDTO) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + "users", clpUserDTO,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of CreateUser " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in CreateUser()"+e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;

	}
	
	
	
	
	public List<ClpUserDTO> getUsersByName(String userName) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<ClpUserDTO> userList = new ArrayList<>();
		String tempurl = "";
		tempurl = "users/searchUser";
		try {
		
			  UriComponentsBuilder builder = UriComponentsBuilder
			    .fromUriString(configBaseUrl+tempurl)
			        .queryParam(USER_NAME, userName);

ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);
			responseBody=responseEntity.getBody();
			List<ClpUserDTO> userDtos = (List<ClpUserDTO>) responseBody.getData();
			
			userList.addAll(userDtos);
			
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getUsersByName()" + e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		}
		logger.debug(CCLPConstants.EXIT);
		return userList;
	}


	@Override
	public List<ClpUserDTO> getAllUsers()  throws ServiceException {
			logger.debug(CCLPConstants.ENTER);
			String url = "";
			ResponseDTO responseBody = null;
			List<ClpUserDTO> userList = new ArrayList<>();
			
			try {
				url = USERS;
				ObjectMapper objectMapper = new ObjectMapper();
				ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + url,
						ResponseDTO.class);

				responseBody = responseEntity.getBody();
				logger.info("reponse from RestCall : " + responseBody.getResult());
				if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.FAILURE)) {
					throw new ServiceException(
							responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all the Users");
				}
				List<Object> issuerDtos = (List<Object>) responseBody.getData();
				Iterator<Object> itr = issuerDtos.iterator();

				while (itr.hasNext()) {
					userList.add(objectMapper.convertValue(itr.next(), ClpUserDTO.class));
				}
			} catch (RestClientException e) {

				logger.error("Error Occured during making a Rest Call in get All Users()"+e);
				throw new ServiceException(CCLPConstants.ERR_MSG);
			}

			logger.debug(CCLPConstants.EXIT);
			return userList;
		}

	
	
	
	@Override
	public List<GroupDTO> getAllGroups()  throws ServiceException {
			logger.debug(CCLPConstants.ENTER);
			String url = "";
			ResponseDTO responseBody = null;
			List<GroupDTO> groupList = new ArrayList<>();
			
			try {
				url = "master/groups";
			
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + url,
						ResponseDTO.class);
			
				
				responseBody = responseEntity.getBody();
				logger.info("reponse from Rest Callservice : " + responseBody.getResult());
				if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
					throw new ServiceException(
							responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all Users");
				}
				List<GroupDTO> groupDtos = (List<GroupDTO>) responseBody.getData();
	
				groupList.addAll(groupDtos);
				
				
			} catch (RestClientException e) {

				logger.error("Error Occured during making a Rest Call in getAllUsers()"+e);
				throw new ServiceException(CCLPConstants.ERR_MSG);
			}

			logger.debug(CCLPConstants.EXIT);
			return groupList;
		}
	public List<ClpUserDTO> getAllUsers(ClpUser clpUser)  throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String url = "";
		ResponseDTO responseBody = null;
		List<ClpUserDTO> userList = new ArrayList<>();
		
		try {
			url = USERS;
			
			
			String userName; 
			if(clpUser.getUserName()==null || clpUser.getUserName().trim().isEmpty()){
				userName="*";
			}
			else{
				userName=clpUser.getUserName();
			}
			String userLoginId; 
			if(clpUser.getUserLoginId()==null || clpUser.getUserLoginId().trim().isEmpty()){
				userLoginId="*";
			}
			else{
				userLoginId=clpUser.getUserLoginId();
			}
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + url+userLoginId+"/"+userName,
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call details : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all Users");
			}
			List<ClpUserDTO> userDtos = (List<ClpUserDTO>) responseBody.getData();

			userList.addAll(userDtos);
			
	
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllUsers()"+e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		}

		logger.debug(CCLPConstants.EXIT);
		return userList;
	}



	
	
	public ClpUser getUserByUserId(Long userId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String tempurl = "";
		tempurl = USERS + userId;
		String idSearchUrl = tempurl;
		ResponseDTO responseBody = null;
		ClpUser clpUser = null;
		List<GroupDTO> groupDTO= null;
		Long userContactNumber;
		
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + idSearchUrl,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			Map<String, Object> userobj = null;
			userobj = (Map<String, Object>) responseBody.getData();
			if(userobj!=null) {
				
				
			
		String userLoginId = (String) userobj.get("userLoginId");
		String userName = (String) userobj.get(USER_NAME);
		String userEmail = (String) userobj.get("userEmail");
		if(userobj.get("userContactNumber")!=null) {
		 userContactNumber =Long.parseLong((userobj.get("userContactNumber").toString()));
		}else {
			userContactNumber=null;
		}
		String userStatus = String.valueOf(userobj.get("userStatus"));
		
		groupDTO=(List<GroupDTO>) userobj.get("groups");
			
clpUser = new ClpUser(userId, userLoginId, userStatus, userName, userEmail, userContactNumber,groupDTO);
		
		} 
		}catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getUserById()" + e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		}

		logger.debug(CCLPConstants.EXIT);
		return clpUser;
	}


	@Override
	public ResponseDTO updateUser(ClpUserDTO clpUserDTO) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<ClpUserDTO> requestEntity = new HttpEntity<>(clpUserDTO, headers);
ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + "users", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in updateUser()" + e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}


	
	
	@Override
	public ResponseEntity<ResponseDTO> approveUser(ClpUser clpUser) throws ServiceException {
		String url = USERS;
		logger.debug(CCLPConstants.ENTER);
		ClpUserDTO clpuserDTO = new ClpUserDTO();
		ResponseDTO responseBody = null;
		ResponseEntity<ResponseDTO> responseEntity = null;

		clpuserDTO.setInsUser(clpUser.getInsUser());
		clpuserDTO.setUserStatus(clpUser.getUserStatus());
		clpuserDTO.setCheckerRemarks(clpUser.getCheckerRemarks());
		clpuserDTO.setUserId(clpUser.getUserId());
		clpuserDTO.setInsDate(new Date());
		clpuserDTO.setLastUpdDate(new Date());
		clpuserDTO.setLastUpdUser(clpUser.getInsUser());
		clpuserDTO.setUserName(clpUser.getUserName());

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<ClpUserDTO> requestEntity = new HttpEntity<>(clpuserDTO, headers);
			responseEntity = restTemplate.exchange(configBaseUrl + url + "changeUserStatus/", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("responseBody in approveuser" + responseBody);

		} catch (RestClientException e) {
			logger.error("RestClientException in approve user:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		logger.debug(CCLPConstants.EXIT);
		return responseEntity;
	}


	@Override
	public ResponseEntity<ResponseDTO> rejectUser(ClpUser clpUser) throws ServiceException {
        String url = USERS;
        logger.debug(CCLPConstants.ENTER);
        ClpUserDTO clpuserDTO=new ClpUserDTO();
        ResponseDTO responseBody = null;
        ResponseEntity<ResponseDTO> responseEntity =null;
        
        
        clpuserDTO.setInsUser(clpUser.getInsUser());
        clpuserDTO.setUserStatus(clpUser.getUserStatus());
        clpuserDTO.setCheckerRemarks(clpUser.getCheckerRemarks());
        clpuserDTO.setUserId(clpUser.getUserId());
        clpuserDTO.setInsDate(new Date());
        clpuserDTO.setLastUpdDate(new Date());
        clpuserDTO.setLastUpdUser(clpUser.getInsUser());
        clpuserDTO.setUserName(clpUser.getUserName());
        
        try {

 HttpHeaders headers = new HttpHeaders();
	HttpEntity<ClpUserDTO> requestEntity = new HttpEntity<>(clpuserDTO, headers);
	responseEntity = restTemplate.exchange(configBaseUrl+url+"changeUserStatus/", HttpMethod.PUT,
			requestEntity, ResponseDTO.class);
	responseBody = responseEntity.getBody();
	logger.info("responseBody in rejectuser"+responseBody);
 
        }
        catch (RestClientException e) {
            logger.error("RestClientException in approveuser:"+e);
            throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
        } 
        
        logger.debug(CCLPConstants.EXIT);
        return responseEntity;
    }


	@Override
	public ResponseEntity<ResponseDTO> deleteUser(ClpUser clpUser) throws ServiceException {
        String url = USERS;
        logger.debug(CCLPConstants.ENTER);
        ClpUserDTO clpuserDTO=new ClpUserDTO();
        ResponseDTO responseBody = null;
        ResponseEntity<ResponseDTO> responseEntity =null;
                
        clpuserDTO.setInsUser(clpUser.getInsUser());
        clpuserDTO.setUserStatus(clpUser.getUserStatus());
        clpuserDTO.setCheckerRemarks(clpUser.getCheckerRemarks());
        clpuserDTO.setUserId(clpUser.getUserId());
        clpuserDTO.setInsDate(new Date());
        clpuserDTO.setLastUpdDate(new Date());
        clpuserDTO.setLastUpdUser(clpUser.getInsUser());
        clpuserDTO.setUserName(clpUser.getUserName());
        
        try {

 HttpHeaders headers = new HttpHeaders();
	HttpEntity<ClpUserDTO> requestEntity = new HttpEntity<>(null, headers);
	responseEntity = restTemplate.exchange(configBaseUrl+url+clpuserDTO.getUserId(), HttpMethod.DELETE,
			requestEntity, ResponseDTO.class,(Object) clpuserDTO.getUserId());
	responseBody = responseEntity.getBody();
	logger.info("responseBody in delete user"+responseBody);
 
        }
        catch (RestClientException e) {
            logger.error("RestClientException in approve user details:"+e);
            throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
        } 
        
        logger.debug(CCLPConstants.EXIT);
        return responseEntity;
    }


	@Override
	public ResponseEntity<ResponseDTO> updateAccessStatus(ClpUser clpUser) throws ServiceException {
		String url = USERS;
		logger.debug(CCLPConstants.ENTER);
		ClpUserDTO clpuserDTO = new ClpUserDTO();
		ResponseDTO responseBody = null;
		ResponseEntity<ResponseDTO> responseEntity = null;

		clpuserDTO.setInsUser(clpUser.getInsUser());
		clpuserDTO.setUserStatus(clpUser.getUserStatus());
		clpuserDTO.setCheckerRemarks(clpUser.getCheckerRemarks());
		clpuserDTO.setUserId(clpUser.getUserId());
		clpuserDTO.setInsDate(new Date());
		clpuserDTO.setLastUpdDate(new Date());
		clpuserDTO.setLastUpdUser(clpUser.getInsUser());
		clpuserDTO.setUserName(clpUser.getUserName());
		clpuserDTO.setAccessStatus(clpUser.getAccessStatus());

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<ClpUserDTO> requestEntity = new HttpEntity<>(clpuserDTO, headers);
			responseEntity = restTemplate.exchange(configBaseUrl + url + "updateAccessStatus/", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("responseBody in approveuser" + responseBody);

		} catch (RestClientException e) {
			logger.error("RestClientException in approve user:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		logger.debug(CCLPConstants.EXIT);
		return responseEntity;
	}


}
