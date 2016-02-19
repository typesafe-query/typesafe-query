package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class SubtractFuncTest {
	
	@Test
	public void ok_value(){
		SubtractFunc func = new SubtractFunc(1);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num - 1"));
	}
	
	@Test
	public void ok_column(){
		DBTable t = new DBTableImpl("table1");
		SubtractFunc func = new SubtractFunc(new NumberDBColumnImpl<Long>(t, "num2"));
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num - table1.num2"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		SubtractFunc func = new SubtractFunc(null);
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
