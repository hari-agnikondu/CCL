package com.incomm.scheduler.serviceimpl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.dao.DigitalCardProcessingDAO;
import com.incomm.scheduler.service.DigitalCardProcessingService;

@Service
public class DigitalCardProcessingServiceImpl implements DigitalCardProcessingService {

	private static final Logger logger = LogManager.getLogger(DigitalCardProcessingServiceImpl.class);
	
	@Autowired
	DigitalCardProcessingDAO digitalCardProcessingDAO;
		
	@Value("${ORDER_BASE_URL}")
	String orderUrl;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public void digitalCardProcessing() {

		try {

			List<Map<String, Object>> productList = new ArrayList<>();
			productList = digitalCardProcessingDAO.getAllDigitalProducts();

			if (!CollectionUtils.isEmpty(productList)) {

				List<Map<String, String>> orderDetails = new ArrayList<>();
				for (Map<String, Object> product : productList) {
					Map<String, String> orderDetail = new HashMap<>();
					int quantity = 0;
					int issuedCardCount = digitalCardProcessingDAO
							.getIssuedCardCountForProd(String.valueOf(product.get("productId")));

					if (issuedCardCount > 0) {
						int availableCardCount = digitalCardProcessingDAO
								.getAvailableCardCountForProd(String.valueOf(product.get("productId")));
						int reorderLevel = Integer.parseInt(String.valueOf(product.get("reorderLevel")));
						if (availableCardCount <= reorderLevel) {
							quantity = Integer.parseInt(String.valueOf(product.get("reorderValue")));
						} else {
							continue;
						}
					} else {
						quantity = Integer.parseInt(String.valueOf(product.get("InitialOrder")));
					}

					orderDetail.put("productId", String.valueOf(product.get("productId")));
					orderDetail.put("quantity", String.valueOf(quantity));

					orderDetails.add(orderDetail);

				}
				if (!CollectionUtils.isEmpty(orderDetails)) {
					List<Map<String, String>> orderStatus = restTemplate
							.postForObject(orderUrl + "/orders/digitalOrder/", orderDetails, List.class);
					List<String> orderIds = new ArrayList<>();

					for (Map<String, String> orderStat : orderStatus) {

						String status = orderStat.get("status");

						if ("success".equalsIgnoreCase(status)) {
							orderIds.add(orderStat.get("orderId"));
						}

					}
					String responseMsg = "";
					if (!CollectionUtils.isEmpty(orderIds)) {
						for (String orderId : orderIds) {

							logger.info("Processing order: {}", orderId);
							responseMsg = digitalCardProcessingDAO.processOrder(orderId);

							if ("ok".equalsIgnoreCase(responseMsg))
								logger.info(" Procedure Called successfully {} ", responseMsg);
							else
								logger.error("Failure response from procedure {} ", responseMsg);
						}

					}

				} else {
					logger.info("No cards for digital products");
				}
			} else {
				logger.info("No digital products");
			}
		} catch (Exception e) {
			logger.info("Error while running Digital order processing job {} ", e);
		}
	}
}
