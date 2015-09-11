package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class MinFuncTest {
	
	@Test
	public void ok(){
		MinFunc func = new MinFunc();
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("MIN(table1.num)"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		MinFunc func = new MinFunc();
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
