package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class MaxFuncTest {
	
	@Test
	public void ok(){
		MaxFunc func = new MaxFunc();
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("MAX(table1.num)"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		MaxFunc func = new MaxFunc();
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
