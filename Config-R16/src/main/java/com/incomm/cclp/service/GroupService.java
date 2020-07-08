package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.exception.ServiceException;


public interface GroupService {
	
	public void createGroup(GroupDTO group) throws ServiceException;
	
	public void updateGroup(GroupDTO group) throws ServiceException;
	
	public List<GroupDTO> getGroups();
	
	public GroupDTO deleteGroup(GroupDTO group) throws ServiceException;
	
	public List<GroupDTO> getGroupByName(String groupName);
	
	public GroupDTO getGroupId(GroupDTO group)throws ServiceException;
	
	public String changeStatus(Long groupId,String status,
			String checkerDescription, Long lastUpdUser )throws ServiceException;

}
