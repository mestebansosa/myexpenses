package org.mes.myexpenses.ds.administration.repositories;

import java.util.Date;
import java.util.Optional;

import org.mes.myexpenses.ds.administration.domains.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "sessions", path = "sessions")
public interface SessionsRepository extends MongoRepository<Session, String> {
	Optional<Session> findFirstByStart(Date start);
}