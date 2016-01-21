package com.github.typesafe_query;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.typesafe_query.query.QueryException;
import com.github.typesafe_query.util.SQLUtils;
import com.sample.model.ApUser;
import com.sample.model.ApUser_;

public class ModelHandlerTest {
	
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
	public void create(){
		ApUser user = new ApUser();
		user.setUserId("A5");
		user.setName("ゆーざー５");
		user.setLockFlg("0");
		user.setValidFrom(Optional.of(Date.valueOf("2015-01-01")));
		user.setUnitId("U2");
		user.setRoleId("R2");
		
		ApUser_.model().create(user);
		
		Optional<ApUser> created = ApUser_.find().byId("A5");
		assertTrue(created.isPresent());
		assertThat(created.get().getUserId(), is("A5"));
		assertThat(created.get().getName(), is("ゆーざー５"));
		assertThat(created.get().getLockFlg(), is("0"));
		assertThat(created.get().getValidFrom().get().getTime(), is(Date.valueOf("2015-01-01").getTime()));
		assertFalse(created.get().getValidTo().isPresent());
		assertThat(created.get().getUnitId(), is("U2"));
		assertThat(created.get().getRoleId(), is("R2"));
		
		//キー重複
		boolean result = ApUser_.model().create(user);
		assertFalse(result);
		
		//NOT NULL項目
		result = ApUser_.model().create(new ApUser());
		
		try {
			ApUser_.model().create(null);
			fail();
		} catch (NullPointerException e) {
		}
	}
	
	@Test
	public void update(){
		Optional<ApUser> user = ApUser_.find().byId("A1");
		ApUser u = user.get();
		u.setName("更新しました");
		
		ApUser_.model().save(u);
		
		Optional<ApUser> updated = ApUser_.find().byId("A1");
		assertThat(updated.get().getUserId(), is("A1"));
		assertThat(updated.get().getName(), is("更新しました"));
		assertThat(updated.get().getLockFlg(), is("1"));
		assertThat(updated.get().getValidFrom().get().getTime(), is(Date.valueOf("2015-01-10").getTime()));
		assertFalse(updated.get().getValidTo().isPresent());
		assertThat(updated.get().getUnitId(), is("U1"));
		assertThat(updated.get().getRoleId(), is("R1"));
		
		
		//キー無し
		u.setUserId(null);
		try {
			ApUser_.model().save(u);
			fail();
		} catch (QueryException e) {
		}
		
		//NOT NULL項目
		ApUser ap = new ApUser();
		ap.setUserId("AA");
		boolean result = ApUser_.model().save(ap);
		assertFalse(result);
		
		try {
			ApUser_.model().save(null);
			fail();
		} catch (NullPointerException e) {
		}
		
	}
	
	@Test
	public void delete(){
		Optional<ApUser> user = ApUser_.find().byId("A2");
		ApUser u = user.get();
		
		ApUser_.model().delete(u);
		
		Optional<ApUser> deleted = ApUser_.find().byId("A2");
		assertThat(deleted, is(Optional.<ApUser>empty()));
		
		//キー無し
		u.setUserId(null);
		try {
			ApUser_.model().delete(u);
			fail();
		} catch (QueryException e) {
		}
		
		try {
			ApUser_.model().delete(null);
			fail();
		} catch (NullPointerException e) {
		}
		
	}
}
