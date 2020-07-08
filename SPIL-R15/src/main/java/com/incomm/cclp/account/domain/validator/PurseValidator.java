package com.incomm.cclp.account.domain.validator;

import static com.incomm.cclp.account.util.CodeUtil.not;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.AccountPurse;
import com.incomm.cclp.account.domain.model.ProductPurse;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;
import com.incomm.cclp.cache.AppCacheService;
import com.incomm.cclp.constants.ValueObjectKeys;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PurseValidator {

	public static final double MAX_BALANCE = 100000;

	public void validatePurseForAction(String purseName, PurseUpdateActionType actionType, Optional<AccountPurse> accountPurseOptional) {

		if ((actionType == PurseUpdateActionType.UNLOAD || actionType == PurseUpdateActionType.TOP_UP
				|| actionType == PurseUpdateActionType.AUTORELOAD || actionType == PurseUpdateActionType.AUTO_ROLLOVER)
				&& not(accountPurseOptional.isPresent())) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
					"Unable to locate the matching purse with purseName:" + purseName);
		} else if ((actionType == PurseUpdateActionType.AUTO_TOP_UP) && accountPurseOptional.isPresent()) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
					"Matching purse with purseName:" + purseName + " already exist");
		}
	}

	public void validatePurseTypeForAction(AppCacheService cacheService, ProductPurse productPurse) {
		if (isDefaultPurseId(cacheService, productPurse.getProductId(), productPurse.getPurseId())) {
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED,
					"Purse load not allowed on consumer funded purses:" + productPurse.getPurseName());
		}
	}

	public void validateMinAndMaxPurseBalance(AppCacheService cacheService, Long productId, Long purseId, PurseUpdateActionType actionType,
			double currentPurseBalace, double transactionAmount) {

		double newBalance;

		if (transactionAmount <= currentPurseBalace && actionType == PurseUpdateActionType.TOP_UP) {
			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED, "accountPurse balance:" + currentPurseBalace
					+ " is more than or equal to top-up transaction amount: " + transactionAmount);

		}

		if (actionType == PurseUpdateActionType.LOAD || actionType == PurseUpdateActionType.AUTORELOAD
				|| actionType == PurseUpdateActionType.AUTO_ROLLOVER) {
			newBalance = currentPurseBalace + transactionAmount;
		} else if (actionType == PurseUpdateActionType.TOP_UP) {
			newBalance = transactionAmount;
		} else {
			newBalance = currentPurseBalace - transactionAmount;
		}

		if (newBalance < 0.0) {
			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
					"Unable to update purse balance to negative value.");
		}

		double maxPurseBalance = getMaxPurseBalance(cacheService, productId, purseId);

		if (newBalance > maxPurseBalance && actionType != PurseUpdateActionType.AUTORELOAD) {

			throw DomainExceptionFactory.from(DomainExceptionType.DOMAIN_VALIDATION_FAILED,
					"Unable to update purse balance to more than max purse balance.");
		}

	}

	double getMaxPurseBalance(AppCacheService cacheService, Long productId, Long purseId) {

		if (isDefaultPurseId(cacheService, productId, purseId)) {
			Object maxCardBalanceObj = cacheService.getProductAttribute(ValueObjectKeys.GENERAL, ValueObjectKeys.MAXCARDBAL);
			if (maxCardBalanceObj == null) {

				throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, "maxCardBalance not set for productId" + productId);

			} else {
				return Double.parseDouble(maxCardBalanceObj.toString());
			}
		} else {

			Map<String, Object> purseAttributes = cacheService.getProductAttributesForGroup(ValueObjectKeys.PURSE);
			Object maxPurseBalanceObj = purseAttributes.get(ValueObjectKeys.MAX_PURSE_BALANCE);

			if (maxPurseBalanceObj == null) {
				log.info("maxPurseBalance not set for productId:{}. purseId:{}", productId, purseId);
				return MAX_BALANCE;
			} else {
				return Double.parseDouble(maxPurseBalanceObj.toString());

			}

		}

	}

	boolean isDefaultPurseId(AppCacheService cacheService, Long productId, Long purseId) {
		Object defaultPurseIdObj = cacheService.getProductAttribute(ValueObjectKeys.PRODUCT, ValueObjectKeys.DEFAULT_PURSE);

		if (defaultPurseIdObj == null) {
			log.warn("Default purseId not set for productId:{}", productId);
			return false;
		}

		long defaultPurseId = Long.parseLong(defaultPurseIdObj.toString());
		return purseId.equals(defaultPurseId);
	}

}
