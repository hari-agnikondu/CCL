package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.Location;
import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.Order;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.dto.OrderDTO;
import com.incomm.cclp.exception.ServiceException;

public interface OrderDAO {

	List<Merchant> getAllMerchantByProductId(Long productId);

	List<Location> getAllLocationByMerchantId(String merchantId);

	void createOrder(Order order);

	Product getProductById(long productId);

	Merchant getMerchantById(String merchantId);

	Location getLocationById(long locationId);

	List<Order> getOrderById(String orderId);

	Long getavailableInventory(List<Long> cardRanges) throws ServiceException;

	List<Object[]> getAllOrdersForApproval();
	
	List<Object[]> getAllOrdersForOrder();
	
	List<Object[]> getAllOrdersForCCF();

	List<Object[]> getOrdersByOrderIdAndProductId(String orderId, Long productId,String status,String fromDate,String toDate);

	int changeOrderStatus(OrderDTO orderDto);

	Long getavailableInventory(String merchantId, Long locationId, Long productId);

	List<String> getProductLinkedPackageIds(Long productId);

	void updateLocationInventory(String merchantId, Long locationId, Long productId, String quantity);

	int b2bDuplicateOrderCheck(String orderId, String partnerId);

	Long b2bCheckOrderIDandPartnerID(String orderId, String partnerId);

	List<String> b2bCheckLineItemID(String orderId, String partnerId);

	int checkProductvalidity(Long productId);

}
