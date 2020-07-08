create or replace PROCEDURE sp_first_party_third_party_check (
    p_prod_id            IN NUMBER,
    p_mdm_id             IN VARCHAR2,
    p_transaction_code   IN VARCHAR2,
    p_delivery_channel   IN VARCHAR2,
    p_msg_type           IN VARCHAR2,
    p_party_supported    IN VARCHAR2,
    p_err_msg            OUT VARCHAR2,
    p_resp_code          OUT VARCHAR2
) AS
    v_partner_id                NUMBER;
    v_group_access_party_type   VARCHAR2(20);
    l_exception EXCEPTION;
BEGIN
    
    --Getting partner_id
    BEGIN
        SELECT
            partner_id
        INTO
            v_partner_id
        FROM
            partner
        WHERE
            mdm_id = p_mdm_id;

    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.transaction_not_supported_on_partner;
            p_err_msg := 'ERROR WHILE GETTING PARTNER_ID'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING PARTNER_ID'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    --Getting group access party type

    BEGIN
        SELECT
            partner_party_type
        INTO
            v_group_access_party_type
        FROM
            group_access_product
        WHERE
                partner_id = v_partner_id
            AND
                product_id = p_prod_id;

    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.transaction_not_supported_on_partner;
            p_err_msg := 'NO DATA FOUND FOR PARTY_TYPE FROM GROUP_ACCESS_PRODUCT TABLE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING PARTY_TYPE FROM GROUP_ACCESS_PRODUCT TABLE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    IF
        v_group_access_party_type <> p_party_supported AND p_party_supported != 'BOTH'
    THEN
        p_err_msg := 'NOT SUPPORTED FOR PARTY TYPE';
        p_resp_code := trans_const.transaction_not_supported_on_partner;
        RAISE l_exception;
    ELSE
        p_resp_code := trans_const.success;
        p_err_msg := 'OK';
    END IF;

EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
    WHEN OTHERS THEN
        p_resp_code := trans_const.system_error;
        p_err_msg := 'ERROR WHILE FIRST PARTY THIRD PARTY CHECK'
         || substr(sqlerrm,1,200);
        ROLLBACK;
END;