package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.impl.MetaDataDAOImpl;
import com.incomm.cclp.dto.PermissionDTO;
import com.incomm.cclp.dto.RoleDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RoleServiceTest {

	@Autowired
	RoleService roleService;

	@Autowired
	MetaDataDAOImpl metadata;
	
	public void create_permission() {
		metadata.createPermission(); 
	}
	
	/*
	 * getting all the roles if it is empty it will return empty list
	 */
	@Test
	public void get_all_roles_test() {
		create_role_firstTime_test();
		List<RoleDTO> roleList = roleService.getAllRoles();
		assertEquals(1, roleList.size());
	}
	
	/*
	 * First Time Creating a role with all values defined
	 */
	@Test
	public void create_role_firstTime_test() {

		create_permission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Creating a role with already existing role name
	 */
	@Test
	public void create_role_with_already_existing_role() {
		
		create_role_firstTime_test();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
	}
	
	/*
	 * Getting  already existing role by Passing roleId
	 */
	//@Test
	public void get_role_by_roleId() {

		create_permission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);

		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		RoleDTO roleDTO = roleService.getRoleById(9);
		assertNull(roleDTO);
	}
	
	/*
	 * Getting a roleDTO  by Passing non existing roleId as input
	 */
	@Test
	public void get_role_by_nonExisting_roleId() {
		
		RoleDTO roleDTO = roleService.getRoleById(7456);
		assertNull(roleDTO);
	}

	
	/*
	 * update a role with other values
	 */
	@Test
	public void update_role_with_newValues() {
		
		create_permission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
		
		roleDto.setRoleDesc("Allow to configure a partner with Product");
		try {
			roleService.updateRole(roleDto);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * update a role with already existing role name
	 */
	@Test
	public void update_role_with_already_existing_role_name() {
		
		create_permission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
		
		Set<PermissionDTO> permissionSet2 = new HashSet<>();
		PermissionDTO permissionDto2 = new PermissionDTO();
		permissionDto2.setPermissionId(1);
		permissionDto2.setPermissionName("SEARCH_ISSUER");
		permissionDto2.setDescription("Allows to Search a Partner");
		permissionDto2.setInsUser((long) 1);
		//permissionDto2.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto2.setLastUpdUser((long) 1);
		//permissionDto2.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet2.add(permissionDto2);
		RoleDTO roleDto2 = new RoleDTO();
		roleDto2.setRoleName("ISSUER_CONFIGURATOR");
		roleDto2.setRoleDesc("Allows to Configure a Issuer");
		roleDto2.setStatus("NEW");
		roleDto2.setPermissions(permissionSet2);
		roleDto2.setInsUser(1);
		roleDto2.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto2);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
		
		roleDto2.setRoleName("PARTNER_CONFIGURATOR");
		try {
			roleService.updateRole(roleDto2);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(),ResponseMessages.ERR_ROLE_ID);
		}
	}
	
	
	/*
	 * update a role with already existing role name
	 */
	@Test
	public void update_role_with_already_existing_role_name_as_substring() {
		
		create_permission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR_FIRST");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
		
		Set<PermissionDTO> permissionSet2 = new HashSet<>();
		PermissionDTO permissionDto2 = new PermissionDTO();
		permissionDto2.setPermissionId(1);
		permissionDto2.setPermissionName("SEARCH_ISSUER");
		permissionDto2.setDescription("Allows to Search a Partner");
		permissionDto2.setInsUser((long) 1);
		//permissionDto2.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto2.setLastUpdUser((long) 1);
		//permissionDto2.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet2.add(permissionDto2);
		RoleDTO roleDto2 = new RoleDTO();
		roleDto2.setRoleName("ISSUER_CONFIGURATOR");
		roleDto2.setRoleDesc("Allows to Configure a Issuer");
		roleDto2.setStatus("NEW");
		roleDto2.setPermissions(permissionSet2);
		roleDto2.setInsUser(1);
		roleDto2.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto2);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
		
		roleDto2.setRoleName("PARTNER_CONFIGURATOR");
		roleDto2.setRoleId(9);
		try {
			roleService.updateRole(roleDto2);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(),ResponseMessages.ERR_ROLE_ID);
		}
	}
	
	
	
	/*
	 * update a role with new Status
	 */
	//@Test
	public void update_role_with_approved_status() {
		
		create_permission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
		
		roleDto.setStatus("APPROVED");
		roleDto.setRoleId(5);
		try {
			roleService.changeRoleStatus(roleDto);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * update a role with new Status
	 */
	@Test
	public void update_role_with_declined_status() {
		
		create_permission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("PARTNER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		try {
			roleService.createRole(roleDto);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_ROLE_EXISTS);
		}
		
		roleDto.setStatus("DECLINED");
		roleDto.setRoleId(3);
		try {
			roleService.changeRoleStatus(roleDto);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * deleting a existing role with roleId
	 */
	@Test
	public void delete_existing_role() {
		create_role_firstTime_test();
		try {
			roleService.deleteRole(1);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		RoleDTO roleDTO = roleService.getRoleById(1);
		assertNull(roleDTO);	
	}
	
	/*
	 * deleting a role with non existing roleId
	 */
	@Test
	public void delete_nonExisting_role() {
		create_role_firstTime_test();
		try {
			roleService.deleteRole(10);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(),ResponseMessages.FAIL_ROLE_DELETE);
		}	
	}
	
	@Test
	public void get_entity_map() {
		
		metadata.createPermission(); 
		Map<Object,Map<Object,Object>> map  = roleService.getEntityPermissionMap();
		assertEquals(1,map.size());
	}
	
	
}
