package com.incomm.scheduler.service;

import java.util.List;

import com.incomm.scheduler.dto.CardNumberInventoryDTO;
import com.incomm.scheduler.exception.ServiceException;

public interface CardNumberInventoryService {

	public List<CardNumberInventoryDTO> getCardInvtryDtls();
	
	public void initiateCardNumberGeneration(Long cardRangeId, String action) throws ServiceException;

	public void pauseCardGenerationProcess(Long cardRangeId) throws ServiceException;

	public CardNumberInventoryDTO getCardInvtryDtlsById(Long cardRangeId) throws ServiceException;

}
