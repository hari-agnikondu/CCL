
package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

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

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ClpUserDTO;
import com.incomm.cclp.exception.ServiceException;

/**
 * @author abutani
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	GroupAccessService groupAccessService;

	private ClpUserDTO adminUser;
	private ClpUserDTO issuerUser;

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

		// Setup user dto

		// 1. Set Permissions
		/*
		 * Set<PermissionDTO> issuerPermissions = new HashSet<PermissionDTO>();
		 * Set<PermissionDTO> partnerPermissions = new HashSet<PermissionDTO>();
		 * 
		 * PermissionDTO permissionDto = new PermissionDTO(); PermissionDTO
		 * permissionDto1 = new PermissionDTO(); PermissionDTO permissionDto2 = new
		 * PermissionDTO(); PermissionDTO permissionDto3 = new PermissionDTO();
		 * 
		 * permissionDto.setPermissionName("SEARCH_ISSUER");
		 * permissionDto1.setPermissionName("ADD_ISSUER");
		 * permissionDto2.setPermissionName("SEARCH_PARTNER");
		 * permissionDto3.setPermissionName("ADD_PARTNER");
		 * 
		 * issuerPermissions.add(permissionDto); issuerPermissions.add(permissionDto1);
		 * 
		 * partnerPermissions.add(permissionDto2);
		 * partnerPermissions.add(permissionDto3);
		 * 
		 * 
		 * // 2. Set Roles Set<RoleDTO> adminRoles = new HashSet<RoleDTO>();
		 * Set<RoleDTO> issuerRoles = new HashSet<RoleDTO>();
		 * 
		 * RoleDTO roleDto = new RoleDTO(); roleDto.setRoleName("ISSUER_CONFIGURATOR");
		 * roleDto.setPermissions(issuerPermissions);
		 * 
		 * RoleDTO roleDto1 = new RoleDTO();
		 * roleDto1.setRoleName("PARTNER_CONFIGURATOR");
		 * roleDto1.setPermissions(partnerPermissions);
		 * 
		 * adminRoles.add(roleDto); adminRoles.add(roleDto1);
		 * 
		 * issuerRoles.add(roleDto);
		 * 
		 * // 3. Set Groups Set<GroupDTO> groupDtos = new HashSet<GroupDTO>();
		 * Set<GroupDTO> groupDtos1 = new HashSet<GroupDTO>(); List<GroupDTO> groupDtos
		 * = new ArrayList<GroupDTO>(); List<GroupDTO> groupDtos1 = new
		 * ArrayList<GroupDTO>();
		 * 
		 * GroupDTO groupDto = new GroupDTO(); groupDto.setGroupName("ADMIN");
		 * groupDto.setRoles(adminRoles);
		 * 
		 * GroupDTO groupDto1 = new GroupDTO(); groupDto1.setGroupName("ISSUER_GROUP");
		 * groupDto1.setRoles(issuerRoles);
		 * 
		 * groupDtos.add(groupDto); groupDtos1.add(groupDto1);
		 */

		// 4. Set User
		adminUser = new ClpUserDTO();
		adminUser.setUserLoginId("admin_user");
		adminUser.setUserName("ADMIN USER");
		adminUser.setUserEmail("admin@incomm.com");
		adminUser.setUserStatus("NEW");
		/* adminUser.setGroups(groupDtos); */

		issuerUser = new ClpUserDTO();
		issuerUser.setUserLoginId("issuer_user");
		issuerUser.setUserName("ISSUER USER");
		issuerUser.setUserEmail("issuer@incomm.com");
		/* issuerUser.setGroups(groupDtos); */

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.cclp.service.impl.UserServiceImpl#createUser(com.cclp.dto.ClpUserDTO)}.
	 * 
	 * @throws ServiceException
	 */
	@Test
	@Transactional
	public void testCreateUser() throws ServiceException {

		userService.createUser(adminUser);

		/*assertEquals("admin_user", userService.getUserByUserName("admin_user").getUserLoginId());*/
		assertEquals("admin_user", userService.getUserByUserId(adminUser.getUserId()).getUserLoginId());
	}

	/**
	 * Test method for
	 * {@link com.cclp.service.impl.UserServiceImpl#getUserByUserName(com.cclp.dto.ClpUserDTO)}.
	 * 
	 * @throws ServiceException
	 */
	/*
	 * @Test public void testGetUserByUserName() throws ServiceException {
	 * 
	 * userService.createUser(adminUser); userService.createUser(issuerUser);
	 * 
	 * assertEquals(adminUser, userService.getUserByUserName("admin_user").
	 * getGroups().size());
	 * 
	 * }
	 */

	/**
	 * Test method for
	 * {@link com.cclp.service.impl.UserServiceImpl#updateLastLoginTime()}.
	 * 
	 * @throws ServiceException
	 */
	@Test
	public void testUpdateLastLoginTime() throws ServiceException {

		userService.createUser(adminUser);

		/*assertEquals(null, userService.getUserByUserName("admin_user").getLastLoginTime());*/
		assertEquals(null, userService.getUserByUserId(adminUser.getUserId()).getLastLoginTime());

		userService.updateLastLoginTime(adminUser.getUserId());

	}

	@Test
	public void testgetAllUsers() throws ServiceException {
		userService.createUser(adminUser);
		userService.createUser(issuerUser);

		assertEquals(2, userService.getAllUsers().size());
	}

	@Test
	public void testcreatUser_Duplicate_UserLoginId() throws ServiceException {
		userService.createUser(adminUser);

		String errorMsg = "USER_LOGINID_" + ResponseMessages.ALREADY_EXISTS;

		try {
			userService.createUser(issuerUser);
		} catch (ServiceException se) {

			assertEquals(errorMsg, se.getMessage());
		}
	}

	@Test
	public void testcreatUser_Duplicate_UserName() throws ServiceException {

		ClpUserDTO duplicateUser = new ClpUserDTO();
		duplicateUser.setUserName("ADMIN USER");

		userService.createUser(adminUser);

		String errorMsg = "USER_USERNAME_" + ResponseMessages.ALREADY_EXISTS;

		try {
			userService.createUser(duplicateUser);
		} catch (ServiceException se) {

			assertEquals(errorMsg, se.getMessage());
		}
	}

	@Test
	public void testcreatUser_Duplicate_UserEmail() throws ServiceException {

		ClpUserDTO duplicateUser = new ClpUserDTO();
		duplicateUser.setUserEmail("admin@gmail.com");

		userService.createUser(adminUser);

		String errorMsg = "USER_EMAIL_" + ResponseMessages.ALREADY_EXISTS;

		try {
			userService.createUser(duplicateUser);
		} catch (ServiceException se) {

			assertEquals(errorMsg, se.getMessage());
		}
	}

	@Test
	public void testUpdateUser() throws ServiceException {

		userService.createUser(adminUser);

		adminUser.setUserName("ADMIN");

		userService.updateUser(adminUser);

		assertEquals("ADMIN", userService.getUserByUserId(adminUser.getUserId()).getUserName());

	}

	@Test
	public void testUpdateUser_Duplicate_UserName() throws ServiceException {

		userService.createUser(adminUser);
		userService.createUser(issuerUser);

		adminUser.setUserName("ISSUER USER");

		String errorMsg = "USER_USERNAME_" + ResponseMessages.ALREADY_EXISTS;

		try {
			userService.updateUser(issuerUser);
		} catch (ServiceException se) {

			assertEquals(errorMsg, se.getMessage());
		}
	}

	@Test
	public void testUpdateUser_Duplicate_UserEmail() throws ServiceException {

		userService.createUser(adminUser);
		userService.createUser(issuerUser);

		issuerUser.setUserEmail("admin@gmail.com");

		String errorMsg = "USER_EMAIL_" + ResponseMessages.ALREADY_EXISTS;

		try {
			userService.updateUser(issuerUser);
		} catch (ServiceException se) {

			assertEquals(errorMsg, se.getMessage());
		}
	}

	@Test
	public void testUpdateUser_Duplicate_UserLoginId() throws ServiceException {

		userService.createUser(adminUser);
		userService.createUser(issuerUser);

		issuerUser.setUserLoginId("admin_user");

		String errorMsg = "USER_LOGINID_" + ResponseMessages.ALREADY_EXISTS;

		try {
			userService.updateUser(issuerUser);
		} catch (ServiceException se) {

			assertEquals(errorMsg, se.getMessage());
		}
	}

	@Test
	public void testchangeUserStatus_ToApprove() throws ServiceException {

		userService.createUser(adminUser);
		
		assertEquals("NEW", userService.getUserByUserId(adminUser.getUserId()).getUserStatus());
		
		adminUser.setUserStatus(CCLPConstants.USER_STATUS_APPROVED);
		
		userService.changeUserStatus(adminUser);
		
		assertEquals(CCLPConstants.USER_STATUS_APPROVED, userService.getUserByUserId(adminUser.getUserId()).getUserStatus());

	}
	
	@Test
	public void testchangeUserStatus_ToRejected() throws ServiceException {

		userService.createUser(adminUser);
		
		assertEquals("NEW", userService.getUserByUserId(adminUser.getUserId()).getUserStatus());
		
		adminUser.setUserStatus(CCLPConstants.USER_STATUS_REJECTED);
		
		userService.changeUserStatus(adminUser);
		
		assertEquals(CCLPConstants.USER_STATUS_REJECTED, userService.getUserByUserId(adminUser.getUserId()).getUserStatus());

	}
	
	@Test
	public void testDeleteUser() throws ServiceException {

		userService.createUser(adminUser);
		
		String errorMsg = "CLPUSER" + ResponseMessages.DOESNOT_EXISTS;

		try {
			/*userService.deleteUser(adminUser);*/
			userService.deleteUserById(adminUser.getUserId());
		} catch (ServiceException se) {

			assertEquals(errorMsg, se.getMessage());
		}
	}
}
