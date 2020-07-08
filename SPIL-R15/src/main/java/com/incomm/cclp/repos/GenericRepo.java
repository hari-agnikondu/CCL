/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.repos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.domain.Account;
import com.incomm.cclp.domain.AccountPurse;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.domain.AuthCheck;
import com.incomm.cclp.domain.CardInfo;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.domain.TransactionLogInfo;
import com.incomm.cclp.dto.StatementLog;
import com.incomm.cclp.exception.ServiceException;

/**
 *
 * @author skocherla
 */

@Repository
public class GenericRepo {

	@Autowired
	private CardRepository cardRepo;

	@Autowired
	private AccountPurseRepo accountPurseRepo;

	@Autowired
	private AccountPurseUsageRepo accountPurseUsageRepo;

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private AuthCheckRepository authRepo;

	@Autowired
	private TransactionLogRepository transactionLogRepo;

	@Autowired
	private LocationInventoryRepo locationInventoryRepo;

	@Autowired
	private StatementsLogRepository statementsLogRepo;

	@Autowired
	private RedemptionLockRepository redemptionLockRepository;

	@Autowired
	private EntityManager em;

	private static final Logger logger = LogManager.getLogger(GenericRepo.class);

	public static final String QUERY_STR = "SELECT nvl(available_balance,0) as avlbl, "
			+ " nvl(ledger_balance,0) as ledbl, b.purse_id as pid " + " FROM CLP_CONFIGURATION.product_purse a JOIN "
			+ " CLP_TRANSACTIONAL.account_purse b ON a.purse_id = b.purse_id "
			+ " JOIN CLP_TRANSACTIONAL.card c ON b.account_id = c.account_id " + " AND a.is_default = 'Y' AND a.product_id = :productId "
			+ " AND b.account_id = :accountId AND c.card_num_hash = :cardNumber";

	public static final String INSERT_STATEMENT_LOG = "INSERT INTO statements_log ("
			+ "	card_num_hash,card_num_encr,opening_balance,closing_balance,transaction_amount,credit_debit_flag,transaction_narration,last_upd_date,ins_date,rrn,auth_id,"
			+ "	transaction_date,transaction_time,fee_flag,delivery_channel,transaction_code,account_id,to_account_id,merchant_name,merchant_city,merchant_state,"
			+ "	card_last4digit,product_id,record_seq,purse_id,to_purse_id,transaction_sqid,business_date,store_id" + ") VALUES ("
			+ "        :cardNumHash,:cardNumEncr,:openingBal,:closingBal,:txnAmt,:crDbFlag,:txnDesc,:lastUpdDate,:insDate,:rrn,:authId,"
			+ ":txnDate,:txnTime,:feeFlag,:channel,:txnCode,:accountId,:toAccountid,:merchantName,:merchantCity,:merchantState,"
			+ ":card4Digit,:productId,:recordSqId,:purseId,:toPurseId,:txnSqId,:businessDate,:storeId" + ")";

	public AccountPurseInfo getAccountInfo(BigInteger productId, BigInteger accountId, String cardNumber) {
		try {
			logger.debug("productId: {},accountId: {}, cardNumber: {}", productId, accountId, cardNumber);
			Query query = em.createNativeQuery(QUERY_STR, "AccountPurseInfoResult");
			query.setParameter("productId", productId);
			query.setParameter("accountId", accountId);
			query.setParameter("cardNumber", cardNumber);
			List<AccountPurseInfo> ac = query.getResultList();
			if (ac.get(0) == null) {
				logger.error("NO DATA FOUND");
				throw new ServiceException(SpilExceptionMessages.ERROR_NO_DATA_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
			}
			return ac.get(0);
		} catch (NoResultException nre) {
			logger.error("No Result found", nre.getMessage(), nre);
			throw new ServiceException(SpilExceptionMessages.ERROR_NO_DATA_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public Optional<Account> getAccountByAccountId(BigDecimal accountId) {
		return accountRepo.findById(accountId);
	}

	public int insertStatementsLog(StatementsLog stmtlog) {
		try {
			Query query = em.createNativeQuery(INSERT_STATEMENT_LOG);
			query.setParameter("cardNumHash", stmtlog.getCardNumHash());
			query.setParameter("cardNumEncr", stmtlog.getCardNumEncr());
			query.setParameter("openingBal", stmtlog.getOpeningBalance());
			query.setParameter("closingBal", stmtlog.getClosingBalance());
			query.setParameter("txnAmt", stmtlog.getTransactionAmount());
			query.setParameter("crDbFlag", stmtlog.getCreditDebitFlag());
			query.setParameter("txnDesc", stmtlog.getTransactionNarration());
			query.setParameter("lastUpdDate", stmtlog.getLastUpdDate());
			query.setParameter("insDate", stmtlog.getInsDate());
			query.setParameter("rrn", stmtlog.getRrn());
			query.setParameter("authId", stmtlog.getAuthId());
			query.setParameter("txnDate", stmtlog.getTransactionDate());
			query.setParameter("txnTime", stmtlog.getTransactionTime());
			query.setParameter("feeFlag", stmtlog.getFeeFlag());
			query.setParameter("channel", stmtlog.getDeliveryChannel());
			query.setParameter("txnCode", stmtlog.getTransactionCode());
			query.setParameter("accountId", stmtlog.getAccountId());
			query.setParameter("toAccountid", stmtlog.getToAccountId());
			query.setParameter("merchantName", stmtlog.getMerchantName());
			query.setParameter("merchantCity", stmtlog.getMerchantCity());
			query.setParameter("merchantState", stmtlog.getMerchantState());
			query.setParameter("card4Digit", stmtlog.getCardLast4digit());
			query.setParameter("productId", stmtlog.getProductId());
			query.setParameter("recordSqId", stmtlog.getStatementsLogPK()
				.getRecordSeq());
			query.setParameter("purseId", stmtlog.getPurseId());
			query.setParameter("toPurseId", stmtlog.getToPurseId());
			query.setParameter("txnSqId", stmtlog.getStatementsLogPK()
				.getTransactionSqid());
			query.setParameter("businessDate", stmtlog.getBusinessDate());
			query.setParameter("storeId", stmtlog.getStoreId());
			query.executeUpdate();
			return 1;
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_STATEMENT_LOG_INSERT, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_STATEMENT_LOG_INSERT, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int getDamagedCardCount(BigInteger accountId, String cardStatus) {
		try {
			logger.debug("accountId: {}, cardStatus: {}", accountId, cardStatus);
			return cardRepo.getDamagedCardCount(accountId, cardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_DAMAGE_CARD_COUNT_GET, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_DAMAGE_CARD_COUNT_GET, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateOldCurrentCardStatus(String cardHashNum, String cardStatus, String oldCardStatus) {
		try {
			logger.debug("cardHashNum: {},cardStatus: {}, OldCardStatus: {}", cardHashNum, cardStatus, oldCardStatus);
			return cardRepo.updateOldCurrentCardStatus(cardHashNum, cardStatus, oldCardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateOldCardStatus(String cardHashNum, String oldCardStatus) {
		try {
			logger.debug("cardHashNum: {}, OldCardStatus: {}", cardHashNum, oldCardStatus);
			return cardRepo.updateOldCardStatus(cardHashNum, oldCardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_OLD_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_OLD_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateCurrentCardStatus(String cardHashNum, String cardStatus) {
		try {
			logger.debug("cardHashNum: {}, cardStatus: {}", cardHashNum, cardStatus);
			return cardRepo.updateCurrentCardStatus(cardHashNum, cardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateCardStatToSuspForNotInactCard(String cardHashNum, String cardStatus) {
		try {
			logger.debug("cardHashNum: {}, cardStatus: {}", cardHashNum, cardStatus);
			return cardRepo.updateCardStatToSuspForNotInactCard(cardHashNum, cardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateCardStatus(String newStatus, String cardHashNum) {
		try {
			logger.debug("newStatus: {}, cardHashNum: {}", newStatus, cardHashNum);
			return cardRepo.updateCardStatus(newStatus, cardHashNum);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateTopup(String cardNumber, String topUpFlag) {
		try {
			logger.debug("cardNumber: {}, topUpFlag: {}", cardNumber, topUpFlag);
			return cardRepo.updateTopup(cardNumber, topUpFlag);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_TOPUP_FLAG, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_TOPUP_FLAG, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateActivationStatus(String cardNumber, Date activationDate) {
		try {
			logger.debug("cardNumber: {}, activationDate: {}", cardNumber, activationDate);
			return cardRepo.updateActivationStatus(cardNumber, activationDate);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateActivationCardStatus(String cardNumber, java.sql.Date activationDate, String cardStatus) {
		try {
			logger.debug("cardNumber: {}, activationDate: {}, cardStatus: {}", cardNumber, activationDate, cardStatus);
			return cardRepo.updateActivationCardStatus(cardNumber, cardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateActivationDateCardStatus(String cardNumber, String cardStatus) {
		try {
			logger.debug("cardNumber: {}, cardStatus: {}", cardNumber, cardStatus);
			return cardRepo.updateActivationDateCardStatus(cardNumber, cardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateCardStatus(BigDecimal accountId, String cardStatus, String oldCardStatus) {
		try {
			logger.debug("accountId: {}, cardStatus: {}, oldCardStatus: {}", accountId, cardStatus, oldCardStatus);
			return cardRepo.updateCardStatus(accountId, cardStatus, oldCardStatus);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_REPLACED_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_REPLACED_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateFirstLoadDate(long accountPurseId, java.sql.Date date) {
		try {
			return accountPurseRepo.updateFirstLoadDate(accountPurseId, date);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_LOAD_DATE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_LOAD_DATE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateFirstLoadDatetoNull(BigInteger accountId) {
		try {
			return accountPurseRepo.updateFirstLoadDatetoNull(accountId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_LOAD_DATE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_LOAD_DATE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateBalances(BigInteger accountId, BigInteger purseId, double closingLegderBalance, double closingAvailBalance) {
		try {
			return accountPurseRepo.updateBalances(accountId, purseId, closingLegderBalance, closingAvailBalance);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_BALANCE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public AccountPurseInfo getAccountPurse(BigInteger productId, BigInteger accountId, String currencyCode, String cardNumHash) {
		try {
			return accountPurseRepo.getAccountPurse(productId, accountId, currencyCode, cardNumHash);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_NO_DATA_AVAILABLE_BALANCE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_NO_DATA_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateInitialLoadBalance(BigInteger accountId, Double initialLoadAmt, Double newInitialLoadAmt) {
		try {
			logger.debug("accountId: {}, initialLoadAmt: {}, newInitialLoadAmt: {}", accountId, initialLoadAmt, newInitialLoadAmt);
			return accountRepo.updateInitialLoadBalance(accountId, initialLoadAmt, newInitialLoadAmt);

		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_INITIAL_LOAD, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_INITIAL_LOAD, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateInitialLoadBalance(BigInteger accountId) {
		try {
			return accountRepo.updateInitialLoadBalance(accountId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_INITIAL_LOAD, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_INITIAL_LOAD, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public List<AuthCheck> findAuthChecks(String txnCode, String channel, String msgType) {
		try {
			logger.debug("txnCode: {}, channel: {}, msgType: {}", txnCode, channel, msgType);
			return authRepo.findAuthChecks(txnCode, channel, msgType);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_AUTHCHECK, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_AUTHCHECK, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public CardInfo getReplacedCardStatus(BigInteger accountId, String cardStatus) {
		try {
			logger.debug("accountId: {}, cardStatus: {},", accountId, cardStatus);
			CardInfo cardinfo = cardRepo.getReplacedCardStatus(accountId, cardStatus);
			if (cardinfo == null) {
				return new CardInfo();
			}
			return cardinfo;
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_REPLACED_CARDSTATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_REPLACED_CARDSTATUS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public TransactionLogInfo getLastSuccessTxn(String deliveryChannel, String cardNumber, String txnCode, String responseId,
			String msgType, String rrn, String txnDate) {
		try {
			TransactionLogInfo txnLogInfo = transactionLogRepo.getLastSuccessTxn(deliveryChannel, cardNumber, txnCode, responseId, msgType,
					rrn, txnDate);
			if (txnLogInfo == null) {
				logger.error(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND);
				throw new ServiceException(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND,
						ResponseCodes.ORIGINAL_TRANSACTION_NOT_FOUND);
			}
			return txnLogInfo;
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public List<TransactionLogInfo> getLastSuccessfulTransactions(String deliveryChannel, String cardNumber, String txnCode,
			String responseId, String msgType, String rrn, String txnDate) {
		try {
			List<TransactionLogInfo> txnLogInfo = transactionLogRepo.getLastSuccessfulTransactions(deliveryChannel, cardNumber, txnCode,
					responseId, msgType, rrn, txnDate);
			if (txnLogInfo == null) {
				logger.error(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND);
				throw new ServiceException(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND,
						ResponseCodes.ORIGINAL_TRANSACTION_NOT_FOUND);
			}
			return txnLogInfo;
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public List<StatementsLogInfo> getLastFeeTransactions(String rrn, String creditDebitFlag, String txnDate, String txnCode) {
		try {
			logger.debug("rrn: {}, cr_db_flg: {}, txnDate: {}", rrn, creditDebitFlag, txnDate);
			return statementsLogRepo.getLastFeeTransactions(rrn, creditDebitFlag, txnDate, txnCode);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_FEE_TXN, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_FEE_TXN, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public List<StatementLog> getAllStatementLogs(String rrn, String txnDate, String txnCode) {
		try {
			logger.debug("rrn: {}, txnDate: {}", rrn, txnDate);
			return statementsLogRepo.getAllStatementLogs(rrn, txnDate, txnCode);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_FEE_TXN, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_FEE_TXN, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public List<RedemptionLockEntity> getRedemptionLocks(String cardNumberHash, String lockFlag) {
		try {
			logger.debug("cardNumberHash: {}, lockFlag:{}", cardNumberHash, lockFlag);
			return redemptionLockRepository.getRedemptionLocks(cardNumberHash, lockFlag);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GETTING_REDEMPTION_LOCKS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GETTING_REDEMPTION_LOCKS, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateInventory(BigInteger productId, String merchantId, String locationId) {
		try {
			logger.debug("productId: {}, merchantId: {}, locationId: {}", productId, merchantId, locationId);
			return locationInventoryRepo.updateInventory(productId, merchantId, locationId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATING_INVENTORY, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATING_INVENTORY, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int removeFromInventory(BigInteger productId, String merchantId, String locationId) {
		try {
			logger.debug("productId: {}, merchantId: {}, locationId: {}", productId, merchantId, locationId);
			return locationInventoryRepo.removeFromInventory(productId, merchantId, locationId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATING_INVENTORY, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATING_INVENTORY, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public Map<String, String> getLocationMerchanntID(String cardNumHash) {
		try {
			logger.debug("cardNumHash: {}", cardNumHash);
			return locationInventoryRepo.getLocationMerchanntID(cardNumHash);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_MERCHANT_LOCATION_ID, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_MERCHANT_LOCATION_ID, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateTransactionReversalFlag(String cardNumHash, String txnCode, String rrn, String reversalFlag) {
		try {
			logger.debug("cardNumHash: {}, txnCode: {}, rrn: {}", cardNumHash, txnCode, rrn);
			return transactionLogRepo.updateTransactionReversalFlag(cardNumHash, txnCode, rrn, reversalFlag);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_REVERSAL_FLAG, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_REVERSAL_FLAG, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateLastTxnDate(BigInteger accountId, BigInteger purseId) {
		try {
			return accountPurseUsageRepo.updateLastTxnDate(accountId, purseId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_LASTTXN_DATE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_LASTTXN_DATE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public List<AccountPurse> getAccountPurseByPurseId(BigInteger accountId, BigInteger productId, BigInteger purseId) {
		try {
			return accountPurseRepo.getAccountPurseByPurseId(accountId, productId, purseId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public AccountPurse getAccountPurseByAccountPurseId(BigInteger accountPurseId) {
		try {
			return accountPurseRepo.getAccountPurseByAccountPurseId(accountPurseId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateBalancesByAccountPurseId(long accountPurseId, double closingLegderBalance, double closingAvailBalance) {
		try {
			return accountPurseRepo.updateBalancesByAccountPurseId(accountPurseId, closingLegderBalance, closingAvailBalance);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_BALANCE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateBalanceExpDate(long accountPurseId, double closingLegderBalance, double closingAvailBalance) {
		try {
			return accountPurseRepo.updateBalanceExpDate(accountPurseId, closingLegderBalance, closingAvailBalance);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_BALANCE, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateCardDetails(String cardHashNum, String OldCardStatus, String firstTimeTopUp) {
		try {
			logger.debug("cardHashNum: {}", cardHashNum);
			return cardRepo.updateCardDetails(cardHashNum, OldCardStatus, firstTimeTopUp);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}

	}

	public int updateFirstLoadDatetoNull(BigInteger accountId, BigInteger purseId, BigInteger productId) {
		try {
			logger.debug("accountId: {}", accountId, "purseId: {}", purseId, "productId: {}", productId);
			return cardRepo.updateFirstLoadDatetoNull(accountId, purseId, productId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_FIRST_LOAD_DATE + "::" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}

	}

	public int updateCardToInactiveStatus(String cardNumHash, String inactiveCard, String cardStatus) {
		try {
			logger.debug("cardNumHash: {}", cardNumHash, "inactiveCard: {}", inactiveCard, "cardStatus: {}", cardStatus);
			return cardRepo.updateCardToInactiveStatus(inactiveCard, cardStatus, cardNumHash);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS + "::" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATE_CARD_STATUS, ResponseCodes.SYSTEM_ERROR);
		}

	}

	public int addToInventory(BigInteger productId, String merchantId, String locationId) {
		try {
			logger.debug("productId: {}, merchantId: {}, locationId: {}", productId, merchantId, locationId);
			return locationInventoryRepo.updateInventory(productId, merchantId, locationId);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_UPDATING_INVENTORY, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_UPDATING_INVENTORY, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public Map<String, Object> getDefaultPurseDetails(long accountId, long purseId) {
		return accountPurseRepo.getDefaultPurseDetails(accountId, purseId);
	}

	public TransactionLogInfo getLastSuccessfulActTxn(String deliveryChannel, String cardNumber, String txnCode, String responseId,
			String msgType, BigInteger purseId) {
		try {

			logger.debug("deliveryChannel: {}, cardNumber: {}, txnCode: {}, responseId: {}, msgType: {}, purseId: {}", deliveryChannel,
					cardNumber, txnCode, responseId, purseId);
			TransactionLogInfo txnLogInfo = transactionLogRepo.getLastSuccessfulActTxn(deliveryChannel, cardNumber, txnCode, responseId,
					msgType, purseId);

			if (txnLogInfo == null) {
				logger.error(SpilExceptionMessages.SPIL_ACTIVATION_NOT_DONE);
				throw new ServiceException(SpilExceptionMessages.SPIL_ACTIVATION_NOT_DONE,
						ResponseCodes.SPIL_ACTIVATION_NOT_DONE_FOR_THIS_CARD);
			}
			logger.info("Last successful activation transaction amount:: {} ", txnLogInfo.getTransactionAmount());
			return txnLogInfo;
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN + "::" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public TransactionLogInfo getLastSuccessPurseTxn(String deliveryChannel, String cardNumber, String txnCode, String responseId,
			String msgType, String rrn, String txnDate, long purseId) {
		try {
			TransactionLogInfo txnLogInfo = transactionLogRepo.getLastSuccessPurseTxn(deliveryChannel, cardNumber, txnCode, responseId,
					msgType, rrn, txnDate, purseId);
			if (txnLogInfo == null) {
				logger.error(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND);
				throw new ServiceException(SpilExceptionMessages.ORIGINAL_TRANSACTION_NOT_FOUND,
						ResponseCodes.ORIGINAL_TRANSACTION_NOT_FOUND);
			}
			return txnLogInfo;
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public TransactionLog getAllLastSuccessPurseTxn(String deliveryChannel, String cardNumber, String txnCode, String responseId,
			String msgType, String rrn, String txnDate, long purseId) {
		try {
			TransactionLog txnLogInfo = transactionLogRepo.getAllLastSuccessPurseTxn(deliveryChannel, cardNumber, txnCode, responseId,
					msgType, rrn, txnDate, purseId);
			return txnLogInfo;
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_GET_LAST_SUCCESS_ACT_TXN, ResponseCodes.SYSTEM_ERROR);
		}
	}
}