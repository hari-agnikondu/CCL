package com.incomm.cclp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.CustomerProfileDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CustomerProfileService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.Util;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/customerProfile")
@Api(value="customerProfile")
public class CustomerProfileController {

	
	@Autowired
	CustomerProfileService customerProfileService;
	
	@Autowired
	private ResponseBuilder responseBuilder;
	
	private static final Logger logger = LogManager.getLogger(CustomerProfileController.class);
	
	/**
	 * Creates an product.
	 * 
	 * @param productDto
	 *            The productDto.
	 * 
	 * @return the ResponseEntity with the result.
	 * @throws ServiceException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> createCustomerProfile(@RequestBody CustomerProfileDTO customerProfileDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		logger.info(customerProfileDto.toString());
		customerProfileService.createCustomerProfile(customerProfileDto);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null, ResponseMessages.SUCCESS_CUSTOMER_PROFILE_CREATE,ResponseMessages.SUCCESS);
		logger.info(responseDto.toString());
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{profileId}",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCustomerProfileById(@PathVariable("profileId") Long profileId) {
		logger.info(CCLPConstants.ENTER);
		CustomerProfileDTO customerProfileDto = customerProfileService.getCustomerProfileById(profileId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(customerProfileDto,
				ResponseMessages.SUCCESS_CUSTOMER_PROFILE_RETRIEVE,ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
			
	}

	/**
	 * Getting the Orders to check Order Status
	 * 
	 * @param The orderId or ProductId as input parameters
	 *      
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/searchCustomerProfile/{accountNumber}/{cardNumber}/{proxyNumber}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCustomerProfiles(@PathVariable("accountNumber") String accountNumber,
			@PathVariable("cardNumber") String cardNumber,@PathVariable("proxyNumber") String  proxyNumber){
		logger.info(CCLPConstants.ENTER);
		List<CustomerProfileDTO> customerProfileDtoList = customerProfileService.getCustomerProfiles(accountNumber, cardNumber,proxyNumber);
		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(customerProfileDtoList,
				( ResponseMessages.CUSTOMER_PROFILE_RETRIEVE_SUCCESS), ResponseMessages.SUCCESS);

		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{profileId}",method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteCustomerProfileById(@PathVariable("profileId") Long profileId) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		
		if(profileId>0)
		 customerProfileService.deleteCustomerProfileById(profileId);

		ResponseDTO responseDto = responseBuilder.buildSuccessResponse(null,
				ResponseMessages.SUCCESS_PROFILE_DELETE,ResponseMessages.SUCCESS);
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
			
	}
	
	/**
	 * Update the Limit Attributes After fetching the existing Card Limits from actual Card
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{profileId}/card", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateCardAttributes(@RequestBody Map<String, Object> inputCardAttributes,
			@PathVariable("profileId") Long profileId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (profileId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
			logger.info("Profile Id is negative {}",profileId);
		} else {
			try {
				logger.debug("Updating limit attributes for profileId: {} Limit Attributes -> {}" , profileId,inputCardAttributes);
				int count = customerProfileService.updateFeeLimitAttributes(inputCardAttributes, profileId,"Card");
				if (count > 0) {
					logger.info("Card attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,ResponseMessages.SUCCESS_CARD_PROFILE_UPDATE,ResponseMessages.SUCCESS);
				} else {
					logger.info("Failed to update cards attributes");	
					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_PROFILE_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating limit attributes {}",e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_PROFILE_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	/**
	 * Update the Limit Attributes After fetching the existing Card Limits from actual Card
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{profileId}/limits", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateLimitAttributes(@RequestBody Map<String, Object> inputLimitAttributes,
			@PathVariable("profileId") Long profileId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (profileId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
			logger.info("Profile Id is negative {}",profileId);
		} else {
			try {
				logger.debug("Updating limit attributes for profileId: {} Limit Attributes -> {}" , profileId,inputLimitAttributes);
				int count = customerProfileService.updateFeeLimitAttributes(inputLimitAttributes, profileId,"Limits");
				if (count > 0) {
					logger.info("Limit attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,ResponseMessages.SUCCESS_CARD_LIMITS_UPDATE,ResponseMessages.SUCCESS);
				} else {
					logger.info("Failed to update limits attributes");	
					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_LIMITS_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating limit attributes {}",e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_LIMITS_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}
	
	/**
	 * Getting the Card Limits from actual Card  Using profileId 
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{profileId}/limits", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getLimitAttributes(@PathVariable("profileId") Long profileId)
			 {

		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;

		if (profileId <= 0) {
			logger.info("Product Id cant negative");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Get limit attributes for product Id: {}",profileId);
			Map<String, Object> limitAttributes = customerProfileService.getCardFeeLimitsById(profileId,"Limits");

			 if (CollectionUtils.isEmpty(limitAttributes)){
				logger.info("Failed to get limit attributes, limitAttribute is empty");
				responseDto = responseBuilder.buildSuccessResponse(limitAttributes,
						ResponseMessages.EMPTY_CARD_LIMITS_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Limit attributes retrieved successfully");
				responseDto = responseBuilder.buildSuccessResponse(limitAttributes,
						ResponseMessages.SUCCESS_CARD_LIMITS_RETRIEVE,ResponseMessages.SUCCESS);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	
	/**
	 * Update the Fees Attributes After fetching the existing Card fees using ProfileId
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{profileId}/txnFees", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateTxnFeeAttributes(@RequestBody Map<String, Object> inputFeeAttributes,
			@PathVariable("profileId") Long profileId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		
		if (profileId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
			logger.info("Card Id is negative {}",profileId);
		} else {
			try {
				logger.debug("Updating fee attributes for ProfileId: {} fee Attributes -> {}" , profileId,inputFeeAttributes);
				int count = customerProfileService.updateFeeLimitAttributes(inputFeeAttributes, profileId,"Transaction Fees");
				if (count > 0) {
					logger.info("Fee attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,ResponseMessages.SUCCESS_CARD_TXN_FEES_UPDATE,ResponseMessages.SUCCESS);
				} else {
					logger.info("Failed to update fees attributes");	
					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_TXN_FEES_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating fee attributes {}",e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_TXN_FEES_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}

	/**
	 * Getting the Card transaction fee from Product Using ProductId 
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{profileId}/txnFees", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getTxnFeesAttributes(@PathVariable("profileId") Long profileId)
			{
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, Object> feeAttributes = null;

		if (profileId <= 0) {
			logger.info("Product Id should not be negative");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.debug("Get fee attributes for product Id: {}",profileId);
			feeAttributes = customerProfileService.getCardFeeLimitsById(profileId,"Transaction Fees");

			 if (CollectionUtils.isEmpty(feeAttributes)) {
				logger.info("Failed to get fee attributes, feeAttribute is empty");
				responseDto = responseBuilder.buildSuccessResponse(feeAttributes,
						ResponseMessages.EMPTY_CARD_TXN_FEES_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.debug("Fee attributes retrieved successfully feeAttributes -> {}",feeAttributes);
				responseDto = responseBuilder.buildSuccessResponse(feeAttributes,
						ResponseMessages.SUCCESS_CARD_TXN_FEES_RETRIEVE,ResponseMessages.SUCCESS);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	/**
	 * Update the maintenance Fees Attributes After fetching the existing Product fees using productId
	 * 
	 * @return the ResponseEntity with the result.
	 */

	@RequestMapping(value = "/{profileId}/maintenanceFee", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateMaintenanceFeeAttributes(@RequestBody Map<String, Object> inputFeeAttributes,
			@PathVariable("profileId") Long profileId) {
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		
		if (profileId <= 0) {
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
			logger.error("Product id is negative {}",profileId);
		} else {
			try {
				logger.debug("Updating MaintenanceFee attributes for productId: {} MaintenanceFee Attributes -> {}" , profileId,inputFeeAttributes);
				int count = customerProfileService.updateFeeLimitAttributes(inputFeeAttributes, profileId,"Maintenance Fees");
				if (count > 0) {
					logger.info("MaintenanceFee attributes updated successfully");
					responseDto = responseBuilder.buildSuccessResponse(count,ResponseMessages.SUCCESS_CARD_MAINTENANCE_FEE_UPDATE,ResponseMessages.SUCCESS);
				} else {
					logger.info("Failed to update MaintenanceFee attributes");	
					responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_MAINTENANCE_FEE_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);

				}
			} catch (Exception e) {
				logger.error("Error while updating MaintenanceFee attributes {}",e);
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_MAINTENANCE_FEE_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}

	/**
	 * Getting the Product Maintenance fee from  Product Using ProductId 
	 * 
	 * @return the ResponseEntity with the result.
	 */
	@RequestMapping(value = "/{profileId}/maintenanceFee", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getMaintenanceFeeAttributes(@PathVariable("profileId") Long profileId)
			 {
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		Map<String, Object> maintenanceFeeAttributes = null;

		if (profileId <= 0) {
			logger.info("ProductId is negative");
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.debug("Get Maintenance Fee attributes for product Id: {}",profileId);
			maintenanceFeeAttributes = customerProfileService.getCardFeeLimitsById(profileId,"Maintenance Fees");

			 if (CollectionUtils.isEmpty(maintenanceFeeAttributes)) {
				logger.info("Failed to get MaintenanceFee attributes, feeAttribute is NULL");
				responseDto = responseBuilder.buildSuccessResponse(maintenanceFeeAttributes,
						ResponseMessages.EMPTY_CARD_MAINTENANCE_FEE_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
			} else {
				logger.info("Fee attributes retrieved successfully");
				responseDto = responseBuilder.buildSuccessResponse(maintenanceFeeAttributes,
						ResponseMessages.SUCCESS_CARD_MAINTENANCE_FEE_RETRIEVE,ResponseMessages.SUCCESS);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	

/**
 * Update the Monthly Fee Attributes After fetching the existing Product fees using productId
 * 
 * @return the ResponseEntity with the result.
 */

@RequestMapping(value = "/{profileId}/monthlyFeeCap", method = RequestMethod.PUT)
public ResponseEntity<ResponseDTO> updateMonthlyFeeCapAttributes(@RequestBody Map<String, Object> inputFeeAttributes,
		@PathVariable("profileId") Long profileId) {
	logger.info(CCLPConstants.ENTER);
	ResponseDTO responseDto = null;
	
	if (profileId <= 0) {
		responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
		logger.error("ProductId should not be negative {}",profileId);
	} else {
		try {
			logger.debug("Updating monthly fee cap attributes for productId: {} Monthly fee Attributes -> {}" , profileId,inputFeeAttributes);
			int count = customerProfileService.updateFeeLimitAttributes(inputFeeAttributes, profileId,"Monthly Fee Cap");
			if (count > 0) {
				logger.info("Monthly Fee cap attributes updated successfully");
				responseDto = responseBuilder.buildSuccessResponse(count,ResponseMessages.SUCCESS_CARD_MONTHLYFEECAP_UPDATE,ResponseMessages.SUCCESS);
			} else {
				logger.info("Failed to update monthly fee cap attributes");	
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_MONTHLYFEECAP_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);

			}
		} catch (Exception e) {
			logger.error("Error while updating monthly fee cap attributes {}",e);
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARD_MONTHLYFEECAP_UPDATE,ResponseMessages.CONFIGSERVICE_EXCEPTION);
		}
	}
	logger.info(CCLPConstants.EXIT);
	return new ResponseEntity<>(responseDto, HttpStatus.OK);
	
}

/**
 * Getting the Product Monthly fee cap from actual Product and parent Products Using ProductId 
 * 
 * @return the ResponseEntity with the result.
 */
@RequestMapping(value = "/{profileId}/monthlyFeeCap", method = RequestMethod.GET)
public ResponseEntity<ResponseDTO> getMonthlyFeeCapAttributes(@PathVariable("profileId") Long profileId)
		 {
	
	logger.info(CCLPConstants.ENTER);
	ResponseDTO responseDto = null;
	Map<String, Object> monthlyFeeCapAttributes = null;

	if (profileId <= 0) {
		logger.info("ProductId cant be negative");
		responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
	} else {
		logger.debug("Get fee attributes for product Id: {}",profileId);
		monthlyFeeCapAttributes = customerProfileService.getCardFeeLimitsById(profileId,"Monthly Fee Cap");

		 if (CollectionUtils.isEmpty(monthlyFeeCapAttributes)) {
			logger.info("Failed to get fee attributes, feeAttribute is empty");
			responseDto = responseBuilder.buildSuccessResponse(monthlyFeeCapAttributes,
					ResponseMessages.EMPTY_CARD_MONTHLYFEECAP_RETRIEVE,ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Fee attributes retrieved successfully");
			responseDto = responseBuilder.buildSuccessResponse(monthlyFeeCapAttributes,
					ResponseMessages.SUCCESS_CARD_MONTHLYFEECAP_RETRIEVE,ResponseMessages.SUCCESS);
		}
	}
	logger.info(CCLPConstants.EXIT);
	return new ResponseEntity<>(responseDto, HttpStatus.OK);
}


/**
 * Getting the Product Monthly fee cap from actual Product and parent Products Using ProductId 
 * 
 * @return the ResponseEntity with the result.
 */
@RequestMapping(value = "/{attributeGrp}/{type}/{value}", method = RequestMethod.GET)
public ResponseEntity<ResponseDTO> getFeeLimitProfile(@PathVariable("attributeGrp") String attributeGrp,
		@PathVariable("type") String type,
		@PathVariable("value") String value)
		throws ServiceException {
	
	logger.info(CCLPConstants.ENTER);
	ResponseDTO responseDto = null;
	Map<String, Object> monthlyFeeCapAttributes = null;

	if(Util.isEmpty(type) || Util.isEmpty(value))
		responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ERR_PROFILE_ID_NEGATIVE,ResponseMessages.DOESNOT_EXISTS);
	else {
		logger.debug("Get {} attributes for type {} ", attributeGrp, type);
		monthlyFeeCapAttributes = customerProfileService.getCardFeeLimitsByType(type, value, attributeGrp);

		if (CollectionUtils.isEmpty(monthlyFeeCapAttributes)) {
			logger.info("Failed to get attributes, Attribute is empty");
			responseDto = responseBuilder.buildSuccessResponse(monthlyFeeCapAttributes,
					ResponseMessages.EMPTY_CARD_MONTHLYFEECAP_RETRIEVE, ResponseMessages.DOESNOT_EXISTS);
		} else {
			logger.info("Attributes retrieved successfully");
			responseDto = responseBuilder.buildSuccessResponse(monthlyFeeCapAttributes,
					ResponseMessages.SUCCESS_CARD_MONTHLYFEECAP_RETRIEVE, ResponseMessages.SUCCESS);
		}
	}
	logger.info(CCLPConstants.EXIT);
	return new ResponseEntity<>(responseDto, HttpStatus.OK);
}


@RequestMapping(value = "/{accountNumber}/cards",method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCardsByAccount(@PathVariable("accountNumber") String accountNumber) {
	logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		List<Object[]> cards = new ArrayList<>();

		if (!Util.isEmpty(accountNumber))
			cards = customerProfileService.getCardsByAccountNumber(accountNumber);
		if (!CollectionUtils.isEmpty(cards))
			responseDto = responseBuilder.buildSuccessResponse(cards, ResponseMessages.SUCCESS_CARDS_RETRIEVE,
					ResponseMessages.SUCCESS);
		else
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILED_CARDS_RETRIEVE,
					ResponseMessages.FAILURE);
		logger.info(CCLPConstants.EXIT);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

}
