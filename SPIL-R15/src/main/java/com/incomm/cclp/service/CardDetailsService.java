package com.incomm.cclp.service;

import java.time.LocalDateTime;
import java.util.Map;

import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.account.domain.model.SpNumberType;
import com.incomm.cclp.domain.CardDetail;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface CardDetailsService {

	public ValueDTO getCardDetails(String spNumber, Map<String, String> valueObj) throws ServiceException;

	public void doPopulateSupportedPurseDtls(ValueDTO valueDto) throws ServiceException;

	public ValueDTO getAccountPurseUsageDetails(String accountId, String purseId, ValueDTO valueDto);

	public CardDetail getCardDetails(SpNumberType spNumberType, String spNumber);

	public int updateCard(String cardNumberHash, CardStatusType newCardStatus, CardStatusType oldCardStatus, LocalDateTime activationDate,
			Boolean firstTimeTopup);

}
