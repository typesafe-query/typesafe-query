/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.internal.DefaultQueryContext;


/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NotInExpTest {
	
	@Test
	public void ok_withObject(){
		IDBTable t = new DBTableImpl("table1");
		String[] objs = new String[2];
		objs[0] = "piyo1";
		objs[1] = "piyo2";
		Exp exp = new NotInExp<String>(new StringDBColumnImpl(t,"name"),objs);
		
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, notNullValue());
		assertThat(actual, is("table1.name NOT IN('piyo1','piyo2')"));
	}
	@Test(expected=NullPointerException.class)
	public void ng_leftNullWithObjects(){
		String[] objs = new String[2];
		objs[0] = "piyo1";
		objs[1] = "piyo2";
		new NotInExp<String>(null, objs);
	}
	@Test(expected=NullPointerException.class)
	public void ng_ObjectsNull(){
		IDBTable t = new DBTableImpl("table1");
		String[] objs = null;
		new NotInExp<String>(new StringDBColumnImpl(t,"name"),objs);
	}
	
	@Test
	public void ok_ObjectsEmpty(){
		IDBTable t = new DBTableImpl("table1");
		String[] objs = new String[0];
		Exp exp = new NotInExp<String>(new StringDBColumnImpl(t,"name"),objs);
		
		QueryContext context = new DefaultQueryContext(t);
		String actual = exp.getSQL(context); 
		
		assertThat(actual, nullValue());
	}
}
