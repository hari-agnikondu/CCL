package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "FULFILLMENT_VENDOR")
@NamedQuery(name = "FulFillmentVendor.findAll", query = "SELECT F FROM FulFillmentVendor F")
public class FulFillmentVendor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FULFILLMENT_SEQ_GEN", sequenceName = "FULFILLMENT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FULFILLMENT_SEQ_GEN")
	@Column(name = "FULFILLMENT_VENDOR_SQ_ID")
	private long fulFillmentSEQID;

	@Column(name = "FULFILLMENT_VENDOR_ID")
	private String fulfillmentID;

	@Column(name = "FULFILLMENT_VENDOR_NAME")
	private String fulFillmentName;

	@Column(name = "IS_AUTOMATIC_SHIPPED")
	private String isAutomaticShipped;

	@Column(name = "SHIPPED_TIME_DELAY")
	private int shippedTimeDealy;

	@Column(name = "CCF_FILE_FORMAT")
	private String ccfFileFormat;

	@Column(name = "REPLACE_CCF_FILE_FORMAT")
	private String replaceCcfFileFormat;

	@Column(name = "B2B_VENDOR_CNFILE_REQ")
	private String b2bVendorConfReq;

	@Column(name = "B2B_PRINTER_RESP_IDENTIFIER")
	private String b2bPrinterRespIdentifier;

	@Column(name = "B2B_CNF_FILE_IDENTIFIER")
	private String b2bConfFileIdentifier;

	@Column(name = "B2B_CN_FILE_IDENTIFIER")
	private String b2bCnFileIdentifier;

	@Column(name = "INS_USER")
	private long insUser;

	@Column(name = "INS_DATE")
	private Date insDate;
	
	@Column(name = "LAST_UPD_USER")
	private long lastUpdUser;

	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;
	
	@Column(name = "MFT_DESTINATION_KEY")
	private String mftDestinationKey;	
	
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
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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
		return "FulFillmentVendor [fulfillmentID=" + fulfillmentID + ", fulFillmentName=" + fulFillmentName
				+ ", ccfFileFormat=" + ccfFileFormat + "]";
	}

}
