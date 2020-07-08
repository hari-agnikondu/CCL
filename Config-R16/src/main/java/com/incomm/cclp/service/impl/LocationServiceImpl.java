/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.LocationDAO;
import com.incomm.cclp.domain.Location;
import com.incomm.cclp.dto.LocationDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.LocationService;

/**
 * Location Service provides all the Service operations for Location.
 *
 */

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	LocationDAO locationDAO;

	private final Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * Gets all Locations.
	 * 
	 * @return the list of all Locations.
	 */
	@Override
	public List<LocationDTO> getAllLocations() {
		logger.info(CCLPConstants.ENTER);

		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<LocationDTO>>() {
		}.getType();

		mm.getConfiguration().setAmbiguityIgnored(true);
		logger.info(CCLPConstants.EXIT);
		return mm.map(locationDAO.getAllLocations(), targetListType);
	}

	/**
	 * Getting Location based on Merchant Name
	 * 
	 * @throws ServiceException
	 * 
	 */
	@Override
	public List<LocationDTO> getLocationByMerchantName(String merchantName)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<LocationDTO> locationDtoList = null;

		List<Location> locationList = locationDAO
				.getLocationByMerchantName(merchantName);

		if (locationList != null) {
			ModelMapper mm = new ModelMapper();
			mm.getConfiguration().setAmbiguityIgnored(true);
			locationDtoList = mm.map(locationList,
					new TypeToken<List<LocationDTO>>() {
					}.getType());
		}
		logger.info(CCLPConstants.EXIT);
		return locationDtoList;

	}

	/**
	 * Create a Location.
	 * 
	 * @throws ServiceException
	 */
	@Override
	public void createLocation(LocationDTO locationDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<LocationDTO> existingLocation=getAllLocations();
		
		LocationDTO matchLocation =existingLocation		
				.stream()
				.filter(p -> (p.getLocationName() != null ? p
						.getLocationName() : "").equalsIgnoreCase(locationDto.getLocationName())
						&& p.getMerchantName().equalsIgnoreCase(locationDto.getMerchantName()))
				.findAny().orElse(null);
		if (matchLocation == null) {

			ModelMapper mm = new ModelMapper();
			mm.getConfiguration().setAmbiguityIgnored(true);
			Location location = mm.map(locationDto, Location.class);

			locationDAO.createLocation(location);
		} else {
			logger.error("Location already exists");
			throw new ServiceException(
					ResponseMessages.ERR_LOCATION_ALREADY_EXIST);
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public void updateLocation(LocationDTO locationDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		LocationDTO existinglocation = locationById(locationDto.getLocationId());
		
		if(existinglocation != null)
		{
			ModelMapper mm = new ModelMapper();
			Location location = mm.map(locationDto, Location.class);
			if(existinglocation.getLocationName().equalsIgnoreCase(locationDto.getLocationName())) {
				locationDAO.updateLocation(location);	
			}else {
				List<LocationDTO> existingLocation=getAllLocations();
				boolean flag=existingLocation.stream().anyMatch(existLocation -> (existLocation.getLocationName().equalsIgnoreCase(locationDto.getLocationName())
						&& existLocation.getMerchantName().equalsIgnoreCase(locationDto.getMerchantName())));

				
				if(!flag) {
					locationDAO.updateLocation(location);	
				}else {
					logger.error("Loaction already exists");
					throw new ServiceException(
							ResponseMessages.ERR_LOCATION_ALREADY_EXIST);
				}

				
			}
				
		}
		logger.info(CCLPConstants.EXIT);

	}

	@Override
	public LocationDTO locationById(Long locationId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Location locatioDao = locationDAO.getLocationById(locationId);
		ModelMapper mm = new ModelMapper();

		if (locatioDao != null) {
			mm.getConfiguration().setAmbiguityIgnored(true);
			LocationDTO locationDto =  mm.map(locatioDao, LocationDTO.class);
			locationDto.setMerchantId(locatioDao.getMerchant().getMerchantId());
			logger.info(CCLPConstants.EXIT);
			return locationDto;

		} else {
			logger.error("Location does not exist");
			throw new ServiceException(
					ResponseMessages.ERR_LOCATION_DOES_NOT_EXIST);
		}
	}

	@Override
	public void deleteLocation(LocationDTO locationDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		LocationDTO locationdto = locationById(locationDto.getLocationId());

		ModelMapper mm = new ModelMapper();
		Location location = mm.map(locationdto, Location.class);
		locationDto.setLocationName(location.getLocationName());
		 locationDto.setMerchantName(location.getMerchant().getMerchantName());
		BigDecimal count = locationDAO.isMappedToInventory(locationDto
				.getLocationId());

		if (count.intValue() > 0) {
			logger.error("Count is: {}",count.intValue());
			throw new ServiceException(ResponseMessages.ERROR_LOCATOIN_COUNT);
		} else {
			logger.info("Deleting the location");
			locationDAO.deleteLocation(location);
		}
		logger.info(CCLPConstants.EXIT);
		return;
	}

}
