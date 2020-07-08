package com.incomm.cclp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class CclpApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CclpApplication.class, args);
		
	}
}
