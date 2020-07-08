package com.incomm.cclp.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.MerchantRedemptionDAO;
import com.incomm.cclp.domain.MerchantRedemption;
import com.incomm.cclp.dto.MerchantRedemptionDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MerchantRedemptionService;

@Service
public class MerchantRedemptionServiceImpl implements MerchantRedemptionService {
	@Autowired
	MerchantRedemptionDAO merchantRedemptionDao;
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	@Transactional
	public void createMerchant(MerchantRedemptionDTO merchantRedemptionDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(merchantRedemptionDto.toString());
		
		
		MerchantRedemption merchantRedemption = merchantRedemptionDao.getMerchantById(merchantRedemptionDto.getMerchantId());
		
		if (!Objects.isNull(merchantRedemption)) {
			logger.debug("Merchant record already exists with same name: {}", merchantRedemptionDto.getMerchantId());
			throw new ServiceException(("Merch_Redem_Error" + ResponseMessages.ALREADY_EXISTS),
					ResponseMessages.ALREADY_EXISTS);
		}

		List<MerchantRedemptionDTO> merchants = getMerchantsByName(merchantRedemptionDto.getMerchantName());
		List<MerchantRedemptionDTO> existingMerchants = merchants.stream()
				.filter(merchant -> merchant.getMerchantName().equalsIgnoreCase(merchantRedemptionDto.getMerchantName()))
				.collect(Collectors.toList());
		if (existingMerchants != null && !existingMerchants.isEmpty()) {
			logger.debug("Merchant  record already exists with same name: {}", merchantRedemptionDto.getMerchantName());
			throw new ServiceException("MER_" + ResponseMessages.ALREADY_EXISTS, ResponseMessages.ALREADY_EXISTS);
		}

		ModelMapper mm = new ModelMapper();
		MerchantRedemption merchant = mm.map(merchantRedemptionDto, MerchantRedemption.class);
		merchantRedemptionDao.createMerchant(merchant);

		logger.info("Record created for :" + merchant.getMerchantId());
		logger.info(CCLPConstants.EXIT);

		merchantRedemptionDto.setMerchantId(merchant.getMerchantId());
		
	}

	@Override
	public List<MerchantRedemptionDTO> getAllMerchants() {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getAllMerchants");

		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<MerchantRedemptionDTO>>() {
		}.getType();

		logger.info(CCLPConstants.EXIT);
		return mm.map(merchantRedemptionDao.getAllMerchants(), targetListType);
	}
	
	@Override
	public List<MerchantRedemptionDTO> getMerchantsByName(String merchantName) {
		logger.info(CCLPConstants.ENTER);
		logger.debug("inside getMerchantsByName with data : " + merchantName);

		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<MerchantRedemptionDTO>>() {
		}.getType();

		logger.debug("after retrieving data for MerchantName : " + merchantName);
		logger.info(CCLPConstants.EXIT);

		return mm.map(merchantRedemptionDao.getMerchantsByName(merchantName), targetListType);
	}



}
