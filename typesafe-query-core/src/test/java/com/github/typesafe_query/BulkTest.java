package com.github.typesafe_query;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.util.SQLUtils;
import com.sample.model.ApUser2_;
import com.sample.model.ApUser_;
import com.sample.model.Role_;
import com.sample.model.Unit_;

public class BulkTest {
	
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
	public void insert(){
		int count = ApUser2_.bulk().insert(
				Q.select(
				ApUser_.USER_ID,
				ApUser_.NAME,
				ApUser_.LOCK_FLG,
				ApUser_.VALID_FROM,
				ApUser_.VALID_TO,
				ApUser_.UNIT_ID,
				ApUser_.ROLE_ID)
				.from(ApUser_.TABLE)
				.where(ApUser_.USER_ID.neq("A1")));
		
		assertThat(count, is(3));
		
		int count2 = Unit_.bulk().insert(
				new Into(Unit_.UNIT_ID, Unit_.NAME),
				Q.select(Role_.ROLE_ID, Role_.NAME).from(Role_.TABLE));
		
		assertThat(count2, is(2));
		
		try {
			ApUser_.bulk().insert(null);
			fail();
		} catch (NullPointerException e) {
			
		}

		try {
			ApUser_.bulk().insert(Q.into(ApUser_.USER_ID), null);
			fail();
		} catch (NullPointerException e) {
			
		}
	}
	
	@Test
	public void update(){
		int count = ApUser_.bulk().update(Q.set(
			ApUser_.LOCK_FLG.eq("0")
		));
		
		assertThat(count, is(4));
		
		try {
			ApUser_.bulk().update(null);
			fail();
		} catch (NullPointerException e) {
			
		}
		
		try {
			ApUser_.bulk().update(new HashSet<Exp>());
			fail();
		} catch (InvalidQueryException e) {
			
		}
	}
	
	@Test
	public void updateWhere(){
		int count = ApUser_.bulk().updateWhere(Q.set(
			ApUser_.LOCK_FLG.eq("0")
		),ApUser_.UNIT_ID.eq("U1"),ApUser_.ROLE_ID.eq("R1"));
		
		assertThat(count, is(3));

		count = ApUser_.bulk().updateWhere(Q.set(
			ApUser_.LOCK_FLG.eq("0")
		),ApUser_.UNIT_ID.eq("U2"),ApUser_.ROLE_ID.eq("R2"));
			
		assertThat(count, is(1));

		count = ApUser_.bulk().updateWhere(Q.set(
			ApUser_.LOCK_FLG.eq("0")
		),new Exp[]{});
		
		assertThat(count, is(4));

		try {
			ApUser_.bulk().updateWhere(null,ApUser_.UNIT_ID.eq("U1"));
			fail();
		} catch (NullPointerException e) {
			
		}
		
		try {
			ApUser_.bulk().updateWhere(new HashSet<Exp>(),ApUser_.UNIT_ID.eq("U1"));
			fail();
		} catch (InvalidQueryException e) {
			
		}
	}
	
	@Test
	public void delete(){
		int count = ApUser_.bulk().delete();
		assertThat(count, is(4));
	}

	@Test
	public void deleteWhere(){
		int count = ApUser_.bulk().deleteWhere(ApUser_.UNIT_ID.eq("U1"));
		assertThat(count, is(3));
		
		count = ApUser_.bulk().deleteWhere(ApUser_.UNIT_ID.eq("U2"),ApUser_.ROLE_ID.eq("R2"));
		assertThat(count, is(1));

		count = ApUser_.bulk().deleteWhere(new Exp[]{});
		assertThat(count, is(0));
	}

}
