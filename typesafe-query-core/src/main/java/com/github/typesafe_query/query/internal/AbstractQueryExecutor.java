package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.jdbc.SQLRunner;
import com.github.typesafe_query.jdbc.SQLRunnerFactory;

public abstract class AbstractQueryExecutor {
	
	private final String sql;
	private final SQLRunner sqlRunner;
	
	AbstractQueryExecutor(String sql) {
		this.sql = sql;
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
