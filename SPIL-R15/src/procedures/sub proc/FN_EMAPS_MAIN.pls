create or replace FUNCTION        fn_emaps_main (prm_in_val VARCHAR2)
   RETURN RAW DETERMINISTIC
IS
   encr_out          RAW (4000);

   encrkey           RAW (2000);

   encryption_type   PLS_INTEGER
      :=   DBMS_CRYPTO.encrypt_aes256
         + DBMS_CRYPTO.chain_cbc
         + DBMS_CRYPTO.pad_pkcs5;
   errmsg            VARCHAR2 (500);

BEGIN

   encrkey := keyaccess.getAesKey;
   --dbms_output.put_line(encrkey);

   encr_out :=
      DBMS_CRYPTO.encrypt (UTL_I18N.string_to_raw (prm_in_val, 'AL32UTF8'),
                           encryption_type,
                           encrkey

             );
             --dbms_output.put_line('encr_out7777'||encr_out);
   RETURN encr_out;
END;