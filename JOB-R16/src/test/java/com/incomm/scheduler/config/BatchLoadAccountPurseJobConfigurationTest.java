package com.incomm.scheduler.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

@RunWith(MockitoJUnitRunner.class)
public class BatchLoadAccountPurseJobConfigurationTest {
	@Mock
	private JobBuilderFactory jobBuilderFactory;

	@Mock
	private StepBuilderFactory stepBuilderFactory;
	@InjectMocks
	private BatchLoadAccountPurseJobConfiguration batchLoadAccountPurseJobConfiguration;

	@Test
	public void testGetCardStatusClause() throws Exception {
		
		String defaultResponse = "('1')";
		
		String cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause(null);
		Assert.assertEquals("Default cardStatusCaluse did not match", defaultResponse, cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause("");
		Assert.assertEquals("Default cardStatusCaluse did not match", defaultResponse, cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause("          ");
		Assert.assertEquals("Default cardStatusCaluse did not match", defaultResponse, cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause("1");
		Assert.assertEquals("Default cardStatusCaluse did not match", defaultResponse, cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause("1,");
		Assert.assertEquals("Default cardStatusCaluse did not match", defaultResponse, cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause("1,,,");
		Assert.assertEquals("Default cardStatusCaluse did not match", defaultResponse, cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause("1,2");
		Assert.assertEquals("Default cardStatusCaluse did not match", "('1','2')", cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause("1,2,,,");
		Assert.assertEquals("Default cardStatusCaluse did not match", "('1','2')", cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause(",1,2, , ,");
		Assert.assertEquals("Default cardStatusCaluse did not match", "('1','2')", cardStatusClause);

		cardStatusClause = batchLoadAccountPurseJobConfiguration.getCardStatusClause(" , 1 , 2 , , ,");
		Assert.assertEquals("Default cardStatusCaluse did not match", "('1','2')", cardStatusClause);

		
	}

}
