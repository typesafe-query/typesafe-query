/**
 * 
 */
package com.github.typesafe_query.meta.impl;

import com.github.typesafe_query.meta.IComparableDBColumn;
import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.meta.IDBTable;
import com.github.typesafe_query.query.Case;
import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Func;
import com.github.typesafe_query.query.Order;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.TypesafeQuery;
import com.github.typesafe_query.query.Order.Type;
import com.github.typesafe_query.query.internal.OrderImpl;
import com.github.typesafe_query.query.internal.expression.BetweenExp;
import com.github.typesafe_query.query.internal.expression.GeExp;
import com.github.typesafe_query.query.internal.expression.GtExp;
import com.github.typesafe_query.query.internal.expression.InExp;
import com.github.typesafe_query.query.internal.expression.IsNotNullExp;
import com.github.typesafe_query.query.internal.expression.IsNullExp;
import com.github.typesafe_query.query.internal.expression.LeExp;
import com.github.typesafe_query.query.internal.expression.LtExp;
import com.github.typesafe_query.query.internal.expression.NotBetweenExp;
import com.github.typesafe_query.query.internal.expression.NotInExp;
import com.github.typesafe_query.query.internal.function.CoalesceFunc;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public abstract class ComparableDBColumnImpl<T extends Comparable<? super T>> extends DBColumnImpl<T> implements
		IComparableDBColumn<T> {

	public ComparableDBColumnImpl(IDBTable table, String name) {
		super(table, name);
	}
	
	public ComparableDBColumnImpl(TypesafeQuery query) {
		super(query);
	}

	public ComparableDBColumnImpl(Case case_) {
		super(case_);
	}

	protected ComparableDBColumnImpl(IDBColumn<?> wrap) {
		super(wrap);
	}
	
	protected ComparableDBColumnImpl(IDBColumn<?> wrap, String otherName) {
		super(wrap, otherName);
	}

	protected abstract <C extends IComparableDBColumn<T>> C addFunc(Func func);

	@Override
	public <C extends IComparableDBColumn<T>> C coalesce(C c) {
		return addFunc(new CoalesceFunc(c));
	}

	@Override
	public <C extends IComparableDBColumn<T>> C coalesce(T t) {
		return addFunc(new CoalesceFunc(t));
	}

	@Override
	public Exp gt(IComparableDBColumn<T> c) {
		return new GtExp<T>(this, c);
	}

	@Override
	public Exp gt(T t) {
		return new GtExp<T>(this, t);
	}
	
	@Override
	public Exp gt(Param p) {
		return new GtExp<T>(this, p);
	}

	@Override
	public Exp gt(TypesafeQuery subQuery) {
		return new GtExp<T>(this, subQuery);
	}

	@Override
	public Exp lt(IComparableDBColumn<T> c) {
		return new LtExp<T>(this, c);
	}

	@Override
	public Exp lt(T t) {
		return new LtExp<T>(this, t);
	}
	
	@Override
	public Exp lt(Param p) {
		return new LtExp<T>(this, p);
	}

	@Override
	public Exp lt(TypesafeQuery subQuery) {
		return new LtExp<T>(this, subQuery);
	}

	@Override
	public Exp ge(IComparableDBColumn<T> c) {
		return new GeExp<T>(this, c);
	}

	@Override
	public Exp ge(T t) {
		return new GeExp<T>(this, t);
	}
	
	@Override
	public Exp ge(Param p) {
		return new GeExp<T>(this, p);
	}

	@Override
	public Exp ge(TypesafeQuery subQuery) {
		return new GeExp<T>(this, subQuery);
	}

	@Override
	public Exp le(IComparableDBColumn<T> c) {
		return new LeExp<T>(this, c);
	}

	@Override
	public Exp le(T t) {
		return new LeExp<T>(this, t);
	}

	@Override
	public Exp le(Param p) {
		return new LeExp<T>(this, p);
	}

	@Override
	public Exp le(TypesafeQuery subQuery) {
		return new LeExp<T>(this, subQuery);
	}

	@Override
	public Exp isNull() {
		return new IsNullExp<T>(this);
	}

	@Override
	public Exp isNotNull() {
		return new IsNotNullExp<T>(this);
	}

	@Override
	public Exp between(IComparableDBColumn<T> from, IComparableDBColumn<T> to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp between(IComparableDBColumn<T> from, T to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp between(T from, IComparableDBColumn<T> to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp between(T from, T to) {
		return new BetweenExp<T>(this, from, to);
	}
	
	@Override
	public Exp between(IComparableDBColumn<T> from, Param to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp between(Param from, T to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp between(Param from, IComparableDBColumn<T> to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp between(T from, Param to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp between(Param from, Param to) {
		return new BetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(IComparableDBColumn<T> from, IComparableDBColumn<T> to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(IComparableDBColumn<T> from, T to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(T from, IComparableDBColumn<T> to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(T from, T to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(IComparableDBColumn<T> from, Param to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(Param from, T to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(Param from, IComparableDBColumn<T> to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(T from, Param to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@Override
	public Exp notBetween(Param from, Param to) {
		return new NotBetweenExp<T>(this, from, to);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Exp in(T... ts) {
		return new InExp<T>(this,ts);
	}

	@Override
	public Exp in(TypesafeQuery query) {
		return new InExp<T>(this, query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Exp notIn(T... ts) {
		return new NotInExp<T>(this, ts);
	}

	@Override
	public Exp notIn(TypesafeQuery query) {
		return new NotInExp<T>(this, query);
	}

	@Override
	public Order asc() {
		return new OrderImpl(this, Type.ASC);
	}

	@Override
	public Order desc() {
		return new OrderImpl(this, Type.DESC);
	}
}
