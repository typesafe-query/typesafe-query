/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.expression.EqExp;
import com.github.typesafe_query.query.internal.expression.NotEqExp;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public abstract class DBColumnImpl<T> implements IDBColumn<T> {
	
	private IDBTable table;
	private String name;
	private String otherName;
	
	private IDBColumn<?> wrap;
	
	private TypesafeQuery query;
	
	private Case case_;

	private List<Func> funcs;
	
	public DBColumnImpl(IDBTable table,String name) {
		this.table = table;
		this.name = name;
		this.funcs = new ArrayList<Func>();
	}
	
	public DBColumnImpl(TypesafeQuery query) {
		this.query = query;
	}
	
	public DBColumnImpl(Case case_) {
		this.case_ = case_;
	}
	
	protected DBColumnImpl(IDBColumn<?> wrap) {
		if(wrap == null){
			throw new NullPointerException("wrap is null");
		}
		this.table = wrap.getTable();
		this.name = wrap.getName();
		this.otherName = wrap.getOtherName();
		this.funcs = new ArrayList<Func>();
		this.wrap = wrap;
	}
	
	protected DBColumnImpl(IDBColumn<?> wrap,String otherName) {
		if(wrap == null){
			throw new NullPointerException("wrap is null");
		}
		this.table = wrap.getTable();
		this.name = wrap.getName();
		this.otherName = otherName;
		this.funcs = new ArrayList<Func>();
		this.wrap = wrap;
	}
	
	@Override
	public Exp eq(IDBColumn<T> c) {
		return new EqExp<T>(this, c);
	}

	@Override
	public Exp eq(T t) {
		return new EqExp<T>(this, t);
	}
	
	@Override
	public Exp eq(Param p) {
		return new EqExp<T>(this, p);
	}

	@Override
	public Exp eq(TypesafeQuery subQuery) {
		return new EqExp<T>(this, subQuery);
	}
	
	@Override
	public Exp neq(IDBColumn<T> c) {
		return new NotEqExp<T>(this, c);
	}

	@Override
	public Exp neq(T t) {
		return new NotEqExp<T>(this, t);
	}
	
	@Override
	public Exp neq(Param p) {
		return new NotEqExp<T>(this, p);
	}
	
	@Override
	public Exp neq(TypesafeQuery subQuery) {
		return new NotEqExp<T>(this, subQuery);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getOtherName() {
		return otherName;
	}
	
	@Override
	public IDBTable getTable() {
		return table;
	}
	/**
	 * @return the funcs
	 */
	protected List<Func> getFuncs() {
		return funcs;
	}
	
	protected void add(Func func){
		funcs.add(func);
	}
	
	@Override
	public String getExpression(QueryContext context, String expression) {
		if(query != null){
			return String.format("(%s)", query.getSQL(context));
		}
		
		if(case_ != null){
			return String.format("(%s)", case_.getSQL(context));
		}
		
		if(wrap != null){
			expression = wrap.getExpression(context, expression);
		}
		
		for(Func func : getFuncs()){
			expression = func.getSQL(context, expression);
		}
		return expression;
	}
	
}
