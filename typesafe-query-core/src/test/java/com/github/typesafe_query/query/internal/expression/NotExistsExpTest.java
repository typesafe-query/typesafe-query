package com.github.typesafe_query.query.internal.expression;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.github.typesafe_query.Q;

public class NotExistsExpTest {
	@Test
	public void ok_constructors(){
		new NotExistsExp(Q.select());
	}
	
	@Test
	public void ok_getSQL(){
		String actual = new NotExistsExp(Q.select()).getSQL("select 1 from table2");
		assertThat(actual, notNullValue());
		assertThat(actual, is("NOT EXISTS(select 1 from table2)"));
	}
}
