/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.IComparableDBColumn;
import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.meta.INumberDBColumn;
import com.github.typesafe_query.meta.IStringDBColumn;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.expression.LikeExp;
import com.github.typesafe_query.query.internal.expression.NotLikeExp;
import com.github.typesafe_query.query.internal.function.ConcatFunc;
import com.github.typesafe_query.query.internal.function.LengthFunc;
import com.github.typesafe_query.query.internal.function.LowerFunc;
import com.github.typesafe_query.query.internal.function.SubstringFunc;
import com.github.typesafe_query.query.internal.function.ToNumberFunc;
import com.github.typesafe_query.query.internal.function.TrimFunc;
import com.github.typesafe_query.query.internal.function.UpperFunc;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class StringDBColumnImpl extends ComparableDBColumnImpl<String> implements IStringDBColumn{

	public StringDBColumnImpl(IDBTable table, String name) {
		super(table, name);
	}
	
	public StringDBColumnImpl(TypesafeQuery query) {
		super(query);
	}

	public StringDBColumnImpl(Case case_) {
		super(case_);
	}

	protected StringDBColumnImpl(IDBColumn<?> wrap) {
		super(wrap);
	}

	protected StringDBColumnImpl(IDBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C extends IComparableDBColumn<String>> C addFunc(Func func) {
		StringDBColumnImpl wrap = new StringDBColumnImpl(this);
		wrap.add(func);
		return (C)wrap;
	}
	
	protected <T extends Number & Comparable<? super T>> INumberDBColumn<T> addNumberFunc(Func func){
		NumberDBColumnImpl<T> wrap = new NumberDBColumnImpl<T>(this);
		wrap.add(func);
		return wrap;
	}

	@Override
	public IStringDBColumn lower() {
		return addFunc(new LowerFunc());
	}

	@Override
	public IStringDBColumn upper() {
		return addFunc(new UpperFunc());
	}

	@Override
	public IStringDBColumn trim() {
		return addFunc(new TrimFunc());
	}

	@Override
	public IStringDBColumn substring(INumberDBColumn<Integer> from) {
		return addFunc(new SubstringFunc(from));
	}

	@Override
	public IStringDBColumn substring(INumberDBColumn<Integer> from,
			INumberDBColumn<Integer> to) {
		return addFunc(new SubstringFunc(from,to));
	}

	@Override
	public IStringDBColumn substring(int from) {
		return addFunc(new SubstringFunc(from));
	}

	@Override
	public IStringDBColumn substring(int from, int to) {
		return addFunc(new SubstringFunc(from,to));
	}

	@Override
	public INumberDBColumn<Integer> length() {
		NumberDBColumnImpl<Integer> c = new NumberDBColumnImpl<Integer>(addNumberFunc(new LengthFunc()));
		return c;
	}

	@Override
	public IStringDBColumn concat(IStringDBColumn s) {
		return addFunc(new ConcatFunc(s));
	}

	@Override
	public IStringDBColumn concat(String s) {
		return addFunc(new ConcatFunc(s));
	}

	@Override
	public INumberDBColumn<Long> toNumber() {
		return toNumber(Long.class);
	}

	@Override
	public INumberDBColumn<Long> toNumber(String format) {
		return toNumber(Long.class, format);
	}

	@Override
	public <T extends Number & Comparable<? super T>> INumberDBColumn<T> toNumber(
			Class<T> cls) {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(addNumberFunc(new ToNumberFunc()));
		return c;
	}

	@Override
	public <T extends Number & Comparable<? super T>> INumberDBColumn<T> toNumber(
			Class<T> cls, String format) {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(addNumberFunc(new ToNumberFunc(format)));
		return c;
	}

	@Override
	public Exp like(String s) {
		return new LikeExp(this, s);
	}

	@Override
	public Exp like(Param s) {
		return new LikeExp(this, s);
	}

	@Override
	public Exp like(TypesafeQuery query) {
		return new LikeExp(this, query);
	}

	@Override
	public Exp notLike(String s) {
		return new NotLikeExp(this, s);
	}

	@Override
	public Exp notLike(Param s) {
		return new NotLikeExp(this, s);
	}

	@Override
	public Exp notLike(TypesafeQuery query) {
		return new NotLikeExp(this, query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends IDBColumn<String>> V createFromTableAlias(
			String tableAlias) {
		return (V)new StringDBColumnImpl(new DBTableImpl(getTable().getName(), tableAlias), getName());
	}

	@Override
	public IDBColumn<String> as(String otherName) {
		return new StringDBColumnImpl(this, otherName);
	}
}
