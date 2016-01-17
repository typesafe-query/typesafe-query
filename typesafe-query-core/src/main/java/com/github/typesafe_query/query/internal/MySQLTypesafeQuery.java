package com.github.typesafe_query.query.internal;

import com.github.typesafe_query.meta.DBColumn;

public class MySQLTypesafeQuery extends DefaultTypesafeQuery{

	public MySQLTypesafeQuery() {
		super();
	}

	public MySQLTypesafeQuery(DBColumn<?>... columns) {
		super(columns);
	}

	@Override
	protected StringBuilder createLimitOffset(StringBuilder sb,Integer limit,Integer offset) {
		if(limit != null){
			sb.append("LIMIT " + limit + " ");
		}
		
		if(offset != null){
			sb.append("OFFSET " + offset + " ");
		}
		return sb;
	}
}
