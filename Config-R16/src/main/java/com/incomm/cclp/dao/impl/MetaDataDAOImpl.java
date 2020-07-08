package com.incomm.cclp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class MetaDataDAOImpl {
	
	@PersistenceContext
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
				"	ERIF VARCHAR(10)," + 
				"	FLEX_DESCRIPTION VARCHAR(100)" + 
				"   ) ").executeUpdate();
	    em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF,FLEX_DESCRIPTION) values ('01','01','11','D','D','dgdfgddffgdg')").executeUpdate();
		 em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF,FLEX_DESCRIPTION) values ('01','02','12','D','D','tretret')").executeUpdate();
		 em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF,FLEX_DESCRIPTION) values ('02','03','21','D','D','swe')").executeUpdate();
		 em.createNativeQuery(
				"Insert into DELIVERY_CHANNEL_TRANSACTION (CHANNEL_CODE,TRANSACTION_CODE,CHANNEL_TRANSACTION_CODE,MRIF,ERIF,FLEX_DESCRIPTION) values ('02','04','24','D','D','were')").executeUpdate();		 
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
	
	public void createCountry() {
		em.createNativeQuery("insert into country_code(country_code,currency_id,country_name,switch_country_code,iso_country_code,alpha_country_code,ins_user,ins_date,last_upd_user,last_upd_date) values(3,124,'CANADA',1,1,'CA',1,SYSDATE,1,SYSDATE)").executeUpdate();

	}
	
	public void createState() {
		em.createNativeQuery("insert into state_code(country_code,state_code,state_name,switch_state_code,alpha_country_code,ins_user,ins_date,last_upd_user,last_upd_date) values(3,1,'NEW YORK','NY','CA',1,SYSDATE,1,SYSDATE)").executeUpdate();

	}
	
	public void createCurrency() {
		em.createNativeQuery("insert into currency_code(currency_id,currency_code,currency_description,minor_units) values(124,'CAD','Canadian Dollars',0)").executeUpdate();

	}
}
