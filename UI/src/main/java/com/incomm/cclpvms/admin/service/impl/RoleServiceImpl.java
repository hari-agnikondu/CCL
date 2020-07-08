package com.incomm.cclpvms.admin.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.admin.model.PermissionDTO;
import com.incomm.cclpvms.admin.model.Role;
import com.incomm.cclpvms.admin.model.RoleDTO;
import com.incomm.cclpvms.admin.service.RoleService;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class RoleServiceImpl implements RoleService {

	private static final Logger logger = LogManager.getLogger(RoleServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate ;
	@Value("${CONFIG_BASE_URL}") String CONFIG_BASE_URL;

	
	@Override
	public ResponseDTO createRole(Role role) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			logger.debug("Calling '{}' service to create new Role",CONFIG_BASE_URL);
			long[] permIdAry = role.getPermissionID();  
			Set<PermissionDTO> perDtoSet = new HashSet<>();
			for(int i=0;i < permIdAry.length;i++) {
				PermissionDTO permDto = new PermissionDTO();
				permDto.setPermissionId(permIdAry[i]);
				perDtoSet.add(permDto);
			}
			role.setPermissions(perDtoSet);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(CONFIG_BASE_URL + "/roles", role,ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while creating new role, {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<Role> roleList = new ArrayList<>();
		try {
			logger.debug("Calling '{}' service to search all roles",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/roles",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				logger.error("Failed to Fetch role list from config srvice");
				throw new ServiceException(responseBody.getMessage());
			}
			List<RoleDTO> roleDtos = (List<RoleDTO>) responseBody.getData();
			logger.debug(roleDtos);
			if (roleDtos != null) {
				Iterator<RoleDTO> itr = roleDtos.iterator();
				while (itr.hasNext()) {
					ModelMapper mm = new ModelMapper();
					RoleDTO obj = mm.map(itr.next(), RoleDTO.class);
					Set<PermissionDTO> permSet=obj.getPermissions();
					long[] permissionIDAry = new long[permSet.size()];
					int i=0;
					for (PermissionDTO permDto : permSet) {
						permissionIDAry[i]=permDto.getPermissionId();
						i++;
					}
					Role role=new Role(obj.getRoleId(),obj.getRoleName(),obj.getRoleDesc(),permissionIDAry,obj.getStatus(),obj.getPermissions(),obj.getInsUser());
					roleList.add(role);
				} 
			}
			logger.debug(roleList);
		} catch (RestClientException e) {
			logger.error("Exception while fetching records {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		
		logger.debug(CCLPConstants.EXIT);
		return roleList;
	}

	@Override
	public ResponseDTO updateRole(Role role) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			long[] permIdAry = role.getPermissionID();
			Set<PermissionDTO> perDtoSet = new HashSet<>();
			for(int i=0;i < permIdAry.length;i++) {
				PermissionDTO permDto = new PermissionDTO();
				permDto.setPermissionId(permIdAry[i]);
				perDtoSet.add(permDto);
			}
			role.setPermissions(perDtoSet);
			HttpEntity<Role> requestEntity = new HttpEntity<>(role, headers);
			logger.debug("Calling '{}' service to update role ",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + "/roles", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating role {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO deleteRole(long roleId) {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		logger.debug("Calling '{}' service to delete role '{}'",CONFIG_BASE_URL,roleId);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + "/roles/{roleId}",
				HttpMethod.DELETE, null, ResponseDTO.class, roleId);
		responseBody = responseEntity.getBody();
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public List<Role> getRoleByName(String roleName) {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		String tempurl="";
		ObjectMapper objectMapper = new ObjectMapper();
		List<Role> roleList = new ArrayList<>();
		logger.debug("Calling '{}' service for Search Role '{}'",CONFIG_BASE_URL,roleName);
		 tempurl = CONFIG_BASE_URL + "roles/search";
		  
		  UriComponentsBuilder builder = UriComponentsBuilder
		    .fromUriString(tempurl)
		        .queryParam("roleName", roleName);

		  ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
			      ResponseDTO.class);
		
		responseBody = responseEntity.getBody();
		@SuppressWarnings("unchecked")
		List<RoleDTO> roleDtos = (List<RoleDTO>) responseBody.getData();
		logger.debug(roleDtos);
		if (roleDtos != null) {
			Iterator<RoleDTO> itr = roleDtos.iterator();
			while (itr.hasNext()) {
				ModelMapper mm = new ModelMapper();
				RoleDTO obj = mm.map(itr.next(), RoleDTO.class);
				Set<PermissionDTO> permSet=obj.getPermissions();
				long[] permissionIDAry = new long[permSet.size()];
				int i=0;
				for (PermissionDTO permDto : permSet) {
					permissionIDAry[i]=permDto.getPermissionId();
					i++;
				}
				Role role=new Role(obj.getRoleId(),obj.getRoleName(),obj.getRoleDesc(),permissionIDAry,obj.getStatus(),obj.getPermissions(),obj.getInsUser());
				roleList.add(role);
			} 
		}
		logger.debug(CCLPConstants.EXIT);
		return roleList;
	}

	@Override
	public ResponseDTO getRoleByRoleId(long roleId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		logger.debug("Calling '{}' service for get Role '{}'",CONFIG_BASE_URL,roleId);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/roles/{roleId}",ResponseDTO.class, roleId);
		ResponseDTO responseDTO = responseEntity.getBody();
		logger.info("Role information "+responseEntity.getBody());
		logger.debug(CCLPConstants.EXIT);
		return responseDTO;
	}

	

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, Long>> getEntityPermissionMap() throws ServiceException {
	
		ResponseDTO responseBody = null;
		Map<String, Map<String, Long>> entityMap=null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/roles/entityPermissionMaps", ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.debug(responseBody);
			if(responseBody.getResult().equalsIgnoreCase("success")) {
				entityMap=(Map<String, Map<String, Long>>)responseBody.getData();
				logger.debug(entityMap);
			}
			else {
				logger.error("Failed to fetch entity permission map "+responseBody.getMessage());
				
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getPINTabByProductId:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug("EXIT");

		return entityMap;
	}

	public ResponseDTO approveRejectRole(Role role) throws ServiceException {
		
		logger.debug("ENTER "+role);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Role> requestEntity = new HttpEntity<>(role, headers);
			logger.debug("Calling '{}' service to approve or reject role ",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + "/roles/status", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating role {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
		
	}
	
	
}

