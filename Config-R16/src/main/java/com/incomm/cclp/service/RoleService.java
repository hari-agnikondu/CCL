package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.RoleDTO;
import com.incomm.cclp.exception.ServiceException;

public interface RoleService {

	List<RoleDTO> getAllRoles();

	List<RoleDTO> getRoleByName(String roleName);

	void createRole(RoleDTO roleDto) throws ServiceException;

	RoleDTO getRoleById(long roleId);

	void updateRole(RoleDTO roleDto) throws ServiceException;

	Map<Object, Map<Object, Object>> getEntityPermissionMap();

	void deleteRole(long roleId) throws ServiceException;

	void changeRoleStatus(RoleDTO roleDto) throws ServiceException;

}
