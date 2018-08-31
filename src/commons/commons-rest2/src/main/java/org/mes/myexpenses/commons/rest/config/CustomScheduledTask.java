package org.mes.myexpenses.commons.rest.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import lombok.extern.slf4j.Slf4j;

/**
 * Component Configuration for Background jobs
 *
 * @author rudasilv
 * @version 1.1
 */
@Slf4j
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "jobs.scheduled.task")
@ConditionalOnProperty(prefix = "jobs.scheduled.task", name = "enable", havingValue = "true")
public class CustomScheduledTask implements SchedulingConfigurer {
	// properties
	private int poolSize = 100;
	private String threadNamePrefix = "Alaska-ThreadPool-";
	private boolean waitForTasksToCompleteOnShutdown = true;

	@PostConstruct
	public void init() {
		log.info("CustomScheduledTask [poolSize={}, threadNamePrefix={}, waitForTasksToCompleteOnShutdown={}]",
				poolSize, threadNamePrefix, waitForTasksToCompleteOnShutdown);
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean(destroyMethod = "shutdown")
	public ThreadPoolTaskScheduler taskExecutor() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(poolSize);
		taskScheduler.setThreadNamePrefix(threadNamePrefix);
		taskScheduler.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
		return taskScheduler;
	}
}
