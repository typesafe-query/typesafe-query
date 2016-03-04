package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.VirtualDBTable;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;

public class VirtualDBTableImple implements VirtualDBTable{
	
	private String virtualTableName;
	
	private TypesafeQuery alias;
	
	public VirtualDBTableImple(String virtualTableName, TypesafeQuery alias) {
		this.virtualTableName = virtualTableName;
		this.alias = alias;
	}
	
	@Override
	public String getSchema() {
		return null;
	}

	@Override
	public String getSimpleName() {
		return this.virtualTableName;
	}

	@Override
	public String getName() {
		return this.virtualTableName;
	}

	@Override
	public String getAlias() {
		return this.virtualTableName;
	}

	@Override
	public String getSQL(QueryContext context) {
		return String.format("%s %s", getName(),getAlias());
	}
	
	@Override
	public String getWithSQL(QueryContext context) {
		if(this.alias == null){
			throw new NullPointerException();
		}
		return String.format("%s AS (%s)", getName(), alias.getSQL(context));
	}
}
