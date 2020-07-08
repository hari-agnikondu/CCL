package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.GroupAccessDAO;
import com.incomm.cclp.domain.GroupAccess;
import com.incomm.cclp.domain.GroupAccessPartner;
import com.incomm.cclp.domain.GroupAccessProduct;


/*
 * Group Access DAO provides all the group access data access operations.
 */
@Repository
public class GroupAccessDAOImpl implements GroupAccessDAO {


	@PersistenceContext
	private EntityManager em;
	/**
	 * Getting Group Access List  
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<GroupAccessProduct> getGroupAccessProducts(String groupAccessName
			, String productName){
		String qry= QueryConstants.GROUP_ACCESS_PRODUCTS_LIST;
		if((groupAccessName!=null && !"*".equals(groupAccessName) && !groupAccessName.trim().isEmpty()) 
				&& (productName!=null && !"*".equals(productName) && !productName.isEmpty())){

			qry+= " where upper(groupAccess.groupAccessName) like :groupAccessName ESCAPE '\\' and "
					+ " upper(product.productName) like :productName ESCAPE '\\' ";
			return em.createQuery(qry)
					.setParameter(CCLPConstants.GROUP_ACCESS_NAME, "%"+groupAccessName.replaceAll("_", CCLPConstants.SLASH).toUpperCase()+"%")
					.setParameter("productName", "%"+productName.replaceAll("_", CCLPConstants.SLASH).toUpperCase()+"%")
					.getResultList();
		}

		else if(productName!=null && !productName.trim().isEmpty() && !"*".equals(productName)){
			qry+= " where upper(product.productName) like :productName ESCAPE '\\'";
			return em.createQuery(qry)
					.setParameter("productName", "%"+ productName.replaceAll("_", CCLPConstants.SLASH).toUpperCase()+"%")
					.getResultList();
		}
		else if(groupAccessName!=null && !groupAccessName.trim().isEmpty() && !"*".equals(groupAccessName)){
			qry+= " where upper(groupAccess.groupAccessName) like :groupAccessName ESCAPE '\\'";
			return em.createQuery(qry)
					.setParameter(CCLPConstants.GROUP_ACCESS_NAME, "%"+groupAccessName.replaceAll("_", CCLPConstants.SLASH).toUpperCase()+"%")
					.getResultList();
		}

		else{
			return em.createQuery(qry)
					.getResultList();
		}

	}
	@Override
	@SuppressWarnings("unchecked")
	public List<GroupAccess> getGroupAccess(String groupAccessName){
		String qry=QueryConstants.GROUP_ACCESS_LIST;
		if(groupAccessName!=null && !groupAccessName.isEmpty() && !"*".equals(groupAccessName)){
			qry+=" where upper(groupAccess.groupAccessName)=:groupAccessName  order by groupAccess.groupAccessName";
			return em.createQuery(qry)
					.setParameter(CCLPConstants.GROUP_ACCESS_NAME, groupAccessName.toUpperCase())
					.getResultList();
		}
		else{
			qry+=" order by groupAccess.groupAccessName";
			return em.createQuery(qry)
					.getResultList();
		}

	}

	/**
	 * Getting Group Access Partners List
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<GroupAccessPartner> getGroupAccessPartners(){

		return em.createQuery(QueryConstants.GROUP_ACCESS_PARTNERS_LIST)
				.getResultList();

	}

	/**
	 * Getting Group Access Partners List
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<GroupAccessPartner> getGroupAccessPartnersByAccessId(Long groupAccessId){

		return em.createQuery(QueryConstants.GROUP_ACCESS_PARTNERS_LIST_BY_ACCESSID)
				.setParameter(CCLPConstants.GROUP_ACCESS_ID, groupAccessId)
				.getResultList();

	}

	public GroupAccess getGroupAccessById(Long groupAccessId){
		return em.find(GroupAccess.class,groupAccessId);

	}


	/**
	 * Getting Group Access Partners List
	 * 
	 * */
	@Override
	@SuppressWarnings("unchecked")
	public List<GroupAccessProduct> getGroupAccessProductsByAccessAndProductId(Long groupAccessId,Long productId){
		String qry=	QueryConstants.GROUP_ACCESS_PRODUCTS_LIST;
		qry+=" where groupAccessProduct.groupAccess.groupAccessId=:groupAccessId and groupAccessProduct.product.productId=:productId";
		return em.createQuery(qry)
				.setParameter(CCLPConstants.GROUP_ACCESS_ID, groupAccessId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.getResultList();

	}


	/**
	 * Getting Group Access Partners List
	 * 
	 * */
	@Override
	@SuppressWarnings("unchecked")
	public List<GroupAccessProduct> getGroupAccessProductsByAccessId(Long groupAccessId){
		String qry=	QueryConstants.GROUP_ACCESS_PRODUCTS_LIST;
		qry+=" where groupAccessProduct.groupAccess.groupAccessId=:groupAccessId";
		return em.createQuery(qry)
				.setParameter(CCLPConstants.GROUP_ACCESS_ID, groupAccessId)
				.getResultList();

	}

	@Override
	@Transactional
	public void createGroupAccess(GroupAccess groupAccess){
		em.persist(groupAccess);
	}

	@Override
	@Transactional
	public void updateGroupAccess(GroupAccess groupAccess){
		em.merge(groupAccess);
	}

	@Override
	@Transactional
	public int deleteGroupAccessProduct(Long groupAccessId,Long productId){
		String qry=QueryConstants.DELETE_GROUP_ACCESS_PRODUCTS;
		qry+=" WHERE GROUP_ACCESS_ID=:groupAccessId AND PRODUCT_ID=:productId";
		return em.createNativeQuery(qry)
				.setParameter(CCLPConstants.GROUP_ACCESS_ID, groupAccessId)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.executeUpdate();
	}

	/**
	 * Getting Group Access Partners List
	 * 
	 * */
	@Override
	public int deleteBulkGroupAccessProduct(List<String> groupAccessIdList,Long groupAccessId){
		String qry=QueryConstants.DELETE_GROUP_ACCESS_PRODUCTS;
		qry+=" WHERE GROUP_ACCESS_ID=:groupAccessId and PARTNER_ID in (:partnerId)";
		return em.createNativeQuery(qry)
				.setParameter("partnerId", groupAccessIdList)
				.setParameter(CCLPConstants.GROUP_ACCESS_ID, groupAccessId)
				.executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getProductsByAccessId(Long groupAccessId){
		javax.persistence.Query query = em.createNativeQuery(QueryConstants.GET_PRODUCTS_BY_ACCESSID).
				setParameter(CCLPConstants.GROUP_ACCESS_ID, groupAccessId);		
		return query.getResultList();
	}

	@Override
	public void createGroupAccessProduct(GroupAccessProduct groupAccessProduct){

		em.persist(groupAccessProduct);

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPartnersListByAccessId(Long groupAccessId){
		javax.persistence.Query query = em.createNativeQuery(QueryConstants.GET_PARTNERS_LIST_BY_ACCESSID).
				setParameter(CCLPConstants.GROUP_ACCESS_ID, groupAccessId);		
		return query.getResultList();
	}
	
	@Override
	public int getGroupAccessProductsCountByProductId(Long productId,Long partnerId){
		String qry=	QueryConstants.GROUP_ACCESS_PRODUCTS_LIST_COUNT;
		return ((BigDecimal)em.createNativeQuery(qry)
				.setParameter(CCLPConstants.PRODUCT_ID, productId)
				.setParameter(CCLPConstants.PARTNER_ID, partnerId)
				.getSingleResult()).intValue();

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getGroupAccessProdByProductId(Long productId){
		String qry=QueryConstants.GET_GROUP_ACCESS_PRODS_BY_PRODUCTID;
		javax.persistence.Query query =null;
		if(productId==0){
			query = em.createNativeQuery(qry);	
		}
		else{
			qry+=" and Product_Id=:productId";
			query = em.createNativeQuery(qry).
					setParameter("productId", productId);		
		}
		
		return query.getResultList();
	}
	
	
	
	
	
}

