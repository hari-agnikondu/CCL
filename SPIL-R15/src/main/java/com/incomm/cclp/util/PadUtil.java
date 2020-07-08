package com.incomm.cclp.util;

public final class PadUtil {

	private PadUtil() {

	}

	public static String padRight(final String str, final int length, final char c) {
		int needed;

		if (str == null) {
			needed = length;
		} else {
			needed = length - str.length();
		}
		if (needed <= 0) {
			return str;
		}
		final char[] padding = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuilder sb = new StringBuilder(length);
		if (str != null) {
			sb.append(str);
		}
		sb.append(padding);
		return sb.toString();
	}
}
