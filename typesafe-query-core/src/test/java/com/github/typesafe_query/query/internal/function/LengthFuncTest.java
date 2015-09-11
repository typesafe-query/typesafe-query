package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class LengthFuncTest {
	
	@Test
	public void ok(){
		LengthFunc func = new LengthFunc();
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("LENGTH(table1.num)"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		LengthFunc func = new LengthFunc();
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
