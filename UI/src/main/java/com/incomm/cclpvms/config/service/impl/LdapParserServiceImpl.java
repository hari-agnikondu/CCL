package com.incomm.cclpvms.config.service.impl;

import java.io.IOException;
/** import java.io.InputStream;*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.directory.shared.ldap.ldif.LdifEntry;
import org.apache.directory.shared.ldap.ldif.LdifReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.incomm.cclpvms.config.service.LdapParseService;


@Service
public class LdapParserServiceImpl implements LdapParseService {
	
	/** private static final Logger logger = LogManager.getLogger(LdapParserServiceImpl.class);*/

	private static final String PHONE = "phone";

	public Map ldapParser(String ldapId) throws NamingException, IOException {
	
		Map ldapMap=new HashMap();
		
    LdifReader reader = new LdifReader();
    
    List<LdifEntry> entries = null;

    Resource resource = new ClassPathResource("/users.ldif");
    /**InputStream resourceInputStream = resource.getInputStream();*/
    
    String userName="";
    String mail="";
    String phone="";
		entries = reader.parseLdifFile(resource.getFile().toString());
		
		
		String dn = "uid=";
		dn=dn+ldapId;
    //iterate the entries
    for (LdifEntry entry : entries) {
        final String name =entry.getDn().getNormName();
        if (name.contains(dn)) {
            
            if(entry.get("cn")!=null)
            {
            userName=entry.get("cn").toString().trim();
            
            userName=userName.replace("cn: ", "");
            ldapMap.put("userName", userName);
            }
            if(entry.get("mail")!=null)
            {
            	mail=entry.get("mail").toString().trim();
            	mail=mail.replace("mail: ", "");
                 ldapMap.put("mail", mail);
            }
            if(entry.get(PHONE)!=null)
            {
            	phone=entry.get(PHONE).toString().trim();
            	phone=phone.replace("phone: ", "");
                 ldapMap.put(PHONE, phone);
            }	
            
            
            
          
            /**System.out.println(entry.get("uid"));*/
        }
    }
    
    
    return ldapMap;

	}
    
}
