package com.github.typesafe_query.query.internal;

import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.InvalidQueryException;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.SearchedCase;
import com.github.typesafe_query.util.Pair;

public class DefaultSearchedCase extends DefaultCase implements SearchedCase{
	
	private List<Pair<Exp, Object>> expressions;
	private Exp when;
	private Object else_;
	
	public DefaultSearchedCase() {
		expressions = new ArrayList<Pair<Exp,Object>>();
	}

	@Override
	public SearchedCase when(Exp exp) {
		when = exp;
		return this;
	}

	@Override
	public SearchedCase then(Object o) {
		expressions.add(new Pair<Exp, Object>(when, o));
		return this;
	}

	@Override
	public SearchedCase else_(Object o) {
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
		.append("CASE");
		
		for(Pair<Exp, Object> t : expressions){
			sb
			.append(" WHEN ")
			.append(t._1.getSQL(context))
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
