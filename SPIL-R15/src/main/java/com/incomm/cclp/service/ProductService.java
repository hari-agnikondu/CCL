package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.SupportedPurse;

public interface ProductService {

	public Map<String, Map<String, Object>> getProductAttributes(String productId, String purseId) throws ServiceException;

	public String getProductUPC(ValueDTO valueDto) throws ServiceException;

	public boolean checkProductValidity(ValueDTO valueDto);

	public void updateCardStatus(String valueOf, String expiredProductCardStatus) throws ServiceException;

	public String getProductIdUsingUPC(String upc) throws ServiceException;

	public String getProductType(ValueDTO valueDto);

	public List<String> getPackageIdsByProductId(String productId) throws ServiceException;

	public List<SupportedPurse> getSupportedPurseDtls(ValueDTO valueDto);

	public String getPartnerDetails(ValueDTO valueDto);

	public String getProductDefaultCurr(ValueDTO valueDto);

	public String getMdmId(String partnerId);

	public void getPurseDetails(ValueDTO valueDto);

}
