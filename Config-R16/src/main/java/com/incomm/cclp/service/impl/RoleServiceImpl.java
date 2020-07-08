package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.RoleDAO;
import com.incomm.cclp.domain.Permission;
import com.incomm.cclp.domain.Role;
import com.incomm.cclp.domain.RolePermission;
import com.incomm.cclp.domain.RolePermissionTemp;
import com.incomm.cclp.dto.PermissionDTO;
import com.incomm.cclp.dto.RoleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RoleService;

/**
 * RoleServiceImpl class defines all the Business Logic for 
 * Role Configuration.
 *
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDAO roleDao;

	// the logger
	private static final Logger logger = LogManager.getLogger(RoleServiceImpl.class);

	public List<RoleDTO> getAllRoles() {
		logger.info(CCLPConstants.ENTER);

		List<Role> roles = roleDao.getAllRoles();

		logger.info(CCLPConstants.EXIT);
		return constructRoleDto(roles);
	}

	@Override
	public List<RoleDTO> getRoleByName(String roleName) {
		logger.info(CCLPConstants.ENTER);

		List<Role> roles = roleDao.getRoleByName(roleName);
		
		logger.info(CCLPConstants.EXIT);
		return constructRoleDto(roles);
	}

	public List<RoleDTO> constructRoleDto(List<Role> roles) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		List<RoleDTO> roleDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(roles)) {
			roles.stream().forEach(p -> {
				Set<PermissionDTO> permissionDtos = new HashSet<>();
				RoleDTO roleDto = new RoleDTO();
				roleDto.setInsUser(p.getInsUser());
				roleDto.setLastUpdUser(p.getLastUpdUser());
				roleDto.setRoleDesc(p.getRoleDesc());
				roleDto.setRoleId(p.getRoleId());
				roleDto.setRoleName(p.getRoleName());
				roleDto.setStatus(p.getStatus());

				p.getRolePermissionsTemp().stream().forEach(q -> {
					PermissionDTO permissionDto = new PermissionDTO();
					permissionDto = mm.map(q.getPermission(), PermissionDTO.class);
					permissionDtos.add(permissionDto);
				});
				roleDto.setPermissions(permissionDtos);
				roleDtos.add(roleDto);

			});
		}
		logger.info(CCLPConstants.EXIT);
		return roleDtos;

	}

	@Override
	@Transactional
	public void createRole(RoleDTO roleDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		Role role = new Role();
		Set<RolePermissionTemp> rolePermissions = new HashSet<>();
		List<RoleDTO> roleDtos = getRoleByName(roleDto.getRoleName());
		if (!CollectionUtils.isEmpty(roleDtos)) {

			List<RoleDTO> existingRoles = roleDtos.stream()
					.filter(p -> p.getRoleName().equalsIgnoreCase(roleDto.getRoleName())).collect(Collectors.toList());

			if (existingRoles != null && !existingRoles.isEmpty()) {

				logger.debug("Role name '{}' does not exist in table", roleDto.getRoleName());
				throw new ServiceException(ResponseMessages.ERR_ROLE_EXISTS);
			}
		}
		role.setInsUser(roleDto.getInsUser());
		role.setLastUpdUser(roleDto.getLastUpdUser());
		role.setRoleDesc(roleDto.getRoleDesc());
		role.setRoleName(roleDto.getRoleName());
		role.setStatus(roleDto.getStatus());

		roleDto.getPermissions().stream().forEach(p -> {
			RolePermissionTemp rolePermissionTemp = new RolePermissionTemp();
			rolePermissionTemp.setRole(role);
			rolePermissionTemp.setPermission(mm.map(p, Permission.class));
			rolePermissionTemp.setInsUser(roleDto.getInsUser());
			rolePermissionTemp.setLastUpdUser(roleDto.getLastUpdUser());
			rolePermissions.add(rolePermissionTemp);
		});
		role.setRolePermissionsTemp(rolePermissions);
		logger.debug("Creating role for Role Name: {}", roleDto.getRoleName());
		roleDao.createRole(role);
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public RoleDTO getRoleById(long roleId) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		RoleDTO roleDto = new RoleDTO();
		Role role = roleDao.getRoleById(roleId);
		Set<PermissionDTO> permissionDtos = new HashSet<>();
		if (role == null) {
			logger.error("Error while fetching record for roleId: {}", roleId);
			logger.info(CCLPConstants.ENTER);
			return null;
		}
		roleDto.setInsUser(role.getInsUser());
		roleDto.setLastUpdUser(role.getLastUpdUser());
		roleDto.setRoleDesc(role.getRoleDesc());
		roleDto.setRoleId(role.getRoleId());
		roleDto.setRoleName(role.getRoleName());
		roleDto.setStatus(role.getStatus());

		role.getRolePermissionsTemp().stream().forEach(q -> {
			PermissionDTO permissionDto = new PermissionDTO();
			permissionDto = mm.map(q.getPermission(), PermissionDTO.class);
			permissionDtos.add(permissionDto);
		});
		roleDto.setPermissions(permissionDtos);

		logger.info(CCLPConstants.EXIT);
		return roleDto;

	}

	@Override
	@Transactional
	public void updateRole(RoleDTO roleDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		Role role = roleDao.getRoleById(roleDto.getRoleId());
		if (Objects.isNull(role)) {
			logger.error("Role is null");
			throw new ServiceException(ResponseMessages.ERR_ROLE_ID, ResponseMessages.DOESNOT_EXISTS);
		}
		
		ModelMapper mm = new ModelMapper();

		List<RoleDTO> existingRoles = new ArrayList<>();
		try {
			existingRoles = getRoleByName(roleDto.getRoleName());
		} catch (Exception e) {
			logger.error("Role already exists");
			throw new ServiceException(ResponseMessages.ERR_ROLE_EXISTS, ResponseMessages.ALREADY_EXISTS);

		}
		if (!existingRoles.isEmpty()) {
			Iterator<RoleDTO> iterator = existingRoles.iterator();
			while (iterator.hasNext()) {
				RoleDTO existRole = iterator.next();
				if (existRole.getRoleName().equalsIgnoreCase(roleDto.getRoleName())
						&& (existRole.getRoleId() != roleDto.getRoleId())) {

					throw new ServiceException(ResponseMessages.ERR_ROLE_EXISTS, ResponseMessages.ALREADY_EXISTS);
				}
			}
			logger.debug("Role name '{}' does not exist in table", roleDto.getRoleName());
		}

	
		role.setInsUser(roleDto.getInsUser());
		role.setLastUpdUser(roleDto.getLastUpdUser());
		role.setRoleDesc(roleDto.getRoleDesc());
		role.setRoleName(roleDto.getRoleName());
		role.setStatus(roleDto.getStatus());
		
		if (!CollectionUtils.isEmpty(role.getRolePermissionsTemp())) {
			role.getRolePermissionsTemp().clear();
		}
		
		roleDto.getPermissions().stream().forEach(p -> {
			RolePermissionTemp rolePermissionTemp = new RolePermissionTemp();
			rolePermissionTemp.setRole(role);
			rolePermissionTemp.setPermission(mm.map(p, Permission.class));
			rolePermissionTemp.setInsUser(roleDto.getInsUser());
			rolePermissionTemp.setLastUpdUser(roleDto.getLastUpdUser());
			role.getRolePermissionsTemp().add(rolePermissionTemp);
		});
		
		roleDao.updateRole(role);
		logger.debug("Role record {} has updated successfully", roleDto.getRoleName());

		logger.info(CCLPConstants.EXIT);

	}

	@Override
	@Transactional
	public void changeRoleStatus(RoleDTO roleDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();

		Role role = roleDao.getRoleById(roleDto.getRoleId());
		if(role != null) {
			
		role.setStatus(roleDto.getStatus());
		role.setCheckerRemarks(roleDto.getCheckerRemarks());
		if (roleDto.getStatus().equalsIgnoreCase("approved")) {

			logger.debug("Role record {} has approved ", role.getRoleName());
			
			if (!CollectionUtils.isEmpty(role.getRolePermissions())){
				role.getRolePermissions().clear();
			}

			
			role.getRolePermissionsTemp().stream().forEach(p -> 
				role.getRolePermissions().add(mm.map(p, RolePermission.class))
			);
			
		}
		roleDao.updateRole(role);
		}else
		{
			logger.error("Role id does not exists");
			throw new ServiceException(ResponseMessages.ERR_ROLE_ID, ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);

	}

	@Override
	public void deleteRole(long roleId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Role role = roleDao.getRoleById(roleId);

		if (role == null) {
			logger.error("Role does not exists");
			throw new ServiceException(ResponseMessages.FAIL_ROLE_DELETE, ResponseMessages.DOESNOT_EXISTS);
		}
		try {
			roleDao.deleteRole(role);
		} catch (Exception e) {
			logger.error("Error while deleting record for '{}'", role.getRoleName());
			throw new ServiceException(ResponseMessages.FAIL_ROLE_DELETE, ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public Map<Object, Map<Object, Object>> getEntityPermissionMap() {
		logger.info(CCLPConstants.ENTER);
		List<Object[]> entityList = roleDao.getEntityPermissionMap();
		Map<Object, Map<Object, Object>> map = new HashMap<>();
		for (int i = 0; i < entityList.size(); i++) {
			Object[] obj = entityList.get(i);
			Map<Object, Object> opMap = new HashMap<>();
			opMap.put(obj[1], obj[2]);
			entityList.remove(i);
			i=i-1;
			for (int j = 0; j < entityList.size(); j++) {
				if (entityList.get(j)[0].equals(obj[0])) {
					opMap.put(entityList.get(j)[1], entityList.get(j)[2]);
					entityList.remove(j);
					j=j-1;
				}
			}
			map.put(obj[0], opMap);
		}

		logger.info(CCLPConstants.EXIT);
		return map;
	}

}
