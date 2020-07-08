/**
 * 
 */
package com.incomm.cclpvms.security;


import java.text.MessageFormat;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

import com.incomm.cclpvms.admin.model.ClpUserDTO;
import com.incomm.cclpvms.util.Util;

/**
 * LdapService provides methods for interacting with the LDAP Server.
 * 
 * @author abutani
 *
 */
@SuppressWarnings("deprecation")
@Service
public class LdapService {

	@Value("${ldap.url}") 
	private String ldapUrl;

	@Value("${ldap.userSearchBase}") 
	private String ldapUserSearchBase;

	@Value("${ldap.userSearchFilter}") 
	private String ldapUserSearchFilter;

	@Value("${ldap.managerDn}") 
	private String ldapManagerDn;

	@Value("${ldap.managerPwd}") 
	private String ldapManagerPwd;


	/**
	 * Validate the user against the LDAP server
	 * 
	 * @param userId the user ID to validate.
	 */ 
	public ClpUserDTO validateUser(String userId) throws Exception
	{

		final ClpUserDTO user = new ClpUserDTO();
		
		if (Util.isEmpty(userId))
			return user;

		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(ldapUrl);

		contextSource.setBase(ldapUserSearchBase);
		contextSource.setUserDn(ldapManagerDn);
		contextSource.setPassword(ldapManagerPwd);

		contextSource.afterPropertiesSet();

		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);

		// Avoid any referals
		ldapTemplate.setIgnorePartialResultException(true);
		ldapTemplate.afterPropertiesSet();

		// Search for the user
		
		ldapTemplate.search(
				DistinguishedName.EMPTY_PATH, MessageFormat.format(ldapUserSearchFilter, userId),
				new AttributesMapper<Object>() {
					public Object mapFromAttributes(Attributes attrs)
							throws NamingException {

						user.setUserLoginId(userId);
						
						user.setUserName(attrs.get("cn") == null ? "" : 
							(String) attrs.get("cn").get());

						user.setUserEmail(attrs.get("mail") == null ? "" : 
							(String) attrs.get("mail").get());

						return user;
					}
				});

		return user;

	}
}
