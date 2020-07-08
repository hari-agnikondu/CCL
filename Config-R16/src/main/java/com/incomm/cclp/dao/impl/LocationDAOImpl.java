/**
 * 
 */
package com.incomm.cclp.dao.impl;


import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.LocationDAO;
import com.incomm.cclp.domain.Location;

/**
 * Purse DAO provides all the Location data access operations.
 * 
 */
@Repository
public class LocationDAOImpl implements LocationDAO {

	// the entity manager
	@PersistenceContext
	private EntityManager em;


	
	@Override
	@Transactional
	public void createLocation(Location location) {
		em.persist(location);
	}

	
	@Override
	@Transactional
	public void updateLocation(Location location) {
		em.merge(location);
		
	}
	@Override
	@Transactional
	public void deleteLocation(Location location){
		em.remove(em.contains(location) ? location : em.merge(location));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Location> getAllLocations() {

		return em.createQuery(QueryConstants.GET_ALL_LOCATIONS)
				.getResultList();
	}

	@Override
	public Location getLocationById(Long locationId) {
		return em.find(Location.class, locationId);
	}
	
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Location> getLocationByMerchantName(String merchantName){
		
		String qry=QueryConstants.GET_ALL_LOCATIONS;
		
		if(merchantName!=null && !merchantName.isEmpty()){
			qry+= " where upper(location.merchant.merchantName) like :merchantName";
			
			return em.createQuery(qry)
					.setParameter("merchantName", "%"+merchantName.replaceAll("_", "\\_").toUpperCase()+"%")
					.getResultList();
		}
		else{
			qry+=" order by location.merchant.merchantName";
			return em.createQuery(qry)
					.getResultList();
		}

	}
	

	@Override
	public BigDecimal isMappedToInventory(Long locationId) {

		return (BigDecimal)  em.createNativeQuery(QueryConstants.COUNT_BY_LOCATION)
				.setParameter("locationId", locationId).getSingleResult();
	}
	
	
}