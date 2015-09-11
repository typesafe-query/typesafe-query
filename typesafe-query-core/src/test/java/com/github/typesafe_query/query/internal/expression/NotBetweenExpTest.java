/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


import org.junit.Test;

import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.internal.DefaultQueryContext;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NotBetweenExpTest {
	
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
	
	@Test(expected=NullPointerException.class)
	public void ng_leftNull(){
		new NotBetweenExp<String>(null, "fromPiyo", "toPiyo");
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_fromColumnNull(){
		IDBTable t = new DBTableImpl("table1");
		IDBColumn<Long> col = null;
		new NotBetweenExp<Long>(new NumberDBColumnImpl<Long>(t,"hoge"), col, 20000L);
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
	
	@Test(expected=NullPointerException.class)
	public void ng_toColumnNull(){
		IDBTable t = new DBTableImpl("table1");
		IDBColumn<Long> col = null;
		new NotBetweenExp<Long>(new NumberDBColumnImpl<Long>(t,"hoge"), 5L, col);
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
