  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."UPDATE_SERIALS" (p_productid_in   IN     VARCHAR2,
                          p_quantity_in    IN     NUMBER,
                          p_line_item_id_in   IN VARCHAR2,
                          p_order_id_in   IN VARCHAR2,
                          p_formFactor_in IN VARCHAR2,
                          p_errmsg_out       OUT VARCHAR2)
   AS
      l_serials          shuffle_array_type;
      card_num_hash_array shuffle_array_type;
      l_exception exception;
      l_proxy_no  card.proxy_number%type;
      l_pins               shuffle_array_type;
      l_encr_key     VARCHAR2(100):= '478CCB8898D9F3A58BCD420677D8E27EC4C380B202A01B604E7389BDF1AA0D0D';
      l_proxy_pin_encr order_line_item_dtl.PROXY_PIN_ENCR%TYPE;
      l_proxy_pin_hash order_line_item_dtl.PROXY_PIN_HASH%TYPE;

--CURSOR line_items IS
--         SELECT
--            card_num_hash
--
--                FROM
--                    ORDER_LINE_ITEM_DTL
--                 where
--                    order_id=p_order_id_in
--                 AND
--                     ORDER_LINE_ITEM_ID=p_line_item_id_in;

   BEGIN
      p_errmsg_out := 'OK';

BEGIN
        SELECT
            card_num_hash
                 BULK COLLECT INTO card_num_hash_array
                FROM
                    ORDER_LINE_ITEM_DTL
                 where
                    order_id=p_order_id_in
                 AND
                     ORDER_LINE_ITEM_ID=p_line_item_id_in;

     EXCEPTION
     WHEN NO_DATA_FOUND
        THEN
            p_errmsg_out:='No data found While getting card dtls :'
                     || SUBSTR (SQLERRM, 1, 200);
                   RAISE l_exception;
      WHEN OTHERS THEN
                  p_errmsg_out:='Error While getting card dtls :'
                     || SUBSTR (SQLERRM, 1, 200);
                   RAISE l_exception;
            END;
    BEGIN
        get_serials (p_productid_in,
                         p_quantity_in,
                         l_serials,
                         p_errmsg_out);

    IF p_errmsg_out <> 'OK'
            THEN
                p_errmsg_out:='Error from get_serials :'
                     || SUBSTR (SQLERRM, 1, 200);
               RAISE l_exception;
            END IF;
            END;
            --- get pin
             SELECT TRUNC (DBMS_RANDOM.VALUE (1000000000, 9999999999)) num
                     BULK COLLECT INTO l_pins
                     FROM DUAL
               CONNECT BY LEVEL <= p_quantity_in;

          -- updating serial number in card table
         FOR i IN 1 .. p_quantity_in
         LOOP

         -- Get proxy number to check generate proxy pin
         BEGIN
        SELECT
            PROXY_NUMBER
                  INTO l_proxy_no
                FROM
                    card
                 where
                    card_num_hash=card_num_hash_array(i);
     EXCEPTION
     WHEN NO_DATA_FOUND
        THEN
            p_errmsg_out:='No data found While getting proxy Number dtls :'
                     || SUBSTR (SQLERRM, 1, 200);
                   RAISE l_exception;
      WHEN OTHERS THEN
                  p_errmsg_out:='Error While getting proxy number dtls :'
                     || SUBSTR (SQLERRM, 1, 200);
                   RAISE l_exception;
            END;

         BEGIN

         l_proxy_pin_encr := fn_emaps_main (l_proxy_no || l_pins (i));

          l_proxy_pin_hash := fn_hash (l_proxy_no || l_pins (i));

             UPDATE card
                  SET serial_number = l_serials(i)
                WHERE     card_num_hash = card_num_hash_array(i);

            EXCEPTION
               WHEN OTHERS
               THEN
                  p_errmsg_out :=
                     'Error While updating serial dtls :'
                     || SUBSTR (SQLERRM, 1, 200);
                  RAISE l_exception;
            END;
            -- update serial number in order line item dtls
            BEGIN
             UPDATE order_line_item_dtl
                  SET serial_number = l_serials(i),
                      PROXY_PIN_ENCR =   (CASE
                          WHEN upper(p_formFactor_in) = 'VIRTUAL' THEN l_proxy_pin_encr
                          ELSE NULL
                       END),
                       PROXY_PIN_HASH =   (CASE
                          WHEN upper(p_formFactor_in) = 'VIRTUAL' THEN l_proxy_pin_hash
                          ELSE NULL
                       END),
                       pin = (CASE
                          WHEN upper(p_formFactor_in) = 'VIRTUAL' THEN l_pins(i)
                          ELSE NULL
                          END)
                WHERE     card_num_hash = card_num_hash_array(i);

            EXCEPTION
               WHEN OTHERS
               THEN
                  p_errmsg_out :=
                     'Error While updating serial dtls :'
                     || SUBSTR (SQLERRM, 1, 200);
                  RAISE l_exception;
            END;
            END LOOP;
commit;

   EXCEPTION
     WHEN l_exception THEN
     ROLLBACK;
    WHEN OTHERS THEN
    --NULL;
        p_errmsg_out:= 'Error while processing JOB ...'||substr(sqlerrm,1,200);
        dbms_output.put_line(p_errmsg_out);

   END update_serials;

/
SHOW ERROR