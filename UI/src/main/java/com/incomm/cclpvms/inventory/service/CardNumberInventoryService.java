package com.incomm.cclpvms.inventory.service;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface CardNumberInventoryService {

	ResponseDTO getAllCardInvntryDtls() throws ServiceException;

	ResponseDTO generateCardInventory(long cardRangeId) throws ServiceException;

	ResponseDTO pauseCardInventory(long cardRangeId) throws ServiceException;

	ResponseDTO resumeCardInventory(long cardRangeId) throws ServiceException;

	ResponseDTO regenerateCardInventory(long cardRangeId) throws ServiceException;

	
}
