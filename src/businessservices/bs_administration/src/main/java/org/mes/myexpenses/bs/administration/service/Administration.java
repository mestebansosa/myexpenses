package org.mes.myexpenses.bs.administration.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mes.myexpenses.bs.administration.domains.Login;
import org.mes.myexpenses.bs.administration.domains.User;
import org.mes.myexpenses.commons.exceptions.exceptions.LoginException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;

public interface Administration {
	public void login(Login login, String sessionId) throws OperationNotCompletedException;

	public void logout(Login login, String sessionId, HttpServletRequest request, HttpServletResponse response)
			throws OperationNotCompletedException;

	public void signup(User signup, String sessionId) throws LoginException, OperationNotCompletedException;

}
