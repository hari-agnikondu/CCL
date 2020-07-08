package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.OrderDAO;
import com.incomm.cclp.domain.Location;
import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.Order;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.dto.OrderDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;

@Repository
public class OrderDAOImpl implements OrderDAO{

//	@Autowired
	@Qualifier("orderEntityManagerFactory")
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> getAllMerchantByProductId(Long productId) {
		return em.createNativeQuery(QueryConstants.GET_ALL_MERCHANT_BY_PRODUCTID, Merchant.class)
				.setParameter(ValueObjectKeys.PRODUCTID, productId).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getAllLocationByMerchantId(String merchantId) {
		return em.createNativeQuery(QueryConstants.GET_ALL_LOCATION_BY_MERCHANTID, Location.class)
				.setParameter(ValueObjectKeys.MERCHANT_ID, merchantId).getResultList();
	}

	@Override
	public Product getProductById(long productId) {
		return em.find(Product.class, productId);
	}
	
	@Override
	public Merchant getMerchantById(String merchantId) {
		return em.find(Merchant.class, merchantId);
	}
	
	@Override
	public Location getLocationById(long locationId) {
		return em.find(Location.class, locationId);
	}
	
	@Override
	@Transactional
	public void createOrder(Order order) {
		em.persist(order);
		
		
	}

	@Override
	public List<Order> getOrderById(String orderId) {
		@SuppressWarnings("unchecked")
		List<Order> order =   em.createQuery(QueryConstants.GET_ORDER_BY_ORDERID)
				.setParameter(ValueObjectKeys.ORDERID,orderId)
				.getResultList();
		
		 return order;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getOrdersByOrderIdAndProductId(String orderId, Long productId,String status,String fromDate,String toDate) {

	 String queryString=QueryConstants.GET_ORDERS_BY_ORDERID_AND_PRODUCTID +" and (:orderId ='-1'  or O.ORDER_ID = :orderId) and (:productId ='-1' or OL.PRODUCT_ID = :productId) and (:ORDER_STATUS ='-1' or O.ORDER_STATUS = :ORDER_STATUS) ";
	if(!fromDate.equals("-1") ) {
	 queryString=queryString+" and (:fromDate ='-1' or :toDate = '-1'  or trunc(O.INS_DATE) between trunc(to_date(:fromDate,'DD-MM-YYYY')) and trunc(to_date(:toDate,'DD-MM-YYYY')) ) ";
	queryString=queryString+" and (:fromDate ='-1' or :toDate != '-1' or  trunc(O.INS_DATE) >= trunc(to_date(:fromDate,'DD-MM-YYYY')) ) ";
	
	}Query query=em.createNativeQuery(queryString);
	 query.setParameter(ValueObjectKeys.ORDERID, orderId).setParameter(ValueObjectKeys.PRODUCTID, productId).setParameter("ORDER_STATUS", status);
	 if(!fromDate.equals("-1") ) {
		 query.setParameter("fromDate", fromDate).setParameter("toDate",toDate);
	 }
	 return query.getResultList();
	}
	
	
	@Override
	public Long getavailableInventory(List<Long> cardRanges) throws ServiceException {
		long availableInventory = (long) 0;
		try {
		 availableInventory =  ((Number)em.createNativeQuery(QueryConstants.GET_AVAILABLE_INVENTORY)
				.setParameter("cardRanges", cardRanges).getSingleResult()).longValue();
		}catch(NullPointerException npe) {
			throw new ServiceException(B2BResponseMessage.SYSTEM_ERROR,B2BResponseCode.SYSTEM_ERROR);
		}
		return availableInventory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllOrdersForApproval() {
		return em.createNativeQuery(QueryConstants.GET_ALL_ORDERS_FOR_APPROVAL)
				.getResultList();
	}

	@Override
	@Transactional
	public int changeOrderStatus(OrderDTO orderDto) {
		
		int updateCount=0;
		
		for (String orderPartnerIds : orderDto.getOrderPartnerId()) {
			if(orderPartnerIds != null) {
				
			String[] orderAndPartnerId = orderPartnerIds.split("~");
			  
			updateCount += em.createNativeQuery(QueryConstants.UPDATE_ORDER_STATUS)
					.setParameter("status", orderDto.getStatus())
					.setParameter("checkerRemarks", orderDto.getCheckerRemarks())
					.setParameter(ValueObjectKeys.ORDERID, orderAndPartnerId[0])
					.setParameter(ValueObjectKeys.PARTNERID, orderAndPartnerId[1])
					.setParameter("insUser", orderDto.getInsUser())
					.executeUpdate();
			
					em.createNativeQuery(QueryConstants.UPDATE_ORDER_LINE_ITEM_STATUS)
					.setParameter("status", orderDto.getStatus())
					.setParameter(ValueObjectKeys.ORDERID, orderAndPartnerId[0])
					.setParameter(ValueObjectKeys.PARTNERID, orderAndPartnerId[1])
					.executeUpdate();
					
					if("REJECTED".equals(orderDto.getStatus())) {
						em.createNativeQuery(QueryConstants.UPDATE_LOCATION_INVENTORY_FOR_REJECTED_ORDER)
						.setParameter(ValueObjectKeys.ORDERID, orderAndPartnerId[0]).setParameter(ValueObjectKeys.PARTNERID, orderAndPartnerId[1])
						.executeUpdate();
					}
			}
        } 
		return updateCount;
	
	}

	@Override
	public Long getavailableInventory(String merchantId, Long locationId, Long productId) {
		return ((Number)em.createNativeQuery(QueryConstants.GET_AVAILABLE_INVENTORY_FOR_LOCATION)
				.setParameter(ValueObjectKeys.MERCHANT_ID, merchantId)
				.setParameter(ValueObjectKeys.PRODUCTID, productId)
				.setParameter("locationId", locationId).getSingleResult()).longValue();
	}

	@Override
	@Transactional
	public void updateLocationInventory(String merchantId, Long locationId, Long productId, String quantity) {
		Long quantityValue = Long.parseLong(quantity);
		
		// change
			em.createNativeQuery(QueryConstants.UPDATE_LOCATION_INVENTORY)
			.setParameter(ValueObjectKeys.MERCHANT_ID, merchantId).setParameter("locationId", locationId).setParameter(ValueObjectKeys.PRODUCTID, productId)
			.setParameter("quantityValue", quantityValue).executeUpdate();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllOrdersForOrder() {
		return em.createNativeQuery(QueryConstants.GET_ALL_ORDERS_FOR_ORDER)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllOrdersForCCF() {
		return em.createNativeQuery(QueryConstants.GET_ALL_ORDERS_FOR_CCF)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getProductLinkedPackageIds(Long productId) {
		return em.createNativeQuery(QueryConstants.GET_ALL_PRODUCT_PACKAGES).setParameter(ValueObjectKeys.PRODUCTID, productId).getResultList();
	}

	
	@Override
	public int b2bDuplicateOrderCheck(String orderId, String partnerId) {
		int count = ((Number) em.createNativeQuery(QueryConstants.B2B_DUPLLICATE_ORDER_CHECK)
				.setParameter(ValueObjectKeys.ORDERID, orderId)
				.setParameter(ValueObjectKeys.PARTNERID, partnerId).getSingleResult()).intValue();
		if(count<=0)
			return 0;
		return 1;
	}

	@Override
	public Long b2bCheckOrderIDandPartnerID(String orderId, String partnerId) {
		return ((Number)em.createNativeQuery(QueryConstants.B2B_ORDERID_PARTNERID_CHECK)
				.setParameter(ValueObjectKeys.ORDERID, orderId)
				.setParameter(ValueObjectKeys.PARTNERID, partnerId).getSingleResult()).longValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> b2bCheckLineItemID(String orderId, String partnerId) {
		return em.createNativeQuery(QueryConstants.B2B_LINEITEMID_CHECK).setParameter(ValueObjectKeys.ORDERID, orderId)
				.setParameter(ValueObjectKeys.PARTNERID, partnerId).getResultList();
	}

	@Override
	public int checkProductvalidity(Long productId) {
		
		int count = ((Number) em.createNativeQuery(QueryConstants.CHECK_PRODUCT_VALIDITY).setParameter(ValueObjectKeys.PRODUCTID, productId).getSingleResult()).intValue();
		if(count == 1 ) 
			return 1;
		else return 0;
	}

}
