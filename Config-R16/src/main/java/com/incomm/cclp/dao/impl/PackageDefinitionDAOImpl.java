package com.incomm.cclp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.incomm.cclp.dao.PackageDefinitionDAO;
import com.incomm.cclp.domain.PackageDefinition;

public class PackageDefinitionDAOImpl implements PackageDefinitionDAO {

	@PersistenceContext
	EntityManager em;

	@Override
	public PackageDefinition getPackageDefinitionById(String packageDefinitionId) {

		return em.find(PackageDefinition.class, packageDefinitionId);

	}

}
