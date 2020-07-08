package com.incomm.cclp.account.util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public enum DateTimeFormatType {

	YYYY_MM_DD_WITH_HYPHEN("yyyy-MM-dd"),
	MM_DD_YYYY_WITH_SLASH("MM/dd/yyyy"),
	DD_MMM_YY_WITH_HYPHEN("dd-MMM-yy"),
	DD_MM_YY_WITH_HYPHEN("dd-MM-YY"),
	DD_MM_YYHH_MM_DD_WITH_HYPHEN_AND_COLON("dd-MM-yyhh:mm:ss"),
	YYYY_MM_DD_WITH_HYPHEN_AND_COLON("yyyy-MM-dd hh:mm:ss"),
	YYYYMMDD("yyyyMMdd"),
	YYYYMM("yyyyMM"),
	HHMMSS("HHmmss");

	private final String formatString;
	private final DateTimeFormatter dateTimeFormatter;

	private DateTimeFormatType(String formatString) {
		this.formatString = formatString;
		this.dateTimeFormatter = DateTimeFormatter.ofPattern(formatString);
	}

	public SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat(formatString);
	}

}
