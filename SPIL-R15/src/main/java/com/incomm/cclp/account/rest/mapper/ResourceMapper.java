package com.incomm.cclp.account.rest.mapper;

import static com.incomm.cclp.account.util.CodeUtil.isNotNullAndEmpty;

import org.springframework.http.HttpHeaders;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.view.AccountPurseView;
import com.incomm.cclp.account.domain.view.PurseView;
import com.incomm.cclp.account.rest.resources.UpdateAccountPurseResponseResource;
import com.incomm.cclp.account.rest.resources.UpdatePurseStatusResponseResource;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.constants.ResponseCodes;

public class ResourceMapper {

	private ResourceMapper() {
		super();
		// do not allow object creation for this utility class.
	}

	public static UpdateAccountPurseResponseResource mapUpdateAccountPurseResponse(AccountPurseView view) {

		TransactionInfo transactionInfo = view.getTransactionInfo();

		return UpdateAccountPurseResponseResource.builder()
			.responseCode(mapResponseCode(view.getResponseCode()))
			.responseMessage(view.getResponseMessage())
			.date(view.getDate())
			.transactionId(view.getTransactionId())
			.spNumber(transactionInfo.getSpNumber())
			.spNumberType(transactionInfo.getSpNumberType()
				.isPresent()
						? transactionInfo.getSpNumberType()
							.get()
							.name()
						: null)
			.purseName(view.getPurseName())
			.accountPurseId(view.getAccountPurseId())
			.transactionAmount(view.getTransactionAmount())
			.authAmount(view.getAuthorizedAmount())
			.availablePurseBalance(view.getAvailablePurseBalance())
			.effectiveDate(DateTimeUtil.toISOString(view.getEffectiveDate()))
			.expiryDate(DateTimeUtil.toISOString(view.getExpiryDate()))
			.currency(view.getCurrency())
			.skuCode(view.getSkuCode())
			.cardStatus(view.getCardStatus())
			.date(transactionInfo.getTransactionDateTime())
			.action(view.getAction())
			.build();

	}

	public static UpdatePurseStatusResponseResource mapUpdatePurseStatusResponse(PurseView view) {

		TransactionInfo transactionInfo = view.getTransactionInfo();

		return UpdatePurseStatusResponseResource.builder()
			.responseCode(mapResponseCode(view.getResponseCode()))
			.responseMessage(view.getResponseMessage())
			.date(view.getDate())
			.transactionId(view.getTransactionId())
			.spNumber(transactionInfo.getSpNumber())
			.spNumberType(transactionInfo.getSpNumberType()
				.isPresent()
						? transactionInfo.getSpNumberType()
							.get()
							.name()
						: null)
			.purseName(view.getPurseName())
			.purseStatus(view.getPurseStatus()
				.name())
			.startDate(DateTimeUtil.toISOString(view.getPurseStartDate()))
			.endDate(DateTimeUtil.toISOString(view.getPurseEndDate()))
			.build();

	}

	public static HttpHeaders map(String headerName, String value) {
		HttpHeaders responseHeaders = new HttpHeaders();

		if (isNotNullAndEmpty(value)) {
			responseHeaders.add(headerName, value);
		}

		return responseHeaders;
	}

	private static String mapResponseCode(String input) {
		return ResponseCodes.SUCCESS.equals(input) ? "0" : input;
	}

}
