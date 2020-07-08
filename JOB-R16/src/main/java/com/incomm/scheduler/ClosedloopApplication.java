package com.incomm.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@SpringBootApplication
@ComponentScan(basePackages = { "com.incomm.scheduler" })
@EnableAsync
@EnableRetry
public class ClosedloopApplication extends SpringBootServletInitializer {

	@Value("${REST_TEMPLATE_TIMEOUT:120}")
	Integer restTemplateTimeout;

	public static void main(String[] args) {
		SpringApplication.run(ClosedloopApplication.class, args);
	}

	/**
	 * create TaskScheduler Object
	 * 
	 * @return TaskScheduler
	 */
	@Bean
	public TaskScheduler scheduler() {
		return new ThreadPoolTaskScheduler();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		int timeout = this.restTemplateTimeout == null ? 120 * 1000 : this.restTemplateTimeout * 1000;
		return builder.setConnectTimeout(timeout)
			.setReadTimeout(timeout)
			.build();

	}

}
