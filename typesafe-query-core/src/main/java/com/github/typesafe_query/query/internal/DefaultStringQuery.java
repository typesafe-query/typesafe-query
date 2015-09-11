package com.github.typesafe_query.query.internal;

import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.StringQuery;

public class DefaultStringQuery extends AbstractSQLQuery implements StringQuery{
	
	private List<String> queries;
	
	public DefaultStringQuery(String sql) {
		this();
		if(sql == null){
			throw new NullPointerException();
		}
		queries.add(sql);
	}
	
	public DefaultStringQuery() {
		queries = new ArrayList<String>();
	}

	@Override
	public String getSQL(QueryContext context) {
		return QueryUtils.joinWith(" ", queries);
	}

	@Override
	public void addQuery(String query) {
		if(query == null){
			throw new NullPointerException();
		}
		queries.add(query);
	}
}
