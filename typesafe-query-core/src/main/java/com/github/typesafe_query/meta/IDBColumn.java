/**
 * 
 */
package com.github.typesafe_query.meta;


import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.QueryContext;
import com.github.typesafe_query.query.TypesafeQuery;


/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface IDBColumn<T>{
	//--->basic methods
	String getName();
	String getOtherName();
	IDBTable getTable();
	<V extends IDBColumn<T>> V createFromTableAlias(String tableAlias);
	
	String getExpression(QueryContext context,String expression);
	
	//--->expressions
	Exp eq(IDBColumn<T> c);
	Exp eq(T t);
	Exp eq(TypesafeQuery subQuery);
	Exp eq(Param p);
	Exp neq(IDBColumn<T> c);
	Exp neq(T t);
	Exp neq(Param p);
	Exp neq(TypesafeQuery subQuery);

	
	//--->as
	IDBColumn<T> as(String otherName);
}
