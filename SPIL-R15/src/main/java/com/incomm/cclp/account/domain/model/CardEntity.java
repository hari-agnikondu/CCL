package com.incomm.cclp.account.domain.model;

import static com.incomm.cclp.account.util.CodeUtil.isNotNull;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
@Getter(value = AccessLevel.PUBLIC)
public class CardEntity implements CardInterface {

	private final long productId;
	private final long partnerId;
	private final long issuerId;

	private final long customerCode;
	private final long accountId;
	private final String accountNumber;

	private final String cardNumberEncrypted;
	private final String cardNumberHash;
	private final String cardNumber;
	private final String serialNumber;
	private final String proxyNumber;
	private final String lastFourDigit;
	private LocalDateTime activationDate;

	private String expiryDate;

	private Optional<CardStatusType> oldCardStatus;
	private CardStatusType cardStatus;
	private String isRedeemed;

	private String digitalPin;

	private LocalDateTime lastTransactionDate;

	private Boolean firstTimeTopUp;
	private String starterCardFlag;
	private String profileCode;
	private Integer replFlag;

	private LocalDateTime dbSysDate;

	@Override
	public CardUpdate updateCardStatus(CardStatusType cardStatusType) {
		log.info("updating the card status from:{} to:{} for card: {}", this.cardStatus, cardStatusType, this.cardNumberHash);
		return updateCardStatus(cardStatusType, activationDate, firstTimeTopUp);
	}

	@Override
	public CardUpdate updateCardStatusToActive(boolean firstTimeTopUp) {
		log.info("updating the card status from:{} to:{} for card: {}, firstTimeTopup:{}", this.cardStatus, CardStatusType.ACTIVE,
				this.cardNumberHash, firstTimeTopUp);
		return updateCardStatus(CardStatusType.ACTIVE, LocalDateTime.now(), firstTimeTopUp);
	}

	private CardUpdate updateCardStatus(CardStatusType cardStatusType, LocalDateTime activationDate, boolean firstTimeTopUp) {
		return apply(getBuilder().newCardStatus(cardStatusType)
			.oldFirstTimeTopUpFlag(firstTimeTopUp)
			.activationDate(activationDate)
			.build());
	}

	@Override
	public CardUpdate updateFirstTimeTopUp(boolean newFirstTimeTopUpFlag) {

		return apply(getBuilder().newFirstTimeTopUpFlag(newFirstTimeTopUpFlag)
			.build());
	}

	private CardUpdate.CardUpdateBuilder getBuilder() {
		return CardUpdate.builder()
			.cardNumberHash(cardNumberHash)
			.oldCardStatus(cardStatus)
			.newCardStatus(cardStatus)
			.oldFirstTimeTopUpFlag(firstTimeTopUp)
			.newFirstTimeTopUpFlag(firstTimeTopUp)
			.activationDate(activationDate)
			.lastTransactionDate(LocalDateTime.now());
	}

	private CardUpdate apply(CardUpdate cardUpdate) {
		this.firstTimeTopUp = cardUpdate.getNewFirstTimeTopUpFlag();
		this.cardStatus = cardUpdate.getNewCardStatus();
		this.activationDate = cardUpdate.getActivationDate();
		this.lastTransactionDate = cardUpdate.getLastTransactionDate();
		return cardUpdate;
	}

	@Override
	public boolean isFirstTimeTopUpCompleted() {
		return isNotNull(this.firstTimeTopUp) && this.firstTimeTopUp;
	}

}