package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.AttributeDefinitionDAO;
import com.incomm.cclp.domain.AttributeDefinition;

@Repository
public class AttributeDefinitionDAOImpl implements AttributeDefinitionDAO {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<AttributeDefinition> getAllAttributeDefinitions() {
		return em.createQuery(QueryConstants.GET_ALL_ATTRIBUTES).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AttributeDefinition> getAllCardAttributeDefinitions() {
		return em.createQuery(QueryConstants.GET_CARD_ATTRIBUTES).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AttributeDefinition> getAttributeDefinitionsByGroupName(String groupName) {
		return em.createQuery(QueryConstants.GET_GLOBAL_PARAMETERS).setParameter("attributeGroup", groupName)
				.getResultList();
	}

	@Override
	@Transactional
	public void createAttributeDefinition(AttributeDefinition attributeDefinition) {
		em.persist(attributeDefinition);		
	}
	
	@Override
	@Transactional
	public void updateAttributeDefinition(AttributeDefinition attributeDefinition) {
		em.merge(attributeDefinition);
	}

	@Override
	public AttributeDefinition getAttributeDefinitonByAttributeName(String attributeName) {
		return em.find(AttributeDefinition.class, attributeName);
	}

	@Override
	@Transactional
	public void updateAttributeDefinition(int chunkSize, int threadPoolSize, int maxThreadPoolSize) {
		em.createNativeQuery(QueryConstants.UPDATE_THREAD_POOL_PARAMETERS)
		.setParameter("chunkSize", chunkSize).setParameter("threadPoolSize", threadPoolSize).setParameter("maxThreadPoolSize", maxThreadPoolSize).executeUpdate();
		
	}

}
