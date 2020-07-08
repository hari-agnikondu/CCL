package com.incomm.cclp.account.domain.model;

import lombok.Builder;
import lombok.Value;

/*
 * This class should be part of reference data package and not part of domain.
 */

@Value
@Builder
public class ProductPurse {

	private long productId;
	private long purseId;
	private String purseName;

	private PurseType purseType;

	private String description;

	private String currencyId;

	private String currencyCode;
	private Integer currencyMinorUnits;

	private boolean isDefault;

}
