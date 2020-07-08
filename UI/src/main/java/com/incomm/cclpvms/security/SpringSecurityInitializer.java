package com.incomm.cclpvms.security;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * SpringSecurityInitializer initializes the spring security configurations.
 * Commenting the extends in this class will disable the spring security module.
 * 
 * @author abutani
 *
 */
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
	
	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) 
	{
	    FilterRegistration.Dynamic characterEncodingFilter = 
	    		servletContext.addFilter("encodingFilter", new CharacterEncodingFilter());
	    characterEncodingFilter.setInitParameter("encoding", "UTF-8");
	    characterEncodingFilter.setInitParameter("forceEncoding", "true");
	    characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
	    }


    @Override
    protected boolean enableHttpSessionEventPublisher() {

    	// Notify Spring security that a logout event happened.
        return true;
    }
}
