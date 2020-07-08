package com.incomm.cclp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.OrderDTO;
import com.incomm.cclp.exception.ServiceException;

public interface OrderService {


	Map<Long, String> getAllLocationByMerchantId(String merchantId);

	List<OrderDTO> getAllOrdersForApproval();
	
	List<OrderDTO> getAllOrdersForOrder();
	
	List<OrderDTO> getAllOrdersForCCF();

	void createOrder(OrderDTO orderDto) throws ServiceException;

	Map<String, Object> getAllMerchantsAndPackageIdsByProductId(Long productId) throws ServiceException;

	List<OrderDTO> getOrdersByOrderIdAndProductId(String orderId, Long productId,String status,String fromDate,String toDate);

	String changeOrderStatus(OrderDTO orderDto) throws ServiceException;

	String getAvailableInventoryForLocation(String merchantId, Long locationId, Long productId) throws ServiceException;

	void loadMailAlertDetails(String smtpHostAddress, String smtpHostAddressPropKey);

	List<Map<String, String>> placeDigitalOrder(List<Map<String, String>> orderDetails);

}


