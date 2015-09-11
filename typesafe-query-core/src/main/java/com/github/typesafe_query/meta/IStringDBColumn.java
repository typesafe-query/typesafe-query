/**
 * 
 */
package com.github.typesafe_query.meta;

import com.github.typesafe_query.query.Exp;
import com.github.typesafe_query.query.Param;
import com.github.typesafe_query.query.TypesafeQuery;


/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public interface IStringDBColumn extends IComparableDBColumn<String>{
	//--->conversions
	IStringDBColumn lower();
	IStringDBColumn upper();
	IStringDBColumn trim();
	IStringDBColumn substring(INumberDBColumn<Integer> from);
	IStringDBColumn substring(INumberDBColumn<Integer> from,INumberDBColumn<Integer> to);
	IStringDBColumn substring(int from);
	IStringDBColumn substring(int from,int to);
	INumberDBColumn<Integer> length();
	IStringDBColumn concat(IStringDBColumn s);
	IStringDBColumn concat(String s);
	
	INumberDBColumn<Long> toNumber();
	INumberDBColumn<Long> toNumber(String format);
	<T extends Number & Comparable<? super T>> INumberDBColumn<T> toNumber(Class<T> cls);
	<T extends Number & Comparable<? super T>> INumberDBColumn<T> toNumber(Class<T> cls,String format);

	//--->expressions
	Exp like(String s);
	Exp like(Param s);
	Exp like(TypesafeQuery query);
	Exp notLike(String s);
	Exp notLike(Param s);
	Exp notLike(TypesafeQuery query);
	
}
