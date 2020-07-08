package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.GroupDAO;
import com.incomm.cclp.domain.Group;
import com.incomm.cclp.domain.GroupRole;
import com.incomm.cclp.domain.GroupRoleTemp;
import com.incomm.cclp.domain.Role;
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.RoleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService{
	private static final Logger logger = LogManager.getLogger(GroupServiceImpl.class);

	@Autowired
	GroupDAO groupDao;
	/**
	 * Create an Group.
	 * Checking the Group Name already exist or Not
	 * @param GroupDTO The GroupDTO to be created.
	 * @throws ServiceException if Already exist or any Exception scenario it will raise
	 */
	@Override
	public void createGroup(GroupDTO groupdto) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		Group group=mm.map(groupdto, Group.class);
		if (!CollectionUtils.isEmpty(group.getRoletemp())){
			group.getRoletemp().clear();
		}
		if (!CollectionUtils.isEmpty(group.getGroupRole())){
			group.getGroupRole().clear();
		}
		
	
		List<GroupDTO> existingGroupList=getGroupByName(groupdto.getGroupName());
		boolean flag=existingGroupList.stream().anyMatch(existGroup -> existGroup.getGroupName().equalsIgnoreCase(groupdto.getGroupName()));
		if(!flag ) {
			List<GroupRoleTemp> roles=new ArrayList<>();
			for(String id:groupdto.getSelectedRoleList()){
				
				GroupRoleTemp roletemp=new GroupRoleTemp();
				Role role=new Role();
						role.setRoleId(Long.parseLong(id));						
				roletemp.setRole(role);
				roletemp.setGroup(group);
				roletemp.setInsDate(new Date());
				roletemp.setLastUpdDate(new Date());
				roletemp.setInsUser(group.getInsUser());		
				roletemp.setLastUpdUser(group.getInsUser());
				roles.add(roletemp);
			
			}
			group.setInsUser(groupdto.getInsUser());
			group.setInsDate(new Date());
			group.setLastUpdDate(new Date());
			group.setStatus("NEW");
			group.setLastUpdateUser(groupdto.getInsUser());
			group.setRoletemp(roles);
			groupDao.createGroup(group);
			groupdto.setGroupId(group.getGroupId());
		}else {
			logger.error("Group name already present");
			throw new ServiceException(
					ResponseMessages.ERR_GROUP_NAME_EXIST);
		}
		logger.info(CCLPConstants.EXIT);
	}
	/**
	 * Update an Group.
	 * 
	 * @param GroupDTO The GroupDTO to be update.
	 * @throws  any Exception scenario it will raise
	 */
	@Override
	public void updateGroup(GroupDTO groupdto)throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		Group group = mm.map(groupdto, Group.class);
		
		Group grpDao=	groupDao.findGroup(group);
		if(grpDao!=null) {
			
			if (!CollectionUtils.isEmpty(grpDao.getRoletemp())){
				grpDao.getRoletemp().clear();
			}
			
				for(String id:groupdto.getSelectedRoleList()){
					
					GroupRoleTemp roletemp=new GroupRoleTemp();
					Role role=new Role();
							role.setRoleId(Long.parseLong(id));						
					roletemp.setRole(role);
					roletemp.setGroup(grpDao);
					roletemp.setInsUser(grpDao.getInsUser());
					roletemp.setInsDate(new Date());
					roletemp.setLastUpdDate(new Date());
					roletemp.setLastUpdUser(group.getLastUpdateUser());
					
					grpDao.getRoletemp().add(roletemp);
				
				}

				if (!CollectionUtils.isEmpty(grpDao.getGroupRole())){
					grpDao.getGroupRole().clear();
				}
				grpDao.setStatus("NEW");
				groupDao.updateGroup(grpDao);
				
				groupdto.setGroupName(grpDao.getGroupName());
			
			
		}else {
			logger.error("Group does not exist");
			throw new ServiceException(
					ResponseMessages.ERR_GROUP_DOES_NOT_EXIST);
		}
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * Get an all Group.
	 * 
	 * @param No paramater
	 * 
	 */

	@Override
	public List<GroupDTO> getGroups() {
		logger.info(CCLPConstants.ENTER);
		List<Group> groups = groupDao.getGroups();
		List<GroupDTO> dtoList=new ArrayList<>();
		for(Group groupdaobean:groups){
			GroupDTO groupDto = new GroupDTO();
			groupDto.setGroupId(groupdaobean.getGroupId());
			groupDto.setGroupName(groupdaobean.getGroupName());
			groupDto.setGroupStatus(groupdaobean.getStatus());
			groupDto.setGroupCheckerRemarks(groupdaobean.getGroupCheckerRemarks());
			
			groupDto.setRoles(getRolesFromGroupRoleList(groupdaobean.getRoletemp()));
			
			
			
			dtoList.add(groupDto);			
		}
		logger.info(CCLPConstants.EXIT);
		return dtoList;
		
		
				
	}
	
	private Set<RoleDTO> getRolesFromGroupRoleList(List<GroupRoleTemp> groupRolesTemp){
		logger.info(CCLPConstants.ENTER);
		Set<RoleDTO> roles = new HashSet<>();
		
		if (!CollectionUtils.isEmpty(groupRolesTemp)) {
			ModelMapper mm = new ModelMapper();
			groupRolesTemp.stream().forEach(groupRole -> {
				if (!Objects.isNull(groupRole.getRole())) {
					logger.info(groupRole.getRole().toString());
					roles.add(mm.map(groupRole.getRole(), RoleDTO.class));
				}
			});
		}
		logger.info(CCLPConstants.EXIT);
		return roles;
	}

	/**
	 * Delete an Group.
	 * 
	 * @param GroupDTO The GroupDTO to be delete.
	 * @throws  any Exception scenario it will raise
	 */

	@Override
	public GroupDTO deleteGroup(GroupDTO groupdto) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		Group group = mm.map(groupdto, Group.class);
		Group grpDao=	groupDao.findGroup(group);
		
		if(grpDao!=null) {
		
				BigDecimal count=groupDao.isMappedToUser(groupdto.getGroupId());
			logger.info("count: {}",count.intValue());
			if(count.intValue()>0){
				throw new ServiceException(
						ResponseMessages.ERROR_GROUP_COUNT);
			}else{
				groupDao.deleteGroup(grpDao);
			}
			logger.info(CCLPConstants.EXIT);
			return mm.map(grpDao, GroupDTO.class);	
		}else {
			logger.error("Group does not exist");
			throw new ServiceException(
					ResponseMessages.ERR_GROUP_DOES_NOT_EXIST);
		}
		
	}
	/**
	 * Search the name from Group.
	 * 
	 * @param groupName .
	 * 
	 */

	@Override
	public List<GroupDTO> getGroupByName(String groupName) {
		
		logger.info(CCLPConstants.ENTER);
		List<Group> groups = groupDao.getGroupByName(groupName);
		List<GroupDTO> dtoList=new ArrayList<>();
		for(Group groupdaobean:groups){
			GroupDTO groupDto = new GroupDTO();
			groupDto.setGroupId(groupdaobean.getGroupId());
			groupDto.setGroupName(groupdaobean.getGroupName());
			groupDto.setGroupStatus(groupdaobean.getStatus());
			groupDto.setGroupCheckerRemarks(groupdaobean.getGroupCheckerRemarks());
			groupDto.setInsUser(groupdaobean.getInsUser());
			groupDto.setLastUpdateUser(groupdaobean.getLastUpdateUser());
			
			groupDto.setRoles(getRolesFromGroupRoleList(groupdaobean.getRoletemp()));
			
			
			
			dtoList.add(groupDto);			
		}
		logger.info(CCLPConstants.EXIT);
		return dtoList;
	}


	/**
	 * find the group, Either groupName or id 
	 * 
	 * @param groupdto .
	 * 
	 */


	@Override
	public GroupDTO getGroupId(GroupDTO groupdto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm=new ModelMapper();
		Group group = mm.map(groupdto, Group.class);
		Group grpDao=groupDao.findGroup(group);
		if(grpDao!=null) {
		
			logger.debug(CCLPConstants.EXIT);
				
		
		}else {
			logger.error("Group does not exist");
			throw new ServiceException(
					ResponseMessages.ERR_GROUP_DOES_NOT_EXIST);
		}
		
		List<GroupRoleTemp> roletmp=grpDao.getRoletemp();
		Set<RoleDTO> rolesSet=new HashSet<>();
		for(GroupRoleTemp groupRoleTemp:roletmp){
			Role roledao=groupRoleTemp.getRole();
			rolesSet.add(mm.map(roledao, RoleDTO.class));
		}
		groupdto.setGroupStatus(grpDao.getStatus());
		groupdto.setRoles(rolesSet);
		groupdto.setGroupName(grpDao.getGroupName());
		logger.info(CCLPConstants.EXIT);
		return groupdto;
		
		
	}
	@Override
	public String changeStatus(Long groupId,String status,
			String checkerDescription, Long lastUpdUser )throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		GroupDTO groupDTO=new GroupDTO();
		groupDTO.setGroupId(groupId);
		GroupDTO existGroupDao=getGroupId(groupDTO);
		if(existGroupDao !=null && existGroupDao.getGroupId()>0) {
			
			if(CCLPConstants.USER_STATUS_APPROVED.equalsIgnoreCase(existGroupDao.getGroupStatus()) ){
				logger.error("Group already approved");
				throw new ServiceException(ResponseMessages.GROUP_ALREADY_APPROVED);
			}
			else if(CCLPConstants.USER_STATUS_REJECTED.equalsIgnoreCase(existGroupDao.getGroupStatus())){
				logger.error("Group already rejected");
				throw new ServiceException(ResponseMessages.GROUP_ALREADY_REJECTED);
			}
		}
		
		Group group = new Group();
		group.setStatus(status);
		group.setGroupId(groupId);
		group.setGroupCheckerRemarks(checkerDescription);
		group.setLastUpdateUser(lastUpdUser);
		
		int statusCnt=groupDao.changeStatus(group);
		
		if(statusCnt==0) {
			logger.error("Error while changing the status");
			throw new ServiceException(CCLPConstants.USER_STATUS_APPROVED.equalsIgnoreCase(status)?ResponseMessages.ERROR_GROUP_APPROVE:ResponseMessages.ERROR_GROUP_REJECT);
		}else if(CCLPConstants.USER_STATUS_APPROVED.equalsIgnoreCase(status)){
			
			group=groupDao.findGroup(group);
		
		 	if (!CollectionUtils.isEmpty(group.getRoletemp())) {

		 		if(!group.getGroupRole().isEmpty()){
					group.getGroupRole().clear();
				}
				for(GroupRoleTemp groupTemp:group.getRoletemp()){
					GroupRole groupRole = new GroupRole();
					groupRole.setGroup(groupTemp.getGroup());
					groupRole.setInsDate(new Date());
					groupRole.setInsUser(groupTemp.getInsUser());
					groupRole.setRole(groupTemp.getRole());
					groupRole.setLastUpdUser(groupTemp.getLastUpdUser());
					groupRole.setLastUpdDate(new Date());
					group.getGroupRole().add(groupRole);
				}
				groupDao.updateGroup(group);
		 	}
		 	
		}
		logger.info(CCLPConstants.EXIT);
		return ResponseMessages.SUCCESS;
	}
		

}
