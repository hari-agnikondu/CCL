package com.incomm.scheduler.external.adapter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.constants.APIConstants;
import com.incomm.scheduler.external.resource.LoadAccountPurseRequest;
import com.incomm.scheduler.external.resource.LoadAccountPurseResponse;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SpilServiceAdapter {

	private RestTemplate restTemplate;

	private String spilUrl;
	private final String purseLoadApiEndPoint;

	@Autowired
	public SpilServiceAdapter(RestTemplate restTemplate, @Value("${SPIL_BATCH_URL}") String spilUrl) {
		super();
		this.restTemplate = restTemplate;
		this.spilUrl = spilUrl;

		this.purseLoadApiEndPoint = this.spilUrl + "/purses/update";
	}

	public LoadAccountPurseResponse updateAccountPurse(LoadAccountPurseRequest request) {

		try {

			HttpEntity<LoadAccountPurseRequest> apiRequest = new HttpEntity<>(request, this.getHeader(request.getCorrelationId()));
			log.info("Calling spilendpoint for purse update operation for source reference number:" + request.getCorrelationId());
			ResponseEntity<LoadAccountPurseResponse> apiResponse = this.restTemplate.postForEntity(this.purseLoadApiEndPoint, apiRequest,
					LoadAccountPurseResponse.class);

			if (apiResponse.getStatusCode()
				.is2xxSuccessful()) {
				return apiResponse.getBody();
			} else {
				log.info("Received error response from spilService for sourceReferenceNumber:" + request.getCorrelationId()
						+ ", response:" + apiResponse.toString());
				return mapError(request.getCorrelationId(), request.getSpNumber(), apiResponse.getStatusCode()
					.name(), apiResponse.toString());
			}

		} catch (HttpStatusCodeException exception) {
			log.info("Unable to call spilService for request:" + request.toString() + ", response: " + exception.getResponseBodyAsString());
			return mapError(request.getCorrelationId(), request.getSpNumber(), exception.getRawStatusCode() + "",
					exception.getResponseBodyAsString());

		} catch (Exception exception) {
			log.error("Unable to call spilService at url:" + this.spilUrl + " for request:" + request.toString(), exception);
			return mapError(request.getCorrelationId(), request.getSpNumber(), "999", exception.getMessage());
		}

	}

	private LoadAccountPurseResponse mapError(String correlationId, String spNumber, String responseCode, String message) {
		return LoadAccountPurseResponse.builder()
			.correlationId(correlationId)
			.spNumber(spNumber)
			.responseCode(responseCode)
			.responseMessage(message)
			.build();
	}

	private HttpHeaders getHeader(String correlationId) {

		final HttpHeaders headers = new HttpHeaders();
		headers.set(APIConstants.API_HEADER_CORRELATION_ID, correlationId);
		headers.set(APIConstants.API_HEADER_DELIVERY_CHANNEL, "HOST");
		headers.set(APIConstants.API_HEADER_X_INCFS_DATE, ZonedDateTime.now()
			.format(DateTimeFormatter.RFC_1123_DATE_TIME));

		return headers;
	}

}
