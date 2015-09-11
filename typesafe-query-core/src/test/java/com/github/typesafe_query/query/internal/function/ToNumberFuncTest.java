package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class ToNumberFuncTest {
	
	@Test
	public void ok(){
		ToNumberFunc func = new ToNumberFunc();
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("TO_NUMBER(table1.num)"));
	}
	
	@Test
	public void ok_format(){
		ToNumberFunc func = new ToNumberFunc("9,999");
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("TO_NUMBER(table1.num,'9,999')"));
	}
	
	@Test
	public void ok_format_null(){
		ToNumberFunc func = new ToNumberFunc(null);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("TO_NUMBER(table1.num)"));
	}


	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		ToNumberFunc func = new ToNumberFunc();
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
	
}
