  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_ORDER_CANCEL_REQUEST" (
      p_order_id_in     IN       VARCHAR2,
      p_partner_id_in   IN       VARCHAR2,
      p_resp_code_out   OUT      VARCHAR2,
      p_resp_msg_out    OUT      VARCHAR2
   )
   AS
      excp_reject_order    EXCEPTION;
      l_order_count        NUMBER (5) := 0;
      l_cancel_order_cnt   NUMBER (5) := 0;
      l_activation_cnt     NUMBER (5) := 0;
   BEGIN
      p_resp_msg_out := 'SUCCESS';
      p_resp_code_out := 'R0001';

      BEGIN
         SELECT COUNT (1)
           INTO l_order_count
           FROM order_details
          WHERE order_id = p_order_id_in
            AND partner_id = p_partner_id_in;

         IF l_order_count = 0
         THEN
            p_resp_msg_out :=
                  'NO ORDER EXISTS FOR ORDER ID AND PARTNER ID COMBINATION:Order ID:'
               || p_order_id_in
               || ':ParnerID:'
               || p_partner_id_in;
            p_resp_code_out := 'R0184';
            RAISE excp_reject_order;
         END IF;
      EXCEPTION
         WHEN excp_reject_order
         THEN
            RAISE;
         WHEN OTHERS
         THEN
            p_resp_msg_out :=
                  'Error While getting   ORDER_COUNT  :'
               || SUBSTR (SQLERRM, 1, 200);
            p_resp_code_out := 'R0012';
      END;

      BEGIN
         SELECT COUNT (1)
           INTO l_cancel_order_cnt
           FROM order_details
          WHERE order_id = p_order_id_in
            AND partner_id = p_partner_id_in
            AND UPPER (order_status) = 'CANCELLED';

         IF l_cancel_order_cnt <> 0
         THEN
            p_resp_msg_out := 'ORDER ALREADY CANCELLED:';
            p_resp_code_out := 'R0152';
            RAISE excp_reject_order;
         END IF;
      EXCEPTION
         WHEN excp_reject_order
         THEN
            RAISE;
         WHEN OTHERS
         THEN
            p_resp_msg_out :=
                  'Error While getting ORDER ALREADY CANCELLED check :'
               || SUBSTR (SQLERRM, 1, 200);
            p_resp_code_out := 'R0012';
      END;

      BEGIN
         SELECT COUNT (1)
           INTO l_activation_cnt
          FROM order_line_item_dtl o, card c
          WHERE o.order_id = p_order_id_in
            AND o.partner_id = p_partner_id_in
            AND o.card_num_hash = c.card_num_hash
            AND c.date_of_activation IS NOT NULL;

         IF l_activation_cnt <> 0
         THEN
            p_resp_msg_out :=
                  l_activation_cnt
               || ' CARDS ALREADY ACTIVATED IN ORDER ID:'
               || p_order_id_in;
            p_resp_code_out := 'R0153';
            RAISE excp_reject_order;
         END IF;
      EXCEPTION
         WHEN excp_reject_order
         THEN
            RAISE;
         WHEN OTHERS
         THEN
            p_resp_msg_out :=
                  'Error While getting CARDS ALREADY ACTIVATED check :'
               || SUBSTR (SQLERRM, 1, 200);
            p_resp_code_out := 'R0012';
      END;

      BEGIN
            UPDATE order_line_item
            SET order_status = 'CANCELLED'
          WHERE order_id = p_order_id_in
            AND partner_id = p_partner_id_in;
      EXCEPTION
         WHEN OTHERS
         THEN
            p_resp_msg_out :=
                  'Error While UPDATE ORDER STATUS AS CANCELLED IN OREDR LINE ITEM :'
               || SUBSTR (SQLERRM, 1, 200);
            p_resp_code_out := 'R0012';
      END;

      BEGIN
         UPDATE order_details
            SET order_status = 'CANCELLED'
          WHERE order_id = p_order_id_in
            AND partner_id = p_partner_id_in;
      EXCEPTION
         WHEN OTHERS
         THEN
            p_resp_msg_out :=
                  'Error While UPDATE OREDR DETAILS STATUS AS CANCELLED :'
               || SUBSTR (SQLERRM, 1, 200);
            p_resp_code_out := 'R0012';
      END;
   EXCEPTION
      WHEN excp_reject_order
      THEN
         NULL;
      WHEN OTHERS
      THEN
         p_resp_msg_out :=
             'Error While cancel_order_request :' || SUBSTR (SQLERRM, 1, 200);
         p_resp_code_out := 'R0012';
   END;

/
SHOW ERROR