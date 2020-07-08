/**
 * 
 */
package com.incomm.cclp.util;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.controller.ProductController;
import com.incomm.cclp.dto.CardRangeDTO;
import com.incomm.cclp.dto.ClpUserDTO;
import com.incomm.cclp.dto.FulfillmentDTO;
import com.incomm.cclp.dto.GlobalParametersDTO;
import com.incomm.cclp.dto.GroupAccessDTO;
import com.incomm.cclp.dto.GroupDTO;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.LocationDTO;
import com.incomm.cclp.dto.MerchantDTO;
import com.incomm.cclp.dto.MerchantProductDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.dto.ProgramIDDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.PurseTypeEnum;
import com.incomm.cclp.dto.RuleDTO;
import com.incomm.cclp.exception.ServiceException;

/**
 * ValidationService class provides all the validations for Input DTO's.
 * 
 * @author abutani
 *
 */
public class ValidationService {
	private static final Logger logger = LogManager.getLogger(ValidationService.class);

	/**
	 * Class should not be instantiated.
	 */
	private ValidationService() {

	}

	/**
	 * Validates Issuer. It validates the presence or absence of following fields
	 * Issuer Name Issuer Create User Issuer Update User
	 * 
	 * @param issuerDto The IssuerDTO to be validated.
	 * @param insert    flag to indicate whether its an insert operation or not.
	 * 
	 *                  throws ServiceException
	 * 
	 * @return the ResponseDTO for the validation response.
	 */
	public static void validateIssuer(IssuerDTO issuerDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (issuerDto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_ISSUER_NULL);

		// If its not an insert, issuer id should be present.
		if (!insert && issuerDto.getIssuerId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_ID);
		}

		if (Util.isEmpty(issuerDto.getIssuerName())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_NAME);
		}

		if (issuerDto.getInsUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER);
		}

		if (issuerDto.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_UPD_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	/* /** validation for delete */

	public static void validateForDeleteIssuer(IssuerDTO issuerDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (issuerDto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_ISSUER_NULL);

		// If its not an insert, issuer id should be present.
		if (!insert && issuerDto.getIssuerId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_ID);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * It validates the presence or absence of following fields Partner Name Partner
	 * Create User Partner Update User
	 * 
	 * @param partnerDto The PartnerDTO to be validated.
	 * @param insert     flag to indicate whether its an insert operation or not.
	 * 
	 * @throws ServiceException on Validation error
	 * @see ServiceException
	 * @return the ResponseDTO for the validation response.
	 */

	public static void validatePartner(PartnerDTO partnerDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (partnerDto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PARTNER_NULL);

		// If its not an insert, issuer id should be present.
		if (!insert && partnerDto.getPartnerId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_ID);
		}

		if (Util.isEmpty(partnerDto.getPartnerName())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_NAME);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateCardRange(CardRangeDTO cardRangeDTO, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (cardRangeDTO == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_CARDRANGE_NULL);

		// If its not an insert, card range id should be present.
		if (!insert && cardRangeDTO.getCardRangeId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_ID);
		}
		// If its not an insert, issuer id should be present.
		if (cardRangeDTO.getIssuerId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_ID);
		}

		if (Util.isEmpty(cardRangeDTO.getPrefix())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PREFIX_NULL);
		}

		if (Util.isEmpty(cardRangeDTO.getStartCardNbr())) {
			validationErrBuilder
					.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_START_CARDRANGE);
		}
		if (Util.isEmpty(cardRangeDTO.getEndCardNbr())) {
			validationErrBuilder
					.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_END_CARDRANGE);
		}
		if (Util.isEmpty(cardRangeDTO.getIsCheckDigitRequired())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_CHECKDIGIT);
		}
		if (Util.isEmpty(cardRangeDTO.getCardInventory())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CARDRANGE_INVENTORY);
		}

		if (insert && cardRangeDTO.getInsUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_INS_USER);
		}

		if (!insert && cardRangeDTO.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_UPD_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateProduct(ProductDTO productDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (productDto == null) {
			logger.error("ProductDto is null");
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PRODUCT_NULL);
		}

		// If its not an insert, product id should be present.
		if (!insert && productDto.getProductId() <= 0) {
			logger.debug("ProductId not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PRODUCT_ID);
		}

		if (productDto.getIssuerId() <= 0) {
			logger.debug("IssuerId not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ISSUER_ID);
		}

		if (productDto.getPartnerId() <= 0) {
			logger.debug("PartnerId not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PARTNER_ID);
		}

		if (productDto.getProgramId() <= 0) {
			logger.debug("ProgramId not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PROGRAM_ID);
		}

		if (Util.isEmpty(productDto.getProductName())) {
			logger.debug("ProductName not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PRODUCT_NAME);
		}

		if (Util.isEmpty(productDto.getProductShortName())) {
			logger.debug("ProductShortName not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PRODUCT_SHORT_NAME);
		}

		if (productDto.getInsUser() <= 0) {
			logger.debug("InsUser not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PRODUCT_INS_USER);
		}

		if (productDto.getInsDate() == null) {
			logger.debug("InsDate not present");
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PRODUCT_UPD_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			logger.error("Error while validating product");
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validatePurse(PurseDTO purseDTO, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (purseDTO == null) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PURSE_NULL);

		}

		if (purseDTO.getPurseTypeId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSETYPEID_NULL);
		}

		if (purseDTO.getPurseTypeId() == PurseTypeEnum.CONSUMER_FUNDED_CURRENCY.getPurseTypeId() && Util.isEmpty(purseDTO.getCurrencyTypeID())
				&& !"-1".equals(purseDTO.getCurrencyTypeID())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CURRENCYCODE_NULL);
		}

		if (purseDTO.getPurseTypeId() == PurseTypeEnum.POINTS.getPurseTypeId() && (purseDTO.getUpc() != null && !(Util.isEmpty(purseDTO.getUpc())))) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOYALTY_NULL);
		}

		/*
		 * if (purseDTO.getPurseTypeId() == PurseTypeEnum.SKU.getPurseTypeId() &&
		 * (purseDTO.getUpc() == null || "".equals(purseDTO.getUpc()))) {
		 * validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER +
		 * ResponseMessages.ERR_UPC_NULL); }
		 */
		if (purseDTO.getPurseTypeId() == PurseTypeEnum.PARTNER_FUNDED_CURRENCY.getPurseTypeId() && Util.isEmpty(purseDTO.getCurrencyTypeID())
				&& !"-1".equals(purseDTO.getCurrencyTypeID())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CURRENCYCODE_NULL);
		}
		if(purseDTO.getExtPurseId() == null || "".equals(purseDTO.getExtPurseId())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSE_NULL);
		}
		if (insert && purseDTO.getInsUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_INSUSER_NULL);
		}

		if (!insert && purseDTO.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_UPDUSER_NULL);
		}
		if (!insert && purseDTO.getPurseId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PURSEID_INVALID);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateGlobalParameters(GlobalParametersDTO globalParameterDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (Objects.isNull(globalParameterDto) || CollectionUtils.isEmpty(globalParameterDto.getGlobalParameters())) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_GLOBAL_PARAMETERS_NULL);
		}

		Map<String, Object> globalParameters = globalParameterDto.getGlobalParameters();

		if (!globalParameters.containsKey("customerPasswordLength")
				|| Objects.isNull(globalParameters.get("customerPasswordLength"))) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.LENGTH);
		}

		if (!globalParameters.containsKey("maskingCharValue")
				|| Objects.isNull(globalParameters.get("maskingCharValue"))) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MASKING_CHAR_VALUE);
		}

		if (!globalParameters.containsKey("hsm") || Objects.isNull(globalParameters.get("hsm"))) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_HSM);
		}

		if (!globalParameters.containsKey("dateFormat") || Objects.isNull(globalParameters.get("dateFormat"))) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_DATE_FORMAT);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateUser(ClpUserDTO clpUserDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (clpUserDto == null) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_CLP_USER_NULL);
		}

		if (!insert && clpUserDto.getUserId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_ID);
		}

		if (Util.isEmpty(clpUserDto.getUserName())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_USERNAME);
		}

		if (Util.isEmpty(clpUserDto.getUserLoginId())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_LOGINID);
		}

		if (Util.isEmpty(clpUserDto.getUserStatus())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_STATUS);
		}

		if (Util.isEmpty(clpUserDto.getUserEmail())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_EMAIL);
		}

		if (Objects.isNull(clpUserDto.getInsUser()) || clpUserDto.getInsUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_INS_USER);
		}

		if (Objects.isNull(clpUserDto.getLastUpdUser()) || clpUserDto.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_UPD_USER);
		}

		if (CollectionUtils.isEmpty(clpUserDto.getGroups())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CLP_USER_GROUPS);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateGroupAccess(GroupAccessDTO groupAccessDTO, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);
		if (Objects.isNull(groupAccessDTO)) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_GROUP_ACCESS_NULL);
		}

		if (groupAccessDTO.getPartnerList() != null && CollectionUtils.isEmpty(groupAccessDTO.getPartnerList())) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PARTNER_LIST_IS_NULL);
		}

		if (!insert && (groupAccessDTO.getGroupAccessId() == null || groupAccessDTO.getGroupAccessId() <= 0)) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_GROUP_ACCESS_ID_NULL);
		}

		if (insert && (groupAccessDTO.getInsUser() == null || groupAccessDTO.getInsUser() <= 0)) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PARTNER_INST_USER_IS_NULL);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateGroupAccessProduct(GroupAccessDTO groupAccessDTO, boolean insert)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (Objects.isNull(groupAccessDTO)) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_GROUP_ACCESS_NULL);
		}

		if (!insert && (groupAccessDTO.getGroupAccessId() == null || groupAccessDTO.getGroupAccessId() <= 0)) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_GROUP_ACCESS_ID_NULL);
		}

		if (groupAccessDTO.getProductId() == null || groupAccessDTO.getProductId() < 0) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PRODUCT_ID_NULL);
		}
		if (!insert && groupAccessDTO.getPartnerArray() != null && groupAccessDTO.getPartnerArray().length == 0) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PARTNER_LIST_IS_NULL);
		}

		if (insert && (groupAccessDTO.getInsUser() == null || groupAccessDTO.getInsUser() <= 0)) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PARTNER_INST_USER_IS_NULL);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateGroup(GroupDTO groupdto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);
		if (groupdto == null) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER +

					ResponseMessages.ERR_GROUP_NULL);
		}
		if (Util.isEmpty(groupdto.getGroupName()) && insert) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_GROUP_NAME);

		}
		if (CollectionUtils.isEmpty(groupdto.getSelectedRoleList())) {
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_GROUP_ROLE_NAME);

		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateMerchant(MerchantDTO merchantDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (merchantDto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_MERCHANT_NULL);

		if (!insert && merchantDto.getMerchantId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_ID);
		}

		if (Util.isEmpty(merchantDto.getMerchantName())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_NAME);
		}

		if (merchantDto.getInsUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_INS_USER);
		}

		if (merchantDto.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_UPD_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * Validates Fulfillment.
	 * 
	 * @param FulfillmentDTO The FulfillmentDTO to be validated.
	 * @param insert         flag to indicate whether its an insert operation or
	 *                       not.
	 * 
	 *                       throws ServiceException
	 * 
	 * @return the ResponseDTO for the validation response.
	 */
	public static void validateFulfillment(FulfillmentDTO fulfilementDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (fulfilementDto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_FULFILLMENT_NULL);

		if (Util.isEmpty(fulfilementDto.getFulfillmentID())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_FULFILLMENT_ID);
		}

		if (Util.isEmpty(fulfilementDto.getFulFillmentName())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_FULFILLMENT_NAME);
		}

		if (Util.isEmpty(fulfilementDto.getCcfFileFormat())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_FULFILLMENT_CCF);
		}

		if (Util.isEmpty(fulfilementDto.getReplaceCcfFileFormat())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_FULFILLMENT_REPL_CCF);
		}

		if (!insert && fulfilementDto.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_FULFILLMENT_UPD_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateMerchantProduct(MerchantProductDTO merchantProductDto, boolean delete)
			throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (merchantProductDto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_MERCHANT_PRODUCT_NULL);

		if (merchantProductDto.getProductId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PRODUCT_ID);
		}

		if (merchantProductDto.getMerchantId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_ID);
		}

		if (merchantProductDto.getInsUser() <= 0 && !delete) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_INS_USER);
		}

		if (merchantProductDto.getLastUpdUser() <= 0 && !delete) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_UPD_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * It validates the presence or absence of following fields Rule Name Rule
	 * Create User Rule Update User
	 * 
	 * @param ruleDto The RuleDTO to be validated.
	 * @param insert  flag to indicate whether its an insert operation or not.
	 * 
	 * @throws ServiceException on Validation error
	 * @see ServiceException
	 * @return the ResponseDTO for the validation response.
	 */

	public static void validateRule(RuleDTO ruleDto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (ruleDto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_RULE_NULL);

		if (!insert && ruleDto.getRuleId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_RULE_ID);
		}

		if (Util.isEmpty(ruleDto.getRuleName())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_RULE_NAME);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateLocation(LocationDTO locationdto, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (locationdto == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_LOCATION_NULL);

		if (!insert && locationdto.getLocationId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOCATION_ID);
		}

		if (locationdto.getMerchantId() == null || locationdto.getMerchantId() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_MERCHANT_ID);
		}

		if (Util.isEmpty(locationdto.getAddressOne())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ADDRESSONE_NULL);
		}

		if (Util.isEmpty(locationdto.getCity())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_CITY_NULL);
		}
		if (Util.isEmpty(locationdto.getZip())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_ZIP_NULL);
		}
		if (Objects.isNull(locationdto.getStateCodeID()) || locationdto.getStateCodeID() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_STATE_NULL);
		}

		if (Objects.isNull(locationdto.getCountryCodeID()) || locationdto.getCountryCodeID() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_COUNTRY_NULL);
		}

		if (!insert && locationdto.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOCATION_UPD_USER);
		}

		if (insert && (locationdto.getInsUser() == null || locationdto.getInsUser() <= 0)) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_LOCATION_INS_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();
		logger.info(CCLPConstants.EXIT);
	}

	public static void validateProgramId(ProgramIDDTO programIDDTO, boolean insert) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		StringBuilder validationErrBuilder = new StringBuilder(ResponseMessages.VALIDATION_HEADER_MSG);

		if (programIDDTO == null)
			throw new ServiceException(validationErrBuilder.toString() + ResponseMessages.MESSAGE_DELIMITER
					+ ResponseMessages.ERR_PROGRAM_ID_NULL);

		// If its not an insert, issuer id should be present.
		if (!insert && programIDDTO.getProgramID() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PROGRAM_ID);
		}

		if (Util.isEmpty(programIDDTO.getProgramIDName()) || Objects.isNull(programIDDTO.getProgramIDName())) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PROGRAM_NAME);
		}
		if (insert && Util.convertAsInteger(programIDDTO.getPartnerId()) <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PROGRAM_NAME);
		}

		if (insert && programIDDTO.getInsUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PROGRAM_INS_USER);
		}

		if (programIDDTO.getLastUpdUser() <= 0) {
			validationErrBuilder.append(ResponseMessages.MESSAGE_DELIMITER + ResponseMessages.ERR_PROGRAM_UPD_USER);
		}

		String validationErrMsg = validationErrBuilder.toString();

		if (!validationErrMsg.equals(ResponseMessages.VALIDATION_HEADER_MSG)) {
			throw new ServiceException(validationErrMsg);
		}
		logger.info(CCLPConstants.EXIT);
	}
}