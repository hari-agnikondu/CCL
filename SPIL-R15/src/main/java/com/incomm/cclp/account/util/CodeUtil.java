package com.incomm.cclp.account.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

public class CodeUtil {

	private CodeUtil() {
		// no op
	}

	public static boolean not(boolean value) {
		return !value;
	}

	public static boolean isTrue(Boolean value) {
		return isNotNull(value) && value;
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.trim()
			.equals("");
	}

	public static boolean isNotNullAndEmpty(String value) {
		return value != null && !value.trim()
			.equals("");
	}

	public static boolean isNullOrEmpty(Object value) {
		if (value == null) {
			return true;
		} else if (value.toString()
			.trim()
			.equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isNullOrEmpty(Map<String, Object> value) {
		if (value == null) {
			return true;
		} else if (value.isEmpty()) {
			return true;
		}
		return false;
	}

	public static <T> boolean isNotNull(T value) {
		return value != null;
	}

	public static <T> boolean isNull(T value) {
		return value == null;
	}

	public static <T> List<T> wrapIfNull(List<T> collection) {
		return isEmpty(collection) ? Collections.emptyList() : collection;
	}

	public static <T> Collection<T> wrapIfNull(Collection<T> collection) {
		return isEmpty(collection) ? Collections.emptyList() : collection;
	}

	public static <T> Set<T> wrapIfNull(Set<T> collection) {
		return isEmpty(collection) ? Collections.emptySet() : collection;
	}

	public static <K, V> Map<K, V> wrapIfNull(Map<K, V> map) {
		return map == null ? Collections.emptyMap() : map;
	}

	public static String trimToLength(String value, int length) {
		if (isNullOrEmpty(value)) {
			return value;
		}
		return value.trim()
			.length() > length ? value.trim()
				.substring(0, length) : value;
	}

	public static <T> boolean isEqual(T value1, T value2) {
		if (isNull(value1) && isNull(value2)) {
			return true;
		}
		if (isNull(value1) && isNotNull(value2) || isNotNull(value1) && isNull(value2)) {
			return false;
		}
		return value1.equals(value2);
	}

	public static <T> boolean isNotEqual(T value1, T value2) {
		return not(isEqual(value1, value2));
	}

	public static <T extends Enum<T>> boolean isEqual(T value1, T value2) {
		if (isNull(value1) && isNull(value2)) {
			return true;
		}
		if (isNull(value1) && isNotNull(value2) || isNotNull(value1) && isNull(value2)) {
			return false;
		}
		return value1 == value2;
	}

	public static <T extends Enum<T>> boolean isNotEqual(T value1, T value2) {
		return not(isEqual(value1, value2));
	}

	public static <E extends Enum<E>, T> Optional<E> mapEnumType(E[] types, BiPredicate<E, T> predicate, T messageTypeCode) {
		for (E type : types) {
			if (predicate.test(type, messageTypeCode)) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static boolean hasOneElementOnly(Collection<?> collection) {
		return collection != null && collection.size() == 1;
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return not(isEmpty(collection));
	}

	public static boolean isEqualIgnoreCase(String value1, String value2) {
		if (isNull(value1) && isNull(value2)) {
			return true;
		}
		if (isNull(value1) && isNotNull(value2) || isNotNull(value1) && isNull(value2)) {
			return false;
		}
		return value1.equalsIgnoreCase(value2);
	}

	public static boolean isNotEqualIgnoreCase(String value1, String value2) {
		return not(isEqualIgnoreCase(value1, value2));
	}

	public static BigDecimal mapToBigDecimal(Long value) {
		return value == null ? null : new BigDecimal(value.toString());
	}

	public static BigDecimal mapToBigDecimal(String value) {
		return value == null ? null : new BigDecimal(value);
	}

	public static BigInteger mapToBigInteger(Long value) {
		return value == null ? null : BigInteger.valueOf(value);
	}

	public static BigDecimal setScale(BigDecimal input, int scale) {
		return input.setScale(scale, RoundingMode.HALF_EVEN);
	}

	public static Optional<BigDecimal> mapToOptionalBigDecimal(String value) {
		return value == null ? Optional.empty() : Optional.of(new BigDecimal(value));
	}

	public static Optional<Long> mapToOptionalLong(String value) {
		return value == null ? Optional.empty() : Optional.of(Long.valueOf(value));
	}

	public static String convertListToString(List<?> list) {
		StringBuilder result = new StringBuilder();

		for (Object value : list) {
			if (isNotNull(value))
				result.append(value.toString() + ",");
		}
		if (result.length() > 1)
			return result.substring(0, result.length() - 1);

		return null;
	}

	public static int mapToInteger(String value) {
		return "null".equalsIgnoreCase(value) || value == null ? 0 : Integer.parseInt(value);
	}

	public static boolean mapYNToBoolean(String value) {
		return "Y".equals(value);
	}

	public static boolean mapZeroOneToBoolean(String value) {
		return "1".equals(value);
	}

	public static boolean isPositive(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) > 0;
	}

	public static boolean isZeroOrPositive(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
	}

	public static boolean isZero(BigDecimal value) {
		return value != null && value.compareTo(BigDecimal.ZERO) == 0;
	}

	public static String convertToEmptyStringIfNull(String value) {
		return value == null ? "" : value;
	}

}
