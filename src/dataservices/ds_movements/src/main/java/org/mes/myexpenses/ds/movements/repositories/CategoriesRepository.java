package org.mes.myexpenses.ds.movements.repositories;

import org.mes.myexpenses.ds.movements.domains.Category;
import org.mes.myexpenses.ds.movements.domains.CategoryId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoriesRepository extends MongoRepository<Category, CategoryId> {
}