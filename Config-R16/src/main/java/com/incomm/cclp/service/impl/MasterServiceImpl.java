package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.AttributeDefinitionDAO;
import com.incomm.cclp.dao.MasterDao;
import com.incomm.cclp.domain.AttributeDefinition;
import com.incomm.cclp.domain.AuthenticationType;
import com.incomm.cclp.domain.CountryCode;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.Group;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.domain.PurseType;
import com.incomm.cclp.dto.ClpResourceDTO;
import com.incomm.cclp.dto.CountryCodeDTO;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.PurseTypeDTO;
import com.incomm.cclp.dto.StateCodeDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MasterService;

/**
 * @author lavanyam
 *
 */
@Service
public class MasterServiceImpl implements MasterService{
	
	@Autowired
	MasterDao masterDao;

	@Autowired
	LocalCacheServiceImpl localCacheService;
	
	@Autowired
	AttributeDefinitionDAO attributeDefinitionDao;

	private final Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * @return
	 */

	public List<PurseTypeDTO> getAllPurseType()
	{

		logger.info(CCLPConstants.ENTER);

		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new 
				TypeToken<List<PurseTypeDTO>>() {}.getType();
				
		logger.info(CCLPConstants.EXIT);
		
		return mm.map(masterDao.getAllPurseType(), 
						targetListType);

	}

	/**
	 * Getting all currency codes
	 * 
	 */
	public List<CurrencyCodeDTO> getAllCurrencyCode() throws ServiceException
	{
		logger.info(CCLPConstants.ENTER);
		List<CurrencyCodeDTO> currecyCodeDtoList=null;
		List<CurrencyCode> currencyCodeList=masterDao.getAllCurrencyCode();
		if(currencyCodeList==null){
			logger.error("CurrencyCode is null");
			throw new ServiceException(ResponseMessages.ERR_CURRENCYCODE_NULL);
		}
			currecyCodeDtoList=new ModelMapper().map(currencyCodeList,new TypeToken<List<CurrencyCodeDTO>>() {}.getType());
			currecyCodeDtoList.sort(Comparator.comparing(CurrencyCodeDTO::getCurrCodeAlpha));
			logger.info(CCLPConstants.EXIT);
		return currecyCodeDtoList;

	}

	/**
	 * Getting purse Type by purse Id
	 * 
	 */
	@Override
	public PurseTypeDTO getPurseTypeById(long purseTypeId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		PurseType purseType = masterDao.getPurseTypeById(purseTypeId);
		if(purseType==null){
			logger.error("Purse type is null");
			throw new ServiceException(ResponseMessages.ERR_PURSETYPE_NULL);
		}
		logger.info(CCLPConstants.EXIT);
		return new ModelMapper().map(purseType,new TypeToken<PurseTypeDTO>() {}.getType());
	}
	
	/**
	 * To create currency 
	 * 
	 */
	@Override
	public void createCurrency(CurrencyCodeDTO currencyCodeDTO){
		logger.info(CCLPConstants.ENTER);
		CurrencyCode currencyCode= new ModelMapper().map(currencyCodeDTO,new TypeToken<CurrencyCode>() {}.getType());

		masterDao.createCurrency(currencyCode);
		currencyCodeDTO.setCurrencyTypeID(currencyCode.getCurrencyTypeID());
		logger.info(CCLPConstants.EXIT);
	}
	
	
	/**
	 * Get the fulfillment shipments list. 
	 * 
	 */
	@Override
	public List<Object[]> getFulfillmentShipmentAttrs() throws ServiceException {
		return masterDao.getFulfillmentShipmentAttrs();
		
	}
	
	/**
	 * Get the package shipments list. 
	 * 
	 */
	@Override
	public List<Object[]> getPackageShipmentAttrs() throws ServiceException {
		return masterDao.getPackageShipmentAttrs();
		
	}
	
	@Override
	public List<CountryCodeDTO> getAllCountryCode() {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm=new ModelMapper();
		java.lang.reflect.Type targetListType = new 
				TypeToken<List<CountryCodeDTO>>() {}.getType();
				logger.info(CCLPConstants.EXIT);
				return mm.map(masterDao.getAllCountryCode(), targetListType);
		
		
	}
	

	@Override
	public CountryCodeDTO getCountryCodeById(Long countryCodeId) {
		logger.info(CCLPConstants.ENTER);
		CountryCode countryCode=new CountryCode();
		countryCode.setCountryCodeID(countryCodeId);
		ModelMapper mm = new ModelMapper();
		logger.info(CCLPConstants.EXIT);
		return mm.map(masterDao.getCountryCodeById(countryCode), CountryCodeDTO.class);
		
		
	}
	/**
	 * Getting all menus
	 */
	@Override
	public List<ClpResourceDTO> getMenus() throws ServiceException{
		
		return new ModelMapper().map(masterDao.getMenus(), new TypeToken<List<ClpResourceDTO>>() {}.getType());
		
	}
	
	@Override
	public List<StateCodeDTO> getStateCodeByCountryId(Long countryCodeId) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm=new ModelMapper();
		java.lang.reflect.Type targetListType = new 
				TypeToken<List<StateCodeDTO>>() {}.getType();
				logger.info(CCLPConstants.EXIT);
				return mm.map(masterDao.getStateCodeByCountryId(countryCodeId), targetListType);
	}

	@Override
	public List<Object[]> getCardStatus() throws ServiceException {
		return masterDao.getCardStatusAttrs();
	}

	@Override
	public List<GroupDTO> getGroups() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<Group> groups = masterDao.getGroupsForUser();
		List<GroupDTO> groupDtoList=  new ArrayList<>();
		
		
		groups.stream().forEach(group->{
			GroupDTO groupDto = new GroupDTO();
			groupDto.setGroupId(group.getGroupId());
			groupDto.setGroupName(group.getGroupName());
			groupDtoList.add(groupDto);
		});
		logger.info(CCLPConstants.EXIT);
		return groupDtoList;
	}

	@Override
	public Map<String, Map<String, String>> getAllAuthenticationTypes() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, String>> authenticationTypes = new HashMap<>();
		Map<String, String> chwUserAuthenticationTypes = new HashMap<>();
		Map<String, String> ivrUserAuthenticationTypes = new HashMap<>();

		try {
			List<AuthenticationType> listOfAuthenticationType = masterDao.getAllAuthenticationTypes();

			if (!CollectionUtils.isEmpty(listOfAuthenticationType)) {
				listOfAuthenticationType.stream().forEach(authenticationType -> {
					if ("Y".equalsIgnoreCase(authenticationType.getChwSupport())) {
						chwUserAuthenticationTypes.put(authenticationType.getAuthTypeId().toString(),
								authenticationType.getAuthTypeDesc());
					}
					if ("Y".equalsIgnoreCase(authenticationType.getIvrSupport())) {
						ivrUserAuthenticationTypes.put(authenticationType.getAuthTypeId().toString(),
								authenticationType.getAuthTypeDesc());
					}
				});

				authenticationTypes.put("chwUserAuthType", chwUserAuthenticationTypes);
				authenticationTypes.put("ivrUserAuthType", ivrUserAuthenticationTypes);

			} else {
				logger.debug("Master Data not present in AUTHENTICATION_TYPE table");

				authenticationTypes.put("chwUserAuthType", chwUserAuthenticationTypes);
				authenticationTypes.put("ivrUserAuthType", ivrUserAuthenticationTypes);
			}
		} catch (Exception e) {
			logger.error("Exception occured while getting Authentication Master data {}", e);

			throw new ServiceException(ResponseMessages.ERR_AUTHENTICATION_TYPE);
		}

		logger.info(CCLPConstants.EXIT);
		return authenticationTypes;
	}

	@Override
	public Map<String, Map<String, String>> getAuthenticationTypes() throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, Map<String, String>> authenticationTypes = null;

		try {
			authenticationTypes = localCacheService.getAllAuthenticationTypes(authenticationTypes);
			if (CollectionUtils.isEmpty(authenticationTypes)) {
				authenticationTypes = getAllAuthenticationTypes();

				localCacheService.getAllAuthenticationTypes(authenticationTypes);
			}
		} catch (Exception e) {
			logger.error("Exception occured while getting Authentication Master data {}", e);

			throw new ServiceException(ResponseMessages.ERR_AUTHENTICATION_TYPE);
		}

		logger.info(CCLPConstants.EXIT);

		return authenticationTypes;
	}

	@Override
	public Map<String, String> getEnvironmentTypes() throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, String> environmentsTypes = new HashMap<>();

		try {
			List<AttributeDefinition> envAttributeList = attributeDefinitionDao
					.getAttributeDefinitionsByGroupName(CCLPConstants.ATTRIBUTES_GROUP_ENV_PARAMETERS);

			if (!CollectionUtils.isEmpty(envAttributeList)) {
				envAttributeList.stream()
						.forEach(attr -> environmentsTypes.put(attr.getAttributeValue(), attr.getAttributeName()));
			} else {
				logger.debug("Attribute group 'Environment' missing in ATTRIBUTE_DEFINITION table");
			}
		} catch (Exception e) {
			logger.error("Exception occured while getting Environment Master data {}", e);

			throw new ServiceException(ResponseMessages.ERR_ENV_TYPE);
		}

		logger.info(CCLPConstants.EXIT);
		return environmentsTypes;
	}

	@Override
	public Map<String, String> getAllEnvironmentTypes() throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, String> environmentTypes = null;

		try {
			environmentTypes = localCacheService.getAllEnvironmentTypes(environmentTypes);
			if (CollectionUtils.isEmpty(environmentTypes)) {
				environmentTypes = getEnvironmentTypes();

				localCacheService.getAllEnvironmentTypes(environmentTypes);
			}
		} catch (Exception e) {
			logger.error("Exception occured while getting Environment Master data {}", e);

			throw new ServiceException(ResponseMessages.ERR_ENV_TYPE);
		}

		logger.info(CCLPConstants.EXIT);

		return environmentTypes;
	}
	
	/**
	 * Getting all purses
	 * 
	 */
	@Override
	public List<PurseDTO> getAllPurse() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<PurseDTO> purseDtoList=null;
		List<Purse> purseList=masterDao.getAllPurse();
		if(purseList==null){
			logger.error("Purse list is null");
			throw new ServiceException(ResponseMessages.ERR_PURSE_NULL);
		}
		purseDtoList=new ModelMapper().map(purseList,new TypeToken<List<PurseDTO>>() {}.getType());
		purseDtoList.sort(Comparator.comparing(PurseDTO::getPurseTypeId).thenComparing(PurseDTO::getExtPurseId));
			logger.info(CCLPConstants.EXIT);
		return purseDtoList;
	}

}
