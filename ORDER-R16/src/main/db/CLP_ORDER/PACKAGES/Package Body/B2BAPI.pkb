create or replace PACKAGE BODY                   "CLP_ORDER"."B2BAPI" IS

    PROCEDURE card_replacerenewal (
        p_card_no_in              IN VARCHAR2,
        p_msg_in                  IN VARCHAR2,
        p_txn_mode_in             IN VARCHAR2,
        p_curr_code_in            IN VARCHAR2,
        p_first_name_in           IN VARCHAR2,
        p_middleinitial_in        IN VARCHAR2,
        p_last_name_in            IN VARCHAR2,
        p_email_in                IN VARCHAR2,
        p_phone_in                IN VARCHAR2,
        p_addressline_one_in      IN VARCHAR2,
        p_addressline_two_in      IN VARCHAR2,
        p_addressline_three_in    IN VARCHAR2,
        p_state_in                IN VARCHAR2,
        p_city_in                 IN VARCHAR2,
        p_country_in              IN VARCHAR2,
        p_postal_code_in          IN VARCHAR2,
        p_comments_in             IN VARCHAR2,
        p_request_reason_in       IN VARCHAR2,
        p_shippingmethod_in       IN VARCHAR2,
        p_isfeewaived_in          IN VARCHAR2,
        p_fsapi_channel_in        IN VARCHAR2,
        p_stan_in                 IN VARCHAR2,
        p_mbr_numb_in             IN VARCHAR2,
        p_rvsl_code_in            IN VARCHAR2,
        p_ship_companyname_in     IN VARCHAR2,
        p_correlation_id_in       IN VARCHAR2,
        p_card_expirty_date_out   OUT VARCHAR2,
        p_available_balance_out   OUT VARCHAR2,
        p_last4digits_pan_out     OUT VARCHAR2,
        p_card_fee_out            OUT VARCHAR2,
        p_resp_code_out           OUT VARCHAR2,
        p_resp_messge_out         OUT VARCHAR2
    ) AS

	 /*****************************************************************************

    * Modified by          : Karthik.S
    * Modified Date        : 23-JUL-19
    * Modified For         : VMSCL-714	Enhancement	Card replacement change
    * Reviewer             : Rajagobal
    * Build Number         : R11 B2
     ******************************************************************************/
	 /*****************************************************************************

    * Modified by          : Karthik.L
    * Modified Date        : 13-FEB-19
    * Modified For         : Serial Number passing to SP_CARD_REPLACE Procedure for logging card table
    * Reviewer             : Chandru Ramu
    * Build Number         : R05 B2

    * Modified by          : Sampath Kumar L
    * Modified Date        : 13-FEB-19
    * Modified For         : In Transaction_Log Business date,purseId,accountId..etc added.
    * Reviewer             : Chandru Ramu
    * Build Number         :  R05.B3

    * Modified by          : Sampath Kumar L
    * Modified Date        : 05-Mar-19
    * Modified For         : In_use flag update removed.
    * Reviewer             :
    * Build Number         :  R06.B2
  ******************************************************************************/
        l_auth_savepoint           NUMBER DEFAULT 0;
        l_hash_pan                 card.card_num_hash%TYPE;
        exp_reject_record EXCEPTION;
        l_encr_pan                 card.card_num_encr%TYPE;
        l_cap_card_stat            card.card_status%TYPE;
        l_acct_number              card.account_id%TYPE;
        l_cust_code                card.customer_code%TYPE;
        l_startercard_flag         card.startercard_flag%TYPE;
        l_new_dispname             card.disp_name%TYPE;
        l_prod_code                card.product_id%TYPE;
        l_lmtprfl                  card.prfl_code%TYPE;
        l_profile_level            card.prfl_levl%TYPE;
        l_oldcard_expry            card.expiry_date%TYPE;
        l_rrn                      transaction_log.rrn%TYPE;
        l_business_date            transaction_log.transaction_date%TYPE;
        l_business_time            transaction_log.transaction_time%TYPE;
        l_statecunt                NUMBER(2);
        l_dr_cr_flag               transaction.credit_debit_indicator%TYPE;
        l_txn_type                 transaction.is_financial%TYPE;
        l_tran_type                transaction.is_financial%TYPE;
        l_timestamp                TIMESTAMP;
        l_delivery_channel         delivery_channel_transaction.channel_code%TYPE;
        l_txn_code                 transaction.transaction_code%TYPE;
        l_trans_date               DATE;
        l_proxunumber              card.proxy_number%TYPE;
  ---      l_resp_cde                 VARCHAR2(5);
  ---      l_err_msg                  VARCHAR2(500);
        l_auth_id                  transaction_log.auth_id%TYPE;
        l_dup_check                NUMBER(2);
        l_cam_lupd_date            address.last_upd_date%TYPE;
        l_acct_balance             account_purse.available_balance%TYPE;
        l_ledger_bal               account_purse.ledger_balance%TYPE;
        l_expry_date               card.expiry_date%TYPE;
--        l_new_hash_pan             card.card_num_hash%TYPE;
        l_new_card_no              VARCHAR2(100);
        p_expiry_date_out          DATE;
        l_fee_flag_out             VARCHAR2(1) DEFAULT 'Y';
        l_applpan_cardstat         card.card_status%TYPE;
        p_resp_msg_out             VARCHAR2(100);
        l_capture_date_out         DATE;
        l_cntry_code               country_code.country_code%TYPE;
        l_state_code               state_code.state_code%TYPE;
        l_tran_desc                transaction.transaction_description%TYPE;
        l_cardstat_tran_code       transaction.transaction_code%TYPE;
        l_fee_amt_out              VARCHAR2(20);
        l_serial_no                card.serial_number%TYPE;
        l_card_id                  card.cardpack_id%TYPE;
        l_encr_addr_lineone        address.address_one%TYPE;
        l_encr_addr_linetwo        address.address_two%TYPE;
        l_encr_addr_linethree      address.address_three%TYPE;
        l_encr_city                address.city_name%TYPE;
        l_encr_mob_one             address.mobl_one%TYPE;
        l_encr_email               address.email%TYPE;
        l_encr_zip                 address.pin_code%TYPE;
        l_encr_first_name          customer_profile.first_name%TYPE;
        l_encr_mid_name            customer_profile.middle_name%TYPE;
        l_encr_last_name           customer_profile.last_name%TYPE;
        v_acct_id                  card.account_id%TYPE;
        v_cust_code                card.customer_code%TYPE;
        v_address_id               card.address_id%TYPE;
        v_proxy_number             card.proxy_number%TYPE;
        v_new_hash_pan             card.card_num_hash%TYPE;
        v_new_card_no_mask         card.card_num_mask%TYPE;
        v_expry_date               card.expiry_date%TYPE;
        v_closing_avail_balance    account_purse.available_balance%TYPE;
        v_closing_ledger_balance   account_purse.ledger_balance%TYPE;
        v_resv_code                VARCHAR2(40);
        v_err_msg                  VARCHAR2(500);
        v_purse_id                 purse.purse_id%TYPE;
        v_avail_opening_balance    account_purse.available_balance%TYPE;
        v_ledger_opening_balance   account_purse.ledger_balance%TYPE;
        v_tran_fee                 NUMBER;
        v_flat_fee                 NUMBER;
        v_per_fee                  NUMBER;
        v_min_fee                  NUMBER;
        v_total_amt                NUMBER;
        v_fee_condition            VARCHAR2(20);
        v_free_txncount_flag       VARCHAR2(20);
        v_max_txncount_flag        VARCHAR2(20);
        v_business_date            DATE;
        l_order_id                 order_line_item.order_id%TYPE;
        l_order_id_num             order_line_item.order_id%TYPE;
        l_line_item_id             order_line_item.line_item_id%TYPE;
        l_parent_id                order_details.parent_oid%TYPE;
        l_package_id               order_line_item.package_id%TYPE;
        v_issuer_id                order_details.issuer_id%TYPE;
        l_shipment_id              card.repl_flag%TYPE;
        v_ctrl_num                 order_line_item_dtl.ctrl_num%TYPE;
        v_partner_id               order_line_item_dtl.partner_id%TYPE;
        l_activation_code          card.ACTIVATION_CODE%TYPE;
        l_replacement_option       VARCHAR2(20);
        l_profile_code             CARD.PRFL_CODE%TYPE;
        l_new_product              PRODUCT.PRODUCT_ID%TYPE;
        l_DISABLE_REPL_FLAG        CARD.REPL_FLAG%TYPE;
        l_PROD_CATTYPE             VARCHAR2(50);
        l_RESP_CDE                 VARCHAR2(20);
        l_FROM_PURSE_ID            product_purse.PURSE_ID%TYPE;       --VMSCL-714	Enhancement	Card replacement change
        l_TO_PURSE_ID              product_purse.PURSE_ID%TYPE;       --VMSCL-714	Enhancement	Card replacement change
		l_purse_count              NUMBER;                            --VMSCL-714	Enhancement	Card replacement change
        l_currency_count           NUMBER;                            --VMSCL-714	Enhancement	Card replacement change
    --V_DBMS  VARCHAR2(1000);
	   	l_from_prod_internSupp  VARCHAR2(10);
	    l_to_prod_internSupp    VARCHAR2(10);
        v_pur_auth_resp         VARCHAR2(10);
        V_pur_accountPurseId NUMBER(38);

    BEGIN
        SAVEPOINT l_auth_savepoint;
        l_RESP_CDE:=trans_const.success;
        p_resp_messge_out:='OK';
        BEGIN
            SELECT channel_code,
                   tran_code
            INTO
                l_delivery_channel,l_txn_code
            FROM fsapi_transaction
              WHERE channel_desc=p_fsapi_channel_in  AND request_type=p_shippingmethod_in;

        EXCEPTION
         WHEN no_data_found THEN
          l_RESP_CDE:='31';
                p_resp_messge_out:='Invalid shipping method ';
               --||substr(sqlerrm,1,200);
                RAISE exp_reject_record;

            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while getting delivery channel and tran code '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
         END;

        BEGIN
            SELECT attribute_value
            INTO
                l_shipment_id
            FROM shipment_attribute
            WHERE attribute_group='PACKAGE_SHIPMENT' AND attribute_name=p_shippingmethod_in;

        EXCEPTION
         WHEN no_data_found THEN
          l_RESP_CDE:='31';
                p_resp_messge_out:='Invalid shipping method ';
               --||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while getting delivery channel and tran code '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;
       -------

        BEGIN
            l_hash_pan:=fn_hash(p_card_no_in);
        EXCEPTION
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while converting into hash value '
                ||fn_getmaskpan(p_card_no_in)
                ||' '
                ||substr(sqlerrm,1,200);

                RAISE exp_reject_record;
        END;

        BEGIN
            l_encr_pan:=fn_emaps_main(p_card_no_in);
        EXCEPTION
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while converting into encrypted value '
                ||fn_getmaskpan(p_card_no_in)
                ||' '
                ||substr(sqlerrm,1,200);

                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT TO_CHAR(TO_CHAR(SYSDATE,'YYMMDDHH24MISS')
                ||lpad(
                    seq_cardrplrenewal.NEXTVAL,
                    3,
                    '0'
                ) )
            INTO
                l_rrn
            FROM dual;

        EXCEPTION
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while generating rrn'
                ||' '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT TO_CHAR(SYSDATE,'YYYYMMDD'),
                   TO_CHAR(SYSDATE,'HH24MMSS')
            INTO
                l_business_date,l_business_time
            FROM dual;

        EXCEPTION
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while generating business date'
                ||' '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        BEGIN
            l_trans_date:=TO_DATE(
                substr(
                    trim(l_business_date),
                    1,
                    8
                )
                ||' '
                ||substr(
                    trim(l_business_time),
                    1,
                    10
                ),
                'yyyymmdd hh24:mi:ss'
            );

        EXCEPTION
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while generating TRANS_DATE'
                ||' '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT lpad(
                    seq_auth_id.NEXTVAL,
                    6,
                    '0'
                )
            INTO
                l_auth_id
            FROM dual;

        EXCEPTION
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while generating authid '
                ||substr(sqlerrm,1,300);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT credit_debit_indicator,
                   to_number(DECODE(
                    is_financial,
                    'N',
                    '0',
                    'Y',
                    '1'
                ) ),
                   is_financial,
                   transaction_description
            INTO
                l_dr_cr_flag,
                 l_txn_type,l_tran_type,l_tran_desc
            FROM delivery_channel_transaction a,
                 transaction b
            WHERE
            a.transaction_code=b.transaction_code
              AND a.transaction_code=l_txn_code
             AND a.channel_code=l_delivery_channel
             AND ROWNUM<=1;

        EXCEPTION
            WHEN no_data_found THEN
                l_RESP_CDE:='89'; --Ineligible Transaction
                p_resp_messge_out:='Transflag  not defined for txn code '
                ||l_txn_code
                ||' and delivery channel '
                ||l_delivery_channel;
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                l_RESP_CDE:='89'; --Ineligible Transaction
                p_resp_messge_out:='Error while selecting transaction details';
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT c.card_status,
                   c.account_id,
                   c.customer_code,
               -- CAP_APPL_CODE,
                   c.startercard_flag,
                   c.disp_name,
                   c.product_id,---,CAP_CARD_TYPE,
                p.attributes.Product.formFactor,
                   c.prfl_code,
                   c.prfl_levl,
                   c.expiry_date,
                   c.proxy_number,
                   c.serial_number,
                   c.cardpack_id,
                   c.ACTIVATION_CODE
            INTO
                l_cap_card_stat,l_acct_number,l_cust_code,
               -- l_APPL_CODE,
                 l_startercard_flag,l_new_dispname,l_prod_code,l_PROD_CATTYPE,
                 l_lmtprfl,l_profile_level,l_oldcard_expry,l_proxunumber,l_serial_no,l_package_id,l_activation_code
            FROM card c,product p
            WHERE card_num_hash=l_hash_pan and C.PRODUCT_ID=P.PRODUCT_ID;

        EXCEPTION
            WHEN no_data_found THEN
                p_resp_messge_out:='Pan not found in master';
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting CARD'
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;
 --- form factor validation..
            BEGIN
              IF l_PROD_CATTYPE ='Virtual' THEN
                  l_RESP_CDE := '28';  --
                  p_resp_messge_out  := 'Replacement Not Allowed For Virtual product';
                  RAISE exp_reject_record;
                END if;
             EXCEPTION  WHEN EXP_REJECT_RECORD THEN
              RAISE;
              WHEN OTHERS THEN
                  l_RESP_CDE := '89';
                  p_resp_messge_out  := 'Error while selecting card type '||substr(sqlerrm,1,200);
                  raise exp_reject_record;
             END;


        BEGIN
            SELECT country_code,
                   state_code
            INTO
                l_cntry_code,l_state_code
            FROM state_code
            WHERE switch_state_code=p_state_in;

        EXCEPTION
            WHEN no_data_found THEN
                l_RESP_CDE:='26';
                p_resp_messge_out:='State code is not valid';
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while selecting state code '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT COUNT(1)
            INTO
                l_dup_check
            FROM htlst_reisu
            WHERE card_num_hash=l_hash_pan AND reisu_cause='R' AND new_card_num_hash IS NOT NULL;

            IF
                l_dup_check>0
            THEN
                l_RESP_CDE:='29';
                p_resp_messge_out:='Card already Replaced';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while selecting Replaced or Renewed dtls '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT last_upd_date
            INTO
                l_cam_lupd_date
            FROM address
            WHERE customer_code=l_cust_code AND addr_flag='P';

            IF
                l_cam_lupd_date>SYSDATE-1
            THEN
                p_resp_messge_out:='Card replacement is not allowed to customer who changed address in last 24 hr';
                l_RESP_CDE:='25';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting customer address details'
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;

        IF
            upper(p_isfeewaived_in)='TRUE' OR p_isfeewaived_in='1'
        THEN
            l_fee_flag_out:='N';
        ELSE
            l_fee_flag_out:='Y';
        END IF;

        BEGIN
            SELECT account_id,
                   customer_code,
                   address_id,
                   proxy_number
            INTO
                v_acct_id,v_cust_code,v_address_id,v_proxy_number
            FROM card
            WHERE card_num_hash=l_hash_pan;

        EXCEPTION
            WHEN OTHERS THEN
                p_resp_messge_out:='From CARD REPLACE-- '
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='21';
                RAISE exp_reject_record;
        END;

        BEGIN
            sp_authorize_transaction(
                l_prod_code,
                NULL,   --p_last_txndate,
                'mdm',  --p_mdm_id      ,
                l_txn_code,--p_transaction_code,
                v_acct_id,--p_account_id      ,
                0,      --p_txn_amt         ,
                0,      --p_partial_auth    ,
                l_delivery_channel,--p_delivery_channel,
                l_hash_pan,--p_card_number     ,
                l_rrn,    --p_rrn             ,
                l_applpan_cardstat,--p_card_status              ,
                l_hash_pan,--p_card_num_hash            ,
                'merchant_name',--p_merchant_name            ,
                '0200',     --p_msg_type                 ,
                p_curr_code_in,--p_currency_code            ,
                'first_party',--p_party_supported          ,
                'cr_dr',   --p_dr_cr_flag               ,
                v_closing_avail_balance,
                v_closing_ledger_balance,
                v_resv_code,
                v_err_msg,
                v_purse_id,
                v_avail_opening_balance,
                v_ledger_opening_balance,
                v_tran_fee,
                v_flat_fee,
                v_per_fee,
                v_min_fee,
                v_total_amt,
                v_fee_condition,
                v_free_txncount_flag,
                v_max_txncount_flag,
                v_business_date,
                v_pur_auth_resp,
                V_pur_accountPurseId
            );

            IF
                l_RESP_CDE <> trans_const.success AND p_resp_messge_out <> 'OK'
            THEN
                p_resp_messge_out:='Error from auth process'||p_resp_messge_out;
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error from Card authorization'
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT DECODE(
                    upper(p_request_reason_in),
                    'LOST-STOLEN',
                    '2',
                    'DAMAGED',
                    '3'
                )
            INTO
                l_cardstat_tran_code
            FROM dual;

        EXCEPTION
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error from DECODE of cards trans code'
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        BEGIN
            UPDATE card
                SET
                    card_status=DECODE(
                        p_request_reason_in,
                        'LOST-STOLEN',
                        2,
                        'DAMAGED',
                        3
                    )
            WHERE card_num_hash=l_hash_pan;

            IF
                SQL%rowcount <> 1
            THEN
                p_resp_messge_out:='Error while updating appl_pan';
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while updating appl_pan '
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;

        BEGIN

            sp_log_cardstat_chnge(
                l_hash_pan,
                l_encr_pan,
                l_auth_id,
                l_txn_code,-- '02',l_cardstat_tran_code
                l_rrn,
                l_business_date,
                l_business_time,
                l_RESP_CDE,
                p_resp_messge_out
            );

            IF
                l_RESP_CDE <> trans_const.success AND p_resp_messge_out <> 'OK'
            THEN
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
                p_resp_messge_out:='Error while logging system initiated card status change '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;



--added for 2B changes
  BEGIN
           SELECT NVL(p.attributes.General.cardRenewReplaceProd, 'NP'),

                 p.attributes.General.cardReplaceRenewalNewProd,p.attributes."Product"."internationalSupported"

             INTO l_replacement_option,l_new_product,l_from_prod_internSupp

             FROM PRODUCT P
            WHERE    P.PRODUCT_ID = l_prod_code;
        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                l_RESP_CDE:='89';
              p_resp_messge_out := 'Error while selecting replacement params '|| SUBSTR (SQLERRM, 1, 200);
              RAISE exp_reject_record;
        END;

        BEGIN
            SELECT available_balance
            INTO
                l_acct_balance
            FROM account_purse
            WHERE account_id=(
                    SELECT account_id
                    FROM card
                    WHERE card_num_hash=l_hash_pan
                );

        EXCEPTION
            WHEN OTHERS THEN
                l_acct_balance:=0;
                l_ledger_bal:=0;
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT card_status
            INTO
                l_cap_card_stat
            FROM card
            WHERE card_num_hash=l_hash_pan;

        EXCEPTION
            WHEN no_data_found THEN
                p_resp_messge_out:='Pan not found in master';
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting CARD'
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;


        IF  l_replacement_option='SP'  and  l_cap_card_stat <> '2' THEN
           -- l_cap_card_stat='7'

         --Sn find validitty param

            CLP_TRANSACTIONAL.GET_EXPIRY_DATE_CARD(
                l_prod_code,
                p_expiry_date_out,
                p_resp_msg_out
                );

          --Sn Update new expry
            BEGIN
                UPDATE card
                    SET
                        replace_exprydt=p_expiry_date_out,
                        repl_flag=l_shipment_id
                WHERE card_num_hash=l_hash_pan;

                IF
                    SQL%rowcount <> 1
                THEN
                    p_resp_messge_out:='Error while updating card ';
                    l_RESP_CDE:='89';
                    RAISE exp_reject_record;
                END IF;

            EXCEPTION
                WHEN exp_reject_record THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while updating Expiry Date'
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
                    RAISE exp_reject_record;
            END;
      --selecting the partnerId,IssuerId
         SELECT po.issuer_id,po.PARTNER_ID
        INTO
            v_issuer_id,v_partner_id
        FROM product po
        WHERE po.product_id=l_prod_code;



          --En Update new expry

            p_last4digits_pan_out:=substr(
                p_card_no_in,
                length(p_card_no_in)-3,
                length(p_card_no_in)
            );
          --Sn Update application status as printer pending

--            BEGIN
--                UPDATE cardissuance_status
--                    SET
--                        card_status='20'
--                WHERE card_num_hash=l_hash_pan;
--
--                IF
--                    SQL%rowcount <> 1
--                THEN
--                    p_resp_messge_out:='Error while updating CARDISSUANCE_STATUS ';
--                    l_RESP_CDE:='89';
--                    RAISE exp_reject_record;
--                END IF;
--
--            EXCEPTION
--                WHEN exp_reject_record THEN
--                    RAISE;
--                WHEN OTHERS THEN
--                    p_resp_messge_out:='Error while updating Application Card Issuance Status'
--                    ||substr(sqlerrm,1,200);
--                    l_RESP_CDE:='89';
--                    RAISE exp_reject_record;
--            END;
       --En Update application status as printer pending

        ELSE   -- NP ,NPP

                IF l_replacement_option = 'NPP' THEN
			BEGIN
                  select PP.PURSE_ID INTO l_FROM_PURSE_ID from product_purse pp
                      where product_id=l_prod_code and is_default='Y';       --VMSCL-714	Enhancement	Card replacement change
              EXCEPTION
			      WHEN OTHERS THEN
                   p_resp_messge_out := 'Error while selecting Current product Purse_id'|| substr(sqlerrm,1,200);
                   l_RESP_CDE := 'R0012';
                   RAISE exp_reject_record;
            END;

            BEGIN
                  select PP.PURSE_ID INTO l_TO_PURSE_ID from product_purse pp
                      where product_id=l_new_product and is_default='Y';      --VMSCL-714	Enhancement	Card replacement change
           EXCEPTION
             WHEN OTHERS THEN
			  p_resp_messge_out := 'Error while selecting New product Purse_id'|| substr(sqlerrm,1,200);
                   l_RESP_CDE := 'R0012';
                   RAISE exp_reject_record;

            END;

            IF
            l_FROM_PURSE_ID<>l_TO_PURSE_ID             --VMSCL-714	Enhancement	Card replacement change
              THEN
                   p_resp_messge_out := 'New Product purse_id not matched with current product purse_id';
                   l_RESP_CDE := 'R0012';
                   RAISE exp_reject_record;
            end if;

				---------------------------------------VMSCL-714	Enhancement	Card replacement change    matching purse_id
           BEGIN
               select count(*) into l_purse_count from (
				SELECT purse_id FROM clp_configuration.product_purse p where product_id = l_prod_code
					minus
						SELECT purse_id FROM clp_configuration.product_purse pp where product_id = l_new_product);
           EXCEPTION
             WHEN OTHERS THEN
			  p_resp_messge_out := 'Error while matching OLD product and NEW product'|| substr(sqlerrm,1,200);
                   l_RESP_CDE := 'R0012';
                   RAISE exp_reject_record;

            END;

			IF  l_purse_count<>0 THEN
			    p_resp_messge_out := 'New Product purse_id not matched with current product purse_id';
                   l_RESP_CDE := 'R0012';
                   RAISE exp_reject_record;
            end if;

-- ------------------------------------currency check

			BEGIN
                      select po.attributes."Product"."internationalSupported" into l_to_prod_internSupp from product po where product_id=l_new_product;
           EXCEPTION
             WHEN OTHERS THEN
			  p_resp_messge_out := 'Error while checking NEW product internationalSupported'|| substr(sqlerrm,1,200);
               l_RESP_CDE := 'R0201';
                   RAISE exp_reject_record;

            END;

			if l_from_prod_internSupp='Enable' and l_from_prod_internSupp=l_to_prod_internSupp then

			---------------------------------------VMSCL-714	Enhancement	Card replacement change matching currency_id
           BEGIN
               select count(*) into l_currency_count from (
				SELECT currency_id FROM clp_configuration.PRODUCT_CURRENCY p where product_id = l_prod_code
					minus
						SELECT currency_id FROM clp_configuration.PRODUCT_CURRENCY pp where product_id = l_new_product);
           EXCEPTION
             WHEN OTHERS THEN
			  p_resp_messge_out := 'Error while matching OLD product and NEW product currency'|| substr(sqlerrm,1,200);
                   l_RESP_CDE := 'R0201';
                   RAISE exp_reject_record;

            END;

			IF  l_currency_count<>0 THEN
			       p_resp_messge_out := 'New Product currency_id not matched with current product currency_id';
                   l_RESP_CDE := 'R0201';
                   RAISE exp_reject_record;
            end if;
	------------------------------------


	       elsif l_from_prod_internSupp='Disable'  and l_to_prod_internSupp='Disable'  then

				  null;
	       else
		           p_resp_messge_out := 'from and to Product internationalSupported flag not matched';
                   l_RESP_CDE := 'R0201';
                   RAISE exp_reject_record;
           end if;


                    l_prod_code := l_new_product;

                BEGIN
                    SELECT
                        pd.replacment_package_id
                    INTO
                        l_package_id
                    FROM
                        product_package pp,
                        package_definition pd,
                        product p
                    WHERE
                            pp.package_id = pd.package_id
                        AND
                            p.product_id = pp.product_id
                        AND
                            p.attributes."Product"."defaultPackage" = pd.package_id
                        AND
                            pp.product_id = l_prod_code;
             EXCEPTION
                    WHEN OTHERS THEN
                        p_resp_messge_out := 'Error while selecting replacement package id'
                         || substr(sqlerrm,1,200);
                        l_RESP_CDE := 'R0012';
                        RAISE exp_reject_record;
            END;
            else
                      -- Getting replacement package Id from package_definition
        BEGIN
        SELECT
                  DECODE(
                       replacment_package_id,
                         '-1',
                        package_id,
                         replacment_package_id
                        )
                    INTO
                        l_package_id
                    FROM
                        clp_configuration.package_definition
                    WHERE
                        package_id =l_package_id;
                EXCEPTION
                    WHEN OTHERS THEN
                        p_resp_messge_out := 'Error while selecting replacement package id'
                         || substr(sqlerrm,1,200);
                        l_RESP_CDE := 'R0012';
                        RAISE exp_reject_record;
            END;
         END IF;

            BEGIN
                sp_card_replace(
                    l_prod_code,
                    v_acct_id,
                    v_cust_code,
                    v_address_id,
                    v_proxy_number,
					l_serial_no,
                    l_package_id,
                    v_new_hash_pan,
                    l_new_card_no,--- ,v_new_card_no_encr,
                    v_new_card_no_mask,
                    v_expry_date,
                    p_resp_messge_out
                );

                IF
                    p_resp_messge_out != 'OK'
                THEN
                    p_resp_messge_out:='From CARD REPLACE-- '||p_resp_messge_out;
                    l_RESP_CDE:='R0104';
                    RAISE exp_reject_record;
                END IF;

            EXCEPTION
                WHEN exp_reject_record THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_messge_out:='From CARD REPLACE-- '||p_resp_messge_out;
                    l_RESP_CDE:='21';
                    RAISE exp_reject_record;
            END;

            l_prod_code:=l_prod_code;
            p_last4digits_pan_out:=substr(
                v_new_card_no_mask,
                length(v_new_card_no_mask)-3,
                length(v_new_card_no_mask)
            );

            BEGIN
                SELECT expiry_date
                INTO
                    p_expiry_date_out
                FROM card
                WHERE card_num_hash=v_new_hash_pan;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while selecting new expry date'
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
                    RAISE exp_reject_record;
            END;

            BEGIN
                UPDATE card
                    SET
                        repl_flag=l_shipment_id,
                        serial_number=l_serial_no,
                        --cardpack_id=l_card_id,
                    ACTIVATION_CODE=l_activation_code
                WHERE card_num_hash=v_new_hash_pan;

                IF
                    SQL%rowcount=0
                THEN
                    p_resp_messge_out:='Problem in updation of replacement flag for pan '||v_new_card_no_mask;
                    l_RESP_CDE:='89';
                    RAISE exp_reject_record;
                END IF;

            EXCEPTION
                WHEN exp_reject_record THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while updating CARD'
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
                    RAISE exp_reject_record;
            END;


  ------------start changes from cca

        UPDATE card
            SET
                serial_number=l_serial_no,
                proxy_number=l_proxunumber
        WHERE card_num_hash=v_new_hash_pan;

        SELECT replace_order_id.NEXTVAL,
               seq_parent_id.NEXTVAL
        INTO
            l_order_id_num,l_parent_id
        FROM dual;

        l_order_id:='ROID'||l_order_id_num;
        l_line_item_id:='RLID'||l_order_id_num;
                    --PACJKAGE_ID
--        SELECT
--            REPLACMENT_PACKAGE_ID
--        INTO
--            l_package_id
--        FROM
--            product_package PP,PACKAGE_DEFINITION  PD
--        WHERE
--            PP.PACKAGE_ID=PD.PACKAGE_ID
--        AND
--            product_id=l_prod_code;


        BEGIN
            SELECT ctrl_num,
                   partner_id
            INTO
                v_ctrl_num,v_partner_id
            FROM order_line_item_dtl
            WHERE card_num_encr=l_encr_pan;

        EXCEPTION
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting old card details from order_line_item_dtl'
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;

        SELECT issuer_id
        INTO
            v_issuer_id
        FROM product
        WHERE product_id=l_prod_code;
                    --INSERT iNTO ORDER_DETAILS

        INSERT INTO order_details (
            order_id,
            partner_id,
            issuer_id,
            merchant_id,
            order_default_card_status,
            postback_response,
            activation_code,
            shipping_method,
            order_status,
            address_line1,
            address_line2,
            city,
            state,
            postal_code,
            country,
            first_name,
            middle_initial,
            last_name,
            ins_date,
            error_msg,
            channel_id,
            accept_partial,
            order_type,
            parent_oid
        ) VALUES (
            l_order_id,
            v_partner_id,--l_partner_id,
            v_issuer_id,
            'MERCHANT_ID',--p_merchantid_in,
            'INACTIVE',
            'False',
             NULL,
            'l_shipment_key',--l_shipment_key,
            'ORDER-GENERATED',
            p_addressline_one_in,
            p_addressline_two_in,
            p_city_in,
            p_state_in,
            p_postal_code_in,
            p_country_in,
            p_first_name_in,
            p_middleinitial_in,
            p_last_name_in,
            SYSDATE,
            'OK',
            'WEB',
            'true',
            'IND',
            l_parent_id
        );

---INSERT ORDER_LINE_ITEM
        INSERT INTO order_line_item (
            order_id,
            line_item_id,
            package_id,
            product_id,
            quantity,
            order_status,
            ins_date,
            error_msg,
            partner_id,
            parent_oid,
            ccf_flag,
            return_file_msg
        ) VALUES (
            l_order_id,
            l_line_item_id,
            l_package_id,
            l_prod_code,
            1,
            'ORDER-GENERATED',
            SYSDATE,
            'OK',
            v_partner_id,--l_partner_id,
            l_parent_id,
            1,
            NULL
        );

--ORDER_LINE_ITEM_DTL
        INSERT INTO order_line_item_dtl (
            card_num_hash,
            card_num_encr,--
            card_num_mask,
            account_id,--
            customer_code,--
            address_id,---
            ctrl_num,---
            product_id,---
            order_id,
            partner_id,  ---
            order_line_item_id,
            parent_oid,
            serial_number,
            expiry_date
        ) VALUES (
            v_new_hash_pan,
            l_new_card_no,
            v_new_card_no_mask,
            v_acct_id,
            v_cust_code,
            v_address_id,
            v_ctrl_num,
            l_prod_code,
            l_order_id,
            v_partner_id,--l_partner_id,
            l_line_item_id,
            l_parent_id,
            l_serial_no,
            v_expry_date
        );

      IF  l_cap_card_stat <> '3' THEN
        UPDATE card
            SET
                card_status='9'
        WHERE card_num_encr=l_encr_pan;
END IF;


----------------end changes from cca
END IF;

        IF
            l_RESP_CDE=trans_const.success
        THEN
            p_resp_code_out:='00';
            p_resp_messge_out:='OK';
            p_card_expirty_date_out:=TO_CHAR(p_expiry_date_out,'MM/YY');
            p_available_balance_out:=trim(TO_CHAR(l_acct_balance,'999999999999999990.99') );
            p_card_fee_out:=trim(TO_CHAR(l_fee_amt_out,'999999999999999990.99') );
            IF l_new_card_no IS NOT NULL
          THEN
            BEGIN
                INSERT INTO htlst_reisu (
                    card_num_hash,
                    mbr_numb,
                    new_card_num_hash,
                    new_mbr,
                    reisu_cause,
                    ins_user,
                    last_upd_user,
                    card_num_encr,
                    new_card_num_encr,
                    ins_date,
                    last_upd_date
                ) VALUES (
                    l_hash_pan,
                    p_mbr_numb_in,
                    v_new_hash_pan,
                    p_mbr_numb_in,
                    'R',
                    1,
                    1,
                    l_encr_pan,
                    l_new_card_no,
                    SYSDATE,
                    SYSDATE
                );

            EXCEPTION
           --excp of begin 4
                WHEN OTHERS THEN
                   --V_DBMS:=SUBSTR(SQLERRM,1,200);
                    p_resp_messge_out:='Error while creating  reissuue record '
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
                    RAISE exp_reject_record;
            END;

            BEGIN
                INSERT INTO cardissuance_status (
                    card_num_hash,
                    card_status,
                    ins_user,
                    ins_date,
                    card_num_encr
--                        appl_code
                ) VALUES (
                    v_new_hash_pan,
                    '2',
                    1,
                    SYSDATE,
                    l_new_card_no
--                        l_appl_code
                );
            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while Inserting CCF table '
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
                    RAISE exp_reject_record;
            END;

        END IF;

        l_encr_addr_lineone:=p_addressline_one_in;
        l_encr_addr_linetwo:=p_addressline_two_in;
        l_encr_addr_linethree:=p_addressline_three_in;
        l_encr_city:=p_city_in;
        l_encr_mob_one:=p_phone_in;
        l_encr_email:=p_email_in;
        l_encr_zip:=p_postal_code_in;
        l_encr_first_name:=p_first_name_in;
        l_encr_mid_name:=p_middleinitial_in;
        l_encr_last_name:=p_last_name_in;


   END IF;
        BEGIN
            UPDATE address
                SET
                    address_one=l_encr_addr_lineone,
                    address_two=l_encr_addr_linetwo,
                    address_three=l_encr_addr_linethree,
                    city_name=l_encr_city,
                    state_switch=p_state_in,
                    state_code=l_state_code,
                    mobl_one=l_encr_mob_one,
                    email=l_encr_email,
                    pin_code=l_encr_zip,
                    country_code=l_cntry_code,--p_country_in,
                    last_upd_date=SYSDATE
            WHERE customer_code=l_cust_code;

            IF
                SQL%rowcount=0
            THEN
                p_resp_messge_out:='No records found in addrss mast';
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while updating address mast'
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;

        BEGIN
            UPDATE customer_profile
                SET
                    first_name=l_encr_first_name,
                    middle_name=l_encr_mid_name,
                    last_name=l_encr_last_name
--                CCM_EMAIL_ONE=l_encr_email,
--                CCM_MOBL_ONE=l_encr_mob_one,
--                  CCM_BUSINESS_NAME=p_ship_CompanyName_in,
--                  CCM_LUPD_DATE=SYSDATE
            WHERE customer_code=l_cust_code;

            IF
                SQL%rowcount=0
            THEN
                p_resp_messge_out:='No records found in cust mast';
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while updating cust mast'
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;

--added purrse id for logging purpose
        BEGIN
            SELECT available_balance,
                   ledger_balance,
--                       cam_type_code,
                   account_id,
                   purse_id
            INTO
                l_acct_balance,l_ledger_bal,
--                    l_cam_type_code,
                 l_acct_number,v_purse_id
            FROM account_purse
            WHERE account_id=(
                    SELECT account_id
                    FROM card
                    WHERE card_num_hash=l_hash_pan
                );

        EXCEPTION
            WHEN OTHERS THEN
                l_acct_balance:=0;
                l_ledger_bal:=0;
                RAISE exp_reject_record;
        END;

        BEGIN

            SELECT response_code,CHANNEL_RESPONSE_CODE,
                   delivery_response_description
            INTO
                l_RESP_CDE,p_resp_code_out,
                p_resp_messge_out
            FROM CLP_TRANSACTIONAL.delivery_channel_response_code
            WHERE channel_code=DECODE(
                    l_delivery_channel,
                    '13',
                    '17',
                    l_delivery_channel
                ) AND response_code='R0001';

        EXCEPTION
            WHEN OTHERS THEN
                p_resp_messge_out:='Problem while selecting data from response master 1 '
                ||l_RESP_CDE
                ||substr(sqlerrm,1,200);
                l_RESP_CDE:='89';
                RAISE exp_reject_record;
        END;

        l_timestamp:=systimestamp;
	--removed commented paramaters and added purseid,accountid,..etc
        BEGIN
            INSERT INTO transaction_log (
                transaction_sqid,
                msg_type,
                rrn,
                delivery_channel,
                terminal_id,
                transaction_code,
                transaction_status,
                transaction_date,
                transaction_time,
                card_number,
                rule_indicator,
                mccode,
                tran_curr,
                product_id,
                auth_id,
                transaction_desc,
                transaction_amount,
                rules,
                preauth_date,
                customer_card_nbr_encr,
                proxy_number,
                reversal_code,
                account_number,
                account_balance,
                ledger_balance,
                response_id,
                card_status,
                error_msg,
                process_flag,
                remark,
                ins_date,
                correlation_id,
                is_financial,
                partner_id,
                issuer_id,
                cr_dr_flag,
                business_date,
                TRANFEE_AMOUNT,
                auth_amount,
                OPENING_LEDGER_BALANCE,
                OPENING_AVAILABLE_BALANCE,
                purse_id,
                account_id
            ) VALUES (
                transaction_id_seq.NEXTVAL,
                p_msg_in,
                l_rrn,
                l_delivery_channel,
                '0',--P_TERM_ID,
                l_txn_code,

                DECODE(
                    l_RESP_CDE,
                    'R0001',
                    'C',
                    'F'
                ),
--                    l_RESP_CDE,
                l_business_date,
                substr(l_business_time,1,10),
                l_hash_pan,
                NULL,
                NULL,--P_MCC_CODE,
                p_curr_code_in,
                l_prod_code,
                l_auth_id,
                l_tran_desc,--V_NARRATION,
                '0',
                '',
                '',
                l_encr_pan,
                l_proxunumber,
                p_rvsl_code_in,
                l_acct_number,
                nvl(l_acct_balance,0),
                nvl(l_ledger_bal,0),
                l_RESP_CDE,
                l_applpan_cardstat,
                p_resp_messge_out,
                'E',
                substr(p_comments_in,1,1000),
                SYSDATE,
                p_correlation_id_in,
                l_tran_type,
                v_partner_id,
                v_issuer_id,
                l_dr_cr_flag,
                v_business_date,
                '0',
                '0',
                 nvl(l_ledger_bal,0),
                 nvl(l_acct_balance,0),
                 v_purse_id,
                 l_acct_number
            );

        EXCEPTION
            WHEN OTHERS THEN
                ROLLBACK;
                l_RESP_CDE:='89'; -- Server Declione
                p_resp_messge_out:='Problem while inserting data into transaction log  '
                ||substr(sqlerrm,1,300);
                RAISE exp_reject_record;
        END;

       -- END IF;


        IF
            l_RESP_CDE=trans_const.success
        THEN
            l_RESP_CDE:='00';
        END IF;
      --         ROLLBACK;
    EXCEPTION
        WHEN exp_reject_record THEN
            ROLLBACK;
			p_resp_code_out:=l_RESP_CDE;
	--getting business date
          if v_business_date is null then
             BEGIN
            clp_transactional.sp_get_cutoff(l_prod_code,v_business_date,p_resp_messge_out,p_resp_code_out);


        EXCEPTION

            WHEN OTHERS THEN
                v_business_date:=null;
        END;
        end if;

            -- added purse id for logging purpose
             BEGIN
                SELECT available_balance,
                       ledger_balance,
                       account_id,purse_id
                INTO
                    l_acct_balance,l_ledger_bal,
                     l_acct_number,v_purse_id
                FROM account_purse
                WHERE account_id=(
                        SELECT account_id
                        FROM card
                        WHERE card_num_hash=l_hash_pan
                    );

            EXCEPTION
                WHEN OTHERS THEN
                    l_acct_balance:=0;
                    l_ledger_bal:=0;

            END;

            BEGIN
                SELECT product_id
                INTO
                    l_prod_code
                FROM card
                WHERE card_num_hash=l_hash_pan;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Problem while selecting data from CARD'
                    ||l_RESP_CDE
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
            END;

--added for logging transaction details.
            BEGIN
                SELECT response_code,
                       CHANNEL_RESPONSE_CODE,
                       delivery_response_description
                INTO
                    l_RESP_CDE,p_resp_code_out,p_resp_messge_out
                FROM CLP_TRANSACTIONAL.delivery_channel_response_code
                WHERE channel_code=DECODE(
                        l_delivery_channel,
                        '13',
                        '17',
                        l_delivery_channel
                    ) AND CHANNEL_RESPONSE_CODE=l_RESP_CDE;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Problem while selecting data from response master 3'
                    ||l_RESP_CDE
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
            END;

            l_timestamp:=systimestamp;
            BEGIN
                INSERT INTO transaction_log (
                    transaction_sqid,
                    msg_type,
                    rrn,
                    delivery_channel,
                    terminal_id,
                    transaction_code,
                    transaction_status,
                    transaction_date,
                    transaction_time,
                    card_number,
                    rule_indicator,
                    mccode,
                    tran_curr,
                    product_id,
                    auth_id,
                    transaction_desc,
                    transaction_amount,
                    rules,
                    preauth_date,
                    customer_card_nbr_encr,
                    topup_card_nbr_encr,
                    proxy_number,
                    reversal_code,
                    account_number,
                    account_balance,
                    ledger_balance,
                    response_id,
                    card_status,
                    error_msg,
                    process_flag,
                    remark,
                    ins_date,
                    correlation_id,
                    is_financial,
                    partner_id,
                    issuer_id,
                    cr_dr_flag,
                    business_date,
                    TRANFEE_AMOUNT,
                    auth_amount,
                    OPENING_LEDGER_BALANCE,
                    OPENING_AVAILABLE_BALANCE,
                    purse_id,
                    account_id
                ) VALUES (
                    transaction_id_seq.NEXTVAL,
                    p_msg_in,
                    l_rrn,
                    l_delivery_channel,
                    '0',--P_TERM_ID,
                    l_txn_code,
                    DECODE(
                        l_RESP_CDE,
                        '00',
                        'C',
                        'F'
                    ),
--                    l_RESP_CDE,
                    l_business_date,
                    substr(l_business_time,1,10),
                    l_hash_pan,
                    '',
                    NULL,--P_MCC_CODE,
                    p_curr_code_in,
                    l_prod_code,
                    l_auth_id,
                    l_tran_desc,--V_NARRATION,
                    '0',
                    '',
                    '',

                    l_encr_pan,
                    NULL,
                    l_proxunumber,
                    p_rvsl_code_in,
                    l_acct_number,
                    nvl(l_acct_balance,0),
                    nvl(l_ledger_bal,0),
                    l_RESP_CDE,
                    l_applpan_cardstat,

                    p_resp_messge_out,
                    'E',
                    substr(p_comments_in,1,1000),
                    SYSDATE,
                    p_correlation_id_in,
                    l_tran_type,
                    v_partner_id,
                    v_issuer_id,
                    l_dr_cr_flag,
                    v_business_date,
                    '0',
                    '0',
                    nvl(l_ledger_bal,0),
                    nvl(l_acct_balance,0),
                    v_purse_id,
                    l_acct_number

                );


            EXCEPTION
                WHEN OTHERS THEN
		rollback;
                    p_resp_code_out:='89'; -- Server Declione
                    p_resp_messge_out:='Problem while inserting data into transaction log  '
                    ||substr(sqlerrm,1,300);
                -- return;
            END;


        WHEN OTHERS THEN    --exception for main begin
            ROLLBACK TO l_auth_savepoint;
			l_RESP_CDE:='89';

             if v_business_date is null then
             BEGIN
            clp_transactional.sp_get_cutoff(l_prod_code,v_business_date,p_resp_messge_out,p_resp_code_out);


        EXCEPTION

            WHEN OTHERS THEN
                v_business_date:=null;
        END;
        end if;


            BEGIN
                SELECT available_balance,
                       ledger_balance,
                       account_id,purse_id
                INTO
                    l_acct_balance,l_ledger_bal,
                     l_acct_number,v_purse_id
                FROM account_purse
                WHERE account_id=(
                        SELECT account_id
                        FROM card
                        WHERE card_num_hash=l_hash_pan
                    );

            EXCEPTION
                WHEN OTHERS THEN
                    l_acct_balance:=0;
                    l_ledger_bal:=0;

            END;

            BEGIN
                SELECT product_id
                INTO
                    l_prod_code
                FROM card
                WHERE card_num_hash=l_hash_pan;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Problem while selecting data from CARD'
                    ||l_RESP_CDE
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
            END;

            BEGIN
                SELECT response_code,
                       CHANNEL_RESPONSE_CODE,
                       delivery_response_description
                INTO
                    l_RESP_CDE,p_resp_code_out,p_resp_messge_out
                FROM CLP_TRANSACTIONAL.delivery_channel_response_code
                WHERE channel_code=DECODE(
                        l_delivery_channel,
                        '13',
                        '17',
                        l_delivery_channel
                    ) AND response_code=l_RESP_CDE;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Problem while selecting data from response master 3'
                    ||l_RESP_CDE
                    ||substr(sqlerrm,1,200);
                    l_RESP_CDE:='89';
            END;

            l_timestamp:=systimestamp;
	    --removed commented parameters and logging purseId,accountid...etc
            BEGIN
                INSERT INTO transaction_log (
                    transaction_sqid,
                    msg_type,
                    rrn,
                    delivery_channel,
                    terminal_id,
                    transaction_code,
                    transaction_status,
                    transaction_date,
                    transaction_time,
                    card_number,
                    rule_indicator,
                    mccode,
                    tran_curr,
                    product_id,
                    auth_id,
                    transaction_desc,
                    transaction_amount,
                    rules,
                    preauth_date,
                    customer_card_nbr_encr,
                    topup_card_nbr_encr,
                    proxy_number,
                    reversal_code,
                    account_number,
                    account_balance,
                    ledger_balance,
                    response_id,
                    card_status,
                    error_msg,
                    process_flag,
                    remark,
                    ins_date,
                    correlation_id,
                    is_financial,
                    partner_id,
                    issuer_id,
                    cr_dr_flag,
                    business_date,
                    TRANFEE_AMOUNT,
                    auth_amount,
                    OPENING_AVAILABLE_BALANCE,
                    OPENING_LEDGER_BALANCE,
                    purse_id,
                    account_id
                ) VALUES (
                    transaction_id_seq.NEXTVAL,
                    p_msg_in,
                    l_rrn,
                    l_delivery_channel,
                    '0',--P_TERM_ID,
                    l_txn_code,
                    DECODE(
                        l_RESP_CDE,
                        '00',
                        'C',
                        'F'
                    ),
--                    l_RESP_CDE,
                    l_business_date,
                    substr(l_business_time,1,10),
                    l_hash_pan,
                    '',
                    NULL,--P_MCC_CODE,
                    p_curr_code_in,
                    l_prod_code,
                    l_auth_id,
                    l_tran_desc,--V_NARRATION,
                    '0',
                    '',
                    '',

                    l_encr_pan,
                    NULL,
                    l_proxunumber,
                    p_rvsl_code_in,
                    l_acct_number,
                    nvl(l_acct_balance,0),
                    nvl(l_ledger_bal,0),
                    l_RESP_CDE,
                    l_applpan_cardstat,

                    p_resp_messge_out,
                    'E',
                    substr(p_comments_in,1,1000),
                    SYSDATE,
                    p_correlation_id_in,
                    l_tran_type,
                    v_partner_id,
                    v_issuer_id,
                    l_dr_cr_flag,
                    v_business_date,
                    '0',
                    '0',
                    nvl(l_acct_balance,0),
                    nvl(l_ledger_bal,0),
                    v_purse_id,
                    l_acct_number
                );


            EXCEPTION
                WHEN OTHERS THEN
rollback;
                    p_resp_code_out:='89'; -- Server Declione
                    p_resp_messge_out:='Problem while inserting data into transaction log  '
                    ||substr(sqlerrm,1,300);
                -- return;
            END;

    END;


END B2BAPI;
/
show errors;

------------------------------------------------------------------------------------------------------