/**
 * 
 */
package com.incomm.cclpvms.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.incomm.cclpvms.util.Util;

/**
 * ClpSession maintains the session information for the web application.
 * 
 * @author abutani
 *
 */
@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, value="session")
public class ClpSession {
	
	private Long userId;
	
	private String userLastLoginTime;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserLastLoginTime() {
		return userLastLoginTime;
	}

	public void setUserLastLoginTime(String userLastLoginTime) {
		
		this.userLastLoginTime = Util.convertDateFormat(userLastLoginTime, 
				"yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss");
	}

}
