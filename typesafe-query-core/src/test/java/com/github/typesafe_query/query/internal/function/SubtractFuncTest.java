package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.github.typesafe_query.enums.IntervalUnit;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class SubtractFuncTest {
	
	@Test
	public void ok_num_value(){
		SubtractFunc func = new SubtractFunc(1);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num - 1"));
	}
	
	@Test
	public void ok_num_column(){
		DBTable t = new DBTableImpl("table1");
		SubtractFunc func = new SubtractFunc(new NumberDBColumnImpl<Long>(t, "num2"));
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num - table1.num2"));
	}
	
	@Test
	public void ok_date_value(){
		SubtractFunc func = new SubtractFunc(1, IntervalUnit.DAY);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.date");
		
		assertThat(actual, is("table1.date - INTERVAL 1 DAY"));
	}
	
	@Test
	public void ok_date_column(){
		DBTable t = new DBTableImpl("table1");
		SubtractFunc func = new SubtractFunc(new NumberDBColumnImpl<Long>(t, "num"), IntervalUnit.DAY);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.date");
		
		assertThat(actual, is("table1.date - INTERVAL table1.num DAY"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		Integer i = null;
		SubtractFunc func = new SubtractFunc(i);
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
