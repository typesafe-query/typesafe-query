package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class AllFuncTest {

	@Test
	public void ok(){
		AllFunc func = new AllFunc();
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("ALL table1.num"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		AllFunc func = new AllFunc();
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
