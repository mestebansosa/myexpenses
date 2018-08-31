package org.mes.myexpenses.commons.rest.config;

import javax.annotation.PostConstruct;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "rest.template")
@ConditionalOnProperty(prefix = "rest.template", name = "enabled", havingValue = "true")
public class RestTemplateConfig {
	// connection Pool
	private int maxTotal = 200;
	// connection Pool per route
	private int defaultMaxPerRoute = 100;
	// how long do we wait for the connection request to be answered
	private int connectionRequestTimeout = 2500;
	// how long do we wait to establish a connection?
	private int connectTimeout = 2500;
	// how long do we wait, at most, between data packets (SO_TIMEOUT)
	private int socketTimeout = 1000;

	@PostConstruct
	public void init() {
		log.info(
				"REST Template [maxTotal={}, defaultMaxPerRoute={}, connectionRequestTimeout={}, connectTimeout={}, socketTimeout={}]",
				maxTotal, defaultMaxPerRoute, connectionRequestTimeout, connectTimeout, socketTimeout);
	}

/*	@Bean("custom-rest-template")
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.requestFactory(getClientHttpRequestFactory()).build();
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(maxTotal);
		connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();

		HttpClientBuilder builder = HttpClientBuilder.create().setConnectionManager(connectionManager)
				.setDefaultRequestConfig(config);

		return new HttpComponentsClientHttpRequestFactory(builder.build());
	}
	
*/	
	@Bean("custom-rest-template")
	public RestTemplate restTemplate() {
		return new RestTemplate(getClientHttpRequestFactory2());
	}
	
/*	@Autowired
	@Qualifier("custom-rest-template")
	public RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory2());
*/
	private ClientHttpRequestFactory getClientHttpRequestFactory2() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(maxTotal);
		connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
		
	    RequestConfig config = RequestConfig.custom()
	      .setConnectTimeout(connectTimeout)
	      .setConnectionRequestTimeout(connectionRequestTimeout)
	      .setSocketTimeout(socketTimeout)
	      .build();
	    
	    CloseableHttpClient client = HttpClientBuilder
	      .create()
	      .setConnectionManager(connectionManager)
	      .setDefaultRequestConfig(config)
	      .build();
	    
	    return new HttpComponentsClientHttpRequestFactory(client);
	}
}