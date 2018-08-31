package org.mes.myexpenses.ds.movements.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import javax.annotation.PostConstruct;

import org.mes.myexpenses.ds.movements.domains.Category;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "myexpenses.dataservices.mongodb")
@Service
@Data
public class MongoOperationsImpl implements MongoOperations {

	// https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#repositories.create-instances.java-config
	private final MongoTemplate mongoTemplate;

	public MongoOperationsImpl(@Qualifier("mongoTemplate") MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


	@PostConstruct
	public void init() {
		log.info("MongoOperationsImpl");
	}
	
	@Override
	public Category findOne(@RequestParam String name) {
		log.info("Find User by id '{}'", name);
		Category category = mongoTemplate.findOne(new Query(where("name").is(name)), Category.class);
		return category;
	}

}
