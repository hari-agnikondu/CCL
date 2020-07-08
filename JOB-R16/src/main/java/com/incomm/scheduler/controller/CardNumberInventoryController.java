package com.incomm.scheduler.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.ResponseMessages;
import com.incomm.scheduler.dto.CardNumberInventoryDTO;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.service.CardNumberInventoryService;
import com.incomm.scheduler.utils.ResponseBuilder;

@RestController
@RequestMapping("/inventories")
public class CardNumberInventoryController {

	@Autowired
	private ResponseBuilder responseBuilder;

	private static final Logger logger = LogManager.getLogger(CardNumberInventoryController.class);

	@Autowired
	CardNumberInventoryService cardInvtryService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllCardInventoryDtls() {
		logger.info(CCLPConstants.ENTER);

		List<CardNumberInventoryDTO> cardInvtryListDtos = cardInvtryService.getCardInvtryDtls();
		ResponseDTO responseDTO = responseBuilder.buildSuccessResponse(cardInvtryListDtos,
				ResponseMessages.ALL_SUCCESS_CARDINVENTORYDTLS_RETRIEVE, ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/{cardRangeId}/{action}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> generateCardNumbers(@PathVariable("cardRangeId") Long cardRangeId,
			@PathVariable("action") String action) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		logger.info("Card Range Id: " + cardRangeId + " and Action: " + action);

		ResponseDTO responseDto = null;

		if (Objects.isNull(cardRangeId) || cardRangeId <= 0) {
			logger.debug("Card Range Id is empty/ negative");

			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_CARDRANGE_ID,
					ResponseMessages.ERR_CARDRANGE_ID);
		} else {

			CardNumberInventoryDTO cardInvtryListDtos = cardInvtryService.getCardInvtryDtlsById(cardRangeId);

			if (Objects.isNull(cardInvtryListDtos)) {
				logger.debug("Card Range record with id {} does not exist", cardRangeId);

				throw new ServiceException(ResponseMessages.CARDRANGE_DOESNOT_EXIT, ResponseMessages.DOESNOT_EXISTS);
			}

			else {
				logger.debug("Card Range record with id {} exist", cardRangeId);
			
				cardInvtryService.initiateCardNumberGeneration(cardRangeId, action);

				if (CCLPConstants.CARDNUMBER_INVENTORY_ACTION_GENERATE.equalsIgnoreCase(action)) {
					logger.info("Card Number Inventory Generation Successfully started for card range id: {} ",
							cardRangeId);

					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.SUCCESS_INITIATE_CARDGEN,
							ResponseMessages.SUCCESS);
				} else if (CCLPConstants.CARDNUMBER_INVENTORY_ACTION_REGENERATE.equalsIgnoreCase(action)) {
					logger.info("Card Number Inventory Re Generation Successfully started for card range id: {} ",
							cardRangeId);

					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.SUCCESS_REGEN_CARDGEN,
							ResponseMessages.SUCCESS);
				} else if(CCLPConstants.CARDNUMBER_INVENTORY_ACTION_RESUME.equalsIgnoreCase(action)) {
					logger.info("Card Number Inventory Generation Successfully started for card range id: {} ",
							cardRangeId);

					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.SUCCESS_RESUME_CARDGEN,
							ResponseMessages.SUCCESS);
				} 

				valuesMap.put("issuerName", cardInvtryListDtos.getIssuerName());
				valuesMap.put("cardRange", cardInvtryListDtos.getPrefix() + cardInvtryListDtos.getStartCardNbr() + "-"
						+ cardInvtryListDtos.getPrefix() + cardInvtryListDtos.getEndCardNbr());

				String templateString = "";
				if(responseDto != null) {
				templateString = responseDto.getMessage();
				

				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);

				responseDto.setMessage(resolvedString);
				}
			}
		}

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/{cardRangeId}/pause", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> pauseCardNumberGenerationProcess(@PathVariable("cardRangeId") Long cardRangeId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("Card Range Id: " + cardRangeId);

		ResponseDTO responseDto = null;
		Map<String, String> valuesMap = new HashMap<>();

		if (Objects.isNull(cardRangeId) || cardRangeId <= 0) {
			logger.debug("Card Range Id is empty/ negative");

			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_CARDRANGE_ID,
					ResponseMessages.ERR_CARDRANGE_ID);
		} else {
			CardNumberInventoryDTO cardInvtryListDtos = cardInvtryService.getCardInvtryDtlsById(cardRangeId);

			if (Objects.isNull(cardInvtryListDtos)) {
				logger.debug("Card Range record with id {} does not exist", cardRangeId);

				throw new ServiceException(ResponseMessages.CARDRANGE_DOESNOT_EXIT, ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.debug("Card Range record with id {} does exist", cardRangeId);

				cardInvtryService.pauseCardGenerationProcess(cardRangeId);

				logger.info("Inventory Card Generation process for '{}' for Issuer '{}' paused successfully",
						cardInvtryListDtos.getPrefix() + cardInvtryListDtos.getStartCardNbr() + "-"
								+ cardInvtryListDtos.getPrefix() + cardInvtryListDtos.getEndCardNbr(),
						cardInvtryListDtos.getIssuerName());

				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.SUCCESS_PAUSE_CARDGEN,
						ResponseMessages.SUCCESS);
				valuesMap.put("issuerName", cardInvtryListDtos.getIssuerName());
				valuesMap.put("cardRange", cardInvtryListDtos.getPrefix() + cardInvtryListDtos.getStartCardNbr() + "-"
						+ cardInvtryListDtos.getPrefix() + cardInvtryListDtos.getEndCardNbr());

				String templateString = "";

				templateString = responseDto.getMessage();

				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);

				responseDto.setMessage(resolvedString);
			}
		}

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
