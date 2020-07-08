  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."GET_SERIALS" (p_productid_in   IN     VARCHAR2,
                          p_quantity_in    IN     NUMBER,
                          p_serials_out       OUT shuffle_array_type,
                          p_respmsg_out       OUT VARCHAR2)
   AS
      l_rowid        ROWID;
      l_cntrl_numb   product_serial_control.SERIAL_NUMBER%TYPE;
   BEGIN
      p_respmsg_out := 'OK';

      BEGIN
         SELECT rd, serial
           INTO l_rowid, l_cntrl_numb
           FROM (  SELECT ROWID rd, SERIAL_NUMBER serial
                     FROM product_serial_control
                    WHERE product_id = p_productid_in
                          AND (SERIAL_NUMBER-1) + p_quantity_in <= END_SERIAL
                 ORDER BY END_SERIAL - SERIAL_NUMBER)
          WHERE ROWNUM = 1;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            p_respmsg_out := 'Sufficient serials not available';
            RETURN;
         WHEN OTHERS
         THEN
            p_respmsg_out :=
               'Error While fetching product_serial_cntrl :'
               || SUBSTR (SQLERRM, 1, 200);
            RETURN;
      END;

      BEGIN
         UPDATE product_serial_control
            SET SERIAL_NUMBER = SERIAL_NUMBER + p_quantity_in
          WHERE ROWID = l_rowid;
      EXCEPTION
         WHEN OTHERS
         THEN
            p_respmsg_out :=
               'Error While fetching product_serial_cntrl :'
               || SUBSTR (SQLERRM, 1, 200);
            RETURN;
      END;

          SELECT l_cntrl_numb + (LEVEL - 1)
            BULK COLLECT INTO p_serials_out
            FROM DUAL
      CONNECT BY LEVEL <= p_quantity_in;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_respmsg_out := 'Main Excp :' || SUBSTR (SQLERRM, 1, 200);
   END get_serials;

/
SHOW ERROR