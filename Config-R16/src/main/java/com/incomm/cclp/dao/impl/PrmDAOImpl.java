package com.incomm.cclp.dao.impl;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.PrmDAO;

@Repository
public class PrmDAOImpl implements PrmDAO {

	@PersistenceContext
	EntityManager em;

	@Override
	@Transactional
	public int updateMRIFIN(String prmAttributes) {

		String replace = prmAttributes.replace("[", "");
		String replace1 = replace.replace("]", "");
		List<String> mriflist = Arrays.asList(replace1.split(CCLPConstants.SLASH_S));

		return em.createNativeQuery(QueryConstants.UPDATE_PRM_ATTRIBUTES_MRIF_IN).setParameter("MRIFLIST", mriflist)
				.executeUpdate();

	}

	@Override
	@Transactional
	public int updateMRIFNOTIN(String prmAttributes) {

		String replace = prmAttributes.replace("[", "");
		String replace1 = replace.replace("]", "");
		List<String> mriflist = Arrays.asList(replace1.split(CCLPConstants.SLASH_S));
		
		return em.createNativeQuery(QueryConstants.UPDATE_PRM_ATTRIBUTES_MRIF_NOTIN).setParameter("MRIFLIST", mriflist)
				.executeUpdate();

	}

	@Override
	@Transactional
	public int updateERIFIN(String prmAttributes) {
		String replace = prmAttributes.replace("[", "");
		String replace1 = replace.replace("]", "");
		List<String> eriflist = Arrays.asList(replace1.split(CCLPConstants.SLASH_S));

		return em.createNativeQuery(QueryConstants.UPDATE_PRM_ATTRIBUTES_ERIF_IN).setParameter("ERIFLIST", eriflist)
				.executeUpdate();
	}

	@Override
	@Transactional
	public int updateERIFNOTIN(String prmAttributes) {
		String replace = prmAttributes.replace("[", "");
		String replace1 = replace.replace("]", "");
		List<String> eriflist = Arrays.asList(replace1.split(CCLPConstants.SLASH_S));

		return em.createNativeQuery(QueryConstants.UPDATE_PRM_ATTRIBUTES_ERIF_NOTIN).setParameter("ERIFLIST", eriflist)
				.executeUpdate();
	}

	@Override
	@Transactional
	public int updateAllPrm() { 
		
		return em.createNativeQuery(QueryConstants.UPDATE_ALL_PRM_ATTRIBUTES).executeUpdate();
	}

	@Override
	@Transactional
	public int updateAllPrmToDiable(String arguement) {
		if (arguement.equalsIgnoreCase("ERIF")) {
			return em.createNativeQuery(QueryConstants.DISABLE_ALL_ERIF_PRM_ATTRIBUTES).executeUpdate();
		} else {
			return em.createNativeQuery(QueryConstants.DISABLE_ALL_MRIF_PRM_ATTRIBUTES).executeUpdate();
		}

	}

}
