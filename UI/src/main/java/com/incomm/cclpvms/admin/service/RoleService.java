package com.incomm.cclpvms.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.admin.model.Role;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface RoleService {
	
	public ResponseDTO createRole(Role role) throws ServiceException;

	public List<Role> getAllRoles() throws ServiceException;

	public ResponseDTO updateRole(Role role) throws ServiceException;

	public ResponseDTO deleteRole(long roleId);

	public List<Role> getRoleByName(String roleName);

	public ResponseDTO getRoleByRoleId(long roleId) throws ServiceException;
	
	public Map<String, Map<String, Long>> getEntityPermissionMap() throws ServiceException;

	public ResponseDTO approveRejectRole(Role roleForm) throws ServiceException;


}
