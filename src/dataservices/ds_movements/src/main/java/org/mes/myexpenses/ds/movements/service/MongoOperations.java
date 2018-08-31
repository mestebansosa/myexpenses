package org.mes.myexpenses.ds.movements.service;

import org.mes.myexpenses.ds.movements.domains.Category;

public interface MongoOperations {
	public Category findOne(String name);
}
