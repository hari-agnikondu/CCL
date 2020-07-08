package com.incomm.cclpvms.order.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.order.model.OrderForm;

public interface OrderService {

	public Map<String, Object> getMerchantsAndPackages(Long productId) throws ServiceException ;
	
	public Map<String,String> getStoresByMerchantId(Long merchantId) throws ServiceException;
	
	public ResponseDTO saveOrder(OrderForm orderForm) throws ServiceException;

	public List<OrderForm> getAllOrdersForApproval() throws ServiceException;
	
	public List<OrderForm> getAllOrdersForOrder() throws ServiceException;
	
	public List<OrderForm> getAllOrdersForCCF() throws ServiceException;

	public ResponseDTO approveRejectOrder(OrderForm orderForm) throws ServiceException;
	
	public ResponseDTO generateOrder(OrderForm orderForm) throws ServiceException;
		
	public List<OrderForm> getAllOrdersByProductIdOrOrderId(OrderForm orderForm, String fromDate, String toDate) throws ServiceException;

	public String getAvailableInventory(long merchantId, long locationId, long productId) throws ServiceException;

	ResponseDTO generateCCF(String[] orderPartnerId, long userId) throws ServiceException;

	public ResponseDTO getCNFileList() throws ServiceException;

	public ResponseDTO uploadCNFiles(String files) throws ServiceException;

	public List<Map<String, Object>> getCnFileStatus() throws ServiceException;


}
