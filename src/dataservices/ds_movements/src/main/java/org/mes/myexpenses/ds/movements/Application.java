package org.mes.myexpenses.ds.movements;

import org.mes.myexpenses.ds.movements.repositories.CategoriesRepository;
import org.mes.myexpenses.ds.movements.repositories.MovementsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Bootstrap loader
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "org.mes.myexpenses.ds.movements.repositories", basePackageClasses = {
		CategoriesRepository.class, MovementsRepository.class }, mongoTemplateRef = "mongoTemplate")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
