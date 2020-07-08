package com.incomm.scheduler.config;

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
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.incomm.scheduler.model.CardRange;
import com.incomm.scheduler.utils.Util;

@Configuration
public class JobDatasourceConfig {

	@Autowired
	private Environment environment;

	@Value("${spring.inventoryDatasource.jndi-name}")
	private String inventoryJndiName;
	
	@Value("${spring.orderDatasource.jndi-name}")
	private String orderJndiName;
	
	@Value("${spring.transactionalDatasource.jndi-name}")
	private String transactionalJndiName;
	
	@Value("${spring.configurationDatasource.jndi-name}")
	private String configurationJndiName;
	

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
	@ConfigurationProperties(prefix = "spring.configurationDatasource")
	@Bean(name = "configurationDs")
	public DataSource getconfiguraitonDs() {
		
		if (!Util.hasLocalProfile(environment.getActiveProfiles()))
		{
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			
			return lookup.getDataSource(configurationJndiName);
		}

		return DataSourceBuilder.create().build();
	}
	
	@Primary	
	@ConfigurationProperties(prefix = "spring.inventoryDatasource")
	@Bean(name = "inventoryDs")
	public DataSource getInventoryDs() {

		if (!Util.hasLocalProfile(environment.getActiveProfiles()))
		{
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			
			return lookup.getDataSource(inventoryJndiName);
		}
		
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "inventoryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("inventoryDs") DataSource dataSource) {
		return builder.dataSource(dataSource).packages(CardRange.class).persistenceUnit("inventory").build();
	}

}
