package com.github.typesafe_query.query.internal;

import static com.github.typesafe_query.Q.$;
import static com.github.typesafe_query.Q.and;
import static com.github.typesafe_query.Q.case_;
import static com.github.typesafe_query.Q.exists;
import static com.github.typesafe_query.Q.param;
import static com.github.typesafe_query.Q.select;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.typesafe_query.ConnectionHolder;
import com.github.typesafe_query.util.SQLUtils;
import com.sample.model.ApUser;
import com.sample.model.ApUser_;
import com.sample.model.Role_;
import com.sample.model.Unit;
import com.sample.model.Unit_;


public class SelectQueryTest {
	
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
	public void select_allColumn(){
		List<ApUser> result = select().from(ApUser_.TABLE).forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(4));
	}

	@Test
	public void selectSingleColumn(){
		List<ApUser> result = select(ApUser_.USER_ID).from(ApUser_.TABLE).forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(4));
		assertNotNull(result.get(0).getUserId());
		assertNull(result.get(0).getName());
	}
	
	@Test
	public void selectTwoColumn(){
		List<ApUser> result = select(ApUser_.USER_ID,ApUser_.ROLE_ID).from(ApUser_.TABLE).forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(4));
		assertNotNull(result.get(0).getUserId());
		assertNotNull(result.get(0).getRoleId());
		assertNull(result.get(0).getName());
	}
	
	@Test
	public void selectSingleResult(){
		Optional<ApUser> result = select().from(ApUser_.TABLE).forOnce().getResult(ApUser.class);
		
		assertNotNull(result);
		assertTrue(result.isPresent());
		assertThat(result.get().getUserId(), is("A1"));
	}

	@Test
	public void select_singleWhere(){
		List<ApUser> result = select()
								.from(ApUser_.TABLE)
								.where(
									ApUser_.UNIT_ID.eq("U1")
								)
								.forOnce()
								.getResultList(ApUser.class);

		
		assertNotNull(result);
		assertThat(result.size(), is(3));
		assertThat(result.get(0).getUnitId(), is("U1"));
	}
	
	@Test
	public void select_singleWhereWithParam(){
		List<ApUser> result = select()
								.from(ApUser_.TABLE)
								.where(
									ApUser_.UNIT_ID.eq(param())
								)
								.forOnce()
								.addParam("U1")
								.getResultList(ApUser.class);

		
		assertNotNull(result);
		assertThat(result.size(), is(3));
		assertThat(result.get(0).getUnitId(), is("U1"));
	}
	
	@Test
	public void select_multipuleWhere(){
		List<ApUser> result = select()
				.from(ApUser_.TABLE)
				.where(
					and(
						ApUser_.UNIT_ID.eq("U1"),
						ApUser_.LOCK_FLG.eq("1")
					)
				)
				.forOnce()
				.getResultList(ApUser.class);


		assertNotNull(result);
		assertThat(result.size(), is(2));
		assertThat(result.get(0).getUnitId(), is("U1"));
		assertThat(result.get(0).getUserId(),is("A1"));
	}
	
	@Test
	public void select_multipuleWhereWithParam(){
		List<ApUser> result = select()
				.from(ApUser_.TABLE)
				.where(
					and(
						ApUser_.UNIT_ID.eq(param()),
						ApUser_.LOCK_FLG.eq(param())
					)
				)
				.forOnce()
				.addParam("U1")
				.addParam("1")
				.getResultList(ApUser.class);


		assertNotNull(result);
		assertThat(result.size(), is(2));
		assertThat(result.get(0).getUnitId(), is("U1"));
		assertThat(result.get(0).getUserId(),is("A1"));
	}

	@Test
	public void select_singleOrderBy(){
		List<ApUser> result = select()
				.from(ApUser_.TABLE)
				.orderBy(ApUser_.USER_ID.desc())
				.forOnce()
				.getResultList(ApUser.class);

		assertNotNull(result);
		assertThat(result.size(), is(4));
		assertThat(result.get(0).getUserId(), is("A4"));
		assertThat(result.get(1).getUserId(),is("A3"));
	}
	
	@Test
	public void select_multipuleOrderBy(){
		List<ApUser> result = select()
				.from(ApUser_.TABLE)
				.orderBy(ApUser_.ROLE_ID.desc(),ApUser_.USER_ID.asc())
				.forOnce()
				.getResultList(ApUser.class);

		assertNotNull(result);
		assertThat(result.size(), is(4));
		assertThat(result.get(0).getUserId(), is("A4"));
		assertThat(result.get(1).getUserId(),is("A1"));
	}
	
	@Test
	public void select_singleGroupBy(){
		List<ApUser> result = select(ApUser_.UNIT_ID)
				.from(ApUser_.TABLE)
				.groupBy(ApUser_.UNIT_ID)
				.forOnce()
				.getResultList(ApUser.class);

		assertNotNull(result);
		assertThat(result.size(), is(2));
	}
	
	@Test
	public void select_multipuleGroupBy(){
		List<ApUser> result = select(ApUser_.UNIT_ID,ApUser_.LOCK_FLG)
				.from(ApUser_.TABLE)
				.groupBy(ApUser_.UNIT_ID,ApUser_.LOCK_FLG)
				.forOnce()
				.getResultList(ApUser.class);

		assertNotNull(result);
		assertThat(result.size(), is(3));
	}
	
	@Test
	public void select_having(){
		List<ApUser> result = select(ApUser_.UNIT_ID,ApUser_.LOCK_FLG)
				.from(ApUser_.TABLE)
				.groupBy(ApUser_.UNIT_ID,ApUser_.LOCK_FLG)
				.having(ApUser_.LOCK_FLG.eq("0"))
				.forOnce()
				.getResultList(ApUser.class);

		assertNotNull(result);
		assertThat(result.size(), is(2));
	}

	@Test
	public void select_limit(){
		List<ApUser> result = select().from(ApUser_.TABLE).limit(1).forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(1));
	}
	@Test
	public void select_limit_offset(){
		List<ApUser> result = select().from(ApUser_.TABLE).orderBy(ApUser_.USER_ID.asc()).limit(1).offset(1).forOnce().getResultList(ApUser.class);
		
		assertNotNull(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getUserId(), is("A2"));
	}
	@Test
	public void select_innerjoin(){
		List<Unit> result = select()
				.from(Unit_.TABLE)
				.innerJoin(ApUser_.TABLE).on(Unit_.UNIT_ID.eq(ApUser_.UNIT_ID))
				.where(Unit_.UNIT_ID.eq("U3"))
				.forOnce()
				.getResultList(Unit.class);
			
		assertNotNull(result);
		assertThat(result.size(), is(0));
	}
	@Test
	public void select_outerjoin(){
		List<Unit> result = select()
				.from(Unit_.TABLE)
				.outerJoin(ApUser_.TABLE).on(Unit_.UNIT_ID.eq(ApUser_.UNIT_ID))
				.where(Unit_.UNIT_ID.eq("U3"))
				.forOnce()
				.getResultList(Unit.class);
			
		assertNotNull(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getUnitId(), is("U3"));
	}
	
	@Test
	public void select_exists(){
		List<Unit> result = select()
				.from(Unit_.TABLE)
				.where(
					exists(select()
						.from(ApUser_.TABLE)
						.where(
							ApUser_.UNIT_ID.eq(Unit_.UNIT_ID)
						)
					)
				)
				.orderBy(Unit_.UNIT_ID.asc())
				.forOnce()
				.getResultList(Unit.class);
			
		assertNotNull(result);
		assertThat(result.size(), is(2));
		assertThat(result.get(0).getUnitId(), is("U1"));
		assertThat(result.get(1).getUnitId(), is("U2"));
	}
	
	@Test
	public void select_distinct(){
		List<ApUser> result = select(ApUser_.UNIT_ID).distinct().from(ApUser_.TABLE).forOnce().getResultList(ApUser.class);

		assertNotNull(result);
		assertThat(result.size(), is(2));
		assertThat(result.get(0).getUnitId(), is("U1"));
		assertThat(result.get(1).getUnitId(), is("U2"));
	}
	
	@Test
	public void select_forUpdate(){
		Optional<ApUser> result = select().forUpdate().from(ApUser_.TABLE).where(ApUser_.USER_ID.eq("A1")).forOnce().getResult(ApUser.class);
		
		assertNotNull(result);
		assertTrue(result.isPresent());
	}
	
	@Test
	public void select_subquery_from(){
		List<Object> results = select().from(select().from(ApUser_.TABLE).as("sub")).forOnce().getResultList((rs)->rs.get(1));
		assertNotNull(results);
		assertThat(results.size(), is(4));
	}
	
	@Test
	public void select_subquery_innerjoin(){
		List<Object> results = select()
				.from(
					ApUser_.TABLE
				)
				.innerJoin(
					select()
						.from(Role_.TABLE).as("sub")
				)
				.on(
					ApUser_.ROLE_ID.eq($("sub",Role_.ROLE_ID))
				)
				.forOnce().getResultList((rs)->rs.get(1));
		assertNotNull(results);
		assertThat(results.size(), is(4));
	}
	
	@Test
	public void select_subquery_outerjoin(){
		List<Object> results = select()
				.from(
					ApUser_.TABLE
				)
				.outerJoin(
					select()
						.from(Role_.TABLE).as("sub")
				)
				.on(
					ApUser_.ROLE_ID.eq($("sub",Role_.ROLE_ID))
				)
				.forOnce().getResultList((rs)->rs.get(1));
		assertNotNull(results);
		assertThat(results.size(), is(4));
	}
	
	@Test
	public void select_subquery_column(){
		List<Object> results = select(
					ApUser_.ROLE_ID,
					select(Role_.NAME).from(Role_.TABLE).where(ApUser_.ROLE_ID.eq(Role_.ROLE_ID)).asStringColumn().as("NAME")
				)
				.from(
					ApUser_.TABLE
				)
				.forOnce().getResultList((rs)->rs.get(1));
		assertNotNull(results);
		assertThat(results.size(), is(4));
	}
	
	@Test
	public void select_subquery_expression(){
		List<Object> results = select()
				.from(
					ApUser_.TABLE
				)
				.where(ApUser_.ROLE_ID.eq(select(Role_.ROLE_ID).from(Role_.TABLE).where(Role_.ROLE_ID.eq("R1"))))
				.forOnce().getResultList((rs)->rs.get(1));
		assertNotNull(results);
		assertThat(results.size(), is(3));
	}
	
	@Test
	public void select_subquery_expression_in(){
		List<Object> results = select()
				.from(
					ApUser_.TABLE
				)
				.where(ApUser_.ROLE_ID.in(select(Role_.ROLE_ID).from(Role_.TABLE)))
				.forOnce().getResultList((rs)->rs.get(1));
		assertNotNull(results);
		assertThat(results.size(), is(4));
	}
	
	@Test
	public void select_subquery_expression_like(){
		List<Object> results = select()
				.from(
					ApUser_.TABLE
				)
				.where(ApUser_.ROLE_ID.like(select(Role_.ROLE_ID).from(Role_.TABLE).where(Role_.ROLE_ID.eq("R1"))))
				.forOnce().getResultList((rs)->rs.get(1));
		assertNotNull(results);
		assertThat(results.size(), is(3));
	}
	
	@Test
	public void select_case_(){
		List<String> results = 
				select(
					case_(ApUser_.USER_ID)
						.when("A1").then("ほげ")
						.when("A2").then("ぴよ")
						.else_("はげ")
					.endAsStringColumn().as("ADANA")
				)
				.from(
					ApUser_.TABLE
				)
				.forOnce().getResultList((rs)->rs.getString("ADANA"));
		assertNotNull(results);
		assertThat(results.size(), is(4));
		assertThat(results.get(0), is("ほげ"));
		assertThat(results.get(1), is("ぴよ"));
		assertThat(results.get(2), is("はげ"));
		assertThat(results.get(3), is("はげ"));
	}
	
	@Test
	public void select_searched_case_(){
		List<String> results = 
				select(
					case_()
						.when(ApUser_.USER_ID.eq("A1")).then("ほげ")
						.when(ApUser_.USER_ID.eq("A2")).then("ぴよ")
						.else_("はげ")
					.endAsStringColumn().as("ADANA")
				)
				.from(
					ApUser_.TABLE
				)
				.forOnce().getResultList((rs)->rs.getString("ADANA"));
		assertNotNull(results);
		assertThat(results.size(), is(4));
		assertThat(results.get(0), is("ほげ"));
		assertThat(results.get(1), is("ぴよ"));
		assertThat(results.get(2), is("はげ"));
		assertThat(results.get(3), is("はげ"));
	}
}
