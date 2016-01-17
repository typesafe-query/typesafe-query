package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class CountFuncTest {

	@Test
	public void ok() {
		CountFunc func = new CountFunc();
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");

		assertThat(actual, is("COUNT(table1.num)"));
	}

	@Test(expected = NullPointerException.class)
	public void ng_null() {
		CountFunc func = new CountFunc();
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}