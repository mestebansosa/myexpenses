package org.mes.myexpenses.bs.movements.constants;

public class MyExpensesConstants {
	public static class ENDPOINTS {
		public static final String CATEGORIES = "api/categories";
		public static final String MOVEMENTS = "api/movements";
		public static final String MONGO = "api/mongo";
		
		public static final String COUNTER_CATEGORIES = "categories";
		public static final String COUNTER_CATEGORIES_FINDBYID = COUNTER_CATEGORIES + ".findById";
		public static final String COUNTER_CATEGORIES_FINDALL = COUNTER_CATEGORIES + ".findAll";
		public static final String COUNTER_CATEGORIES_COUNT = COUNTER_CATEGORIES + ".count";
		public static final String COUNTER_CATEGORIES_EXISTBYID = COUNTER_CATEGORIES + ".existsById";
		public static final String COUNTER_CATEGORIES_CREATE = COUNTER_CATEGORIES + ".create";
		public static final String COUNTER_CATEGORIES_UPDATE = COUNTER_CATEGORIES + ".update";
		public static final String COUNTER_CATEGORIES_DELETE = COUNTER_CATEGORIES + ".delete";
		public static final String COUNTER_CATEGORIES_DELETEBYID = COUNTER_CATEGORIES + ".deleteById";

		public static final String COUNTER_MOVEMENTS = "movements";
		public static final String COUNTER_EXPENSES = ".expenses";
		public static final String COUNTER_MOVEMENTS_FINDBYID = COUNTER_MOVEMENTS + ".findById";
		public static final String COUNTER_MOVEMENTS_FINDALL = COUNTER_MOVEMENTS + ".findAll";
		public static final String COUNTER_MOVEMENTS_COUNT = COUNTER_MOVEMENTS + ".count";
		public static final String COUNTER_MOVEMENTS_EXISTBYID = COUNTER_MOVEMENTS + ".existsById";
		public static final String COUNTER_MOVEMENTS_CREATE = COUNTER_MOVEMENTS + ".create";
		public static final String COUNTER_EXPENSESS_ADD = COUNTER_MOVEMENTS + COUNTER_EXPENSES + "Add";
		public static final String COUNTER_EXPENSESS_DELETE = COUNTER_MOVEMENTS + COUNTER_EXPENSES + "Delete";
		public static final String COUNTER_MOVEMENTS_DELETE = COUNTER_MOVEMENTS + ".delete";
		public static final String COUNTER_MOVEMENTS_DELETEBYID = COUNTER_MOVEMENTS + ".deleteById";

		public static final String COUNTER_MONGO = "mongotemplate";
		public static final String COUNTER_MONGO_FINDONE = COUNTER_MONGO + ".findOne";
	}
}
