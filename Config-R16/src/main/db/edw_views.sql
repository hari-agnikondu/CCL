
CREATE OR REPLACE VIEW PRODUCT_VIEW AS
	SELECT prod.PRODUCT_ID AS Product_ID, 
		   prod.PRODUCT_NAME AS Product_Name, 
		   NVL(prod.ATTRIBUTES.Product.retailUPC, prod.ATTRIBUTES.Product.b2bUPC) AS Product_UPC,
		   purse.CURRENCY_CODE AS Currency_Code,
		   prod.IS_ACTIVE AS Is_Active,
		   prod.ISSUER_ID AS Iusser_ID,
		   prod.LAST_UPD_DATE AS Last_Upd_Date
		   FROM PRODUCT prod, PRODUCT_PURSE prod_purse, PURSE purse
		   WHERE prod.PRODUCT_ID = prod_purse.PRODUCT_ID 
		   AND prod_purse.PURSE_ID = purse.PURSE_ID 
		   AND prod_purse.IS_DEFAULT='Y'
		   ORDER BY prod.LAST_UPD_DATE DESC;
		
SELECT * FROM CLP_CONFIGURATION.PRODUCT_VIEW;		   
		   
CREATE OR REPLACE VIEW CARD_VIEW AS
	SELECT card.SERIAL_NUMBER AS Card_ID, 
		   card.PRODUCT_ID AS Product_ID, 
		   card.CARD_STATUS AS Card_Status,
		   1 as Primary_Card_Indicator,
		   card.DATE_OF_ACTIVATION AS Card_Activation_Date,
		   card.PAN_GENERATION_DATE AS Card_Issued_Date,
		   card.EXPIRY_DATE AS Card_Expiration_Date,
		   card.SERIAL_NUMBER AS Serial_Number,
		   card.CARD_NUM_ENCR AS EncryptedPAN,
		   card.CARD_RANGE_ID AS BIN_ID,
		   card.INS_DATE AS Source_Created_Date,
		   card.LAST_UPD_DATE AS Source_Modified_Date,
		   card.LAST_UPD_DATE AS LAST_UPD_DATE 
		   FROM CARD card
		   ORDER BY card.LAST_UPD_DATE DESC;		
		   
SELECT * FROM CLP_TRANSACTIONAL.CARD_VIEW;			   
		   
CREATE OR REPLACE VIEW ACCOUNT_VIEW AS
	SELECT account.ACCOUNT_NUMBER AS ACCOUNT_NUMBER, 
		   account.PRODUCT_ID AS Product_ID, 
		   account.ACCOUNT_STATUS AS Account_Status,
		   account.INS_DATE AS Account_Created_Date,
		   acct_purse.AVAILABLE_BALANCE AS Available_Balance,
		   (CASE WHEN acct_purse.AVAILABLE_BALANCE >= 0 THEN 1 ELSE -1 END) AS Available_Balance_Sign,
		   account.ACCOUNT_NUMBER AS External_Account_Number,
		   account.LAST_UPD_DATE AS Last_Transaction_Date,
		   acct_purse.CURRENCY_CODE AS Account_Currency_Code,
		   account.LAST_UPD_DATE AS LAST_UPD_DATE 
		   FROM ACCOUNT account INNER JOIN ACCOUNT_PURSE acct_purse ON 
		   account.ACCOUNT_ID = acct_purse.ACCOUNT_ID AND 
		   account.PRODUCT_ID = acct_purse.PRODUCT_ID
		   ORDER BY account.LAST_UPD_DATE DESC;		

SELECT * FROM CLP_TRANSACTIONAL.ACCOUNT_VIEW;
		   
CREATE OR REPLACE VIEW TRANSACTION_LOG_VIEW AS
	SELECT tl.CUSTOMER_ACCOUNT_NUMBER AS Account_Number, 
		   tl.CARD_NUMBER AS Card_ID, 
		   tl.RRN AS Transaction_Reference_Number,
		   tl.AUTH_ID AS Authorization_Code,
		   tl.MSG_TYPE AS Message_Type,
		   tl.REASON AS Authorization_Response,
		   tl.AMOUNT AS Transaction_Amount,
		   tl.CURRENCY_CODE AS Transaction_Currency_Code,
		   tl.TRANSACTION_DATE AS Transaction_Date,
		   tl.BUSINESS_DATE AS Post_Date,
		   tl.STORE_ID AS Store_Number,
		   tl.MERCHANT_ID AS Merchant_Number,
		   tl.MERCHANT_NAME AS Merchant_Name,
		   tl.MCCODE AS Merchant_Category_Code,
		   tl.MERCHANT_CITY AS Merchant_City,
		   tl.MERCHANT_STATE AS Merchant_State,
		   tl.MERCHANT_ZIP AS Merchant_Zip,
		   tl.TRANFEE_AMOUNT AS Fee_Amt,
		   (CASE WHEN tl.TRANFEE_AMOUNT > 0 THEN 1 WHEN tl.TRANFEE_AMOUNT < 0 THEN -1 ELSE 0 END) AS Fee_Amt_Sign,
		   tl.PRODUCT_ID AS Product_ID,
		   tl.ORGNL_RRN AS Orig_Reference_Number,
		   tl.SOURCE_NAME AS Origination_Name,
		   tl.ACCOUNT_BALANCE AS Balance_Amt,
		   tl.TERMINAL_ID AS Terminal_Identifier,
		   tl.AUTH_AMOUNT AS PreAuth_Amt
		   FROM TRANSACTION_LOG tl
		   ORDER BY tl.BUSINESS_DATE DESC;		   
		   
SELECT * FROM CLP_TRANSACTIONAL.TRANSACTION_LOG_VIEW;		   