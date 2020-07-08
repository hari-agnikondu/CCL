CREATE OR REPLACE PACKAGE BODY clp_order.pkg_digital_order_process AS

    PROCEDURE order_process_digital (
        p_order_id_in       IN ORDER_LINE_ITEM.ORDER_ID%type,
        p_line_item_id_in   IN ORDER_LINE_ITEM.LINE_ITEM_ID%type
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
	
        l_quantity            order_line_item.quantity%TYPE;
        l_pending_qty         NUMBER;
        l_tot_available_inv   clp_inventory.card_range_inventory.available_inventory%TYPE;
        l_inv_req             NUMBER;
        l_partner_id          order_line_item.partner_id%TYPE;
        l_product_id          order_line_item.product_id%TYPE;
        l_purse_id            clp_configuration.purse.purse_id%TYPE;
        l_purse_type_id       clp_configuration.purse.purse_type_id%TYPE;
        l_currency_code       clp_configuration.purse.currency_code%TYPE;
        l_upc                 clp_configuration.purse.upc%TYPE;
        l_expiry_date         TIMESTAMP;
        l_expiry_err_msg      VARCHAR2(200) := 'OK';
        l_max_ctrl            order_ctrl.max_ctrl%TYPE;
        l_active_from         DATE;
        l_usage_limit         CLOB;
        l_purse_type_name     clp_configuration.purse_type.purse_type_name%TYPE;
        l_defaultcardstatus   VARCHAR2(3 CHAR);
        l_formfactor          VARCHAR2(20);
        l_cnt                 NUMBER;
        l_cnt_failed          NUMBER;
        l_ins_user            order_details.ins_user%TYPE;
        l_order_type          order_details.order_type%TYPE;
        l_order_status        order_details.order_status%TYPE;
        l_product_name        clp_configuration.product.product_name%TYPE;
        l_subject             VARCHAR2(1000);
        l_message             VARCHAR2(1000);
        l_package_id          order_line_item.package_id%TYPE;
        l_err_msg             VARCHAR2(200);
        l_exception EXCEPTION;
    BEGIN
        l_err_msg := 'OK';

	--selecting partner_id,product_id,quantity and package_id from order_line_item_table
        BEGIN
            SELECT
                partner_id,
                product_id,
                quantity,
                package_id
            INTO
                l_partner_id,l_product_id,l_quantity,l_package_id
            FROM
                clp_order.order_line_item
            WHERE
                    order_id = p_order_id_in
                AND
                    line_item_id = p_line_item_id_in
                AND
                    order_status = ( 'ORDER-IN-PROGRESS' );

        EXCEPTION
            WHEN no_data_found THEN
                l_err_msg := 'THERE IS NO DATA FOUND FOR GIVEN ORDER_ID AND LINE_ITME_ID'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE SELECTING PARTNER_ID,PRODUCT_ID,QUANTITY '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

	--selecting purse_id,purse_type_id,currency_code,upc from product_purse table

        BEGIN
            SELECT
                p.purse_id,
                p.purse_type_id,
                p.currency_code,
                p.upc,
                pt.purse_type_name
            INTO
                l_purse_id,l_purse_type_id,l_currency_code,l_upc,l_purse_type_name
            FROM
                clp_configuration.purse p,
                clp_configuration.product_purse pp,
                clp_configuration.purse_type pt
            WHERE
                    pp.product_id = l_product_id
                AND
                    is_default = 'Y'
                AND
                    p.purse_id = pp.purse_id
                AND
                    p.purse_type_id = pt.purse_type_id;

        EXCEPTION
            WHEN no_data_found THEN
                l_err_msg := 'THERE IS NO DATA FOUND FOR GIVEN DEFAULT_PURSE'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE SELECTING DEFAULT_PURSE DETAILS '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

	--TO CHECK SUM OF AVAILABLE INVENTORY and THROW ERROR IF IT IS NOT ENOUGH INVENTORY for the PRODUCT

        BEGIN
            SELECT
                nvl(SUM(available_inventory),0)
            INTO
                l_tot_available_inv
            FROM
                clp_configuration.product_card_range prod_card_range,
                clp_inventory.card_range_inventory
            WHERE
                    product_id = l_product_id
                AND
                    prod_card_range.card_range_id = card_range_inventory.card_range_id;

        EXCEPTION
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE GETTING AVAILABLE INVENTORY COUNT'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

/*
	--Getting expiry date
	BEGIN
        GET_EXPIRY_DATE_CARD(l_product_id,l_expiry_date,l_expiry_err_msg);
        IF
            l_expiry_err_msg <> 'OK'
        THEN
            RAISE l_exception;
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            l_err_msg := 'ERROR WHILE GETTING EXPIRY DATE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;
*/

	--Getting default card_status from prduct

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
            l_active_from,l_usage_limit,l_defaultcardstatus,l_formFactor
        FROM
            product po,clp_configuration.product_purse pp
        WHERE
			po.product_id = pp.product_id
			and pp.is_default = 'Y'
			and po.product_id = l_product_id;

            IF
                l_active_from IS NULL
            THEN
                l_active_from := SYSDATE;
            END IF;
        EXCEPTION
            WHEN no_data_found THEN
                l_active_from := SYSDATE;
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE GETTING DEFAULT CARD STATUS AND ACTIVE DATE'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

        IF
            l_tot_available_inv < l_quantity
        THEN
            l_err_msg := 'Not sufficient inventory';
            RAISE l_exception;
        END IF;

	--UPDATING CARD_RANGE_INVENTORY TABLE IS_USED FLAG
        BEGIN
            UPDATE clp_inventory.card_range_inventory
                SET
                    is_used = 'Y'
            WHERE
                card_range_id IN (
                    SELECT
                        card_range_id
                    FROM
                        product_card_range
                    WHERE
                        product_id = l_product_id
                );

        EXCEPTION
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE UPDATING CARD_RAGNE_INVENTORY table IS_USED FLAG'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

	
    -- select the list of available card ranges for the product
    --loop and process the order

        l_pending_qty := l_quantity;
        FOR x IN (
            SELECT
                i.available_inventory,
                i.card_range_id,
                i.issued_inventory
            FROM
                clp_configuration.product_card_range c,
                clp_inventory.card_range_inventory i
            WHERE
                    product_id = l_product_id
                AND
                    c.card_range_id = i.card_range_id
                AND
                    i.available_inventory > 0
            ORDER BY c.card_range_order
        ) LOOP
            EXIT WHEN l_pending_qty = 0;
            IF
                l_pending_qty > x.available_inventory
            THEN
                l_pending_qty := l_pending_qty - x.available_inventory;
                l_inv_req := x.available_inventory;
            ELSE
                l_inv_req := l_pending_qty;
                l_pending_qty := 0;
            END IF;

		---UPDATING inv_flag in INVENTORY table

            BEGIN
                UPDATE clp_inventory.inventory
                    SET
                        inv_flag = 'I'
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        inv_flag = 'N'
                    AND
                        ctrl_num < x.issued_inventory + l_inv_req;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE UPDATING INVENTORY table'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--GETTING SEQUENCE VALUE

            BEGIN
                SELECT
                    max_ctrl
                INTO
                    l_max_ctrl
                FROM
                    clp_order.order_ctrl
                WHERE
                    product_id = l_product_id;

            EXCEPTION
                WHEN no_data_found THEN
                    l_max_ctrl := '000000';
                    INSERT INTO clp_order.order_ctrl VALUES ( l_product_id,l_max_ctrl );

            END;

		--inserting data into order_line_item_dtl

            BEGIN
                INSERT INTO clp_order.order_line_item_dtl (
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
                    p_order_id_in,
                    l_partner_id,
                    p_line_item_id_in,
                    card_num_hash,
                    lpad(l_product_id,5,0)
                     || lpad(
                        l_max_ctrl + ROWNUM,
                        8,
                        0
                    ),
                    lpad(l_product_id,5,0)
                     || lpad(
                        l_max_ctrl + ROWNUM,
                        8,
                        0
                    ),
                    lpad(l_product_id,5,0)
                     || lpad(
                        l_max_ctrl + ROWNUM,
                        8,
                        0
                    ),
                    ctrl_num,
                    card_range_id,
                    card_num_encr,
                    SYSDATE,
                    l_expiry_date,
               --l_product_id,
                    l_product_id,
                    card_num_mask--(june 22nd added)
                FROM
                    clp_inventory.inventory
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1
                );

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING INTO order_line_item_dtl table'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--INSERT into customer_profile

            BEGIN
                INSERT INTO clp_transactional.customer_profile (
                    customer_code,
                    customer_id,
                    optinoptout_status,
                    product_id,
                    partner_id,
                    first_name,
                    middle_name,
                    last_name
                ) SELECT
                    customer_code,
                    customer_code,
                    'Y',
                    l_product_id,
                    l_partner_id,
                    fn_emaps_main('FIRST_NAME'),
                    fn_emaps_main('MIDDLE_NAME'),
                    fn_emaps_main('LAST_NAME')
                FROM
                    clp_order.order_line_item_dtl
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING INTO customer profile:'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--INSERTING INTO CUSTOMER TABLE

            BEGIN
                INSERT INTO clp_transactional.customer (
                    customer_code,
                    product_id,
                    partner_id,
                    optinoptout_status,
                    ins_date
                ) SELECT
                    customer_code,
                    l_product_id,
                    l_partner_id,
                    'Y',
                    SYSDATE
                FROM
                    clp_order.order_line_item_dtl
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING INTO customer:'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--INSERTING INTO address TABLE

            BEGIN
                INSERT INTO clp_transactional.address (
                    address_id,
                    customer_code,
                    address_one,
                    address_two,
                    address_three,
                    address_four,
                    addr_flag,
                    ins_date
                ) SELECT
                    address_id,
                    customer_code,
                    fn_emaps_main('ADDRESS_1'),
                    fn_emaps_main('ADDRESS_2'),
                    fn_emaps_main('ADDRESS_3'),
                    fn_emaps_main('ADDRESS_4'),
                    'P',
                    SYSDATE
                FROM
                    clp_order.order_line_item_dtl
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING INTO address:'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--INSERTING INTO ACCOUNT TABLE

            BEGIN
                INSERT INTO clp_transactional.account (
                    account_id,
                    product_id,
                    account_number,
                    type_code,
                    ins_date
                ) SELECT
                    account_id,
                    l_product_id,
                    'A'
                     || lpad(l_product_id,5,0)
                     || lpad(
                        l_max_ctrl + ROWNUM,
                        12,
                        '0'
                    ),
                    1,
                    SYSDATE
                FROM
                    clp_order.order_line_item_dtl
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING INTO account:'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--Insert on cards table

            BEGIN
                INSERT INTO clp_transactional.card (
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
                    card_num_mask,
                    card_id,
                    pin_flag,
                    mbr_numb,
                    ins_date,
                    ins_user,
                    last_upd_user,
                    last_upd_date,
                    cardpack_id,
                    digital_pin
                ) ( SELECT
                    card_num_hash,
                    card_num_encr,
                    TO_CHAR('9999'
                     || lpad(l_product_id,4,0)
                     || lpad(
                        l_max_ctrl + ROWNUM,
                        12,
                        '0'
                    ) ),--4 digit identifier as 9999
                    account_id,
                    customer_code,
                    address_id,
                    l_product_id,
                    card_range_id,
                    lpad(l_product_id,4,0)
                     || lpad(
                        l_max_ctrl + ROWNUM,
                        8,
                        0
                    ),
                    DECODE(
                        l_formfactor,
                        'Virtual',
                        l_defaultcardstatus,
                        '99'
                    ),
                    l_expiry_date,
                    SYSDATE,
                    NULL,
                    --l_usage_limit,
                    --'{}',
                    card_num_mask,
                    lpad(3,7,0)
                     || ( lpad(l_product_id,4,0)
                     || lpad(
                        l_max_ctrl + ROWNUM,
                        8,
                        0
                    ) ),
                    'N',
                    '000',
                    SYSDATE,
                    1,
                    1,
                    SYSDATE,
                    l_package_id,
                    ceil(dbms_random.value() * 10000000000)
                FROM
                    clp_order.order_line_item_dtl
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1
                );

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING CARD table in TRANSACTIONAL schema'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--insert into ACCOUNT_PURSE

            BEGIN
                INSERT INTO clp_transactional.account_purse (
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
                    l_product_id,
                    l_purse_id,
                    0,
                    0,
                    l_purse_type_name,
                    l_currency_code,
                    l_upc,
                    l_purse_type_id,
                    SYSDATE,
					
					clp_transactional.seq_account_purse_id.nextval  --Modified for JIRA VMSCL-713 on 18th July 2019
					
                FROM
                    clp_order.order_line_item_dtl
                WHERE
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING INTO account:'
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
				INS_DATE,																--Modified for JIRA VMSCL-713 on 18th July 2019
				LAST_UPD_DATE                         
            ) SELECT
                account_id,
				l_purse_id,
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
                    ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1;

        EXCEPTION
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE INSERTING INTO ACCOUNT_PURSE_USAGE :'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

		--insert into ACCOUNT_CUSTOMER_PROFILE

            BEGIN
                INSERT INTO clp_transactional.account_customer_profile (
                    account_id,
                    account_product_id,
                    account_number,
                    customer_code
                ) SELECT
                    b.account_id,
                    l_product_id,
                    b.account_number,
                    a.customer_code
                FROM
                    clp_transactional.customer_profile a,
                    clp_transactional.account b,
                    clp_order.order_line_item_dtl c
                WHERE
                        c.account_id = b.account_id
                    AND
                        c.customer_code = a.customer_code
                    AND
                        card_range_id = x.card_range_id
                    AND
                        ctrl_num BETWEEN x.issued_inventory AND x.issued_inventory + l_inv_req - 1;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE INSERTING INTO ACCOUNT_CUSTOMER_PROFILE:'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

		--Updating card_range inventory table

            BEGIN
                UPDATE clp_inventory.card_range_inventory
                    SET
                        issued_inventory = x.issued_inventory + l_inv_req,
                        available_inventory = x.available_inventory - l_inv_req,
                        is_inventory_generated = 'Y'
                WHERE
                    card_range_id = x.card_range_id;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE UPDATING CARD_RANGE_INVENTORY TABLE'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

            BEGIN
                UPDATE clp_order.order_ctrl
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
                                product_id = l_product_id
                        )
                WHERE
                    product_id = l_product_id;

            EXCEPTION
                WHEN OTHERS THEN
                    l_err_msg := 'ERROR WHILE UPDATING ORDER_CTRL TABLE'
                     || substr(sqlerrm,1,200);
            END;

        END LOOP;

	--FOR ORDER STATUS UPDATE ON  ORDER_LINE_ITEM AND ORDER_DETAILS TABLE

        BEGIN
            UPDATE clp_order.order_line_item
                SET
                    order_status = DECODE(
                        l_formfactor,
                        'Virtual',
                        'SHIPPED',
                        'ORDER-GENERATED'
                    ),
                    ccf_flag = DECODE(
                        l_formfactor,
                        'Virtual',
                        2,
                        1
                    ),
                    shipping_datetime = DECODE(
                        l_formfactor,
                        'Virtual',
                        SYSDATE,
                        NULL
                    )
            WHERE
                    order_id = p_order_id_in
                AND
                    line_item_id = p_line_item_id_in
                AND
                    order_status = 'ORDER-IN-PROGRESS';

        EXCEPTION
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE UPDATING ORDER STATUS IN ORDER_LINE_ITEM'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

	--UPDATING CARD_RANGE_INVENTORY TABLE IS_USED FLAG

        BEGIN
            UPDATE clp_inventory.card_range_inventory
                SET
                    is_used = 'N'
            WHERE
                card_range_id IN (
                    SELECT
                        card_range_id
                    FROM
                        product_card_range
                    WHERE
                        product_id = l_product_id
                );

        EXCEPTION
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE UPDATING CARD_RAGNE_INVENTORY table IS_USED FLAG'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

        BEGIN
            INSERT INTO clp_order.job_error_log (
                order_id,
                card_range_id,
                job_name,
                job_date,
                error_msg
            ) VALUES (
                p_order_id_in,
                NULL,
                'schedule_order',
                SYSDATE,
                'GIVEN ORDER PROCESSED SUCCESSFULLY'
            );

        EXCEPTION
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE INSERTING JOB_ERROR_LOG table'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

        BEGIN
            SELECT
                COUNT(1)
            INTO
                l_cnt
            FROM
                clp_order.order_line_item
            WHERE
                    order_status = 'ORDER-GENERATED'
                AND
                    order_id = p_order_id_in;

            SELECT
                COUNT(1)
            INTO
                l_cnt_failed
            FROM
                clp_order.order_line_item
            WHERE
                    order_status IN (
                        'ORDER-GENERATED','SHIPPED'
                    )
                AND
                    order_id = p_order_id_in;

            UPDATE clp_order.order_details
                SET
                    order_status = (
                        CASE
                            WHEN l_cnt > 0        THEN 'ORDER-GENERATED'
                            WHEN l_cnt_failed = 0 THEN 'FAILED'
                            ELSE 'SHIPPED'
                        END
                    )
            WHERE
                order_id = p_order_id_in;

        EXCEPTION
            WHEN OTHERS THEN
                l_err_msg := 'ERROR WHILE UPDATING ORDER STATUS IN ORDER_DETAILS'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

        COMMIT;

    --added for email notification starts
        SELECT
            od.order_type,
            od.order_status
        INTO
            l_order_type,l_order_status
        FROM
            clp_order.order_line_item oi,
            clp_order.order_details od
        WHERE
                od.order_id = oi.order_id
            AND
                od.order_id = p_order_id_in
            AND
                line_item_id = p_line_item_id_in;

        SELECT
            ins_user
        INTO
            l_ins_user
        FROM
            clp_order.order_details
        WHERE
            order_id = p_order_id_in;
	--SELECT user_email INTO l_user_email FROM clp_user u WHERE u.user_id =l_INS_USER;

        IF
            l_order_type = 'RETAIL' AND ( l_order_status = 'ORDER-GENERATED' OR l_order_status = 'FAILED' )
        THEN
            BEGIN
                SELECT
                    p.product_name
                INTO
                    l_product_name
                FROM
                    clp_order.order_line_item olt,
                    clp_configuration.product p
                WHERE
                        olt.product_id = p.product_id
                    AND
                        olt.order_id = p_order_id_in
                    AND
                        ROWNUM = 1;

            END;

            l_subject := 'Retail Order Status';
            IF
                l_order_status = 'ORDER-GENERATED' AND l_order_type = 'RETAIL'
            THEN
                l_message := 'Order Generated Successfully for Order number - '
                 || p_order_id_in
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
                 || p_order_id_in
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
                p_order_id_in,
                l_ins_user,
                l_subject,
                l_message
            );
        END IF;
-- added for email notification ends

    EXCEPTION
        WHEN l_exception THEN
            ROLLBACK;
            BEGIN
                UPDATE clp_order.order_line_item
                    SET
                        order_status = 'FAILED'
                WHERE
                        order_id = p_order_id_in
                    AND
                        line_item_id = p_line_item_id_in
                    AND
                        order_status = 'ORDER-IN-PROGRESS';

            END;

            INSERT INTO clp_order.job_error_log (
                order_id,
                card_range_id,
                job_name,
                job_date,
                error_msg
            ) VALUES (
                p_order_id_in,
                NULL,
                'schedule_order',
                SYSDATE,
                l_err_msg
            );

            COMMIT;
        WHEN OTHERS THEN
            ROLLBACK;
            l_err_msg := 'ERROR WHILE GENERATING SER_NUM'
             || substr(sqlerrm,1,200);
            BEGIN
                UPDATE clp_order.order_line_item
                    SET
                        order_status = 'FAILED'
                WHERE
                        order_id = p_order_id_in
                    AND
                        line_item_id = p_line_item_id_in
                    AND
                        order_status = 'ORDER-IN-PROGRESS';

            END;

            INSERT INTO clp_order.job_error_log (
                order_id,
                card_range_id,
                job_name,
                job_date,
                error_msg
            ) VALUES (
                p_order_id_in,
                NULL,
                'schedule_order',
                SYSDATE,
                l_err_msg
            );

            COMMIT;
    END order_process_digital;

    PROCEDURE schedule_process_digital (
        p_order_id_in       IN VARCHAR2,
        p_line_item_id_in   IN ORDER_LINE_ITEM.LINE_ITEM_ID%type,
        p_err_msg_out       OUT VARCHAR2
    ) AS
        l_order_status   VARCHAR(20);
        l_exception EXCEPTION;
    BEGIN
            BEGIN
                BEGIN
                    SELECT
                        order_status
                    INTO
                        l_order_status
                    FROM
                        clp_order.order_line_item
                    WHERE
                            order_id = p_order_id_in
                        AND
                            line_item_id = p_line_item_id_in;

                EXCEPTION
                    WHEN no_data_found THEN
                        p_err_msg_out := 'ERROR NO_DATA_FOUND FOR GIVEN ORDER_ID AND LINE_ITEM_ID'
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                    WHEN OTHERS THEN
                        p_err_msg_out := 'ERROR WHILE UPDATING ORDER STATUS '
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                END;

                IF
                    l_order_status = 'ORDER-IN-PROGRESS' OR l_order_status = 'ORDER-GENERATED'
                THEN
                    p_err_msg_out := 'GIVEN ORDER_ID ALREADY PROCESSED';
                    RAISE l_exception;
                ELSE
                    BEGIN
                        UPDATE clp_order.order_line_item
                            SET
                                order_status = 'ORDER-IN-PROGRESS'
                        WHERE
                                order_id = p_order_id_in
                            AND
                                line_item_id = p_line_item_id_in
                            AND
                                order_status IN (
                                    'APPROVED','FAILED'
                                );

                        COMMIT;
                    EXCEPTION
                        WHEN no_data_found THEN
                            p_err_msg_out := 'ERROR NO_DATA_FOUND FOR GIVEN ORDER_ID AND LINE_ITEM_ID'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                        WHEN OTHERS THEN
                            p_err_msg_out := 'ERROR WHILE UPDATING ORDER STATUS '
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;

                    dbms_scheduler.create_job(
                        job_name     => 'ONE_TIME_job_ORDER_' || p_order_id_in,
                        job_type     => 'PLSQL_BLOCK',
                        job_action   => 'BEGIN pkg_digital_order_process.ORDER_PROCESS_DIGITAL('''
                         || p_order_id_in
                         || ''','''
                         || p_line_item_id_in
                         || '''); END;',
                        start_date   => SYSDATE,
                        enabled      => true,
                        auto_drop    => true,
                        comments     => 'Job will run one time'
                    );

                    p_err_msg_out := 'OK';
                END IF;

            EXCEPTION
                WHEN l_exception THEN
                    ROLLBACK;
                WHEN OTHERS THEN
    --NULL;
                    p_err_msg_out := 'Error while processing JOB ...'
                     || substr(sqlerrm,1,200);
                    dbms_output.put_line(p_err_msg_out);
            END;
    END schedule_process_digital;

END pkg_digital_order_process;
/
SHOW ERRORS;
------------------------------------------------------------------------------------