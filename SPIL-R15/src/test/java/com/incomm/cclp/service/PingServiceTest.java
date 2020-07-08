package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.dao.impl.SpilDAOImpl;
import com.incomm.cclp.response.SPILResponseBuilder;
import com.incomm.cclp.service.impl.PingServiceImpl;
import com.incomm.cclp.util.LoggerUtil;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PingServiceTest {

	@InjectMocks
	PingServiceImpl pingServiceImpl;

	@Mock
	SpilDAOImpl spilDao;

	@Mock
	LoggerUtil loggerUtil;

	@Mock
	SPILResponseBuilder responseBuilder;

	@Before
	public void setup() throws UnknownHostException {
		ReflectionTestUtils.setField(pingServiceImpl, "functionCodeConfig", 831);
		ReflectionTestUtils.setField(pingServiceImpl, "hostId", false);
		ReflectionTestUtils.setField(pingServiceImpl, "heartBeat", false);
		ReflectionTestUtils.setField(pingServiceImpl, "localHost", InetAddress.getLocalHost().getHostName());
	}

	Map<String, String> map = new HashMap<>();
	ObjectMapper mapper = new ObjectMapper();
	public String hostName = "FSSCHND1530";
	public String serverDateTime = "2019-12-18 00:45:53.13-05:00";

	@Test
	public void getSuccessResponse() throws JsonParseException, JsonMappingException, IOException {

		MultiValueMap<String, String> reqHeaders = getSuccessHeaders();
		when(spilDao.getServerDate()).thenReturn(serverDateTime);
		when(responseBuilder.pingSuccessResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(successResponse());
		doNothing().when(loggerUtil).logSupport(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());
		
		ResponseEntity<String> tmp = pingServiceImpl.getServerDetails(reqHeaders, 831);
		map = mapper.readValue(tmp.getBody(), new TypeReference<HashMap<String, String>>() {
		});
		assertEquals("00", map.get("responseCode"));
	}

	@Test
	public void getInvalid_FunctionCode_Response() throws JsonParseException, JsonMappingException, IOException {

		MultiValueMap<String, String> reqHeaders = getSuccessHeaders();
		when(spilDao.getServerDate()).thenReturn(serverDateTime);
		when(responseBuilder.pingSuccessResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(failureResponse());
		doNothing().when(loggerUtil).logSupport(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());
		
		ResponseEntity<String> tmp = pingServiceImpl.getServerDetails(reqHeaders, 83);
		map = mapper.readValue(tmp.getBody(), new TypeReference<HashMap<String, String>>() {
		});
		assertEquals("01", map.get("responseCode"));
	}

	@Test
	public void getNullDateResponse() throws JsonParseException, JsonMappingException, IOException {

		MultiValueMap<String, String> reqHeaders = getSuccessHeaders();
		when(spilDao.getServerDate()).thenReturn(null);
		when(responseBuilder.pingSuccessResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(failureResponse());
		doNothing().when(loggerUtil).logSupport(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());
		
		ResponseEntity<String> tmp = pingServiceImpl.getServerDetails(reqHeaders, 831);
		map = mapper.readValue(tmp.getBody(), new TypeReference<HashMap<String, String>>() {
		});
		assertEquals("01", map.get("responseCode"));
	}

	@Test
	public void getHeartbeat_TrueResponse() throws JsonParseException, JsonMappingException, IOException {

		ReflectionTestUtils.setField(pingServiceImpl, "heartBeat", true);

		MultiValueMap<String, String> reqHeaders = getSuccessHeaders();
		ResponseEntity<String> tmp = pingServiceImpl.getServerDetails(reqHeaders, 831);

		assertEquals(HttpStatus.SC_FORBIDDEN, tmp.getStatusCodeValue());
	}

	@Test
	public void getFailureHeaderResponse() throws JsonParseException, JsonMappingException, IOException {

		MultiValueMap<String, String> reqHeaders = getInvalidHeaders();
		when(responseBuilder.pingSuccessResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyBoolean(), Mockito.anyString())).thenReturn(failureResponse());
		doNothing().when(loggerUtil).logSupport(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyMap());
		
		ResponseEntity<String> tmp = pingServiceImpl.getServerDetails(reqHeaders, 831);
		map = mapper.readValue(tmp.getBody(), new TypeReference<HashMap<String, String>>() {
		});
		assertEquals("01", map.get("responseCode"));
	}

	public Map<String, Object> successResponse() {
		Map<String, Object> respObj = new HashMap<>();
		respObj.put("responseCode", "00");
		respObj.put("responseMessage", "SUCCESS");
		respObj.put("dateTime", serverDateTime);
		respObj.put("hostIdentifier", "1775811070");
		return respObj;
	}

	public Map<String, Object> failureResponse() {
		Map<String, Object> respObj = new HashMap<>();
		respObj.put("responseCode", "01");
		respObj.put("responseMessage", "Invalid Headers/function Code");
		respObj.put("dateTime", serverDateTime);
		respObj.put("hostIdentifier", "1775811070");
		return respObj;
	}

	public MultiValueMap<String, String> getSuccessHeaders() {

		MultiValueMap<String, String> reqHeaders = new HttpHeaders();
		reqHeaders.add("Content-Type", "application/json");
		reqHeaders.add("x-incfs-channel", "SPIL");
		reqHeaders.add("x-incfs-correlationid", "12345");
		reqHeaders.add("x-incfs-date", "2018-12-06T23:59:59.123IST");
		return reqHeaders;
	}

	public MultiValueMap<String, String> getInvalidHeaders() {

		MultiValueMap<String, String> reqHeaders = new HttpHeaders();
		reqHeaders.add("Content-Type", "application/json");
		reqHeaders.add("x-incfs-channel", null);
		reqHeaders.add("x-incfs-correlationid", "12345");
		reqHeaders.add("x-incfs-date", "2018-12-06T23:59:59.123IST");
		return reqHeaders;
	}

}
