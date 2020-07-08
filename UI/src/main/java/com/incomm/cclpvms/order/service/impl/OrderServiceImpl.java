package com.incomm.cclpvms.order.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.order.model.OrderForm;
import com.incomm.cclpvms.order.service.OrderService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class OrderServiceImpl implements OrderService{


    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate ;
		
	@Value("${ORDER_BASE_URL}")
	String ORDER_BASE_URL;
	
	@Value("${JOB_BASE_URL}")
	String JOB_BASE_URL;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getStoresByMerchantId(Long merchantId) throws ServiceException {
		ResponseDTO responseBody = null;
		Map<String, String>  storeMap ;
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(ORDER_BASE_URL + "/orders/{merchantId}/locations",
					ResponseDTO.class,merchantId);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch Store id from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			
			storeMap = (Map<String, String>) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching stores by merchant id{}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return storeMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getMerchantsAndPackages(Long productId) throws ServiceException {
		ResponseDTO responseBody = null;
		Map<String,Object>   merchantPckgMap ;
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(ORDER_BASE_URL + "/orders/{productId}/getMerchantsAndPackageIdsByProductId",
					ResponseDTO.class,productId);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch Store id from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			
			merchantPckgMap = ( Map<String,Object> ) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching merchants and packages by product id{}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return merchantPckgMap;
	}

	
	@Override
	public ResponseDTO saveOrder(OrderForm orderForm) throws ServiceException {
		logger.debug("ENTER");
		ResponseDTO responseBody = null;
		try {
			logger.debug("Calling '{}' service to place new Order Record",ORDER_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(ORDER_BASE_URL + "/orders", orderForm,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while creating new order, {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderForm>  getAllOrdersForApproval() throws ServiceException {
		ResponseDTO responseBody = null;
		List<OrderForm>   orderList ;
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(ORDER_BASE_URL + "/orders/",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch all orders for approval from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			 orderList = ( List<OrderForm> ) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching all orders for approval ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return orderList;
	}

	@Override
	public ResponseDTO approveRejectOrder(OrderForm orderForm) throws ServiceException {
		logger.debug(CCLPConstants.ENTER+orderForm);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<OrderForm> requestEntity = new HttpEntity<>(orderForm, headers);
			logger.debug("Calling {} service to approve or reject order ",ORDER_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(ORDER_BASE_URL + "/orders/status", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating order {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderForm> getAllOrdersByProductIdOrOrderId(OrderForm orderForm,String fromDate,String toDate) throws ServiceException {
		ResponseDTO responseBody = null;
		List<OrderForm>   orderList ;
		String orderId = orderForm.getOrderId()!=null && orderForm.getOrderId().equals("")?"-1" :orderForm.getOrderId();
		String status = orderForm.getStatus()!=null && orderForm.getStatus().equals("")?"-1" :orderForm.getStatus().toUpperCase();
		
		Long productId = orderForm.getProductId();
		
		
		/*orderForm.getOrderId()+"/"+orderForm.getProductId()*/
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(ORDER_BASE_URL + "/orders/searchproduct/{orderId}/{productId}/{status}/{fromDate}/{toDate}",
					ResponseDTO.class,orderId,productId,status,fromDate,toDate);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch orders from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			 orderList = ( List<OrderForm> ) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching orders for status ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return orderList;
	}

	
	@Override
	public String getAvailableInventory(long merchantId, long locationId, long productId) throws ServiceException {
		ResponseDTO responseBody = null;
		String availInventory  ;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(ORDER_BASE_URL + "/orders/{productId}/{merchantId}/{locationId}/availableInventory",
					ResponseDTO.class,productId,merchantId,locationId);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch available inventory from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			
			availInventory = ( String) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching AvailableInventory by merchantId and locationId{}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return availInventory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderForm> getAllOrdersForOrder() throws ServiceException {
		ResponseDTO responseBody = null;
		List<OrderForm>   orderList ;
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(ORDER_BASE_URL + "/orders/order",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch all orders for order Process from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			 orderList = ( List<OrderForm> ) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching all orders for order Process ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return orderList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderForm> getAllOrdersForCCF() throws ServiceException {
		ResponseDTO responseBody = null;
		List<OrderForm>   orderList ;
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(ORDER_BASE_URL + "/orders/CCF",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch all orders for CCF process from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			 orderList = ( List<OrderForm> ) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching all orders for ccf process ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return orderList;
	}

	@Override
	public ResponseDTO generateOrder(OrderForm orderForm) throws ServiceException {
		logger.debug(CCLPConstants.ENTER+orderForm);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<OrderForm> requestEntity = new HttpEntity<>(orderForm, headers);
			logger.debug("Calling '{}' service to approve or reject order ",JOB_BASE_URL);
			for (String orderPartnerIds : orderForm.getOrderPartnerId()) {
				if(orderPartnerIds != null) {
					
				String[] orderAndPartnerId = orderPartnerIds.split("~");
				logger.debug(orderAndPartnerId[0]+"/"+orderAndPartnerId[1]);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(JOB_BASE_URL + "/schedulers/"+ orderAndPartnerId[0]+"/"+orderAndPartnerId[1]+"/"+orderForm.getPartnerId(), HttpMethod.GET,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
				}
		}
		} catch (RestClientException e) {
			logger.error("Exception while order processing {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO generateCCF(String[] orderPartnerId,long userId) throws ServiceException {

		
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String[]> requestEntity = new HttpEntity<>(orderPartnerId,headers);
			logger.debug("Calling '{}' service to approve or reject order ",JOB_BASE_URL);
			
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(JOB_BASE_URL + "/ccfGeneration/{userId}", HttpMethod.PUT,
					requestEntity, ResponseDTO.class,userId);
			responseBody = responseEntity.getBody();
			 
			 logger.debug("CCF Generation started.....");
				
		} catch (RestClientException e) {
			logger.error("Exception while updating order {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO getCNFileList() throws ServiceException {
		ResponseDTO responseBody = null;
		
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(JOB_BASE_URL + "cnFileUpload/listCnFiles",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
					
		} catch (RestClientException e) {
			logger.error("Exception while fetching CN File List ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO uploadCNFiles(String files) throws ServiceException{
		logger.debug(CCLPConstants.ENTER+files);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(files, headers);
			logger.debug("Calling '{}' service to upload CN Files ",JOB_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(JOB_BASE_URL + "cnFileUpload/runJob", HttpMethod.PUT,requestEntity,ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while uploading CN Files {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getCnFileStatus() throws ServiceException {
		ResponseDTO responseBody = null;
		List<Map<String, Object>>   cnFileList =null;
	
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(JOB_BASE_URL + "cnFileUpload/status",ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch CN file's status from order service");
				throw new ServiceException(responseBody.getMessage() );
			}
			cnFileList = ( List<Map<String, Object>> ) responseBody.getData();
				
		} catch (RestClientException e) {
			logger.error("Exception while fetching Cn File's status  ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return cnFileList;
	}
	
}
