create or replace PROCEDURE       CLP_ORDER.SP_CREATE_EMBFNAME(
    
    FILENAME    IN VARCHAR2,
    TYPEOFORDER IN VARCHAR2,
    VENDER      IN VARCHAR2,
    LUPDUSER    IN NUMBER,
    P_PROD_ID   IN NUMBER,
    EMBFNAME OUT VARCHAR2,
    ERRMSG OUT VARCHAR2)
AS

  l_CURR_DATE VARCHAR2(8);
  l_FILE_NUM  NUMBER(3);
  l_FILE_CNT  VARCHAR2(3);
  l_FILE_LEN  NUMBER(3);
  l_FILE_TYPE VARCHAR(10);
  l_FILE_FORMAT CLP_CONFIGURATION.FULFILLMENT_VENDOR.CCF_FILE_FORMAT%TYPE;
  l_REISSU_FILE_FORMAT CLP_CONFIGURATION.FULFILLMENT_VENDOR.REPLACE_CCF_FILE_FORMAT%TYPE;
  l_FLE_FORMAT CLP_CONFIGURATION.FULFILLMENT_VENDOR.CCF_FILE_FORMAT%TYPE;
  l_EMB_FNAME VARCHAR2(200);
  l_SRC_NAME CLP_CONFIGURATION.ATTRIBUTE_DEFINITION.attribute_value%TYPE;
  
BEGIN
--l_FILE_TYPE:='CCF';
  BEGIN
    SELECT TO_CHAR(SYSDATE, 'mmddyyyy') INTO l_CURR_DATE FROM DUAL;
    SELECT LPAD(NVL(MAX(FILE_COUNT)+1,0),3,0)
    INTO l_FILE_CNT
    FROM CLP_CONFIGURATION.CCF_FILE_CTRL
    WHERE 
	VENDOR_NAME        = VENDER
    AND TRUNC(CREATE_DATE) = TRUNC(SYSDATE);
    BEGIN
        select attribute_value into l_SRC_NAME from CLP_CONFIGURATION.ATTRIBUTE_DEFINITION where ATTRIBUTE_GROUP='Global Parameters' and ATTRIBUTE_NAME='srcPlatform';

     EXCEPTION
     
    WHEN OTHERS THEN
      ERRMSG := 'Exeption Occurred while fetching from ATTRIBUTE_DEFINITION' || SQLCODE || '--' || SQLERRM;
    END;
    
    BEGIN
	  
      
	  SELECT regexp_replace(NVL(DECODE(TYPEOFORDER,'03',REPLACE_CCF_FILE_FORMAT,'05',REPLACE_CCF_FILE_FORMAT,CCF_FILE_FORMAT),''),'('||CHR(10)||'|'||CHR(13)||')+','')
      INTO l_FILE_FORMAT
      FROM CLP_CONFIGURATION.FULFILLMENT_VENDOR
      WHERE FULFILLMENT_VENDOR_ID=VENDER;

	  IF TYPEOFORDER not in ('03','05') THEN 
		l_FILE_TYPE:='CCF';
      else 
		l_FILE_TYPE:='';
       END IF;
	   
	  SELECT   replace(replace(REPLACE(REPLACE(REPLACE(REPLACE(l_FILE_FORMAT ,'<DestinationID>', VENDER),'<SourcePlatform>',l_SRC_NAME),'<ProductCode>',P_PROD_ID),'<date>',l_CURR_DATE),'<SequenceNbr>',l_FILE_CNT),'<filetype>',l_FILE_TYPE)
      INTO l_EMB_FNAME
      FROM dual;
	  
    EXCEPTION
    WHEN OTHERS THEN
      ERRMSG := 'Exeption Prod -- ' || SQLCODE || '--' || SQLERRM;
    END;
    EMBFNAME :=l_EMB_FNAME|| '.tmp';
    BEGIN
      INSERT
      INTO CLP_CONFIGURATION.CCF_FILE_CTRL
        (
          EMB_FNAME,
          CREATE_TOT,
          CREATE_DATE,
          INS_USER,
          INS_DATE,
          LAST_UPD_USER,
          LAST_UPD_DATE,
          FILE_COUNT,
          VENDOR_NAME
        )
        VALUES
        (
          EMBFNAME,
          0,
          SYSDATE,
          LUPDUSER,
          SYSDATE,
          LUPDUSER,
          SYSDATE,
          l_FILE_CNT,
          VENDER
        );
      ERRMSG := 'OK';
    EXCEPTION
    WHEN OTHERS THEN
      ERRMSG := 'Exception 2 --' || SQLCODE || '--' || SQLERRM;
    END;
  EXCEPTION
  WHEN OTHERS THEN
    ERRMSG := 'Exeption KK 1 -- ' || SQLCODE || '--' || SQLERRM;
  END;
EXCEPTION
WHEN OTHERS THEN
  ERRMSG := 'Exeption Main -- ' || SQLCODE || '--' || SQLERRM;
END;
/
show errors;

-------------------------------------------------------------------