  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_SMSEMAIL_DAILY_ALTMSG" (
    p_issuer    IN NUMBER,
    p_err_msg   OUT VARCHAR2
) IS

    v_totalrows   NUMBER;
    v_errmsg      VARCHAR2(500);
    l_exception EXCEPTION;
    CURSOR c1 IS
        SELECT
            c.serial_number snumber,
            c.card_num_hash cardhash,
            c.card_num_encr cardencr,
            cp.mobile_one mobileno,
            cp.email_one email,
            cp.alert_setting.alerts alerts,
            c.customer_code customer_code,
            cp.mobile_one mobile_one1,
            c.product_id productid,
            c.account_id accountid,
            (
                SELECT
                    currency_code
                FROM
                    currency_code
                WHERE
                    currency_id = a.currency_code
            ) currency
        FROM
            card c,
            customer_profile cp,
            account_purse a,
            product p
        WHERE
                cp.customer_code = c.customer_code
            AND
                a.account_id = c.account_id
            AND
                c.card_status <> 9
            AND
                JSON_EXISTS ( cp.alert_setting,'$?(@.alerts.id==$id)' PASSING 17 AS "id" )
                ---ADDED ON 24-09-2018 FOR DAILY BALANCE ALERT BASED ON MUTLIPLE CARD STATUS IN PRODUCT ALERTS
            AND
                c.product_id = p.product_id
            AND (
                    c.card_status IN (
                        SELECT
                            regexp_substr(
                                p.attributes."Alerts"."alertCardStatus",
                                '[^,]+',
                                1,
                                level
                            )
                        FROM
                            dual
                        CONNECT BY
                            regexp_substr(
                                p.attributes."Alerts"."alertCardStatus",
                                '[^,]+',
                                1,
                                level
                            ) IS NOT NULL
                    )
                OR
                    p.attributes."Alerts"."alertCardStatus" IS NULL
            ) AND (
                    a.available_balance > p.attributes."Alerts"."alertMinBalance"
                OR
                    p.attributes."Alerts"."alertMinBalance" IS NULL
            ) AND (
                    ( SYSDATE - c.last_txndate ) < p.attributes."Alerts"."alertInactivityPeriod"
                OR
                    p.attributes."Alerts"."alertInactivityPeriod" IS NULL
            );

BEGIN
  --OPEN C1;
   --LOOP
     --Truncate table data
    p_err_msg := 'Ok';
    EXECUTE IMMEDIATE 'truncate table SMS_EMAIL_DAILY_ALERT_MSG';
    FOR x IN c1 LOOP
        BEGIN
            INSERT INTO sms_email_daily_alert_msg (
                serial_number,
                card_num_hash,
                card_num_encr,
                mobile_number,
                email,
                product_id,
                account_id,
                ins_date,
                begin_interval,
                process_status,
                process_date,
                currency_code,
                alert_id
            ) VALUES (
                x.snumber,
                x.cardhash,
                x.cardencr,
                x.mobileno,
                x.email,
                x.productid,
                x.accountid,
                SYSDATE,
                SYSDATE,
                'N',
                SYSDATE,
                x.currency,
                '17'
            );

            EXIT WHEN c1%notfound;
        EXCEPTION
            WHEN OTHERS THEN
                p_err_msg := ' insertion Exception occured while insert the table SMS_EMAIL_DAILY_ALERT_MSG'
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
    END LOOP;


     --  END;

   --- END LOOP;

  -- CLOSE c1;

EXCEPTION
    WHEN l_exception THEN
        p_err_msg := p_err_msg
         || ' Main Exception occured '
         || substr(sqlerrm,1,200);
        ROLLBACK;
END;

/
SHOW ERROR