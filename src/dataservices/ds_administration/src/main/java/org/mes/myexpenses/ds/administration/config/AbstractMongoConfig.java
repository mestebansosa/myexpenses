package org.mes.myexpenses.ds.administration.config;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.mes.myexpenses.commons.rest.config.CustomCORS;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Data
public abstract class AbstractMongoConfig {
	// Mongo DB Properties
	protected String uri;
	protected String host, database, username;
	protected char[] password;
	protected int port;
	protected Options options;

	@Data
	public static class Options {
		protected int socketTimeout = 0; // ms
		protected int connectionTimeout = 1000 * 10; // ms
		protected int serverSelectionTimeout = 1000 * 30; // ms
	}

	@PostConstruct
	public void init() {
		log.info("Mongo Configuration [uri={}]", uri);
	}

	protected MongoClientOptions mongoOptions(MongoClientOptions uriOptions) {
		return MongoClientOptions.builder(uriOptions).connectTimeout(this.getOptions().getConnectionTimeout())
				.socketTimeout(this.getOptions().getSocketTimeout())
				.serverSelectionTimeout(this.getOptions().getServerSelectionTimeout()).build();
	}

	protected MongoDbFactory mongoDbFactory() {
		final MongoClientURI mongoClientURI = mongoClientURI();
		return new SimpleMongoDbFactory(
				new MongoClient(serverAddress(), credentials(), mongoOptions(mongoClientURI.getOptions())), database);
				// new MongoClient(serverAddress(), mongoOptions(mongoClientURI.getOptions())), database);
	}

	protected MongoClientURI mongoClientURI() {
		return new MongoClientURI(uri);
	}

	protected ServerAddress serverAddress() {
		return new ServerAddress(host, port);
	}

/*	protected List<MongoCredential> credentials() {
		return Collections.singletonList(MongoCredential.createCredential(username, database, password));
	}
*/
	protected MongoCredential credentials() {
		return MongoCredential.createCredential(username, database, password);
	}

	abstract public MongoTemplate getMongoTemplate() throws Exception;
}
