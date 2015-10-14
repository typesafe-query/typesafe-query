package com.github.typesafe_query.jdbc.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.typesafe_query.ConnectionHolder;
import com.github.typesafe_query.Q;
import com.github.typesafe_query.util.SQLUtils;
import com.sample.model.ApUser_;

public class MapResultMapperTest {

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
	public void ok(){
		List<Map<String, Object>> list = Q.select().from(ApUser_.TABLE).forOnce().getResultList(new MapResultMapper());
		
	}
}
