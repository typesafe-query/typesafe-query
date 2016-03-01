/**
 * 
 */
package com.github.typesafe_query.query.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.BooleanDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.StringDBColumn;
import com.github.typesafe_query.meta.impl.BooleanDBColumnImpl;
import com.github.typesafe_query.meta.impl.DBTableImpl;
import com.github.typesafe_query.meta.impl.DateDBColumnImpl;
import com.github.typesafe_query.meta.impl.NumberDBColumnImpl;
import com.github.typesafe_query.meta.impl.StringDBColumnImpl;
import com.github.typesafe_query.meta.impl.SubQueryDBTableImpl;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Join;
import com.github.typesafe_query.query.Order;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class DefaultTypesafeQuery extends AbstractSQLQuery implements TypesafeQuery {
	
	private static final String ROWNUM = "ROW_NUMBER() OVER() AS RN";
	private static final String ROWNUM_TEMPATE_BOTH = "SELECT E.* FROM (%s) E WHERE E.RN BETWEEN %d AND %d";
	private static final String ROWNUM_TEMPATE_NOLIMIT = "SELECT E.* FROM (%s) E WHERE E.RN >= %d";
	
	private DBColumn<?>[] select;
	
	private DBTable from;
	
	private boolean distinct;
	
	private boolean forUpdate;
	
	private List<Join<TypesafeQuery>> joins;
	
	private Exp where;
	
	private DBColumn<?>[] groupBy;
	
	private Order[] orderBy;
	
	private Exp having;
	
	private Integer limit;
	private Integer offset;
	
	private TypesafeQuery union;

	private TypesafeQuery unionAll;
	
	private String sql;
	
	public DefaultTypesafeQuery() {
		this(new DBColumn<?>[0]);
	}

	public DefaultTypesafeQuery(DBColumn<?>... columns) {
		this.joins = new ArrayList<Join<TypesafeQuery>>();
		this.select = columns;
	}

	@Override
	public TypesafeQuery from(DBTable root) {
		this.from = root;
		return this;
	}

	@Override
	public TypesafeQuery from(DBTable root, String alias) {
		return from(new DBTableImpl(root.getSchema(), root.getSimpleName(), alias));
	}

	@Override
	public TypesafeQuery distinct() {
		this.distinct = true;
		return this;
	}

	@Override
	public TypesafeQuery forUpdate() {
		this.forUpdate = true;
		return this;
	}
	
	@Override
	public Join<TypesafeQuery> innerJoin(DBTable joinTable) {
		Join<TypesafeQuery> join = new InnerJoin<TypesafeQuery>(joinTable,this);
		joins.add(join);
		return join;
	}
	
	@Override
	public Join<TypesafeQuery> innerJoin(DBTable joinTable, String alias) {
		return innerJoin(new DBTableImpl(joinTable.getSchema(), joinTable.getSimpleName(), alias));
	}

	@Override
	public Join<TypesafeQuery> outerJoin(DBTable joinTable) {
		Join<TypesafeQuery> join = new OuterJoin<TypesafeQuery>(joinTable,this);
		joins.add(join);
		return join;
	}

	@Override
	public Join<TypesafeQuery> outerJoin(DBTable joinTable, String alias) {
		return outerJoin(new DBTableImpl(joinTable.getSchema(),joinTable.getSimpleName(), alias));
	}

	@Override
	public TypesafeQuery where(Exp... exps) {
		if(exps == null){
			this.where = null;
		}else if(exps.length == 1){
			this.where = exps[0];
		}else{
			this.where = Q.and(exps);
		}
		return this;
	}

	@Override
	public TypesafeQuery groupBy(DBColumn<?>... columns) {
		this.groupBy = columns;
		return this;
	}

	@Override
	public TypesafeQuery orderBy(Order... orders) {
		this.orderBy = orders;
		return this;
	}

	@Override
	public TypesafeQuery having(Exp... exps) {
		if(exps == null){
			this.having = null;
		}else if(exps.length == 1){
			this.having = exps[0];
		}else{
			this.having = Q.and(exps);
		}
		return this;
	}

	@Override
	public TypesafeQuery limit(Integer limit) {
		this.limit = limit;
		return this;
	}

	@Override
	public TypesafeQuery offset(Integer offset) {
		this.offset = offset;
		return this;
	}
	
	@Override
	public TypesafeQuery union(TypesafeQuery query){
		this.union = query;
		return this;
	}

	@Override
	public TypesafeQuery unionAll(TypesafeQuery query){
		this.unionAll = query;
		return this;
	}
	
	@Override
	public DBTable as(String alias) {
		return new DBTableImpl(new SubQueryDBTableImpl(this,from),alias);
	}

	@Override
	public StringDBColumn asStringColumn() {
		return new StringDBColumnImpl(this);
	}

	@Override
	public DateDBColumn<LocalDate> asLocalDateColumn() {
		return new DateDBColumnImpl<LocalDate>(this);
	}

	@Override
	public DateDBColumn<LocalTime> asLocalTimeColumn() {
		return new DateDBColumnImpl<LocalTime>(this);
	}

	@Override
	public DateDBColumn<LocalDateTime> asLocalDateTimeColumn() {
		return new DateDBColumnImpl<LocalDateTime>(this);
	}

	@Override
	public DateDBColumn<Date> asDateColumn() {
		return new DateDBColumnImpl<Date>(this);
	}

	@Override
	public DateDBColumn<Timestamp> asTimestampColumn() {
		return new DateDBColumnImpl<Timestamp>(this);
	}

	@Override
	public DateDBColumn<Time> asTimeColumn() {
		return new DateDBColumnImpl<Time>(this);
	}

	@Override
	public BooleanDBColumn asBooleanColumn() {
		return new BooleanDBColumnImpl(this);
	}

	@Override
	public <N extends Number & Comparable<? super N>> NumberDBColumn<N> asNumberColumn() {
		return new NumberDBColumnImpl<N>(this);
	}

	@Override
	public NumberDBColumn<Short> asShortColumn() {
		return new NumberDBColumnImpl<Short>(this);
	}

	@Override
	public NumberDBColumn<Integer> asIntegerColumn() {
		return new NumberDBColumnImpl<Integer>(this);
	}

	@Override
	public NumberDBColumn<Long> asLongColumn() {
		return new NumberDBColumnImpl<Long>(this);
	}

	@Override
	public NumberDBColumn<BigInteger> asBigIntegerColumn() {
		return new NumberDBColumnImpl<BigInteger>(this);
	}

	@Override
	public NumberDBColumn<Float> asFloatColumn() {
		return new NumberDBColumnImpl<Float>(this);
	}

	@Override
	public NumberDBColumn<Double> asDoubleColumn() {
		return new NumberDBColumnImpl<Double>(this);
	}

	@Override
	public NumberDBColumn<BigDecimal> asBigDecimalColumn() {
		return new NumberDBColumnImpl<BigDecimal>(this);
	}

	@Override
	public String getSQL(QueryContext context) {
		if(sql != null){
			return sql;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		
		if(distinct){
			sb.append("DISTINCT ");
		}
		
		if(context == null){
			context = new DefaultQueryContext(this.from);
		}else{
			context.addFrom(from);
		}
		
		//SELECT項目の指定前にテーブルをCONTEXTに追加
		for(Join<TypesafeQuery> j : joins){
			context.addFrom(j.getTargetTable());
		}
		
		if(select != null && select.length > 0){
			sb.append(createSelection(context));
		}else{
			if(limit != null || offset != null){
				sb.append(ROWNUM + ",* ");
			}else{
				sb.append("* ");
			}
		}
		
		sb
			.append("FROM ")
			.append(from.getSQL(context))
			.append(" ");
		
		if(!joins.isEmpty()){
			sb.append(createJoin(context));
		}
		
		if(where != null){
			String p = where.getSQL(context);
			if(p != null && !p.isEmpty()){
				sb
					.append("WHERE ")
					.append(p)
					.append(" ");
			}
		}
		
		if(groupBy != null && groupBy.length > 0){
			sb
				.append("GROUP BY ")
				.append(createGroupBy(context));
		}
		
		if(orderBy != null && orderBy.length > 0){
			sb
				.append("ORDER BY ")
				.append(createOrderBy(context));
		}
		
		if(having != null){
			String p = having.getSQL(context);
			if(!p.isEmpty()){
				sb
					.append("HAVING ")
					.append(p);
			}
		}
		
		sb = createLimitOffset(sb,limit,offset);
		
		if(forUpdate){
			sb.append(createForUpdate()).append(" ");
		}
		
		if(union != null){
			sb.append("UNION ");
			sb.append(union.getSQL(context));
		}

		if(unionAll != null){
			sb.append("UNION ALL ");
			sb.append(unionAll.getSQL(context));
		}

		sql = sb.toString();
		
		return sql;
	}

	private String createSelection(QueryContext context){
		List<String> sels = new ArrayList<String>();
		if(limit != null || offset != null){
			sels.add(ROWNUM);
		}
		for(DBColumn<?> c : select){
			sels.add(context.getColumnPath(c));
		}
		return joinWith(",",sels) + " ";
	}

	private String createJoin(QueryContext context){
		List<String> list = new ArrayList<String>();
		for(Join<TypesafeQuery> j : joins){
			StringBuilder sb = new StringBuilder();
			if(j instanceof OuterJoin){
				sb.append("LEFT OUTER JOIN ");
			}else{
				sb.append("INNER JOIN ");
			}
			DBTable jt = j.getTargetTable();
			sb.append(String.format("%s ON %s ", jt.getSQL(context),j.getOn().getSQL(context)));
			list.add(sb.toString());
		}
		
		return joinWith(" ",list) + " ";
	}
	
	private String createGroupBy(QueryContext context){
		List<String> list = new ArrayList<String>();
		for(DBColumn<?> c : groupBy){
			list.add(context.getColumnPath(c));
		}
		return joinWith(",",list) + " ";
	}
	
	private String createOrderBy(QueryContext context){
		List<String> list = new ArrayList<String>();
		for(Order o : orderBy){
			list.add(o.getOrder(context));
		}
		return joinWith(",",list) + " ";
	}
	
	protected String createForUpdate(){
		return "FOR UPDATE";
	}
	
	protected StringBuilder createLimitOffset(StringBuilder sb,Integer limit,Integer offset){
		if(limit != null || offset != null){
			Integer off = offset;
			if(off == null){
				off = 0;
			}

			if(limit == null){
				sb = new StringBuilder(String.format(ROWNUM_TEMPATE_NOLIMIT, sb.toString(),off + 1));
			}else{
				sb = new StringBuilder(String.format(ROWNUM_TEMPATE_BOTH, sb.toString(),off + 1,off + limit));
			}
		}
		return sb;
	}
	
	private static String joinWith(String ch,List<String> list){
		return QueryUtils.joinWith(ch, list);
	}

	@Override
	public String toString() {
		return getSQL(null);
	}
}
