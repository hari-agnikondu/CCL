package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
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
	private String version_Name;
	private String valueKey;
	private String formatType;
	private String possible_Value;
	
	@Value("${INS_USER}")
	private long insUser;
	private Date insDate;
	
	@Value("${INS_USER}")
	private long lastUpdUser;
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

	@Override
	public String toString() {
		return "CCFDTO [dataSeqNo=" + dataSeqNo + ", dataTitle=" + dataTitle + ", dataDescription=" + dataDescription
				+ ", dataLength=" + dataLength + ", dataAttrType=" + dataAttrType + ", dataValue=" + dataValue
				+ ", dataFormat=" + dataFormat + ", dataFiller=" + dataFiller + ", dataFillerSide=" + dataFillerSide
				+ ", recordType=" + recordType + ", version_Name=" + version_Name + ", valueKey=" + valueKey
				+ ", formatType=" + formatType + ", possible_Value=" + possible_Value + ", insUser=" + insUser
				+ ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
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

	public String getVersion_Name() {
		return version_Name;
	}

	public void setVersion_Name(String version_Name) {
		this.version_Name = version_Name;
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

	public String getPossible_Value() {
		return possible_Value;
	}

	public void setPossible_Value(String possible_Value) {
		this.possible_Value = possible_Value;
	}

}
