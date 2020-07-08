  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_CANCEL_ORDER_PROCESS" (
      p_order_id_in        IN       VARCHAR2,
      p_partner_id_in      IN       VARCHAR2,
      p_correlation_id_in   IN      VARCHAR2,
      p_resp_code_out      OUT      VARCHAR2,
      p_resp_msg_out       OUT      VARCHAR2,
      p_postback_url_out   OUT      VARCHAR2
   )
   AS
    /*****************************************************************************
	* Modified by          : Sampath Kumar L
    * Modified Date        : 13-FEB-19
    * Modified For         : In Transaction_Log Business date,Account number, proxynumber ,purse_id,...etc added.
    * Reviewer             : 
    * Build Number         :  R05.B3
  ******************************************************************************/
   
      excp_reject_orderprocess   EXCEPTION;
      v_cap_pan_code_encr        card.card_num_encr%TYPE;
      v_tran_date                VARCHAR2 (50);
      v_tran_time                VARCHAR2 (50);
--      l_ccs_tran_code            card.ccs_tran_code%TYPE;
      l_auth_id                  transaction_log.auth_id%TYPE;
      l_err_msg                  transaction_log.error_msg%TYPE;
      l_resp_cde                 transaction_log.response_id%TYPE;
      v_hash_pan                 order_line_item_dtl.card_num_hash%TYPE;
      v_cap_acct_no              card.account_id%TYPE;
      l_drcr_flag                transaction_log.CR_DR_FLAG%TYPE
                                                                      := 'D';
      l_cam_acct_bal             account_purse.available_balance%TYPE;
      l_cam_ledger_bal           account_purse.ledger_balance%TYPE;
      l_cap_prod_code            card.product_id%TYPE;
--      l_cap_card_type            cms_appl_pan.cap_card_type%TYPE;
      l_delivery_channel         transaction_log.delivery_channel%TYPE := '17';
      --l_txn_code                 transaction_log.transaction_code%TYPE         := '091';
	  l_txn_code                 transaction_log.transaction_code%TYPE := '097'; --modified on April 19th 2019 (internal fixes)
      l_timestamp                DATE;
      l_rrn                      transaction_log.rrn%TYPE;
      l_issuerid                 product.issuer_id%type;
      l_partnerid                 product.partner_id%type;

      l_business_time            TRANSACTION_LOG.TRANSACTION_TIME%TYPE;
      l_hashkey_id               TRANSACTION_LOG.HASHKEY_ID%TYPE;
      l_narration                statements_log.TRANSACTION_NARRATION%TYPE;
      l_tran_desc                delivery_channel_transaction.CHANNEL_TRANSACTION_CODE%TYPE;
     	  l_transaction_sqid         transaction_log.transaction_sqid%type;
      --for logging the transaction_log l_business_date,l_purse_id,l_account_number,l_proxy_number
      l_business_date             DATE;
      l_purse_id                  account_purse.purse_id%TYPE;
      l_account_number            account.account_number%TYPE;
      l_proxy_number              card.proxy_number%TYPE;


CURSOR cur_cards (l_order_id VARCHAR2, l_partner_id VARCHAR2)
      IS
         SELECT card_num_hash
           FROM order_line_item_dtl ol, order_details od
          WHERE ol.order_id = p_order_id_in
            AND ol.partner_id = p_partner_id_in
            AND UPPER (od.order_status) = 'CANCELLED'
            AND ol.order_id = od.order_id
            AND ol.partner_id = od.partner_id;
   BEGIN
      p_resp_code_out := 'R0001';
      p_resp_msg_out := 'Success';

      BEGIN
         SELECT channel_transaction_code
           INTO l_tran_desc
           FROM delivery_channel_transaction
          WHERE channel_code = l_delivery_channel
            AND transaction_code = l_txn_code;
      EXCEPTION
       WHEN no_data_found THEN
              p_resp_code_out := 'R0012';
              p_resp_msg_out :=
                  'Error While getting transaction details :'
               || SUBSTR (SQLERRM, 1, 200);
            RAISE excp_reject_orderprocess;
         WHEN OTHERS
         THEN
            p_resp_code_out := 'R0012';
            p_resp_msg_out :=
                  'Error While getting transaction details :'
               || SUBSTR (SQLERRM, 1, 200);
            RAISE excp_reject_orderprocess;
      END;
      OPEN cur_cards (p_order_id_in, p_partner_id_in);

      LOOP
         FETCH cur_cards
          INTO v_hash_pan;

         EXIT WHEN cur_cards%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('v_hash_pan = ' || v_hash_pan);
         BEGIN
            IF v_hash_pan IS NOT NULL
            THEN
               BEGIN
                  UPDATE card
                     SET card_status = '9'
                   WHERE card_num_hash = v_hash_pan;

                  IF SQL%ROWCOUNT = 1
                  THEN
                  DBMS_OUTPUT.PUT_LINE('Count = ' || 1);
--                     BEGIN
--                        SELECT ccs_tran_code
--                          INTO l_ccs_tran_code
--                          FROM cms_card_stat
--                         WHERE ccs_stat_code = '9';

                        BEGIN
                           l_auth_id := LPAD (seq_auth_id.NEXTVAL, 6, '0');
                           l_rrn :=
                                 TO_CHAR (SYSTIMESTAMP, 'yymmddHH24MISS')
                              || seq_passivestatupd_rrn.NEXTVAL;
                            l_business_time := TO_CHAR (SYSDATE, 'hh24miss');
 --                          l_timestamp := SYSTIMESTAMP;
--getting proxy number for logging purpose
                           SELECT c.card_num_encr, c.account_id, c.product_id,p.issuer_id,p.partner_id,c.proxy_number
                             INTO v_cap_pan_code_encr, v_cap_acct_no,
                                  l_cap_prod_code,l_issuerid,l_partnerid,l_proxy_number
                             FROM card c,product p
                             WHERE p.product_id=c.product_id and c.card_num_hash= v_hash_pan;

--added for getting  businessDate

  BEGIN
            clp_transactional.sp_get_cutoff(l_cap_prod_code,l_business_date,p_resp_msg_out,p_resp_code_out);

            IF
                p_resp_code_out <> trans_const.success OR p_resp_msg_out <> 'OK'
            THEN
                RAISE excp_reject_orderprocess;
            END IF;

        EXCEPTION
            WHEN excp_reject_orderprocess THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_code_out := trans_const.system_error;
                p_resp_msg_out := 'ERROR WHILE CHECKING CUTOFF '
                 || substr(sqlerrm,1,200);
                RAISE excp_reject_orderprocess;
        END;
   
   
   







--                           sp_log_cardstat_chnge (1,
--                                                  v_hash_pan,
--                                                  v_cap_pan_code_encr,
--                                                  l_auth_id,
--                                                  l_ccs_tran_code,
--                                                  '',
--                                                  v_tran_date,
--                                                  v_tran_time,
--                                                  l_resp_cde,
--                                                  l_err_msg
--                                                 );

    --getting account number                      
    BEGIN
        select account_number
        INTO l_account_number
        from account
        where account_id=v_cap_acct_no;

        EXCEPTION
            WHEN no_data_found THEN
                p_resp_code_out := trans_const.system_error;
                p_resp_msg_out := 'ERROR WHILE Getting ACCOUNT NUMBER';
            WHEN OTHERS THEN
                p_resp_code_out := trans_const.system_error;
                p_resp_msg_out := 'ERROR WHILE Getting ACCOUNT NUMBER';
            
        END;
                          
                  
		  --getting purseid.        
                           BEGIN
                            DBMS_OUTPUT.PUT_LINE('v_cap_acct_no = ' ||v_cap_acct_no);
                              SELECT available_balance, ledger_balance , purse_id
                                INTO l_cam_acct_bal, l_cam_ledger_bal,l_purse_id
                                FROM ACCOUNT_PURSE
                               WHERE account_id = v_cap_acct_no;

                              UPDATE ACCOUNT_PURSE
                                 SET available_balance = 0,
                                     ledger_balance = 0
                               WHERE account_id = v_cap_acct_no;

                              IF SQL%ROWCOUNT = 1
                              THEN
                                 BEGIN
                                    l_hashkey_id :=
                                       fn_hash
                                          (   l_delivery_channel
                                           || l_txn_code
                                           || fn_dmaps_main
                                                          (v_cap_pan_code_encr)
                                           || l_rrn
                                           || TO_CHAR (l_timestamp,
                                                       'YYYYMMDDHH24MISSFF5'
                                                      )
                                          );
                                    l_narration :=
                                          l_tran_desc
                                       || '/'
                                       || TO_CHAR (SYSDATE, 'yyyymmdd')
                                       || '/'
                                       || l_auth_id;
									   
									   
	 BEGIN
	       l_transaction_sqid:=transaction_id_seq.nextval;   --modified on feb 19th
	 
	 EXCEPTION
	 WHEN OTHERS  THEN
          p_resp_code_out := 'R0012';
          p_resp_msg_out := 'Error While Getting Transaction_sqid :'
                                             || SUBSTR (SQLERRM, 1, 200);
           RAISE excp_reject_orderprocess;
	 END;

                                    IF l_cam_acct_bal >0 THEN
                                    BEGIN
                                       INSERT INTO STATEMENTS_LOG
                                                   (CARD_NUM_HASH,
                                                    OPENING_BALANCE,
                                                    TRANSACTION_AMOUNT,
                                                    CREDIT_DEBIT_FLAG,
                                                    TRANSACTION_DATE,
                                                    CLOSING_BALANCE,
                                                    TRANSACTION_NARRATION,
                                                    CARD_NUM_ENCR,
                                                    RRN, AUTH_ID,
                                                    BUSINESS_DATE,

                                                    FEE_FLAG,
                                                    DELIVERY_CHANNEL,

                                                    TRANSACTION_CODE,
                                                    INS_DATE,

                                                    ACCOUNT_ID,
                                                    CARD_LAST4DIGIT,
                                                    TRANSACTION_TIME,
                                                    PRODUCT_ID,
                                                  transaction_sqid,
												  RECORD_SEQ

                                                   )
                                            VALUES (v_hash_pan,
                                                    0,
                                                    NVL(l_cam_acct_bal,0),
                                                    l_drcr_flag,
                                                    TO_CHAR (SYSDATE,
                                                             'yyyymmdd'
                                                            ),
                                                    0,
                                                    l_narration,
                                                    v_cap_pan_code_encr,
                                                    l_rrn, l_auth_id,
                                                    SYSDATE,

                                                    'N',
                                                    l_delivery_channel,

                                                    l_txn_code,
                                                    SYSDATE,
--                                                    1,
                                                    v_cap_acct_no,
                                                    SUBSTR
                                                       (fn_dmaps_main
                                                           (v_cap_pan_code_encr
                                                           ),
                                                        -4
                                                       ),
                                                    '00',
                                                    l_cap_prod_code,
                                                 l_transaction_sqid,
												 RECORD_ID_SEQ.NEXTVAL  
												 );
                                    EXCEPTION
                                       WHEN OTHERS
                                       THEN
                                          p_resp_code_out := 'R0012';
                                          p_resp_msg_out :=
                                                'Error While logging statements_log txn :'
                                             || SUBSTR (SQLERRM, 1, 200);
                                          RAISE excp_reject_orderprocess;
                                    END;
                            END IF;
                                    BEGIN
                                       INSERT INTO transaction_log
                                                   (MSG_TYPE, rrn,
                                                    delivery_channel,
                                                    transaction_sqid,
--                                                    date_time,
                                                    TRANSACTION_CODE,
--                                                    txn_type, txn_mode,
                                                    TRANSACTION_STATUS,
                                                    RESPONSE_ID,
                                                    BUSINESS_DATE,
                                                   TRANSACTION_DATE,
                                                    CARD_NUMBER,
                                                    TRANSACTION_AMOUNT,
                                                    PRODUCT_ID,
--                                                    categoryid,
                                                    auth_id, TRANSACTION_DESC,
                                                    AUTH_AMOUNT,
--                                                    instcode,
                                                    TRANFEE_AMOUNT,
                                                    CR_DR_FLAG,
                                                    CUSTOMER_CARD_NBR_ENCR,
                                                    REVERSAL_CODE,
                                                    ACCOUNT_ID,
                                                    ACCOUNT_BALANCE,
                                                    LEDGER_BALANCE,
                                                    REQ_RESP_CODE,
                                                    INS_DATE,
                                                    INS_USER,
                                                    CARD_STATUS, ERROR_MSG,
                                                    TRANSACTION_TIME,
                                                    CORRELATION_ID,
                                                    LAST_UPD_DATE,
                                                    ISSUER_ID,
                                                    PARTNER_ID,
                                                    IS_FINANCIAL,
                                                    opening_available_balance,
                                                    opening_ledger_balance,
                                                    proxy_number,
                                                    purse_id,
                                                    account_number                    
                                                   )
                                            VALUES ('0200', l_rrn,
                                                    l_delivery_channel,
                                                    l_transaction_sqid,
--                                                    SYSDATE,
                                                    l_txn_code,
--                                                    1, '0',
                                                    'C',
                                                    'R0001',
                                                    TO_CHAR (SYSDATE,
                                                             'yyyymmdd'
                                                            ),
    --                                                    l_business_time,
                                                     TO_CHAR (SYSDATE,
                                                             'yyyymmdd'
                                                            ),
                                                    v_hash_pan,
                                                    l_cam_acct_bal,
                                                    l_cap_prod_code,
--                                                    l_cap_card_type,
                                                    l_auth_id, l_tran_desc,
                                                    l_cam_acct_bal,
--                                                    p_inst_code_in,
                                                    '0.00',
                                                    l_drcr_flag,
                                                    v_cap_pan_code_encr,
                                                    0,
                                                    v_cap_acct_no,
                                                    '0',
                                                    '0',
                                                   'R0001',
                                                    SYSDATE,
                                                    1,
                                                    '9', 'OK',
                                                    l_business_time,
                                                    p_correlation_id_in,
                                                   SYSDATE,
                                                     l_issuerid,
                                                     l_partnerid,
                                                     'Y',
                                                    nvl(l_cam_acct_bal,0),
                                                    nvl(l_cam_ledger_bal,0),
                                                     l_proxy_number,
                                                     l_purse_id,
                                                     l_account_number
                                                   );
                                    EXCEPTION
                                       WHEN OTHERS
                                       THEN
                                          p_resp_code_out := 'R0012';
                                          p_resp_msg_out :=
                                                'Error While logging transactionlog txn :'
                                             || SUBSTR (SQLERRM, 1, 200);
                                          RAISE excp_reject_orderprocess;
                                    END;

--                                    BEGIN
--                                       INSERT INTO cms_transaction_log_dtl
--                                                   (ctd_delivery_channel,
--                                                    ctd_txn_code,
--                                                    ctd_txn_type,
--                                                    ctd_txn_mode,
--                                                    ctd_business_date,
--                                                    ctd_business_time,
--                                                    ctd_customer_card_no,
--                                                    ctd_txn_amount,
--                                                    ctd_actual_amount,
--                                                    ctd_bill_amount,
--                                                    ctd_process_flag,
--                                                    ctd_process_msg,
--                                                    ctd_rrn,
--                                                    ctd_customer_card_no_encr,
--                                                    ctd_msg_type,
--                                                    ctd_cust_acct_number,
--                                                    ctd_inst_code,
--                                                    ctd_hashkey_id
--                                                   )
--                                            VALUES (l_delivery_channel,
--                                                    l_txn_code,
--                                                    1,
--                                                    '0',
--                                                    TO_CHAR (SYSDATE,
--                                                             'yyyymmdd'
--                                                            ),
--                                                    l_business_time,
--                                                    v_hash_pan,
--                                                    l_cam_acct_bal,
--                                                    l_cam_acct_bal,
--                                                    l_cam_acct_bal,
--                                                    'Y',
--                                                    'Successful',
--                                                    l_rrn,
--                                                    v_cap_pan_code_encr,
--                                                    '0200',
--                                                    v_cap_acct_no,
--                                                    p_inst_code_in,
--                                                    l_hashkey_id
--                                                   );
--                                    EXCEPTION
--                                       WHEN OTHERS
--                                       THEN
--                                          p_resp_code_out := '89';
--                                          p_resp_msg_out :=
--                                                'Error While logging log_dtl txn :'
--                                             || SUBSTR (SQLERRM, 1, 200);
--                                          RAISE excp_reject_orderprocess;
--                                    END;
                                 EXCEPTION
                                    WHEN excp_reject_orderprocess
                                    THEN
                                       RAISE;
                                    WHEN OTHERS
                                    THEN
                                       p_resp_code_out := 'R0012';
                                       p_resp_msg_out :=
                                             'Error While logging cancel order txn :'
                                          || SUBSTR (SQLERRM, 1, 200);
                                       RAISE excp_reject_orderprocess;
                                 END;
                              ELSE
                                 p_resp_msg_out :=
                                       'Account balance is not updated:'
                                    || v_cap_acct_no;
                                 p_resp_code_out := 'R0012';
                                 RAISE excp_reject_orderprocess;
                              END IF;
                           EXCEPTION
                              WHEN excp_reject_orderprocess
                              THEN
                                 RAISE;
                              WHEN OTHERS
                              THEN
                                 p_resp_msg_out :=
                                       'Error While close card acct no:'
                                    || SUBSTR (SQLERRM, 1, 200);
                                 p_resp_code_out := 'R0012';
                                 RAISE excp_reject_orderprocess;
                           END;
                        EXCEPTION
                           WHEN excp_reject_orderprocess
                           THEN
                              RAISE;
                           WHEN OTHERS
                           THEN
                              p_resp_msg_out :=
                                    'Error While closing txn code :'
                                 || SUBSTR (SQLERRM, 1, 200);
                              p_resp_code_out := 'R0012';
                              RAISE excp_reject_orderprocess;
                        END;
--                     EXCEPTION
--                        WHEN excp_reject_orderprocess
--                        THEN
--                           RAISE;
--                        WHEN OTHERS
--                        THEN
--                           p_resp_msg_out :=
--                                 'Error While getting closed card txn code :'
--                              || SUBSTR (SQLERRM, 1, 200);
--                           p_resp_code_out := 'R0012';
--                           RAISE excp_reject_orderprocess;
--                     END;
                  ELSE
                     p_resp_msg_out := 'Card Not Closed:' || v_hash_pan;
                     p_resp_code_out := 'R0012';
                     RAISE excp_reject_orderprocess;
                  END IF;
               EXCEPTION
                  WHEN excp_reject_orderprocess
                  THEN
                     RAISE;
                  WHEN OTHERS
                  THEN
                     p_resp_msg_out :=
                           'Error While closinappl_pan closing cards :'
                        || SUBSTR (SQLERRM, 1, 200);
                     p_resp_code_out := 'R0012';
                     RAISE excp_reject_orderprocess;
               END;
            ELSE
                     p_resp_msg_out := 'Card NO IS NULL' || v_hash_pan;
                     p_resp_code_out := 'R0012';
                     RAISE excp_reject_orderprocess;

            END IF;
--         EXCEPTION
--            WHEN excp_reject_orderprocess
--            THEN
--               NULL;
--            WHEN OTHERS
--            THEN
--               p_resp_msg_out :=
--                    'Error While closing cards :' || SUBSTR (SQLERRM, 1, 200);
--               p_resp_code_out := 'R0012';
--         END;
    END;
      END LOOP;

      IF p_resp_code_out = 'R0001'
      THEN
         SELECT DECODE (UPPER (postback_response),
                        '1', postback_url,
                        'TRUE', postback_url,
                        ''
                       )
           INTO p_postback_url_out
           FROM order_details
          WHERE order_id = p_order_id_in
            AND partner_id = p_partner_id_in;
      END IF;
   END;

/
SHOW ERROR