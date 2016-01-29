package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.BooleanDBColumn;
import com.github.typesafe_query.meta.ComparableDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.function.AllFunc;
import com.github.typesafe_query.query.internal.function.AnyFunc;
import com.github.typesafe_query.query.internal.function.SomeFunc;

public class BooleanDBColumnImpl extends ComparableDBColumnImpl<Boolean> implements BooleanDBColumn{

	public BooleanDBColumnImpl(DBTable table, String name) {
		super(table, name);
	}
	
	public BooleanDBColumnImpl(TypesafeQuery query) {
		super(query);
	}

	public BooleanDBColumnImpl(Case case_) {
		super(case_);
	}

	protected BooleanDBColumnImpl(DBColumn<?> wrap) {
		super(wrap);
	}

	protected BooleanDBColumnImpl(DBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DBColumn<Boolean>> V createFromTableAlias(
			String tableAlias) {
		return (V)new BooleanDBColumnImpl(new DBTableImpl(getTable().getSchema(),getTable().getSimpleName(), tableAlias), getName());
	}

	@Override
	public DBColumn<Boolean> as(String otherName) {
		return new BooleanDBColumnImpl(this, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C extends ComparableDBColumn<Boolean>> C addFunc(Func func) {
		BooleanDBColumnImpl wrap = new BooleanDBColumnImpl(this);
		wrap.add(func);
		return (C)wrap;
	}

	@Override
	public BooleanDBColumn any() {
		return addFunc(new AnyFunc());
	}

	@Override
	public BooleanDBColumn some() {
		return addFunc(new SomeFunc());
	}

	@Override
	public BooleanDBColumn all() {
		return addFunc(new AllFunc());
	}
}
