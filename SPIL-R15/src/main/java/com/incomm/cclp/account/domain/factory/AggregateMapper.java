package com.incomm.cclp.account.domain.factory;

import static com.incomm.cclp.account.util.CodeUtil.mapYNToBoolean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.domain.model.AccountPurse;
import com.incomm.cclp.account.domain.model.AccountPurseAltKeyAttributes;
import com.incomm.cclp.account.domain.model.AccountPurseKey;
import com.incomm.cclp.account.domain.model.CardEntity;
import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.account.domain.model.ProductPurse;
import com.incomm.cclp.account.domain.model.PurseType;
import com.incomm.cclp.account.domain.validator.DomainValidationLevel;
import com.incomm.cclp.account.domain.validator.DomainValidator;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.CardDetail;
import com.incomm.cclp.dto.PurseDTO;

class AggregateMapper {

	private AggregateMapper() {
		// No op constructor
	}

	public static CardEntity map(CardDetail cardDetail) {

		return CardEntity.builder()
			.productId(cardDetail.getProductId())
			.partnerId(cardDetail.getPartnerId())
			.customerCode(cardDetail.getCustomerCode())
			.accountId(cardDetail.getAccountId())
			.issuerId(cardDetail.getIssuerId())
			.cardNumberEncrypted(cardDetail.getCardNumberEncrypted())
			.cardNumberHash(cardDetail.getCardNumberHash())
			.cardNumber(cardDetail.getCardNumber())
			.serialNumber(cardDetail.getSerialNumber())
			.proxyNumber(cardDetail.getProxyNumber())
			.lastFourDigit(cardDetail.getLastFourDigit())
			.activationDate(cardDetail.getDateOfActication())
			.expiryDate(cardDetail.getExpiryDate())
			.oldCardStatus(CardStatusType.byStatusCode(cardDetail.getOldCardStatus()))
			.cardStatus(CardStatusType.byStatusCode(cardDetail.getCardStatus())
				.get())
			.isRedeemed(cardDetail.getIsRedeemed())
			.digitalPin(cardDetail.getDigitalPin())
			.lastTransactionDate(cardDetail.getLastTransactionDate())
			.firstTimeTopUp(mapYNToBoolean(cardDetail.getFirstTimeTopUp()))
			.starterCardFlag(cardDetail.getStarterCardFlag())
			.profileCode(cardDetail.getProfileCode())
			.replFlag(cardDetail.getReplFlag())
			.dbSysDate(cardDetail.getDbSysDate())
			.accountNumber(cardDetail.getAccountNumber())
			.build();
	}

	public static AccountPurse map(AccountPurseBalance accountPurseBalance, ProductPurse productPurse) {

		AccountPurseAltKeyAttributes accountPurseAltKeyAttributes = AccountPurseAltKeyAttributes
			.from(accountPurseBalance.getEffectiveDate(), accountPurseBalance.getExpiryDate(), accountPurseBalance.getSkuCode());

		return AccountPurse.builder()
			.accountPurseKey(AccountPurseKey.from(accountPurseBalance.getAccountId(), accountPurseBalance.getPurseId(),
					accountPurseBalance.getAccountPurseId()))
			.accountPurseAltKeyAttributes(accountPurseAltKeyAttributes)
			.ledgerBalance(accountPurseBalance.getLedgerBalance())
			.availableBalance(accountPurseBalance.getAvailableBalance())
			.firstLoadDate(accountPurseBalance.getFirstLoadDate())
			.purseTypeId(accountPurseBalance.getPurseTypeId())
			.purseName(productPurse.getPurseName())
			.minorUnits(productPurse.getCurrencyMinorUnits())
			.topUpStatus(accountPurseBalance.getTopupStatus())
			.build();
	}

	public static List<AccountPurse> map(List<AccountPurseBalance> accountPurseBalances, Map<Long, ProductPurse> productPurseByProductId) {
		return CollectionUtils.isEmpty(accountPurseBalances) ? new ArrayList<AccountPurse>() :

				accountPurseBalances.stream()
					.map(accountPurseBalance -> map(accountPurseBalance, productPurseByProductId.get(accountPurseBalance.getPurseId())))
					.collect(Collectors.toList());
	}

	public static ProductPurse map(long productId, PurseDTO purseDto) {
		Optional<PurseType> purseTypeOptional;

		if (purseDto.getPurseTypeId() != null) {
			purseTypeOptional = PurseType.byPurseTypeId(purseDto.getPurseTypeId()
				.intValue());
		} else {
			purseTypeOptional = PurseType.byPurseTypeString(purseDto.getPurseType());
		}

		DomainValidator.validateState(purseTypeOptional.isPresent(), DomainValidationLevel.WARNING,
				() -> ("Unable to determine the purse type from:" + purseDto.toString()));

		return ProductPurse.builder()
			.productId(productId)
			.purseId(purseDto.getPurseId())
			.purseName(purseDto.getPurseName())
			.purseType(purseTypeOptional.isPresent() ? purseTypeOptional.get() : null)
			.description(purseDto.getDescription())
			.currencyCode(purseDto.getCurrencyCode())
			.currencyMinorUnits(purseDto.getMinorUnits())
			.build();
	}

}
