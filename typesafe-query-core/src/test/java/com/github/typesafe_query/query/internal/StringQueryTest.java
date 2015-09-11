package com.github.typesafe_query.query.internal;

import static com.github.typesafe_query.Q.stringQuery;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.typesafe_query.ConnectionHolder;
import com.github.typesafe_query.query.BatchQueryExecutor;
import com.github.typesafe_query.query.QueryExecutor;
import com.github.typesafe_query.query.StringQuery;
import com.github.typesafe_query.util.SQLUtils;
import com.sample.model.ApUser;
import com.sample.model.TypeModel;

public class StringQueryTest {
	
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
	public void sql(){
		List<ApUser> result = stringQuery("select * from ap_user where user_id = 'A1'").forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getUserId(), is("A1"));
	}
	
	@Test
	public void sql_param(){
		try(QueryExecutor q = stringQuery("select * from ap_user where user_id = ?").forReuse()){
			List<ApUser> result = q.addParam("A1").getResultList(ApUser.class);
			
			assertNotNull(result);
			assertThat(result.size(), is(1));
			assertThat(result.get(0).getUserId(), is("A1"));
			
			result = q.clearParam().addParam("A2").getResultList(ApUser.class);
			assertNotNull(result);
			assertThat(result.size(), is(1));
			assertThat(result.get(0).getUserId(), is("A2"));
		}
	}
	
	@Test
	public void sql_param_addQuery(){
		StringQuery sq = stringQuery("select * from ap_user");
		sq.addQuery(" where user_id = ?");
		try(QueryExecutor q = sq.forReuse()){
			List<ApUser> result = q.addParam("A1").getResultList(ApUser.class);
			
			assertNotNull(result);
			assertThat(result.size(), is(1));
			assertThat(result.get(0).getUserId(), is("A1"));
			
			result = q.clearParam().addParam("A2").getResultList(ApUser.class);
			assertNotNull(result);
			assertThat(result.size(), is(1));
			assertThat(result.get(0).getUserId(), is("A2"));
		}
	}
	
	@Test
	public void allTypeMapping() throws SQLException{
		QueryExecutor q = stringQuery("select * from typemodel").forOnce();
		Optional<TypeModel> tm = q.getResult(TypeModel.class);
		assertTrue(tm.isPresent());
		
		TypeModel model = tm.get();
		assertThat(model.isBoolean1(), is(true));
		assertThat(model.getBoolean2(), is(true));
		//assertThat(model.getByte1(), is((byte)1));
		//assertThat(model.getByte2(), is(Byte.valueOf((byte)1)));
		assertThat(model.getFloat1(), is(0.1f));
		assertThat(model.getFloat2(), is(Float.valueOf(0.2f)));
		assertThat(model.getDouble1(), is(0.3));
		assertThat(model.getDouble2(), is(Double.valueOf(0.4)));
//		assertThat(model.getBigInteger1(), is(BigInteger.valueOf(7)));
		assertThat(model.getBigDecimal(), is(BigDecimal.valueOf(0.8)));
		
		//assertThat(model.getInnerModel().getClob1().length(), is(26L));
		//assertThat(model.getInnerModel().getBlob1().length(), is(2L));
		
		//assertNull(model.getInnerModel().getEnum1());
		
		assertThat(model.getDate1(), is(LocalDate.parse("2015-07-01")));
		assertThat(model.getTime1(), is(LocalTime.parse("12:00:00")));
		assertThat(model.getTimestamp1(), is(LocalDateTime.parse("2015-07-01 12:00:00.555",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
	}
	
	@Test
	public void batchTest() throws IOException{
		BatchQueryExecutor q = stringQuery("update ap_user set name=? where user_id=?").forBatch();
		q.clearParam().addParam("HOGE").addParam("A1").executeUpdate();
		q.clearParam().addParam("PIYO").addParam("A2").executeUpdate();
		q.executeBatch();
		q.close();
		
		QueryExecutor eq = stringQuery("select * from ap_user where user_id=?").forReuse();
		
		Optional<String> rop = eq.addParam("A1").getResult((rs)->rs.getString("NAME"));
		assertTrue(rop.isPresent());
		assertThat(rop.get(), is("HOGE"));

		rop = eq.clearParam().addParam("A2").getResult((rs)->rs.getString("NAME"));
		assertTrue(rop.isPresent());
		assertThat(rop.get(), is("PIYO"));
		
		eq.close();
	}
}
