package com.incomm.cclpvms.config.model;

public class PackageAttributesDTO {
	private String shipMethods;
	private String replacementshipMethods;
	private String expRepshipMethods;
	private String carrierId;
	private String logoId;
	private String embossLine3;
	private String embossLine4;
	private String envelopeId;
	private String envelopeSealed;
	private String insertId;
	private String activationStickerId;
	private String thermalPrintColorId;
	private String cardPrintVersionId;
	private String packingSlipId;
	private String fulfillmentMethod;
	private String bundleSize;

	public String getShipMethods() {
		return shipMethods;
	}

	public void setShipMethods(String shipMethods) {
		this.shipMethods = shipMethods;
	}

	public String getReplacementshipMethods() {
		return replacementshipMethods;
	}

	public void setReplacementshipMethods(String replacementshipMethods) {
		this.replacementshipMethods = replacementshipMethods;
	}

	public String getExpRepshipMethods() {
		return expRepshipMethods;
	}

	public void setExpRepshipMethods(String expRepshipMethods) {
		this.expRepshipMethods = expRepshipMethods;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	public String getLogoId() {
		return logoId;
	}

	public void setLogoId(String logoId) {
		this.logoId = logoId;
	}

	public String getEmbossLine3() {
		return embossLine3;
	}

	public void setEmbossLine3(String embossLine3) {
		this.embossLine3 = embossLine3;
	}

	public String getEmbossLine4() {
		return embossLine4;
	}

	public void setEmbossLine4(String embossLine4) {
		this.embossLine4 = embossLine4;
	}

	public String getEnvelopeId() {
		return envelopeId;
	}

	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}

	public String getEnvelopeSealed() {
		return envelopeSealed;
	}

	public void setEnvelopeSealed(String envelopeSealed) {
		this.envelopeSealed = envelopeSealed;
	}

	public String getInsertId() {
		return insertId;
	}

	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}

	public String getActivationStickerId() {
		return activationStickerId;
	}

	public void setActivationStickerId(String activationStickerId) {
		this.activationStickerId = activationStickerId;
	}

	public String getThermalPrintColorId() {
		return thermalPrintColorId;
	}

	public void setThermalPrintColorId(String thermalPrintColorId) {
		this.thermalPrintColorId = thermalPrintColorId;
	}

	public String getCardPrintVersionId() {
		return cardPrintVersionId;
	}

	public void setCardPrintVersionId(String cardPrintVersionId) {
		this.cardPrintVersionId = cardPrintVersionId;
	}

	public String getPackingSlipId() {
		return packingSlipId;
	}

	public void setPackingSlipId(String packingSlipId) {
		this.packingSlipId = packingSlipId;
	}

	public String getFulfillmentMethod() {
		return fulfillmentMethod;
	}

	public void setFulfillmentMethod(String fulfillmentMethod) {
		this.fulfillmentMethod = fulfillmentMethod;
	}

	public String getBundleSize() {
		return bundleSize;
	}

	public void setBundleSize(String bundleSize) {
		this.bundleSize = bundleSize;
	}

	@Override
	public String toString() {
		return "PackageAttributes [shipMethods=" + shipMethods + ", replacementshipMethods=" + replacementshipMethods
				+ ", expRepshipMethods=" + expRepshipMethods + ", carrierId=" + carrierId + ", logoId=" + logoId
				+ ", embossLine3=" + embossLine3 + ", embossLine4=" + embossLine4 + ", envelopeId=" + envelopeId
				+ ", envelopeSealed=" + envelopeSealed + ", insertId=" + insertId + ", activationStickerId="
				+ activationStickerId + ", thermalPrintColorId=" + thermalPrintColorId + ", cardPrintVersionId="
				+ cardPrintVersionId + ", packingSlipId=" + packingSlipId + ", fulfillmentMethod=" + fulfillmentMethod
				+ ", bundleSize=" + bundleSize + "]";
	}

}
