/**
 * 
 */
package com.incomm.cclpvms.admin.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.admin.model.ClpUser;
import com.incomm.cclpvms.admin.model.ClpUserDTO;
import com.incomm.cclpvms.admin.model.GroupDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

/**
 * @author abutani
 *
 */
public interface UserService {
	
	public ClpUserDTO getUserByUserName(String userName) throws ServiceException;
	
	public void updateLastLoginTime(long userId) throws ServiceException;
	
	public ResponseDTO createUser(ClpUserDTO clpUserDTO)  throws ServiceException;

	public List<ClpUserDTO> getUsersByName(String userName) throws ServiceException;

	public List<ClpUserDTO> getAllUsers() throws ServiceException;
	
	public List<ClpUserDTO> getAllUsers(ClpUser clpUser) throws ServiceException;
	
	public List<GroupDTO> getAllGroups() throws ServiceException;
	
	public ClpUser getUserByUserId(Long userId) throws ServiceException;
	
	public ResponseDTO updateUser(ClpUserDTO clpUserDTO) throws ServiceException;
	
	public ResponseEntity<ResponseDTO> approveUser(ClpUser clpUser) throws ServiceException ;
	
	public ResponseEntity<ResponseDTO> rejectUser(ClpUser clpUser) throws ServiceException ;
	
	public ResponseEntity<ResponseDTO> deleteUser(ClpUser clpUser) throws ServiceException ;

	public ResponseEntity<ResponseDTO> updateAccessStatus(ClpUser clpUser) throws ServiceException;

	
}
