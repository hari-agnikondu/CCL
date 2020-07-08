package com.incomm.cclp.account.rest.resources;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorDetails {

	private LocalDateTime date;
	private String code;
	private String message;
	private String systemMessage;

	public static ErrorDetails from(String code, String message, String systemMessage) {
		return new ErrorDetails(LocalDateTime.now(), code, message, systemMessage);
	}

	public static ErrorDetails from(String code, String message) {
		return new ErrorDetails(LocalDateTime.now(), code, message, null);
	}

}