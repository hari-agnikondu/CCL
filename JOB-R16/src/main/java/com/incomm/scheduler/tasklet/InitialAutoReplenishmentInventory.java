package com.incomm.scheduler.tasklet;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.AutoReplenishInventoryDAO;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.AutoReplenishmentInventory;
import com.incomm.scheduler.model.OrderForm;
import com.incomm.scheduler.service.ManualOrderService;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.Util;

@Component
public class InitialAutoReplenishmentInventory {

	@Autowired
	AutoReplenishInventoryDAO autoReplenishInventoryDAO;

	private static RestTemplate restTemplate = new RestTemplate();

	@Value("${ORDER_BASE_URL}")
	String orderBaseUrl;

	@Autowired
	ManualOrderService orderService;

	private static final Logger logger = LogManager.getLogger(InitialAutoReplenishmentInventory.class);

	public void setAutoReplenishInvDaoObj(AutoReplenishInventoryDAO autoReplenishInventoryDAO) {

		this.autoReplenishInventoryDAO = autoReplenishInventoryDAO;

	}

	public void intiateBatchAutoReplenishInventory() throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		List<AutoReplenishmentInventory> replData = autoReplenishInventoryDAO.autoReplenishCheck();

		if (replData != null && !replData.isEmpty()) {

			logger.info("No Of Records for Auto Replenishment >>>>: " + replData.size());

			for (int i = 0; i < replData.size(); i++) {

				logger.info("Auto Replenishment for Record Number >>>>: " + i);

				int reorderCount = 0;

				AutoReplenishmentInventory autoReplInv = replData.get(i);
				reorderCount = Integer.parseInt(autoReplInv.getReorderValue()); 
				/**ADDED  BY  NAWAZ FOR UPDATING LOCATION INVENTORY*/
				
				if (reorderCount <= Integer.parseInt(autoReplInv.getMaxInventory())) {
					logger.info("ReOrder Count for Auto Replenishment for Record Number >>>" + i + ">>>>: " + reorderCount);

					 List prodData = autoReplenishInventoryDAO.getProductDetails(String.valueOf(autoReplInv.getProductId()));

					if (prodData != null) {

						logger.info("After Fectching Data for Prpduct id: " + autoReplInv.getProductId());

						OrderForm orderForm = new OrderForm();

						Map<String, String> prodMap = (Map<String, String>) prodData.get(0);

						orderForm.setLocationId(Long.parseLong(autoReplInv.getLocationId()));
						orderForm.setQuantity(String.valueOf(reorderCount));
						orderForm.setIssuerId(Long.parseLong(String.valueOf(prodMap.get("ISSUER_ID"))));
						orderForm.setPartnerId(Long.parseLong(String.valueOf(prodMap.get("PARTNER_ID"))));
						orderForm.setMerchantId(Long.parseLong(autoReplInv.getMerchantId()));
						orderForm.setProductId(Long.parseLong(String.valueOf(autoReplInv.getProductId())));
						orderForm.setInsUser(1);
						orderForm.setPackageId(prodMap.get("default_Package"));
						orderForm.setOrderId(
								generateRandomOrderId(Long.parseLong(String.valueOf(autoReplInv.getProductId()))));
						logger.info("Generated OrderId is {}", orderForm.getOrderId());

						ResponseDTO orderData = saveOrder(orderForm);

						if (orderData != null && orderData.getResult().equalsIgnoreCase("SUCCESS")) {

							logger.info("Order Successfully Saved for Order ID : {}", orderForm.getOrderId());

							String[] orderPartnerId = {
									orderForm.getOrderId() + "~" +orderForm.getPartnerId() };

							orderForm.setOrderPartnerId(orderPartnerId);
							orderForm.setStatus("APPROVED");
							ResponseDTO approvedOrderData = approveRejectOrder(orderForm);

							if (approvedOrderData != null
									&& approvedOrderData.getResult().equalsIgnoreCase("SUCCESS")) {

								logger.info("Order Successfully Approved for Order ID : {}", orderForm.getOrderId());

								String result = null;

								result = orderService.getManualOrder(orderForm.getOrderId(), "1",
										orderForm.getPartnerId());

								if (!Util.isEmpty(result) && result.equalsIgnoreCase("ok")) {
									logger.info("AUTO REPLENISHMENT OF iNVENTORY GENERATED for orderId: {}, lineItemId: {} and partnerId: {}",
											orderForm.getOrderId(), "1", orderForm.getPartnerId());
								}

							}

						}
					}
				}

			}

		}
		logger.info(CCLPConstants.EXIT);
	}

	public ResponseDTO saveOrder(OrderForm orderForm) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			logger.debug("Calling '{}' service to place new Order Record", orderBaseUrl);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(orderBaseUrl + "/orders",
					orderForm, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while creating new order, {}", e);
			throw new ServiceException(JobConstants.ORDER_CONNECTION_ERROR);
		}
		logger.info(CCLPConstants.EXIT);
		return responseBody;
	}

	public ResponseDTO approveRejectOrder(OrderForm orderForm) throws ServiceException {
		logger.info(CCLPConstants.ENTER + orderForm);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<OrderForm> requestEntity = new HttpEntity<>(orderForm, headers);
			logger.debug("Calling '{}' service to approve or reject order ", orderBaseUrl);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(orderBaseUrl + "/orders/status",
					HttpMethod.PUT, requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating order {}", e);
			throw new ServiceException(JobConstants.ORDER_LESS_NO_OF_CARDS_FOR_THIS_LOCATION_ID);
		}
		logger.info(CCLPConstants.EXIT);
		return responseBody;

	}

	public String generateRandomOrderId(Long productID) {
		int leftLimit = 97; 
	    int rightLimit = 122;
	    int targetStringLength = 9;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
		return String.valueOf(productID)+generatedString;

	}

}
