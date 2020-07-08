  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."GET_EXPIRY_DATE_CARD" (
    p_product_id IN NUMBER,
    p_expiry_date_out OUT TIMESTAMP,
    p_resp_msg_out OUT VARCHAR2 )
AS
  v_expryparam          NUMBER;
  v_validity_period     VARCHAR2(30);
  v_exp_date_exemption  VARCHAR2(30);
  v_monthend_expry_date VARCHAR2(30);
  v_active_from DATE;
BEGIN
  p_resp_msg_out := 'OK';
  -------------checking expiry date exemption flag
       /* BEGIN
         SELECT cpc_exp_date_exemption
           INTO v_exp_date_exemption
           FROM cms_prod_cattype
          WHERE cpc_inst_code = p_inst_code_in
            AND cpc_prod_code = p_prod_code_in
            AND cpc_card_type = p_card_type_in;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            p_resp_msg_out :=
                  'expiry date excemption not defined for product code '
               || p_prod_code_in
               || 'card type '
               || p_card_type_in;
            RETURN;
         WHEN OTHERS
         THEN
            p_resp_msg_out :=
                  'Error while selecting applcode from applmast'
               || SUBSTR (SQLERRM, 1, 300);
            RETURN;
      END;
  */

  --Getting expiry param from PRODUCT
  BEGIN
    SELECT PO.ATTRIBUTES.Product.validityPeriod
    INTO v_expryparam
    FROM PRODUCT PO
    WHERE PRODUCT_ID=P_PRODUCT_ID;
--DBMS_OUTPUT.PUT_LINE ('EXPIRY param '||v_expryparam);
  EXCEPTION
  WHEN OTHERS THEN
    p_resp_msg_out := 'Error while selecting validityPeriod' || SUBSTR (SQLERRM, 1, 300);
    RETURN;
  END;

  --Getting validityPeriodFormat from PRODUCT
  BEGIN
    SELECT PO.ATTRIBUTES.Product.validityPeriodFormat
    INTO v_validity_period
    FROM PRODUCT PO
    WHERE PRODUCT_ID=P_PRODUCT_ID;
--DBMS_OUTPUT.PUT_LINE ('Validity Period  '||v_validity_period);
  EXCEPTION
  WHEN OTHERS THEN
    p_resp_msg_out := 'Error while selecting validityPeriodFormat' || SUBSTR (SQLERRM, 1, 300);
    RETURN;
  END;

  --Getting month end expiry from PRODUCT
  BEGIN
    SELECT PO.ATTRIBUTES.Product.monthEndCardExpiry
    INTO v_monthend_expry_date
    FROM PRODUCT PO
    WHERE PRODUCT_ID=P_PRODUCT_ID;
--DBMS_OUTPUT.PUT_LINE ('Month end expiry '||v_monthend_expry_date);
  EXCEPTION
  WHEN OTHERS THEN
    p_resp_msg_out := 'Error while selecting monthEndCardExpiry' || SUBSTR (SQLERRM, 1, 300);
    RETURN;
  END;

  --Getting activeFrom from  PRODUCT
  BEGIN
    SELECT TO_DATE(PO.ATTRIBUTES.Product.activeFrom,'MM/DD/YYYY')
    INTO v_active_from
    FROM PRODUCT PO
    WHERE PRODUCT_ID=P_PRODUCT_ID;
--DBMS_OUTPUT.PUT_LINE ('Active from '||v_active_from);
    IF v_active_from IS null
    then
    v_active_from:=SYSDATE;
    end if;

  EXCEPTION
  WHEN NO_DATA_FOUND THEN
  v_active_from:=SYSDATE;
  WHEN OTHERS THEN
    p_resp_msg_out := 'Error while selecting active date' || SUBSTR (SQLERRM, 1, 300);
    RETURN;
  END;

  --CHECKING conditions
  IF v_expryparam  IS NULL THEN
    p_resp_msg_out := 'Validity is not defined for product profile ';
    RETURN;
  END IF;
  IF v_validity_period       = 'Hours' THEN
    p_expiry_date_out       := v_active_from + v_expryparam / 24;
---DBMS_OUTPUT.PUT_LINE ('EXPIRY date out '||p_expiry_date_out);
  ELSIF v_validity_period    = 'Days' THEN
    p_expiry_date_out       := v_active_from + v_expryparam;
  ELSIF v_validity_period    = 'Weeks' THEN
    p_expiry_date_out       := v_active_from + (7 * v_expryparam);
  ELSIF v_validity_period    = 'Months' THEN
    IF v_monthend_expry_date = 'Enable' THEN
      p_expiry_date_out     := LAST_DAY (ADD_MONTHS (v_active_from, v_expryparam));
    ELSE
      p_expiry_date_out := ADD_MONTHS (v_active_from, v_expryparam);
    END IF;
  ELSIF v_validity_period    = 'Years' THEN
    IF v_monthend_expry_date = 'Enable' THEN
      p_expiry_date_out     := LAST_DAY (ADD_MONTHS (v_active_from, (12 * v_expryparam) - 1));
    ELSE
      p_expiry_date_out := ADD_MONTHS (v_active_from, (12 * v_expryparam) - 1);
    END IF;
  END IF;
  ------if expiry date exemption flag is set then adding exemption months with expiry date
  /*
  IF v_exp_date_exemption = 'Y'
      THEN
         BEGIN
            SELECT ADD_MONTHS (p_expiry_date_out, vem_month_value)
              INTO p_expiry_date_out
              FROM vms_expiry_mast
             WHERE vem_month_id = TO_CHAR (p_expiry_date_out, 'mm')
               AND vem_prod_code = p_prod_code_in
               AND vem_prod_cattype = p_card_type_in;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               p_expiry_date_out := p_expiry_date_out;
            WHEN OTHERS
            THEN
               p_resp_msg_out :=
                     'Error while selecting the value from vms_expiry_mast '
                  || SQLERRM;
               RETURN;
         END;
      END IF;*/

EXCEPTION
WHEN OTHERS THEN
  p_resp_msg_out := 'Error in vmsfunutilities.get_expiry_date' || SUBSTR (SQLERRM, 1, 300);
END;

/
SHOW ERROR