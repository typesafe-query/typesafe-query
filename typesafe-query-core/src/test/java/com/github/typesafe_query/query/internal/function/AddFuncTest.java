package com.github.typesafe_query.query.internal.function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.typesafe_query.ConnectionHolder;
import com.github.typesafe_query.enums.IntervalUnit;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.query.internal.DefaultQueryContext;
import com.github.typesafe_query.util.SQLUtils;

public class AddFuncTest {
	private Connection con;

	@Before
	public void before()throws Exception{
		con = DriverManager.getConnection("jdbc:h2:mem:db1","sa","");
		con.setAutoCommit(false);
		ConnectionHolder.getInstance().set(con);
		
		SQLUtils.executeResource(con, "/create-tables.sql");
		con.commit();
	}
	
	@After
	public void after()throws Exception{
		ConnectionHolder.getInstance().set(null);
		con.close();
	}
	
	@Test
	public void ok_num_value(){
		AddFunc func = new AddFunc(1);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num + 1"));
	}
	
	@Test
	public void ok_num_column(){
		DBTable t = new DBTableImpl("table1");
		AddFunc func = new AddFunc(new NumberDBColumnImpl<Long>(t, "num2"));
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.num");
		
		assertThat(actual, is("table1.num + table1.num2"));
	}
	
	@Test
	public void ok_date_value(){
		AddFunc func = new AddFunc(1, IntervalUnit.DAY);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.date");
		
		assertThat(actual, is("table1.date + INTERVAL 1 DAY"));
	}
	
	@Test
	public void ok_date_column(){
		DBTable t = new DBTableImpl("table1");
		AddFunc func = new AddFunc(new NumberDBColumnImpl<Long>(t, "num"), IntervalUnit.DAY);
		String actual = func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), "table1.date");
		
		assertThat(actual, is("table1.date + INTERVAL table1.num DAY"));
	}
	
	@Test(expected=NullPointerException.class)
	public void ng_null(){
		Integer i = null;
		AddFunc func = new AddFunc(i);
		func.getSQL(new DefaultQueryContext(new DBTableImpl("table1")), null);
	}
}
