package com.incomm.cclp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.CCFDAO;
import com.incomm.cclp.domain.CCFConfDetail;
import com.incomm.cclp.domain.CCFConfVersion;
import com.incomm.cclp.dto.CCFConfDetailDTO;
import com.incomm.cclp.exception.ServiceException;


@Repository
public class CCFDAOImpl implements CCFDAO {

	@PersistenceContext
	private EntityManager em;

	
	public void deleteCCFConfig(CCFConfVersion ccfVersion) throws ServiceException {
		Query q = em.createQuery(QueryConstants.DELETE_CCF_DETAIL_BY_ID);
		q.setParameter("versionID", ccfVersion.getVersionName());
		q.executeUpdate();

		CCFConfVersion newccfVersion = em.find(CCFConfVersion.class, ccfVersion.getVersionName());
		em.remove(newccfVersion);
		em.flush();
	}

	@Override
	public CCFConfDetailDTO getCCFVersionByName(String versionName) throws ServiceException {

		return null;
	}

	@Override
	public void createCCFConfig(CCFConfVersion ccfVersion) throws ServiceException {
		em.persist(ccfVersion);

	}

	@Override
	public CCFConfDetailDTO getAllCCFVersionDtls() throws ServiceException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCCFParam() throws ServiceException {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_CCFPARAM);
		
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCCFMappingKeys() throws ServiceException {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_CCFKEY);
		return query.getResultList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, List> getCCFlist() {
		Map<String, List> ccfList = new HashMap<>();
		List<Map> ccfData = em.createNativeQuery(QueryConstants.GET_CCF_VERSION).getResultList();
		ccfList.put("ccfData", ccfData);
		return ccfList;
	}

	public List<CCFConfDetail> getCCFVersionDtls(String versionName) throws ServiceException {
		return em.createQuery(QueryConstants.GET_CCF_VERSION_DTLS, CCFConfDetail.class)
				.setParameter("version_Name", versionName).getResultList();
	}

	@Override
	public void updateCCFConfig(CCFConfVersion ccfVersion) throws ServiceException {
		em.merge(ccfVersion);
	}

}
