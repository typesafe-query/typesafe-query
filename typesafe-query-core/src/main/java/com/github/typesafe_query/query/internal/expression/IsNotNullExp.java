/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class IsNotNullExp<T extends Comparable<? super T>> implements Exp {
	
	private DBColumn<T> c;
	
	public IsNotNullExp(DBColumn<T> c) {
		if(c == null){
			throw new NullPointerException();
		}
		this.c = c;
	}
	
	@Override
	public String getSQL(QueryContext context) {
		return String.format("%s IS NOT NULL", context.getColumnPath(c));
	}
}
