package com.incomm.cclp.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dao.SpilSaleActiveCodeRevDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.GenerateCvvService;
import com.incomm.cclp.service.RedemptionDelayService;
import com.incomm.cclp.service.SpilCommonService;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.util.Util;

@Service("com.incomm.cclp.service.impl.SpilSaleActiveCodeRevServiceImpl")
public class SpilSaleActiveCodeRevServiceImpl implements SpilCommonService {
	private static final Logger logger = LogManager.getLogger(SpilSaleActiveCodeServiceImpl.class);

	@Autowired
	private SpilDAO spilDao;

	@Autowired
	SpilSaleActiveCodeRevDAO spilSaleActiveCodeRevDao;

	@Autowired
	TransactionService transactionService;

	@Autowired
	RedemptionDelayService redemptionDelayService;

	@Autowired
	GenerateCvvService generateCvvService;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException {

		String[] response = null;

		try {

			BigDecimal txnAmount = new BigDecimal(valueDto.getValueObj()
				.get(ValueObjectKeys.SPIL_TRAN_AMT));
			if (txnAmount.compareTo(BigDecimal.ZERO) > 0) {
				valueDto.getValueObj()
					.replace(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, ValueObjectKeys.FLAG_YES);
			}
			if (valueDto.getProductAttributes()
				.get(ValueObjectKeys.PRODUCT)
				.get(ValueObjectKeys.SALE_ACTIVE_CODE_RESP_TYPE) != null) {
				String responseType = valueDto.getProductAttributes()
					.get(ValueObjectKeys.PRODUCT)
					.get(ValueObjectKeys.SALE_ACTIVE_CODE_RESP_TYPE)
					.toString();
				logger.debug("SaleActiveCodeResponseType: {}", responseType);
				if (ValueObjectKeys.CVV.equalsIgnoreCase(responseType)) {
					String expDate = Util.formatExpDate(valueDto.getValueObj()
						.get(ValueObjectKeys.CARD_EXPDATE));
					valueDto.getValueObj()
						.replace(ValueObjectKeys.CARD_EXPDATE, expDate);
					String cvv = generateCvvService.generateCVV(valueDto);
					valueDto.getValueObj()
						.replace(ValueObjectKeys.DIGITAL_PIN, cvv);
				}
			}
			response = spilSaleActiveCodeRevDao.invokeSaleActiveCodeRev(valueDto);

			logger.info("Response for spil Sale activation code: responseCode: {},"
					+ "response msg: {},Authorized Id: {}, Balance: {}, Currency Code: {} , Card status: {}, Card Number: {}, Serial Number: {}, PIN: {}",
					response[0], response[1], response[2], response[3], response[4], response[5], Util.getMaskCardNum(response[6]),
					response[7], response[8]);

			if (response[0].equalsIgnoreCase(ResponseCodes.SUCCESS) && response[1].equalsIgnoreCase("OK")) {
				spilDao.updateUsageLimits(valueDto);
				redemptionDelayService.redemptionDelayCheck(valueDto);
				spilDao.updateUsageFee(valueDto);
				if (!Util.isEmpty(valueDto.getValueObj()
					.get(ValueObjectKeys.CURRENT_FEE_ACCRUED)) && !Util.isEmpty(
							valueDto.getValueObj()
								.get(ValueObjectKeys.USAGE_FEE_PERIOD))) {
					spilDao.updateMonthlyFeeCap(valueDto.getValueObj());
				}
			}
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {
			logger.error("Exception occured in SpilSaleActiveCodeRevServiceImpl: {}", e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		return response;

	}
}
