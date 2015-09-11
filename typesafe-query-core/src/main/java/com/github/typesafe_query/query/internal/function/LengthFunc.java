/**
 * 
 */
package com.github.typesafe_query.query.internal.function;

import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class LengthFunc implements Func {

	@Override
	public String getSQL(QueryContext context,
			String expression) {
		return String.format("LENGTH(%s)", expression);
	}
}
