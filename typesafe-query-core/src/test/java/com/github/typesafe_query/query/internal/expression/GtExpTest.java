package com.github.typesafe_query.query.internal.expression;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;

public class GtExpTest {
	@Test
	public void ok_constructors(){
		DBTable t = new DBTableImpl("table1");
		DBColumn<String> left = new StringDBColumnImpl(t, "left");
		DBColumn<String> col1 = new StringDBColumnImpl(t, "col1");
		
		new GtExp<>(left, col1);
		new GtExp<>(left, Q.param());
		new GtExp<>(left, "to");
		new GtExp<>(left, Q.select());
	}
	
	@Test
	public void ok_getSQL(){
		DBTable t = new DBTableImpl("table1");
		DBColumn<String> left = new StringDBColumnImpl(t, "left");
		DBColumn<String> col1 = new StringDBColumnImpl(t, "col1");
		
		String actual = new GtExp<>(left, col1).getSQL("'left'", "'right'");
		assertThat(actual, notNullValue());
		assertThat(actual, is("'left' > 'right'"));
	}
}
