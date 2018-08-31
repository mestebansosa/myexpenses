package org.mes.myexpenses.commons.rest.config;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.mes.myexpenses.commons.rest.accesslog.CustomRequestLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
public class MyJettyWebServerCustomizer
		implements WebServerFactoryCustomizer<JettyServletWebServerFactory> {
	
	@Value("${server.port:8080}")
	private int serverPort;

	@Override
	public void customize(JettyServletWebServerFactory factory) {
		// customize the factory here
		factory.setPort(serverPort);
	}
}

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "jetty.thread.pool")
@ConditionalOnProperty(prefix = "jetty.thread.pool", name = "enabled", havingValue = "true")
class CustomJettyThreadPool {
	private int minThreads = 50;
	private int maxThreads = 200;
	private int idleTimeout = 60000;

	@PostConstruct
	public void init() {
		log.info("CustomJettyThreadPool [minThreads={}, maxThreads={}, idleTimeout={}]", minThreads, maxThreads,
				idleTimeout);
	}

	@Bean
	public JettyServerCustomizer threadPool() {
		return new JettyServerCustomizer() {
			@Override
			public void customize(final Server server) {
				final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
				threadPool.setMaxThreads(maxThreads);
				threadPool.setMinThreads(minThreads);
				threadPool.setIdleTimeout(idleTimeout);
			}
		};
	}
}

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "jetty.access.log")
@ConditionalOnProperty(prefix = "jetty.access.log", name = "enabled", havingValue = "true")
class CustomJettyAccessLog {
	private boolean origin = false;
	private boolean latency = false;
	private boolean extended = false;
	private boolean server = false;
	private boolean proxyAddress = false;
	private String timeZone = "GMT";
	private Locale locale = Locale.getDefault();
	private String logName = "org.eclipse.jetty.server.RequestLog";
	private String dateFormat = "dd/MMM/yyyy:HH:mm:ss Z";
	private boolean customField = false;
	private String customFieldName = "";

	@PostConstruct
	public void init() {
		log.info(
				"CustomJettyAccessLog Configuration [origin={}, latency={}, extended={}, server={}, proxyAddress={}, "
						+ "timeZone={}, locale={}, logName={}, dateFormat={}, customParam={}, customParamName={}]",
				origin, latency, extended, server, proxyAddress, timeZone, locale, logName, dateFormat, customField,
				customFieldName);
	}

	@Bean
	public JettyServerCustomizer accessLog() {
		return new JettyServerCustomizer() {
			@Override
			public void customize(final Server _server) {
				_server.setRequestLog(CustomRequestLog.builder().isOrigin(origin).isExtended(extended)
						.isLatency(latency).isServer(server).isProxiedForAddressPrefered(proxyAddress).logName(logName)
						.dateFormat(dateFormat).timeZone(timeZone).locale(locale).isCustomField(customField)
						.customFieldName(customFieldName).build());
			}
		};
	}
}
