package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.CustomerProfile;
import com.incomm.cclpvms.config.model.CustomerProfileDTO;
import com.incomm.cclpvms.config.model.DeliveryChannel;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.CustomerProfileService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {

	private static final Logger logger = LogManager.getLogger(CustomerProfileServiceImpl.class.getName());

	@Autowired
	RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	String CONFIG_BASE_URL;

	@Autowired
	SessionService sessionService;

	String customerProfileBaseURL = "/customerProfile";

	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerProfileDTO> getCustomerProfileByCardOrAccountOrProxy(CustomerProfile customerProfileForm) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<CustomerProfileDTO> customerProfileList = new ArrayList<>();

		String accountNumber = Util.isEmpty(customerProfileForm.getAccountNumber()) ? "-1": customerProfileForm.getAccountNumber();
		String cardNumber = Util.isEmpty(customerProfileForm.getCardNumber()) ? "-1": customerProfileForm.getCardNumber();
		String proxyNumber = Util.isEmpty(customerProfileForm.getProxyNumber()) ? "-1": customerProfileForm.getProxyNumber();
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
					CONFIG_BASE_URL + customerProfileBaseURL
							+ "/searchCustomerProfile/{accountNumber}/{cardNumber}/{proxyNumber}",
					ResponseDTO.class, accountNumber, cardNumber, proxyNumber);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call is : " + responseBody.getResult());
			if (!responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.error("Failed to Fetch customer profile from config service");
				throw new ServiceException(responseBody.getMessage() );
			}
			customerProfileList = ( List<CustomerProfileDTO>) responseBody.getData();
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching customer profile details",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return customerProfileList;
		
	}
	
	
	@Override
	public ResponseDTO addCustomerProfile(CustomerProfile customerProfile) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			Map<String,Map<String,Object>> attributeMap=new  HashMap<>(); 
			attributeMap.put("Card",customerProfile.getCardAttributes());
			CustomerProfileDTO customerProfiledto=new CustomerProfileDTO();
			customerProfiledto.setAccountNumber(customerProfile.getAccountNumber());
			customerProfiledto.setProxyNumber(customerProfile.getProxyNumber());
			customerProfiledto.setInsUser(customerProfile.getInsUser());
            customerProfiledto.setAttributesMap(attributeMap);			
			
			logger.debug("Calling '{}' service to add customer Profile",CONFIG_BASE_URL);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<CustomerProfileDTO> requestEntity = new HttpEntity<>(customerProfiledto, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL+customerProfileBaseURL, HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while adding customer profile",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		catch(Exception e)
		{
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	

	@Override
	public CustomerProfileDTO getCustomerProfileById(Long profileID) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		CustomerProfileDTO customerProfileDTO=new CustomerProfileDTO();
		ObjectMapper omMapper = new ObjectMapper();
		
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + customerProfileBaseURL	+ "/{profileId}",
					                                     ResponseDTO.class, profileID);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call " , responseBody.getResult());
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch customer profile by profile Id from config service");
				throw new ServiceException(responseBody.getMessage() );
			}
			customerProfileDTO =omMapper.convertValue(responseBody.getData(),CustomerProfileDTO.class);
			
			logger.debug(customerProfileDTO.toString());
		} catch (RestClientException e) {
			logger.error("Exception while fetching customer profiles",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return customerProfileDTO;
	}

	@Override
	public ResponseDTO deleteCustomerProfile(Long profileID) {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		logger.debug("Calling '{}' service to delete customer profile '{}'",CONFIG_BASE_URL,profileID);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + customerProfileBaseURL	+ "/{profileId}",
				HttpMethod.DELETE, null, ResponseDTO.class, profileID);
		responseBody = responseEntity.getBody();
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	
	
	@Override
	public ResponseDTO updateCardAttributes(CustomerProfile customerProfile) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			logger.debug("Calling '{}' service to update card Attributes ",CONFIG_BASE_URL);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(customerProfile.getCardAttributes(), headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL+customerProfileBaseURL+"/{profileId}/card", HttpMethod.PUT,
					requestEntity, ResponseDTO.class,customerProfile.getProfileId());
			responseBody = responseEntity.getBody();
			
		} catch (RestClientException e) {
			logger.error("Exception while updating role {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	
	/*Copy customer profile*/
	@Override
	public ResponseDTO getCustomerProfileDetails(String type,String value,String attributeGrp) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {

			logger.debug("Calling '{}' service to get customer profile details for copy '{}'",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
					CONFIG_BASE_URL + customerProfileBaseURL+ "/{attributeGrp}/{type}/{value}",	ResponseDTO.class, attributeGrp, type, value);
			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
		} catch (RestClientException e) {
			logger.error("Exception while fetching customer profile ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCardsByAccountNumber(String accountNumber) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<Object[]> cardList = new ArrayList<>();

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
					CONFIG_BASE_URL + customerProfileBaseURL
							+ "/{accountNumber}/cards",
					ResponseDTO.class, accountNumber);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (!responseBody.getResult().equalsIgnoreCase("SUCCESS")) {
				logger.error("Failed to Fetch customer profile from config service");
				throw new ServiceException(responseBody.getMessage() );
			}
			cardList = ( List<Object[]>) responseBody.getData();
			logger.debug("cardList"+cardList);
			
		} catch (RestClientException e) {
			logger.error("Exception while fetching customer profile ",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return cardList;
	}
	
	/* Limit tab starts */
 
    
	public ResponseDTO addLimits(Map<String, Object> limitAttributes, long profileId) throws ServiceException {

		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(limitAttributes, headers);
			logger.debug("Calling '{}' service to update customer limits", CONFIG_BASE_URL);
			logger.debug("addLimits" + limitAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					CONFIG_BASE_URL+customerProfileBaseURL+"/{profileId}/limits", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					profileId);

			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating customer limits {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return responseBody;
	}

	
	public ResponseDTO getLimits(long profileId) throws ServiceException {

		ResponseDTO responseBody = null;
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL +customerProfileBaseURL+"/{profileId}/limits", ResponseDTO.class, profileId);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseBody;
	}

	@SuppressWarnings("unchecked")
	
	public List<Object> getDeliveryChnlTxns() throws ServiceException {

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;

		List<List<Object>> deliveryChnlTxns = null;
		List<DeliveryChannel> delChnlList = new ArrayList<>();
		List<Object> txnDtlsList = new ArrayList<>();
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL + "/products/transactionsList", ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase("success")) {
				deliveryChnlTxns = mm.map(responseBody.getData(), List.class);

				List<Object> list = new ArrayList<>();
				Map<String, String> txnFinancialFlag = new HashMap<>();
				Map<String, String> txnERIFFlag = new HashMap<>();
				Map<String, String> txnMRIFFlag = new HashMap<>();
				Map<String, String> txndesc = new HashMap<>();
				Map<String, String> dlctxn = new HashMap<>();

				Iterator<List<Object>> chnlIterator = deliveryChnlTxns.iterator();
				while (chnlIterator.hasNext()) {
					List<Object> objAry = chnlIterator.next();

					if (list.contains(objAry.get(0)))
						continue;

					Map<String, String> txnMap = new HashMap<>();
					Iterator<List<Object>> chnlIterator2 = deliveryChnlTxns.iterator();
					while (chnlIterator2.hasNext()) {
						List<Object> objAry1 = chnlIterator2.next();
						if (objAry1.get(0).equals(objAry.get(0)) && !objAry1.get(3).equals(objAry.get(3))) {
							txnMap.put((String) objAry1.get(3), (String) objAry1.get(4));
							txnERIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(6));
							txnMRIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(7));
							txndesc.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(9));
							dlctxn.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(8));
							list.add(objAry1.get(0));

						}
						txnFinancialFlag.put((String) objAry1.get(3), (String) objAry1.get(5));
					}
					txnMap.put((String) objAry.get(3), (String) objAry.get(4));
					txnERIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(6));
					txnMRIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(7));
					txndesc.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(9));
					dlctxn.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(8));

					DeliveryChannel delChnl = new DeliveryChannel((String) objAry.get(0), (String) objAry.get(1),
							(String) objAry.get(2), txnMap);
					delChnlList.add(delChnl);
					logger.debug(delChnl);
				}

				Collections.sort(delChnlList);
				logger.debug(delChnlList);

				txnDtlsList.add(delChnlList);
				txnDtlsList.add(txnFinancialFlag);
				txnDtlsList.add(txnERIFFlag);
				txnDtlsList.add(txnMRIFFlag);
				txnDtlsList.add(dlctxn);
				txnDtlsList.add(txndesc);
			} else
				logger.error("Failed to Fetch Delivery Channels from config srvice");
		} catch (RestClientException e) {

			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return txnDtlsList;
	}

	@SuppressWarnings("unchecked")
	
	public Map<Object, String> getAllParentProducts() throws ServiceException {

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;
		Map<Object, String> productMap = null;

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL + "/products/parentProductList", ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase("success")) {
				Map<Object, String> parentProductMap = mm.map(responseBody.getData(), Map.class);
				productMap = parentProductMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
				logger.debug(parentProductMap);
			} else
				logger.error("Failed to Fetch LimitAttributes from config srvice");
		} catch (RestClientException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return productMap;
	}
	/* Limit tab ends */

	/* TxnFee Tab Starts */
	
	public ResponseDTO addTxnFees(Map<String, Object> txnFeeAttributes, long profileId) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(txnFeeAttributes, headers);
			logger.debug("Calling '{}' service to update TxnFees", CONFIG_BASE_URL);

			logger.debug("addTxnFees" + txnFeeAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					CONFIG_BASE_URL +customerProfileBaseURL+ "/{profileId}/txnFees", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					profileId);

			responseBody = responseEntity.getBody();
			logger.info("responseBody " + responseBody);
		} catch (RestClientException e) {
			logger.error("Exception while updating TxnFees {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	
	public ResponseDTO getTxnFees(long productId) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL +customerProfileBaseURL+"/{profileId}/txnFees", ResponseDTO.class, productId);
			responseBody = responseEntity.getBody();
			logger.debug("responseBody " + responseBody);

		} catch (RestClientException e) {
			logger.error("Failed to Fetch transaction fee Attributes from config srvice " + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	/* Txn Fees Tab Ends */
	/* Monthly Fee Cap Tab Starts */
	
	public ResponseDTO addMonthlyFeeCap(Map<String, Object> monthlyFeeCapAttributes, long profileId)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(monthlyFeeCapAttributes, headers);
			logger.debug("Calling '{}' service to update Monthly Fee Cap", CONFIG_BASE_URL);

			logger.debug("addmonthlyFeeCapAttributes " + monthlyFeeCapAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					CONFIG_BASE_URL+customerProfileBaseURL + "/{profileId}/monthlyFeeCap", HttpMethod.PUT, requestEntity,
					ResponseDTO.class, profileId);

			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating Monthly Fee Cap {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	
	public ResponseDTO getMonthlyFeeCap(long profileId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ResponseEntity<ResponseDTO> responseEntity = null;
		try {

			responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+ customerProfileBaseURL + "/{profileId}/monthlyFeeCap",
					ResponseDTO.class, profileId);
			logger.debug(responseEntity.getBody());

		} catch (RestClientException e) {
			logger.error("Failed to Fetch Monthly Fee Cap Attributes from config srvice " + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseEntity.getBody();
	}

	/* Monthly Fee Cap Tab Ends */
	/* Maintenance Fee Tab Starts */
	public ResponseDTO addMaintenanceFee(Map<String, Object> maintenanceFeeAttributes, long profileId)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(maintenanceFeeAttributes, headers);
			logger.debug("Calling '{}' service to update maintenance Fee Attributes", CONFIG_BASE_URL);

			logger.debug("add maintenanceFeeAttributes " + maintenanceFeeAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					CONFIG_BASE_URL+customerProfileBaseURL+"/{profileId}/maintenanceFee", HttpMethod.PUT, requestEntity,
					ResponseDTO.class, profileId);

			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating MaintenanceFee {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	
	public ResponseDTO getMaintenanceFee(long profileId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ResponseEntity<ResponseDTO> responseEntity = null;
		try {

			responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL+customerProfileBaseURL+"/{profileId}/maintenanceFee",
					ResponseDTO.class, profileId);
			logger.debug(responseEntity.getBody());

		} catch (RestClientException e) {
			logger.error("Failed to Fetch MaintenanceFee Attributes from config srvice " + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseEntity.getBody();
	}

	/* Maintenance Fee Tab Ends */

}
