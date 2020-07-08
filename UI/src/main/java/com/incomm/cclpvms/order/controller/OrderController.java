package com.incomm.cclpvms.order.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.order.model.OrderForm;
import com.incomm.cclpvms.order.service.OrderService;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;
import com.incomm.cclpvms.session.SessionService;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
    private static final Logger logger = LogManager.getLogger(OrderController.class);
    
      
    @Autowired
	SessionService sessionService;
    
	@Autowired
	public OrderService orderService;
	
	@Autowired
	public ProductService productService;
	
	@PreAuthorize("hasRole('ADD_ORDER')")
	@RequestMapping("/placeOrder")
    public ModelAndView placeOrder(HttpServletRequest request,HttpServletResponse response) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("placeOrder");
		Map<Object,String> productMap  = productService.getAllRetailProducts();
		
		mav.addObject(CCLPConstants.PRODUCT_MAP,productMap);
		mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	@PreAuthorize("hasRole('ADD_ORDER')")
	@GetMapping("/getMerchantsAndPackages")
    public @ResponseBody Map<String,Object> getMerchantsAndPackages(HttpServletRequest request,HttpServletResponse response) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
        Map<String,Object>   merchantPckgMap= null;
		try {
			String productid = request.getParameter("productId");
			logger.debug(CCLPConstants.ENTER + productid);
			if(productid!=null && Long.parseLong(productid)!=-1)
				merchantPckgMap  = orderService.getMerchantsAndPackages(Long.parseLong(productid));
			else {
				merchantPckgMap=new HashMap<>();
				merchantPckgMap.put("error","Please select a product");
			}
		} catch (Exception e) {
			logger.error("Failed to fetch merchants and package ids by product");
		}
		logger.debug(CCLPConstants.EXIT);
		return merchantPckgMap;
	}
	
	@PreAuthorize("hasRole('ADD_ORDER')")
	@GetMapping("/getStoresByMerchantId")
    public @ResponseBody Map<String,String> getStoresByMerchantId(HttpServletRequest request,HttpServletResponse response) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
        
		Map<String,String> storeMap = null; 
		try {
			String merchantId = request.getParameter("merchantId");
			logger.debug(CCLPConstants.ENTER + merchantId);
			if(merchantId!=null && Long.parseLong(merchantId)!=-1)
				storeMap  = orderService.getStoresByMerchantId(Long.parseLong(merchantId));
		} catch (ServiceException e) {
			logger.error("Failed to fetch stores by merchant");
		}
		logger.debug(CCLPConstants.EXIT);
		return storeMap;
	}
	
	@GetMapping("/getAvailableInventory")
    public @ResponseBody String getAvailableInventory(HttpServletRequest request,HttpServletResponse response) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
        String availInventory = null;
      
		try {
			String merchantId = request.getParameter("merchantId");
			String locationId = request.getParameter("locationId");
			String productId=request.getParameter("productId");
			logger.debug(CCLPConstants.ENTER + merchantId);
			if(merchantId!=null && Long.parseLong(merchantId)!=-1 && locationId!=null && Long.parseLong(locationId)!=-1 && productId!=null &&  Long.parseLong(productId)!=-1 )
				availInventory  = orderService.getAvailableInventory(Long.parseLong(merchantId),Long.parseLong(locationId),Long.parseLong(productId));
		} catch (ServiceException e) {
			logger.error("Failed to fetch stores by merchant");
		}
		logger.debug(CCLPConstants.EXIT);
		return availInventory;
	}
	@PreAuthorize("hasRole('ADD_ORDER')")
	@RequestMapping("/saveOrder")
    public ModelAndView saveOrder(@Valid  @ModelAttribute OrderForm orderForm,  BindingResult bindingResult) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView("placeOrder");
		Map<Object,String> productMap = productService.getAllRetailProducts();
		mav.addObject(CCLPConstants.PRODUCT_MAP,productMap);
		orderForm.setInsUser(sessionService.getUserId());
		if (bindingResult.hasErrors()) {
			mav.addObject(CCLPConstants.ORDER_FORM, orderForm);
			logger.error("Some error occured while binding the Order object");
			Map<String,Object> merchantPckgMap  = orderService.getMerchantsAndPackages(orderForm.getProductId());
			if(merchantPckgMap!=null) {	
				mav.addObject("merchantMap",merchantPckgMap.get("merchants"));
				mav.addObject("packageMap",merchantPckgMap.get("packageIds"));
			}
			if(orderForm.getMerchantId()!=null && orderForm.getMerchantId()!=-1) {
				Map<String,String>	storeMap  = orderService.getStoresByMerchantId( orderForm.getMerchantId());
				mav.addObject("storeMap",storeMap);
			}
			return mav;
		}
		ResponseDTO responseDto =  orderService.saveOrder(orderForm) ;
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Order record '{}' has been added successfully");
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
			} else {
				logger.error("Error while adding Order "+responseDto.getMessage());
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				if (responseDto.getMessage() != null)
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.addObject(CCLPConstants.ORDER_FORM, orderForm);
				Map<String,Object> merchantPckgMap  = orderService.getMerchantsAndPackages(orderForm.getProductId());
				if(merchantPckgMap!=null) {	
					mav.addObject("merchantMap",merchantPckgMap.get("merchants"));
					mav.addObject("packageMap",merchantPckgMap.get("packageIds"));
				}
				if(orderForm.getMerchantId()!=null && !orderForm.getMerchantId().equals(Long.valueOf(-1))) {
					
					Map<String,String>	storeMap  = orderService.getStoresByMerchantId( orderForm.getMerchantId());
					mav.addObject("storeMap",storeMap);
				}
			}
		}
	
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('APPROVE_ORDER')")
	@RequestMapping("/showApproveOrder")
    public ModelAndView showApproveOrder(HttpServletRequest request,HttpServletResponse response) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("approveOrder");
		mav.addObject("loginUserId", sessionService.getUserId());
		List<OrderForm> orderList= orderService.getAllOrdersForApproval();
		mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
		mav.addObject(CCLPConstants.ORDER_LIST,orderList);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('APPROVE_ORDER')")
	@RequestMapping("/approveRejectOrder")
    public ModelAndView approveRejectOrder(@ModelAttribute OrderForm orderForm) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER+orderForm);
		ModelAndView mav = new ModelAndView("approveOrder");
		
		ResponseDTO responseDto = null;
		orderForm.setInsUser(sessionService.getUserId());
		logger.info(" Approve/reject order list from table {}"+Arrays.toString(orderForm.getOrderPartnerId()));
		String[] orderPartnerId=orderForm.getOrderPartnerId();
		if(orderPartnerId!=null && orderPartnerId.length>0) {
			responseDto = orderService.approveRejectOrder(orderForm);
		}
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Order record '{}' has been updated successfully", Arrays.toString(orderPartnerId));
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			} else {
				logger.error("Failed to update record for order '{}'",  Arrays.toString(orderPartnerId));
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			}
			mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
		}
		List<OrderForm> orderList= orderService.getAllOrdersForApproval();
		mav.addObject("loginUserId", sessionService.getUserId());
		mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
		mav.addObject(CCLPConstants.ORDER_LIST,orderList);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/showOrderStatus")
    public ModelAndView showOrderStatus() throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("checkOrderStatus");
		Map<Object,String> productMap  = productService.getAllParentProducts();
		mav.addObject(CCLPConstants.PRODUCT_MAP,productMap);
		mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/checkOrderStatus")
    public ModelAndView checkOrderStatus(@ModelAttribute OrderForm orderForm,HttpServletRequest request ) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
    	ModelAndView mav = new ModelAndView();
    	mav.addObject(CCLPConstants.ORDER_FORM, orderForm);   
    	mav.setViewName("checkOrderStatus");
    	mav.addObject(CCLPConstants.FROM_DATE, request.getParameter(CCLPConstants.FROM_DATE));
        mav.addObject(CCLPConstants.TO_DATE, request.getParameter(CCLPConstants.TO_DATE));
    
    	String fromDate=Util.isEmpty(request.getParameter("fromDate"))?"-1":request.getParameter("fromDate");
    	String toDate=Util.isEmpty(request.getParameter("toDate"))?"-1":request.getParameter("toDate");
             fromDate=fromDate.replace('/','-');
        toDate=toDate.replace('/','-');
        
		List<OrderForm> orderList= orderService.getAllOrdersByProductIdOrOrderId(orderForm,fromDate,toDate);
		mav.addObject(CCLPConstants.SHOW_GRID,"true");
		mav.addObject(CCLPConstants.ORDER_LIST,orderList);
		Map<Object,String> productMap  = productService.getAllParentProducts();
		mav.addObject(CCLPConstants.PRODUCT_MAP,productMap);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/showOrderAndCCFGeneration")
    public ModelAndView showOrderAndCCFGeneration(@ModelAttribute OrderForm orderForm) {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(CCLPConstants.ORDER_AND_CCF_PROCESS);
		mav.addObject(CCLPConstants.SHOW_GRID,"false");
		mav.addObject(CCLPConstants.ORDER_FORM, orderForm);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/showCNFileUpload")
    public ModelAndView showCNFileUpload() throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDto=null;
		List<String> cnFileList = null;
		responseDto = orderService.getCNFileList();
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				cnFileList=(List<String>) responseDto.getData();
				logger.info("Order record '{}' has been uploaded successfully", cnFileList);
			} else {
				logger.error("Failed to upload Cn Files '{}'",  cnFileList);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			}
			
		}
		
		 
		
		mav.addObject(CCLPConstants.CN_FILE_LIST,cnFileList);
		mav.setViewName(CCLPConstants.CN_FILE_UPLOAD);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/showCNFileStatus")
    public ModelAndView checkCNFileStatus() throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		List<Map<String,Object>> cnFileList=orderService.getCnFileStatus();
		mav.addObject(CCLPConstants.CN_FILE_LIST,cnFileList);
		mav.setViewName("checkCNFileStatus");
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/cnFileUpload")
    public ModelAndView cnFileupload(HttpServletRequest request, HttpServletResponse response) throws ServiceException  {
        logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView(CCLPConstants.CN_FILE_UPLOAD);
		
		ResponseDTO responseDto = null;
		String[] cnFileList=request.getParameterValues("selectedItems");
		
       	logger.info("upload cn file list from table {}"+Arrays.toString(cnFileList));
		String files =cnFileList[0];
       	for(int i=1;i<cnFileList.length;i++)
       		files =files+":"+cnFileList[i];
		
		responseDto = orderService.uploadCNFiles(files);
		if (responseDto != null) {
			if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				logger.info("Order record '{}' has been uploaded successfully", Arrays.toString(cnFileList));
				mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			} else {
				logger.error("Failed to upload Cn Files '{}'",  Arrays.toString(cnFileList));
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			}
			mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
		}
		responseDto = orderService.getCNFileList();
		List<String> cnFileList1 = null;
		if(responseDto != null) {
		 cnFileList1 = (List<String>) responseDto.getData();
		}
		mav.addObject(CCLPConstants.CN_FILE_LIST,cnFileList1);
		mav.setViewName(CCLPConstants.CN_FILE_UPLOAD);
		
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	
	
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/orderProcessing")
    public ModelAndView orderProcessing(@ModelAttribute OrderForm orderForm) throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(CCLPConstants.ORDER_AND_CCF_PROCESS);
		List<OrderForm> orderList= orderService.getAllOrdersForOrder();
		mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
		mav.addObject(CCLPConstants.SHOW_GRID,"true");
		mav.addObject(CCLPConstants.ORDER_LIST,orderList);
		mav.addObject("GenerationType","ORDER");
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/ccfGeneration")
    public ModelAndView ccfGeneration(@ModelAttribute OrderForm orderForm) throws ServiceException  {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(CCLPConstants.ORDER_AND_CCF_PROCESS);
		List<OrderForm> orderList= orderService.getAllOrdersForCCF();
		mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
		mav.addObject(CCLPConstants.SHOW_GRID,"true");
		mav.addObject(CCLPConstants.ORDER_LIST,orderList);
		mav.addObject("GenerationType","CCF");
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/generateOrder")
    public ModelAndView generateOrder(@ModelAttribute OrderForm orderForm) throws ServiceException  {
		 logger.debug(CCLPConstants.ENTER+orderForm);
			ModelAndView mav = new ModelAndView(CCLPConstants.ORDER_AND_CCF_PROCESS);
			
			ResponseDTO responseDto = null;
			orderForm.setInsUser(sessionService.getUserId());
			logger.info("Order list from table {}"+Arrays.toString(orderForm.getOrderPartnerId()));
			String[] orderPartnerId=orderForm.getOrderPartnerId();
		
			if(orderPartnerId!=null && orderPartnerId.length>0) {
				responseDto = orderService.generateOrder(orderForm);
			}
			if (responseDto != null) {
				if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
					logger.info("Order record '{}' has been updated successfully", Arrays.toString(orderPartnerId));
					mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
				} else {
					logger.error("Failed to update record for order '{}'",  Arrays.toString(orderPartnerId));
					mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				}
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			}
			List<OrderForm> orderList= orderService.getAllOrdersForOrder();
			mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
			mav.addObject(CCLPConstants.ORDER_LIST,orderList);
			mav.addObject("GenerationType","ORDER");
			logger.debug(CCLPConstants.EXIT);
			return mav;
	}
	
	@PreAuthorize("hasRole('SEARCH_ORDER')")
	@RequestMapping("/generateCCF")
    public ModelAndView generateCCF(@ModelAttribute OrderForm orderForm) throws ServiceException  {
		 logger.debug(CCLPConstants.ENTER+orderForm);
			ModelAndView mav = new ModelAndView(CCLPConstants.ORDER_AND_CCF_PROCESS);
			
			ResponseDTO responseDto = null;
			orderForm.setInsUser(sessionService.getUserId());
			logger.info("Order list from table {}"+Arrays.toString(orderForm.getOrderPartnerId()));
			String[] orderPartnerId=orderForm.getOrderPartnerId();
			
			
			if(orderPartnerId!=null && orderPartnerId.length>0) {
				responseDto = orderService.generateCCF(orderPartnerId,sessionService.getUserId());
			}
			if (responseDto != null) {
				if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase("success")) {
					logger.info("CCF record '{}' has been updated successfully", Arrays.toString(orderPartnerId));
					mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				} else {
					logger.error("Failed to Generate CCF '{}'",  Arrays.toString(orderPartnerId));
					mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				}
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			}
			List<OrderForm> orderList= orderService.getAllOrdersForCCF();
			mav.addObject(CCLPConstants.ORDER_FORM, new OrderForm());
			mav.addObject(CCLPConstants.ORDER_LIST,orderList);
			mav.addObject("GenerationType","CCF");
			logger.debug(CCLPConstants.EXIT);
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
