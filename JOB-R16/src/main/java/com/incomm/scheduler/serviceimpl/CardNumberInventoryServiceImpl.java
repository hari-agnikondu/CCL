package com.incomm.scheduler.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.ResponseMessages;
import com.incomm.scheduler.dao.CardNumberInventoryDAO;
import com.incomm.scheduler.dto.CardNumberInventoryDTO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.CardRange;
import com.incomm.scheduler.model.CardRangeInventory;
import com.incomm.scheduler.service.CardNumberInventoryService;
import com.incomm.scheduler.utils.Util;

@Service
public class CardNumberInventoryServiceImpl implements CardNumberInventoryService {

	@Autowired
	CardNumberInventoryDAO cardInvtryDao;

	private static final Logger logger = LogManager.getLogger(CardNumberInventoryServiceImpl.class);

	@Override
	public List<CardNumberInventoryDTO> getCardInvtryDtls() {
		logger.info(CCLPConstants.ENTER);

		List<CardNumberInventoryDTO> cardNumberInventoryDtos = new ArrayList<>();

		List<CardRange> cardRanges = cardInvtryDao.getCardInvtryDtls();

		if (!CollectionUtils.isEmpty(cardRanges)) {
			cardRanges.stream().forEach(cardRange -> {
				CardNumberInventoryDTO cardNumberInventoryDto = new CardNumberInventoryDTO();
				cardNumberInventoryDto.setCardRangeId(cardRange.getCardRangeId());
				cardNumberInventoryDto
						.setIssuerName(cardRange.getIssuer() != null ? cardRange.getIssuer().getIssuerName() : null);
				cardNumberInventoryDto.setPrefix(cardRange.getPrefix());
				cardNumberInventoryDto.setStartCardNbr(cardRange.getStartCardNbr());
				cardNumberInventoryDto.setEndCardNbr(cardRange.getEndCardNbr());
				cardNumberInventoryDto.setCardLength(cardRange.getCardLength());
				cardNumberInventoryDto.setCardInventory(cardRange.getCardInventory());

				CardRangeInventory cardRangeInventory = cardRange.getCardRangeInventory();
				if (!Objects.isNull(cardRangeInventory)) {
					cardNumberInventoryDto.setStatus(cardRangeInventory.getInventoryStatus());
				}

				cardNumberInventoryDtos.add(cardNumberInventoryDto);
			});
		}

		logger.info("Retrived Card Range Inventory details Successfully");

		logger.info(CCLPConstants.EXIT);
		return cardNumberInventoryDtos;
	}

	@Override
	public void initiateCardNumberGeneration(Long cardRangeId, String action) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		String inventoryStatus = "";	

		CardRangeInventory cardRangeInventory = cardInvtryDao.getCardRangeInventoryByCardRange(cardRangeId);
		if (!Objects.isNull(cardRangeInventory)) {
			inventoryStatus = cardRangeInventory.getInventoryStatus();
		}
		
		logger.info("Inventory Card Generation process for Card Range Id: {} current status was '{}'", cardRangeId,
				inventoryStatus);

		if (CCLPConstants.CARDNUMBER_INVENTORY_ACTION_GENERATE.equalsIgnoreCase(action)) {

			if (Util.isEmpty(inventoryStatus)) {
				try {

					int qryStatus = cardInvtryDao.initiateCardNumberGeneration(cardRangeId);

					if (qryStatus != 1) {
						logger.debug("Card Number generation for Card Range id: {} failed to start the procedure", cardRangeId);
						
						throw new ServiceException(ResponseMessages.FAILED_TO_START_CARDNUMGEN,
								ResponseMessages.FAILED_TO_START_CARDNUMGEN);
					}

				} catch (Exception e) {
					logger.error("Error occured while try to start Card Number generation for Card Range id: {},"
							+ " Exception: {}", cardRangeId, e);
					throw new ServiceException(ResponseMessages.FAILED_TO_START_CARDNUMGEN,
							ResponseMessages.FAILED_TO_START_CARDNUMGEN);
				}
				
				logger.info("Inventory Card Generation process for Card Range Id: {} started successfully", cardRangeId);
				 
			} else {

				if (CCLPConstants.INVENTORY_STATUS_STARTED.equalsIgnoreCase(inventoryStatus)) {
					logger.debug("Card number generation for card range id : {} already started", cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED,
							ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED);
				}

				if (CCLPConstants.INVENTORY_STATUS_PAUSED.equalsIgnoreCase(inventoryStatus)) {
					logger.debug("Card number generation for card range id : {} already in PAUSE state", cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERY_PAUSED,
							ResponseMessages.ERR_CARDNUMGEN_ALERY_PAUSED);
				}

				if (CCLPConstants.INVENTORY_STATUS_COMPLETED.equalsIgnoreCase(inventoryStatus)) {
					logger.debug("Card number generation process for card range id : {} already completed",
							cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED,
							ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED);
				}
			}

		} else if (CCLPConstants.CARDNUMBER_INVENTORY_ACTION_RESUME.equalsIgnoreCase(action)) {
			if (CCLPConstants.INVENTORY_STATUS_PAUSED.equalsIgnoreCase(inventoryStatus)) {
				try {

					int qryStatus = cardInvtryDao.initiateCardNumberGeneration(cardRangeId);

					if (qryStatus != 1) {
						logger.debug("Card Number generation for Card Range id: {} failed to resume the procedure", cardRangeId);
						
						throw new ServiceException(ResponseMessages.FAILED_TO_START_CARDNUMGEN,
								ResponseMessages.FAILED_TO_START_CARDNUMGEN);
					}

				} catch (Exception e) {
					logger.error("Error occured while try to resume Card Number generation for Card Range id: {},"
							+ " Exp: {}", cardRangeId, e);
					throw new ServiceException(ResponseMessages.FAILED_TO_START_CARDNUMGEN,
							ResponseMessages.FAILED_TO_START_CARDNUMGEN);
				}

				logger.info("Inventory Card Generation process for Card Range Id: {} resumed successfully", cardRangeId);
				
			} else {
				if (CCLPConstants.INVENTORY_STATUS_STARTED.equalsIgnoreCase(inventoryStatus)) {
					logger.debug("Card number generation for card range id : {} already started", cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED,
							ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED);
				}

				if (CCLPConstants.INVENTORY_STATUS_COMPLETED.equalsIgnoreCase(inventoryStatus)) {
					logger.debug("Card number generation process for card range id : {} already completed",
							cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED,
							ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED);
				}
			}
		}

		else if (CCLPConstants.CARDNUMBER_INVENTORY_ACTION_REGENERATE.equalsIgnoreCase(action)) {

			if (CCLPConstants.INVENTORY_STATUS_FAILED.equalsIgnoreCase(inventoryStatus)) {
				try {

					int qryStatus = cardInvtryDao.initiateCardNumberGeneration(cardRangeId);

					if (qryStatus != 1) {
						logger.debug("Card Number generation for Card Range id: {} failed to regenerate the procedure", cardRangeId);
						
						throw new ServiceException(ResponseMessages.FAILED_TO_START_CARDNUMGEN,
								ResponseMessages.FAILED_TO_START_CARDNUMGEN);
					}

				} catch (Exception e) {
					logger.error("Error occured while try to call procedure to start regenerate process for Card Range id: {},"
							+ " Error: {}", cardRangeId, e.getMessage());
					throw new ServiceException(ResponseMessages.FAILED_TO_START_CARDNUMGEN,
							ResponseMessages.FAILED_TO_START_CARDNUMGEN);
				}

				logger.info("Inventory Card Generation process for Card Range Id: {} regenerated successfully", cardRangeId);
				
			} else {
				if (Util.isEmpty(inventoryStatus)) {
					logger.debug("Inventory Card Generation process for Card Range Id: {} not yet started", cardRangeId);
					
					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED,
							ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED);
					
				} else if (CCLPConstants.INVENTORY_STATUS_STARTED.equalsIgnoreCase(inventoryStatus)) {				
					logger.debug("Inventory Card Generation process for Card Range Id: {} already started", cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED,
							ResponseMessages.ERR_CARDNUMGEN_ALERYSTARTED);
				} else if (CCLPConstants.INVENTORY_STATUS_PAUSED.equalsIgnoreCase(inventoryStatus)) {
					logger.debug("Inventory Card Generation process for Card Range Id: {} already paused", cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERY_PAUSED,
							ResponseMessages.ERR_CARDNUMGEN_ALERY_PAUSED);
				} else if (CCLPConstants.INVENTORY_STATUS_COMPLETED.equalsIgnoreCase(inventoryStatus)) {
					logger.debug("Inventory Card Generation process for Card Range Id: {} already completed", cardRangeId);

					throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED,
							ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED);
				}
			}

		}

		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public void pauseCardGenerationProcess(Long cardRangeId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		String inventoryStatus = "";

		CardRangeInventory cardRangeInventory = cardInvtryDao.getCardRangeInventoryByCardRange(cardRangeId);
		if (Objects.isNull(cardRangeInventory)) {
			logger.debug("Card Number Generation for Card Range id : {} not yet started", cardRangeId);

			throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_NOT_YET_STARTED,
					ResponseMessages.ERR_CARDNUMGEN_NOT_YET_STARTED);
		}

		inventoryStatus = cardRangeInventory.getInventoryStatus();

		logger.info("Card Number Generation process current status for Card Range id : {} is {}", cardRangeId,
				inventoryStatus);

		if (CCLPConstants.INVENTORY_STATUS_STARTED.equalsIgnoreCase(inventoryStatus)) {

			try {
				int count = cardInvtryDao.pauseCardNumberProcessByCardRangeId(cardRangeId);

				if (count != 1) {
					logger.debug("Failed to pause the Card Number Generationfor process for Card Range id : {}",
							cardRangeId);

					throw new ServiceException(ResponseMessages.FAILED_CARDNUMGEN_PAUSE,
							ResponseMessages.FAILED_CARDNUMGEN_PAUSE);
				}

				logger.info("Inventory Card Generation process for Card Range Id: {} paused successfully", cardRangeId);
				
			} catch (Exception e) {
				logger.error("Error occured while try to pause the Card Number Generationfor process"
						+ " for Card Range id : {}, Error Details: {}", cardRangeId, e.getMessage());

				throw new ServiceException(ResponseMessages.FAILED_CARDNUMGEN_PAUSE,
						ResponseMessages.FAILED_CARDNUMGEN_PAUSE);
			}

		} else if (CCLPConstants.INVENTORY_STATUS_PAUSED.equalsIgnoreCase(inventoryStatus)) {
			logger.debug("Card Number Generationfor process for Card Range id : {}, already in paused status",
					cardRangeId);

			throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERYPAUSED,
					ResponseMessages.ERR_CARDNUMGEN_ALERYPAUSED);
		} else if (CCLPConstants.INVENTORY_STATUS_COMPLETED.equalsIgnoreCase(inventoryStatus)) {
			logger.debug("Card Number Generationfor process for Card Range id : {} was already completed", cardRangeId);
			throw new ServiceException(ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED,
					ResponseMessages.ERR_CARDNUMGEN_ALERY_COMPLETED);
		} else {
			logger.debug("Card Number Generationfor process for Card Range id : {}, already in '{}' status",
					inventoryStatus, cardRangeId);

			throw new ServiceException(ResponseMessages.FAILED_CARDNUMGEN_PAUSE,
					ResponseMessages.FAILED_CARDNUMGEN_PAUSE);
		}

		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public CardNumberInventoryDTO getCardInvtryDtlsById(Long cardRangeId) throws ServiceException {
		CardNumberInventoryDTO cardNumberInventoryDto = null;

		CardRange cardRange = cardInvtryDao.getCardRangeById(cardRangeId);

		if (!Objects.isNull(cardRange)) {
			cardNumberInventoryDto = new CardNumberInventoryDTO();
			cardNumberInventoryDto.setCardRangeId(cardRange.getCardRangeId());
			cardNumberInventoryDto
					.setIssuerName(cardRange.getIssuer() != null ? cardRange.getIssuer().getIssuerName() : null);
			cardNumberInventoryDto.setPrefix(cardRange.getPrefix());
			cardNumberInventoryDto.setStartCardNbr(cardRange.getStartCardNbr());
			cardNumberInventoryDto.setEndCardNbr(cardRange.getEndCardNbr());
		}

		logger.info("Retrived Card Range Inventory details Successfully");

		logger.info(CCLPConstants.EXIT);
		return cardNumberInventoryDto;
	}
}
