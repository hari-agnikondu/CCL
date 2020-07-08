package com.incomm.cclp.account.domain.factory;

import java.util.List;
import java.util.Optional;

import com.incomm.cclp.account.domain.model.DeliveryChannelType;
import com.incomm.cclp.account.domain.model.SpNumberType;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class AccountAggregateParams {

	private final DeliveryChannelType deliveryChannelType;

	private final String transactionShortCode;

	@Default
	private final Optional<SpNumberType> spNumberType = Optional.empty();
	private final String spNumber;

	private final String purseName;
	private final Long accountPurseId;

	@Singular
	private final List<AccountStateType> states;

}
