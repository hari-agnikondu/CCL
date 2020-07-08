create or replace PROCEDURE             "SP_ORDER_PROCESS_DIGITAL" ( 
    p_order_id_in       IN VARCHAR2,
    p_line_item_id_in   IN VARCHAR2
) AS

    l_quantity            NUMBER;
    l_pending_qty         NUMBER;
    l_card_range_id       NUMBER := 0;
    l_tot_available_inv   NUMBER;
    l_available_inv       NUMBER;
    l_ctrl_num            NUMBER;
    l_inv_req             NUMBER;
    l_count               NUMBER;
    l_line_item_id        NUMBER;
    l_partner_id          NUMBER;
    l_issued_inventory    NUMBER;
    l_card_range_order    NUMBER := 0;
    l_product_id          NUMBER;
    l_max_inv_check       NUMBER;
    l_inv_card_enough     NUMBER := 0;
    l_purse_id            NUMBER;
    l_purse_type_id       NUMBER;
    l_currency_code       VARCHAR2(3);
    l_upc                 VARCHAR2(20);
    l_expiry_date         TIMESTAMP;
    l_expiry_err_msg      VARCHAR2(200) := 'OK';
    l_max_ctrl            VARCHAR2(20);
    l_max_val             NUMBER;
    l_active_from         DATE;
    l_usage_limit         CLOB;
    l_exception EXCEPTION;
    l_err_msg             VARCHAR2(200);
    l_purse_type_name     VARCHAR2(20);
    l_defaultcardstatus   VARCHAR2(3 CHAR);
    l_formFactor          VARCHAR2(20);
    l_cnt NUMBER;
    l_cnt_failed NUMBER;
    l_INS_USER      NUMBER;                         
    l_order_type        ORDER_DETAILS.order_type%type;
    l_order_status      ORDER_DETAILS.order_type%type;
    l_product_name      CLP_CONFIGURATION.PRODUCT.product_name%type;
    l_subject        VARCHAR2(1000);
    l_message        VARCHAR2(1000);
	l_package_id      CLP_CONFIGURATION.PRODUCT_PACKAGE.PACKAGE_ID%TYPE;
    l_user_email     VARCHAR2(50);

	-- select the list of available card ranges for the product
    CURSOR c1 IS
        SELECT
            i.available_inventory,
            i.card_range_id,
            i.issued_inventory
        FROM
            CLP_CONFIGURATION.product_card_range c,
            CLP_INVENTORY.card_range_inventory i
        WHERE
                product_id = l_product_id
            AND
                c.card_range_id = i.card_range_id
            AND
                i.available_inventory > 0
        ORDER BY c.card_range_order;

BEGIN
    l_err_msg := 'OK';

	--selecting partner_id,product_id,quantity and package_id from order_line_item_table
    BEGIN
        SELECT
            partner_id,
            product_id,
            quantity,
            PACKAGE_ID
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
            po.attributes.Limits,
            po.attributes.General.defaultCardStatus,
             po.attributes.Product.formFactor
        INTO
            l_active_from,l_usage_limit,l_defaultcardstatus,l_formFactor
        FROM
            clp_configuration.product po
        WHERE
            product_id = l_product_id;

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

	--loop and process the order
	l_pending_qty := l_quantity;
    FOR x IN c1 LOOP
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
                INSERT INTO order_ctrl VALUES ( l_product_id,l_max_ctrl );
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
                    7,
                    0
                ),
                lpad(l_product_id,5,0)
                 || lpad(
                    l_max_ctrl + ROWNUM,
                    7,
                    0
                ),
                lpad(l_product_id,5,0)
                 || lpad(
                    l_max_ctrl + ROWNUM,
                    7,
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
            INSERT INTO clp_transactional.CUSTOMER (
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
                sysdate
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
                ADDR_FLAG,   
				INS_DATE	
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
                sysdate
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
                usage_limit,
                usage_fee,
                card_num_mask,       
                card_id,             
                PIN_FLAG,
                MBR_NUMB,
				INS_DATE,					 				   
                INS_USER,                                      
                LAST_UPD_USER,
                LAST_UPD_DATE,
				CARDPACK_ID,
                digital_pin
            ) ( SELECT
                card_num_hash,
                card_num_encr,
                to_char('9999'|| lpad(l_product_id,4,0)|| lpad(l_max_ctrl + ROWNUM,12,'0')), --4 digit identifier as 9999
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
               DECODE (l_formFactor,
                                       'Virtual', l_defaultcardstatus,
                                       '99'),
                l_expiry_date,
                SYSDATE,
                 null,
                l_usage_limit,
                '{}',
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
                ceil(dbms_random.value()*10000000000)
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
                ins_date
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
                sysdate
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


		--insert into ACCOUNT_CUSTOMER_PROFILE
        BEGIN
            INSERT INTO CLP_TRANSACTIONAL.ACCOUNT_CUSTOMER_PROFILE (
                account_id,
                ACCOUNT_PRODUCT_ID,
                ACCOUNT_NUMBER,
                CUSTOMER_CODE
			) SELECT
                b.account_id,
                l_product_id,
                b.ACCOUNT_NUMBER,
                a.CUSTOMER_CODE
                FROM clp_transactional.CUSTOMER_PROFILE a,clp_transactional.ACCOUNT b,clp_order.order_line_item_dtl c
                WHERE c.ACCOUNT_ID = b. ACCOUNT_ID
                AND c.CUSTOMER_CODE = a.CUSTOMER_CODE
                AND card_range_id = x.card_range_id
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
                 order_status = DECODE (l_formFactor,
                                       'Virtual', 'SHIPPED',
                                       'ORDER-GENERATED'),
                 CCF_FLAG = DECODE (l_formFactor, 'Virtual',2,1),
                 SHIPPING_DATETIME = DECODE (l_formFactor,
                                       'Virtual', sysdate,
                                       null)
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
       select count(1) INTO l_cnt from clp_order.order_line_item where order_status = 'ORDER-GENERATED' AND  order_id = p_order_id_in;
       select count(1) INTO l_cnt_failed from clp_order.order_line_item where order_status IN ('ORDER-GENERATED','SHIPPED') AND  order_id = p_order_id_in;

        UPDATE clp_order.order_details
            SET order_status =
              (CASE WHEN l_cnt > 0  THEN 'ORDER-GENERATED' WHEN l_cnt_failed = 0 THEN 'FAILED' ELSE 'SHIPPED' END)
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
    SELECT OD.order_type,OD.order_status into l_order_type,l_order_status
    from clp_order.ORDER_LINE_ITEM OI,clp_order.ORDER_DETAILS OD 
	where
		OD.ORDER_ID=OI.ORDER_ID 
	AND OD.order_id= p_order_id_in 
	and line_item_id= p_line_item_id_in ;

	SELECT INS_USER INTO l_INS_USER FROM clp_order.ORDER_DETAILS WHERE order_id=p_order_id_in;
	--SELECT user_email INTO l_user_email FROM clp_user u WHERE u.user_id =l_INS_USER;
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
                clp_configuration.product p
            WHERE
                    olt.product_id = p.product_id
                AND
                    olt.order_id =p_order_id_in
                AND
                    ROWNUM = 1;

        END;

        l_subject := 'Retail Order Status';
        IF
            l_order_status = 'ORDER-GENERATED' AND l_order_type = 'RETAIL'
        THEN
            l_message := 'Order Generated Successfully for Order number - '
             ||  p_order_id_in
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
END;