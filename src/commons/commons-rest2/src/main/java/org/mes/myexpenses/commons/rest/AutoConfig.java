package org.mes.myexpenses.commons.rest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan
@PropertySources(value = { @PropertySource(value = "classpath:spring-config.properties"),
		@PropertySource(value = "classpath:info-config.properties"),
		@PropertySource(value = "classpath:endpoints-config.properties") })
public class AutoConfig {}
