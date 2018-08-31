package org.mes.myexpenses.ds.administration.service;

import org.mes.myexpenses.ds.administration.domains.Session;
import org.mes.myexpenses.ds.administration.domains.User;

public interface MongoOperations {
	public User findOne(String name);
	public void updateCandidateSessions(Session session);
}
