/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import org.junit.Test;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.query.internal.ParamImpl;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class BasicExpTest {
	
	@Test(expected=NullPointerException.class)
	public void ng_column_column_leftnull(){
		DBTable t = new DBTableImpl("table1");
		
		new BasicExp<String>(null, new StringDBColumnImpl(t,"right")) {
			@Override
			protected String getSQL(String l, String r) {
				return null;
			}
		};
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_column_column_rightnull(){
		DBTable t = new DBTableImpl("table1");
		
		DateDBColumn<String> c = null;
		new BasicExp<String>(new StringDBColumnImpl(t,"left"), c) {
			@Override
			protected String getSQL(String l, String r) {
				return null;
			}
		};
	}

	@Test(expected=NullPointerException.class)
	public void ng_column_value_columnnull(){
		new BasicExp<String>(null, "value") {
			@Override
			protected String getSQL(String l, String r) {
				return null;
			}
		};
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_column_param_columnnull(){
		new BasicExp<String>(null, new ParamImpl()) {
			@Override
			protected String getSQL(String l, String r) {
				return null;
			}
		};
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_column_query_columnnull(){
		new BasicExp<String>(null, Q.select()) {
			@Override
			protected String getSQL(String l, String r) {
				return null;
			}
		};
	}
}
