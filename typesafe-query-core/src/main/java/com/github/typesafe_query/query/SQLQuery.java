package com.github.typesafe_query.query;

public interface SQLQuery {
	String getSQL(QueryContext context);
	QueryExecutor forOnce();
	@Deprecated
	QueryExecutor forReuse();
}
