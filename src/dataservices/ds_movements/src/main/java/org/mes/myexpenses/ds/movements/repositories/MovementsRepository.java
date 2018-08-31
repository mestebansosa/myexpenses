package org.mes.myexpenses.ds.movements.repositories;

import org.mes.myexpenses.ds.movements.domains.Movement;
import org.mes.myexpenses.ds.movements.domains.MovementId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "movements", path = "movements")
public interface MovementsRepository extends MongoRepository<Movement, MovementId> {
}