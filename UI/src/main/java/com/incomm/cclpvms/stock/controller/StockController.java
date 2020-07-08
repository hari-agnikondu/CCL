package com.incomm.cclpvms.stock.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.stock.model.StockDTO;
import com.incomm.cclpvms.stock.service.StockService;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Controller
@RequestMapping("/config/stocks")
public class StockController {

    private static final Logger logger = LogManager.getLogger(StockController.class);
	
	@Autowired
	private StockService stockService;
	
	@Value("${INS_USER}")
	long userId;
		
	@PreAuthorize("hasRole('SEARCH_STOCK')")
	@RequestMapping("/stockConfig")
	public ModelAndView stockConfig() throws ServiceException {
		ModelAndView mav = new ModelAndView(CCLPConstants.STOCK_CONFIG);
		Map<String,String> merchantMap   = stockService.getAllMerchants();
		mav.addObject(CCLPConstants.MERCHANT_MAP,merchantMap);
		
		Map<String,String> locationMap   = stockService.getAllLocations();
		mav.addObject(CCLPConstants.LOCATION_MAP,locationMap);
		mav.addObject(CCLPConstants.STOCK_FORM,new StockDTO());
		return mav;
	}
	
	@PreAuthorize("hasRole('ADD_STOCK')")
	@RequestMapping("/defineStock")
	public ModelAndView defineStock() throws ServiceException {
		ModelAndView mav = new ModelAndView("defineStock");
		Map<String,String> merchantMap   = stockService.getAllMerchants();
		mav.addObject(CCLPConstants.MERCHANT_MAP,merchantMap);
		mav.addObject(CCLPConstants.STOCK_FORM,new StockDTO());
		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_STOCK')")
	@RequestMapping("/searchStock")
    public ModelAndView searchStock( @ModelAttribute("stockForm") StockDTO stockForm) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView(CCLPConstants.STOCK_CONFIG);
		List<StockDTO> stockList= stockService.getAllStocksByMerchantAndLocation(stockForm);
		mav.addObject("stockForm", stockForm);
		mav.addObject("showGrid","true");
		mav.addObject("stockList",stockList);
		Map<String,String> merchantMap   = stockService.getAllMerchants();
		mav.addObject(CCLPConstants.MERCHANT_MAP,merchantMap);
		
		Map<String,String> locationMap   = stockService.getAllLocations();
		mav.addObject(CCLPConstants.LOCATION_MAP,locationMap);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('EDIT_STOCK')")
	@RequestMapping("/showEditStock")
	public ModelAndView showEditStock( @ModelAttribute("stockForm") StockDTO stockForm) throws ServiceException {
		ModelAndView mav = new ModelAndView(CCLPConstants.EDIT_STOCK);
		Map<String,String> merchantMap   = stockService.getAllMerchants();
		mav.addObject(CCLPConstants.MERCHANT_MAP,merchantMap);
		ResponseDTO responseDTO = stockService.getStockByMerchantLocationAndProduct(stockForm);
		if(responseDTO!=null ) {
			if( responseDTO.getData()!=null && responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.debug("StockDTO fetched successfully. Response from order service is "+responseDTO);
				ObjectMapper objectMapper = new ObjectMapper();
				StockDTO stockDTO=objectMapper.convertValue(responseDTO.getData(), StockDTO.class);
				logger.debug(stockDTO);
				mav.addObject(CCLPConstants.STOCK_FORM,stockDTO);
				List<Map<String, String>> list=  stockService.getStoresAndProductsByMerchantId(Long.parseLong(stockForm.getMerchantId()));
				mav.addObject(CCLPConstants.LOCATION_MAP,list.get(0));
				mav.addObject(CCLPConstants.PRODUCT_MAP,list.get(1));
				return mav;
			}else {
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
				logger.error("Failed to fetch stock details for view. Response from config service is "+responseDTO);
			}
		}
		else {
			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			mav.addObject(CCLPConstants.STATUS_MESSAGE,ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
			
			logger.error("Failed to fetch stock details. Response from config service is "+responseDTO);
		}
		mav = new ModelAndView(CCLPConstants.STOCK_CONFIG);
		Map<String,String> locationMap   = stockService.getAllLocations();
		mav.addObject(CCLPConstants.LOCATION_MAP,locationMap);
		mav.addObject(CCLPConstants.STOCK_FORM,new StockDTO());
		
		return mav;
	}
	
	
	@GetMapping("/getStoresAndProductsByMerchantId")
    public @ResponseBody List<Map<String,String>> getStoresAndProductsByMerchantId(HttpServletRequest request,HttpServletResponse response) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
        
		List<Map<String,String>> list = null; 
		try {
			String merchantId = request.getParameter("merchantId");
			logger.debug("ENTER " + merchantId);
			if(merchantId!=null && Long.parseLong(merchantId)!=-1)
				list  = stockService.getStoresAndProductsByMerchantId(Long.parseLong(merchantId));
		} catch (ServiceException e) {
			logger.error("Failed to fetch stores by merchant");
		}
		logger.debug(CCLPConstants.EXIT);
		return list;
	}
	
	@PreAuthorize("hasRole('ADD_STOCK')")
	@RequestMapping("/addStock")
	public ModelAndView addStock(@Valid @ModelAttribute("stockForm") StockDTO stockForm, BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		stockForm.setInsUser(userId);
		ModelAndView mav = new ModelAndView(CCLPConstants.STOCK_CONFIG);
		Map<String,String> merchantMap   = stockService.getAllMerchants();
		mav.addObject(CCLPConstants.MERCHANT_MAP,merchantMap);
		if (bindingResult.hasErrors()) {
			mav.addObject(CCLPConstants.STOCK_FORM, stockForm);
			logger.error("Some error occured while binding the stockForm object");
			return mav;
		}
		logger.info("Adding stock to table {}",stockForm);

		ResponseDTO responseDto = stockService.saveStock(stockForm);

		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Stock record '{}' has been added successfully");

				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(CCLPConstants.STOCK_CONFIG);
				mav.addObject(CCLPConstants.STOCK_FORM, new StockDTO());
				Map<String,String> locationMap   = stockService.getAllLocations();
				mav.addObject(CCLPConstants.LOCATION_MAP,locationMap);

			} else {
				logger.error("Error while adding stock "+responseDto.getMessage());
				mav.setViewName("defineStock");	
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				List<Map<String, String>> list=  stockService.getStoresAndProductsByMerchantId(Long.parseLong(stockForm.getMerchantId()));
				mav.addObject(CCLPConstants.LOCATION_MAP,list.get(0));
				mav.addObject(CCLPConstants.PRODUCT_MAP,list.get(1));
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				
				mav.addObject(CCLPConstants.STOCK_FORM, stockForm);
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('EDIT_STOCK')")
	@RequestMapping("/editStock")
	public ModelAndView editStock(@Valid @ModelAttribute("stockForm") StockDTO stockForm, BindingResult bindingResult) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		stockForm.setInsUser(userId);
		
		ModelAndView mav = new ModelAndView(CCLPConstants.STOCK_CONFIG);
		Map<String,String> merchantMap   = stockService.getAllMerchants();
		mav.addObject(CCLPConstants.MERCHANT_MAP,merchantMap);
		if (bindingResult.hasErrors()) {
			mav.addObject(CCLPConstants.STOCK_FORM, stockForm);
			mav.setViewName(CCLPConstants.EDIT_STOCK);
			List<Map<String, String>> list=  stockService.getStoresAndProductsByMerchantId(Long.parseLong(stockForm.getMerchantId()));
			mav.addObject(CCLPConstants.LOCATION_MAP,list.get(0));
			mav.addObject(CCLPConstants.PRODUCT_MAP,list.get(1));
			logger.error("Some error occured while binding the stockForm object");
			return mav;
		}
		logger.info("Updating stock to table {}",stockForm);

		ResponseDTO responseDto = stockService.updateStock(stockForm);

		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Stock record '{}' has been updated successfully");

				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.setViewName(CCLPConstants.STOCK_CONFIG);
				mav.addObject(CCLPConstants.STOCK_FORM, new StockDTO());
				Map<String,String> locationMap   = stockService.getAllLocations();
				mav.addObject(CCLPConstants.LOCATION_MAP,locationMap);
			} else {
				logger.error("Error while Updating stock "+responseDto.getMessage());
				mav.setViewName(CCLPConstants.EDIT_STOCK);
				List<Map<String, String>> list=  stockService.getStoresAndProductsByMerchantId(Long.parseLong(stockForm.getMerchantId()));
				mav.addObject(CCLPConstants.LOCATION_MAP,list.get(0));
				mav.addObject(CCLPConstants.PRODUCT_MAP,list.get(1));
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");

				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				
				mav.addObject(CCLPConstants.STOCK_FORM, stockForm);
			}
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	/**
	 * Show View Stock Screen
	 * @param exception
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW_STOCK')")
	@RequestMapping("/showViewStock")
	public ModelAndView showViewStock( @ModelAttribute("stockForm") StockDTO stockForm) throws ServiceException {
		ModelAndView mav = new ModelAndView(CCLPConstants.VIEW_STOCK);
		Map<String,String> merchantMap   = stockService.getAllMerchants();
		Map<Object, String> productMap = null;
		mav.addObject(CCLPConstants.MERCHANT_MAP,merchantMap);
		ResponseDTO responseDTO = stockService.getStockByMerchantLocationAndProduct(stockForm);
		if(responseDTO!=null ) {
			if( responseDTO.getData()!=null && responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.debug("StockDTO fetched successfully in show Stock view. Response from order service is "+responseDTO);
				ObjectMapper objectMapper = new ObjectMapper();
				StockDTO stockDTO=objectMapper.convertValue(responseDTO.getData(), StockDTO.class);
				logger.debug(stockDTO);
				mav.addObject(CCLPConstants.STOCK_FORM,stockDTO);
				List<Map<String, String>> list=  stockService.getStoresAndProductsByMerchantId(Long.parseLong(stockForm.getMerchantId()));
				productMap =list.get(1).entrySet().stream().sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getKey()+":"+e.getValue(),(e1, e2) -> e1,
                                LinkedHashMap::new));
				mav.addObject(CCLPConstants.LOCATION_MAP,list.get(0));
				mav.addObject(CCLPConstants.PRODUCT_MAP,productMap);
				return mav;
			}else {
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE,responseDTO.getMessage());
				logger.error("Failed to fetch stock details. Response from config service is "+responseDTO);
			}
		}
		else {
			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			mav.addObject(CCLPConstants.STATUS_MESSAGE,ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
			
			logger.error("Failed to fetch stock details for view method. Response from config service is "+responseDTO);
		}
		mav = new ModelAndView(CCLPConstants.STOCK_CONFIG);
		Map<String,String> locationMap   = stockService.getAllLocations();
		mav.addObject(CCLPConstants.LOCATION_MAP,locationMap);
		mav.addObject(CCLPConstants.STOCK_FORM,new StockDTO());
		
		return mav;
	}
	
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}
}
