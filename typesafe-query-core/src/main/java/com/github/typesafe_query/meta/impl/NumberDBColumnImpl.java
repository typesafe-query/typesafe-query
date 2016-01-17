/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import java.math.BigDecimal;

import com.github.typesafe_query.meta.ComparableDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.function.AbsFunc;
import com.github.typesafe_query.query.internal.function.AvgFunc;
import com.github.typesafe_query.query.internal.function.MaxFunc;
import com.github.typesafe_query.query.internal.function.MinFunc;
import com.github.typesafe_query.query.internal.function.SqrtFunc;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NumberDBColumnImpl<T extends Number & Comparable<? super T>> extends ComparableDBColumnImpl<T> implements NumberDBColumn<T> {

	public NumberDBColumnImpl(DBTable table, String name) {
		super(table, name);
	}
	
	public NumberDBColumnImpl(TypesafeQuery query) {
		super(query);
	}

	public NumberDBColumnImpl(Case case_) {
		super(case_);
	}

	protected NumberDBColumnImpl(DBColumn<?> wrap) {
		super(wrap);
	}

	protected NumberDBColumnImpl(DBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C extends ComparableDBColumn<T>> C addFunc(Func func) {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(this);
		c.add(func);
		return (C)c;
	}

	@Override
	public NumberDBColumn<T> max() {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(this);
		c.add(new MaxFunc());
		return c;
	}
	
	@Override
	public NumberDBColumn<T> min() {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(this);
		c.add(new MinFunc());
		return c;
	}
	
	@Override
	public NumberDBColumn<BigDecimal> avg() {
		NumberDBColumnImpl<BigDecimal> c = new NumberDBColumnImpl<BigDecimal>(this);
		c.add(new AvgFunc());
		return c;
	}

	@Override
	public NumberDBColumn<BigDecimal> abs() {
		NumberDBColumnImpl<BigDecimal> c = new NumberDBColumnImpl<BigDecimal>(this);
		c.add(new AbsFunc());
		return c;
	}

	@Override
	public NumberDBColumn<BigDecimal> sqrt() {
		NumberDBColumnImpl<BigDecimal> c = new NumberDBColumnImpl<BigDecimal>(this);
		c.add(new SqrtFunc());
		return c;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DBColumn<T>> V createFromTableAlias(String tableAlias) {
		return (V)new NumberDBColumnImpl<T>(new DBTableImpl(getTable().getName(), tableAlias), getName());
	}

	@Override
	public DBColumn<T> as(String otherName) {
		return new NumberDBColumnImpl<T>(this,otherName);
	}
}
