package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.Role;

public interface RoleDAO {

	List<Role> getAllRoles();

	List<Role> getRoleByName(String roleName);

	void createRole(Role role);

	Role getRoleById(long roleId);

	List<Object[]> getEntityPermissionMap();

	void updateRoleStatus(Role role);

	void updateRole(Role role);

	void deleteRole(Role role);

}
