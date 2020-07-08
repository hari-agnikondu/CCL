package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
@Table(name = "CCF_CONF_DETAIL")
@NamedQuery(name = "CCFConfDetail.findAll", query = "SELECT C FROM CCFConfDetail C")
public class CCFConfDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CCFCONFIG_SEQ_GEN", sequenceName = "CCFCONFIG_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CCFCONFIG_SEQ_GEN")
	@Column(name = "CCF_CONF_ID")
	private long confID;

	@Column(name = "DATA_SEQNO")
	private long dataSeqNo;

	@Column(name = "DATA_TITLE")
	private String dataTitle;

	@Column(name = "DATA_DESCRIPTION")
	private String dataDescription;

	@Column(name = "DATA_LENGTH")
	private String dataLength;

	@Column(name = "DATA_ATTR_TYPE")
	private String dataAttrType;

	@Column(name = "DATA_VALUE")
	private String dataValue;

	@Column(name = "DATA_FORMAT")
	private String dataFormat;

	@Column(name = "DATA_FILLER")
	private String dataFiller;

	@Column(name = "DATA_FILLER_SIDE")
	private String dataFillerSide;

	@Column(name = "RECORD_TYPE")
	private String recordType;

	@Column(name = "VERSION_NAME")
	private String versionName;

	@Column(name = "VALUE_KEY")
	private String valueKey;

	@Column(name = "FORMAT_TYPE")
	private String formatType;

	@Column(name = "POSSIBLE_VALUE")
	private String possibleValue;

	@Column(name = "INS_USER")
	private long insUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE")
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VERSION_NAME", referencedColumnName = "VERSION_NAME", insertable = false, updatable = false)
	@JsonIgnore
	private CCFConfVersion ccfConfVersion;

	public long getConfID() {
		return confID;
	}

	public void setConfID(long confID) {
		this.confID = confID;
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

	public void setversionName(String versionName) {
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

	public CCFConfVersion getCcfConfVersion() {
		return ccfConfVersion;
	}

	public void setCcfConfVersion(CCFConfVersion ccfConfVersion) {
		this.ccfConfVersion = ccfConfVersion;
	}

	@Override
	public String toString() {
		return "CCFConfDetail [confID=" + confID + ", dataTitle=" + dataTitle + ", dataDescription=" + dataDescription
				+ ", versionName=" + versionName + ", ccfConfVersion=" + ccfConfVersion + "]";
	}

}
