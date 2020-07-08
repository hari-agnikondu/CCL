package com.incomm.scheduler.config;


import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.file.Files;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import com.incomm.scheduler.constants.FSAPIConstants;
import com.incomm.scheduler.job.CurrencyRateJob;


@Configuration
public class CurrencyRateFilePollingConfiguration {

	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	@Qualifier("currencyRateUploadJob")
	Job currencyRateUploadJob;
	
	@Value("${CURRENCY_RATE_FILE_PATH}") String currencyRatePath;
	
	@Value("${FILE_POLL_INTERVAL}") Integer filePollInterval;
	
	@Bean
	public IntegrationFlow integrationFlow() {
	    return IntegrationFlows.from(Files.inboundAdapter(new File(currencyRatePath)).
	    		 filter(new RegexPatternFileListFilter(FSAPIConstants.CURRENCY_RATE_PATTERN)),
	            c -> c.poller(Pollers.fixedRate(filePollInterval).maxMessagesPerPoll(1))
	            .id("FilePolling")).
	    		handle("currencyRateJob","currencyRateUpload").
	            get();
	}
	@Bean
	public CurrencyRateJob currencyRateJob() {
		return new CurrencyRateJob();
	}
	@Bean
	public IntegrationFlow controlBus() {
	    return IntegrationFlows.from(controlChannel())
	            .controlBus()
	            .get();
	}

	@Bean
	public MessageChannel controlChannel() {
	    return MessageChannels.direct().get();
	}
	
	public String resetPollerReset(String message) {

		if(message.equalsIgnoreCase("stop")) {
		controlChannel().send(new GenericMessage<>("@FilePolling.stop()"));
		}else if(message.equalsIgnoreCase("start")) {
			controlChannel().send(new GenericMessage<>("@FilePolling.start()"));
		}else {
			return "Wrong input";
		}
		return "success";
	}
	
	
}
