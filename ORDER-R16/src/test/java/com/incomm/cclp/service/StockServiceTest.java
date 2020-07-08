package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.impl.MetaDataDAOImpl;
import com.incomm.cclp.dto.StockDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StockServiceTest {

	@Autowired
	StockService stockService;

	@Autowired
	MetaDataDAOImpl daoImpl;

	
	/*
	 * Getting all the merchant returning empty list
	 */
	@Test
	public void testGetAllMerchants_EmptyList()  {

		@SuppressWarnings("unused")
		Map<String, String> merchants;
		try {
			merchants = stockService.getAllMerchants();
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), "STOCK005");
		}

		

	}
	
	/*
	 * Getting all the location and Products by Existing MerchantId
	 */
	@Test
	public void get_locationAndProduct_by_merchantId() {
		daoImpl.insertStockMetaData();

		@SuppressWarnings("rawtypes")
		List<Map> location_productList = stockService.getLocationAndProductByMerchantId("1");
		assertEquals(2, location_productList.size());
	}

	/*
	 * Getting all the existing merchants
	 */
	@Test
	public void testGetAllMerchants() throws ServiceException {

		Map<String, String> merchants = stockService.getAllMerchants();

		assertEquals(2, merchants.size());

	}

	/*
	 * Trying to get the all non existing Merchants list
	 * It will throw Exception
	 */
	@Test
	public void testGetAllNonExistingMercahnts() throws ServiceException {

		try {
			@SuppressWarnings("unused")
			Map<String, String> merchants = stockService.getAllMerchants();
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.ERR_MERCHANT_NOT_EXISTS);
		}
	}

	/*
	 * Creating a Stock with all the Existing Values
	 */
	@Test
	public void testCreateStock() throws ServiceException {
		StockDTO stockDto = new StockDTO();
		stockDto.setMerchantId("1");
		stockDto.setLocationId(1);
		stockDto.setProductId(2351);
		stockDto.setAutoReplenish("Y");
		stockDto.setInitialOrder((long) 10);
		stockDto.setReorderLevel((long) 5);
		stockDto.setReorderValue((long) 10);
		stockDto.setMaxInventory((long) 1940);
		stockDto.setInsUser(1);
		stockService.createStock(stockDto);

		assertNotNull(stockService.getStockByIds(stockDto.getMerchantId(), stockDto.getLocationId(),
				stockDto.getProductId()));
	}

	/*
	 * Trying to create a Stock with Duplication values
	 * 
	 * It will throw ServiceException
	 */
	@Test
	public void testCreateStockDuplicate() throws ServiceException {
		StockDTO stockDto = new StockDTO();
		stockDto.setMerchantId("1");
		stockDto.setLocationId(1);
		stockDto.setProductId(2351);
		stockDto.setAutoReplenish("Y");
		stockDto.setInitialOrder((long) 10);
		stockDto.setReorderLevel((long) 5);
		stockDto.setReorderValue((long) 10);
		stockDto.setMaxInventory((long) 1940);
		stockDto.setInsUser(1);
		stockService.createStock(stockDto);

		try {
			stockService.createStock(stockDto);
		} catch (ServiceException se) {
			assertEquals(ResponseMessages.ERR_STOCK_EXISTS, se.getMessage());
		}
	}

	/*
	 * Getting the Stock By Existing merchantId, LocationId and productId
	 */
	@Test
	public void testSearchExistingStockByIds() throws ServiceException {

		StockDTO stockDto = constructStockDto();
		stockService.createStock(stockDto);

		StockDTO stockDtoResp = stockService.getStockByIds(stockDto.getMerchantId(), stockDto.getLocationId(),
				stockDto.getProductId());

		assertNotNull(stockDtoResp);
	}

	/*
	 * Trying to get non-Existing Stock by Id
	 */
	@Test
	public void searchNonExistingParnterById() throws ServiceException {

		String merchantId = "999";
		Long locationId = (long) 999;
		Long productId = (long) 999;
		assertNull(stockService.getStockByIds(merchantId, locationId, productId));

	}

	/*
	 *Updating the Stock with non-Existing Stock 
	 */
	@Test
	public void testupdateStock_with_existingStock() throws ServiceException {

		StockDTO stockDto = constructStockDto();
		stockService.createStock(stockDto);
		stockDto.setMerchantId("999");
		stockDto.setLocationId(999);
		stockDto.setProductId(999);
		try {
			stockService.updateStock(stockDto);
		} catch (ServiceException se) {
			assertEquals(ResponseMessages.ERR_STOCK_NOT_EXISTS, se.getMessage());
		}

	}

	/*
	 * Updating the Stock with already Existing Stock
	 */
	@Test
	public void testupdateStock() throws ServiceException {

		StockDTO stockDto = constructStockDto();
		stockService.createStock(stockDto);
		stockDto.setMaxInventory((long) 999);

		stockService.updateStock(stockDto);

		StockDTO stockDtoResp = stockService.getStockByIds(stockDto.getMerchantId(), stockDto.getLocationId(),
				stockDto.getProductId());
		assertEquals(new Long(999), stockDtoResp.getMaxInventory());

	}

	/*
	 * Getting the StockDTO by using merchantId and LocationId
	 */
	@Test
	public void testGetStockByMerchantIdAndLocationId() throws ServiceException {

		StockDTO stockDto = constructStockDto();
		stockService.createStock(stockDto);

		StockDTO stockDto1 = constructStockDto();
		stockDto1.setProductId(2);
		stockService.createStock(stockDto1);

		List<StockDTO> getStockList = stockService.getStockByMerchantIdAndLocationId("1", (long) 1);
		assertEquals(2, getStockList.size());

	}

	/*
	 * Constructing a Stock for Test case purpose
	 */
	public StockDTO constructStockDto() {
		StockDTO stockDto = new StockDTO();
		stockDto.setMerchantId("1");
		stockDto.setLocationId(1);
		stockDto.setProductId(2351);
		stockDto.setAutoReplenish("Y");
		stockDto.setInitialOrder((long) 10);
		stockDto.setReorderLevel((long) 5);
		stockDto.setReorderValue((long) 10);
		stockDto.setMaxInventory((long) 1940);
		stockDto.setInsUser(1);
		return stockDto;
	}

	/*
	 *  Get the location and product by non-existing merchantId
	 *  
	 *  It will return empty List as Result
	 */
	@Test
	public void get_locationAndProduct_by_nonExisting_merchantId() {

		@SuppressWarnings("rawtypes")
		List<Map> location_productList = stockService.getLocationAndProductByMerchantId("5");
		assertEquals(0, location_productList.size());
	}

	/*
	 * Getting the Stock by providing positive MerchantId and positive LocationId
	 */
	@Test
	public void getStockBy_MerchantId_and_LocationId() {

		List<StockDTO> stockList = stockService.getStockByMerchantIdAndLocationId("1", (long) 1);
		assertEquals(0, stockList.size());
	}

	
	/*
	 * Getting the Stock by providing only LocationId
	 */
	@Test
	public void getStockBy_Negative_MerchantId_and_LocationId() {

		List<StockDTO> stockList = stockService.getStockByMerchantIdAndLocationId(" ", (long) 1);
		assertEquals(0, stockList.size());
	}

	
	/*
	 * Getting the Stock by providing only MerchantId
	 */
	@Test
	public void getStockBy_MerchantId_and_Negative_LocationId() {

		List<StockDTO> stockList = stockService.getStockByMerchantIdAndLocationId("1", (long) -1);
		assertEquals(0, stockList.size());
	}

	/*
	 * Getting the Stock without providing MercahntId and LocationId
	 */
	@Test
	public void getStockBy_Negative_MerchantId_and_Negative_LocationId() {

		List<StockDTO> stockList = stockService.getStockByMerchantIdAndLocationId(" ", (long) -1);
		assertEquals(0, stockList.size());
	}

}
