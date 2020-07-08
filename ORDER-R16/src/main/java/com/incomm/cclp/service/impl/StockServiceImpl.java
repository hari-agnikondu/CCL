package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.OrderDAO;
import com.incomm.cclp.dao.StockDAO;
import com.incomm.cclp.domain.Merchant;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.domain.Stock;
import com.incomm.cclp.dto.OrderDTO;
import com.incomm.cclp.dto.StockDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.OrderService;
import com.incomm.cclp.service.StockService;
import com.incomm.cclp.util.Util;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	StockDAO stockDao;
	
	@Autowired 
	OrderDAO orderDao;

	@Autowired
	OrderService orderService;
	
	private static final Logger logger = LogManager.getLogger(StockServiceImpl.class);

	@Override
	public Map<String, String> getAllMerchants() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		Map<String, String> merchantMap = new HashMap<>();
		List<Merchant> merchantList = stockDao.getAllMerchants();

		if (merchantList!=null && !CollectionUtils.isEmpty(merchantList)) {

			Iterator<Merchant> iterator = merchantList.iterator();
			while (iterator.hasNext()) {
				Merchant merchant = iterator.next();
				merchantMap.put(merchant.getMerchantId(), merchant.getMerchantName());
			}
			logger.debug("Merchants retrieved successfully merchants -> {}", merchantMap.toString());
		} else
			throw new ServiceException(ResponseMessages.ERR_MERCHANT_NOT_EXISTS, ResponseMessages.DOESNOT_EXISTS);
		logger.debug(CCLPConstants.EXIT);
		return merchantMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getLocationAndProductByMerchantId(String merchantId) {
		logger.debug(CCLPConstants.ENTER);
		Map<Long, String> locationMap = new HashMap<>();
		Map<Long, String> productMap = new HashMap<>();
		List<Map> locationMerchantList = new ArrayList<>();

		List<Object[]> locationsList = stockDao.getLocationAndProductByMerchantId(merchantId);

		if (!CollectionUtils.isEmpty(locationsList)) {

			Iterator<Object[]> iterator = locationsList.iterator();
			while (iterator.hasNext()) {
				Object[] obj = iterator.next();
				locationMap.put(Long.parseLong((String) obj[0]), (String) obj[1]);
				productMap.put(((BigDecimal) obj[2]).longValue(), (String) obj[3]);
			}
			locationMerchantList.add(locationMap);
			locationMerchantList.add(productMap);
			logger.debug("Locations and products retrieved successfully locations and products  -> {}",
					locationMerchantList.toString());
		}
		logger.debug(CCLPConstants.EXIT);
		return locationMerchantList;
	}

	@Override
	public void createStock(StockDTO stockDto) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		Stock stockTemp = stockDao.getStockByIds(stockDto.getMerchantId(), stockDto.getLocationId(),
				stockDto.getProductId());
		stockDto.setCurrInventory((long)0);

		if (stockTemp != null) {
			throw new ServiceException(ResponseMessages.ERR_STOCK_EXISTS, ResponseMessages.ALREADY_EXISTS);
		}

		Long availedInventory = stockDto.getReorderValue() + stockDto.getReorderLevel();

		if (stockDto.getReorderLevel() > stockDto.getMaxInventory()
				|| stockDto.getReorderLevel() > stockDto.getInitialOrder()) {
			throw new ServiceException(ResponseMessages.ERR_REORDER_LEVEL);
		}
		if (stockDto.getMaxInventory() < availedInventory) {
			throw new ServiceException(ResponseMessages.ERR_REORDER_VALUE);
		}
		java.lang.reflect.Type targetListType = new TypeToken<Stock>() {
		}.getType();
		Stock stock = mm.map(stockDto, targetListType);

		
		
		stock.setCurrInventory((long) 0);
		stock.setInsUser();
		stock.setLastUpdUser(stockDto.getInsUser());
		stock.setInsDate();
		stock.setLastUpdDate();
		logger.debug("Creating Stock stock {}", stock.toString());
		
		stockDao.createStock(stock);
		 insertOrderRecord(stockDto);
		
		

	}

	private void insertOrderRecord(StockDTO stockDto) throws ServiceException {
		OrderDTO orderDto = new OrderDTO();
		Map<String, Map<String, Object>> productAttributes = new HashMap<>();
		
		orderDto.setProductId(stockDto.getProductId());
		orderDto.setMerchantId(stockDto.getMerchantId());
		orderDto.setLocationId(stockDto.getLocationId());
		orderDto.setQuantity(String.valueOf(stockDto.getInitialOrder()));
		orderDto.setInsUser(stockDto.getInsUser());
		Product product = orderDao.getProductById(orderDto.getProductId());
		
		
	     if(!Util.isEmpty(product.getAttributes())) {
			try {
				productAttributes = Util.jsonToMap(product.getAttributes());
			} catch (Exception e) {
				logger.error(e);
			}
			Map<String, Object> attributes = productAttributes.get("Product");
			String defaultPackageId =String.valueOf(attributes.get("defaultPackage")) ;
			
			orderDto.setPackageId(defaultPackageId);
			orderDto.setOrderId(generateRandomOrderId());
	     }
		try {
			orderService.createOrder(orderDto);
		} catch (ServiceException e) {
			 throw new ServiceException(ResponseMessages.ERR_INSERTING_ORDER_FOR_STOCK,ResponseMessages.ALREADY_EXISTS);
		}
		
	}
	
	public String generateRandomOrderId() {
		  int leftLimit = 97; 
		    int rightLimit = 122;
		    int targetStringLength = 9;
		    Random random = new Random();
		    StringBuilder buffer = new StringBuilder(targetStringLength);
		    for (int i = 0; i < targetStringLength; i++) {
		        int randomLimitedInt = leftLimit + (int) 
		          (random.nextFloat() * (rightLimit - leftLimit + 1));
		        buffer.append((char) randomLimitedInt);
		    }
		    String generatedString = buffer.toString();
			return "RTL"+generatedString;
	}

	@Override
	public StockDTO getStockByIds(String merchantId, Long locationId, Long productId) {
		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		Stock stock = stockDao.getStockByIds(merchantId, locationId, productId);

		if (stock == null) {
			logger.debug("No stock found for merchantId: {}, locationId {} productId {}", merchantId, locationId,
					productId);
			return null;
		}

		java.lang.reflect.Type targetListType = new TypeToken<StockDTO>() {
		}.getType();
		StockDTO stockDto = mm.map(stock, targetListType);
		logger.debug(CCLPConstants.EXIT);
		return stockDto;
	}

	@Override
	public void updateStock(StockDTO stockDto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		Stock stockTemp = stockDao.getStockByIds(stockDto.getMerchantId(), stockDto.getLocationId(),
				stockDto.getProductId());
		if (stockTemp == null) {

			throw new ServiceException(ResponseMessages.ERR_STOCK_NOT_EXISTS, ResponseMessages.DOESNOT_EXISTS);
		}
		Long availedInventory = stockDto.getReorderValue() + stockDto.getReorderLevel();

		if (stockDto.getReorderLevel() > stockDto.getMaxInventory()
				|| stockDto.getReorderLevel() > stockDto.getInitialOrder()) {
			throw new ServiceException(ResponseMessages.ERR_REORDER_LEVEL);
		}
		if (stockDto.getMaxInventory() < availedInventory) {
			throw new ServiceException(ResponseMessages.ERR_REORDER_VALUE);
		}

		
		Stock stock = mm.map(stockDto, Stock.class);
		stock.setLastUpdDate();
		stock.setLastUpdUser(548);
		stock.setCurrInventory(stockTemp.getCurrInventory());

		stockDao.updateStock(stock);
		logger.debug(CCLPConstants.EXIT);

	}

	@Override
	public List<StockDTO> getStockByMerchantIdAndLocationId(String merchantId, Long locationId) {
		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<StockDTO>>() {
		}.getType();
		logger.debug(CCLPConstants.EXIT);
		return mm.map(stockDao.getStockByMerchantIdAndLocationId(merchantId, locationId), targetListType);
	}

}
