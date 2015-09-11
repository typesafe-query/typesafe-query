package com.github.typesafe_query.query;

import com.github.typesafe_query.Beta;

public interface SQLQuery {
	String getSQL(QueryContext context);
	QueryExecutor forOnce();
	QueryExecutor forReuse();
	@Beta
	BatchQueryExecutor forBatch();
}
