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
public interface IComparableDBColumn<T extends Comparable<? super T>> extends IDBColumn<T>{
	//--->conversions
	<C extends IComparableDBColumn<T>> C coalesce(C c);
	<C extends IComparableDBColumn<T>> C coalesce(T t);
	
	//--->expressions
	Exp gt(IComparableDBColumn<T> c);
	Exp gt(T t);
	Exp gt(Param p);
	Exp gt(TypesafeQuery subQuery);
	Exp lt(IComparableDBColumn<T> c);
	Exp lt(T t);
	Exp lt(Param p);
	Exp lt(TypesafeQuery subQuery);
	Exp ge(IComparableDBColumn<T> c);
	Exp ge(T t);
	Exp ge(Param p);
	Exp ge(TypesafeQuery subQuery);
	Exp le(IComparableDBColumn<T> c);
	Exp le(T t);
	Exp le(Param p);
	Exp le(TypesafeQuery subQuery);
	Exp isNull();
	Exp isNotNull();
	Exp between(IComparableDBColumn<T> from,IComparableDBColumn<T> to);
	Exp between(IComparableDBColumn<T> from,T to);
	Exp between(T from,IComparableDBColumn<T> to);
	Exp between(T from,T to);
	Exp between(IComparableDBColumn<T> from,Param to);
	Exp between(Param from,T to);
	Exp between(Param from,IComparableDBColumn<T> to);
	Exp between(T from,Param to);
	Exp between(Param from,Param to);
	Exp notBetween(IComparableDBColumn<T> from,IComparableDBColumn<T> to);
	Exp notBetween(IComparableDBColumn<T> from,T to);
	Exp notBetween(T from,IComparableDBColumn<T> to);
	Exp notBetween(T from,T to);
	Exp notBetween(IComparableDBColumn<T> from,Param to);
	Exp notBetween(Param from,T to);
	Exp notBetween(Param from,IComparableDBColumn<T> to);
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
}
