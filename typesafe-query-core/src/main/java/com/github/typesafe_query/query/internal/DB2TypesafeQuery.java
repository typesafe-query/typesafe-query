package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.meta.DBColumn;

public class DB2TypesafeQuery extends DefaultTypesafeQuery{

	public DB2TypesafeQuery() {
		super();
	}

	public DB2TypesafeQuery(DBColumn<?>... columns) {
		super(columns);
	}

	@Override
	protected String createForUpdate() {
		return "FOR UPDATE WITH RS";
	}
}
