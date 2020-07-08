  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_LOG_CARDSTAT_CHNGE" (
    p_hash_pan              IN VARCHAR2,
    p_encr_pan              IN RAW,
    p_auth_id               IN VARCHAR2,
    p_txn_code              IN VARCHAR2,
    p_orgnl_rrn             IN VARCHAR2,
    p_orgnl_business_date   IN VARCHAR2,
    p_orgnl_business_time   IN VARCHAR2,
    p_resp_code             OUT VARCHAR2,
    p_errmsg                OUT VARCHAR2
) AS
/*****************************************************************************
    * Modified by          : Chandru R
    * Modified Date        : 30-OCT-19
    * Modified For         : VMSCL-838 Database script to generate the CCF file for IH cards with updated serial number 
    * Reviewer             : Raja Gopal
    * Build Number         : R12.B2
******************************************************************************/
    v_txn_desc         transaction.transaction_description%TYPE;
    v_card_stat        card.card_status%TYPE;
    v_acct_no          account.account_number%TYPE;
    v_acct_balance     account_purse.available_balance%TYPE;
    v_ledger_balance   account_purse.ledger_balance%TYPE;
    v_rrn              VARCHAR2(20);
    v_tran_seq_id      NUMBER;
	
	l_issuer_id		clp_configuration.product.issuer_id%type;
	l_product_id	clp_configuration.product.product_id%type;
	l_partner_id	clp_configuration.product.partner_id%type;
	l_purse_id		clp_configuration.product_purse.purse_id%type;
	l_account_number clp_transactional.account.account_number%type;
	l_proxy_number	clp_transactional.card.proxy_number%type;
	l_account_purse_id clp_transactional.account_purse.account_purse_id%type;

BEGIN
    p_errmsg := 'OK';
    p_resp_code := trans_const.success;
    BEGIN
        SELECT
            TO_CHAR(systimestamp,'yymmddHH24MISS')
             || seq_passivestatupd_rrn.NEXTVAL
        INTO
            v_rrn
        FROM
            dual;

    EXCEPTION
        WHEN OTHERS THEN
         --p_resp_code := '21';
            p_resp_code := trans_const.system_error;
            p_errmsg := 'Error while getting RRN '
             || substr(sqlerrm,1,200);
            return;
    END;

    BEGIN
        SELECT
            account_id,
            card_status,
			proxy_number
        INTO
            v_acct_no,v_card_stat,l_proxy_number
        FROM
            card
        WHERE
            card_num_hash = p_hash_pan;

    EXCEPTION
        WHEN OTHERS THEN
         --p_resp_code := '21';
            p_resp_code := trans_const.system_error;
            p_errmsg := 'Error while selecting Card details-'
             || substr(sqlerrm,1,200);
            return;
    END;

    BEGIN
        SELECT
            available_balance,
            ledger_balance,
			p.PRODUCT_ID,
			pp.purse_id,
			p.partner_id,
			p.issuer_id,
			a.account_number,
			ap.account_purse_id
        INTO
            v_acct_balance,v_ledger_balance,l_product_id,l_purse_id,l_partner_id,l_issuer_id,l_account_number,l_account_purse_id
        FROM
            clp_transactional.account_purse ap,clp_configuration.product_purse pp,clp_configuration.product p,clp_transactional.account a
        WHERE
			ap.account_id = v_acct_no
		and
			a.account_id = ap.account_id
		and
			a.product_id = ap.product_id
		and
			ap.product_id = pp.product_id	
		and
			ap.product_id = p.product_id	
		and
			ap.purse_id = pp.purse_id
		and
			pp.is_default = 'Y';

    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := '12';
            p_errmsg := 'Invalid Card ';
            return;
        WHEN OTHERS THEN
            p_resp_code := trans_const.card_not_found;
            p_errmsg := 'Error while selecting acct details-'
             || substr(sqlerrm,1,200);
            return;
    END;

    dbms_output.put_line('p_txn_code' || p_txn_code);
    BEGIN
        SELECT
            transaction_description
        INTO
            v_txn_desc
        FROM
            transaction
        WHERE
            transaction_code = p_txn_code;

    EXCEPTION
        WHEN no_data_found THEN
         --p_resp_code := '12';
            p_resp_code := trans_const.system_error;
            p_errmsg := 'Txn not defined for txn_code-' || p_txn_code;
            return;
        WHEN OTHERS THEN
         --p_resp_code := '21';
            p_resp_code := trans_const.system_error;
            p_errmsg := 'Error while selecting txn details-'
             || substr(sqlerrm,1,200);
            return;
    END;

    SELECT
        transaction_id_seq.NEXTVAL
    INTO
        v_tran_seq_id
    FROM
        dual;

--  dbms_out.put_line(TO_CHAR (SYSTIMESTAMP,'yymmddHH24MISSFF3'));

    BEGIN
        dbms_output.put_line('v_tran_seq_id ..' || v_tran_seq_id);
        INSERT INTO transaction_log (
            transaction_sqid,
            msg_type,
            rrn,
            delivery_channel,
            transaction_code,
            transaction_desc,
            card_number,
            customer_card_nbr_encr,
            transaction_date,
            transaction_time,
            transaction_status,
            req_resp_code,
            auth_id,
            ins_date,
            response_id,
            --orgnl_rrn,
            --orgnl_transaction_date,
            --orgnl_transaction_time,
            account_balance,
            ledger_balance,
            card_status,
            remark,
            --process_msg,
			
			issuer_id,
			PRODUCT_ID,
			partner_id,
			account_id,
			purse_id,
			customer_id,
			account_number,
			is_financial,
			cr_dr_flag,
			error_msg,
			business_date,
			transaction_amount,
			auth_amount, 
			tranfee_amount, 
			tran_reverse_flag, 
			last_upd_date,
			proxy_number,
			reversal_code,
			reason_code,
			opening_ledger_balance,
			opening_available_balance,
			ORGNL_AUTH_AMOUNT,
			ORGNL_TRAN_AMOUNT,
			account_purse_id
        ) VALUES (
            v_tran_seq_id,
			'0200',
            --v_rrn,
			(CASE WHEN p_orgnl_rrn like '%IH%' THEN
					   p_orgnl_rrn
				  ELSE	
					   v_rrn
			END),
            '06',
			p_txn_code,
            v_txn_desc,
            p_hash_pan,
            p_encr_pan,
            TO_CHAR(SYSDATE,'yyyymmdd'),
            TO_CHAR(SYSDATE,'hh24miss'),
            'C',
            '00',
            p_auth_id,
            SYSDATE,
            'R0001',
            --p_orgnl_rrn,
            --trunc(p_orgnl_business_date),
            --p_orgnl_business_time,
            nvl(v_acct_balance,0),
            nvl(v_ledger_balance,0),
            v_card_stat,
            DECODE(p_auth_id,'PRGE','Card has been removed from store inventory and cannot be issued'),
            --'Successful',
			
			l_issuer_id,
			l_product_id,
			l_partner_id,
			v_acct_no,
			l_purse_id,
			v_acct_no,
			l_account_number,
			'N',
			'NA',
			'OK',
			--to_char(p_orgnl_business_date,'DD-MON-YY'),
			sysdate,
			0,
			0,
			0,
			'N',
			--to_char(p_orgnl_business_date,'DD-MON-YY'),
			sysdate,
			l_proxy_number,
			0,
			0,
			nvl(v_ledger_balance,0),
			nvl(v_acct_balance,0),
			0,
			0,
			l_account_purse_id
        );

    EXCEPTION
        WHEN OTHERS THEN
--         p_resp_code := '21';
            p_resp_code := trans_const.system_error;
            p_errmsg := 'Error while logging system initiated card status change'
             || substr(sqlerrm,1,200);
            return;
    END;

EXCEPTION
    WHEN OTHERS THEN
        p_resp_code := trans_const.system_error;
        p_errmsg := ' Error from main '
         || substr(sqlerrm,1,200);
END;
/
SHOW ERRORS;

-------------------------------------------------------------------------------------------------------------------------