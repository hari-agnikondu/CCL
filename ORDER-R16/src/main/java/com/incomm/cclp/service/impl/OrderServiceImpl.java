package com.incomm.cclp.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.OrderDAO;
import com.incomm.cclp.domain.Location;
import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.Order;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.domain.ProductCardRange;
import com.incomm.cclp.dto.OrderDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;
import com.incomm.cclp.service.OrderService;
import com.incomm.cclp.util.Util;

@Service
public class OrderServiceImpl implements OrderService {

	
	@Autowired 
	OrderDAO orderDao;
	
	@Autowired
	OrderProcessDAO orderProcessDao;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

	@Override
	public Map<String, Object> getAllMerchantsAndPackageIdsByProductId(Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		Map<String, String> mechantList = new HashMap<>();
		 Map<String, Object> merchantAndPackageIds = new HashMap<>();
		Map<String, Map<String, Object>> productAttributes = new HashMap<>();
		Set<String> packageIds = new LinkedHashSet<>();
		Long availableInventory = (long) 0;
		
	    List<Merchant> merchants = orderDao.getAllMerchantByProductId(productId); 
	    if(!CollectionUtils.isEmpty(merchants)) {
	    	merchants.stream().forEach(p -> mechantList.put(p.getMerchantId(), p.getMerchantName()));
	    }
	    
	    Product product = orderDao.getProductById(productId);
	    
	     if(!Util.isEmpty(product.getAttributes())) {
			try {
				productAttributes = Util.jsonToMap(product.getAttributes());
			} catch (Exception e) {
				logger.error(e);
			}
			Map<String, Object> attributes = productAttributes.get("Product");
			String defaultPackageId =String.valueOf(attributes.get("defaultPackage")) ;

			packageIds.add(defaultPackageId);
	
			List<String> listOfProductPackage =null;
			
				listOfProductPackage = orderDao.getProductLinkedPackageIds(productId);
			
			if (!CollectionUtils.isEmpty(listOfProductPackage)) {
				listOfProductPackage.forEach(packageIds::add);
			}
	     }
	 
	     List<ProductCardRange> productCardRanges = product.getListProductCardRange();
	     List<Long> cardRanges = new ArrayList<>();
			if (!CollectionUtils.isEmpty(productCardRanges)) {
				productCardRanges.forEach(productCardRange -> cardRanges.add(productCardRange.getCardRange().getCardRangeId()));
			}
	     
	     if(!CollectionUtils.isEmpty(cardRanges)) {
			 availableInventory = orderDao.getavailableInventory(cardRanges);
	     }
			
	     merchantAndPackageIds.put("packageIds", packageIds);
	     merchantAndPackageIds.put("merchants", mechantList);
	     merchantAndPackageIds.put("availableInventory", availableInventory);
	     
		logger.debug(CCLPConstants.EXIT);		
	    return merchantAndPackageIds;
	}

	@Override
	public Map<Long, String> getAllLocationByMerchantId(String merchantId) {
		
		logger.debug(CCLPConstants.ENTER);
		Map<Long, String> locationList = new HashMap<>();
	    List<Location> locations = orderDao.getAllLocationByMerchantId(merchantId); 
	    if(!CollectionUtils.isEmpty(locations)) {
	    	locations.stream().forEach(p -> locationList.put(p.getLocationId(), p.getLocationName()));
	    }
	    
		logger.debug(CCLPConstants.EXIT);		
	    return locationList;
	}

	@Override
	public List<OrderDTO> getAllOrdersForApproval() {
		
		List<Object[]> orderList = orderDao.getAllOrdersForApproval();
		OrderDTO orderDto = null;
		List<OrderDTO> orderDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(orderList))
		{
			Iterator<Object[]> iterator = orderList.iterator();
			while (iterator.hasNext()) {
				Object[] obj =  iterator.next();

				java.sql.Date date=null;
				if( obj[11]!=null) {
				java.sql.Timestamp timestamp = (Timestamp) obj[11];
				 long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
				  date=new java.sql.Date(milliseconds);
				}
			
				
					orderDto = new OrderDTO((String) obj[0], ((BigDecimal) obj[1]).longValue(), (String) obj[2],
							((BigDecimal) obj[3]).longValue(), (String) obj[4],(String) obj[5],
							(String) obj[6], (Long.parseLong((String) obj[7])),
							(String) obj[8],
							((BigDecimal) obj[9]).longValue(), 
							(String) obj[10],
							date, 
							(String) obj[12],
							(String) obj[13], 
							(String) obj[14],
							(String) obj[15],
							((BigDecimal) obj[16]).toString(),
							((String) obj[17]),
							(String) obj[18],
							((BigDecimal) obj[19]).longValue() );
				
				orderDtos.add(orderDto);
			}
		}
		return orderDtos;
	}
    
	@Override
	public void createOrder(OrderDTO orderDto) throws ServiceException {
		
		logger.debug(CCLPConstants.ENTER);
		logger.info(orderDto.toString());
		Order order = new Order();
		Long availableInventory = null;
		String lineItemId="1";
		Map<String, Object> attributes = new HashMap<>();
		Map<String, Map<String, Object>> productAttributes = new HashMap<>();
		
		
		//orderID..autoGeneration
		if(Util.isEmpty(orderDto.getOrderId()))
		{
			orderDto.setOrderId(generateRandomOrderId(orderDto.getProductId()));
			logger.info("Generated OrderId is {}",orderDto.getOrderId());
		}
		
		
		Product product = orderDao.getProductById(orderDto.getProductId());
		
		checkProductValidity(product.getProductId());
		
		String merchantId = orderDto.getMerchantId();
		Long locationId = orderDto.getLocationId();
		
		
		if(Util.isEmpty(merchantId) || locationId < 0) {
			
			/*if merchant or location is not selected consider it as a inventory*/
			order.setMerchantId(null);
			order.setLocationId(null);
			
		List<ProductCardRange> productCardRanges = product.getListProductCardRange();
	     List<Long> cardRanges = new ArrayList<>();
			if (!CollectionUtils.isEmpty(productCardRanges)) {
				productCardRanges.forEach(productCardRange -> cardRanges.add(productCardRange.getCardRange().getCardRangeId()));
			}
	     
	     if(!CollectionUtils.isEmpty(cardRanges)) {
			 availableInventory = orderDao.getavailableInventory(cardRanges);
	     }
		}else {
			
			order.setMerchantId(merchantId);
			order.setLocationId(locationId);
			
		 availableInventory = orderDao.getavailableInventory(merchantId, locationId, orderDto.getProductId());
		}
		
		if(availableInventory < Long.parseLong(orderDto.getQuantity())) {
			throw new ServiceException(ResponseMessages.ERR_AVAILABLE_INVENTORY);
			
		}
		
		
		
		List<Order> orders = orderDao.getOrderById(orderDto.getOrderId());
	     
		long partnerId = product.getPartner().getPartnerId();
		if(!CollectionUtils.isEmpty(orders)) {
			
		lineItemId = Integer.toString(orders.size()+1);

		
		List<Order> existingOrders = orders.stream()
		.filter(orderTemp -> orderTemp.getPartnerId() == partnerId)
		.collect(Collectors.toList());
		
		if (existingOrders != null && !existingOrders.isEmpty())
			throw new ServiceException(ResponseMessages.ERR_ORDER_EXISTS);
		
		}
		
		if(!Util.isEmpty(product.getAttributes())) {
			try {
				productAttributes = Util.jsonToMap(product.getAttributes());
			} catch (Exception e) {
				logger.error(e);
			}
		      attributes = productAttributes.get("General");
		}
		
		if(attributes != null && attributes.get("defaultCardStatus") != null) {
			
			order.setDefaultCardStatus((String)attributes.get("defaultCardStatus"));
		}
		attributes = productAttributes.get("Product");
		String formFactor =String.valueOf( attributes.get("formFactor"));
		
		if("Digital".equalsIgnoreCase(formFactor)) {
			order.setOrderStatus("APPROVED");
			order.setStatus("APPROVED");
			order.setOrderType(String.valueOf( attributes.get("productType")));
		}else {
			order.setOrderStatus("PENDING");
			order.setStatus("PENDING");
			order.setOrderType("RETAIL");
		}
		
		order.setInsUser(orderDto.getInsUser());
		order.setQuantity(orderDto.getQuantity());
		order.setPackageId(orderDto.getPackageId());
		order.setProductId(orderDto.getProductId());

		order.setOrderId(orderDto.getOrderId());
		order.setPartnerId(product.getPartner().getPartnerId());
		order.setIssuerId(product.getIssuer().getIssuerId());
		order.setLineItempartnerId(product.getPartner().getPartnerId());
		order.setInsDate();
		
		order.setLineItemId(lineItemId);
		orderDao.createOrder(order);

		if(!Util.isEmpty(merchantId) && locationId > 0) {
		
			orderDao.updateLocationInventory(merchantId,locationId,orderDto.getProductId(),orderDto.getQuantity());
		}
		
		logger.info("Record created for :" + orderDto.getOrderId());
		logger.debug(CCLPConstants.EXIT);

	}
	

	private void checkProductValidity(Long productId) throws ServiceException {
		
		int count = orderDao.checkProductvalidity(productId);
		if(count<=0)
			throw new ServiceException(ResponseMessages.INVALID_PRODUCT_VALIDITY_DATE);
	}

	@Override
	public List<OrderDTO> getOrdersByOrderIdAndProductId(String orderId, Long productId,String status,String fromDate,String toDate) {
		List<Object[]> orderList = orderDao.getOrdersByOrderIdAndProductId(orderId,productId,status,fromDate,toDate);
		OrderDTO orderDto = null;
		List<OrderDTO> orderDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(orderList)) {
			
			Iterator<Object[]> iterator = orderList.iterator();
			while (iterator.hasNext()) {
				Object[] obj = iterator.next();
				java.sql.Date date=null;
				if( obj[11]!=null) {
				java.sql.Timestamp timestamp = (Timestamp) obj[11];
				 long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
				  date=new java.sql.Date(milliseconds);
				}   
			
				orderDto = new OrderDTO((String) obj[0], ((BigDecimal) obj[1]).longValue(), (String) obj[2],
						((BigDecimal) obj[3]).longValue(), (String) obj[4], (String) obj[5],
						(String) obj[6], (Long.parseLong((String) obj[7])), (String) obj[8],
						((BigDecimal) obj[9]).longValue(), (String) obj[10],date, (String) obj[12],
						(String) obj[13], (String) obj[14], (String) obj[15],
						((BigDecimal) obj[16]).toString(), ((BigDecimal) obj[17]).longValue(),(String) obj[18],
						(String) obj[19],(String) obj[20],(String) obj[21],(String) obj[22]);

				orderDtos.add(orderDto);
			}
		}else {
			logger.error("No order record found");
		}
		return orderDtos;
	}

	@Override
	public String changeOrderStatus(OrderDTO orderDto) throws ServiceException {
		
		String respMsg="";
		int updatedCnt=orderDao.changeOrderStatus(orderDto);
		if("APPROVED".equals(orderDto.getStatus())){
			if(updatedCnt==0)
				throw new ServiceException(ResponseMessages.ERR_ORDER_APPROVED);
			else if(updatedCnt==1)
				respMsg=ResponseMessages.SUCCESS_ORDER_APPROVED;
			else
				respMsg=ResponseMessages.SUCCESS_ORDERS_APPROVED;
		}else if("REJECTED".equals(orderDto.getStatus())){
			if(updatedCnt==0)
				throw new ServiceException(ResponseMessages.ERR_ORDER_REJECTED);
			else if(updatedCnt==1)
				respMsg=ResponseMessages.SUCCESS_ORDER_REJECTED;
			else
				respMsg=ResponseMessages.SUCCESS_ORDERS_REJECTED;
		}
		return respMsg;
	}

	@Override
	public String getAvailableInventoryForLocation(String merchantId, Long locationId, Long productId) throws ServiceException {
		Long availableInventory = orderDao.getavailableInventory(merchantId, locationId, productId);
	
		if(availableInventory <= 0) {
			throw new ServiceException(ResponseMessages.ERR_NEGATIVE_AVAILABLE_INVENTORY);
		}
		return String.valueOf(availableInventory);
	}

	@Override
	public List<OrderDTO> getAllOrdersForOrder() {
		List<Object[]> orderList = orderDao.getAllOrdersForOrder();
		OrderDTO orderDto = null;
		List<OrderDTO> orderDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(orderList))
		{
			Iterator<Object[]> iterator = orderList.iterator();
			while (iterator.hasNext()) {
				Object[] obj =  iterator.next();

				java.sql.Date date=null;
				if( obj[11]!=null) {
				java.sql.Timestamp timestamp = (Timestamp) obj[11];
				 long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
				  date=new java.sql.Date(milliseconds);
				}
			
				orderDto = new OrderDTO((String) obj[0], ((BigDecimal) obj[1]).longValue(), (String) obj[2],
						((BigDecimal) obj[3]).longValue(), (String) obj[4], (String) obj[5],
						(String) obj[6], (Long.parseLong((String) obj[7])), (String) obj[8],
						((BigDecimal) obj[9]).longValue(), (String) obj[10], date, (String) obj[12],
						(String) obj[13], (String) obj[14], (String) obj[15],
						((BigDecimal) obj[16]).toString(), (String) obj[17],(String) obj[18]);

				orderDtos.add(orderDto);
			}
		}
		return orderDtos;
	}

	@Override
	public List<OrderDTO> getAllOrdersForCCF() {
		List<Object[]> orderList = orderDao.getAllOrdersForCCF();
		OrderDTO orderDto = null;
		List<OrderDTO> orderDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(orderList))
		{
			Iterator<Object[]> iterator = orderList.iterator();
			while (iterator.hasNext()) {
				Object[] obj =  iterator.next();

				java.sql.Date date=null;
				if( obj[11]!=null) {
				java.sql.Timestamp timestamp = (Timestamp) obj[11];
				 long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
				  date=new java.sql.Date(milliseconds);
				}
			
				orderDto = new OrderDTO((String) obj[0], ((BigDecimal) obj[1]).longValue(), (String) obj[2],
						((BigDecimal) obj[3]).longValue(), (String) obj[4], (String) obj[5],
						(String) obj[6], (Long.parseLong((String) obj[7])), (String) obj[8],
						((BigDecimal) obj[9]).longValue(), (String) obj[10], date, (String) obj[12],
						(String) obj[13], (String) obj[14], (String) obj[15],
						((BigDecimal) obj[16]).toString(), ((String) obj[17]),(String) obj[18]);

				orderDtos.add(orderDto);
			}
		}
		return orderDtos;
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

	@Override
	@Transactional
	public void loadMailAlertDetails(String smtpHostAddress,String smtpHostAddressPropKey) {
		
		orderProcessDao.deleteMailAlertDetails();
		orderProcessDao.updateMailAlertDetails(smtpHostAddressPropKey,smtpHostAddress);
		
	}

	@Override
	public List<Map<String, String>> placeDigitalOrder(List<Map<String, String>> orderDetailsList) {
		OrderDTO orderDto = new OrderDTO();
		
		List<Map<String, String>> resposeList = new ArrayList<>();
		
		for(Map<String,String> orderDetails: orderDetailsList ) {
			Map<String, String> response = new HashMap<String,String>();
		Map<String, Map<String, Object>> productAttributes = new HashMap<>();
		String orderId = "";
		try {
			
		orderDto.setProductId(Long.parseLong(orderDetails.get("productId")));
		
		orderDto.setQuantity(orderDetails.get("quantity"));
		orderDto.setInsUser(1);
		Product product = orderDao.getProductById(orderDto.getProductId());
		
		
	     if(!Util.isEmpty(product.getAttributes())) {
			
				try {
					productAttributes = Util.jsonToMap(product.getAttributes());
				} catch (IOException e) {
					logger.error("Exception in Digital order"+ e);
					throw new ServiceException(ResponseMessages.FAILURE,ResponseMessages.FAILURE);
				}
			
			Map<String, Object> attributes = productAttributes.get("Product");
			String defaultPackageId =String.valueOf(attributes.get("defaultPackage")) ;
			
			orderDto.setPackageId(defaultPackageId);
			orderId = generateRandomOrderId();
			
			orderDto.setOrderId(orderId);
	     }
		
			createOrder(orderDto);
			
			response.put("productId", orderDetails.get("productId"));
			response.put("orderId", orderId);
			response.put("status", "success");
		} catch (Exception e) {
			logger.info("Exception while placing order :" + e);
			response.put("productId", orderDetails.get("productId"));
			response.put("orderId", "");
			response.put("status", "failed");
		}
		
		resposeList.add(response);
		}
		return resposeList;
	}
	public String generateRandomOrderId() {
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
			return "DIGI"+generatedString;
	}
	
}

