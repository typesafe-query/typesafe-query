package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class MultiplyFuncTest {
	
	@Test
	public void ok_value(){
		MultiplyFunc func = new MultiplyFunc(1);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num * 1"));
	}
	
	@Test
	public void ok_column(){
		DBTable t = new DBTableImpl("table1");
		MultiplyFunc func = new MultiplyFunc(new NumberDBColumnImpl<Long>(t, "num2"));
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num * table1.num2"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		Integer i = null;
		MultiplyFunc func = new MultiplyFunc(i);
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
