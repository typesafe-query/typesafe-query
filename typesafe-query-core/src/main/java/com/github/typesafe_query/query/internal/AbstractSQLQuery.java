package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.SQLQuery;

public abstract class AbstractSQLQuery implements SQLQuery{

	@Override
	public QueryExecutor forOnce() {
		QueryExecutor executableQuery = new SimpleQueryExecutor(this);
		return executableQuery;
	}

	@Override
	@Deprecated
	public QueryExecutor forReuse() {
		QueryExecutor executableReusableQuery = new ReusableQueryExecutor(this);
		return executableReusableQuery;
	}
}
