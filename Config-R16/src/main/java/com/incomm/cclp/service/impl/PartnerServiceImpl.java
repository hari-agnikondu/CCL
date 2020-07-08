package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.PartnerDAO;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.Partner;
import com.incomm.cclp.domain.PartnerCurrency;
import com.incomm.cclp.domain.PartnerCurrencyID;
import com.incomm.cclp.domain.PartnerPurse;
import com.incomm.cclp.domain.PartnerPurseID;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CacheService;
import com.incomm.cclp.service.PartnerService;

/**
 * PartnerServiceImpl class defines all the Business Logic for Partner
 * Configuration.
 *
 */

@Service
public class PartnerServiceImpl implements PartnerService {

	@Autowired
	CacheService cacheService;
	
	@Autowired
	PartnerDAO partnerDao;

	// the logger
	private static final Logger logger = LogManager.getLogger(PartnerServiceImpl.class);

	@Override
	@Transactional
	public void createPartner(PartnerDTO partnerDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		// Partner name check
		List<PartnerDTO> existingPartnersBypartnerName = getPartnerByName(partnerDto.getPartnerName());
		if (existingPartnersBypartnerName != null && !existingPartnersBypartnerName.isEmpty()) {
			Iterator<PartnerDTO> iterator = existingPartnersBypartnerName.iterator();
			while (iterator.hasNext()) {
				PartnerDTO partner = iterator.next();
				if (partner.getPartnerName().equalsIgnoreCase(partnerDto.getPartnerName()))
					throw new ServiceException(ResponseMessages.ERR_PARTNER_EXISTS, ResponseMessages.ALREADY_EXISTS);
			}
			logger.debug("Partner name '{}' does not exist in table", partnerDto.getPartnerName());
		}

		// Unique MDM ID check
		List<PartnerDTO> existingPartners = getAllPartners();
		if (existingPartners != null && !existingPartners.isEmpty()) {
			Iterator<PartnerDTO> iterator = existingPartners.iterator();
			while (iterator.hasNext()) {
				PartnerDTO partner = iterator.next();
				if (partner.getMdmId().equalsIgnoreCase(partnerDto.getMdmId()))
					throw new ServiceException(ResponseMessages.ERR_MDMID_EXISTS, ResponseMessages.ALREADY_EXISTS);
			}
			logger.debug("MDM  ID '{}' does not exist in table", partnerDto.getMdmId());
		}

		Partner partner = convertPartnerDtoToEntity(partnerDto);
		partnerDao.createPartner(partner);

		cacheService.updateCache(CCLPConstants.PARTNER_CACHE,String.valueOf(partner.getPartnerId()));

		partnerDto.setPartnerId(partner.getPartnerId());
		logger.info(CCLPConstants.EXIT);
	}

	private Partner convertPartnerDtoToEntity(PartnerDTO partnerDto) {
		logger.info(CCLPConstants.ENTER);
		Partner partner = new Partner();
		partner.setPartnerName(partnerDto.getPartnerName());
		partner.setPartnerDesc(partnerDto.getPartnerDesc());
		partner.setPartnerId(partnerDto.getPartnerId());
		partner.setInsUser(partnerDto.getInsUser());
		partner.setLastUpdUser(partnerDto.getLastUpdUser());
		partner.setMdmId(partnerDto.getMdmId());
		partner.setIsActive(partnerDto.getIsActive());

		partner.setPartnerCurrencyList(getPartnerCurrencyList(partnerDto.getCurrencyList(), partner));
		partner.setPartnerPurseList(getPartnerPurseList(partnerDto.getPurseList(), partner));
		logger.info(CCLPConstants.EXIT);
		return partner;
	}

	private List<PartnerCurrency> getPartnerCurrencyList(List<String> currencyList, Partner partner) {
		logger.info(CCLPConstants.ENTER);
		List<PartnerCurrency> partnerCurrencyList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(currencyList)) {
			for (String currencyId : currencyList) {
				PartnerCurrency partnerCurrency = new PartnerCurrency();
				PartnerCurrencyID partnerCurrencyId = new PartnerCurrencyID();

				CurrencyCode currency = partnerDao.getCurrencyCodeById(currencyId);
				partnerCurrencyId.setCurrencyCode(currency);

				partnerCurrencyId.setPartner(partner);
				partnerCurrency.setPartnerCurrencyID(partnerCurrencyId);
				partnerCurrency.setInsUser(partner.getInsUser());
				partnerCurrency.setLastUpdUser(partner.getLastUpdUser());
				partnerCurrencyList.add(partnerCurrency);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return partnerCurrencyList;
	}

	private List<PartnerPurse> getPartnerPurseList(List<String> purseList, Partner partner) {
		logger.info(CCLPConstants.ENTER);
		List<PartnerPurse> partnerPurseList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(purseList)) {
			for (String purseId : purseList) {
				PartnerPurse partnerPurse = new PartnerPurse();
				PartnerPurseID partnerPurseId = new PartnerPurseID();

				Purse purse = partnerDao.getPurseById(purseId);
				partnerPurseId.setPurse(purse);

				partnerPurseId.setPartner(partner);
				partnerPurse.setPartnerPurseID(partnerPurseId);
				partnerPurse.setInsUser(partner.getInsUser());
				partnerPurse.setLastUpdUser(partner.getLastUpdUser());
				partnerPurseList.add(partnerPurse);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return partnerPurseList;
	}

	@Override
	public List<PartnerDTO> getAllPartners() {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<PartnerDTO>>() {
		}.getType();
		logger.info(CCLPConstants.EXIT);
		return mm.map(partnerDao.getAllPartners(), targetListType);
	}

	@Override
	@Transactional
	public void updatePartner(PartnerDTO partnerDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		List<PartnerDTO> existingPartnersBypartnerName = new ArrayList<>();
		try {
			existingPartnersBypartnerName = getPartnerByName(partnerDto.getPartnerName());
		} catch (Exception e) {
			logger.error("Partner already exists");
			throw new ServiceException(ResponseMessages.ERR_PARTNER_EXISTS, ResponseMessages.ALREADY_EXISTS);
		}
		
		if (!existingPartnersBypartnerName.isEmpty()) {
			Iterator<PartnerDTO> iterator = existingPartnersBypartnerName.iterator();
			while (iterator.hasNext()) {
				PartnerDTO partner = iterator.next();
				if (partner.getPartnerName().equalsIgnoreCase(partnerDto.getPartnerName())
						&& (partner.getPartnerId() != partnerDto.getPartnerId()))
					throw new ServiceException(ResponseMessages.ERR_PARTNER_EXISTS, ResponseMessages.ALREADY_EXISTS);
			}
			logger.debug("Partner name '{}' already exists in table", partnerDto.getPartnerName());
		}

		// Unique MDM ID check

		List<PartnerDTO> existingPartners = getAllPartners();
		if (existingPartners != null && !existingPartners.isEmpty()) {
			Iterator<PartnerDTO> iterator = existingPartners.iterator();
			while (iterator.hasNext()) {
				PartnerDTO partner = iterator.next();
				if (partner.getMdmId().equalsIgnoreCase(partnerDto.getMdmId())
						&& (partner.getPartnerId() != partnerDto.getPartnerId()))
					throw new ServiceException(ResponseMessages.ERR_MDMID_EXISTS, ResponseMessages.ALREADY_EXISTS);
			}
			logger.debug("MDM  ID '{}' does not exist in table", partnerDto.getMdmId());
		}

		List<String> currencyList = partnerDto.getCurrencyList();
		List<String> attachedCurrencyList = partnerDao.getAttachedCurrencyList(partnerDto.getPartnerId());

		int size = 0;
		Iterator<String> itr = attachedCurrencyList.iterator();
		while (itr.hasNext()) {
			if (!currencyList.contains(itr.next()))
				size++;
		}
		if (size > 0) {
			logger.error("Attached currency already exists");
			throw new ServiceException(ResponseMessages.ERR_CURRENCY_ATTACHED, ResponseMessages.ALREADY_EXISTS);
		}

		List<String> purseList = partnerDto.getPurseList();
		List<String> attachedPurseList = partnerDao.getAttachedPurseList(partnerDto.getPartnerId());
		int purseCount = 0;
		
		Iterator<String> itr1 = attachedPurseList.iterator();
		while (itr1.hasNext()) {
			if (!purseList.contains(String.valueOf(itr1.next()))) {
				purseCount++;
			}
		}
		if (purseCount > 0) {
			logger.error("Attached purse is already mapped to a product,Cannot be removed from supported purse");
			throw new ServiceException(ResponseMessages.ERR_PURSE_ATTACHED_TO_PRODUCT, ResponseMessages.ALREADY_EXISTS);
		}

		Partner partner = convertPartnerDtoToEntity(partnerDto);
		partnerDao.updatePartner(partner);
		
		cacheService.updateCache(CCLPConstants.PARTNER_CACHE,String.valueOf(partner.getPartnerId()));
		
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deletePartner(long partnerId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		if (partnerDao.partnerLinkCheck(partnerId) > 0) {
			logger.error("Partner attached to product(s)");
			throw new ServiceException(ResponseMessages.FAIL_PARTNER_ATTACHED_TO_PRODUCT_DELETE,
					ResponseMessages.ALREADY_EXISTS);
		} else {
			// Del program
			partnerDao.deletePartnerProg(partnerId);

			// Del currency
			partnerDao.deletePartnerCurr(partnerId);

			// Del partnerPurse
			int deletePrtnrPrseCnt = partnerDao.deletePartnerPurse(partnerId);
			if (deletePrtnrPrseCnt <= 0) {
				logger.error("Error while deleting partner purse record for '{}'", partnerId);
				// throw new ServiceException(ResponseMessages.FAIL_PARTNER_PURSE_DELETE,
				// ResponseMessages.DOESNOT_EXISTS);
			}

			int deletecount = partnerDao.deletePartner(partnerId);
			if (deletecount <= 0) {
				logger.error("Error while deleting record for '{}'", partnerId);
				throw new ServiceException(ResponseMessages.FAIL_PARTNER_DELETE, ResponseMessages.DOESNOT_EXISTS);
			}
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public PartnerDTO getPartnerById(Long partnerId) {

		logger.info(CCLPConstants.ENTER);
		Partner partner = partnerDao.getPartnerById(partnerId);
		if (partner == null) {
			logger.error("Error while fetching record for partnerId: {}", partnerId);
			logger.info(CCLPConstants.EXIT);
			return null;
		}

		PartnerDTO partnerDto = new PartnerDTO(partner.getPartnerId(), partner.getPartnerName(),
				partner.getPartnerDesc(), partner.getMdmId(), partner.getIsActive(), partner.getInsUser(),
				partner.getLastUpdUser(), partner.getInsDate(), partner.getLastUpdDate(),
				getListOfCurrencyCodes(partner.getPartnerCurrencyList()),
				getListOfPurse(partner.getPartnerPurseList()));
		logger.info(CCLPConstants.EXIT);
		return partnerDto;
	}

	private List<CurrencyCode> getListOfCurrencyCodes(List<PartnerCurrency> partnerCurrencyList) {
		logger.info(CCLPConstants.ENTER);
		List<CurrencyCode> listOfCurrencyCode = new ArrayList<>(0);
		if (!CollectionUtils.isEmpty(partnerCurrencyList)) {
			partnerCurrencyList.forEach(partnerCurrency -> listOfCurrencyCode.add(partnerCurrency.getCurrencyCode()));
		}
		logger.info(CCLPConstants.EXIT);
		return listOfCurrencyCode;
	}

	private List<Purse> getListOfPurse(List<PartnerPurse> partnerPurseList) {
		logger.info(CCLPConstants.ENTER);
		List<Purse> listOfPurse = new ArrayList<>(0);
		if (!CollectionUtils.isEmpty(partnerPurseList)) {
			partnerPurseList.forEach(partnerPurse -> listOfPurse.add(partnerPurse.getPurse()));
		}
		logger.info(CCLPConstants.EXIT);
		return listOfPurse;
	}

	@Override
	public List<PartnerDTO> getPartnerByName(String partnerName) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<PartnerDTO>>() {
		}.getType();
		logger.info(CCLPConstants.EXIT);
		return mm.map(partnerDao.getPartnerByName(partnerName), targetListType);

	}

	@Override
	public List<PurseDTO> getAllSupportedPursesByPartnerId(Long partnerId) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = null;
		List<PurseDTO> supportedPurseList = null;
		List<Purse> purseList = null;
		try {
			targetListType = new TypeToken<List<PurseDTO>>() {
			}.getType();
			Partner partner = partnerDao.getPartnerById(partnerId);
			List<PartnerPurse> partnerPurses = partner.getPartnerPurseList();
			purseList = partnerPurses.stream().map(PartnerPurse::getPurse).collect(Collectors.toList());
			supportedPurseList = mm.map(purseList, targetListType);
			logger.info(CCLPConstants.EXIT);
		} catch (Exception e) {
			logger.error(e);
		}
		if (!CollectionUtils.isEmpty(supportedPurseList)) {
			supportedPurseList.sort(Comparator.comparing(PurseDTO::getPurseTypeId));
		}
		return supportedPurseList;
	}

}
