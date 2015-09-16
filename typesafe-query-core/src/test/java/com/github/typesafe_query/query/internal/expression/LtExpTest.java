package com.github.typesafe_query.query.internal.expression;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;

public class LtExpTest {
	@Test
	public void ok_constructors(){
		IDBTable t = new DBTableImpl("table1");
		IDBColumn<String> left = new StringDBColumnImpl(t, "left");
		IDBColumn<String> col1 = new StringDBColumnImpl(t, "col1");
		
		new LtExp<>(left, col1);
		new LtExp<>(left, Q.param());
		new LtExp<>(left, "to");
		new LtExp<>(left, Q.select());
	}
	
	@Test
	public void ok_getSQL(){
		IDBTable t = new DBTableImpl("table1");
		IDBColumn<String> left = new StringDBColumnImpl(t, "left");
		IDBColumn<String> col1 = new StringDBColumnImpl(t, "col1");
		
		String actual = new LtExp<>(left, col1).getSQL("'left'", "'right'");
		assertThat(actual, notNullValue());
		assertThat(actual, is("'left' < 'right'"));
	}
}
