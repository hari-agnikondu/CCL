CREATE OR REPLACE PACKAGE clp_order.pkg_digital_order_process AS
    PROCEDURE order_process_digital (
        p_order_id_in       IN ORDER_LINE_ITEM.ORDER_ID%type,
        p_line_item_id_in   IN ORDER_LINE_ITEM.LINE_ITEM_ID%type
    );

    PROCEDURE schedule_process_digital (
        p_order_id_in       IN VARCHAR2,
        p_line_item_id_in   IN ORDER_LINE_ITEM.LINE_ITEM_ID%type,
        p_err_msg_out       OUT VARCHAR2
    );

END pkg_digital_order_process;
/
SHOW ERRORS;
-----------------------------------------------------------------------