  CREATE OR REPLACE  PACKAGE BODY "CLP_ORDER"."CLP_UTIL" AS

    PROCEDURE SP_PUBLISH_ORDER_STATUS (
        p_order_id     IN VARCHAR2,
        p_partner_id   IN NUMBER,
        p_package_id   IN NUMBER
    ) IS

        req utl_http.req;
        res utl_http.resp;
        buffer varchar2(4000);
        v_url           VARCHAR2(400);
      --  v_url_replace   VARCHAR2(400);
        reqData varchar2(4000);



    BEGIN

        SELECT
            url
        INTO
            v_url
        FROM
            postback_restservice
        WHERE
            name = 'PSTBK_ORDER_STATUS_UPDATE';

          reqData:= '{"orderID":"'||p_order_id||'", "packageID":"'||p_package_id||'", "partnerID":"'||p_partner_id||'"}';
          dbms_output.put_line('reqData'||reqData);
          req := utl_http.begin_request(v_url, 'POST',' HTTP/1.1');
          --utl_http.set_header(req, 'user-agent', 'mozilla/4.0');
           utl_http.set_header(req, 'content-type', 'application/json');
           utl_http.set_header(req, 'x-incfs-channel', 'WEB');
           utl_http.set_header(req, 'x-incfs-channel-identifier', 'WEB');
           utl_http.set_header(req, 'x-incfs-correlationid', SUBSTR(dbms_random.value(1,9),3,9));
          utl_http.set_header(req, 'Content-Length', length(reqData));
          utl_http.write_text(req,reqData);
          res := utl_http.get_response(req);

       BEGIN
        LOOP
             utl_http.read_line(res, buffer);
            -- dbms_output.put_line(buffer);
        END LOOP;
            utl_http.end_response(res);
        EXCEPTION
            when utl_http.end_of_body
        THEN
         utl_http.end_response(res);
      END;

    END;
    PROCEDURE send_mail_order (

                    p_order_id          IN VARCHAR2,
                    p_ins_user        IN VARCHAR2,
                    p_subject     IN VARCHAR2,
                    p_message     IN VARCHAR2

    )
    AS


        v_url           VARCHAR2(400);
        req         utl_http.req;
        res        utl_http.resp;
        reqData varchar2(4000);
        buff varchar2(4000);
    BEGIN
       -- v_url:=trans_const.SCHEDULER_QA_URL;
            SELECT
            url
        INTO
            v_url
        FROM
            email_host
        WHERE
            name = 'SMTP_STATUS_UPDATE';
         reqData:= '{"orderID":"'||p_order_id||'", "insUser":"'||p_ins_user||'", "subject":"'||p_subject||'", "message":"'||p_message||'"}';
        dbms_output.put_line('reqData'||reqData);
        req := utl_http.begin_request(v_url, 'POST',' HTTP/1.1');
          utl_http.set_header(req,'User-Agent','Mozilla/4.0');
          utl_http.set_header(req, 'content-type', 'application/json');
          utl_http.set_header(req, 'Content-Length', length(reqData));
           utl_http.write_text(req,reqData);
          res := utl_http.get_response(req);

         BEGIN
        LOOP
             utl_http.read_line(res, buff);
            -- dbms_output.put_line(buffer);
        END LOOP;
            utl_http.end_response(res);
        EXCEPTION
            when utl_http.end_of_body
        THEN
         utl_http.end_response(res);
      END;


    END;
    END;

/
SHOW ERROR