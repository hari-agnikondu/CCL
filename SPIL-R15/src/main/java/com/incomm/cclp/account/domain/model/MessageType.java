package com.incomm.cclp.account.domain.model;

import java.util.Optional;
import java.util.function.BiPredicate;

import com.incomm.cclp.account.util.CodeUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

	// (messageTypeCode, description)
	ORIGINAL("0200", "Original Transaction"),
	REVERSE("0400", "Reversal Transaction");

	private final String messageTypeCode;
	private final String description;

	private static final BiPredicate<MessageType, String> isEqualMessageTypeCode = (type, string) -> type.messageTypeCode.equals(string);

	public static Optional<MessageType> byMessageTypeCode(String messageTypeCode) {
		return CodeUtil.mapEnumType(MessageType.values(), isEqualMessageTypeCode, messageTypeCode);
	}

}
