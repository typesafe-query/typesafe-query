/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;


import org.junit.Test;

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
public class OrExpTest {
	
	@Test
	public void ok(){
		IDBTable t = new DBTableImpl("table1");
		Exp[] exps = new Exp[2];
		exps[0] = new LtExp<Integer>(new NumberDBColumnImpl<Integer>(t, "age"), 245);
		exps[1] = new EqExp<String>(new StringDBColumnImpl(t, "name"), "piyo");
		Exp exp = new OrExp(exps);
		
		QueryContext context = new DefaultQueryContext(t);
		String predicate = exp.getSQL(context); 
		
		assertThat(predicate, notNullValue());
		assertThat(predicate,is("(table1.age < 245 OR table1.name = 'piyo')"));
	}
	
	@Test
	public void ok_andInAnd(){
		IDBTable t = new DBTableImpl("table1");
		
		Exp[] inner = new Exp[3];
		inner[0] = new LtExp<Integer>(new NumberDBColumnImpl<Integer>(t, "age"), 245);
		inner[1] = new EqExp<String>(new StringDBColumnImpl(t, "name1"), "piyo");
		inner[2] = new LikeExp(new StringDBColumnImpl(t, "name2"), "%piyo");

		Exp[] exps = new Exp[2];
		exps[0] = new LtExp<Integer>(new NumberDBColumnImpl<Integer>(t, "age"), 12);
		exps[1] = new OrExp(inner);
		
		Exp exp = new OrExp(exps);
		
		QueryContext context = new DefaultQueryContext(t);
		String predicate = exp.getSQL(context); 
		
		assertThat(predicate, notNullValue());
		assertThat(predicate,is("(table1.age < 12 OR (table1.age < 245 OR table1.name1 = 'piyo' OR table1.name2 LIKE '%piyo' ESCAPE '!'))"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_expsNull(){
		Exp[] exps = null;
		new OrExp(exps);
	}
	
	@Test
	public void ok_exps_0(){
		IDBTable t = new DBTableImpl("table1");
		Exp[] exps = new Exp[0];
		
		Exp exp = new OrExp(exps);
		
		QueryContext context = new DefaultQueryContext(t);
		String predicate = exp.getSQL(context);
		
		assertNull(predicate);
	}
	@Test
	public void ok_exps_1(){
		IDBTable t = new DBTableImpl("table1");
		Exp[] exps = new Exp[1];
		exps[0] = new LtExp<Long>(new NumberDBColumnImpl<Long>(t, "age"), 245L);
		
		Exp exp = new OrExp(exps);
		
		QueryContext context = new DefaultQueryContext(t);
		String predicate = exp.getSQL(context);
		
		assertThat(predicate, notNullValue());
		assertThat(predicate,is("table1.age < 245"));
	}
}
