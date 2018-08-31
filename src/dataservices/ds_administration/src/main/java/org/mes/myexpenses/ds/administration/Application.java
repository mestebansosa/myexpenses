package org.mes.myexpenses.ds.administration;

import org.mes.myexpenses.ds.administration.repositories.SessionsRepository;
import org.mes.myexpenses.ds.administration.repositories.UsersRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Bootstrap loader
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "org.mes.myexpenses.ds.administration.repositories", basePackageClasses = {
		SessionsRepository.class, UsersRepository.class }, mongoTemplateRef = "mongoTemplate")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
