/**
 * 
 */
package com.incomm.cclpvms.config.service;

import java.io.IOException;
import java.util.Map;

import javax.naming.NamingException;


/**
 * @author abutani
 *
 */
public interface LdapParseService {
	
	public Map ldapParser(String ldapId) throws NamingException, IOException;
	
}
