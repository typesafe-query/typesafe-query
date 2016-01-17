/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.ComparableDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.TypesafeQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class DateDBColumnImpl<T extends Comparable<? super T>> extends ComparableDBColumnImpl<T> implements
		DateDBColumn<T> {

	public DateDBColumnImpl(DBTable table, String name) {
		super(table, name);
	}
	
	public DateDBColumnImpl(TypesafeQuery query) {
		super(query);
	}

	public DateDBColumnImpl(Case case_) {
		super(case_);
	}

	protected DateDBColumnImpl(DBColumn<?> wrap) {
		super(wrap);
	}
	
	protected DateDBColumnImpl(DBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C extends ComparableDBColumn<T>> C addFunc(Func func) {
		DateDBColumnImpl<T> wrap = new DateDBColumnImpl<T>(this);
		wrap.add(func);
		return (C)func;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DBColumn<T>> V createFromTableAlias(String tableAlias) {
		return (V)new DateDBColumnImpl<T>(new DBTableImpl(getTable().getName(), tableAlias), getName());
	}

	@Override
	public DBColumn<T> as(String otherName) {
		return new DateDBColumnImpl<T>(this, otherName);
	}
}
