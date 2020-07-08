package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.ProductPurseDao;
import com.incomm.cclp.domain.ProductPurse;

@SuppressWarnings("unchecked")
@Repository
public class ProductPurseDAOImpl implements ProductPurseDao{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public int updateAttributes(String productPurseAttributes, Long productId, Long purseId) {
		logger.debug(CCLPConstants.ENTER);
		logger.info("Update prod purse attributes for productId {} and purseId {}::",productId,purseId);
		return em.createNativeQuery(QueryConstants.UPDATE_PRODUCT_PURSE_ATTRIBUTES)
				.setParameter("attributes", productPurseAttributes).setParameter(CCLPConstants.PRODUCT_ID, productId)
				.setParameter(CCLPConstants.PURSE_ID, purseId)
				.executeUpdate();
	}
	
	
	@Override
	public ProductPurse getProdPurseAttributesByProdPurseId(Long productId, Long purseId) {
		logger.debug(CCLPConstants.ENTER);
		logger.info("Get prod purse attributes for productId {} and purseId {}::",productId,purseId);
		ProductPurse prodPurse=null;
		try{
		 prodPurse= (ProductPurse) em.createNativeQuery(QueryConstants.GET_PRODUCT_PURSE, ProductPurse.class)
				 				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				 				.setParameter(CCLPConstants.PURSE_ID, purseId).getSingleResult();
		}catch (NoResultException nre){
			logger.info("ProdPurse Attributes not found for productId {} and purseId {}::",productId,purseId);
			}
		return prodPurse;

	}	
}
