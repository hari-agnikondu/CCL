
package com.incomm.cclp.controller;

import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.domain.CardRange;
import com.incomm.cclp.dto.CardRangeDTO;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CardRangeService;
import com.incomm.cclp.service.IssuerService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.ValidationService;

/**
 * Card Range Controller provides all the REST operations for Card range.
 */
@RestController
@RequestMapping("/cardRanges")
@Api(value = "card range")
public class CardRangeController {

	// the CardRangeService service.
	@Autowired
	private CardRangeService cardRangeService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	private IssuerService issuerService;

	// the logger
	private static final Logger logger = LogManager.getLogger(CardRangeController.class);

	/**
	 * Creates an card range
	 * 
	 * @param The
	 *            CardRangeDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createCardRange(@RequestBody CardRangeDTO cardRangeDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String,String> valuesMap = new HashMap<>();
		logger.info("cardRangeDto:" + cardRangeDto);
		ValidationService.validateCardRange(cardRangeDto, true);
		cardRangeService.createCardRange(cardRangeDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("CARDRANGE_ADD_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		valuesMap.put(CCLPConstants.ISSUER_NAME, cardRangeDto.getIssuerName());
		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * To get all card ranges
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardRanges() {
		logger.info(CCLPConstants.ENTER);
		List<CardRangeDTO> cardRangeList = cardRangeService.getCardRanges();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cardRangeList,
				(CCLPConstants.CARDRANGE_RETRIEVE + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * To get all card ranges by using Issuer and prefix
	 * 
	 * @param The
	 *            issuerName and Prefix
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/{prefix}/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardRangesByIssuerAndPrefix(@RequestParam("issuerName") String issuerName,
			@PathVariable("prefix") String prefix) {
		logger.info(CCLPConstants.ENTER);
		List<CardRangeDTO> cardRangeList = cardRangeService.getCardRangeByIssuerNameAndPrefix(issuerName, prefix);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cardRangeList,
				(CCLPConstants.CARDRANGE_RETRIEVE + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update an card range
	 * 
	 * @param The
	 *            CardRangeDTO
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateCardRange(@RequestBody CardRangeDTO cardRangeDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();

		ValidationService.validateCardRange(cardRangeDto, false);

		cardRangeService.updateCardRange(cardRangeDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("CARDRANGE_UPDATED_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		valuesMap.put(CCLPConstants.ISSUER_NAME, cardRangeDto.getIssuerName());
		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Deletes an card Range.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteCardRange(@RequestBody CardRangeDTO cardRangeDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, String> valuesMap = new HashMap<>();
		CardRange returnDto = cardRangeService.getExistCardRanges(cardRangeDto);
		if (returnDto != null && returnDto.getCardRangeId() > 0) {

			if (CCLPConstants.USER_STATUS_APPROVED.equals(returnDto.getStatus())) {
				logger.info("Card range already approved");
				throw new ServiceException(ResponseMessages.CARDRANGE_ALREADY_APPROVED);
			}

			cardRangeService.deleteCardRange(cardRangeDto);

			responseDto = responseBuilder.buildSuccessResponse(null, ("CARDRANGE_DELETE_" + ResponseMessages.SUCCESS),
					ResponseMessages.SUCCESS);

			valuesMap.put(CCLPConstants.ISSUER_NAME, cardRangeDto.getIssuerName());
			String templateString = "";

			templateString = responseDto.getMessage();

			StrSubstitutor sub = new StrSubstitutor(valuesMap);
			String resolvedString = sub.replace(templateString);

			responseDto.setMessage(resolvedString);

		} else {
			logger.info("Card Range does not present");
			throw new ServiceException(ResponseMessages.CARDRANGE_DOESNOT_EXIT);
		}

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Change card range status
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/{cardRangeId}/status", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> changeCardRangeStatus(@RequestBody CardRangeDTO cardRangeDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, String> valuesMap = new HashMap<>();
		IssuerDTO issuerDto = null;

		CardRangeDTO cardrangeDto = new CardRangeDTO();
		cardrangeDto.setCardRangeId(cardRangeDto.getCardRangeId());
		CardRange returnDto = cardRangeService.getExistCardRanges(cardrangeDto);
		if (returnDto != null && returnDto.getCardRangeId() > 0) {
			if (returnDto.getInsUpdclpUser().getUserId() == cardRangeDto.getLastUpdUser()) {
				logger.info("Checker not authorized");
				throw new ServiceException(ResponseMessages.CHECKER_NOT_AUTHORIZED);
			}

			if (CCLPConstants.USER_STATUS_APPROVED.equals(returnDto.getStatus())) {
				logger.info("Card range already approved");
				throw new ServiceException(ResponseMessages.CARDRANGE_ALREADY_APPROVED);
			} else if ("REJECTED".equals(returnDto.getStatus())) {
				logger.info("Card range already rejected");
				throw new ServiceException(ResponseMessages.CARDRANGE_ALREADY_REJECTED);
			}
		} else {
			logger.info("Card Range does not present");
			throw new ServiceException(ResponseMessages.CARDRANGE_DOESNOT_EXIT);

		}

		String respMsg = cardRangeService.changeCardRangeStatus(cardRangeDto.getCardRangeId(), cardRangeDto.getStatus(),
				cardRangeDto.getCheckerDesc(), cardRangeDto.getLastUpdUser());

		if (CCLPConstants.USER_STATUS_APPROVED.equalsIgnoreCase(cardRangeDto.getStatus())) {
			logger.info("Card range approved");
			responseDto = responseBuilder.buildSuccessResponse(null, (respMsg),
					ResponseMessages.SUCCESS_CARDRANGE_APPROVED);
		} else {
			logger.info("Card range rejected");
			responseDto = responseBuilder.buildSuccessResponse(null, (respMsg),
					ResponseMessages.SUCCESS_CARDRANGE_REJECTED);
		}

		if (returnDto.getIssuer().getIssuerId() > 0) {
			issuerDto = issuerService.getIssuerById(returnDto.getIssuer().getIssuerId());
		}

		if (issuerDto != null) {
			valuesMap.put(CCLPConstants.ISSUER_NAME, issuerDto.getIssuerName());
		}

		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * To get all issuers
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/getIssuers", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getIssuers() {
		logger.info(CCLPConstants.ENTER);

		Map<Long, String> issuerMap = cardRangeService.getIssuers();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(issuerMap,
				(CCLPConstants.CARDRANGE_RETRIEVE + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * To get Card Range Details
	 * 
	 * @param The
	 *            cardRangeId
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/{cardRangeId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardRangeById(@PathVariable("cardRangeId") String cardRangeId)
			{
		logger.info(CCLPConstants.ENTER);

		CardRangeDTO cardRangeDTO = cardRangeService.getCardRangeById(cardRangeId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cardRangeDTO,
				(CCLPConstants.CARDRANGE_RETRIEVE + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * To get Card Range Details
	 * 
	 * @param The
	 *            Issuer ID
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/getCardRangesByIssuerId/{issuerId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardRangeDetailsByIssuerId(@PathVariable("issuerId") Long issuerId)
			{
		logger.info(CCLPConstants.ENTER);

		List<CardRangeDTO> cardRangesList = cardRangeService.getCardRangesByIssuerId(issuerId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cardRangesList,
				(CCLPConstants.CARDRANGE_RETRIEVE+ ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/* added by nawaz */

	@RequestMapping(value = "/getAllCardRanges", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllCardRanges() {
		logger.info(CCLPConstants.ENTER);

		@SuppressWarnings("rawtypes")
		List<Map> cardRangesList = cardRangeService.getAllCardRanges();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cardRangesList,
				ResponseMessages.CARDRANGE_RETRIEVE_000, ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/* added by nawaz */

	@RequestMapping(value = "/getCardRangeDataById/{cardRangeId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardRangeDataById(@PathVariable("cardRangeId") List<String> cardRangeId)
			{
		logger.info(CCLPConstants.ENTER);

		List<Long> cardsList = new ArrayList<>();

		for (int i = 0; i < cardRangeId.size(); i++) {
			String id = "";
			id = cardRangeId.get(i).replaceAll("\\[", "");
			id = id.replaceAll("\\]", "");

			cardsList.add(Long.parseLong(id));
		}

		List<String> cardRangesList = cardRangeService.getCardRangeDataById(cardsList);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cardRangesList,
				ResponseMessages.CARDRANGE_RETRIEVE_000, ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
