package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.BlockListDTO;
import com.incomm.cclp.exception.ServiceException;

public interface BlockListService {

	void createBlockList(BlockListDTO blockListDto) throws ServiceException;

	void deleteBlockList(List<Object> instrumentIds) throws ServiceException;

	List<BlockListDTO> getAllBlockList();

	List<BlockListDTO> getBlockListById(String channelCode);

	List<Object[]> getDeliveryChannelList();
	

}
