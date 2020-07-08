package com.incomm.scheduler.config;
/**
 * BatchCoreConfiguration provides the jobRepository,jobOperator,jobExplorer,asyncJobLauncher to the spring batch processing.
 * author venkateshgaddam
 */

import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan
public class BatchCoreConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private DataSource dataSource;
    
    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        factory.setIsolationLevelForCreate("ISOLATION_READ_UNCOMMITTED");
        
        return factory.getObject();        
    }
    
    /** job launcher used for executing jobs on http requests, here: for batch admin */
    @Bean 
    public JobLauncher asyncJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
    
    
    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry() {
            @Override
            public Set<String> getJobNames() {
                return new TreeSet<>(super.getJobNames());
            }
        };
        
    }
    
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry());
        return jobRegistryBeanPostProcessor;
    }
    
	@Bean
	public JobOperator jobOperator(final JobLauncher jobLauncher, final JobRepository jobRepository,
	        final JobRegistry jobRegistry, final JobExplorer jobExplorer) {
	    final SimpleJobOperator jobOperator = new SimpleJobOperator();
	    jobOperator.setJobLauncher(jobLauncher);
	    jobOperator.setJobRepository(jobRepository);
	    jobOperator.setJobRegistry(jobRegistry);
	    jobOperator.setJobExplorer(jobExplorer);
	    return jobOperator;
	}

	@Bean
	public JobExplorer jobExplorer(final @Qualifier("inventoryDs") DataSource dataSource) throws Exception {
	    final JobExplorerFactoryBean bean = new JobExplorerFactoryBean();
	    bean.setDataSource(dataSource);
	    bean.setTablePrefix("BATCH_");
	    bean.setJdbcOperations(new JdbcTemplate(dataSource));
	    bean.afterPropertiesSet();
	    return bean.getObject();
	}
    
}