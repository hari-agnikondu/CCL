package com.incomm.cclp.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;

	private static void setContext(ApplicationContext context) {
		SpringContext.context = context;
	}

	public static <T extends Object> T getBean(Class<T> beanClass) {
		return context == null ? null : context.getBean(beanClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContext.setContext(context);
	}

}
