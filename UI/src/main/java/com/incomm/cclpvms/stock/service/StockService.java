package com.incomm.cclpvms.stock.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.stock.model.StockDTO;

public interface StockService {

	public ResponseDTO saveStock(StockDTO stockForm) throws ServiceException;
	public Map<String, String> getAllMerchants()  throws ServiceException;
	public List<Map<String, String>> getStoresAndProductsByMerchantId(Long merchantid)  throws ServiceException;
	public Map<String, String> getAllLocations()  throws ServiceException;
	public ResponseDTO getStockByMerchantLocationAndProduct(StockDTO stockForm)  throws ServiceException;
	public ResponseDTO updateStock( StockDTO stockForm) throws ServiceException;
	List<StockDTO> getAllStocksByMerchantAndLocation(StockDTO stockDTO) throws ServiceException;
}
