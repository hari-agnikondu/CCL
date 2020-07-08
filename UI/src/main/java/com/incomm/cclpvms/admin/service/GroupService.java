package com.incomm.cclpvms.admin.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.admin.model.Group;
import com.incomm.cclpvms.admin.model.GroupDTO;
import com.incomm.cclpvms.admin.model.RoleDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;


public interface GroupService {
	
	public Group getGroupById(Long groupId) throws ServiceException ;

	public Object getGroupByName(String groupName)throws ServiceException;

	public ResponseEntity<ResponseDTO> changeGroupStatus(Group groupConfig) throws ServiceException;

	public ResponseEntity<ResponseDTO> deleteGroup(Long groupId) throws ServiceException;

	public List<RoleDTO> getAllRoles() throws ServiceException;
	
	public List<GroupDTO> getGroupNameList () throws ServiceException;

	public ResponseDTO addGroup(Group groupConfig) throws ServiceException;
	
	public  GroupDTO getRoleNamesbyGroupId(Long groupId)  throws ServiceException; 
	
	public ResponseDTO updateGroup(Group groupConfig) throws ServiceException;
		
		



}
