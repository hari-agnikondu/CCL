package com.incomm.cclp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.OrderDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.OrderService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/orders")
@Api(value="order")
public class OrderController {

	@Autowired
	private ResponseBuilder responseBuilder;
	
	private static final Logger logger = LogManager.getLogger(OrderController.class);

	@Autowired
	OrderService orderService;
	
	@Value("${SMTP_STATUS_UPDATE}")
	String smtpHostAddress;
	

	
	
	@PostConstruct
	public void loadMailAlertDetails() {
		
		orderService.loadMailAlertDetails(smtpHostAddress,"SMTP_STATUS_UPDATE");
	}
	
	
	
	/**
	 * Creates an Order.
	 * 
	 * @param OrderDto
	 *            The orderDto.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createOrder(@RequestBody OrderDTO orderDto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		Map<String, String> valuesMap = new HashMap<>();
		
		logger.info(orderDto.toString());
		orderService.createOrder(orderDto);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				("ORDER_ADD_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
		valuesMap.put("orderId", orderDto.getOrderId());
		String templateString = "";

		templateString = responseDto.getMessage();

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		String resolvedString = sub.replace(templateString);

		responseDto.setMessage(resolvedString);

		logger.debug("Order created successfully");
		logger.info(responseDto.toString());
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets merchant list mapped to given product id.
	 * 
	 * @param productId
	 *            The id of the Product for which merchant list to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/{productId}/getMerchantsAndPackageIdsByProductId", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllMerchantsAndPackageIdsByProductId(
			@PathVariable("productId") Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		if (productId <= 0) {
			logger.error("merchant Id is negative: {}", productId);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PRODUCT_ID,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			Map<String, Object> merchantList = orderService.getAllMerchantsAndPackageIdsByProductId(productId);
			if (CollectionUtils.isEmpty(merchantList)) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_MERCHANT_RETRIEVE,
						ResponseMessages.DOESNOT_EXISTS);
				logger.error("Failed to fetch merchant List for product {}", productId);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(merchantList,
						("MERCHANT_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);
				logger.debug("merchant list for productId: {} has retrieved successfully {}", productId, merchantList);
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	/**
	 * Getting the Orders to check Order Status
	 * 
	 * @param The orderId or ProductId as input parameters
	 *      
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/searchproduct/{orderId}/{productId}/{status}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getOrdersByOrderIdAndProductId(@PathVariable("orderId") String orderId,
			@PathVariable("productId") Long productId,@PathVariable("status") String  status,
			@PathVariable("fromDate") String  fromDate,@PathVariable("toDate") String  toDate){
		logger.debug("Entered");
		List<OrderDTO> orderList = orderService.getOrdersByOrderIdAndProductId(orderId, productId,status,fromDate,toDate);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(orderList,
				("ORDER_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets merchant list mapped to given product id.
	 * 
	 * @param productId
	 *            The id of the Product for which merchant list to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{merchantId}/locations", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllLocationByMerchantId(@PathVariable("merchantId") String merchantId) {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		if (Util.isEmpty(merchantId)) {
			logger.error("merchant Id is negative: {}", merchantId);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_MERCHANT_ID,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			Map<Long, String> locationList = orderService.getAllLocationByMerchantId(merchantId);
			if (CollectionUtils.isEmpty(locationList)) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_LOCATION_RETRIEVE,
						ResponseMessages.DOESNOT_EXISTS);
				logger.error("Failed to fetch Location List for merchant {}", merchantId);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(locationList,
						("LOCATION_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

				logger.debug("Location list for merchantId: {} has retrieved successfully {}", merchantId,
						locationList);
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	/**
	 * Gets Available inventory value for a merchants particular location.
	 * 
	 * @param merchantId
	 *            The id of the merchant for which available inventory to be retrieved.
	 * @param locationId
	 *            The id of the location for which available inventory to be retrieved.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/{productId}/{merchantId}/{locationId}/availableInventory", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAvailableInventoryForLocation(@PathVariable("merchantId") String merchantId,
			@PathVariable("locationId") Long locationId, @PathVariable("productId") Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		if (Util.isEmpty(merchantId) || locationId <= 0 || productId <= 0 ) {
			logger.error("merchant Id or location Id or productId can't be negative: MId{} LId PId", merchantId,locationId,productId);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_MERCHANT_ID_OR_LOCATION_ID,
					ResponseMessages.DOESNOT_EXISTS);
		} else {
			String availableInventory = orderService.getAvailableInventoryForLocation(merchantId, locationId, productId);
			if (availableInventory.isEmpty()) {
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_AVAILABLE_INVENTORY,
						ResponseMessages.DOESNOT_EXISTS);
				logger.error("Failed to fetch available inventory for merchant {} at Location  {}", merchantId, locationId);
			} else {
				responseDto = responseBuilder.buildSuccessResponse(availableInventory,
						("AVAILABLE_INVENTORY_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

				logger.debug("available inventory for merchant {} at Location {} has retrieved successfully available inventory is {}", merchantId,
						locationId,availableInventory);
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	/**
	 * Change Order status
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/status", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> changeOrderStatus(@RequestBody OrderDTO orderDto) throws ServiceException {
		logger.debug("Entered");
		ResponseDTO responseDto = null;

		String respMsg = orderService.changeOrderStatus(orderDto);

		if (("APPROVED").equalsIgnoreCase(orderDto.getStatus())) {
			responseDto = responseBuilder.buildSuccessResponse(null, (respMsg),
					ResponseMessages.SUCCESS_ORDER_APPROVED);
		} else {
			responseDto = responseBuilder.buildSuccessResponse(null, (respMsg),
					ResponseMessages.SUCCESS_ORDER_REJECTED);
		}

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	/**
	 * Gets all Order status which are in PENDING status
	 *
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllOrdersForApproval() {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		List<OrderDTO> orderDtos = orderService.getAllOrdersForApproval();
			responseDto = responseBuilder.buildSuccessResponse(orderDtos,
					("ORDER_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Gets all Order status which are in APPROVED status
	 *
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllOrdersFororder() {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		List<OrderDTO> orderDtos = orderService.getAllOrdersForOrder();
			responseDto = responseBuilder.buildSuccessResponse(orderDtos,
					("ORDER_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Gets all Order status which are in PROCESSED status
	 *
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/CCF", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getAllOrdersForCCF() {
		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDto = null;

		List<OrderDTO> orderDtos = orderService.getAllOrdersForCCF();
			responseDto = responseBuilder.buildSuccessResponse(orderDtos,
					("ORDER_RETRIEVE_" + ResponseMessages.SUCCESS), ResponseMessages.SUCCESS);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="/digitalOrder", method=RequestMethod.POST)
	public ResponseEntity<List<Map<String, String>>> placeDigitalOrder(@RequestBody List<Map<String, String>> orderDetails){
		
		logger.debug(CCLPConstants.ENTER);
		List<Map<String, String>> orderStatus = null;
		orderStatus = orderService.placeDigitalOrder(orderDetails);
		
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity<>(orderStatus, HttpStatus.OK);
		
	}
	
}
