/**
 * 
 */
package com.incomm.cclp.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.UserDAO;
import com.incomm.cclp.domain.ClpUser;
import com.incomm.cclp.domain.Group;
import com.incomm.cclp.domain.GroupRole;
import com.incomm.cclp.domain.Role;
import com.incomm.cclp.domain.RolePermission;
import com.incomm.cclp.domain.UserGroup;
import com.incomm.cclp.domain.UserGroupTemp;
import com.incomm.cclp.dto.ClpUserDTO;
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.PermissionDTO;
import com.incomm.cclp.dto.RoleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.UserService;

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
	UserDAO userDao;
	
	private  final Logger logger = LogManager.getLogger(this.getClass());
	/**
	 * Creates a new user
	 * 
	 * @param userDto The user to be created.
	 * 
	 */
	@Override
	public void createUser(ClpUserDTO userDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		List<ClpUserDTO> existingUsers = getAllUsers();

		if (!CollectionUtils.isEmpty(existingUsers)) {
			Long matchingUserNameCount = existingUsers.stream()
					.filter(clpuser -> clpuser.getUserName().equals(userDto.getUserName())).count();
			if (matchingUserNameCount > 0) {
				logger.error("userName already present");
				throw new ServiceException(("USER_USERNAME_" + ResponseMessages.ALREADY_EXISTS),
						ResponseMessages.ALREADY_EXISTS);
			}

			Long matchingUserLoginIdCount = existingUsers.stream()
					.filter(clpuser -> clpuser.getUserLoginId().equals(userDto.getUserLoginId())).count();
			if (matchingUserLoginIdCount > 0) {
				logger.error("user login ID already present");
				throw new ServiceException(("USER_LOGINID_" + ResponseMessages.ALREADY_EXISTS),
						ResponseMessages.ALREADY_EXISTS);
			}

			Long matchingEmailIdCount = existingUsers.stream()
					.filter(clpuser -> clpuser.getUserEmail().equals(userDto.getUserEmail())).count();
			if (matchingEmailIdCount > 0) {
				logger.error("Email ID already present");
				throw new ServiceException(("USER_EMAIL_" + ResponseMessages.ALREADY_EXISTS),
						ResponseMessages.ALREADY_EXISTS);
			}
		}
		
		ClpUser user = constructClpUser(userDto);
		userDao.createUser(user);

		logger.info("User {} added successfully with user id : {}", user.getUserName(), user.getUserId());

		userDto.setUserId(user.getUserId());

		logger.info(CCLPConstants.EXIT);
	}
	
	private ClpUser constructClpUser(ClpUserDTO userDto) {
		logger.info(CCLPConstants.ENTER);
		ClpUser user = new ClpUser();

		user.setUserId(userDto.getUserId());
		user.setUserLoginId(userDto.getUserLoginId());
		user.setUserName(userDto.getUserName());
		user.setUserEmail(userDto.getUserEmail());
		user.setUserContactNumber(userDto.getUserContactNumber());
		user.setUserStatus(userDto.getUserStatus());
		
		user.setLastLoginTime(userDto.getLastLoginTime());
		user.setInsUser(userDto.getInsUser());
		user.setInsDate(userDto.getInsDate());
		user.setLastUpdUser(userDto.getLastUpdUser());
		user.setLastUpdDate(userDto.getLastUpdDate());
		
		List<GroupDTO> groupDtos = userDto.getGroups();		
		if (!CollectionUtils.isEmpty(groupDtos)) {
			List<UserGroupTemp> listUserGroupTemp = new ArrayList<>();
			
			groupDtos.stream().forEach(groupDto -> {
				ModelMapper mm = new ModelMapper();
				Group group = mm.map(groupDto, Group.class);
				if (!Objects.isNull(group)) {
					UserGroupTemp userGroupTemp = new UserGroupTemp();
					userGroupTemp.setClpUser(user);
					userGroupTemp.setGroup(group);
					userGroupTemp.setInsUser(userDto.getInsUser());
					userGroupTemp.setInsDate(userDto.getInsDate());
					userGroupTemp.setLastUpdUser(userDto.getLastUpdUser());
					userGroupTemp.setLastUpdDate(userDto.getLastUpdDate());

					listUserGroupTemp.add(userGroupTemp);
				}
			});
			
			if (!CollectionUtils.isEmpty(listUserGroupTemp)) {
				user.setListUserGroupTemp(listUserGroupTemp);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return user;
	}

	/**
	 * Gets an User by user name.
	 * 
	 * @param userName The user name for the User to be retrieved.
	 * 
	 * @return the User.
	 * @throws ServiceException 
	 */
	@Override
	public ClpUserDTO getUserByUserName(String userName) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ClpUserDTO userDto = null;
		ModelMapper mm = new ModelMapper();

		int count = 0;
		count = userDao.countOfUserByUserLoginID(userName);
		if (count != 1) {
			logger.error("User record with user login id: {} does not exist", userName);

			throw new ServiceException(("USER" + ResponseMessages.DOESNOT_EXISTS),
					ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info("Getting user details..");	
		ClpUser clpUser = userDao.getUserByUserName(userName);

		if (!Objects.isNull(clpUser)) {
			userDto = new ClpUserDTO();
			userDto.setUserId(clpUser.getUserId());
			userDto.setUserLoginId(clpUser.getUserLoginId());
			userDto.setLastLoginTime(clpUser.getLastLoginTime());
			userDto.setUserName(clpUser.getUserName());
			userDto.setCheckerRemarks(clpUser.getCheckerRemarks());
			userDto.setAccessStatus(clpUser.getAccessStatus());
			
			List<UserGroup> listUserGroups = clpUser.getListUserGroup();
			List<GroupDTO> groups = new ArrayList<>();

			if (!CollectionUtils.isEmpty(listUserGroups)) {
				listUserGroups.stream().forEach(usergroup -> {
					Group group = usergroup.getGroup();
					if (!Objects.isNull(group)) {
						GroupDTO groupDto = new GroupDTO();
						groupDto.setGroupId(group.getGroupId());
						groupDto.setGroupName(group.getGroupName());

						List<GroupRole> listGroupRoles = group.getGroupRole();
						Set<RoleDTO> roles = new HashSet<>();
						if (!CollectionUtils.isEmpty(listGroupRoles)) {
							listGroupRoles.stream().forEach(grouprole -> {
								Role role = grouprole.getRole();
								if (!Objects.isNull(role)) {
									RoleDTO roleDto = new RoleDTO();
									roleDto.setRoleId(role.getRoleId());
									roleDto.setRoleName(role.getRoleName());

									Set<RolePermission> rolePermissions = role.getRolePermissions();
									Set<PermissionDTO> permissionDtos = new HashSet<>();
									if (!CollectionUtils.isEmpty(rolePermissions)) {
										rolePermissions.stream().forEach(rolepermission -> {
											PermissionDTO permissionDto = new PermissionDTO();
											permissionDto = mm.map(rolepermission.getPermission(), PermissionDTO.class);
											permissionDtos.add(permissionDto);
										});
										roleDto.setPermissions(permissionDtos);
										roles.add(roleDto);
									}
								}
							});
							groupDto.setRoles(roles);
							groups.add(groupDto);
						}						
					}
				});
				userDto.setGroups(groups);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return userDto;

	}

	
	/**
	 * Updates last login time for user.
	 * 
	 * @param userId The userId for the User to be updated.
	 * 
	 */
	@Override
	public void updateLastLoginTime(long userId) {
		logger.info(CCLPConstants.ENTER);
		userDao.updateLastLoginTime(userId);
		logger.info(CCLPConstants.EXIT);
	}
	
	
	@Override
	public List<ClpUserDTO> getUsersByName(String userName) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getUsersByName with data : "+userName);
		ModelMapper mm = new ModelMapper();		
	    java.lang.reflect.Type targetListType = new 
	    		TypeToken<List<ClpUserDTO>>() {}.getType();
	   logger.info("after retrieving data for User name : "+userName);
	   logger.info(CCLPConstants.EXIT);
	    return mm.map(userDao.getUsersByName(userName), 
	    		targetListType);

	}

	/**
	 * Gets all Users.
	 * 
	 * @return the list of all Users.
	 */
	@Override
	public List<ClpUserDTO> getAllUsers() {
		logger.info(CCLPConstants.ENTER);
		
		List<ClpUserDTO> clpUserDTOList = new ArrayList<>();
		List<ClpUser> clpUserList = userDao.getAllUsers();
		
		if (!CollectionUtils.isEmpty(clpUserList)) {
			clpUserDTOList = clpUserList.stream()
					.map(clpUser -> new ClpUserDTO(
							clpUser.getUserId(),
							clpUser.getUserLoginId(),
							clpUser.getLastLoginTime(),
							clpUser.getUserName(),
							clpUser.getUserEmail(),
							clpUser.getUserContactNumber(),
							clpUser.getUserStatus(),
							clpUser.getCheckerRemarks(),
							clpUser.getInsUser(),
							clpUser.getInsDate(),
							clpUser.getLastUpdUser(),
							clpUser.getLastUpdDate(),
							getGroupDtosFromUserGroupsList(clpUser.getListUserGroupTemp()),
							clpUser.getAccessStatus()))
					.collect(Collectors.toList());
		}

		logger.info(CCLPConstants.EXIT);

		return clpUserDTOList;
	}

	private List<GroupDTO> getGroupDtosFromUserGroupsList(List<UserGroupTemp> userGroupsTemp){
		logger.info(CCLPConstants.ENTER);
		List<GroupDTO> groupDtos = new ArrayList<>();
		
		if (!CollectionUtils.isEmpty(userGroupsTemp)) {
			ModelMapper mm = new ModelMapper();
			userGroupsTemp.stream().forEach(userGroup -> {
				if (!Objects.isNull(userGroup.getGroup())) {
					logger.info(userGroup.getGroup().toString());
					groupDtos.add(mm.map(userGroup.getGroup(), GroupDTO.class));
				}
			});
		}
		logger.info(CCLPConstants.EXIT);
		return groupDtos;
	}
	
	
	private List<ClpUserDTO> toClpUserDTO(List<ClpUser> clpUserList){
		logger.info(CCLPConstants.ENTER);
		List<ClpUserDTO> clpUserDTOList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(clpUserList)) {
			clpUserDTOList = clpUserList.stream()
					.map(clpUser -> new ClpUserDTO(
							clpUser.getUserId(),
							clpUser.getUserLoginId(),
							clpUser.getLastLoginTime(),
							clpUser.getUserName(),
							clpUser.getUserEmail(),
							clpUser.getUserContactNumber(),
							clpUser.getUserStatus(),
							clpUser.getCheckerRemarks(),
							clpUser.getInsUser(),
							clpUser.getInsDate(),
							clpUser.getLastUpdUser(),
							clpUser.getLastUpdDate(),
							getGroupDtosFromUserGroupsList(clpUser.getListUserGroupTemp()),
							clpUser.getAccessStatus()))
					.collect(Collectors.toList());
		}
		logger.info(CCLPConstants.EXIT);
		return clpUserDTOList;
	}

	
	/**
	 * Getting list of ClpUserDTO by User Login Id and User Name 
	 * @param   userLoginId, userName
	 */
	@Override
	public List<ClpUserDTO> getUserByUserLoginIdAndUserName(String userLoginId, String userName) {
		logger.info(CCLPConstants.ENTER);

		List<ClpUser> clpUserList = userDao.getUserByUserLoginIdAndUserName(userLoginId, userName);

		logger.info(CCLPConstants.EXIT);
		
		return toClpUserDTO(clpUserList);
	}

	/**
	 * Get ClpUserDTO by User Id 
	 * @param   userId
	 */
	@Override
	public ClpUserDTO getUserByUserId(Long userId) {
		logger.info(CCLPConstants.ENTER);

		ClpUser user = userDao.getUserByUserId(userId);
		if (Objects.isNull(user)) {
			logger.error("Error while fetching record for userId: {}", userId);
			logger.info(CCLPConstants.EXIT);
			return null;
		}
		logger.info(CCLPConstants.EXIT);
		return new ClpUserDTO(user.getUserId(), user.getUserLoginId(), user.getLastLoginTime(), user.getUserName(),
				user.getUserEmail(), user.getUserContactNumber(), user.getUserStatus(), user.getCheckerRemarks(),
				user.getInsUser(), user.getInsDate(), user.getLastUpdUser(), user.getLastUpdDate(),
				getGroupDtosFromUserGroupsList(user.getListUserGroupTemp()),user.getAccessStatus());
	}

	@Override
	public void updateUser(ClpUserDTO userDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		ClpUser existingUser = userDao.getUserByUserId(userDto.getUserId());
		
		if (Objects.isNull(existingUser)) {
			logger.error("User record not found for Id: {}", userDto.getUserId());
			
			throw new ServiceException(("USER" + ResponseMessages.DOESNOT_EXISTS),
					ResponseMessages.DOESNOT_EXISTS);
		}
		
		List<ClpUserDTO> existingUsers = getAllUsers();

		if (!CollectionUtils.isEmpty(existingUsers)) {
			Long matchingUserNameCount = existingUsers.stream()
					.filter(clpuser -> clpuser.getUserName().equals(userDto.getUserName())
							&& (!userDto.getUserId().equals(userDto.getUserId()))).count();
			if (matchingUserNameCount > 0) {
				
				logger.error("User record already exist with User Name : {}", userDto.getUserName());

				throw new ServiceException(("USER_USERNAME_" + ResponseMessages.ALREADY_EXISTS),
						ResponseMessages.ALREADY_EXISTS);
			}

			Long matchingUserLoginIdCount = existingUsers.stream()
					.filter(clpuser -> clpuser.getUserLoginId().equals(userDto.getUserLoginId())
							&& (!userDto.getUserId().equals(userDto.getUserId()))).count();
			if (matchingUserLoginIdCount > 0) {
				logger.error("User record already exist with User Login Id : {}", userDto.getUserLoginId());
				
				throw new ServiceException(("USER_LOGINID_" + ResponseMessages.ALREADY_EXISTS),
						ResponseMessages.ALREADY_EXISTS);
			}

			Long matchingEmailIdCount = existingUsers.stream()
					.filter(clpuser -> clpuser.getUserEmail().equals(userDto.getUserEmail())
							&& (!userDto.getUserId().equals(userDto.getUserId()))).count();
			if (matchingEmailIdCount > 0) {
				logger.error("User record already exist with Email id : {}", userDto.getUserEmail());
				
				throw new ServiceException(("USER_EMAIL_" + ResponseMessages.ALREADY_EXISTS),
						ResponseMessages.ALREADY_EXISTS);
			}
			logger.info(CCLPConstants.ENTER);
		}
		
		existingUser.setUserId(userDto.getUserId());
		existingUser.setUserLoginId(userDto.getUserLoginId());
		existingUser.setUserName(userDto.getUserName());
		existingUser.setUserEmail(userDto.getUserEmail());
		existingUser.setUserContactNumber(userDto.getUserContactNumber());
		existingUser.setUserStatus(userDto.getUserStatus());
		existingUser.setInsUser(userDto.getInsUser());
		existingUser.setInsDate(userDto.getInsDate());
		existingUser.setLastUpdUser(userDto.getLastUpdUser());
		existingUser.setLastUpdDate(userDto.getLastUpdDate());
		existingUser.setAccessStatus(existingUser.getAccessStatus());
		
		if (!CollectionUtils.isEmpty(existingUser.getListUserGroupTemp())) {
			existingUser.getListUserGroupTemp().clear();
		}
		
		List<GroupDTO> groupDtos = userDto.getGroups();		
		if (!CollectionUtils.isEmpty(groupDtos)) {
			
			groupDtos.stream().forEach(groupDto -> {
				ModelMapper mm = new ModelMapper();
				Group group = mm.map(groupDto, Group.class);
				if (!Objects.isNull(group)) {
					UserGroupTemp userGroupTemp = new UserGroupTemp();
					userGroupTemp.setClpUser(existingUser);
					userGroupTemp.setGroup(group);
					userGroupTemp.setInsUser(userDto.getInsUser());
					userGroupTemp.setInsDate(userDto.getInsDate());
					userGroupTemp.setLastUpdUser(userDto.getLastUpdUser());
					userGroupTemp.setLastUpdDate(userDto.getLastUpdDate());

					existingUser.getListUserGroupTemp().add(userGroupTemp);
				}
			});
		}
		logger.info("updating user..");
		userDao.updateUser(existingUser);
		
		logger.info(CCLPConstants.EXIT);
	}
	
	@Override
	public void changeUserStatus(ClpUserDTO userDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ClpUser existingUser = userDao.getUserByUserId(userDto.getUserId());		

		if (Objects.isNull(existingUser)) {
			logger.error("User record with user id: {} does not exist", userDto.getUserId());

			throw new ServiceException(("CLPUSER" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		} else {

			if (CCLPConstants.USER_STATUS_APPROVED.equals(userDto.getUserStatus())) {
				if (CCLPConstants.USER_STATUS_APPROVED.equals(existingUser.getUserStatus())) {
					logger.error("User record with user id: {} already approved", userDto.getUserId());

					throw new ServiceException(ResponseMessages.USER_ALREADY_APPROVED,
							ResponseMessages.USER_ALREADY_APPROVED);
				} else {
					existingUser.setUserStatus(CCLPConstants.USER_STATUS_APPROVED);
					existingUser.setCheckerRemarks(userDto.getCheckerRemarks());
					existingUser.setAccessStatus(CCLPConstants.ACCESS_STATUS_ACTIVE);
					
					if (!CollectionUtils.isEmpty(existingUser.getListUserGroup())) {
						existingUser.getListUserGroup().clear();						
					}
					
					if (!CollectionUtils.isEmpty(existingUser.getListUserGroupTemp())) {
						existingUser.getListUserGroupTemp().stream().forEach(userGroupTemp -> {
							UserGroup userGroup = new UserGroup();
							userGroup.setClpUser(userGroupTemp.getClpUser());
							userGroup.setGroup(userGroupTemp.getGroup());
							userGroup.setInsUser(userDto.getInsUser());
							userGroup.setInsDate(userDto.getInsDate());
							userGroup.setLastUpdUser(userDto.getLastUpdUser());
							userGroup.setLastUpdDate(userDto.getLastUpdDate());

							existingUser.getListUserGroup().add(userGroup);
						});
					}
					
					try {
						userDao.updateUser(existingUser);
						
						logger.info("User record with user id: {} successfully approved", userDto.getUserId());

					} catch (Exception e) {
						logger.error("User record with user id: {} failed approve", userDto.getUserId());
						
						throw new ServiceException(ResponseMessages.ERR_USER_APPROVED,
								ResponseMessages.ERR_USER_APPROVED);
					}

				}

			} else if (CCLPConstants.USER_STATUS_REJECTED.equals(userDto.getUserStatus())) {
				if (CCLPConstants.USER_STATUS_REJECTED.equals(existingUser.getUserStatus())) {

					logger.error("User record with user id: {} already rejected", userDto.getUserId());

					throw new ServiceException(ResponseMessages.USER_ALREADY_REJECTED,
							ResponseMessages.USER_ALREADY_REJECTED);
				} else {
					existingUser.setUserStatus(CCLPConstants.USER_STATUS_REJECTED);
					existingUser.setCheckerRemarks(userDto.getCheckerRemarks());
					
					try {
						userDao.updateUser(existingUser);
						
						logger.info("User record with user id: {} successfully rejected", userDto.getUserId());

					} catch (Exception e) {
						logger.error("User record with user id: {} failed reject", userDto.getUserId());
						throw new ServiceException(ResponseMessages.ERR_USER_REJECTED,
								ResponseMessages.ERR_USER_REJECTED);
					}

				}

			}

			userDto.setUserName(existingUser.getUserName());
			
		}
		logger.info(CCLPConstants.EXIT);
	}
	
	@Override
	public void deleteUserById(Long userId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		ClpUser user = userDao.getUserByUserId(userId);

		if (Objects.isNull(user)) {
			logger.error("User record with user id: {} does not exist", userId);

			throw new ServiceException(("CLPUSER" + ResponseMessages.DOESNOT_EXISTS), ResponseMessages.DOESNOT_EXISTS);
		}

		try {
			userDao.deleteUser(user);
		} catch (Exception e) {
			logger.error("Error while deleting User record for '{}'", user.getUserName());
			throw new ServiceException(ResponseMessages.FAIL_USER_DELETE, ResponseMessages.FAIL_USER_DELETE);
		}

		logger.info(CCLPConstants.EXIT);

	}
	
	
	@Override
	public int countOfUser(long userId) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside countOfuser with data : "+userId);
		int count=0;
		count=userDao.countOfUser(userId);
		logger.info("end of countOfuser with count : "+count);
		logger.info(CCLPConstants.EXIT);
		return count;
	}

	@Override
	public void updateAccessStatus(ClpUserDTO userDto) {
		
		logger.info(CCLPConstants.ENTER);
		userDao.updateAccessStatus(userDto);
		logger.info(CCLPConstants.EXIT);
	}
	
	

}
