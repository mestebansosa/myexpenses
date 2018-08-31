package org.mes.myexpenses.ds.administration.constants;

public class MyExpensesConstants {
	public static class ENDPOINTS {
		public static final String SESSIONS = "api/sessions";
		public static final String USERS = "api/users";
		public static final String MONGO = "api/mongo";
		
		public static final String COUNTER_SESSIONS = "sessions";
		public static final String COUNTER_SESSIONS_FINDBYID = COUNTER_SESSIONS + ".findById";
		public static final String COUNTER_SESSIONS_FINDFIRSTBYSTART = COUNTER_SESSIONS + ".findFirstByStart";
		public static final String COUNTER_SESSIONS_FINDALL = COUNTER_SESSIONS + ".findAll";
		public static final String COUNTER_SESSIONS_COUNT = COUNTER_SESSIONS + ".count";
		public static final String COUNTER_SESSIONS_EXISTBYID = COUNTER_SESSIONS + ".existsById";
		public static final String COUNTER_SESSIONS_SAVE = COUNTER_SESSIONS + ".save";
		public static final String COUNTER_SESSIONS_DELETE = COUNTER_SESSIONS + ".delete";
		public static final String COUNTER_SESSIONS_DELETEBYID = COUNTER_SESSIONS + ".deleteById";
		
		public static final String COUNTER_USERS = "users";
		public static final String COUNTER_USERS_FINDBYID = COUNTER_USERS + ".findById";
		public static final String COUNTER_USERS_FINDFIRSTBYFIRSTNAME = COUNTER_USERS + ".findFirstByFirstname";
		public static final String COUNTER_USERS_FINDALL = COUNTER_USERS + ".findAll";
		public static final String COUNTER_USERS_COUNT = COUNTER_USERS + ".count";
		public static final String COUNTER_USERS_EXISTBYID = COUNTER_USERS + ".existsById";
		public static final String COUNTER_USERS_SAVE = COUNTER_USERS + ".save";
		public static final String COUNTER_USERS_VALIDATE = COUNTER_USERS + ".validate";
		public static final String COUNTER_USERS_DELETE = COUNTER_USERS + ".delete";
		public static final String COUNTER_USERS_DELETEBYID = COUNTER_USERS + ".deleteById";
		
		public static final String COUNTER_MONGO = "mongotemplate";
		public static final String COUNTER_MONGO_FINDONE = COUNTER_MONGO + ".findOne";
		public static final String COUNTER_MONGO_UPDATECANDIDATESESSIONS = COUNTER_MONGO + ".updateCandidateSessions";
	}
}
