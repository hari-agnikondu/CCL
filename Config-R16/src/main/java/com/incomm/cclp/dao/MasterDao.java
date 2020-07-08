package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.AuthenticationType;
import com.incomm.cclp.domain.ClpResource;
import com.incomm.cclp.domain.CountryCode;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.Group;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.domain.PurseType;
import com.incomm.cclp.domain.StateCode;

public interface MasterDao {
	
	public List<PurseType> getAllPurseType();
	
	public List<CurrencyCode> getAllCurrencyCode();

	void createCurrency(CurrencyCode currencycode);

	void createPurseType(PurseType pursetype);

	public PurseType getPurseTypeById(long purseTypeId);
	
	public List<Object[]> getFulfillmentShipmentAttrs();
	
	public List<Object[]> getPackageShipmentAttrs();
	
	public List<CountryCode> getAllCountryCode();
	
	public CountryCode getCountryCodeById(CountryCode countryCode);
	
	public List<ClpResource> getMenus();
	
	public List<StateCode> getStateCodeByCountryId(Long countryCodeId);

	public List<Object[]> getCardStatusAttrs();

	public List<Group> getGroupsForUser();
	
	public List<AuthenticationType> getAllAuthenticationTypes();

	public List<Purse> getAllPurse();
	
}
