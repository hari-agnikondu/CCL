package com.incomm.cclp.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.incomm.cclp.domain.Order;
import com.incomm.cclp.domain.Stock;
import com.incomm.cclp.util.Util;


@Configuration
public class DatasourceConfig {

	@Autowired
	private Environment environment;

	@Value("${spring.transactionalDatasource.jndi-name}")
	private String transactionalJndiName;
	
	@Value("${spring.orderDatasource.jndi-name}")
	private String orderJndiName;
	
    @Primary
	@ConfigurationProperties(prefix = "spring.orderDatasource")
	@Bean(name = "orderDs")
	public DataSource getOrderDs() {
		
		if (!Util.hasLocalProfile(environment.getActiveProfiles()))
		{
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			
			return lookup.getDataSource(orderJndiName);
		}

		return DataSourceBuilder.create().build();
	}

		
	@ConfigurationProperties(prefix = "spring.transactionalDatasource")
	@Bean(name = "transactionalDs")
	public DataSource getTransactionalDs() {

		if (!Util.hasLocalProfile(environment.getActiveProfiles()))
		{
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			
			return lookup.getDataSource(transactionalJndiName);
		}
		
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "orderEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("orderDs") DataSource dataSource) {
		return builder.dataSource(dataSource).packages(Order.class,Stock.class).persistenceUnit("order").build();
	}

	
	 @Primary
	 @Bean(name = "orderTransactionManager")
	 public PlatformTransactionManager orderTransactionManager(@Qualifier("orderEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
	  return new JpaTransactionManager(entityManagerFactory);
	 }
}
