package com.incomm.cclp.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class AppBeanConfig {

	@Bean
	public ValidatorFactory validatorFactory() {
		return Validation.buildDefaultValidatorFactory();
	}

	@Bean
	public Validator validator(ValidatorFactory validatorFactory) {
		return validatorFactory.getValidator();
	}

	@Bean
	public ObjectMapper objectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.findAndRegisterModules();

		return objectMapper;
	}

}
