/**
 * 
 */
package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.ClpUser;
import com.incomm.cclp.dto.ClpUserDTO;

/**
 * @author abutani
 *
 */
public interface UserDAO {

	public void createUser(ClpUser user);

	public ClpUser getUserByUserName(String userName);

	public void updateLastLoginTime(long userId);

	public List<ClpUser> getAllUsers();
	
	public List<ClpUser> getUsersByName(String userName);

	List<ClpUser> getUserByUserLoginIdAndUserName(String userLoginId, String userName);

	public ClpUser getUserByUserId(Long userId);
	
	public void updateUser(ClpUser user);
	
	void deleteUser(ClpUser user);
	
	public int countOfUser(long userId);
	
	public int countOfUserByUserLoginID(String userLoginId);

	public void updateAccessStatus(ClpUserDTO userDto);
	
}
