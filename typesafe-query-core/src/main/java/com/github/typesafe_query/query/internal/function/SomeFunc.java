package com.github.typesafe_query.query.internal.function;

import java.util.Objects;

import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.QueryContext;

public class SomeFunc implements Func {

	@Override
	public String getSQL(QueryContext context,
			String expression) {
		return String.format("SOME %s", Objects.requireNonNull(expression));
	}
}
