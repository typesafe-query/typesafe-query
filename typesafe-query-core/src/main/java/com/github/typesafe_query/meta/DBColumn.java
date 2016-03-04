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
public interface DBColumn<T>{
	//--->basic methods
	String getName();
	String getOtherName();
	DBTable getTable();
	<V extends DBColumn<T>> V createFromTableAlias(String tableAlias);
	<V extends DBColumn<T>> V toVirtualDBTableColumn(VirtualDBTable with);
	
	String getExpression(QueryContext context,String expression);
	
	//--->expressions
	Exp eq(DBColumn<T> c);
	Exp eq(T t);
	Exp eq(TypesafeQuery subQuery);
	Exp eq(Param p);
	Exp neq(DBColumn<T> c);
	Exp neq(T t);
	Exp neq(Param p);
	Exp neq(TypesafeQuery subQuery);

	
	//--->as
	DBColumn<T> as(String otherName);
}
