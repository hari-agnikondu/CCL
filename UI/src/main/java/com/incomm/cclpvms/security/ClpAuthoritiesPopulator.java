package com.incomm.cclpvms.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclpvms.admin.model.ClpUserDTO;
import com.incomm.cclpvms.admin.service.UserService;
import com.incomm.cclpvms.session.ClpSession;


/**
 * ClpAuthoritiesPopulator provides a custom implementation of the 
 * ldap authorities populator to load the authorizations 
 * for the logged in user. 
 * The CLP user authorizations will be loaded from the CLP database.
 * 
 * @author abutani
 *
 */

@Service("clpAuthoritiesPopulator")
public class ClpAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	@Autowired
	private UserService userService;

	@Autowired
	private ClpSession clpSession;
	
	private static final Logger logger = LogManager.getLogger(ClpAuthoritiesPopulator.class);	


	/**
	 * Get the list of authorities for the logged in user from the CLP RBAC schema.
	 * 
	 * @param userData The ldap directory context object.
	 * @param username The username of the logged in user.
	 * 
	 * @return the list of authorities for the logged in user.
	 */ 
	@Transactional(readOnly=true)
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(
			DirContextOperations userData, String userName) 
	{
		
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		try
		{
			
			if (userName.contains("@"))
				userName = userName.substring(0, userName.lastIndexOf("@"));
			
			// Get the user from CLP database.
			ClpUserDTO user = userService.getUserByUserName(userName);
			
			if (user==null || user.getUserId() <= 0)
			{
				logger.error("No user found in CLP database for userName: " + userName);
			}
			else{
				
				logger.debug("logged in user: " + user.toString());
				
				// User is logged in and authorities were retrieved from CLP database.
				// Update last login time at this point.
				userService.updateLastLoginTime(user.getUserId());

				// Add the userId and last login time to session.
				clpSession.setUserId(user.getUserId());
				clpSession.setUserLastLoginTime(user.getLastLoginTime());

				// Add the granted authorities to the user.
				// These will be used by spring security framework to 
				// look up permissions for the logged in user.
				authorities.addAll(getGrantedAuthorities(user));
			}
		}
		catch(Exception e)
		{
			logger.error("Exception occured while setting user authorities: " + e.getMessage());
		}
		
		return authorities;
	}

	
	/**
	 * Get the list of authorities for the logged in user.
	 * 
	 * @param user The logged in user.
	 * 
	 * @return the list of authorities for the logged in user.
	 */ 
	public Set<SimpleGrantedAuthority> getGrantedAuthorities(ClpUserDTO user) {

		Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
		
		user.getGroups().stream().forEach(group -> 
		{
			group.getRoles().stream().forEach(role -> 
			{
				role.getPermissions().stream().forEach(permission -> 
				{
					authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
				});
			});
		});
		
		return authorities;
	}
	
}
