package com.github.typesafe_query;

import com.github.typesafe_query.jdbc.translator.DefaultDialectTranslator;
import com.github.typesafe_query.jdbc.translator.DialectTranslator;
import com.github.typesafe_query.query.SQLBuilder;
import com.github.typesafe_query.query.internal.sql.DefaultSQLBuilder;

public final class DBManager {
	private static final DialectTranslator DEFAULT = new DefaultDialectTranslator();

	public static DialectTranslator getDialectTranslator(){
		return DEFAULT;
	}
	
	public static SQLBuilder getSQLBuilder(){
		return new DefaultSQLBuilder();
	}
}
