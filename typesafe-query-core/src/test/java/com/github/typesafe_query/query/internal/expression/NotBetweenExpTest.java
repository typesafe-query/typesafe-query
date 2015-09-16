/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


import org.junit.Test;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NotBetweenExpTest {
	@Test
	public void ok_constructors(){
		IDBTable t = new DBTableImpl("table1");
		IDBColumn<String> left = new StringDBColumnImpl(t, "left");
		IDBColumn<String> from = new StringDBColumnImpl(t, "col1");
		IDBColumn<String> to = new StringDBColumnImpl(t, "col2");
		
		new NotBetweenExp<>(left, from, to);
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, from, to);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			new NotBetweenExp<>(left, (IDBColumn<String>)null, to);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			new NotBetweenExp<>(left, from, (IDBColumn<String>)null);
			fail();
		} catch (NullPointerException e) {
		}

		
		new NotBetweenExp<>(left, from, Q.param());
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, from, (Param)null);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			new NotBetweenExp<>(left, from, (Param)null);
			fail();
		} catch (NullPointerException e) {
		}

		
		new NotBetweenExp<>(left, from, "to");
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, (IDBColumn<String>)null, "to");
			fail();
		} catch (NullPointerException e) {
		}

		new NotBetweenExp<>(left, Q.param(), to);
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, Q.param(), (IDBColumn<String>)null);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			new NotBetweenExp<>(left, Q.param(), (IDBColumn<String>)null);
			fail();
		} catch (NullPointerException e) {
		}

		
		new NotBetweenExp<>(left, Q.param(), Q.param());
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, Q.param(), (Param)null);
			fail();
		} catch (NullPointerException e) {
		}
		try {
			new NotBetweenExp<>(left, Q.param(), (Param)null);
			fail();
		} catch (NullPointerException e) {
		}

		
		new NotBetweenExp<>(left, Q.param(), "to");
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, Q.param(), "to");
			fail();
		} catch (NullPointerException e) {
		}

		
		new NotBetweenExp<>(left, "from", to);
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, "from", (IDBColumn<String>)null);
			fail();
		} catch (NullPointerException e) {
		}

		
		new NotBetweenExp<>(left, "from", Q.param());
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, "from", Q.param());
			fail();
		} catch (NullPointerException e) {
		}
		
		new NotBetweenExp<>(left, "from", "to");
		try {
			new NotBetweenExp<>((IDBColumn<String>)null, "from", "to");
			fail();
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void ok_withColumnColumn(){
		IDBTable t = new DBTableImpl("table1");
		Exp exp = new NotBetweenExp<String>(new StringDBColumnImpl(t,"name1"), new StringDBColumnImpl(t,"name2"), new StringDBColumnImpl(t,"name3"));
		
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, notNullValue());
		assertThat(actual, is("table1.name1 NOT BETWEEN table1.name2 AND table1.name3"));
	}
	
	@Test
	public void ok_withColumnObject(){
		IDBTable t = new DBTableImpl("table1");
		Exp exp = new NotBetweenExp<String>(new StringDBColumnImpl(t,"name1"), new StringDBColumnImpl(t,"name2"), "to");

		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, notNullValue());
		assertThat(actual, is("table1.name1 NOT BETWEEN table1.name2 AND 'to'"));
	}
	
	@Test
	public void ok_withObjectColumn(){
		IDBTable t = new DBTableImpl("table1");
		Exp exp = new NotBetweenExp<String>(new StringDBColumnImpl(t,"name1"), "fromPiyo", new StringDBColumnImpl(t,"name2"));
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, notNullValue());
		assertThat(actual, is("table1.name1 NOT BETWEEN 'fromPiyo' AND table1.name2"));
	}
	
	@Test
	public void ok_withObjectObject(){
		IDBTable t = new DBTableImpl("table1");
		Exp exp = new NotBetweenExp<Integer>(new NumberDBColumnImpl<Integer>(t,"age"), 18, 30);
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, notNullValue());
		assertThat(actual, is("table1.age NOT BETWEEN 18 AND 30"));

	}
	
	
	
	@Test
	public void ok_fromObjectNull(){
		IDBTable t = new DBTableImpl("table1");
		String s = null;
		
		Exp exp = new NotBetweenExp<String>(new StringDBColumnImpl(t,"name"), s, new StringDBColumnImpl(t,"name"));
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, nullValue());
	}
	
	@Test
	public void ok_toObjectNull(){
		IDBTable t = new DBTableImpl("table1");
		String s = null;
		
		Exp exp = new NotBetweenExp<String>(new StringDBColumnImpl(t,"name"), new StringDBColumnImpl(t,"name"),s);
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, nullValue());
	}
}
