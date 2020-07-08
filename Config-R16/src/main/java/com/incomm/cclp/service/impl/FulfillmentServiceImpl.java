package com.incomm.cclp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.dao.FulfillmentDAO;
import com.incomm.cclp.domain.FulFillmentVendor;
import com.incomm.cclp.dto.FulfillmentDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.FulfillmentService;

@Service
public class FulfillmentServiceImpl implements FulfillmentService {

	@Autowired
	FulfillmentDAO fulfillmentDao;

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	@Transactional
	public void createFulfillment(FulfillmentDTO fulfillmentDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug(fulfillmentDto.getFulFillmentName());
		logger.info(fulfillmentDto.toString());
		ModelMapper mm = new ModelMapper();
		FulFillmentVendor fulFillmentVendor = mm.map(fulfillmentDto, FulFillmentVendor.class);
		fulfillmentDao.createFulfillment(fulFillmentVendor);
		logger.info("Record created for :" + fulFillmentVendor.getFulFillmentName());
		fulfillmentDto.setFulFillmentName(fulFillmentVendor.getFulFillmentName());
		logger.info(CCLPConstants.ENTER);
	}

	@Override
	@Transactional
	public void updateFulfillment(FulfillmentDTO fulfillmentDto) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		logger.info("fulfillment ID :" + fulfillmentDto.toString());
		ModelMapper mm = new ModelMapper();
		FulFillmentVendor fulFillmentVendor = mm.map(fulfillmentDto, FulFillmentVendor.class);
		fulfillmentDao.updateFulfillment(fulFillmentVendor);
		logger.info("Record updated for :" + fulFillmentVendor.getFulfillmentID());
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public FulfillmentDTO getFulfillmentById(long fulfillmentSEQID) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		FulFillmentVendor fulFillmentVendor = fulfillmentDao.getFulfillmentById(fulfillmentSEQID);
		if (fulFillmentVendor != null) {

			logger.info("After retrieving data for FulFillment ID : " + fulFillmentVendor.getFulFillmentSEQID());
		} else {
			logger.info(CCLPConstants.EXIT);
			return null;
		}
		logger.info(CCLPConstants.EXIT);
		return mm.map(fulFillmentVendor, FulfillmentDTO.class);
	}

	@Override
	public int chkDuplicateByID(String fulfillmentID) {
		logger.info(CCLPConstants.ENTER);
		logger.info("fulfillment ID is :" , fulfillmentID);
		int cnt = fulfillmentDao.chkDuplicateByID(fulfillmentID);
		logger.info("Total Count : " , cnt);
		logger.info(CCLPConstants.EXIT);
		return cnt;
	}

	@Override
	public List<FulfillmentDTO> getFulfillmentByName(String fulFillmentName) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<FulfillmentDTO>>() {
		}.getType();
		logger.info(CCLPConstants.EXIT);
		return mm.map(fulfillmentDao.getFulfillmentByName(fulFillmentName), targetListType);
	}

	@Override
	public List<FulfillmentDTO> getAllFulfillments() throws ServiceException {
	
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<FulfillmentDTO>>() {
		}.getType();
		logger.debug("targetListType"+targetListType);
		
		List<FulFillmentVendor> fulFillmentVendor=fulfillmentDao.getAllFulfillments();
		logger.debug("fulFillmentVendor"+fulFillmentVendor);
		logger.info(CCLPConstants.EXIT);
		return mm.map(fulfillmentDao.getAllFulfillments(), targetListType);
	}


	@Override
	public void deleteFulfillment(long fulfillmentID) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("fulfillment ID : {}", fulfillmentID);
		fulfillmentDao.deleteFulfillment(fulfillmentID);
		logger.info(CCLPConstants.EXIT);
	}


	@Override
	public int checkPackageIdMap(String fulfillmentID) {
		 return fulfillmentDao.checkPackageIdMap(fulfillmentID);
	}
}
