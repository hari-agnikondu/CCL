/**
 * 
 */
package com.incomm.cclpvms.security;

import javax.servlet.annotation.WebListener;

import org.springframework.web.context.request.RequestContextListener;

/**
 * ClpRequestContextListener exposes session scoped beans to current thread.
 * 
 * @author abutani
 *
 */
@WebListener
public class ClpRequestContextListener extends RequestContextListener {

}
