/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.meta.DBColumn;
import com.github.typesafe_query.query.Param;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NotBetweenExp<T extends Comparable<? super T>> extends BetweenExp<T> {

	public NotBetweenExp(DBColumn<T> left, DBColumn<T> from, DBColumn<T> to) {
		super(left, from, to);
	}

	public NotBetweenExp(DBColumn<T> left, DBColumn<T> from, T toObject) {
		super(left, from, toObject);
	}

	public NotBetweenExp(DBColumn<T> left, T fromObject, DBColumn<T> to) {
		super(left, fromObject, to);
	}

	public NotBetweenExp(DBColumn<T> left, T fromObject, T toObject) {
		super(left, fromObject, toObject);
	}
	
	public NotBetweenExp(DBColumn<T> left, DBColumn<T> from, Param toParam) {
		super(left, from, toParam);
	}

	public NotBetweenExp(DBColumn<T> left, Param fromParam, DBColumn<T> to) {
		super(left, fromParam, to);
	}

	public NotBetweenExp(DBColumn<T> left, Param fromParam, Param toParam) {
		super(left, fromParam, toParam);
	}

	public NotBetweenExp(DBColumn<T> left, Param fromParam, T toObject) {
		super(left, fromParam, toObject);
	}

	public NotBetweenExp(DBColumn<T> left, T fromObject, Param toParam) {
		super(left, fromObject, toParam);
	}

	protected String getSQL(String l,String f,String t){
		return String.format("%s NOT BETWEEN %s AND %s",l,f,t);
	}
}
