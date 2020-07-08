/**
 * 
 */
package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.UserDAO;
import com.incomm.cclp.domain.ClpUser;
import com.incomm.cclp.dto.ClpUserDTO;

/**
 * User DAO provides all the user data access operations.
 * 
 * @author abutani
 *
 */
@Repository
public class UserDAOImpl implements UserDAO {
	
	// the entity manager
	@PersistenceContext
	private EntityManager em;

	
	/**
	 * Creates a new user
	 * 
	 * @param user The user to be created.
	 * 
	 */
	@Override
	@Transactional
	public void createUser(ClpUser user) {

		em.persist(user);
	}
	
	/**
	 * Gets an User by user name.
	 * 
	 * @param userName The user name for the User to be retrieved.
	 * 
	 * @return the User.
	 */
	@Override
	public ClpUser getUserByUserName(String userName) {
		
		Object clpUser = em.createQuery(QueryConstants.GET_USER_BY_USER_NAME)
				.setParameter(CCLPConstants.USERNAME, userName.toUpperCase())
				.getSingleResult();

		return (clpUser == null ? null : (ClpUser) clpUser);
	}
	
	
	
	
	/**
	 * Getting ClpUser Entity object by user id
     * @param  userId
	 */
	@Override
	public ClpUser getUserByUserId(Long userId) {
		return em.find(ClpUser.class, userId);
	}

	
	/**
	 * Updates last login time for user.
	 * 
	 * @param userId The userId for the User to be updated.
	 * 
	 */
	@Override
	@Transactional
	public void updateLastLoginTime(long userId) {

		em.createNativeQuery(QueryConstants.UPDATE_LAST_LOGIN_TIME_FOR_USER)
		.setParameter("userId", userId)
		.executeUpdate();
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClpUser> getUsersByName(String userName) {

		String usrName = "";

		if (userName != null) {
			usrName = userName;
		}
		if (usrName.equalsIgnoreCase("_")) {
			usrName = "\\_";

			return em.createQuery(QueryConstants.GET_USER_BY_NAME_UNDERSCORE)
					.setParameter(CCLPConstants.USERNAME, "%" + usrName.toUpperCase() + "%").getResultList();
		} else {

			return em.createQuery(QueryConstants.GET_USER_BY_NAME)
					.setParameter(CCLPConstants.USERNAME, "%" + usrName.toUpperCase() + "%").getResultList();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClpUser> getAllUsers() {
		return em.createQuery(QueryConstants.GET_ALL_USERS)
				.getResultList();
	}
	/**
	 * Getting the users by UserLoginId and UserName
	 * 
	 * @param  UserLoginId, UserName 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ClpUser> getUserByUserLoginIdAndUserName(String userLoginId, String userName) {
		String qry = QueryConstants.GET_ALL_USERS;
		String loginId = userLoginId.replaceAll("_", "\\\\_");
		String name = userName.replaceAll("_", "\\\\_");

		if ((loginId != null && !"*".equals(loginId) && !loginId.trim().isEmpty())
				&& (name != null && !"*".equals(name) && !name.isEmpty())) {

			qry += " where upper(user.userLoginId) like :userLoginId and upper(user.userName) like :userName";
			return em.createQuery(qry).setParameter(CCLPConstants.USER_LOGIN_ID, "%" + loginId.toUpperCase() + "%")
					.setParameter(CCLPConstants.USERNAME, name.toUpperCase() + "%").getResultList();
		}

		else if (name != null && !name.trim().isEmpty() && !"*".equals(name)) {
			qry += " where upper (user.userName) like :userName";
			return em.createQuery(qry).setParameter(CCLPConstants.USERNAME, "%" + name.toUpperCase() + "%")
					.getResultList();
		}

		else if (loginId != null && !loginId.trim().isEmpty() && !"*".equals(loginId)) {
			qry += " where upper(user.userLoginId) like :userLoginId";
			return em.createQuery(qry).setParameter(CCLPConstants.USER_LOGIN_ID, "%" + loginId.toUpperCase() + "%")
					.getResultList();
		} else {
			return em.createQuery(qry).getResultList();

		}

	}
	
	
	

	@Override
	@Transactional
	public void updateUser(ClpUser user) {
	
		em.merge(user);
	}
	
	@Override
	@Transactional
	public void deleteUser(ClpUser user) {
		em.remove(user);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int countOfUser(long userId) {
		int count=0;
		List <BigDecimal> countofUsers=null;
		
	 countofUsers=em.createNativeQuery(QueryConstants.COUNT_BY_USER)
				.setParameter("userId", String.valueOf(userId))
				.getResultList();

		BigDecimal countofUser= countofUsers.get(0);
		
		count=countofUser.intValue();

		return count;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int countOfUserByUserLoginID(String userLoginId) {
		int count=0;
		List <BigDecimal> countofUsers=null;
		
	 countofUsers=em.createNativeQuery(QueryConstants.COUNT_BY_USER_LOGINID)
				.setParameter(CCLPConstants.USER_LOGIN_ID, String.valueOf(userLoginId))
				.getResultList();

		BigDecimal countofUser= countofUsers.get(0);
		
		count=countofUser.intValue();

		return count;
	}

	@Override
	@Transactional
	public void updateAccessStatus(ClpUserDTO userDto) {

		em.createNativeQuery(QueryConstants.UPDATE_ACCESS_STATUS)
		.setParameter("accessStatus", userDto.getAccessStatus())
		.setParameter("userId", userDto.getUserId())
		.executeUpdate();
	}
	

}
