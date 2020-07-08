package com.incomm.cclp.account.domain.model;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum DeliveryChannelType {

	// enum value indicates channel name.
	// code, short-name, desc
	SPIL("01", "spil", "Secured Service Provider Interface Layer", null, true),
	CHW("02", "chw", "Card Holder Website", "IP_ADDRESS", false),
	CCA("03", "cca", "Customer Care Application", null, false),
	IVR("04", "ivr", "Interactive Voice Response", "MOBILE_NO", false),
	MOBILE("05", "mob", "Mobile", "DEVICE_ID", false),
	HOST("06", "host", "Host", null, true),
	WEB("17", "web", "Web", null, false),
	IH("08", "ih", "IH", null, true);

	private final String channelCode;
	private final String channelShortName;
	private final String channelDescription;
	private final String instrumentType;
	private final Boolean passiveSupported;

	private DeliveryChannelType(String channelCode, String channelShortName, String channelDescription, String instrumentType,
			Boolean passiveSupported) {
		this.channelCode = channelCode;
		this.channelShortName = channelShortName;
		this.channelDescription = channelDescription;
		this.instrumentType = instrumentType;
		this.passiveSupported = passiveSupported;
	}

	public static Optional<DeliveryChannelType> byName(String channelName) {
		for (DeliveryChannelType type : DeliveryChannelType.values()) {
			if (type.name()
				.equalsIgnoreCase(channelName)) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

	public static Optional<DeliveryChannelType> byChannelCode(String channelCode) {
		return Arrays.stream(DeliveryChannelType.values())
			.filter(type -> type.getChannelCode()
				.equals(channelCode))
			.findFirst();
	}

	public static Optional<DeliveryChannelType> byChannelShortName(String channelShortName) {
		return Arrays.stream(DeliveryChannelType.values())
			.filter(type -> type.getChannelShortName()
				.equals(channelShortName))
			.findFirst();
	}

}
