/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


import org.junit.Test;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class InExpTest {
	
	@Test
	public void ok_constructors(){
		DBTable t = new DBTableImpl("table1");
		DBColumn<String> col1 = new StringDBColumnImpl(t,"col1");
		
		new InExp<String>(col1,new String[]{});
		
		try {
			new InExp<String>(null,new String[]{});
			fail();
		} catch (NullPointerException e) {
		}
		try {
			new InExp<String>(col1,(String[])null);
			fail();
		} catch (NullPointerException e) {
		}
		
		new InExp<String>(col1,Q.select());
		try {
			new InExp<String>(null,Q.select());
			fail();
		} catch (NullPointerException e) {
		}
		try {
			new InExp<String>(col1,(TypesafeQuery)null);
			fail();
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void ok_withObject(){
		DBTable t = new DBTableImpl("table1");
		String[] objs = new String[2];
		objs[0] = "piyo1";
		objs[1] = "piyo2";
		Exp exp = new InExp<String>(new StringDBColumnImpl(t,"name"),objs);
		
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, notNullValue());
		assertThat(actual, is("table1.name IN('piyo1','piyo2')"));
	}
	
	@Test
	public void ok_withObject_includeNull(){
		DBTable t = new DBTableImpl("table1");
		String[] objs = new String[2];
		objs[0] = "piyo1";
		objs[1] = null;
		Exp exp = new InExp<String>(new StringDBColumnImpl(t,"name"),objs);
		
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, notNullValue());
		assertThat(actual, is("table1.name IN('piyo1')"));
	}
	
	@Test
	public void ok_ObjectsEmpty(){
		DBTable t = new DBTableImpl("table1");
		String[] objs = new String[0];
		
		Exp exp = new InExp<String>(new StringDBColumnImpl(t,"name"),objs);
		
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, nullValue());
	}
}
