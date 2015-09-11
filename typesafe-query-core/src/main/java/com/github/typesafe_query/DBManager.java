package com.github.typesafe_query;

import com.github.typesafe_query.jdbc.converter.DefaultJdbcValueConverter;
import com.github.typesafe_query.jdbc.converter.JdbcValueConverter;
import com.github.typesafe_query.query.SQLBuilder;
import com.github.typesafe_query.query.internal.sql.DefaultSQLBuilder;

public final class DBManager {
	private static final JdbcValueConverter DEFAULT = new DefaultJdbcValueConverter();

	public static JdbcValueConverter getJdbcValueConverter(){
		return DEFAULT;
	}
	
	public static SQLBuilder getSQLBuilder(){
		return new DefaultSQLBuilder();
	}
}
