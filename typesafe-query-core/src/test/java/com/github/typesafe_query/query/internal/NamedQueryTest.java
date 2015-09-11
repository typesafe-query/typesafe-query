package com.github.typesafe_query.query.internal;

import static com.github.typesafe_query.Q.namedQuery;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.typesafe_query.ConnectionHolder;
import com.github.typesafe_query.query.NamedQuery;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.util.SQLUtils;
import com.sample.model.ApUser;

public class NamedQueryTest {

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
	public void named(){
		List<ApUser> result = namedQuery("/NamedQueryTest.named.sql").forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(4));
		assertThat(result.get(0).getUserId(), is("A1"));
		assertNull(result.get(0).getName());
	}
	
	@Test
	public void named_param(){
		QueryExecutor nq = namedQuery("/NamedQueryTest.named_param.sql").forReuse();
		List<ApUser> result = nq.addParam("%1%").getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getUserId(), is("A1"));
		assertNull(result.get(0).getName());

		result = nq.clearParam().addParam("%2%").getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getUserId(), is("A2"));
		assertNull(result.get(0).getName());
		
		nq.close();
	}
	
	@Test
	public void named_param_addWhere(){
		NamedQuery nq = namedQuery("/NamedQueryTest.named._addSQL.sql");
		nq.addQuery("AND AP_USER.UNIT_ID = 'U2'");
		List<ApUser> result = nq.forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.get(0).getUserId(), is("A4"));
		assertNull(result.get(0).getName());

	}
}
