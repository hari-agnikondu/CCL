package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.RoleDAO;
import com.incomm.cclp.domain.Role;

@Repository
public class RoleDAOImpl implements RoleDAO {

	@PersistenceContext
	private EntityManager em;

	// the logger
	private static final Logger logger = LogManager.getLogger(RoleDAOImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() {
	
		logger.debug("Performing database query \"{}\"", QueryConstants.GET_ALL_ROLES);
		return em.createQuery(QueryConstants.GET_ALL_ROLES).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRoleByName(String roleName) {

		String name = roleName.replaceAll("_", "\\\\_");
		return em.createQuery(QueryConstants.GET_ROLE_BY_NAME)
				.setParameter("roleName", "%" + name.toUpperCase() + "%").getResultList();
	}

	@Override
	public void createRole(Role role) {
		em.persist(role);

	}

	@Override
	public Role getRoleById(long roleId) {
		return em.find(Role.class, roleId);
	}

	@Override
	@Transactional
	public void updateRole(Role role) {
		em.merge(role);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getEntityPermissionMap() {

		return em.createNativeQuery(QueryConstants.GET_ENTITY_PERMISSION).getResultList();
	}

	@Override
	@Transactional
	public void updateRoleStatus(Role role) {
		em.merge(role);

	}

	@Override
	@Transactional
	public void deleteRole(Role role) {
		em.remove(role);
	}

}
