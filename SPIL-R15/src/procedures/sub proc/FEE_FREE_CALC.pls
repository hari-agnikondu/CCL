create or replace PACKAGE FEE_FREE_CALC AS 

  PROCEDURE fee_freecnt_check (p_acctid_in                NUMBER,
                                p_freecnt_freq_in          VARCHAR2,
                                p_msgtype_in               VARCHAR2,
                                p_confgcnt_in              NUMBER,
                                p_last_trans_date          DATE,
                                p_free_txncount_flag         OUT VARCHAR2,
                                p_resp_code                 OUT VARCHAR2,
                                p_err_msg                 OUT VARCHAR2
                                );
                                
   PROCEDURE fee_freecnt_reset (p_acctid_in             NUMBER,
                                p_msgtype_in            VARCHAR2,
                                p_freecnt_freq_in       VARCHAR2,
                                p_reset_flag_in         VARCHAR2,
                                p_resp_code          OUT VARCHAR2,
                                p_err_msg         OUT VARCHAR2
                                );
                                
  PROCEDURE fee_freecnt_reverse (p_acctid_in        NUMBER,
                                  p_transaction_code_in       VARCHAR2,
                                  p_delivery_channel_in    VARCHAR2,
                                  p_resp_code     OUT VARCHAR2,
                                   p_err_msg         OUT VARCHAR2
                                  );                             

END FEE_FREE_CALC;