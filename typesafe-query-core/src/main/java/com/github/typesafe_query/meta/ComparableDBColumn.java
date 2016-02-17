/**
 * 
 */
package com.github.typesafe_query.meta;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Order;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.TypesafeQuery;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface ComparableDBColumn<T extends Comparable<? super T>> extends DBColumn<T>{
	//--->conversions
	<C extends ComparableDBColumn<T>> C coalesce(C c);
	<C extends ComparableDBColumn<T>> C coalesce(T t);
	
	//--->expressions
	Exp gt(ComparableDBColumn<T> c);
	Exp gt(T t);
	Exp gt(Param p);
	Exp gt(TypesafeQuery subQuery);
	Exp lt(ComparableDBColumn<T> c);
	Exp lt(T t);
	Exp lt(Param p);
	Exp lt(TypesafeQuery subQuery);
	Exp ge(ComparableDBColumn<T> c);
	Exp ge(T t);
	Exp ge(Param p);
	Exp ge(TypesafeQuery subQuery);
	Exp le(ComparableDBColumn<T> c);
	Exp le(T t);
	Exp le(Param p);
	Exp le(TypesafeQuery subQuery);
	Exp isNull();
	Exp isNotNull();
	Exp between(ComparableDBColumn<T> from,ComparableDBColumn<T> to);
	Exp between(ComparableDBColumn<T> from,T to);
	Exp between(T from,ComparableDBColumn<T> to);
	Exp between(T from,T to);
	Exp between(ComparableDBColumn<T> from,Param to);
	Exp between(Param from,T to);
	Exp between(Param from,ComparableDBColumn<T> to);
	Exp between(T from,Param to);
	Exp between(Param from,Param to);
	Exp notBetween(ComparableDBColumn<T> from,ComparableDBColumn<T> to);
	Exp notBetween(ComparableDBColumn<T> from,T to);
	Exp notBetween(T from,ComparableDBColumn<T> to);
	Exp notBetween(T from,T to);
	Exp notBetween(ComparableDBColumn<T> from,Param to);
	Exp notBetween(Param from,T to);
	Exp notBetween(Param from,ComparableDBColumn<T> to);
	Exp notBetween(T from,Param to);
	Exp notBetween(Param from,Param to);
	@SuppressWarnings("unchecked")
	Exp in(T...ts);
	Exp in(TypesafeQuery query);
	@SuppressWarnings("unchecked")
	Exp notIn(T...ts);
	Exp notIn(TypesafeQuery query);
	
	//---->orders
	Order asc();
	Order desc();
	
	ComparableDBColumn<T> any();
	ComparableDBColumn<T> some();
	ComparableDBColumn<T> all();
}
