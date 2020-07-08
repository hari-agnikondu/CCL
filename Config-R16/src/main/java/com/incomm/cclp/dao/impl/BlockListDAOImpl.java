package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.BlockListDAO;
import com.incomm.cclp.domain.BlockList;

@Repository
public class BlockListDAOImpl implements BlockListDAO{

	@PersistenceContext
	private EntityManager em;
	@Override
	public List<Object[]> getDeliveryChannelList() {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_DELIVERY_CHANNELS);		
		@SuppressWarnings("unchecked")
		List<Object[]> deliveryChannels = query.getResultList();
		return deliveryChannels;
	}

	@Override
	public int isBlockListExist(String instrumentId) {

			return ((Number)em.createQuery(QueryConstants.GET_BLOCKLIST_COUNT_BY_INS_ID)
						.setParameter("instrumentId", instrumentId).getSingleResult()).intValue();
	}

	@Override
	@Transactional
	public void createBlockList(BlockList blockList) {
		em.persist(blockList);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlockList> getBlockListById(String channelCode) {
		return em.createQuery(QueryConstants.GET_BLOCKLIST_BY_ID).setParameter("channelCode", channelCode)
				.getResultList();
	}

	@Override
	@Transactional
	public int deleteBlockList(List<Object> instrumentIds) {
		return em.createQuery(QueryConstants.REMOVE_BLOCKLIST_BY_INSID)
				.setParameter("instrumentIds", instrumentIds  )
				.executeUpdate();
		
	}
	@Override
	public Object getAllBlockList() {
		return em.createQuery(QueryConstants.GET_ALL_BLOCKLIST).getResultList();
	}


}
