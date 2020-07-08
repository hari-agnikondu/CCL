package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Date;

public class CCFConfDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long dataSeqNo;
	private String dataTitle;
	private String dataDescription;
	private String dataLength;
	private String dataAttrType;
	private String dataValue;
	private String dataFormat;
	private String dataFiller;
	private String dataFillerSide;
	private String recordType;
	private String versionName;
	private String valueKey;
	private String formatType;
	private String possibleValue;
	private long insUser;
	private long lastUpdUser;
	private Date insDate;
	private Date lastUpdDate;

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

	public long getDataSeqNo() {
		return dataSeqNo;
	}

	public void setDataSeqNo(long dataSeqNo) {
		this.dataSeqNo = dataSeqNo;
	}

	public String getDataTitle() {
		return dataTitle;
	}

	public void setDataTitle(String dataTitle) {
		this.dataTitle = dataTitle;
	}

	public String getDataDescription() {
		return dataDescription;
	}

	public void setDataDescription(String dataDescription) {
		this.dataDescription = dataDescription;
	}

	public String getDataLength() {
		return dataLength;
	}

	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}

	public String getDataAttrType() {
		return dataAttrType;
	}

	public void setDataAttrType(String dataAttrType) {
		this.dataAttrType = dataAttrType;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getDataFiller() {
		return dataFiller;
	}

	public void setDataFiller(String dataFiller) {
		this.dataFiller = dataFiller;
	}

	public String getDataFillerSide() {
		return dataFillerSide;
	}

	public void setDataFillerSide(String dataFillerSide) {
		this.dataFillerSide = dataFillerSide;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getValueKey() {
		return valueKey;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public String getFormatType() {
		return formatType;
	}

	public void setFormatType(String formatType) {
		this.formatType = formatType;
	}

	public String getPossibleValue() {
		return possibleValue;
	}

	public void setPossibleValue(String possibleValue) {
		this.possibleValue = possibleValue;
	}

	@Override
	public String toString() {
		return "CCFConfDetailDTO [dataSeqNo=" + dataSeqNo + ", dataTitle=" + dataTitle + ", dataDescription="
				+ dataDescription + ", dataLength=" + dataLength + ", dataAttrType=" + dataAttrType + ", dataValue="
				+ dataValue + ", dataFormat=" + dataFormat + ", dataFiller=" + dataFiller + ", dataFillerSide="
				+ dataFillerSide + ", recordType=" + recordType + ", versionName=" + versionName + ", valueKey="
				+ valueKey + ", formatType=" + formatType + ", possibleValue=" + possibleValue + ", insDate=" + insDate
				+ ", lastUpdDate=" + lastUpdDate + "]";
	}
	
}
