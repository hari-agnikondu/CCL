/**
 * 
 */
package com.incomm.cclpvms.security;

import java.util.Collection;

import javax.naming.NamingException;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

/**
 * ClpLdapUserDetails provides the ability to override the 
 * user ldap details as needed.
 * 
 * @author abutani
 *
 */
public class ClpLdapUserDetails implements LdapUserDetails {


	private LdapUserDetails details;
	
	private DirContextOperations context;

	public ClpLdapUserDetails(LdapUserDetails details, DirContextOperations context) 
	{
		this.details = details;
		this.context = context;
	}

	public String getDn() {
		return details.getDn();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return details.getAuthorities();
	}

	public String getPassword() {
		return details.getPassword();
	}

	public String getUsername() {
		try 
		{
			return (String) this.context.getAttributes().get("cn").get();
		} 
		catch (NamingException e) 
		{
			return details.getUsername();
		}
	}
	
	public String getEmail() {
		try 
		{
			return (String) this.context.getAttributes().get("mail").get();
		} 
		catch (NamingException e) 
		{
			return details.getDn();
		}
	}

	public boolean isAccountNonExpired() {
		return details.isAccountNonExpired();
	}

	public boolean isAccountNonLocked() {
		return details.isAccountNonLocked();
	}

	public boolean isCredentialsNonExpired() {
		return details.isCredentialsNonExpired();
	}

	public void eraseCredentials() {
	}

	public boolean isEnabled() {

		return true;
	}
	
	@Override
	public boolean equals(Object o)
	{

		if (o == null)
			return false;
		
        if (o == this) 
        	return true;
        
        if (!(o instanceof ClpLdapUserDetails)) {
            return false;
        }

        ClpLdapUserDetails user = (ClpLdapUserDetails) o;

        return user.getEmail().equalsIgnoreCase(this.getEmail());
	}
	
    @Override
    public int hashCode() {
        return this.getEmail().hashCode();

    }
}
