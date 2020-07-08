package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.ClpResourceDTO;
import com.incomm.cclp.dto.CountryCodeDTO;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.PurseTypeDTO;
import com.incomm.cclp.dto.StateCodeDTO;
import com.incomm.cclp.exception.ServiceException;

public interface MasterService {
	
	public List<PurseTypeDTO> getAllPurseType();
	
	public List<CurrencyCodeDTO> getAllCurrencyCode() throws ServiceException;

	public PurseTypeDTO getPurseTypeById(long purseTypeId) throws ServiceException ;
	
	public void createCurrency(CurrencyCodeDTO currencyCodeDTO);
	
	public List<Object[]> getFulfillmentShipmentAttrs() throws ServiceException;
	
	public List<Object[]> getPackageShipmentAttrs() throws ServiceException;

	public List<CountryCodeDTO> getAllCountryCode();
	
	public CountryCodeDTO getCountryCodeById(Long countryCode);
	
	public List<ClpResourceDTO> getMenus() throws ServiceException;
	
	public List<StateCodeDTO> getStateCodeByCountryId(Long countryCodeId);

	public List<Object[]> getCardStatus() throws ServiceException;

	public List<GroupDTO> getGroups() throws ServiceException;
	
	public Map<String, Map<String,String>> getAuthenticationTypes() throws ServiceException;
	
	public Map<String, Map<String,String>> getAllAuthenticationTypes() throws ServiceException;
	
	public Map<String,String> getEnvironmentTypes() throws ServiceException;
	
	public Map<String,String> getAllEnvironmentTypes() throws ServiceException;

	public List<PurseDTO> getAllPurse() throws ServiceException;
}
