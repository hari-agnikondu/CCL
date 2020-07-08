package com.incomm.cclpvms.config.model;

import com.incomm.cclpvms.config.validator.FieldValidation;

public class FulFillment {

	private long fulFillmentSEQID;

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 _]+$", min = 2, max = 50, messageNotEmpty = "{messageNotEmpty.fulFillment.fullFillmentID}", messageLength = "{messageLength.fulFillment.fullFillmentID}", startsWithSpace = "{startsWithSpace.fulFillment.fullFillmentID}", messagePattern = "{messagepattern.fulFillment.fullFillmentID}", groups = ValidationStepOne.class)
	private String fulfillmentID;

	@FieldValidation(notEmpty = true, pattern = "^[A-Za-z0-9 _]+$", min = 2, max = 200, messageNotEmpty = "{messageNotEmpty.fulFillment.fulfillmentName}", messageLength = "{messageLength.fulFillment.fulfillmentName}", startsWithSpace = "{startsWithSpace.fulFillment.fulfillmentName}", messagePattern = "{messagepattern.fulFillment.fulfillmentName}", groups = ValidationStepTwo.class)
	private String fulFillmentName;

	private String isAutomaticShipped;
	
	@FieldValidation(notEmpty = false, pattern = "^[1-9]([0-9]+)?$", min = 1, max = 3, messageLength = "{messageLength.fulFillment.shippedTimeDealy}", messagePattern = "{messagepattern.fulFillment.shippedTimeDealy}", groups = ValidationStepTwo.class)
	private int shippedTimeDealy;

	@FieldValidation(notEmpty = true, pattern = "",  min = 1, max = 100, messageLength = "{messageLength.fulFillment.CCFFileName}", messageNotEmpty = "{messageNotEmpty.fulFillment.CCFFileName}", groups = ValidationStepTwo.class)
	private String ccfFileFormat;
	
	@FieldValidation(notEmpty = true,  pattern = "", min = 1, max = 100, messageLength = "{messageLength.fulFillment.CCFReplFilemessage}", messageNotEmpty = "{messageNotEmpty.fulFillment.CCFReplFilemessage}", groups = ValidationStepTwo.class)
	private String replaceCcfFileFormat;
	private String b2bVendorConfReq;
	
	@FieldValidation(notEmpty = false, pattern = "", min = 1, max = 20, messageLength = "{messageLength.fulFillment.b2bPrinterRespIdentifier}" , groups = ValidationStepTwo.class)
	private String b2bPrinterRespIdentifier;
	
	@FieldValidation(notEmpty = false, pattern = "", min = 1, max = 20, messageLength = "{messageLength.fulFillment.b2bConfFileIdentifier}" , groups = ValidationStepTwo.class)
	private String b2bConfFileIdentifier;
	
	
	private String b2bCnFileIdentifier;
	
	private String mftDestinationKey;
	
	public FulFillment() {

	}

	public FulFillment(long fulFillmentSEQID, String fulfillmentID, String fulFillmentName, String isAutomaticShipped,
			int shippedTimeDealy, String ccfFileFormat, String replaceCcfFileFormat, String b2bVendorConfReq,
			String b2bPrinterRespIdentifier, String b2bConfFileIdentifier, String b2bCnFileIdentifier, String mftDestinationKey) {
		super();
		this.fulFillmentSEQID = fulFillmentSEQID;
		this.fulfillmentID = fulfillmentID;
		this.fulFillmentName = fulFillmentName;
		this.isAutomaticShipped = isAutomaticShipped;
		this.shippedTimeDealy = shippedTimeDealy;
		this.ccfFileFormat = ccfFileFormat;
		this.replaceCcfFileFormat = replaceCcfFileFormat;
		this.b2bVendorConfReq = b2bVendorConfReq;
		this.b2bPrinterRespIdentifier = b2bPrinterRespIdentifier;
		this.b2bConfFileIdentifier = b2bConfFileIdentifier;
		this.b2bCnFileIdentifier = b2bCnFileIdentifier;
		this.mftDestinationKey = mftDestinationKey;
	}

	public long getFulFillmentSEQID() {
		return fulFillmentSEQID;
	}

	public void setFulFillmentSEQID(long fulFillmentSEQID) {
		this.fulFillmentSEQID = fulFillmentSEQID;
	}

	public String getFulfillmentID() {
		return fulfillmentID;
	}

	public void setFulfillmentID(String fulfillmentID) {
		this.fulfillmentID = fulfillmentID;
	}

	public String getFulFillmentName() {
		return fulFillmentName;
	}

	public void setFulFillmentName(String fulFillmentName) {
		this.fulFillmentName = fulFillmentName;
	}

	public String getIsAutomaticShipped() {
		return isAutomaticShipped;
	}

	public void setIsAutomaticShipped(String isAutomaticShipped) {
		this.isAutomaticShipped = isAutomaticShipped;
	}

	public int getShippedTimeDealy() {
		return shippedTimeDealy;
	}

	public void setShippedTimeDealy(int shippedTimeDealy) {
		this.shippedTimeDealy = shippedTimeDealy;
	}

	public String getCcfFileFormat() {
		return ccfFileFormat;
	}

	public void setCcfFileFormat(String ccfFileFormat) {
		this.ccfFileFormat = ccfFileFormat;
	}

	public String getReplaceCcfFileFormat() {
		return replaceCcfFileFormat;
	}

	public void setReplaceCcfFileFormat(String replaceCcfFileFormat) {
		this.replaceCcfFileFormat = replaceCcfFileFormat;
	}

	public String getB2bVendorConfReq() {
		return b2bVendorConfReq;
	}

	public void setB2bVendorConfReq(String b2bVendorConfReq) {
		this.b2bVendorConfReq = b2bVendorConfReq;
	}

	public String getB2bPrinterRespIdentifier() {
		return b2bPrinterRespIdentifier;
	}

	public void setB2bPrinterRespIdentifier(String b2bPrinterRespIdentifier) {
		this.b2bPrinterRespIdentifier = b2bPrinterRespIdentifier;
	}

	public String getB2bConfFileIdentifier() {
		return b2bConfFileIdentifier;
	}

	public void setB2bConfFileIdentifier(String b2bConfFileIdentifier) {
		this.b2bConfFileIdentifier = b2bConfFileIdentifier;
	}

	public String getB2bCnFileIdentifier() {
		return b2bCnFileIdentifier;
	}

	public void setB2bCnFileIdentifier(String b2bCnFileIdentifier) {
		this.b2bCnFileIdentifier = b2bCnFileIdentifier;
	}

	public String getMftDestinationKey() {
		return mftDestinationKey;
	}

	public void setMftDestinationKey(String mftDestinationKey) {
		this.mftDestinationKey = mftDestinationKey;
	}

	public interface ValidationStepOne {

		// validation group marker interface

	}

	public interface ValidationStepTwo {

	}

}
