/**
 * 
 */
package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.MerchantDTO;
import com.incomm.cclp.dto.MerchantProductDTO;
import com.incomm.cclp.exception.ServiceException;

public interface MerchantService {

	public void createMerchant(MerchantDTO merchantDto) throws ServiceException;

	public List<MerchantDTO> getAllMerchants();

	public List<MerchantDTO> getMerchantsByName(String merchantName);

	public void updateMerchant(MerchantDTO merchantDto) throws ServiceException;

	public MerchantDTO getMerchantById(Long merchantId);

	public void deleteMerchantById(Long merchantId) throws ServiceException;

	public List<MerchantProductDTO> getMerchantProducts(String merchantName, String productName) throws ServiceException;

	public void assignProductToMerchant(MerchantProductDTO merchantProductDto) throws ServiceException;

	public void removeMerchantProductMapping(MerchantProductDTO merchantProductDto) throws ServiceException;

	List<MerchantProductDTO> getMerchantProductsById(Long merchantId, Long productId) throws ServiceException;

}
