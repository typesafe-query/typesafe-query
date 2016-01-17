/**
 * 
 */
package com.github.typesafe_query.query.internal;


import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.Order;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class OrderImpl implements Order{
	private DBColumn<?> c;
	private Type type;
	
	public OrderImpl(DBColumn<?> c,Type type) {
		if(c == null){
			throw new InvalidQueryException("join target is null.");
		}
		
		if(type == null){
			type = Type.ASC;
		}
		
		this.c = c;
		this.type = type;
	}

	@Override
	public String getOrder(QueryContext context) {
		if(type == Type.DESC){
			return context.getColumnPath(c) + " DESC";
		}
		return context.getColumnPath(c) + " ASC";
	}
}
