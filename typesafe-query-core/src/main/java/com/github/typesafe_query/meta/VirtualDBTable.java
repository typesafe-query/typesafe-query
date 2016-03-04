package com.github.typesafe_query.meta;

import com.github.typesafe_query.query.QueryContext;

public interface VirtualDBTable extends DBTable {
	String getName();
	String getWithSQL(QueryContext context);
}
