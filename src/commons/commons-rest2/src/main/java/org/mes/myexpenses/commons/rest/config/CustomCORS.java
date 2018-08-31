package org.mes.myexpenses.commons.rest.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Component Configuration for CORS Configuration Bean
 *
 * @author rudasilv
 * @version 1.0
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "cors")
@ConditionalOnProperty(prefix = "cors", name = "enabled", havingValue = "true")
public class CustomCORS {
	// properties (with default values)
	private boolean allowCredentials = false;
	private String allowedOrigins = "*";
	private String allowedHeaders = "*";
	private List<String> allowedMethods = Arrays.asList("HEAD", "GET");
	private long maxAge = 1800L;
	private List<String> exposeHeaders = Collections.emptyList();

	@PostConstruct
	public void init() {
		log.info(
				"CORS Configuration [allowCredentials={}, allowedOrigins={}, allowedMethods={}, allowedHeaders={}, maxAge={}, exposeHeaders={}]",
				allowCredentials, allowedOrigins, allowedMethods, allowedHeaders, maxAge, exposeHeaders);
	}

	@Bean
	public CorsFilter corsFilter() {
		final CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin(allowedOrigins);
		config.addAllowedHeader(allowedHeaders);
		config.setAllowedMethods(allowedMethods);
		config.setAllowCredentials(allowCredentials);
		config.setMaxAge(maxAge);
		config.setExposedHeaders(exposeHeaders);

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
