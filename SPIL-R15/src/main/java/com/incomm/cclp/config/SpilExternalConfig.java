package com.incomm.cclp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpilExternalConfig {

	@Value("${URL_READ_TIMEOUT}")
	private int urlReadTimeout;

	@Value("${URL_CONNECTION_TIMEOUT}")
	private int urlConnectionTimeout;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(clientHttpRequestFactory());
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(urlReadTimeout);
		factory.setConnectTimeout(urlConnectionTimeout);
		return factory;
	}
}
