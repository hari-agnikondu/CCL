package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.dto.CustomerProfileDTO;
import com.incomm.cclp.exception.ServiceException;

public interface CustomerProfileDAO {

	int createCustomerProfile(CustomerProfileDTO customerProfileDto, String attributes);

	Object getCustomerProfileById(Long profileId);

	int checkExistingProfileCode(String proxyNumber);

	List<Object[]> getCustomerProfiles(String accountNumber, String cardNumber, String proxyNumber);

	int updateCardAttributes(String attributesJsonResp, Long profileId);

	int deleteCustomerProfileById(Long profileId);

	Object getAttributesByType(String type, String value, String attributeGrp) throws ServiceException;

	List<Object[]> getCardsByAccountNumber(String accountNumber);

}
