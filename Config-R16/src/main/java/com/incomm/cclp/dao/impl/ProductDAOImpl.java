package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.domain.Alert;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.domain.ProductPurse;
import com.incomm.cclp.domain.ProductRuleSet;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.dto.ProductDTO;

/*
 * Product DAO provides all the product data access operations.
 */
@SuppressWarnings("unchecked")
@Repository
public class ProductDAOImpl implements ProductDAO {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager em;
	
	@Value("#{'${limitSupportedPurseType}'.split(',')}")
	private List<String> limitPurseType;
	
	@Value("#{'${feesSupportedPurseType}'.split(',')}")
	private List<String> feePurseType;

	/**
	 * Persists an product.
	 * 
	 * @param product.
	 *            The Product to be persisted.
	 */

	@Override
	public void createProduct(Product product) {
		em.persist(product);
	}

	/**
	 * Gets all Products.
	 * 
	 * @return the list of Active Products.
	 */
	@Override
	public List<Product> getAllProducts() {

		return em.createQuery(QueryConstants.GET_ALL_PRODUCTS).getResultList();
	}

	@Override
	@Transactional
	public void updateProduct(Product product) {

		em.merge(product);

	}

	@Override
	public void deleteProduct(Product product) {

		em.remove(em.contains(product) ? product : em.merge(product));

	}

	@Override
	public Product getProductById(long productId) {

		return em.find(Product.class, productId);
	}

	/**
	 * Gets an product by name.
	 * 
	 * @param productName
	 *            The product name for the Product to be retrieved. The productName
	 *            parameter can be a partial or complete name.
	 * 
	 * @return the list of all Products for the given name.
	 */

	@Override
	public List<Product> getProductsByName(String productName) {

		return em.createQuery(QueryConstants.GET_PRODUCT_BY_NAME)
				.setParameter("productName", "%" + productName.toUpperCase() + "%").getResultList();
	}

	@Override
	public int countAndDeleteProductById(long product) {
		return 0;
	}

	@Override
	public int countOfproductById(long product) {
		return 0;
	}

	@SuppressWarnings( "rawtypes" )
	@Override
	public Map<String, List> getCCFlist() {

		Map<String, List> totalData = new HashMap<>();

		List<Map> ccfData = em.createNativeQuery(QueryConstants.GET_CCF_VERSION).getResultList();

		totalData.put("ccfData", ccfData);
		return totalData;
	}

	@Override
	public List<Product> getParentProducts() {
		return em.createQuery(QueryConstants.GET_PARENT_PRODUCTS_LIST, Product.class).getResultList();
	}

	@Override
	@Transactional
	public int updateProductAttributes(ProductDTO productDTO) {

		return em.createNativeQuery(QueryConstants.UPDATE_PRODUCT_ATTRIBUTES)
				.setParameter("attributes", productDTO.getAttributes())
				.setParameter(CCLPConstants.PRODUCT_ID, productDTO.getProductId()).executeUpdate();

	}

	@Override
	@Transactional
	public int updateAttributes(String productAttributes, Long productId) {

		return em.createNativeQuery(QueryConstants.UPDATE_PRODUCT_ATTRIBUTES)
				.setParameter("attributes", productAttributes).setParameter(CCLPConstants.PRODUCT_ID, productId)
				.executeUpdate();
	}

	@Override
	public Product getAttributes(Long productId) {

		return em.find(Product.class, productId);
	}
	@Override
	public ProductPurse getProductPurseAttributes(Long productId,Long purseId) {

		String qry = QueryConstants.GET_PRODUCT_PURSE_BY_ID;
		Query query ;
		if(purseId == -1) {
			qry = qry+" AND IS_DEFAULT='Y'";
			 query = em.createQuery(qry, ProductPurse.class).setParameter("productId", productId);
		}else {
			qry = qry+" AND productPurse.primaryKey.purse.purseId=:purseId";
			query= em.createQuery(qry, ProductPurse.class).setParameter("productId", productId).setParameter("purseId", purseId);
		}
		
		return (ProductPurse) query.getSingleResult();
	}

	@Override
	public List<Object[]> getTransactionList() {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_TRANSACTIONS);
		List<Object[]> transactionsList = query.getResultList();
		return transactionsList;

	}

	@Override
	public List<Product> getAllParentProducts() {

		Query query = em.createNativeQuery(QueryConstants.GET_ALL_PARENT_PRODUCTS, Product.class);
		List<Product> parentProductsList = query.getResultList();
		return parentProductsList;

	}

	@Override
	public List<Product> getAllRetailProducts() {

		Query query = em.createNativeQuery(QueryConstants.GET_ALL_RETAIL_PRODUCTS, Product.class);

		List<Product> retailProductsList = query.getResultList();
		return retailProductsList;

	}

	@Override
	public List<Product> getChildProductList(long productId) {

		return em.createNativeQuery(QueryConstants.GET_CHILD_PRODUCTS_LIST_LIMITS, Product.class)
				.setParameter("product_Id", productId).getResultList();

	}

	@Override
	public List<Product> getParentProductList(long productId) {

		return em.createNativeQuery(QueryConstants.GET_PARENT_PRODUCTS_LIST_LIMITS, Product.class)
				.setParameter("product_Id", productId).getResultList();

	}

	@Override
	public ProductRuleSet getRuleSetByProductId(long product) {
		ProductRuleSet productRuleSet = null;
		try {
			productRuleSet = em.createQuery(QueryConstants.GET_PRODUCT_RULESET, ProductRuleSet.class)
					.setParameter(CCLPConstants.PRODUCT_ID, product).getSingleResult();
		} catch (Exception e) {
			logger.error("Error Occured : {}", e.getMessage());
		}

		return productRuleSet;
	}

	@Override
	@Transactional
	public int deleteProductRuleSet(ProductRuleSet productRuleSet) {

		return em.createNativeQuery(QueryConstants.DELETE_PRODUCT_RULESET)
				.setParameter(CCLPConstants.PRODUCT_ID, productRuleSet.getId().getProductId()).executeUpdate();
	}

	@Override
	@Transactional
	public void updateProductRuleSet(ProductRuleSet productRuleSet) {

		em.merge(productRuleSet);

	}

	// alerts Tab

	@Override
	public List<Alert> getProductAlertMessages() {
		return em.createQuery(QueryConstants.GET_ALERT_MESSAGES, Alert.class).getResultList();
	}

	@Override
	@Transactional
	public int updateProductAttributes(String attributes, Long productId) {
		return em.createQuery(QueryConstants.UPDATE_ATTRIBUTES).setParameter("prodAttributes", attributes)
				.setParameter(CCLPConstants.PRODUCT_ID, productId).executeUpdate();
	}

	@Override
	public List<Product> getProductsByNameForCopy(String productName) {
		return em.createQuery(QueryConstants.GET_PRODUCT_BY_NAME_COPY)
				.setParameter("productName", productName.toUpperCase()).getResultList();
	}

	public List<Object[]> getProductsWithSamePartnerId(Long productId) {

		Query query = em.createNativeQuery(QueryConstants.GET_PRODUCTS_LINKED_WITH_PARTNER_ID_AND_PARTNER_ID)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId);
		
		return query.getResultList();
	}

	@Override
	public List<Object[]> getTransactionListByChannelName(String channelName) {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_TRANSACTIONS_BY_CHANNEL_NAME)
				.setParameter("CHANNEL_SHORT_NAME", channelName);

		return query.getResultList();
	}

	@Override
	public List<Object[]> getTransactionListByChannelNameTxnName(String channelName, String transactionShortName) {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_TRANSACTIONS_BY_CHANNEL_NAME_TXN_NAME)
				.setParameter("CHANNEL_SHORT_NAME", channelName)
				.setParameter("TRANSACTION_SHORT_NAME", transactionShortName);

		return query.getResultList();
	}

	@Override
	public List<Product> getProductsByProgramId(Long programId) {
		return em.createNativeQuery(QueryConstants.GET_PRODUCT_BY_PROGRAM_ID, Product.class)
				.setParameter("programId", programId).getResultList();
	}

	@Override
	public Object getProductIdByUPC(String upc) {
		return em.createNativeQuery(QueryConstants.GET_PRODUCTID_BY_UPC).setParameter("upc", upc).getSingleResult();
	}

	@Override
	@Transactional
	public int deleteProductRuleSetByProductId(Long productId) {
		return em.createNativeQuery(QueryConstants.DELETE_PRODUCT_RULESET)
				.setParameter(CCLPConstants.PRODUCT_ID, productId).executeUpdate();

	}

	@Override
	public List<Object[]> getSupportedPurse(Long productId) {
		if (productId > 0) {
			return em.createNativeQuery(QueryConstants.GET_SUPPOTED_PURSE + "where pp.product_id=:productId ")
					.setParameter("productId", productId).getResultList();
		} else {
			return em.createNativeQuery(QueryConstants.GET_SUPPOTED_PURSE + " ORDER BY pp.product_id").getResultList();
		}

	}

	@Override
	public List<Object[]> getprodAttributesUpdate(Long productId) {
		return em.createNativeQuery(QueryConstants.GET_PROD_ATTRIBUTES_OVERRIDE).setParameter("productId", productId)
				.getResultList();
	}

	@Override
	public List<BigDecimal> getDistinctProductId() {
		return em.createNativeQuery(QueryConstants.GET_DISTINCT_PRODUCT_ID).getResultList();
	}

	@Override
	@Transactional
	public int updateProdAttributesFlag(Long productId) {

		return em.createNativeQuery(QueryConstants.UPDATE_PROD_ATTRIBUTES_FLAG).setParameter("productId", productId)
				.executeUpdate();
	}


	@Override
	public List<Object[]> getInternationalCurrencyByProductId(long productId) {
		if(productId>0){
			return em.createNativeQuery(QueryConstants.GET_PRODUCT_CURRENCY+ "  WHERE PRODUCT_ID=:productId").setParameter(CCLPConstants.PRODUCT_ID, productId).getResultList();
		}else{
			return em.createNativeQuery(QueryConstants.GET_PRODUCT_CURRENCY).getResultList();
		}
	}


	@Override
	public List<Object[]> getPartnerCurrency(Long partnerId) {

		return em.createNativeQuery(QueryConstants.GET_PARTNER_CURRENCY).setParameter("PARTNER_ID", partnerId)
				.getResultList();

	}

	@Override
	public List<Object[]> getPartnerCurrencyCodes(Long partnerId) {
		return em.createNativeQuery(QueryConstants.GET_ALL_CURRENCY_CODE_BY_PARTNER)
				.setParameter("PARTNER_ID", partnerId)
				.getResultList();
	}


	@Override
	public ProductPurse getProductPurseById(long productId, long purseId) {
		ProductPurse productPurse = null;
		try {
			productPurse = (ProductPurse) em.createNativeQuery(QueryConstants.GET_PRODUCT_PURSE, ProductPurse.class)
					.setParameter(CCLPConstants.PRODUCT_ID, productId).setParameter(CCLPConstants.PURSE_ID, purseId)
					.getSingleResult();
		} catch (Exception e) {
			logger.error("Error Occured : {}", e.getMessage());
		}

		return productPurse;
	}

	@Override
	@Transactional
	public int updateProductPurseAttributes(String productPurseAttributesString, Long productId, Long purseId) {
		return em.createNativeQuery(QueryConstants.UPDATE_PRODUCT_PURSE_ATTRIBUTES)
				.setParameter("attributes", productPurseAttributesString).setParameter("productId", productId)
				.setParameter("purseId", purseId).executeUpdate();

	}
	

	@Override
	public List<Purse> getPurseByProductId(Long productId,Long parentProductId,String attributeGroup) {

		List<Purse> purseList = new ArrayList<>();
		List<Long> purseIds = new ArrayList<>();
		List<BigDecimal> purses  = new ArrayList<>();
		String query = null;

		try {
			logger.info("productId " +productId + "  "+"parentProductId "+ parentProductId + " "+"attributeGroup  "+ attributeGroup);
			logger.info("limitPurseType"+limitPurseType);
			if(attributeGroup.equalsIgnoreCase("Limits")) {
							
				query = QueryConstants.GET_PURSE_BY_PRODUCT_ID + " AND pt.purse_type_name IN (:purseType)";
				logger.info("query"+query);
				purses = (List<BigDecimal>) em
						.createNativeQuery(query)
						.setParameter("PRODUCT_ID", productId)
						.setParameter("PARENT_PRODUCT_ID", parentProductId)
						.setParameter("purseType", limitPurseType)
						.getResultList();
				
			}else if(attributeGroup.equalsIgnoreCase("Fees")) {
				
			
				query = QueryConstants.GET_PURSE_BY_PRODUCT_ID+ " AND pt.purse_Type_Name IN (:purseType) ";
				purses = (List<BigDecimal>) em
						.createNativeQuery(query)
						.setParameter("PRODUCT_ID", productId)
						.setParameter("PARENT_PRODUCT_ID", parentProductId)
						.setParameter("purseType", feePurseType)
						.getResultList();
				
			}else {
				query = QueryConstants.GET_PURSE_BY_PRODUCT_ID+" and p.IS_DEFAULT='N'";
				 purses = (List<BigDecimal>) em
						.createNativeQuery(query)
						.setParameter("PRODUCT_ID", productId)
						.setParameter("PARENT_PRODUCT_ID", parentProductId)
						.getResultList();
			}
			
			
			logger.info("purses"+purses);	
		if( purses.size() > 0) {
			for (int i = 0; i < purses.size(); i++) {
				purseIds.add(purses.get(i).longValue());
			}
			purseList = em.createQuery(QueryConstants.GET_PURSES_BY_IDS).setParameter("purseId", purseIds)
					.getResultList();
		}
		} catch (Exception e) {
			logger.error("Error Occured : {}", e.getMessage());
		}
		
		logger.info("purseList"+purseList);	
		return purseList;
	}

	
	@Override
	public List<BigDecimal> getDistinctProductPurseId() {
		return em.createNativeQuery(QueryConstants.GET_DISTINCT_PRODUCT_PURSE_ID).getResultList();
	}

	@Override
	public List<Object[]> getprodPurseAttributesUpdate(Long productId) {
		return em.createNativeQuery(QueryConstants.GET_PROD_PURSE_ATTRIBUTES_OVERRIDE).setParameter("productId", productId)
				.getResultList();
	}
	
	@Override
	@Transactional
	public int updateProdPurseAttributesFlag(Long productId, long purseId,String attributeGroup,String attributeKey) {
		return em.createNativeQuery(QueryConstants.UPDATE_PROD_PURSE_ATTRIBUTES_FLAG)
				.setParameter("productId", productId)
				.setParameter("purseId", purseId)
				.setParameter("attributeGroup", attributeGroup)
				.setParameter("attributeKey", attributeKey)
				.executeUpdate();
	}


}
