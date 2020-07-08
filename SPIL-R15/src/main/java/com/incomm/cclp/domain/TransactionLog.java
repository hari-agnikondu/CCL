/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Persistable;

import com.incomm.cclp.constants.QueryConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "TRANSACTION_LOG")
@XmlRootElement
@NamedNativeQueries({
		@NamedNativeQuery(name = "TransactionLog.getLastSuccessTxn", query = QueryConstants.GET_LAST_SUCCESS_ACT_TXN, resultSetMapping = "TransactionLogInfoResult"),
		@NamedNativeQuery(name = "TransactionLog.getLastSuccessfulActTxn", query = QueryConstants.GET_LAST_SUCCESSFUL_ACT_TXN_FOR_DEACT, resultSetMapping = "TransactionLogInfoResult"),
		@NamedNativeQuery(name = "TransactionLog.getLastSuccessPurseTxn", query = QueryConstants.GET_LAST_SUCCESS_ACT_TXN
				+ " and purse_id=:purseId", resultSetMapping = "TransactionLogInfoResult"),
		@NamedNativeQuery(name = "TransactionLog.getLastSuccessfulTransactions", query = QueryConstants.GET_LAST_SUCCESS_ACT_TXN, resultSetMapping = "TransactionLogInfoResult") })

@SqlResultSetMapping(name = "TransactionLogInfoResult", classes = { @ConstructorResult(targetClass = TransactionLogInfo.class, columns = {
		@ColumnResult(name = "authAmount", type = Double.class), @ColumnResult(name = "tranfeeAmount", type = Double.class),
		@ColumnResult(name = "tranReverseFlag", type = String.class), @ColumnResult(name = "processFlag", type = String.class),
		@ColumnResult(name = "transactionAmount", type = Double.class), @ColumnResult(name = "transactionDate", type = String.class),
		@ColumnResult(name = "transactionTime", type = String.class), @ColumnResult(name = "terminalId", type = String.class),
		@ColumnResult(name = "originalRrn", type = String.class), @ColumnResult(name = "accountPurseId", type = BigInteger.class),
		@ColumnResult(name = "openingLedgerBalance", type = Double.class),
		@ColumnResult(name = "openingAvailableBalance", type = Double.class),
		@ColumnResult(name = "closingLedgerBalance", type = Double.class),
		@ColumnResult(name = "closingAvailableBalance", type = Double.class)

		}) })

@NamedQueries({ @NamedQuery(name = "TransactionLog.findAll", query = "SELECT t FROM TransactionLog t"),
		@NamedQuery(name = "TransactionLog.findByTransactionSqid", query = "SELECT t FROM TransactionLog t WHERE t.transactionSqid = :transactionSqid"),
		@NamedQuery(name = "TransactionLog.findByIssuerId", query = "SELECT t FROM TransactionLog t WHERE t.issuerId = :issuerId"),
		@NamedQuery(name = "TransactionLog.findByPartnerId", query = "SELECT t FROM TransactionLog t WHERE t.partnerId = :partnerId"),
		@NamedQuery(name = "TransactionLog.findByProductId", query = "SELECT t FROM TransactionLog t WHERE t.productId = :productId"),
		@NamedQuery(name = "TransactionLog.findByDeliveryChannel", query = "SELECT t FROM TransactionLog t WHERE t.deliveryChannel = :deliveryChannel"),
		@NamedQuery(name = "TransactionLog.findByTransactionCode", query = "SELECT t FROM TransactionLog t WHERE t.transactionCode = :transactionCode"),
		@NamedQuery(name = "TransactionLog.findByMsgType", query = "SELECT t FROM TransactionLog t WHERE t.msgType = :msgType"),
		@NamedQuery(name = "TransactionLog.findByIsFinancial", query = "SELECT t FROM TransactionLog t WHERE t.isFinancial = :isFinancial"),
		@NamedQuery(name = "TransactionLog.findByCrDrFlag", query = "SELECT t FROM TransactionLog t WHERE t.crDrFlag = :crDrFlag"),
		@NamedQuery(name = "TransactionLog.findByTransactionDesc", query = "SELECT t FROM TransactionLog t WHERE t.transactionDesc = :transactionDesc"),
		@NamedQuery(name = "TransactionLog.findByResponseId", query = "SELECT t FROM TransactionLog t WHERE t.responseId = :responseId"),
		@NamedQuery(name = "TransactionLog.findByErrorMsg", query = "SELECT t FROM TransactionLog t WHERE t.errorMsg = :errorMsg"),
		@NamedQuery(name = "TransactionLog.findByTransactionStatus", query = "SELECT t FROM TransactionLog t WHERE t.transactionStatus = :transactionStatus"),
		@NamedQuery(name = "TransactionLog.findByTerminalId", query = "SELECT t FROM TransactionLog t WHERE t.terminalId = :terminalId"),
		@NamedQuery(name = "TransactionLog.findByRrn", query = "SELECT t FROM TransactionLog t WHERE t.rrn = :rrn"),
		@NamedQuery(name = "TransactionLog.findByCardNumber", query = "SELECT t FROM TransactionLog t WHERE t.cardNumber = :cardNumber"),
		@NamedQuery(name = "TransactionLog.findByTopupCardNumber", query = "SELECT t FROM TransactionLog t WHERE t.topupCardNumber = :topupCardNumber"),
		@NamedQuery(name = "TransactionLog.findByCustomerCardNbrEncr", query = "SELECT t FROM TransactionLog t WHERE t.customerCardNbrEncr = :customerCardNbrEncr"),
		@NamedQuery(name = "TransactionLog.findByTopupCardNbrEncr", query = "SELECT t FROM TransactionLog t WHERE t.topupCardNbrEncr = :topupCardNbrEncr"),
		@NamedQuery(name = "TransactionLog.findByProxyNumber", query = "SELECT t FROM TransactionLog t WHERE t.proxyNumber = :proxyNumber"),
		@NamedQuery(name = "TransactionLog.findByTransactionDate", query = "SELECT t FROM TransactionLog t WHERE t.transactionDate = :transactionDate"),
		@NamedQuery(name = "TransactionLog.findByTransactionTime", query = "SELECT t FROM TransactionLog t WHERE t.transactionTime = :transactionTime"),
		@NamedQuery(name = "TransactionLog.findByTransactionTimezone", query = "SELECT t FROM TransactionLog t WHERE t.transactionTimezone = :transactionTimezone"),
		@NamedQuery(name = "TransactionLog.findByPartialPreauthInd", query = "SELECT t FROM TransactionLog t WHERE t.partialPreauthInd = :partialPreauthInd"),
		@NamedQuery(name = "TransactionLog.findByBusinessDate", query = "SELECT t FROM TransactionLog t WHERE t.businessDate = :businessDate"),
		@NamedQuery(name = "TransactionLog.findByAuthId", query = "SELECT t FROM TransactionLog t WHERE t.authId = :authId"),
		@NamedQuery(name = "TransactionLog.findByRuleIndicator", query = "SELECT t FROM TransactionLog t WHERE t.ruleIndicator = :ruleIndicator"),
		@NamedQuery(name = "TransactionLog.findByRulesetId", query = "SELECT t FROM TransactionLog t WHERE t.rulesetId = :rulesetId"),
		@NamedQuery(name = "TransactionLog.findByMccode", query = "SELECT t FROM TransactionLog t WHERE t.mccode = :mccode"),
		@NamedQuery(name = "TransactionLog.findByRules", query = "SELECT t FROM TransactionLog t WHERE t.rules = :rules"),
		@NamedQuery(name = "TransactionLog.findByCategoryId", query = "SELECT t FROM TransactionLog t WHERE t.categoryId = :categoryId"),
		@NamedQuery(name = "TransactionLog.findByTransactionAmount", query = "SELECT t FROM TransactionLog t WHERE t.transactionAmount = :transactionAmount"),
		@NamedQuery(name = "TransactionLog.findByAuthAmount", query = "SELECT t FROM TransactionLog t WHERE t.authAmount = :authAmount"),
		@NamedQuery(name = "TransactionLog.findByTranfeeAmount", query = "SELECT t FROM TransactionLog t WHERE t.tranfeeAmount = :tranfeeAmount"),
		@NamedQuery(name = "TransactionLog.findByFeeProfile", query = "SELECT t FROM TransactionLog t WHERE t.feeProfile = :feeProfile"),
		@NamedQuery(name = "TransactionLog.findByFeeReversalFlag", query = "SELECT t FROM TransactionLog t WHERE t.feeReversalFlag = :feeReversalFlag"),
		@NamedQuery(name = "TransactionLog.findByWaiverAmount", query = "SELECT t FROM TransactionLog t WHERE t.waiverAmount = :waiverAmount"),
		@NamedQuery(name = "TransactionLog.findByPreauthDate", query = "SELECT t FROM TransactionLog t WHERE t.preauthDate = :preauthDate"),
		@NamedQuery(name = "TransactionLog.findByProcessFlag", query = "SELECT t FROM TransactionLog t WHERE t.processFlag = :processFlag"),
		@NamedQuery(name = "TransactionLog.findByTranReverseFlag", query = "SELECT t FROM TransactionLog t WHERE t.tranReverseFlag = :tranReverseFlag"),
		@NamedQuery(name = "TransactionLog.findByReversalCode", query = "SELECT t FROM TransactionLog t WHERE t.reversalCode = :reversalCode"),
		@NamedQuery(name = "TransactionLog.findByInsDate", query = "SELECT t FROM TransactionLog t WHERE t.insDate = :insDate"),
		@NamedQuery(name = "TransactionLog.findByInsUser", query = "SELECT t FROM TransactionLog t WHERE t.insUser = :insUser"),
		@NamedQuery(name = "TransactionLog.findByLastUpdDate", query = "SELECT t FROM TransactionLog t WHERE t.lastUpdDate = :lastUpdDate"),
		@NamedQuery(name = "TransactionLog.findByLastUpdUser", query = "SELECT t FROM TransactionLog t WHERE t.lastUpdUser = :lastUpdUser"),
		@NamedQuery(name = "TransactionLog.findByTranCurr", query = "SELECT t FROM TransactionLog t WHERE t.tranCurr = :tranCurr"),
		@NamedQuery(name = "TransactionLog.findByOrgnlCardNbr", query = "SELECT t FROM TransactionLog t WHERE t.orgnlCardNbr = :orgnlCardNbr"),
		@NamedQuery(name = "TransactionLog.findByOrgnlRrn", query = "SELECT t FROM TransactionLog t WHERE t.orgnlRrn = :orgnlRrn"),
		@NamedQuery(name = "TransactionLog.findByOrgnlTransactionDate", query = "SELECT t FROM TransactionLog t WHERE t.orgnlTransactionDate = :orgnlTransactionDate"),
		@NamedQuery(name = "TransactionLog.findByOrgnlTransactionTime", query = "SELECT t FROM TransactionLog t WHERE t.orgnlTransactionTime = :orgnlTransactionTime"),
		@NamedQuery(name = "TransactionLog.findByOrgnlTerminalId", query = "SELECT t FROM TransactionLog t WHERE t.orgnlTerminalId = :orgnlTerminalId"),
		@NamedQuery(name = "TransactionLog.findByLedgerBalance", query = "SELECT t FROM TransactionLog t WHERE t.ledgerBalance = :ledgerBalance"),
		@NamedQuery(name = "TransactionLog.findByAccountBalance", query = "SELECT t FROM TransactionLog t WHERE t.accountBalance = :accountBalance"),
		@NamedQuery(name = "TransactionLog.findByOpeningLedgerBalance", query = "SELECT t FROM TransactionLog t WHERE t.openingLedgerBalance = :openingLedgerBalance"),
		@NamedQuery(name = "TransactionLog.findByOpeningAvailableBalance", query = "SELECT t FROM TransactionLog t WHERE t.openingAvailableBalance = :openingAvailableBalance"),
		@NamedQuery(name = "TransactionLog.findByTopupAccountBalance", query = "SELECT t FROM TransactionLog t WHERE t.topupAccountBalance = :topupAccountBalance"),
		@NamedQuery(name = "TransactionLog.findByTopupLedgerBalance", query = "SELECT t FROM TransactionLog t WHERE t.topupLedgerBalance = :topupLedgerBalance"),
		@NamedQuery(name = "TransactionLog.findByCustomerFirstName", query = "SELECT t FROM TransactionLog t WHERE t.customerFirstName = :customerFirstName"),
		@NamedQuery(name = "TransactionLog.findByCustomerLastName", query = "SELECT t FROM TransactionLog t WHERE t.customerLastName = :customerLastName"),
		@NamedQuery(name = "TransactionLog.findByCardStatus", query = "SELECT t FROM TransactionLog t WHERE t.cardStatus = :cardStatus"),
		@NamedQuery(name = "TransactionLog.findByProcessType", query = "SELECT t FROM TransactionLog t WHERE t.processType = :processType"),
		@NamedQuery(name = "TransactionLog.findByCountryCode", query = "SELECT t FROM TransactionLog t WHERE t.countryCode = :countryCode"),
		@NamedQuery(name = "TransactionLog.findByZipCode", query = "SELECT t FROM TransactionLog t WHERE t.zipCode = :zipCode"),
		@NamedQuery(name = "TransactionLog.findByMerchantId", query = "SELECT t FROM TransactionLog t WHERE t.merchantId = :merchantId"),
		@NamedQuery(name = "TransactionLog.findByMerchantName", query = "SELECT t FROM TransactionLog t WHERE t.merchantName = :merchantName"),
		@NamedQuery(name = "TransactionLog.findByMerchantStreet", query = "SELECT t FROM TransactionLog t WHERE t.merchantStreet = :merchantStreet"),
		@NamedQuery(name = "TransactionLog.findByMerchantCity", query = "SELECT t FROM TransactionLog t WHERE t.merchantCity = :merchantCity"),
		@NamedQuery(name = "TransactionLog.findByMerchantState", query = "SELECT t FROM TransactionLog t WHERE t.merchantState = :merchantState"),
		@NamedQuery(name = "TransactionLog.findByMerchantZip", query = "SELECT t FROM TransactionLog t WHERE t.merchantZip = :merchantZip"),
		@NamedQuery(name = "TransactionLog.findByStoreId", query = "SELECT t FROM TransactionLog t WHERE t.storeId = :storeId"),
		@NamedQuery(name = "TransactionLog.findByStoreAddress1", query = "SELECT t FROM TransactionLog t WHERE t.storeAddress1 = :storeAddress1"),
		@NamedQuery(name = "TransactionLog.findByStoreAddress2", query = "SELECT t FROM TransactionLog t WHERE t.storeAddress2 = :storeAddress2"),
		@NamedQuery(name = "TransactionLog.findByStoreCity", query = "SELECT t FROM TransactionLog t WHERE t.storeCity = :storeCity"),
		@NamedQuery(name = "TransactionLog.findByStoreState", query = "SELECT t FROM TransactionLog t WHERE t.storeState = :storeState"),
		@NamedQuery(name = "TransactionLog.findByStoreZip", query = "SELECT t FROM TransactionLog t WHERE t.storeZip = :storeZip"),
		@NamedQuery(name = "TransactionLog.findByFsapiUsername", query = "SELECT t FROM TransactionLog t WHERE t.fsapiUsername = :fsapiUsername"),
		@NamedQuery(name = "TransactionLog.findByReqPartnerId", query = "SELECT t FROM TransactionLog t WHERE t.reqPartnerId = :reqPartnerId"),
		@NamedQuery(name = "TransactionLog.findBySpilProdId", query = "SELECT t FROM TransactionLog t WHERE t.spilProdId = :spilProdId"),
		@NamedQuery(name = "TransactionLog.findBySpilFee", query = "SELECT t FROM TransactionLog t WHERE t.spilFee = :spilFee"),
		@NamedQuery(name = "TransactionLog.findBySpilUpc", query = "SELECT t FROM TransactionLog t WHERE t.spilUpc = :spilUpc"),
		@NamedQuery(name = "TransactionLog.findBySpilMerrefNum", query = "SELECT t FROM TransactionLog t WHERE t.spilMerrefNum = :spilMerrefNum"),
		@NamedQuery(name = "TransactionLog.findBySpilReqTmzm", query = "SELECT t FROM TransactionLog t WHERE t.spilReqTmzm = :spilReqTmzm"),
		@NamedQuery(name = "TransactionLog.findBySpilLocCntry", query = "SELECT t FROM TransactionLog t WHERE t.spilLocCntry = :spilLocCntry"),
		@NamedQuery(name = "TransactionLog.findBySpilLocCrcy", query = "SELECT t FROM TransactionLog t WHERE t.spilLocCrcy = :spilLocCrcy"),
		@NamedQuery(name = "TransactionLog.findBySpilLocLang", query = "SELECT t FROM TransactionLog t WHERE t.spilLocLang = :spilLocLang"),
		@NamedQuery(name = "TransactionLog.findBySpilPosEntry", query = "SELECT t FROM TransactionLog t WHERE t.spilPosEntry = :spilPosEntry"),
		@NamedQuery(name = "TransactionLog.findBySpilPosCond", query = "SELECT t FROM TransactionLog t WHERE t.spilPosCond = :spilPosCond"),
		@NamedQuery(name = "TransactionLog.findByRequestXml", query = "SELECT t FROM TransactionLog t WHERE t.requestXml = :requestXml"),
		@NamedQuery(name = "TransactionLog.findByPosVerification", query = "SELECT t FROM TransactionLog t WHERE t.posVerification = :posVerification"),
		@NamedQuery(name = "TransactionLog.findByCorrelationId", query = "SELECT t FROM TransactionLog t WHERE t.correlationId = :correlationId"),
		@NamedQuery(name = "TransactionLog.findByMatchRule", query = "SELECT t FROM TransactionLog t WHERE t.matchRule = :matchRule"),
		@NamedQuery(name = "TransactionLog.findByCompletionCount", query = "SELECT t FROM TransactionLog t WHERE t.completionCount = :completionCount"),
		@NamedQuery(name = "TransactionLog.findByCvvVerficationType", query = "SELECT t FROM TransactionLog t WHERE t.cvvVerficationType = :cvvVerficationType"),
		@NamedQuery(name = "TransactionLog.findByReason", query = "SELECT t FROM TransactionLog t WHERE t.reason = :reason"),
		@NamedQuery(name = "TransactionLog.findByDisputeFlag", query = "SELECT t FROM TransactionLog t WHERE t.disputeFlag = :disputeFlag"),
		@NamedQuery(name = "TransactionLog.findByReasonCode", query = "SELECT t FROM TransactionLog t WHERE t.reasonCode = :reasonCode"),
		@NamedQuery(name = "TransactionLog.findByIpAddress", query = "SELECT t FROM TransactionLog t WHERE t.ipAddress = :ipAddress"),
		@NamedQuery(name = "TransactionLog.findByAni", query = "SELECT t FROM TransactionLog t WHERE t.ani = :ani"),
		@NamedQuery(name = "TransactionLog.findByDni", query = "SELECT t FROM TransactionLog t WHERE t.dni = :dni"),
		@NamedQuery(name = "TransactionLog.findByRemark", query = "SELECT t FROM TransactionLog t WHERE t.remark = :remark"),
		@NamedQuery(name = "TransactionLog.findByUuid", query = "SELECT t FROM TransactionLog t WHERE t.uuid = :uuid"),
		@NamedQuery(name = "TransactionLog.findByOsName", query = "SELECT t FROM TransactionLog t WHERE t.osName = :osName"),
		@NamedQuery(name = "TransactionLog.findByOsVersion", query = "SELECT t FROM TransactionLog t WHERE t.osVersion = :osVersion"),
		@NamedQuery(name = "TransactionLog.findByGpsCoordinates", query = "SELECT t FROM TransactionLog t WHERE t.gpsCoordinates = :gpsCoordinates"),
		@NamedQuery(name = "TransactionLog.findByDisplayResoultion", query = "SELECT t FROM TransactionLog t WHERE t.displayResoultion = :displayResoultion"),
		@NamedQuery(name = "TransactionLog.findByPhysicalMemory", query = "SELECT t FROM TransactionLog t WHERE t.physicalMemory = :physicalMemory"),
		@NamedQuery(name = "TransactionLog.findByAppName", query = "SELECT t FROM TransactionLog t WHERE t.appName = :appName"),
		@NamedQuery(name = "TransactionLog.findByAppVersion", query = "SELECT t FROM TransactionLog t WHERE t.appVersion = :appVersion"),
		@NamedQuery(name = "TransactionLog.findBySessionId", query = "SELECT t FROM TransactionLog t WHERE t.sessionId = :sessionId"),
		@NamedQuery(name = "TransactionLog.findByDeviceCountry", query = "SELECT t FROM TransactionLog t WHERE t.deviceCountry = :deviceCountry"),
		@NamedQuery(name = "TransactionLog.findByDeviceRegion", query = "SELECT t FROM TransactionLog t WHERE t.deviceRegion = :deviceRegion"),
		@NamedQuery(name = "TransactionLog.findByIpCountry", query = "SELECT t FROM TransactionLog t WHERE t.ipCountry = :ipCountry"),
		@NamedQuery(name = "TransactionLog.findByProxyFlag", query = "SELECT t FROM TransactionLog t WHERE t.proxyFlag = :proxyFlag"),
		@NamedQuery(name = "TransactionLog.findByProcessMsg", query = "SELECT t FROM TransactionLog t WHERE t.processMsg = :processMsg"),
		@NamedQuery(name = "TransactionLog.findByMobileAlertName", query = "SELECT t FROM TransactionLog t WHERE t.mobileAlertName = :mobileAlertName"),
		@NamedQuery(name = "TransactionLog.findByMobileAlertStat", query = "SELECT t FROM TransactionLog t WHERE t.mobileAlertStat = :mobileAlertStat"),
		@NamedQuery(name = "TransactionLog.findByMobileAlertNotify", query = "SELECT t FROM TransactionLog t WHERE t.mobileAlertNotify = :mobileAlertNotify"),
		@NamedQuery(name = "TransactionLog.findByDeviceId", query = "SELECT t FROM TransactionLog t WHERE t.deviceId = :deviceId"),
		@NamedQuery(name = "TransactionLog.findByMobileNumber", query = "SELECT t FROM TransactionLog t WHERE t.mobileNumber = :mobileNumber"),
		@NamedQuery(name = "TransactionLog.findByUserName", query = "SELECT t FROM TransactionLog t WHERE t.userName = :userName"),
		@NamedQuery(name = "TransactionLog.findByHashkeyId", query = "SELECT t FROM TransactionLog t WHERE t.hashkeyId = :hashkeyId"),
		@NamedQuery(name = "TransactionLog.findByAlertOptin", query = "SELECT t FROM TransactionLog t WHERE t.alertOptin = :alertOptin"),
		@NamedQuery(name = "TransactionLog.findByLocationId", query = "SELECT t FROM TransactionLog t WHERE t.locationId = :locationId"),
		@NamedQuery(name = "TransactionLog.findByTaxprepareId", query = "SELECT t FROM TransactionLog t WHERE t.taxprepareId = :taxprepareId"),
		@NamedQuery(name = "TransactionLog.findByOptnPhone2", query = "SELECT t FROM TransactionLog t WHERE t.optnPhone2 = :optnPhone2"),
		@NamedQuery(name = "TransactionLog.findByEmail", query = "SELECT t FROM TransactionLog t WHERE t.email = :email"),
		@NamedQuery(name = "TransactionLog.findByOptnEmail", query = "SELECT t FROM TransactionLog t WHERE t.optnEmail = :optnEmail"),
		@NamedQuery(name = "TransactionLog.findByReqRespCode", query = "SELECT t FROM TransactionLog t WHERE t.reqRespCode = :reqRespCode"),
		@NamedQuery(name = "TransactionLog.findByChwComment", query = "SELECT t FROM TransactionLog t WHERE t.chwComment = :chwComment"),
		@NamedQuery(name = "TransactionLog.findByCardVerificationResult", query = "SELECT t FROM TransactionLog t WHERE t.cardVerificationResult = :cardVerificationResult"),
		@NamedQuery(name = "TransactionLog.findByCompletionFee", query = "SELECT t FROM TransactionLog t WHERE t.completionFee = :completionFee"),
		@NamedQuery(name = "TransactionLog.findByComplfeeIncrementType", query = "SELECT t FROM TransactionLog t WHERE t.complfeeIncrementType = :complfeeIncrementType"),
		@NamedQuery(name = "TransactionLog.findByCompfeeAttachType", query = "SELECT t FROM TransactionLog t WHERE t.compfeeAttachType = :compfeeAttachType"),
		@NamedQuery(name = "TransactionLog.findBySourceName", query = "SELECT t FROM TransactionLog t WHERE t.sourceName = :sourceName"),
		@NamedQuery(name = "TransactionLog.findByOriginatorStatCode", query = "SELECT t FROM TransactionLog t WHERE t.originatorStatCode = :originatorStatCode"),
		@NamedQuery(name = "TransactionLog.findByPaymentType", query = "SELECT t FROM TransactionLog t WHERE t.paymentType = :paymentType"),
		@NamedQuery(name = "TransactionLog.findByCustomerField1", query = "SELECT t FROM TransactionLog t WHERE t.customerField1 = :customerField1"),
		@NamedQuery(name = "TransactionLog.findByCustomerField2", query = "SELECT t FROM TransactionLog t WHERE t.customerField2 = :customerField2"),
		@NamedQuery(name = "TransactionLog.findByPosentryModeId", query = "SELECT t FROM TransactionLog t WHERE t.posentryModeId = :posentryModeId"),
		@NamedQuery(name = "TransactionLog.findByCnpIndicator", query = "SELECT t FROM TransactionLog t WHERE t.cnpIndicator = :cnpIndicator"),
		@NamedQuery(name = "TransactionLog.findByCardholderTraveling", query = "SELECT t FROM TransactionLog t WHERE t.cardholderTraveling = :cardholderTraveling"),
		@NamedQuery(name = "TransactionLog.findByDataVersion", query = "SELECT t FROM TransactionLog t WHERE t.dataVersion = :dataVersion"),
		@NamedQuery(name = "TransactionLog.findBySellerId", query = "SELECT t FROM TransactionLog t WHERE t.sellerId = :sellerId"),
		@NamedQuery(name = "TransactionLog.findByDynamicRuleCode", query = "SELECT t FROM TransactionLog t WHERE t.dynamicRuleCode = :dynamicRuleCode"),
		@NamedQuery(name = "TransactionLog.findByMaxFeeFlag", query = "SELECT t FROM TransactionLog t WHERE t.maxFeeFlag = :maxFeeFlag"),
		@NamedQuery(name = "TransactionLog.findByFreeFeeFlag", query = "SELECT t FROM TransactionLog t WHERE t.freeFeeFlag = :freeFeeFlag"),
		@NamedQuery(name = "TransactionLog.findByAccountNumber", query = "SELECT t FROM TransactionLog t WHERE t.accountNumber = :accountNumber"),
		@NamedQuery(name = "TransactionLog.findByAccountId", query = "SELECT t FROM TransactionLog t WHERE t.accountId = :accountId"),
		@NamedQuery(name = "TransactionLog.findByPurseId", query = "SELECT t FROM TransactionLog t WHERE t.purseId = :purseId"),
		@NamedQuery(name = "TransactionLog.findByTopupAccountNumber", query = "SELECT t FROM TransactionLog t WHERE t.topupAccountNumber = :topupAccountNumber"),
		@NamedQuery(name = "TransactionLog.findByTopupAccountId", query = "SELECT t FROM TransactionLog t WHERE t.topupAccountId = :topupAccountId"),
		@NamedQuery(name = "TransactionLog.findByCashierId", query = "SELECT t FROM TransactionLog t WHERE t.cashierId = :cashierId"),
		@NamedQuery(name = "TransactionLog.findByExpirationDate", query = "SELECT t FROM TransactionLog t WHERE t.expirationDate = :expirationDate"),
		@NamedQuery(name = "TransactionLog.findByMdmId", query = "SELECT t FROM TransactionLog t WHERE t.mdmId = :mdmId"),
		@NamedQuery(name = "TransactionLog.findByCustomerId", query = "SELECT t FROM TransactionLog t WHERE t.customerId = :customerId"),
		@NamedQuery(name = "TransactionLog.findByMarkedStatus", query = "SELECT t FROM TransactionLog t WHERE t.markedStatus = :markedStatus"),
		@NamedQuery(name = "TransactionLog.findBySpilStoreDbId", query = "SELECT t FROM TransactionLog t WHERE t.spilStoreDbId = :spilStoreDbId"),
		@NamedQuery(name = "TransactionLog.findByInsTimeStamp", query = "SELECT t FROM TransactionLog t WHERE t.insTimeStamp = :insTimeStamp") })

@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class TransactionLog implements Persistable<BigDecimal>, Serializable {

	private static final long serialVersionUID = 1L;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce
	// field validation
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "TRANSACTION_SQID")
	private BigDecimal transactionSqid;
	@Column(name = "ISSUER_ID")
	private BigInteger issuerId;
	@Column(name = "PARTNER_ID")
	private BigInteger partnerId;
	@Column(name = "PRODUCT_ID")
	private BigInteger productId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "DELIVERY_CHANNEL")
	private String deliveryChannel;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 3)
	@Column(name = "TRANSACTION_CODE")
	private String transactionCode;
	@Size(max = 10)
	@Column(name = "MSG_TYPE")
	private String msgType;
	@Size(max = 1)
	@Column(name = "IS_FINANCIAL")
	private String isFinancial;
	@Size(max = 2)
	@Column(name = "CR_DR_FLAG")
	private String crDrFlag;
	@Size(max = 100)
	@Column(name = "TRANSACTION_DESC")
	private String transactionDesc;
	@Size(max = 7)
	@Column(name = "RESPONSE_ID")
	private String responseId;
	@Size(max = 500)
	@Column(name = "ERROR_MSG")
	private String errorMsg;
	@Size(max = 1)
	@Column(name = "TRANSACTION_STATUS")
	private String transactionStatus;
	@Size(max = 21)
	@Column(name = "TERMINAL_ID")
	private String terminalId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "RRN")
	private String rrn;
	@Size(max = 100)
	@Column(name = "CARD_NUMBER")
	private String cardNumber;
	@Size(max = 100)
	@Column(name = "TOPUP_CARD_NUMBER")
	private String topupCardNumber;
	@Size(max = 100)
	@Column(name = "CUSTOMER_CARD_NBR_ENCR")
	private String customerCardNbrEncr;
	@Size(max = 100)
	@Column(name = "TOPUP_CARD_NBR_ENCR")
	private String topupCardNbrEncr;
	@Size(max = 20)
	@Column(name = "PROXY_NUMBER")
	private String proxyNumber;
	@Size(max = 8)
	@Column(name = "TRANSACTION_DATE")
	private String transactionDate;
	@Size(max = 6)
	@Column(name = "TRANSACTION_TIME")
	private String transactionTime;
	@Size(max = 3)
	@Column(name = "TRANSACTION_TIMEZONE")
	private String transactionTimezone;
	@Size(max = 4)
	@Column(name = "PARTIAL_PREAUTH_IND")
	private String partialPreauthInd;
	@Size(max = 15)
	@Column(name = "BUSINESS_DATE")
	private String businessDate;
	@Size(max = 14)
	@Column(name = "AUTH_ID")
	private String authId;
	@Size(max = 1)
	@Column(name = "RULE_INDICATOR")
	private String ruleIndicator;
	@Column(name = "RULESET_ID")
	private BigInteger rulesetId;
	@Size(max = 4)
	@Column(name = "MCCODE")
	private String mccode;
	@Size(max = 200)
	@Column(name = "RULES")
	private String rules;
	@Size(max = 4)
	@Column(name = "CATEGORY_ID")
	private String categoryId;
//    @Size(max = 20)
	@Column(name = "TRANSACTION_AMOUNT")
	private double transactionAmount;
//    @Size(max = 20)
	@Column(name = "AUTH_AMOUNT")
	private double authAmount;
	@Column(name = "TRANFEE_AMOUNT")
	private double tranfeeAmount;
	@Size(max = 4)
	@Column(name = "FEE_PROFILE")
	private String feeProfile;
	@Size(max = 1)
	@Column(name = "FEE_REVERSAL_FLAG")
	private String feeReversalFlag;
	@Column(name = "WAIVER_AMOUNT")
	private BigInteger waiverAmount;
	@Column(name = "PREAUTH_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date preauthDate;
	@Size(max = 1)
	@Column(name = "PROCESS_FLAG")
	private String processFlag;
	@Size(max = 1)
	@Column(name = "TRAN_REVERSE_FLAG")
	private String tranReverseFlag;
	@Column(name = "REVERSAL_CODE")
	private Short reversalCode;
	@Column(name = "INS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insDate;
	@Column(name = "INS_USER")
	private BigInteger insUser;
	@Column(name = "LAST_UPD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdDate;
	@Column(name = "LAST_UPD_USER")
	private BigInteger lastUpdUser;

	@Size(max = 90)
	@Column(name = "ORGNL_CARD_NBR")
	private String orgnlCardNbr;
	@Size(max = 40)
	@Column(name = "ORGNL_RRN")
	private String orgnlRrn;
	@Size(max = 8)
	@Column(name = "ORGNL_TRANSACTION_DATE")
	private String orgnlTransactionDate;
	@Size(max = 6)
	@Column(name = "ORGNL_TRANSACTION_TIME")
	private String orgnlTransactionTime;
	@Size(max = 21)
	@Column(name = "ORGNL_TERMINAL_ID")
	private String orgnlTerminalId;
	@Column(name = "LEDGER_BALANCE")
	private double ledgerBalance;
	@Column(name = "ACCOUNT_BALANCE")
	private double accountBalance;
	@Column(name = "OPENING_LEDGER_BALANCE")
	private double openingLedgerBalance;
	@Column(name = "OPENING_AVAILABLE_BALANCE")
	private double openingAvailableBalance;
	@Column(name = "TOPUP_ACCOUNT_BALANCE")
	private double topupAccountBalance;
	@Column(name = "TOPUP_LEDGER_BALANCE")
	private double topupLedgerBalance;
	@Size(max = 2000)
	@Column(name = "CUSTOMER_FIRST_NAME")
	private String customerFirstName;
	@Size(max = 2000)
	@Column(name = "CUSTOMER_LAST_NAME")
	private String customerLastName;
	@Size(max = 3)
	@Column(name = "CARD_STATUS")
	private String cardStatus;
	@Size(max = 2)
	@Column(name = "PROCESS_TYPE")
	private String processType;
	@Size(max = 4)
	@Column(name = "COUNTRY_CODE")
	private String countryCode;
	@Size(max = 15)
	@Column(name = "ZIP_CODE")
	private String zipCode;
	@Size(max = 20)
	@Column(name = "MERCHANT_ID")
	private String merchantId;
	@Size(max = 50)
	@Column(name = "MERCHANT_NAME")
	private String merchantName;
	@Size(max = 50)
	@Column(name = "MERCHANT_STREET")
	private String merchantStreet;
	@Size(max = 50)
	@Column(name = "MERCHANT_CITY")
	private String merchantCity;
	@Size(max = 30)
	@Column(name = "MERCHANT_STATE")
	private String merchantState;
	@Size(max = 15)
	@Column(name = "MERCHANT_ZIP")
	private String merchantZip;
	@Size(max = 64)
	@Column(name = "STORE_ID")
	private String storeDbId;
	@Size(max = 50)
	@Column(name = "STORE_ADDRESS1")
	private String storeAddress1;
	@Size(max = 50)
	@Column(name = "STORE_ADDRESS2")
	private String storeAddress2;
	@Size(max = 50)
	@Column(name = "STORE_CITY")
	private String storeCity;
	@Size(max = 3)
	@Column(name = "STORE_STATE")
	private String storeState;
	@Size(max = 15)
	@Column(name = "STORE_ZIP")
	private String storeZip;
	@Size(max = 20)
	@Column(name = "FSAPI_USERNAME")
	private String fsapiUsername;
	@Size(max = 100)
	@Column(name = "REQ_PARTNER_ID")
	private String reqPartnerId;
	@Size(max = 50)
	@Column(name = "SPIL_PROD_ID")
	private String spilProdId;
	@Column(name = "SPIL_FEE")
	private double spilFee;
	@Size(max = 50)
	@Column(name = "SPIL_UPC")
	private String spilUpc;
	@Size(max = 50)
	@Column(name = "SPIL_MERREF_NUM")
	private String spilMerrefNum;
	@Size(max = 50)
	@Column(name = "SPIL_REQ_TMZM")
	private String spilReqTmzm;
	@Size(max = 50)
	@Column(name = "SPIL_LOC_CNTRY")
	private String spilLocCntry;
	@Size(max = 50)
	@Column(name = "SPIL_LOC_CRCY")
	private String spilLocCrcy;
	@Size(max = 50)
	@Column(name = "SPIL_LOC_LANG")
	private String spilLocLang;
	@Size(max = 50)
	@Column(name = "SPIL_POS_ENTRY")
	private String spilPosEntry;
	@Size(max = 50)
	@Column(name = "SPIL_POS_COND")
	private String spilPosCond;
	@Size(max = 2000)
	@Column(name = "REQUEST_XML")
	private String requestXml;
	@Size(max = 1)
	@Column(name = "POS_VERIFICATION")
	private String posVerification;
	@Size(max = 20)
	@Column(name = "CORRELATION_ID")
	private String correlationId;
	@Size(max = 5)
	@Column(name = "MATCH_RULE")
	private String matchRule;
	@Size(max = 4)
	@Column(name = "COMPLETION_COUNT")
	private String completionCount;
	@Size(max = 1)
	@Column(name = "CVV_VERFICATION_TYPE")
	private String cvvVerficationType;
	@Size(max = 100)
	@Column(name = "REASON")
	private String reason;
	@Size(max = 1)
	@Column(name = "DISPUTE_FLAG")
	private String disputeFlag;
	@Column(name = "REASON_CODE")
	private Short reasonCode;
	@Size(max = 15)
	@Column(name = "IP_ADDRESS")
	private String ipAddress;
	@Size(max = 12)
	@Column(name = "ANI")
	private String ani;
	@Size(max = 12)
	@Column(name = "DNI")
	private String dni;
	@Size(max = 1000)
	@Column(name = "REMARK")
	private String remark;
	@Size(max = 200)
	@Column(name = "UUID")
	private String uuid;
	@Size(max = 200)
	@Column(name = "OS_NAME")
	private String osName;
	@Size(max = 200)
	@Column(name = "OS_VERSION")
	private String osVersion;
	@Size(max = 200)
	@Column(name = "GPS_COORDINATES")
	private String gpsCoordinates;
	@Size(max = 200)
	@Column(name = "DISPLAY_RESOULTION")
	private String displayResoultion;
	@Size(max = 200)
	@Column(name = "PHYSICAL_MEMORY")
	private String physicalMemory;
	@Size(max = 200)
	@Column(name = "APP_NAME")
	private String appName;
	@Size(max = 200)
	@Column(name = "APP_VERSION")
	private String appVersion;
	@Size(max = 200)
	@Column(name = "SESSION_ID")
	private String sessionId;
	@Size(max = 200)
	@Column(name = "DEVICE_COUNTRY")
	private String deviceCountry;
	@Size(max = 200)
	@Column(name = "DEVICE_REGION")
	private String deviceRegion;
	@Size(max = 200)
	@Column(name = "IP_COUNTRY")
	private String ipCountry;
	@Size(max = 200)
	@Column(name = "PROXY_FLAG")
	private String proxyFlag;
	@Size(max = 500)
	@Column(name = "PROCESS_MSG")
	private String processMsg;
	@Size(max = 50)
	@Column(name = "MOBILE_ALERT_NAME")
	private String mobileAlertName;
	@Size(max = 5)
	@Column(name = "MOBILE_ALERT_STAT")
	private String mobileAlertStat;
	@Size(max = 20)
	@Column(name = "MOBILE_ALERT_NOTIFY")
	private String mobileAlertNotify;
	@Size(max = 40)
	@Column(name = "DEVICE_ID")
	private String deviceId;
	@Size(max = 40)
	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;
	@Size(max = 40)
	@Column(name = "USER_NAME")
	private String userName;
	@Size(max = 100)
	@Column(name = "HASHKEY_ID")
	private String hashkeyId;
	@Size(max = 1)
	@Column(name = "ALERT_OPTIN")
	private String alertOptin;
	@Size(max = 64)
	@Column(name = "LOCATION_ID")
	private String locationId;
	@Size(max = 6)
	@Column(name = "TAXPREPARE_ID")
	private String taxprepareId;
	@Column(name = "OPTN_PHONE_2")
	private Character optnPhone2;
	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	// message="Invalid email")//if the field contains email address consider using this annotation to enforce field
	// validation
	@Size(max = 30)
	@Column(name = "EMAIL")
	private String email;
	@Column(name = "OPTN_EMAIL")
	private Character optnEmail;
	@Size(max = 7)
	@Column(name = "REQ_RESP_CODE")
	private String reqRespCode;
	@Size(max = 100)
	@Column(name = "CHW_COMMENT")
	private String chwComment;
	@Size(max = 1)
	@Column(name = "CARD_VERIFICATION_RESULT")
	private String cardVerificationResult;
	@Column(name = "COMPLETION_FEE")
	private BigInteger completionFee;
	@Size(max = 1)
	@Column(name = "COMPLFEE_INCREMENT_TYPE")
	private String complfeeIncrementType;
	@Size(max = 4)
	@Column(name = "COMPFEE_ATTACH_TYPE")
	private String compfeeAttachType;
	@Size(max = 100)
	@Column(name = "SOURCE_NAME")
	private String sourceName;
	@Size(max = 2)
	@Column(name = "ORIGINATOR_STAT_CODE")
	private String originatorStatCode;
	@Size(max = 50)
	@Column(name = "PAYMENT_TYPE")
	private String paymentType;
	@Size(max = 20)
	@Column(name = "CUSTOMER_FIELD1")
	private String customerField1;
	@Size(max = 20)
	@Column(name = "CUSTOMER_FIELD2")
	private String customerField2;
	@Size(max = 3)
	@Column(name = "POSENTRY_MODE_ID")
	private String posentryModeId;
	@Size(max = 20)
	@Column(name = "CNP_INDICATOR")
	private String cnpIndicator;
	@Size(max = 200)
	@Column(name = "CARDHOLDER_TRAVELING")
	private String cardholderTraveling;
	@Size(max = 200)
	@Column(name = "DATA_VERSION")
	private String dataVersion;
	@Size(max = 200)
	@Column(name = "SELLER_ID")
	private String sellerId;
	@Size(max = 200)
	@Column(name = "DYNAMIC_RULE_CODE")
	private String dynamicRuleCode;
	@Size(max = 5)
	@Column(name = "MAX_FEE_FLAG")
	private String maxFeeFlag;
	@Size(max = 5)
	@Column(name = "FREE_FEE_FLAG")
	private String freeFeeFlag;
	@Size(max = 20)
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	@Column(name = "ACCOUNT_ID")
	private BigInteger accountId;
	@Column(name = "PURSE_ID")
	private BigInteger purseId;
	@Size(max = 20)
	@Column(name = "TOPUP_ACCOUNT_NUMBER")
	private String topupAccountNumber;
	@Column(name = "TOPUP_ACCOUNT_ID")
	private BigInteger topupAccountId;
	@Size(max = 10)
	@Column(name = "CASHIER_ID")
	private String cashierId;
	@Size(max = 10)
	@Column(name = "EXPIRATION_DATE")
	private String expirationDate;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "MDM_ID")
	private String mdmId;
	@Column(name = "CUSTOMER_ID")
	private BigInteger customerId;
	@Size(max = 20)
	@Column(name = "MARKED_STATUS")
	private String markedStatus;
	@Size(max = 20)
	@Column(name = "SPIL_STORE_DB_ID")
	private String spilStoreDbId;
	@Column(name = "INS_TIME_STAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insTimeStamp;

	@Column(name = "ORGNL_TRAN_AMOUNT")
	private double orgnlTranAmt;

	@Column(name = "ORGNL_AUTH_AMOUNT")
	private double orgnlAuthAmount;

	@Column(name = "CONVERSION_RATE")
	private BigDecimal convRate;

	@Size(max = 6)
	@Column(name = "TRAN_CURR")
	private String tranCurr; // Purse Currency

	@Size(max = 6)
	@Column(name = "ORGNL_TRAN_CURR")
	private String orgnlTranCurr;

	@Column(name = "TO_ACCOUNT_PURSE_ID")
	private Long toAccountPurseId;

	@Column(name = "ACCOUNT_PURSE_ID")
	private Long accountPurseId;

	@Column(name = "PURSE_NAME")
	private String purseName;

	@Column(name = "PURSE_TXN_AMOUNT")
	private Double purseTxnAmount;

	@Column(name = "PURSE_TYPE")
	private String purseCurrency;

	@Column(name = "SKUCODE")
	private String skuCode;

	@Column(name = "SPIL_STORE_ID")
	private String storeId;

	public BigDecimal getConvRate() {
		return convRate;
	}

	public String getOrgnlTranCurr() {
		return orgnlTranCurr;
	}

	public void setOrgnlTranCurr(String orgnlTranCurr) {
		this.orgnlTranCurr = orgnlTranCurr;
	}

	public void setConvRate(BigDecimal convRate) {
		this.convRate = convRate;
	}

	public double getOrgnlTranAmt() {
		return orgnlTranAmt;
	}

	public void setOrgnlTranAmt(double orgnlTranAmt) {
		this.orgnlTranAmt = orgnlTranAmt;
	}

	public double getOrgnlAuthAmount() {
		return orgnlAuthAmount;
	}

	public void setOrgnlAuthAmount(double orgnlAuthAmount) {
		this.orgnlAuthAmount = orgnlAuthAmount;
	}

	public TransactionLog() {
	}

	public TransactionLog(BigDecimal transactionSqid, String deliveryChannel, String transactionCode, String rrn, String mdmId) {
		this.transactionSqid = transactionSqid;
		this.deliveryChannel = deliveryChannel;
		this.transactionCode = transactionCode;
		this.rrn = rrn;
		this.mdmId = mdmId;
	}

	public BigDecimal getTransactionSqid() {
		return transactionSqid;
	}

	public void setTransactionSqid(BigDecimal transactionSqid) {
		this.transactionSqid = transactionSqid;
	}

	public BigInteger getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(BigInteger issuerId) {
		this.issuerId = issuerId;
	}

	public BigInteger getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(BigInteger partnerId) {
		this.partnerId = partnerId;
	}

	public BigInteger getProductId() {
		return productId;
	}

	public void setProductId(BigInteger productId) {
		this.productId = productId;
	}

	public String getDeliveryChannel() {
		return deliveryChannel;
	}

	public void setDeliveryChannel(String deliveryChannel) {
		this.deliveryChannel = deliveryChannel;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getIsFinancial() {
		return isFinancial;
	}

	public void setIsFinancial(String isFinancial) {
		this.isFinancial = isFinancial;
	}

	public String getCrDrFlag() {
		return crDrFlag;
	}

	public void setCrDrFlag(String crDrFlag) {
		this.crDrFlag = crDrFlag;
	}

	public String getTransactionDesc() {
		return transactionDesc;
	}

	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getTopupCardNumber() {
		return topupCardNumber;
	}

	public void setTopupCardNumber(String topupCardNumber) {
		this.topupCardNumber = topupCardNumber;
	}

	public String getCustomerCardNbrEncr() {
		return customerCardNbrEncr;
	}

	public void setCustomerCardNbrEncr(String customerCardNbrEncr) {
		this.customerCardNbrEncr = customerCardNbrEncr;
	}

	public String getTopupCardNbrEncr() {
		return topupCardNbrEncr;
	}

	public void setTopupCardNbrEncr(String topupCardNbrEncr) {
		this.topupCardNbrEncr = topupCardNbrEncr;
	}

	public String getProxyNumber() {
		return proxyNumber;
	}

	public void setProxyNumber(String proxyNumber) {
		this.proxyNumber = proxyNumber;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getTransactionTimezone() {
		return transactionTimezone;
	}

	public void setTransactionTimezone(String transactionTimezone) {
		this.transactionTimezone = transactionTimezone;
	}

	public String getPartialPreauthInd() {
		return partialPreauthInd;
	}

	public void setPartialPreauthInd(String partialPreauthInd) {
		this.partialPreauthInd = partialPreauthInd;
	}

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getRuleIndicator() {
		return ruleIndicator;
	}

	public void setRuleIndicator(String ruleIndicator) {
		this.ruleIndicator = ruleIndicator;
	}

	public BigInteger getRulesetId() {
		return rulesetId;
	}

	public void setRulesetId(BigInteger rulesetId) {
		this.rulesetId = rulesetId;
	}

	public String getMccode() {
		return mccode;
	}

	public void setMccode(String mccode) {
		this.mccode = mccode;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public double getAuthAmount() {
		return authAmount;
	}

	public void setAuthAmount(double authAmount) {
		this.authAmount = authAmount;
	}

	public double getTranfeeAmount() {
		return tranfeeAmount;
	}

	public void setTranfeeAmount(double tranfeeAmount) {
		this.tranfeeAmount = tranfeeAmount;
	}

	public String getFeeProfile() {
		return feeProfile;
	}

	public void setFeeProfile(String feeProfile) {
		this.feeProfile = feeProfile;
	}

	public String getFeeReversalFlag() {
		return feeReversalFlag;
	}

	public void setFeeReversalFlag(String feeReversalFlag) {
		this.feeReversalFlag = feeReversalFlag;
	}

	public BigInteger getWaiverAmount() {
		return waiverAmount;
	}

	public void setWaiverAmount(BigInteger waiverAmount) {
		this.waiverAmount = waiverAmount;
	}

	public Date getPreauthDate() {
		return preauthDate;
	}

	public void setPreauthDate(Date preauthDate) {
		this.preauthDate = preauthDate;
	}

	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}

	public String getTranReverseFlag() {
		return tranReverseFlag;
	}

	public void setTranReverseFlag(String tranReverseFlag) {
		this.tranReverseFlag = tranReverseFlag;
	}

	public Short getReversalCode() {
		return reversalCode;
	}

	public void setReversalCode(Short reversalCode) {
		this.reversalCode = reversalCode;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {

		this.insDate = insDate;
	}

	public BigInteger getInsUser() {
		return insUser;
	}

	public void setInsUser(BigInteger insUser) {
		this.insUser = insUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public BigInteger getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(BigInteger lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public String getTranCurr() {
		return tranCurr;
	}

	public void setTranCurr(String tranCurr) {
		this.tranCurr = tranCurr;
	}

	public String getOrgnlCardNbr() {
		return orgnlCardNbr;
	}

	public void setOrgnlCardNbr(String orgnlCardNbr) {
		this.orgnlCardNbr = orgnlCardNbr;
	}

	public String getOrgnlRrn() {
		return orgnlRrn;
	}

	public void setOrgnlRrn(String orgnlRrn) {
		this.orgnlRrn = orgnlRrn;
	}

	public String getOrgnlTransactionDate() {
		return orgnlTransactionDate;
	}

	public void setOrgnlTransactionDate(String orgnlTransactionDate) {
		this.orgnlTransactionDate = orgnlTransactionDate;
	}

	public String getOrgnlTransactionTime() {
		return orgnlTransactionTime;
	}

	public void setOrgnlTransactionTime(String orgnlTransactionTime) {
		this.orgnlTransactionTime = orgnlTransactionTime;
	}

	public String getOrgnlTerminalId() {
		return orgnlTerminalId;
	}

	public void setOrgnlTerminalId(String orgnlTerminalId) {
		this.orgnlTerminalId = orgnlTerminalId;
	}

	public double getLedgerBalance() {
		return ledgerBalance;
	}

	public void setLedgerBalance(double ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public double getOpeningLedgerBalance() {
		return openingLedgerBalance;
	}

	public void setOpeningLedgerBalance(double openingLedgerBalance) {
		this.openingLedgerBalance = openingLedgerBalance;
	}

	public double getOpeningAvailableBalance() {
		return openingAvailableBalance;
	}

	public void setOpeningAvailableBalance(double openingAvailableBalance) {
		this.openingAvailableBalance = openingAvailableBalance;
	}

	public double getTopupAccountBalance() {
		return topupAccountBalance;
	}

	public void setTopupAccountBalance(double topupAccountBalance) {
		this.topupAccountBalance = topupAccountBalance;
	}

	public double getTopupLedgerBalance() {
		return topupLedgerBalance;
	}

	public void setTopupLedgerBalance(double topupLedgerBalance) {
		this.topupLedgerBalance = topupLedgerBalance;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantStreet() {
		return merchantStreet;
	}

	public void setMerchantStreet(String merchantStreet) {
		this.merchantStreet = merchantStreet;
	}

	public String getMerchantCity() {
		return merchantCity;
	}

	public void setMerchantCity(String merchantCity) {
		this.merchantCity = merchantCity;
	}

	public String getMerchantState() {
		return merchantState;
	}

	public void setMerchantState(String merchantState) {
		this.merchantState = merchantState;
	}

	public String getMerchantZip() {
		return merchantZip;
	}

	public void setMerchantZip(String merchantZip) {
		this.merchantZip = merchantZip;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreAddress1() {
		return storeAddress1;
	}

	public void setStoreAddress1(String storeAddress1) {
		this.storeAddress1 = storeAddress1;
	}

	public String getStoreAddress2() {
		return storeAddress2;
	}

	public void setStoreAddress2(String storeAddress2) {
		this.storeAddress2 = storeAddress2;
	}

	public String getStoreCity() {
		return storeCity;
	}

	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}

	public String getStoreState() {
		return storeState;
	}

	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}

	public String getStoreZip() {
		return storeZip;
	}

	public void setStoreZip(String storeZip) {
		this.storeZip = storeZip;
	}

	public String getFsapiUsername() {
		return fsapiUsername;
	}

	public void setFsapiUsername(String fsapiUsername) {
		this.fsapiUsername = fsapiUsername;
	}

	public String getReqPartnerId() {
		return reqPartnerId;
	}

	public void setReqPartnerId(String reqPartnerId) {
		this.reqPartnerId = reqPartnerId;
	}

	public String getSpilProdId() {
		return spilProdId;
	}

	public void setSpilProdId(String spilProdId) {
		this.spilProdId = spilProdId;
	}

	public double getSpilFee() {
		return spilFee;
	}

	public void setSpilFee(double spilFee) {
		this.spilFee = spilFee;
	}

	public String getSpilUpc() {
		return spilUpc;
	}

	public void setSpilUpc(String spilUpc) {
		this.spilUpc = spilUpc;
	}

	public String getSpilMerrefNum() {
		return spilMerrefNum;
	}

	public void setSpilMerrefNum(String spilMerrefNum) {
		this.spilMerrefNum = spilMerrefNum;
	}

	public String getSpilReqTmzm() {
		return spilReqTmzm;
	}

	public void setSpilReqTmzm(String spilReqTmzm) {
		this.spilReqTmzm = spilReqTmzm;
	}

	public String getSpilLocCntry() {
		return spilLocCntry;
	}

	public void setSpilLocCntry(String spilLocCntry) {
		this.spilLocCntry = spilLocCntry;
	}

	public String getSpilLocCrcy() {
		return spilLocCrcy;
	}

	public void setSpilLocCrcy(String spilLocCrcy) {
		this.spilLocCrcy = spilLocCrcy;
	}

	public String getSpilLocLang() {
		return spilLocLang;
	}

	public void setSpilLocLang(String spilLocLang) {
		this.spilLocLang = spilLocLang;
	}

	public String getSpilPosEntry() {
		return spilPosEntry;
	}

	public void setSpilPosEntry(String spilPosEntry) {
		this.spilPosEntry = spilPosEntry;
	}

	public String getSpilPosCond() {
		return spilPosCond;
	}

	public void setSpilPosCond(String spilPosCond) {
		this.spilPosCond = spilPosCond;
	}

	public String getRequestXml() {
		return requestXml;
	}

	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	public String getPosVerification() {
		return posVerification;
	}

	public void setPosVerification(String posVerification) {
		this.posVerification = posVerification;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(String matchRule) {
		this.matchRule = matchRule;
	}

	public String getCompletionCount() {
		return completionCount;
	}

	public void setCompletionCount(String completionCount) {
		this.completionCount = completionCount;
	}

	public String getCvvVerficationType() {
		return cvvVerficationType;
	}

	public void setCvvVerficationType(String cvvVerficationType) {
		this.cvvVerficationType = cvvVerficationType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDisputeFlag() {
		return disputeFlag;
	}

	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}

	public Short getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Short reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAni() {
		return ani;
	}

	public void setAni(String ani) {
		this.ani = ani;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getGpsCoordinates() {
		return gpsCoordinates;
	}

	public void setGpsCoordinates(String gpsCoordinates) {
		this.gpsCoordinates = gpsCoordinates;
	}

	public String getDisplayResoultion() {
		return displayResoultion;
	}

	public void setDisplayResoultion(String displayResoultion) {
		this.displayResoultion = displayResoultion;
	}

	public String getPhysicalMemory() {
		return physicalMemory;
	}

	public void setPhysicalMemory(String physicalMemory) {
		this.physicalMemory = physicalMemory;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getDeviceCountry() {
		return deviceCountry;
	}

	public void setDeviceCountry(String deviceCountry) {
		this.deviceCountry = deviceCountry;
	}

	public String getDeviceRegion() {
		return deviceRegion;
	}

	public void setDeviceRegion(String deviceRegion) {
		this.deviceRegion = deviceRegion;
	}

	public String getIpCountry() {
		return ipCountry;
	}

	public void setIpCountry(String ipCountry) {
		this.ipCountry = ipCountry;
	}

	public String getProxyFlag() {
		return proxyFlag;
	}

	public void setProxyFlag(String proxyFlag) {
		this.proxyFlag = proxyFlag;
	}

	public String getProcessMsg() {
		return processMsg;
	}

	public void setProcessMsg(String processMsg) {
		this.processMsg = processMsg;
	}

	public String getMobileAlertName() {
		return mobileAlertName;
	}

	public void setMobileAlertName(String mobileAlertName) {
		this.mobileAlertName = mobileAlertName;
	}

	public String getMobileAlertStat() {
		return mobileAlertStat;
	}

	public void setMobileAlertStat(String mobileAlertStat) {
		this.mobileAlertStat = mobileAlertStat;
	}

	public String getMobileAlertNotify() {
		return mobileAlertNotify;
	}

	public void setMobileAlertNotify(String mobileAlertNotify) {
		this.mobileAlertNotify = mobileAlertNotify;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHashkeyId() {
		return hashkeyId;
	}

	public void setHashkeyId(String hashkeyId) {
		this.hashkeyId = hashkeyId;
	}

	public String getAlertOptin() {
		return alertOptin;
	}

	public void setAlertOptin(String alertOptin) {
		this.alertOptin = alertOptin;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getTaxprepareId() {
		return taxprepareId;
	}

	public void setTaxprepareId(String taxprepareId) {
		this.taxprepareId = taxprepareId;
	}

	public Character getOptnPhone2() {
		return optnPhone2;
	}

	public void setOptnPhone2(Character optnPhone2) {
		this.optnPhone2 = optnPhone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Character getOptnEmail() {
		return optnEmail;
	}

	public void setOptnEmail(Character optnEmail) {
		this.optnEmail = optnEmail;
	}

	public String getReqRespCode() {
		return reqRespCode;
	}

	public void setReqRespCode(String reqRespCode) {
		this.reqRespCode = reqRespCode;
	}

	public String getChwComment() {
		return chwComment;
	}

	public void setChwComment(String chwComment) {
		this.chwComment = chwComment;
	}

	public String getCardVerificationResult() {
		return cardVerificationResult;
	}

	public void setCardVerificationResult(String cardVerificationResult) {
		this.cardVerificationResult = cardVerificationResult;
	}

	public BigInteger getCompletionFee() {
		return completionFee;
	}

	public void setCompletionFee(BigInteger completionFee) {
		this.completionFee = completionFee;
	}

	public String getComplfeeIncrementType() {
		return complfeeIncrementType;
	}

	public void setComplfeeIncrementType(String complfeeIncrementType) {
		this.complfeeIncrementType = complfeeIncrementType;
	}

	public String getCompfeeAttachType() {
		return compfeeAttachType;
	}

	public void setCompfeeAttachType(String compfeeAttachType) {
		this.compfeeAttachType = compfeeAttachType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getOriginatorStatCode() {
		return originatorStatCode;
	}

	public void setOriginatorStatCode(String originatorStatCode) {
		this.originatorStatCode = originatorStatCode;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getCustomerField1() {
		return customerField1;
	}

	public void setCustomerField1(String customerField1) {
		this.customerField1 = customerField1;
	}

	public String getCustomerField2() {
		return customerField2;
	}

	public void setCustomerField2(String customerField2) {
		this.customerField2 = customerField2;
	}

	public String getPosentryModeId() {
		return posentryModeId;
	}

	public void setPosentryModeId(String posentryModeId) {
		this.posentryModeId = posentryModeId;
	}

	public String getCnpIndicator() {
		return cnpIndicator;
	}

	public void setCnpIndicator(String cnpIndicator) {
		this.cnpIndicator = cnpIndicator;
	}

	public String getCardholderTraveling() {
		return cardholderTraveling;
	}

	public void setCardholderTraveling(String cardholderTraveling) {
		this.cardholderTraveling = cardholderTraveling;
	}

	public String getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(String dataVersion) {
		this.dataVersion = dataVersion;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getDynamicRuleCode() {
		return dynamicRuleCode;
	}

	public void setDynamicRuleCode(String dynamicRuleCode) {
		this.dynamicRuleCode = dynamicRuleCode;
	}

	public String getMaxFeeFlag() {
		return maxFeeFlag;
	}

	public void setMaxFeeFlag(String maxFeeFlag) {
		this.maxFeeFlag = maxFeeFlag;
	}

	public String getFreeFeeFlag() {
		return freeFeeFlag;
	}

	public void setFreeFeeFlag(String freeFeeFlag) {
		this.freeFeeFlag = freeFeeFlag;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigInteger getAccountId() {
		return accountId;
	}

	public void setAccountId(BigInteger accountId) {
		this.accountId = accountId;
	}

	public BigInteger getPurseId() {
		return purseId;
	}

	public void setPurseId(BigInteger purseId) {
		this.purseId = purseId;
	}

	public String getTopupAccountNumber() {
		return topupAccountNumber;
	}

	public void setTopupAccountNumber(String topupAccountNumber) {
		this.topupAccountNumber = topupAccountNumber;
	}

	public BigInteger getTopupAccountId() {
		return topupAccountId;
	}

	public void setTopupAccountId(BigInteger topupAccountId) {
		this.topupAccountId = topupAccountId;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

	public BigInteger getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigInteger customerId) {
		this.customerId = customerId;
	}

	public String getMarkedStatus() {
		return markedStatus;
	}

	public void setMarkedStatus(String markedStatus) {
		this.markedStatus = markedStatus;
	}

	public String getSpilStoreDbId() {
		return spilStoreDbId;
	}

	public void setSpilStoreDbId(String spilStoreDbId) {
		this.spilStoreDbId = spilStoreDbId;
	}

	public Date getInsTimeStamp() {
		return insTimeStamp;
	}

	public void setInsTimeStamp(Date insTimeStamp) {
		this.insTimeStamp = insTimeStamp;
	}

	public Long getToAccountPurseId() {
		return toAccountPurseId;
	}

	public void setToAccountPurseId(Long toAccountPurseId) {
		this.toAccountPurseId = toAccountPurseId;
	}

	public Long getAccountPurseId() {
		return accountPurseId;
	}

	public void setAccountPurseId(Long accountPurseId) {
		this.accountPurseId = accountPurseId;
	}

	public String getPurseName() {
		return purseName;
	}

	public void setPurseName(String purseName) {
		this.purseName = purseName;
	}

	public Double getPurseTxnAmount() {
		return purseTxnAmount;
	}

	public void setPurseTxnAmount(Double purseTxnAmount) {
		this.purseTxnAmount = purseTxnAmount;
	}

	public String getPurseCurrency() {
		return purseCurrency;
	}

	public void setPurseCurrency(String purseCurrency) {
		this.purseCurrency = purseCurrency;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (transactionSqid != null ? transactionSqid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof TransactionLog)) {
			return false;
		}
		TransactionLog other = (TransactionLog) object;
		if ((this.transactionSqid == null && other.transactionSqid != null)
				|| (this.transactionSqid != null && !this.transactionSqid.equals(other.transactionSqid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.TransactionLog[ transactionSqid=" + transactionSqid + " ]";
	}

	@Override
	public BigDecimal getId() {
		return getTransactionSqid();
	}

	@Override
	public boolean isNew() {
		return !this.update;
	}

	public boolean isUpdate() {
		return this.update;
	}

	public boolean setUpdate(boolean update) {
		this.update = update;
		return this.update;
	}

	public String getStoreDbId() {
		return storeDbId;
	}

	public void setStoreDbId(String storeDbId) {
		this.storeDbId = storeDbId;
	}

	@Transient
	private boolean update;
}
