package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.CustomerProfile;
import com.incomm.cclpvms.config.model.CustomerProfileDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface CustomerProfileService {

	public ResponseDTO addMaintenanceFee(Map<String, Object> monthlyFeeCapAttributes, long productId) throws ServiceException;

	public ResponseDTO getMaintenanceFee(long productId) throws ServiceException;

	public ResponseDTO getMonthlyFeeCap(long productId) throws ServiceException;

	public ResponseDTO addMonthlyFeeCap(Map<String, Object> monthlyFeeCapAttributes, long productId) throws ServiceException;

	public ResponseDTO getTxnFees(long productId) throws ServiceException;

	public ResponseDTO addTxnFees(Map<String, Object> txnFeeAttributes, long productId) throws ServiceException;

	public Map<Object, String> getAllParentProducts() throws ServiceException;

	public List<Object> getDeliveryChnlTxns() throws ServiceException;

	public ResponseDTO getLimits(long productId) throws ServiceException;

	public ResponseDTO addLimits(Map<String, Object> limitAttributes, long productId) throws ServiceException;

	public List<CustomerProfileDTO> getCustomerProfileByCardOrAccountOrProxy(CustomerProfile customerProfileForm) throws ServiceException;

	public ResponseDTO addCustomerProfile(CustomerProfile customerProfile) throws ServiceException;

	public CustomerProfileDTO getCustomerProfileById(Long profileID) throws ServiceException;

	public ResponseDTO deleteCustomerProfile(Long profileID);

	public ResponseDTO updateCardAttributes(CustomerProfile customerProfile) throws ServiceException;

	public ResponseDTO getCustomerProfileDetails(String spnumbertype, String spnumber, String string) throws ServiceException;

	public List<Object[]> getCardsByAccountNumber(String accountNumber) throws ServiceException;

}
