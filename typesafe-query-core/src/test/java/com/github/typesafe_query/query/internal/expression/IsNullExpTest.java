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
public class IsNullExpTest {
	
	@Test
	public void ok_withColumn(){
		IDBTable t = new DBTableImpl("table1");
		Exp exp = new IsNullExp<String>(new StringDBColumnImpl(t,"name"));
		
		QueryContext context = new DefaultQueryContext(t);
		String predicate = exp.getSQL(context); 
		
		assertThat(predicate, notNullValue());
		assertThat(predicate, is("table1.name IS NULL"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_leftNull(){
		new IsNullExp<String>(null);
	}
}
