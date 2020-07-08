package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.StockDAO;
import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.Stock;
import com.incomm.cclp.util.Util;

@Repository
public class StockDAOImpl implements StockDAO {

	
	@Qualifier("orderEntityManagerFactory")
	@PersistenceContext
	EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> getAllMerchants() {
		
		return em.createQuery(QueryConstants.GET_ALL_MERCHANTS).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getLocationAndProductByMerchantId(String merchantId) {
	
		return em.createNativeQuery(QueryConstants.GET_LOCATIONS_AND_PRODUCTS_BY_MERCHANTID)
				.setParameter("merchant_id", merchantId).getResultList();
	}


	@Override
	@Transactional
	public void createStock(Stock stock) {
		em.persist(stock);
	}

	@Override
	public Stock getStockByIds(String merchantId, Long locationId, Long productId) {

		Stock stock = null;
		try {

			stock = (Stock) em.createQuery(QueryConstants.GET_STOCKS_BY_IDS).setParameter("merchant_id", merchantId)
					.setParameter("location_id", locationId).setParameter("productId", productId).getSingleResult();

		}catch(NoResultException e) {
			return null;
		}
		return stock;
	}

	@Override
	@Transactional
	public void updateStock(Stock stock) {
		em.merge(stock);
		
	}

	@Override
	public Object getStockByMerchantIdAndLocationId(String merchantId, Long locationId) {
	if ((!Util.isEmpty(merchantId)) && (locationId != null && (locationId > 0))) {
			return em
					.createQuery(QueryConstants.GET_STOCKS_BY_MERCHANTID_AND_LOCATIONID
							+ " WHERE MERCHANT_ID=:merchantId AND LOCATION_ID=:locationId")
					.setParameter("merchantId", merchantId).setParameter("locationId", locationId).getResultList();

		} else if (merchantId != null && !Util.isEmpty(merchantId)) {
			return em
					.createQuery(
							QueryConstants.GET_STOCKS_BY_MERCHANTID_AND_LOCATIONID + " WHERE MERCHANT_ID=:merchantId")
					.setParameter("merchantId", merchantId).getResultList();
		} else if (locationId != null && locationId > 0) {
			return em
					.createQuery(
							QueryConstants.GET_STOCKS_BY_MERCHANTID_AND_LOCATIONID + " WHERE LOCATION_ID=:locationId")
					.setParameter("locationId", locationId).getResultList();
		} else {
			return em.createQuery(QueryConstants.GET_STOCKS_BY_MERCHANTID_AND_LOCATIONID).getResultList();
		}

	}
	
}
