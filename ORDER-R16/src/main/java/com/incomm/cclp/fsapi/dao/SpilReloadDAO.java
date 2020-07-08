
package com.incomm.cclp.fsapi.dao;
import java.util.Map;

import com.incomm.cclp.exception.ServiceException;

public interface SpilReloadDAO {
	
	public String[] invokeReload(Map<String,String> valueObj) throws ServiceException;
	
}
