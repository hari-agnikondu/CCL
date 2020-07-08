package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.AttributeDefinition;

public interface AttributeDefinitionDAO {

	public List<AttributeDefinition> getAllAttributeDefinitions();

	public List<AttributeDefinition> getAttributeDefinitionsByGroupName(String groupName);

	public void createAttributeDefinition(AttributeDefinition attributeDefinition);

	public void updateAttributeDefinition(AttributeDefinition attributeDefinition);

	public AttributeDefinition getAttributeDefinitonByAttributeName(String attributeName);

	List<AttributeDefinition> getAllCardAttributeDefinitions();

	public void updateAttributeDefinition(int chunkSize, int threadPoolSize, int maxThreadPoolSize);

}
