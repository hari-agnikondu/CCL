
package com.incomm.cclp.controller;

import io.swagger.annotations.Api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.CardStatusDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.service.CardStatusService;
import com.incomm.cclp.util.ResponseBuilder;
/**
 * Card Status Controller provides all the REST operations for Card Status. 
 */
@RestController
@RequestMapping("/cardStatus")
@Api(value="Card Status")
public class CardStatusController {

	// the CardStatusService service.
	@Autowired
	private CardStatusService cardStatusService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;


	// the logger
	private static final Logger logger = LogManager.getLogger(CardStatusController.class);

	/**
	 * Getting Active Card Status details
	 * 
	 * */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardStatus(){
		logger.info("ENTER");
		List<CardStatusDTO> cardStatusList=cardStatusService.getCardStatus();
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cardStatusList,ResponseMessages.SUCCESS_CARD_STATUS_RETRIEVE,"");
		
		logger.info("Exit");
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	

}
