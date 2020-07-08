/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.repos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.domain.Card;
import com.incomm.cclp.domain.CardInfo;

/**
 *
 * @author skocherla
 */

@Repository
public interface CardRepository extends CrudRepository<Card, String> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set card_status = :cardStatus , old_cardstat=:oldCardStatus, last_txndate=SYSDATE"
			+ " where card_num_hash=:cardNumber and account_id=:accountId", nativeQuery = true)
	public int updateCardStatus(@Param("cardNumber") String cardNumber, @Param("accountId") BigInteger accountId,
			@Param("cardStatus") String cardStatus, @Param("oldCardStatus") String oldCardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set card_status = :cardStatus , old_cardstat=:oldCardStatus, in_use='Y'"
			+ " where card_num_hash=:cardNumber", nativeQuery = true)
	public int updateCardStatus(@Param("cardNumber") String cardNumber, @Param("cardStatus") String cardStatus,
			@Param("oldCardStatus") String oldCardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set card_status=:newStatus , in_use='N' where account_id=:accountId and card_status=:currentStatus", nativeQuery = true)
	public int updateCardStatus(@Param("accountId") BigDecimal accountId, @Param("currentStatus") String currentCardStatus,
			@Param("newStatus") String newStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set date_of_activation=:activationDate, last_txndate = SYSDATE "
			+ " where card_num_hash =:cardNumber", nativeQuery = true)
	public int updateActivationStatus(@Param("cardNumber") String cardNumber, @Param("activationDate") Date activationDate);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set date_of_activation=null,expiry_date=null, last_txndate = SYSDATE, card_status=:cardStatus where card_num_hash =:cardNumber", nativeQuery = true)
	public int updateActivationCardStatus(@Param("cardNumber") String cardNumber, @Param("cardStatus") String cardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set date_of_activation=null,last_txndate = SYSDATE, card_status=:cardStatus where card_num_hash =:cardNumber", nativeQuery = true)
	public int updateActivationDateCardStatus(@Param("cardNumber") String cardNumber, @Param("cardStatus") String cardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set FIRSTTIME_TOPUP=:topUpFlag where card_num_hash=:cardNumber", nativeQuery = true)
	public int updateTopup(@Param("cardNumber") String cardNumber, @Param("topUpFlag") String topUpFlag);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update card set card_status=:newStatus , in_use='Y' where card_num_hash=:cardNumHash", nativeQuery = true)
	public int updateCardStatus(@Param("newStatus") String newStatus, @Param("cardNumHash") String cardNumHash);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_CARD_STATUS_NOT_ACTIVE_CLOSED, nativeQuery = true)
	public int updateCardStatToSuspForNotInactCard(@Param("cardNumHash") String cardHashNum, @Param("cardStatus") String cardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_CURRENT_CARD_STATUS, nativeQuery = true)
	public int updateCurrentCardStatus(@Param("cardNumHash") String cardHashNum, @Param("cardStatus") String cardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_CARD_STATUS_OLD_CARD_STATUS, nativeQuery = true)
	public int updateOldCurrentCardStatus(@Param("cardNumHash") String cardHashNum, @Param("cardStatus") String cardStatus,
			@Param("oldCardStatus") String OldCardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_OLD_CARD_STATUS, nativeQuery = true)
	public int updateOldCardStatus(@Param("cardNumHash") String cardHashNum, @Param("oldCardStatus") String OldCardStatus);

	@Query(nativeQuery = true)
	int getDamagedCardCount(@Param("accountId") BigInteger accountId, @Param("cardStatus") String cardStatus);

	@Query(nativeQuery = true)
	CardInfo getReplacedCardStatus(@Param("accountId") BigInteger accountId, @Param("cardStatus") String cardStatus);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_CARD_DETAILS, nativeQuery = true)
	public int updateCardDetails(@Param("cardNumHash") String cardHashNum, @Param("oldCardStatus") String OldCardStatus,
			@Param("firstTimeTopUp") String firstTimeTopUp);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_CARDSTATUS_TO_INACTIVE, nativeQuery = true)
	public int updateCardToInactiveStatus(String inactiveCard, String cardStatus, String cardNumHash);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_FIRST_LOAD_DATE_TO_NULL, nativeQuery = true)
	public int updateFirstLoadDatetoNull(BigInteger accountId, BigInteger purseId, BigInteger productId);
}
