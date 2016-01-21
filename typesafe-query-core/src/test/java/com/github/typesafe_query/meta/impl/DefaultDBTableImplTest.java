package com.github.typesafe_query.meta.impl;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.After;
import org.junit.Before;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

import com.github.typesafe_query.ConnectionHolder;
import com.github.typesafe_query.util.SQLUtils;
import com.sample.model.ApUser2_;

public class DefaultDBTableImplTest {

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
	public void schema(){
		int size = ApUser2_.find().list().size();
		assertThat(size, is(1));
	}
}
