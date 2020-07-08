package com.incomm.cclpvms.config.service;

import java.util.List;

import com.incomm.cclpvms.config.model.Merchant;
import com.incomm.cclpvms.config.model.MerchantDTO;
import com.incomm.cclpvms.config.model.MerchantProduct;
import com.incomm.cclpvms.config.model.MerchantProductDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.MerchantException;
import com.incomm.cclpvms.exception.ServiceException;

public interface MerchantService {
public ResponseDTO createMerchant(MerchantDTO merchantDTO)  throws ServiceException;
	
	public List<MerchantDTO> getAllMerchants() throws ServiceException, MerchantException;
	
	public ResponseDTO updateMerchant(MerchantDTO merchantDTO) throws ServiceException;
	
	public ResponseDTO deleteMerchant(Long merchantId) throws ServiceException;
	
	public Merchant getMerchantById(Long merchantId) throws ServiceException;
	
	public List<MerchantDTO> getMerchantsByName(String merchantName) throws ServiceException;

	public List<MerchantProductDTO> getAllMerchantsLinkedWithProducts(MerchantProduct merchantToProductForm) throws ServiceException;
	
	public ResponseDTO updateAssignProductToMerchant(MerchantProductDTO merchantProductDTO) throws ServiceException;
	
	public ResponseDTO deleteProductMerchant(MerchantProductDTO merchantProductDTO) throws ServiceException;

	public List<MerchantProductDTO> getMerchantProductId(Long merchantId, Long productId) throws ServiceException;
}
