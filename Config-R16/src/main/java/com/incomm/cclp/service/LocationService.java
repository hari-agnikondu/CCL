/**
 * 
 */
package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.LocationDTO;
import com.incomm.cclp.exception.ServiceException;



public interface LocationService {

	public List<LocationDTO> getAllLocations();

	public List<LocationDTO> getLocationByMerchantName(String merchantName) throws ServiceException;
	public void createLocation(LocationDTO locationDto) throws ServiceException;
	
	public void updateLocation(LocationDTO locationDto) throws ServiceException;
	
	public LocationDTO locationById(Long locationId) throws ServiceException;
	
	public void deleteLocation(LocationDTO locationDto) throws ServiceException;
}
