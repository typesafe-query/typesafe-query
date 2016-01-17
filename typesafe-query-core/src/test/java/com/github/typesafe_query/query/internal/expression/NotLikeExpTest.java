package com.github.typesafe_query.query.internal.expression;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;

public class NotLikeExpTest {
	@Test
	public void ok_constructors(){
		DBTable t = new DBTableImpl("table1");
		DBColumn<String> left = new StringDBColumnImpl(t, "left");
		
		new NotLikeExp(left, Q.param());
		new NotLikeExp(left, "to");
		new NotLikeExp(left, Q.select());
	}
	
	@Test
	public void ok_getSQL(){
		DBTable t = new DBTableImpl("table1");
		DBColumn<String> left = new StringDBColumnImpl(t, "left");
		
		String actual = new NotLikeExp(left, "LIKE").getSQL("'left'", "'right%'");
		assertThat(actual, notNullValue());
		assertThat(actual, is("'left' NOT LIKE 'right%' ESCAPE '!'"));
	}
}
