package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.CustomerProfileDTO;
import com.incomm.cclp.exception.ServiceException;

public interface CustomerProfileService {

	void createCustomerProfile(CustomerProfileDTO customerProfileDto) throws ServiceException;

	CustomerProfileDTO getCustomerProfileById(Long profileId);

	List<CustomerProfileDTO> getCustomerProfiles(String accountNumber, String cardNumber, String proxyNumber);

	int updateFeeLimitAttributes(Map<String, Object> inputLimitAttributes, Long profileId, String string) ;

	Map<String, Object> getCardFeeLimitsById(Long profileId, String attributesGroup);

	public void deleteCustomerProfileById(Long profileId) throws ServiceException;

	Map<String, Object> getCardFeeLimitsByType(String type, String value, String attributeGrp) throws ServiceException;

	List<Object[]> getCardsByAccountNumber(String accountNumber);

}
