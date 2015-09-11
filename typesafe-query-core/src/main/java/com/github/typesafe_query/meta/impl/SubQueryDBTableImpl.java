package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;


public class SubQueryDBTableImpl extends DBTableImpl{
	
	private TypesafeQuery query;
	
	public SubQueryDBTableImpl(TypesafeQuery query,IDBTable table) {
		super(table.getName());
		this.query = query;
	}

	@Override
	public String getQuery(QueryContext context) {
		return String.format("(%s)", query.getSQL(context).trim());
	}
}
