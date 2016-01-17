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
	
	private String name;
	
	private String alias;
	
	private DBTable wrap;
	
	public DBTableImpl(String name) {
		this.name = name;
	}
	
	public DBTableImpl(String name,String alias) {
		this.name = name;
		this.alias = alias;
	}
	
	public DBTableImpl(DBTable table,String alias) {
		this.wrap = table;
		this.name = table.getName();
		this.alias = alias;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getAlias() {
		return alias !=null?alias:getName().toLowerCase();
	}
	
	@Override
	public String getQuery(QueryContext context) {
		if(wrap != null){
			return String.format("%s %s", wrap.getQuery(context),getAlias());
		}
		return String.format("%s %s", getName(),getAlias());
	}

}
