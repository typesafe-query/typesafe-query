package com.github.typesafe_query.query.internal;

import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.SimpleCase;
import com.github.typesafe_query.util.Pair;

public class DefaultSimpleCase<T> extends DefaultCase implements SimpleCase<T>{
	
	private DBColumn<T> caseTarget;
	
	private List<Pair<T, Object>> expressions;
	
	private T when;
	
	private Object else_;
	
	public DefaultSimpleCase(DBColumn<T> column) {
		this.caseTarget = column;
		expressions = new ArrayList<Pair<T,Object>>();
	}

	@Override
	public SimpleCase<T> when(T t) {
		when = t;
		return this;
	}

	@Override
	public SimpleCase<T> then(Object o) {
		expressions.add(new Pair<T, Object>(when, o));
		return this;
	}

	@Override
	public SimpleCase<T> else_(Object o) {
		this.else_ = o;
		return this;
	}

	@Override
	public String getSQL(QueryContext context) {
		if(expressions.isEmpty()){
			throw new InvalidQueryException("CASE条件がありません");
		}
		
		StringBuilder sb = new StringBuilder();
		sb
		.append("CASE ")
		.append(context.getColumnPath(caseTarget));
		
		for(Pair<T, Object> t : expressions){
			sb
			.append(" WHEN ")
			.append(QueryUtils.literal(t._1))
			.append(" THEN ")
			.append(QueryUtils.literal(t._2));
		}
		
		if(else_ != null){
			sb
			.append(" ELSE ")
			.append(QueryUtils.literal(else_));
		}
		sb.append(" END");
		return sb.toString();
	}
}
