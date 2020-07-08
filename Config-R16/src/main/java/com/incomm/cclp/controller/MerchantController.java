/**
 * 
 */
package com.incomm.cclp.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.MerchantDTO;
import com.incomm.cclp.dto.MerchantProductDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.MerchantService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/merchants")
@Api(value = "merchant")
public class MerchantController {

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private ResponseBuilder responseBuilder;

	private static final Logger logger = LogManager.getLogger(MerchantController.class);

	/**
	 * Creates an merchant.
	 * 
	 * @param merchantDto The MerchantDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createMerchant(@RequestBody MerchantDTO merchantDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("Clp User DTO: " + merchantDto);
		
		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";
		
		ValidationService.validateMerchant(merchantDto, true);

		merchantService.createMerchant(merchantDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ("MER_ADD_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);

		valuesMap.put(CCLPConstants.MERCHANT_NAME, merchantDto.getMerchantName());
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

	/**
	 * Gets all Active Issuers.
	 *
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllMerchants() {
		logger.info(CCLPConstants.ENTER);
		List<MerchantDTO> issuerDtos = merchantService.getAllMerchants();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(issuerDtos,
				("MER_RETRIVEALL_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS); // KALAIVANI P ADDED

		responseDto.setMessage(responseDto.getMessage());
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Updates an merchant.
	 * 
	 * @param merchantDto
	 *            The MerchantDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateMerchant(@RequestBody MerchantDTO merchantDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		logger.info(merchantDto.toString());

		ResponseDTO responseDto = null;
		ValidationService.validateMerchant(merchantDto, false);

		merchantService.updateMerchant(merchantDto);

		responseDto = responseBuilder.buildSuccessResponse(merchantDto, ("MER_UPDATE_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		valuesMap.put(CCLPConstants.MERCHANT_NAME, merchantDto.getMerchantName());
		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.info(responseDto.toString());

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Deletes an merchant.
	 * 
	 * @param merchantId
	 *            The id of the Merchant to be deleted.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/{merchantId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteMerchant(@PathVariable("merchantId") Long merchantId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("Merchant Id to delete :" + merchantId);
		
		MerchantDTO merchantDto = null;
		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";	
		String resolvedString = "";		
		ResponseDTO responseDto = null;
		
		if (merchantId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_MERCHANT_ID,
					ResponseMessages.ERR_MERCHANT_ID);
		}
		else {
			merchantDto = merchantService.getMerchantById(merchantId);
			
			if (Objects.isNull(merchantDto)) {
				logger.info("Merchant record not exists with merchantId : {}", merchantId);
				
				responseDto = responseBuilder.buildFailureResponse(("MER_" + ResponseMessages.DOESNOT_EXISTS),
						ResponseMessages.DOESNOT_EXISTS);
			}
			else {
				merchantService.deleteMerchantById(merchantId);
				
				logger.info("Merchant '{}' record deleted successfully", merchantDto.getMerchantName());
				
				responseDto = responseBuilder.buildSuccessResponse(null, ("MER_DELETE_" + ResponseMessages.SUCCESS),
						ResponseMessages.SUCCESS);
				valuesMap.put(CCLPConstants.MERCHANT_NAME, merchantDto.getMerchantName().trim());
				templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			}
		}
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets an merchant by Merchant Id.
	 * 
	 * @param merchantId
	 *            The id of the Merchant to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{merchantId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMerchantById(@PathVariable("merchantId") Long merchantId) {
		logger.info(CCLPConstants.ENTER);
		logger.info("MerchantId  data: " + merchantId);
		ResponseDTO responseDto = null;
		if (merchantId <= 0) {

			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ISSUER_ID,
					ResponseMessages.ERR_ISSUER_ID);
		} else {
			MerchantDTO merchantDto = merchantService.getMerchantById(merchantId);
			if (merchantDto == null) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.DOESNOT_EXISTS,
						ResponseMessages.DOESNOT_EXISTS);
				logger.info("Failed to retrieve data for Merchant id: " + merchantId);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(merchantDto, ResponseMessages.SUCCESS,
						ResponseMessages.SUCCESS);
				logger.debug("Record for Merchant: {} has been retrieved successfully {}", merchantId,
						merchantDto.toString());
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets an merchant by name. The name can be a complete or partial Merchant
	 * name.
	 * 
	 * @param merchantName The name of the Merchant to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMerchantsByName(@RequestParam("merchantName") String merchantName) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		logger.info("Merchant Name to get data: " + merchantName);
		if (Util.isEmpty(merchantName)) {
			logger.info("Invalid merchant name: {}",merchantName);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ISSUER_NAME,
					ResponseMessages.ERR_ISSUER_NAME);
		} else {
			List<MerchantDTO> merchantDtos = merchantService.getMerchantsByName(merchantName);

			responseDto = responseBuilder.buildSuccessResponse(merchantDtos, ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * searching merchant names linked with product
	 * @throws ServiceException 
	 */

	
	@RequestMapping(value="/searchMerchantProduct",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMerchantProducts(@RequestParam("merchantName") String merchantName
			,@RequestParam("productName") String productName) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (Util.isEmpty(merchantName)) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_MERCHANT_NAME,
					ResponseMessages.DOESNOT_EXISTS);
		} else if (Util.isEmpty(productName)) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_NAME,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			List<MerchantProductDTO> merchantProductDtos = merchantService.getMerchantProducts(merchantName,
					productName);
			responseDto = responseBuilder.buildSuccessResponse(merchantProductDtos, ("RETRIVE_MERPRO_"+ResponseMessages.SUCCESS),
					ResponseMessages.SUCCESS);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Assignes a product to merchant.
	 * 
	 * @param merchantProductDto The MerchantProductDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */	
	@RequestMapping(value = "/assignProductToMerchant", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> assignProductToMerchant(@RequestBody MerchantProductDTO merchantProductDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("merchantProduct DTO: " + merchantProductDto);

		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";

		ValidationService.validateMerchantProduct(merchantProductDto, false);

		merchantService.assignProductToMerchant(merchantProductDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("ASSIGN_PRD_TO_MER_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		valuesMap.put(CCLPConstants.MERCHANT_NAME, merchantProductDto.getMerchantName());
		valuesMap.put("ProductName", merchantProductDto.getProductName());
		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);
	
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/assignProductToMerchant", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> removeMerchantProductMapping(@RequestBody MerchantProductDTO merchantProductDto)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
	
		Map<String, String> valuesMap = new HashMap<>();
		String templateString = "";
		String resolvedString = "";
		ResponseDTO responseDto = null;
		String merchantName="";
		String productName="";

		ValidationService.validateMerchantProduct(merchantProductDto, true);
		
		merchantName=merchantProductDto.getMerchantName();
		productName=merchantProductDto.getProductName();
		
		merchantService.removeMerchantProductMapping(merchantProductDto);

		logger.info("Mapping of Merchant '{}' to Product '{}' DELETED SUCCESSFULLY", merchantProductDto.getMerchantName(), merchantProductDto.getProductName());

		responseDto = responseBuilder.buildSuccessResponse(null, ("ASSIGN_PRD_TO_MER_DELETE_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		valuesMap.put(CCLPConstants.MERCHANT_NAME, merchantName);
		valuesMap.put("ProductName", productName);
		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	/**
	 * Getting merchant products by merchantId and productId
	 * @throws ServiceException 
	 * 
	 * */
	@RequestMapping(value="/{merchantId}/{productId}",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMerchantProductById(@PathVariable("merchantId") Long merchantId,@PathVariable("productId") Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		
		ResponseDTO responseDto =null;
		if (merchantId<=0) {
			logger.info("invalid merchant ID {}",merchantId);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_MERCHANT_PRODUCT_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else if(productId<=0){
			logger.info("Invalid product ID: {}",productId);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_MERCHANT_PRODUCT_NULL,ResponseMessages.DOESNOT_EXISTS);
		}
		else{
			List<MerchantProductDTO> merchantProductDTOList=merchantService.getMerchantProductsById(merchantId,productId);
			 responseDto = responseBuilder.buildSuccessResponse(merchantProductDTOList,("RETRIVE_MERPRO_"+ResponseMessages.SUCCESS),
					ResponseMessages.SUCCESS);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
