package org.mes.myexpenses.commons.rest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;

/**
 * Component Configuration for Asynchronous task processing <br>
 * Use cases: <br>
 * - methods using @async annotation <br>
 * or <br>
 * - Rest Endpoints using: <br>
 * -- Deferred return values <br>
 * -- Callable return values <br>
 * -- CompletableFuture return values <br>
 *
 * @author Rui Pereira
 * @version 1.1
 */
@Slf4j
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "web.executor")
@ConditionalOnProperty(prefix = "web.executor", name = "enable", havingValue = "true")
public class CustomAsyncTaskExecutor implements AsyncConfigurer
{
    // public bean name
    public static final String ASYNC_TASK_EXECUTOR = "AsyncTaskExecutor";

    // properties with default values
    private int minPoolSize = 10;
    private int maxPoolSize = 100;
    private int maxQueueSize = 200;
    private boolean waitForTasksToCompleteOnShutdown = true;

    @PostConstruct
    public void init()
    {
        log.info(
                "CustomAsyncTaskExecutor [minPoolSize={}, maxPoolSize={}, maxQueueSize={}]",
                minPoolSize,
                maxPoolSize,
                maxQueueSize);
    }

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(AsyncTaskExecutor executor)
    {
        return new WebMvcConfigurerAdapter()
        {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer)
            {
                configurer.setTaskExecutor(executor);
                super.configureAsyncSupport(configurer);
            }
        };
    }

    @Override
    @Bean(name = ASYNC_TASK_EXECUTOR, initMethod = "initialize", destroyMethod = "destroy")
    public ThreadPoolTaskExecutor getAsyncExecutor()
    {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(minPoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(maxQueueSize);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
    {
        return new AsyncUncaughtExceptionHandler()
        {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... obj)
            {
                log.error("Exception message - {} Method name - {}", throwable.getMessage(), method.getName());
                for (final Object param : obj) {
                    log.error("Parameter value - {}", param);
                }
            }
        };
    }
}
