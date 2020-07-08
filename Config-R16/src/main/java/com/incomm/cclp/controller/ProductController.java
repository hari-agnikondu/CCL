package com.incomm.cclp.controller;

import java.io.IOException;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.domain.Alert;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;
import com.incomm.cclp.util.ValidationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/products")
@Api(value = "product")

public class ProductController {
	@Autowired
	private ProductService productService;

	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	private static final Logger logger = LogManager.getLogger(ProductController.class);

	/**
	 * Creates an product.
	 * 
	 * @param productDto
	 *            The productDto.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createProduct(@RequestBody ProductDTO productDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ValidationService.validateProduct(productDto, true);

		logger.debug(productDto.toString());
		productService.createProduct(productDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_PRODUCT_CREATE,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	/**
	 * Updates an product.
	 * 
	 * @param productDto
	 *            The ProductDTO.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateProduct(@RequestBody ProductDTO productDto) throws ServiceException {

		logger.info(CCLPConstants.ENTER);

		Map<String, String> valuesMap = new HashMap<>();
		ResponseDTO responseDto = null;
		String templateString = "";

		ValidationService.validateProduct(productDto, false);
		logger.debug("Updating product details for {} as {}", productDto.getPartnerName(), productDto.toString());
		
		productService.updateProduct(productDto);

		responseDto = responseBuilder.buildSuccessResponse(null, ("PRODUCT_UPDATE_" + ResponseMessages.SUCCESS),
				ResponseMessages.SUCCESS);
		valuesMap.put("ProductName", productDto.getProductName());

		templateString = responseDto.getMessage();
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);
		responseDto.setMessage(resolvedString);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/*
	 * Get all products
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllProducts() {
		logger.info(CCLPConstants.ENTER);

		List<ProductDTO> productDtos = productService.getAllProducts();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(productDtos,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/*
	 * Get Product By ID
	 */
	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductById(@PathVariable("productId") Long productId) throws IOException {
		logger.info(CCLPConstants.ENTER);

		ProductDTO productDtos = productService.getProductById(productId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(productDtos,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets an product by name. The name can be a complete or partial product name.
	 * 
	 * @param productrName
	 *            The name of the product to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductsByName(@RequestParam("productName") String productName) {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		if (Util.isEmpty(productName)) {
			logger.error("Product name is empty: {} ",productName);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_NAME,
					ResponseMessages.FAILURE);
		} else {
			List<ProductDTO> productDtos = productService.getProductsByName(productName);
			responseDto = responseBuilder.buildSuccessResponse(productDtos, ResponseMessages.SUCCESS_PRODUCT_RETRIEVE,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * product by name for copy
	 * 
	 * @return
	 */

	@RequestMapping(value = "/searchProductCopy", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductsByNameForCopy(@RequestParam("productName") String productName) {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		if (Util.isEmpty(productName)) {
			logger.error("Product name is empty: {}",productName);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_NAME,
					ResponseMessages.FAILURE);
		} else {
			List<ProductDTO> productDtos = productService.getProductsByNameForCopy(productName);
			responseDto = responseBuilder.buildSuccessResponse(productDtos, ResponseMessages.SUCCESS_PRODUCT_RETRIEVE,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/ccfs", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getccfDetails() {
		logger.info(CCLPConstants.ENTER);

		@SuppressWarnings("rawtypes")
		Map<String, List> data = productService.getCCFlist();

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(data, ResponseMessages.SUCCESS_PRODUCT_RETRIEVE,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get Product general details by ProductID
	 */
	@RequestMapping(value = "/{productId}/generalConfigs", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductGeneral(@PathVariable("productId") Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			responseDto = responseBuilder.buildSuccessResponse(productService.getProductGeneral(productId),
					ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get Product general details by ProductID
	 */
	@RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateProductGeneral(@PathVariable("productId") Long productId,
			@RequestBody Map<String, Object> productMap) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			if (CollectionUtils.isEmpty(productMap)) {
				logger.error("Attributes map is empty for Product Id {}", productId);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY,
						ResponseMessages.DOESNOT_EXISTS);
			} else {
				productService.updateProductRuleSet(productMap, productId);
				productService.updateProductGeneral(productMap, productId);
				responseDto = responseBuilder.buildSuccessResponse(null,
						CCLPConstants.PRODUCT_ACTION_UPDATE + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				Map<String, String> valuesMap = new HashMap<>();
				valuesMap.put(CCLPConstants.ACTION, "GENERAL");

				String templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting parent product details
	 * 
	 */
	@RequestMapping(value = "/getParentProducts", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getParentProduct() {
		logger.info(CCLPConstants.ENTER);

		Map<Long, String> parentProductMap = productService.getParentProducts();
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(parentProductMap,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update the Limit Attributes After fetching the existing Product Limits from
	 * actual Product and parentProducts
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{productId}/purse/{purseId}/limits", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateLimitAttributes(@RequestBody Map<String, Object> inputLimitAttributes,
			@PathVariable("productId") Long productId,@PathVariable("purseId") Long purseId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			try {
				logger.debug("Updating limit attributes for productId: {} Limit Attributes -> {}", productId,
						inputLimitAttributes);
				int count = productService.updatePurseFeeLimitAttributes(inputLimitAttributes, productId,purseId, "Limits");
				if (count > 0) {
					logger.info("Limit attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,
							ResponseMessages.SUCCESS_PRODUCT_LIMITS_UPDATE, ResponseMessages.SUCCESS);
				} else {
					logger.error("Failed to update limits attributes");
					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRODUCT_LIMITS_UPDATE,
							ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating limit attributes {}", e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRODUCT_LIMITS_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	/**
	 * Getting the Product Limits from actual Product and parent Products Using
	 * ProductId
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{productId}/parentProduct/{parentProductId}/purse/{purseId}/limits", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getLimitAttributes(@PathVariable("productId") Long productId,@PathVariable("parentProductId") Long parentProductId,@PathVariable("purseId") Long purseId)
			throws IOException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.debug("Get limit attributes for product Id: {} purse Id: {}", productId, purseId);
			Map<String, Object> limitAttributes = productService.getProductFeeLimitsById(productId,parentProductId,purseId, "Limits");

			if (CollectionUtils.isEmpty(limitAttributes)) {
				logger.error("Failed to get limit attributes, limitAttribute is empty");
				responseDto = responseBuilder.buildSuccessResponse(limitAttributes,
						ResponseMessages.EMPTY_PRODUCT_LIMITS_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Limit attributes retrieved successfully");
				responseDto = responseBuilder.buildSuccessResponse(limitAttributes,
						ResponseMessages.SUCCESS_PRODUCT_LIMITS_RETRIEVE, ResponseMessages.SUCCESS);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting all the Existing parent Products List
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/parentProductList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllParentProducts() {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		Map<Object, Object> parentProductList = productService.getAllParentProducts();

		if (CollectionUtils.isEmpty(parentProductList)) {
			logger.debug("There is no parent product retrieved from table");
			responseDto = responseBuilder.buildSuccessResponse(parentProductList,
					ResponseMessages.EMPTY_PARENT_PRODUCTS_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);

		} else {
			logger.debug("Parent products retrieved successfully product list: {}", parentProductList);
			responseDto = responseBuilder.buildSuccessResponse(parentProductList,
					ResponseMessages.SUCCESS_PARENT_PRODUCTS_RETRIEVE, ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting all the Existing Retail Products List
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/retailProductList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllRetailProducts() {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		Map<Object, Object> retailProductList = productService.getAllRetailProducts();

		if (CollectionUtils.isEmpty(retailProductList)) {
			logger.debug("There is no parent product retrieved from table");
			responseDto = responseBuilder.buildSuccessResponse(retailProductList,
					ResponseMessages.EMPTY_PARENT_PRODUCTS_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);

		} else {
			logger.debug("Parent products retrieved successfully product list: {}", retailProductList);
			responseDto = responseBuilder.buildSuccessResponse(retailProductList,
					ResponseMessages.SUCCESS_PARENT_PRODUCTS_RETRIEVE, ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting all the Existing Transactions List
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/transactionsList", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllTransactionsList() {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> transactionsList = productService.getTransactionList();

		if (transactionsList != null && !transactionsList.isEmpty()) {

			//logger.debug("Transaction list fetched successfully, List -> {}", transactionsList);
			responseDto = responseBuilder.buildSuccessResponse(transactionsList,
					ResponseMessages.SUCCESS_TRANSACTION_LIST_RETRIEVE, ResponseMessages.SUCCESS);

		} else {

			logger.error("Transaction list is empty");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.EMPTY_TRANSACTION_LIST,
					ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting all the Existing Transactions List using channel code
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/transactionsList/{channelName}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllTransactionsListByChannelCode(
			@PathVariable("channelName") String channelName) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> transactionsList = productService.getTransactionListByChannelName(channelName);

		if (transactionsList != null && !transactionsList.isEmpty()) {

			//logger.debug("Transaction list fetched successfully, List -> {}", transactionsList);
			responseDto = responseBuilder.buildSuccessResponse(transactionsList,
					ResponseMessages.SUCCESS_TRANSACTION_LIST_RETRIEVE, ResponseMessages.SUCCESS);

		} else {

			logger.error("Transaction list is empty");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.EMPTY_TRANSACTION_LIST,
					ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(responseDto.toString());
		
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting all the Existing Transactions List using channel code and transaction
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/transactionsList/{channelName}/{transactionShortName}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllTransactionsListByChannelCodeTxnName(
			@PathVariable("channelName") String channelName,
			@PathVariable("transactionShortName") String transactionShortName) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> transactionsList = productService.getTransactionListByChannelNameTransactionName(channelName,
				transactionShortName);

		if (transactionsList != null && !transactionsList.isEmpty()) {

			//logger.debug("Transaction list fetched successfully, List -> {}", transactionsList);
			responseDto = responseBuilder.buildSuccessResponse(transactionsList,
					ResponseMessages.SUCCESS_TRANSACTION_LIST_RETRIEVE, ResponseMessages.SUCCESS);

		} else {

			logger.error("Transaction list is empty");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.EMPTY_TRANSACTION_LIST,
					ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get Product Cvv details by ProductID
	 */
	@RequestMapping(value = "/{productId}/cvvs", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductCVV(@PathVariable("productId") Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, Object> cvvAttributes = productService.getProductCvv(productId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(cvvAttributes,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get Product cvv details by ProductID
	 */
	@RequestMapping(value = "/{productId}/cvvs", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateProductCvv(@PathVariable("productId") Long productId,
			@RequestBody Map<String, Object> productCvvMap) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			if (CollectionUtils.isEmpty(productCvvMap)) {
				logger.error("Attributes map is empty {}", productId);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY,
						ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Updating Product CVV..");
				productService.updateProductCvv(productCvvMap, productId);
				responseDto = responseBuilder.buildSuccessResponse(null,
						CCLPConstants.PRODUCT_ACTION_UPDATE + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				Map<String, String> valuesMap = new HashMap<>();
				valuesMap.put(CCLPConstants.ACTION, "CVV");

				String templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			}
		}
		logger.info("responseDto: {}",responseDto);
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get Card Status Details for the Transactions by ProductID
	 */
	@RequestMapping(value = "/{productId}/txnCardStatusConfigs", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductCardStatusAttributes(@PathVariable("productId") Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Successfully retrieved Product card status attribues");
			responseDto = responseBuilder.buildSuccessResponse(productService.getCardStatusAttributes(productId),
					ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update Card Status Details for the Transactions by ProductID
	 */
	@RequestMapping(value = "/{productId}/txnCardStatusConfigs", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateCardStatusAttributes(@PathVariable("productId") Long productId,
			@RequestBody Map<String, Object> productCardStatMap) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			if (CollectionUtils.isEmpty(productCardStatMap)) {
				logger.error("Attributes are empty {}", productId);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY,
						ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Updating card status attributes..");
				productService.updateCardStatusAttributes(productCardStatMap, productId);
				responseDto = responseBuilder.buildSuccessResponse(null,
						CCLPConstants.PRODUCT_ACTION_UPDATE + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				Map<String, String> valuesMap = new HashMap<>();
				valuesMap.put(CCLPConstants.ACTION, "CARD STATUS");

				String templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update the Fees Attributes After fetching the existing Product fees using
	 * productId
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{productId}/purse/{purseId}/txnFees", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateFeeAttributes(@RequestBody Map<String, Object> inputFeeAttributes,
			@PathVariable("productId") Long productId,@PathVariable("purseId") Long purseId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			try {
				logger.debug("Updating fee attributes for productId: {} fee Attributes -> {}", productId,
						inputFeeAttributes);
				int count = productService.updatePurseFeeLimitAttributes(inputFeeAttributes, productId,purseId, "Transaction Fees");
				if (count > 0) {
					logger.info("Fee attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,
							ResponseMessages.SUCCESS_PRODUCT_TXN_FEES_UPDATE, ResponseMessages.SUCCESS);
				} else {
					logger.error("Failed to update fees attributes");
					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRODUCT_TXN_FEES_UPDATE,
							ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating fee attributes {}", e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRODUCT_TXN_FEES_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	/**
	 * Getting the Product transaction fee from Product Using ProductId
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{productId}/parentProduct/{parentProductId}/purse/{purseId}/txnFees", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getTxnFeesAttributes(@PathVariable("productId") Long productId,@PathVariable("parentProductId") Long parentProductId,@PathVariable("purseId") Long purseId)
			throws IOException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, Object> feeAttributes = null;

		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.debug("Get fee attributes for product Id: {},purse Id: {}", productId, purseId);
			feeAttributes = productService.getProductFeeLimitsById(productId,parentProductId,purseId, "Transaction Fees");

			if (CollectionUtils.isEmpty(feeAttributes)) {
				logger.error("Failed to get fee attributes, feeAttribute is empty");
				responseDto = responseBuilder.buildSuccessResponse(feeAttributes,
						ResponseMessages.EMPTY_PRODUCT_TXN_FEES_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.debug("Fee attributes retrieved successfully feeAttributes -> {}", feeAttributes);
				responseDto = responseBuilder.buildSuccessResponse(feeAttributes,
						ResponseMessages.SUCCESS_PRODUCT_TXN_FEES_RETRIEVE, ResponseMessages.SUCCESS);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update the Monthly Fee Attributes After fetching the existing Product fees
	 * using productId
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{productId}/purse/{purseId}/monthlyFeeCap", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateMonthlyFeeCapAttributes(
			@RequestBody Map<String, Object> inputFeeAttributes, @PathVariable("productId") Long productId,@PathVariable("purseId") Long purseId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			try {
				logger.debug("Updating monthly fee cap attributes for productId: {} Monthly fee Attributes -> {}",
						productId, inputFeeAttributes);
				int count = productService.updatePurseFeeLimitAttributes(inputFeeAttributes, productId,purseId, "Monthly Fee Cap");
				if (count > 0) {
					logger.info("Monthly Fee cap attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,
							ResponseMessages.SUCCESS_PRODUCT_MONTHLYFEECAP_UPDATE, ResponseMessages.SUCCESS);
				} else {
					logger.info("Failed to update monthly fee cap attributes");
					responseDto = responseBuilder.buildFailureResponse(
							ResponseMessages.FAILED_PRODUCT_MONTHLYFEECAP_UPDATE,
							ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating monthly fee cap attributes {}", e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_PRODUCT_MONTHLYFEECAP_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	/**
	 * Getting the Product Monthly fee cap from actual Product and parent Products
	 * Using ProductId
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{productId}/parentProduct/{parentProductId}/purse/{purseId}/monthlyFeeCap", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMonthlyFeeCapAttributes(@PathVariable("productId") Long productId,@PathVariable("parentProductId") Long parentProductId,@PathVariable("purseId") Long purseId)
			throws IOException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, Object> monthlyFeeCapAttributes = null;

		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Get fee attributes for product Id: {}, purseId: {}", purseId);
			monthlyFeeCapAttributes = productService.getProductFeeLimitsById(productId,parentProductId,purseId, "Monthly Fee Cap");

			if (CollectionUtils.isEmpty(monthlyFeeCapAttributes)) {
				logger.error("Failed to get fee attributes, feeAttribute is empty");
				responseDto = responseBuilder.buildSuccessResponse(monthlyFeeCapAttributes,
						ResponseMessages.EMPTY_PRODUCT_MONTHLYFEECAP_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Fee attributes retrieved successfully");
				responseDto = responseBuilder.buildSuccessResponse(monthlyFeeCapAttributes,
						ResponseMessages.SUCCESS_PRODUCT_MONTHLYFEECAP_RETRIEVE, ResponseMessages.SUCCESS);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/alerts", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAlertMessages() {
		logger.info(CCLPConstants.ENTER);

		List<Alert> alertMessagesMap = productService.getProductAlertMessages();
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(alertMessagesMap,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);

		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update the maintenance Fees Attributes After fetching the existing Product
	 * fees using productId
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{productId}/maintenanceFee", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateMaintenanceFeeAttributes(
			@RequestBody Map<String, Object> inputFeeAttributes, @PathVariable("productId") Long productId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			try {
				logger.debug("Updating MaintenanceFee attributes for productId: {} MaintenanceFee Attributes -> {}",
						productId, inputFeeAttributes);
				int count = productService.updateMaintenanceFeeAttributes(inputFeeAttributes, productId, "Maintenance Fees");
				if (count > 0) {
					logger.info("MaintenanceFee attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,
							ResponseMessages.SUCCESS_PRODUCT_MAINTENANCE_FEE_UPDATE, ResponseMessages.SUCCESS);
				} else {
					logger.info("Failed to update MaintenanceFee attributes");
					responseDto = responseBuilder.buildFailureResponse(
							ResponseMessages.FAILED_PRODUCT_MAINTENANCE_FEE_UPDATE,
							ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating MaintenanceFee attributes {}", e);
				responseDto = responseBuilder.buildFailureResponse(
						ResponseMessages.FAILED_PRODUCT_MAINTENANCE_FEE_UPDATE,
						ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	/**
	 * Getting the Product Maintenance fee from Product Using ProductId
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{productId}/maintenanceFee", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMaintenanceFeeAttributes(@PathVariable("productId") Long productId)
			throws IOException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, Object> maintenanceFeeAttributes = null;

		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Get Maintenance Fee attributes for product Id: {}", productId);
			maintenanceFeeAttributes = productService.getProductFeeLimitsById(productId, "Maintenance Fees");

			if (CollectionUtils.isEmpty(maintenanceFeeAttributes)) {
				logger.error("Failed to get MaintenanceFee attributes, feeAttribute is NULL");
				responseDto = responseBuilder.buildSuccessResponse(maintenanceFeeAttributes,
						ResponseMessages.EMPTY_PRODUCT_MAINTENANCE_FEE_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Fee attributes retrieved successfully");
				responseDto = responseBuilder.buildSuccessResponse(maintenanceFeeAttributes,
						ResponseMessages.SUCCESS_PRODUCT_MAINTENANCE_FEE_RETRIEVE, ResponseMessages.SUCCESS);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get Pin Details for the Transactions by ProductID
	 */
	@RequestMapping(value = "/{productId}/pins", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductPIN(@PathVariable("productId") Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, Object> pinAttributes = productService.getProductPin(productId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(pinAttributes,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update Pin Details for the Transactions by ProductID
	 */
	@RequestMapping(value = "/{productId}/pins", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateProductPin(@PathVariable("productId") Long productId,
			@RequestBody Map<String, Object> productPinMap) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			if (CollectionUtils.isEmpty(productPinMap)) {
				logger.info("Attributes map empty {}", productId);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY,
						ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Updating Product Pin Attributes..");
				productService.updateProductPinAttributes(productPinMap, productId);
				responseDto = responseBuilder.buildSuccessResponse(null,
						CCLPConstants.PRODUCT_ACTION_UPDATE + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				Map<String, String> valuesMap = new HashMap<>();
				valuesMap.put(CCLPConstants.ACTION, "PIN");

				String templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/{productId}/alerts", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateAlertsAttributes(@RequestBody Map<String, Object> inputAlertsAttributes,
			@PathVariable("productId") Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, Object> newInputAlertsAttributes;
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
			logger.info(CCLPConstants.EXIT);
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		} else {

			logger.debug("input Alert Attributes for Product Id::" + productId + " >>> " + inputAlertsAttributes);

			productService.updateAlertAttributes(inputAlertsAttributes, productId);

			newInputAlertsAttributes = productService.getAlertAttributesByProductId(productId);
		}
		responseDto = responseBuilder.buildSuccessResponse(newInputAlertsAttributes,
				ResponseMessages.SUCCESS_PRODUCT_ALERT_UPDATE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/{productId}/alerts", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAlertAttributes(@PathVariable("productId") Long productId)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, Object> alertAttributes = null;

		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Get alert attributes for product Id: {}", productId);
			alertAttributes = productService.getAlertAttributesByProductId(productId);

			logger.debug("Alert attributes >>> " + alertAttributes);

			if (CollectionUtils.isEmpty(alertAttributes)) {
				logger.error("Failed to get alert attributes, alertAttributes is NULL");
				responseDto = responseBuilder.buildSuccessResponse(alertAttributes,
						ResponseMessages.EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Alert attributes retrieved successfully");
				responseDto = responseBuilder.buildSuccessResponse(alertAttributes,
						ResponseMessages.SUCCESS_PRODUCT_ALERT_RETRIEVE, ResponseMessages.SUCCESS);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting the Product PAN Expire from actual Product
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{productId}/panExpiry", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getPanExpiryAttributes(@PathVariable("productId") Long productId)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Get pan expiry attributes for product Id: {}", productId);
			Map<String, Object> panExpiryAttributes = productService.getProductAttributesByIdAndGroupname(productId,
					"PAN Expiry");

			if (CollectionUtils.isEmpty(panExpiryAttributes)) {
				logger.error("Failed to get pan expiry attributes, panExpiryAttributes is empty");
				responseDto = responseBuilder.buildSuccessResponse(panExpiryAttributes,
						ResponseMessages.EMPTY_PRODUCT_PANEXPIRY_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Pan Expiry attributes retrieved successfully");
				responseDto = responseBuilder.buildSuccessResponse(panExpiryAttributes,
						ResponseMessages.SUCCESS_PRODUCT_PANEXPIRY_RETRIEVE, ResponseMessages.SUCCESS);
			}
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update the Pan Expiry Attributes
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */

	@RequestMapping(value = "/{productId}/panExpiry", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updatePanExpiryAttributes(@RequestBody Map<String, Object> inputAttributes,
			@PathVariable("productId") Long productId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);

			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {

			logger.debug("Updating pan expiry attributes for productId: {} PAN Expiry Attributes -> {}", productId,
					inputAttributes);

			int updateCount = productService.updateProductAttributesByGroupName(inputAttributes, productId,
					"PAN Expiry");

			if (updateCount != 1) {
				logger.debug("Failed to update PAN Expiry Attributes", productId, inputAttributes);

				responseDto = responseBuilder.buildSuccessResponse(inputAttributes,
						ResponseMessages.FAILED_PRODUCT_PANEXPIRY_UPDATE,
						ResponseMessages.FAILED_PRODUCT_PANEXPIRY_UPDATE);
			} else {

				logger.info("PAN Expiry Attributes '{}' for Product id {} updated successfully", inputAttributes,
						productId);

				responseDto = responseBuilder.buildSuccessResponse(inputAttributes,
						ResponseMessages.SUCCESS_PRODUCT_PANEXPIRY_UPDATE, ResponseMessages.SUCCESS);
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Update the product attributes in cache
	 * 
	 * @param productId
	 *            The id of the Product to be updated.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/updProductPurseAttributesCacheById", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> updProductPurseAttributesCacheById(@RequestBody Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);

			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {

			productService.updProductPurseAttributesCacheById(productId);

			logger.info("Product attributes sucessfully updated for id {} into cache", productId);

			responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_PRODUCT_CACHE_UPDTAE,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Updating the product attributes in cache without passing Product Id
	 * 
	 * @throws Exception
	 */

	@RequestMapping(value = "/updateAllProductAndPurseAttributesCache", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> updateAllProductAttributesCache() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		productService.updateAllProductAndPurseAttributesCache();

		responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_PRODUCT_CACHE_UPDTAE,
				ResponseMessages.SUCCESS);
		
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting parent product details
	 * 
	 * @throws ServiceException
	 * 
	 */
	@RequestMapping(value = "/{partnerId}/getProductsWithSamePartner/{productId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductsWithSamePartner(@PathVariable("partnerId") Long partnerId,@PathVariable("productId") Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<Long, String> productsWithPartnerMap = productService.getProductsWithSamePartnerId(partnerId,productId);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(productsWithPartnerMap,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);

		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting parent product details
	 * 
	 * @throws ServiceException
	 * 
	 */
	@GetMapping(value = "/download")
	public ResponseEntity<ResponseDTO> buildPdfDocument() {
		logger.info(CCLPConstants.ENTER);
		Map<Double, String> buildPdfMetaData = productService.buildPdfMetaData();
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(buildPdfMetaData,
				ResponseMessages.SUCCESS_PDF_METADATA, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Updating the Product Attributes by ProgramId
	 * 
	 * @throws ServiceException
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/{programId}/program", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateParametersByProgramId(@RequestBody Map<String, Object> inputAttributes,
			@PathVariable("programId") Long programId) throws IOException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<String> errorList;

		if (programId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);

			logger.error("Program id is Negative", programId);
		} else {
			errorList = productService.updateProductAttributesByProgramId(inputAttributes, programId);
			if (CollectionUtils.isEmpty(errorList)) {
				logger.info("Updated product attributes successfully");
				responseDto = responseBuilder.buildSuccessResponse(errorList,
						ResponseMessages.SUCCESS_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.SUCCESS);
			} else {
				logger.error("Error while updating product attributes");
				responseDto = responseBuilder.buildFailureResponse(errorList,
						ResponseMessages.FAILED_PROGRAM_PARAMETERS_UPDATE, ResponseMessages.FAILURE);
			}

		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get product ID based on UPC
	 * 
	 * @param upc
	 * @return
	 * @throws ServiceException
	 */
	@GetMapping(value = "/{upc}/getProductIDByUPC")
	public ResponseEntity<ResponseDTO> getProductIdByUPC(@PathVariable("upc") String upc) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;
		Long productId = null;

		if (Util.isEmpty(upc)) {
			logger.info("UPC is empty");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			productId = productService.getProductIdByUPC(upc);
			responseDto = responseBuilder.buildSuccessResponse(productId, ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Get Package Ids by Product Id
	 * 
	 * @param productId
	 * @return
	 * @throws ServiceException
	 */
	@GetMapping(value = "/{productId}/packageIds")
	public ResponseEntity<ResponseDTO> getPackagesByProductId(@PathVariable("productId") Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.debug("productId: {}",productId);
		ResponseDTO responseDto = null;
		List<String> packages = null;
		if (Objects.isNull(productId) || productId <= 0) {
			logger.error("Invalid Product Id");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROGRAM_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			packages = productService.getPackagesIdByProductId(productId);
			responseDto = responseBuilder.buildSuccessResponse(packages, ResponseMessages.SUCCESS,
					ResponseMessages.SUCCESS);
		}
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Getting ProductAttributes based on productID
	 * 
	 * @param productId
	 * @throws ServiceException
	 * @author venkateshgaddam
	 **/

	@RequestMapping(value = "{productId}/getProdAttributes", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProdAttributes(@PathVariable("productId") Long productId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		String existingAttributes = productService.getOrUpdateProductAttributes(productId,0l,CCLPConstants.FROM_UI);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(existingAttributes, ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}
	
	
	/*
	 * Get all products
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@PostMapping(value = "/updateProductAttributesValueInCache")
	public ResponseEntity<ResponseDTO> updateProductAttributesValueInCache()
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		productService.updateProductAttributesValueInCache();

		responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_PRODUCT_UPDATE,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/*
	 * Get all Currency Details based on partner
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@GetMapping("/{partnerId}/getPartnerCurrency")
	public ResponseEntity<ResponseDTO> getPartnerCurrency(@PathVariable("partnerId") Long partnerId) {
		logger.info(CCLPConstants.ENTER);

		Map<String, Object> partnerCurrency = productService.getPartnerCurrency(partnerId);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(partnerCurrency,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);

		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}


	@GetMapping("/{partnerId}/getPartnerCurrencyList")
	public ResponseEntity<ResponseDTO> getPartnerCurrencyCodes(@PathVariable("partnerId") Long partnerId)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);

		List<CurrencyCodeDTO> partnerCurrency = productService.getPartnerCurrencyCodes(partnerId);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(partnerCurrency,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);

		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	/**
	 * Get Purse Details for the Transactions by ProductID
	 */
	@RequestMapping(value = "/{productId}/{parentProductId}/{attributeGroup}/purse", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductPurses(@PathVariable("productId") Long productId,
			@PathVariable("parentProductId") Long parentProductId,
			@PathVariable("attributeGroup") String attributeGroup)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);

		List<Purse> purseAttributes = productService.getPurseByProductId(productId,parentProductId,attributeGroup);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(purseAttributes,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Get Purse Details for the Transactions by ProductID
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/{productId}/purse/{purseId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateProductPurses(@PathVariable("productId") Long productId,
			@PathVariable("purseId") Long purseId, @RequestBody Map<String, Object> productPurseMap)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		if (productId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID_NEGATIVE,
					ResponseMessages.DOESNOT_EXISTS);
			logger.error(CCLPConstants.PRODUCT_ID_NEGATIVE, productId);
		} else {
			if (CollectionUtils.isEmpty(productPurseMap)) {
				logger.error("Attributes map is empty {}", productId);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_ATTRIBUTES_MAP_ISEMPTY,
						ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Updating Product Purse..");
				productService.updateProductPurse(productPurseMap, productId, purseId);
				responseDto = responseBuilder.buildSuccessResponse(null,
						ResponseMessages.PRODUCT_PURSE_UPDATE + ResponseMessages.SUCCESS, ResponseMessages.SUCCESS);
				Map<String, String> valuesMap = new HashMap<>();
				valuesMap.put(CCLPConstants.ACTION, "CVV");

				String templateString = responseDto.getMessage();
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				String resolvedString = sub.replace(templateString);
				responseDto.setMessage(resolvedString);
			}
		}
		logger.info("responseDto: {}", responseDto);
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	
	@RequestMapping(value = "/{productId}/purse/{purseId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getProductPurseAttributes(@PathVariable("productId") Long productId,
			@PathVariable("purseId") Long purseId) throws ServiceException, IOException {

		logger.info(CCLPConstants.ENTER);

		Map<String, Object> purseAttributes = productService.getProductPurse(productId, purseId, CCLPConstants.FROM_UI);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(purseAttributes,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	//Commented by Karthik
	/*@RequestMapping(value = "/{productId}/purse/{purseId}/cache", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> updateProductPurseAttributesCache(@PathVariable("productId") Long productId,
			@PathVariable("purseId") Long purseId) throws ServiceException, IOException {

		logger.info(CCLPConstants.ENTER);

		Map<String, Object> purseAttributes = productService.getProductPurse(productId, purseId,
				CCLPConstants.FROM_CACHE);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(purseAttributes,
				ResponseMessages.SUCCESS_PRODUCT_RETRIEVE, ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}*/
	
	/**
	 * Getting and updating ProductAttributes based on productID
	 * 
	 * @param productId
	 * @throws ServiceException
	 **/
	@RequestMapping(value = "{productId}/purse/{purseId}/cache/getOrUpdateProdAttributesCache", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getorUpdateProdAttributesCache(@PathVariable("productId") Long productId,
			@PathVariable("purseId") Long purseId)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		String existingAttributes = productService.getOrUpdateProductAttributes(productId,purseId,CCLPConstants.FROM_CACHE);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(existingAttributes, ResponseMessages.SUCCESS,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}
	
	

	/**
	 * Getting and updating Product Purse Attributes based on product Id and purse Id
	 * 
	 * @param productId
	 * @throws ServiceException
	 **/

	@PostMapping(value = "/updatePurseAttributesValueInCache")
	public ResponseEntity<ResponseDTO> updatePurseAttributesValueInCache() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		productService.updatePurseAttributesValueInCache();
		responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_PRODUCT_UPDATE,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/* One time activity developed by vinoth
	 * This method will be executed as part of R11 Release to remove null from all the
	 * existing product Attributes and Purse attributes
	 */
	@RequestMapping(value = "/removeNullFromAttributes", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> removeNullFromAttributes() throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		productService.removeNullFromAttributes();

		responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_PRODUCT_UPDATE,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	/* One time activity developed by vinoth
	 * This method will be executed as part of R12 Release to update splitTender attribute to txn level in
	 * existing product Attributes and Purse attributes
	 */
	@RequestMapping(value = "/updateBumpUpConfig", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> updateBumpUpConfig() throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		productService.updateBumpUpConfig();

		responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_PRODUCT_UPDATE,
				ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
}
