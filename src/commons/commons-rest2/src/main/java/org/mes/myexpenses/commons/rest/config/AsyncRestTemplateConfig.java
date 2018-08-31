package org.mes.myexpenses.commons.rest.config;

import javax.annotation.PostConstruct;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "async.rest.template")
@ConditionalOnProperty(prefix = "async.rest.template", name = "enabled", havingValue = "true")
public class AsyncRestTemplateConfig
{
    // connection Pool
    private int maxTotal = 200;
    private int defaultMaxPerRoute = 100;
    // timeouts
    private int connectionRequestTimeout = 5000;
    private int connectTimeout = 5000;
    private int socketTimeout = 5000;

    @PostConstruct
    public void init()
    {
        log.info(
                "ASYNC REST Template [maxTotal={}, defaultMaxPerRoute={}, connectionRequestTimeout={}, connectTimeout={}, " +
                        "socketTimeout={}]",
                maxTotal,
                defaultMaxPerRoute,
                connectionRequestTimeout,
                connectTimeout,
                socketTimeout);
    }

    @Bean("custom-async-rest-template")
    public AsyncRestTemplate getAsyncRestTemplate() throws IOReactorException
    {
        return new AsyncRestTemplate(getAsyncRequestFactory());
    }

    @Bean(destroyMethod = "destroy")
    public HttpComponentsAsyncClientHttpRequestFactory getAsyncRequestFactory() throws IOReactorException
    {
        HttpComponentsAsyncClientHttpRequestFactory asyncRequestFactory = new HttpComponentsAsyncClientHttpRequestFactory();
        asyncRequestFactory.setHttpAsyncClient(asyncHttpClient());
        return asyncRequestFactory;
    }

    @Bean
    public CloseableHttpAsyncClient asyncHttpClient() throws IOReactorException
    {
        final IOReactorConfig ioReactorConfig = IOReactorConfig
                .custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setConnectTimeout(connectTimeout)
                .setSoTimeout(socketTimeout)
                .build();

        final RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        final PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(
                new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

        return HttpAsyncClients
                .custom()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultIOReactorConfig(ioReactorConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}
