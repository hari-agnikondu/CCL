package com.incomm.cclp.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.MerchantRedemptionDAO;
import com.incomm.cclp.domain.MerchantProductRedemptionId;
import com.incomm.cclp.domain.MerchantRedemption;
import com.incomm.cclp.domain.RedemptionDelay;

@Repository
public class MerchantRedemptionDAOImpl implements MerchantRedemptionDAO {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public void createMerchant(MerchantRedemption merchant) {
		em.persist(merchant);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantRedemption> getAllMerchants() {
		return em.createQuery(QueryConstants.GET_ALL_REDEMPTION_MERCHANTS).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantRedemption> getMerchantsByName(String merchantName) {

		if (merchantName != null && merchantName.equalsIgnoreCase("_")) {
			String merchName = "\\_";

			return em.createQuery(QueryConstants.GET_MERCHANT_REDEMPTION_BY_NAME_UNDERSCORE)
					.setParameter("merchantName", "%" + merchName.toUpperCase() + "%").getResultList();
		} else if(merchantName!= null) {

			return em.createQuery(QueryConstants.GET_MERCHANT_REDEMPTIONBY_NAME)
					.setParameter("merchantName", "%" + merchantName.toUpperCase() + "%").getResultList();

		}
		return new ArrayList<>();
	}

	@Override
	public MerchantRedemption getMerchantById(String merchantId) {
		return em.find(MerchantRedemption.class, merchantId);
	}
	


	@Override
	public RedemptionDelay getMerchantProductById(MerchantProductRedemptionId merchantProductRedemptionId) {
		return em.find(RedemptionDelay.class, merchantProductRedemptionId);
	}



}
