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
public interface StringDBColumn extends ComparableDBColumn<String>{
	//--->conversions
	StringDBColumn lower();
	StringDBColumn upper();
	StringDBColumn trim();
	StringDBColumn rtrim();
	StringDBColumn ltrim();
	StringDBColumn substring(NumberDBColumn<Integer> from);
	StringDBColumn substring(NumberDBColumn<Integer> from,NumberDBColumn<Integer> to);
	StringDBColumn substring(int from);
	StringDBColumn substring(int from,int to);
	NumberDBColumn<Integer> length();
	StringDBColumn concat(StringDBColumn s);
	StringDBColumn concat(String s);
	
	NumberDBColumn<Long> toNumber();
	NumberDBColumn<Long> toNumber(String format);
	<T extends Number & Comparable<? super T>> NumberDBColumn<T> toNumber(Class<T> cls);
	<T extends Number & Comparable<? super T>> NumberDBColumn<T> toNumber(Class<T> cls,String format);

	//--->expressions
	Exp like(String s);
	Exp like(Param s);
	Exp like(TypesafeQuery query);
	Exp notLike(String s);
	Exp notLike(Param s);
	Exp notLike(TypesafeQuery query);
	
}
