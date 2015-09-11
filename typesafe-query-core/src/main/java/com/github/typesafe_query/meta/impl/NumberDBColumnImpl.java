/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.IComparableDBColumn;
import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.INumberDBColumn;
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
public class NumberDBColumnImpl<T extends Number & Comparable<? super T>> extends ComparableDBColumnImpl<T> implements INumberDBColumn<T> {

	/**
	 * @param table
	 * @param name
	 */
	public NumberDBColumnImpl(IDBTable table, String name) {
		super(table, name);
	}
	
	public NumberDBColumnImpl(TypesafeQuery query) {
		super(query);
	}

	public NumberDBColumnImpl(Case case_) {
		super(case_);
	}

	/**
	 * @param table
	 * @param name
	 * @param otherName
	 * @param converters
	 */
	protected NumberDBColumnImpl(IDBColumn<?> wrap) {
		super(wrap);
	}

	protected NumberDBColumnImpl(IDBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C extends IComparableDBColumn<T>> C addFunc(Func func) {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(this);
		c.add(func);
		return (C)c;
	}

	@Override
	public INumberDBColumn<T> max() {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(this);
		c.add(new MaxFunc());
		return c;
	}
	
	@Override
	public INumberDBColumn<T> min() {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(this);
		c.add(new MinFunc());
		return c;
	}
	
	@Override
	public INumberDBColumn<T> avg() {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(this);
		c.add(new AvgFunc());
		return c;
	}

	@Override
	public INumberDBColumn<T> abs() {
		return addFunc(new AbsFunc());
	}

	@Override
	public INumberDBColumn<T> sqrt() {
		return addFunc(new SqrtFunc());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends IDBColumn<T>> V createFromTableAlias(String tableAlias) {
		return (V)new NumberDBColumnImpl<T>(new DBTableImpl(getTable().getName(), tableAlias), getName());
	}

	@Override
	public IDBColumn<T> as(String otherName) {
		return new NumberDBColumnImpl<T>(this,otherName);
	}
}
