package com.incomm.cclpvms.config.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclpvms.admin.model.GroupDTO;
import com.incomm.cclpvms.admin.service.GroupService;
import com.incomm.cclpvms.config.model.CardRangeDTO;
import com.incomm.cclpvms.config.model.CountryCodeDTO;
import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.GroupAccessDTO;
import com.incomm.cclpvms.config.model.ProgramID;
import com.incomm.cclpvms.config.model.ProgramIDDTO;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.RedemptionDelayDTO;
import com.incomm.cclpvms.config.service.CardRangeService;
import com.incomm.cclpvms.config.service.GroupAccessService;
import com.incomm.cclpvms.config.service.LocationService;
import com.incomm.cclpvms.config.service.PackageService;
import com.incomm.cclpvms.config.service.PartnerService;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.config.service.ProgramIdService;
import com.incomm.cclpvms.config.service.RedemptionService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.CronSchedule;
import com.incomm.cclpvms.util.CronUtil;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

	private static final Logger logger = LogManager.getLogger(AjaxController.class);

	HttpSession session;
	
	@Autowired
	CardRangeService cardRangeService;
	@Autowired
	PartnerService partnerService;

	/**
	 * the GroupAccessService service.
	 */
	@Autowired
	private GroupAccessService groupAccessService;
	@Autowired
	private ProductService productService;
	@Autowired
	private GroupService groupService;

	@Autowired
	private PackageService packageService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private RedemptionService redemptionService;

	@Autowired
	private ProgramIdService programIdService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getApprovedCardRangeDetails/{issuerId}", method = RequestMethod.GET)
	public ResponseEntity getCardRangeDetails(@PathVariable("issuerId") Long issuerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		List<CardRangeDTO> cardRange = cardRangeService.getCardRangeByIssuerId(issuerId);

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity(cardRange, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getProgramIdDetails/{partnerId}", method = RequestMethod.GET)
	public ResponseEntity getProgramIdDetails(@PathVariable("partnerId") Long partnerId) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);

		List<ProgramIDDTO> programIdDto = programIdService.getProgramIdsByPartnerId(partnerId);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity(programIdDto, HttpStatus.OK);

	}

	/* Group access starts */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getGroupAccessProducts/{groupAccessId}", method = RequestMethod.GET)
	public ResponseEntity getGroupAccessProducts(@PathVariable("groupAccessId") Long groupAccessId)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity(groupAccessService.getProductsByAccessId(groupAccessId), HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getGroupAccessPartners/{groupAccessId}/{productId}", method = RequestMethod.GET)
	public ResponseEntity getGroupAccessPartners(@PathVariable("groupAccessId") Long groupAccessId,
			@PathVariable("productId") Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<GroupAccessDTO> groupAccessList = groupAccessService
				.getGroupAccessPartnersByAccessIDAndProductId(groupAccessId, productId);
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity(groupAccessList, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getGroupAccessPartners/{groupAccessId}", method = RequestMethod.GET)
	public ResponseEntity getGroupAccessPartnersforUpdateGroupAccess(@PathVariable("groupAccessId") Long groupAccessId)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		List<GroupAccessDTO> groupAccessList = groupAccessService.getGroupAccessPartnersByAccessID(groupAccessId);
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity(groupAccessList, HttpStatus.OK);
	}

	/* Group access ends */

	/** Group Config */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getRoleList/{groupId}", method = RequestMethod.GET)
	public ResponseEntity getRoleNamesbyGroupName(@PathVariable("groupId") Long groupId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		logger.debug(CCLPConstants.EXIT);
		GroupDTO groupDTO = groupService.getRoleNamesbyGroupId(groupId);
		return new ResponseEntity(groupDTO, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getStates/{country}", method = RequestMethod.GET)
	public ResponseEntity getStates(@PathVariable("country") Long countryId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		CountryCodeDTO countryCodeDTO = locationService.getStates(countryId);

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity(countryCodeDTO, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getOverlapRedemptionDetails/{previousValue}/{currentValue}", method = RequestMethod.GET)
	public ResponseEntity getOverlapRedemptionDetails(@PathVariable("previousValue") String previousValue,
			@PathVariable("currentValue") String currentValue) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String message = "";
		message = redemptionService.getOverlapDelayDetails(previousValue, currentValue);
		String s = "{\"Msg\":\"" + message + "\"}";

		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity(s, HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getMerchantProductRedemption/{productId}/{merchantId}", method = RequestMethod.GET)
	public ResponseEntity getMerchantProductRedemption(@PathVariable("productId") Long productId,
			@PathVariable("merchantId") String merchantId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<RedemptionDelayDTO> redemptionList = redemptionService.getRedemptionDelayByMerchantIdProductId(productId,
				merchantId);
		logger.debug(CCLPConstants.EXIT);
		return new ResponseEntity(redemptionList, HttpStatus.OK);
	}

	/* added by nawaz for autocomeplete */
	@RequestMapping(value = "/getParentProducts", method = RequestMethod.GET)
	public ResponseEntity<Map<Object, String>> getParentProducts() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		Map<Object, String> data = productService.getAllParentProducts();

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@RequestMapping(value = "/getParentPackagedetails", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getPackagedetails() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		Map<String, String> data = packageService.getPackageIdList();

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	/**
	 * added for getting the description in program Id
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getProgramIdDescription/{programID}", method = RequestMethod.GET)
	public ResponseEntity getProgramIdDescription(@PathVariable("programID") Long programID) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ProgramID programList = programIdService.getProgramId(programID);

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity(programList, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getTransactionListByChannelName/{channelName}", method = RequestMethod.GET)
	public ResponseEntity getTransactionListByChannelName(@PathVariable("channelName") String channelName)
			throws ServiceException	 {
		logger.debug(CCLPConstants.ENTER);

		List<Object> transactionDetails = productService.getDeliveryChnlTxnsByChnlCode(channelName);

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity(transactionDetails, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/getPartnerCurrency/{partnerId}")
	public ResponseEntity getPartnerCurrency(@PathVariable("partnerId") String partnerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		List<CurrencyCodeDTO> partnerCurrencyDetails = productService.getCurrencyCodeList(Long.valueOf(partnerId));

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity(partnerCurrencyDetails, HttpStatus.OK);
	}
	


	@RequestMapping(value = "/getProductPurseAttributes/{productId}/{purseId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProductPurseAttributes(@PathVariable("productId") Long productId,@PathVariable("purseId") Long purseId,HttpServletRequest req)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);

		session = req.getSession();
		Long id = (productId == CCLPConstants.DROPDOWN_DEFAULTVALUE) ? (Long) session.getAttribute("productId") : productId;

		Map<String, Object> productPurseAttributes = productService.getProductPurseAttributes(id, purseId);
		
           String autoTopupEnable = String.valueOf(productPurseAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_ENABLE));		
		if (CCLPConstants.ENABLE.equals(autoTopupEnable)) {
			String autoTopupFrequency = String.valueOf(productPurseAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_FREQUENCY));
			String autoTopupCron = String.valueOf(productPurseAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_TOPUP_CRON));
			productPurseAttributes.putAll(CronUtil.convertFrequencyDtlsFromCron(autoTopupFrequency, autoTopupCron, "topup"));
		}
		
		String autoReloadEnable = String.valueOf(productPurseAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_ENABLE));		
		if (CCLPConstants.ENABLE.equals(autoReloadEnable)) {
			String autoReloadFrequency = String.valueOf(productPurseAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_FREQUENCY));
			String autoReloadCron = String.valueOf(productPurseAttributes.get(CCLPConstants.ATTRIBUTE_AUTO_RELOAD_CRON));
			productPurseAttributes.putAll(CronUtil.convertFrequencyDtlsFromCron(autoReloadFrequency, autoReloadCron, "reload"));
		}
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(productPurseAttributes, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getAllSupportedPurses/{partnerId}", method = RequestMethod.GET)
	public ResponseEntity getAllSupportedPurses(@PathVariable("partnerId") Long partnerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		List<PurseDTO> supportedPurses = partnerService.getAllSupportedPurses(partnerId);

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity(supportedPurses, HttpStatus.OK);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getProgramIdPurses/{programId}", method = RequestMethod.GET)
	public ResponseEntity getProgramIdPurses(@PathVariable("programId") Long programId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		List<PurseDTO> supportedPurses = partnerService.getProgramIDPartnerPurses(programId);

		logger.debug(CCLPConstants.EXIT);

		return new ResponseEntity(supportedPurses, HttpStatus.OK);
	}
	

}
