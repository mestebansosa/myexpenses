package org.mes.myexpenses.ds.administration.service;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.mes.myexpenses.ds.administration.domains.Session;
import org.mes.myexpenses.ds.administration.domains.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.client.result.UpdateResult;

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
	public User findOne(@RequestParam String name) {
		log.info("Find User by id '{}'", name);
		User user = mongoTemplate.findOne(new Query(where("name").is(name)), User.class);
		return user;
	}

	@Override
	public void updateCandidateSessions(Session session) {
		log.info("updateCandidateSessions {} {}", session.getUser(), session.getId());
		// db.sessions.find( {$and : [{'use':'string'},{_id:'sessionid'},{end:{$exists:false}} ]} )
		Query query = new Query();

/*		Other way.
 		query.addCriteria(
				Criteria.where("use").is(session.getUser()).and("_id").ne(session.getId()).and("end").exists(false));
*/
		query.addCriteria(
				Criteria.where("use").is(session.getUser())
				.andOperator(
						Criteria.where("_id").ne(session.getId()), 
						Criteria.where("end").exists(false))
				);

		Update update = new Update();
		update.set("end", new Date());
		UpdateResult result = mongoTemplate.updateMulti(query, update, Session.class);
		log.info("updateCandidateSessions result mofified {}", result.getModifiedCount());
	}

}
