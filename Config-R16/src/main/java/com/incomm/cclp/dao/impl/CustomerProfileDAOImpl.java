package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.CustomerProfileDAO;
import com.incomm.cclp.dto.CustomerProfileDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.impl.ProductServiceImpl;

@Repository
public class CustomerProfileDAOImpl implements CustomerProfileDAO{

	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public int createCustomerProfile(CustomerProfileDTO customerProfileDto, String attributes) {
			
			return em.createNativeQuery(QueryConstants.CREATE_CARD_ATTRIBUTES)
				.setParameter("attributes", attributes)
				.setParameter(CCLPConstants.PROXY_NUMBER, customerProfileDto.getProxyNumber())
				.executeUpdate();
			
		}

	@Override
	public Object getCustomerProfileById(Long profileId) {
		Query query = em.createNativeQuery(QueryConstants.GET_CUSTOMER_PROFILE_BY_ID);		
		return query.setParameter(CCLPConstants.PROFILE_ID, profileId).getSingleResult();

	}

	@Override
	public int checkExistingProfileCode(String proxyNumber) {
		return ((Number) em.createNativeQuery(QueryConstants.GET_CUSTOMER_PROFILE_CODE_COUNT)
				.setParameter(CCLPConstants.PROXY_NUMBER, proxyNumber)
				.getSingleResult()).intValue();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCustomerProfiles(String accountNumber, String cardNumber, String proxyNumber) {
		
		String qry = QueryConstants.GET_CUSTOMER_PROFILES_LIST + " (:accountNumber ='-1'  or act.ACCOUNT_NUMBER = :accountNumber) and (:cardNumber ='-1' or c.CARD_NUM_ENCR = clp_transactional.fn_emaps_main(:cardNumber)) and (:proxyNumber ='-1' or c.PROXY_NUMBER = :proxyNumber)";
		Query query = em.createNativeQuery(qry);		
		return query.setParameter(CCLPConstants.ACCOUNT_NUMBER, accountNumber)
				.setParameter(CCLPConstants.CARD_NUMBER, cardNumber)
				.setParameter(CCLPConstants.PROXY_NUMBER, proxyNumber)
				.getResultList();
	}

	@Override
	@Transactional
	public int updateCardAttributes(String attributesJsonResp, Long profileId) {
		return em.createNativeQuery(QueryConstants.UPDATE_CARD_ATTRIBUTES)
				.setParameter("attributes", attributesJsonResp)
				.setParameter(CCLPConstants.PROFILE_ID, profileId)
				.executeUpdate();
	}
	
	@Transactional
	@Override
	public int deleteCustomerProfileById(Long profileId) {
		return em.createNativeQuery(QueryConstants.DELETE_CUSTOMER_PROFILE_BY_ID)
				.setParameter(CCLPConstants.PROFILE_ID, profileId)
				.executeUpdate();
		
	}

	@Override
	public Object getAttributesByType(String type, String value, String attributeGrp) throws ServiceException {
		String proxyNumber="-1";
		String cardNumber = "-1";
		String accountNumber = "-1";
		
		Object obj;
		if(type.equalsIgnoreCase(CCLPConstants.PROXY_NUMBER)) {
			 proxyNumber = value;	
		}else if(type.equalsIgnoreCase(CCLPConstants.CARD_NUMBER)) {
			cardNumber = value;
		}else if(type.equalsIgnoreCase(CCLPConstants.ACCOUNT_NUMBER)) {
			 accountNumber = value;
		}
		
		Query query  =  em.createNativeQuery(QueryConstants.GET_ATTRIBUTES_BY_TYPE)
				.setParameter(CCLPConstants.ACCOUNT_NUMBER, accountNumber)
				.setParameter( CCLPConstants.PROXY_NUMBER, proxyNumber)
				.setParameter(CCLPConstants.CARD_NUMBER, cardNumber);
	
		try {
			
			obj = query.getSingleResult();
			
		}catch(Exception e) {
			logger.error("Error Occured : {}" , e.getMessage());
			throw new ServiceException(ResponseMessages.ERR_CUSTOMER_PROFILE_DOES_NOT_EXISTS,ResponseMessages.DOESNOT_EXISTS);
		}
		
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCardsByAccountNumber(String accountNumber) {
		
		return em.createNativeQuery(QueryConstants.GET_CARDS_BY_ACCOUNTNUMBER).setParameter(CCLPConstants.ACCOUNT_NUMBER,accountNumber)
		.getResultList();
		
	}

}
