package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.impl.MetaDataDAOImpl;
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.PermissionDTO;
import com.incomm.cclp.dto.RoleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.ValidationService;



@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GroupServiceTest {

	@Autowired
	GroupService groupService;
	@Autowired
	RoleService roleService;
	@Autowired
	IssuerService issuerService;
	
	

	@Autowired
	MetaDataDAOImpl metadata;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test_validateGroupAs_null() throws ServiceException {
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);
	 try {
		GroupDTO groupdto =null;
		ValidationService.validateGroup(groupdto, true);
	 
	 	}catch(Exception e) {
		 assertEquals(validationErrBuilder.toString()+ResponseMessages.MESSAGE_DELIMITER + 

					ResponseMessages.ERR_GROUP_NULL,e.getMessage());
		 
	 }
	}
	@Test
	public void test_validateGroupNameAs_Empty() throws ServiceException {
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);
	 try {
		GroupDTO groupdto =new GroupDTO();
		ValidationService.validateGroup(groupdto, true);
	 
	 	}catch(Exception e) {
		 assertEquals(validationErrBuilder.toString()+ResponseMessages.MESSAGE_DELIMITER + 
					ResponseMessages.ERR_GROUP_NAME,e.getMessage());
		 
	 }
	}
	
	@Test
	public void test_validateGroupNameAs_selectList() throws ServiceException {
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);
	 try {
		GroupDTO groupdto =new GroupDTO();
		groupdto.setGroupName("Test");
		Set<String> selectedRoleList=new HashSet<String>();
		
		
		groupdto.setSelectedRoleList(selectedRoleList);
		ValidationService.validateGroup(groupdto, true);
	 
	 	}catch(Exception e) {
	 		
		 assertEquals(validationErrBuilder.toString()+ResponseMessages.MESSAGE_DELIMITER + 
					ResponseMessages.ERR_GROUP_ROLE_NAME,e.getMessage());
		 
	 }
	}
	/*
	 * Group create test classs
	 */
	@Test
	public void test_Group_Create() throws ServiceException {
		
		
		RoleDTO rolesdto=createRole();
		roleService.createRole(rolesdto);
		GroupDTO groupdto =new GroupDTO();
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesdto.getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		groupService.createGroup(groupdto);
		
		
		 assertEquals(groupService.getGroupId(groupdto).getGroupName(),groupdto.getGroupName());
	}
	/*
	 * Group update does not exist test case
	 */
	
	@Test
	public void test_Group_DoesNotExist() throws ServiceException {
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_CARDRANGE");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("CARDRANGE_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		GroupDTO groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		Long groupid=groupdto.getGroupId();
		
		groupService.createGroup(groupdto);
		
		GroupDTO getExistGroupDto=new GroupDTO();
		
		getExistGroupDto.setGroupId(groupid);
		
		GroupDTO existGroup=groupService.getGroupId(getExistGroupDto);
	
	
		assertEquals(groupService.getGroupId(groupdto).getGroupName(),groupdto.getGroupName());
		}catch(Exception e){
			assertEquals(e.getMessage(),ResponseMessages.ERR_GROUP_DOES_NOT_EXIST);
		}
	}
	/*
	 * Group update test 
	 */
	@Test
	public void test_Group_Update() throws ServiceException {
		
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("CARDRANGE_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		GroupDTO groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		
		
		groupService.createGroup(groupdto);
		//Group created and getting next
		
		Long groupid=groupdto.getGroupId();
		GroupDTO getExistGroupDto=new GroupDTO();
		
		getExistGroupDto.setGroupId(groupid);
		
		GroupDTO existGroup=groupService.getGroupId(getExistGroupDto);
		//updaet group 
		groupdto.setGroupStatus("UPDATED");
		groupService.updateGroup(groupdto);
		assertEquals(groupService.getGroupId(groupdto).getGroupStatus(),groupdto.getGroupStatus());
		
	}
	
	/*
	 * Group name already exist test case
	 */
	
	
	@Test
	public void test_Group_AlreadyExist() throws ServiceException {
		
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_CARDRANGE");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("CARDRANGE_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		GroupDTO groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		groupService.createGroup(groupdto);
		

		GroupDTO grpDto1 =new GroupDTO();
		
		grpDto1.setGroupName("Test");
		
		groupService.createGroup(grpDto1);
		
		}catch(Exception e){
			assertEquals(e.getMessage(),ResponseMessages.ERR_GROUP_NAME_EXIST);
		}
	
	}
	
	public RoleDTO createRole() throws ServiceException {

		metadata.createPermission();
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_CARDRANGE");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		
		
		
		//permissionDto.setInsDate(new java.sql.Date(new java.util.Date().getTime()));
		permissionDto.setLastUpdUser((long) 1);
		//permissionDto.setLastUpdDate(new java.sql.Date(new java.util.Date().getTime()));

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("CARDRANGE_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		
		
	return roleDto;
	}

	/*
	 * changes status to approve
	 */
	@Test
	public void test_Group_changeStatustoApprove() throws ServiceException {
		GroupDTO groupdto=null;
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_CARDRANGE");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("CARDRANGE_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		 groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		
		groupdto.setGroupStatus("NEW");
		groupService.createGroup(groupdto);
		//Group created and getting next
		
		Long groupid=groupdto.getGroupId();
		
		String status=groupService.changeStatus(groupid, "APPROVED", "Test checker",(long) 1);
		
		
		
		}catch(Exception e){
			assertEquals(groupService.getGroupId(groupdto).getGroupStatus(),groupdto.getGroupStatus());
		}
		
		
	}
	
	/*
	 * changes status to reject
	 */
	@Test
	public void test_Group_changeStatustoReject() throws ServiceException {
		GroupDTO groupdto=null;
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_CARDRANGE");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("CARDRANGE_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		 groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		
		groupdto.setGroupStatus("NEW");
		groupService.createGroup(groupdto);
		//Group created and getting next
		
		Long groupid=groupdto.getGroupId();
		
		String status=groupService.changeStatus(groupid, "REJECTED", "Test checker",(long) 1);
		
		groupdto=groupService.getGroupId(groupdto);
		
		}catch(Exception e){
			assertEquals(groupService.getGroupId(groupdto).getGroupStatus(),groupdto.getGroupStatus());
		}
		
		
	}
	
	@Test
	public void test_Group_changeStatustoRejectFailed_Case() throws ServiceException {
		GroupDTO groupdto=null;
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_CARDRANGE");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("CARDRANGE_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("NEW");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		 groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		
		groupdto.setGroupStatus("NEW");
		groupService.createGroup(groupdto);
		//Group created and getting next
		
		Long groupid=groupdto.getGroupId();
		
		
	
		groupdto=groupService.getGroupId(groupdto);
		groupdto.setGroupStatus("REJECTED");
		groupService.updateGroup(groupdto);
		
		String status=groupService.changeStatus(groupid, "REJECTED", "Test checker",(long) 1);
		}catch(Exception e){
			assertEquals(e.getMessage(),ResponseMessages.ERROR_GROUP_REJECT);
		}
		
		
	}
	@Test
	public void test_Group_changeStatustoAlready_Approve_Case() throws ServiceException {
		GroupDTO groupdto=null;
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("ISSUER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("APPROVED");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		 groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		
		groupdto.setGroupStatus("APPROVED");
		groupService.createGroup(groupdto);
		//Group created and getting next
		
		Long groupid=groupdto.getGroupId();
		
		//String status=groupService.changeStatus(groupid, "REJECTED", "Test checker");
		String status=groupService.changeStatus(groupid, "APPROVED", "Test checker",(long) 1);
		
		
		}catch(Exception e){
			assertEquals(groupService.getGroupId(groupdto).getGroupStatus(),groupdto.getGroupStatus());
		}
		
		
	}
	@Test
	public void test_Group_delete_DoesnotExist() throws ServiceException {
		try{
		GroupDTO groupdto=null;
		
		 groupdto =new GroupDTO();
		
		
		groupService.deleteGroup(groupdto);
		}catch(Exception  e){
			assertEquals(ResponseMessages.ERR_GROUP_DOES_NOT_EXIST,e.getMessage());
		}
		
		
		
	}
	
	@Test
	public void test_Group_delete() throws ServiceException {
		GroupDTO groupdto=null;
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("ISSUER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("APPROVED");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		 groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		
		groupdto.setGroupStatus("APPROVED");
		groupService.createGroup(groupdto);
		//Group created and getting next
		groupService.deleteGroup(groupdto);
		
		}catch(Exception e){
			assertEquals(groupService.getGroups().size(),1);
		}
		
		
		
	}
	@Test
	public void test_Group_getAll() throws ServiceException {
		GroupDTO groupdto=null;
		try{
		metadata.createPermission();
		
		Set<PermissionDTO> permissionSet = new HashSet<>();
		PermissionDTO permissionDto = new PermissionDTO();
		permissionDto.setPermissionId(1);
		permissionDto.setPermissionName("SEARCH_PARTNER");
		permissionDto.setDescription("Allows to Search a Partner");
		permissionDto.setInsUser((long) 1);
		permissionDto.setLastUpdUser((long) 1);
		

		permissionSet.add(permissionDto);
		RoleDTO roleDto = new RoleDTO();
		roleDto.setRoleName("ISSUER_CONFIGURATOR");
		roleDto.setRoleDesc("Allows to Configure a partner");
		roleDto.setStatus("APPROVED");
		roleDto.setPermissions(permissionSet);
		roleDto.setInsUser(1);
		roleDto.setLastUpdUser((long) 1);
		roleService.createRole(roleDto);
		
		
		
		
		
		//Group Section 
		List<RoleDTO> rolesDtoList=roleService.getAllRoles();
		
		 groupdto =new GroupDTO();
		
		groupdto.setGroupName("Test");
		Set<String> roleSet=new HashSet<String>();
		String roleId=String.valueOf(rolesDtoList.get(0).getRoleId());
		roleSet.add(roleId);
		groupdto.setSelectedRoleList(roleSet);
		//For getting transient state
		
		groupdto.setGroupStatus("APPROVED");
		groupService.createGroup(groupdto);
		//Group created and getting next
		
		
		
		//String status=groupService.changeStatus(groupid, "REJECTED", "Test checker");
		
		groupService.getGroups();
		
		}catch(Exception e){
			assertEquals(groupService.getGroups().size(),1);
		}
		
		
	}
}
