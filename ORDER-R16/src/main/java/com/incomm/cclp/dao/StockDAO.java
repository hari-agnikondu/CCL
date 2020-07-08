package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.Stock;

public interface StockDAO {

	List<Merchant> getAllMerchants();

	List<Object[]> getLocationAndProductByMerchantId(String merchantId);

	void createStock(Stock stock);

	void updateStock(Stock stock);

	Object getStockByMerchantIdAndLocationId(String merchantId, Long locationId);

	Stock getStockByIds(String merchantId, Long locationId, Long productId);

}

