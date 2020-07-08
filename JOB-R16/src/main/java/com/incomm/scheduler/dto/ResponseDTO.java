/**
 * 
 */
package com.incomm.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResponseDTO class represents the data returned by all REST operations.
 * 
 * @author abutani
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseDTO {

	private Object data;

	private String result;

	private String message;

	private String responseCode;

	private String code;

}
