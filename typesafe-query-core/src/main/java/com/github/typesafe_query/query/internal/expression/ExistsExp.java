/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class ExistsExp implements Exp {
	private TypesafeQuery subQuery;
	public ExistsExp(TypesafeQuery subQuery) {
		this.subQuery = subQuery;
	}
	
	@Override
	public String getSQL(QueryContext context) {
		return getSQL(subQuery.getSQL(context));
	}
	
	protected String getSQL(String subquery){
		return String.format("EXISTS(%s)",subquery);
	}
}
