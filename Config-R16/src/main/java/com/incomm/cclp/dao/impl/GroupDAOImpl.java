package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.GroupDAO;
import com.incomm.cclp.domain.Group;


@Repository
public class GroupDAOImpl implements GroupDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	@Override
	public void createGroup(Group group) {
		em.persist(group);
		
	}
	@Transactional
	@Override
	public void updateGroup(Group group) {
		em.merge(group);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroups() {
		return em.createQuery(QueryConstants.GET_ALL_GROUPS).getResultList();
		
	}
	@Transactional
	@Override
	public void deleteGroup(Group group) {
		em.remove(em.contains(group) ? group : em.merge(group));
		
	}
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupByName(String groupName) {
		

		return em.createQuery(QueryConstants.GET_GROUP_BY_NAME)
				.setParameter("groupName", "%" +groupName.replaceAll("_", "\\\\_").toUpperCase()+ "%").getResultList();
		
	}

	@Override
	public Group findGroup(Group group) {
		
		return em.find(Group.class, group.getGroupId());	
		
	}
	@Transactional
	@Override
	public int changeStatus(Group group) {
		
	
		
	      return  em.createNativeQuery(QueryConstants.UPDATE_GROUP_STATUS)
			.setParameter("status", group.getStatus())
			.setParameter("checkerDesc", group.getGroupCheckerRemarks())
			.setParameter("groupId",group.getGroupId())
			.setParameter("newStatus", "NEW")
			.setParameter("lastUpdUser", group.getLastUpdateUser())
			.executeUpdate();
	}
	
	@Override
	public BigDecimal isMappedToUser(long groupId) {

		return (BigDecimal)  em.createNativeQuery(QueryConstants.COUNT_BY_GROUP)
				.setParameter("groupId", groupId).getSingleResult();
	}

	

}
