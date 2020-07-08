/**
 * 
 */
package com.incomm.cclp.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.MerchantDAO;
import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.MerchantProduct;
import com.incomm.cclp.domain.MerchantProductId;

@Repository
public class MerchantDAOImpl implements MerchantDAO {

	// the entity manager
	@PersistenceContext
	private EntityManager em;

	/**
	 * Persists an merchant.
	 * 
	 * @param merchant
	 *            The Merchant to be persisted.
	 */
	@Override
	public void createMerchant(Merchant merchant) {
		em.persist(merchant);
	}

	/**
	 * Gets all Merchants.
	 * 
	 * @return the list of Active Merchants.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> getAllMerchants() {
		return em.createQuery(QueryConstants.GET_ALL_MERCHANTS).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> getMerchantsByName(String merchantName) {

		if (merchantName != null && merchantName.equalsIgnoreCase("_")) {
			String merchName = "\\_";

			return em.createQuery(QueryConstants.GET_MERCHANT_BY_NAME_UNDERSCORE)
					.setParameter(CCLPConstants.MERCHANTNAME, "%" + merchName.toUpperCase() + "%").getResultList();
		} else {
			if(merchantName != null)
			return em.createQuery(QueryConstants.GET_MERCHANT_BY_NAME)
					.setParameter(CCLPConstants.MERCHANTNAME, "%" + merchantName.toUpperCase() + "%").getResultList();

		}
		return new ArrayList<>();
	}

	/**
	 * Updates an Merchant.
	 * 
	 * @param issuer
	 *            The Merchant to be updated.
	 */
	@Override
	@Transactional
	public void updateMerchant(Merchant merchant) {

		em.merge(merchant);
	}

	/**
	 * Gets an merchant by ID.
	 * 
	 * @param merchantId
	 *            The merchant id for the merchant to be retrieved.
	 * 
	 * @return the merchant.
	 */
	@Override
	public Merchant getMerchantById(Long merchantId) {
		return em.find(Merchant.class, merchantId);
	}


	@Override
	@Transactional
	public void deleteMerchant(Merchant merchant) {
		em.remove(merchant);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantProduct> getMerchantProducts(String merchantName, String productName) {
		String qry = QueryConstants.MERCHANT_PRODUCTS_LIST;

		if ((merchantName != null && !"*".equals(merchantName) && !merchantName.trim().isEmpty())
				&& (productName != null && !"*".equals(productName) && !productName.isEmpty())) {

			qry += " where upper(merchantProduct.primaryKey.merchant.merchantName) like :merchantName ESCAPE '\\' and "
					+ " upper(merchantProduct.primaryKey.product.productName) like :productName ESCAPE '\\' ";
			return em.createQuery(qry)
					.setParameter(CCLPConstants.MERCHANTNAME, "%" + merchantName.replaceAll("_", CCLPConstants.SLASH).toUpperCase() + "%")
					.setParameter("productName", "%" + productName.replaceAll("_", CCLPConstants.SLASH).toUpperCase() + "%")
					.getResultList();
		} else if (productName != null && !productName.trim().isEmpty() && !"*".equals(productName)) {
			qry += " where upper(merchantProduct.primaryKey.product.productName) like :productName ESCAPE '\\'";
			return em.createQuery(qry)
					.setParameter("productName", "%" + productName.replaceAll("_", CCLPConstants.SLASH).toUpperCase() + "%")
					.getResultList();
		} else if (merchantName != null && !merchantName.trim().isEmpty() && !"*".equals(merchantName)) {
			qry += " where upper(merchantProduct.primaryKey.merchant.merchantName) like :merchantName ESCAPE '\\'";
			return em.createQuery(qry)
					.setParameter(CCLPConstants.MERCHANTNAME, "%" + merchantName.replaceAll("_", CCLPConstants.SLASH).toUpperCase() + "%")
					.getResultList();
		} else {
			return em.createQuery(qry).getResultList();
		}

	}

	@Transactional
	@Override
	public void assignProductToMerchant(MerchantProduct merchantProduct) {
		em.merge(merchantProduct);
	}

	@Transactional
	@Override
	public void removeMerchantProductMapping(MerchantProduct merchantProduct) {
		em.remove(merchantProduct);
	}

	@Override
	public MerchantProduct getMerchantProductById(MerchantProductId merchantProductId) {
		return em.find(MerchantProduct.class, merchantProductId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantProduct> getMerchantProductsById(Long merchantId, Long productId) {
		String qry=	QueryConstants.MERCHANT_PRODUCTS_LIST;
		qry+=" where merchantProduct.primaryKey.merchant.merchantId=:merchantId and merchantProduct.primaryKey.product.productId=:productId";
		return em.createQuery(qry)
				.setParameter(CCLPConstants.MERCHANT_ID, merchantId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.getResultList();
	}

}
