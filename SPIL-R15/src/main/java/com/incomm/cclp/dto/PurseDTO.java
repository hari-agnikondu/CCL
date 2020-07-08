package com.incomm.cclp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurseDTO {

	private String purseType;
	private String purseName;
	private String description;
	private Long purseId;
	private String currencyCode;
	private Long purseTypeId;
	private Integer minorUnits;

}
