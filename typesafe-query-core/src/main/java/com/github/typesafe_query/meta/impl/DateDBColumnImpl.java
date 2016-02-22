/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.ComparableDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.DateDBColumn;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.function.AddFunc;
import com.github.typesafe_query.query.internal.function.AllFunc;
import com.github.typesafe_query.query.internal.function.AnyFunc;
import com.github.typesafe_query.query.internal.function.SomeFunc;
import com.github.typesafe_query.query.internal.function.SubtractFunc;

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
		return (C)wrap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DBColumn<T>> V createFromTableAlias(String tableAlias) {
		return (V)new DateDBColumnImpl<T>(new DBTableImpl(getTable().getSchema(),getTable().getSimpleName(), tableAlias), getName());
	}

	@Override
	public DBColumn<T> as(String otherName) {
		return new DateDBColumnImpl<T>(this, otherName);
	}
	@Override
	public DateDBColumnImpl<T> any(){
		return addFunc(new AnyFunc());
	}

	@Override
	public DateDBColumnImpl<T> some(){
		return addFunc(new SomeFunc());
	}

	@Override
	public DateDBColumnImpl<T> all(){
		return addFunc(new AllFunc());
	}
	
	@Override
	public DateDBColumnImpl<T> add(Integer expr, String unit){
		return addFunc(new AddFunc(expr, unit));
	}

	@Override
	public DateDBColumnImpl<T> add(NumberDBColumn<?> column, String unit){
		return addFunc(new AddFunc(column, unit));
	}
	
	@Override
	public DateDBColumnImpl<T> subtract(Integer expr, String unit){
		return addFunc(new SubtractFunc(expr, unit));
	}

	@Override
	public DateDBColumnImpl<T> subtract(NumberDBColumn<?> column, String unit){
		return addFunc(new SubtractFunc(column, unit));
	}
}
