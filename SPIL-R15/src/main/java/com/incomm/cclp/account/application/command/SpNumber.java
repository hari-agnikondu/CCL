package com.incomm.cclp.account.application.command;

import java.util.Optional;

import com.incomm.cclp.account.domain.model.SpNumberType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpNumber {

	private final Optional<SpNumberType> spNumberType;
	private final String spNumber;

	public static SpNumber from(Optional<SpNumberType> spNumberType, String spNumber) {
		return new SpNumber(spNumberType, spNumber);
	}
}
