package com.incomm.cclpvms.config.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.incomm.cclpvms.config.model.CardRange;
import com.incomm.cclpvms.config.model.CardRangeDTO;
import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.DeliveryChannel;
import com.incomm.cclpvms.config.model.Issuer;
import com.incomm.cclpvms.config.model.IssuerDTO;
import com.incomm.cclpvms.config.model.Limitmap;
import com.incomm.cclpvms.config.model.PackageDTO;
import com.incomm.cclpvms.config.model.Partner;
import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.ProductAlert;
import com.incomm.cclpvms.config.model.ProductDTO;
import com.incomm.cclpvms.config.model.ProgramIDDTO;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.maintenanceFee;
import com.incomm.cclpvms.config.model.monthlyFeeCapFee;
import com.incomm.cclpvms.config.model.transactionFee;
import com.incomm.cclpvms.config.service.CardRangeService;
import com.incomm.cclpvms.config.service.GlobalParameterService;
import com.incomm.cclpvms.config.service.IssuerService;
import com.incomm.cclpvms.config.service.PackageService;
import com.incomm.cclpvms.config.service.PartnerService;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.service.ProgramIdService;
import com.incomm.cclpvms.config.service.PurseService;
import com.incomm.cclpvms.config.service.impl.PdfGenerator;
import com.incomm.cclpvms.config.validator.ValidateProductAlerts;
import com.incomm.cclpvms.config.validator.ValidateProductEdit;
import com.incomm.cclpvms.config.validator.ValidateProductImpl;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.IssuerException;
import com.incomm.cclpvms.exception.ProductException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.CronSchedule;
import com.incomm.cclpvms.util.CronUtil;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Controller
@RequestMapping("/config/product")
public class ProductController {
	private static final Logger logger = LogManager.getLogger(ProductController.class);

	HttpSession session;

	@Autowired
	public IssuerService issuerService;

	@Autowired
	public ProductService productService;

	@Autowired
	public CardRangeService cardRangeService;

	@Autowired
	public PartnerService partnerservice;

	@Autowired
	public PackageService packageService;

	@Autowired
	public PurseService purseService;

	@Autowired
	SessionService sessionService;

	@Autowired
	public ValidateProductImpl customValidator;

	@Autowired
	public ValidateProductEdit customValidatorEdit;

	@Autowired
	public ValidateProductAlerts customValidatorAlert;

	@Autowired
	public GlobalParameterService globalParameterService;

	@Autowired
	private ProgramIdService programIdService;

	// Pdf generation MetaData
	@Value("#{${languageDesc}}")
	private Map<String, String> languageDesc;

	@Value("#{${mainFeeMap}}")
	private Map<String, String> mainFeeMap;

	@Value("#{${monthlyfeeCapMap}}")
	private Map<String, String> monthlyfeeCapMap;
	
	@Value("#{${productPurse}}")
	private Map<String, String> productPurseAttributes;

	@PreAuthorize("hasRole('SEARCH_PRODUCT')")
	@RequestMapping("/productConfig")
	public ModelAndView productConfiguration(HttpServletRequest request) {
		session = request.getSession();
		long insUser = sessionService.getUserId();
		session.setAttribute("insUser", insUser);
		ModelAndView mav;

		logger.info(CCLPConstants.ENTER);
		mav = new ModelAndView();
		mav.addObject("productForm", new Product());
		mav.setViewName("productConfig");
		logger.info(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('SEARCH_PRODUCT')")
	@RequestMapping("/showAllProducts")
	public ModelAndView showAllProducts(Map<String, Object> model, @ModelAttribute("productForm") Product productForm,
			HttpServletRequest request) throws ServiceException, ProductException {

		ModelAndView mav = new ModelAndView();
		logger.info("showAllProducts Method Starts Here");
		List<ProductDTO> prodList = null;
		prodList = productService.getAllProducts();
		mav.addObject("productForm", productForm);
		mav.setViewName("productConfig");
		mav.addObject(CCLPConstants.SHOW_GRID, "true");
		model.put("productTableList", prodList);
		return mav;
	}

	/**
	 * for searching product By name
	 */
	@PreAuthorize("hasRole('SEARCH_PRODUCT')")
	@RequestMapping("/searchProductByName")
	public ModelAndView searchProductByName(Map<String, Object> model,
			@Validated(Product.ValidationStepOne.class) @ModelAttribute("productForm") Product productForm,
			BindingResult bindingresult, HttpServletRequest request) throws ServiceException, ProductException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		String productName = "";
		String searchType = "";

		List<ProductDTO> productDtoList = null;
		mav.setViewName("productConfig");
		searchType = request.getParameter("searchType");
		mav.addObject("SearchType", searchType);

		logger.debug("Before calling productService.getAllIssuers()");

		if (bindingresult.hasErrors()) {
			return mav;
		}

		productName = productForm.getProductName();
		productDtoList = productService.getProductsByName(productName);

		logger.debug("after calling productService.getProductsByName()");

		mav.addObject("productForm", new Product());
		mav.setViewName("productConfig");
		mav.addObject("productTableList", productDtoList);
		mav.addObject("productForm", productForm);
		mav.addObject("showGrid", "true");
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * for searching product By name
	 */
	@PreAuthorize("hasRole('SEARCH_PRODUCT')")
	@RequestMapping("/searchProductByNameForCopy")
	public ModelAndView searchProductByNameForCopy(Map<String, Object> model,
			@ModelAttribute("productForm") Product productForm, BindingResult bindingresult, HttpServletRequest request)
			throws ServiceException, ProductException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		String productName = "";
		String searchType = "";

		List<ProductDTO> productDtoList = null;
		mav.setViewName("productConfig");
		searchType = request.getParameter("searchType");
		mav.addObject("SearchType", searchType);

		logger.debug("Before calling productService.getAllProducts()");

		if (bindingresult.hasErrors()) {
			return mav;
		}

		productName = productForm.getProductName();
		productDtoList = productService.getProductsByNameForCopy(productName);

		logger.debug("after calling productService.getProductsByName()");

		mav.addObject("productForm", new Product());
		mav.setViewName("productConfig");
		mav.addObject("productTableList", productDtoList);
		mav.addObject("productForm", productForm);
		mav.addObject("showGrid", "true");
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('ADD_PRODUCT')")
	@RequestMapping("/showAddProduct")
	public ModelAndView showAddProduct(@ModelAttribute("productForm") Product product, HttpServletRequest req)
			throws ServiceException, ProductException {
		ModelAndView mav = new ModelAndView();
		List<String> cardRangeList = null;
		List<Partner> partnerList = null;
		List<IssuerDTO> issuerList = null;
		List<ProductDTO> productList = null;
		List<PackageDTO> packageList = null;
		List<PurseDTO> purseList = null;

		Map<String, List> ccfList = null;

		String addUrl = Util.constructUrl(req);

		mav.addObject("addUrl", addUrl);

		List<String> validityList = new ArrayList<>();

		validityList.add("Hours");
		validityList.add("Days");
		validityList.add("Months");
		validityList.add("Years");

		mav.addObject("validityList", validityList);

		Map<String, Object> globalParamData = globalParameterService.getGlobalParameters();

		if (globalParamData != null && globalParamData.get("defaultIssuer") != null) {

			String defaultIssuer = (String) globalParamData.get("defaultIssuer");
			String defaultIssuerName;

			Long defaultIssuerId = Long.parseLong(defaultIssuer);

			Issuer issuerpojo = issuerService.getIssuerById(defaultIssuerId);
			defaultIssuerName = issuerpojo.getIssuerName();
			mav.addObject("defaultIssuer", defaultIssuerName);

		}

		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {

			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		try {

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			packageList = packageService.getAllPackages();
			purseList = purseService.getAllPurses();
			ccfList = productService.getCCFList();

			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			//mav.addObject("purseData", purseList);

			mav.addObject("productForm", new Product());
			mav.setViewName("addProduct");
		} catch (IssuerException e) {
			logger.error("NumberFormatException Occured While Editing Product in addProduct()" + e);
		}

		return mav;
	}

	@RequestMapping("/getProdAttributes")
	public ModelAndView getProdAttributes() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("productForm", new Product());
		mav.setViewName("addProduct");
		return mav;
	}

	@PreAuthorize("hasRole('ADD_PRODUCT')")
	@RequestMapping("/addProduct")
	public ModelAndView addProduct(
			@Validated({ Product.ValidationStepTwo.class }) @ModelAttribute("productForm") Product productForm,
			BindingResult bindingResult, HttpServletRequest request)
			throws ServiceException, ProductException, IssuerException {

		Long parentProductId = Long.parseLong(
				(request.getParameter("productId").equals("") ? "-1" : request.getParameter("productId")).toString());

		ModelAndView mav = new ModelAndView();
		Long productId;

		productForm.setProductId(null);

		ProductDTO productDTO = new ProductDTO();
		productDTO.setParentProductId(parentProductId);
		ResponseDTO response ;
		String message = "";
		List<String> cardRangeList = null;
		List<String> partnerCurrencies = new ArrayList<>();

		List<Partner> partnerList = null;
		List<IssuerDTO> issuerList = null;
		List<ProductDTO> productList = null;
		List<PackageDTO> packageList = null;
		List<PurseDTO> purseList = null;
		Map<String, List> ccfList = null;
		List<ProgramIDDTO> programIdList = null;
		List<CurrencyCodeDTO> availableCurrencyCodeList = null;

		List<ProductDTO> prodList = null;
		Map prodMap = new HashMap<>();

		List<String> cardRangeitems = null;

		List<String> cards;

		List<String> purses;

		List<String> packages;

		new CardRange();

		String addUrl = Util.constructUrl(request);

		mav.addObject("addUrl", addUrl);

		List<String> validityList = new ArrayList<>();

		validityList.add("Hours");
		validityList.add("Days");
		validityList.add("Months");
		validityList.add("Years");

		mav.addObject("validityList", validityList);

		Map<String, Map<String, String>> attributes = new HashMap<>();
		mav.setViewName("addProduct");

		Map<String, String> prodAttributes = productForm.getProdAttributes();
		String b2bUpc = prodAttributes.containsKey("b2bUpc") ? prodAttributes.get("b2bUpc") : null;

		String initialQuantity = (prodAttributes.containsKey("b2bInitSerialNumQty")
				&& !Util.isEmpty(prodAttributes.get("b2bInitSerialNumQty"))) ? prodAttributes.get("b2bInitSerialNumQty")
						: "0";
		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {

			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		attributes.put("Product", prodAttributes);
		List<String> purseWithTypeId = productForm.getSupportedPurseUpdate();
		List<String> purseIdList = new ArrayList<>();
		for (int j = 0; j < purseWithTypeId.size(); j++) {
			
			purseIdList.add(purseWithTypeId.get(j).split("~")[1]);
		}
		productForm.setSupportedPurseUpdate(purseIdList);
		customValidatorEdit.validate(productForm, bindingResult);

		if (bindingResult.hasErrors()) {

			addUrl = Util.constructUrl(request);

			mav.addObject("addUrl", addUrl);

			try {
				cardRangeList = cardRangeService.getAllCardRanges();
				partnerList = partnerservice.getAllPartners();
				issuerList = issuerService.getAllIssuers();
				productList = productService.getAllProducts();
				packageList = packageService.getAllPackages();
				purseList = purseService.getAllPurses();
				ccfList = productService.getCCFList();

				String partnerData = productForm.getPartnerName();
				String[] partnerdatatoSplit = partnerData.split("~");
				if (partnerdatatoSplit[0] != null && !partnerdatatoSplit[0].equalsIgnoreCase("NONE")) {
					productForm.setPartnerId(Long.parseLong(partnerdatatoSplit[0]));
					productForm.setPartnerName(partnerdatatoSplit[1]);
				}

				String issuerData = productForm.getIssuerName();
				String[] issuerdatatoSplit = issuerData.split("~");
				if (issuerdatatoSplit[0] != null && !issuerdatatoSplit[0].equalsIgnoreCase("NONE")) {
					productForm.setIssuerId(Long.parseLong(issuerdatatoSplit[0]));
					productForm.setIssuerName(issuerdatatoSplit[1]);
				}

				cardRangeitems = productForm.getCardRangesUpdate();

				List<Long> cardRangeIds = new ArrayList<>();

				List<Long> purseIds = new ArrayList<>();

				List<String> packageIds = new ArrayList<>();

				productForm.getCardRangesUpdate();

				for (int i = 0; i < cardRangeitems.size(); i++) {
					cardRangeIds.add(Long.parseLong(cardRangeitems.get(i)));
				}

				cards = cardRangeService.getCardRangebyCardId(cardRangeIds);

				productForm.setCardRanges(cards);

				/* added by nawaz for smart search */
				if (productForm.getParentProductName() != null && !productForm.getParentProductName().isEmpty()) {

					prodList = productService
							.getProductsByNameForCopy(productForm.getParentProductName().split(":")[1]);

					prodMap = (Map) prodList.get(0);
					productForm.setParentProductId(Long.parseLong(prodMap.get("productId").toString()));

				}

				List<String> purseItemsList = productForm.getSupportedPurseUpdate();

				for (int j = 0; j < purseItemsList.size(); j++) {
					
					purseIds.add(Long.parseLong(purseItemsList.get(j)));
				}

				purses = purseService.getPurseByIds(purseIds);

				productForm.setSupportedPurse(purses);

				productForm.setPartnerCurrency(partnerCurrencies);

				List<String> packageItemsList = productForm.getPackageUpdate();

				for (int j = 0; j < packageItemsList.size(); j++) {
					packageIds.add(packageItemsList.get(j));
				}

				packages = packageService.getPackageByIds(packageIds);

				productForm.setPackageId(packages);

				Map<String, String> temMap1 = productForm.getProdAttributes();

				if (temMap1.get("validityPeriodFormat") != null) {
					mav.addObject("validityPeriodFormat", temMap1.get("validityPeriodFormat"));

				}

			} catch (IssuerException e) {
				logger.error("IssuerException" + e);
			}

			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseList);

			mav.addObject("productForm", productForm);
			mav.setViewName("addProduct");

			return mav;
		}

		String issuerData = productForm.getIssuerName();
		String partnerData = productForm.getPartnerName();
		String[] issuerdatatoSplit = issuerData.split("~");

		productForm.setIssuerId(Long.parseLong(issuerdatatoSplit[0]));
		productForm.setIssuerName(issuerdatatoSplit[1]);

		String[] partnerdatatoSplit = partnerData.split("~");

		productForm.setPartnerId(Long.parseLong(partnerdatatoSplit[0]));
		productForm.setPartnerName(partnerdatatoSplit[1]);

		String parentProductName = productForm.getParentProductName();
		// 26-07-2018
		if (parentProductName != null && !parentProductName.isEmpty() && parentProductName.equalsIgnoreCase("")) {
			prodList = productService.getProductsByNameForCopy(parentProductName);
			if (prodList != null) {
				prodMap = (Map) prodList.get(0);
				productForm.setParentProductId(Long.parseLong(prodMap.get("productId").toString()));
			}

		}

		String description = productForm.getDescription();
		String productShortName = productForm.getProductShortName();
		prodAttributes = productForm.getProdAttributes();
		attributes.put("Product", prodAttributes);

		productForm.getCardRangesUpdate();

		StringBuilder otherTxnStr = new StringBuilder();
		StringBuilder activationStr = new StringBuilder();

		String[] otherTxnStrarr = productForm.getTxnTypeId();
		String[] activationStrArr = productForm.getActivationId();

		for (int i = 0; i < otherTxnStrarr.length; i++) {
			otherTxnStr.append(otherTxnStrarr[i]);
			if (i < otherTxnStrarr.length - 1) {
				otherTxnStr.append(",");
			}

		}
		for (int i = 0; i < activationStrArr.length; i++) {
			activationStr.append(activationStrArr[i]);
			if (i < activationStrArr.length - 1) {
				activationStr.append(",");
			}

		}
		prodAttributes.put(CCLPConstants.OTHER_TXN_ID, otherTxnStr.toString());
		prodAttributes.put(CCLPConstants.ACTIVATION_ID, activationStr.toString());

		productDTO.setIssuerId(productForm.getIssuerId());

		productDTO.setPartnerId(productForm.getPartnerId());
		productDTO.setIssuerName(productForm.getIssuerName());
		productDTO.setPartnerName(productForm.getPartnerName());
		productDTO.setProgramId(productForm.getProgramId());
		productDTO.setProductName(productForm.getProductName());
		productDTO.setUpc(productForm.getUpc());
		productDTO.setCardRanges(productForm.getCardRangesUpdate());
		productDTO.setSupportedPurse(productForm.getSupportedPurseUpdate());
		productDTO.setPartnerCurrency(productForm.getPartnerCurrencyUpdate());
		productDTO.setPackageId(productForm.getPackageUpdate());
		productDTO.setAttributesMap(attributes);
		productDTO.setAttributes(attributes.toString());
		productDTO.setProductShortName(productShortName);

		productDTO.setParentProductName(productForm.getParentProductName());
		// 10.11.2018

		if (productForm.getCopyAllCheck() == null) {
			productDTO.setParentProductId(null);
		} else {
			productDTO.setParentProductId(Long
					.parseLong((request.getParameter("productId").equals("") ? "-1" : request.getParameter("productId"))
							.toString()));
		}
		String flag = "y";
		productDTO.setIsActive(flag);
		productDTO.setDescription(description);

		productDTO.setInsUser(sessionService.getUserId());
		productDTO.setInsDate(new Date());
		productDTO.setLastUpdDate(new Date());
		productDTO.setLastUpdUser(sessionService.getUserId());

		productDTO.setCopyAllCheck(productForm.getCopyAllCheck());

		response = productService.createProduct(productDTO);

		if (productDTO.getCopyAllCheck() == null && response.getMessage().equalsIgnoreCase("CREATED SUCCESSFULLY")) {
			message = "PRODUCT " + "\"" + productForm.getProductName() + "\" "
					+ ResourceBundleUtil.getMessage(ResponseMessages.SUCCESS_PRODUCT_CREATE);
			mav.addObject("successstatus", message);
			mav.addObject("productForm", new Product());
			mav.setViewName("productConfig");

		} else if (productDTO.getCopyAllCheck() != null && productDTO.getCopyAllCheck().equalsIgnoreCase("Y")
				&& response.getMessage().equalsIgnoreCase("CREATED SUCCESSFULLY")) {

			logger.info("Reached Inside copyAll");
			String pinTab = "";
			String purseTab ;
			String cvvTab = "";
			String limitsTab = "";
			String txnFeesTab = "";
			String alertsTab = "";
			String txnBasedonCardStatTab = "";
			String maintenanceFeesTab = "";
			List<PurseDTO> purseDTOList = null;
			Product product = null;
			List<CardRangeDTO> availableCardRangeList = null;
			Map<String, String> headerData = new HashMap<>();

			mav.addObject("productConfirmBox", new Product());
			session = request.getSession();

			mav.setViewName("editProduct");

			prodList = productService.getProductsByNameForCopy(productDTO.getProductName());
			prodMap = (Map) prodList.get(0);
			productForm.setParentProductId(Long.parseLong(prodMap.get("productId").toString()));
			productId = Long.parseLong(prodMap.get("productId").toString());

			session.setAttribute("productId", productId);
			logger.info("Product Id is {} ", productId);
			logger.debug("Before Calling the issuerservice.getProductId()  ");
			product = productService.getProductById(productId);

			logger.debug("After Calling issuerservice.getProductId() ");

			Map<String, String> temMap = product.getProdAttributes();

			if (temMap != null) {
				if (temMap.get("validityPeriodFormat") != null) {
					mav.addObject("validityPeriodFormat", temMap.get("validityPeriodFormat"));
				}
				if (temMap.get("denomSelect") != null) {
					mav.addObject("selectedDenom", temMap.get("denomSelect"));
				}
				if (temMap.get("otherTxnId") != null) {
					product.setTxnTypeId(temMap.get("otherTxnId").split(","));
				}
				if (temMap.get("activationId") != null) {
					product.setActivationId(temMap.get("activationId").split(","));
				}
				if (temMap.containsKey("alertSupported") && !Objects.isNull(temMap.get("alertSupported"))) {
					alertsTab = temMap.get("alertSupported");
					headerData.put("AlertsTab", alertsTab);
				}
				if (temMap.containsKey("cvvSupported") && !Objects.isNull(temMap.get("cvvSupported"))) {
					cvvTab = temMap.get("cvvSupported");
					headerData.put("CVVTab", cvvTab);
				}
				if (temMap.containsKey("feesSupported") && !Objects.isNull(temMap.get("feesSupported"))) {
					txnFeesTab = temMap.get("feesSupported");
					headerData.put("TxnFeesTab", txnFeesTab);
				}
				if (temMap.containsKey("pinSupported") && !Objects.isNull(temMap.get("pinSupported"))) {
					pinTab = temMap.get("pinSupported");
					headerData.put("PINTab", pinTab);
				}
				if (temMap.containsKey("multiPurseSupport") && !Objects.isNull(temMap.get("multiPurseSupport"))) {
					purseTab = temMap.get("multiPurseSupport");
					headerData.put("PurseTab", purseTab);
				}

				if (temMap.containsKey("limitSupported") && !Objects.isNull(temMap.get("limitSupported"))) {
					limitsTab = temMap.get("limitSupported");
					headerData.put("LimitsTab", limitsTab);
				}
				if (temMap.containsKey("cardStatusSupported") && !Objects.isNull(temMap.get("cardStatusSupported"))) {
					txnBasedonCardStatTab = temMap.get("cardStatusSupported");
					headerData.put("TxnBasedonCardStatTab", txnBasedonCardStatTab);
				}
				if (temMap.containsKey("maintainanceFeeSupported")
						&& !Objects.isNull(temMap.get("maintainanceFeeSupported"))) {
					maintenanceFeesTab = temMap.get("maintainanceFeeSupported");
					headerData.put("MaintenanceFeesTab", maintenanceFeesTab);
				}

			}
			mav.addObject("productForm", product);
			mav.addObject("editProductId", productId);

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseDTOList = purseService.getAllPurses();
			ccfList = productService.getCCFList();
			programIdList = programIdService.getProgramIdsByPartnerId(product.getPartnerId());

			availableCardRangeList = cardRangeService.getCardRangeByIssuerId(product.getIssuerId());

			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseDTOList);
			mav.addObject("programDropDown", programIdList);

			mav.addObject("availableCardRangeList", availableCardRangeList);

			session.setAttribute("headerData", headerData);

			mav.setViewName("editProduct");
			mav.addObject("messageAlertBox", new ProductAlert());

			message = "PRODUCT " + "\"" + productForm.getProductName() + "\" "
					+ ResourceBundleUtil.getMessage(ResponseMessages.SUCCESS_PRODUCT_CREATE);
			mav.addObject("successstatus", message);

		}
		/* ends */
		else {

			message = response.getMessage();
			mav.addObject("failstatus", message);
			addUrl = Util.constructUrl(request);

			mav.addObject("addUrl", addUrl);

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseList = purseService.getAllPurses();
			ccfList = productService.getCCFList();
			programIdList = programIdService.getProgramIdsByPartnerId(productForm.getPartnerId());

			availableCurrencyCodeList = productService.getCurrencyCodeList(productForm.getPartnerId());
			
			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseList);
			mav.addObject("programDropDown", programIdList);
			mav.addObject("availablePartnerCurrencyList", availableCurrencyCodeList);
			
			List<Long> purseIds = new ArrayList<>();

			List<String> packageIds = new ArrayList<>();

			List<Long> cardRangeIds = new ArrayList<>();

			List<String> purseItemsList = productForm.getSupportedPurseUpdate();

			for (int j = 0; j < purseItemsList.size(); j++) {
				purseIds.add(Long.parseLong(purseItemsList.get(j)));
			}

			purses = purseService.getPurseByIds(purseIds);

			productForm.setSupportedPurse(purses);

			cardRangeitems = productForm.getCardRangesUpdate();

			for (int i = 0; i < cardRangeitems.size(); i++) {
				cardRangeIds.add(Long.parseLong(cardRangeitems.get(i)));
			}

			cards = cardRangeService.getCardRangebyCardId(cardRangeIds);

			productForm.setCardRanges(cards);

			List<String> packageItemsList = productForm.getPackageUpdate();

			for (int j = 0; j < packageItemsList.size(); j++) {
				packageIds.add(packageItemsList.get(j));
			}

			packages = packageService.getPackageByIds(packageIds);

			productForm.setPackageId(packages);

			mav.addObject("productForm", productForm);

			mav.setViewName("addProduct");
		}
		prodList = productService.getProductsByNameForCopy(productDTO.getProductName());
	
		logger.info(prodList);
		prodMap = (Map) prodList.get(0);
		productId = Long.parseLong(Util.convertAsString(prodMap.get("productId")));
		if (!Util.isEmpty(initialQuantity) && !Util.isEmpty(b2bUpc)) {
			String response1 = productService.getSerialNumberResponse(productId, Util.convertAsString(b2bUpc),
					Long.parseLong(initialQuantity));
			logger.info("response1  serialnumber" + response1);
		}
		return mav;

	}

	@RequestMapping("/getProductDetails")
	public ModelAndView getProductDetails(Map<String, Object> model, @ModelAttribute("productForm") Product productForm,
			HttpServletRequest request) throws ServiceException, ProductException {

		logger.debug(CCLPConstants.ENTER);
		String productName;
		Long productId;
		Product product = null;
		List<String> cardRangeList = null;
		List<Partner> partnerList = null;
		List<IssuerDTO> issuerList = null;
		List<ProductDTO> productList = null;
		List<ProductDTO> prodList = null;
		List<PackageDTO> packageList = null;
		List<PurseDTO> purseList = null;
		List<ProgramIDDTO> programIdList = null;
		Map prodMap = new HashMap();
		Map<String, List> ccfList = null;
		
		List<CurrencyCodeDTO> availableCurrencyCodeList = null;

		ModelAndView mav = new ModelAndView();

		List<String> validityList = new ArrayList<>();

		validityList.add("Hours");
		validityList.add("Days");
		validityList.add("Months");
		validityList.add("Years");

		mav.addObject("validityList", validityList);

		Map<String, Object> globalParamData;
		globalParamData = globalParameterService.getGlobalParameters();

		if (globalParamData != null && globalParamData.get("defaultIssuer") != null) {

			String defaultIssuer = (String) globalParamData.get("defaultIssuer");
			String defaultIssuerName;

			Long defaultIssuerId = Long.parseLong(defaultIssuer);

			Issuer issuerpojo = issuerService.getIssuerById(defaultIssuerId);
			defaultIssuerName = issuerpojo.getIssuerName();
			mav.addObject("defaultIssuer", defaultIssuerName);

		}
		try {

			String copyAllCheck = "";
			String addUrl = Util.constructUrl(request);

			mav.addObject("addUrl", addUrl);
			copyAllCheck = productForm.getCopyAllCheck();

			productName = String.valueOf(productForm.getParentProductName());

			if (productName != null && productName.indexOf(':') != -1) {
				productName = productName.split(":")[1];
			}

			prodList = productService.getProductsByNameForCopy(productName);
			prodMap = (Map) prodList.get(0);
			productId = Long.parseLong(prodMap.get("productId").toString());
			mav.addObject("showCopyFrom", "true");
			mav.setViewName("addProduct");
			mav.addObject("addProductForm", productId);
			logger.info("Product Id :: " + productId);
			logger.debug("Before Calling issuerservice.getProductId() ");
			product = productService.getProductById(productId);
			logger.debug("After Calling issuerservice.getProductId() ");
			Map<Long, String> parentProductMap = productService.getParentProducts();

			if (parentProductMap != null && !parentProductMap.isEmpty()) {

				mav.addObject("parentProductMap", parentProductMap);
				mav.addObject("showCopyFrom", "true");

			}

			Map<String, String> temMap = product.getProdAttributes();

			if (temMap != null) {

				if (temMap.get("validityPeriodFormat") != null) {
					mav.addObject("validityPeriodFormat", temMap.get("validityPeriodFormat"));

				}

				if (temMap.containsKey("denomSelect")) {
					mav.addObject("selectedDenom", temMap.get("denomSelect"));
				}

				if (temMap.get("otherTxnId") != null) {

					product.setTxnTypeId(temMap.get("otherTxnId").split(","));

				}

				if (temMap.get("activationId") != null) {

					product.setActivationId(temMap.get("activationId").split(","));

				}
			}

			product.setParentProductName(productForm.getParentProductName());
			product.setParentProductId(productId);
			product.setCopyAllCheck(copyAllCheck);
			mav.addObject("productForm", product);

			product.getSupportedPurseUpdate();

			productForm.setSupportedPurse(product.getSupportedPurse());

			List<String> emptyList = new ArrayList<>();

			product.setCardRanges(emptyList);

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseList = partnerservice.getAllSupportedPurses(product.getPartnerId());
			ccfList = productService.getCCFList();
			programIdList = programIdService.getProgramIdsByPartnerId(product.getPartnerId());

			availableCurrencyCodeList = productService.getCurrencyCodeList(product.getPartnerId());
			
			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseList);
			mav.addObject("programDropDown", programIdList);
			mav.addObject("issuerFlag", "true");
			mav.addObject("availablePartnerCurrencyList", availableCurrencyCodeList);
			

			mav.setViewName("addProduct");

		} catch (NumberFormatException e) {
			mav.setViewName("addProduct");
			logger.error("NumberFormatException Occured While Editing Product in addProduct()" + e);
		} catch (IssuerException e) {
			logger.error("IssuerException" + e);
		}

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/showEditProduct")
	public ModelAndView showEditProduct(Map<String, Object> model, @ModelAttribute("productForm") Product productForm,
			HttpServletRequest request) throws ServiceException, ProductException {

		logger.debug(CCLPConstants.ENTER);

		Long productId;
		String id = "";
		Product product = null;
		List<String> cardRangeList = null;
		List<Partner> partnerList = null;
		List<IssuerDTO> issuerList = null;
		List<ProductDTO> productList = null;
		List<PackageDTO> packageList = null;
		List<PurseDTO> purseDTOList = new ArrayList<>();
		List<ProgramIDDTO> programIdList = null;

		List<CardRangeDTO> availableCardRangeList = null;
		List<CurrencyCodeDTO> availableCurrencyCodeList = null;


		Map<String, List> ccfList = null;

		Map<String, String> headerData = new HashMap<>();

		String pinTab = "";
		String purseTab;
		String cvvTab = "";
		String limitsTab = "";
		String txnFeesTab = "";
		String alertsTab = "";
		String txnBasedonCardStatTab = "";
		String maintenanceFeesTab = "";

		String addUrl = Util.constructUrl(request);

		ModelAndView mav = new ModelAndView();

		List<String> validityList = new ArrayList<>();

		validityList.add("Hours");
		validityList.add("Days");
		validityList.add("Months");
		validityList.add("Years");

		mav.addObject("validityList", validityList);

		mav.addObject("addUrl", addUrl);
		try {

			session = request.getSession();
			id = request.getParameter("productId");

			if (id == null || id.isEmpty()) {
				productId = (Long) session.getAttribute("productId");
			} else {
				productId = Long.parseLong(id);
			}
			mav.addObject("productConfirmBox", new Product());

			session.setAttribute("productId", productId);

			mav.setViewName("editProduct");

			logger.info("Product Id :: " + productId);
			logger.debug("Before Calling issuerservice.getProductId() ");
			product = productService.getProductById(productId);
			product.setProductName(product.getProductName());
			logger.debug("After Calling issuerservice.getProductId() ");

			Map<String, String> temMap = product.getProdAttributes();
			if (temMap != null) {

				if (temMap.get("validityPeriodFormat") != null) {
					mav.addObject("validityPeriodFormat", temMap.get("validityPeriodFormat"));

				}

				if (temMap.get("denomSelect") != null) {
					mav.addObject("selectedDenom", temMap.get("denomSelect"));
				}

				if (temMap.get("otherTxnId") != null) {

					product.setTxnTypeId(temMap.get("otherTxnId").split(","));

				}

				if (temMap.get("activationId") != null) {

					product.setActivationId(temMap.get("activationId").split(","));

				}

				if (temMap.containsKey("alertSupported") && !Objects.isNull(temMap.get("alertSupported"))) {
					alertsTab = temMap.get("alertSupported");
					headerData.put("AlertsTab", alertsTab);
				}

				if (temMap.containsKey("cvvSupported") && !Objects.isNull(temMap.get("cvvSupported"))) {
					cvvTab = temMap.get("cvvSupported");
					headerData.put("CVVTab", cvvTab);

				}

				if (temMap.containsKey("feesSupported") && !Objects.isNull(temMap.get("feesSupported"))) {
					txnFeesTab = temMap.get("feesSupported");
					headerData.put("TxnFeesTab", txnFeesTab);
				}

				if (temMap.containsKey("pinSupported") && !Objects.isNull(temMap.get("pinSupported"))) {
					pinTab = temMap.get("pinSupported");
					headerData.put("PINTab", pinTab);
				}
				
				if (temMap.containsKey("multiPurseSupport") && !Objects.isNull(temMap.get("multiPurseSupport"))) {
					purseTab = temMap.get("multiPurseSupport");
					headerData.put("PurseTab", purseTab);
				}

				if (temMap.containsKey("limitSupported") && !Objects.isNull(temMap.get("limitSupported"))) {
					limitsTab = temMap.get("limitSupported");
					headerData.put("LimitsTab", limitsTab);
				}
				if (temMap.containsKey("cardStatusSupported") && !Objects.isNull(temMap.get("cardStatusSupported"))) {
					txnBasedonCardStatTab = temMap.get("cardStatusSupported");
					headerData.put("TxnBasedonCardStatTab", txnBasedonCardStatTab);
				}

				if (temMap.containsKey("maintainanceFeeSupported")
						&& !Objects.isNull(temMap.get("maintainanceFeeSupported"))) {
					maintenanceFeesTab = temMap.get("maintainanceFeeSupported");
					headerData.put("MaintenanceFeesTab", maintenanceFeesTab);
				}

			}
			mav.addObject("productForm", product);
			mav.addObject("editProductId", id);

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseDTOList = partnerservice.getAllSupportedPurses(product.getPartnerId());
			ccfList = productService.getCCFList();
			programIdList = programIdService.getProgramIdsByPartnerId(product.getPartnerId());
			availableCardRangeList = cardRangeService.getCardRangeByIssuerId(product.getIssuerId());
			availableCurrencyCodeList = productService.getCurrencyCodeList(product.getPartnerId());
			
			
			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseDTOList);
			mav.addObject("programDropDown", programIdList);

			mav.addObject("availableCardRangeList", availableCardRangeList);
			mav.addObject("availablePartnerCurrencyList", availableCurrencyCodeList);

			session.setAttribute("headerData", headerData);

			mav.setViewName("editProduct");
			mav.addObject("messageAlertBox", new ProductAlert());

		} catch (NumberFormatException e) {
			mav.setViewName("editProduct");
			logger.error("NumberFormatException Occured While Editing Product in editProduct()" + e);
		} catch (IssuerException e) {
			logger.error("Exception Occured While Editing Product in editProduct()" + e);
		}

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * updating product
	 */
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/editProduct")
	public ModelAndView updateProduct(
			@Validated({ Product.ValidationStepTwo.class }) @ModelAttribute("productForm") Product productForm,
			BindingResult bindingResult, HttpServletRequest request)
			throws ServiceException, ProductException, IssuerException {
		logger.debug(CCLPConstants.ENTER);

		long productId = 0;
		String productName = "";
		String message = "";
		String flag = "Y";
		ProductDTO productDTO = new ProductDTO();
		ResponseDTO responseDTO = new ResponseDTO();
		List<String> cardRangeList = null;
		List<Partner> partnerList = null;
		List<IssuerDTO> issuerList = null;
		List<ProductDTO> productList = null;
		List<PackageDTO> packageList = null;
		List<PurseDTO> purseList = null;
		Map<String, List> ccfList = null;
		List<String> cardRangeitems;
		List<String> cards;
		List<String> purses;
		List<String> packages;
		List<CurrencyCodeDTO> availableCurrencyCodeList = null;
		

		String addUrl = Util.constructUrl(request);

		Map<String, Map<String, String>> attributes = new HashMap<>();
		ModelAndView mav = new ModelAndView();

		List<String> validityList = new ArrayList<>();

		validityList.add("Hours");
		validityList.add("Days");
		validityList.add("Months");
		validityList.add("Years");

		mav.addObject("validityList", validityList);

		mav.addObject("addUrl", addUrl);

		mav.addObject("productConfirmBox", new Product());
		if (session.getAttribute("productId") != null) {
			productId = Long.parseLong(session.getAttribute("productId").toString());
		}
		mav.addObject("editProductId", productId);

		List<String> purseWithTypeId = productForm.getSupportedPurseUpdate();
		List<String> purseIdList = new ArrayList<>();
		for (int j = 0; j < purseWithTypeId.size(); j++) {
			
			purseIdList.add(purseWithTypeId.get(j).split("~")[1]);
		}
		productForm.setSupportedPurseUpdate(purseIdList);
		productName = productForm.getProductName();

		customValidatorEdit.validate(productForm, bindingResult);
		if (bindingResult.hasErrors()) {

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseList = purseService.getAllPurses();
			ccfList = productService.getCCFList();

			/**
			 * added by nawaz fo return multi select display
			 */

			cardRangeitems = productForm.getCardRangesUpdate();

			List<Long> cardRangeIds = new ArrayList<>();

			List<Long> purseIds = new ArrayList<>();

			List<String> packageIds = new ArrayList<>();

			String partnerData = productForm.getPartnerName();
			String[] partnerdatatoSplit = partnerData.split("~");
			if (partnerdatatoSplit[0] != null && !partnerdatatoSplit[0].equalsIgnoreCase("NONE")) {
				productForm.setPartnerId(Long.parseLong(partnerdatatoSplit[0]));
				productForm.setPartnerName(partnerdatatoSplit[1]);
			}

			String issuerData = productForm.getIssuerName();
			String[] issuerdatatoSplit = issuerData.split("~");
			if (issuerdatatoSplit[0] != null && !issuerdatatoSplit[0].equalsIgnoreCase("NONE")) {
				productForm.setIssuerId(Long.parseLong(issuerdatatoSplit[0]));
				productForm.setIssuerName(issuerdatatoSplit[1]);
			}

			for (int i = 0; i < cardRangeitems.size(); i++) {
				cardRangeIds.add(Long.parseLong(cardRangeitems.get(i)));
			}

			cards = cardRangeService.getCardRangebyCardId(cardRangeIds);

			productForm.setCardRanges(cards);

			List<String> purseItemsList = productForm.getSupportedPurseUpdate();

			for (int j = 0; j < purseItemsList.size(); j++) {
				purseIds.add(Long.parseLong(purseItemsList.get(j)));
			}

			purses = purseService.getPurseByIds(purseIds);

			productForm.setSupportedPurse(purses);

			List<String> packageItemsList = productForm.getPackageUpdate();

			for (int j = 0; j < packageItemsList.size(); j++) {
				packageIds.add(packageItemsList.get(j));
			}

			packages = packageService.getPackageByIds(packageIds);

			productForm.setPackageId(packages);

			availableCurrencyCodeList = productService.getCurrencyCodeList(productForm.getPartnerId());
			mav.addObject("availablePartnerCurrencyList", availableCurrencyCodeList);
			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseList);

			Map<String, String> temMap1 = productForm.getProdAttributes();

			if (temMap1.get("validityPeriodFormat") != null) {
				mav.addObject("validityPeriodFormat", temMap1.get("validityPeriodFormat"));

			}

			mav.addObject("productForm", productForm);
			mav.setViewName("editProduct");

			return mav;
		}

		productForm.getIsActive();

		String issuerData = productForm.getIssuerName();
		String partnerData = productForm.getPartnerName();

		String[] issuerdatatoSplit = issuerData.split("~");

		productForm.setIssuerId(Long.parseLong(issuerdatatoSplit[0]));
		productForm.setIssuerName(issuerdatatoSplit[1]);

		String[] partnerdatatoSplit = partnerData.split("~");

		productForm.setPartnerId(Long.parseLong(partnerdatatoSplit[0]));
		productForm.setPartnerName(partnerdatatoSplit[1]);

		logger.info("product  details :: Product Name: " + productName + "  insUser " + sessionService.getUserId());

		String description = productForm.getDescription();
		String productShortName = productForm.getProductShortName();
		Map<String, String> prodAttributes = productForm.getProdAttributes();
		attributes.put("Product", prodAttributes);

		productForm.getCardRangesUpdate();

		StringBuilder otherTxnStr = new StringBuilder();
		StringBuilder activationStr = new StringBuilder();

		String[] otherTxnStrarr = productForm.getTxnTypeId();
		String[] activationStrArr = productForm.getActivationId();

		for (int i = 0; i < otherTxnStrarr.length; i++) {
			otherTxnStr.append(otherTxnStrarr[i]);
			if (i < otherTxnStrarr.length - 1) {
				otherTxnStr.append(",");
			}

		}
		for (int i = 0; i < activationStrArr.length; i++) {
			activationStr.append(activationStrArr[i]);
			if (i < activationStrArr.length - 1) {
				activationStr.append(",");
			}

		}
		prodAttributes.put("otherTxnId", otherTxnStr.toString());
		prodAttributes.put("activationId", activationStr.toString());

		productDTO.setIssuerId(productForm.getIssuerId());
		productDTO.setPartnerId(productForm.getPartnerId());
		productDTO.setIssuerName(productForm.getIssuerName());
		productDTO.setPartnerName(productForm.getPartnerName());
		productDTO.setProductId(productId);

		productDTO.setProductName(productForm.getProductName());

		productDTO.setUpc(productForm.getUpc());
		productDTO.setCardRanges(productForm.getCardRangesUpdate());
		productDTO.setSupportedPurse(productForm.getSupportedPurseUpdate());
		productDTO.setPartnerCurrency(productForm.getPartnerCurrencyUpdate());
		productDTO.setPackageId(productForm.getPackageUpdate());
		productDTO.setProgramId(productForm.getProgramId());

		if (!(String.valueOf(attributes.get("Product").get("b2bProductFunding")).equalsIgnoreCase("CARD_ACTIVATION"))) {
			attributes.get("Product").put("b2bSourceOfFunding", "");
		}

		if ((String.valueOf(attributes.get("Product").get("productType")).equalsIgnoreCase("RETAIL"))) {
			attributes.get("Product").put("b2bSerialNumAutoReplenishLevel", "");
			attributes.get("Product").put("b2bInitSerialNumQty", "");
			attributes.get("Product").put("b2bUpc", "");
			attributes.get("Product").put("b2bSerialNumAutoReplenishVal", "");
			attributes.get("Product").put("b2bProductFunding", "");
			attributes.get("Product").put("b2bSourceOfFunding", "");

		} else {
			attributes.get("Product").put("retailUPC", "");
		}

		if ((String.valueOf(attributes.get("Product").get("denominationType")).equalsIgnoreCase("Fixed"))) {
			attributes.get("Product").put("denomVarMin", "");
			attributes.get("Product").put("denomVarMax", "");
			attributes.get("Product").put("denomSelect", "");
		} else if ((String.valueOf(attributes.get("Product").get("denominationType")).equalsIgnoreCase("Variable"))) {
			attributes.get("Product").put("denomSelect", "");
			attributes.get("Product").put("denomFixed", "");
		} else {
			attributes.get("Product").put("denomVarMin", "");
			attributes.get("Product").put("denomVarMax", "");
			attributes.get("Product").put("denomFixed", "");
		}

		productDTO.setAttributesMap(attributes);
		productDTO.setAttributes(attributes.toString());
		productDTO.setProductShortName(productShortName);

		productDTO.setParentProductName(productForm.getParentProductName());
		productDTO.setParentProductId(productForm.getParentProductId());
		productDTO.setIsActive(flag);
		productDTO.setDescription(description);
		productDTO.setInsUser(sessionService.getUserId());
		productDTO.setInsDate(new Date());
		productDTO.setLastUpdDate(new Date());
		productDTO.setLastUpdUser(sessionService.getUserId());

		logger.debug("Before Calling issuerservice.getIssuerById() ");
		Product product = productService.getProductById(productId);

		if (product == null) {
			message = ResourceBundleUtil.getMessage("PRODUCT_" + ResponseMessages.DOESNOT_EXISTS);
			mav.addObject("failstatus", message);
			mav.setViewName("editProduct");
			mav.addObject("productForm", productForm);
		}

		else {
			logger.debug("After Calling productService.getProductById ");

			logger.debug("Before Calling productservice.updateProduct ");
			responseDTO = productService.updateProduct(productDTO);
			logger.debug("After Calling issuerservice.updateIssuer() ");
			logger.info("Message from  response " + responseDTO.getMessage());

		}

		if (responseDTO.getCode().equalsIgnoreCase("000")) {

			message = responseDTO.getMessage();
			mav.addObject("successstatus", message);
			mav.setViewName("productConfig");
			mav.addObject("deleteBox", new Product());
			mav.addObject("productForm", new Product());
		} else if (responseDTO.getCode().equalsIgnoreCase("PRODUCT_" + ResponseMessages.ALREADY_EXISTS)) {

			message = ResourceBundleUtil.getMessage("PRODUCT_" + ResponseMessages.ALREADY_EXISTS);
			mav.addObject("failstatus", message);
			List<ProgramIDDTO> programIdList = null;
			addUrl = Util.constructUrl(request);

			mav.addObject("addUrl", addUrl);

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseList = purseService.getAllPurses();
			ccfList = productService.getCCFList();
			programIdList = programIdService.getProgramIdsByPartnerId(product.getPartnerId());

			availableCurrencyCodeList = productService.getCurrencyCodeList(product.getPartnerId());
			
			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseList);
			mav.addObject("programDropDown", programIdList);
			mav.addObject("availablePartnerCurrencyList", availableCurrencyCodeList);

			List<Long> purseIds = new ArrayList<>();

			List<String> packageIds = new ArrayList<>();

			List<Long> cardRangeIds = new ArrayList<>();

			List<String> purseItemsList = productForm.getSupportedPurseUpdate();

			for (int j = 0; j < purseItemsList.size(); j++) {
				purseIds.add(Long.parseLong(purseItemsList.get(j)));
			}

			purses = purseService.getPurseByIds(purseIds);

			productForm.setSupportedPurse(purses);

			cardRangeitems = productForm.getCardRangesUpdate();

			for (int i = 0; i < cardRangeitems.size(); i++) {
				cardRangeIds.add(Long.parseLong(cardRangeitems.get(i)));
			}

			cards = cardRangeService.getCardRangebyCardId(cardRangeIds);

			productForm.setCardRanges(cards);

			List<String> packageItemsList = productForm.getPackageUpdate();

			for (int j = 0; j < packageItemsList.size(); j++) {
				packageIds.add(packageItemsList.get(j));
			}

			packages = packageService.getPackageByIds(packageIds);

			productForm.setPackageId(packages);

			mav.addObject("productForm", productForm);

			mav.setViewName("editProduct");
		} else if (responseDTO.getCode().equalsIgnoreCase("PRODUCT_" + ResponseMessages.DOESNOT_EXISTS)) {

			message = ResourceBundleUtil.getMessage("PRODUCT_" + ResponseMessages.DOESNOT_EXISTS);
			mav.addObject("failstatus", message);
			addUrl = Util.constructUrl(request);

			mav.addObject("addUrl", addUrl);

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseList = purseService.getAllPurses();
			ccfList = productService.getCCFList();

			availableCurrencyCodeList = productService.getCurrencyCodeList(product.getPartnerId());
			
			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseList);
			mav.addObject("availablePartnerCurrencyList", availableCurrencyCodeList);
			

			List<Long> purseIds = new ArrayList<>();

			List<String> packageIds = new ArrayList<>();

			List<Long> cardRangeIds = new ArrayList<>();

			List<String> purseItemsList = productForm.getSupportedPurseUpdate();

			for (int j = 0; j < purseItemsList.size(); j++) {
				purseIds.add(Long.parseLong(purseItemsList.get(j)));
			}

			purses = purseService.getPurseByIds(purseIds);

			productForm.setSupportedPurse(purses);

			cardRangeitems = productForm.getCardRangesUpdate();

			for (int i = 0; i < cardRangeitems.size(); i++) {
				cardRangeIds.add(Long.parseLong(cardRangeitems.get(i)));
			}

			cards = cardRangeService.getCardRangebyCardId(cardRangeIds);

			productForm.setCardRanges(cards);

			List<String> packageItemsList = productForm.getPackageUpdate();

			for (int j = 0; j < packageItemsList.size(); j++) {
				packageIds.add(packageItemsList.get(j));
			}

			packages = packageService.getPackageByIds(packageIds);

			productForm.setPackageId(packages);

			mav.addObject("productForm", productForm);
			mav.setViewName("editProduct");
		} else {

			message = ResourceBundleUtil.getMessage(ResponseMessages.FAILURE) + productName;
			mav.addObject("failstatus", message);
			mav.setViewName("editProduct");
			mav.addObject("deleteBox", new Product());
		}

		return mav;

	}

	/**
	 * update product ends here
	 */

	@RequestMapping("/generalTab")
	public ModelAndView generalTab(@ModelAttribute("productGeneral") Product product, HttpServletRequest req)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		mav.addObject("ruleSetMap", productService.getRuleSetMetaData());

		Map<String, Map<String, String>> authenticationTypes = productService.getAuthTypeMetaData();

		if (!CollectionUtils.isEmpty(authenticationTypes)) {
			mav.addObject("chwUserAuthTypes", authenticationTypes.get("chwUserAuthType"));
			mav.addObject("ivrUserAuthTypes", authenticationTypes.get("ivrUserAuthType"));
		}

		Product existingProduct = productService.getGeneralTabByProductId(productId);
		if (existingProduct == null) {
			mav.addObject("productGeneral", new Product());
		} else {
			mav.addObject("productMap",
					productService.getProductListwithSamePartner(existingProduct.getPartnerId(), productId));
			mav.addObject("productGeneral", existingProduct);

		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productGeneral");
		return mav;
	}

	@RequestMapping(value = "/saveGeneralDetails")
	public ModelAndView productGeneralSave(@ModelAttribute("productGeneral") Product product,
			BindingResult bindingResult, HttpServletRequest req) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		Map<Long, String> parentProductMap = productService.getParentProducts();
		mav.addObject("productMap",
				productService.getProductListwithSamePartner(product.getPartnerId(), product.getProductId()));

		String mainProductId = String.valueOf(product.getProductId());

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Map<String, Object> prodAttributes = product.getProductAttributes();

		String cardBlocking = (String) prodAttributes.get("cardBlocking");

		if ("false".equals(cardBlocking)) {
			prodAttributes.remove("invalidPinAttempts");
			prodAttributes.remove("startTimeHours");
			prodAttributes.remove("startTimeMins");
		}

		customValidator.validate(product, bindingResult);

		Map<String, Map<String, String>> authenticationTypes = productService.getAuthTypeMetaData();
		if (!CollectionUtils.isEmpty(authenticationTypes)) {
			mav.addObject("chwUserAuthTypes", authenticationTypes.get("chwUserAuthType"));
			mav.addObject("ivrUserAuthTypes", authenticationTypes.get("ivrUserAuthType"));
		}

		if (bindingResult.hasErrors()) {
			mav.addObject("ruleSetMap", productService.getRuleSetMetaData());

			mav.setViewName("productGeneral");
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}
		ResponseEntity<ResponseDTO> responseDTO = productService.updateGeneralTab(product);
		if (responseDTO != null && responseDTO.getBody() != null) {
			if (ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())) {
				mav.addObject("ruleSetMap", productService.getRuleSetMetaData());
				mav.addObject("productGeneral", product);
				mav.addObject(CCLPConstants.STATUS, responseDTO.getBody().getMessage());
			} else {
				mav.addObject("ruleSetMap", productService.getRuleSetMetaData());
				mav.addObject("productGeneral", product);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getBody().getMessage());
			}
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productGeneral");
		return mav;
	}

	@RequestMapping("/getProductDetailsById")
	public ModelAndView getProductDetailsById(@ModelAttribute("productGeneral") Product product, HttpServletRequest req)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(product.getProductId());

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		mav.addObject("ruleSetMap", productService.getRuleSetMetaData());

		Map<String, Map<String, String>> authenticationTypes = productService.getAuthTypeMetaData();

		if (!CollectionUtils.isEmpty(authenticationTypes)) {
			mav.addObject("chwUserAuthTypes", authenticationTypes.get("chwUserAuthType"));
			mav.addObject("ivrUserAuthTypes", authenticationTypes.get("ivrUserAuthType"));
		}

		Product existingProduct = productService.getGeneralTabByProductId(product.getParentProductId());
		if (existingProduct == null) {
			mav.addObject("productGeneral", new Product());
		} else {
			existingProduct.setParentProductId(product.getParentProductId());
			existingProduct.setProductId(product.getProductId());
			existingProduct.setPartnerId(product.getPartnerId());

			mav.addObject("productGeneral", existingProduct);
			mav.addObject("productMap", productService.getProductListwithSamePartner(existingProduct.getPartnerId(),
					existingProduct.getProductId()));
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productGeneral");
		return mav;
	}

	@RequestMapping("/getCvvDetailsById")
	public ModelAndView getCvvDetailsById(@ModelAttribute("productCVV") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(product.getProductId());

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Product existingProduct = productService.getCVVTabByProductId(product.getParentProductId());
		if (existingProduct == null) {
			mav.addObject("productCVV", new Product());
		} else {
			existingProduct.setParentProductId(product.getParentProductId());
			existingProduct.setProductId(product.getProductId());
			mav.addObject("productCVV", existingProduct);
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productCvv");
		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();
		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject("failstatus", errMessage);
		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	/*
	 * This method is called onClick of limits tab or onClick of Copy From button.
	 * productId = productId which propagates from general tab, productName =
	 * productName which propagates from general tab. copyFromProduct = productId of
	 * copy from dropdown which propagates from limits tab
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/productLimit")
	public ModelAndView productLimit(HttpServletRequest request, HttpServletResponse response) throws ServiceException {

		logger.info(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView();
		session = request.getSession(false);

		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));
		logger.debug("product Id : " + mainProductId);
		
		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copy Product Id : " + copyProductId);
		
		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);

		Product product = new Product();

		if (mainProductId != null || copyProductId != null) {
			String parentproductId = copyProductId == null ? "0" : copyProductId;
			String productId = copyProductId != null ? copyProductId : mainProductId;
			ModelMapper mm = new ModelMapper();
			List<Object> txnDtls = productService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.setViewName("productLimit");

			product.setProductId(Long.parseLong(mainProductId));
			ResponseDTO responseDTO = productService.getLimits(Long.parseLong(mainProductId),Long.parseLong(parentproductId),Long.parseLong(purseId));
			if (responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> productLimitMap = responseDTO.getData() != null
						? mm.map(responseDTO.getData(), Map.class)
						: new HashMap<>();
				product.setProductAttributes(productLimitMap);
				mav.addObject("productForm", product);

				Map<Object, String> parentProductMap = productService.getAllParentProducts();
				if (parentProductMap != null && !parentProductMap.isEmpty()) {

					parentProductMap.remove(mainProductId);
					mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
					mav.addObject("copyProductId", copyProductId);
					mav.addObject("showCopyFrom", "true");
					logger.debug(parentProductMap);
				}
				 List<PurseDTO> purseList = productService.getPurseByProductId(Long.parseLong(mainProductId),Long.parseLong(productId),"Limits");
				 logger.debug(purseList);
				mav.addObject(CCLPConstants.PURSE_LIST, purseList);
				if (Long.parseLong(purseId) < 0) {
					Product defaultPurseProduct = productService.getProductById(Long.parseLong(mainProductId));

					Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
					Map<String, String> prodAttributes = attributes.get("Product");
					purseId = prodAttributes.get("defaultPurse");
					logger.info("Default Purse is : " + purseId);
				}
				mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
			} else {
				mav.addObject("productForm", product);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getMessage());
			}
		} else {
			logger.error("mainProductId and copyProductId are null");

		}

		logger.info(CCLPConstants.EXIT );

		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveLimits")
	public ModelAndView saveLimits(HttpServletRequest request,@Validated(Limitmap.class) @ModelAttribute("productForm") Product productForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView();
		logger.debug(CCLPConstants.ENTER);
		logger.debug("productForm is ", productForm);

		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		
		mav.setViewName("productLimit");
		if (bindingResult.hasErrors()) {
			logger.debug("Server side validation errors ");
			List<Object> txnDtls = productService.getDeliveryChnlTxns();
			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.addObject("productForm", productForm);

			Map<Object, String> parentProductMap = productService.getAllParentProducts();
			if (parentProductMap != null && !parentProductMap.isEmpty()) {
				parentProductMap.remove(productForm.getProductId());
				mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
				logger.debug(parentProductMap);
				mav.addObject("showCopyFrom", "true");
			}
			List<PurseDTO> purseList = productService.getPurseByProductId(productForm.getProductId(),productForm.getProductId(),"Limits");
			mav.addObject(CCLPConstants.PURSE_LIST, purseList);
			if (Long.parseLong(purseId) < 0) {
				Product defaultPurseProduct = productService.getProductById(productForm.getProductId());

				Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
				Map<String, String> prodAttributes = attributes.get("Product");
				purseId = prodAttributes.get("defaultPurse");
				logger.info("Default Purse is : " + purseId);
			}
			mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
			return mav;
		}
		ResponseDTO responseDto = productService.addLimits(productForm.getProductAttributes(),
				productForm.getProductId(),Long.parseLong(purseId));
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Limit record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_LIMIT_UPDATE_FAIL));
		}

		List<Object> txnDtls = productService.getDeliveryChnlTxns();
		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		mav.addObject("productForm", productForm);

		Map<Object, String> parentProductMap = productService.getAllParentProducts();
		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(productForm.getProductId());
			mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
			logger.debug(parentProductMap);
			mav.addObject("showCopyFrom", "true");
		}
		List<PurseDTO> purseList = productService.getPurseByProductId(productForm.getProductId(),productForm.getProductId(),"Limits");
		mav.addObject(CCLPConstants.PURSE_LIST, purseList);
		
		if (Long.parseLong(purseId) < 0) {
			Product defaultPurseProduct = productService.getProductById(productForm.getProductId());

			Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
			Map<String, String> prodAttributes = attributes.get("Product");
			purseId = prodAttributes.get("defaultPurse");
			logger.info("Default Purse is : " + purseId);
		}
		
		mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/showCVVtab")
	public ModelAndView getCVVTab(@ModelAttribute("productCVV") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		session = req.getSession(false);
		Long productId = (Long) session.getAttribute("productId");
		

		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {

			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Product existingProduct = productService.getCVVTabByProductId(productId);
		if (existingProduct == null) {
			mav.addObject("productCVV", new Product());
		} else {
			existingProduct.setProductId(productId);
			mav.addObject("productCVV", existingProduct);
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productCvv");
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveCVVDetails")
	public ModelAndView saveCVVDetails(@ModelAttribute("productCVV") Product product, HttpServletRequest req,
			BindingResult bindingResult) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(product.getProductId());

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		removeCvvKeys(product.getProductAttributes());
		customValidator.validate(product, bindingResult);

		if (bindingResult.hasErrors()) {
			mav.addObject("productCVV", product);
			mav.setViewName("productCvv");
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}

		ResponseEntity<ResponseDTO> responseDTO = productService.updateCVVTab(product.getProductAttributes(),
				product.getProductId());

		if (responseDTO != null && responseDTO.getBody() != null) {
			if (ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())) {
				mav.addObject(CCLPConstants.STATUS, responseDTO.getBody().getMessage());
			} else {
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getBody().getMessage());
			}

			mav.addObject("productCVV", product);
		}

		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productCvv");
		return mav;

	}

	public void removeCvvKeys(Map<String, Object> productAttributes) {
		/* Condition check based on the selection in UI */

		String embossApplicable = (String) productAttributes.get("embossApplicable");
		String cvkFormat = (String) productAttributes.get("cvkFormat");
		String cardVerifyType = (String) productAttributes.get("cardVerifyType");

		if ("true".equals(embossApplicable) && "true".equals(cvkFormat) && "true".equals(cardVerifyType)) {
			productAttributes.remove("cvkA");
			productAttributes.remove("cvkB");
			productAttributes.remove("cscKey");

		} else if ("true".equals(embossApplicable) && "false".equals(cardVerifyType)) {
			productAttributes.remove("cvkIndex");
			productAttributes.remove("cvkA");
			productAttributes.remove("cvkB");
			productAttributes.remove("cvkKeySpecifier");
		} else if ("true".equals(embossApplicable) && "false".equals(cvkFormat) && "true".equals(cardVerifyType)) {
			productAttributes.remove("cscKey");
			productAttributes.remove("cvkIndex");
		} else if ("false".equals(embossApplicable)) {
			productAttributes.remove("cvkA");
			productAttributes.remove("cvkB");
			productAttributes.remove("cscKey");
			productAttributes.remove("cvkKeySpecifier");
			productAttributes.remove("cvkIndex");
		}
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/txnCardStatus")
	public ModelAndView txnCardStatus(@ModelAttribute("productStat") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		session = req.getSession(false);
		Long productId = (Long) session.getAttribute("productId");
		String mainProductId = String.valueOf(product.getProductId());

		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");
		}

		List<Object> txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
		mav.addObject("cardStatusList", productService.getCardStatus());

		Product existingProduct = productService.getTxnBasedOnCardStatus(productId);
		if (existingProduct == null) {
			mav.addObject("productStat", new Product());
		} else {
			existingProduct.setProductId(productId);
			mav.addObject("productStat", existingProduct);
		}

		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productTxnCardStatus");
		return mav;
	}

	/*
	 * This Method Save the Transaction based on Card Status Details into the
	 * Product Table
	 */
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveTxnOnCardStatById")
	public ModelAndView saveTxnOnCardStatById(@ModelAttribute("productStat") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		String mainProductId = String.valueOf(product.getProductId());

		ResponseEntity<ResponseDTO> responseDTO = productService.saveTxnOnCardStatById(product.getProductAttributes(),
				product.getProductId());
		if (responseDTO != null && responseDTO.getBody() != null) {

			/* To Retrieve Parent Product Details for Copy */
			Map<Long, String> parentProductMap = productService.getParentProducts();

			if (parentProductMap != null && !parentProductMap.isEmpty()) {
				parentProductMap.remove(mainProductId);
				mav.addObject("parentProductMap", parentProductMap);
				mav.addObject("showCopyFrom", "true");

			}

			/* This is to Display the Success/Error Messages in UI */
			if (ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())) {
				mav.addObject(CCLPConstants.STATUS, responseDTO.getBody().getMessage());
			} else {
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getBody().getMessage());
			}

			List<Object> txnDtls = productService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject("cardStatusList", productService.getCardStatus());
			mav.addObject("productStat", product);
		}

		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productTxnCardStatus");
		return mav;

	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/getTxnStatDetails")
	public ModelAndView getTxnStatDetails(@ModelAttribute("productStat") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		String mainProductId = String.valueOf(product.getProductId());

		/* To Retrieve Parent Product Details for Copy */
		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");
		}

		List<Object> txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
		mav.addObject("cardStatusList", productService.getCardStatus());

		Product existingProduct = productService.getTxnBasedOnCardStatus(product.getParentProductId());
		if (existingProduct == null) {
			mav.addObject("productStat", new Product());
		} else {
			existingProduct.setParentProductId(product.getParentProductId());
			existingProduct.setProductId(product.getProductId());
			mav.addObject("productStat", existingProduct);
		}

		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("productTxnCardStatus");
		return mav;
	}

	/*
	 * This method is called onClick of TxnFee tab or onClick of Copy From button.
	 * productId = productId which propagates from general tab, productName =
	 * productName which propagates from general tab. copyFromProduct = productId of
	 * copy from dropdown which propagates from limits tab
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/productTransactionFee")
	public ModelAndView productTxnFee(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView();
		session = request.getSession(false);

		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));

		logger.debug("product Id is: " + mainProductId);

		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copy Product Id is: " + copyProductId);

		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		Product product = new Product();

		if (mainProductId != null || copyProductId != null) {

			String productId = copyProductId != null ? copyProductId : mainProductId; //
			String parentProductId = copyProductId == null ? "0" : copyProductId;
			ModelMapper mm = new ModelMapper();
			List<Object> txnDtls = productService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.setViewName("productTxnFee");

			product.setProductId(Long.parseLong(mainProductId));
			ResponseDTO responseBody = productService.getTxnFees(Long.parseLong(mainProductId),Long.parseLong(parentProductId),Long.parseLong(purseId));

			logger.debug("responseBody " + responseBody);
			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {

				Map<String, Object> productTxnFeeMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				product.setProductAttributes(productTxnFeeMap);
				mav.addObject("productForm", product);
				Map<Object, String> parentProductMap = productService.getAllParentProducts();
				if (parentProductMap != null && !parentProductMap.isEmpty()
						&& parentProductMap.keySet().contains(mainProductId)) {

					parentProductMap.remove(mainProductId);
					mav.addObject("showCopyFrom", "true");
					mav.addObject("copyProductId", copyProductId);
				}
				mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
				List<PurseDTO> purseList = productService.getPurseByProductId(Long.parseLong(mainProductId),
						Long.parseLong(productId), "Fees");

				mav.addObject(CCLPConstants.PURSE_LIST, purseList);
				if (Long.parseLong(purseId) < 0) {
					Product defaultPurseProduct = productService.getProductById(Long.parseLong(mainProductId));

					Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
					Map<String, String> prodAttributes = attributes.get("Product");
					purseId = prodAttributes.get("defaultPurse");
					logger.info("Default Purse is : " + purseId);
				}
				mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
			} else {
				mav.addObject("productForm", product);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {
			logger.error("Product ID is null or Empty");

		}
		logger.debug("EXIT " + product);
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveTransactionFees")
	public ModelAndView saveTxnFees(HttpServletRequest req,@Validated(transactionFee.class) @ModelAttribute("productForm") Product productForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView("productTxnFee");
		logger.debug(CCLPConstants.ENTER);
		logger.debug("product Form " + productForm);
		String purseId = req.getParameter("purseId") == null ? "-1" :req.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		
		if (bindingResult.hasErrors()) {
			logger.debug("Server side validation errors");
			List<Object> txnDtls = productService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.addObject("productForm", productForm);

			Map<Object, String> parentProductMap = productService.getAllParentProducts();
			if (parentProductMap != null && !parentProductMap.isEmpty()
					&& parentProductMap.keySet().contains(String.valueOf(productForm.getProductId()))) {

				mav.addObject("showCopyFrom", "true");
				parentProductMap.remove(productForm.getProductId());

			}
			List<PurseDTO> purseList = productService.getPurseByProductId(productForm.getProductId(),productForm.getProductId(),"Fees");

			mav.addObject(CCLPConstants.PURSE_LIST, purseList);
			if (Long.parseLong(purseId) < 0) {
				Product defaultPurseProduct = productService.getProductById(productForm.getProductId());

				Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
				Map<String, String> prodAttributes = attributes.get("Product");
				purseId = prodAttributes.get("defaultPurse");
				logger.info("Default Purse is : " + purseId);
			}
			mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
			mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);

			return mav;
		}
		ResponseDTO responseDto = productService.addTxnFees(productForm.getProductAttributes(),
				productForm.getProductId(),Long.parseLong(purseId));
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Txn Fee record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}

		List<Object> txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
		mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
		mav.addObject("productForm", productForm);

		Map<Object, String> parentProductMap = productService.getAllParentProducts();
		if (parentProductMap != null && !parentProductMap.isEmpty()
				&& parentProductMap.keySet().contains(String.valueOf(productForm.getProductId()))) {

			mav.addObject("showCopyFrom", "true");
			parentProductMap.remove(productForm.getProductId());

		}
		List<PurseDTO> purseList = productService.getPurseByProductId(productForm.getProductId(),productForm.getProductId(),"Fees");

		mav.addObject(CCLPConstants.PURSE_LIST, purseList);
		if (Long.parseLong(purseId) < 0) {
			Product defaultPurseProduct = productService.getProductById(productForm.getProductId());

			Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
			Map<String, String> prodAttributes = attributes.get("Product");
			purseId = prodAttributes.get("defaultPurse");
			logger.info("Default Purse is : " + purseId);
		}
		mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
		mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);

		return mav;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/productMonthlyFeeCap")
	public ModelAndView productMonthlyFeeCap(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		ModelAndView mav = new ModelAndView("productMonthlyFeeCap");
		session = request.getSession();

		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		
		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));

		logger.debug("productId : " + mainProductId);

		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copyProductId : " + copyProductId);

		Product product = new Product();
		product.setProductId(Long.valueOf(mainProductId));
		mav.addObject("productForm", product);
		if (mainProductId != null || copyProductId != null) {

			String productId = copyProductId != null ? copyProductId : mainProductId;
			String parentProductId = copyProductId == null ? "0" : copyProductId;
			ResponseDTO responseBody = productService.getMonthlyFeeCap(Long.parseLong(mainProductId),Long.parseLong(parentProductId),Long.parseLong(purseId));

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> productFeeCapMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				if (productFeeCapMap != null) {

					logger.debug("productFeeCapMap " + productFeeCapMap);

					product.setProductAttributes(productFeeCapMap);
					mav.addObject("productForm", product);

					Map<Object, String> parentProductMap = productService.getAllParentProducts();

					if (parentProductMap != null && !parentProductMap.isEmpty()
							&& parentProductMap.keySet().contains(mainProductId)) {

						parentProductMap.remove(mainProductId);
						mav.addObject("showCopyFrom", "true");
						mav.addObject("copyProductId", copyProductId);

					}
					List<PurseDTO> purseList = productService.getPurseByProductId(Long.parseLong(mainProductId),Long.parseLong(productId),"Fees");

					mav.addObject(CCLPConstants.PURSE_LIST, purseList);
					if (Long.parseLong(purseId) < 0) {
						Product defaultPurseProduct = productService.getProductById(Long.parseLong(mainProductId));

						Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
						Map<String, String> prodAttributes = attributes.get("Product");
						purseId = prodAttributes.get("defaultPurse");
						logger.info("Default Purse is : " + purseId);
					}
					mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
					mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
				}
			} else {
				logger.error("Failed to Fetch Monthly Fee Cap Attributes from config srvice");
				mav.addObject("productForm", product);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {

			logger.error("Product ID is null");
		}
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveMonthlyFeeCap")
	public ModelAndView saveMonthlyFeeCap( HttpServletRequest request,
			@Validated(monthlyFeeCapFee.class) @ModelAttribute("productForm") Product productForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView("productMonthlyFeeCap");
		logger.debug(CCLPConstants.ENTER);
		logger.debug("productForm " + productForm);
		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		
		if (bindingResult.hasErrors()) {
			logger.debug("Server side validation errors");
			mav.addObject("productForm", productForm);

			Map<Object, String> parentProductMap = productService.getAllParentProducts();
			if (parentProductMap != null && !parentProductMap.isEmpty()
					&& parentProductMap.keySet().contains(String.valueOf(productForm.getProductId()))) {

				mav.addObject("showCopyFrom", "true");
				parentProductMap.remove(productForm.getProductId());

			}
			
			mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
			List<PurseDTO> purseList = productService.getPurseByProductId(productForm.getProductId(),productForm.getProductId(),"Fees");

			mav.addObject(CCLPConstants.PURSE_LIST, purseList);
			if (Long.parseLong(purseId) < 0) {
				Product defaultPurseProduct = productService.getProductById(productForm.getProductId());

				Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
				Map<String, String> prodAttributes = attributes.get("Product");
				purseId = prodAttributes.get("defaultPurse");
				logger.info("Default Purse is : " + purseId);
			}
			mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
			return mav;
		}
		ResponseDTO responseDto = productService.addMonthlyFeeCap(productForm.getProductAttributes(),
				productForm.getProductId(),Long.parseLong(purseId));
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Monthly Fee Cap record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.PRODUCT_MONTHLYFEECAP_UPDATE_FAIL));
		}

		mav.addObject("productForm", productForm);

		Map<Object, String> parentProductMap = productService.getAllParentProducts();
		if (parentProductMap != null && !parentProductMap.isEmpty()
				&& parentProductMap.keySet().contains(String.valueOf(productForm.getProductId()))) {

			mav.addObject("showCopyFrom", "true");
			parentProductMap.remove(productForm.getProductId());
		}
		mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
		List<PurseDTO> purseList = productService.getPurseByProductId(productForm.getProductId(),productForm.getProductId(),"Fees");
		mav.addObject(CCLPConstants.PURSE_LIST, purseList);
		if (Long.parseLong(purseId) < 0) {
			Product defaultPurseProduct = productService.getProductById(productForm.getProductId());

			Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
			Map<String, String> prodAttributes = attributes.get("Product");
			purseId = prodAttributes.get("defaultPurse");
			logger.info("Default Purse is : " + purseId);
		}
		mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
		return mav;
	}

	/**
	 * ALERTS STARTS HERE
	 * 
	 * @param model
	 * @param productAlerts
	 * @param request
	 * @return
	 * @throws ServiceException
	 */

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/showProductAlerts")
	public ModelAndView showProductAlerts(Map<String, Object> model,
			@ModelAttribute("productAlerts") ProductAlert productAlerts, HttpServletRequest request)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		Long productId;
		String id = "";
		ModelAndView mav = new ModelAndView();

		ResponseDTO responseDTO = null;
		ResponseDTO res = null;
		Map<String, String> alertsData = null;
		Map<String, String> alertsdetails = null;

		session = request.getSession();
		id = request.getParameter("productId");

		if (id == null || id.isEmpty()) {
			productId = (Long) session.getAttribute("productId");
		} else {
			productId = Long.parseLong(id);
		}
		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Map<String, String> cardStatus = productService.getCardStatusList();

		res = productService.getAlertsTab(productId);
		alertsdetails = (Map<String, String>) res.getData();

		String cardStatusSelList = "";
		if (alertsdetails.get(CCLPConstants.ALERT_CARD_STATUS) != null) {

			cardStatusSelList = String.valueOf(alertsdetails.get(CCLPConstants.ALERT_CARD_STATUS));

		}

		try {
			mav.addObject("parentProductMap", productService.getParentProducts());

			mav.addObject("messagesMap", productService.getMessages());

			mav.addObject("cardStatus", cardStatus);

			mav.addObject(CCLPConstants.SEL_CARD_STATUS, cardStatusSelList);

			responseDTO = productService.getAlertsTab(productId);

			if (responseDTO.getCode().equalsIgnoreCase("000")) {

				alertsData = (Map<String, String>) responseDTO.getData();
				ProductAlert productAlert = new ProductAlert(alertsData);
				mav.addObject("productAlerts", productAlert);
			} else if (responseDTO.getCode().equalsIgnoreCase("999")) {
				mav.addObject("productAlerts", new ProductAlert());
			}

		} catch (ServiceException e) {

			logger.error(e);
		}
		session.setAttribute("productId", productId);
		mav.setViewName("showProductAlerts");
		logger.info("Product Id :: " + productId);

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/productMaintenanceFee")
	public ModelAndView productMaintenanceFee(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		ModelAndView mav = new ModelAndView("productMaintenanceFee");
		session = request.getSession(false);

		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));
		logger.debug("productId : " + mainProductId);

		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copyProductId : " + copyProductId);

		Product product = new Product();
		product.setProductId(Long.valueOf(mainProductId));
		mav.addObject("productForm", product);
		if (mainProductId != null || copyProductId != null) {

			String productId = copyProductId != null ? copyProductId : mainProductId;
			ResponseDTO responseBody = productService.getMaintenanceFee(Long.parseLong(productId));

			if (responseBody.getResult().equalsIgnoreCase("success")) {
				Map<String, Object> productFeeCapMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				if (productFeeCapMap != null) {

					logger.debug("productMaintenanceFeeMap " + productFeeCapMap);
					product.setProductAttributes(productFeeCapMap);
					mav.addObject("productForm", product);

					Map<Object, String> parentProductMap = productService.getAllParentProducts();

					if (parentProductMap != null && !parentProductMap.isEmpty()
							&& parentProductMap.keySet().contains(mainProductId)) {

						parentProductMap.remove(mainProductId);
						mav.addObject("showCopyFrom", "true");
						mav.addObject("copyProductId", copyProductId);
					}
					mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
				}
			} else {
				logger.error("Failed to Fetch Maintenance Fee Attributes from config srvice");
				mav.addObject("productForm", product);
				mav.addObject("statusFlag", "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {
			logger.error("Product ID is null");
		}
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveMaintenanceFee")
	public ModelAndView saveMaintenanceFee(
			@Validated(maintenanceFee.class) @ModelAttribute("productForm") Product productForm,
			BindingResult bindingResult) throws ServiceException {
		ModelAndView mav = new ModelAndView("productMaintenanceFee");
		logger.debug(CCLPConstants.ENTER);
		logger.debug("productForm " + productForm);
		if (bindingResult.hasErrors()) {
			logger.info("Server Side validation errors");
			mav.addObject("productForm", productForm);

			Map<Object, String> parentProductMap = productService.getAllParentProducts();
			if (parentProductMap != null && !parentProductMap.isEmpty()
					&& parentProductMap.keySet().contains(String.valueOf(productForm.getProductId()))) {

				mav.addObject("showCopyFrom", "true");
				parentProductMap.remove(productForm.getProductId());

			}
			mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
			return mav;
		}
		ResponseDTO responseDto = productService.addMaintenanceFee(productForm.getProductAttributes(),
				productForm.getProductId());
		if (responseDto.getResult() != null && responseDto.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
			logger.info("Maintenance Fee record '{}' has been updated successfully");
			mav.addObject(CCLPConstants.STATUS_FLAG, CCLPConstants.SUCCESS);
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());
			else
				mav.addObject(CCLPConstants.STATUS_MESSAGE,
						ResourceBundleUtil.getMessage(ResponseMessages.UPDATE_SUCCESS));
		} else {

			mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
			if (responseDto.getMessage() != null)
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDto.getMessage());

		}

		mav.addObject("productForm", productForm);

		Map<Object, String> parentProductMap = productService.getAllParentProducts();
		if (parentProductMap != null && !parentProductMap.isEmpty()
				&& parentProductMap.keySet().contains(String.valueOf(productForm.getProductId()))) {

			mav.addObject("showCopyFrom", "true");
			parentProductMap.remove(productForm.getProductId());

		}
		mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);

		return mav;
	}

	@SuppressWarnings("unlikely-arg-type")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/productPurse")
	public ModelAndView getPProductPurseTab(@ModelAttribute("productPurse") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		String addUrl = Util.constructUrl(req);
		mav.addObject("addUrl", addUrl);
		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");

		Map<String, String> timeZone = Util.getTimeZones();
		Long parentProductId = null;
		parentProductId = product.getParentProductId() == null ? productId : product.getParentProductId();

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject(CCLPConstants.SHOW_COPY_FROM, CCLPConstants.TRUE);
		}

		List<PurseDTO> purseList = productService.getPurseByProductId(productId, parentProductId, CCLPConstants.PURSE);
		if (purseList == null || purseList.isEmpty()) {
			mav.addObject(CCLPConstants.STATUS_MESSAGE, "No Purses are attached to this Product");
		}
		mav.addObject(CCLPConstants.PURSE_LIST, purseList);
		mav.addObject(CCLPConstants.TIME_ZONE, timeZone);

		mav.setViewName(CCLPConstants.PRODUCT_PURSE);
		logger.info(CCLPConstants.EXIT);
		return mav;
	}


	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/savePurseParameters")
	public ModelAndView savePurseParameters(@ModelAttribute("productPurse") Product product, HttpServletRequest req,
			BindingResult bindingResult) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		session = req.getSession();
		String addUrl = Util.constructUrl(req);
		mav.addObject("addUrl", addUrl);
		Long productId = (Long) session.getAttribute("productId");
		long purseId = Long.parseLong(req.getParameter("purseId"));
		
		String purseTypeName = req.getParameter("purseTypeName");
		String minorUnits = req.getParameter("minorUnits");

		Long parentProductId = null;
		parentProductId = product.getParentProductId() == CCLPConstants.DROPDOWN_DEFAULTVALUE ? productId
				: product.getParentProductId();

		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(productId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject(CCLPConstants.SHOW_COPY_FROM, CCLPConstants.TRUE);
		}
		Map<String,Object> tempMap=new HashMap<String,Object>();
		Map<String, Object> productAttributes = product.getProductAttributes();

		String activeDate = (String) productAttributes.get("purseUIActiveDate");
		String activeTime = (String) productAttributes.get("purseActiveTime");
		String activeTimeZone = (String) productAttributes.get("purseActiveTimeZone");
		String purseActiveDateTime = activeDate.concat(" ").concat(activeTime);

		String estActiveDate = Util.getConvertedTimeZone(activeTimeZone, purseActiveDateTime);
		String estValidityPeriod = null;

		if (!Util.isEmpty((String) productAttributes.get(CCLPConstants.PURSE_UI_VALIDITY_PERIOD))
				&& !Util.isEmpty((String) productAttributes.get(CCLPConstants.PURSE_VALIDITY_TIME))
				&& !Util.isEmpty((String) productAttributes.get(CCLPConstants.PURSE_VALIDITY_TIMEZONE))) {
			String validityDate = (String) productAttributes.get(CCLPConstants.PURSE_UI_VALIDITY_PERIOD);
			String validityTime = (String) productAttributes.get(CCLPConstants.PURSE_VALIDITY_TIME);
			String validityTimeZone = (String) productAttributes.get(CCLPConstants.PURSE_VALIDITY_TIMEZONE);
			String purseValidityDateTime = validityDate.concat(" ").concat(validityTime);
			estValidityPeriod = Util.getConvertedTimeZone(validityTimeZone, purseValidityDateTime);
		}

		productAttributes.put("purseActiveDate", estActiveDate);
		productAttributes.put("purseValidityPeriod", estValidityPeriod);

		LocalDateTime date1 = Util.getLocalDateTime(estActiveDate);

		if (estValidityPeriod != null) {
			LocalDateTime date2 = Util.getLocalDateTime(estValidityPeriod);
			if (date1.isAfter(date2)) {
				mav.addObject(CCLPConstants.PURSE_LIST,
						productService.getPurseByProductId(productId, parentProductId, CCLPConstants.PURSE));
				Map<String, String> timeZone = Util.getTimeZones();
				mav.addObject(CCLPConstants.TIME_ZONE, timeZone);
				mav.setViewName(CCLPConstants.PRODUCT_PURSE);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, "Purse validity Period should be greater than active Date");
				logger.info(CCLPConstants.EXIT);
				return mav;
			}
		}
		
		String autoTopupEnable = String.valueOf(productAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_ENABLE));		
		if (CCLPConstants.ENABLE.equals(autoTopupEnable)) {		
			tempMap.put(CCLPConstants.TOPUP_FREQ_DAY, String.valueOf(productAttributes.get(CCLPConstants.TOPUP_FREQ_DAY)));
			tempMap.put(CCLPConstants.TOPUP_FREQ_DAYOFMONTH_MULTI,productAttributes.get(CCLPConstants.TOPUP_FREQ_DAYOFMONTH_MULTI));
			tempMap.put(CCLPConstants.TOPUP_FREQ_DOM, String.valueOf(productAttributes.get(CCLPConstants.TOPUP_FREQ_DOM)));
			tempMap.put(CCLPConstants.TOPUP_FREQ_MONTH, String.valueOf(productAttributes.get(CCLPConstants.TOPUP_FREQ_MONTH)));
			
			CronSchedule schedule = CronUtil.buildCronSchedule(String.valueOf(productAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_FREQUENCY)), String.valueOf(productAttributes.get(CCLPConstants.TOPUP_FREQ_DAY)),
					productAttributes.get(CCLPConstants.TOPUP_FREQ_DAYOFMONTH_MULTI), String.valueOf(productAttributes.get(CCLPConstants.TOPUP_FREQ_DOM)), String.valueOf(productAttributes.get(CCLPConstants.TOPUP_FREQ_MONTH)));
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_CRON, schedule.getCron());
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_NXT_RUN, schedule.getNextRunDate());
		}else {
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_CRON, null);
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_NXT_RUN, null);
		}
		
		String autoReloadEnable = String.valueOf(productAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_ENABLE));
		if (CCLPConstants.ENABLE.equals(autoReloadEnable)) {
			tempMap.put(CCLPConstants.RELOAD_FREQ_DAY, String.valueOf(productAttributes.get(CCLPConstants.RELOAD_FREQ_DAY)));
			tempMap.put(CCLPConstants.RELOAD_FREQ_DAYOFMONTH_MULTI, productAttributes.get(CCLPConstants.RELOAD_FREQ_DAYOFMONTH_MULTI));
			tempMap.put(CCLPConstants.RELOAD_FREQ_DOM, String.valueOf(productAttributes.get(CCLPConstants.RELOAD_FREQ_DOM)));
			tempMap.put(CCLPConstants.RELOAD_FREQ_MONTH, String.valueOf(productAttributes.get(CCLPConstants.RELOAD_FREQ_MONTH)));
			
			CronSchedule schedule = CronUtil.buildCronSchedule(String.valueOf(productAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_FREQUENCY)), String.valueOf(productAttributes.get(CCLPConstants.RELOAD_FREQ_DAY)),
					productAttributes.get(CCLPConstants.RELOAD_FREQ_DAYOFMONTH_MULTI), String.valueOf(productAttributes.get(CCLPConstants.RELOAD_FREQ_DOM)), String.valueOf(productAttributes.get(CCLPConstants.RELOAD_FREQ_MONTH)));
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_CRON, schedule.getCron());
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_NXT_RUN, schedule.getNextRunDate());
		} else {
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_CRON, null);
			productAttributes.put(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_NXT_RUN, null);
		}
	
		productAttributes.remove(CCLPConstants.TOPUP_FREQ_DAY);
		productAttributes.remove(CCLPConstants.TOPUP_FREQ_DAYOFMONTH_MULTI);
		productAttributes.remove(CCLPConstants.TOPUP_FREQ_DOM);
		productAttributes.remove(CCLPConstants.TOPUP_FREQ_MONTH);
		productAttributes.remove(CCLPConstants.RELOAD_FREQ_DAY);
		productAttributes.remove(CCLPConstants.RELOAD_FREQ_DAYOFMONTH_MULTI);
		productAttributes.remove(CCLPConstants.RELOAD_FREQ_DOM);
		productAttributes.remove(CCLPConstants.RELOAD_FREQ_MONTH);

		ResponseEntity<ResponseDTO> responseDTO = productService.updatePurse(productAttributes, productId,
				purseId);
		productAttributes.putAll(tempMap);
		if (responseDTO != null && responseDTO.getBody() != null) {
			if (ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())) {
				mav.addObject(CCLPConstants.PRODUCT_PURSE, product);
				mav.addObject(CCLPConstants.PURSE_LIST,
						productService.getPurseByProductId(productId, parentProductId, CCLPConstants.PURSE));
				mav.addObject(CCLPConstants.STATUS, responseDTO.getBody().getMessage());
			} else {
				mav.addObject(CCLPConstants.PRODUCT_PURSE, product);
				mav.addObject(CCLPConstants.PURSE_LIST,
						productService.getPurseByProductId(productId, parentProductId, CCLPConstants.PURSE));
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getBody().getMessage());
			}

			Map<String, String> timeZone = Util.getTimeZones();
			mav.addObject(CCLPConstants.TIME_ZONE, timeZone);
		}
		mav.addObject("minorUnits", minorUnits);
		mav.addObject("purseTypeName", purseTypeName);
		product.setPurseId(purseId);
		logger.info(CCLPConstants.EXIT);
		mav.setViewName(CCLPConstants.PRODUCT_PURSE);
		return mav;

	}

	@SuppressWarnings("unlikely-arg-type")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/showPINtab")
	public ModelAndView getPINTab(@ModelAttribute("productPIN") Product product, HttpServletRequest req)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Product existingProduct = productService.getPinTabByProductId(productId);
		if (existingProduct == null) {
			mav.addObject("productPIN", new Product());
		} else {
			mav.addObject("productPIN", existingProduct);
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("pin");
		return mav;
	}

	@SuppressWarnings("unlikely-arg-type")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/savePinParameters")
	public ModelAndView savePinParameters(@ModelAttribute("productPIN") Product product, HttpServletRequest req,
			BindingResult bindingResult) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		customValidator.validate(product, bindingResult);

		if (bindingResult.hasErrors()) {
			mav.addObject("productPIN", product);
			mav.setViewName("pin");
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}

		ResponseEntity<ResponseDTO> responseDTO = productService.updatePinTab(product.getProductAttributes(),
				productId);
		if (responseDTO != null && responseDTO.getBody() != null) {
			if (ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())) {

				mav.addObject("productPIN", product);
				mav.addObject(CCLPConstants.STATUS, responseDTO.getBody().getMessage());
			} else {

				mav.addObject("productPIN", product);
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getBody().getMessage());
			}
		}

		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("pin");
		return mav;

	}

	@SuppressWarnings("unlikely-arg-type")
	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/getProductPinDetailsById")
	public ModelAndView getProductPinDetailsById(@ModelAttribute("productPIN") Product product)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(product.getProductId());

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Product existingProduct = productService.getPinTabByProductId(product.getParentProductId());
		if (existingProduct == null) {
			mav.addObject("productPIN", new Product());
		} else {
			existingProduct.setParentProductId(product.getParentProductId());
			existingProduct.setProductId(product.getProductId());
			mav.addObject("productPIN", existingProduct);
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("pin");
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_PRODUCT')")
	@RequestMapping("/saveProductAlerts")
	public ModelAndView saveProductAlerts(Map<String, Object> model,
			@ModelAttribute("productAlerts") ProductAlert productAlerts, BindingResult bindingResult,
			HttpServletRequest request) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		Long productId;
		String id = "";
		ModelAndView mav = new ModelAndView();

		session = request.getSession();
		id = request.getParameter("productId");

		if (id == null || id.isEmpty()) {
			productId = (Long) session.getAttribute("productId");
		} else {
			productId = Long.parseLong(id);
		}
		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		customValidatorAlert.validate(productAlerts, bindingResult);

		if (bindingResult.hasErrors()) {
			mav.addObject("parentProductMap", productService.getParentProducts());
			mav.addObject("messagesMap", productService.getMessages());
			mav.addObject("productAlert", productAlerts);
			mav.addObject("messageAlertBox", new ProductAlert());
			mav.addObject("cardStatus", productService.getCardStatusList());
			mav.setViewName("showProductAlerts");
			logger.debug(CCLPConstants.EXIT);
			return mav;
		}

		try {
			mav.addObject("parentProductMap", productService.getParentProducts());

			mav.addObject("messagesMap", productService.getMessages());

		} catch (ServiceException e) {

			logger.error(e);
		}

		Map<String, String> alertAttribs = productAlerts.getAlertAttributes();
		String cardStatusSelList = "";

		if (alertAttribs.get(CCLPConstants.ALERT_CARD_STATUS) != null) {
			cardStatusSelList = alertAttribs.get(CCLPConstants.ALERT_CARD_STATUS);
		}
		logger.debug("alert attributes data :" + alertAttribs.toString());

		ResponseEntity<ResponseDTO> responseDTO = productService.updateAlertsTab(alertAttribs, productId);

		if (responseDTO != null && responseDTO.getBody() != null) {
			if (ResponseMessages.SUCCESS.equals(responseDTO.getBody().getCode())) {

				mav.addObject("productAlert", productAlerts);
				mav.addObject("cardStatus", productService.getCardStatusList());
				mav.addObject(CCLPConstants.SEL_CARD_STATUS, cardStatusSelList);
				mav.addObject(CCLPConstants.STATUS, responseDTO.getBody().getMessage());
			} else {

				mav.addObject("productAlert", productAlerts);
				mav.addObject("cardStatus", productService.getCardStatusList());
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getBody().getMessage());
			}
		}

		session.setAttribute("productId", productId);
		mav.setViewName("showProductAlerts");
		logger.info("Product Id :: " + productId);

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	/**
	 * alert copy function
	 */

	@RequestMapping("/getAlertProductDetails")
	public ModelAndView getAlertProductDetails(@ModelAttribute("productForm") Product productAlerts,
			HttpServletRequest request) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		Long productId;
		String id = "";
		ModelAndView mav = new ModelAndView();
		ResponseDTO responseDTO;
		Map<String, String> alertsData;
		ResponseDTO res = null;
		Map<String, String> alertsdetails = null;

		session = request.getSession();
		id = request.getParameter("productId");

		if (id == null || id.isEmpty()) {
			productId = (Long) session.getAttribute("productId");
		} else {
			productId = Long.parseLong(id);
		}
		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");
		}
		res = productService.getAlertsTab(productAlerts.getParentProductId());
		alertsdetails = (Map<String, String>) res.getData();

		String cardStatusSelList = "";
		if (alertsdetails.get(CCLPConstants.ALERT_CARD_STATUS) != null) {

			cardStatusSelList = String.valueOf(alertsdetails.get(CCLPConstants.ALERT_CARD_STATUS));

		}
		mav.addObject("messagesMap", productService.getMessages());

		responseDTO = productService.getAlertsTab(productAlerts.getParentProductId());
		alertsData = (Map<String, String>) responseDTO.getData();
		ProductAlert productAlert = new ProductAlert(alertsData);
		mav.addObject("cardStatus", productService.getCardStatusList());
		mav.addObject("productAlerts", productAlert);
		mav.addObject(CCLPConstants.SEL_CARD_STATUS, cardStatusSelList);

		productAlert.setParentProductId(productAlerts.getParentProductId() + "");

		mav.setViewName("showProductAlerts");
		logger.info("Product Id :: " + productId);

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@GetMapping("/generateSerialNumber")
	public @ResponseBody String updateSerialForm(HttpServletRequest request,
			@ModelAttribute("productForm") Product product) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		long productId = Long.parseLong(request.getParameter("productId"));

		String upc = request.getParameter("upc");

		long serialNumberQuantity = Long.parseLong(request.getParameter("serialNumberQuantity"));

		String response = productService.getSerialNumberResponse(productId, upc, serialNumberQuantity);

		logger.debug(CCLPConstants.EXIT);
		return response;
	}

	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/showViewProduct")
	public ModelAndView showViewProduct(Map<String, Object> model, @ModelAttribute("productForm") Product productForm,
			HttpServletRequest request) throws ServiceException, ProductException {

		logger.debug(CCLPConstants.ENTER);

		Long productId;
		String id = "";
		Product product = null;
		List<String> cardRangeList = null;
		List<Partner> partnerList = null;
		List<IssuerDTO> issuerList = null;
		List<ProductDTO> productList = null;
		List<PackageDTO> packageList = null;
		List<PurseDTO> purseDTOList = null;

		List<CardRangeDTO> availableCardRangeList = null;

		Map<String, List> ccfList = null;
		Map<String, String> headerData = new HashMap<>();
		List<CurrencyCodeDTO> availableCurrencyCodeList = null;
		String pinTab = "";
		String cvvTab = "";
		String purseTab;
		String limitsTab = "";
		String txnFeesTab = "";
		String alertsTab = "";
		String txnBasedonCardStatTab = "";
		String maintenanceFeesTab = "";

		String addUrl = Util.constructUrl(request);

		ModelAndView mav = new ModelAndView();

		List<String> validityList = new ArrayList<>();

		validityList.add("Hours");
		validityList.add("Days");
		validityList.add("Months");
		validityList.add("Years");

		mav.addObject("validityList", validityList);

		mav.addObject("addUrl", addUrl);
		try {

			session = request.getSession();
			id = request.getParameter("productId");

			if (id == null || id.isEmpty()) {
				productId = (Long) session.getAttribute("productId");
			} else {
				productId = Long.parseLong(id);
			}
			mav.addObject("productConfirmBox", new Product());

			session.setAttribute("productId", productId);

			logger.info("Product Id :: " + productId);
			logger.debug("Before Calling issuerservice.getProductId() ");
			product = productService.getProductById(productId);
			product.setProductName(product.getProductName());
			logger.debug("After Calling issuerservice.getProductId() ");

			Map<String, String> temMap = product.getProdAttributes();
			if (temMap != null) {

				if (temMap.get("validityPeriodFormat") != null) {
					mav.addObject("validityPeriodFormat", temMap.get("validityPeriodFormat"));

				}

				if (temMap.get("denomSelect") != null) {
					mav.addObject("selectedDenom", temMap.get("denomSelect"));
				}

				if (temMap.get("otherTxnId") != null) {

					product.setTxnTypeId(temMap.get("otherTxnId").split(","));

				}

				if (temMap.get("activationId") != null) {

					product.setActivationId(temMap.get("activationId").split(","));

				}

				if (temMap.containsKey("alertSupported") && !Objects.isNull(temMap.get("alertSupported"))) {
					alertsTab = temMap.get("alertSupported");
					headerData.put("AlertsTab", alertsTab);
				}

				if (temMap.containsKey("cvvSupported") && !Objects.isNull(temMap.get("cvvSupported"))) {
					cvvTab = temMap.get("cvvSupported");
					headerData.put("CVVTab", cvvTab);

				}

				if (temMap.containsKey("feesSupported") && !Objects.isNull(temMap.get("feesSupported"))) {
					txnFeesTab = temMap.get("feesSupported");
					headerData.put("TxnFeesTab", txnFeesTab);
				}

				if (temMap.containsKey("pinSupported") && !Objects.isNull(temMap.get("pinSupported"))) {
					pinTab = temMap.get("pinSupported");
					headerData.put("PINTab", pinTab);
				}
				
				if (temMap.containsKey("multiPurseSupport") && !Objects.isNull(temMap.get("multiPurseSupport"))) {
					purseTab = temMap.get("multiPurseSupport");
					headerData.put("PurseTab", purseTab);
				}
				

				if (temMap.containsKey("limitSupported") && !Objects.isNull(temMap.get("limitSupported"))) {
					limitsTab = temMap.get("limitSupported");
					headerData.put("LimitsTab", limitsTab);
				}
				if (temMap.containsKey("cardStatusSupported") && !Objects.isNull(temMap.get("cardStatusSupported"))) {
					txnBasedonCardStatTab = temMap.get("cardStatusSupported");
					headerData.put("TxnBasedonCardStatTab", txnBasedonCardStatTab);
				}

				if (temMap.containsKey("maintainanceFeeSupported")
						&& !Objects.isNull(temMap.get("maintainanceFeeSupported"))) {
					maintenanceFeesTab = temMap.get("maintainanceFeeSupported");
					headerData.put("MaintenanceFeesTab", maintenanceFeesTab);
				}

			}
			mav.addObject("productForm", product);
			mav.addObject("editProductId", id);

			cardRangeList = cardRangeService.getAllCardRanges();
			partnerList = partnerservice.getAllPartners();
			issuerList = issuerService.getAllIssuers();
			productList = productService.getAllProducts();
			packageList = packageService.getAllPackages();
			purseDTOList = partnerservice.getAllSupportedPurses(product.getPartnerId());
			ccfList = productService.getCCFList();

			availableCardRangeList = cardRangeService.getCardRangeByIssuerId(product.getIssuerId());
			

			availableCurrencyCodeList = productService.getCurrencyCodeList(product.getPartnerId());
			
			mav.addObject("issuerDropDown", issuerList);
			mav.addObject("partnerDropDown", partnerList);
			mav.addObject("parentProductDropDown", productList);
			mav.addObject("cardRangeData", cardRangeList);
			mav.addObject("packageData", packageList);
			mav.addObject("ccfData", ccfList.get("ccfData"));
			mav.addObject("purseData", purseDTOList);
			mav.addObject("availablePartnerCurrencyList", availableCurrencyCodeList);

			mav.addObject("availableCardRangeList", availableCardRangeList);

			session.setAttribute("headerData", headerData);

			mav.setViewName("viewProduct");
			mav.addObject("messageAlertBox", new ProductAlert());

		} catch (NumberFormatException e) {
			mav.setViewName("viewProduct");
			logger.error("NumberFormatException Occured While Editing Product in editProduct()" + e);
		} catch (IssuerException e) {
			logger.error("Exception Occured While Editing Product in editProduct()" + e);
		}

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@SuppressWarnings("unlikely-arg-type")
	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewGeneralTab")
	public ModelAndView viewGeneralTab(@ModelAttribute("productGeneral") Product product, HttpServletRequest req)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		mav.addObject("ruleSetMap", productService.getRuleSetMetaData());
		Map<String, Map<String, String>> authenticationTypes = productService.getAuthTypeMetaData();

		if (!CollectionUtils.isEmpty(authenticationTypes)) {
			mav.addObject("chwUserAuthTypes", authenticationTypes.get("chwUserAuthType"));
			mav.addObject("ivrUserAuthTypes", authenticationTypes.get("ivrUserAuthType"));
		}
		Product existingProduct = productService.getGeneralTabByProductId(productId);
		if (existingProduct == null) {
			mav.addObject("productGeneral", new Product());
		} else {
			mav.addObject("productGeneral", existingProduct);
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("viewProductGeneral");
		return mav;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewProductLimit")
	public ModelAndView viewProductLimit(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView();
		session = request.getSession(false);

		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));
		logger.debug("product Id : " + mainProductId);

		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		
		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copy Product Id : " + copyProductId);

		Product product = new Product();

		if (mainProductId != null || copyProductId != null) {

			String productId = copyProductId != null ? copyProductId : mainProductId;
			String parentProductId = copyProductId == null ? "0" : copyProductId;
			ModelMapper mm = new ModelMapper();
			List<Object> txnDtls = productService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.setViewName("viewProductLimits");

			product.setProductId(Long.parseLong(mainProductId));
			ResponseDTO responseDTO = productService.getLimits(Long.parseLong(mainProductId),Long.parseLong(parentProductId),Long.parseLong(purseId));
			if (responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> productLimitMap = responseDTO.getData() != null
						? mm.map(responseDTO.getData(), Map.class)
						: new HashMap<>();
				product.setProductAttributes(productLimitMap);
				mav.addObject("productForm", product);

				Map<Object, String> parentProductMap = productService.getAllParentProducts();

				if (parentProductMap != null && !parentProductMap.isEmpty()) {

					parentProductMap.remove(mainProductId);
					mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
					mav.addObject("copyProductId", copyProductId);
					mav.addObject("showCopyFrom", "true");
					logger.debug(parentProductMap);
				}
				
				List<PurseDTO> purseList = productService.getPurseByProductId(Long.parseLong(mainProductId),Long.parseLong(productId),"Limits");
				mav.addObject(CCLPConstants.PURSE_LIST, purseList);
				if (Long.parseLong(purseId) < 0) {
					Product defaultPurseProduct = productService.getProductById(Long.parseLong(mainProductId));

					Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
					Map<String, String> prodAttributes = attributes.get("Product");
					purseId = prodAttributes.get("defaultPurse");
					logger.info("Default Purse is : " + purseId);
				}
				mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);

			} else {
				mav.addObject("productForm", product);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getMessage());
			}
		} else {
			logger.error("mainProductId and copyProductId are null");

		}

		logger.debug("EXIT " + product);

		return mav;
	}

	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewCVVtab")
	public ModelAndView viewCVVtab(@ModelAttribute("productCVV") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();
		session = req.getSession(false);
		Long productId = (Long) session.getAttribute("productId");

		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {

			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Product existingProduct = productService.getCVVTabByProductId(productId);
		if (existingProduct == null) {
			mav.addObject("productCVV", new Product());
		} else {
			existingProduct.setProductId(productId);
			mav.addObject("productCVV", existingProduct);
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("viewProductCvv");
		return mav;
	}

	@SuppressWarnings("unlikely-arg-type")
	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewPINtab")
	public ModelAndView viewPINtab(@ModelAttribute("productPIN") Product product, HttpServletRequest req)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");

		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Product existingProduct = productService.getPinTabByProductId(productId);
		if (existingProduct == null) {
			mav.addObject("productPIN", new Product());
		} else {
			mav.addObject("productPIN", existingProduct);
		}
		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("viewProductPin");
		return mav;
	}

	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping("/viewTxnCardStatus")
	public ModelAndView viewTxnCardStatus(@ModelAttribute("productStat") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		session = req.getSession(false);
		Long productId = (Long) session.getAttribute("productId");
		String mainProductId = String.valueOf(product.getProductId());

		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");
		}

		List<Object> txnDtls = productService.getDeliveryChnlTxns();
		logger.debug(txnDtls);

		mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
		mav.addObject("cardStatusList", productService.getCardStatus());

		Product existingProduct = productService.getTxnBasedOnCardStatus(productId);
		if (existingProduct == null) {
			mav.addObject("productStat", new Product());
		} else {
			existingProduct.setProductId(productId);
			mav.addObject("productStat", existingProduct);
		}

		logger.debug(CCLPConstants.EXIT);
		mav.setViewName("viewProductTxnCardStatus");
		return mav;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewProductTransactionFee")
	public ModelAndView viewProductTransactionFee(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		ModelAndView mav = new ModelAndView();
		session = request.getSession(false);

		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));

		logger.debug("product Id is: " + mainProductId);

		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copy Product Id is: " + copyProductId);

		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		
		Product product = new Product();

		if (mainProductId != null || copyProductId != null) {

			String productId = copyProductId != null ? copyProductId : mainProductId; //
			String parentProductId = copyProductId == null ? "0" : copyProductId;
			ModelMapper mm = new ModelMapper();
			List<Object> txnDtls = productService.getDeliveryChnlTxns();
			logger.debug(txnDtls);

			mav.addObject(CCLPConstants.DELIVERY_CHANNEL_LIST, txnDtls.get(0));
			mav.addObject(CCLPConstants.TXN_FINANCIAL_FLAG_MAP, txnDtls.get(1));
			mav.setViewName("viewProductTxnFee");

			product.setProductId(Long.parseLong(mainProductId));
			ResponseDTO responseBody = productService.getTxnFees(Long.parseLong(mainProductId),Long.parseLong(parentProductId),Long.parseLong(purseId));

			logger.debug("responseBody " + responseBody);
			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {

				Map<String, Object> productTxnFeeMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				product.setProductAttributes(productTxnFeeMap);
				mav.addObject("productForm", product);
				Map<Object, String> parentProductMap = productService.getAllParentProducts();
				if (parentProductMap != null && !parentProductMap.isEmpty()
						&& parentProductMap.keySet().contains(mainProductId)) {

					parentProductMap.remove(mainProductId);
					mav.addObject("showCopyFrom", "true");
					mav.addObject("copyProductId", copyProductId);

				}
				mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
				
				List<PurseDTO> purseList = productService.getPurseByProductId(Long.parseLong(mainProductId),Long.parseLong(productId),"Fees");
				mav.addObject(CCLPConstants.PURSE_LIST, purseList);
				if (Long.parseLong(purseId) < 0) {
					Product defaultPurseProduct = productService.getProductById(Long.parseLong(mainProductId));

					Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
					Map<String, String> prodAttributes = attributes.get("Product");
					purseId = prodAttributes.get("defaultPurse");
					logger.info("Default Purse is : " + purseId);
				}
				mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);

			} else {
				mav.addObject("productForm", product);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {
			logger.error("Product ID is null or Empty");

		}
		logger.debug("EXIT " + product);
		return mav;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewProductMaintenanceFee")
	public ModelAndView viewProductMaintenanceFee(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		ModelAndView mav = new ModelAndView("viewProductMaintenanceFee");
		session = request.getSession(false);

		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));
		logger.debug("productId : " + mainProductId);

		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copyProductId : " + copyProductId);

		Product product = new Product();
		product.setProductId(Long.valueOf(mainProductId));
		mav.addObject("productForm", product);
		if (mainProductId != null || copyProductId != null) {

			String productId = copyProductId != null ? copyProductId : mainProductId;
			ResponseDTO responseBody = productService.getMaintenanceFee(Long.parseLong(productId));

			if (responseBody.getResult().equalsIgnoreCase("success")) {
				Map<String, Object> productFeeCapMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				if (productFeeCapMap != null) {

					logger.debug("productMaintenanceFeeMap " + productFeeCapMap);
					product.setProductAttributes(productFeeCapMap);
					mav.addObject("productForm", product);

					Map<Object, String> parentProductMap = productService.getAllParentProducts();

					if (parentProductMap != null && !parentProductMap.isEmpty()
							&& parentProductMap.keySet().contains(mainProductId)) {

						parentProductMap.remove(mainProductId);
						mav.addObject("showCopyFrom", "true");
						mav.addObject("copyProductId", copyProductId);
					}
					mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
				}
			} else {
				logger.error("Failed to Fetch Maintenance Fee Attributes from config srvice");
				mav.addObject("productForm", product);
				mav.addObject("statusFlag", "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {
			logger.error("Product ID is null");
		}
		return mav;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewProductMonthlyFeeCap")
	public ModelAndView viewProductMonthlyFeeCap(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		ModelAndView mav = new ModelAndView("viewProductMonthlyFeeCap");
		session = request.getSession();

		String mainProductId = String.valueOf((Long) session.getAttribute("productId"));

		logger.debug("productId : " + mainProductId);

		String copyProductId = request.getParameter("copyFromProductId");
		logger.debug("copyProductId : " + copyProductId);

		Product product = new Product();
		product.setProductId(Long.valueOf(mainProductId));
		mav.addObject("productForm", product);
	
		String purseId = request.getParameter("purseId") == null ? "-1" :request.getParameter("purseId");
		logger.debug("purse Id is: " + purseId);
		
		if (mainProductId != null || copyProductId != null) {

			String productId = copyProductId != null ? copyProductId : mainProductId;
			String parentProductId = copyProductId == null ? "0" : copyProductId;
			ResponseDTO responseBody = productService.getMonthlyFeeCap(Long.parseLong(mainProductId),Long.parseLong(parentProductId),Long.parseLong(purseId));

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<String, Object> productFeeCapMap = responseBody.getData() != null
						? mm.map(responseBody.getData(), Map.class)
						: new HashMap<>();
				if (productFeeCapMap != null) {

					logger.debug("productFeeCapMap " + productFeeCapMap);

					product.setProductAttributes(productFeeCapMap);
					mav.addObject("productForm", product);

					Map<Object, String> parentProductMap = productService.getAllParentProducts();

					if (parentProductMap != null && !parentProductMap.isEmpty()
							&& parentProductMap.keySet().contains(mainProductId)) {

						parentProductMap.remove(mainProductId);
						mav.addObject("showCopyFrom", "true");
						mav.addObject("copyProductId", copyProductId);

					}
					mav.addObject(CCLPConstants.PRODUCT_MAP, parentProductMap);
					
					List<PurseDTO> purseList = productService.getPurseByProductId(Long.parseLong(mainProductId),Long.parseLong(productId),"Fees");
					mav.addObject(CCLPConstants.PURSE_LIST, purseList);
					if (Long.parseLong(purseId) < 0) {
						Product defaultPurseProduct = productService.getProductById(Long.parseLong(mainProductId));

						Map<String, Map<String, String>> attributes = defaultPurseProduct.getAttributes();
						Map<String, String> prodAttributes = attributes.get("Product");
						purseId = prodAttributes.get("defaultPurse");
						logger.info("Default Purse is : " + purseId);
					}
					mav.addObject(CCLPConstants.SELECTED_PURSE_ID, purseId);
					

				}
			} else {
				logger.error("Failed to Fetch Monthly Fee Cap Attributes from config srvice");
				mav.addObject("productForm", product);
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, responseBody.getMessage());
			}
		} else {

			logger.error("Product ID is null");
		}
		return mav;
	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewProductAlerts")
	public ModelAndView viewProductAlerts(Map<String, Object> model,
			@ModelAttribute("productAlerts") ProductAlert productAlerts, HttpServletRequest request)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		Long productId;
		String id = "";
		ModelAndView mav = new ModelAndView();

		ResponseDTO responseDTO = null;
		ResponseDTO res = null;
		Map<String, String> alertsData = null;
		Map<String, String> alertsdetails = null;

		session = request.getSession();
		id = request.getParameter("productId");

		if (id == null || id.isEmpty()) {
			productId = (Long) session.getAttribute("productId");
		} else {
			productId = Long.parseLong(id);
		}
		Map<Long, String> parentProductMap = productService.getParentProducts();
		String mainProductId = String.valueOf(productId);

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(mainProductId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");

		}

		Map<String, String> cardStatus = productService.getCardStatusList();

		res = productService.getAlertsTab(productId);
		alertsdetails = (Map<String, String>) res.getData();

		String cardStatusSelList = "";
		if (alertsdetails.get(CCLPConstants.ALERT_CARD_STATUS) != null) {

			cardStatusSelList = String.valueOf(alertsdetails.get(CCLPConstants.ALERT_CARD_STATUS));

		}

		try {
			mav.addObject("parentProductMap", productService.getParentProducts());

			mav.addObject("messagesMap", productService.getMessages());

			mav.addObject("cardStatus", cardStatus);

			mav.addObject(CCLPConstants.SEL_CARD_STATUS, cardStatusSelList);

			responseDTO = productService.getAlertsTab(productId);

			if (responseDTO.getCode().equalsIgnoreCase("000")) {

				alertsData = (Map<String, String>) responseDTO.getData();
				ProductAlert productAlert = new ProductAlert(alertsData);
				mav.addObject("productAlerts", productAlert);
			} else if (responseDTO.getCode().equalsIgnoreCase("999")) {
				mav.addObject("productAlerts", new ProductAlert());
			}

		} catch (ServiceException e) {

			logger.error(e);
		}
		session.setAttribute("productId", productId);
		mav.setViewName("viewAlerts");
		logger.info("Product Id :: " + productId);

		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('DOWNLOAD_PRODUCT')")
	@RequestMapping(value = "/download")
	public ModelAndView generatePdfDocument(HttpServletRequest request) throws  ServiceException {
		logger.debug(CCLPConstants.ENTER);
		long productId;
		String id = null;
		ModelAndView view = new ModelAndView();
		Map<String, Object> model = new HashMap<>();
		session = request.getSession();
		id = request.getParameter("productId");
		if (id == null || id.isEmpty()) {
			productId = (Long) session.getAttribute("productId");
		} else {
			productId = Long.parseLong(id);
		}
		Product productDtos = productService.getProductById(productId);
		productDtos.setProductName(productDtos.getProductId() + ":" + productDtos.getProductName());
		Map<Double, String> buildPdfMetaData = productService.generatePdfDocument();
		List<DeliveryChannel> txnDtls = productService.getDelChnlTxns();
		List<String> cardStatusList = productService.getCardStatus();
		@SuppressWarnings("unchecked")
		ProductAlert productAlert = new ProductAlert(
				(Map<String, String>) productService.getAlertsTab(productId).getData());
		List<PackageDTO> packageList = packageService.getAllPackages();
		List<PurseDTO> purseLIst = purseService.getAllPurses();
		Map<String, String> cardStatus = productService.getCardStatusList();
		Product getGeneralAttr = productService.getGeneralTabByProductId(productId);
		Map<Long, String> ruleSetMap = productService.getRuleSetMetaData();
		List<ProgramIDDTO> programIdDto = programIdService.getAllProgramIds();
		Map<String, Map<String, String>> authenticationTypes = productService.getAuthTypeMetaData();
		txnDtls.sort(Comparator.comparing(DeliveryChannel::getDeliveryChnlShortName));
		
		/*Collections.sort(txnDtls, new Comparator<DeliveryChannel>() {
			@Override
			public int compare(DeliveryChannel s1, DeliveryChannel s2) {
				return s1.getDeliveryChnlShortName().compareToIgnoreCase(s2.getDeliveryChnlShortName());
			}
		});*/
		String ruleSetName = ruleSetMap.get(getGeneralAttr.getRuleSetId());
		model.put("ruleSet", ruleSetName);
		model.put("programIdData", programIdDto);
		model.put("productAlerts", productAlert);
		model.put("product", productDtos);
		model.put("txnDetails", txnDtls);
		model.put("cardStatusList", cardStatusList);
		model.put("cardStatus", cardStatus);
		model.put("languageDesc", languageDesc);
		model.put("mainFeeMap", mainFeeMap);
		model.put("monthlyfeeCapMap", monthlyfeeCapMap);
		model.put("productPurseAttributes", productPurseAttributes);
		model.put("package", packageList);
		model.put("purse", purseLIst);
		if (!CollectionUtils.isEmpty(authenticationTypes)) {
			model.put("chwUserAuthTypes", authenticationTypes.get("chwUserAuthType"));
			model.put("ivrUserAuthTypes", authenticationTypes.get("ivrUserAuthType"));
		}

		if (productDtos != null && buildPdfMetaData != null) {
			try {

				return new ModelAndView(new PdfGenerator(buildPdfMetaData), model);
			} catch (Exception e) {
				throw new ServiceException("Failed to generate document");
			}

		} else {
			view.setViewName("searchProductByName");
			return view;
		}
	}

	

	@PreAuthorize("hasRole('VIEW_PRODUCT')")
	@RequestMapping("/viewProductPurse")
	public ModelAndView viewProductPurseTab(@ModelAttribute("productPurse") Product product, HttpServletRequest req)
			throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		String addUrl = Util.constructUrl(req);
		mav.addObject("addUrl", addUrl);
		
		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");
		
		List<PurseDTO> purseList = productService.getPurseByProductId(productId, productId, "Purse");
		if (purseList == null || purseList.isEmpty()) {
			mav.addObject(CCLPConstants.STATUS_MESSAGE, "No Purses are attached to this Product");
		}
		Map<String,String> timeZone = Util.getTimeZones(); 
    	mav.addObject(CCLPConstants.TIME_ZONE, timeZone);
		mav.addObject(CCLPConstants.PURSE_LIST, purseList);
		logger.info(CCLPConstants.EXIT);
		mav.setViewName("viewProductPurse");
		return mav;
	}
	
	@RequestMapping("/getParentProductPurseIds")
	public ModelAndView getParentProductPurseIds(@ModelAttribute("productPurse") Product product, HttpServletRequest req)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView();

		String addUrl = Util.constructUrl(req);
		mav.addObject("addUrl", addUrl);
		session = req.getSession();
		Long productId = (Long) session.getAttribute("productId");

		Map<Long, String> parentProductMap = productService.getParentProducts();

		if (parentProductMap != null && !parentProductMap.isEmpty()) {
			parentProductMap.remove(productId);
			mav.addObject("parentProductMap", parentProductMap);
			mav.addObject("showCopyFrom", "true");
		}
		List<PurseDTO> purseList = productService.getPurseByProductId(productId, product.getParentProductId(),"Purse");

		mav.addObject(CCLPConstants.PURSE_LIST, purseList);
		mav.setViewName(CCLPConstants.PRODUCT_PURSE);
		logger.info(CCLPConstants.EXIT);
		return mav;
	}
}
