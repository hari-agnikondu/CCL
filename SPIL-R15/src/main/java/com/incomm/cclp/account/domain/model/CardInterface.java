package com.incomm.cclp.account.domain.model;

public interface CardInterface {

	public CardUpdate updateCardStatus(CardStatusType cardStatusType);

	public CardUpdate updateCardStatusToActive(boolean firstTimeTopUp);

	public CardUpdate updateFirstTimeTopUp(boolean newFirstTimeTopUpFlag);

	public boolean isFirstTimeTopUpCompleted();

}
