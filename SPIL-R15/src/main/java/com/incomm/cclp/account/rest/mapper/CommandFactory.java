package com.incomm.cclp.account.rest.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.application.command.UpdateAccountPurseCommand;
import com.incomm.cclp.account.application.command.UpdatePurseStatusCommand;
import com.incomm.cclp.account.application.command.UpdateRolloverAccountPurseCommand;
import com.incomm.cclp.account.application.service.AccountApplicationService;
import com.incomm.cclp.account.domain.event.TransactionFailedEvent;
import com.incomm.cclp.account.domain.exception.DomainException;
import com.incomm.cclp.account.domain.model.MessageType;
import com.incomm.cclp.account.domain.model.TransactionType;
import com.incomm.cclp.account.rest.resources.UpdateAccountPurseRequestResource;
import com.incomm.cclp.account.rest.resources.UpdatePurseStatusRequestResource;
import com.incomm.cclp.domain.TransactionLog;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CommandFactory {

	@Autowired
	AccountApplicationService applicationService;

	public UpdateAccountPurseCommand getUpdateAccountPurseCommand(String correlationId, String channelName, String userName,
			UpdateAccountPurseRequestResource requestResource) {

		try {
			return createCommand(correlationId, channelName, userName, requestResource);
		} catch (DomainException e) {
			log.info("Exception occured while creating the UpdateAccountPurseCommand object for correlation id:{}, message:{}",
					correlationId, e.getMessage());

			TransactionLog transactionLog = FailedRequestMapper.map(correlationId, channelName, userName, requestResource, e);
			applicationService.processTransactionFailedEvent(TransactionFailedEvent.from(transactionLog));

			throw e;
		} catch (Exception e) {
			log.info("Exception occured while creating the UpdateAccountPurseCommand object for correlation id:" + correlationId,
					e.getMessage(), e);

			TransactionLog transactionLog = FailedRequestMapper.map(correlationId, channelName, userName, requestResource, e);
			applicationService.processTransactionFailedEvent(TransactionFailedEvent.from(transactionLog));

			throw e;
		}

	}

	public UpdatePurseStatusCommand getUpdatePurseStatusCommand(String correlationId, String channelName, String userName,
			UpdatePurseStatusRequestResource requestResource) {

		try {
			return createCommand(correlationId, channelName, userName, requestResource);
		} catch (DomainException e) {
			log.info("Exception occured while creating the UpdatePurseStatusCommand object for correlation id:" + correlationId,
					e.getMessage());

			TransactionLog transactionLog = FailedRequestMapper.map(correlationId, channelName, userName, requestResource, e);
			applicationService.processTransactionFailedEvent(TransactionFailedEvent.from(transactionLog));

			throw e;
		} catch (Exception e) {
			log.info("Exception occured while creating the UpdatePurseStatusCommand object for correlation id:" + correlationId,
					e.getMessage(), e);

			TransactionLog transactionLog = FailedRequestMapper.map(correlationId, channelName, userName, requestResource, e);
			applicationService.processTransactionFailedEvent(TransactionFailedEvent.from(transactionLog));

			throw e;
		}

	}

	public UpdateRolloverAccountPurseCommand getUpdateRolloverAccountPurseCommand(String correlationId, String channelName, String userName,
			UpdateAccountPurseRequestResource requestResource) {
		try {
			return createCommand1(correlationId, channelName, userName, requestResource);
		} catch (DomainException e) {
			log.info("Exception occured while creating the UpdateAccountPurseCommand object for correlation id:{}, message:{}",
					correlationId, e.getMessage());

			TransactionLog transactionLog = FailedRequestMapper.map(correlationId, channelName, userName, requestResource, e);
			applicationService.processTransactionFailedEvent(TransactionFailedEvent.from(transactionLog));

			throw e;
		} catch (Exception e) {
			log.info("Exception occured while creating the UpdateAccountPurseCommand object for correlation id:" + correlationId,
					e.getMessage(), e);

			TransactionLog transactionLog = FailedRequestMapper.map(correlationId, channelName, userName, requestResource, e);
			applicationService.processTransactionFailedEvent(TransactionFailedEvent.from(transactionLog));

			throw e;
		}
	}

	private static UpdatePurseStatusCommand createCommand(String correlationId, String channelName, String userName,
			UpdatePurseStatusRequestResource resource) {

		TransactionInfo transactionInfo = TransactionInfo.builder()
			.correlationId(correlationId)
			.deliveryChannelType(channelName)
			.messageType(MessageType.ORIGINAL)
			.transactionType(TransactionType.UPDATE_PURSE_STATUS)
			.spNumberTypeString(resource.getSpNumberType())
			.spNumber(resource.getSpNumber())
			.mdmId(resource.getMdmId())
			.upc(resource.getUpc())
			.storeId(resource.getStoreId())
			.terminalId(resource.getTerminalId())
			.userName(userName)
			.build();

		return UpdatePurseStatusCommand.builder()
			.transactionInfo(transactionInfo)
			.purseName(resource.getPurseName())
			.purseStatus(resource.getPurseStatus())
			.startDate(resource.getStartDate())
			.endDate(resource.getEndDate())
			.build();

	}

	private static UpdateAccountPurseCommand createCommand(String correlationId, String channelName, String userName,
			UpdateAccountPurseRequestResource resource) {

		TransactionInfo transactionInfo = TransactionInfo.builder()
			.correlationId(correlationId)
			.deliveryChannelType(channelName)
			.messageType(MessageType.ORIGINAL)
			.spNumberTypeString(resource.getSpNumberType())
			.spNumber(resource.getSpNumber())
			.mdmId(resource.getMdmId())
			.upc(resource.getUpc())
			.storeId(resource.getStoreId())
			.terminalId(resource.getTerminalId())
			.userName(userName)
			.build();

		return UpdateAccountPurseCommand.builder()
			.transactionInfo(transactionInfo)
			.purseName(resource.getPurseName())
			.accountPurseId(resource.getAccountPurseId())
			.effectiveDate(resource.getEffectiveDate())
			.expiryDate(resource.getExpiryDate())
			.transactionAmount(resource.getTransactionAmount())
			.currency(resource.getCurrency())
			.skuCode(resource.getSkuCode())
			.actionType(resource.getAction())
			.build();

	}

	private static UpdateRolloverAccountPurseCommand createCommand1(String correlationId, String channelName, String userName,
			UpdateAccountPurseRequestResource resource) {

		TransactionInfo transactionInfo = TransactionInfo.builder()
			.correlationId(correlationId)
			.deliveryChannelType(channelName)
			.messageType(MessageType.ORIGINAL)
			.spNumberTypeString(resource.getSpNumberType())
			.spNumber(resource.getSpNumber())
			.mdmId(resource.getMdmId())
			.upc(resource.getUpc())
			.storeId(resource.getStoreId())
			.terminalId(resource.getTerminalId())
			.userName(userName)
			.build();

		return UpdateRolloverAccountPurseCommand.builder()
			.transactionInfo(transactionInfo)
			.purseName(resource.getPurseName())
			.accountPurseId(resource.getAccountPurseId())
			.effectiveDate(resource.getEffectiveDate())
			.expiryDate(resource.getExpiryDate())
			.transactionAmount(resource.getTransactionAmount())
			.currency(resource.getCurrency())
			.skuCode(resource.getSkuCode())
			.actionType(resource.getAction())
			.percentageAmount(resource.getPercentageAmount())
			.build();

	}

}
