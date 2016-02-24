package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.jdbc.SQLRunner;
import com.github.typesafe_query.jdbc.SQLRunnerFactory;
import com.github.typesafe_query.query.SQLQuery;

public abstract class AbstractQueryExecutor {
	
	private final String sql;
	private final SQLRunner sqlRunner;
	
	AbstractQueryExecutor(SQLQuery sqlQuery) {
		this.sql = sqlQuery.getSQL(null);
		this.sqlRunner = SQLRunnerFactory.get().newSQLRunner(sql);
	}
	
	protected SQLRunner getSQLRunner(){
		return sqlRunner;
	}
	
	public void close(){
		sqlRunner.close();
	}

	@Override
	public String toString() {
		return this.sql;
	}
}
