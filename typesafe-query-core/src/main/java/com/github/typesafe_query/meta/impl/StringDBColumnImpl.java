/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.ComparableDBColumn;
import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.meta.DBTable;
import com.github.typesafe_query.meta.NumberDBColumn;
import com.github.typesafe_query.meta.StringDBColumn;
import com.github.typesafe_query.meta.VirtualDBTable;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.internal.expression.LikeExp;
import com.github.typesafe_query.query.internal.expression.NotLikeExp;
import com.github.typesafe_query.query.internal.function.AllFunc;
import com.github.typesafe_query.query.internal.function.AnyFunc;
import com.github.typesafe_query.query.internal.function.ConcatFunc;
import com.github.typesafe_query.query.internal.function.LengthFunc;
import com.github.typesafe_query.query.internal.function.LowerFunc;
import com.github.typesafe_query.query.internal.function.LtrimFunc;
import com.github.typesafe_query.query.internal.function.RtrimFunc;
import com.github.typesafe_query.query.internal.function.SomeFunc;
import com.github.typesafe_query.query.internal.function.SubstringFunc;
import com.github.typesafe_query.query.internal.function.ToNumberFunc;
import com.github.typesafe_query.query.internal.function.TrimFunc;
import com.github.typesafe_query.query.internal.function.UpperFunc;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class StringDBColumnImpl extends ComparableDBColumnImpl<String> implements StringDBColumn{

	public StringDBColumnImpl(DBTable table, String name) {
		super(table, name);
	}
	
	public StringDBColumnImpl(TypesafeQuery query) {
		super(query);
	}

	public StringDBColumnImpl(Case case_) {
		super(case_);
	}

	protected StringDBColumnImpl(DBColumn<?> wrap) {
		super(wrap);
	}

	protected StringDBColumnImpl(DBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <C extends ComparableDBColumn<String>> C addFunc(Func func) {
		StringDBColumnImpl wrap = new StringDBColumnImpl(this);
		wrap.add(func);
		return (C)wrap;
	}
	
	protected <T extends Number & Comparable<? super T>> NumberDBColumn<T> addNumberFunc(Func func){
		NumberDBColumnImpl<T> wrap = new NumberDBColumnImpl<T>(this);
		wrap.add(func);
		return wrap;
	}

	@Override
	public StringDBColumn lower() {
		return addFunc(new LowerFunc());
	}

	@Override
	public StringDBColumn upper() {
		return addFunc(new UpperFunc());
	}

	@Override
	public StringDBColumn trim() {
		return addFunc(new TrimFunc());
	}

	@Override
	public StringDBColumn rtrim() {
		return addFunc(new RtrimFunc());
	}

	@Override
	public StringDBColumn ltrim() {
		return addFunc(new LtrimFunc());
	}

	@Override
	public StringDBColumn substring(NumberDBColumn<Integer> from) {
		return addFunc(new SubstringFunc(from));
	}

	@Override
	public StringDBColumn substring(NumberDBColumn<Integer> from,
			NumberDBColumn<Integer> to) {
		return addFunc(new SubstringFunc(from,to));
	}

	@Override
	public StringDBColumn substring(int from) {
		return addFunc(new SubstringFunc(from));
	}

	@Override
	public StringDBColumn substring(int from, int to) {
		return addFunc(new SubstringFunc(from,to));
	}

	@Override
	public NumberDBColumn<Integer> length() {
		NumberDBColumnImpl<Integer> c = new NumberDBColumnImpl<Integer>(addNumberFunc(new LengthFunc()));
		return c;
	}

	@Override
	public StringDBColumn concat(StringDBColumn s) {
		return addFunc(new ConcatFunc(s));
	}

	@Override
	public StringDBColumn concat(String s) {
		return addFunc(new ConcatFunc(s));
	}

	@Override
	public NumberDBColumn<Long> toNumber() {
		return toNumber(Long.class);
	}

	@Override
	public NumberDBColumn<Long> toNumber(String format) {
		return toNumber(Long.class, format);
	}

	@Override
	public <T extends Number & Comparable<? super T>> NumberDBColumn<T> toNumber(
			Class<T> cls) {
		NumberDBColumnImpl<T> c = new NumberDBColumnImpl<T>(addNumberFunc(new ToNumberFunc()));
		return c;
	}

	@Override
	public <T extends Number & Comparable<? super T>> NumberDBColumn<T> toNumber(
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
	public <V extends DBColumn<String>> V createFromTableAlias(
			String tableAlias) {
		return (V)new StringDBColumnImpl(new DBTableImpl(getTable().getSchema(), getTable().getSimpleName(), tableAlias), getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends DBColumn<String>> V toVirtualDBTableColumn(VirtualDBTable with){
		return (V)new StringDBColumnImpl(with, getName());
	}

	@Override
	public DBColumn<String> as(String otherName) {
		return new StringDBColumnImpl(this, otherName);
	}

	@Override
	public StringDBColumn any(){
		return addFunc(new AnyFunc());
	}

	@Override
	public StringDBColumn some(){
		return addFunc(new SomeFunc());
	}

	@Override
	public StringDBColumn all(){
		return addFunc(new AllFunc());
	}
}
