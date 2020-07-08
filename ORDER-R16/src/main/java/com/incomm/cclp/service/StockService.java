package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;



import com.incomm.cclp.dto.StockDTO;
import com.incomm.cclp.exception.ServiceException;

@SuppressWarnings("rawtypes")
public interface StockService {

	Map<String, String> getAllMerchants() throws ServiceException;
	
	List<Map> getLocationAndProductByMerchantId(String merchantId);

	void createStock(StockDTO stockDto) throws ServiceException;

	void updateStock(StockDTO stockDto) throws ServiceException;

	StockDTO getStockByIds(String merchantId, Long locationId, Long productId);

	List<StockDTO> getStockByMerchantIdAndLocationId(String merchantId, Long locationId);
	

}
