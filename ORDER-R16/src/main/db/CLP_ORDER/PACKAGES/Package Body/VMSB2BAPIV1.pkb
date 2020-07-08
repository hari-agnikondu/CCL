  CREATE OR REPLACE  PACKAGE BODY "CLP_ORDER"."VMSB2BAPIV1" IS

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
    --V_DBMS  VARCHAR2(1000);
    BEGIN
        SAVEPOINT l_auth_savepoint;
        p_resp_code_out:=trans_const.success;
        p_resp_messge_out:='OK';
        BEGIN
            dbms_output.put_line('welcome');
            SELECT channel_code,
                   tran_code
            INTO
                l_delivery_channel,l_txn_code
            FROM fsapi_transaction
            WHERE channel_desc=p_fsapi_channel_in AND request_type=p_shippingmethod_in;

            dbms_output.put_line('l_delivery_channel...'||l_delivery_channel);
            dbms_output.put_line('l_txn_code...'||l_txn_code);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
                p_resp_messge_out:='Error while getting delivery channel and tran code '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;
        -------

        BEGIN
            dbms_output.put_line('welcome');
            SELECT attribute_value
            INTO
                l_shipment_id
            FROM shipment_attribute
            WHERE attribute_group='PACKAGE_SHIPMENT' AND attribute_name=p_shippingmethod_in;

            dbms_output.put_line('l_delivery_channel...'||l_delivery_channel);
            dbms_output.put_line('l_txn_code...'||l_txn_code);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
                p_resp_messge_out:='Error while getting delivery channel and tran code '
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;
       -------

        BEGIN
            l_hash_pan:=fn_hash(p_card_no_in);
            dbms_output.put_line('l_hash_pan...'||l_hash_pan);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
                p_resp_messge_out:='Error while converting into hash value '
                ||fn_getmaskpan(p_card_no_in)
                ||' '
                ||substr(sqlerrm,1,200);

                RAISE exp_reject_record;
        END;

        BEGIN
            l_encr_pan:=fn_emaps_main(p_card_no_in);
            dbms_output.put_line('l_encr_pan...'||l_encr_pan);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
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

            dbms_output.put_line('l_rrn...'||l_rrn);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
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

            dbms_output.put_line('l_business_date...'||l_business_date);
            dbms_output.put_line('l_business_time...'||l_business_time);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
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

            dbms_output.put_line('l_trans_date...'||l_trans_date);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
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

            dbms_output.put_line('l_auth_id...'||l_auth_id);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while generating authid '
                ||substr(sqlerrm,1,300);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT credit_debit_indicator,
             --  CTM_OUTPUT_TYPE,
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
           --l_OUTPUT_TYPE,
                 l_txn_type,l_tran_type,l_tran_desc
            FROM delivery_channel_transaction a,
                 transaction b
            WHERE a.transaction_code=b.transaction_code AND a.transaction_code=l_txn_code AND a.channel_code=l_delivery_channel AND ROWNUM<=1;

            dbms_output.put_line('l_dr_cr_flag...'||l_dr_cr_flag);
            dbms_output.put_line('l_txn_type...'||l_txn_type);
            dbms_output.put_line('l_tran_type...'||l_tran_type);
            dbms_output.put_line('l_tran_desc...'||l_tran_desc);
        EXCEPTION
            WHEN no_data_found THEN
                p_resp_code_out:='89'; --Ineligible Transaction
                p_resp_messge_out:='Transflag  not defined for txn code '
                ||l_txn_code
                ||' and delivery channel '
                ||l_delivery_channel;
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                p_resp_code_out:='89'; --Ineligible Transaction
                p_resp_messge_out:='Error while selecting transaction details';
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT card_status,
                   account_id,
                   customer_code,
               -- CAP_APPL_CODE,
                   startercard_flag,
                   disp_name,
                   product_id,---,CAP_CARD_TYPE,
                   prfl_code,
                   prfl_levl,
                   expiry_date,
                   proxy_number,
                   serial_number,
                   cardpack_id,
                   ACTIVATION_CODE
            INTO
                l_cap_card_stat,l_acct_number,l_cust_code,
               -- l_APPL_CODE,
                 l_startercard_flag,l_new_dispname,l_prod_code,---,l_PROD_CATTYPE,
                 l_lmtprfl,l_profile_level,l_oldcard_expry,l_proxunumber,l_serial_no,l_card_id,l_activation_code
            FROM card
            WHERE card_num_hash=l_hash_pan;

            dbms_output.put_line('l_cap_card_stat...'||l_cap_card_stat);
            dbms_output.put_line('l_acct_number...'||l_acct_number);
            dbms_output.put_line('l_cust_code...'||l_cust_code);
            dbms_output.put_line('l_startercard_flag...'||l_startercard_flag);
            dbms_output.put_line('l_new_dispname...'||l_new_dispname);
            dbms_output.put_line('l_prod_code...'||l_prod_code);
            dbms_output.put_line('l_lmtprfl...'||l_lmtprfl);
            dbms_output.put_line('l_profile_level...'||l_profile_level);
            dbms_output.put_line('l_card_id...'||l_card_id);
        EXCEPTION
            WHEN no_data_found THEN
                p_resp_messge_out:='Pan not found in master';
                p_resp_code_out:='89';
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting CARD'
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;
 --- form factor validation..

        BEGIN
            SELECT country_code,
                   state_code
            INTO
                l_cntry_code,l_state_code
            FROM state_code
            WHERE switch_state_code=p_state_in;

            dbms_output.put_line('l_cntry_code...'||l_cntry_code);
            dbms_output.put_line('l_state_code...'||l_state_code);
        EXCEPTION
            WHEN no_data_found THEN
                p_resp_code_out:='26';
                p_resp_messge_out:='State code is not valid';
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                p_resp_code_out:='89';
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

            dbms_output.put_line('l_dup_check...'||l_dup_check);
            IF
                l_dup_check>0
            THEN
                p_resp_code_out:='29';
                p_resp_messge_out:='Card already Replaced';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_code_out:='89';
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

            dbms_output.put_line('l_cam_lupd_date...'||l_cam_lupd_date);
            IF
                l_cam_lupd_date>SYSDATE-1
            THEN
                p_resp_messge_out:='Card replacement is not allowed to customer who changed address in last 24 hr';
                p_resp_code_out:='25';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting customer address details'
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
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
                p_resp_code_out:='21';
                RAISE exp_reject_record;
        END;

        dbms_output.put_line('v_acct_id...'||v_acct_id);
        dbms_output.put_line('v_cust_code...'||v_cust_code);
        dbms_output.put_line('v_address_id...'||v_address_id);
        dbms_output.put_line('v_proxy_number...'||v_proxy_number);
        BEGIN
            dbms_output.put_line('l_prod_code...'||l_prod_code);
            dbms_output.put_line('l_txn_code...'||l_txn_code);
            dbms_output.put_line('v_acct_id...'||v_acct_id);
            dbms_output.put_line('l_delivery_channel...'||l_delivery_channel);
            dbms_output.put_line('l_hash_pan...'||l_hash_pan);
            dbms_output.put_line('l_rrn...'||l_rrn);
            dbms_output.put_line('l_applpan_cardstat...'||l_applpan_cardstat);
            dbms_output.put_line('l_hash_pan...'||l_hash_pan);
            dbms_output.put_line('p_curr_code_in...'||p_curr_code_in);
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
                v_business_date
            );

            dbms_output.put_line('v_closing_avail_balance...'||v_closing_avail_balance);
            dbms_output.put_line('v_resv_code...'||v_resv_code);
            dbms_output.put_line('v_err_msg...'||v_err_msg);
            dbms_output.put_line('v_business_date...'||v_business_date);
            dbms_output.put_line('v_purse_id...'||v_purse_id);
            IF
                p_resp_code_out <> trans_const.success AND p_resp_messge_out <> 'OK'
            THEN
                p_resp_code_out:=p_resp_code_out;
                p_resp_messge_out:='Error from auth process'||p_resp_messge_out;
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_code_out:='89';
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

            dbms_output.put_line('l_cardstat_tran_code...'||l_cardstat_tran_code);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code_out:='89';
                p_resp_messge_out:='Error from DECODE of cards trans code'
                ||substr(sqlerrm,1,200);
                RAISE exp_reject_record;
        END;

        dbms_output.put_line('p_request_reason_in...'||p_request_reason_in);
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

            dbms_output.put_line('SQL%rowcount...'||SQL%rowcount);
            IF
                SQL%rowcount <> 1
            THEN
                p_resp_messge_out:='Error while updating appl_pan';
                p_resp_code_out:='89';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while updating appl_pan '
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

        BEGIN
            dbms_output.put_line('l_hash_pan...'||l_hash_pan);
            dbms_output.put_line('l_encr_pan...'||l_encr_pan);
            dbms_output.put_line('l_auth_id...'||l_auth_id);
            dbms_output.put_line('l_txn_code...'||l_txn_code);
            dbms_output.put_line('l_rrn...'||l_rrn);
            dbms_output.put_line('l_business_date...'||l_business_date);
            dbms_output.put_line('l_business_time...'||l_business_time);
            sp_log_cardstat_chnge(
                l_hash_pan,
                l_encr_pan,
                l_auth_id,
                l_txn_code,--l_cardstat_tran_code,-- '02',
                l_rrn,
                l_business_date,
                l_business_time,
                p_resp_code_out,
                p_resp_messge_out
            );

            dbms_output.put_line('p_resp_code_out..597.'||p_resp_code_out);
            dbms_output.put_line('p_resp_messge_out...'||p_resp_messge_out);
            IF
                p_resp_code_out <> trans_const.success AND p_resp_messge_out <> 'OK'
            THEN
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_code_out:='89';
                p_resp_messge_out:='Error while logging system initiated card status change '
                ||substr(sqlerrm,1,200);
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
                p_resp_code_out:='89';
                RAISE exp_reject_record;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting CARD'
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

        IF
   --    L_replacement_option='SP'  and
            --l_cap_card_stat <> '2'
            l_cap_card_stat='7'
        THEN
         --Sn find validitty param
            get_expiry_date_card(l_prod_code,p_expiry_date_out,p_resp_msg_out);

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
                    p_resp_messge_out:='Error while updating appl_pan ';
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
                END IF;

            EXCEPTION
                WHEN exp_reject_record THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while updating Expiry Date'
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
            END;
          --En Update new expry

            p_last4digits_pan_out:=substr(
                p_card_no_in,
                length(p_card_no_in)-3,
                length(p_card_no_in)
            );
          --Sn Update application status as printer pending

            BEGIN
                UPDATE cardissuance_status
                    SET
                        card_status='20'
                WHERE card_num_hash=l_hash_pan;

                IF
                    SQL%rowcount <> 1
                THEN
                    p_resp_messge_out:='Error while updating CARDISSUANCE_STATUS ';
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
                END IF;

            EXCEPTION
                WHEN exp_reject_record THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while updating Application Card Issuance Status'
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
            END;
       --En Update application status as printer pending

            dbms_output.put_line('l_prod_code...'||l_prod_code);
            dbms_output.put_line('v_acct_id...'||v_acct_id);
            dbms_output.put_line('v_cust_code...'||v_cust_code);
            dbms_output.put_line('v_address_id...'||v_address_id);
            dbms_output.put_line('v_proxy_number...'||v_proxy_number);
        ELSE   -- NP ,NPP
            BEGIN
                sp_card_replace(
                    l_prod_code,
                    v_acct_id,
                    v_cust_code,
                    v_address_id,
                    v_proxy_number,
                    v_new_hash_pan,
                    l_new_card_no,--- ,v_new_card_no_encr,
                    v_new_card_no_mask,
                    v_expry_date,
                    p_resp_messge_out
                );

                dbms_output.put_line('v_new_hash_pan...'||v_new_hash_pan);
                dbms_output.put_line('l_new_card_no...'||l_new_card_no);
                dbms_output.put_line('v_new_card_no_mask...'||v_new_card_no_mask);
                dbms_output.put_line('v_expry_date...'||v_expry_date);
                dbms_output.put_line('p_resp_messge_out...'||p_resp_messge_out);
                IF
                    p_resp_messge_out != 'OK'
                THEN
                    p_resp_messge_out:='From CARD REPLACE-- '||p_resp_messge_out;
                    p_resp_code_out:='21';
                    RAISE exp_reject_record;
                END IF;

            EXCEPTION
                WHEN exp_reject_record THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_messge_out:='From CARD REPLACE-- '||p_resp_messge_out;
                    p_resp_code_out:='21';
                    RAISE exp_reject_record;
            END;

            l_prod_code:=l_prod_code;
            p_last4digits_pan_out:=substr(
                v_new_card_no_mask,
                length(v_new_card_no_mask)-3,
                length(v_new_card_no_mask)
            );

            dbms_output.put_line('v_new_card_no_mask...'||v_new_card_no_mask);
            dbms_output.put_line('p_last4digits_pan_out...'||p_last4digits_pan_out);
            BEGIN
                SELECT expiry_date
                INTO
                    p_expiry_date_out
                FROM card
                WHERE card_num_hash=v_new_hash_pan;

                dbms_output.put_line('p_expiry_date_out...'||p_expiry_date_out);
            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while selecting new expry date'
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
            END;

            dbms_output.put_line('l_serial_no...'||l_serial_no);
            dbms_output.put_line('l_card_id...'||l_card_id);
            BEGIN
                UPDATE card
                    SET
                        repl_flag=l_shipment_id,
                        serial_number=l_serial_no,
                        cardpack_id=l_card_id,
                    ACTIVATION_CODE=l_activation_code
                WHERE card_num_hash=v_new_hash_pan;

                dbms_output.put_line('SQL%rowcount...'||SQL%rowcount);
                IF
                    SQL%rowcount=0
                THEN
                    p_resp_messge_out:='Problem in updation of replacement flag for pan '||v_new_card_no_mask;
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
                END IF;

            EXCEPTION
                WHEN exp_reject_record THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while updating CARD'
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
            END;

        END IF;
  ------------start changes from cca

        UPDATE card
            SET
                serial_number=l_serial_no,
                proxy_number=l_proxunumber
        WHERE card_num_hash=v_new_hash_pan;

        dbms_output.put_line('SQL%rowcount...'||SQL%rowcount);
        SELECT replace_order_id.NEXTVAL,
               seq_parent_id.NEXTVAL
        INTO
            l_order_id_num,l_parent_id
        FROM dual;

        l_order_id:='ROID'||l_order_id_num;
        l_line_item_id:='RLID'||l_order_id_num;
        dbms_output.put_line('l_order_id_num...'||l_order_id_num);
        dbms_output.put_line('l_parent_id...'||l_parent_id);
        dbms_output.put_line('l_order_id...'||l_order_id);
        dbms_output.put_line('l_line_item_id...'||l_line_item_id);

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
            SELECT pd.replacment_package_id
            INTO
                l_package_id
            FROM product_package pp,
                 package_definition pd,
                 product p
            WHERE pp.package_id=pd.package_id
                AND p.product_id=pp.product_id
                AND p.attributes."Product"."defaultPackage"=pd.package_id
                AND pp.product_id = l_prod_code;

        EXCEPTION
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while selecting replacement package id'
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

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
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

        dbms_output.put_line('l_package_id...'||l_package_id);
        SELECT issuer_id
        INTO
            v_issuer_id
        FROM product
        WHERE product_id=l_prod_code;
                    --INSERT iNTO ORDER_DETAILS

        dbms_output.put_line('v_issuer_id...'||v_issuer_id);
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
            '009017',
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

        dbms_output.put_line('order_details sql%ROWCOUNT...'||SQL%rowcount);
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

        dbms_output.put_line('order_line sql%ROWCOUNT...'||SQL%rowcount);
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

        dbms_output.put_line('order_LINE_DTL sql%ROWCOUNT...'||SQL%rowcount);
        UPDATE card
            SET
                card_status='9'
        WHERE card_num_encr=l_encr_pan;

        dbms_output.put_line('card sql%ROWCOUNT...'||SQL%rowcount);
        dbms_output.put_line('p_resp_code_out...'||p_resp_code_out);
----------------end changes from cca
        IF
            p_resp_code_out='R0001'
        THEN
            p_resp_code_out:=trans_const.success;
            p_resp_messge_out:='OK';
            p_card_expirty_date_out:=TO_CHAR(p_expiry_date_out,'MM/YY');
            p_available_balance_out:=trim(TO_CHAR(l_acct_balance,'999999999999999990.99') );
            p_card_fee_out:=trim(TO_CHAR(l_fee_amt_out,'999999999999999990.99') );
--            IF l_new_card_no IS NOT NULL
--            THEN
            dbms_output.put_line('p_resp_code_out...'||p_resp_code_out);
            dbms_output.put_line('p_resp_messge_out...'||p_resp_messge_out);
            BEGIN
                dbms_output.put_line('inside INSERT...');
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

                dbms_output.put_line('HTLIST SQL%ROWCOUNT...'||SQL%rowcount);
            EXCEPTION
           --excp of begin 4
                WHEN OTHERS THEN
                   --V_DBMS:=SUBSTR(SQLERRM,1,200);
                   --DBMS_OUTPUT.PUT_LINE('ERROR IN HTLIST SQL%ROWCOUNT...'||V_DBMS);
                    dbms_output.put_line('ERROR IN HTLIST SQL%ROWCOUNT...'
                    ||substr(sqlerrm,1,200) );
                    p_resp_messge_out:='Error while creating  reissuue record '
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
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

                dbms_output.put_line('cardissuance_status SQL%ROWCOUNT...'||SQL%rowcount);
            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Error while Inserting CCF table '
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
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

            dbms_output.put_line('ADDRESS SQL%ROWCOUNT...'||SQL%rowcount);
            IF
                SQL%rowcount=0
            THEN
                p_resp_messge_out:='No records found in addrss mast';
                p_resp_code_out:='89';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while updating address mast'
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

        BEGIN
            UPDATE customer_profile
                SET
                    first_name=l_encr_first_name,
                    middle_name=l_encr_mid_name,
                    last_name=l_encr_last_name
--				  CCM_EMAIL_ONE=l_encr_email,
--                CCM_MOBL_ONE=l_encr_mob_one,
--                  CCM_BUSINESS_NAME=p_ship_CompanyName_in,
--                  CCM_LUPD_DATE=SYSDATE
            WHERE customer_code=l_cust_code;

            dbms_output.put_line('customer_profile SQL%ROWCOUNT...'||SQL%rowcount);
            IF
                SQL%rowcount=0
            THEN
                p_resp_messge_out:='No records found in cust mast';
                p_resp_code_out:='89';
                RAISE exp_reject_record;
            END IF;

        EXCEPTION
            WHEN exp_reject_record THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_messge_out:='Error while updating cust mast'
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

        BEGIN
            SELECT available_balance,
                   ledger_balance,
--                       cam_type_code,
                   account_id
            INTO
                l_acct_balance,l_ledger_bal,
--                    l_cam_type_code,
                 l_acct_number
            FROM account_purse
            WHERE account_id=(
                    SELECT account_id
                    FROM card
                    WHERE card_num_hash=l_hash_pan
                );

            dbms_output.put_line('l_acct_balance ...'||l_acct_balance);
            dbms_output.put_line('l_ledger_bal ...'||l_ledger_bal);
            dbms_output.put_line('l_acct_number ...'||l_acct_number);
        EXCEPTION
            WHEN OTHERS THEN
                l_acct_balance:=0;
                l_ledger_bal:=0;
        END;

        BEGIN
            dbms_output.put_line('INSIDE delivery_channel_response_code ...'||l_delivery_channel);
            dbms_output.put_line('INSIDE p_resp_code_out ...'||p_resp_code_out);
            SELECT response_code,
                   response_code,
                   delivery_response_description
            INTO
                p_resp_code_out,p_resp_code_out,p_resp_messge_out
            FROM delivery_channel_response_code
            WHERE channel_code=DECODE(
                    l_delivery_channel,
                    '13',
                    '17',
                    l_delivery_channel
                ) AND response_code=p_resp_code_out;

        EXCEPTION
            WHEN OTHERS THEN
                p_resp_messge_out:='Problem while selecting data from response master 1 '
                ||p_resp_code_out
                ||substr(sqlerrm,1,200);
                p_resp_code_out:='89';
                RAISE exp_reject_record;
        END;

        dbms_output.put_line('p_comments_in ...'||p_comments_in);
        l_timestamp:=systimestamp;
        BEGIN
            dbms_output.put_line('INSIDE transaction_log ...');
            INSERT INTO transaction_log (
                transaction_sqid,
                msg_type,
                rrn,
                delivery_channel,
                terminal_id,
--                    TRANSACTION_DATE,
                transaction_code,
--                    TRANSACTION_DESC,
--                    txn_mode,
                transaction_status,
--                    RESPONSE_ID,
                transaction_date,
                transaction_time,
                card_number,
                topup_card_number,
                topup_account_number,
--                    topup_acct_type,
--                    bank_code,
--                    total_amount,
                rule_indicator,
--                    rulegroupid,
                mccode,
                tran_curr,
--                    addcharge,
                product_id,
--                    CATEGORY_ID,
--                    tips,
--                    decline_ruleid,
--                    atm_name_location,
                auth_id,
                transaction_desc,
                transaction_amount,
--                    preauthamount,
--                    partialamount,
--                    mccodegroupid,
--                    currencycodegroupid,
--                    transcodegroupid,
                rules,
                preauth_date,
--                    gl_upd_flag,
--                    system_trace_audit_no,
--                    instcode,
/*
                    feecode,
                    tranfee_amt,
                    servicetax_amt,
                    cess_amt,
                    cr_dr_flag,
                    tranfee_cr_acctno,
                    tranfee_dr_acctno,
                    tran_st_calc_flag,
                    tran_cess_calc_flag,
                    tran_st_cr_acctno,
                    tran_st_dr_acctno,
                    tran_cess_cr_acctno,
                    tran_cess_dr_acctno,
*/
                customer_card_nbr_encr,
                topup_card_nbr_encr,
                proxy_number,
                reversal_code,
                account_number,
                account_balance,
                ledger_balance,
                response_id,
                card_status,
--                    fee_plan,
--                    csr_achactiontaken,
                error_msg,
                process_flag,
--                    acct_type,
--                    time_stamp,
                remark,
                ins_date,
                correlation_id,
                is_financial,
                partner_id,
                issuer_id,
                cr_dr_flag,
                business_date
            ) VALUES (
                transaction_id_seq.NEXTVAL,
                p_msg_in,
                l_rrn,
                l_delivery_channel,
                '0',--P_TERM_ID,
--                    l_trans_date,
                l_txn_code,
--                    l_txn_type,
--                    p_txn_mode_in,
                DECODE(
                    p_resp_code_out,
                    'R0001',
                    'C',
                    'F'
                ),
--                    p_resp_code_out,
                l_business_date,
                substr(l_business_time,1,10),
                l_hash_pan,
                NULL,
                NULL,--P_topup_acctno    ,
--                    NULL,--P_topup_accttype,
--                    NULL,--P_BANK_CODE,
--                    NULL,--TRIM(TO_CHAR(NVL(V_TOTAL_AMT,0),'99999999999999990.99')),
                '',
--                    '',
                NULL,--P_MCC_CODE,
                p_curr_code_in,
--                    NULL,-- P_add_charge,
                l_prod_code,
--                    l_prod_cattype,
--                    0,
--                    '',
--                    '',
                l_auth_id,
                l_tran_desc,--V_NARRATION,
                NULL,--TRIM(TO_CHAR(NVL(V_TRAN_AMT,0),'99999999999999990.99')),
--                    '0.00',
--                    '0.00',
--                    '',
--                    '',
--                    '',
                '',
                '',
--                    NULL,--V_GL_UPD_FLAG,
--  	                  p_stan_in,
--                    p_inst_code_in,
/*
                    NULL,--V_FEE_CODE,
                    NULL,--NVL(V_FEE_AMT,0),
                    NULL,--NVL(V_SERVICETAX_AMOUNT,0),
                    NULL,--NVL(V_CESS_AMOUNT,0),
                    NULL,--V_DR_CR_FLAG,
                    NULL,--V_FEE_CRACCT_NO,
                    NULL,--V_FEE_DRACCT_NO,
                    NULL,--V_ST_CALC_FLAG,
                    NULL,--V_CESS_CALC_FLAG,
                    NULL,-- V_ST_CRACCT_NO,
                    NULL,--V_ST_DRACCT_NO,
                    NULL,--V_CESS_CRACCT_NO,
                    NULL,--V_CESS_DRACCT_NO,
*/
                l_encr_pan,
                NULL,
                l_proxunumber,
                p_rvsl_code_in,
                l_acct_number,
                nvl(l_acct_balance,0),
                nvl(l_ledger_bal,0),
                p_resp_code_out,
                l_applpan_cardstat,
--                    NULL,--V_FEE_PLAN,
--                    NULL,--P_FEE_FLAG,
                p_resp_messge_out,
                'E',
--                    l_cam_type_code,
--                    l_timestamp,
                substr(p_comments_in,1,1000),
                SYSDATE,
                p_correlation_id_in,
                l_tran_type,
                v_partner_id,
                v_issuer_id,
                l_dr_cr_flag,
                l_business_date
            );

            dbms_output.put_line('trans_log SQL%ROWCOUNT...'||SQL%rowcount);
        EXCEPTION
            WHEN OTHERS THEN
                dbms_output.put_line('in exception trans_log SQL%ROWCOUNT...'
                ||substr(sqlerrm,1,200) );
                ROLLBACK;
                p_resp_code_out:='89'; -- Server Declione
                p_resp_messge_out:='Problem while inserting data into transaction log  '
                ||substr(sqlerrm,1,300);
                RAISE exp_reject_record;
        -- return;
        END;

        COMMIT;
        IF
            p_resp_code_out=trans_const.success
        THEN
            p_resp_code_out:='00';
        END IF;
        dbms_output.put_line('SUCCESS AND ROLLBACK......');
      --         ROLLBACK;
    EXCEPTION
        WHEN exp_reject_record THEN
            ROLLBACK;
        WHEN OTHERS THEN    --exception for main begin
            ROLLBACK TO l_auth_savepoint;
            BEGIN
                SELECT available_balance,
                       ledger_balance,
--                       cam_type_code,
                       account_id
                INTO
                    l_acct_balance,l_ledger_bal,
--                    l_cam_type_code,
                     l_acct_number
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
--                       ,cap_card_type
                INTO
                    l_prod_code
--                    ,l_prod_cattype
                FROM card
                WHERE card_num_hash=l_hash_pan;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Problem while selecting data from CARD'
                    ||p_resp_code_out
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
            END;

            BEGIN
                SELECT response_code,
                       response_code,
                       delivery_response_description
                INTO
                    p_resp_code_out,p_resp_code_out,p_resp_messge_out
                FROM delivery_channel_response_code
                WHERE channel_code=DECODE(
                        l_delivery_channel,
                        '13',
                        '17',
                        l_delivery_channel
                    ) AND response_code=p_resp_code_out;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_messge_out:='Problem while selecting data from response master 3'
                    ||p_resp_code_out
                    ||substr(sqlerrm,1,200);
                    p_resp_code_out:='89';
                    RAISE exp_reject_record;
            --ROLLBACK;
            END;

            dbms_output.put_line('p_comments_in ...'||p_comments_in);
            l_timestamp:=systimestamp;
            BEGIN
                INSERT INTO transaction_log (
                    transaction_sqid,
                    msg_type,
                    rrn,
                    delivery_channel,
                    terminal_id,
--                    TRANSACTION_DATE,
                    transaction_code,
--                    TRANSACTION_DESC,
--                    txn_mode,
                    transaction_status,
--                    RESPONSE_ID,
                    transaction_date,
                    transaction_time,
                    card_number,
                    topup_card_number,
                    topup_account_number,
--                    topup_acct_type,
--                    bank_code,
--                    total_amount,
                    rule_indicator,
--                    rulegroupid,
                    mccode,
                    tran_curr,
--                    addcharge,
                    product_id,
--                    CATEGORY_ID,
--                    tips,
--                    decline_ruleid,
--                    atm_name_location,
                    auth_id,
                    transaction_desc,
                    transaction_amount,
--                    preauthamount,
--                    partialamount,
--                    mccodegroupid,
--                    currencycodegroupid,
--                    transcodegroupid,
                    rules,
                    preauth_date,
--                    gl_upd_flag,
--                    system_trace_audit_no,
--                    instcode,
/*
                    feecode,
                    tranfee_amt,
                    servicetax_amt,
                    cess_amt,
                    cr_dr_flag,
                    tranfee_cr_acctno,
                    tranfee_dr_acctno,
                    tran_st_calc_flag,
                    tran_cess_calc_flag,
                    tran_st_cr_acctno,
                    tran_st_dr_acctno,
                    tran_cess_cr_acctno,
                    tran_cess_dr_acctno,
*/
                    customer_card_nbr_encr,
                    topup_card_nbr_encr,
                    proxy_number,
                    reversal_code,
                    account_number,
                    account_balance,
                    ledger_balance,
                    response_id,
                    card_status,
--                    fee_plan,
--                    csr_achactiontaken,
                    error_msg,
                    process_flag,
--                    acct_type,
--                    time_stamp,
                    remark,
                    ins_date,
                    correlation_id,
                    is_financial,
                    partner_id,
                    issuer_id,
                    cr_dr_flag,
                    business_date
                ) VALUES (
                    transaction_id_seq.NEXTVAL,
                    p_msg_in,
                    l_rrn,
                    l_delivery_channel,
                    '0',--P_TERM_ID,
--                    l_trans_date,
                    l_txn_code,
--                    l_txn_type,
--                    p_txn_mode_in,
                    DECODE(
                        p_resp_code_out,
                        '00',
                        'C',
                        'F'
                    ),
--                    p_resp_code_out,
                    l_business_date,
                    substr(l_business_time,1,10),
                    l_hash_pan,
                    NULL,
                    NULL,--P_topup_acctno    ,
--                    NULL,--P_topup_accttype,
--                    NULL,--P_BANK_CODE,
--                    NULL,--TRIM(TO_CHAR(NVL(V_TOTAL_AMT,0),'99999999999999990.99')),
                    '',
--                    '',
                    NULL,--P_MCC_CODE,
                    p_curr_code_in,
--                    NULL,-- P_add_charge,
                    l_prod_code,
--                    l_prod_cattype,
--                    0,
--                    '',
--                    '',
                    l_auth_id,
                    l_tran_desc,--V_NARRATION,
                    NULL,--TRIM(TO_CHAR(NVL(V_TRAN_AMT,0),'99999999999999990.99')),
--                    '0.00',
--                    '0.00',
--                    '',
--                    '',
--                    '',
                    '',
                    '',
--                    NULL,--V_GL_UPD_FLAG,
--  	                  p_stan_in,
--                    p_inst_code_in,
/*
                    NULL,--V_FEE_CODE,
                    NULL,--NVL(V_FEE_AMT,0),
                    NULL,--NVL(V_SERVICETAX_AMOUNT,0),
                    NULL,--NVL(V_CESS_AMOUNT,0),
                    NULL,--V_DR_CR_FLAG,
                    NULL,--V_FEE_CRACCT_NO,
                    NULL,--V_FEE_DRACCT_NO,
                    NULL,--V_ST_CALC_FLAG,
                    NULL,--V_CESS_CALC_FLAG,
                    NULL,-- V_ST_CRACCT_NO,
                    NULL,--V_ST_DRACCT_NO,
                    NULL,--V_CESS_CRACCT_NO,
                    NULL,--V_CESS_DRACCT_NO,
*/
                    l_encr_pan,
                    NULL,
                    l_proxunumber,
                    p_rvsl_code_in,
                    l_acct_number,
                    nvl(l_acct_balance,0),
                    nvl(l_ledger_bal,0),
                    p_resp_code_out,
                    l_applpan_cardstat,
--                    NULL,--V_FEE_PLAN,
--                    NULL,--P_FEE_FLAG,
                    p_resp_messge_out,
                    'E',
--                    l_cam_type_code,
--                    l_timestamp,
                    substr(p_comments_in,1,1000),
                    SYSDATE,
                    p_correlation_id_in,
                    l_tran_type,
                    v_partner_id,
                    v_issuer_id,
                    l_dr_cr_flag,
                    l_business_date
                );

                dbms_output.put_line('trans_log SQL%ROWCOUNT...'||SQL%rowcount);
            EXCEPTION
                WHEN OTHERS THEN
                 --ROLLBACK;
                    p_resp_code_out:='89'; -- Server Declione
                    p_resp_messge_out:='Problem while inserting data into transaction log  '
                    ||substr(sqlerrm,1,300);
                -- return;
            END;

    END;

END VMSB2BAPIV1;

/
SHOW ERROR