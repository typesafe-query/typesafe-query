/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class DBTableImpl implements DBTable {
	
	private String schema;
	
	private String simpleName;
	
	private String alias;
	
	private DBTable wrap;
	
	public DBTableImpl(String simpleName) {
		this.simpleName = simpleName;
	}
	
	public DBTableImpl(String schema,String simpleName) {
		this.schema = schema;
		this.simpleName = simpleName;
	}
	
	public DBTableImpl(String schema,String simpleName,String alias) {
		this.schema = schema;
		this.simpleName = simpleName;
		this.alias = alias;
	}
	
	public DBTableImpl(DBTable table,String alias) {
		this.wrap = table;
		this.schema = table.getSchema();
		this.simpleName = table.getSimpleName();
		this.alias = alias;
	}

	@Override
	public String getSchema() {
		return schema;
	}

	@Override
	public String getSimpleName() {
		return simpleName;
	}

	@Override
	public String getName() {
		if(schema != null){
			return String.format("%s.%s", schema,simpleName);
		}
		return simpleName;
	}
	
	@Override
	public String getAlias() {
		if(alias !=null){
			return alias;
		}
		if(schema != null){
			return String.format("%s_%s", schema,simpleName).toLowerCase();
		}
		return simpleName.toLowerCase();
	}
	
	@Override
	public String getQuery(QueryContext context) {
		if(wrap != null){
			return String.format("%s %s", wrap.getQuery(context),getAlias());
		}
		return String.format("%s %s", getName(),getAlias());
	}
}
