  CREATE OR REPLACE  PACKAGE "CLP_ORDER"."CLP_UTIL" AS

    PROCEDURE SP_PUBLISH_ORDER_STATUS (
        p_order_id     IN VARCHAR2,
        p_partner_id   IN NUMBER,
        p_package_id   IN NUMBER
    );


    PROCEDURE send_mail_order (
     p_order_id          IN VARCHAR2,
        p_ins_user        IN VARCHAR2,
        p_subject     IN VARCHAR2,
        p_message     IN VARCHAR2
    );


END clp_util;

/
SHOW ERROR