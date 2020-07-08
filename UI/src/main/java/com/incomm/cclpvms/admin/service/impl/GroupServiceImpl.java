package com.incomm.cclpvms.admin.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.incomm.cclpvms.admin.model.Group;
import com.incomm.cclpvms.admin.model.GroupDTO;
import com.incomm.cclpvms.admin.model.Role;
import com.incomm.cclpvms.admin.model.RoleDTO;
import com.incomm.cclpvms.admin.service.GroupService;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;


@Service
public class GroupServiceImpl implements GroupService {
	
	private static final Logger logger = LogManager.getLogger(GroupServiceImpl.class.getName());
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${CONFIG_BASE_URL}") String CONFIG_BASE_URL;
	
	String groupBaseUrl="/groups";
	
	@Autowired
	SessionService sessionService;
	
	@Override
	public Group getGroupById(Long groupId) throws ServiceException {
		return null;
			
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupByName(String groupName) throws ServiceException {
		List<Group> groupDtoList=null;
		String tempUrl="";	
		try{

			tempUrl = CONFIG_BASE_URL + groupBaseUrl + "/search";		  
			  UriComponentsBuilder builder = UriComponentsBuilder
			    .fromUriString(tempUrl)
			        .queryParam("groupName", groupName);
			  
			  ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
				      ResponseDTO.class);

			if(responseEntity!=null && responseEntity.getBody()!=null)
			{
				ResponseDTO responseDTO = responseEntity.getBody();
				logger.debug("response from Rest Call : "+responseDTO.getResult());
				
				if (!ResponseMessages.SUCCESS.equals(responseDTO.getCode())) 
				{
					throw new ServiceException(responseDTO.getResult());
				}

			 groupDtoList=(List<Group>) responseDTO.getData();
			}
		
		} catch (RestClientException e) {
						
			logger.error("Error Occured during making a Rest Call in getGroups()"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return groupDtoList;
	}

	
	/**
	 * This method used to update the status  Approved or Rejected.
	 */
	@Override
	public ResponseEntity<ResponseDTO> changeGroupStatus(Group groupConfig) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
		  
			  GroupDTO groupDto = new ModelMapper().map(groupConfig, GroupDTO.class);
			  groupDto.setLastUpdateUser(sessionService.getUserId());
				
			  UriComponentsBuilder builder = UriComponentsBuilder
						.fromUriString(CONFIG_BASE_URL +groupBaseUrl+ "/"+groupConfig.getGroupId()+"/status");

				 responseDTO = restTemplate.exchange(builder.build().encode().toUri(),
						HttpMethod.PUT,new HttpEntity<GroupDTO>(groupDto), ResponseDTO.class);
			
		}
		catch (RestClientException e) {
			logger.error("RestClientException in GroupStatusUpdate:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
/**
 * This method used to Delete the Group.
 */
	@Override
	public ResponseEntity<ResponseDTO> deleteGroup(Long groupId) throws ServiceException 
	{
		ResponseEntity<ResponseDTO> responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
						
			logger.debug("Calling '{}' service to delete Group '{}'",CONFIG_BASE_URL,groupId);
			 responseDTO = restTemplate.exchange(CONFIG_BASE_URL +groupBaseUrl+"/"+groupId,HttpMethod.DELETE,null, ResponseDTO.class);
		}//try
		catch (RestClientException e) {
			logger.error("RestClientException in deleteGroup:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	
	/**
	 * This method retrieves  all the roles.
	 */
	
	public List<RoleDTO> getAllRoles() throws ServiceException
	{
		List<RoleDTO> roleList = new ArrayList<>();
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to search all Roles",CONFIG_BASE_URL);						
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/roles",
					ResponseDTO.class);		
			
			ResponseDTO responseBody  = responseEntity.getBody();			
			if(responseBody!=null && responseBody.getData()!=null) {			
				ModelMapper mm = new ModelMapper();			
				roleList= mm.map(responseBody.getData(),new TypeToken<List<RoleDTO>>() {}.getType());				
			}
									
		}
		catch (RestClientException e) {
			logger.error("RestClientException in getAllIssuers:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		
		logger.debug(CCLPConstants.EXIT);
		return roleList;
	}
/**
 * This method is used to Add the Group.
 */
	@Override
	public ResponseDTO addGroup(Group groupConfig) throws ServiceException {
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to create new addGroupAccess",CONFIG_BASE_URL);
			ModelMapper mm = new ModelMapper();
			
				Set<Role> roles=new HashSet<>();
			
			Set<String> rolesSelList=groupConfig.getSelectedRoleList();
			for(String id:rolesSelList){
				
				Role role=new Role(Long.parseLong(id), null, null, null, null, null);
				role.setInsUser(sessionService.getUserId());		
				role.setLastUpdUser(sessionService.getUserId());
				roles.add(role);
			
			}
			
			groupConfig.setRoles(roles);		
			GroupDTO groupDTO = mm.map(groupConfig, GroupDTO.class);			
			groupDTO.setInsUser(sessionService.getUserId());			
			responseDTO=restTemplate.postForObject(CONFIG_BASE_URL + groupBaseUrl ,groupDTO, ResponseDTO.class); 
			
		} 
		catch (RestClientException e) {
			logger.error("Error while creating new GroupAccess:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	
	
	
	/** 
	 * This Method retrieves GroupNames List.
	 */
	
	@Override
	public List<GroupDTO> getGroupNameList () throws ServiceException {
		List<GroupDTO> groupNameList=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + groupBaseUrl,
					ResponseDTO.class);		
			ResponseDTO responseBody  = responseEntity.getBody();
			
			if(responseBody!=null && responseBody.getData()!=null) {	
				ModelMapper mm = new ModelMapper();
				groupNameList= mm.map(responseBody.getData(),new TypeToken<List<GroupDTO>>() {}.getType());					
			}
		} 
		catch (RestClientException e) {
			logger.error("Error while creating new Group:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return groupNameList;
	}
	
	/** 
	 * This Method retrieves RolesNames Based on GroupId
	 */
	public GroupDTO getRoleNamesbyGroupId(Long groupId) throws ServiceException {		
	GroupDTO groupDTO=null;

	logger.debug(CCLPConstants.ENTER);

	ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + groupBaseUrl+ "/"+groupId,
			ResponseDTO.class);			  
	ResponseDTO responseBody  = responseEntity.getBody();
	
	if(responseBody!=null && responseBody.getData()!=null) {
		
		ModelMapper mm = new ModelMapper();
	
		groupDTO= mm.map(responseBody.getData(),new TypeToken<GroupDTO>() {}.getType());
		
	}
	logger.debug(CCLPConstants.EXIT);
	return groupDTO;

}
/** 
 * This Method used to updateGroup.
 */
	@Override
	public ResponseDTO updateGroup(Group groupConfig) throws ServiceException {
		ResponseDTO responseDTO=null;
		logger.debug(CCLPConstants.ENTER);
		
		try {
			logger.debug("Calling '{}' service to create new addGroupAc",CONFIG_BASE_URL);			
			ModelMapper mm = new ModelMapper();
			GroupDTO groupDTO = mm.map(groupConfig, GroupDTO.class);
			
			
			groupDTO.setGroupId(groupConfig.getGroupId());
			groupDTO.setGroupName(groupConfig.getGroupName());
			groupDTO.setLastUpdateUser(sessionService.getUserId());
			
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + groupBaseUrl,
						HttpMethod.PUT,new HttpEntity<GroupDTO>(groupDTO), ResponseDTO.class);
			responseDTO=responseEntity.getBody();		
		} 
		catch (RestClientException e) {
			logger.error("Error while Updating Group:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} 
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}
	


}
