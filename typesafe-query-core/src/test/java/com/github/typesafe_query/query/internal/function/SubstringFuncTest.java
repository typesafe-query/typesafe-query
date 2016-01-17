package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

public class SubstringFuncTest {
	
	@Test
	public void ok_column_from(){
		DBTable t = new DBTableImpl("table1");
		SubstringFunc func = new SubstringFunc(new NumberDBColumnImpl<>(t, "frm"));
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.name");
		
		assertThat(actual, is("SUBSTR(table1.name,table1.frm)"));
	}
	
	@Test
	public void ok_column_from_to(){
		DBTable t = new DBTableImpl("table1");
		SubstringFunc func = new SubstringFunc(new NumberDBColumnImpl<>(t, "frm"),new NumberDBColumnImpl<>(t, "t"));
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.name");
		
		assertThat(actual, is("SUBSTR(table1.name,table1.frm,table1.t)"));
	}
	
	@Test
	public void ok_column_null(){
		DBTable t = new DBTableImpl("table1");
		NumberDBColumn<Integer> c = null;
		SubstringFunc func = new SubstringFunc(c);
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("table1.num"));
	}

	
	@Test
	public void ok_value_from(){
		DBTable t = new DBTableImpl("table1");
		SubstringFunc func = new SubstringFunc(0);
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("SUBSTR(table1.num,0)"));
	}
	@Test
	public void ok_value_from_to(){
		DBTable t = new DBTableImpl("table1");
		SubstringFunc func = new SubstringFunc(0,5);
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("SUBSTR(table1.num,0,5)"));
	}
	
	@Test
	public void ok_value_null(){
		DBTable t = new DBTableImpl("table1");
		Integer i = null;
		SubstringFunc func = new SubstringFunc(i);
		String actual = func.getSQL(new DefaultQueryContext(t), "table1.num");
		
		assertThat(actual, is("table1.num"));
	}
}
