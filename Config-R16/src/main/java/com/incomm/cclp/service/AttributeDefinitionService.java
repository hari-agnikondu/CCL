package com.incomm.cclp.service;

import java.util.Map;

import com.incomm.cclp.exception.ServiceException;

public interface AttributeDefinitionService {

	Map<String, Map<String, Object>> getAllAttributeDefinitions();

	Map<String, Map<String, Object>> getAllCardAttributeDefinitions();


	Map<Double, String> getAttributeDefinitionsByOrder() throws ServiceException;
	
}
