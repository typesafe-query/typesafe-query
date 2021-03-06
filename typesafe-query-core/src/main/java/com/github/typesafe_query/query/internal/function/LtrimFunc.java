package com.github.typesafe_query.query.internal.function;

import java.util.Objects;

import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

/**
 * @author Shohei Nozaki(MOSA architect Inc.)
 *
 */
public class LtrimFunc implements Func {

	@Override
	public String getSQL(QueryContext context,
			String expression) {
		return String.format("LTRIM(%s)", Objects.requireNonNull(expression));
	}
}
