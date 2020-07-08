  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_CARD_ORDER" ( 
    p_order_id       IN VARCHAR2,
    p_line_item_id   IN VARCHAR2
    --p_err_msg OUT VARCHAR2
) AS
	/*****************************************************************************
		* Modified by          : KARTHIK S
		* Modified Date        : 03-SEP-19
		* Modified For         : JIRA (VMSCL-772)
		* Reviewer             : Raja Gopal
		* Build Number         : R11.B5
	******************************************************************************/	
/*****************************************************************************
    * Modified by          : Chandru R
    * Modified Date        : 31-JULY-19
    * Modified For         : Move the usage limits fees from card to Account Purse and logic change on Transactions - JIRA 716
    * Reviewer             : Raja Gopal
    * Build Number         : R11.B3
******************************************************************************/

/*****************************************************************************
    * Modified by          : Chandru R
    * Modified Date        : 18-JULY-19
    * Modified For         : Order processing dynamic purse id creation for default purse (Retail/B2B/Digital) - VMSCL-713
    * Reviewer             : Raja Gopal
    * Build Number         : R11.B1
******************************************************************************/
    v_quantity            NUMBER;
    v_pending_qty         NUMBER;
    v_card_range_id       NUMBER := 0;
    v_tot_available_inv   NUMBER;
    v_available_inv       NUMBER;
    v_ctrl_num            NUMBER;
    v_inv_req             NUMBER;
    v_count               NUMBER;
    v_line_item_id        NUMBER;
    v_partner_id          NUMBER;
    v_issued_inventory    NUMBER;
    v_card_range_order    NUMBER := 0;
    v_product_id          NUMBER;
    v_max_inv_check       NUMBER;
    v_inv_card_enough     NUMBER := 0;
    v_purse_id            NUMBER;
    v_purse_type_id       NUMBER;
    v_currency_code       VARCHAR2(3);
    v_upc                 VARCHAR2(20);
    v_expiry_date         TIMESTAMP;
    v_expiry_err_msg      VARCHAR2(200) := 'OK';
    v_max_ctrl            VARCHAR2(20);
    v_max_val             NUMBER;
    v_active_from         DATE;
    v_usage_limit         CLOB;
    l_exception EXCEPTION;
    p_err_msg             VARCHAR2(200);
    v_purse_type_name     CLP_TRANSACTIONAL.ACCOUNT_PURSE.PURSE_TYPE%TYPE;--Modified by Farahan for Multi Purse
    v_defaultcardstatus   VARCHAR2(3 CHAR);
      v_formFactor          VARCHAR2(20);
      v_cnt NUMBER;
      v_cnt_failed NUMBER;
    l_INS_USER      NUMBER;                         --19 July
    Y              NUMBER;
    l_order_type        ORDER_DETAILS.order_type%type;
    l_order_status      ORDER_DETAILS.order_type%type;
    l_product_name      CLP_CONFIGURATION.PRODUCT.product_name%type;
    l_subject        VARCHAR2(1000);
    l_message        VARCHAR2(1000);
	l_package_id      CLP_CONFIGURATION.PRODUCT_PACKAGE.PACKAGE_ID%TYPE;
    v_user_email     VARCHAR2(50);

   -- select the list of available card ranges for the product
    CURSOR c1 IS
        SELECT
            i.available_inventory,
            i.card_range_id,
            i.issued_inventory
        FROM
            product_card_range c,
            card_range_inventory i
        WHERE
                product_id = v_product_id
            AND
                c.card_range_id = i.card_range_id
            AND
                i.available_inventory > 0
        ORDER BY c.card_range_order;

BEGIN
    p_err_msg := 'OK';

 ---selecting partner_id,product_id,quantity and package_id from order_line_item_table
    BEGIN
        SELECT
            partner_id,
            product_id,
            quantity,
            PACKAGE_ID
        INTO
            v_partner_id,v_product_id,v_quantity,l_package_id
        FROM
            order_line_item
        WHERE
                order_id = p_order_id
            AND
                line_item_id = p_line_item_id
            AND
                order_status = ( 'ORDER-IN-PROGRESS' );

    EXCEPTION
        WHEN no_data_found THEN
            p_err_msg := 'THERE IS NO DATA FOUND FOR GIVEN ORDER_ID AND LINE_ITME_ID'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE SELECTING PARTNER_ID,PRODUCT_ID,QUANTITY '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

-------------FOR ORDER STATUS UPDATE
/*BEGIN
UPDATE ORDER_LINE_ITEM SET ORDER_STATUS='ORDER-IN-PROGRESS'
WHERE order_id=p_order_id AND line_item_id=p_line_item_id
    AND ORDER_STATUS='APPROVED';
 EXCEPTION
  WHEN OTHERS THEN
    p_err_msg:='ERROR WHILE UPDATING ORDER STATUS ' ||SUBSTR(sqlerrm,1,200);
    raise l_exception;
END;*/
    --------------------- for creating default_purse
  ---selecting purse_id,purse_type_id,currency_code,upc from product_purse table

    BEGIN
        SELECT
            p.purse_id,
            p.purse_type_id,
            p.currency_code,
            p.upc,
            pt.purse_type_name
        INTO
            v_purse_id,v_purse_type_id,v_currency_code,v_upc,v_purse_type_name
        FROM
            purse p,
            product_purse pp,
            purse_type pt
        WHERE
                pp.product_id = v_product_id
            AND
                is_default = 'Y'
            AND
                p.purse_id = pp.purse_id
            AND
                p.purse_type_id = pt.purse_type_id;

    EXCEPTION
        WHEN no_data_found THEN
            p_err_msg := 'THERE IS NO DATA FOUND FOR GIVEN DEFAULT_PURSE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE SELECTING DEFAULT_PURSE DETAILS '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

  ---TO CHECK SUM OF AVAILABLE INVENTORY and THROW ERROR IF IT IS NOT ENOUGH INVENTORY for the PRODUCT

    BEGIN
        SELECT
            nvl(SUM(available_inventory),0)
        INTO
            v_tot_available_inv
        FROM
            product_card_range prod_card_range,
            card_range_inventory
        WHERE
                product_id = v_product_id
            AND
                prod_card_range.card_range_id = card_range_inventory.card_range_id;

    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE GETTING AVAILABLE INVENTORY COUNT'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

-----------------getting expiry date

    BEGIN
        get_expiry_date_card(v_product_id,v_expiry_date,v_expiry_err_msg);
        IF
            v_expiry_err_msg <> 'OK'
        THEN
            RAISE l_exception;
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE GETTING EXPIRY DATE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;
--------------getting default card_status from prduct

    BEGIN
        SELECT
            trunc(TO_DATE(
                po.attributes.product.activeFrom,
                'MM/DD/YYYY'
            ) ),
            pp.attributes.Limits,
            po.attributes.General.defaultCardStatus,
             po.attributes.Product.formFactor
        INTO
            v_active_from,v_usage_limit,v_defaultcardstatus,v_formFactor
        FROM
            product po,clp_configuration.product_purse pp
        WHERE
			po.product_id = pp.product_id
			and pp.is_default = 'Y'
			and po.product_id = v_product_id;

        IF
            v_active_from IS NULL
        THEN
            v_active_from := SYSDATE;
        END IF;
    EXCEPTION
        WHEN no_data_found THEN
            v_active_from := SYSDATE;
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE GETTING DEFAULT CARD STATUS AND ACTIVE DATE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;
-----------

    IF
        v_tot_available_inv < v_quantity
    THEN
        p_err_msg := 'Not sufficient inventory';
        RAISE l_exception;
    END IF;
  -----------------------UPDATING CARD_RANGE_INVENTORY TABLE IS_USED FLAG
    BEGIN
        UPDATE card_range_inventory
            SET
                is_used = 'Y'
        WHERE
            card_range_id IN (
                SELECT
                    card_range_id
                FROM
                    product_card_range
                WHERE
                    product_id = v_product_id
            );

     ----   COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE UPDATING CARD_RAGNE_INVENTORY table IS_USED FLAG'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

 -- loop and process the order

    v_pending_qty := v_quantity;
    FOR x IN c1 LOOP
        EXIT WHEN v_pending_qty = 0;
        IF
            v_pending_qty > x.available_inventory
        THEN
            v_pending_qty := v_pending_qty - x.available_inventory;
            v_inv_req := x.available_inventory;
        ELSE
            v_inv_req := v_pending_qty;
            v_pending_qty := 0;
        END IF;

      ---UPDATING inv_flag in INVENTORY table

        BEGIN
            UPDATE inventory
                SET
                    inv_flag = 'I'
            WHERE
                    card_range_id = x.card_range_id
                AND
                    inv_flag = 'N'
                AND
                    ctrl_num < x.issued_inventory + v_inv_req;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE UPDATING INVENTORY table'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
-------------------GETTING SEQUENCE VALUE

        BEGIN
            SELECT
                max_ctrl
            INTO
                v_max_ctrl
            FROM
                order_ctrl
            WHERE
                product_id = v_product_id;
         --V_PRODUCT_ID;

        EXCEPTION
            WHEN no_data_found THEN
                v_max_ctrl := '000000';
                INSERT INTO order_ctrl VALUES ( v_product_id,v_max_ctrl );

        END;
   


-------------SELECTING INS DATE from ORDER Details
/*BEGIN
SELECT INS_USER INTO V_INS_USER FROM ORDER_DETAILS
WHERE order_id=p_order_id;

 EXCEPTION
  WHEN OTHERS THEN
    p_err_msg:='ERROR WHILE selecting INS_USER from ORDER details' ||SUBSTR(sqlerrm,1,200);
    raise l_exception;
END;*/

	--inserting data into order_line_item_dtl

        BEGIN
            INSERT INTO order_line_item_dtl (
                order_id,
                partner_id,
                order_line_item_id,
                card_num_hash,
                account_id,
                customer_code,
                address_id,
                ctrl_num,
                card_range_id,
                card_num_encr,
                pan_generation_date,
                expiry_date,
              --DISP_NAME,--(Data type varchar)
                product_id,
                card_num_mask --(june 22nd added)
            ) ( SELECT
                p_order_id,
                v_partner_id,
                p_line_item_id,
                card_num_hash,
                lpad(v_product_id,5,0)
                 || lpad(
                    v_max_ctrl + ROWNUM,
                    8,
                    0
                ),
                lpad(v_product_id,5,0)
                 || lpad(
                    v_max_ctrl + ROWNUM,
                    8,
                    0
                ),
                lpad(v_product_id,5,0)
                 || lpad(
                    v_max_ctrl + ROWNUM,
                    8,
                    0
                ),
                ctrl_num,
                card_range_id,
                card_num_encr,
                SYSDATE,
                v_expiry_date,
               --v_product_id,
                v_product_id,
                card_num_mask--(june 22nd added)
            FROM
                inventory
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1
            );

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO order_line_item_dtl table'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
            --------------INSERT into customer_profile

        BEGIN

            INSERT INTO customer_profile (
                customer_code,
                customer_id,              --9th July (we are using USER_ID,so that we are keeping CODE as ID)
                optinoptout_status,
                --address_id,             -- 18th July
                product_id,
                partner_id,
                first_name,
                middle_name,
                last_name
            ) SELECT
                customer_code,
                customer_code,            --9th July (we are using USER_ID,so that we are keeping CODE as ID)
                'Y',
                --address_id,             -- 18th July
                v_product_id,
                v_partner_id,
                fn_emaps_main('FIRST_NAME'),
                fn_emaps_main('MIDDLE_NAME'),
                fn_emaps_main('LAST_NAME')
            FROM
                order_line_item_dtl
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO customer profile:'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;


----------------------INSERTING INTO CUSTOMER TABLE

        BEGIN
            INSERT INTO CUSTOMER (
                customer_code,
                --address_id,               -- 18th July
                product_id,
                partner_id,
                optinoptout_status,
                ins_date

            ) SELECT
                customer_code,
                --address_id,               -- 18th July
                v_product_id,
                v_partner_id,
                'Y',
                sysdate
            FROM
                order_line_item_dtl
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO customer:'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;


----------------------INSERTING INTO address TABLE

        BEGIN
            INSERT INTO address (
                address_id,
                customer_code,
                address_one,
                address_two,
                address_three,
                address_four,
                ADDR_FLAG,                   -- 18th July

				INS_DATE					 -- 3rd Oct

            ) SELECT
                address_id,
                customer_code,
                fn_emaps_main('ADDRESS_1'),
                fn_emaps_main('ADDRESS_2'),
                fn_emaps_main('ADDRESS_3'),
                fn_emaps_main('ADDRESS_4'),
                'P',                         -- 18th July

				SYSDATE					 -- 3rd Oct

            FROM
                order_line_item_dtl
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO address:'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
      -------------------INSERTING INTO ACCOUNT TABLE

        BEGIN
            INSERT INTO account (
            account_id,
            product_id,
            account_number,
            type_code,                       --July 25th 2018
            ins_date

            ) SELECT
                account_id,
                v_product_id,
                'A'
                 || lpad(v_product_id,5,0)
                 || lpad(
                    v_max_ctrl + ROWNUM,
                    12,
                    '0'
                ),
                1,                          --July 25th 2018
                sysdate

            FROM
                order_line_item_dtl
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO account:'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

	---Insert on cards table

        BEGIN
            INSERT INTO card (
                card_num_hash,
                card_num_encr,
                serial_number,
                account_id,
                customer_code,
                address_id,
                product_id,
                card_range_id,
                proxy_number,
                card_status,
                expiry_date,
                pan_generation_date,
                date_of_activation,
                --usage_limit,
                --usage_fee,
                card_num_mask,                                    --(june 22nd)
                card_id,                                          --(july 04 th)

                PIN_FLAG,                                         --(July 13 th)

                MBR_NUMB,                                          --(Aug 31 st)

				INS_DATE,					 				   -- 3rd Oct

                INS_USER,                                      --1st dec
                LAST_UPD_USER,
                LAST_UPD_DATE,
				CARDPACK_ID

            ) ( SELECT
                card_num_hash,
                card_num_encr,
                DECODE(v_formFactor, 				--- Modified on (25-Sep-2019)
				'IH', 
				lpad(v_product_id,4,0)
                 || lpad(
                    v_max_ctrl + ROWNUM,
                    11,
                    0
                ),
				0),
                account_id,
                customer_code,
                address_id,
                v_product_id,
                card_range_id,
                lpad(v_product_id,4,0)
                 || lpad(
                    v_max_ctrl + ROWNUM,
                    8,
                    0
                ),
            --    v_defaultcardstatus,          ------ Aug (31 st)
               -- '98',
               DECODE (v_formFactor,
                                       'Virtual', v_defaultcardstatus,
                                       '98'),
                v_expiry_date,
                SYSDATE,
            ---    v_active_from,               -------commented date will be inserted when card is activating
                 null,
                --v_usage_limit,
                --'{}',
                card_num_mask,                                     --(june 22nd)

                -- 4 th July
                lpad(3,7,0)
                 || ( lpad(v_product_id,4,0)
                 || lpad(
                    v_max_ctrl + ROWNUM,
                    8,
                    0
                ) ),

                'N',

                '000',                                          --(Aug 31 st)
                SYSDATE,
                1,
                1,
				SYSDATE,
                l_package_id				-- 3rd Oct

            FROM
                order_line_item_dtl
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1
            );

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING CARD table in TRANSACTIONAL schema'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

    --------------------insert into ACCOUNT_PURSE

        BEGIN
            INSERT INTO account_purse (
                account_id,
                product_id,
                purse_id,
                ledger_balance,
                available_balance,
                purse_type,
                currency_code,
                upc,
                purse_type_id,
                ins_date,
                
                account_purse_id                           --Modified for JIRA VMSCL-713 on 18th July 2019
                
            ) SELECT
                account_id,
                v_product_id,
                v_purse_id,
                0,
                0,
                v_purse_type_name,
                v_currency_code,
                v_upc,
                v_purse_type_id,
                sysdate,
                
                clp_transactional.seq_account_purse_id.nextval  --Modified for JIRA VMSCL-713 on 18th July 2019
                
            FROM
                order_line_item_dtl
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO account:'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

	--------------------insert into ACCOUNT_PURSE_USAGE

        BEGIN
            INSERT INTO clp_transactional.ACCOUNT_PURSE_USAGE (
                ACCOUNT_ID,
				PURSE_ID,
				USAGE_FEE,
				USAGE_LIMIT,
				ACCOUNT_PURSE_USAGE_SQID,
				INS_DATE,													--Modified for JIRA VMSCL-713 on 18th July 2019
				LAST_UPD_DATE                         
            ) SELECT
                account_id,
				v_purse_id,
				'{}',
				'',
				CLP_TRANSACTIONAL.SEQ_ACCOUNT_PURSE_USAGE_ID.nextval,
				SYSDATE,
				SYSDATE   
            FROM
                clp_order.order_line_item_dtl
            WHERE
                    card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO ACCOUNT_PURSE_USAGE :'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
   	
    --------------------insert into ACCOUNT_CUSTOMER_PROFILE

        BEGIN
            INSERT INTO CLP_TRANSACTIONAL.ACCOUNT_CUSTOMER_PROFILE (
                account_id,
                ACCOUNT_PRODUCT_ID,
                ACCOUNT_NUMBER,
                CUSTOMER_CODE


            ) SELECT
                b.account_id,
                v_product_id,
                b.ACCOUNT_NUMBER,
                a.CUSTOMER_CODE

                FROM CUSTOMER_PROFILE a,ACCOUNT b,order_line_item_dtl c
                WHERE c.ACCOUNT_ID = b. ACCOUNT_ID
                AND c.CUSTOMER_CODE = a.CUSTOMER_CODE
                AND card_range_id = x.card_range_id
                AND
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + v_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE INSERTING INTO ACCOUNT_CUSTOMER_PROFILE:'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;


	---Updating card_range inventory table

        BEGIN
            UPDATE card_range_inventory
                SET
                    issued_inventory = x.issued_inventory + v_inv_req,
                    available_inventory = x.available_inventory - v_inv_req,
                    is_inventory_generated = 'Y'
            WHERE
                card_range_id = x.card_range_id;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE UPDATING CARD_RANGE_INVENTORY TABLE'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

        BEGIN
            UPDATE order_ctrl
                SET
                    max_ctrl = (
                        SELECT
                            substr(
                                MAX(customer_code),
                                -6,
                                6
                            )
                        FROM
                            order_line_item_dtl
                        WHERE
                            product_id = v_product_id
                    )
            WHERE
                product_id = v_product_id;

        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := 'ERROR WHILE UPDATING ORDER_CTRL TABLE'
                 || substr(sqlerrm,1,200);
        END;

    END LOOP;
 -----------

 -------------FOR ORDER STATUS UPDATE ON  ORDER_LINE_ITEM AND ORDER_DETAILS TABLE

    BEGIN
        UPDATE order_line_item
            SET
                 order_status = DECODE (v_formFactor,
                                       'Virtual', 'SHIPPED',
                                       'ORDER-GENERATED'),
                 CCF_FLAG = DECODE (v_formFactor, 'Virtual',2,1),
                 SHIPPING_DATETIME = DECODE (v_formFactor,
                                       'Virtual', sysdate,
                                       null)
        WHERE
                order_id = p_order_id
            AND
                line_item_id = p_line_item_id
            AND
                order_status = 'ORDER-IN-PROGRESS';

    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE UPDATING ORDER STATUS IN ORDER_LINE_ITEM'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;
----------

--    BEGIN
--        UPDATE order_details
--            SET
--                order_status = 'ORDER-GENERATED'
--        WHERE
--                order_id = p_order_id
--            AND
--                partner_id = v_partner_id;
--
--    EXCEPTION
--        WHEN OTHERS THEN
--            p_err_msg := 'ERROR WHILE UPDATING ORDER STATUS IN ORDER_DETAILS'
--             || substr(sqlerrm,1,200);
--            RAISE l_exception;
--    END;
 -----------------------UPDATING CARD_RANGE_INVENTORY TABLE IS_USED FLAG

    BEGIN
        UPDATE card_range_inventory
            SET
                is_used = 'N'
        WHERE
            card_range_id IN (
                SELECT
                    card_range_id
                FROM
                    product_card_range
                WHERE
                    product_id = v_product_id
            );

    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE UPDATING CARD_RAGNE_INVENTORY table IS_USED FLAG'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    BEGIN
        INSERT INTO job_error_log (
            order_id,
            card_range_id,
            job_name,
            job_date,
            error_msg
        ) VALUES (
            p_order_id,
            NULL,
            'schedule_order',
            SYSDATE,
            'GIVEN ORDER PROCESSED SUCCESSFULLY'
        );

    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE INSERTING JOB_ERROR_LOG table'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

     BEGIN

       select count(1) INTO v_cnt from order_line_item where order_status = 'ORDER-GENERATED' AND  order_id = p_order_id ;

       select count(1) INTO v_cnt_failed from order_line_item where order_status IN ('ORDER-GENERATED','SHIPPED') AND  order_id = p_order_id ;

        UPDATE order_details
            SET order_status =
              (CASE WHEN v_cnt > 0  THEN 'ORDER-GENERATED' WHEN v_cnt_failed = 0 THEN 'FAILED' ELSE 'SHIPPED' END)
        WHERE
                order_id = p_order_id;


        EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE UPDATING ORDER STATUS IN ORDER_DETAILS'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    COMMIT;


    -- added for email notification starts
    SELECT OD.order_type,OD.order_status into l_order_type,l_order_status
     from ORDER_LINE_ITEM OI,ORDER_DETAILS OD where
      OD.ORDER_ID=OI.ORDER_ID AND
     OD.order_id= p_order_id and line_item_id= p_line_item_id ;

SELECT INS_USER INTO l_INS_USER FROM ORDER_DETAILS WHERE order_id=p_order_id;
--SELECT user_email INTO v_user_email FROM clp_user u WHERE u.user_id =l_INS_USER;

IF
        l_order_type = 'RETAIL' AND (l_order_status = 'ORDER-GENERATED' OR l_order_status = 'FAILED' )
    THEN
        BEGIN
            SELECT
                p.product_name
            INTO
                l_product_name
            FROM
                clp_order.order_line_item olt,
                product p
            WHERE
                    olt.product_id = p.product_id
                AND
                    olt.order_id =p_order_id
                AND
                    ROWNUM = 1;

        END;

        l_subject := 'Retail Order Status';
        IF
            l_order_status = 'ORDER-GENERATED' AND l_order_type = 'RETAIL'
        THEN
            l_message := 'Order Generated Successfully for Order number - '
             ||  p_order_id
             || '\r\n'
             || 'Product - '
             || l_product_name
             || '\r\n'
             || 'Order date - '
             || SYSDATE
             || '\r\n'
             || 'Order Type - '
             || 'RTL';
        ELSIF l_order_status = 'FAILED' AND l_order_type = 'RETAIL' THEN
            l_message := 'Order Generation failed for Order number - '
             || p_order_id
             || '\r\n'
             || 'Product - '
             || l_product_name
             || '\r\n'
             || 'Order date - '
             || SYSDATE
             || '\r\n'
             || 'Order Type - '
             || 'RTL';
        END IF;
        clp_util.send_mail_order(
            p_order_id,
            l_INS_USER,
            l_subject,
            l_message

        );

     END IF;
-- added for email notification ends

EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
        BEGIN
            UPDATE order_line_item
                SET
                    order_status = 'FAILED'
            WHERE
                    order_id = p_order_id
                AND
                    line_item_id = p_line_item_id
                AND
                    order_status = 'ORDER-IN-PROGRESS';

        END;

        INSERT INTO job_error_log (
            order_id,
            card_range_id,
            job_name,
            job_date,
            error_msg
        ) VALUES (
            p_order_id,
            NULL,
            'schedule_order',
            SYSDATE,
            p_err_msg
        );

        COMMIT;
    WHEN OTHERS THEN
        ROLLBACK;
        p_err_msg := 'ERROR WHILE GENERATING SER_NUM'
         || substr(sqlerrm,1,200);
        BEGIN
            UPDATE order_line_item
                SET
                    order_status = 'FAILED'
            WHERE
                    order_id = p_order_id
                AND
                    line_item_id = p_line_item_id
                AND
                    order_status = 'ORDER-IN-PROGRESS';

        END;

        INSERT INTO job_error_log (
            order_id,
            card_range_id,
            job_name,
            job_date,
            error_msg
        ) VALUES (
            p_order_id,
            NULL,
            'schedule_order',
            SYSDATE,
            p_err_msg
        );

        COMMIT;

        
END;

/
SHOW ERRORS;
--------------------------------------------------------------------------------------------------------------------------------------------