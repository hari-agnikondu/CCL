/**
 * 
 */
package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.ClpUserDTO;
import com.incomm.cclp.exception.ServiceException;

/**
 * @author abutani
 *
 */
public interface UserService {
	
	public void createUser(ClpUserDTO userDto) throws ServiceException;

	public ClpUserDTO getUserByUserName(String userName) throws ServiceException;

	public void updateLastLoginTime(long userId);

	public List<ClpUserDTO> getUsersByName(String userName);

	public List<ClpUserDTO> getAllUsers();

	public List<ClpUserDTO> getUserByUserLoginIdAndUserName(String userLoginId, String userName);

	public ClpUserDTO getUserByUserId(Long userId);

	public void updateUser(ClpUserDTO userDto) throws ServiceException;
	
	public void changeUserStatus (ClpUserDTO userDto) throws ServiceException;
	
	public void deleteUserById(Long userId) throws ServiceException;
	
	public int countOfUser(long userId);

	public void updateAccessStatus(ClpUserDTO userDto);
	
}
