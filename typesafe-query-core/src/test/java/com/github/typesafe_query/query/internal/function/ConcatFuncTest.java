package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class ConcatFuncTest {
	
	@Test
	public void ok_column(){
		IDBTable t = new DBTableImpl("table1");
		ConcatFunc func = new ConcatFunc(new StringDBColumnImpl(t, "name"));
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("CONCAT(table1.num,table1.name)"));
	}
	
	@Test
	public void ok_column_null(){
		IDBTable t = new DBTableImpl("table1");
		ConcatFunc func = new ConcatFunc(null);
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("table1.num"));
	}

	
	@Test
	public void ok_value(){
		IDBTable t = new DBTableImpl("table1");
		ConcatFunc func = new ConcatFunc("name");
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("CONCAT(table1.num,'name')"));
	}
	
	@Test
	public void ok_value_null(){
		IDBTable t = new DBTableImpl("table1");
		ConcatFunc func = new ConcatFunc(null);
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("table1.num"));
	}
}
