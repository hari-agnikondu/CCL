package com.incomm.cclp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class MetaDataDAOImpl {
	
	@PersistenceContext
	@Qualifier("orderEntityManagerFactory")
	EntityManager em;

	
	public void getDeliveryChannel() {
		em.createNativeQuery("create table delivery_channel(CHANNEL_CODE VARCHAR(2),CHANNEL_NAME VARCHAR(100),"
				+ "CHANNEL_SHORT_NAME VARCHAR(10),INSTRUMENT_TYPE VARCHAR(50))").executeUpdate();
		em.createNativeQuery("insert into delivery_channel values('04','IVR','ivr','MOBILE_NO')").executeUpdate();	
		em.createNativeQuery("insert into delivery_channel values('02','CHW','chw','IP_ADDRESS')").executeUpdate();	
	}

	public void prmMetaData(){

        em.createNativeQuery("DROP TABLE IF EXISTS DELIVERY_CHANNEL_TRANSACTION").executeUpdate();		
		em.createNativeQuery("CREATE TABLE DELIVERY_CHANNEL_TRANSACTION "
				+ " (CHANNEL_CODE VARCHAR(50), " + 
				"	TRANSACTION_CODE VARCHAR(50), " + 
				"	CHANNEL_TRANSACTION_CODE VARCHAR(50), " + 
				"	MRIF VARCHAR(10)," + 
				"	ERIF VARCHAR(10)" + 
				"   ) ").executeUpdate();
	    em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF) values ('01','01','11','D','D')").executeUpdate();
		 em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF) values ('01','02','12','D','D')").executeUpdate();
		 em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF) values ('02','03','21','D','D')").executeUpdate();
		 em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF) values ('02','04','24','D','D')").executeUpdate();		 
	}
	
	public void jobSchedulerMetaData() {
		em.createNativeQuery("DROP TABLE IF EXISTS PROCESS_SCHEDULE").executeUpdate();	
		em.createNativeQuery("create table PROCESS_SCHEDULE(PROCESS_ID BIGINT,PROCESS_NAME VARCHAR(50),PROCESS_INTERVAL VARCHAR(50)," + 
				"SCHEDULE_DAYS VARCHAR(15),START_HOUR VARCHAR(2),START_MIN VARCHAR(2),PROCINTERVAL_TYPE VARCHAR(5),RETRY_CNT VARCHAR(50)," + 
				"SCHEDULER_STAT CHAR(1),MAIL_SUCCESS VARCHAR(80),MAIL_FAIL VARCHAR(80),PROCRETRY_DATE date, DAYOF_MONTH VARCHAR(15),MULTIRUN_INTERVAL VARCHAR(50)," + 
				"MULTIRUN_INTERVAL_TYPE VARCHAR(20),MULTIRUN_FLAG VARCHAR(20))").executeUpdate();
		em.createNativeQuery("insert into PROCESS_SCHEDULE values(1,'DAILY JOB','10','1|2|3','12','00','MM','1','E','1|2','1',sysdate,'1','1','HH','1')").executeUpdate();
	}
	
	public void jobSchedulerEmptyMetaData() {
		em.createNativeQuery("DROP TABLE IF EXISTS PROCESS_SCHEDULE").executeUpdate();		
		em.createNativeQuery("create table PROCESS_SCHEDULE(PROCESS_ID VARCHAR(5),PROCESS_NAME VARCHAR(50),PROCESS_INTERVAL VARCHAR(30),SCHEDULE_DAYS VARCHAR(15),START_HOUR VARCHAR(2),START_MIN VARCHAR(2),PROCINTERVAL_TYPE VARCHAR(5),RETRY_CNT VARCHAR(5),SCHEDULER_STAT CHAR(1),MAIL_SUCCESS VARCHAR(80),MAIL_FAIL VARCHAR(80),DAYOF_MONTH VARCHAR(15),MULTIRUN_INTERVAL VARCHAR(30),MULTIRUN_INTERVAL_TYPE VARCHAR(20),MULTIRUN_FLAG VARCHAR(20))").executeUpdate();
	}
	
	public void schedulerRunningMetaData() {
		em.createNativeQuery("DROP TABLE IF EXISTS SCHEDULER_RUNNING").executeUpdate();	
		em.createNativeQuery("create table SCHEDULER_RUNNING(PHYSICAL_SERVER VARCHAR(50),MANAGED_SERVER VARCHAR(50),SCHEDULER_RUNNING  VARCHAR(1))").executeUpdate();
		em.createNativeQuery("insert into SCHEDULER_RUNNING values('FSS','FSS','N')").executeUpdate();
		em.createNativeQuery("insert into SCHEDULER_RUNNING values('INCOMM','INCOMM','N')").executeUpdate();
	}
	
	public void schedulerRunningEmptyMetaData() {
		em.createNativeQuery("DROP TABLE IF EXISTS SCHEDULER_RUNNING").executeUpdate();	
		em.createNativeQuery("create table SCHEDULER_RUNNING(PHYSICAL_SERVER VARCHAR(50),MANAGED_SERVER VARCHAR(50),SCHEDULER_RUNNING  VARCHAR(1))").executeUpdate();
	}
	
	public void metaFileCreationStatus() {
		em.createNativeQuery("DROP TABLE IF EXISTS MENU_FILE_CREATION_STATUS").executeUpdate();	
		em.createNativeQuery("create table MENU_FILE_CREATION_STATUS(PHYSICAL_SERVER VARCHAR(50),MANAGED_SERVER VARCHAR(50)," + 
				"FILETOBE_GENERATED VARCHAR(1),LISTEN_PORT BIGINT)").executeUpdate();
		em.createNativeQuery("insert into MENU_FILE_CREATION_STATUS values('FSS','FSS','N',8080)").executeUpdate();
		em.createNativeQuery("insert into MENU_FILE_CREATION_STATUS values('INCOMM','INCOMM','N',9090)").executeUpdate();
	}
	
	public void createPermission() {
		em.createNativeQuery("insert into permission(PERMISSION_ID,PERMISSION_NAME,DESCRIPTION,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE,ENTITY,OPERATION) values(1,'ADD_PARTNER','Allows to Add a Partner',1,SYSDATE,1,SYSDATE,'Partner','Add')").executeUpdate();
		em.createNativeQuery("insert into permission(PERMISSION_ID,PERMISSION_NAME,DESCRIPTION,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE,ENTITY,OPERATION) values(2,'SEARCH_PARTNER','Allows to Search a Partner',1,SYSDATE,1,SYSDATE,'Partner','Search')").executeUpdate();
	}
	
	public void insertOrderMetaData() {
		
		em.createNativeQuery("INSERT INTO PARTNER(partner_id,partner_name) VALUES(1,'partner1')").executeUpdate();
		em.createNativeQuery("insert into issuer(issuer_id,issuer_name) VALUES(1,'issuer1')").executeUpdate();
		em.createNativeQuery("insert into Merchant(merchant_id,merchant_name) VALUES(1,'merchant1')").executeUpdate();
		em.createNativeQuery("insert into Location(location_id,location_name) VALUES('1','location1')").executeUpdate();
		em.createNativeQuery("").executeUpdate();
		em.createNativeQuery("").executeUpdate();
		
	}
	public void insertStockMetaData() {
		em.createNativeQuery("insert into Merchant(merchant_id,merchant_name,DESCRIPTION,MDM_ID,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE) VALUES(1,'merchant1','desc1','ERTFD2333',1,sysdate,1,sysdate)").executeUpdate();
		em.createNativeQuery("insert into Merchant(merchant_id,merchant_name,DESCRIPTION,MDM_ID,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE) VALUES(18,'merchant1','desc1','ERTFD2333',1,sysdate,1,sysdate)").executeUpdate();
		
		em.createNativeQuery("insert into partner(PARTNER_ID,PARTNER_NAME,MDM_ID,INS_USER,LAST_UPD_USER) values(1,'partner1','123456',1,1)").executeUpdate();
		em.createNativeQuery("insert into issuer(ISSUER_ID,ISSUER_NAME,INS_USER,LAST_UPD_USER) values(1,'issuer1',1,1)").executeUpdate();
		em.createNativeQuery("alter table product rename to product1").executeUpdate();
		
		em.createNativeQuery("create table product(PRODUCT_ID INTEGER,PRODUCT_NAME VARCHAR(100),DESCRIPTION VARCHAR(255),ATTRIBUTES VARCHAR(100),IS_ACTIVE CHAR(1),PARENT_PRODUCT_ID INTEGER," + 
			     			 "ISSUER_ID INTEGER,PARTNER_ID INTEGER,INS_USER INTEGER,INS_DATE date,LAST_UPD_USER INTEGER,LAST_UPD_DATE date,PRODUCT_SHORT_NAME VARCHAR(50))").executeUpdate();
		em.createNativeQuery("insert into product(PRODUCT_ID,PRODUCT_NAME,ISSUER_ID,PARTNER_ID,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE,PRODUCT_SHORT_NAME) " + 
							 "values(1,'hari',1,1,1,sysdate,1,sysdate,'PROD1')").executeUpdate();
		
		em.createNativeQuery("create table merchant_product(merchant_id VARCHAR(50),product_id DECIMAL,ins_user INTEGER,ins_date DATE,last_upd_user INTEGER,last_upd_date DATE)").executeUpdate();
		em.createNativeQuery("insert into merchant_product values('1',1,1,sysdate,1,sysdate) ").executeUpdate();
		
		em.createNativeQuery("alter table Location rename to Location1").executeUpdate();
		em.createNativeQuery("create table Location(MERCHANT_ID VARCHAR(50),LOCATION_ID VARCHAR(50),LOCATION_NAME VARCHAR(50),ADDRESS_ONE VARCHAR(50),ADDRESS_TWO VARCHAR(50),CITY VARCHAR(50),STATE VARCHAR(50),COUNTRY VARCHAR(50),ZIP VARCHAR(50),INS_USER INTEGER,INS_DATE date,LAST_UPD_USER INTEGER,LAST_UPD_DATE date)").executeUpdate();
		
		em.createNativeQuery("insert into Location(MERCHANT_ID,LOCATION_ID,LOCATION_NAME,ADDRESS_ONE,ADDRESS_TWO,CITY,STATE,COUNTRY,ZIP) values('1','1','CHENNAI','SIPCOT','CHENNAI','CHENNAI','TN','INDIA','603103')").executeUpdate();
		em.createNativeQuery("insert into LOCATION_INVENTORY(MERCHANT_ID,LOCATION_ID,PRODUCT_ID,INS_USER,LAST_UPD_USER) values('1','1',1,1,1)").executeUpdate();
		em.createNativeQuery("insert into LOCATION_INVENTORY(MERCHANT_ID,LOCATION_ID,PRODUCT_ID,INS_USER,LAST_UPD_USER) values('1','1',2,1,1)").executeUpdate();
	}
}
