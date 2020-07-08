package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.ProductDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.GlobalParameterService;
import com.incomm.cclpvms.config.service.PanExpiryService;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.validator.ValidatePanExpiry;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.IssuerException;
import com.incomm.cclpvms.exception.ProductException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Controller
@RequestMapping("/config/panExpiryExemption")
public class PanExpiryController {
	private static final Logger logger = LogManager.getLogger(PanExpiryController.class);
	
	public static final String ENTER="Enter";
	public static final String EXIT="Exit";
	
	@Autowired
	public GlobalParameterService globalParameterService;

	@Autowired
	public PanExpiryService panExpiryService;
	
	@Autowired
	public ProductService productService;
	
	@Autowired
	public  ValidatePanExpiry validatePanExpiry;
	
	private static final String PAN_EXP_DATE_CNFG = "panExpiryDateConfig";
	private static final String PAN_EXP_PARAM = "panExpiryParameters";
	private static final String PRODUCT_DROP_DWN = "productDropDown";
	
	
	
	
	/**
	 * 
	 * @param model
	 * @param globalParameters
	 * @param request
	 * @param response
	 * @return
	 * @throws ServiceException
	 * @throws ProductException 
	 */
	@PreAuthorize("hasRole('EDIT_PAN_EXPIRY_EXEMPTION')")
	@RequestMapping("/panExpiryDateConfig")
	public ModelAndView panExpiryDateConfiguration(Map<String, Object> model,@ModelAttribute(PAN_EXP_PARAM) 
	Product panExpiryExemption,
			HttpServletRequest request, HttpServletResponse response) throws ServiceException, ProductException {
	
		logger.debug(ENTER);
		ModelAndView mav=new ModelAndView();
		List<ProductDTO>  productList = null;
				
		productList=productService.getAllProducts();
		mav.addObject(PRODUCT_DROP_DWN,productList);
		mav.addObject(PAN_EXP_PARAM,new Product());
		mav.setViewName(PAN_EXP_DATE_CNFG);
		
		logger.debug("EXIT");
		return mav;
	}
	
	/**
	 * 
	 * @param model
	 * @param globalParameters
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @throws IssuerException
	 * @throws ProductException 
	 */

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT_PAN_EXPIRY_EXEMPTION')")
	@RequestMapping("/savePanExpiryParameters")
	public ModelAndView savePanExpiryParameters(Map<String, Object> model, 
			@ModelAttribute(PAN_EXP_PARAM) Product panExpiryExemption,
			BindingResult bindingResult,HttpServletRequest request)
					throws ServiceException, ProductException {

		logger.debug(ENTER);
		String message="";
		
		ModelAndView mav = new ModelAndView();
		
		Long productId=panExpiryExemption.getProductId();
		
		validatePanExpiry.validate(panExpiryExemption, bindingResult);
		if(bindingResult.hasErrors()) {
			
			mav.addObject(PRODUCT_DROP_DWN,productService.getAllProducts());
			  mav.addObject(PAN_EXP_PARAM, panExpiryExemption);
              mav.setViewName(PAN_EXP_DATE_CNFG);
                 
              return mav;
			
		}
		
		Product product= new Product();
		product.setProductId(productId);
		
		ResponseDTO responseDTO = null;
		
		Map <String,Object> prodData=null;
	
	

		Map<String,Object> panExpiryMap=null;
		
		panExpiryMap=panExpiryExemption.getProductAttributes();
	
		
		logger.debug("Before Calling panExpiryService");
		responseDTO =panExpiryService.updatePanExpiryParameters(panExpiryMap, panExpiryExemption.getProductId());
		logger.debug("after Calling panExpiryService");
		logger.info("Message from panExpiryService.updatePanExpiryParameters method: " + responseDTO.getMessage());
		
		prodData= (Map<String, Object>) responseDTO.getData();
		
		 
		panExpiryExemption = new Product(prodData);
		panExpiryExemption.setProductId(productId);

		logger.debug("CCLP-VMS Response Code : "+responseDTO.getCode()+" Error Message : "+responseDTO.getMessage());
		if (responseDTO.getCode().equalsIgnoreCase("000")) 
		{ 
			message=responseDTO.getMessage(); 
			mav.addObject("status", message);
			product.setProductId(panExpiryExemption.getProductId());
			
			mav.addObject(PRODUCT_DROP_DWN,productService.getAllProducts());
			mav.setViewName(PAN_EXP_DATE_CNFG);
			mav.addObject(PAN_EXP_PARAM, panExpiryExemption);
			
		} else {
			message=responseDTO.getMessage(); 
			mav.addObject("statusMessage", message);
			mav.setViewName(PAN_EXP_DATE_CNFG);
			mav.addObject(PAN_EXP_PARAM, panExpiryExemption);
		}	
	
	logger.debug("EXIT");
   return mav;


	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getPanExpiryDetails")
	public ModelAndView getPanExpiryDetails(Map<String, Object> model,@ModelAttribute(PAN_EXP_PARAM) 
	Product panExpiryParameters,
			HttpServletRequest request, HttpServletResponse response) throws ServiceException, ProductException {
	
		logger.debug(ENTER);
		ModelAndView mav=new ModelAndView();
		Product product= new Product();
		List<ProductDTO>  productList = null;
		ResponseDTO responseDTO= null;
		Map<String,Object> panExpiryMap = null; 
		
		
		
		productList=productService.getAllProducts();
		String productId = String.valueOf(panExpiryParameters.getProductId());
		if(productList!=null && !productList.isEmpty()) 
		{
				mav.addObject(PRODUCT_DROP_DWN,productList);
				product.setProductId(product.getProductId());
				
		}


		responseDTO=productService.getPanExpiryDetails(panExpiryParameters.getProductId());
		
	
		if(responseDTO!=null && responseDTO.getData()!=null){
			panExpiryMap=(Map<String,Object>)responseDTO.getData();
			product.setProductAttributes(panExpiryMap);
			product.setProductId(Long.parseLong(productId));
			
		}
		
			/**if(responseDTO.getData()==null){*/
			
				product.setProductId(panExpiryParameters.getProductId());
			
				mav.addObject(PRODUCT_DROP_DWN,productList);
			
				mav.addObject(PAN_EXP_PARAM,product);
			/**}
			else{
				product.setProductId(panExpiryParameters.getProductId());
			
				mav.addObject(PRODUCT_DROP_DWN,productList);
			
				mav.addObject(PAN_EXP_PARAM,product);
			}
		*/
		
		mav.setViewName(PAN_EXP_DATE_CNFG);
		
		logger.debug("EXIT");
		return mav;
	}
	
	
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug("Exception in pan expiry controller starts here");
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);
		logger.info("Error Message in Pan Expiry "+errMessage);

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("statusMessage", errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug("Exception in pan expiry controller ends here");

		return mav;
	}
	
	
	
	
}
