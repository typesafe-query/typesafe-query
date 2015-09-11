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
public class InExpTest {
	
	
	@Test
	public void ok_withObject(){
		IDBTable t = new DBTableImpl("table1");
		String[] objs = new String[2];
		objs[0] = "piyo1";
		objs[1] = "piyo2";
		Exp exp = new InExp<String>(new StringDBColumnImpl(t,"name"),objs);
		
		QueryContext context = new DefaultQueryContext(t);
		String predicate = exp.getSQL(context); 
		
		assertThat(predicate, notNullValue());
		assertThat(predicate, is("table1.name IN('piyo1','piyo2')"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_leftNullWithObjects(){
		String[] objs = new String[2];
		objs[0] = "piyo1";
		objs[1] = "piyo2";
		new InExp<String>(null, objs);
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_ObjectsNull(){
		IDBTable t = new DBTableImpl("table1");
		String[] objs = null;
		new InExp<String>(new StringDBColumnImpl(t,"name"),objs);
	}
	
	@Test
	public void ok_ObjectsEmpty(){
		IDBTable t = new DBTableImpl("table1");
		String[] objs = new String[0];
		
		Exp exp = new InExp<String>(new StringDBColumnImpl(t,"name"),objs);
		
		QueryContext context = new DefaultQueryContext(t);
		String predicate = exp.getSQL(context); 
		
		assertThat(predicate, nullValue());
	}
}
