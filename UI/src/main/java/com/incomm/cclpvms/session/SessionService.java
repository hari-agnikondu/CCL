/**
 * 
 */
package com.incomm.cclpvms.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the interface to access all user session data.
 * 
 * @author abutani
 *
 */
@Service
public class SessionService {
	
	@Autowired
	private ClpSession clpSession;
	
	public long getUserId()
	{
		return clpSession.getUserId();
	}

}
