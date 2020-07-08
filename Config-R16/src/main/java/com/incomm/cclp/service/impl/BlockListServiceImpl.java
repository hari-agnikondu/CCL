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
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.BlockListDAO;
import com.incomm.cclp.domain.BlockList;
import com.incomm.cclp.dto.BlockListDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.BlockListService;

@Service
public class BlockListServiceImpl implements BlockListService {

	@Autowired
	BlockListDAO blockListDao;
	// the logger
	private static final Logger logger = LogManager.getLogger(BlockListServiceImpl.class);

	@Override
	@Transactional
	public void createBlockList(BlockListDTO blockListDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		int  existingBlockList = blockListDao.isBlockListExist(blockListDto.getInstrumentId());
		if (existingBlockList >0) {
			logger.error("Error Block list record already exist '{}'", blockListDto);
			throw new ServiceException(ResponseMessages.ERR_BLOCKLIST_EXISTS, ResponseMessages.ALREADY_EXISTS);
		}
		BlockList blockList = mm.map(blockListDto, BlockList.class);
		blockListDao.createBlockList(blockList);
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public List<Object[]> getDeliveryChannelList() {
		return blockListDao.getDeliveryChannelList();
	}

	@Override
	public List<BlockListDTO> getAllBlockList() {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<BlockListDTO>>() {
		}.getType();
		logger.info(CCLPConstants.EXIT);
		return mm.map(blockListDao.getAllBlockList(), targetListType);
	}

	@Override
	public List<BlockListDTO> getBlockListById(String channelCode) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<BlockListDTO>>() {
		}.getType();
		logger.info(CCLPConstants.EXIT);
		return mm.map(blockListDao.getBlockListById(channelCode), targetListType);
	}

	@Override
	public void deleteBlockList(List<Object> instrumentIds) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		int deletecount = blockListDao.deleteBlockList(instrumentIds);
		if (deletecount <= 0) {
			logger.error("Error while deleting BlockList record for '{}'", instrumentIds);
			throw new ServiceException(ResponseMessages.FAIL_BLOCKLIST_DELETE, ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);

	}

}
