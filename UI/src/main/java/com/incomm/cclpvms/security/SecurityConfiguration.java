package com.incomm.cclpvms.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * SecurityConfiguration provides the configurations to spring security 
 * for the LDAP authentication and authorizations provided by CLP database. 
 * 
 * @author abutani
 *
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {


	@Autowired
	LdapAuthoritiesPopulator clpAuthoritiesPopulator;
	
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
	
	@Value("${maxConcurrentSessions}") 
	private int maxConcurrentSessions;
	
	@Value("${ldap_ou}") String ldap_ou;
	/**
	 * Configure the LDAP authentication manager.
	 * 
	 * @param auth The authentication manager.
	 */ 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
			throws Exception {


		auth.ldapAuthentication()
		.userDetailsContextMapper(userDetailsContextMapper())  // Provide custom context mapper.
		.userSearchBase("ou="+ldap_ou)                           // Organizational Unit.  
		.userSearchFilter("(uid={0})")                          // User ID.
		.ldapAuthoritiesPopulator(clpAuthoritiesPopulator)     // Custom Authorities populator.
		//.groupSearchBase("ou=groups")                          // LDAP Groups.
		.contextSource()                                       // spring security context source.
		.root("dc=incomm,dc=com")                              // LDAP Domain Component.  
		.ldif("classpath:users.ldif");                         // Custom LDIF file.
		//.url("ldap://10.44.1.151:389/dc=fss,dc=india")
		
		/*
		auth.ldapAuthentication()
		.userDetailsContextMapper(userDetailsContextMapper())  // Provide custom context mapper.
		.userSearchBase(ldapUserSearchBase)                    // Search Base.  
		.userSearchFilter(ldapUserSearchFilter)                // Search Filter.
		.ldapAuthoritiesPopulator(clpAuthoritiesPopulator)     // Custom Authorities populator.
		.contextSource()                                       // spring security context source.
		//.ldif("classpath:users.ldif")  ;                      // Custom LDIF file.
		.url(ldapUrl)
		.managerDn(ldapManagerDn)							   // LDAP Manager user ID.
		.managerPassword(ldapManagerPwd);		*/			   // LDAP Manager Pwd.
	}
	
	
	/**
	 * Custom user details mapper for getting LDAP attributes for user.
	 * 
	 */ 
	@Bean
	public UserDetailsContextMapper userDetailsContextMapper() 
	{
		return new LdapUserDetailsMapper() {
			
			@Override
			public UserDetails mapUserFromContext(DirContextOperations context, String userName, 
					Collection<? extends GrantedAuthority> authorities) {

				UserDetails details = super.mapUserFromContext(context, userName, authorities);
				return new ClpLdapUserDetails((LdapUserDetails) details, context);
			}
		};
	}

	
	/**
	 * Configure the http security configurations.
	 * 
	 * @param http The http security object.
	 */ 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		http
		.headers().frameOptions().deny().and()							  // Deny X-FRAME-OPTIONS
		.formLogin()
		.loginPage("/clpLogin").loginProcessingUrl("/login")  			  // login page urls
		.and()
		.logout().logoutSuccessUrl("/clpLogin")                           // logout page urls
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.and()
		.authorizeRequests()                                              // authorize requests 
		.antMatchers("/clpLogin","/login", "/logout").permitAll()         // allow these urls access without login.
		.antMatchers("/resources/**").permitAll()                       // disable access to resources without permission.
		.anyRequest().authenticated()                                     // authenticate all other requests.
		.and()
		.sessionManagement()
		.maximumSessions(maxConcurrentSessions);       					  // The maximum concurrent sessions allowed per user.
	}

	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}
