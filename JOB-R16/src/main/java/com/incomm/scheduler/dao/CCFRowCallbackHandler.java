package com.incomm.scheduler.dao;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.model.CCFFormatDtl;
import com.incomm.scheduler.utils.HSMCommandBuilder;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ResponseMessages;
import com.incomm.scheduler.utils.Util;

public class CCFRowCallbackHandler implements RowCallbackHandler {

	String hsmIpAddress;
	int hsmPort;

	Logger logger = LogManager.getLogger(CCFRowCallbackHandler.class);
	
	Map<String,Object> valueObj1;
	String psFileName;
	FileWriter fileWriter;
	int count;
	int cardRecordCount = 1;
    
    CCFGenerationDAO ccfGenerationDao;
    
    HSMCommandBuilder hsmCommandBuilder = new HSMCommandBuilder();

	public void setFileWriter(FileWriter fileWriter) {
		this.fileWriter = fileWriter;
	}

	public void setHsmIpAddress(String hsmIpAddress) {
		this.hsmIpAddress = hsmIpAddress;
	}

	public void setHsmPort(int hsmPort) {
		this.hsmPort = hsmPort;
	}

	public void setCcfGenerationDao(CCFGenerationDAO ccfGenerationDao) {
		this.ccfGenerationDao = ccfGenerationDao;
	}

	public void setValueObj1(Map<String, Object> valueObj1) {
		this.valueObj1 = valueObj1;
	}

	public String getPsFileName() {
		return psFileName;
	}

	public void setPsFileName(String psFileName) {
		this.psFileName = psFileName;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {

		String srvCode = "";
		String psExpYY = null;
		String psExpMM = null;
		String addressone = null;
		String addresstwo = null;
		String addrcity = null;
		String cityStateZip = null;
		String stateCode = null;
		String pinCode = null;

		String msPan = "";

		java.sql.Date msExpiryDate;
		String msProdCode = "";
		String hashPancode = "";

		String msProxyNumber = "";

		
		String panLength = null;
		final StringBuilder tempSb = new StringBuilder();

		
		try {
			
		
		Map<String,Object> valueObj = rowAsMap(rs);	
		logger.debug("ccf valueObj:" + valueObj);
		valueObj.put(JobConstants.CCF_CARD_RECORD_COUNT, String.valueOf(cardRecordCount));
        
        msPan = (String) valueObj.get(JobConstants.CCF_PAN);
        msProdCode = String.valueOf(valueObj.get(JobConstants.CCF_PRODUCT_ID));
        logger.debug("ProductId for order {}",msProdCode);
        //srvCode = ccfGenerationDao.getServiceCode(msProdCode);
        srvCode = Util.convertAsString(valueObj1.get("srvCode"));
        logger.debug("Service code for product srvCode: {}",srvCode);
        valueObj.put(JobConstants.CCF_SRV_CODE, srvCode);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date = sdf1.parse((String) valueObj.get(JobConstants.CCF_EXPIRY_DATE));
		msExpiryDate = new java.sql.Date(date.getTime());
		valueObj.put(JobConstants.CCF_EXPIRY_DATE, msExpiryDate);
		hashPancode = (String) valueObj.get(JobConstants.CCF_CARD_NUM_HASH);
		logger.debug("hashPanCode: {}",hashPancode);
        msProxyNumber = (String) valueObj.get(JobConstants.CCF_PROXY_NUMBER);
        logger.debug("msProxyNumber: {}",msProxyNumber);
        valueObj.put(JobConstants.CCF_PROXY_NUMBER, msProxyNumber != null ? msProxyNumber : " ");
        logger.debug("CustomerId: {}",valueObj.get(JobConstants.CUSTOMER_ID));

        if (panLength == null || (" ".equals(panLength))) {
            panLength = msPan != null ? String.valueOf(msPan.length()) : " ";
        }
        
        if (!Objects.isNull(valueObj.get(JobConstants.CHILD_ORDER_ID))) {
			valueObj1.put(JobConstants.CHILD_ORDER_ID, valueObj.get(JobConstants.CHILD_ORDER_ID));
		}
        
        if (!Objects.isNull(valueObj.get("PARENTORDERID"))) {
			valueObj1.put("parentOrderId", valueObj.get("PARENTORDERID"));
		}
		if (!Objects.isNull(valueObj.get("ORDERTYPE"))) {
			valueObj1.put("orderType", valueObj.get("ORDERTYPE"));
		}
		
		if (!Objects.isNull(valueObj.get(JobConstants.SHIP_TO_ADDRESS1))) {
			valueObj1.put(JobConstants.CCF_REP_ADDRESS_ONE, valueObj.get(JobConstants.SHIP_TO_ADDRESS1));
		}
		
		if (!Objects.isNull(valueObj.get(JobConstants.SHIP_TO_ADDRESS2))) {
			valueObj1.put(JobConstants.CCF_REP_ADDRESS_TWO, valueObj.get(JobConstants.SHIP_TO_ADDRESS2));
		}
		if (!Objects.isNull(valueObj.get("CITY"))||!Objects.isNull(valueObj.get("STATE"))||!Objects.isNull(valueObj.get("POSTALCODE"))) {
			valueObj1.put("shipToCityStatePostalcode", valueObj.get("CITY")+JobConstants.COMMA_SEPARATOR+valueObj.get("STATE")+JobConstants.COMMA_SEPARATOR+valueObj.get("POSTALCODE"));
		}
		if (!Objects.isNull(valueObj.get("COUNTRY"))) {
			valueObj1.put("countryName", valueObj.get("COUNTRY"));
		}
		if (!Objects.isNull(valueObj.get("SHIPTOCOMPANYNAME"))) {
			valueObj1.put("companyName", valueObj.get("SHIPTOCOMPANYNAME"));
		}

		
        //String ccfGenerationVersion = ccfGenerationDao.getCCFFormatVersion(msProdCode);
		String ccfGenerationVersion = Util.convertAsString(valueObj1.get("ccfFormatversion"));
        logger.debug("Generating CCF file for version: {}",ccfGenerationVersion);
        valueObj.put(JobConstants.CCF_VERSION_ID, ccfGenerationVersion);

        psExpYY = Util.padLeft(String.valueOf(Util.getYear(msExpiryDate)), 2, '0');
        if(psExpYY != null) {
        psExpYY = psExpYY.substring(psExpYY.length() - 2);
        }
        psExpMM = Util.padLeft(String.valueOf(Util.getMonth(msExpiryDate)), 2, '0');

        msProdCode = String.valueOf(valueObj.get(JobConstants.CCF_PRODUCT_ID));
        valueObj.put(JobConstants.CCF_FULL_NAME, JobConstants.EMPTY_STRING);
        valueObj.put(JobConstants.CCF_EMB_NAME, JobConstants.EMPTY_STRING);
        valueObj.put(JobConstants.CCF_EMB_NAME_TWO, JobConstants.EMPTY_STRING);
              
        valueObj.put(JobConstants.UPC, valueObj1.get(JobConstants.UPC)!=null?valueObj1.get(JobConstants.UPC):JobConstants.EMPTY_STRING);
    
        //List<?> address= ccfGenerationDao.getAddressDetails(hashPancode);
       
        
//        for (Object addressDetail : address) {
//        	
//        	tempSb.delete(0, tempSb.length());
//        	
//            Map m = (Map) addressDetail;
//            addressone = m.get(JobConstants.CCF_ADDRESS_ONE) != null ?  String.valueOf( m.get(JobConstants.CCF_ADDRESS_ONE))
//                    : JobConstants.EMPTY_STRING;
//
//            addresstwo = m.get(JobConstants.CCF_ADDRESS_TWO)!= null ? String.valueOf(m.get(JobConstants.CCF_ADDRESS_TWO))
//                    : JobConstants.EMPTY_STRING;
//
//            addrcity = m.get(JobConstants.CCF_ADDRESS_CITY) != null ?  String.valueOf(m.get(JobConstants.CCF_ADDRESS_CITY))
//                    : JobConstants.EMPTY_STRING;
//            cityStateZip = addrcity;
//            stateCode = m.get(JobConstants.CCF_STATE_CODE) != null ?  String.valueOf( m.get(JobConstants.CCF_STATE_CODE))
//                    : JobConstants.EMPTY_STRING;
//            cityStateZip = String.valueOf(tempSb.append(cityStateZip).append(JobConstants.COMMA_SEPARATOR).append(stateCode));
//            tempSb.delete(0, tempSb.length());
//            pinCode = m.get(JobConstants.CCF_PIN_CODE) != null ?  (String) m.get(JobConstants.CCF_PIN_CODE)
//                    : JobConstants.EMPTY_STRING;
//            cityStateZip = String.valueOf(tempSb.append(cityStateZip).append(JobConstants.COMMA_SEPARATOR).append(pinCode));
//            
//            valueObj.put(JobConstants.CCF_ADDRESS_ONE, addressone);
//            valueObj.put(JobConstants.CCF_ADDRESS_TWO, addresstwo);
//            valueObj.put(JobConstants.CCF_CITY_STATE,cityStateZip);
//            logger.debug("Card generation address: addressOne: {}, addressTwo: {}, cityStateZip: {}",addressone,addresstwo,cityStateZip);
//        }

        String addr[] = Util.convertAsString(valueObj.get("address")).split("\\|");
        logger.debug("addr[]" + addr.length);
        if(addr !=null && addr.length==3) {
        	valueObj.put(JobConstants.CCF_ADDRESS_ONE, addr[0]);
            valueObj.put(JobConstants.CCF_ADDRESS_TWO, addr[1]);
            valueObj.put(JobConstants.CCF_CITY_STATE, addr[2]);  	
        }
      
        
        //String cvvSupported = ccfGenerationDao.getCvvSupportedFlag(msProdCode);
        String cvvSupported =  Util.convertAsString(valueObj1.get("cvvSupported"));
        if(cvvSupported.equalsIgnoreCase(JobConstants.CCF_ENABLE)) {
        	logger.debug("CVV support is enabled for this product");
        tempSb.delete(0, tempSb.length());
        //final String cvk = ccfGenerationDao.getCVK(msProdCode);
        final String cvk = Util.convertAsString(valueObj1.get("cvk"));
        logger.debug("CVK: {}",cvk);
        tempSb.delete(0, tempSb.length());

        String cvv1 = hsmCommandBuilder.genCVV(Util.padRight(msPan, msPan.length(), JobConstants.EMPTY_SPACE),  psExpYY + psExpMM,srvCode, cvk,hsmIpAddress,hsmPort);
        valueObj.put(JobConstants.CCF_CVV_1, cvv1);
        valueObj.put(JobConstants.FIVE_DIGIT_CSC, cvv1);
        logger.debug("cvv1: {}",cvv1);
        String cvv2 =hsmCommandBuilder.genCVV(
                Util.padRight(msPan, msPan.length(), JobConstants.EMPTY_SPACE), psExpMM + psExpYY,
                JobConstants.TRIPLE_ZERO, cvk,hsmIpAddress,hsmPort);
        logger.debug("cvv2: {}",cvv2);
        valueObj.put(JobConstants.CCF_CVV_2, cvv2);
        valueObj.put(JobConstants.THREE_DIGIT_CSC, cvv2);
        logger.debug("valueObj: {}",valueObj);
        }
        logger.debug("Cards generated so far is {}",cardRecordCount);
        cardRecordCount++;
        logger.debug("CCF VersionId-->"+valueObj.get(JobConstants.CCF_VERSION_ID).toString());
        StringBuilder detailSB = new StringBuilder();
        ccfFormation(valueObj, valueObj.get(JobConstants.CCF_VERSION_ID).toString(), JobConstants.CCF_DTL_PART, detailSB);

        String endValue = JobConstants.CCF_SLASH_N;
        detailSB.append(endValue);
        
        logger.debug("card has successfully generated for hash pan code : {} in ccf file: {}",hashPancode,psFileName );
        int i = psFileName.lastIndexOf('.');
        String name =psFileName.substring(0,i); 
        String FileName = name+".csv";
        
        
        writeDetail(fileWriter,detailSB);
        //updateCardStatus(hashPancode, FileName);
        
        valueObj1.put(JobConstants.CCF_PAN_LENGTH, panLength);
        
        logger.info("CCF Rec# " + cardRecordCount);
		}catch(Exception e) {
			logger.error("Error in CCFRowCallbackHandler:",e);
		}
		
    }
	

	
	
	/**
     * 
     * @param tempValueHashMap
     * @param object
     * @param dtlPart
     * @param valuSb
     */
    private void ccfFormation(Map<String, Object> tempValueHashMap, String ccfFormatVersion, String dtlPart,
            StringBuilder valuSb) {
        String tempValue = "";
        logger.info(CCLPConstants.ENTER);
        logger.debug("Writing {} Data",dtlPart);
        final List<CCFFormatDtl> ccfFormat = loadCCFVersionDtls(ccfFormatVersion, dtlPart);
        logger.debug("ccfFormation:"+ tempValueHashMap);
        if (ccfFormat != null) {
            for (CCFFormatDtl tempCCFFormatDtl : ccfFormat) {
 
                if (tempCCFFormatDtl.getDefaultValue() != null) {
                    valuSb.append(tempCCFFormatDtl.getDefaultValue());
                } else if ("H_GENERATE_DATE".equalsIgnoreCase(tempCCFFormatDtl.getTitleName())) {
                    final SimpleDateFormat sdf = new SimpleDateFormat(tempCCFFormatDtl.getFormat());
                    tempValue = sdf.format(new java.sql.Date(new java.util.Date().getTime()));
                    valuSb.append(tempValue);
                } else {
                	tempValue =  tempValueHashMap.get(tempCCFFormatDtl.getValueKey())!=null?tempValueHashMap.get(tempCCFFormatDtl.getValueKey()).toString():JobConstants.EMPTY_STRING;
 
                    final String tempVal = "L".equals(tempCCFFormatDtl.getFillerSide())
                            ? Util.padLeft(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
                                    tempCCFFormatDtl.getFiller().charAt(0))
                            : Util.padRight(tempValue, Integer.parseInt(tempCCFFormatDtl.getDataLength()),
                                    tempCCFFormatDtl.getFiller().charAt(0), false);
                    valuSb.append(tempVal);
 
                }
 
            }
        } else {
        	logger.error("Failed to get CCF Formation details");
        }
        logger.debug("EXIT");
    }
    
    private List<CCFFormatDtl> loadCCFVersionDtls(String version, String recType) {
    	logger.info(CCLPConstants.ENTER);
        HashMap<String, List<CCFFormatDtl>> ccfVersionDtls = new HashMap<>();
        List<CCFFormatDtl> ccfFormatDtlList = new ArrayList<>();
        try {
            List ccfVersionDetails = ccfGenerationDao.getCcfVersionDetails(version, recType);
            ccfVersionDtls.clear();
            for (Object ccfVersionDetail : ccfVersionDetails) {
                CCFFormatDtl temp = new CCFFormatDtl();
 
                Map m = (Map) ccfVersionDetail;
 
                temp.setTitleName((String) m.get("v5"));
                temp.setRecordType((String) m.get("v2"));
                temp.setDataLength((String) m.get("v7"));
                temp.setDefaultValue((String) m.get("v6"));
                temp.setFormat((String) m.get("v8"));
                temp.setFiller((String) m.get("v9"));
                temp.setFillerSide((String) m.get("v10"));
                temp.setValueKey((String) m.get("v4"));
                temp.setFormatType((String) m.get("v3"));
                final String versionType = (String) m.get("v1");
                ccfFormatDtlList = ccfVersionDtls.get(versionType);
 
                if (ccfFormatDtlList == null) {
                    ccfVersionDtls.put((String) m.get("v1"), new LinkedList<CCFFormatDtl>());
                }
                if ("H".equalsIgnoreCase(temp.getRecordType())) {
                    ccfVersionDtls.get(versionType).add(temp);
                } else if ("D".equalsIgnoreCase(temp.getRecordType())) {
                    ccfVersionDtls.get(versionType).add(temp);
                } else if ("T".equalsIgnoreCase(temp.getRecordType())) {
                    ccfVersionDtls.get(versionType).add(temp);
                }else {
                	logger.error("Missing record type");
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        logger.debug("EXIT");
        return ccfFormatDtlList;
    }
    
    /**
     * 
     * @param valueObj
     * @param versionIdCCF
     * @param listofData
     * @throws ServiceException 
     */
    private void writeDetail(FileWriter moFileWriter, StringBuilder record) throws ServiceException {
 
    	logger.info(CCLPConstants.ENTER);
        try {
            
                if (record.toString().length() != 1) {
                    moFileWriter.write(record.toString());
                }
                
        } catch (Exception e) {
            logger.error("Exception writing ccf:",e);
        	throw new ServiceException(ResponseMessages.CCF_ERROR_WRITE_DETAILS);
        }
        logger.debug("EXIT");
    }

    private Map<String,Object> rowAsMap(ResultSet rs) throws SQLException{
    	ResultSetMetaData md = rs.getMetaData();
    	int columns = md.getColumnCount();
    	Map<String, Object> row = new HashMap<String, Object>(columns);
    	
//    	for(int i = 1; i <= columns; ++i) {
//    		logger.debug(JdbcUtils.lookupColumnName(md, i));
//    		row.put(md.getColumnLabel(i), rs.getObject(i));
//    	}
    	ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
    	row = rowMapper.mapRow(rs,count);
    	logger.debug(row);
    	return row;

    }
}
