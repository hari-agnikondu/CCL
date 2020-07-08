package com.incomm.cclp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class CclpOrderApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CclpOrderApplication.class, args);
		
	}
	
	/**
	 * create RestTemplate Object 
	 * @return restTemplate
	 */
 	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
