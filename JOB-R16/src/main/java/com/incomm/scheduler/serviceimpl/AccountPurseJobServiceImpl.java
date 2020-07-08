package com.incomm.scheduler.serviceimpl;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.logging.log4j.core.util.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.CardStatusType;
import com.incomm.scheduler.constants.FSAPIConstants;
import com.incomm.scheduler.constants.PurseType;
import com.incomm.scheduler.constants.PurseUpdateActionType;
import com.incomm.scheduler.constants.ResponseMessages;
import com.incomm.scheduler.constants.TransactionType;
import com.incomm.scheduler.dao.BatchLoadAccountPurseDAO;
import com.incomm.scheduler.dao.PartnerDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.BatchLoadAccountPurse;
import com.incomm.scheduler.model.PartnerProductInfo;
import com.incomm.scheduler.service.AccountPurseJobService;
import com.incomm.scheduler.service.ProductService;
import com.incomm.scheduler.service.command.CreatePurseLoadJobCommand;
import com.incomm.scheduler.utils.DateTimeUtil;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.Util;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountPurseJobServiceImpl implements AccountPurseJobService {

	@Autowired
	private PartnerDAO partnerDAO;

	@Autowired
	private BatchLoadAccountPurseDAO batchLoadAccountPurseDAO;
	
	@Autowired
	private ProductService productService;

	@Override
	public long createPurseLoadJobRequest(CreatePurseLoadJobCommand command) {

		Optional<PartnerProductInfo> partnerProductInfo = partnerDAO.getPartnerProductInfo(command.getPartnerId(),
				command.getProductId(), command.getPurseName());

		if (!partnerProductInfo.isPresent()) {
			throw new ServiceException("Unable to locate partner product configuration for given parameters.",
					ResponseMessages.BAD_REQUEST);
		}

		if (!partnerProductInfo.get().getMdmId().equals(command.getMdmId())) {
			throw new ServiceException("Invalid MDM Id.", ResponseMessages.BAD_REQUEST);
		}

		Optional<PurseType> pursetype = PurseType.byPurseTypeId(partnerProductInfo.get().getPurseTypeId());

		if (!pursetype.isPresent()) {
			throw new ServiceException("Invalid purse Name.", ResponseMessages.BAD_REQUEST);
		}

		if (pursetype.get() == PurseType.SKU && isEmptyOrNull(command.getSkuCode())) {
			throw new ServiceException("Invalid skuCode.", ResponseMessages.BAD_REQUEST);
		}

		long nextBatchId = this.batchLoadAccountPurseDAO.getNextPurseLoadBatchId();

		BatchLoadAccountPurse batchLoadAccountPurse = BatchLoadAccountPurse.builder().batchId(nextBatchId)
				.partnerId(command.getPartnerId()).productId(command.getProductId()).purseName(command.getPurseName())
				.mdmId(command.getMdmId()).transactionAmount(command.getTransactionAmount())
				.effectiveDate(map(command.getEffectiveDate())).expiryDate(map(command.getExpiryDate()))
				.skuCode(command.getSkuCode()).actionType(command.getActionType().getAction())
				.overrideCardStatus(CollectionUtils.isEmpty(command.getOverrideCardStatus()) ? null
						: String.join(",", command.getOverrideCardStatus()))
				.status("NEW").insertedDate(LocalDateTime.now()).lastUpdatedDate(LocalDateTime.now()).percentageAmount(new BigDecimal(0)).build();

		int value = batchLoadAccountPurseDAO.addBatchLoadAccountPurseRequest(batchLoadAccountPurse);
		if (value == 0) {
			throw new ServiceException("Unable to insert record in the database.",
					ResponseMessages.GENERIC_ERR_MESSAGE);
		}
		return nextBatchId;
	}

	private LocalDateTime map(ZonedDateTime zonedDatetime) {
		ZoneId zoneId = ZoneId.systemDefault();
		return zonedDatetime == null ? null : zonedDatetime.withZoneSameInstant(zoneId).toLocalDateTime();
	}

	private boolean isEmptyOrNull(String value) {
		return value == null || value.trim().isEmpty();
	}

	@Override
	public void autoPurseJob() {
		List<PartnerProductInfo> productPurseInfo = partnerDAO.getProductPurseInfo();
		if (!CollectionUtils.isEmpty(productPurseInfo)) {
			productPurseInfo.stream().forEach(p -> {
				Map<String, Map<String, Object>> productPurse = Util.convertJsonToHashMap(p.getProductAttributes());
				Map<String, Object> productPurseAttributes = productPurse.get(JobConstants.PURSE);
				LocalDate currentDate = LocalDate.now();
				if (JobConstants.ENABLE.equals(productPurseAttributes.get("autoTopupEnable"))) {		
					autoTopupRequest(p, productPurse, productPurseAttributes, currentDate);
				}
				if (JobConstants.ENABLE.equals(productPurseAttributes.get("rolloverEnable"))) {
					autoRolloverRequest(p, productPurseAttributes);
				}

				if (JobConstants.ENABLE.equals(productPurseAttributes.get("autoReloadEnable"))) {		
					autoReloadRequest(p, productPurse, productPurseAttributes, currentDate);

				}

			});

		}
	}

	private void autoReloadRequest(PartnerProductInfo p, Map<String, Map<String, Object>> productPurse,
			Map<String, Object> productPurseAttributes, LocalDate currentDate) {
		LocalDate autoReloadNextRun = DateTimeUtil
				.getMMDDYYYY(String.valueOf(productPurseAttributes.get("autoReloadNextRun")));
		if (currentDate.isEqual(autoReloadNextRun)) {
			long nextBatchId = this.batchLoadAccountPurseDAO.getNextPurseLoadBatchId();
			Optional<PurseUpdateActionType> actionType = PurseUpdateActionType.byAction("autoReload");
			if (!actionType.isPresent()) {
				throw new ServiceException("Invalid Reload Action.", ResponseMessages.BAD_REQUEST);
			}
			LocalDate lastReloadRunDate = currentDate;
			LocalDate nextReloadRunDate = nextRunDateCalc(lastReloadRunDate,
					String.valueOf(productPurseAttributes.get("autoReloadCron")));
			String overrideCardStatus = getSupportedCardStatusByTransactionType(p.getProductId(), p.getPurseId(), TransactionType.AUTORELOAD_ACCOUNT_PURSE);
			
			if (!Util.isEmpty(overrideCardStatus)) {
				log.debug("Transaction based on Card status: {{}} are enabled for this procuct : {}", overrideCardStatus, p.getProductId());
			BatchLoadAccountPurse batchLoadAccountPurse = BatchLoadAccountPurse.builder().batchId(nextBatchId)
					.partnerId(p.getPartnerId()).productId(p.getProductId()).purseName(p.getPurseName()).mdmId(p.getMdmId())
					.transactionAmount(new BigDecimal(String.valueOf(productPurseAttributes.get("autoReloadAmount"))))
					.actionType(actionType.get().getAction()).status("NEW").insertedDate(LocalDateTime.now())
					.lastUpdatedDate(LocalDateTime.now()).nextRunDate(nextReloadRunDate).percentageAmount(new BigDecimal(0))
					.overrideCardStatus(overrideCardStatus).build();
	
			int value = batchLoadAccountPurseDAO.addBatchLoadAccountPurseRequest(batchLoadAccountPurse);
			if (value == 0) {
				throw new ServiceException("Unable to insert Auto Reload record in the database.",
						ResponseMessages.GENERIC_ERR_MESSAGE);
			}
			} else {
				log.info("Transaction based on Card status not enabled for this procuct : {}", p.getProductId());
			}
			productPurseAttributes.put("autoReloadNextRun", DateTimeUtil.convertLocalDateToString(nextReloadRunDate));
			productPurseAttributes.put("autoReloadLastRun", DateTimeUtil.convertLocalDateToString(lastReloadRunDate));
			productPurse.put(JobConstants.PURSE, productPurseAttributes);
			String updateAttributes = Util.convertHashMapToJson(productPurse);
			partnerDAO.updatePurseAttributes(updateAttributes, p.getProductId(), p.getPurseId());
		}
	
		}

	private void autoRolloverRequest(PartnerProductInfo p, Map<String, Object> productPurseAttributes) {
		long nextBatchId = this.batchLoadAccountPurseDAO.getNextPurseLoadBatchId();
		Optional<PurseUpdateActionType> actionType = PurseUpdateActionType.byAction("autoRollover");
		if (!actionType.isPresent()) {
			throw new ServiceException("Invalid Action.", ResponseMessages.BAD_REQUEST);
		}
		String overrideCardStatus = getSupportedCardStatusByTransactionType(p.getProductId(), p.getPurseId(), TransactionType.AUTO_ROLLOVER_PURSE);
		
		if(!Util.isEmpty(overrideCardStatus)) {
			log.debug("Transaction based on Card status: {{}} are enabled for this procuct : {}", overrideCardStatus, p.getProductId());
		BatchLoadAccountPurse batchLoadAccountPurse = BatchLoadAccountPurse.builder().batchId(nextBatchId)
				.partnerId(p.getPartnerId()).productId(p.getProductId()).purseName(p.getPurseName()).mdmId(p.getMdmId())
				.transactionAmount(productPurseAttributes.containsKey("rolloverMaxAmount")
						? new BigDecimal(String.valueOf(productPurseAttributes.get("rolloverMaxAmount")))
						: new BigDecimal(0))
				.actionType(actionType.get().getAction()).status("NEW").insertedDate(LocalDateTime.now())
				.lastUpdatedDate(LocalDateTime.now())
				.percentageAmount(productPurseAttributes.containsKey("rolloverPercent")
						? new BigDecimal(String.valueOf(productPurseAttributes.get("rolloverPercent")))
						: new BigDecimal(0)).overrideCardStatus(overrideCardStatus)
				.build();

		int value = batchLoadAccountPurseDAO.addBatchLoadAccountPurseRequest(batchLoadAccountPurse);
		if (value == 0) {
			throw new ServiceException("Unable to insert RollOver record in the database.",
					ResponseMessages.GENERIC_ERR_MESSAGE);
		}
		} else {
			log.info("Transaction based on Card status not enabled for this procuct : {}", p.getProductId());
		}
	}

	private LocalDateTime effectiveExpiryDateCalc(LocalDate runDate, String defaultTime, String autoTopupTimeZone) {
		String formatDateTime = DateTimeUtil.convertLocalDateToString(runDate);
		String dateTime = formatDateTime.concat(" ").concat(defaultTime);
		String expiryDateTime = DateTimeUtil.getConvertedTimeZone(autoTopupTimeZone, dateTime);
		return DateTimeUtil.getMMDDYYYYHHMMSS(expiryDateTime);
	}

	private LocalDate nextRunDateCalc(LocalDate lastRunDate, String cronExp) {
		CronExpression exp = null;
		String strDate = null;
		try {
			exp = new CronExpression(cronExp);
			Date date = Date.from(lastRunDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date nextDate = exp.getNextValidTimeAfter(date);
			SimpleDateFormat formatter = new SimpleDateFormat(JobConstants.DATE_FORMAT);
			strDate = formatter.format(nextDate);

		} catch (ParseException e) {
			log.error("Next run Date parsing Exception" + e);
		}

		return DateTimeUtil.getMMDDYYYY(strDate);
	}

	public void autoTopupRequest(PartnerProductInfo p, Map<String, Map<String, Object>> productPurse,
			Map<String, Object> productPurseAttributes, LocalDate currentDate) {
		LocalDate autoTopupNextRun = DateTimeUtil
				.getMMDDYYYY(String.valueOf(productPurseAttributes.get("autoTopupNextRun")));
		if (currentDate.isEqual(autoTopupNextRun)) {
			long nextBatchId = this.batchLoadAccountPurseDAO.getNextPurseLoadBatchId();
			Optional<PurseUpdateActionType> actionType = PurseUpdateActionType.byAction("autoTopup");
			if (!actionType.isPresent()) {
				throw new ServiceException("Invalid Action.", ResponseMessages.BAD_REQUEST);
			}
			LocalDate lastRunDate = currentDate.plusDays(p.getPreemptDays());
			LocalDate nextRunDate = nextRunDateCalc(lastRunDate,
					String.valueOf(productPurseAttributes.get("autoTopupCron")));
			LocalDateTime effectiveDate = effectiveExpiryDateCalc(lastRunDate, "00:00:00",
					String.valueOf(productPurseAttributes.get("autoTopupValidityTimeZone")));
			LocalDateTime expiryDate = effectiveExpiryDateCalc(nextRunDate, "23:59:59",
					String.valueOf(productPurseAttributes.get("autoTopupValidityTimeZone")));
			
			String overrideCardStatus = getSupportedCardStatusByTransactionType(p.getProductId(), p.getPurseId(), TransactionType.AUTO_TOPUP_PURSE);		
			
			if (!Util.isEmpty(overrideCardStatus)) {
				log.debug("Transaction based on Card status: {{}} are enabled for this procuct : {}", overrideCardStatus, p.getProductId());
				
			BatchLoadAccountPurse batchLoadAccountPurse = BatchLoadAccountPurse.builder().batchId(nextBatchId)
					.partnerId(p.getPartnerId()).productId(p.getProductId()).purseName(p.getPurseName()).mdmId(p.getMdmId())
					.transactionAmount(new BigDecimal(String.valueOf(productPurseAttributes.get("autoTopupAmount"))))
					.effectiveDate(effectiveDate).expiryDate(expiryDate).actionType(actionType.get().getAction())
					.status("NEW").insertedDate(LocalDateTime.now()).lastUpdatedDate(LocalDateTime.now())
					.nextRunDate(nextRunDate).preemptDays(p.getPreemptDays()).percentageAmount(new BigDecimal(0))
					.overrideCardStatus(overrideCardStatus).build();
	
			int value = batchLoadAccountPurseDAO.addBatchLoadAccountPurseRequest(batchLoadAccountPurse);
			if (value == 0) {
				throw new ServiceException("Unable to insert Auto Topup record in the database.",
						ResponseMessages.GENERIC_ERR_MESSAGE);
			}
			} else {
				log.info("Transaction based on Card status not enabled for this procuct : {}", p.getProductId());
			}
			productPurseAttributes.put("autoTopupNextRun", DateTimeUtil.convertLocalDateToString(nextRunDate));
			productPurseAttributes.put("autoTopupLastRun", DateTimeUtil.convertLocalDateToString(lastRunDate));
			productPurse.put(JobConstants.PURSE, productPurseAttributes);
			String updateAttributes = Util.convertHashMapToJson(productPurse);
			partnerDAO.updatePurseAttributes(updateAttributes, p.getProductId(), p.getPurseId());
		}	
	}
	
	private String getSupportedCardStatusByTransactionType(Long productId, Long purseId, TransactionType transactionType) {
		log.debug(CCLPConstants.ENTER);
		
		StringJoiner sj = new StringJoiner(",");
		
		Map<String, Map<String, Object>> productAttributes = productService.getProductAttributes(productId.toString(), purseId.toString());
		
		if (!CollectionUtils.isEmpty(productAttributes)) {
			for (CardStatusType type : CardStatusType.values()) {
				String attributeName = String.join("_", FSAPIConstants.DELIVERY_CHANNEL_HOST,
						transactionType.getTransactionShortName(), type.getStatusDescription());
				
				if (!Objects.isNull(Util.getProductAttributeValue(productAttributes, "Card Status", attributeName))) {
					sj.add(type.getStatusCode());
				}
			}
		}
		
		log.debug(CCLPConstants.EXIT);
		
		return sj.toString();
	}

}
