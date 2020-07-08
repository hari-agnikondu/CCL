package com.incomm.cclpvms.config.model;


import com.incomm.cclpvms.config.validator.EmptyValidation;
import com.incomm.cclpvms.config.validator.FieldValidation;


/* @GroupSequence({ First.class, Second.class }) */
public class Issuer {
	@EmptyValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ,&.;'_-]+$", min = 0, max = 100,  messageLength = "{messageLength.issuer.issuerName}",
            messagePattern = "{messagepattern.issuer.issuerName}", 
           groups = SearchScreen.class)

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 ,&.;'_-]+$",min=2,max = 100,
			messageNotEmpty = "{messageNotEmpty.issuer.issuerName}", 
			messageLength = "{messageLength.issuer.issuerName}",
			startsWithSpace = "{startsWithSpace.issuerName.message}",
			messagePattern = "{messagepattern.issuer.issuerName}", groups = ValidationStepOne.class)
	private String issuerName;
	private String issuerCode;
	@FieldValidation(notEmpty = false, max = 255, messageNotEmpty = "messageNotEmpty.issuer.issuerdesc}", 
			messageLength = "{messageLength.issuer.issuerdesc}", groups = ValidationStepTwo.class)
	private String description;
	@FieldValidation(notEmpty = false, pattern = "^[0-9]+$",  max = 20, 
			messageNotEmpty = "{messageNotEmpty.issuer.issuerMdmId}", 
			messageLength = "{messageLength.issuer.issuerMdmId}", 
			messagePattern = "{messagepattern.issuer.issuerMdmId}", groups = ValidationStepTwo.class)
	private String mdmId;
	private String active;

	
	public Issuer(String issuerName, String issuerCode, String description, String mdmId, String active) {
		super();
		this.issuerName = issuerName;
		this.issuerCode = issuerCode;
		this.description = description;
		this.mdmId = mdmId;
		this.active = active;
	}

	public Issuer() {
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getIssuerCode() {
		return issuerCode;
	}

	public void setIssuerCode(String issuerCode) {
		this.issuerCode = issuerCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public interface ValidationStepOne {

	}

	public interface ValidationStepTwo {

	}
	public interface SearchScreen {

	}

}
