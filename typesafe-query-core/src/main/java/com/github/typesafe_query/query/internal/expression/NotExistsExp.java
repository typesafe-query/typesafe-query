/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.query.TypesafeQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NotExistsExp extends ExistsExp {

	public NotExistsExp(TypesafeQuery subQuery) {
		super(subQuery);
	}

	@Override
	protected String getPredicate(String subquery) {
		return String.format("NOT EXISTS(%s)",subquery);
	}
}
