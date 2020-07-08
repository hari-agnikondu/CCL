package com.incomm.cclp.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.MerchantRedemptionDTO;
import com.incomm.cclp.dto.RedemptionDelayDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MerchantRedemptionService;
import com.incomm.cclp.service.RedemptionDelayService;
import com.incomm.cclp.util.ResponseBuilder;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/redemptiondelay")
@Api(value = "redemption delay")
public class RedemptionDelayConfigController {

	private static final Logger logger = LogManager.getLogger(RedemptionDelayConfigController.class);
	
	@Autowired
	private RedemptionDelayService redemptionDelayService;
	
	@Autowired
	private MerchantRedemptionService merchantRedemptionService;
	
	@Autowired 
	private ResponseBuilder responseBuilder;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createRedemptionDelay(@RequestBody RedemptionDelayDTO redemptionDelayDTO) throws ServiceException{
		
		logger.info(CCLPConstants.ENTER);
		logger.info("Clp Redemption Delay DTO: " + redemptionDelayDTO);
		
		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";
		
		redemptionDelayService.createRedemptionDelay(redemptionDelayDTO);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ("REDEMPTION_DELAY_ADD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		
		valuesMap.put("MerchantName", redemptionDelayDTO.getMerchantName());
		valuesMap.put("ProductName", redemptionDelayDTO.getProductName());
		
		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);
		
		logger.debug(" Config Response Code : " + responseDto.getCode()
		+ " Error Message : " + responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/merchantRedemptioDelay",method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createMerchant(@RequestBody MerchantRedemptionDTO merchantRedemptionDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("Clp User DTO: " + merchantRedemptionDto);
		
		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";
		ResponseDTO responseDto;

		merchantRedemptionService.createMerchant(merchantRedemptionDto);

		 responseDto = responseBuilder.buildSuccessResponse(null, ("MER_ADD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);

		valuesMap.put("MerchantName", merchantRedemptionDto.getMerchantName());
		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);

		logger.debug(" Config Response Code : " + responseDto.getCode()
		+ " Error Message : " + responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	

	
	@RequestMapping(value = "/getAllMerchants",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllMerchants() {
		logger.info(CCLPConstants.ENTER);
		List<MerchantRedemptionDTO> merchantDtos = merchantRedemptionService.getAllMerchants();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(merchantDtos,
				("MER_RETRIVEALL_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 

		responseDto.setMessage(responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * overlap delay
	 */
	@RequestMapping(value = "overlapDelays/{previousValue}/{currentValue}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getOverlapDelayDetails(@PathVariable("previousValue") String previousValue,
			@PathVariable("currentValue") String currentValue) {
		logger.info(CCLPConstants.ENTER);
		String resp="";
		
		resp=redemptionDelayService.getOverlapRedemptionDetails(previousValue,currentValue);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(resp, ("REDEMPTION_DELAY_ADD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAllRedemptionProductMerchants/{productId}/{merchantId}",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllRedemptionProductMerchants(@PathVariable("productId") Long productId,
			@PathVariable("merchantId") String merchantId) {
		logger.info(CCLPConstants.ENTER);
		List<RedemptionDelayDTO> redemptiondtosDtos = redemptionDelayService.getRedeemMerchantProductIddetails(productId,merchantId);
		
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(redemptiondtosDtos,
				("MER_RETRIVEALL_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); 

		logger.info(responseDto.toString());
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
