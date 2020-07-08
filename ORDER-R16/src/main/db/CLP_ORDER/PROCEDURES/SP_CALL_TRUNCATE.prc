create or replace PROCEDURE CLP_ORDER.SP_CALL_TRUNCATE(
p_table_name IN VARCHAR2,
p_file_name IN VARCHAR2,
p_err_msg OUT VARCHAR2
) AS 

BEGIN
p_err_msg := 'FAIL';
    If UPPER(p_table_name)||':'||UPPER(p_file_name) IN ('CPIFILE_DATA_TEMP:CN_FILE',
        'RESPONSEFILE_DATA_STG:RESPONSE_FILE','RETURNFILE_DATA_STG:RETURN_FILE',
        'SERIAL_DETAILS_STG:SERIAL_NUMBER_FILE','SHIPMENTFILE_DATA_STG:SHIPMENT_FILE')
        THEN
            execute immediate ('truncate table clp_order.'||p_table_name );
            p_err_msg := 'SUCCESS';
    END IF;

END SP_CALL_TRUNCATE;
/

-----------------------------------------------------------------------------