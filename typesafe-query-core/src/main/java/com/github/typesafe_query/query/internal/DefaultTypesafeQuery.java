/**
 * 
 */
package com.github.typesafe_query.query.internal;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.Q;
import com.github.typesafe_query.meta.IBooleanDBColumn;
import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.IDateDBColumn;
import com.github.typesafe_query.meta.INumberDBColumn;
import com.github.typesafe_query.meta.IStringDBColumn;
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
	
	private IDBColumn<?>[] select;
	
	private IDBTable from;
	
	private boolean distinct;
	
	private boolean forUpdate;
	
	private List<Join<TypesafeQuery>> joins;
	
	private Exp where;
	
	private IDBColumn<?>[] groupBy;
	
	private Order[] orderBy;
	
	private Exp having;
	
	private Integer limit;
	private Integer offset;
	
	private String sql;
	
	public DefaultTypesafeQuery() {
		this(new IDBColumn<?>[0]);
	}

	public DefaultTypesafeQuery(IDBColumn<?>... columns) {
		this.joins = new ArrayList<Join<TypesafeQuery>>();
		this.select = columns;
	}

	@Override
	public TypesafeQuery from(IDBTable root) {
		this.from = root;
		return this;
	}

	@Override
	public TypesafeQuery from(IDBTable root, String alias) {
		return from(new DBTableImpl(root.getName(), alias));
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
	public Join<TypesafeQuery> innerJoin(IDBTable joinTable) {
		Join<TypesafeQuery> join = new InnerJoin<TypesafeQuery>(joinTable,this);
		joins.add(join);
		return join;
	}
	
	@Override
	public Join<TypesafeQuery> innerJoin(IDBTable joinTable, String alias) {
		return innerJoin(new DBTableImpl(joinTable.getName(), alias));
	}

	@Override
	public Join<TypesafeQuery> outerJoin(IDBTable joinTable) {
		Join<TypesafeQuery> join = new OuterJoin<TypesafeQuery>(joinTable,this);
		joins.add(join);
		return join;
	}

	@Override
	public Join<TypesafeQuery> outerJoin(IDBTable joinTable, String alias) {
		return outerJoin(new DBTableImpl(joinTable.getName(), alias));
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
	public TypesafeQuery groupBy(IDBColumn<?>... columns) {
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
	public IDBTable as(String alias) {
		return new DBTableImpl(new SubQueryDBTableImpl(this,from),alias);
	}

	@Override
	public IStringDBColumn asStringColumn() {
		return new StringDBColumnImpl(this);
	}

	@Override
	public IDateDBColumn<LocalDate> asLocalDateColumn() {
		return new DateDBColumnImpl<LocalDate>(this);
	}

	@Override
	public IDateDBColumn<LocalTime> asLocalTimeColumn() {
		return new DateDBColumnImpl<LocalTime>(this);
	}

	@Override
	public IDateDBColumn<LocalDateTime> asLocalDateTimeColumn() {
		return new DateDBColumnImpl<LocalDateTime>(this);
	}

	@Override
	public IDateDBColumn<Date> asDateColumn() {
		return new DateDBColumnImpl<Date>(this);
	}

	@Override
	public IDateDBColumn<Timestamp> asTimestampColumn() {
		return new DateDBColumnImpl<Timestamp>(this);
	}

	@Override
	public IDateDBColumn<Time> asTimeColumn() {
		return new DateDBColumnImpl<Time>(this);
	}

	@Override
	public IBooleanDBColumn asBooleanColumn() {
		return new BooleanDBColumnImpl(this);
	}

	@Override
	public <N extends Number & Comparable<? super N>> INumberDBColumn<N> asNumberColumn() {
		return new NumberDBColumnImpl<N>(this);
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
			.append(from.getQuery(context))
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

		sql = sb.toString();
		
		return sql;
	}

	private String createSelection(QueryContext context){
		List<String> sels = new ArrayList<String>();
		if(limit != null || offset != null){
			sels.add(ROWNUM);
		}
		for(IDBColumn<?> c : select){
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
			IDBTable jt = j.getTargetTable();
			sb.append(String.format("%s ON %s ", jt.getQuery(context),j.getOn().getSQL(context)));
			list.add(sb.toString());
		}
		
		return joinWith(" ",list) + " ";
	}
	
	private String createGroupBy(QueryContext context){
		List<String> list = new ArrayList<String>();
		for(IDBColumn<?> c : groupBy){
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
