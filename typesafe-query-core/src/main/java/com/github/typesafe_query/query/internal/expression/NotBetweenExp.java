/**
 * 
 */
package com.github.typesafe_query.query.internal.expression;

import com.github.typesafe_query.meta.IDBColumn;
import com.github.typesafe_query.query.Param;

/**
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class NotBetweenExp<T extends Comparable<? super T>> extends BetweenExp<T> {

	/**
	 * @param left
	 * @param from
	 * @param to
	 */
	public NotBetweenExp(IDBColumn<T> left, IDBColumn<T> from, IDBColumn<T> to) {
		super(left, from, to);
	}

	/**
	 * @param left
	 * @param from
	 * @param toObject
	 */
	public NotBetweenExp(IDBColumn<T> left, IDBColumn<T> from, T toObject) {
		super(left, from, toObject);
	}

	/**
	 * @param left
	 * @param fromObject
	 * @param to
	 */
	public NotBetweenExp(IDBColumn<T> left, T fromObject, IDBColumn<T> to) {
		super(left, fromObject, to);
	}

	/**
	 * @param left
	 * @param fromObject
	 * @param toObject
	 */
	public NotBetweenExp(IDBColumn<T> left, T fromObject, T toObject) {
		super(left, fromObject, toObject);
	}
	
	/**
	 * @param left
	 * @param from
	 * @param toParam
	 */
	public NotBetweenExp(IDBColumn<T> left, IDBColumn<T> from, Param toParam) {
		super(left, from, toParam);
	}

	/**
	 * @param left
	 * @param fromParam
	 * @param to
	 */
	public NotBetweenExp(IDBColumn<T> left, Param fromParam, IDBColumn<T> to) {
		super(left, fromParam, to);
	}

	/**
	 * @param left
	 * @param fromParam
	 * @param toParam
	 */
	public NotBetweenExp(IDBColumn<T> left, Param fromParam, Param toParam) {
		super(left, fromParam, toParam);
	}

	/**
	 * @param left
	 * @param fromParam
	 * @param toObject
	 */
	public NotBetweenExp(IDBColumn<T> left, Param fromParam, T toObject) {
		super(left, fromParam, toObject);
	}

	/**
	 * @param left
	 * @param fromObject
	 * @param toParam
	 */
	public NotBetweenExp(IDBColumn<T> left, T fromObject, Param toParam) {
		super(left, fromObject, toParam);
	}

	protected String getPredicate(String l,String f,String t){
		return String.format("%s NOT BETWEEN %s AND %s",l,f,t);
	}
}
