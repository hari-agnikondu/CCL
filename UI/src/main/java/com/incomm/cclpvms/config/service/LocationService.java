package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.config.model.CountryCodeDTO;
import com.incomm.cclpvms.config.model.Location;
import com.incomm.cclpvms.config.model.LocationDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;


public interface LocationService {
	
	public List<Location> getAllLocations(Location location) throws ServiceException;
	
	public ResponseDTO addLocation(Location locationConfig) throws ServiceException ;
	
	public Location getLocationById(Long locationId) throws ServiceException ;
	
	public ResponseEntity<ResponseDTO> updateLocation(Location location) throws ServiceException ;

	public Map<Long,String> getAllCountries() throws ServiceException;

	public CountryCodeDTO getStates(Long countryId)throws ServiceException;
	
	public Map<Long,String> listStates(Long countryId)throws ServiceException;

	public ResponseEntity<ResponseDTO> deleteLocations(Long countryId)throws ServiceException;
	
}
