package com.incomm.cclp.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.dto.StockDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.StockService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/stocks")
@Api(value="stock")
public class StockController {
	
	@Autowired
	private ResponseBuilder responseBuilder;
	
	private static final Logger logger = LogManager.getLogger(StockController.class);
	
	@Autowired
	StockService stockService;
	
	/*
	 * Gets all the Merchants.
	 *
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/merchants", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllMerchants() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		Map<String,String> merchantMap = stockService.getAllMerchants();
		responseDto = responseBuilder.buildSuccessResponse(merchantMap,
					("MERCHANT_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/*
	 * Gets all the Locations and products linked to a merchant.
	 *
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{merchantId}/locationsAndProducts", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getLocationAndProductByMerchantId(@PathVariable("merchantId") String merchantId) {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		List<?> merchantMap =  stockService.getLocationAndProductByMerchantId(merchantId);
		responseDto = responseBuilder.buildSuccessResponse(merchantMap,
					("MERCHANT_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Creates a Stock.
	 * 
	 * @param StockDto
	 *            The StockDto.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createStock(@RequestBody StockDTO stockDto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		logger.info(stockDto.toString());
		ResponseDTO responseDto = null;
		
		if (stockDto.getLocationId() <= 0 || Util.isEmpty(stockDto.getMerchantId()) || stockDto.getProductId() <= 0 ){
		
			logger.error("Product Id or location id or merchant id can't be negative: merchantId {}, Product {}, locationId {}"
					,stockDto.getMerchantId(),stockDto.getProductId(),stockDto.getLocationId());
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_MERCH_LOCATION_PRODUCT_ID,ResponseMessages.DOESNOT_EXISTS);
		}else {
		stockService.createStock(stockDto);
		responseDto = responseBuilder.buildSuccessResponse(null,
				("STOCK_ADD_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		
		logger.debug("Stock created successfully");
		logger.info(responseDto.toString());
		logger.debug(CCLPConstants.EXIT);
		}
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Gets a Stock mapped to a particular merchant, location & product.
	 * 
	 * @param merchantId, locationId & productId of the Stock to be retrieved.

	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{merchantId}/{locationId}/{productId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getStockById(@PathVariable("merchantId") String merchantId,
			@PathVariable("locationId") Long locationId, @PathVariable("productId") Long productId) {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;
		
		if (Util.isEmpty(merchantId) || locationId <= 0 || productId <= 0 ){
			
			logger.error("Product Id or location id or merchant id can't be negative: merchantId {}, Product {}, locationId {}"
					,merchantId,productId,locationId);
			responseDto = responseBuilder.buildFailureResponse(
					ResponseMessages.ERR_MERCH_LOCATION_PRODUCT_ID,ResponseMessages.DOESNOT_EXISTS);
		}else {
		
			StockDTO stockDto = stockService.getStockByIds(merchantId,locationId,productId);
			if(stockDto == null) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_STOCK_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
				logger.error("Failed to fetch Stock details for merchant {}, location {},  product {}", merchantId,locationId,productId);
			}
			else {
				responseDto = responseBuilder.buildSuccessResponse(stockDto,("STOCK_RETRIEVE_"+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
				 
				logger.debug("Stock for merchant {} at location {} has retrieved successfully {}",merchantId,locationId,stockDto.toString());
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Updates a stock.
	 * 
	 * @param stockDto The StockDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	
	@RequestMapping (method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateStock(@RequestBody StockDTO stockDto) 
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		
		logger.debug(stockDto.toString());	
		stockService.updateStock(stockDto);
		logger.debug("Updating stock details stockDto {}",stockDto.toString());
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, 
				("STOCK_UPDATE_"+ResponseMessages.SUCCESS),ResponseMessages.SUCCESS);
		
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Getting the stocks mapped to a merchantId and locationId
	 * 
	 * @param The merchantId and locationId as input parameters
	 *      
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/searchStocks/{merchantId}/{locationId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getStockByMerchantIdAndLocationId(@PathVariable("merchantId") String merchantId,
			@PathVariable("locationId") Long locationId) {
		logger.debug(CCLPConstants.ENTER);
		List<StockDTO> stockList = stockService.getStockByMerchantIdAndLocationId(merchantId, locationId);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(stockList,
				("STOCK_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
