package org.mes.myexpenses.ds.movements.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

//@EnableMongoRepositories(basePackageClasses = {SessionsRepository.class, UsersRepository.class}, mongoTemplateRef = "mongoTemplate")
//@EnableMongoRepositories(basePackages = "org.mes.myexpenses.repositories", basePackageClasses = {
//		SessionsRepository.class, UsersRepository.class }, mongoTemplateRef = "mongoTemplate")
@Configuration
public class MongoConfig extends AbstractMongoConfig {
	//@Override
	@Primary
	@Bean("mongoTemplate")
	public MongoTemplate getMongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}
}
