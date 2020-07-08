CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_CREATE_HEADERFILENUMBER" (
                                                     lupduser                 IN     number     ,
                                                     headfileno               OUT    VARCHAR2,
                                                     cardrecordcnt            OUT    VARCHAR2,
                                                     errmsg                   OUT    varchar2)
AS
v_ccc_header_seq     varchar2 (12);
v_ccc_cardrec_seq    varchar2 (12);


BEGIN        --Main Begin Block Starts Here
      BEGIN
      errmsg := 'OK';
      SELECT  HEADER_SEQ,DETAIL_SEQ
      INTO    v_ccc_header_seq,v_ccc_cardrec_seq
      FROM    CCF_CTRL
      WHERE   trunc(CCFGEN_DATE) = trunc(sysdate);

      headfileno := v_ccc_header_seq+1;
      cardrecordcnt := v_ccc_cardrec_seq+1;

      UPDATE CCF_CTRL SET HEADER_SEQ= headfileno,DETAIL_SEQ=cardrecordcnt
      WHERE
             trunc(CCFGEN_DATE) = trunc(sysdate);

      EXCEPTION	--Exception of begin 1
		WHEN NO_DATA_FOUND THEN

                headfileno:='100000';
                cardrecordcnt:='200000';

                INSERT INTO CCF_CTRL VALUES
                (
                SYSDATE,
                100000,
                200000,
                1,
                SYSDATE,
                1,
                SYSDATE
                );

                errmsg := 'OK';

	   WHEN OTHERS THEN
			errmsg := 'Exeption 1 -- '||SQLCODE||'--'||SQLERRM;
        END;

EXCEPTION    --Exception of Main Begin
    WHEN OTHERS THEN
    errmsg := 'Exception Main -- '||SQLCODE||'--'||SQLERRM;
END
;

/
SHOW ERROR