package com.incomm.cclp.account.rest.mapper;

import static com.incomm.cclp.account.util.CodeUtil.trimToLength;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.model.DeliveryChannelType;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.PurseUpdateActionType;
import com.incomm.cclp.account.rest.resources.UpdateAccountPurseRequestResource;
import com.incomm.cclp.account.rest.resources.UpdatePurseStatusRequestResource;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.domain.TransactionLog;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FailedRequestMapper {

	public static TransactionLog map(String correlationId, String channelName, String userName, UpdateAccountPurseRequestResource resource,
			Exception exception) {

		Optional<DeliveryChannelType> channel = DeliveryChannelType.byName(channelName);
		Optional<PurseUpdateActionType> actionType = PurseUpdateActionType.byAction(resource.getAction());

		String channelString = channel.isPresent() ? channel.get()
			.getChannelCode() : "NA";

		String mdmId = CodeUtil.isNullOrEmpty(resource.getMdmId()) ? "0" : resource.getMdmId();
		String responseCode = exception instanceof DomainException ? "400" : "500";
		String transactionCode = actionType.isPresent() ? actionType.get()
			.getTransctionCode() : "0";
		String transactionDescription = actionType.isPresent() ? actionType.get()
			.getTransactionShortName() : trimToLength(resource.getAction(), 100);

		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yy");
		String dateString = newFormat.format(new Date());

		return TransactionLog.builder()
			.deliveryChannel(channelString)
			.transactionCode(transactionCode)
			.msgType(MessageType.ORIGINAL.getMessageTypeCode())
			.isFinancial("Y")
			.crDrFlag(actionType.isPresent() ? actionType.get()
				.getOperationType()
				.getFlag() : "NA")
			.transactionDesc(transactionDescription)
			.errorMsg(exception.getMessage())
			.responseId(responseCode)
			.transactionStatus("F")
			.terminalId(resource.getTerminalId())
			.rrn(correlationId)
			.transactionAmount(parseDouble(resource.getTransactionAmount()))
			.orgnlTranCurr(trimToLength(resource.getCurrency(), 6))
			.storeId(resource.getStoreId())
			.correlationId(trimToLength(correlationId, 20))
			.mdmId(mdmId)
			.tranCurr(trimToLength(resource.getCurrency(), 6))
			.skuCode(resource.getSkuCode())
			.insTimeStamp(new java.sql.Timestamp(System.currentTimeMillis()))
			.insDate(DateTimeUtil.map(LocalDateTime.now()))
			.userName(userName)
			.build();
	}

	public static TransactionLog map(String correlationId, String channelName, String userName, UpdatePurseStatusRequestResource resource,
			Exception exception) {

		Optional<DeliveryChannelType> channel = DeliveryChannelType.byName(channelName);

		String channelString = channel.isPresent() ? channel.get()
			.getChannelCode() : "NA";

		String mdmId = CodeUtil.isNullOrEmpty(resource.getMdmId()) ? "0" : resource.getMdmId();
		String responseCode = exception instanceof DomainException ? "400" : "500";

		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yy");
		String dateString = newFormat.format(new Date());

		return TransactionLog.builder()
			.deliveryChannel(channelString)
			.transactionCode(UpdatePurseStatusCommand.TRANSACTION_CODE)
			.msgType(MessageType.ORIGINAL.getMessageTypeCode())
			.isFinancial("N")
			.crDrFlag("NA")
			.transactionDesc(UpdatePurseStatusCommand.TRANSACTION_SHORT_CODE)
			.errorMsg(exception.getMessage())
			.responseId(responseCode)
			.transactionStatus("F")
			.terminalId(resource.getTerminalId())
			.rrn(correlationId)
			.storeId(resource.getStoreId())
			.correlationId(trimToLength(correlationId, 20))
			.mdmId(mdmId)
			.insTimeStamp(new java.sql.Timestamp(System.currentTimeMillis()))
			.insDate(DateTimeUtil.map(LocalDateTime.now()))
			.userName(userName)
			.build();
	}

	private static double parseDouble(String value) {
		try {
			return Double.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}

}
