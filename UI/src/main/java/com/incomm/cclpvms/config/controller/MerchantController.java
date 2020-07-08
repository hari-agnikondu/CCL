package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.Merchant;
import com.incomm.cclpvms.config.model.MerchantDTO;
import com.incomm.cclpvms.config.model.MerchantProduct;
import com.incomm.cclpvms.config.model.MerchantProductDTO;
import com.incomm.cclpvms.config.model.ProductDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.MerchantService;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.validator.ValidateMerchantProduct;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.MerchantException;
import com.incomm.cclpvms.exception.ProductException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/merchant")
public class MerchantController {	

	private static final Logger logger = LogManager.getLogger(MerchantController.class);
	

	@Autowired
	public MerchantService merchantService;
	

	@Autowired
	public ProductService productService;
	
	@Autowired
	public  ValidateMerchantProduct validateMerchantProduct;
	 


	
	@Value("${INS_USER}")
	public long insUser;
	
	public static final String ENTER="Enter";
	public static final String EXIT="Exit";
	public static final String SEARCH_TYPE = "SearchType";
	public static final String ADD_MERCHANT = "addMerchant";
	public static final String ASSIGN_MERCHANT_CNFG = "assignMerchantConfig";
	public static final String CONFIRM_BOX = "confirmBox";
	public static final String DELETE_BOX = "deleteBox";
	public static final String EDIT_MERCHANT = "editMerchant";
	public static final String EDIT_MERCHANT_ID = "editMerchantId";
	public static final String INS_USER = "insUser";
	public static final String MERCHANT_CONFIG = "merchantConfig";
	public static final String MERCHANT_DROPDWN = "merchantDropDown";
	public static final String MERHCHANT_FORM = "merchantForm";
	public static final String MERCHANT_ID = "merchantId";
	public static final String MERCHANT_TABLE_LIST = "merchantTableList";
	public static final String MERCHANT_TO_PRODUCT_FORM = "merchantToProductForm";
	public static final String PRODUCT_DROP_DWN = "productDropDown";
	public static final String SHOW_ASSIGN_PROD_TO_MERCH = "showAssignProductToMerchant";
	public static final String SHOW_GRID = "showGrid";
	public static final String SUCCESS_STATUS = "successstatus";
	public static final String SRCH_TYPE = "searchType";
	public static final String FAIL_STATUS = "failstatus";
	public static final String VIEW_MERCHANT = "viewMerchant";
	public static final String VIEW_MERCHANT_ID = "viewMerchantId";
	public static final String PRODUCT_ID="productId";
	

	

	@PreAuthorize("hasRole('SEARCH_MERCHANT')")
	@RequestMapping("/merchantConfig")
	public ModelAndView merchantCongiguration(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
	
		logger.debug(ENTER);
		HttpSession session = request.getSession();
		session.setAttribute(INS_USER, insUser);
		ModelAndView mav = new ModelAndView();
		mav.addObject(MERHCHANT_FORM, new Merchant());
		mav.addObject(DELETE_BOX, new Merchant());
		
		mav.setViewName(MERCHANT_CONFIG);
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('ADD_MERCHANT')")
	@RequestMapping("/addMerchant")
	public ModelAndView addMerchant(Map<String, Object> model,
			@Validated({ Merchant.ValidationStepOneMerchant.class,
				Merchant.ValidationStepTwoMerchant.class }) @ModelAttribute(MERHCHANT_FORM) Merchant merchantForm,
			BindingResult bindingResult,HttpServletRequest request)
					throws ServiceException {

		logger.debug(ENTER);
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		MerchantDTO merchantDTO = new MerchantDTO();
		String flag = "";
		ResponseDTO responseDTO = null;
		String merchantName ="";
		String iMdmId="";
		String iDescription="";
		Long insuser = null;
		String message = "";

			if (bindingResult.hasErrors()) {
				mav.setViewName(ADD_MERCHANT);
				return mav;

			} 
			else
			{
				
				if (Util.isEmpty(merchantForm.getMerchantName())) {
					merchantName="";
				}else {
					merchantName = merchantForm.getMerchantName().trim();
				}
				
				if (Util.isEmpty(merchantForm.getMdmId())) {
					iMdmId="";
				}else {
					iMdmId = merchantForm.getMdmId();
				}
				
				if (Util.isEmpty(merchantForm.getDescription())) {
					iDescription="";
				}else {
					iDescription = merchantForm.getDescription();
				}
								
					
				insuser= (Long) session.getAttribute(INS_USER);
			
			flag = "Y";
				
			   
				logger.info(" Merchant  details :: Merchant Name: " + merchantName + " MDMID: " + iMdmId + " Active: " + flag
						+ " Description: " + iDescription);

				merchantDTO.setMerchantName(merchantName);
				merchantDTO.setMdmId(iMdmId);
				merchantDTO.setInsUser(insuser);
				merchantDTO.setInsDate(new Date());
				merchantDTO.setLastUpdDate(new Date());
				merchantDTO.setLastUpdUser(insuser);
				merchantDTO.setDescription(iDescription);

				logger.debug("Before Calling merchantService.createMerchant");
				responseDTO = merchantService.createMerchant(merchantDTO);
				logger.debug("after Calling issuerservice.createIssuer");
				logger.info("Message from createIssuer method: " + responseDTO.getMessage());

				logger.debug("CCLP-VMS Response Code : "+responseDTO.getCode()+" Error Message : "+responseDTO.getMessage());
				if (responseDTO.getCode().equalsIgnoreCase("000")) 
				{ 
					message=responseDTO.getMessage(); 
					mav.addObject(SUCCESS_STATUS, message);
					mav.setViewName(MERCHANT_CONFIG);
					mav.addObject(MERHCHANT_FORM,  new MerchantDTO());
					mav.addObject(DELETE_BOX, new Merchant());
				} else {
					message=responseDTO.getMessage(); 
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(ADD_MERCHANT);
					mav.addObject(MERHCHANT_FORM, merchantDTO);
				}	
			}
			logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('ADD_MERCHANT')")
	@RequestMapping("/showAddMerchant")
	public ModelAndView showAddMerchant(Map<String, Object> model) {
		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		mav.addObject("merchantDTO", new Merchant());
		mav.addObject(MERHCHANT_FORM, new Merchant());
		mav.setViewName(ADD_MERCHANT);
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_MERCHANT')")
	@RequestMapping("/showAllMerchants")
	public ModelAndView showAllMerchants(Map<String, Object> model,
			@ModelAttribute(MERHCHANT_FORM) Merchant merchantForm, BindingResult bindingresult,
			HttpServletRequest request) throws ServiceException, MerchantException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		List<MerchantDTO> merchantList = null;
		String merchantName ="";
		String searchType="";
		
			 mav.setViewName(MERCHANT_CONFIG);
			 merchantName = merchantForm.getMerchantName();
			 if(request.getParameter(SRCH_TYPE)==null) {
				 searchType="";
			 }else {
			 searchType = request.getParameter(SRCH_TYPE);
			 }
			 mav.addObject(SEARCH_TYPE, searchType);
			logger.info("Merchant Name: " + merchantName);
			logger.debug("Before calling issuerservice.getAllMerchants()");
			
			merchantList = merchantService.getAllMerchants();
			
			logger.debug("after calling issuerservice.getAllMerchants()");
			mav.addObject(MERHCHANT_FORM, new Merchant());
			mav.setViewName(MERCHANT_CONFIG);
			mav.addObject(MERCHANT_TABLE_LIST, merchantList);
			mav.addObject(MERHCHANT_FORM, merchantForm);
			mav.addObject(SHOW_GRID, "true");
			mav.addObject(DELETE_BOX, new Merchant());
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_MERCHANT')")
	@RequestMapping("/searchMerchantByName")
	public ModelAndView searchMerchantByName(Map<String, Object> model,
			@Validated(Merchant.SearchMerchantScreen.class)  @ModelAttribute(MERHCHANT_FORM) Merchant merchantForm,
			BindingResult bindingresult, HttpServletRequest request) throws ServiceException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		String merchantName="";
		String searchType="";
			
			List<MerchantDTO> merchantList = null;
			mav.setViewName(MERCHANT_CONFIG);
			mav.addObject(DELETE_BOX, new Merchant());
			searchType = request.getParameter(SRCH_TYPE);
			mav.addObject(SEARCH_TYPE, searchType);
			if (bindingresult.hasErrors()) {
				return mav;
			}
			merchantName = merchantForm.getMerchantName();
			logger.info("Merchant Name: " + merchantName);
			logger.debug("Before calling merchantService.getMerchantsByName()");
			merchantList = merchantService.getMerchantsByName(merchantName);
			logger.debug("after calling issuerservice.getIssuersByName()");
			mav.addObject(MERHCHANT_FORM, new Merchant());
			mav.setViewName(MERCHANT_CONFIG);
			mav.addObject(MERCHANT_TABLE_LIST, merchantList);
			mav.addObject(MERHCHANT_FORM, merchantForm);
			mav.addObject(SHOW_GRID, "true");

		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_MERCHANT')")
	@RequestMapping("/showEditMerchant")
	public ModelAndView showEditMerchant(Map<String, Object> model, HttpServletRequest request)
			throws ServiceException {

		logger.debug(ENTER);
		long merchantId=0;
		String id="";
		Merchant merchant =null;
		ModelAndView mav = new ModelAndView();
		try {
			
			id = request.getParameter(MERCHANT_ID);
			merchantId = Long.parseLong(id);
			mav.setViewName(EDIT_MERCHANT);
			logger.info("merchantId  :: "+merchantId);
			logger.debug("Before Calling merchantService.getMerchantById() ");
			merchant = merchantService.getMerchantById(merchantId);
			logger.debug("After Calling merchantService.getMerchantById() ");
			mav.addObject(MERHCHANT_FORM, merchant);
			mav.addObject(EDIT_MERCHANT_ID, id);
			mav.setViewName(EDIT_MERCHANT);
			mav.addObject(CONFIRM_BOX, new Merchant());
			mav.addObject(MERCHANT_ID, merchantId);
			

		} catch (NumberFormatException e) {
			mav.setViewName(EDIT_MERCHANT);
			logger.error("NumberFormatException Occured While Editing Merchant in showEditMerchant()"+e);       
			 }
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_MERCHANT')")
	@RequestMapping("/editMerchant")
	public ModelAndView editMerchant(Map<String, Object> model,
			@Validated({ Merchant.ValidationStepOneMerchant.class,
					Merchant.ValidationStepTwoMerchant.class }) @ModelAttribute(MERHCHANT_FORM) Merchant merchantForm,
			BindingResult bindingresult, HttpServletRequest request) throws ServiceException {

		logger.debug(ENTER);
		long merchantId = 0;
		String merchantName = "";
		String merchantDescription = "";
		String mdmId = "";
		String message = "";
		long insuser = 0;
		String flag = "";
		MerchantDTO merchantdto = new MerchantDTO();
		ResponseDTO responseDTO = null;
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();

		if (request.getParameter(EDIT_MERCHANT_ID) != null
				&& !request.getParameter(EDIT_MERCHANT_ID).equalsIgnoreCase("")) {
			merchantId = Long.parseLong(request.getParameter(EDIT_MERCHANT_ID));
		}
		if (bindingresult.hasErrors()) {
			mav.setViewName(EDIT_MERCHANT);
			mav.addObject(EDIT_MERCHANT_ID, merchantId);
			mav.addObject(CONFIRM_BOX, new Merchant());
			return mav;
		}
		mav.addObject(EDIT_MERCHANT_ID, merchantId);
		mav.addObject(CONFIRM_BOX, new Merchant());

		merchantName = merchantForm.getMerchantName();
		insuser = (Long) session.getAttribute(INS_USER);

		if (merchantForm.getDescription() == null) {
			merchantDescription = "";
		} else {
			merchantDescription = merchantForm.getDescription();
		}

		if (merchantForm.getMdmId() == null) {
			mdmId = "";
		} else {
			mdmId = merchantForm.getMdmId();

		}

		flag = "Y";

		logger.info("merchant  details :: Merchant Name: " + merchantName + " MDMID: " + mdmId + " Active: " + flag
				+ "Merchant Description: " + merchantDescription + "  insUser " + insUser);

		merchantdto.setMerchantId(merchantId);
		merchantdto.setMerchantName(merchantName);
		merchantdto.setMdmId(mdmId);
		merchantdto.setInsUser(insuser);
		merchantdto.setInsDate(new Date());
		merchantdto.setLastUpdDate(new Date());
		merchantdto.setLastUpdUser(insuser);
		merchantdto.setDescription(merchantDescription);

		logger.debug("Before Calling merchantService.getMerchantById() ");
		Merchant merchant = merchantService.getMerchantById(merchantId);

		if (merchant == null || merchant.getMerchantId()==0) {
			message = ResourceBundleUtil.getMessage("MER_" + ResponseMessages.DOESNOT_EXISTS);
			mav.addObject(FAIL_STATUS, message);
			mav.setViewName(EDIT_MERCHANT);
			mav.addObject(MERHCHANT_FORM, merchantForm);
		} else {
			logger.debug("After Calling merchantService.getMerchantById() ");

			logger.debug("Before Calling merchantService.updateMerchant() ");
			
			responseDTO = merchantService.updateMerchant(merchantdto);
			
			logger.debug("After Calling merchantService.updateMerchant() ");
			logger.info("Message from  response " + responseDTO.getMessage());
			Object data = responseDTO.getData();
			if (data != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> obj = (Map<String, Object>) data;

				merchantName = (String) obj.get("merchantName");

				/**if (obj.get("description") != null) {
					merchantDescription = String.valueOf(obj.get("description"));
				} else {
					merchantDescription = "";
				}
				if (obj.get("mdmId") != null) {
					mdmId = String.valueOf(obj.get("mdmId"));
				} else {
					mdmId = "";
				}*/

				mav.addObject(MERHCHANT_FORM, new MerchantDTO());
			}
			if (responseDTO.getCode().equalsIgnoreCase("000")) {
				message = responseDTO.getMessage();
				mav.addObject(SUCCESS_STATUS, message);
				mav.setViewName(MERCHANT_CONFIG);
				mav.addObject(DELETE_BOX, new Merchant());
			} else if (responseDTO.getCode().equalsIgnoreCase("MER_001")) {
				message = ResourceBundleUtil.getMessage("MER_" + ResponseMessages.ALREADY_EXISTS);
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(EDIT_MERCHANT);
			} else if (responseDTO.getCode().equalsIgnoreCase("002")) {
				message = ResourceBundleUtil.getMessage("MER_" + ResponseMessages.DOESNOT_EXISTS);
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(EDIT_MERCHANT);
			} else {

				message = ResourceBundleUtil.getMessage(ResponseMessages.FAILURE) + merchantName;
				mav.addObject(FAIL_STATUS, message);
				mav.setViewName(EDIT_MERCHANT);
				mav.addObject(DELETE_BOX, new Merchant());
			}
		}
		logger.debug("EXIT");

		return mav;
	}

	@PreAuthorize("hasRole('DELETE_MERCHANT')")
	@RequestMapping("/deleteMerchant")
	public ModelAndView deleteMerchant(Map<String, Object> model, HttpServletRequest request)
			throws ServiceException, MerchantException {

		logger.debug(ENTER);
		List<MerchantDTO> merchantList = null;
		MerchantDTO merdtoobj = new MerchantDTO();
		ResponseDTO responseDTO = null;
		String id ="";
		String searchedName="";
		String searchType="";
		String message="";
		Merchant mer = new Merchant();
		ModelAndView mav = new ModelAndView();
		long merchantId =0;
	
			mav.setViewName(MERCHANT_CONFIG);
			 id = request.getParameter(MERCHANT_ID);
			 searchedName = request.getParameter("searchedName");
			 logger.info("id and SearchedName : "+id +"  "+searchedName);
			if (searchedName == null || searchedName.isEmpty()) {
				searchedName = request.getParameter("retrievedName");
			}
			mav.addObject("SearchedName", searchedName);
			searchType = request.getParameter(SRCH_TYPE);
			mav.addObject(SEARCH_TYPE, searchType);
			logger.info("SearchType : "+searchType);
         	 merchantId = Long.valueOf(id);
			merdtoobj.setMerchantId(merchantId);
				
			logger.debug("Before Calling merchantService.deleteMerchant()");
				responseDTO = merchantService.deleteMerchant(merchantId);
				logger.debug("After Calling merchantService.deleteMerchant()");

				if (responseDTO.getCode().equalsIgnoreCase("000")) { 
					message=responseDTO.getMessage();
					mav.addObject(SUCCESS_STATUS, message);
					mav.setViewName(MERCHANT_CONFIG);
					mav.addObject(DELETE_BOX, new Merchant());
				}	
				
				else if(responseDTO.getCode().equalsIgnoreCase("MER010")) {
					message=responseDTO.getMessage();
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(MERCHANT_CONFIG);
					mav.addObject(DELETE_BOX, new Merchant());
				}
				else
				{
					message=responseDTO.getMessage();
					logger.info("failure message in delete merchant"+message);
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(MERCHANT_CONFIG);
					mav.addObject(DELETE_BOX, new Merchant());
				}
			/**
			 * displaying remaining things after delete starts here 
			 */

			if (searchType != null && searchedName!=null && 
					!searchedName.isEmpty() && searchType.equalsIgnoreCase("byName")) {
				logger.info("Before Calling merchantService.getMerchantsByName() with name "+searchedName);
				merchantList = merchantService.getMerchantsByName(searchedName);
				logger.debug("After Calling merchantService.getMerchantsByName()");
			} else {
				logger.debug("Before Calling merchantService.getAllMerchants()");
				merchantList = merchantService.getAllMerchants();
				logger.debug("After Calling merchantService.getAllMerchants()");
			}

			mav.addObject(MERCHANT_TABLE_LIST, merchantList);
			mav.addObject(SHOW_GRID, "true");

			

			logger.info("Response Message "+responseDTO.getMessage());
			mer.setMerchantName(searchedName);
			mav.addObject(MERHCHANT_FORM, new MerchantDTO());
			mav.addObject(DELETE_BOX, new Merchant());

		logger.debug("EXIT");
		return mav;
	}

	
	
	
	
	
	
	
	@PreAuthorize("hasRole('ADD_MERCHANT_PRODUCT_LINK')")
	@RequestMapping("/showAssignProductToMerchant")
	public ModelAndView showAssignProductToMerchant(Map<String, Object> model,
			@ModelAttribute(MERCHANT_TO_PRODUCT_FORM) MerchantProduct merchantToProductForm, 
			HttpServletRequest request) throws ServiceException, ProductException, MerchantException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		List<MerchantDTO> merchantList = null;
		List<ProductDTO> productList = null;
		
		logger.debug("before  calling productService.getAllProducts()");
		
		productList = productService.getAllProducts();
			
		merchantList = merchantService.getAllMerchants();
			
				
		logger.debug("after calling issuerservice.getAllMerchants()");
		
			mav.addObject(MERCHANT_TO_PRODUCT_FORM, new MerchantProduct());
			mav.setViewName(SHOW_ASSIGN_PROD_TO_MERCH);
			mav.addObject(PRODUCT_DROP_DWN, productList);
			mav.addObject(MERCHANT_DROPDWN, merchantList);
	
		logger.debug("EXIT");
		return mav;
	}
	
	
	
	/**
	 * search assign merchant to product
	 * @param exception
	 * @return
	 */
	@PreAuthorize("hasRole('SEARCH_MERCHANT_PRODUCT_LINK')")
	@RequestMapping("/assignMerchantConfig")
	public ModelAndView assignMerchantConfiguration(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
	
		logger.debug(ENTER);
		HttpSession session = request.getSession();
		session.setAttribute(INS_USER, insUser);
		ModelAndView mav = new ModelAndView();
		mav.addObject(MERCHANT_TO_PRODUCT_FORM, new MerchantProduct());
		mav.addObject(DELETE_BOX, new MerchantProduct());
		
		mav.setViewName(ASSIGN_MERCHANT_CNFG);
		logger.debug("EXIT");
		return mav;
	}
	
	/**
	 * for displaying merchant names linked with the productNames
	 * @param exception
	 * @return
	 */
	@PreAuthorize("hasRole('SEARCH_MERCHANT_PRODUCT_LINK')")
	@RequestMapping("/showAllMerchantsLinkedWithProducts")
	public ModelAndView showAllMerchantsLinkedWithProducts(Map<String, Object> model,
		    @Validated(MerchantProduct.ValidationMerchantSearch.class) @ModelAttribute("merchantToProductForm") MerchantProduct merchantToProductForm, BindingResult bindingResult,
HttpServletRequest request) throws ServiceException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		List<MerchantProductDTO> merchantProductList = null;
		
			 mav.setViewName(ASSIGN_MERCHANT_CNFG);
			 if(bindingResult.hasErrors())
             {
                 mav.setViewName(ASSIGN_MERCHANT_CNFG);
                 mav.addObject(DELETE_BOX, new MerchantProduct());
                 return mav;
             }

		
			logger.debug("Before calling merchantService.getAllMerchantsLinkedWithProducts()");
			
						
			merchantProductList = merchantService.getAllMerchantsLinkedWithProducts(merchantToProductForm);
			logger.debug("after calling merchantService.getAllMerchantsLinkedWithProducts()");
			
			mav.addObject(MERCHANT_TO_PRODUCT_FORM, new MerchantProduct());
			mav.setViewName(ASSIGN_MERCHANT_CNFG);
			mav.addObject("merchantProductTableList", merchantProductList);
			mav.addObject(MERCHANT_TO_PRODUCT_FORM, merchantToProductForm);
			mav.addObject(SHOW_GRID, "true");
			mav.addObject(DELETE_BOX, new MerchantProduct());
			
		logger.debug("EXIT");
		return mav;
	}

	
	
	@PreAuthorize("hasRole('ADD_MERCHANT_PRODUCT_LINK')")
	@RequestMapping("/assignProductToMerchant")
	public ModelAndView assignProductToMerchant(@ModelAttribute(MERCHANT_TO_PRODUCT_FORM) MerchantProduct merchantToProductForm, 
			HttpServletRequest request,BindingResult bindingResult) throws ServiceException, MerchantException, ProductException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MerchantProductDTO merToProdDTO = new MerchantProductDTO();
		ResponseDTO responseDTO = null;
		Long insuser=null;
		String merchantData="";
		String productData="";
		Long merchantId=null;
		Long productId=null;
		String merchantName="";
		String productName="";
		String [] merchantDataToSplit=null;
		String [] productDataToSplit=null;
		String message="";
		
		 validateMerchantProduct.validate(merchantToProductForm, bindingResult);
	        
	        

		
		
		 insuser= (Long) session.getAttribute(INS_USER);
		
		
		
		merchantData=merchantToProductForm.getMerchantName();
		productData=merchantToProductForm.getProductName();
		
		
		
		if(merchantData!=null && merchantData.endsWith(","))
		{
			merchantData = merchantData.substring(0,merchantData.length() - 1);
			
		}
		
		if(productData!=null && productData.endsWith(","))
		{
			productData = productData.substring(0,productData.length() - 1);
		}
		
		
		if(!Objects.isNull(merchantData)) {
			merchantDataToSplit=merchantData.split("~");
		
			merchantId=Long.parseLong(merchantDataToSplit[0]);
			merchantName=merchantDataToSplit[1];
		}
		
		if(!Objects.isNull(productData)) {
			productDataToSplit=productData.split("~");
			productId=Long.parseLong(productDataToSplit[0]);
			productName=productDataToSplit[1];
		}
		
		
		merToProdDTO.setMerchantId(merchantId);
		merToProdDTO.setProductId(productId);
		merToProdDTO.setMerchantName(merchantName);
		merToProdDTO.setProductName(productName);
		merToProdDTO.setInsUser(insuser);
		merToProdDTO.setInsDate(new Date());
		merToProdDTO.setLastUpdDate(new Date());
		merToProdDTO.setLastUpdUser(insuser);
		
		logger.debug("before calling productService.getAllProducts()");
		if(bindingResult.hasErrors()) {
			
			merchantToProductForm.setProductId(productId);
			merchantToProductForm.setMerchantId(merchantId);
			merchantToProductForm.setMerchantName(merchantName);
			merchantToProductForm.setProductName(productName);
	        mav.addObject(PRODUCT_DROP_DWN,productService.getAllProducts());
	        mav.addObject(MERCHANT_DROPDWN,merchantService.getAllMerchants());
	        mav.setViewName(SHOW_ASSIGN_PROD_TO_MERCH);
	        return mav;
	        }
			
		responseDTO=merchantService.updateAssignProductToMerchant(merToProdDTO);
			
				
		logger.debug("after calling merchantService.updateAssignProductToMerchant()");
		
		if (responseDTO.getCode().equalsIgnoreCase("000")) { 
			message=responseDTO.getMessage();
			
			logger.info("after calling merchantService.updateAssignProductToMerchant"+message);
			mav.addObject(SUCCESS_STATUS, message);
			mav.setViewName(ASSIGN_MERCHANT_CNFG);
			mav.addObject(DELETE_BOX, new MerchantProduct());
			mav.addObject(MERCHANT_TO_PRODUCT_FORM, new MerchantProduct());
		}	
		else
		{
			message=responseDTO.getMessage();
			
			mav.addObject(FAIL_STATUS, message);
			merchantToProductForm.setProductId(productId);
			merchantToProductForm.setMerchantId(merchantId);
			merchantToProductForm.setMerchantName(merchantName);
			merchantToProductForm.setProductName(productName);
	        mav.addObject(PRODUCT_DROP_DWN,productService.getAllProducts());
	        mav.addObject(MERCHANT_DROPDWN,merchantService.getAllMerchants());
			mav.addObject(MERCHANT_TO_PRODUCT_FORM, merchantToProductForm);
			mav.setViewName(SHOW_ASSIGN_PROD_TO_MERCH);
		}
		
			
			
	
		logger.debug("EXIT");
		return mav;
	}

	@PreAuthorize("hasRole('DELETE_MERCHANT_PRODUCT_LINK')")
	@RequestMapping("/deleteProductMerchant")
	public ModelAndView deleteProductMerchant(@ModelAttribute(DELETE_BOX) MerchantProduct merchantToProductForm,
			HttpServletRequest request) throws ServiceException {

		logger.debug(ENTER);
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MerchantProductDTO merProd = new MerchantProductDTO();
		ResponseDTO responseDTO = null;
		Long merchantId=null;
		Long productId=null;
		String merchantName="";
		String productName="";
		String message="";
		
		insUser= (Long) session.getAttribute(INS_USER);
		
		if(request.getParameter(MERCHANT_ID)!=null && request.getParameter(PRODUCT_ID)!=null)
		{
		merchantId=Long.parseLong(request.getParameter(MERCHANT_ID));
		productId=Long.parseLong(request.getParameter(PRODUCT_ID));
		
		}
		merchantName=merchantToProductForm.getMerchantName();
		productName=merchantToProductForm.getProductName();
		
			
			merProd.setMerchantId(merchantId);
			merProd.setProductId(productId);
			merProd.setMerchantName(merchantName);
			merProd.setProductName(productName);
			merProd.setInsUser(insUser);
			merProd.setInsDate(new Date());
			merProd.setLastUpdDate(new Date());
			merProd.setLastUpdUser(insUser);
			
				
			logger.debug("Before Calling merchantService.deleteMerchant()");
				responseDTO = merchantService.deleteProductMerchant(merProd);
				logger.debug("After Calling merchantService.deleteMerchant()");

				if (responseDTO.getCode().equalsIgnoreCase("000")) { 
					message=responseDTO.getMessage();
					mav.addObject(SUCCESS_STATUS, message);
					mav.setViewName(ASSIGN_MERCHANT_CNFG);
					mav.addObject(DELETE_BOX, new MerchantProduct());
					mav.addObject(MERCHANT_TO_PRODUCT_FORM, new MerchantProduct());
				}	
				else
				{
					message=responseDTO.getMessage();
					mav.addObject(FAIL_STATUS, message);
					mav.setViewName(ASSIGN_MERCHANT_CNFG);
					mav.addObject(DELETE_BOX, new MerchantProduct());
					mav.addObject(MERCHANT_TO_PRODUCT_FORM, new MerchantProduct());
				}


		logger.debug("EXIT");
		return mav;
	}
	/**
	 * For View Merchant
	 * @param exception
	 * @return
	 */
	@PreAuthorize("hasRole('VIEW_MERCHANT')")
	@RequestMapping("/showViewMerchant")
	public ModelAndView showViewMerchant(Map<String, Object> model, HttpServletRequest request)
			throws ServiceException {

		logger.debug(ENTER);
		long merchantId=0;
		String id="";
		Merchant merchant =null;
		ModelAndView mav = new ModelAndView();
		try {
			
			id = request.getParameter(MERCHANT_ID);
			merchantId = Long.parseLong(id);
			mav.setViewName(VIEW_MERCHANT);
			logger.info("merchantId  :: "+merchantId);
			logger.debug("Before Calling merchantService.getMerchantById() in view merchant ");
			merchant = merchantService.getMerchantById(merchantId);
			logger.debug("After Calling merchantService.getMerchantById() in view merchant");
			mav.addObject(MERHCHANT_FORM, merchant);
			mav.addObject(VIEW_MERCHANT_ID, id);
			mav.setViewName(VIEW_MERCHANT);
			mav.addObject(CONFIRM_BOX, new Merchant());
			mav.addObject(MERCHANT_ID, merchantId);
			

		} catch (NumberFormatException e) {
			mav.setViewName(VIEW_MERCHANT);
			logger.error("Exception Occured While Viewing  Merchant in showViewMerchant()"+e);       
			 }
		logger.debug("EXIT");
		return mav;
	}
	
	@PreAuthorize("hasRole('VIEW_MERCHANT_PRODUCT_LINK')")
	@RequestMapping("/viewAssignProductToMerchant")
	public ModelAndView viewAssignProductToMerchant(Map<String, Object> model, HttpServletRequest request)
			throws ServiceException, MerchantException {

		logger.debug(CCLPConstants.ENTER);
		Long merchantId = null;
		Long productId = null;
		MerchantProduct merchantproductForm = new MerchantProduct();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("viewAssignProductToMerchant");
		try {

			if (request.getParameter(MERCHANT_ID) != null && request.getParameter(PRODUCT_ID) != null) {
				merchantId = Long.parseLong(request.getParameter(MERCHANT_ID));
				productId = Long.parseLong(request.getParameter(PRODUCT_ID));

			}

			merchantproductForm.setMerchantId(merchantId);
			merchantproductForm.setProductId(productId);

			Map<Long, String> parentProductMap = productService.getParentProducts();
			mav.addObject(PRODUCT_DROP_DWN, parentProductMap);
			mav.addObject(MERCHANT_DROPDWN, merchantService.getAllMerchants());
			mav.addObject(MERCHANT_TO_PRODUCT_FORM, merchantproductForm);
			mav.addObject(MERCHANT_ID, merchantId);

		} catch (NumberFormatException e) {
			mav.setViewName("viewAssignProductToMerchant");
			logger.error("Exception Occured While Viewing  Merchant in showViewMerchant()" + e);
		}
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}
	

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug("Exception for merchant starts here");
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || 
				exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("statusMessage", errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug("exception for merchant ends here");

		return mav;
	}


}
