package com.incomm.cclpvms.fraud.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.ProductDTO;
import com.incomm.cclpvms.config.model.RedemptionDelay;
import com.incomm.cclpvms.config.model.RedemptionDelayDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.service.RedemptionService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.MerchantException;
import com.incomm.cclpvms.exception.ProductException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.model.MerchantRedemption;
import com.incomm.cclpvms.fraud.model.MerchantRedemptionDTO;
import com.incomm.cclpvms.fraud.service.MerchantRedemptionService;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/fraud")

public class RedemptionController {
	private static final Logger logger = LogManager.getLogger(RedemptionController.class);
	
	private static final String ADD_REDEMPTION_DELAY_MERCH = "addRedemptionDelayMerchant";
	private static final String FAIL_STATUS = "failstatus";
	private static final String MERCHANT_DROP_DWN = "merchantDropDown";
	private static final String PRODCUCT_DROP_DWN = "productDropDown";
	private static final String REDEMPTION_DELAY_CONFIG = "redemptionDelayConfig";
	private static final String REDEMPTION_DELAY_FORM = "redemptionDelayForm";
	private static final String ADD_URL = "addurl";
	

	
	@Autowired
	public MerchantRedemptionService merchantRedemptionService;
	
	@Value("${INS_USER}")
	public long insUser;
	

	@Autowired
	public ProductService productService;
	
	@Autowired
	public RedemptionService redemptionService;

	
	
	@RequestMapping("/redemptionDelayConfig")
	public ModelAndView showRedemptionDelayConfig(Map<String, Object> model,
			@ModelAttribute(REDEMPTION_DELAY_FORM) RedemptionDelay redemptionDelayForm, 
			HttpServletRequest request) throws ServiceException, ProductException, MerchantException {
		
		HttpSession session = request.getSession();
		session.setAttribute("insUser", insUser);

		logger.debug("Enter");
		ModelAndView mav = new ModelAndView();
		List<MerchantRedemptionDTO> merchantList = null;
		List<ProductDTO> productList = null;
		
		logger.debug("before  calling productService.getAllProducts()");
		
		productList = productService.getAllProducts();
			
		merchantList = merchantRedemptionService.getAllRedemptionMerchants();
		mav.addObject(ADD_URL,Util.constructUrl(request));
			
				
		logger.debug("after calling issuerservice.getAllMerchants()");
		
			mav.addObject(REDEMPTION_DELAY_FORM,  new RedemptionDelayDTO());
			mav.addObject(PRODCUCT_DROP_DWN, productList);
			mav.addObject(MERCHANT_DROP_DWN, merchantList);
			mav.setViewName(REDEMPTION_DELAY_CONFIG);
	
		logger.debug("EXIT");
		return mav;
	}
	
	/**
	 * update redemption delay
	 * @param model
	 * @param redemptionDelayForm
	 * @param request
	 * @return
	 * @throws ServiceException
	 * @throws ProductException
	 * @throws MerchantException
	 */
	
	
	
	@RequestMapping("/updateRedemptionDelay")
	public ModelAndView updateRedemptionDelay(Map<String, Object> model,
			@ModelAttribute(REDEMPTION_DELAY_FORM) RedemptionDelay redemptionDelayForm, 
			HttpServletRequest request) throws ServiceException, ProductException, MerchantException {

		logger.debug("Enter");
		ModelAndView mav = new ModelAndView();
		List<MerchantRedemptionDTO> merchantList = null;
		List<ProductDTO> productList = null;
		ResponseDTO responseDTO = null;
		RedemptionDelayDTO redemptionDelaytdto= new RedemptionDelayDTO();
		String data;
		String message;
		String[] startEndtime = null;
		
		
		logger.debug("before  calling productService.getAllProducts()");
		mav.addObject(ADD_URL,Util.constructUrl(request));
		productList = productService.getAllProducts();
			
		merchantList = merchantRedemptionService.getAllRedemptionMerchants();
		mav.addObject(ADD_URL,Util.constructUrl(request));
		
		String productData=redemptionDelayForm.getProductName();
		
		String [] productdatatoSplit = productData.split("~");
		if(productdatatoSplit[0]!=null && !productdatatoSplit[0].equalsIgnoreCase("-1")) {
		
		redemptionDelayForm.setProductId(Long.parseLong(productdatatoSplit[0]));
		redemptionDelayForm.setProductName(productdatatoSplit[1]);
		}
		
		String merchantData=redemptionDelayForm.getMerchantName();
		
		String [] merchantdatatoSplit = merchantData.split("~");
		if(merchantdatatoSplit[0]!=null && !merchantdatatoSplit[0].equalsIgnoreCase("-1")) {
		redemptionDelayForm.setMerchantId(merchantdatatoSplit[0]);
		redemptionDelayForm.setMerchantName(merchantdatatoSplit[1]);

		}
		
		if(request.getParameter("operationList")==null) {
			 data="";
		 }else {
			 data = request.getParameter("operationList");
		 }
	
		
		startEndtime = data.split(",");
		List<String> msgTypeList = new ArrayList<>(Arrays.asList(startEndtime));
		
		
		redemptionDelaytdto.setOperationsList(msgTypeList);
		redemptionDelaytdto.setMerchantId(redemptionDelayForm.getMerchantId());
		redemptionDelaytdto.setProductId(redemptionDelayForm.getProductId());
		redemptionDelaytdto.setMerchantName(redemptionDelayForm.getMerchantName());
		redemptionDelaytdto.setProductName(redemptionDelayForm.getProductName());
		redemptionDelaytdto.setInsUser(insUser);
		redemptionDelaytdto.setLastUpdUser(insUser);
		
	
		responseDTO = redemptionService.createRedemption(redemptionDelaytdto);

		
		if (responseDTO.getCode().equalsIgnoreCase("000")) 
		{ 
			message=responseDTO.getMessage(); 
			mav.addObject("successstatus", message);
	        mav.addObject(PRODCUCT_DROP_DWN,productService.getAllProducts());
	        mav.addObject(MERCHANT_DROP_DWN,merchantRedemptionService.getAllRedemptionMerchants());
			mav.addObject(REDEMPTION_DELAY_FORM,  redemptionDelaytdto);
			mav.setViewName(REDEMPTION_DELAY_CONFIG);
			
		} else {
			message=responseDTO.getMessage(); 
			mav.addObject(FAIL_STATUS, message);
			mav.setViewName(REDEMPTION_DELAY_CONFIG);
			mav.addObject(PRODCUCT_DROP_DWN, productList);
			mav.addObject(MERCHANT_DROP_DWN, merchantList);
			mav.addObject(REDEMPTION_DELAY_FORM, redemptionDelaytdto);
		}	
	
				logger.debug("EXIT");
				return mav;
	}
	
	/**
	 *  show add Merchant for redemption delay
	 */
	@RequestMapping("/showAddMerchantRedemptionDelayConfig")
	public ModelAndView showAddMerchant(Map<String, Object> model) {
		logger.debug("ENTER");
		ModelAndView mav = new ModelAndView();
		
		mav.addObject(REDEMPTION_DELAY_FORM, new MerchantRedemption());
		mav.setViewName(ADD_REDEMPTION_DELAY_MERCH);
		logger.debug("EXIT");
		return mav;
	}
	
	/**
	 * add redemption Merchant
	 * 
	 */
	@RequestMapping("/addRedemptionMerchant")
	public ModelAndView addMerchant(Map<String, Object> model,	@Validated({ MerchantRedemption.ValidationStepOneMerchantRedeem.class})
			 @ModelAttribute(REDEMPTION_DELAY_FORM) MerchantRedemption redemptionDelayForm,
			BindingResult bindingResult,HttpServletRequest request)
					throws ServiceException, MerchantException {

		logger.debug("ENTER");
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		MerchantRedemptionDTO merchantRedemptionDTO = new MerchantRedemptionDTO();
		
		ResponseDTO responseDTO = null;
		String merchantName ="";
		String merchantId="";
		Long insUser = null;
		String message = "";

			if (bindingResult.hasErrors()) {
				
				mav.setViewName(ADD_REDEMPTION_DELAY_MERCH);
				return mav;

			} 
			else
			{
				
				if (Util.isEmpty(redemptionDelayForm.getMerchantName())) {
					merchantName="";
				}else {
					merchantName = redemptionDelayForm.getMerchantName().trim();
				}
				
				if (Util.isEmpty(redemptionDelayForm.getMerchantId())) {
					merchantId="";
				}else {
					merchantId = redemptionDelayForm.getMerchantId();
				}
				
		
								
					
			insUser= (Long) session.getAttribute("insUser");
			
			
				
			   
				logger.info(" Merchant  details :: Merchant Name: " + merchantName + " merchantId: " + merchantId);

				merchantRedemptionDTO.setMerchantName(merchantName);
				merchantRedemptionDTO.setMerchantId(merchantId);
				merchantRedemptionDTO.setInsUser(insUser);
				merchantRedemptionDTO.setInsDate(new Date());
				merchantRedemptionDTO.setLastUpdDate(new Date());
				merchantRedemptionDTO.setLastUpdUser(insUser);
				

				logger.debug("Before Calling merchantredeem service.createMerchant");
				responseDTO = merchantRedemptionService.createRedemptionMerchant(merchantRedemptionDTO);
				logger.debug("after Calling merchantredeem service.createMerchant");
				logger.info("Message from createIssuer method: " + responseDTO.getMessage());

				logger.debug("CCLP-VMS Response Code : "+responseDTO.getCode()+" Error Message : "+responseDTO.getMessage());
				if (responseDTO.getCode().equalsIgnoreCase("000")) 
				{ 
					message=responseDTO.getMessage(); 
					mav.addObject("successstatus", message);
					mav.setViewName(ADD_REDEMPTION_DELAY_MERCH);
					mav.addObject(REDEMPTION_DELAY_FORM, merchantRedemptionDTO);
					
				} else {
					message=responseDTO.getMessage(); 
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(ADD_REDEMPTION_DELAY_MERCH);
					mav.addObject(REDEMPTION_DELAY_FORM, merchantRedemptionDTO);
				}	
			}
			logger.debug("EXIT");
		return mav;
	}

	
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {

		logger.info("exceptionHandler Method Starts Here");
		
		String errMessage = ResponseMessages.SERVER_DOWN;
		ModelAndView mav = new ModelAndView();
		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		mav.setViewName("serviceError");
		mav.addObject(FAIL_STATUS, errMessage);
		
		logger.info("exceptionHandler Method Ends Here");
		return mav;

	}


}
