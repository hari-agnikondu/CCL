package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;

public class FulfillmentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long fulFillmentSEQID;
	private String fulfillmentID;
	private String fulFillmentName;
	private String isAutomaticShipped;
	private int shippedTimeDealy;
	private String ccfFileFormat;
	private String replaceCcfFileFormat;
	private String b2bVendorConfReq;
	private String b2bPrinterRespIdentifier;
	private String b2bConfFileIdentifier;
	private String b2bCnFileIdentifier;
	private long insUser;
	private Date insDate;
	private long lastUpdUser;
	private Date lastUpdDate;
	private String mftDestinationKey;
	
	public FulfillmentDTO() {

	}

	public FulfillmentDTO(long fulFillmentSEQID, String fulfillmentID, String fulFillmentName,
			String isAutomaticShipped, int shippedTimeDealy, String ccfFileFormat, String replaceCcfFileFormat,
			String b2bVendorConfReq, String b2bPrinterRespIdentifier, String b2bConfFileIdentifier,String b2bCnFileIdentifier,
			long insUser, Date insDate, long lastUpdUser, Date lastUpdDate, String mftDestinationKey) {
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
		this.insUser = insUser;
		this.insDate = insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
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

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getMftDestinationKey() {
		return mftDestinationKey;
	}

	public void setMftDestinationKey(String mftDestinationKey) {
		this.mftDestinationKey = mftDestinationKey;
	}

	@Override
	public String toString() {
		return "FulfillmentDTO [fulFillmentSEQID=" + fulFillmentSEQID + ", fulfillmentID=" + fulfillmentID
				+ ", fulFillmentName=" + fulFillmentName + ", isAutomaticShipped=" + isAutomaticShipped
				+ ", shippedTimeDealy=" + shippedTimeDealy + ", ccfFileFormat=" + ccfFileFormat
				+ ", replaceCcfFileFormat=" + replaceCcfFileFormat + ", b2bVendorConfReq=" + b2bVendorConfReq
				+ ", b2bPrinterRespIdentifier=" + b2bPrinterRespIdentifier + ", b2bConfFileIdentifier="
				+ b2bConfFileIdentifier + ", b2bCnFileIdentifier=" + b2bCnFileIdentifier + ", insUser=" + insUser
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ ", mftDestinationKey=" + mftDestinationKey + "]";
	}
	
}
