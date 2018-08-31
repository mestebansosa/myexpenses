package org.mes.myexpenses.ds.administration.repositories;

import java.util.Optional;

import org.mes.myexpenses.ds.administration.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UsersRepository extends MongoRepository<User, String> {
	Optional<User> findFirstByFirstname(String firstname);
}