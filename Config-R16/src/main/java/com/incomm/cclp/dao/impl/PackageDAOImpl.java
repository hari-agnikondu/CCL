package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.PackageDAO;
import com.incomm.cclp.domain.PackageDefinition;
import com.incomm.cclp.exception.ServiceException;

@Repository
public class PackageDAOImpl implements PackageDAO {

	// the entity manager
	@PersistenceContext
	private EntityManager em;

	@Override
	public PackageDefinition getPackageDefinitionById(String packageDefinitionId) {

		return em.find(PackageDefinition.class, packageDefinitionId);
	}

	@SuppressWarnings("unchecked")
	public List<PackageDefinition> getAllPackages() {

		return em.createQuery(QueryConstants.GET_ALL_PACKAGES).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PackageDefinition> getAllPackagesByName(String description) {

		String desc = "";
		if (description != null) {
			desc = description;
		}

		if (desc.equalsIgnoreCase("_")) {
			desc = "\\_";

			return em.createQuery(QueryConstants.GET_PACKAGE_BY_NAME_UNDERSCORE)
					.setParameter("description", "%" + desc.toUpperCase() + "%").getResultList();
		} else {
			return em.createQuery(QueryConstants.GET_PACKAGEDESC_BY_NAME)
					.setParameter("description", "%" + desc.toUpperCase() + "%").getResultList();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getPackageIdList() throws ServiceException {
		Query query = em.createNativeQuery(QueryConstants.GET_PACKAGE_ID_DRPDOWN);
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getfulFillmentList() throws ServiceException {
		Query query = em.createNativeQuery(QueryConstants.GET_PACKAGE_LIST);
		return query.getResultList();


	}

	@Override
	public void createPackage(PackageDefinition packageDefinition) throws ServiceException {
		em.persist(packageDefinition);
	}

	@Override
	public void updatePackage(PackageDefinition packageDefinition) throws ServiceException {
		em.merge(packageDefinition);
	}

	public void deletePackage(PackageDefinition packageDefinition) throws ServiceException {
		Query q = em.createQuery(QueryConstants.DELETE_PACKAGE_ATTRIBUTES_BY_ID);
		q.setParameter(CCLPConstants.PACKAGE_ID, packageDefinition.getPackageId());
		q.executeUpdate();

		PackageDefinition newPackageDefinition = em.find(PackageDefinition.class, packageDefinition.getPackageId());
		em.remove(newPackageDefinition);
		em.flush();

	}

	@Override
	public PackageDefinition getPackageIdDtls(String packageId) throws ServiceException {
		return em.find(PackageDefinition.class, packageId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPackageAttributes(String packageId) throws ServiceException {
		Query query = em.createNativeQuery(QueryConstants.GET_PACKAGE_ATTR_LIST).setParameter(CCLPConstants.PACKAGE_ID, packageId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int checkduplicatePackageId(String packageId) throws ServiceException {
		List<BigDecimal> cntList = null;
		cntList = em.createNativeQuery(QueryConstants.GET_PACKAGE_ID_CNT).setParameter(CCLPConstants.PACKAGE_ID, packageId)
				.getResultList();
		BigDecimal cntBD = cntList.get(0);
		return cntBD.intValue();
	}
	
	@Override
	public List<String> getPackageByIds(List<String> packageId) {

		@SuppressWarnings("unchecked")
		List<String> packages  = em.createQuery(QueryConstants.GET_PACKAGES_BY_ID).setParameter(CCLPConstants.PACKAGE_ID, packageId)
				.getResultList();

		return packages;

	}
	
}
