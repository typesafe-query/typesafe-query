package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.meta.IDBColumn;

public class DB2TypesafeQuery extends DefaultTypesafeQuery{

	public DB2TypesafeQuery() {
		super();
	}

	public DB2TypesafeQuery(IDBColumn<?>... columns) {
		super(columns);
	}

	@Override
	protected String createForUpdate() {
		return "FOR UPDATE WITH RS";
	}
}
